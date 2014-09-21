package be.pixxis.devoxx;

import be.pixxis.devoxx.animation.AnimateLedStrip;
import be.pixxis.devoxx.animation.MainAnimationThread;
import be.pixxis.lpd8806.LedStrip;

import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import java.util.List;

/**
 * @author Gert Leenders (leenders.gert@gmail.com)
 */
public class NFCListenerThread implements Runnable {

    private List<Terminal> terminals;
    private final MainAnimationThread mainAnimationThread;
    private AnimateLedStrip animateLedStrip;
    private Thread mainAnimationThread2;


    private static CommandAPDU commandAPDUMiFare = new CommandAPDU(new byte[]{
            (byte) 0xff, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x06,  // 122
            (byte) 0xD4, (byte) 0x60,
            (byte) 0x01, (byte) 0x01,
            (byte) 0x00, (byte) 0x20  // MiFare ,ISO/A, DEP
    });

    public NFCListenerThread(final List<Terminal> terminals, final MainAnimationThread mainAnimationThread,
                             final LedStrip ledStrip, Thread mainAnimationThread2) {

        this.terminals = terminals;
        this.mainAnimationThread = mainAnimationThread;
        this.animateLedStrip = new AnimateLedStrip(ledStrip);
        this.mainAnimationThread2 = mainAnimationThread2;
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
//
//                        mainAnimationThread.setNfcAction(NFCAction.VOTE_UP);
//                        mainAnimationThread.interrupt();

                        if (mainAnimationThread2 != null) {
                            //mainAnimationThread.suspendUpdates(true);
                            mainAnimationThread.setNfcAction(terminal.getNfcAction());
                            mainAnimationThread2.interrupt();
                        }


//                // Non persistent message
//                if (NFCScanner.MESSAGING_ENABLED) {
//                    rabbitChannel.basicPublish("", NFC_QUEUE, MessageProperties.TEXT_PLAIN, id.getBytes());
//                    NFCScanner.log("ID: " + id + " enqueued on NON persistent queue.");
//                }

                        //channel.close();
                        //card.disconnect(true);
                        //must be longer then action animation
                        Thread.sleep(250);

                    } else {
                        //Thread.sleep(20);
                        NFCScanner.log("No scan");
                    }


                    // }

                } catch (InterruptedException e) {
//                    try {
//                        if (card != null) {
//                            card.disconnect(true);
//                        }
//                    } catch (CardException ce) {
//                        ce.printStackTrace();
//                    }
//                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                    System.exit(0);
                } catch (CardException e) {

//                    try {
//                        if (card != null) {
//                            card.disconnect(true);
////                        }
//                    } catch (CardException ce) {
//                        ce.printStackTrace();
//                    }
                    e.printStackTrace();
                    System.exit(0);
                } catch (Exception ed) {
//                    try {
//                        if (card != null) {
//                            card.disconnect(true);
//                        }
//                    } catch (CardException ce) {
//                        ce.printStackTrace();
//                    }
                    ed.printStackTrace();
                    System.exit(0);
                }
//        } catch (IOException ioe) {
//            ioe.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//        }
            }

        }
    }
}
