package org.joda.time.base;

import java.io.Serializable;
import java.util.Locale;
import org.joda.time.Chronology;
import org.joda.time.DateTimeUtils;
import org.joda.time.ReadablePartial;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.PartialConverter;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public abstract class BasePartial extends AbstractPartial implements ReadablePartial, Serializable {
   private static final long serialVersionUID = 2353678632973660L;
   private final Chronology iChronology;
   private final int[] iValues;

   protected BasePartial() {
      this(DateTimeUtils.currentTimeMillis(), (Chronology)null);
   }

   protected BasePartial(long var1) {
      this(var1, (Chronology)null);
   }

   protected BasePartial(long var1, Chronology var3) {
      var3 = DateTimeUtils.getChronology(var3);
      this.iChronology = var3.withUTC();
      this.iValues = var3.get(this, var1);
   }

   protected BasePartial(Object var1, Chronology var2) {
      PartialConverter var3 = ConverterManager.getInstance().getPartialConverter(var1);
      var2 = DateTimeUtils.getChronology(var3.getChronology(var1, var2));
      this.iChronology = var2.withUTC();
      this.iValues = var3.getPartialValues(this, var1, var2);
   }

   protected BasePartial(Object var1, Chronology var2, DateTimeFormatter var3) {
      PartialConverter var4 = ConverterManager.getInstance().getPartialConverter(var1);
      var2 = DateTimeUtils.getChronology(var4.getChronology(var1, var2));
      this.iChronology = var2.withUTC();
      this.iValues = var4.getPartialValues(this, var1, var2, var3);
   }

   protected BasePartial(Chronology var1) {
      this(DateTimeUtils.currentTimeMillis(), var1);
   }

   protected BasePartial(BasePartial var1, Chronology var2) {
      this.iChronology = var2.withUTC();
      this.iValues = var1.iValues;
   }

   protected BasePartial(BasePartial var1, int[] var2) {
      this.iChronology = var1.iChronology;
      this.iValues = var2;
   }

   protected BasePartial(int[] var1, Chronology var2) {
      var2 = DateTimeUtils.getChronology(var2);
      this.iChronology = var2.withUTC();
      var2.validate(this, var1);
      this.iValues = var1;
   }

   public Chronology getChronology() {
      return this.iChronology;
   }

   public int getValue(int var1) {
      return this.iValues[var1];
   }

   public int[] getValues() {
      return (int[])this.iValues.clone();
   }

   protected void setValue(int var1, int var2) {
      int[] var3 = this.getField(var1).set(this, var1, this.iValues, var2);
      int[] var4 = this.iValues;
      System.arraycopy(var3, 0, var4, 0, var4.length);
   }

   protected void setValues(int[] var1) {
      this.getChronology().validate(this, var1);
      int[] var2 = this.iValues;
      System.arraycopy(var1, 0, var2, 0, var2.length);
   }

   public String toString(String var1) {
      return var1 == null ? this.toString() : DateTimeFormat.forPattern(var1).print(this);
   }

   public String toString(String var1, Locale var2) throws IllegalArgumentException {
      return var1 == null ? this.toString() : DateTimeFormat.forPattern(var1).withLocale(var2).print(this);
   }
}
