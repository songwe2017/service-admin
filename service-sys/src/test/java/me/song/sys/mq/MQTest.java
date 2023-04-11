package me.song.sys.mq;

import me.song.sys.AbstractBaseTest;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragelyByCircle;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.junit.jupiter.api.Test;

/**
 * @author Songwe
 * @since 2023/3/27 15:27
 */
public class MQTest extends AbstractBaseTest {

    @Test
    public void testPushConsumeMq() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("dw_test_consumer_6");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.subscribe("TOPIC_TEST", "*");
        consumer.setAllocateMessageQueueStrategy(new AllocateMessageQueueAveragelyByCircle());
        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            try {
                System.out.printf("%s Receive New Messages: %s", Thread.currentThread().getName(), msgs);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            } catch (Throwable e) {
                e.printStackTrace();
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        });
        consumer.start();

        System.in.read();
    }

    @Test
    public void testSyncMq() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("testProducerGroup");
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.start();
        System.out.println(producer.buildMQClientId());

        try {
            Message msg = new Message("TOPIC_TEST", "hello rocketmq".getBytes());
            /**
             * 异步发送的实现原理:
             * 每一个消息发送者实例（DefaultMQProducer）内部会创建一个异步消息发送线程池，默认线程数量为 CPU 核数，线程池内部持有一个有界队列，并且会控制异步调用的最大并发度，
             * 其可以通过参数 clientAsyncSemaphoreValue 来配置。
             * 客户端使线程池将消息发送到服务端，服务端处理完成后，返回结构并根据是否发生异常调用 SendCallback 调回函数
             */
            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println(sendResult);
                }

                @Override
                public void onException(Throwable e) {
                    e.printStackTrace();
                }
            });
        }
        catch (Throwable e) {
            throw new RuntimeException("异步消息异常", e);
        }

        //producer.shutdown();
    }

    @Test
    public void testSimpleMq() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("testProducerGroup");
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.start();

        Message message = new Message("TOPIC_TEST", "hello rocketmq".getBytes());
        SendResult result;
        result  = producer.send(message, 15000);
        System.out.println(result);

        message = new Message("TOPIC_TEST", null, "ODS2020072615490001", "{\"id\":1, \"orderNo\":\"ODS2020072615490001\",\"buyerId\":1,\"sellerId\":1  }".getBytes());
        result = producer.send(message);
        System.out.println(result);
        producer.shutdown();
    }




}
