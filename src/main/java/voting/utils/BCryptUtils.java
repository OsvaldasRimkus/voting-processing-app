package voting.utils;

import java.util.Random;

/**
 * Created by andrius on 2/23/17.
 */

public class BCryptUtils {

    public static String generateRandomPassword(int length){
        Random randomizer = new Random();
        return randomizer.ints(48,122)
                .filter(i-> (i<57 || i>65) && (i <90 || i>97))
                .mapToObj(i -> (char) i)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }
}
