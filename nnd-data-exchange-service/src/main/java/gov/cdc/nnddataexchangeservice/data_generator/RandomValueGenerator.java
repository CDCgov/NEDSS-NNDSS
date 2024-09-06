package gov.cdc.nnddataexchangeservice.data_generator;

import java.security.SecureRandom;
import java.sql.Date;

public class RandomValueGenerator {
    public static String getRandomString(SecureRandom  random) {
        int length = random.nextInt(10) + 1;  // FLAG
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append((char) (random.nextInt(26) + 'a'));  // FLAG
        }
        return sb.toString();
    }

    public static Date getRandomDate() {
        long beginTime = Date.valueOf("2000-01-01").getTime();
        long endTime = Date.valueOf("2020-12-31").getTime();
        long randomTime = beginTime + (long) (Math.random() * (endTime - beginTime));  // FLAG
        return new Date(randomTime);
    }

    public static String getRandomString(SecureRandom random, int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append((char) (random.nextInt(26) + 'a')); // FLAG
        }
        return sb.toString();
    }
}
