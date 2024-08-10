package com.mefrreex.mines.utils;

import cn.nukkit.Player;

import java.util.concurrent.TimeUnit;

public class TimeFormatter {

    private static final String[] TIME_FORMS = {
            "time-form-day",
            "time-form-days",
            "time-form-many-days",
            "time-form-hour",
            "time-form-hours",
            "time-form-many-hours",
            "time-form-minute",
            "time-form-minutes",
            "time-form-many-minutes",
            "time-form-second",
            "time-form-seconds",
            "time-form-many-seconds"
    };

    /**
     * Get time form
     */
    private static String form(int form, Player player) {
        return Language.get(TIME_FORMS[form]);
    }

    /**
     * Formats the time in milliseconds into a string representation.
     * @param millis Time in milliseconds
     * @return String representation of the formatted time
     */
    public static String format(long millis, Player player) {
        if (millis < 1000) {
            return 0 + " " + form(11, player);
        }

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        long hours = TimeUnit.MILLISECONDS.toHours(millis) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60;

        StringBuilder formatted = new StringBuilder();
        appendTimeUnit(formatted, days, form(0, player), form(1, player), form(2, player));
        appendTimeUnit(formatted, hours, form(3, player), form(4, player), form(5, player));
        appendTimeUnit(formatted, minutes, form(6, player), form(7, player), form(8, player));
        appendTimeUnit(formatted, seconds, form(9, player), form(10, player), form(11, player));

        return formatted.toString().trim();
    }

    private static void appendTimeUnit(StringBuilder builder, long value, String form1, String form2, String form5) {
        if (value > 0) {
            builder.append(value).append(" ").append(getNounForm(value, form1, form2, form5)).append(" ");
        }
    }

    public static String getNounForm(long number, String form1, String form2, String form5) {
        long absNumber = Math.abs(number);
        if (absNumber % 100 >= 11 && absNumber % 100 <= 19) {
            return form5;
        }
        if (absNumber % 10 == 1) {
            return form1;
        }
        if (absNumber % 10 >= 2 && absNumber % 10 <= 4) {
            return form2;
        }
        return form5;
    }
}