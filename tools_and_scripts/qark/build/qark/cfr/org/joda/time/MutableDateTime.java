/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.joda.convert.FromString
 *  org.joda.time.Chronology
 *  org.joda.time.DateTimeField
 *  org.joda.time.DateTimeFieldType
 *  org.joda.time.DateTimeUtils
 *  org.joda.time.DurationField
 *  org.joda.time.DurationFieldType
 *  org.joda.time.MutableDateTime$Property
 *  org.joda.time.chrono.ISOChronology
 *  org.joda.time.field.FieldUtils
 *  org.joda.time.format.DateTimeFormatter
 *  org.joda.time.format.ISODateTimeFormat
 */
package org.joda.time;

import java.io.Serializable;
import org.joda.convert.FromString;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.MutableDateTime;
import org.joda.time.ReadWritableDateTime;
import org.joda.time.ReadableDateTime;
import org.joda.time.ReadableDuration;
import org.joda.time.ReadableInstant;
import org.joda.time.ReadablePeriod;
import org.joda.time.base.BaseDateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class MutableDateTime
extends BaseDateTime
implements ReadWritableDateTime,
Cloneable,
Serializable {
    public static final int ROUND_CEILING = 2;
    public static final int ROUND_FLOOR = 1;
    public static final int ROUND_HALF_CEILING = 4;
    public static final int ROUND_HALF_EVEN = 5;
    public static final int ROUND_HALF_FLOOR = 3;
    public static final int ROUND_NONE = 0;
    private static final long serialVersionUID = 2852608688135209575L;
    private DateTimeField iRoundingField;
    private int iRoundingMode;

    public MutableDateTime() {
    }

    public MutableDateTime(int n, int n2, int n3, int n4, int n5, int n6, int n7) {
        super(n, n2, n3, n4, n5, n6, n7);
    }

    public MutableDateTime(int n, int n2, int n3, int n4, int n5, int n6, int n7, Chronology chronology) {
        super(n, n2, n3, n4, n5, n6, n7, chronology);
    }

    public MutableDateTime(int n, int n2, int n3, int n4, int n5, int n6, int n7, DateTimeZone dateTimeZone) {
        super(n, n2, n3, n4, n5, n6, n7, dateTimeZone);
    }

    public MutableDateTime(long l) {
        super(l);
    }

    public MutableDateTime(long l, Chronology chronology) {
        super(l, chronology);
    }

    public MutableDateTime(long l, DateTimeZone dateTimeZone) {
        super(l, dateTimeZone);
    }

    public MutableDateTime(Object object) {
        super(object, (Chronology)null);
    }

    public MutableDateTime(Object object, Chronology chronology) {
        super(object, DateTimeUtils.getChronology((Chronology)chronology));
    }

    public MutableDateTime(Object object, DateTimeZone dateTimeZone) {
        super(object, dateTimeZone);
    }

    public MutableDateTime(Chronology chronology) {
        super(chronology);
    }

    public MutableDateTime(DateTimeZone dateTimeZone) {
        super(dateTimeZone);
    }

    public static MutableDateTime now() {
        return new MutableDateTime();
    }

    public static MutableDateTime now(Chronology chronology) {
        if (chronology != null) {
            return new MutableDateTime(chronology);
        }
        throw new NullPointerException("Chronology must not be null");
    }

    public static MutableDateTime now(DateTimeZone dateTimeZone) {
        if (dateTimeZone != null) {
            return new MutableDateTime(dateTimeZone);
        }
        throw new NullPointerException("Zone must not be null");
    }

    @FromString
    public static MutableDateTime parse(String string) {
        return MutableDateTime.parse(string, ISODateTimeFormat.dateTimeParser().withOffsetParsed());
    }

    public static MutableDateTime parse(String string, DateTimeFormatter dateTimeFormatter) {
        return dateTimeFormatter.parseDateTime(string).toMutableDateTime();
    }

    @Override
    public void add(long l) {
        this.setMillis(FieldUtils.safeAdd((long)this.getMillis(), (long)l));
    }

    @Override
    public void add(DurationFieldType durationFieldType, int n) {
        if (durationFieldType != null) {
            if (n != 0) {
                this.setMillis(durationFieldType.getField(this.getChronology()).add(this.getMillis(), n));
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Field must not be null");
    }

    @Override
    public void add(ReadableDuration readableDuration) {
        this.add(readableDuration, 1);
    }

    @Override
    public void add(ReadableDuration readableDuration, int n) {
        if (readableDuration != null) {
            this.add(FieldUtils.safeMultiply((long)readableDuration.getMillis(), (int)n));
            return;
        }
    }

    @Override
    public void add(ReadablePeriod readablePeriod) {
        this.add(readablePeriod, 1);
    }

    @Override
    public void add(ReadablePeriod readablePeriod, int n) {
        if (readablePeriod != null) {
            this.setMillis(this.getChronology().add(readablePeriod, this.getMillis(), n));
            return;
        }
    }

    @Override
    public void addDays(int n) {
        if (n != 0) {
            this.setMillis(this.getChronology().days().add(this.getMillis(), n));
            return;
        }
    }

    @Override
    public void addHours(int n) {
        if (n != 0) {
            this.setMillis(this.getChronology().hours().add(this.getMillis(), n));
            return;
        }
    }

    @Override
    public void addMillis(int n) {
        if (n != 0) {
            this.setMillis(this.getChronology().millis().add(this.getMillis(), n));
            return;
        }
    }

    @Override
    public void addMinutes(int n) {
        if (n != 0) {
            this.setMillis(this.getChronology().minutes().add(this.getMillis(), n));
            return;
        }
    }

    @Override
    public void addMonths(int n) {
        if (n != 0) {
            this.setMillis(this.getChronology().months().add(this.getMillis(), n));
            return;
        }
    }

    @Override
    public void addSeconds(int n) {
        if (n != 0) {
            this.setMillis(this.getChronology().seconds().add(this.getMillis(), n));
            return;
        }
    }

    @Override
    public void addWeeks(int n) {
        if (n != 0) {
            this.setMillis(this.getChronology().weeks().add(this.getMillis(), n));
            return;
        }
    }

    @Override
    public void addWeekyears(int n) {
        if (n != 0) {
            this.setMillis(this.getChronology().weekyears().add(this.getMillis(), n));
            return;
        }
    }

    @Override
    public void addYears(int n) {
        if (n != 0) {
            this.setMillis(this.getChronology().years().add(this.getMillis(), n));
            return;
        }
    }

    public  centuryOfEra() {
        return new /* Unavailable Anonymous Inner Class!! */;
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

    public MutableDateTime copy() {
        return (MutableDateTime)this.clone();
    }

    public  dayOfMonth() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public  dayOfWeek() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public  dayOfYear() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public  era() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public DateTimeField getRoundingField() {
        return this.iRoundingField;
    }

    public int getRoundingMode() {
        return this.iRoundingMode;
    }

    public  hourOfDay() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public  millisOfDay() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public  millisOfSecond() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public  minuteOfDay() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public  minuteOfHour() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public  monthOfYear() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public  property(DateTimeFieldType dateTimeFieldType) {
        if (dateTimeFieldType != null) {
            Object object = dateTimeFieldType.getField(this.getChronology());
            if (object.isSupported()) {
                return new /* Unavailable Anonymous Inner Class!! */;
            }
            object = new StringBuilder();
            object.append("Field '");
            object.append((Object)dateTimeFieldType);
            object.append("' is not supported");
            throw new IllegalArgumentException(object.toString());
        }
        throw new IllegalArgumentException("The DateTimeFieldType must not be null");
    }

    public  secondOfDay() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public  secondOfMinute() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    @Override
    public void set(DateTimeFieldType dateTimeFieldType, int n) {
        if (dateTimeFieldType != null) {
            this.setMillis(dateTimeFieldType.getField(this.getChronology()).set(this.getMillis(), n));
            return;
        }
        throw new IllegalArgumentException("Field must not be null");
    }

    @Override
    public void setChronology(Chronology chronology) {
        super.setChronology(chronology);
    }

    @Override
    public void setDate(int n, int n2, int n3) {
        this.setDate(this.getChronology().getDateTimeMillis(n, n2, n3, 0));
    }

    public void setDate(long l) {
        this.setMillis(this.getChronology().millisOfDay().set(l, this.getMillisOfDay()));
    }

    public void setDate(ReadableInstant object) {
        long l = DateTimeUtils.getInstantMillis((ReadableInstant)object);
        if (object instanceof ReadableDateTime && (object = DateTimeUtils.getChronology((Chronology)((ReadableDateTime)object).getChronology()).getZone()) != null) {
            l = object.getMillisKeepLocal(this.getZone(), l);
        }
        this.setDate(l);
    }

    @Override
    public void setDateTime(int n, int n2, int n3, int n4, int n5, int n6, int n7) {
        this.setMillis(this.getChronology().getDateTimeMillis(n, n2, n3, n4, n5, n6, n7));
    }

    @Override
    public void setDayOfMonth(int n) {
        this.setMillis(this.getChronology().dayOfMonth().set(this.getMillis(), n));
    }

    @Override
    public void setDayOfWeek(int n) {
        this.setMillis(this.getChronology().dayOfWeek().set(this.getMillis(), n));
    }

    @Override
    public void setDayOfYear(int n) {
        this.setMillis(this.getChronology().dayOfYear().set(this.getMillis(), n));
    }

    @Override
    public void setHourOfDay(int n) {
        this.setMillis(this.getChronology().hourOfDay().set(this.getMillis(), n));
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @Override
    public void setMillis(long var1_1) {
        switch (this.iRoundingMode) {
            default: {
                break;
            }
            case 5: {
                var1_1 = this.iRoundingField.roundHalfEven(var1_1);
                break;
            }
            case 4: {
                var1_1 = this.iRoundingField.roundHalfCeiling(var1_1);
                break;
            }
            case 3: {
                var1_1 = this.iRoundingField.roundHalfFloor(var1_1);
                break;
            }
            case 2: {
                var1_1 = this.iRoundingField.roundCeiling(var1_1);
                break;
            }
            case 1: {
                var1_1 = this.iRoundingField.roundFloor(var1_1);
                ** break;
            }
lbl19: // 2 sources:
            case 0: 
        }
        super.setMillis(var1_1);
    }

    @Override
    public void setMillis(ReadableInstant readableInstant) {
        this.setMillis(DateTimeUtils.getInstantMillis((ReadableInstant)readableInstant));
    }

    @Override
    public void setMillisOfDay(int n) {
        this.setMillis(this.getChronology().millisOfDay().set(this.getMillis(), n));
    }

    @Override
    public void setMillisOfSecond(int n) {
        this.setMillis(this.getChronology().millisOfSecond().set(this.getMillis(), n));
    }

    @Override
    public void setMinuteOfDay(int n) {
        this.setMillis(this.getChronology().minuteOfDay().set(this.getMillis(), n));
    }

    @Override
    public void setMinuteOfHour(int n) {
        this.setMillis(this.getChronology().minuteOfHour().set(this.getMillis(), n));
    }

    @Override
    public void setMonthOfYear(int n) {
        this.setMillis(this.getChronology().monthOfYear().set(this.getMillis(), n));
    }

    public void setRounding(DateTimeField dateTimeField) {
        this.setRounding(dateTimeField, 1);
    }

    public void setRounding(DateTimeField object, int n) {
        if (object != null && (n < 0 || n > 5)) {
            object = new StringBuilder();
            object.append("Illegal rounding mode: ");
            object.append(n);
            throw new IllegalArgumentException(object.toString());
        }
        DateTimeField dateTimeField = n == 0 ? null : object;
        this.iRoundingField = dateTimeField;
        if (object == null) {
            n = 0;
        }
        this.iRoundingMode = n;
        this.setMillis(this.getMillis());
    }

    @Override
    public void setSecondOfDay(int n) {
        this.setMillis(this.getChronology().secondOfDay().set(this.getMillis(), n));
    }

    @Override
    public void setSecondOfMinute(int n) {
        this.setMillis(this.getChronology().secondOfMinute().set(this.getMillis(), n));
    }

    @Override
    public void setTime(int n, int n2, int n3, int n4) {
        this.setMillis(this.getChronology().getDateTimeMillis(this.getMillis(), n, n2, n3, n4));
    }

    public void setTime(long l) {
        int n = ISOChronology.getInstanceUTC().millisOfDay().get(l);
        this.setMillis(this.getChronology().millisOfDay().set(this.getMillis(), n));
    }

    public void setTime(ReadableInstant object) {
        long l = DateTimeUtils.getInstantMillis((ReadableInstant)object);
        if ((object = DateTimeUtils.getInstantChronology((ReadableInstant)object).getZone()) != null) {
            l = object.getMillisKeepLocal(DateTimeZone.UTC, l);
        }
        this.setTime(l);
    }

    @Override
    public void setWeekOfWeekyear(int n) {
        this.setMillis(this.getChronology().weekOfWeekyear().set(this.getMillis(), n));
    }

    @Override
    public void setWeekyear(int n) {
        this.setMillis(this.getChronology().weekyear().set(this.getMillis(), n));
    }

    @Override
    public void setYear(int n) {
        this.setMillis(this.getChronology().year().set(this.getMillis(), n));
    }

    @Override
    public void setZone(DateTimeZone dateTimeZone) {
        dateTimeZone = DateTimeUtils.getZone((DateTimeZone)dateTimeZone);
        Chronology chronology = this.getChronology();
        if (chronology.getZone() != dateTimeZone) {
            this.setChronology(chronology.withZone(dateTimeZone));
            return;
        }
    }

    @Override
    public void setZoneRetainFields(DateTimeZone dateTimeZone) {
        DateTimeZone dateTimeZone2;
        if ((dateTimeZone = DateTimeUtils.getZone((DateTimeZone)dateTimeZone)) == (dateTimeZone2 = DateTimeUtils.getZone((DateTimeZone)this.getZone()))) {
            return;
        }
        long l = dateTimeZone2.getMillisKeepLocal(dateTimeZone, this.getMillis());
        this.setChronology(this.getChronology().withZone(dateTimeZone));
        this.setMillis(l);
    }

    public  weekOfWeekyear() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public  weekyear() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public  year() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public  yearOfCentury() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }

    public  yearOfEra() {
        return new /* Unavailable Anonymous Inner Class!! */;
    }
}

