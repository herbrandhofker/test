package academy.kafka.entities;

import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaDescription;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaFormat;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaInject;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaOptions;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaOptions.Item;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaString;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaTitle;

import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import academy.kafka.utils.BsnGenerator;
import academy.kafka.utils.IbanGenerator;
import academy.kafka.utils.KafkaUtils;
import academy.kafka.utils.RocksDbUtils;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonSchemaInject(strings = { @JsonSchemaString(path = "$id", value = "academy.kafka.entity.Person") })
@JsonSchemaDescription(value = "A natural person or a company (NYI)")
@JsonSchemaTitle(value = "Persons")
public class Person extends Entity {

    private static final Faker faker = new Faker(new Locale("nl"));

    @JsonProperty(required = true)
    private String bsn;
    private String firstName;
    @JsonProperty(required = true)
    private String lastName;
    private String bancAccount;
    @JsonProperty(required = true, defaultValue = "1980-11-11")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSchemaFormat(value = "date")
    private LocalDate birthday;
    @JsonProperty(required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSchemaFormat(value = "date")
    private LocalDate registerDate;

    @JsonProperty(required = true, defaultValue = "21")
    @JsonSchemaOptions(items = { @Item(name = "minimum", value = "18") })
    private Integer age;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "[0-9]{5}")
    private String zipCode;
    @JsonProperty(required = true)
    private Province province;

    public Person() {
    }

    public Person(LocalDate registerDate, String bsn, String firstName, String lastName, String bancAccount,
            LocalDate birthday, Province province) {
        this.registerDate = registerDate;
        this.bsn = bsn;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bancAccount = bancAccount;
        this.birthday = birthday;
        this.province = province;
    }

    public String getKey() {
        return bsn;
    }

    public String getBsn() {
        return bsn;
    }

    public void setBsn(String bsn) {
        this.bsn = bsn;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBancAccount() {
        return bancAccount;
    }

    public void setBancAccount(String bancAccount) {
        this.bancAccount = bancAccount;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public LocalDate getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDate registerDate) {
        this.registerDate = registerDate;
    }

    @Override
    public String toString() {
        return "Person [bancAccount=" + bancAccount + ", birthday=" + birthday + ", bsn=" + bsn + ", province="
                + province + ", firstName=" + firstName + ", lastName=" + lastName + ", registerDate=" + registerDate
                + "]";
    }

    /**************************************************************************************
     ************************** Utilities**************************************************
     *************************************************************************************/

    static public final String topicName = Person.class.getSimpleName(); 
 
    public static Person fromJson(String jsonStr) {
        try {
            return JACKSON_MAPPER.readValue(jsonStr, Person.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    static public Person generatePerson(String bsn) {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        Province province = Province.getRandomProvince();
        LocalDate birthDay = KafkaUtils.convertDateToLocalDate(faker.date().birthday(18, 99));

        String bancAccount = IbanGenerator.ibanGenerater();// faker.business().creditCardNumber();
        LocalDate registerDate = KafkaUtils.convertDateToLocalDate(faker.date().birthday(10, 18));

        return new Person(registerDate, bsn, firstName, lastName, bancAccount, birthDay, province);
    }

    static public Person movePerson(Person person) {
        Province newProvince = Province.getRandomProvince();

        if (!newProvince.getName().equals(person.getProvince().getName())) {
            try {
                LocalDate lastRegisterDate = person.getRegisterDate().plusYears(1);

                LocalDate newDate = KafkaUtils.convertDateToLocalDate(
                        faker.date().between(KafkaUtils.convertLocalDateToDate(lastRegisterDate), new Date()));
                if (newDate == null)
                    return null;
                if (newDate.isAfter(lastRegisterDate)) {
                    person.setProvince(newProvince);
                    person.setRegisterDate(newDate);
                }
            } catch (Exception e) {
                return null;
            }
        }
        return person;
    }

    static public String[] generateDatabase(int aantal) {
        Set<String> bsnSet = BsnGenerator.generateRandomBsnNummers(aantal);// first a unique list of bsn's
        RocksDB personDb = RocksDbUtils.newDatabase(Person.topicName);
        try {
            for (String bsn : bsnSet) {
                Person person = Person.generatePerson(bsn);
                personDb.put(person.getBsn().getBytes(), person.toJson().getBytes());
            }
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
        personDb.close();
        return bsnSet.toArray(String[]::new);
    }

    public static Person getRandomPerson(int aantal, String bsns[], RocksDB personDb) {
        int randomBsnId = ThreadLocalRandom.current().nextInt(0, aantal);// 33% of cases
        String bsn = bsns[randomBsnId];
        byte[] bytes;
        try {
            bytes = personDb.get(bsn.getBytes());
            String json = new String(bytes);
            Person person = Person.fromJson(json);
            return person;
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
        return null;
    }
}
