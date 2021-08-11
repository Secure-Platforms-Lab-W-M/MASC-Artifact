/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.joda.convert.FromString
 *  org.joda.time.Chronology
 *  org.joda.time.DateTimeFieldType
 *  org.joda.time.DateTimeUtils
 *  org.joda.time.DurationFieldType
 *  org.joda.time.PeriodType
 *  org.joda.time.chrono.ISOChronology
 *  org.joda.time.field.FieldUtils
 *  org.joda.time.format.ISOPeriodFormat
 *  org.joda.time.format.PeriodFormatter
 */
package org.joda.time;

import java.io.Serializable;
import org.joda.convert.FromString;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.Days;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.PeriodType;
import org.joda.time.ReadableDuration;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.Seconds;
import org.joda.time.Weeks;
import org.joda.time.base.BasePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;

public final class Period
extends BasePeriod
implements ReadablePeriod,
Serializable {
    public static final Period ZERO = new Period();
    private static final long serialVersionUID = 741052353876488155L;

    public Period() {
        super(0L, (PeriodType)null, (Chronology)null);
    }

    public Period(int n, int n2, int n3, int n4) {
        super(0, 0, 0, 0, n, n2, n3, n4, PeriodType.standard());
    }

    public Period(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        super(n, n2, n3, n4, n5, n6, n7, n8, PeriodType.standard());
    }

    public Period(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, PeriodType periodType) {
        super(n, n2, n3, n4, n5, n6, n7, n8, periodType);
    }

    public Period(long l) {
        super(l);
    }

    public Period(long l, long l2) {
        super(l, l2, null, null);
    }

    public Period(long l, long l2, Chronology chronology) {
        super(l, l2, null, chronology);
    }

    public Period(long l, long l2, PeriodType periodType) {
        super(l, l2, periodType, null);
    }

    public Period(long l, long l2, PeriodType periodType, Chronology chronology) {
        super(l, l2, periodType, chronology);
    }

    public Period(long l, Chronology chronology) {
        super(l, (PeriodType)null, chronology);
    }

    public Period(long l, PeriodType periodType) {
        super(l, periodType, (Chronology)null);
    }

    public Period(long l, PeriodType periodType, Chronology chronology) {
        super(l, periodType, chronology);
    }

    public Period(Object object) {
        super(object, null, null);
    }

    public Period(Object object, Chronology chronology) {
        super(object, null, chronology);
    }

    public Period(Object object, PeriodType periodType) {
        super(object, periodType, null);
    }

    public Period(Object object, PeriodType periodType, Chronology chronology) {
        super(object, periodType, chronology);
    }

    public Period(ReadableDuration readableDuration, ReadableInstant readableInstant) {
        super(readableDuration, readableInstant, null);
    }

    public Period(ReadableDuration readableDuration, ReadableInstant readableInstant, PeriodType periodType) {
        super(readableDuration, readableInstant, periodType);
    }

    public Period(ReadableInstant readableInstant, ReadableDuration readableDuration) {
        super(readableInstant, readableDuration, null);
    }

    public Period(ReadableInstant readableInstant, ReadableDuration readableDuration, PeriodType periodType) {
        super(readableInstant, readableDuration, periodType);
    }

    public Period(ReadableInstant readableInstant, ReadableInstant readableInstant2) {
        super(readableInstant, readableInstant2, null);
    }

    public Period(ReadableInstant readableInstant, ReadableInstant readableInstant2, PeriodType periodType) {
        super(readableInstant, readableInstant2, periodType);
    }

    public Period(ReadablePartial readablePartial, ReadablePartial readablePartial2) {
        super(readablePartial, readablePartial2, null);
    }

    public Period(ReadablePartial readablePartial, ReadablePartial readablePartial2, PeriodType periodType) {
        super(readablePartial, readablePartial2, periodType);
    }

    private Period(int[] arrn, PeriodType periodType) {
        super(arrn, periodType);
    }

    private void checkYearsAndMonths(String string) {
        if (this.getMonths() == 0) {
            if (this.getYears() == 0) {
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Cannot convert to ");
            stringBuilder.append(string);
            stringBuilder.append(" as this period contains years and years vary in length");
            throw new UnsupportedOperationException(stringBuilder.toString());
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot convert to ");
        stringBuilder.append(string);
        stringBuilder.append(" as this period contains months and months vary in length");
        throw new UnsupportedOperationException(stringBuilder.toString());
    }

    public static Period days(int n) {
        PeriodType periodType = PeriodType.standard();
        return new Period(new int[]{0, 0, 0, n, 0, 0, 0, 0}, periodType);
    }

    public static Period fieldDifference(ReadablePartial readablePartial, ReadablePartial readablePartial2) {
        if (readablePartial != null && readablePartial2 != null) {
            if (readablePartial.size() == readablePartial2.size()) {
                DurationFieldType[] arrdurationFieldType = new DurationFieldType[readablePartial.size()];
                int[] arrn = new int[readablePartial.size()];
                int n = readablePartial.size();
                for (int i = 0; i < n; ++i) {
                    if (readablePartial.getFieldType(i) == readablePartial2.getFieldType(i)) {
                        arrdurationFieldType[i] = readablePartial.getFieldType(i).getDurationType();
                        if (i > 0 && arrdurationFieldType[i - 1] == arrdurationFieldType[i]) {
                            throw new IllegalArgumentException("ReadablePartial objects must not have overlapping fields");
                        }
                        arrn[i] = readablePartial2.getValue(i) - readablePartial.getValue(i);
                        continue;
                    }
                    throw new IllegalArgumentException("ReadablePartial objects must have the same set of fields");
                }
                return new Period(arrn, PeriodType.forFields((DurationFieldType[])arrdurationFieldType));
            }
            throw new IllegalArgumentException("ReadablePartial objects must have the same set of fields");
        }
        throw new IllegalArgumentException("ReadablePartial objects must not be null");
    }

    public static Period hours(int n) {
        PeriodType periodType = PeriodType.standard();
        return new Period(new int[]{0, 0, 0, 0, n, 0, 0, 0}, periodType);
    }

    public static Period millis(int n) {
        PeriodType periodType = PeriodType.standard();
        return new Period(new int[]{0, 0, 0, 0, 0, 0, 0, n}, periodType);
    }

    public static Period minutes(int n) {
        PeriodType periodType = PeriodType.standard();
        return new Period(new int[]{0, 0, 0, 0, 0, n, 0, 0}, periodType);
    }

    public static Period months(int n) {
        PeriodType periodType = PeriodType.standard();
        return new Period(new int[]{0, n, 0, 0, 0, 0, 0, 0}, periodType);
    }

    @FromString
    public static Period parse(String string) {
        return Period.parse(string, ISOPeriodFormat.standard());
    }

    public static Period parse(String string, PeriodFormatter periodFormatter) {
        return periodFormatter.parsePeriod(string);
    }

    public static Period seconds(int n) {
        PeriodType periodType = PeriodType.standard();
        return new Period(new int[]{0, 0, 0, 0, 0, 0, n, 0}, periodType);
    }

    public static Period weeks(int n) {
        PeriodType periodType = PeriodType.standard();
        return new Period(new int[]{0, 0, n, 0, 0, 0, 0, 0}, periodType);
    }

    public static Period years(int n) {
        PeriodType periodType = PeriodType.standard();
        return new Period(new int[]{n, 0, 0, 0, 0, 0, 0, 0, 0}, periodType);
    }

    public int getDays() {
        return this.getPeriodType().getIndexedField((ReadablePeriod)this, PeriodType.DAY_INDEX);
    }

    public int getHours() {
        return this.getPeriodType().getIndexedField((ReadablePeriod)this, PeriodType.HOUR_INDEX);
    }

    public int getMillis() {
        return this.getPeriodType().getIndexedField((ReadablePeriod)this, PeriodType.MILLI_INDEX);
    }

    public int getMinutes() {
        return this.getPeriodType().getIndexedField((ReadablePeriod)this, PeriodType.MINUTE_INDEX);
    }

    public int getMonths() {
        return this.getPeriodType().getIndexedField((ReadablePeriod)this, PeriodType.MONTH_INDEX);
    }

    public int getSeconds() {
        return this.getPeriodType().getIndexedField((ReadablePeriod)this, PeriodType.SECOND_INDEX);
    }

    public int getWeeks() {
        return this.getPeriodType().getIndexedField((ReadablePeriod)this, PeriodType.WEEK_INDEX);
    }

    public int getYears() {
        return this.getPeriodType().getIndexedField((ReadablePeriod)this, PeriodType.YEAR_INDEX);
    }

    public Period minus(ReadablePeriod readablePeriod) {
        if (readablePeriod == null) {
            return this;
        }
        int[] arrn = this.getValues();
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.YEAR_INDEX, arrn, - readablePeriod.get(DurationFieldType.YEARS_TYPE));
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.MONTH_INDEX, arrn, - readablePeriod.get(DurationFieldType.MONTHS_TYPE));
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.WEEK_INDEX, arrn, - readablePeriod.get(DurationFieldType.WEEKS_TYPE));
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.DAY_INDEX, arrn, - readablePeriod.get(DurationFieldType.DAYS_TYPE));
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.HOUR_INDEX, arrn, - readablePeriod.get(DurationFieldType.HOURS_TYPE));
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.MINUTE_INDEX, arrn, - readablePeriod.get(DurationFieldType.MINUTES_TYPE));
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.SECOND_INDEX, arrn, - readablePeriod.get(DurationFieldType.SECONDS_TYPE));
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.MILLI_INDEX, arrn, - readablePeriod.get(DurationFieldType.MILLIS_TYPE));
        return new Period(arrn, this.getPeriodType());
    }

    public Period minusDays(int n) {
        return this.plusDays(- n);
    }

    public Period minusHours(int n) {
        return this.plusHours(- n);
    }

    public Period minusMillis(int n) {
        return this.plusMillis(- n);
    }

    public Period minusMinutes(int n) {
        return this.plusMinutes(- n);
    }

    public Period minusMonths(int n) {
        return this.plusMonths(- n);
    }

    public Period minusSeconds(int n) {
        return this.plusSeconds(- n);
    }

    public Period minusWeeks(int n) {
        return this.plusWeeks(- n);
    }

    public Period minusYears(int n) {
        return this.plusYears(- n);
    }

    public Period multipliedBy(int n) {
        if (this != ZERO) {
            if (n == 1) {
                return this;
            }
            int[] arrn = this.getValues();
            for (int i = 0; i < arrn.length; ++i) {
                arrn[i] = FieldUtils.safeMultiply((int)arrn[i], (int)n);
            }
            return new Period(arrn, this.getPeriodType());
        }
        return this;
    }

    public Period negated() {
        return this.multipliedBy(-1);
    }

    public Period normalizedStandard() {
        return this.normalizedStandard(PeriodType.standard());
    }

    public Period normalizedStandard(PeriodType object) {
        PeriodType periodType = DateTimeUtils.getPeriodType((PeriodType)object);
        object = new Period((long)this.getMillis() + (long)this.getSeconds() * 1000L + (long)this.getMinutes() * 60000L + (long)this.getHours() * 3600000L + (long)this.getDays() * 86400000L + (long)this.getWeeks() * 604800000L, periodType, (Chronology)ISOChronology.getInstanceUTC());
        int n = this.getYears();
        int n2 = this.getMonths();
        if (n == 0 && n2 == 0) {
            return object;
        }
        long l = (long)n * 12L + (long)n2;
        if (periodType.isSupported(DurationFieldType.YEARS_TYPE)) {
            n = FieldUtils.safeToInt((long)(l / 12L));
            object = object.withYears(n);
            l -= (long)(n * 12);
        }
        if (periodType.isSupported(DurationFieldType.MONTHS_TYPE)) {
            n = FieldUtils.safeToInt((long)l);
            object = object.withMonths(n);
            l -= (long)n;
        }
        if (l == 0L) {
            return object;
        }
        object = new StringBuilder();
        object.append("Unable to normalize as PeriodType is missing either years or months but period has a month/year amount: ");
        object.append(this.toString());
        throw new UnsupportedOperationException(object.toString());
    }

    public Period plus(ReadablePeriod readablePeriod) {
        if (readablePeriod == null) {
            return this;
        }
        int[] arrn = this.getValues();
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.YEAR_INDEX, arrn, readablePeriod.get(DurationFieldType.YEARS_TYPE));
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.MONTH_INDEX, arrn, readablePeriod.get(DurationFieldType.MONTHS_TYPE));
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.WEEK_INDEX, arrn, readablePeriod.get(DurationFieldType.WEEKS_TYPE));
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.DAY_INDEX, arrn, readablePeriod.get(DurationFieldType.DAYS_TYPE));
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.HOUR_INDEX, arrn, readablePeriod.get(DurationFieldType.HOURS_TYPE));
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.MINUTE_INDEX, arrn, readablePeriod.get(DurationFieldType.MINUTES_TYPE));
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.SECOND_INDEX, arrn, readablePeriod.get(DurationFieldType.SECONDS_TYPE));
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.MILLI_INDEX, arrn, readablePeriod.get(DurationFieldType.MILLIS_TYPE));
        return new Period(arrn, this.getPeriodType());
    }

    public Period plusDays(int n) {
        if (n == 0) {
            return this;
        }
        int[] arrn = this.getValues();
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.DAY_INDEX, arrn, n);
        return new Period(arrn, this.getPeriodType());
    }

    public Period plusHours(int n) {
        if (n == 0) {
            return this;
        }
        int[] arrn = this.getValues();
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.HOUR_INDEX, arrn, n);
        return new Period(arrn, this.getPeriodType());
    }

    public Period plusMillis(int n) {
        if (n == 0) {
            return this;
        }
        int[] arrn = this.getValues();
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.MILLI_INDEX, arrn, n);
        return new Period(arrn, this.getPeriodType());
    }

    public Period plusMinutes(int n) {
        if (n == 0) {
            return this;
        }
        int[] arrn = this.getValues();
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.MINUTE_INDEX, arrn, n);
        return new Period(arrn, this.getPeriodType());
    }

    public Period plusMonths(int n) {
        if (n == 0) {
            return this;
        }
        int[] arrn = this.getValues();
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.MONTH_INDEX, arrn, n);
        return new Period(arrn, this.getPeriodType());
    }

    public Period plusSeconds(int n) {
        if (n == 0) {
            return this;
        }
        int[] arrn = this.getValues();
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.SECOND_INDEX, arrn, n);
        return new Period(arrn, this.getPeriodType());
    }

    public Period plusWeeks(int n) {
        if (n == 0) {
            return this;
        }
        int[] arrn = this.getValues();
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.WEEK_INDEX, arrn, n);
        return new Period(arrn, this.getPeriodType());
    }

    public Period plusYears(int n) {
        if (n == 0) {
            return this;
        }
        int[] arrn = this.getValues();
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.YEAR_INDEX, arrn, n);
        return new Period(arrn, this.getPeriodType());
    }

    @Override
    public Period toPeriod() {
        return this;
    }

    public Days toStandardDays() {
        this.checkYearsAndMonths("Days");
        return Days.days(FieldUtils.safeToInt((long)FieldUtils.safeAdd((long)FieldUtils.safeAdd((long)(((long)this.getMillis() + (long)this.getSeconds() * 1000L + (long)this.getMinutes() * 60000L + (long)this.getHours() * 3600000L) / 86400000L), (long)this.getDays()), (long)((long)this.getWeeks() * 7L))));
    }

    public Duration toStandardDuration() {
        this.checkYearsAndMonths("Duration");
        return new Duration((long)this.getMillis() + (long)this.getSeconds() * 1000L + (long)this.getMinutes() * 60000L + (long)this.getHours() * 3600000L + (long)this.getDays() * 86400000L + (long)this.getWeeks() * 604800000L);
    }

    public Hours toStandardHours() {
        this.checkYearsAndMonths("Hours");
        return Hours.hours(FieldUtils.safeToInt((long)FieldUtils.safeAdd((long)FieldUtils.safeAdd((long)FieldUtils.safeAdd((long)(((long)this.getMillis() + (long)this.getSeconds() * 1000L + (long)this.getMinutes() * 60000L) / 3600000L), (long)this.getHours()), (long)((long)this.getDays() * 24L)), (long)((long)this.getWeeks() * 168L))));
    }

    public Minutes toStandardMinutes() {
        this.checkYearsAndMonths("Minutes");
        return Minutes.minutes(FieldUtils.safeToInt((long)FieldUtils.safeAdd((long)FieldUtils.safeAdd((long)FieldUtils.safeAdd((long)FieldUtils.safeAdd((long)(((long)this.getMillis() + (long)this.getSeconds() * 1000L) / 60000L), (long)this.getMinutes()), (long)((long)this.getHours() * 60L)), (long)((long)this.getDays() * 1440L)), (long)((long)this.getWeeks() * 10080L))));
    }

    public Seconds toStandardSeconds() {
        this.checkYearsAndMonths("Seconds");
        return Seconds.seconds(FieldUtils.safeToInt((long)FieldUtils.safeAdd((long)FieldUtils.safeAdd((long)FieldUtils.safeAdd((long)FieldUtils.safeAdd((long)FieldUtils.safeAdd((long)(this.getMillis() / 1000), (long)this.getSeconds()), (long)((long)this.getMinutes() * 60L)), (long)((long)this.getHours() * 3600L)), (long)((long)this.getDays() * 86400L)), (long)((long)this.getWeeks() * 604800L))));
    }

    public Weeks toStandardWeeks() {
        this.checkYearsAndMonths("Weeks");
        long l = this.getMillis();
        long l2 = this.getSeconds();
        long l3 = this.getMinutes();
        long l4 = this.getHours();
        long l5 = this.getDays();
        return Weeks.weeks(FieldUtils.safeToInt((long)((long)this.getWeeks() + (l + l2 * 1000L + l3 * 60000L + l4 * 3600000L + l5 * 86400000L) / 604800000L)));
    }

    public Period withDays(int n) {
        int[] arrn = this.getValues();
        this.getPeriodType().setIndexedField((ReadablePeriod)this, PeriodType.DAY_INDEX, arrn, n);
        return new Period(arrn, this.getPeriodType());
    }

    public Period withField(DurationFieldType durationFieldType, int n) {
        if (durationFieldType != null) {
            int[] arrn = this.getValues();
            super.setFieldInto(arrn, durationFieldType, n);
            return new Period(arrn, this.getPeriodType());
        }
        throw new IllegalArgumentException("Field must not be null");
    }

    public Period withFieldAdded(DurationFieldType durationFieldType, int n) {
        if (durationFieldType != null) {
            if (n == 0) {
                return this;
            }
            int[] arrn = this.getValues();
            super.addFieldInto(arrn, durationFieldType, n);
            return new Period(arrn, this.getPeriodType());
        }
        throw new IllegalArgumentException("Field must not be null");
    }

    public Period withFields(ReadablePeriod readablePeriod) {
        if (readablePeriod == null) {
            return this;
        }
        return new Period(super.mergePeriodInto(this.getValues(), readablePeriod), this.getPeriodType());
    }

    public Period withHours(int n) {
        int[] arrn = this.getValues();
        this.getPeriodType().setIndexedField((ReadablePeriod)this, PeriodType.HOUR_INDEX, arrn, n);
        return new Period(arrn, this.getPeriodType());
    }

    public Period withMillis(int n) {
        int[] arrn = this.getValues();
        this.getPeriodType().setIndexedField((ReadablePeriod)this, PeriodType.MILLI_INDEX, arrn, n);
        return new Period(arrn, this.getPeriodType());
    }

    public Period withMinutes(int n) {
        int[] arrn = this.getValues();
        this.getPeriodType().setIndexedField((ReadablePeriod)this, PeriodType.MINUTE_INDEX, arrn, n);
        return new Period(arrn, this.getPeriodType());
    }

    public Period withMonths(int n) {
        int[] arrn = this.getValues();
        this.getPeriodType().setIndexedField((ReadablePeriod)this, PeriodType.MONTH_INDEX, arrn, n);
        return new Period(arrn, this.getPeriodType());
    }

    public Period withPeriodType(PeriodType periodType) {
        if ((periodType = DateTimeUtils.getPeriodType((PeriodType)periodType)).equals((Object)this.getPeriodType())) {
            return this;
        }
        return new Period((Object)this, periodType);
    }

    public Period withSeconds(int n) {
        int[] arrn = this.getValues();
        this.getPeriodType().setIndexedField((ReadablePeriod)this, PeriodType.SECOND_INDEX, arrn, n);
        return new Period(arrn, this.getPeriodType());
    }

    public Period withWeeks(int n) {
        int[] arrn = this.getValues();
        this.getPeriodType().setIndexedField((ReadablePeriod)this, PeriodType.WEEK_INDEX, arrn, n);
        return new Period(arrn, this.getPeriodType());
    }

    public Period withYears(int n) {
        int[] arrn = this.getValues();
        this.getPeriodType().setIndexedField((ReadablePeriod)this, PeriodType.YEAR_INDEX, arrn, n);
        return new Period(arrn, this.getPeriodType());
    }
}

