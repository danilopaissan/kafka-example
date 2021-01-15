package net.differentplace.java.jugkafka.config;

public class KafkaConstants {
    public static String  KAFKA_BROKERS              = "localhost:9092,localhost:9094,localhost:9091";
    public static Integer MESSAGE_COUNT              = 1000;
    public static String  CLIENT_ID                  = "client-";
    public static String  TOPIC_NAME                 = "catalog";
    public static String  GROUP_ID_CONFIG            = "consumerGroup1";
    public static Integer MAX_NO_MESSAGE_FOUND_COUNT = 100;
    public static String  OFFSET_RESET               = "earliest";
    public static Integer MAX_POLL_RECORDS           = 1;
}
