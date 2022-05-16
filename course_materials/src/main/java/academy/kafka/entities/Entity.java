package academy.kafka.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import academy.kafka.utils.KafkaUtils;

public abstract class Entity {
    public static final ObjectMapper JACKSON_MAPPER = KafkaUtils.getJacksonMapper();

    public String toJson() {
        try {
            return JACKSON_MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @JsonIgnore    
    abstract String getKey();
}
