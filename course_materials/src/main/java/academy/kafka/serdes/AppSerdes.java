package academy.kafka.serdes;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

import academy.kafka.entities.FunctionalException;
import academy.kafka.entities.Notification;
import academy.kafka.entities.Payment;
import academy.kafka.entities.PaymentIntake;
import academy.kafka.entities.PaymentRequest;
import academy.kafka.entities.PaymentStatus;
import academy.kafka.entities.Person;
import academy.kafka.entities.Car;
import academy.kafka.entities.Day;
import academy.kafka.entities.PaymentIntakeAggregate;
import academy.kafka.entities.ProvinceAggregate;
import academy.kafka.entities.Registration;
import academy.kafka.serializers.JsonDeserializer;
import academy.kafka.serializers.JsonSerializer;

public class AppSerdes extends Serdes {

    static public final class CarSerde extends WrapperSerde<Car> {
        public CarSerde() {
            super(new JsonSerializer<Car>(), new JsonDeserializer<Car>());
        }
    }

    static public Serde<Car> Car() {
        CarSerde serde = new CarSerde();
        Map<String, Object> serdeConfigs = new HashMap<>();
        serdeConfigs.put(JsonDeserializer.JSON_CLASS, Car.class);
        serde.configure(serdeConfigs, false);
        return serde;
    }

    static public final class FunctionalExceptionSerde extends WrapperSerde<FunctionalException> {
        public FunctionalExceptionSerde() {
            super(new JsonSerializer<FunctionalException>(), new JsonDeserializer<FunctionalException>());
        }
    }

    static public Serde<FunctionalException> FunctionalException() {
        FunctionalExceptionSerde serde = new FunctionalExceptionSerde();
        Map<String, Object> serdeConfigs = new HashMap<>();
        serdeConfigs.put(JsonDeserializer.JSON_CLASS, FunctionalException.class);
        serde.configure(serdeConfigs, false);
        return serde;
    }

    static public final class PersonSerde extends WrapperSerde<Person> {
        public PersonSerde() {
            super(new JsonSerializer<Person>(), new JsonDeserializer<Person>());
        }
    }

    static public Serde<Person> Person() {
        PersonSerde serde = new PersonSerde();
        Map<String, Object> serdeConfigs = new HashMap<>();
        serdeConfigs.put(JsonDeserializer.JSON_CLASS, Person.class);
        serde.configure(serdeConfigs, false);
        return serde;
    }

    static public final class RegistrationSerde extends WrapperSerde<Registration> {
        public RegistrationSerde() {
            super(new JsonSerializer<Registration>(), new JsonDeserializer<Registration>());
        }
    }

    static public Serde<Registration> Registration() {
        RegistrationSerde serde = new RegistrationSerde();
        Map<String, Object> serdeConfigs = new HashMap<>();
        serdeConfigs.put(JsonDeserializer.JSON_CLASS, Registration.class);
        serde.configure(serdeConfigs, false);
        return serde;
    }

    static public final class NotificationSerde extends WrapperSerde<Notification> {
        public NotificationSerde() {
            super(new JsonSerializer<Notification>(), new JsonDeserializer<Notification>());
        }
    }

    static public Serde<Notification> Notification() {
        NotificationSerde serde = new NotificationSerde();
        Map<String, Object> serdeConfigs = new HashMap<>();
        serdeConfigs.put(JsonDeserializer.JSON_CLASS, Notification.class);
        serde.configure(serdeConfigs, false);
        return serde;
    }

    static public final class PaymentRequestSerde extends WrapperSerde<PaymentRequest> {
        public PaymentRequestSerde() {
            super(new JsonSerializer<PaymentRequest>(), new JsonDeserializer<PaymentRequest>());
        }
    }

    static public Serde<PaymentRequest> PaymentRequest() {
        PaymentRequestSerde serde = new PaymentRequestSerde();
        Map<String, Object> serdeConfigs = new HashMap<>();
        serdeConfigs.put(JsonDeserializer.JSON_CLASS, PaymentRequest.class);
        serde.configure(serdeConfigs, false);
        return serde;
    }

    static public final class PaymentSerde extends WrapperSerde<Payment> {
        public PaymentSerde() {
            super(new JsonSerializer<Payment>(), new JsonDeserializer<Payment>());
        }
    }

    static public Serde<Payment> Payment() {
        PaymentSerde serde = new PaymentSerde();
        Map<String, Object> serdeConfigs = new HashMap<>();
        serdeConfigs.put(JsonDeserializer.JSON_CLASS, Payment.class);
        serde.configure(serdeConfigs, false);
        return serde;
    }

    static public final class PaymentIntakeSerde extends WrapperSerde<PaymentIntake> {
        public PaymentIntakeSerde() {
            super(new JsonSerializer<PaymentIntake>(), new JsonDeserializer<PaymentIntake>());
        }
    }

    static public Serde<PaymentIntake> PaymentIntake() {
        PaymentIntakeSerde serde = new PaymentIntakeSerde();
        Map<String, Object> serdeConfigs = new HashMap<>();
        serdeConfigs.put(JsonDeserializer.JSON_CLASS, PaymentIntake.class);
        serde.configure(serdeConfigs, false);
        return serde;
    }

    static public final class BetalingStatusSerde extends WrapperSerde<PaymentStatus> {
        public BetalingStatusSerde() {
            super(new JsonSerializer<PaymentStatus>(), new JsonDeserializer<PaymentStatus>());
        }
    }

    static public Serde<PaymentStatus> BetalingStatus() {
        BetalingStatusSerde serde = new BetalingStatusSerde();
        Map<String, Object> serdeConfigs = new HashMap<>();
        serdeConfigs.put(JsonDeserializer.JSON_CLASS, PaymentStatus.class);
        serde.configure(serdeConfigs, false);
        return serde;
    }

    static public final class ProvinceAggregateSerde extends WrapperSerde<ProvinceAggregate> {
        public ProvinceAggregateSerde() {
            super(new JsonSerializer<ProvinceAggregate>(), new JsonDeserializer<ProvinceAggregate>());
        }
    }

    static public Serde<ProvinceAggregate> ProvinceAggregate() {
        ProvinceAggregateSerde serde = new ProvinceAggregateSerde();
        Map<String, Object> serdeConfigs = new HashMap<>();
        serdeConfigs.put(JsonDeserializer.JSON_CLASS, ProvinceAggregate.class);
        serde.configure(serdeConfigs, false);
        return serde;
    }

    static public final class DaySerde extends WrapperSerde<Day> {
        public DaySerde() {
            super(new JsonSerializer<Day>(), new JsonDeserializer<Day>());
        }
    }

    static public Serde<Day> Day() {
        DaySerde serde = new DaySerde();
        Map<String, Object> serdeConfigs = new HashMap<>();
        serdeConfigs.put(JsonDeserializer.JSON_CLASS, Day.class);
        serde.configure(serdeConfigs, false);
        return serde;
    }

    static public final class PaymentIntakeAggregateSerde extends WrapperSerde<PaymentIntakeAggregate> {
        public PaymentIntakeAggregateSerde() {
            super(new JsonSerializer<PaymentIntakeAggregate>(), new JsonDeserializer<PaymentIntakeAggregate>());
        }
    }

    static public Serde<PaymentIntakeAggregate> PaymentIntakeAggregate() {
        PaymentIntakeAggregateSerde serde = new PaymentIntakeAggregateSerde();
        Map<String, Object> serdeConfigs = new HashMap<>();
        serdeConfigs.put(JsonDeserializer.JSON_CLASS, PaymentIntakeAggregate.class);
        serde.configure(serdeConfigs, false);
        return serde;
    }


}
