package be.pixxis.devoxx.animation;

import be.pixxis.devoxx.types.NFCAction;
import be.pixxis.lpd8806.LedStrip;

/**
 * @author Gert Leenders (leenders.gert@gmail.com)
 */
public final class AnimationThread implements Runnable {

    private LedStrip ledStrip;
    private NFCAction nfcAction;

    public AnimationThread(final LedStrip ledStrip) {
        this.ledStrip = ledStrip;
        this.nfcAction = null;
        init();
    }

    /**
     * Initialize the led strip
     */
    private void init() {
        ledStrip.fill(0, 255, 0, 1, 4, 0.13F);
        ledStrip.fill(255, 255, 0, 5, 8, 0.13F);
        ledStrip.fill(255, 0, 0, 9, 12, 0.13F);
        ledStrip.update();
    }

    public void setNfcAction(NFCAction nfcAction) {
        this.nfcAction = nfcAction;
    }

    @Override
    public void run() {
        while (true) {
            try {

                heartbeat();

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


    private void heartbeat() throws InterruptedException {

        ledStrip.fill(0, 255, 0, 1, 4, 0.13F);
        ledStrip.fill(255, 255, 0, 5, 8, 0.13F);
        ledStrip.fill(255, 0, 0, 9, 12, 0.13F);
        ledStrip.update();
        Thread.sleep(1200);

        // First beat
        for (int i = 0; i <= 12; i += 1) {
            float j = (float) i / 100;
            //Green
            ledStrip.fill(0, 255, 0, 1, 4, 0.13F + j);
            //Yellow
            ledStrip.fill(255, 255, 0, 5, 8, 0.13F + j);
            //Red
            ledStrip.fill(255, 0, 0, 9, 12, 0.13F + j);
            ledStrip.update();
            Thread.sleep(5);
        }

        for (int i = 0; i <= 25; i += 1) {
            float j = (float) i / 100;
            //Green
            ledStrip.fill(0, 255, 0, 1, 4, 0.25F - j);
            //Yellow
            ledStrip.fill(255, 255, 0, 5, 8, 0.25F - j);
            //Red
            ledStrip.fill(255, 0, 0, 9, 12, 0.25F - j);
            ledStrip.update();
            Thread.sleep(4);
        }

        // Second beat
        for (int i = 0; i <= 33; i += 1) {
            float j = (float) i / 100;
            //Green
            ledStrip.fill(0, 255, 0, 1, 4, 0.0F + j);
            //Yellow
            ledStrip.fill(255, 255, 0, 5, 8, 0.0F + j);
            //Red
            ledStrip.fill(255, 0, 0, 9, 12, 0.0F + j);
            ledStrip.update();
            Thread.sleep(4);
        }

        for (int i = 0; i <= 20; i += 1) {
            float j = (float) i / 100;
            //Green
            ledStrip.fill(0, 255, 0, 1, 4, 0.33F - j);
            //Yellow
            ledStrip.fill(255, 255, 0, 5, 8, 0.33F - j);
            //Red
            ledStrip.fill(255, 0, 0, 9, 12, 0.33F - j);
            ledStrip.update();
            Thread.sleep(5);
        }
    }

    /**
     * Led strip animation for up vote
     *
     * @throws InterruptedException
     */
    private void upVoteAnimation() throws InterruptedException {

        for (int i = 0; i < 80; i += 5) {
            float j = (float) i / 200;
            // Green
            ledStrip.fill(0, 255, 0, 1, 12, 0.2F + j);
            ledStrip.update();
            Thread.sleep(10);
        }

        for (int i = 0; i < 80; i += 5) {
            float j = (float) i / 200;
            // Green
            ledStrip.fill(0, 255, 0, 1, 12, 0.6F - j);
            ledStrip.update();
            Thread.sleep(10);
        }
    }

    /**
     * Led strip animation for down vote
     *
     * @throws InterruptedException
     */
    private void downVoteAnimation() throws InterruptedException {

        for (int i = 0; i < 80; i += 5) {
            float j = (float) i / 200;
            // Red
            ledStrip.fill(255, 0, 0, 1, 12, 0.2F + j);
            ledStrip.update();
            Thread.sleep(10);
        }

        for (int i = 0; i < 80; i += 5) {
            float j = (float) i / 200;
            // Red
            ledStrip.fill(255, 0, 0, 1, 12, 0.6F - j);
            ledStrip.update();
            Thread.sleep(10);
        }
    }

    /**
     * Led strip animation for favoriting
     *
     * @throws InterruptedException
     */
    private void favoriteAnimation() throws InterruptedException {

        for (int i = 0; i < 80; i += 5) {
            float j = (float) i / 200;
            // Yellow
            ledStrip.fill(255, 255, 0, 1, 12, 0.2F + j);
            ledStrip.update();
            Thread.sleep(10);
        }

        for (int i = 0; i < 80; i += 5) {
            float j = (float) i / 200;
            // Yellow
            ledStrip.fill(255, 255, 0, 1, 12, 0.6F - j);
            ledStrip.update();
            Thread.sleep(10);
        }
    }
}