// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.joda.time;

import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.PeriodFormatter;
import org.joda.convert.FromString;
import org.joda.time.format.ISOPeriodFormat;
import java.io.Serializable;
import org.joda.time.base.BasePeriod;

public final class Period extends BasePeriod implements ReadablePeriod, Serializable
{
    public static final Period ZERO;
    private static final long serialVersionUID = 741052353876488155L;
    
    static {
        ZERO = new Period();
    }
    
    public Period() {
        super(0L, null, null);
    }
    
    public Period(final int n, final int n2, final int n3, final int n4) {
        super(0, 0, 0, 0, n, n2, n3, n4, PeriodType.standard());
    }
    
    public Period(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8) {
        super(n, n2, n3, n4, n5, n6, n7, n8, PeriodType.standard());
    }
    
    public Period(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8, final PeriodType periodType) {
        super(n, n2, n3, n4, n5, n6, n7, n8, periodType);
    }
    
    public Period(final long n) {
        super(n);
    }
    
    public Period(final long n, final long n2) {
        super(n, n2, null, null);
    }
    
    public Period(final long n, final long n2, final Chronology chronology) {
        super(n, n2, null, chronology);
    }
    
    public Period(final long n, final long n2, final PeriodType periodType) {
        super(n, n2, periodType, null);
    }
    
    public Period(final long n, final long n2, final PeriodType periodType, final Chronology chronology) {
        super(n, n2, periodType, chronology);
    }
    
    public Period(final long n, final Chronology chronology) {
        super(n, null, chronology);
    }
    
    public Period(final long n, final PeriodType periodType) {
        super(n, periodType, null);
    }
    
    public Period(final long n, final PeriodType periodType, final Chronology chronology) {
        super(n, periodType, chronology);
    }
    
    public Period(final Object o) {
        super(o, null, null);
    }
    
    public Period(final Object o, final Chronology chronology) {
        super(o, null, chronology);
    }
    
    public Period(final Object o, final PeriodType periodType) {
        super(o, periodType, null);
    }
    
    public Period(final Object o, final PeriodType periodType, final Chronology chronology) {
        super(o, periodType, chronology);
    }
    
    public Period(final ReadableDuration readableDuration, final ReadableInstant readableInstant) {
        super(readableDuration, readableInstant, null);
    }
    
    public Period(final ReadableDuration readableDuration, final ReadableInstant readableInstant, final PeriodType periodType) {
        super(readableDuration, readableInstant, periodType);
    }
    
    public Period(final ReadableInstant readableInstant, final ReadableDuration readableDuration) {
        super(readableInstant, readableDuration, null);
    }
    
    public Period(final ReadableInstant readableInstant, final ReadableDuration readableDuration, final PeriodType periodType) {
        super(readableInstant, readableDuration, periodType);
    }
    
    public Period(final ReadableInstant readableInstant, final ReadableInstant readableInstant2) {
        super(readableInstant, readableInstant2, null);
    }
    
    public Period(final ReadableInstant readableInstant, final ReadableInstant readableInstant2, final PeriodType periodType) {
        super(readableInstant, readableInstant2, periodType);
    }
    
    public Period(final ReadablePartial readablePartial, final ReadablePartial readablePartial2) {
        super(readablePartial, readablePartial2, null);
    }
    
    public Period(final ReadablePartial readablePartial, final ReadablePartial readablePartial2, final PeriodType periodType) {
        super(readablePartial, readablePartial2, periodType);
    }
    
    private Period(final int[] array, final PeriodType periodType) {
        super(array, periodType);
    }
    
    private void checkYearsAndMonths(final String s) {
        if (this.getMonths() != 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot convert to ");
            sb.append(s);
            sb.append(" as this period contains months and months vary in length");
            throw new UnsupportedOperationException(sb.toString());
        }
        if (this.getYears() == 0) {
            return;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Cannot convert to ");
        sb2.append(s);
        sb2.append(" as this period contains years and years vary in length");
        throw new UnsupportedOperationException(sb2.toString());
    }
    
    public static Period days(final int n) {
        return new Period(new int[] { 0, 0, 0, n, 0, 0, 0, 0 }, PeriodType.standard());
    }
    
    public static Period fieldDifference(final ReadablePartial readablePartial, final ReadablePartial readablePartial2) {
        if (readablePartial == null || readablePartial2 == null) {
            throw new IllegalArgumentException("ReadablePartial objects must not be null");
        }
        if (readablePartial.size() == readablePartial2.size()) {
            final DurationFieldType[] array = new DurationFieldType[readablePartial.size()];
            final int[] array2 = new int[readablePartial.size()];
            for (int i = 0; i < readablePartial.size(); ++i) {
                if (readablePartial.getFieldType(i) != readablePartial2.getFieldType(i)) {
                    throw new IllegalArgumentException("ReadablePartial objects must have the same set of fields");
                }
                array[i] = readablePartial.getFieldType(i).getDurationType();
                if (i > 0 && array[i - 1] == array[i]) {
                    throw new IllegalArgumentException("ReadablePartial objects must not have overlapping fields");
                }
                array2[i] = readablePartial2.getValue(i) - readablePartial.getValue(i);
            }
            return new Period(array2, PeriodType.forFields(array));
        }
        throw new IllegalArgumentException("ReadablePartial objects must have the same set of fields");
    }
    
    public static Period hours(final int n) {
        return new Period(new int[] { 0, 0, 0, 0, n, 0, 0, 0 }, PeriodType.standard());
    }
    
    public static Period millis(final int n) {
        return new Period(new int[] { 0, 0, 0, 0, 0, 0, 0, n }, PeriodType.standard());
    }
    
    public static Period minutes(final int n) {
        return new Period(new int[] { 0, 0, 0, 0, 0, n, 0, 0 }, PeriodType.standard());
    }
    
    public static Period months(final int n) {
        return new Period(new int[] { 0, n, 0, 0, 0, 0, 0, 0 }, PeriodType.standard());
    }
    
    @FromString
    public static Period parse(final String s) {
        return parse(s, ISOPeriodFormat.standard());
    }
    
    public static Period parse(final String s, final PeriodFormatter periodFormatter) {
        return periodFormatter.parsePeriod(s);
    }
    
    public static Period seconds(final int n) {
        return new Period(new int[] { 0, 0, 0, 0, 0, 0, n, 0 }, PeriodType.standard());
    }
    
    public static Period weeks(final int n) {
        return new Period(new int[] { 0, 0, n, 0, 0, 0, 0, 0 }, PeriodType.standard());
    }
    
    public static Period years(final int n) {
        return new Period(new int[] { n, 0, 0, 0, 0, 0, 0, 0, 0 }, PeriodType.standard());
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
    
    public Period minus(final ReadablePeriod readablePeriod) {
        if (readablePeriod == null) {
            return this;
        }
        final int[] values = this.getValues();
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.YEAR_INDEX, values, -readablePeriod.get(DurationFieldType.YEARS_TYPE));
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.MONTH_INDEX, values, -readablePeriod.get(DurationFieldType.MONTHS_TYPE));
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.WEEK_INDEX, values, -readablePeriod.get(DurationFieldType.WEEKS_TYPE));
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.DAY_INDEX, values, -readablePeriod.get(DurationFieldType.DAYS_TYPE));
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.HOUR_INDEX, values, -readablePeriod.get(DurationFieldType.HOURS_TYPE));
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.MINUTE_INDEX, values, -readablePeriod.get(DurationFieldType.MINUTES_TYPE));
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.SECOND_INDEX, values, -readablePeriod.get(DurationFieldType.SECONDS_TYPE));
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.MILLI_INDEX, values, -readablePeriod.get(DurationFieldType.MILLIS_TYPE));
        return new Period(values, this.getPeriodType());
    }
    
    public Period minusDays(final int n) {
        return this.plusDays(-n);
    }
    
    public Period minusHours(final int n) {
        return this.plusHours(-n);
    }
    
    public Period minusMillis(final int n) {
        return this.plusMillis(-n);
    }
    
    public Period minusMinutes(final int n) {
        return this.plusMinutes(-n);
    }
    
    public Period minusMonths(final int n) {
        return this.plusMonths(-n);
    }
    
    public Period minusSeconds(final int n) {
        return this.plusSeconds(-n);
    }
    
    public Period minusWeeks(final int n) {
        return this.plusWeeks(-n);
    }
    
    public Period minusYears(final int n) {
        return this.plusYears(-n);
    }
    
    public Period multipliedBy(final int n) {
        if (this == Period.ZERO) {
            return this;
        }
        if (n == 1) {
            return this;
        }
        final int[] values = this.getValues();
        for (int i = 0; i < values.length; ++i) {
            values[i] = FieldUtils.safeMultiply(values[i], n);
        }
        return new Period(values, this.getPeriodType());
    }
    
    public Period negated() {
        return this.multipliedBy(-1);
    }
    
    public Period normalizedStandard() {
        return this.normalizedStandard(PeriodType.standard());
    }
    
    public Period normalizedStandard(final PeriodType periodType) {
        final PeriodType periodType2 = DateTimeUtils.getPeriodType(periodType);
        Period period = new Period(this.getMillis() + this.getSeconds() * 1000L + this.getMinutes() * 60000L + this.getHours() * 3600000L + this.getDays() * 86400000L + this.getWeeks() * 604800000L, periodType2, (Chronology)ISOChronology.getInstanceUTC());
        final int years = this.getYears();
        final int months = this.getMonths();
        if (years == 0 && months == 0) {
            return period;
        }
        long n = years * 12L + months;
        if (periodType2.isSupported(DurationFieldType.YEARS_TYPE)) {
            final int safeToInt = FieldUtils.safeToInt(n / 12L);
            period = period.withYears(safeToInt);
            n -= safeToInt * 12;
        }
        if (periodType2.isSupported(DurationFieldType.MONTHS_TYPE)) {
            final int safeToInt2 = FieldUtils.safeToInt(n);
            period = period.withMonths(safeToInt2);
            n -= safeToInt2;
        }
        if (n == 0L) {
            return period;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unable to normalize as PeriodType is missing either years or months but period has a month/year amount: ");
        sb.append(this.toString());
        throw new UnsupportedOperationException(sb.toString());
    }
    
    public Period plus(final ReadablePeriod readablePeriod) {
        if (readablePeriod == null) {
            return this;
        }
        final int[] values = this.getValues();
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.YEAR_INDEX, values, readablePeriod.get(DurationFieldType.YEARS_TYPE));
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.MONTH_INDEX, values, readablePeriod.get(DurationFieldType.MONTHS_TYPE));
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.WEEK_INDEX, values, readablePeriod.get(DurationFieldType.WEEKS_TYPE));
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.DAY_INDEX, values, readablePeriod.get(DurationFieldType.DAYS_TYPE));
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.HOUR_INDEX, values, readablePeriod.get(DurationFieldType.HOURS_TYPE));
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.MINUTE_INDEX, values, readablePeriod.get(DurationFieldType.MINUTES_TYPE));
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.SECOND_INDEX, values, readablePeriod.get(DurationFieldType.SECONDS_TYPE));
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.MILLI_INDEX, values, readablePeriod.get(DurationFieldType.MILLIS_TYPE));
        return new Period(values, this.getPeriodType());
    }
    
    public Period plusDays(final int n) {
        if (n == 0) {
            return this;
        }
        final int[] values = this.getValues();
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.DAY_INDEX, values, n);
        return new Period(values, this.getPeriodType());
    }
    
    public Period plusHours(final int n) {
        if (n == 0) {
            return this;
        }
        final int[] values = this.getValues();
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.HOUR_INDEX, values, n);
        return new Period(values, this.getPeriodType());
    }
    
    public Period plusMillis(final int n) {
        if (n == 0) {
            return this;
        }
        final int[] values = this.getValues();
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.MILLI_INDEX, values, n);
        return new Period(values, this.getPeriodType());
    }
    
    public Period plusMinutes(final int n) {
        if (n == 0) {
            return this;
        }
        final int[] values = this.getValues();
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.MINUTE_INDEX, values, n);
        return new Period(values, this.getPeriodType());
    }
    
    public Period plusMonths(final int n) {
        if (n == 0) {
            return this;
        }
        final int[] values = this.getValues();
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.MONTH_INDEX, values, n);
        return new Period(values, this.getPeriodType());
    }
    
    public Period plusSeconds(final int n) {
        if (n == 0) {
            return this;
        }
        final int[] values = this.getValues();
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.SECOND_INDEX, values, n);
        return new Period(values, this.getPeriodType());
    }
    
    public Period plusWeeks(final int n) {
        if (n == 0) {
            return this;
        }
        final int[] values = this.getValues();
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.WEEK_INDEX, values, n);
        return new Period(values, this.getPeriodType());
    }
    
    public Period plusYears(final int n) {
        if (n == 0) {
            return this;
        }
        final int[] values = this.getValues();
        this.getPeriodType().addIndexedField((ReadablePeriod)this, PeriodType.YEAR_INDEX, values, n);
        return new Period(values, this.getPeriodType());
    }
    
    @Override
    public Period toPeriod() {
        return this;
    }
    
    public Days toStandardDays() {
        this.checkYearsAndMonths("Days");
        return Days.days(FieldUtils.safeToInt(FieldUtils.safeAdd(FieldUtils.safeAdd((this.getMillis() + this.getSeconds() * 1000L + this.getMinutes() * 60000L + this.getHours() * 3600000L) / 86400000L, (long)this.getDays()), this.getWeeks() * 7L)));
    }
    
    public Duration toStandardDuration() {
        this.checkYearsAndMonths("Duration");
        return new Duration(this.getMillis() + this.getSeconds() * 1000L + this.getMinutes() * 60000L + this.getHours() * 3600000L + this.getDays() * 86400000L + this.getWeeks() * 604800000L);
    }
    
    public Hours toStandardHours() {
        this.checkYearsAndMonths("Hours");
        return Hours.hours(FieldUtils.safeToInt(FieldUtils.safeAdd(FieldUtils.safeAdd(FieldUtils.safeAdd((this.getMillis() + this.getSeconds() * 1000L + this.getMinutes() * 60000L) / 3600000L, (long)this.getHours()), this.getDays() * 24L), this.getWeeks() * 168L)));
    }
    
    public Minutes toStandardMinutes() {
        this.checkYearsAndMonths("Minutes");
        return Minutes.minutes(FieldUtils.safeToInt(FieldUtils.safeAdd(FieldUtils.safeAdd(FieldUtils.safeAdd(FieldUtils.safeAdd((this.getMillis() + this.getSeconds() * 1000L) / 60000L, (long)this.getMinutes()), this.getHours() * 60L), this.getDays() * 1440L), this.getWeeks() * 10080L)));
    }
    
    public Seconds toStandardSeconds() {
        this.checkYearsAndMonths("Seconds");
        return Seconds.seconds(FieldUtils.safeToInt(FieldUtils.safeAdd(FieldUtils.safeAdd(FieldUtils.safeAdd(FieldUtils.safeAdd(FieldUtils.safeAdd((long)(this.getMillis() / 1000), (long)this.getSeconds()), this.getMinutes() * 60L), this.getHours() * 3600L), this.getDays() * 86400L), this.getWeeks() * 604800L)));
    }
    
    public Weeks toStandardWeeks() {
        this.checkYearsAndMonths("Weeks");
        return Weeks.weeks(FieldUtils.safeToInt(this.getWeeks() + (this.getMillis() + this.getSeconds() * 1000L + this.getMinutes() * 60000L + this.getHours() * 3600000L + this.getDays() * 86400000L) / 604800000L));
    }
    
    public Period withDays(final int n) {
        final int[] values = this.getValues();
        this.getPeriodType().setIndexedField((ReadablePeriod)this, PeriodType.DAY_INDEX, values, n);
        return new Period(values, this.getPeriodType());
    }
    
    public Period withField(final DurationFieldType durationFieldType, final int n) {
        if (durationFieldType != null) {
            final int[] values = this.getValues();
            super.setFieldInto(values, durationFieldType, n);
            return new Period(values, this.getPeriodType());
        }
        throw new IllegalArgumentException("Field must not be null");
    }
    
    public Period withFieldAdded(final DurationFieldType durationFieldType, final int n) {
        if (durationFieldType == null) {
            throw new IllegalArgumentException("Field must not be null");
        }
        if (n == 0) {
            return this;
        }
        final int[] values = this.getValues();
        super.addFieldInto(values, durationFieldType, n);
        return new Period(values, this.getPeriodType());
    }
    
    public Period withFields(final ReadablePeriod readablePeriod) {
        if (readablePeriod == null) {
            return this;
        }
        return new Period(super.mergePeriodInto(this.getValues(), readablePeriod), this.getPeriodType());
    }
    
    public Period withHours(final int n) {
        final int[] values = this.getValues();
        this.getPeriodType().setIndexedField((ReadablePeriod)this, PeriodType.HOUR_INDEX, values, n);
        return new Period(values, this.getPeriodType());
    }
    
    public Period withMillis(final int n) {
        final int[] values = this.getValues();
        this.getPeriodType().setIndexedField((ReadablePeriod)this, PeriodType.MILLI_INDEX, values, n);
        return new Period(values, this.getPeriodType());
    }
    
    public Period withMinutes(final int n) {
        final int[] values = this.getValues();
        this.getPeriodType().setIndexedField((ReadablePeriod)this, PeriodType.MINUTE_INDEX, values, n);
        return new Period(values, this.getPeriodType());
    }
    
    public Period withMonths(final int n) {
        final int[] values = this.getValues();
        this.getPeriodType().setIndexedField((ReadablePeriod)this, PeriodType.MONTH_INDEX, values, n);
        return new Period(values, this.getPeriodType());
    }
    
    public Period withPeriodType(PeriodType periodType) {
        periodType = DateTimeUtils.getPeriodType(periodType);
        if (periodType.equals((Object)this.getPeriodType())) {
            return this;
        }
        return new Period(this, periodType);
    }
    
    public Period withSeconds(final int n) {
        final int[] values = this.getValues();
        this.getPeriodType().setIndexedField((ReadablePeriod)this, PeriodType.SECOND_INDEX, values, n);
        return new Period(values, this.getPeriodType());
    }
    
    public Period withWeeks(final int n) {
        final int[] values = this.getValues();
        this.getPeriodType().setIndexedField((ReadablePeriod)this, PeriodType.WEEK_INDEX, values, n);
        return new Period(values, this.getPeriodType());
    }
    
    public Period withYears(final int n) {
        final int[] values = this.getValues();
        this.getPeriodType().setIndexedField((ReadablePeriod)this, PeriodType.YEAR_INDEX, values, n);
        return new Period(values, this.getPeriodType());
    }
}
