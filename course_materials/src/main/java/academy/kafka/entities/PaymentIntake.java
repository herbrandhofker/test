
package academy.kafka.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaDescription;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaFormat;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaInject;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaString;
import com.kjetland.jackson.jsonSchema.annotations.JsonSchemaTitle;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonSchemaInject(strings = { @JsonSchemaString(path = "$id", value = "academy.kafka.entity.PaymentIntake") })
@JsonSchemaDescription(value = "PaymentIntakes recieved from banc")
@JsonSchemaTitle(value = "PaymentIntake")
public class PaymentIntake extends Entity {

    public enum Status {
        OPEN, PAYED
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
    private Status status = Status.OPEN;

    public PaymentIntake() {
    }

    public PaymentIntake(UUID paymentId, String paymentReference, String bancAccount, String nameOnBancAccount,
            LocalDateTime receivedTime, Double amount) {
        this.paymentId = paymentId;
        this.paymentReference = paymentReference;
        this.bancAccount = bancAccount;
        this.nameOnBancAccount = nameOnBancAccount;
        this.receivedTime = receivedTime;
        this.amount = amount;
    }

    public String getKey() {
        return paymentId.toString();
    }

    public UUID getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
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

    public void setNameOnBancAccount(String nameOnBancAccount) {
        this.nameOnBancAccount = nameOnBancAccount;
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
        return "PaymentIntake [amount=" + amount + ", bancAccount=" + bancAccount + ", nameOnBancAccount="
                + nameOnBancAccount + ", paymentId=" + paymentId + ", paymentReference=" + paymentReference
                + ", receivedTime=" + receivedTime + ", remark=" + remark + ", status=" + status + "]";
    }


    /**************************************************************************************
     ************************** Utilities**************************************************
     *************************************************************************************/
    static public final String topicName = PaymentIntake.class.getSimpleName();

    public static PaymentIntake fromJson(String jsonStr) {
        try {
            return JACKSON_MAPPER.readValue(jsonStr, PaymentIntake.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PaymentIntake createBetaling(PaymentRequest inn, Person person, int daysToAdd) {
        LocalDate innDate = inn.getCreatedTime();
        LocalDateTime payDateTime = innDate.plusDays(daysToAdd).atStartOfDay();
        PaymentIntake payment = new PaymentIntake(UUID.randomUUID(), inn.getPaymentReference(), person.getBancAccount(),
                "ING", payDateTime, inn.getAmount());
        payment.setRemark("payment " + daysToAdd + " days after request sent");
        return payment;
    }

    public static PaymentIntake createRandomIntakeBetaling(String stupidReference) {
        LocalDate innDate = LocalDate.now().minusMonths(16);
        LocalDateTime payDateTime = innDate.plusDays(12).atStartOfDay();

        int randomAmount = ThreadLocalRandom.current().nextInt(8, 12) * 100;
        PaymentIntake payment = new PaymentIntake(UUID.randomUUID(), stupidReference, "123456789", "ING", payDateTime,
                Double.valueOf(randomAmount));
        payment.setRemark("some payment with some strange reference");
        return payment;
    }



}
