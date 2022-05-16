package academy.kafka;

import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

import academy.kafka.config.AppConfig;
import academy.kafka.entities.Day;
import academy.kafka.entities.Registration;
import academy.kafka.serdes.AppSerdes;

public class PaymentRequest05Extra1 {
  static final String topicName="registration_on_payment_request_date";
        static int rn = ThreadLocalRandom.current().nextInt(1000);

        public static void main(String[] args) {
                academy.kafka.utils.KafkaUtils.deleteTopic(Day.topicName);
                academy.kafka.utils.KafkaUtils.createTopic(Day.topicName, 1, 1);
                Properties props = new Properties();
                props.put(StreamsConfig.APPLICATION_ID_CONFIG, "registration_to_registration_day" + rn);// !!!! Should
                                                                                                        // be NO RANDOM
                props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfig.BootstrapServers);
                props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, AppSerdes.String().getClass());
                props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 0);

                StreamsBuilder builder = new StreamsBuilder();
                KStream<String, Registration> registrations = builder.stream(Registration.topicName,
                                Consumed.with(AppSerdes.String(), AppSerdes.Registration()));

                KStream<String, Registration> registrations_on_paydate = registrations
                                .selectKey((k, v) -> v.getNewPaymentRequestDate().toString());

                registrations_on_paydate.peek((k, v) -> System.out.println(v));
                registrations_on_paydate.to(topicName,
                                Produced.with(AppSerdes.String(), AppSerdes.Registration()));

                KafkaStreams streams = new KafkaStreams(builder.build(), props);
                streams.start();

                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        System.out.println("Stopping Streams...");
                        streams.close();
                }));
        }
}
