// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.joda.time;

import org.joda.time.field.FieldUtils;
import org.joda.time.format.PeriodFormatter;
import org.joda.convert.FromString;
import org.joda.time.format.ISOPeriodFormat;
import java.io.Serializable;
import org.joda.time.base.BasePeriod;

public class MutablePeriod extends BasePeriod implements ReadWritablePeriod, Cloneable, Serializable
{
    private static final long serialVersionUID = 3436451121567212165L;
    
    public MutablePeriod() {
        super(0L, null, null);
    }
    
    public MutablePeriod(final int n, final int n2, final int n3, final int n4) {
        super(0, 0, 0, 0, n, n2, n3, n4, PeriodType.standard());
    }
    
    public MutablePeriod(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8) {
        super(n, n2, n3, n4, n5, n6, n7, n8, PeriodType.standard());
    }
    
    public MutablePeriod(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8, final PeriodType periodType) {
        super(n, n2, n3, n4, n5, n6, n7, n8, periodType);
    }
    
    public MutablePeriod(final long n) {
        super(n);
    }
    
    public MutablePeriod(final long n, final long n2) {
        super(n, n2, null, null);
    }
    
    public MutablePeriod(final long n, final long n2, final Chronology chronology) {
        super(n, n2, null, chronology);
    }
    
    public MutablePeriod(final long n, final long n2, final PeriodType periodType) {
        super(n, n2, periodType, null);
    }
    
    public MutablePeriod(final long n, final long n2, final PeriodType periodType, final Chronology chronology) {
        super(n, n2, periodType, chronology);
    }
    
    public MutablePeriod(final long n, final Chronology chronology) {
        super(n, null, chronology);
    }
    
    public MutablePeriod(final long n, final PeriodType periodType) {
        super(n, periodType, null);
    }
    
    public MutablePeriod(final long n, final PeriodType periodType, final Chronology chronology) {
        super(n, periodType, chronology);
    }
    
    public MutablePeriod(final Object o) {
        super(o, null, null);
    }
    
    public MutablePeriod(final Object o, final Chronology chronology) {
        super(o, null, chronology);
    }
    
    public MutablePeriod(final Object o, final PeriodType periodType) {
        super(o, periodType, null);
    }
    
    public MutablePeriod(final Object o, final PeriodType periodType, final Chronology chronology) {
        super(o, periodType, chronology);
    }
    
    public MutablePeriod(final PeriodType periodType) {
        super(0L, periodType, null);
    }
    
    public MutablePeriod(final ReadableDuration readableDuration, final ReadableInstant readableInstant) {
        super(readableDuration, readableInstant, null);
    }
    
    public MutablePeriod(final ReadableDuration readableDuration, final ReadableInstant readableInstant, final PeriodType periodType) {
        super(readableDuration, readableInstant, periodType);
    }
    
    public MutablePeriod(final ReadableInstant readableInstant, final ReadableDuration readableDuration) {
        super(readableInstant, readableDuration, null);
    }
    
    public MutablePeriod(final ReadableInstant readableInstant, final ReadableDuration readableDuration, final PeriodType periodType) {
        super(readableInstant, readableDuration, periodType);
    }
    
    public MutablePeriod(final ReadableInstant readableInstant, final ReadableInstant readableInstant2) {
        super(readableInstant, readableInstant2, null);
    }
    
    public MutablePeriod(final ReadableInstant readableInstant, final ReadableInstant readableInstant2, final PeriodType periodType) {
        super(readableInstant, readableInstant2, periodType);
    }
    
    @FromString
    public static MutablePeriod parse(final String s) {
        return parse(s, ISOPeriodFormat.standard());
    }
    
    public static MutablePeriod parse(final String s, final PeriodFormatter periodFormatter) {
        return periodFormatter.parsePeriod(s).toMutablePeriod();
    }
    
    @Override
    public void add(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8) {
        this.setPeriod(FieldUtils.safeAdd(this.getYears(), n), FieldUtils.safeAdd(this.getMonths(), n2), FieldUtils.safeAdd(this.getWeeks(), n3), FieldUtils.safeAdd(this.getDays(), n4), FieldUtils.safeAdd(this.getHours(), n5), FieldUtils.safeAdd(this.getMinutes(), n6), FieldUtils.safeAdd(this.getSeconds(), n7), FieldUtils.safeAdd(this.getMillis(), n8));
    }
    
    public void add(final long n) {
        this.add(new Period(n, this.getPeriodType()));
    }
    
    public void add(final long n, final Chronology chronology) {
        this.add(new Period(n, this.getPeriodType(), chronology));
    }
    
    @Override
    public void add(final DurationFieldType durationFieldType, final int n) {
        super.addField(durationFieldType, n);
    }
    
    public void add(final ReadableDuration readableDuration) {
        if (readableDuration != null) {
            this.add(new Period(readableDuration.getMillis(), this.getPeriodType()));
        }
    }
    
    @Override
    public void add(final ReadableInterval readableInterval) {
        if (readableInterval != null) {
            this.add(readableInterval.toPeriod(this.getPeriodType()));
        }
    }
    
    @Override
    public void add(final ReadablePeriod readablePeriod) {
        super.addPeriod(readablePeriod);
    }
    
    @Override
    public void addDays(final int n) {
        super.addField(DurationFieldType.days(), n);
    }
    
    @Override
    public void addHours(final int n) {
        super.addField(DurationFieldType.hours(), n);
    }
    
    @Override
    public void addMillis(final int n) {
        super.addField(DurationFieldType.millis(), n);
    }
    
    @Override
    public void addMinutes(final int n) {
        super.addField(DurationFieldType.minutes(), n);
    }
    
    @Override
    public void addMonths(final int n) {
        super.addField(DurationFieldType.months(), n);
    }
    
    @Override
    public void addSeconds(final int n) {
        super.addField(DurationFieldType.seconds(), n);
    }
    
    @Override
    public void addWeeks(final int n) {
        super.addField(DurationFieldType.weeks(), n);
    }
    
    @Override
    public void addYears(final int n) {
        super.addField(DurationFieldType.years(), n);
    }
    
    @Override
    public void clear() {
        super.setValues(new int[this.size()]);
    }
    
    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException ex) {
            throw new InternalError("Clone error");
        }
    }
    
    public MutablePeriod copy() {
        return (MutablePeriod)this.clone();
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
    
    public void mergePeriod(final ReadablePeriod readablePeriod) {
        super.mergePeriod(readablePeriod);
    }
    
    @Override
    public void set(final DurationFieldType durationFieldType, final int n) {
        super.setField(durationFieldType, n);
    }
    
    @Override
    public void setDays(final int n) {
        super.setField(DurationFieldType.days(), n);
    }
    
    @Override
    public void setHours(final int n) {
        super.setField(DurationFieldType.hours(), n);
    }
    
    @Override
    public void setMillis(final int n) {
        super.setField(DurationFieldType.millis(), n);
    }
    
    @Override
    public void setMinutes(final int n) {
        super.setField(DurationFieldType.minutes(), n);
    }
    
    @Override
    public void setMonths(final int n) {
        super.setField(DurationFieldType.months(), n);
    }
    
    @Override
    public void setPeriod(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8) {
        super.setPeriod(n, n2, n3, n4, n5, n6, n7, n8);
    }
    
    public void setPeriod(final long n) {
        this.setPeriod(n, null);
    }
    
    public void setPeriod(final long n, final long n2) {
        this.setPeriod(n, n2, null);
    }
    
    public void setPeriod(final long n, final long n2, final Chronology chronology) {
        this.setValues(DateTimeUtils.getChronology(chronology).get((ReadablePeriod)this, n, n2));
    }
    
    public void setPeriod(final long n, final Chronology chronology) {
        this.setValues(DateTimeUtils.getChronology(chronology).get((ReadablePeriod)this, n));
    }
    
    public void setPeriod(final ReadableDuration readableDuration) {
        this.setPeriod(readableDuration, null);
    }
    
    public void setPeriod(final ReadableDuration readableDuration, final Chronology chronology) {
        this.setPeriod(DateTimeUtils.getDurationMillis(readableDuration), chronology);
    }
    
    public void setPeriod(final ReadableInstant readableInstant, final ReadableInstant readableInstant2) {
        if (readableInstant == readableInstant2) {
            this.setPeriod(0L);
            return;
        }
        this.setPeriod(DateTimeUtils.getInstantMillis(readableInstant), DateTimeUtils.getInstantMillis(readableInstant2), DateTimeUtils.getIntervalChronology(readableInstant, readableInstant2));
    }
    
    @Override
    public void setPeriod(final ReadableInterval readableInterval) {
        if (readableInterval == null) {
            this.setPeriod(0L);
            return;
        }
        this.setPeriod(readableInterval.getStartMillis(), readableInterval.getEndMillis(), DateTimeUtils.getChronology(readableInterval.getChronology()));
    }
    
    @Override
    public void setPeriod(final ReadablePeriod period) {
        super.setPeriod(period);
    }
    
    @Override
    public void setSeconds(final int n) {
        super.setField(DurationFieldType.seconds(), n);
    }
    
    @Override
    public void setValue(final int n, final int n2) {
        super.setValue(n, n2);
    }
    
    @Override
    public void setWeeks(final int n) {
        super.setField(DurationFieldType.weeks(), n);
    }
    
    @Override
    public void setYears(final int n) {
        super.setField(DurationFieldType.years(), n);
    }
}
