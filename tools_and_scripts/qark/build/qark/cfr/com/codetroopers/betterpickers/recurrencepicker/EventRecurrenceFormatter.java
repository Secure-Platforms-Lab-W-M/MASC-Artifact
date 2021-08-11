/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.text.format.DateUtils
 *  android.text.format.Time
 *  android.util.TimeFormatException
 *  com.codetroopers.betterpickers.R
 *  com.codetroopers.betterpickers.R$array
 *  com.codetroopers.betterpickers.R$plurals
 *  com.codetroopers.betterpickers.R$string
 */
package com.codetroopers.betterpickers.recurrencepicker;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.TimeFormatException;
import com.codetroopers.betterpickers.R;
import com.codetroopers.betterpickers.recurrencepicker.EventRecurrence;

public class EventRecurrenceFormatter {
    private static int[] mMonthRepeatByDayOfWeekIds;
    private static String[][] mMonthRepeatByDayOfWeekStrs;

    private static void cacheMonthRepeatStrings(Resources resources, int n) {
        int[] arrn;
        if (mMonthRepeatByDayOfWeekIds == null) {
            arrn = new int[7];
            mMonthRepeatByDayOfWeekIds = arrn;
            arrn[0] = R.array.repeat_by_nth_sun;
            EventRecurrenceFormatter.mMonthRepeatByDayOfWeekIds[1] = R.array.repeat_by_nth_mon;
            EventRecurrenceFormatter.mMonthRepeatByDayOfWeekIds[2] = R.array.repeat_by_nth_tues;
            EventRecurrenceFormatter.mMonthRepeatByDayOfWeekIds[3] = R.array.repeat_by_nth_wed;
            EventRecurrenceFormatter.mMonthRepeatByDayOfWeekIds[4] = R.array.repeat_by_nth_thurs;
            EventRecurrenceFormatter.mMonthRepeatByDayOfWeekIds[5] = R.array.repeat_by_nth_fri;
            EventRecurrenceFormatter.mMonthRepeatByDayOfWeekIds[6] = R.array.repeat_by_nth_sat;
        }
        if (mMonthRepeatByDayOfWeekStrs == null) {
            mMonthRepeatByDayOfWeekStrs = new String[7][];
        }
        if ((arrn = mMonthRepeatByDayOfWeekStrs)[n] == null) {
            arrn[n] = resources.getStringArray(mMonthRepeatByDayOfWeekIds[n]);
        }
    }

    private static String dayToString(int n, int n2) {
        return DateUtils.getDayOfWeekString((int)EventRecurrenceFormatter.dayToUtilDay(n), (int)n2);
    }

    private static int dayToUtilDay(int n) {
        if (n != 65536) {
            if (n != 131072) {
                if (n != 262144) {
                    if (n != 524288) {
                        if (n != 1048576) {
                            if (n != 2097152) {
                                if (n == 4194304) {
                                    return 7;
                                }
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("bad day argument: ");
                                stringBuilder.append(n);
                                throw new IllegalArgumentException(stringBuilder.toString());
                            }
                            return 6;
                        }
                        return 5;
                    }
                    return 4;
                }
                return 3;
            }
            return 2;
        }
        return 1;
    }

    public static String getRepeatString(Context object, Resources resources, EventRecurrence object2, boolean bl) {
        CharSequence charSequence = "";
        if (bl) {
            charSequence = new StringBuilder();
            if (object2.until != null) {
                try {
                    Time time = new Time();
                    time.parse(object2.until);
                    object = DateUtils.formatDateTime((Context)object, (long)time.toMillis(false), (int)131072);
                    charSequence.append(resources.getString(R.string.endByDate, new Object[]{object}));
                }
                catch (TimeFormatException timeFormatException) {
                    // empty catch block
                }
            }
            if (object2.count > 0) {
                charSequence.append(resources.getQuantityString(R.plurals.endByCount, object2.count, new Object[]{object2.count}));
            }
            charSequence = charSequence.toString();
        }
        int n = object2.interval <= 1 ? 1 : object2.interval;
        int n2 = object2.freq;
        if (n2 != 3) {
            if (n2 != 4) {
                if (n2 != 5) {
                    if (n2 != 6) {
                        if (n2 != 7) {
                            return null;
                        }
                        object = new StringBuilder();
                        object.append(resources.getQuantityString(R.plurals.yearly_plain, n, new Object[]{n, ""}));
                        object.append((String)charSequence);
                        return object.toString();
                    }
                    object = "";
                    if (object2.byday != null) {
                        int n3;
                        int n4 = EventRecurrence.day2CalendarDay(object2.byday[0]) - 1;
                        EventRecurrenceFormatter.cacheMonthRepeatStrings(resources, n4);
                        n2 = n3 = object2.bydayNum[0];
                        if (n3 == -1) {
                            n2 = 5;
                        }
                        object = mMonthRepeatByDayOfWeekStrs[n4][n2 - 1];
                    }
                    object2 = new StringBuilder();
                    object2.append(resources.getQuantityString(R.plurals.monthly, n, new Object[]{n, object}));
                    object2.append((String)charSequence);
                    return object2.toString();
                }
                if (object2.repeatsOnEveryWeekDay()) {
                    object = new StringBuilder();
                    object.append(resources.getString(R.string.every_weekday));
                    object.append((String)charSequence);
                    return object.toString();
                }
                n2 = 20;
                if (object2.bydayCount == 1) {
                    n2 = 10;
                }
                object = new StringBuilder();
                if (object2.bydayCount > 0) {
                    int n5 = object2.bydayCount - 1;
                    for (int i = 0; i < n5; ++i) {
                        object.append(EventRecurrenceFormatter.dayToString(object2.byday[i], n2));
                        object.append(", ");
                    }
                    object.append(EventRecurrenceFormatter.dayToString(object2.byday[n5], n2));
                    object = object.toString();
                } else {
                    if (object2.startDate == null) {
                        return null;
                    }
                    object = EventRecurrenceFormatter.dayToString(EventRecurrence.timeDay2Day(object2.startDate.weekDay), 10);
                }
                object2 = new StringBuilder();
                object2.append(resources.getQuantityString(R.plurals.weekly, n, new Object[]{n, object}));
                object2.append((String)charSequence);
                return object2.toString();
            }
            object = new StringBuilder();
            object.append(resources.getQuantityString(R.plurals.daily, n, new Object[]{n}));
            object.append((String)charSequence);
            return object.toString();
        }
        object = new StringBuilder();
        object.append(resources.getQuantityString(R.plurals.hourly, n, new Object[]{n}));
        object.append((String)charSequence);
        return object.toString();
    }
}

