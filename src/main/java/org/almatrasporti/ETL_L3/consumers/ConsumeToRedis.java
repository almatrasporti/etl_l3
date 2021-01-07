package org.almatrasporti.ETL_L3.consumers;

import org.almatrasporti.ETL_L3.writers.IWriter;
import org.almatrasporti.common.utils.Config;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Logger;

public class ConsumeToRedis {

    private Properties props;
    private IWriter writer;
    private KafkaConsumer<String, String> consumer;
    protected String inputTopic = Config.getInstance().get("input.topic");

    public ConsumeToRedis(IWriter writer) {
        this.writer = writer;

        props = new Properties();
        props.setProperty("bootstrap.servers", Config.getInstance().get("Kafka.servers")  != null ? Config.getInstance().get("Kafka.servers") : "localhost:9092");
        props.setProperty("group.id", "redis_consumer");
        props.setProperty("enable.auto.commit", "true");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(this.inputTopic));
    }

    public void consume() {
        while(true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

            for (ConsumerRecord<String, String> record : records) {
                writer.upsertRecord(record.value());
            }
        }
    }
}
