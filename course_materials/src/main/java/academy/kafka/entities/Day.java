package academy.kafka.entities;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaDescription;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaFormat;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaInject;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaString;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaTitle;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonSchemaInject(strings = { @JsonSchemaString(path = "$id", value = "academy.kafka.entity.Day") })
@JsonSchemaDescription(value = "The day")
@JsonSchemaTitle(value = "Day")
public class Day extends Entity {
  
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSchemaFormat(value = "date")   
    private LocalDate day;
  
    public Day() {
    }

    public Day(LocalDate day) {
        this.day=day;
       
    }

    public String getKey(){
        return this.day.toString();
    }

    
    /**************************************************************************************
     ************************** Utilities**************************************************
     *************************************************************************************/
    static public final String topicName = Day.class.getSimpleName();   

    public static Day fromJson(String jsonStr) {
        try {
            return JACKSON_MAPPER.readValue(jsonStr, Day.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "Day [day=" + day + "]";
    }

   
  
}
