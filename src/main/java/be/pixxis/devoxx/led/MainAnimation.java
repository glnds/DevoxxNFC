package be.pixxis.devoxx.led;

/**
 * @author Gert Leenders
 */
public final class MainAnimation implements Runnable {

    private LedStrip ledStrip;

    public MainAnimation(final int numberOfLeds, final float brightness) {
        ledStrip = new LedStrip(numberOfLeds, brightness);
    }

    public void suspendUpdates(final boolean suspend) {
        ledStrip.setSuspendUpdates(suspend);
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
