package com.hao.spider.queue;

import com.hao.spider.config.ConfigLoader;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Arrays;
import java.util.Properties;

/**
 * Created by donghao on 16/7/23.
 */
public abstract class AbstractKafkaQueue<T> implements Queue<T>{


    private Properties producerProp() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", ConfigLoader.getString("bootstrap.servers","127.0.0.1:2181"));
        properties.put("acks",ConfigLoader.getString("acks","all"));
        properties.put("retries",ConfigLoader.getInteger("retries",1));
        properties.put("batch.size",ConfigLoader.getInteger("batch.size",16384));
        properties.put("linger.ms",ConfigLoader.getInteger("linger.ms",1));
        properties.put("buffer.memory",ConfigLoader.getInteger("buffer.memory",33554432));
        properties.putIfAbsent("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        return properties;
    }

    private Properties consumerProp() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", ConfigLoader.getString("bootstrap.servers","127.0.0.1:9092"));
        properties.put("group.id",ConfigLoader.getString("group.id",groupId()));
        properties.put("enable.auto.commit",ConfigLoader.getBoolean("enable.auto.commit",true));
        properties.put("auto.commit.interval.ms",ConfigLoader.getInteger("auto.commit.interval.ms",1000));
        properties.put("session.timeout.ms",ConfigLoader.getInteger("session.timeout.ms",30000));
        properties.putIfAbsent("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        return properties;
    }



    private <A,B> Producer<A,B> getProducer() {
        return new KafkaProducer<>(producerProp());
    }

    private <A,B> Consumer<A,B> getConsumer() {
        return new KafkaConsumer<>(consumerProp());
    }

    @Override
    public void add(T body) {
        Producer<Object, Object> producer = getProducer();
        producer.send(new ProducerRecord<Object, Object>(getTopic(),getPartition(),getKey(),body));
    }



    @Override
    public T push() {
        Consumer<Object, Object> consumer = getConsumer();
        consumer.subscribe(Arrays.asList(getTopic()));
        ConsumerRecords<Object, Object> records = consumer.poll(100);
        records.forEach(this::consume);
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                consumer.close();
            }
        });
        return null;
    }

    protected abstract String getTopic();

    protected abstract String groupId();

    protected abstract String getKey();

    protected abstract void consume(ConsumerRecord<Object, Object> objectObjectConsumerRecord);

    protected abstract Integer getPartition();
}
