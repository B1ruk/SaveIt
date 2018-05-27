package io.start.biruk.saveit.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by biruk on 5/15/2018.
 */
public class DateUtil {
    public static String getFormattedDate(Date currentTime) {
        Calendar caland = Calendar.getInstance();
        caland.setTime(currentTime);

        int date = caland.get(Calendar.DATE);
//        int month = caland.get(Calendar.MONTH);
        int year = caland.get(Calendar.YEAR);

        String month = caland.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US);
        String format = String.format("%s %s,%s", month, date, year);

        return format;

    }
}
