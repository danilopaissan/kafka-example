package net.differentplace.java.jugkafka;

import net.differentplace.java.jugkafka.clazz.ItemForSale;
import net.differentplace.java.jugkafka.config.KafkaConstants;
import net.differentplace.java.jugkafka.consumer.GeneralCatalog;
import net.differentplace.java.jugkafka.consumer.LimitedOfferCatalog;
import net.differentplace.java.jugkafka.producer.ProducerCreator;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class KafkaApp {
    public static void main(String[] args) throws InterruptedException {
        KafkaApp app = new KafkaApp();
        app.run(args);
    }

    private void run(String[] args) throws InterruptedException {
        Random rand = new Random();
        KafkaConstants.CLIENT_ID = KafkaConstants.CLIENT_ID + String.format("%04d",rand.nextInt(9999));
        CommandLine line = parseArguments(args);
        if (line.hasOption("consumer")) {
            String consumerType = line.getOptionValue("consumer");
            if(Objects.equals(consumerType,"general")) {
                consumeGeneralCatalog();
            } else {
                if(Objects.equals(consumerType,"limited_offer")) {
                    consumeLimitedOfferCatalog();
                } else {
                    System.err.println("Option unknown");
                    printAppHelp();
                }
            }
        } else {
            if (line.hasOption("producer")) {
                runProducer();
            } else {
                printAppHelp();
            }
        }
    }

    private CommandLine parseArguments(String[] args) {

        Options options = getOptions();
        CommandLine line = null;

        CommandLineParser parser = new DefaultParser();

        try {
            line = parser.parse(options, args);

        } catch (ParseException ex) {

            System.err.println("Failed to parse command line arguments");
            System.err.println(ex.toString());
            printAppHelp();

            System.exit(1);
        }

        return line;
    }

    private Options getOptions() {

        Options options = new Options();

        options.addOption("c", "consumer", true, "run as a consumer [general|limited_offer] ");
        options.addOption("p", "producer", false, "run as a producer");
        return options;
    }

    private void printAppHelp() {

        Options options = getOptions();

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar jug-kafka.jar", options, true);
    }

    private void consumeGeneralCatalog() {
        GeneralCatalog catalog = new GeneralCatalog();
        catalog.run();
    }

    private void consumeLimitedOfferCatalog() {
        LimitedOfferCatalog catalog = new LimitedOfferCatalog();
        catalog.run();
    }


    private void runProducer() throws InterruptedException {
        Producer<String, ItemForSale> producer = ProducerCreator.createProducer();
        for (int index = 0; index < KafkaConstants.MESSAGE_COUNT; index++) {
            Optional<ItemForSale> optionalItem = readForNewItem();
            if(optionalItem.isPresent()) {
                ProducerRecord<String, ItemForSale> record = new ProducerRecord<>(
                        KafkaConstants.TOPIC_NAME,
                        optionalItem.get().getLimitedOffer() ? "LIMITED_OFFER" : "NORMAL",
                        optionalItem.get()
                );
                try {
                    RecordMetadata metadata = producer.send(record).get();
                    System.out.println("Record sent with key " + index + " to partition " + metadata.partition()
                            + " with offset " + metadata.offset());
                } catch (ExecutionException e) {
                    System.out.println("Error in sending record");
                    System.out.println(e);
                } catch (InterruptedException e) {
                    System.out.println("Error in sending record");
                    System.out.println(e);
                }
            }
        }
        producer.close();
    }

    private Optional<ItemForSale> readForNewItem() {
        System.out.println("Insert product name:");
        String name = System.console().readLine();
        System.out.println("Insert product description:");
        String description = System.console().readLine();
        System.out.println("Is a limited offer? [Y/n]");
        String limited = System.console().readLine();
        int offsetValidity = 0;
        boolean limitedBool = Objects.equals(limited.toLowerCase(), "y") ? true : false;
        if (!StringUtils.isEmpty(limited) && (Objects.equals(limited.toLowerCase(), "y") || (Objects.equals(limited.toLowerCase(), "n")))) {
            if (limitedBool) {
                System.out.println("Enter the number of days of the offer:");
                try {
                    offsetValidity = Integer.parseInt(System.console().readLine());
                } catch (NumberFormatException e) {
                    System.err.println("The entered string is not an integer");
                    return Optional.empty();
                }
            }
        } else {
            System.err.println("The entered string is not a valid option!");
            return Optional.empty();
        }
        System.out.println("Insert categories (if more than one separate them with commas):");
        String[] categories = System.console().readLine().split(",");
        System.out.println("The item is discounted? [Y/n]");
        String discounted = System.console().readLine();
        int discount = 0;
        boolean discountedBool = Objects.equals(discounted.toLowerCase(), "y") ? true : false;
        if (!StringUtils.isEmpty(discounted) && (Objects.equals(discounted.toLowerCase(), "y") || (Objects.equals(discounted.toLowerCase(), "n")))) {
            if (discountedBool) {
                System.out.println("Enter the discount percentage:");
                try {
                    discount = Integer.parseInt(System.console().readLine());
                } catch (NumberFormatException e) {
                    System.err.println("The entered string is not an integer");
                    return Optional.empty();
                }
            }
        } else {
            System.err.println("The entered string is not a valid option!");
            return Optional.empty();
        }
        Integer price;
        System.out.println("Enter the price:");
        try {
            price = Integer.parseInt(System.console().readLine());
        } catch (NumberFormatException e) {
            System.err.println("The entered string is not an integer");
            return Optional.empty();
        }
        ItemForSale itemForSale = new ItemForSale(name, Arrays.asList(categories), limitedBool, offsetValidity, discountedBool, discount, description, price);
        return Optional.of(itemForSale);
    }
}
