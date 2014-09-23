package be.pixxis.devoxx;

import be.pixxis.devoxx.animation.AnimationThread;

import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import java.util.List;

/**
 * @author Gert Leenders (leenders.gert@gmail.com)
 */
public class NFCListenerThread implements Runnable {

    private final List<Terminal> terminals;
    private final AnimationThread animation;
    private final Thread animationThread;


    private static CommandAPDU commandAPDUMiFare = new CommandAPDU(new byte[]{
            (byte) 0xff, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x06,  // 122
            (byte) 0xD4, (byte) 0x60,
            (byte) 0x01, (byte) 0x01,
            (byte) 0x00, (byte) 0x20  // MiFare ,ISO/A, DEP
    });

    public NFCListenerThread(final List<Terminal> terminals, final AnimationThread animation,
                             Thread animationThread) {

        this.terminals = terminals;
        this.animation = animation;
        this.animationThread = animationThread;
    }

    @Override
    public void run() {

        ResponseAPDU response;

        while (true) {

            for (Terminal terminal : terminals) {

                try {
                    response = terminal.getChannel().transmit(commandAPDUMiFare);

                    byte[] buffer = response.getBytes();

                    if (buffer[2] == (byte) 0x01) {
                        byte[] ID = new byte[4];
                        ID[0] = buffer[10];
                        ID[1] = buffer[11];
                        ID[2] = buffer[12];
                        ID[3] = buffer[13];

                        final String id = NFCScanner.hex2String(ID);
                        NFCScanner.log("Scanned ID: " + id);


                        if (animation != null) {
                            //mainAnimationThread.suspendUpdates(true);
                            animation.setNfcAction(terminal.getNfcAction());
                            NFCScanner.log(terminal.getNfcAction().toString());
                            animationThread.interrupt();
                        }

                        Thread.sleep(200);

                    } else {
                        // No card detected
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    System.exit(0);
                } catch (CardException e) {
                    e.printStackTrace();
                    System.exit(0);
                } catch (Exception ed) {
                    ed.printStackTrace();
                    System.exit(0);
                }
            }
        }
    }
}
