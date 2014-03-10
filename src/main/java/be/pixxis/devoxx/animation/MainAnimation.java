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
                animation();
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    private void animation() throws InterruptedException {
        ledStrip.fill(0, 255, 0, 1, 4, 0.15F);
        ledStrip.fill(255, 255, 0, 5, 8, 0.15F);
        ledStrip.fill(255, 0, 0, 9, 12, 0.15F);
        ledStrip.update();
        Thread.sleep(100);

        //Green
        ledStrip.setLed(4, 0, 255, 0, 0.3F);
        //Red
        ledStrip.setLed(9, 255, 0, 0, 0.3F);
        ledStrip.update();
        Thread.sleep(100);

        //Green
        ledStrip.setLed(3, 0, 255, 0, 0.3F);
        //Red
        ledStrip.setLed(10, 255, 0, 0, 0.3F);
        ledStrip.update();
        Thread.sleep(100);

        //Green
        ledStrip.setLed(2, 0, 255, 0, 0.3F);
        //Red
        ledStrip.setLed(11, 255, 0, 0, 0.3F);
        ledStrip.update();
        Thread.sleep(100);

        //Green
        ledStrip.setLed(1, 0, 255, 0, 0.3F);
        //Yellow
        ledStrip.fill(255, 255, 0, 5, 8, 0.3F);
        //Red
        ledStrip.setLed(12, 255, 0, 0, 0.3F);
        ledStrip.update();
        Thread.sleep(100);

        for (int i = 0; i < 40; i += 4) {
            float j = (float) i / 100;
            //Green
            ledStrip.setLed(1, 0, 255, 0, 0.3F + j);
            //Yellow
            ledStrip.fill(255, 255, 0, 6, 7, 0.3F + j);
            //Red
            ledStrip.setLed(12, 255, 0, 0, 0.3F + j);
            ledStrip.update();
            Thread.sleep(100);
        }

        for (int i = 0; i < 40; i += 4) {
            float j = (float) i / 100;
            //Green
            ledStrip.setLed(1, 0, 255, 0, 0.6F - j);
            //Yellow
            ledStrip.fill(255, 255, 0, 6, 7, 0.6F - j);
            //Red
            ledStrip.setLed(12, 255, 0, 0, 0.6F - j);
            ledStrip.update();
            Thread.sleep(100);
        }
    }
}