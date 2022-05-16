package academy.kafka.entities;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ProvinceAggregate extends Entity {
    String name;
    int total;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ProvinceAggregate() {
    }

    public ProvinceAggregate(String name,int total) {
        this.name = name;
        this.total = total;
    }

    @Override
    public String toString() {
        return "ProvinceAggregate [name=" + name + ", total=" + total + "]";
    }

    public static ProvinceAggregate fromJson(String jsonStr) {
        try {
            return JACKSON_MAPPER.readValue(jsonStr, ProvinceAggregate.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    String getKey() {
        return name;
    }

    public String toJson() {
        try {
            return JACKSON_MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
