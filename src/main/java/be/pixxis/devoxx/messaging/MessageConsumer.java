package be.pixxis.devoxx.messaging;

import be.pixxis.devoxx.NFCScanner;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;

/**
 * @author Gert Leenders
 */
public class MessageConsumer implements Runnable {

    private QueueingConsumer consumer;

    public MessageConsumer(final Connection connection) {
        try {

            final Channel channel = connection.createChannel();
            channel.queueDeclare(NFCScanner.NFC_PERSISTENT_QUEUE, true, false, false, null);

            consumer = new QueueingConsumer(channel);
            channel.basicConsume(NFCScanner.NFC_PERSISTENT_QUEUE, true, consumer);
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

                String message = new String(delivery.getBody());
                System.out.println(" Received durable message: '" + message + "'");

                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }
}
