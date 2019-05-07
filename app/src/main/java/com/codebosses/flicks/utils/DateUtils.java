package com.codebosses.flicks.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static boolean isAfterToday(long time) {
        Calendar today = Calendar.getInstance();
        Calendar myDate = Calendar.getInstance();

        myDate.setTimeInMillis(time);

        if (myDate.before(today)) {
            return false;
        }
        return true;
    }

}
