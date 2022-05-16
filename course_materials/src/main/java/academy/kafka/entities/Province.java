package academy.kafka.entities;

import java.util.concurrent.ThreadLocalRandom;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import academy.kafka.utils.KafkaUtils;

import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaDescription;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaInject;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaString;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaTitle;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonSchemaInject(strings = { @JsonSchemaString(path = "$id", value = "academy.kafka.entity.Province") })
@JsonSchemaDescription(value = "The province as receiver for tax")
@JsonSchemaTitle(value = "Province")
public class Province {
    private static final ObjectMapper JACKSON_MAPPER = KafkaUtils.getJacksonMapper();

    private String name;
    private int opcenten;

    public Province() {
    }

    public Province(String name, int opcenten) {
        this.name = name;
        this.opcenten = opcenten;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOpcenten() {
        return opcenten;
    }

    public void setOpcenten(int opcenten) {
        this.opcenten = opcenten;
    }

    @Override
    public String toString() {
        return "Province [name=" + name + ", opcenten=" + opcenten + "]";
    }

    public String toJson() {
        try {
            return JACKSON_MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Province fromJson(String jsonStr) {
        try {
            return JACKSON_MAPPER.readValue(jsonStr, Province.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final Province provinces[] = { new Province("Limburg", 1), new Province("Noord Brabant", 1),
            new Province("Zeeland", 1), new Province("Zuid Holland", 2), new Province("Noord Holland", 3),
            new Province("Friesland", 1), new Province("Groningen", 1), new Province("Drente", 1),
            new Province("Overijssel", 1), new Province("Gelderland", 1), new Province("Flevoland", 1),
            new Province("Utrecht", 2) };

    static public Province getRandomProvince() {
        int randomNum = ThreadLocalRandom.current().nextInt(0, provinces.length);
        return Province.provinces[randomNum];
    }

    public static Province getProvinceOnName(String name){
        for (int i=0; i<provinces.length;i++){
            if (provinces[i].getName().equals(name))
            return provinces[i];
        }
        return null;

    }

}
