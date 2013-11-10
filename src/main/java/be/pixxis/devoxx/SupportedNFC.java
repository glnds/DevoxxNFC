package be.pixxis.devoxx;

/**
 * @author Gert Leenders
 */

import javax.smartcardio.ATR;


@SuppressWarnings("restriction")

public enum SupportedNFC {

    MIFARE_DESFIRE {
        String getATR() {
            return hex2String(new byte[]{0x3b, (byte) 0x81, (byte) 0x80,
                    0x01, (byte) 0x80, (byte) 0x80});
        }

        String getName() {
            return "Mifare Desfire Ev_1";
        }

        byte[] getUidAddress() {
            byte[] bc = new byte[5];
            bc[0] = (byte) 0xff;
            bc[1] = (byte) 0xca;
            bc[2] = 0;
            bc[3] = 0;
            bc[4] = 0;
            return bc;
        }
    },
    MIFARE_STANDARD_4K {
        String getATR() {
            return hex2String(new byte[]{0x3b, (byte) 0x8f, (byte) 0x80,
                    0x01, (byte) 0x80, 0x4f, 0x0c, (byte) 0xa0, 0x00, 0x00,
                    0x03, 0x06, 0x03, 0x00, 0x02, 0x00, 0x00, 0x00, 0x00, 0x69});
        }

        String getName() {
            return "Mifare Standard 4K";
        }

        byte[] getUidAddress() {
            byte[] bc = new byte[5];
            bc[0] = (byte) 0xff;
            bc[1] = (byte) 0xca;
            bc[2] = 0;
            bc[3] = 0;
            bc[4] = 0;
            return bc;
        }

    },
    MIFARE_ULTRALIGHT {
        String getATR() {
            return hex2String(new byte[]{0x3b, (byte) 0x8f, (byte) 0x80,
                    0x01, (byte) 0x80, 0x4f, 0x0c, (byte) 0xa0, 0x00, 0x00,
                    0x03, 0x06, 0x03, 0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x68});
        }

        String getName() {
            return "Mifare UltraLight";
        }

        byte[] getUidAddress() {
            byte[] bc = new byte[5];
            bc[0] = (byte) 0xff;
            bc[1] = (byte) 0xca;
            bc[2] = 0;
            bc[3] = 0;
            bc[4] = 0;
            return bc;
        }

    };

    private static String hex2String(byte[] b) {
        String result = "";
        for (byte by : b) {
            result += String.format("%02X", by);
        }
        return result;
    }

    /**
     * Checks whether the given ATR-code represents one of the supported
     * NFC-card types.
     *
     * @param atr
     * @return
     */
    public static boolean isSupportedNFC(ATR atr) {
        Boolean res = false;
        String atrString = hex2String(atr.getBytes());
        //       System.out.println("ATR code = "+atrString);
        for (SupportedNFC nfc : SupportedNFC.values()) {
            //         System.out.println("compare with "+nfc.getATR());
            if (atrString.equals(nfc.getATR())) {
                res = true;
            }
        }
        return res;
    }

    /**
     * Returns the NFC-type matching the given ATR-code. Works if and only if
     * the given ATR-code actually matches one of the supported NFC-types.
     *
     * @param atr
     * @return
     */
    public static SupportedNFC getCorrespondingNfcType(ATR atr) {
        SupportedNFC res = null;
        String atrString = hex2String(atr.getBytes());
        for (SupportedNFC nfc : SupportedNFC.values()) {
            if (atrString.equals(nfc.getATR())) {
                res = nfc;
            }
        }
        return res;

    }

    /**
     * @return The ATR pattern for the given kind of NFC-card.
     */
    abstract String getATR();

    /**
     * @return A String containing the name of this kind of NFC-card
     */
    abstract String getName();

    /**
     * @return A byte array containing the address of the UID on this kind of
     *         NFC-card.
     */
    abstract byte[] getUidAddress();
}