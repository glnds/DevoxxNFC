package be.pixxis.devoxx;

import be.pixxis.devoxx.types.NFCAction;

import javax.smartcardio.*;
import java.util.HashMap;

/**
 * @author Gert Leenders (leenders.gert@gmail.com)
 */
public final class Terminal {

    private static final HashMap READERS = new HashMap();

    static {
        READERS.put("3030323536319000", NFCAction.VOTE_UP);
        READERS.put("3030303634389000", NFCAction.FAVORITE);
        READERS.put("3030303630309000", NFCAction.VOTE_DOWN);
        // more IDs
        READERS.put("3030303633309000", NFCAction.VOTE_UP);
        READERS.put("3030313433309000", NFCAction.FAVORITE);
        READERS.put("3030303539359000", NFCAction.VOTE_DOWN);

        READERS.put("3030303736399000", NFCAction.VOTE_UP);
        READERS.put("3030303630379000", NFCAction.FAVORITE);
        READERS.put("3030303631379000", NFCAction.VOTE_DOWN);

        READERS.put("3030303630349000", NFCAction.VOTE_UP);
        READERS.put("3030313535399000", NFCAction.FAVORITE);
        READERS.put("3030313534349000", NFCAction.VOTE_DOWN);

        READERS.put("3030303634379000", NFCAction.VOTE_UP);
        READERS.put("3030323535339000", NFCAction.FAVORITE);
        READERS.put("3030323533369000", NFCAction.VOTE_DOWN);

        READERS.put("3030323534389000", NFCAction.VOTE_UP);
        READERS.put("3030313135339000", NFCAction.FAVORITE);
        READERS.put("3030303632379000", NFCAction.VOTE_DOWN);

        READERS.put("3030313338359000", NFCAction.VOTE_UP);
        READERS.put("3030303632309000", NFCAction.FAVORITE);
        READERS.put("3030303634369000", NFCAction.VOTE_DOWN);

        READERS.put("3030303634319000", NFCAction.VOTE_UP);
        READERS.put("3030303635309000", NFCAction.FAVORITE);
        READERS.put("3030303632339000", NFCAction.VOTE_DOWN);

        READERS.put("3030303736319000", NFCAction.VOTE_UP);
        READERS.put("3030303633339000", NFCAction.FAVORITE);
        READERS.put("3030303630369000", NFCAction.VOTE_DOWN);
    }

    private final NFCAction nfcAction;
    private final CardTerminal cardTerminal;
    private final CardChannel channel;

    public Terminal(final CardTerminal terminal) throws CardException {

        this.cardTerminal = terminal;

        final Card card = terminal.connect("T=0");
         channel = card.getBasicChannel();

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

        nfcAction = (NFCAction) READERS.get(NFCScanner.hex2String(response.getBytes()));
        if (nfcAction == null) {
            NFCScanner.log("Unable to initialize terminal, lookup in reader table failed!");
            System.exit(0);
        } else {
            NFCScanner.log("Terminal for : " + nfcAction + " initialized.");
        }

       //card.disconnect(true);
    }

    public NFCAction getNfcAction() {
        return nfcAction;
    }

    public CardChannel getChannel() {
        return channel;
    }
}
