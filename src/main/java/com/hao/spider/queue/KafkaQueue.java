package com.hao.spider.queue;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * @author dongh38@ziroom
 * @Date 16/10/15
 * @Time 下午4:11
 */
public class KafkaQueue<T> extends AbstractKafkaQueue<T>{




    @Override
    protected String getTopic() {
        return null;
    }

    @Override
    protected String groupId() {
        return null;
    }

    @Override
    protected String getKey() {
        return null;
    }

    @Override
    protected void consume(ConsumerRecord<Object, Object> objectObjectConsumerRecord) {

    }

    @Override
    protected Integer getPartition() {
        return null;
    }
}
