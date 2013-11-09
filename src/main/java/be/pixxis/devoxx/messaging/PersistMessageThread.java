package be.pixxis.devoxx.messaging;


import be.pixxis.devoxx.NFCScanner;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;

/**
 * @author Gert Leenders
 */
public class PersistMessageThread implements Runnable {

    private QueueingConsumer consumer;
    private Channel channel;

    public PersistMessageThread(final Connection connection) {
        try {

            this.channel = connection.createChannel();
            channel.queueDeclare(NFCScanner.NFC_QUEUE, false, false, false, null);

            consumer = new QueueingConsumer(channel);
            channel.basicConsume(NFCScanner.NFC_QUEUE, true, consumer);

            // Declare Durable queue
            channel.queueDeclare(NFCScanner.NFC_PERSISTENT_QUEUE, true, false, false, null);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                QueueingConsumer.Delivery delivery = consumer.nextDelivery();

                // Blink blue led.
                //(new Thread(new BlinkLed(ledDequeue))).start();

                String message = new String(delivery.getBody());
                System.out.println(" Received NON persistent message: '" + message + "'");

                // Make message durable.
                channel.basicPublish("", NFCScanner.NFC_PERSISTENT_QUEUE, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
                System.out.println("ID: " + message + " enqueued on durable queue.");

                 Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(0);
            } catch (IOException ioe) {
                ioe.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
}

