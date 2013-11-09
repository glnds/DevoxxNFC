//package be.pixxis.devoxx.led;
//
///**
// * @author Gert Leenders
// */
//public class ScanAnimationThread {
//    private LedStrip ledStrip;
//
//    public ScanAnimationThread(final int numberOfLeds, final float brightness) {
//        ledStrip = new LedStrip(numberOfLeds, brightness);
//    }
//
//    @Override
//    public void run() {
//        while (true) {
//            try {
//                ledStrip.setLed(1,255,51, 153, 0.6F);
//            } catch (InterruptedException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }
//        }
//    }
//}
