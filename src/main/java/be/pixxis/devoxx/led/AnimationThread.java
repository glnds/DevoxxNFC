package be.pixxis.devoxx.led;

/**
 * @author Gert Leenders
 */
public final class AnimationThread implements Runnable {

    private LedStrip ledStrip;

    public AnimationThread(final int numberOfLeds, final float brightness) {
        ledStrip = new LedStrip(numberOfLeds, brightness);
    }

    @Override
    public void run() {
        while (true) {
            try {
                ledStrip.animation();
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
}
