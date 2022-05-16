
package academy.kafka.entities;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaDescription;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaFormat;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaInject;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaString;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaTitle;



@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonSchemaInject(strings = { @JsonSchemaString(path = "$id", value = "academy.kafka.PaymentRequest") })
@JsonSchemaDescription(value = "Payment request for a certain period")
@JsonSchemaTitle(value = "PaymentRequest")
public class PaymentRequest extends Entity{
   
    public enum Status {
        OPEN,
        PAYED
      }
    private String paymentReference;
    private String provinceName;
    @JsonProperty(required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSchemaFormat(value = "date") 
    private LocalDate createdTime;
    private Double amount;
    @JsonProperty(required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSchemaFormat(value = "date")  
    private LocalDate periodStart;
    @JsonProperty(required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSchemaFormat(value = "date")  private LocalDate periodEnd;
    private Status status =Status.OPEN;
    
    public PaymentRequest() {
    }

    public PaymentRequest(String paymentReference, UUID regId, String provinceName, LocalDate createdTime, Double amount,
            LocalDate periodStart, LocalDate periodEnd) {
        this.paymentReference = paymentReference;
        this.provinceName = provinceName;
        this.createdTime = createdTime;
        this.amount = amount;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
    }

    public String getKey(){
        return paymentReference;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
    }

    public LocalDate getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDate createdTime) {
        this.createdTime = createdTime;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    
    @Override
    public String toString() {
        return "PaymentRequest [amount=" + amount + ", createdTime=" + createdTime + ", paymentReference=" + paymentReference
                + ", periodEnd=" + periodEnd + ", periodStart=" + periodStart + ", provinceName=" + provinceName
                + ", status=" + status + "]";
    }

    public PaymentAssignment assignPaymentToPaymentRequest(Payment payment) {
       // if (this.amount<= payment.getAmount()){
            this.status=Status.PAYED;
            payment.setStatus(Payment.Status.PAYED);
      //  }
      PaymentAssignment pa= new PaymentAssignment(this, payment);
      System.out.println("===>>>"+pa.getPaymentRequest());
      return pa;
    }

    /**************************************************************************************
     ************************** Utilities**************************************************
     *************************************************************************************/
    static public final String topicName = PaymentRequest.class.getSimpleName();

    public static PaymentRequest fromJson(String jsonStr) {
        try {
            return JACKSON_MAPPER.readValue(jsonStr, PaymentRequest.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    static String getPaymentReference(String regid,LocalDate date){
        String result= String.format("%s%04d%02d",regid, date.getYear()-2000,date.getMonth().getValue());
        return result;
    }

    static public PaymentRequest generatePaymentRequest(Registration registration, LocalDate periodStart, LocalDate periodEnd) {
        Person person = registration.getPerson();
        Province province = person.getProvince();
        String paymentReference = getPaymentReference(person.getBsn(), periodStart);
        Double tax = Double.valueOf(registration.getCar().getTax() * (100 + province.getOpcenten()) / 100);
        return new PaymentRequest(paymentReference, registration.getRegId(), registration.getPerson().getProvince().getName(),
                LocalDate.now(), tax, periodStart, periodEnd);
    } 
}
