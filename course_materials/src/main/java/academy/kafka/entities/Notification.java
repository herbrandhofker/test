package academy.kafka.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaDescription;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaInject;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaString;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaTitle;

import java.text.SimpleDateFormat;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonSchemaInject(strings = { @JsonSchemaString(path = "$id", value = "academy.kafka.entity.Notification") })
@JsonSchemaDescription(value = "Some message for some reason")
@JsonSchemaTitle(value = "Notification")
public class Notification extends Entity{

    private static final SimpleDateFormat birthdayFormat = new SimpleDateFormat("yyyy-MM-dd");

    @JsonProperty(required = true)
    private String bsn;
    private String kenteken;
    private String registrationId;
    @JsonProperty(required = true)
    private String message;

    public Notification() {
    }

    public Notification(String bsn, String kenteken, String registrationId, String message) {
        this.bsn = bsn;
        this.kenteken = kenteken;
        this.registrationId = registrationId;
        this.message = message;
    }

    public static SimpleDateFormat getBirthdayformat() {
        return birthdayFormat;
    }

    public String getBsn() {
        return bsn;
    }

    public void setBsn(String bsn) {
        this.bsn = bsn;
    }

    public String getKenteken() {
        return kenteken;
    }

    public void setKenteken(String kenteken) {
        this.kenteken = kenteken;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**************************************************************************************
     ************************** Utilities**************************************************
     *************************************************************************************/

    static public final String topicName = Notification.class.getSimpleName(); ;
    
    public static Notification fromJson(String jsonStr) {
        try {
            return JACKSON_MAPPER.readValue(jsonStr, Notification.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {        
        String msg= "Notification [ message=" + message;
        if (bsn!=null) msg+=",bsn="+bsn;
        if (kenteken!=null) msg+=",license plate="+kenteken;
        if (registrationId!=null) msg+=",registrationId="+registrationId;
        return msg;
    }

    @Override
    String getKey() {       
        return bsn+kenteken+this.registrationId;
    }

}
