/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.codetroopers.betterpickers.recurrencepicker;

import android.content.Context;
import java.util.Calendar;

public class Utils {
    private static final String TAG = "CalUtils";
    public static final int YEAR_MAX = 2036;
    public static final int YEAR_MIN = 1970;

    public static int convertDayOfWeekFromTimeToCalendar(int n) {
        switch (n) {
            default: {
                throw new IllegalArgumentException("Argument must be between Time.SUNDAY and Time.SATURDAY");
            }
            case 6: {
                return 7;
            }
            case 5: {
                return 6;
            }
            case 4: {
                return 5;
            }
            case 3: {
                return 4;
            }
            case 2: {
                return 3;
            }
            case 1: {
                return 2;
            }
            case 0: 
        }
        return 1;
    }

    public static int getFirstDayOfWeek(Context context) {
        int n = Calendar.getInstance().getFirstDayOfWeek();
        if (n == 7) {
            return 6;
        }
        if (n == 2) {
            return 1;
        }
        return 0;
    }

    public static int getFirstDayOfWeekAsCalendar(Context context) {
        return Utils.convertDayOfWeekFromTimeToCalendar(Utils.getFirstDayOfWeek(context));
    }
}

