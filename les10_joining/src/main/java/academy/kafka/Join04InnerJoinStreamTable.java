package academy.kafka;

import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;

import academy.kafka.config.AppConfig;
import academy.kafka.entities.Payment;
import academy.kafka.entities.PaymentRequest;
import academy.kafka.entities.PaymentStatus;
import academy.kafka.serdes.AppSerdes;


public class Join04InnerJoinStreamTable {
        static int rn = ThreadLocalRandom.current().nextInt(1000);

        public static void main(String[] args) {
                Properties props = new Properties();
                props.put(StreamsConfig.APPLICATION_ID_CONFIG, "inner_join_tbl_tbl" + rn);
                props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfig.BootstrapServers);
                props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, AppSerdes.String().getClass());
                props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 0);

                StreamsBuilder builder = new StreamsBuilder();
                KTable<String, PaymentRequest> paymentRequestTbl = builder.table(PaymentRequest.topicName,
                                Consumed.with(AppSerdes.String(), AppSerdes.PaymentRequest()));
                KStream<String, Payment> paymentStream = builder.stream(Payment.topicName,
                                Consumed.with(AppSerdes.String(), AppSerdes.Payment()));

            
                KStream<String, PaymentStatus> result = paymentStream.join(paymentRequestTbl,
                                (payment, paymentRequest) ->  new PaymentStatus(paymentRequest, payment));

                result.peek((k, v) -> {
                        System.out.println(v.toString());
                });

                KafkaStreams streams = new KafkaStreams(builder.build(), props);
                streams.start();

                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        System.out.println("Stopping Streams...");
                        streams.close();
                }));
        }
}
