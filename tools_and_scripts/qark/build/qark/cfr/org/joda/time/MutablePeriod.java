/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.joda.convert.FromString
 *  org.joda.time.Chronology
 *  org.joda.time.DateTimeUtils
 *  org.joda.time.DurationFieldType
 *  org.joda.time.PeriodType
 *  org.joda.time.ReadableInterval
 *  org.joda.time.field.FieldUtils
 *  org.joda.time.format.ISOPeriodFormat
 *  org.joda.time.format.PeriodFormatter
 */
package org.joda.time;

import java.io.Serializable;
import org.joda.convert.FromString;
import org.joda.time.Chronology;
import org.joda.time.DateTimeUtils;
import org.joda.time.DurationFieldType;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadWritablePeriod;
import org.joda.time.ReadableDuration;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadableInterval;
import org.joda.time.ReadablePeriod;
import org.joda.time.base.BasePeriod;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;

public class MutablePeriod
extends BasePeriod
implements ReadWritablePeriod,
Cloneable,
Serializable {
    private static final long serialVersionUID = 3436451121567212165L;

    public MutablePeriod() {
        super(0L, (PeriodType)null, (Chronology)null);
    }

    public MutablePeriod(int n, int n2, int n3, int n4) {
        super(0, 0, 0, 0, n, n2, n3, n4, PeriodType.standard());
    }

    public MutablePeriod(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        super(n, n2, n3, n4, n5, n6, n7, n8, PeriodType.standard());
    }

    public MutablePeriod(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, PeriodType periodType) {
        super(n, n2, n3, n4, n5, n6, n7, n8, periodType);
    }

    public MutablePeriod(long l) {
        super(l);
    }

    public MutablePeriod(long l, long l2) {
        super(l, l2, null, null);
    }

    public MutablePeriod(long l, long l2, Chronology chronology) {
        super(l, l2, null, chronology);
    }

    public MutablePeriod(long l, long l2, PeriodType periodType) {
        super(l, l2, periodType, null);
    }

    public MutablePeriod(long l, long l2, PeriodType periodType, Chronology chronology) {
        super(l, l2, periodType, chronology);
    }

    public MutablePeriod(long l, Chronology chronology) {
        super(l, (PeriodType)null, chronology);
    }

    public MutablePeriod(long l, PeriodType periodType) {
        super(l, periodType, (Chronology)null);
    }

    public MutablePeriod(long l, PeriodType periodType, Chronology chronology) {
        super(l, periodType, chronology);
    }

    public MutablePeriod(Object object) {
        super(object, null, null);
    }

    public MutablePeriod(Object object, Chronology chronology) {
        super(object, null, chronology);
    }

    public MutablePeriod(Object object, PeriodType periodType) {
        super(object, periodType, null);
    }

    public MutablePeriod(Object object, PeriodType periodType, Chronology chronology) {
        super(object, periodType, chronology);
    }

    public MutablePeriod(PeriodType periodType) {
        super(0L, periodType, (Chronology)null);
    }

    public MutablePeriod(ReadableDuration readableDuration, ReadableInstant readableInstant) {
        super(readableDuration, readableInstant, null);
    }

    public MutablePeriod(ReadableDuration readableDuration, ReadableInstant readableInstant, PeriodType periodType) {
        super(readableDuration, readableInstant, periodType);
    }

    public MutablePeriod(ReadableInstant readableInstant, ReadableDuration readableDuration) {
        super(readableInstant, readableDuration, null);
    }

    public MutablePeriod(ReadableInstant readableInstant, ReadableDuration readableDuration, PeriodType periodType) {
        super(readableInstant, readableDuration, periodType);
    }

    public MutablePeriod(ReadableInstant readableInstant, ReadableInstant readableInstant2) {
        super(readableInstant, readableInstant2, null);
    }

    public MutablePeriod(ReadableInstant readableInstant, ReadableInstant readableInstant2, PeriodType periodType) {
        super(readableInstant, readableInstant2, periodType);
    }

    @FromString
    public static MutablePeriod parse(String string) {
        return MutablePeriod.parse(string, ISOPeriodFormat.standard());
    }

    public static MutablePeriod parse(String string, PeriodFormatter periodFormatter) {
        return periodFormatter.parsePeriod(string).toMutablePeriod();
    }

    @Override
    public void add(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        this.setPeriod(FieldUtils.safeAdd((int)this.getYears(), (int)n), FieldUtils.safeAdd((int)this.getMonths(), (int)n2), FieldUtils.safeAdd((int)this.getWeeks(), (int)n3), FieldUtils.safeAdd((int)this.getDays(), (int)n4), FieldUtils.safeAdd((int)this.getHours(), (int)n5), FieldUtils.safeAdd((int)this.getMinutes(), (int)n6), FieldUtils.safeAdd((int)this.getSeconds(), (int)n7), FieldUtils.safeAdd((int)this.getMillis(), (int)n8));
    }

    public void add(long l) {
        this.add(new Period(l, this.getPeriodType()));
    }

    public void add(long l, Chronology chronology) {
        this.add(new Period(l, this.getPeriodType(), chronology));
    }

    @Override
    public void add(DurationFieldType durationFieldType, int n) {
        super.addField(durationFieldType, n);
    }

    public void add(ReadableDuration readableDuration) {
        if (readableDuration != null) {
            this.add(new Period(readableDuration.getMillis(), this.getPeriodType()));
            return;
        }
    }

    @Override
    public void add(ReadableInterval readableInterval) {
        if (readableInterval != null) {
            this.add(readableInterval.toPeriod(this.getPeriodType()));
            return;
        }
    }

    @Override
    public void add(ReadablePeriod readablePeriod) {
        super.addPeriod(readablePeriod);
    }

    @Override
    public void addDays(int n) {
        super.addField(DurationFieldType.days(), n);
    }

    @Override
    public void addHours(int n) {
        super.addField(DurationFieldType.hours(), n);
    }

    @Override
    public void addMillis(int n) {
        super.addField(DurationFieldType.millis(), n);
    }

    @Override
    public void addMinutes(int n) {
        super.addField(DurationFieldType.minutes(), n);
    }

    @Override
    public void addMonths(int n) {
        super.addField(DurationFieldType.months(), n);
    }

    @Override
    public void addSeconds(int n) {
        super.addField(DurationFieldType.seconds(), n);
    }

    @Override
    public void addWeeks(int n) {
        super.addField(DurationFieldType.weeks(), n);
    }

    @Override
    public void addYears(int n) {
        super.addField(DurationFieldType.years(), n);
    }

    @Override
    public void clear() {
        super.setValues(new int[this.size()]);
    }

    public Object clone() {
        try {
            Object object = Object.super.clone();
            return object;
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
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

    @Override
    public void mergePeriod(ReadablePeriod readablePeriod) {
        super.mergePeriod(readablePeriod);
    }

    @Override
    public void set(DurationFieldType durationFieldType, int n) {
        super.setField(durationFieldType, n);
    }

    @Override
    public void setDays(int n) {
        super.setField(DurationFieldType.days(), n);
    }

    @Override
    public void setHours(int n) {
        super.setField(DurationFieldType.hours(), n);
    }

    @Override
    public void setMillis(int n) {
        super.setField(DurationFieldType.millis(), n);
    }

    @Override
    public void setMinutes(int n) {
        super.setField(DurationFieldType.minutes(), n);
    }

    @Override
    public void setMonths(int n) {
        super.setField(DurationFieldType.months(), n);
    }

    @Override
    public void setPeriod(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        super.setPeriod(n, n2, n3, n4, n5, n6, n7, n8);
    }

    public void setPeriod(long l) {
        this.setPeriod(l, null);
    }

    public void setPeriod(long l, long l2) {
        this.setPeriod(l, l2, null);
    }

    public void setPeriod(long l, long l2, Chronology chronology) {
        this.setValues(DateTimeUtils.getChronology((Chronology)chronology).get((ReadablePeriod)this, l, l2));
    }

    public void setPeriod(long l, Chronology chronology) {
        this.setValues(DateTimeUtils.getChronology((Chronology)chronology).get((ReadablePeriod)this, l));
    }

    public void setPeriod(ReadableDuration readableDuration) {
        this.setPeriod(readableDuration, null);
    }

    public void setPeriod(ReadableDuration readableDuration, Chronology chronology) {
        this.setPeriod(DateTimeUtils.getDurationMillis((ReadableDuration)readableDuration), chronology);
    }

    public void setPeriod(ReadableInstant readableInstant, ReadableInstant readableInstant2) {
        if (readableInstant == readableInstant2) {
            this.setPeriod(0L);
            return;
        }
        this.setPeriod(DateTimeUtils.getInstantMillis((ReadableInstant)readableInstant), DateTimeUtils.getInstantMillis((ReadableInstant)readableInstant2), DateTimeUtils.getIntervalChronology((ReadableInstant)readableInstant, (ReadableInstant)readableInstant2));
    }

    @Override
    public void setPeriod(ReadableInterval readableInterval) {
        if (readableInterval == null) {
            this.setPeriod(0L);
            return;
        }
        Chronology chronology = DateTimeUtils.getChronology((Chronology)readableInterval.getChronology());
        this.setPeriod(readableInterval.getStartMillis(), readableInterval.getEndMillis(), chronology);
    }

    @Override
    public void setPeriod(ReadablePeriod readablePeriod) {
        super.setPeriod(readablePeriod);
    }

    @Override
    public void setSeconds(int n) {
        super.setField(DurationFieldType.seconds(), n);
    }

    @Override
    public void setValue(int n, int n2) {
        super.setValue(n, n2);
    }

    @Override
    public void setWeeks(int n) {
        super.setField(DurationFieldType.weeks(), n);
    }

    @Override
    public void setYears(int n) {
        super.setField(DurationFieldType.years(), n);
    }
}

