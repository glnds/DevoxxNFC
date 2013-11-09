package be.pixxis.devoxx.led;

/**
 * @author Gert Leenders
 */
public final class RGBLed {
    private static final int GAMMA_LENGTH = 256;
    private static final byte[] GAMMA = new byte[GAMMA_LENGTH];
    private byte red;
    private byte green;
    private byte blue;

    static {
        for (int i = 0; i < GAMMA_LENGTH; i++) {
            int j = (int) (Math.pow(((float) i) / 255.0, 2.5) * 127.0 + 0.5);
            GAMMA[i] = (byte) (0x80 | j);
        }
    }

    public RGBLed() {
    }

    public void set(final int red, final int green, final int blue, final float brightness) {
        this.red = GAMMA[(int) (red * brightness)];
        this.green = GAMMA[(int) (green * brightness)];
        this.blue = GAMMA[(int) (blue * brightness)];
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
