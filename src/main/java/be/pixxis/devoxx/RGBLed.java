package be.pixxis.devoxx;

/**
 * @author Gert Leenders
 */
public class RGBLed {
    private byte red;
    private byte green;
    private byte blue;

    public RGBLed(final byte red, final byte green, final byte blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public byte getGreen() {
        return green;
    }

    public byte getBlue() {
        return blue;
    }

    public byte getRed() {
        return red;
    }

}
