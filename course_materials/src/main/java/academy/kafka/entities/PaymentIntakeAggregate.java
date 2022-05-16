package academy.kafka.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentIntakeAggregate {
    @JsonProperty(required = true)
    ArrayList<PaymentIntake> list= new ArrayList<PaymentIntake>(); 


    public PaymentIntakeAggregate() {       
    }
                
    public PaymentIntakeAggregate(PaymentIntakeAggregate old, PaymentIntake paymentIntake) {
        this.list.addAll(old.list);
        this.list.add(paymentIntake);
    }

    @JsonIgnore  
    public PaymentIntake getUnique(){
        return list.get(0);
    }
    
    @JsonIgnore  
    public List<PaymentIntake> getNotUnique(){
        int size= list.size();
        if (size<2)
            return new ArrayList<PaymentIntake>();
        return list.subList(1,size);
    }

    @Override
    public String toString() { 
        return "PaymentIntakeAggregate id="+getUnique().getKey()+" not unique:"+ getNotUnique().size();
    }

   
}
