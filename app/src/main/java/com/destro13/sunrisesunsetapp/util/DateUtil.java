package com.destro13.sunrisesunsetapp.util;

import java.security.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public final class DateUtil {

    private DateUtil() {
        throw new AssertionError();
    }

    public static String utcToLocal(String date, String timeZoneId){
        Calendar cal = Calendar.getInstance();
        TimeZone timeZone = TimeZone.getTimeZone(timeZoneId);
        Date outDate = null;
        DateFormat localDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+00:00");
        localDateFormat.setTimeZone(timeZone);
        try {
            outDate = localDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(outDate);
        cal.setTimeZone(timeZone);
        outDate = cal.getTime();
        DateFormat outFormat = new SimpleDateFormat("HH:mm:ss");
        return outFormat.format(outDate);
    }

    public static String utcToLocal(String date){
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        Date outDate = null;
        DateFormat localDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+00:00");
        localDateFormat.setTimeZone(TimeZone.getTimeZone("PST"));
        try {
            outDate = localDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat outFormat = new SimpleDateFormat("HH:mm:ss");
        return outFormat.format(outDate);
    }
}
