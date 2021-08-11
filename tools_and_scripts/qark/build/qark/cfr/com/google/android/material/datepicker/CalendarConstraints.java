/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 */
package com.google.android.material.datepicker;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.Month;
import com.google.android.material.datepicker.UtcDates;
import java.util.Arrays;

public final class CalendarConstraints
implements Parcelable {
    public static final Parcelable.Creator<CalendarConstraints> CREATOR = new Parcelable.Creator<CalendarConstraints>(){

        public CalendarConstraints createFromParcel(Parcel parcel) {
            return new CalendarConstraints((Month)parcel.readParcelable(Month.class.getClassLoader()), (Month)parcel.readParcelable(Month.class.getClassLoader()), (Month)parcel.readParcelable(Month.class.getClassLoader()), (DateValidator)parcel.readParcelable(DateValidator.class.getClassLoader()));
        }

        public CalendarConstraints[] newArray(int n) {
            return new CalendarConstraints[n];
        }
    };
    private final Month end;
    private final int monthSpan;
    private final Month openAt;
    private final Month start;
    private final DateValidator validator;
    private final int yearSpan;

    private CalendarConstraints(Month month, Month month2, Month month3, DateValidator dateValidator) {
        this.start = month;
        this.end = month2;
        this.openAt = month3;
        this.validator = dateValidator;
        if (month.compareTo(month3) <= 0) {
            if (month3.compareTo(month2) <= 0) {
                this.monthSpan = month.monthsUntil(month2) + 1;
                this.yearSpan = month2.year - month.year + 1;
                return;
            }
            throw new IllegalArgumentException("current Month cannot be after end Month");
        }
        throw new IllegalArgumentException("start Month cannot be after current Month");
    }

    static /* synthetic */ Month access$100(CalendarConstraints calendarConstraints) {
        return calendarConstraints.start;
    }

    static /* synthetic */ Month access$200(CalendarConstraints calendarConstraints) {
        return calendarConstraints.end;
    }

    static /* synthetic */ Month access$300(CalendarConstraints calendarConstraints) {
        return calendarConstraints.openAt;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof CalendarConstraints)) {
            return false;
        }
        object = (CalendarConstraints)object;
        if (this.start.equals(object.start) && this.end.equals(object.end) && this.openAt.equals(object.openAt) && this.validator.equals(object.validator)) {
            return true;
        }
        return false;
    }

    public DateValidator getDateValidator() {
        return this.validator;
    }

    Month getEnd() {
        return this.end;
    }

    int getMonthSpan() {
        return this.monthSpan;
    }

    Month getOpenAt() {
        return this.openAt;
    }

    Month getStart() {
        return this.start;
    }

    int getYearSpan() {
        return this.yearSpan;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.start, this.end, this.openAt, this.validator});
    }

    boolean isWithinBounds(long l) {
        Month month;
        if (this.start.getDay(1) <= l && l <= (month = this.end).getDay(month.daysInMonth)) {
            return true;
        }
        return false;
    }

    public void writeToParcel(Parcel parcel, int n) {
        parcel.writeParcelable((Parcelable)this.start, 0);
        parcel.writeParcelable((Parcelable)this.end, 0);
        parcel.writeParcelable((Parcelable)this.openAt, 0);
        parcel.writeParcelable((Parcelable)this.validator, 0);
    }

    public static final class Builder {
        private static final String DEEP_COPY_VALIDATOR_KEY = "DEEP_COPY_VALIDATOR_KEY";
        static final long DEFAULT_END;
        static final long DEFAULT_START;
        private long end = DEFAULT_END;
        private Long openAt;
        private long start = DEFAULT_START;
        private DateValidator validator = DateValidatorPointForward.from(Long.MIN_VALUE);

        static {
            DEFAULT_START = UtcDates.canonicalYearMonthDay(Month.create((int)1900, (int)0).timeInMillis);
            DEFAULT_END = UtcDates.canonicalYearMonthDay(Month.create((int)2100, (int)11).timeInMillis);
        }

        public Builder() {
        }

        Builder(CalendarConstraints calendarConstraints) {
            this.start = CalendarConstraints.access$100((CalendarConstraints)calendarConstraints).timeInMillis;
            this.end = CalendarConstraints.access$200((CalendarConstraints)calendarConstraints).timeInMillis;
            this.openAt = CalendarConstraints.access$300((CalendarConstraints)calendarConstraints).timeInMillis;
            this.validator = calendarConstraints.validator;
        }

        public CalendarConstraints build() {
            if (this.openAt == null) {
                long l = MaterialDatePicker.thisMonthInUtcMilliseconds();
                if (this.start > l || l > this.end) {
                    l = this.start;
                }
                this.openAt = l;
            }
            Bundle bundle = new Bundle();
            bundle.putParcelable("DEEP_COPY_VALIDATOR_KEY", (Parcelable)this.validator);
            return new CalendarConstraints(Month.create(this.start), Month.create(this.end), Month.create(this.openAt), (DateValidator)bundle.getParcelable("DEEP_COPY_VALIDATOR_KEY"));
        }

        public Builder setEnd(long l) {
            this.end = l;
            return this;
        }

        public Builder setOpenAt(long l) {
            this.openAt = l;
            return this;
        }

        public Builder setStart(long l) {
            this.start = l;
            return this;
        }

        public Builder setValidator(DateValidator dateValidator) {
            this.validator = dateValidator;
            return this;
        }
    }

    public static interface DateValidator
    extends Parcelable {
        public boolean isValid(long var1);
    }

}

