/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.google.android.material.datepicker;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.material.datepicker.UtcDates;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

final class Month
implements Comparable<Month>,
Parcelable {
    public static final Parcelable.Creator<Month> CREATOR = new Parcelable.Creator<Month>(){

        public Month createFromParcel(Parcel parcel) {
            return Month.create(parcel.readInt(), parcel.readInt());
        }

        public Month[] newArray(int n) {
            return new Month[n];
        }
    };
    final int daysInMonth;
    final int daysInWeek;
    private final Calendar firstOfMonth;
    private final String longName;
    final int month;
    final long timeInMillis;
    final int year;

    private Month(Calendar calendar) {
        calendar.set(5, 1);
        this.firstOfMonth = calendar = UtcDates.getDayCopy(calendar);
        this.month = calendar.get(2);
        this.year = this.firstOfMonth.get(1);
        this.daysInWeek = this.firstOfMonth.getMaximum(7);
        this.daysInMonth = this.firstOfMonth.getActualMaximum(5);
        this.longName = UtcDates.getYearMonthFormat().format(this.firstOfMonth.getTime());
        this.timeInMillis = this.firstOfMonth.getTimeInMillis();
    }

    static Month create(int n, int n2) {
        Calendar calendar = UtcDates.getUtcCalendar();
        calendar.set(1, n);
        calendar.set(2, n2);
        return new Month(calendar);
    }

    static Month create(long l) {
        Calendar calendar = UtcDates.getUtcCalendar();
        calendar.setTimeInMillis(l);
        return new Month(calendar);
    }

    static Month today() {
        return new Month(UtcDates.getTodayCalendar());
    }

    @Override
    public int compareTo(Month month) {
        return this.firstOfMonth.compareTo(month.firstOfMonth);
    }

    int daysFromStartOfWeekToFirstOfMonth() {
        int n;
        int n2 = n = this.firstOfMonth.get(7) - this.firstOfMonth.getFirstDayOfWeek();
        if (n < 0) {
            n2 = n + this.daysInWeek;
        }
        return n2;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Month)) {
            return false;
        }
        object = (Month)object;
        if (this.month == object.month && this.year == object.year) {
            return true;
        }
        return false;
    }

    long getDay(int n) {
        Calendar calendar = UtcDates.getDayCopy(this.firstOfMonth);
        calendar.set(5, n);
        return calendar.getTimeInMillis();
    }

    String getLongName() {
        return this.longName;
    }

    long getStableId() {
        return this.firstOfMonth.getTimeInMillis();
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.month, this.year});
    }

    Month monthsLater(int n) {
        Calendar calendar = UtcDates.getDayCopy(this.firstOfMonth);
        calendar.add(2, n);
        return new Month(calendar);
    }

    int monthsUntil(Month month) {
        if (this.firstOfMonth instanceof GregorianCalendar) {
            return (month.year - this.year) * 12 + (month.month - this.month);
        }
        throw new IllegalArgumentException("Only Gregorian calendars are supported.");
    }

    public void writeToParcel(Parcel parcel, int n) {
        parcel.writeInt(this.year);
        parcel.writeInt(this.month);
    }

}

