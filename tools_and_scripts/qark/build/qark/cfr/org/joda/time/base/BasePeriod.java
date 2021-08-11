/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.joda.time.Chronology
 *  org.joda.time.DateTimeFieldType
 *  org.joda.time.DateTimeUtils
 *  org.joda.time.DurationFieldType
 *  org.joda.time.PeriodType
 *  org.joda.time.base.BasePeriod$1
 *  org.joda.time.chrono.ISOChronology
 *  org.joda.time.convert.ConverterManager
 *  org.joda.time.convert.PeriodConverter
 *  org.joda.time.field.FieldUtils
 */
package org.joda.time.base;

import java.io.Serializable;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.Duration;
import org.joda.time.DurationFieldType;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.joda.time.ReadWritablePeriod;
import org.joda.time.ReadableDuration;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePartial;
import org.joda.time.ReadablePeriod;
import org.joda.time.base.AbstractPeriod;
import org.joda.time.base.BaseLocal;
import org.joda.time.base.BasePeriod;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.PeriodConverter;
import org.joda.time.field.FieldUtils;

public abstract class BasePeriod
extends AbstractPeriod
implements ReadablePeriod,
Serializable {
    private static final ReadablePeriod DUMMY_PERIOD = new 1();
    private static final long serialVersionUID = -2110953284060001145L;
    private final PeriodType iType;
    private final int[] iValues;

    protected BasePeriod(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, PeriodType periodType) {
        this.iType = this.checkPeriodType(periodType);
        this.iValues = this.setPeriodInternal(n, n2, n3, n4, n5, n6, n7, n8);
    }

    protected BasePeriod(long l) {
        this.iType = PeriodType.standard();
        int[] arrn = ISOChronology.getInstanceUTC().get(DUMMY_PERIOD, l);
        this.iValues = new int[8];
        System.arraycopy(arrn, 0, this.iValues, 4, 4);
    }

    protected BasePeriod(long l, long l2, PeriodType periodType, Chronology chronology) {
        periodType = this.checkPeriodType(periodType);
        chronology = DateTimeUtils.getChronology((Chronology)chronology);
        this.iType = periodType;
        this.iValues = chronology.get((ReadablePeriod)this, l, l2);
    }

    protected BasePeriod(long l, PeriodType periodType, Chronology chronology) {
        periodType = this.checkPeriodType(periodType);
        chronology = DateTimeUtils.getChronology((Chronology)chronology);
        this.iType = periodType;
        this.iValues = chronology.get((ReadablePeriod)this, l);
    }

    protected BasePeriod(Object object, PeriodType periodType, Chronology chronology) {
        PeriodConverter periodConverter = ConverterManager.getInstance().getPeriodConverter(object);
        PeriodType periodType2 = periodType;
        if (periodType == null) {
            periodType2 = periodConverter.getPeriodType(object);
        }
        this.iType = periodType = this.checkPeriodType(periodType2);
        if (this instanceof ReadWritablePeriod) {
            this.iValues = new int[this.size()];
            periodType = DateTimeUtils.getChronology((Chronology)chronology);
            periodConverter.setInto((ReadWritablePeriod)((Object)this), object, (Chronology)periodType);
            return;
        }
        this.iValues = new MutablePeriod(object, periodType, chronology).getValues();
    }

    protected BasePeriod(ReadableDuration readableDuration, ReadableInstant readableInstant, PeriodType periodType) {
        periodType = this.checkPeriodType(periodType);
        long l = DateTimeUtils.getDurationMillis((ReadableDuration)readableDuration);
        long l2 = DateTimeUtils.getInstantMillis((ReadableInstant)readableInstant);
        l = FieldUtils.safeSubtract((long)l2, (long)l);
        readableDuration = DateTimeUtils.getInstantChronology((ReadableInstant)readableInstant);
        this.iType = periodType;
        this.iValues = readableDuration.get((ReadablePeriod)this, l, l2);
    }

    protected BasePeriod(ReadableInstant readableInstant, ReadableDuration readableDuration, PeriodType periodType) {
        periodType = this.checkPeriodType(periodType);
        long l = DateTimeUtils.getInstantMillis((ReadableInstant)readableInstant);
        long l2 = FieldUtils.safeAdd((long)l, (long)DateTimeUtils.getDurationMillis((ReadableDuration)readableDuration));
        readableInstant = DateTimeUtils.getInstantChronology((ReadableInstant)readableInstant);
        this.iType = periodType;
        this.iValues = readableInstant.get((ReadablePeriod)this, l, l2);
    }

    protected BasePeriod(ReadableInstant readableInstant, ReadableInstant readableInstant2, PeriodType periodType) {
        periodType = this.checkPeriodType(periodType);
        if (readableInstant == null && readableInstant2 == null) {
            this.iType = periodType;
            this.iValues = new int[this.size()];
            return;
        }
        long l = DateTimeUtils.getInstantMillis((ReadableInstant)readableInstant);
        long l2 = DateTimeUtils.getInstantMillis((ReadableInstant)readableInstant2);
        readableInstant = DateTimeUtils.getIntervalChronology((ReadableInstant)readableInstant, (ReadableInstant)readableInstant2);
        this.iType = periodType;
        this.iValues = readableInstant.get((ReadablePeriod)this, l, l2);
    }

    protected BasePeriod(ReadablePartial readablePartial, ReadablePartial readablePartial2, PeriodType periodType) {
        if (readablePartial != null && readablePartial2 != null) {
            if (readablePartial instanceof BaseLocal && readablePartial2 instanceof BaseLocal && readablePartial.getClass() == readablePartial2.getClass()) {
                periodType = this.checkPeriodType(periodType);
                long l = ((BaseLocal)readablePartial).getLocalMillis();
                long l2 = ((BaseLocal)readablePartial2).getLocalMillis();
                readablePartial = DateTimeUtils.getChronology((Chronology)readablePartial.getChronology());
                this.iType = periodType;
                this.iValues = readablePartial.get((ReadablePeriod)this, l, l2);
                return;
            }
            if (readablePartial.size() == readablePartial2.size()) {
                int n = readablePartial.size();
                for (int i = 0; i < n; ++i) {
                    if (readablePartial.getFieldType(i) == readablePartial2.getFieldType(i)) {
                        continue;
                    }
                    throw new IllegalArgumentException("ReadablePartial objects must have the same set of fields");
                }
                if (DateTimeUtils.isContiguous((ReadablePartial)readablePartial)) {
                    this.iType = this.checkPeriodType(periodType);
                    periodType = DateTimeUtils.getChronology((Chronology)readablePartial.getChronology()).withUTC();
                    this.iValues = periodType.get((ReadablePeriod)this, periodType.set(readablePartial, 0L), periodType.set(readablePartial2, 0L));
                    return;
                }
                throw new IllegalArgumentException("ReadablePartial objects must be contiguous");
            }
            throw new IllegalArgumentException("ReadablePartial objects must have the same set of fields");
        }
        throw new IllegalArgumentException("ReadablePartial objects must not be null");
    }

    protected BasePeriod(int[] arrn, PeriodType periodType) {
        this.iType = periodType;
        this.iValues = arrn;
    }

    private void checkAndUpdate(DurationFieldType durationFieldType, int[] object, int n) {
        int n2 = this.indexOf(durationFieldType);
        if (n2 == -1) {
            if (n == 0) {
                return;
            }
            object = new StringBuilder();
            object.append("Period does not support field '");
            object.append(durationFieldType.getName());
            object.append("'");
            throw new IllegalArgumentException(object.toString());
        }
        object[n2] = n;
    }

    private void setPeriodInternal(ReadablePeriod readablePeriod) {
        int[] arrn = new int[this.size()];
        int n = readablePeriod.size();
        for (int i = 0; i < n; ++i) {
            this.checkAndUpdate(readablePeriod.getFieldType(i), arrn, readablePeriod.getValue(i));
        }
        this.setValues(arrn);
    }

    private int[] setPeriodInternal(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        int[] arrn = new int[this.size()];
        this.checkAndUpdate(DurationFieldType.years(), arrn, n);
        this.checkAndUpdate(DurationFieldType.months(), arrn, n2);
        this.checkAndUpdate(DurationFieldType.weeks(), arrn, n3);
        this.checkAndUpdate(DurationFieldType.days(), arrn, n4);
        this.checkAndUpdate(DurationFieldType.hours(), arrn, n5);
        this.checkAndUpdate(DurationFieldType.minutes(), arrn, n6);
        this.checkAndUpdate(DurationFieldType.seconds(), arrn, n7);
        this.checkAndUpdate(DurationFieldType.millis(), arrn, n8);
        return arrn;
    }

    protected void addField(DurationFieldType durationFieldType, int n) {
        this.addFieldInto(this.iValues, durationFieldType, n);
    }

    protected void addFieldInto(int[] object, DurationFieldType durationFieldType, int n) {
        int n2 = this.indexOf(durationFieldType);
        if (n2 == -1) {
            if (n == 0 && durationFieldType != null) {
                return;
            }
            object = new StringBuilder();
            object.append("Period does not support field '");
            object.append((Object)durationFieldType);
            object.append("'");
            throw new IllegalArgumentException(object.toString());
        }
        object[n2] = FieldUtils.safeAdd((int)object[n2], (int)n);
    }

    protected void addPeriod(ReadablePeriod readablePeriod) {
        if (readablePeriod != null) {
            this.setValues(this.addPeriodInto(this.getValues(), readablePeriod));
            return;
        }
    }

    protected int[] addPeriodInto(int[] object, ReadablePeriod readablePeriod) {
        int n = readablePeriod.size();
        for (int i = 0; i < n; ++i) {
            DurationFieldType durationFieldType = readablePeriod.getFieldType(i);
            int n2 = readablePeriod.getValue(i);
            if (n2 == 0) continue;
            int n3 = this.indexOf(durationFieldType);
            if (n3 != -1) {
                object[n3] = FieldUtils.safeAdd((int)this.getValue(n3), (int)n2);
                continue;
            }
            object = new StringBuilder();
            object.append("Period does not support field '");
            object.append(durationFieldType.getName());
            object.append("'");
            throw new IllegalArgumentException(object.toString());
        }
        return object;
    }

    protected PeriodType checkPeriodType(PeriodType periodType) {
        return DateTimeUtils.getPeriodType((PeriodType)periodType);
    }

    @Override
    public PeriodType getPeriodType() {
        return this.iType;
    }

    @Override
    public int getValue(int n) {
        return this.iValues[n];
    }

    protected void mergePeriod(ReadablePeriod readablePeriod) {
        if (readablePeriod != null) {
            this.setValues(this.mergePeriodInto(this.getValues(), readablePeriod));
            return;
        }
    }

    protected int[] mergePeriodInto(int[] arrn, ReadablePeriod readablePeriod) {
        int n = readablePeriod.size();
        for (int i = 0; i < n; ++i) {
            this.checkAndUpdate(readablePeriod.getFieldType(i), arrn, readablePeriod.getValue(i));
        }
        return arrn;
    }

    protected void setField(DurationFieldType durationFieldType, int n) {
        this.setFieldInto(this.iValues, durationFieldType, n);
    }

    protected void setFieldInto(int[] object, DurationFieldType durationFieldType, int n) {
        int n2 = this.indexOf(durationFieldType);
        if (n2 == -1) {
            if (n == 0 && durationFieldType != null) {
                return;
            }
            object = new StringBuilder();
            object.append("Period does not support field '");
            object.append((Object)durationFieldType);
            object.append("'");
            throw new IllegalArgumentException(object.toString());
        }
        object[n2] = n;
    }

    protected void setPeriod(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        this.setValues(this.setPeriodInternal(n, n2, n3, n4, n5, n6, n7, n8));
    }

    protected void setPeriod(ReadablePeriod readablePeriod) {
        if (readablePeriod == null) {
            this.setValues(new int[this.size()]);
            return;
        }
        this.setPeriodInternal(readablePeriod);
    }

    protected void setValue(int n, int n2) {
        this.iValues[n] = n2;
    }

    protected void setValues(int[] arrn) {
        int[] arrn2 = this.iValues;
        System.arraycopy(arrn, 0, arrn2, 0, arrn2.length);
    }

    public Duration toDurationFrom(ReadableInstant readableInstant) {
        long l = DateTimeUtils.getInstantMillis((ReadableInstant)readableInstant);
        return new Duration(l, DateTimeUtils.getInstantChronology((ReadableInstant)readableInstant).add((ReadablePeriod)this, l, 1));
    }

    public Duration toDurationTo(ReadableInstant readableInstant) {
        long l = DateTimeUtils.getInstantMillis((ReadableInstant)readableInstant);
        return new Duration(DateTimeUtils.getInstantChronology((ReadableInstant)readableInstant).add((ReadablePeriod)this, l, -1), l);
    }
}

