
package academy.kafka.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaDescription;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaFormat;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaInject;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaString;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaTitle;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonSchemaInject(strings = { @JsonSchemaString(path = "$id", value = "academy.kafka.entity.Payment") })
@JsonSchemaDescription(value = "Payment recieved from banc")
@JsonSchemaTitle(value = "Payment")
public class Payment extends Entity{
   
    public enum Status {
        REJECTED,
        ACCEPTED,
        OPEN,
        PAYED
      }

    private UUID paymentId;
    private String paymentReference;
    private String bancAccount;
    private String nameOnBancAccount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonSchemaFormat(value = "datetime-local")
   
    private LocalDateTime receivedTime;
    private Double amount;
    private String remark;
    private Status status =Status.OPEN;
  
    public Payment() {
    }

    public Payment(UUID paymentId, String paymentReference, String bancAccount, String nameOnBancAccount,
            LocalDateTime receivedTime, Double amount) {
        this.paymentId = paymentId;
        this.paymentReference = paymentReference;
        this.bancAccount = bancAccount;
        this.nameOnBancAccount = nameOnBancAccount;
        this.receivedTime = receivedTime;
        this.amount = amount;
    }

    public  Payment(PaymentIntake intake, Status status) {
        this.paymentId = intake.getPaymentId();
        this.paymentReference =intake.getPaymentReference();
        this.bancAccount = intake.getBancAccount();
        this.nameOnBancAccount = intake.getNameOnBancAccount();
        this.receivedTime = intake.getReceivedTime();
        this.amount = intake.getAmount();
        this.setStatus(status);        
    }
    
    public String getKey(){
        return paymentId.toString();
    }
    
    public UUID getPaymentId() {
        return paymentId;
    }  

    public String getPaymentReference() {
        return paymentReference;
    }

    public String getBancAccount() {
        return bancAccount;
    }

    public void setBancAccount(String bancAccount) {
        this.bancAccount = bancAccount;
    }

    public String getNameOnBancAccount() {
        return nameOnBancAccount;
    }
    
    public LocalDateTime getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(LocalDateTime receivedTime) {
        this.receivedTime = receivedTime;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Payment : [paymentReference=" + paymentReference +", paymentId=" + paymentId+", amount=" + amount + ", bancAccount=" + bancAccount + ", nameOnBancAccount=" + nameOnBancAccount
                + ", receivedTime="        + receivedTime + ", remark=" + remark + ", status=" + status + "]";
    }

    /**************************************************************************************
     ************************** Utilities**************************************************
     *************************************************************************************/
    static public final String topicName = Payment.class.getSimpleName();

    public static Payment fromJson(String jsonStr) {
        try {
            return JACKSON_MAPPER.readValue(jsonStr, Payment.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Payment createPayment(PaymentRequest paymentRequest, Person person, int daysToAdd) {
        LocalDate innDate = paymentRequest.getCreatedTime();
        LocalDateTime payDateTime = innDate.plusDays(daysToAdd).atStartOfDay();
        Payment payment = new Payment(UUID.randomUUID(), paymentRequest.getPaymentReference(), person.getBancAccount(), "ING",
                payDateTime, paymentRequest.getAmount());
        payment.setRemark("payment " + daysToAdd + " days after request sent");
        return payment;
    }

   

    public static Payment createRandomPayment(String stupidReference) {
        LocalDate innDate = LocalDate.now().minusMonths(16);
        LocalDateTime payDateTime = innDate.plusDays(12).atStartOfDay();

        Payment payment = new Payment(UUID.randomUUID(), stupidReference, "123456789", "ING", payDateTime,
                Double.parseDouble("1200"));
        payment.setRemark("some payment with some strange reference");
        return payment;
    }

  
}
