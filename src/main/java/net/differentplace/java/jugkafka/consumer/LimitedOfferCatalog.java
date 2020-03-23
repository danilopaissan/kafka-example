package net.differentplace.java.jugkafka.consumer;

import net.differentplace.java.jugkafka.clazz.ItemForSale;
import net.differentplace.java.jugkafka.config.KafkaConstants;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

public class LimitedOfferCatalog {

    private static final int LIMITED_OFFER_PARTITION = 0;
    private int counter = 1;

    public void run(){
        Consumer<String, ItemForSale> consumer = ConsumerCreator.createConsumer();
        int noMessageFound = 0;
        List<PartitionInfo> PartitionInfo = consumer.partitionsFor(KafkaConstants.TOPIC_NAME);
        Map<Integer,TopicPartition> topicPartitionMap = new HashMap<>();
        for(PartitionInfo partitionInfo : PartitionInfo){
            TopicPartition topicPartition = new TopicPartition(partitionInfo.topic(),partitionInfo.partition());
            topicPartitionMap.put(partitionInfo.partition(),topicPartition);
        }
        consumer.assign(Arrays.asList(topicPartitionMap.get(LIMITED_OFFER_PARTITION)));
        long position = consumer.position(topicPartitionMap.get(LIMITED_OFFER_PARTITION));
        long newOffset = position > 6 ? position - 6 : 0;
        consumer.seek(topicPartitionMap.get(LIMITED_OFFER_PARTITION),newOffset);
        while (true) {
            ConsumerRecords<String, ItemForSale> consumerRecords = consumer.poll(Duration.ofSeconds(1));
            if (consumerRecords.count() == 0) {
                noMessageFound++;
                if (noMessageFound > KafkaConstants.MAX_NO_MESSAGE_FOUND_COUNT) {
                    // If no message found count is reached to threshold exit loop.
                    break;
                } else {
                    noMessageFound = 0;
                    continue;
                }
            }
            //print each record.
            consumerRecords.forEach(this::consume);
            // commits the offset of record to broker.
            consumer.commitAsync();
        }
        consumer.close();
    }

    public void consume(ConsumerRecord<String, ItemForSale> record) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("###### Limited offet N." + counter++ + " ###############################################################################");
        System.out.println("");
        System.out.println("\t New ITem: " + record.value().getName());
        System.out.println("\t Description: " + record.value().getDescription());
        System.out.println("\t Category: ");
        for (String category : record.value().getCategories()) {
            System.out.println("\t\t - " + category.trim());
        }
        System.out.println("\t Price: " + record.value().getPrice());
        if(record.value().getDiscounted())
            System.out.println("\t Discount: " + record.value().getDiscount());
        if(record.value().getLimitedOffer())
            System.out.println("\t Limited offer until: " + sdf.format(record.value().getValidUntil()));
        System.out.println("");
        System.out.println("#######################################################################################################");
    }
}
