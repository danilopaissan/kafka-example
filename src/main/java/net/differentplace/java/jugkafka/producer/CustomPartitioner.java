package net.differentplace.java.jugkafka.producer;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.InvalidRecordException;
import org.apache.kafka.common.PartitionInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomPartitioner implements Partitioner {
    private String[] productTypes = null;
    private Map<String, Integer> productTypesMap = null;


    @Override
    public void configure(Map<String, ?> configs) {
        productTypes =configs.get("partitions").toString().split(",");
        productTypesMap = new HashMap<>(productTypes.length);
        int idx = 0;
        for (String site : productTypes) {
            productTypesMap.put(site,idx);
            idx++;
        }
    }
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {

        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        int numPartitions = partitions.size();

        if(numPartitions < productTypes.length){
            int idx = 0;
            for (String site : productTypes) {
                if(idx >= numPartitions)
                    idx = 0;
                productTypesMap.put(site,idx);
                idx++;
            }
        }

        if ((keyBytes == null) || (!(key instanceof String)))
            throw new InvalidRecordException("All messages must have a key");

        int partition = productTypesMap.containsKey(key) ? productTypesMap.get(key) : 0;
        return partition;
    }
    @Override
    public void close() {
    }
}
