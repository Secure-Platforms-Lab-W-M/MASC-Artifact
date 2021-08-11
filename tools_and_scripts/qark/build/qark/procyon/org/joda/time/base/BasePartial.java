// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package org.joda.time.base;

import java.util.Locale;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.convert.PartialConverter;
import org.joda.time.convert.ConverterManager;
import org.joda.time.DateTimeUtils;
import org.joda.time.Chronology;
import java.io.Serializable;
import org.joda.time.ReadablePartial;

public abstract class BasePartial extends AbstractPartial implements ReadablePartial, Serializable
{
    private static final long serialVersionUID = 2353678632973660L;
    private final Chronology iChronology;
    private final int[] iValues;
    
    protected BasePartial() {
        this(DateTimeUtils.currentTimeMillis(), null);
    }
    
    protected BasePartial(final long n) {
        this(n, null);
    }
    
    protected BasePartial(final long n, Chronology chronology) {
        chronology = DateTimeUtils.getChronology(chronology);
        this.iChronology = chronology.withUTC();
        this.iValues = chronology.get((ReadablePartial)this, n);
    }
    
    protected BasePartial(final Object o, Chronology chronology) {
        final PartialConverter partialConverter = ConverterManager.getInstance().getPartialConverter(o);
        chronology = DateTimeUtils.getChronology(partialConverter.getChronology(o, chronology));
        this.iChronology = chronology.withUTC();
        this.iValues = partialConverter.getPartialValues((ReadablePartial)this, o, chronology);
    }
    
    protected BasePartial(final Object o, Chronology chronology, final DateTimeFormatter dateTimeFormatter) {
        final PartialConverter partialConverter = ConverterManager.getInstance().getPartialConverter(o);
        chronology = DateTimeUtils.getChronology(partialConverter.getChronology(o, chronology));
        this.iChronology = chronology.withUTC();
        this.iValues = partialConverter.getPartialValues((ReadablePartial)this, o, chronology, dateTimeFormatter);
    }
    
    protected BasePartial(final Chronology chronology) {
        this(DateTimeUtils.currentTimeMillis(), chronology);
    }
    
    protected BasePartial(final BasePartial basePartial, final Chronology chronology) {
        this.iChronology = chronology.withUTC();
        this.iValues = basePartial.iValues;
    }
    
    protected BasePartial(final BasePartial basePartial, final int[] iValues) {
        this.iChronology = basePartial.iChronology;
        this.iValues = iValues;
    }
    
    protected BasePartial(final int[] iValues, Chronology chronology) {
        chronology = DateTimeUtils.getChronology(chronology);
        this.iChronology = chronology.withUTC();
        chronology.validate((ReadablePartial)this, iValues);
        this.iValues = iValues;
    }
    
    @Override
    public Chronology getChronology() {
        return this.iChronology;
    }
    
    @Override
    public int getValue(final int n) {
        return this.iValues[n];
    }
    
    @Override
    public int[] getValues() {
        return this.iValues.clone();
    }
    
    protected void setValue(final int n, final int n2) {
        final int[] set = this.getField(n).set((ReadablePartial)this, n, this.iValues, n2);
        final int[] iValues = this.iValues;
        System.arraycopy(set, 0, iValues, 0, iValues.length);
    }
    
    protected void setValues(final int[] array) {
        this.getChronology().validate((ReadablePartial)this, array);
        final int[] iValues = this.iValues;
        System.arraycopy(array, 0, iValues, 0, iValues.length);
    }
    
    public String toString(final String s) {
        if (s == null) {
            return this.toString();
        }
        return DateTimeFormat.forPattern(s).print((ReadablePartial)this);
    }
    
    public String toString(final String s, final Locale locale) throws IllegalArgumentException {
        if (s == null) {
            return this.toString();
        }
        return DateTimeFormat.forPattern(s).withLocale(locale).print((ReadablePartial)this);
    }
}
