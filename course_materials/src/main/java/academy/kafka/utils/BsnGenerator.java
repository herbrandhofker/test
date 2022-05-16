package academy.kafka.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class BsnGenerator {
    private static final int MAX = 999_999_998;
    private static final int MIN = 100_000_000;

    private static final long[] BSN_ONDNR_MULTIPLIERS = { 9, 8, 7, 6, 5, 4, 3, 2, -1 };

    public static boolean isGeldigBSN(Long bsn) {
        try {
            Long.valueOf(bsn);
        } catch (NumberFormatException e) {
            return false;
        }

        return isElfProef(getDigits(bsn.toString()), BSN_ONDNR_MULTIPLIERS);
    }

    private static boolean isElfProef(long[] digits, long[] multipliers) {
        return isElfProef(digits, multipliers, 0);
    }

    private static boolean isElfProef(long[] digits, long[] multipliers, long uitkomst) {
        long sum = 0;
        for (int i = 0; i < multipliers.length; i++) {
            sum += digits[i] * multipliers[i];
        }
        return sum % 11 == uitkomst;
    }

    private static long[] getDigits(String nummer) {
        long[] digits = new long[9];

        for (int i = nummer.length() - 1, j = 1; i >= 0; i--, j++) {
            digits[digits.length - j] = Character.getNumericValue(nummer.charAt(i));
        }
        return digits;
    }

   public  String generateBsn() {
       return generateRandomBsnNummers(1).iterator().next();
    }

    public static Set<String> generateRandomBsnNummers(int aantal) {
        Set<String> bsnSet = new HashSet<>();

        while (bsnSet.size() < aantal) {
            long randomNumber = ThreadLocalRandom.current().nextInt(MIN, MAX + 1);

            if (isGeldigBSN(randomNumber)) {
                bsnSet.add(String.valueOf(randomNumber));
            }
        }

        return bsnSet;
    }

    public static void main(String[] args) {
        Set<String> bsnSet = generateRandomBsnNummers(2000000);
        System.out.println(bsnSet.size());
    }
}
