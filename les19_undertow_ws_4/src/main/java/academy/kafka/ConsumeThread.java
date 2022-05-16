package academy.kafka;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import academy.kafka.config.AppConfig;
import academy.kafka.entities.Entity;
import academy.kafka.serializers.JsonDeserializer;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;

public class ConsumeThread extends Thread {
    final Class<?> clazz;
    final List<WebSocketChannel> channels;
    final  String topic;
      
    public ConsumeThread(List<WebSocketChannel> channels, String topic, Class<?> clazz) {
        this.channels=channels;
        this.topic=topic;
        this.clazz = clazz;
    }

    static Random rn = new Random();// helper, remove in production

    public void run() {
       
        Properties props = new Properties();
        props.put(JsonDeserializer.JSON_CLASS, clazz);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, new StringDeserializer());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, new JsonDeserializer<Entity>());

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfig.BootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "undertow_3" + rn.nextInt(10000));
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        Consumer<String, Entity> consumer = new KafkaConsumer<String, Entity>(props);
        try {
            consumer.subscribe(Collections.singletonList(topic));
            while (true) {
                final ConsumerRecords<String, Entity> consumerRecords = consumer.poll(Duration.ofMillis(100));
                consumerRecords.forEach(record -> {
                    String key = record.key();
                    Entity entity = record.value();
                    ObjectNode objectNode = Entity.JACKSON_MAPPER.createObjectNode();
                    objectNode.put("key", key);
                    String json = entity.toJson();

                    objectNode.put("value", json);
                    channels.forEach((ch) -> {
                        if (WebsocketService04.getChatboxTag(ch).equals(topic)) {
                            WebSockets.sendText(objectNode.toString(), ch, null);
                        }
                    });

                });
                consumer.commitAsync();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("consumer stopped");
        consumer.close();
    }
}