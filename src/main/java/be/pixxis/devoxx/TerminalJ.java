package be.pixxis.devoxx;

import be.pixxis.devoxx.animation.MainAnimationThread;
import be.pixxis.devoxx.types.NFCAction;
import be.pixxis.lpd8806.LedStrip;

import javax.smartcardio.*;
import java.util.HashMap;

/**
 * @author Gert Leenders
 */
public class TerminalJ implements Runnable {

    private static final HashMap READERS = new HashMap();
    private boolean readyToReadCard = false;

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
    private final MainAnimationThread mainAnimationThread;
    private final Card card;
    private final CardTerminal cardTerminal;

    public TerminalJ(final CardTerminal cardTerminal, final MainAnimationThread mainAnimationThread, final LedStrip ledStrip) throws CardException {
        NFCScanner.log(cardTerminal.toString());

        this.cardTerminal = cardTerminal;
        this.card = cardTerminal.connect("T=0");

        channel = card.getBasicChannel();

        this.mainAnimationThread = mainAnimationThread;
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
                boolean waitForCardPresent = cardTerminal.waitForCardPresent(0);
                handleCard();
                //boolean waitForCardAbsent = cardTerminal.waitForCardAbsent(0);
            } catch (CardException ce) {
                ce.printStackTrace();
            }  catch (InterruptedException ce) {
                ce.printStackTrace();
            }

        }
    }

    private void handleCard() throws InterruptedException {


        //try {
//            card = cardTerminal.connect("*");
//            basicChannel = card.getBasicChannel();
//        } catch (CardException e) {
//            e.printStackTrace();
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException ex) {
//                ex.printStackTrace();
//                //Logger.getLogger(NfcReader.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            readyToReadCard =true;
//            return;
//        }
        ATR atr = card.getATR();
        if (SupportedNFC.isSupportedNFC(atr)) {
            try {
                byte[] uidBytes = readNfcUid(channel, atr);
                if (atr != null){
                final String uid = NFCScanner.hex2String(uidBytes);
                if (uid.equals("")) {
                    throw new CardException("Lees opnieuw");
                }
                NFCScanner.log(SupportedNFC.getCorrespondingNfcType(atr).getName() + " kaart gevonden, uid = " + uid);
                //		connectToServer (uid);
//                Platform.runLater(new Thread() {
//                    @Override
//                    public void run() {
//                        // TODO: hier de echte logica, connectie naar server en uid doorsturen
//                        System.out.println("Do something useful now with " + uid);
//                    }
//                });
                }
            } catch (CardException e) {
                e.printStackTrace();
                //Thread.sleep(2000);
                //readyToReadCard =true;

            }
        readyToReadCard = true;
        } else {
            NFCScanner.log("Geen ondersteunde kaart");
            readyToReadCard =true;
        }
    }

    private byte[] readNfcUid(CardChannel basicChannel, ATR atr) throws CardException {
        SupportedNFC NfcType = SupportedNFC.getCorrespondingNfcType(atr);
        CommandAPDU command = new CommandAPDU(NfcType.getUidAddress());
        ResponseAPDU response = basicChannel.transmit(command);
        return response.getData();
    }


}
