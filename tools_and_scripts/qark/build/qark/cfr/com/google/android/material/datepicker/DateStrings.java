/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 */
package com.google.android.material.datepicker;

import android.os.Build;
import androidx.core.util.Pair;
import com.google.android.material.datepicker.UtcDates;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

class DateStrings {
    private DateStrings() {
    }

    static Pair<String, String> getDateRangeString(Long l, Long l2) {
        return DateStrings.getDateRangeString(l, l2, null);
    }

    static Pair<String, String> getDateRangeString(Long l, Long l2, SimpleDateFormat simpleDateFormat) {
        if (l == null && l2 == null) {
            return Pair.create(null, null);
        }
        if (l == null) {
            return Pair.create(null, DateStrings.getDateString(l2, simpleDateFormat));
        }
        if (l2 == null) {
            return Pair.create(DateStrings.getDateString(l, simpleDateFormat), null);
        }
        Calendar calendar = UtcDates.getTodayCalendar();
        Calendar calendar2 = UtcDates.getUtcCalendar();
        calendar2.setTimeInMillis(l);
        Calendar calendar3 = UtcDates.getUtcCalendar();
        calendar3.setTimeInMillis(l2);
        if (simpleDateFormat != null) {
            l = new Date(l);
            l2 = new Date(l2);
            return Pair.create(simpleDateFormat.format((Date)((Object)l)), simpleDateFormat.format((Date)((Object)l2)));
        }
        if (calendar2.get(1) == calendar3.get(1)) {
            if (calendar2.get(1) == calendar.get(1)) {
                return Pair.create(DateStrings.getMonthDay(l, Locale.getDefault()), DateStrings.getMonthDay(l2, Locale.getDefault()));
            }
            return Pair.create(DateStrings.getMonthDay(l, Locale.getDefault()), DateStrings.getYearMonthDay(l2, Locale.getDefault()));
        }
        return Pair.create(DateStrings.getYearMonthDay(l, Locale.getDefault()), DateStrings.getYearMonthDay(l2, Locale.getDefault()));
    }

    static String getDateString(long l) {
        return DateStrings.getDateString(l, null);
    }

    static String getDateString(long l, SimpleDateFormat simpleDateFormat) {
        Calendar calendar = UtcDates.getTodayCalendar();
        Calendar calendar2 = UtcDates.getUtcCalendar();
        calendar2.setTimeInMillis(l);
        if (simpleDateFormat != null) {
            return simpleDateFormat.format(new Date(l));
        }
        if (calendar.get(1) == calendar2.get(1)) {
            return DateStrings.getMonthDay(l);
        }
        return DateStrings.getYearMonthDay(l);
    }

    static String getMonthDay(long l) {
        return DateStrings.getMonthDay(l, Locale.getDefault());
    }

    static String getMonthDay(long l, Locale locale) {
        if (Build.VERSION.SDK_INT >= 24) {
            return UtcDates.getAbbrMonthDayFormat(locale).format(new Date(l));
        }
        return UtcDates.getMediumNoYear(locale).format(new Date(l));
    }

    static String getMonthDayOfWeekDay(long l) {
        return DateStrings.getMonthDayOfWeekDay(l, Locale.getDefault());
    }

    static String getMonthDayOfWeekDay(long l, Locale locale) {
        if (Build.VERSION.SDK_INT >= 24) {
            return UtcDates.getAbbrMonthWeekdayDayFormat(locale).format(new Date(l));
        }
        return UtcDates.getFullFormat(locale).format(new Date(l));
    }

    static String getYearMonthDay(long l) {
        return DateStrings.getYearMonthDay(l, Locale.getDefault());
    }

    static String getYearMonthDay(long l, Locale locale) {
        if (Build.VERSION.SDK_INT >= 24) {
            return UtcDates.getYearAbbrMonthDayFormat(locale).format(new Date(l));
        }
        return UtcDates.getMediumFormat(locale).format(new Date(l));
    }

    static String getYearMonthDayOfWeekDay(long l) {
        return DateStrings.getYearMonthDayOfWeekDay(l, Locale.getDefault());
    }

    static String getYearMonthDayOfWeekDay(long l, Locale locale) {
        if (Build.VERSION.SDK_INT >= 24) {
            return UtcDates.getYearAbbrMonthWeekdayDayFormat(locale).format(new Date(l));
        }
        return UtcDates.getFullFormat(locale).format(new Date(l));
    }
}

