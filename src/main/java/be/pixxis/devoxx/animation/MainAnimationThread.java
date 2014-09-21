package be.pixxis.devoxx.animation;

import be.pixxis.devoxx.types.NFCAction;
import be.pixxis.lpd8806.LedStrip;

/**
 * @author Gert Leenders (leenders.gert@gmail.com)
 */
public final class MainAnimationThread implements Runnable {

    private LedStrip ledStrip;
    private NFCAction nfcAction;

    public MainAnimationThread(final LedStrip ledStrip) {
        this.ledStrip = ledStrip;
        this.nfcAction = null;
        init();
    }

    private void init() {
        ledStrip.fill(0, 255, 0, 1, 4, 0.2F);
        ledStrip.fill(255, 255, 0, 5, 8, 0.2F);
        ledStrip.fill(255, 0, 0, 9, 12, 0.2F);
        ledStrip.update();
    }

    public NFCAction getNfcAction() {
        return nfcAction;
    }

    public void setNfcAction(NFCAction nfcAction) {
        this.nfcAction = nfcAction;
    }

    @Override
    public void run() {
        while (true) {
            try {

                mainAnimation();

            } catch (InterruptedException e) {

                try {
                    if (nfcAction != null) {
                        if (nfcAction == NFCAction.VOTE_UP) {
                            upVoteAnimation();
                        } else if (nfcAction == NFCAction.VOTE_DOWN) {
                            downVoteAnimation();
                        } else if (nfcAction == NFCAction.FAVORITE) {
                            favoriteAnimation();
                        }
                        init();
                        nfcAction = null;
                    }

                } catch (InterruptedException ie) {
                    ie.printStackTrace(); // Should not happen
                }
            }
        }
    }


    private void mainAnimation() throws InterruptedException {

        for (int i = 0; i < 80; i += 1) {
            float j = (float) i / 200;
            //Green
            ledStrip.fill(0, 255, 0, 1, 2, 0.2F + j);
            //Yellow
            ledStrip.fill(255, 255, 0, 6, 7, 0.2F + j);
            //Red
            ledStrip.fill(255, 0, 0, 11, 12, 0.2F + j);
            ledStrip.update();
            Thread.sleep(10);
        }

        for (int i = 0; i < 80; i += 1) {
            float j = (float) i / 200;
            //Green
            ledStrip.fill(0, 255, 0, 1, 2, 0.6F - j);
            //Yellow
            ledStrip.fill(255, 255, 0, 6, 7, 0.6F - j);
            //Red
            ledStrip.fill(255, 0, 0, 11, 12, 0.6F - j);
            ledStrip.update();
            Thread.sleep(10);
        }
    }

    /**
     * Led strip animation for up vote
     *
     * @throws InterruptedException
     */
    private void upVoteAnimation() throws InterruptedException {
        ledStrip.allOff();
        ledStrip.setLed(1, 0, 0, 255, 0.6F);
        ledStrip.setLed(2, 127, 0, 255, 0.6F);
        ledStrip.setLed(3, 255, 0, 255, 0.6F);
        ledStrip.setLed(4, 255, 0, 127, 0.6F);
        ledStrip.update();
        Thread.sleep(200);
    }

    /**
     * Led strip animation for down vote
     *
     * @throws InterruptedException
     */
    private void downVoteAnimation() throws InterruptedException {
        ledStrip.allOff();
        ledStrip.setLed(12, 0, 0, 255, 0.6F);
        ledStrip.setLed(11, 127, 0, 255, 0.6F);
        ledStrip.setLed(10, 255, 0, 255, 0.6F);
        ledStrip.setLed(9, 255, 0, 127, 0.6F);
        ledStrip.update();
        Thread.sleep(200);
    }

    /**
     * Led strip animation for favoriting
     *
     * @throws InterruptedException
     */
    private void favoriteAnimation() throws InterruptedException {
        ledStrip.allOff();
        ledStrip.setLed(5, 0, 0, 255, 0.5F);
        ledStrip.setLed(8, 0, 0, 255, 0.5F);
        ledStrip.setLed(6, 127, 0, 255, 0.5F);
        ledStrip.setLed(7, 127, 0, 255, 0.5F);
        ledStrip.update();
        Thread.sleep(200);
    }
}