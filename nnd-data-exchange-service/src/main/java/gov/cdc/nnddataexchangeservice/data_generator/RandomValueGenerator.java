package gov.cdc.nnddataexchangeservice.data_generator;

import java.sql.Date;
import java.util.Random;

public class RandomValueGenerator {
    public static String getRandomString(Random random) {
        int length = random.nextInt(10) + 1; // random length between 1 and 10
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append((char) (random.nextInt(26) + 'a'));
        }
        return sb.toString();
    }

    public static Date getRandomDate() {
        long beginTime = Date.valueOf("2000-01-01").getTime();
        long endTime = Date.valueOf("2020-12-31").getTime();
        long randomTime = beginTime + (long) (Math.random() * (endTime - beginTime));
        return new Date(randomTime);
    }

    public static String getRandomString(Random random, int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append((char) (random.nextInt(26) + 'a'));
        }
        return sb.toString();
    }
}
