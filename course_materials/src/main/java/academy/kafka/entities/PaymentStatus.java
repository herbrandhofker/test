
package academy.kafka.entities;

import java.time.Period;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaInject;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaString;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonSchemaInject(strings = {
    @JsonSchemaString(path = "$id", value="academy.kafka.payment_status")
  }
)
public class PaymentStatus extends Entity{

    private String paymentId;
    private String paymentReference;
    private String status;

    public PaymentStatus() {
    }

    public String getKey(){
        return paymentId;
    }
    
    public PaymentStatus(PaymentRequest paymentRequest, Payment payment) {
        if (paymentRequest!=null)
            this.paymentReference=paymentRequest.getKey();
        if (payment == null) {
            this.status = "Not yet payed";
            this.paymentId = paymentRequest.getPaymentReference();
       
            return;
        }
        if (paymentRequest == null) {
            this.status = "cannot understand payment reference "+payment.getPaymentReference();
            this.paymentId = payment.getPaymentReference();
      
            return;
        }

        this.paymentId=payment.getKey();
      
        Period p = Period.between(paymentRequest.getCreatedTime(), payment.getReceivedTime().toLocalDate());
        int days = p.getDays();
        this.status = "Payed " + payment.getAmount() + " " + days + " days after paymentRequest is sent (paymentRequest was "
                + paymentRequest.getAmount() + ")";

    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PaymentStatus [paymentReference=" + paymentReference + ", status=" + status + "]";
    }

    /**************************************************************************************
     ************************** Utilities**************************************************
     *************************************************************************************/

    static public final String topicName = PaymentStatus.class.getSimpleName(); 

    public static PaymentStatus fromJson(String jsonStr) {
        try {
            return JACKSON_MAPPER.readValue(jsonStr, PaymentStatus.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
