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

    public static String getMovieTime(int time) {
        int hours = time / 60; //since both are ints, you get an int
        int minutes = time % 60;
        return hours + "h " + minutes + "m";
    }

    public static String convertSecondsToHMmSs(long milliseconds) {
        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
//        int hours   = (int) ((milliseconds / (1000*60*60)) % 24);
        if (minutes == 0)
            return seconds + "s";
        else
            return minutes + "m " + seconds + "s";
    }

}