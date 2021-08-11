/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  org.joda.time.Chronology
 *  org.joda.time.DateTimeField
 *  org.joda.time.DateTimeUtils
 *  org.joda.time.convert.ConverterManager
 *  org.joda.time.convert.PartialConverter
 *  org.joda.time.format.DateTimeFormat
 *  org.joda.time.format.DateTimeFormatter
 */
package org.joda.time.base;

import java.io.Serializable;
import java.util.Locale;
import org.joda.time.Chronology;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeUtils;
import org.joda.time.ReadablePartial;
import org.joda.time.base.AbstractPartial;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.PartialConverter;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public abstract class BasePartial
extends AbstractPartial
implements ReadablePartial,
Serializable {
    private static final long serialVersionUID = 2353678632973660L;
    private final Chronology iChronology;
    private final int[] iValues;

    protected BasePartial() {
        this(DateTimeUtils.currentTimeMillis(), (Chronology)null);
    }

    protected BasePartial(long l) {
        this(l, (Chronology)null);
    }

    protected BasePartial(long l, Chronology chronology) {
        chronology = DateTimeUtils.getChronology((Chronology)chronology);
        this.iChronology = chronology.withUTC();
        this.iValues = chronology.get((ReadablePartial)this, l);
    }

    protected BasePartial(Object object, Chronology chronology) {
        PartialConverter partialConverter = ConverterManager.getInstance().getPartialConverter(object);
        chronology = DateTimeUtils.getChronology((Chronology)partialConverter.getChronology(object, chronology));
        this.iChronology = chronology.withUTC();
        this.iValues = partialConverter.getPartialValues((ReadablePartial)this, object, chronology);
    }

    protected BasePartial(Object object, Chronology chronology, DateTimeFormatter dateTimeFormatter) {
        PartialConverter partialConverter = ConverterManager.getInstance().getPartialConverter(object);
        chronology = DateTimeUtils.getChronology((Chronology)partialConverter.getChronology(object, chronology));
        this.iChronology = chronology.withUTC();
        this.iValues = partialConverter.getPartialValues((ReadablePartial)this, object, chronology, dateTimeFormatter);
    }

    protected BasePartial(Chronology chronology) {
        this(DateTimeUtils.currentTimeMillis(), chronology);
    }

    protected BasePartial(BasePartial basePartial, Chronology chronology) {
        this.iChronology = chronology.withUTC();
        this.iValues = basePartial.iValues;
    }

    protected BasePartial(BasePartial basePartial, int[] arrn) {
        this.iChronology = basePartial.iChronology;
        this.iValues = arrn;
    }

    protected BasePartial(int[] arrn, Chronology chronology) {
        chronology = DateTimeUtils.getChronology((Chronology)chronology);
        this.iChronology = chronology.withUTC();
        chronology.validate((ReadablePartial)this, arrn);
        this.iValues = arrn;
    }

    @Override
    public Chronology getChronology() {
        return this.iChronology;
    }

    @Override
    public int getValue(int n) {
        return this.iValues[n];
    }

    @Override
    public int[] getValues() {
        return (int[])this.iValues.clone();
    }

    protected void setValue(int n, int n2) {
        int[] arrn = this.getField(n).set((ReadablePartial)this, n, this.iValues, n2);
        int[] arrn2 = this.iValues;
        System.arraycopy(arrn, 0, arrn2, 0, arrn2.length);
    }

    protected void setValues(int[] arrn) {
        this.getChronology().validate((ReadablePartial)this, arrn);
        int[] arrn2 = this.iValues;
        System.arraycopy(arrn, 0, arrn2, 0, arrn2.length);
    }

    public String toString(String string) {
        if (string == null) {
            return this.toString();
        }
        return DateTimeFormat.forPattern((String)string).print((ReadablePartial)this);
    }

    public String toString(String string, Locale locale) throws IllegalArgumentException {
        if (string == null) {
            return this.toString();
        }
        return DateTimeFormat.forPattern((String)string).withLocale(locale).print((ReadablePartial)this);
    }
}

