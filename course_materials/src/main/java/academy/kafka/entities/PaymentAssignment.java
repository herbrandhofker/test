
package academy.kafka.entities;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaInject;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaString;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonSchemaInject(strings = {
    @JsonSchemaString(path = "$id", value="academy.kafka.payment_assignment")
  }
)

public class PaymentAssignment extends Entity{

   
    private PaymentRequest paymentRequest;
    private Payment payment;
    
    public PaymentAssignment(PaymentRequest paymentRequest, Payment payment) {
        this.paymentRequest = paymentRequest;
        this.payment = payment;
    }

    public PaymentAssignment() {
    }

    public String getKey(){
        return paymentRequest.getKey()+payment.getKey();//TODO
    }

    public PaymentRequest getPaymentRequest() {
        return paymentRequest;
    }

    public void setPaymentRequest(PaymentRequest paymentRequest) {
        this.paymentRequest = paymentRequest;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    @Override
    public String toString() {
        return "PaymentAssignment [paymentRequest=" + paymentRequest + ", payment=" + payment + "]";
    }


    /**************************************************************************************
     ************************** Utilities**************************************************
     *************************************************************************************/

    static public final String topicName = PaymentAssignment.class.getSimpleName(); 

    public static PaymentAssignment fromJson(String jsonStr) {
        try {
            return JACKSON_MAPPER.readValue(jsonStr, PaymentAssignment.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

   
   

}
