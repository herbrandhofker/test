package academy.kafka.utils;

import java.util.concurrent.ThreadLocalRandom;

public class IbanGenerator {
      
  public static String ibanGenerater() {
    String result = "";
    String card = "NL";
    for (int i = 0; i < 14; i++) {
      int n = ThreadLocalRandom.current().nextInt(10) + 0;
      card += Integer.toString(n);
    }
    for (int i = 0; i < 16; i++) {
      result += card.charAt(i);
    }
    return result;
  }
}
