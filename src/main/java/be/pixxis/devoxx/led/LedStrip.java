package be.pixxis.devoxx.led;

import com.pi4j.wiringpi.Spi;

/**
 * @author Gert Leenders
 */
public final class LedStrip {

    private int numberOfLeds;
    private RGBLed[] ledBuffer;
    private float brightness;
    private boolean suspendUpdates = false;

    public LedStrip(final int numberOfLeds, final float brightness) throws IllegalArgumentException {
        if (brightness < 0 || brightness > 1.0) {
            throw new IllegalArgumentException("Brightness must be between 0.0 and 1.0");
        }
        this.numberOfLeds = numberOfLeds;
        this.ledBuffer = new RGBLed[numberOfLeds];
        for (int i = 0; i < numberOfLeds; i++) {
            ledBuffer[i] = new RGBLed();
        }

        this.brightness = brightness;
    }

    public void setSuspendUpdates(boolean suspendUpdates) {
        this.suspendUpdates = suspendUpdates;
    }

    public void allOff() {
        fill(0, 0, 0);
        update();
    }

    public void fill(final int red, final int green, final int blue) {
        fill(red, green, blue, 1, numberOfLeds);
    }

    public void fill(final int red, final int green, final int blue, final float brightness) {
        fill(red, green, blue, 1, numberOfLeds, brightness);
    }

    public void fill(final int red, final int green, final int blue, final int start, final int end) throws IllegalArgumentException {
        fill(red, green, blue, start, end, brightness);
    }

    public void fill(final int red, final int green, final int blue, final int start, final int end,
                     final float brightness) throws IllegalArgumentException {

        if (red < 0 || green < 0 || blue < 0 || red > 255 || green > 255 || blue > 255) {
            throw new IllegalArgumentException("Red, green and blue values must be between 0 and 255.");
        }

        if (start < 1 || end > (numberOfLeds + 1)) {
            throw new IllegalArgumentException("Led start must be greater then 0, end must be smaller then " + (numberOfLeds + 1) + ".");
        }

        if (end < start) {
            throw new IllegalArgumentException("End must be greater then or equal as start.");
        }

        for (int i = start; i <= end; i++) {
            setLed(i, red, green, blue, brightness);
        }
    }

    public void setLed(final int number, final int red, final int green, final int blue) {
        setLed(number, red, green, blue, brightness);
    }

    public void setLedOff(final int number) {
        setLed(number, 0, 0, 0, 0);
    }

    public void setLed(final int number, final int red, final int green, final int blue, final float brightness) {
        if (number < 1 || number > numberOfLeds) {
            throw new IllegalArgumentException("led number must be greater then 0 and smaller then " + (numberOfLeds + 1) + ".");
        }

        ledBuffer[number - 1].set(red, green, blue, brightness);
    }

    public void update() {
        if (suspendUpdates) {
            return;
        }
        final byte packet[] = new byte[numberOfLeds * 3];

        for (int i = 0; i < numberOfLeds; i++) {
            packet[i * 3] = ledBuffer[i].getGreen();
            packet[(i * 3) + 1] = ledBuffer[i].getRed();
            packet[(i * 3) + 2] = ledBuffer[i].getBlue();
        }

        // Update the strand
        Spi.wiringPiSPIDataRW(0, packet, this.numberOfLeds * 3);

        byte endPacket[] = {(byte) 0x00};

        // Flush the update
        Spi.wiringPiSPIDataRW(0, endPacket, 1);
    }

    public void testStrip() throws InterruptedException {
        allOff();

        fill(0, 255, 0);
        update();

        Thread.sleep(2000);

        fill(0, 0, 255);
        update();

        Thread.sleep(2000);

        fill(255, 0, 0);
        update();

        Thread.sleep(2000);

        allOff();
    }

    public void animation() throws InterruptedException {
        fill(0, 255, 0, 1, 4, 0.15F);
        fill(255, 255, 0, 5, 8, 0.15F);
        fill(255, 0, 0, 9, 12, 0.15F);
        update();
        Thread.sleep(100);

        //Green
        setLed(4, 0, 255, 0, 0.3F);
        //Red
        setLed(9, 255, 0, 0, 0.3F);
        update();
        Thread.sleep(100);

        //Green
        setLed(3, 0, 255, 0, 0.3F);
        //Red
        setLed(10, 255, 0, 0, 0.3F);
        update();
        Thread.sleep(100);

        //Green
        setLed(2, 0, 255, 0, 0.3F);
        //Red
        setLed(11, 255, 0, 0, 0.3F);
        update();
        Thread.sleep(100);

        //Green
        setLed(1, 0, 255, 0, 0.3F);
        //Yellow
        fill(255, 255, 0, 5, 8, 0.3F);
        //Red
        setLed(12, 255, 0, 0, 0.3F);
        update();
        Thread.sleep(100);

        for (int i = 0; i < 40; i += 4) {
            float j = (float) i / 100;
            //Green
            setLed(1, 0, 255, 0, 0.3F + j);
            //Yellow
            fill(255, 255, 0, 6, 7, 0.3F + j);
            //Red
            setLed(12, 255, 0, 0, 0.3F + j);
            update();
            Thread.sleep(100);
        }

        for (int i = 0; i < 40; i += 4) {
            float j = (float) i / 100;
            //Green
            setLed(1, 0, 255, 0, 0.6F - j);
            //Yellow
            fill(255, 255, 0, 6, 7, 0.6F - j);
            //Red
            setLed(12, 255, 0, 0, 0.6F - j);
            update();
            Thread.sleep(100);
        }
    }

    public void animateUpVote() throws InterruptedException {
        allOff();
        setLed(1, 0, 0, 255, 0.6F);
        setLed(2, 127, 0, 255, 0.6F);
        setLed(3, 255, 0, 255, 0.6F);
        setLed(4, 255,0, 127, 0.6F);
        update();
        Thread.sleep(350);
    }

    public void animateDownVote() throws InterruptedException {
        allOff();
        setLed(12, 0, 0, 255, 0.6F);
        setLed(11, 127, 0, 255, 0.6F);
        setLed(10, 255, 0, 255, 0.6F);
        setLed(9, 255,0, 127, 0.6F);
        update();
        Thread.sleep(350);
    }

    public void animateFavorite() throws InterruptedException {
        allOff();
        setLed(5, 0, 0, 255, 0.5F);
        setLed(8, 0, 0, 255, 0.5F);
        setLed(6, 127, 0, 255, 0.5F);
        setLed(7, 127, 0, 255, 0.5F);
        update();
        Thread.sleep(350);
    }
}
