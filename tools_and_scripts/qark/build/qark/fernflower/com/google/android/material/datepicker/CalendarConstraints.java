package com.google.android.material.datepicker;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;

public final class CalendarConstraints implements Parcelable {
   public static final Creator CREATOR = new Creator() {
      public CalendarConstraints createFromParcel(Parcel var1) {
         return new CalendarConstraints((Month)var1.readParcelable(Month.class.getClassLoader()), (Month)var1.readParcelable(Month.class.getClassLoader()), (Month)var1.readParcelable(Month.class.getClassLoader()), (CalendarConstraints.DateValidator)var1.readParcelable(CalendarConstraints.DateValidator.class.getClassLoader()));
      }

      public CalendarConstraints[] newArray(int var1) {
         return new CalendarConstraints[var1];
      }
   };
   private final Month end;
   private final int monthSpan;
   private final Month openAt;
   private final Month start;
   private final CalendarConstraints.DateValidator validator;
   private final int yearSpan;

   private CalendarConstraints(Month var1, Month var2, Month var3, CalendarConstraints.DateValidator var4) {
      this.start = var1;
      this.end = var2;
      this.openAt = var3;
      this.validator = var4;
      if (var1.compareTo(var3) <= 0) {
         if (var3.compareTo(var2) <= 0) {
            this.monthSpan = var1.monthsUntil(var2) + 1;
            this.yearSpan = var2.year - var1.year + 1;
         } else {
            throw new IllegalArgumentException("current Month cannot be after end Month");
         }
      } else {
         throw new IllegalArgumentException("start Month cannot be after current Month");
      }
   }

   // $FF: synthetic method
   CalendarConstraints(Month var1, Month var2, Month var3, CalendarConstraints.DateValidator var4, Object var5) {
      this(var1, var2, var3, var4);
   }

   public int describeContents() {
      return 0;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof CalendarConstraints)) {
         return false;
      } else {
         CalendarConstraints var2 = (CalendarConstraints)var1;
         return this.start.equals(var2.start) && this.end.equals(var2.end) && this.openAt.equals(var2.openAt) && this.validator.equals(var2.validator);
      }
   }

   public CalendarConstraints.DateValidator getDateValidator() {
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

   boolean isWithinBounds(long var1) {
      if (this.start.getDay(1) <= var1) {
         Month var3 = this.end;
         if (var1 <= var3.getDay(var3.daysInMonth)) {
            return true;
         }
      }

      return false;
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeParcelable(this.start, 0);
      var1.writeParcelable(this.end, 0);
      var1.writeParcelable(this.openAt, 0);
      var1.writeParcelable(this.validator, 0);
   }

   public static final class Builder {
      private static final String DEEP_COPY_VALIDATOR_KEY = "DEEP_COPY_VALIDATOR_KEY";
      static final long DEFAULT_END;
      static final long DEFAULT_START;
      private long end;
      private Long openAt;
      private long start;
      private CalendarConstraints.DateValidator validator;

      static {
         DEFAULT_START = UtcDates.canonicalYearMonthDay(Month.create(1900, 0).timeInMillis);
         DEFAULT_END = UtcDates.canonicalYearMonthDay(Month.create(2100, 11).timeInMillis);
      }

      public Builder() {
         this.start = DEFAULT_START;
         this.end = DEFAULT_END;
         this.validator = DateValidatorPointForward.from(Long.MIN_VALUE);
      }

      Builder(CalendarConstraints var1) {
         this.start = DEFAULT_START;
         this.end = DEFAULT_END;
         this.validator = DateValidatorPointForward.from(Long.MIN_VALUE);
         this.start = var1.start.timeInMillis;
         this.end = var1.end.timeInMillis;
         this.openAt = var1.openAt.timeInMillis;
         this.validator = var1.validator;
      }

      public CalendarConstraints build() {
         if (this.openAt == null) {
            long var1 = MaterialDatePicker.thisMonthInUtcMilliseconds();
            if (this.start > var1 || var1 > this.end) {
               var1 = this.start;
            }

            this.openAt = var1;
         }

         Bundle var3 = new Bundle();
         var3.putParcelable("DEEP_COPY_VALIDATOR_KEY", this.validator);
         return new CalendarConstraints(Month.create(this.start), Month.create(this.end), Month.create(this.openAt), (CalendarConstraints.DateValidator)var3.getParcelable("DEEP_COPY_VALIDATOR_KEY"));
      }

      public CalendarConstraints.Builder setEnd(long var1) {
         this.end = var1;
         return this;
      }

      public CalendarConstraints.Builder setOpenAt(long var1) {
         this.openAt = var1;
         return this;
      }

      public CalendarConstraints.Builder setStart(long var1) {
         this.start = var1;
         return this;
      }

      public CalendarConstraints.Builder setValidator(CalendarConstraints.DateValidator var1) {
         this.validator = var1;
         return this;
      }
   }

   public interface DateValidator extends Parcelable {
      boolean isValid(long var1);
   }
}
