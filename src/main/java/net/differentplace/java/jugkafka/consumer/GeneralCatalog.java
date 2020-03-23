package net.differentplace.java.jugkafka.consumer;

import net.differentplace.java.jugkafka.clazz.ItemForSale;
import net.differentplace.java.jugkafka.config.KafkaConstants;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Collections;
import java.util.Random;

public class GeneralCatalog {

    public void run(){
        String randSuffix = new Random().nextInt(999) + "";
        KafkaConstants.GROUP_ID_CONFIG = "GeneralCatalogGroup-" + randSuffix;
        Consumer<String, ItemForSale> consumer = ConsumerCreator.createConsumer();
        consumer.subscribe(Collections.singletonList(KafkaConstants.TOPIC_NAME));
        int noMessageFound = 0;
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
        System.out.println("#####################################################################################");
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
        System.out.println("#####################################################################################");
    }
}
