package net.differentplace.java.jugkafka.consumer;


import net.differentplace.java.jugkafka.clazz.ItemForSale;
import net.differentplace.java.jugkafka.config.KafkaConstants;
import net.differentplace.java.jugkafka.deserializer.KafkaJsonDeserializer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

public class ConsumerCreator {
    public static Consumer<String, ItemForSale> createConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.KAFKA_BROKERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConstants.GROUP_ID_CONFIG);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, KafkaConstants.MAX_POLL_RECORDS);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, KafkaConstants.OFFSET_RESET); //What to do when there is no initial offset in Kafka or if the current offset does not exist any more on the server
        Consumer<String, ItemForSale> consumer = new KafkaConsumer<>(props,
                new StringDeserializer(),
                new KafkaJsonDeserializer<ItemForSale>(ItemForSale.class));
        //consumer.subscribe(Collections.singletonList(KafkaConstants.TOPIC_NAME));
        return consumer;
    }
}
