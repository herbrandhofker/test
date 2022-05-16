package academy.kafka.utils;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

public class KentekenGenerator {
   

    private static final FakeValuesService fakeValuesService = new FakeValuesService(new Locale("nl"),
    new RandomService());

  static int tries;
    public static Set<String> generateKentekenSet(int aantal) {
        Set<String> kentekenSet = new HashSet<>();

        while (kentekenSet.size() < aantal) {
            tries++;
            kentekenSet.add(fakeValuesService.bothify("??##??", true) );            
        }
        return kentekenSet;
    }

    public static void main(String[] args) {
        Set<String> kentekenSet = generateKentekenSet(2000000);
        System.out.println(kentekenSet.size()+"  "+tries);
    }
}
