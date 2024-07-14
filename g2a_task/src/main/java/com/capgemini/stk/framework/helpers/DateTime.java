package com.capgemini.stk.framework.helpers;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public final class DateTime {
    private DateTime() {
    }

    public static LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.now();
    }

    public static String formatLocalDateTime(LocalDateTime localDateTime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(formatter);
    }

    public static String getCurrentLocalDateTime(String format) {
        return formatLocalDateTime(getCurrentLocalDateTime(), format);
    }

    public static String getCurrentTime() {
        return getCurrentLocalDateTime("HH:mm");
    }

    public static String getCurrentDate() {
        return getCurrentLocalDateTime("dd.MM.yyyy");
    }

    public static String getDate(int years, int months, int days) {
        return getDate(years, months, days, "dd.MM.yyyy");
    }

    public static String getDate(int years, int months, int days, String format) {
        return getDateTime(years, months, days, 0, 0, 0, format);
    }

    public static String getDateTime(int years, int months, int days, int hours, int minutes, int seconds, String format) {
        LocalDateTime date = getDateTime(years, months, days, hours, minutes, seconds);
        return formatLocalDateTime(date, format);
    }

    public static LocalDateTime getDateTime(int years, int months, int days, int hours, int minutes, int seconds) {
        LocalDateTime date = getCurrentLocalDateTime();
        date = date.plusYears(years);
        date = date.plusMonths(months);
        date = date.plusDays(days);
        date = date.plusHours(hours);
        date = date.plusMinutes(minutes);
        date = date.plusSeconds(seconds);
        return date;
    }

    public static String getTime(int hours, int minutes, int seconds, String format) {
        return getDateTime(0, 0, 0, hours, minutes, seconds, format);
    }

    public static boolean isWeekend() {
        return isWeekend(getCurrentLocalDateTime());
    }

    public static boolean isWeekend(int years, int months, int days, int hours, int minutes, int seconds) {
        return isWeekend(getDateTime(years, months, days, hours, minutes, seconds));
    }

    public static boolean isWeekend(LocalDateTime dateTime) {
        List<DayOfWeek> weekendDaysList = Arrays.asList(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);
        return weekendDaysList.contains(dateTime.getDayOfWeek());
    }

    public static String getNextFullHour(Integer hoursToAdd) {
        LocalDateTime next = getCurrentLocalDateTime().plusHours(hoursToAdd).withMinute(0);
        return formatLocalDateTime(next, "HH:mm");
    }

    public static String addTimePeriodToCurrentFullHour(Integer hoursToAdd, Integer minutesToSet) {
        // For example add 1:30h to 9:36 gives 10:30
        LocalDateTime next = getCurrentLocalDateTime().plusHours(hoursToAdd).withMinute(minutesToSet);
        return formatLocalDateTime(next, "HH:mm");
    }

    public static String getTimeRoundedToNextDecimalMinutes(String timeToRounded) {
        LocalTime timeToRoundedAsTimeObject = LocalTime.parse(timeToRounded, DateTimeFormatter.ofPattern("HH:mm"));
        return timeToRoundedAsTimeObject.plusMinutes(10 - timeToRoundedAsTimeObject.getMinute() % 10).toString();
    }

    public static Integer getCurrentMinutes() {
        String currTime = getCurrentTime();
        return Integer.parseInt(currTime.substring(currTime.length() - 1));
    }

    public static String getCurrentTimestamp() {
        return String.valueOf(System.currentTimeMillis() / 1000L);
    }

    public static String getTimeFromMilliseconds(long milliseconds) {
        long   millis = milliseconds % 1000;
        long   second = (milliseconds / 1000) % 60;
        long   minute = (milliseconds / (1000 * 60)) % 60;
        long   hour   = (milliseconds / (1000 * 60 * 60)) % 24;
        String result = "";
        if (hour > 0) {
            result += hour + "h ";
        }
        if (minute > 0) {
            result += minute + "m ";
        }
        if (second > 0) {
            result += second + "s ";
        }
        if (hour < 1 && minute < 1 && second < 1) {
            result += millis + "ms";
        }

        return result.trim();
    }
}