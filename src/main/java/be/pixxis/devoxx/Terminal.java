package be.pixxis.devoxx;

import be.pixxis.devoxx.led.LedStrip;
import be.pixxis.devoxx.led.MainAnimation;
import be.pixxis.devoxx.types.NFCAction;

import javax.smartcardio.*;
import java.util.HashMap;

/**
 * @author Gert Leenders
 */
public final class Terminal implements Runnable {

    private static final HashMap READERS = new HashMap();

    static {
        READERS.put("3030323536319000", NFCAction.VOTE_UP);
        READERS.put("3030303634389000", NFCAction.FAVORITE);
        READERS.put("3030303630309000", NFCAction.VOTE_DOWN);
        // more IDs
        READERS.put("3030303633309000", NFCAction.VOTE_UP);
        READERS.put("3030313433309000", NFCAction.FAVORITE);
        READERS.put("3030303539359000", NFCAction.VOTE_DOWN);
        // nore needed here...
    }

    private final NFCAction action;
    private final CardChannel channel;
    private final LedStrip ledStrip;
    private final MainAnimation mainAnimation;

    public Terminal(final CardTerminal cardTerminal, final MainAnimation mainAnimation, final LedStrip ledStrip) throws CardException {
        NFCScanner.log(cardTerminal.toString());

        final Card card = cardTerminal.connect("*");
        channel = card.getBasicChannel();

        this.mainAnimation = mainAnimation;
        this.ledStrip = ledStrip;

        final int channelNr = channel.getChannelNumber();

        // Configure the readers.
        channel.transmit(new CommandAPDU(new byte[]{
                (byte) 0xff, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x06,  // 122
                (byte) 0xD4, (byte) 0x32,
                (byte) 0x05,
                (byte) 0x00, (byte) 0x00, (byte) 0x50
        }));
        NFCScanner.log("Channel: " + channelNr + " initialized.");

        // SERIAL ID
        ResponseAPDU response = channel.transmit(new CommandAPDU(new byte[]{(byte) 0x80, (byte) 0x14, (byte) 0x00, (byte) 0x00,
                (byte) 0x08})); // Random Nr from ACOS6
        NFCScanner.log("Channel: " + channelNr + ", serial response: " + NFCScanner.hex2String(response.getBytes()));

        // CARD ID
        response = channel.transmit(new CommandAPDU(new byte[]{(byte) 0x80, (byte) 0x14, (byte) 0x04, (byte) 0x00,
                (byte) 0x06})); // Random Nr from ACOS6
        NFCScanner.log("Channel: " + channelNr + ", card response: " + NFCScanner.hex2String(response.getBytes()));

        action = (NFCAction) READERS.get(NFCScanner.hex2String(response.getBytes()));
        if (action == null) {
            NFCScanner.log("Unable to initialize terminal, loolup in reader table failed!");
            System.exit(0);
        } else {
            NFCScanner.log("Terminal for : " + action + " initialized.");
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                ResponseAPDU response;
                response = channel.transmit(new CommandAPDU(new byte[]{
                        (byte) 0xff, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x06,  // 122
                        (byte) 0xD4, (byte) 0x60,
                        (byte) 0x01, (byte) 0x01,
                        (byte) 0x00, (byte) 0x20, (byte) 0x40  // MiFare ,ISO/A, DEP
                }));
                byte[] buffer = response.getBytes();

                if (buffer[2] == (byte) 0x01) {
                    byte[] ID = new byte[4];
                    ID[0] = buffer[10];
                    ID[1] = buffer[11];
                    ID[2] = buffer[12];
                    ID[3] = buffer[13];

                    final String id = NFCScanner.hex2String(ID);
                    NFCScanner.log("Scanned ID: " + id);

                    if (mainAnimation != null) {
                        mainAnimation.suspendUpdates(true);
                    }

                    if (action == NFCAction.VOTE_UP) {
                        ledStrip.animateUpVote();
                    } else if (action == NFCAction.VOTE_DOWN) {
                        ledStrip.animateDownVote();
                    } else if (action == NFCAction.FAVORITE) {
                        ledStrip.animateFavorite();
                    }

                    if (mainAnimation != null) {
                        mainAnimation.suspendUpdates(false);
                    }

//                // Non persistent message
//                if (NFCScanner.MESSAGING_ENABLED) {
//                    rabbitChannel.basicPublish("", NFC_QUEUE, MessageProperties.TEXT_PLAIN, id.getBytes());
//                    NFCScanner.log("ID: " + id + " enqueued on NON persistent queue.");
//                }
                } else {
                    Thread.sleep(100);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                System.exit(0);
            } catch (CardException e) {
                e.printStackTrace();
                System.exit(0);
            } catch (Exception e){
                e.printStackTrace();
                System.exit(0);
            }
//        } catch (IOException ioe) {
//            ioe.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
        }
    }
}
