package academy.kafka.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaDescription;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaInject;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaString;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaTitle;



@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonSchemaInject(strings = { @JsonSchemaString(path = "$id", value = "academy.kafka.entity.FunctionalException") })
@JsonSchemaDescription(value = "A FunctionalException")
@JsonSchemaTitle(value = "FunctionalException")
public class FunctionalException extends Entity {
  
    private LocalDateTime timestamp;
    
    private String streamApp;
    private String topic;
    private String topicKey;
    private String errorMessage;
    
    public FunctionalException() {
    }

    public FunctionalException(String streamApp, String topic, String topicKey, String errorMessage) {
        this.timestamp= LocalDateTime.now();
        this.streamApp = streamApp;
        this.topic = topic;
        this.topicKey = topicKey;
        this.errorMessage = errorMessage;
    }
   

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

     public String getStreamApp() {
        return streamApp;
    }

    public void setStreamApp(String streamApp) {
        this.streamApp = streamApp;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTopicKey() {
        return topicKey;
    }

    public void setTopicKey(String topicKey) {
        this.topicKey = topicKey;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static String getTopicname() {
        return topicName;
    }

  
   

    public String getKey(){
        return timestamp.toString();
    }
 
    @Override
    public String toString() {
        return "FunctionalException [errorMessage=" + errorMessage + ", streamApp=" + streamApp + ", timestamp="
                + timestamp + ", topic=" + topic + ", topicKey=" + topicKey + "]";
    }

   
    /**************************************************************************************
     ************************** Utilities**************************************************
     *************************************************************************************/
    static public final String topicName = FunctionalException.class.getSimpleName();   

    public static FunctionalException fromJson(String jsonStr) {
        try {
            return JACKSON_MAPPER.readValue(jsonStr, FunctionalException.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

 

   
}
