package voting.utils;

/**
 * Created by andrius on 3/7/17.
 */

public class Formatter {

    public static String formUsername(String firstName, String lastName) {
        String latinizedFirstName = firstName.toLowerCase().replace('ą', 'a');
        latinizedFirstName = latinizedFirstName.replace('č', 'c');
        latinizedFirstName = latinizedFirstName.replace('ė', 'e');
        latinizedFirstName = latinizedFirstName.replace('ū', 'u');
        String latinizedLastName = lastName.toLowerCase().replace('ą', 'a');
        latinizedLastName = latinizedLastName.replace('č', 'c');
        latinizedLastName = latinizedLastName.replace('ė', 'e');
        latinizedLastName = latinizedLastName.replace('ū', 'u');
        return latinizedFirstName.concat(".").concat(latinizedLastName);
    }
}
