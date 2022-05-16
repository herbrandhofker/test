package academy.kafka;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;

import academy.kafka.entities.Car;
import academy.kafka.entities.Day;
import academy.kafka.entities.Payment;
import academy.kafka.entities.PaymentIntake;
import academy.kafka.entities.PaymentRequest;
import academy.kafka.entities.Person;
import academy.kafka.entities.Registration;
import academy.kafka.serializers.JsonSerializer;
import academy.kafka.utils.KafkaUtils;
import academy.kafka.utils.RocksDbUtils;

public final class GenerateData {

    public static void generateSeriousStartPoint(int aantal) {
        KafkaUtils.deleteTopic(Car.topicName);
        KafkaUtils.deleteTopic(Registration.topicName);
        KafkaUtils.deleteTopic(Person.topicName);

        KafkaUtils.createTopic(Car.topicName, 1, (short) 1);
        KafkaUtils.createTopic(Registration.topicName, 1, (short) 1);
        KafkaUtils.createTopic(Person.topicName, 1, (short) 1);
        addDataToSeriousStartPoint(aantal);
    }
    
    public static void addDataToSeriousStartPoint(int aantal) {
           /*
         * store the data in rocksdb (persons.cars and registrations
         */
        Registration.generateDatabase(aantal);
        /* and then produce it to Kafka */
        int nrOfCars = KafkaUtils.<Car>produceEntities(Car.topicName, Car.class, (value) -> Car.fromJson(value), 0);
        int nrOfPersons = KafkaUtils.<Person>produceEntities(Person.topicName, Person.class,
                (value) -> Person.fromJson(value), 0);
        int nrOfRegistrations = KafkaUtils.<Registration>produceEntities(Registration.topicName, Registration.class,
                (value) -> Registration.fromJson(value), 0);

        RocksDbUtils.removeDatabase(Car.topicName);
        RocksDbUtils.removeDatabase(Person.topicName);
        RocksDbUtils.removeDatabase(Registration.topicName);

        System.out.printf("%d cars, %d persons and %d registrations added to Apache Kafka\n", nrOfCars, nrOfPersons,
                nrOfRegistrations);

    }

    public static void generateSandbox(int aantal) {
        KafkaUtils.deleteTopic(Car.topicName);
        KafkaUtils.deleteTopic(Registration.topicName);
        KafkaUtils.deleteTopic(Person.topicName);
        KafkaUtils.deleteTopic(PaymentRequest.topicName);
        KafkaUtils.deleteTopic(Payment.topicName);

        KafkaUtils.createTopic(Car.topicName, 1, (short) 1);
        KafkaUtils.createTopic(Registration.topicName, 1, (short) 1);
        KafkaUtils.createTopic(Person.topicName, 1, (short) 1);
        KafkaUtils.createTopic(PaymentRequest.topicName, 1, (short) 1);
        KafkaUtils.createTopic(Payment.topicName, 1, (short) 1);
        generateSandboxNoCleaning(aantal);
    }
 

    public static void generateSandboxNoCleaning(int aantal) {
      
        /*
         * store the data in rocksdb (persons.cars and registrations, paymentRequests
         * and payments
         */
        Registration.generateDatabase(aantal);
        /* and then produce it to Kafka */
        int nrOfCars = KafkaUtils.<Car>produceEntities(Car.topicName, Car.class, (value) -> Car.fromJson(value), 0);
        int nrOfPersons = KafkaUtils.<Person>produceEntities(Person.topicName, Person.class,
                (value) -> Person.fromJson(value), 0);
        int nrOfRegistrations = KafkaUtils.<Registration>produceEntities(Registration.topicName, Registration.class,
                (value) -> Registration.fromJson(value), 0);
        int nrOfPaymentRequests = KafkaUtils.<PaymentRequest>produceEntities(PaymentRequest.topicName,
                PaymentRequest.class, (value) -> PaymentRequest.fromJson(value), 0);
        int nrOfPayments = KafkaUtils.<Payment>produceEntities(Payment.topicName, Payment.class,
                (value) -> Payment.fromJson(value), 0);

        RocksDbUtils.removeDatabase(Car.topicName);
        RocksDbUtils.removeDatabase(Person.topicName);
        RocksDbUtils.removeDatabase(Registration.topicName);
        RocksDbUtils.removeDatabase(PaymentRequest.topicName);
        RocksDbUtils.removeDatabase(Payment.topicName);

        System.out.printf(
                "%d cars, %d persons and %d registrations with %d paymentRequests with %d payments added to Apache Kafka\n",
                nrOfCars, nrOfPersons, nrOfRegistrations, nrOfPaymentRequests, nrOfPayments);

    }

    /**
     * For apps that only use persons
     * 
     * @param aantal
     * @param seconds
     */
    public static void generatePersons(final int aantal, final int seconds) {
        String[] bsnSet = Person.generateDatabase(aantal);
        System.out.println(bsnSet.length + " persons generated");

        int nrOfPersons = KafkaUtils.<Person>produceEntities(Person.topicName, Person.class,
                (value) -> Person.fromJson(value), 0);
        System.out.println(String.format("%d persons added to Apache Kafka", nrOfPersons));

    }

    static public int createStupidPayments(int aantal) {
        RocksDB paymentsDb = RocksDbUtils.newDatabase(Payment.topicName);
        try {
            for (int i = 0; i < aantal; i++) {
                Payment payment = Payment.createRandomPayment("Stupid" + i);
                paymentsDb.put(payment.getPaymentReference().getBytes(), payment.toJson().getBytes());
            }
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
        paymentsDb.close();
        int nrOfPayments = KafkaUtils.<Payment>produceEntities(Payment.topicName, Payment.class,
                (value) -> Payment.fromJson(value), 0);
        System.out.println(String.format("%d stupid payments added to Apache Kafka", nrOfPayments));
        return nrOfPayments;
    }

    static public int createDoublePaymentIntakes(int aantal, int percentage) {
        List<PaymentIntake> changes = new ArrayList<PaymentIntake>();
        int nrOfPayments = 0;
        RocksDB paymentsDb = RocksDbUtils.newDatabase(PaymentIntake.topicName);
        try {
            for (int i = 0; i < aantal; i++) {
                PaymentIntake payment = PaymentIntake.createRandomIntakeBetaling("Stupid" + i);
                paymentsDb.put(payment.getKey().getBytes(), payment.toJson().getBytes());
            }
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
        paymentsDb.close();
              
        nrOfPayments = KafkaUtils.<PaymentIntake>produceEntities(PaymentIntake.topicName, PaymentIntake.class,
                (value) -> PaymentIntake.fromJson(value), 0);
              
        // now make list of changes
        paymentsDb = RocksDbUtils.openDatabase(PaymentIntake.topicName);
        RocksIterator it = paymentsDb.newIterator();
        it.seekToFirst();
        while (it.isValid()) {
            String value = new String(it.value());
            PaymentIntake payment = PaymentIntake.fromJson(value);
            int chance = ThreadLocalRandom.current().nextInt(1, 100);
            if (chance < percentage) {
                int randomAmount = ThreadLocalRandom.current().nextInt(1, 12) * 100;
                payment.setAmount(Double.valueOf(randomAmount));
                changes.add(payment);
            }
            it.next();
        }
        it.close();
        paymentsDb.close();

        // now add them to the database
        paymentsDb = RocksDbUtils.newDatabase(PaymentIntake.topicName);
        for (PaymentIntake p : changes) {
            try {
                paymentsDb.put(p.getKey().getBytes(), p.toJson().getBytes());
            } catch (RocksDBException e) {
                e.printStackTrace();
            }
        }

        paymentsDb.close();
        changes = null;
        int changed = KafkaUtils.<PaymentIntake>produceEntities(PaymentIntake.topicName, PaymentIntake.class,
                (value) -> PaymentIntake.fromJson(value), 0);

        System.out.println("produced payments :" + nrOfPayments + " made double (same paymentid):" + changed);
        return nrOfPayments;
    }

    public static void main(String args[]) {
        // generateSandbox(200, 0);
        createDoublePaymentIntakes(10, 70);
    }

    public static void insertDay(Day day) {
        Properties props = new Properties();
        props.put("JsonClass", Day.class);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, new StringSerializer().getClass());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, new JsonSerializer<Day>().getClass());
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, academy.kafka.config.AppConfig.BootstrapServers);

        Producer<String, Day> producer = new KafkaProducer<>(props);
        producer.send(new ProducerRecord<String, Day>(Day.topicName, day.getKey(), day));
        producer.close();
    }
    
  
}
