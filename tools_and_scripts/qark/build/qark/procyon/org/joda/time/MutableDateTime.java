// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.joda.time;

import org.joda.time.chrono.ISOChronology;
import org.joda.time.field.FieldUtils;
import org.joda.time.format.DateTimeFormatter;
import org.joda.convert.FromString;
import org.joda.time.format.ISODateTimeFormat;
import java.io.Serializable;
import org.joda.time.base.BaseDateTime;

public class MutableDateTime extends BaseDateTime implements ReadWritableDateTime, Cloneable, Serializable
{
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
    
    public MutableDateTime(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7) {
        super(n, n2, n3, n4, n5, n6, n7);
    }
    
    public MutableDateTime(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final Chronology chronology) {
        super(n, n2, n3, n4, n5, n6, n7, chronology);
    }
    
    public MutableDateTime(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final DateTimeZone dateTimeZone) {
        super(n, n2, n3, n4, n5, n6, n7, dateTimeZone);
    }
    
    public MutableDateTime(final long n) {
        super(n);
    }
    
    public MutableDateTime(final long n, final Chronology chronology) {
        super(n, chronology);
    }
    
    public MutableDateTime(final long n, final DateTimeZone dateTimeZone) {
        super(n, dateTimeZone);
    }
    
    public MutableDateTime(final Object o) {
        super(o, (Chronology)null);
    }
    
    public MutableDateTime(final Object o, final Chronology chronology) {
        super(o, DateTimeUtils.getChronology(chronology));
    }
    
    public MutableDateTime(final Object o, final DateTimeZone dateTimeZone) {
        super(o, dateTimeZone);
    }
    
    public MutableDateTime(final Chronology chronology) {
        super(chronology);
    }
    
    public MutableDateTime(final DateTimeZone dateTimeZone) {
        super(dateTimeZone);
    }
    
    public static MutableDateTime now() {
        return new MutableDateTime();
    }
    
    public static MutableDateTime now(final Chronology chronology) {
        if (chronology != null) {
            return new MutableDateTime(chronology);
        }
        throw new NullPointerException("Chronology must not be null");
    }
    
    public static MutableDateTime now(final DateTimeZone dateTimeZone) {
        if (dateTimeZone != null) {
            return new MutableDateTime(dateTimeZone);
        }
        throw new NullPointerException("Zone must not be null");
    }
    
    @FromString
    public static MutableDateTime parse(final String s) {
        return parse(s, ISODateTimeFormat.dateTimeParser().withOffsetParsed());
    }
    
    public static MutableDateTime parse(final String s, final DateTimeFormatter dateTimeFormatter) {
        return dateTimeFormatter.parseDateTime(s).toMutableDateTime();
    }
    
    @Override
    public void add(final long n) {
        this.setMillis(FieldUtils.safeAdd(this.getMillis(), n));
    }
    
    @Override
    public void add(final DurationFieldType durationFieldType, final int n) {
        if (durationFieldType == null) {
            throw new IllegalArgumentException("Field must not be null");
        }
        if (n != 0) {
            this.setMillis(durationFieldType.getField(this.getChronology()).add(this.getMillis(), n));
        }
    }
    
    @Override
    public void add(final ReadableDuration readableDuration) {
        this.add(readableDuration, 1);
    }
    
    @Override
    public void add(final ReadableDuration readableDuration, final int n) {
        if (readableDuration != null) {
            this.add(FieldUtils.safeMultiply(readableDuration.getMillis(), n));
        }
    }
    
    @Override
    public void add(final ReadablePeriod readablePeriod) {
        this.add(readablePeriod, 1);
    }
    
    @Override
    public void add(final ReadablePeriod readablePeriod, final int n) {
        if (readablePeriod != null) {
            this.setMillis(this.getChronology().add(readablePeriod, this.getMillis(), n));
        }
    }
    
    @Override
    public void addDays(final int n) {
        if (n != 0) {
            this.setMillis(this.getChronology().days().add(this.getMillis(), n));
        }
    }
    
    @Override
    public void addHours(final int n) {
        if (n != 0) {
            this.setMillis(this.getChronology().hours().add(this.getMillis(), n));
        }
    }
    
    @Override
    public void addMillis(final int n) {
        if (n != 0) {
            this.setMillis(this.getChronology().millis().add(this.getMillis(), n));
        }
    }
    
    @Override
    public void addMinutes(final int n) {
        if (n != 0) {
            this.setMillis(this.getChronology().minutes().add(this.getMillis(), n));
        }
    }
    
    @Override
    public void addMonths(final int n) {
        if (n != 0) {
            this.setMillis(this.getChronology().months().add(this.getMillis(), n));
        }
    }
    
    @Override
    public void addSeconds(final int n) {
        if (n != 0) {
            this.setMillis(this.getChronology().seconds().add(this.getMillis(), n));
        }
    }
    
    @Override
    public void addWeeks(final int n) {
        if (n != 0) {
            this.setMillis(this.getChronology().weeks().add(this.getMillis(), n));
        }
    }
    
    @Override
    public void addWeekyears(final int n) {
        if (n != 0) {
            this.setMillis(this.getChronology().weekyears().add(this.getMillis(), n));
        }
    }
    
    @Override
    public void addYears(final int n) {
        if (n != 0) {
            this.setMillis(this.getChronology().years().add(this.getMillis(), n));
        }
    }
    
    public MutableDateTime.MutableDateTime$Property centuryOfEra() {
        return new MutableDateTime.MutableDateTime$Property(this, this.getChronology().centuryOfEra());
    }
    
    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException ex) {
            throw new InternalError("Clone error");
        }
    }
    
    public MutableDateTime copy() {
        return (MutableDateTime)this.clone();
    }
    
    public MutableDateTime.MutableDateTime$Property dayOfMonth() {
        return new MutableDateTime.MutableDateTime$Property(this, this.getChronology().dayOfMonth());
    }
    
    public MutableDateTime.MutableDateTime$Property dayOfWeek() {
        return new MutableDateTime.MutableDateTime$Property(this, this.getChronology().dayOfWeek());
    }
    
    public MutableDateTime.MutableDateTime$Property dayOfYear() {
        return new MutableDateTime.MutableDateTime$Property(this, this.getChronology().dayOfYear());
    }
    
    public MutableDateTime.MutableDateTime$Property era() {
        return new MutableDateTime.MutableDateTime$Property(this, this.getChronology().era());
    }
    
    public DateTimeField getRoundingField() {
        return this.iRoundingField;
    }
    
    public int getRoundingMode() {
        return this.iRoundingMode;
    }
    
    public MutableDateTime.MutableDateTime$Property hourOfDay() {
        return new MutableDateTime.MutableDateTime$Property(this, this.getChronology().hourOfDay());
    }
    
    public MutableDateTime.MutableDateTime$Property millisOfDay() {
        return new MutableDateTime.MutableDateTime$Property(this, this.getChronology().millisOfDay());
    }
    
    public MutableDateTime.MutableDateTime$Property millisOfSecond() {
        return new MutableDateTime.MutableDateTime$Property(this, this.getChronology().millisOfSecond());
    }
    
    public MutableDateTime.MutableDateTime$Property minuteOfDay() {
        return new MutableDateTime.MutableDateTime$Property(this, this.getChronology().minuteOfDay());
    }
    
    public MutableDateTime.MutableDateTime$Property minuteOfHour() {
        return new MutableDateTime.MutableDateTime$Property(this, this.getChronology().minuteOfHour());
    }
    
    public MutableDateTime.MutableDateTime$Property monthOfYear() {
        return new MutableDateTime.MutableDateTime$Property(this, this.getChronology().monthOfYear());
    }
    
    public MutableDateTime.MutableDateTime$Property property(final DateTimeFieldType dateTimeFieldType) {
        if (dateTimeFieldType == null) {
            throw new IllegalArgumentException("The DateTimeFieldType must not be null");
        }
        final DateTimeField field = dateTimeFieldType.getField(this.getChronology());
        if (field.isSupported()) {
            return new MutableDateTime.MutableDateTime$Property(this, field);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Field '");
        sb.append(dateTimeFieldType);
        sb.append("' is not supported");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public MutableDateTime.MutableDateTime$Property secondOfDay() {
        return new MutableDateTime.MutableDateTime$Property(this, this.getChronology().secondOfDay());
    }
    
    public MutableDateTime.MutableDateTime$Property secondOfMinute() {
        return new MutableDateTime.MutableDateTime$Property(this, this.getChronology().secondOfMinute());
    }
    
    @Override
    public void set(final DateTimeFieldType dateTimeFieldType, final int n) {
        if (dateTimeFieldType != null) {
            this.setMillis(dateTimeFieldType.getField(this.getChronology()).set(this.getMillis(), n));
            return;
        }
        throw new IllegalArgumentException("Field must not be null");
    }
    
    @Override
    public void setChronology(final Chronology chronology) {
        super.setChronology(chronology);
    }
    
    @Override
    public void setDate(final int n, final int n2, final int n3) {
        this.setDate(this.getChronology().getDateTimeMillis(n, n2, n3, 0));
    }
    
    public void setDate(final long n) {
        this.setMillis(this.getChronology().millisOfDay().set(n, this.getMillisOfDay()));
    }
    
    public void setDate(final ReadableInstant readableInstant) {
        long date = DateTimeUtils.getInstantMillis(readableInstant);
        if (readableInstant instanceof ReadableDateTime) {
            final DateTimeZone zone = DateTimeUtils.getChronology(((ReadableDateTime)readableInstant).getChronology()).getZone();
            if (zone != null) {
                date = zone.getMillisKeepLocal(this.getZone(), date);
            }
        }
        this.setDate(date);
    }
    
    @Override
    public void setDateTime(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7) {
        this.setMillis(this.getChronology().getDateTimeMillis(n, n2, n3, n4, n5, n6, n7));
    }
    
    @Override
    public void setDayOfMonth(final int n) {
        this.setMillis(this.getChronology().dayOfMonth().set(this.getMillis(), n));
    }
    
    @Override
    public void setDayOfWeek(final int n) {
        this.setMillis(this.getChronology().dayOfWeek().set(this.getMillis(), n));
    }
    
    @Override
    public void setDayOfYear(final int n) {
        this.setMillis(this.getChronology().dayOfYear().set(this.getMillis(), n));
    }
    
    @Override
    public void setHourOfDay(final int n) {
        this.setMillis(this.getChronology().hourOfDay().set(this.getMillis(), n));
    }
    
    @Override
    public void setMillis(long millis) {
        while (true) {
            switch (this.iRoundingMode) {
                default: {}
                case 0: {
                    super.setMillis(millis);
                }
                case 5: {
                    millis = this.iRoundingField.roundHalfEven(millis);
                    continue;
                }
                case 4: {
                    millis = this.iRoundingField.roundHalfCeiling(millis);
                    continue;
                }
                case 3: {
                    millis = this.iRoundingField.roundHalfFloor(millis);
                    continue;
                }
                case 2: {
                    millis = this.iRoundingField.roundCeiling(millis);
                    continue;
                }
                case 1: {
                    millis = this.iRoundingField.roundFloor(millis);
                    continue;
                }
            }
            break;
        }
    }
    
    @Override
    public void setMillis(final ReadableInstant readableInstant) {
        this.setMillis(DateTimeUtils.getInstantMillis(readableInstant));
    }
    
    @Override
    public void setMillisOfDay(final int n) {
        this.setMillis(this.getChronology().millisOfDay().set(this.getMillis(), n));
    }
    
    @Override
    public void setMillisOfSecond(final int n) {
        this.setMillis(this.getChronology().millisOfSecond().set(this.getMillis(), n));
    }
    
    @Override
    public void setMinuteOfDay(final int n) {
        this.setMillis(this.getChronology().minuteOfDay().set(this.getMillis(), n));
    }
    
    @Override
    public void setMinuteOfHour(final int n) {
        this.setMillis(this.getChronology().minuteOfHour().set(this.getMillis(), n));
    }
    
    @Override
    public void setMonthOfYear(final int n) {
        this.setMillis(this.getChronology().monthOfYear().set(this.getMillis(), n));
    }
    
    public void setRounding(final DateTimeField dateTimeField) {
        this.setRounding(dateTimeField, 1);
    }
    
    public void setRounding(final DateTimeField dateTimeField, int iRoundingMode) {
        if (dateTimeField != null && (iRoundingMode < 0 || iRoundingMode > 5)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Illegal rounding mode: ");
            sb.append(iRoundingMode);
            throw new IllegalArgumentException(sb.toString());
        }
        DateTimeField iRoundingField;
        if (iRoundingMode == 0) {
            iRoundingField = null;
        }
        else {
            iRoundingField = dateTimeField;
        }
        this.iRoundingField = iRoundingField;
        if (dateTimeField == null) {
            iRoundingMode = 0;
        }
        this.iRoundingMode = iRoundingMode;
        this.setMillis(this.getMillis());
    }
    
    @Override
    public void setSecondOfDay(final int n) {
        this.setMillis(this.getChronology().secondOfDay().set(this.getMillis(), n));
    }
    
    @Override
    public void setSecondOfMinute(final int n) {
        this.setMillis(this.getChronology().secondOfMinute().set(this.getMillis(), n));
    }
    
    @Override
    public void setTime(final int n, final int n2, final int n3, final int n4) {
        this.setMillis(this.getChronology().getDateTimeMillis(this.getMillis(), n, n2, n3, n4));
    }
    
    public void setTime(final long n) {
        this.setMillis(this.getChronology().millisOfDay().set(this.getMillis(), ISOChronology.getInstanceUTC().millisOfDay().get(n)));
    }
    
    public void setTime(final ReadableInstant readableInstant) {
        long time = DateTimeUtils.getInstantMillis(readableInstant);
        final DateTimeZone zone = DateTimeUtils.getInstantChronology(readableInstant).getZone();
        if (zone != null) {
            time = zone.getMillisKeepLocal(DateTimeZone.UTC, time);
        }
        this.setTime(time);
    }
    
    @Override
    public void setWeekOfWeekyear(final int n) {
        this.setMillis(this.getChronology().weekOfWeekyear().set(this.getMillis(), n));
    }
    
    @Override
    public void setWeekyear(final int n) {
        this.setMillis(this.getChronology().weekyear().set(this.getMillis(), n));
    }
    
    @Override
    public void setYear(final int n) {
        this.setMillis(this.getChronology().year().set(this.getMillis(), n));
    }
    
    @Override
    public void setZone(DateTimeZone zone) {
        zone = DateTimeUtils.getZone(zone);
        final Chronology chronology = this.getChronology();
        if (chronology.getZone() != zone) {
            this.setChronology(chronology.withZone(zone));
        }
    }
    
    @Override
    public void setZoneRetainFields(DateTimeZone zone) {
        zone = DateTimeUtils.getZone(zone);
        final DateTimeZone zone2 = DateTimeUtils.getZone(this.getZone());
        if (zone == zone2) {
            return;
        }
        final long millisKeepLocal = zone2.getMillisKeepLocal(zone, this.getMillis());
        this.setChronology(this.getChronology().withZone(zone));
        this.setMillis(millisKeepLocal);
    }
    
    public MutableDateTime.MutableDateTime$Property weekOfWeekyear() {
        return new MutableDateTime.MutableDateTime$Property(this, this.getChronology().weekOfWeekyear());
    }
    
    public MutableDateTime.MutableDateTime$Property weekyear() {
        return new MutableDateTime.MutableDateTime$Property(this, this.getChronology().weekyear());
    }
    
    public MutableDateTime.MutableDateTime$Property year() {
        return new MutableDateTime.MutableDateTime$Property(this, this.getChronology().year());
    }
    
    public MutableDateTime.MutableDateTime$Property yearOfCentury() {
        return new MutableDateTime.MutableDateTime$Property(this, this.getChronology().yearOfCentury());
    }
    
    public MutableDateTime.MutableDateTime$Property yearOfEra() {
        return new MutableDateTime.MutableDateTime$Property(this, this.getChronology().yearOfEra());
    }
}
