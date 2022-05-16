package academy.kafka.utils;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksIterator;


import academy.kafka.config.AppConfig;
import academy.kafka.serializers.JsonDeserializer;
import academy.kafka.serializers.JsonSerializer;

public class KafkaUtils {

    static int counter = 0;

    public static <T> int produceEntities(String topic, Class<?> clazz, Function<String, T> fromJson, int seconds) {
              
        counter = 0;
        System.out.println(topic);
        Properties props = new Properties();
        props.put("JsonClass", clazz);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,  new JsonSerializer<T>().getClass());
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfig.BootstrapServers);
              
        Producer<String, T> producer = new KafkaProducer<>(props);
              
        RocksDB db = RocksDbUtils.openDatabase(topic);
        RocksIterator it = db.newIterator();
        

        it.seekToFirst();
              
        while (it.isValid()) {
            if (seconds > 0)
                try {
                    java.util.concurrent.TimeUnit.SECONDS.sleep(seconds);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            String key = new String(it.key());
            String value = new String(it.value());
              
            T entity = fromJson.apply(value);
            producer.send(new ProducerRecord<String, T>(topic, key, entity), new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception e) {
                    if (e != null) {
                        System.out.println("Writing to Kafka failed with an exception {}" + e.getMessage());
                        throw new RuntimeException(e);
                    } else
                        counter++;
                }
            });
            it.next();
        }
        producer.close();
        it.close();
        db.close();
        return counter;
    }

    public static <T> void consumeEntities(String topic, Class<?> clazz, String grpName) {
        Properties props = new Properties();
        props.put("JsonClass", clazz);

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, new StringDeserializer());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,  new JsonDeserializer<T>());
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfig.BootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, grpName);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        Consumer<String, T> consumer = new KafkaConsumer<String, T>(props);

        try {
            consumer.subscribe(Collections.singletonList(topic));
            while (true) {
                final ConsumerRecords<String, T> consumerRecords = consumer.poll(Duration.ofMillis(100));

                consumerRecords.forEach(record -> {
                    System.out.printf("%s:(key=%s, value=%s, partition=%s, %s)\n",topic, record.key(),
                            record.value(), record.partition(), record.offset());
                });
                consumer.commitAsync();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("consumer stopped");
        consumer.close();
    }
    //

    public static ObjectMapper getJacksonMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    public static Date convertLocalDateToDate(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate convertDateToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static long localDateToLong(LocalDate date) {
        return date.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public static long localDateToLong(LocalDateTime dateTime) {
        return dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    public static String formatTimestamp(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return formatter.format(instant.atOffset(ZoneOffset.UTC));
    }

    public static boolean createTopic(String topicName, int numPartitions, int replicationFactor) {
        if (topicExists(topicName)) {
            System.out.printf("topic %s exists\n", topicName);
            return true;
        }
        int nrOfBrokers = nrOfKafkaBrokers();
        if (numPartitions > nrOfBrokers || replicationFactor > nrOfBrokers) {
            return false;
        }

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfig.BootstrapServers);
        final AdminClient adminClient = KafkaAdminClient.create(props);
        final NewTopic newTopic = new NewTopic(topicName, numPartitions, (short) replicationFactor);
        final CreateTopicsResult createTopicsResult = adminClient.createTopics(Collections.singleton(newTopic));
        // Since the call is Async, Lets wait for it to complete.
        try {
            createTopicsResult.values().get(topicName).get();

        } catch (InterruptedException | ExecutionException up) {
            up.printStackTrace();
            adminClient.close();
            return false;
        }
        System.out.printf("topic %s created\n", topicName);

        adminClient.close();
        return true;
    }

    public static boolean deleteTopic(String topicName) {
        if (!topicExists(topicName)) {
            System.out.printf("topic %s does not exists\n", topicName);
            return true;
        }

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfig.BootstrapServers);
        final AdminClient adminClient = KafkaAdminClient.create(props);
        final DeleteTopicsResult deleteTopicsResult = adminClient.deleteTopics(Collections.singleton(topicName));
        // Since the call is Async, Lets wait for it to complete.
        try {
            deleteTopicsResult.values().get(topicName).get();

        } catch (InterruptedException | ExecutionException up) {
            up.printStackTrace();
            adminClient.close();
            return false;
        }
        System.out.printf("topic %s deleted\n", topicName);

        adminClient.close();
        return true;
    }

    static public Integer nrOfKafkaBrokers() {
        Properties props = new Properties();
        Integer result = null;
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfig.BootstrapServers);
        final AdminClient adminClient = KafkaAdminClient.create(props);
        try {
            result = adminClient.describeCluster().nodes().get().stream().map(Node::id).collect(Collectors.toList())
                    .size();

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        adminClient.close();
        return result;
    }

    /**
     * Get all actual topics residing in Apache Kafka
     * 
     * @return A list of topics
     */
    static public TreeSet<String> getTopicNames() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfig.BootstrapServers);
        final AdminClient adminClient = KafkaAdminClient.create(props);
        ListTopicsResult result = adminClient.listTopics();
        Set<String> topics = null;
        try {
            topics = result.names().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        TreeSet<String> ts = new TreeSet<String>(topics);
        adminClient.close();
        return ts;
    }

    /**
     * @return a boolean that indicates if the topic exists in Apache Kafka or not.
     */
    static public boolean topicExists(String topicName) {
        Collection<String> lt = getTopicNames();
        for (String t : lt) {
            if (t.equals(topicName)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String args[]) {
        System.out.println(nrOfKafkaBrokers());
    }

}
