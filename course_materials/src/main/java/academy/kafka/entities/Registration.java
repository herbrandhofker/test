package academy.kafka.entities;

import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;

import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.rocksdb.RocksIterator;

import academy.kafka.utils.RocksDbUtils;
import academy.kafka.utils.KafkaUtils;

import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaDescription;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaFormat;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaInject;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaString;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaTitle;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonSchemaInject(strings = { @JsonSchemaString(path = "$id", value = "academy.kafka.registration") })
@JsonSchemaDescription(value = "The registration of the car in the country")
@JsonSchemaTitle(value = "Registration")
public class Registration extends Entity {

    private static final Faker faker = new Faker(new Locale("nl"));

    private UUID regId = UUID.randomUUID();

    private Car car;
    private Person person;
    @JsonProperty(required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSchemaFormat(value = "date")
    private LocalDate from;
    @JsonProperty(required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSchemaFormat(value = "date")
    private LocalDate newPaymentRequestDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSchemaFormat(value = "date")
    private LocalDate till = null;

    public Registration() {
    }

    public Registration(Car car, Person person, LocalDate from) {
        this.car = car;
        this.person = person;
        this.from = from;
        this.newPaymentRequestDate=this.from;
    }

    public String getKey() {
        return regId.toString();
    }

    public UUID getRegId() {
        return regId;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public LocalDate getFrom() {
        return from;
    }

    public void setFrom(LocalDate from) {
        this.from = from;
    }

    public LocalDate getTill() {
        if (this.till == null)
            return null;
        return till;
    }

    public void setTill(LocalDate till) {
        this.till = till;
    }

    public LocalDate getNewPaymentRequestDate() {
        return newPaymentRequestDate;
    }

    public void setNewPaymentRequestDate(LocalDate newPaymentRequestDate) {
        this.newPaymentRequestDate = newPaymentRequestDate;
    }

    @Override
    public String toString() {
        return "Registration [car=" + car + ", from=" + from + ", newPaymentRequestDate=" + newPaymentRequestDate
                + ", person=" + person + ", regId=" + regId + ", till=" + till + "]";
    }


    /**************************************************************************************
     ************************** Utilities**************************************************
     *************************************************************************************/

    static public final String topicName = Registration.class.getSimpleName();

    public static Registration fromJson(String jsonStr) {
        try {
            return JACKSON_MAPPER.readValue(jsonStr, Registration.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Generates registrations, each car should have an owner from when the car is
     * first registered till now. A person can have 0, 1, or more cars registered.
     * For now, cars that stop to exist are not taking into account
     * 
     */

    public static int generateDatabase(int aantal) {
        int nrOfRegistrations = 0;
        String[] bsns = Person.generateDatabase(aantal);
        Set<String> kentekens = Car.generateDatabase(aantal);
        RocksDB personDb = RocksDbUtils.openDatabase(Person.topicName);
        RocksDB carDb = RocksDbUtils.openDatabase(Car.topicName);
        RocksDB regDb = RocksDbUtils.newDatabase(Registration.topicName);
        try {
            for (String kenteken : kentekens) {

                byte[] bytes = carDb.get(kenteken.getBytes());
                Car car = Car.fromJson(new String(bytes));

                Person person = Person.getRandomPerson(aantal, bsns, personDb);
                Date from = faker.date().birthday(0, 12);
                LocalDate firstReg = KafkaUtils.convertDateToLocalDate(from);
                Registration reg = new Registration(car, person, firstReg);
                for (;;) {

                    LocalDate somewhereInTheFuture = LocalDate.now().plusYears(3);
                    Date tmp = faker.date().between(from, KafkaUtils.convertLocalDateToDate(somewhereInTheFuture));
                    LocalDate till = KafkaUtils.convertDateToLocalDate(tmp);
                    if (till.isAfter(LocalDate.now())) {
                        String json = reg.toJson();
                        regDb.put(reg.getRegId().toString().getBytes(), json.getBytes());
                        nrOfRegistrations++; // registered till now
                        break;
                    }
                    reg.setTill(till);
                    String json = reg.toJson();
                    regDb.put(reg.getRegId().toString().getBytes(), json.getBytes());
                    nrOfRegistrations++;

                    person = Person.getRandomPerson(aantal, bsns, personDb);
                    firstReg = till;
                    reg = new Registration(car, person, firstReg);
                }
            }
        } catch (RocksDBException e) {
            e.printStackTrace();
        }

        personDb.close();
        carDb.close();
        regDb.close();

        regDb = RocksDbUtils.openDatabase(Registration.topicName);
        RocksIterator it = regDb.newIterator();
        RocksDB paymentRequestDb = RocksDbUtils.newDatabase(PaymentRequest.topicName);
        RocksDB paymentsDb = RocksDbUtils.newDatabase(Payment.topicName);
        it.seekToFirst();
        while (it.isValid()) {

            Registration registration = Registration.fromJson(new String(it.value()));

            LocalDate from = registration.getFrom();
            LocalDate tillFromReg = registration.getTill();
            if (tillFromReg == null)
                tillFromReg = LocalDate.now();

            LocalDate till = from.plusMonths(3);

            while (till.isBefore(tillFromReg)) {
                try {
                    PaymentRequest paymentRequest = PaymentRequest.generatePaymentRequest(registration, from, till);
                    paymentRequestDb.put(paymentRequest.getPaymentReference().getBytes(),
                            paymentRequest.toJson().getBytes());
                    int randomNum = ThreadLocalRandom.current().nextInt(1, 10 + 1);
                    if (randomNum > 6) {
                        int days = ThreadLocalRandom.current().nextInt((60 - 40), 80);
                        Payment payment = Payment.createPayment(paymentRequest, registration.getPerson(),
                                days);
                        paymentsDb.put(payment.getPaymentReference().getBytes(), payment.toJson().getBytes());
                    }
                } catch (RocksDBException e) {
                    e.printStackTrace();
                }
                till = till.plusMonths(3);
            }
            it.next();
        }

        it.close();
        regDb.close();
        paymentRequestDb.close();
        paymentsDb.close();
        return nrOfRegistrations;
    }

  
    
}
