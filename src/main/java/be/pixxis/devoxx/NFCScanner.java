package be.pixxis.devoxx;

import be.pixxis.devoxx.led.LedStrip;
import be.pixxis.devoxx.led.MainAnimation;
import be.pixxis.devoxx.messaging.MessageConsumer;
import be.pixxis.devoxx.messaging.PersistMessageThread;
import be.pixxis.devoxx.types.Platform;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Spi;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.commons.cli.*;

import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals;
import javax.smartcardio.TerminalFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * @author Gert Leenders (leenders.gert@gmail.com)
 */
public class NFCScanner {

    public static boolean DEBUG_MODE = false;
    public static boolean MESSAGING_ENABLED = false;
    public static String NFC_PERSISTENT_QUEUE;
    public static String NFC_QUEUE;
    private static int ROOM_NUMBER;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

        final CommandLineParser parser = new GnuParser();

        final Options options = new Options();

        // Add room option
        options.addOption(OptionBuilder.withLongOpt("room")
                .hasArgs()
                .withArgName("number")
                .withDescription("use number to specify the room")
                .isRequired()
                .create("r"));

        // Add room option
        options.addOption(OptionBuilder.withLongOpt("platform")
                .hasArgs()
                .withArgName("name")
                .withDescription("specify then platform")
                .create("p"));


        //TODO add server ip

        // Add debug option
        options.addOption("d", "debug", false, "enter debug mode");

        // Add messaging option
        options.addOption("m", "messaging", false, "enable messaging");

        Platform platform = Platform.RASPBERRY_PI;

        try {
            // parse the command line arguments
            CommandLine line = parser.parse(options, args);

            // Get the value for option r (room)
            ROOM_NUMBER = Integer.valueOf(line.getOptionValue("r"));

            // Get the value for option p (platform)
            final String platformOption = line.getOptionValue("p");

            if (platformOption != null) {
                if (platformOption.equalsIgnoreCase("ubuntu")) {
                    platform = Platform.UBUNTU;
                }
            }

            if (line.hasOption("d")) {
                DEBUG_MODE = true;
                log("Debug Mode ENABLED.");
            }

            if (line.hasOption("m")) {
                MESSAGING_ENABLED = true;
                log("Messaging ENABLED.");
            }

        } catch (UnrecognizedOptionException uoe) {
            System.out.println(uoe.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("DevoxxNFC", options);
            System.exit(0);
        } catch (MissingOptionException moe) {
            System.out.println(moe.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("DevoxxNFC", options);
            System.exit(0);
        } catch (MissingArgumentException mae) {
            System.out.println(mae.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("DevoxxNFC", options);
            System.exit(0);
        } catch (ParseException exp) {
            exp.printStackTrace();
            System.exit(0);
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid number for option:r");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("DevoxxNFC", options);
            System.exit(0);
        }

        NFC_PERSISTENT_QUEUE = "nfc_scans_pers_room_" + ROOM_NUMBER;
        NFC_QUEUE = "nfc_scans_room_" + ROOM_NUMBER;

        // Initialize NFC library.
        try {
            initializePcsclite(platform);
        } catch (FileNotFoundException e) {
            log(e.getMessage());
            System.exit(0);
        }

        //TODO only enable leds in debug mode.
        //TODO Clean up code
        //TODO add logger
        final GpioController gpio = GpioFactory.getInstance();
        final GpioPinDigitalOutput led = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00);
        final GpioPinDigitalOutput ledEnQueue = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);
        final GpioPinDigitalOutput ledDequeue = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06);


        // Initialize Rabbit MQ connection
        final ConnectionFactory rabbitConnectionFactory = new ConnectionFactory();
        rabbitConnectionFactory.setHost("localhost");
        final Connection rabbitConnection;
        Channel rabbitChannel = null;


        // setup SPI for communication with the led strip.
        int fd = Spi.wiringPiSPISetup(0, 10000000);
        if (fd <= -1) {
            log("SPI initialization FAILED.");
            return;
        }
        log("SPI initialization SUCCEEDED.");

        // Test proper working of ledstrip
        final LedStrip ledStrip = new LedStrip(12, 0.5F);
        try {
            ledStrip.testStrip();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        // Start led animation
        MainAnimation mainAnimation = null;
        Thread mainAnimationThread = null;
        if (!DEBUG_MODE) {
            mainAnimation = new MainAnimation(12, 0.5F);
            mainAnimationThread = new Thread(mainAnimation);
            mainAnimationThread.setPriority(Thread.MIN_PRIORITY);
            mainAnimationThread.start();
        }

        try {
            if (MESSAGING_ENABLED) {
                rabbitConnection = rabbitConnectionFactory.newConnection();
                rabbitChannel = rabbitConnection.createChannel();
                // Non persistent queue
                rabbitChannel.queueDeclare(NFC_QUEUE, false, false, false, null);

                // Start a thread to move message from a non persistent queue to a durable que.
                (new Thread(new PersistMessageThread(rabbitConnection, ledDequeue))).start();


                // Start a thread to consume the durable messages.
                (new Thread(new MessageConsumer(rabbitConnection, ledDequeue))).start();
            }

            // Initialize the NFC terminals.
            TerminalFactory terminalFactory = TerminalFactory.getDefault();
            log("Terminal factory = " + terminalFactory);

            final CardTerminals terminals = terminalFactory.terminals();
            log(terminals.toString());

            if (terminals != null) {

                try {
                    log(terminals.list().size() + " terminal(s) found...");
                } catch (CardException e) {
                    log("No terminals found! Exit!");
                    System.exit(0);
                }

                // Open a channel for every terminal.
                for (CardTerminal cardTerminal : terminals.list()) {
                    log(cardTerminal.toString());

                    final Terminal terminal = new Terminal(cardTerminal, mainAnimation, ledStrip);
                    final Thread terminalThread = new Thread(terminal);
                    terminalThread.setPriority(Thread.MAX_PRIORITY);
                    terminalThread.start();
                }

            } else {
                log("No terminals found! Exit!");
            }

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (CardException e) {
            e.printStackTrace();
        }
    }

    private static void initializePcsclite(final Platform platform) throws FileNotFoundException {
        final String pcscliteFileName = System.mapLibraryName("pcsclite") + ".1";

        final File pcscliteFile;
        if (platform == Platform.UBUNTU) {
            pcscliteFile = new File("/lib/x86_64-linux-gnu", pcscliteFileName);
        } else {
            pcscliteFile = new File("/usr/lib/arm-linux-gnueabihf", pcscliteFileName);
        }

        if (pcscliteFile.exists()) {
            System.setProperty("sun.security.smartcardio.library", pcscliteFile.getAbsolutePath());
            log("Pcsclite library found and initialized.");
        } else {
            throw new FileNotFoundException("Pcsclite library not found! Exit!");
        }
    }

    public static void log(final String string) {
        System.out.println(">> " + string);
    }

        public static String hex2String(byte[] b) {
        String result = "";
        for (byte by : b) {
            result += String.format("%02X", by);
        }
        return result;
    }
}
