package be.pixxis.devoxx;

import com.pi4j.wiringpi.Spi;

/**
 * @author Gert Leenders
 */
public class LedStrand {

    private final static byte[] GAMMA = new byte[256];
    private int numberOfLeds;
    private RGBLed[] strand;
    private float brightness;

    static {
        for (int i = 0; i < 256; i++) {
            int j = (int) (Math.pow(((float) i) / 255.0, 2.5) * 127.0 + 0.5);
            GAMMA[i] = (byte)( 0x80 | j);
        }
    }


    public LedStrand(final int numberOfLeds, final float brightness) throws IllegalArgumentException {
        if (brightness < 0 || brightness > 1.0) {
            throw new IllegalArgumentException("Brightness must be between 0.0 and 1.0");
        }
        this.numberOfLeds = numberOfLeds;
        this.strand = new RGBLed[numberOfLeds];
        this.brightness = brightness;
    }

    public static void main(String[] args) {
        // setup SPI for communication
        int fd = Spi.wiringPiSPISetup(0, 10000000);

        if (fd <= -1) {
            System.out.println(" ==>> SPI SETUP FAILED");
            return;
        }

        LedStrand s = new LedStrand(12, 0.3F);

        while (true) {

            try {

                s.fill(0, 255, 0);
                s.update();

                Thread.sleep(1000);

                s.fill(0, 0, 255);
                s.update();

                Thread.sleep(1000);

                s.fill(255, 0, 0);
                s.update();

                Thread.sleep(1000);


                s.fill(0, 0, 0);
                s.update();

                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    public void fill(final int red, final int green, final int blue) {
        fill(red, green, blue, 0, 0);
    }

    public void fill(final int red, final int green, final int blue, final int start, final int end) throws IllegalArgumentException {
        int endLed = end;

        if (red < 0 || green < 0 || blue < 0 || red > 255 || green > 255 || blue > 255) {
            throw new IllegalArgumentException("Red, green and blue values must be between 0 and 255.");
        }

        if (start < 0 || end < 0) {
            throw new IllegalArgumentException("Start and end values must be equal or greater then zero.");
        }

        if (endLed == 0) {
            endLed = this.numberOfLeds;
        }

        for (int i = start; i < endLed; i++) {
            strand[i] = new RGBLed(GAMMA[(int) (red * this.brightness)], GAMMA[(int) (green * this.brightness)],
                    GAMMA[(int) (blue * this.brightness)]);
        }

    }

    public void update() {
        final byte packet[] = new byte[this.numberOfLeds * 3];

        for (int i = 0; i < this.numberOfLeds; i++) {
            packet[i * 3] = strand[i].getGreen();
            packet[(i * 3) + 1] = strand[i].getRed();
            packet[(i * 3) + 2] = strand[i].getBlue();
        }

        // Update the strand
        Spi.wiringPiSPIDataRW(0, packet, this.numberOfLeds * 3);

        byte endPacket[] = {(byte) 0x00};

        // Flush the update
        Spi.wiringPiSPIDataRW(0, endPacket, 1);

    }
}
