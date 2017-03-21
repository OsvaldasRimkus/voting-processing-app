package voting.utils;

import java.util.Calendar;

/**
 * Created by andrius on 2/24/17.
 */

public class DateUtils {

    public static String stringifyCalendar(Calendar calendar, CharSequence delimiter) {
        StringBuilder sb = new StringBuilder();
        sb.append(calendar.get(Calendar.YEAR));
        sb.append(delimiter);
        String month = appendZero(calendar.get(Calendar.MONTH));
        sb.append(month);
        sb.append(delimiter);
        String day = appendZero(calendar.get(Calendar.DATE));
        sb.append(day);
        return sb.toString();
    }

    public static Calendar stringToCalendar(String stringer) {
        if (stringer == null || !stringer.matches("\\d{11}")) {
            return null;
        }

        String year = stringer.substring(1, 3);
        int month = Integer.parseInt(stringer.substring(3, 5));
        int day = Integer.parseInt(stringer.substring(5, 7));
        int centurySymbol = Integer.parseInt(stringer.substring(0, 1));

        year = (centurySymbol == 3 || centurySymbol == 4) ? "19" + year : "20" + year;

        Calendar.Builder builder = new Calendar.Builder();
        builder.setDate(Integer.parseInt(year), month, day);

        return builder.build();
    }

    private static String appendZero(int number) {
        return (number < 10) ? ("0" + String.valueOf(number)) : String.valueOf(number);
    }
}
