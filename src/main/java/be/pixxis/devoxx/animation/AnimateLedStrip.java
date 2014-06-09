package be.pixxis.devoxx.animation;

import be.pixxis.lpd8806.LedStrip;

/**
 * Created with IntelliJ IDEA.
 * User: glnd
 * Date: 08/03/14
 * Time: 10:29
 * To change this template use File | Settings | File Templates.
 */
public class AnimateLedStrip {

    private LedStrip ledStrip;

    public AnimateLedStrip(final LedStrip ledStrip) {
        this.ledStrip = ledStrip;
    }

    /**
     * Led strip animation for up vote
     *
     * @throws InterruptedException
     */
    public void animateUpVote() throws InterruptedException {
        ledStrip.allOff();
        ledStrip.setLed(1, 0, 0, 255, 0.6F);
        ledStrip.setLed(2, 127, 0, 255, 0.6F);
        ledStrip.setLed(3, 255, 0, 255, 0.6F);
        ledStrip.setLed(4, 255, 0, 127, 0.6F);
        ledStrip.update();
    }

    /**
     * Led strip animation for down vote
     *
     * @throws InterruptedException
     */
    public void animateDownVote() throws InterruptedException {
        ledStrip.allOff();
        ledStrip.setLed(12, 0, 0, 255, 0.6F);
        ledStrip.setLed(11, 127, 0, 255, 0.6F);
        ledStrip.setLed(10, 255, 0, 255, 0.6F);
        ledStrip.setLed(9, 255, 0, 127, 0.6F);
        ledStrip.update();
        Thread.sleep(350);
    }

    /**
     * Led strip animation for favoriting
     *
     * @throws InterruptedException
     */
    public void animateFavorite() throws InterruptedException {
        ledStrip.allOff();
        ledStrip.setLed(5, 0, 0, 255, 0.5F);
        ledStrip.setLed(8, 0, 0, 255, 0.5F);
        ledStrip.setLed(6, 127, 0, 255, 0.5F);
        ledStrip.setLed(7, 127, 0, 255, 0.5F);
        ledStrip.update();
        Thread.sleep(350);
    }
}
