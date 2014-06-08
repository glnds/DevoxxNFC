package be.pixxis.devoxx.animation;

import be.pixxis.lpd8806.LedStrip;

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
                ledStrip.fill(0, 255, 0, 1, 4, 0.2F);
                ledStrip.fill(255, 255, 0, 5, 8, 0.2F);
                ledStrip.fill(255, 0, 0, 9, 12, 0.2F);
                ledStrip.update();
                animation();
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    private void animation() throws InterruptedException {


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
}