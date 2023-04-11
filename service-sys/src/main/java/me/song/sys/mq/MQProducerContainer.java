package me.song.sys.mq;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Songwe
 * @date 2022/5/6 22:16
 */
@Component
public class MQProducerContainer {
    private final Map<String, DefaultMQProducer> normalContainer = new HashMap<>();
    private final Map<String, TransactionMQProducer> transactionalContainer = new HashMap<>();
    private static final String NORMAL_PRODUCER_GROUP_PREFIX = "normal-";
    private static final String TRANSACTIONAL_PRODUCER_GROUP_PREFIX = "transactional-";
    
    @Value("rocket.producer.group")
    private String producerGroup;
    @Value("rocket.producer.namesrv-addr")
    private String namesrvAddr;

    /**
     * 根据 topic 创建消息生产者
     * 普通消息生产者组前缀 normal-
     * @param topic
     * @return
     */
    public DefaultMQProducer getDefaultMQProducer(String topic) {
        DefaultMQProducer producer = normalContainer.get(topic);
        if (producer == null) {
            synchronized (normalContainer) {
                producer = normalContainer.get(topic);
                if (producer == null) {
                    producer = new DefaultMQProducer(NORMAL_PRODUCER_GROUP_PREFIX + topic);
                    producer.setNamesrvAddr(namesrvAddr);
                    try {
                        producer.start();
                    } catch (MQClientException e) {
                        throw new RuntimeException("普通消息生产者启动失败，NameServer地址[{" + namesrvAddr + "}]",  e);
                    }
                    normalContainer.put(topic, producer);
                    return producer;
                }
            }
        }
        return producer;
    }


    /**
     * 根据 topic 创建事务消息生产者
     * 事务消息生产者组名前缀 transactional-
     * @param topic
     * @param listener
     * @return
     */
    public TransactionMQProducer getTransactionMQProducer(String topic, TransactionListener listener) {
        TransactionMQProducer producer = transactionalContainer.get(topic);
        if (producer == null) {
            synchronized (transactionalContainer) {
                producer = transactionalContainer.get(topic);
                if (producer == null) {
                    producer = new TransactionMQProducer(TRANSACTIONAL_PRODUCER_GROUP_PREFIX);
                    producer.setNamesrvAddr(namesrvAddr);
                    producer.setTransactionListener(listener);
                    try {
                        producer.start();
                    } catch (MQClientException e) {
                        throw new RuntimeException("事务消息生产者启动失败，NameServer地址[{" + namesrvAddr + "}]",  e);
                    }
                    transactionalContainer.put(topic, producer);
                    return producer;
                }
            }
        }
        return producer;
    }
    
    
    
    
}
