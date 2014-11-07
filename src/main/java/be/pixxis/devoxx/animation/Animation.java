package be.pixxis.devoxx.animation;

import be.pixxis.devoxx.types.NFCAction;
import be.pixxis.lpd8806.LedStrip;

/**
 * @author Gert Leenders (leenders.gert@gmail.com)
 */
public class Animation {
    private LedStrip ledStrip;

    public Animation(final LedStrip ledStrip) {
        this.ledStrip = ledStrip;
        idle();
    }

    /**
     * Animation when idle.
     */
    public void idle() {
        ledStrip.allOff();
        //Green Led
        ledStrip.setLed(3, 0, 255, 0, 0.6F);
        //Yellow Led
        ledStrip.setLed(6, 255, 255, 0, 0.6F);
        //Red Led
        ledStrip.setLed(11, 255, 0, 0, 0.6F);
        ledStrip.update();
    }


    public void scanAnimation(final NFCAction nfcAction) {
        if (nfcAction != null) {
            try {
                if (nfcAction == NFCAction.VOTE_UP) {
                    upVoteAnimation();
                } else if (nfcAction == NFCAction.VOTE_DOWN) {
                    downVoteAnimation();
                } else if (nfcAction == NFCAction.FAVORITE) {
                    favoriteAnimation();
                }
            } catch (InterruptedException ie) {
                ie.printStackTrace(); // Should not happen
            }
            idle();
        }
    }

    /**
     * Led strip animation for up vote
     *
     * @throws InterruptedException
     */
    private void upVoteAnimation() throws InterruptedException {

        ledStrip.allOff();

        for (int i = 0; i < 80; i += 5) {
            float j = (float) i / 200;
            // Green
            ledStrip.fill(0, 255, 0, 1, 4, 0.2F + j);
            ledStrip.update();
            Thread.sleep(13);
        }

        for (int i = 0; i < 80; i += 5) {
            float j = (float) i / 200;
            // Green
            ledStrip.fill(0, 255, 0, 1, 4, 0.6F - j);
            ledStrip.update();
            Thread.sleep(13);
        }
    }

    /**
     * Led strip animation for down vote
     *
     * @throws InterruptedException
     */
    private void downVoteAnimation() throws InterruptedException {

        ledStrip.allOff();

        for (int i = 0; i < 80; i += 5) {
            float j = (float) i / 200;
            // Red
            ledStrip.fill(255, 0, 0, 9, 12, 0.2F + j);
            ledStrip.update();
            Thread.sleep(13);
        }

        for (int i = 0; i < 80; i += 5) {
            float j = (float) i / 200;
            // Red
            ledStrip.fill(255, 0, 0, 9, 12, 0.6F - j);
            ledStrip.update();
            Thread.sleep(13);
        }
    }

    /**
     * Led strip animation for favoriting
     *
     * @throws InterruptedException
     */
    private void favoriteAnimation() throws InterruptedException {

        ledStrip.allOff();

        for (int i = 0; i < 80; i += 5) {
            float j = (float) i / 200;
            // Yellow
            ledStrip.fill(255, 255, 0, 5, 8, 0.2F + j);
            ledStrip.update();
            Thread.sleep(13);
        }

        for (int i = 0; i < 80; i += 5) {
            float j = (float) i / 200;
            // Yellow
            ledStrip.fill(255, 255, 0, 5, 8, 0.6F - j);
            ledStrip.update();
            Thread.sleep(13);
        }
    }
}
