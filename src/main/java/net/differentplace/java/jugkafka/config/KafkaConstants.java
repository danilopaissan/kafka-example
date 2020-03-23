package net.differentplace.java.jugkafka.config;

public class KafkaConstants {
    public static String  KAFKA_BROKERS              = "kafkajug:9092,kafkajug:9094,kafkajug:9093";
    public static Integer MESSAGE_COUNT              = 1000;
    public static String  CLIENT_ID                  = "client-";
    public static String  TOPIC_NAME                 = "catalog";
    public static String  GROUP_ID_CONFIG            = "consumerGroup1";
    public static Integer MAX_NO_MESSAGE_FOUND_COUNT = 100;
    public static String  OFFSET_RESET               = "earliest";
    public static Integer MAX_POLL_RECORDS           = 1;
}
