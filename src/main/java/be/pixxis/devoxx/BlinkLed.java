package be.pixxis.devoxx;


import com.pi4j.io.gpio.GpioPinDigitalOutput;

/**
 * @author Gert Leenders
 */
public class BlinkLed implements Runnable {

    private GpioPinDigitalOutput led;

    public BlinkLed(final GpioPinDigitalOutput led) {
        this.led = led;
    }

    @Override
    public void run() {

        led.pulse(500);
    }
}



