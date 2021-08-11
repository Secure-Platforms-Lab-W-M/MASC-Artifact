package org.joda.time.base;

import java.io.Serializable;
import org.joda.time.Chronology;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadableDateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.convert.ConverterManager;
import org.joda.time.convert.InstantConverter;

public abstract class BaseDateTime extends AbstractDateTime implements ReadableDateTime, Serializable {
   private static final long serialVersionUID = -6728882245981L;
   private volatile Chronology iChronology;
   private volatile long iMillis;

   public BaseDateTime() {
      this(DateTimeUtils.currentTimeMillis(), (Chronology)ISOChronology.getInstance());
   }

   public BaseDateTime(int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      this(var1, var2, var3, var4, var5, var6, var7, (Chronology)ISOChronology.getInstance());
   }

   public BaseDateTime(int var1, int var2, int var3, int var4, int var5, int var6, int var7, Chronology var8) {
      this.iChronology = this.checkChronology(var8);
      this.iMillis = this.checkInstant(this.iChronology.getDateTimeMillis(var1, var2, var3, var4, var5, var6, var7), this.iChronology);
      this.adjustForMinMax();
   }

   public BaseDateTime(int var1, int var2, int var3, int var4, int var5, int var6, int var7, DateTimeZone var8) {
      this(var1, var2, var3, var4, var5, var6, var7, (Chronology)ISOChronology.getInstance(var8));
   }

   public BaseDateTime(long var1) {
      this(var1, (Chronology)ISOChronology.getInstance());
   }

   public BaseDateTime(long var1, Chronology var3) {
      this.iChronology = this.checkChronology(var3);
      this.iMillis = this.checkInstant(var1, this.iChronology);
      this.adjustForMinMax();
   }

   public BaseDateTime(long var1, DateTimeZone var3) {
      this(var1, (Chronology)ISOChronology.getInstance(var3));
   }

   public BaseDateTime(Object var1, Chronology var2) {
      InstantConverter var3 = ConverterManager.getInstance().getInstantConverter(var1);
      this.iChronology = this.checkChronology(var3.getChronology(var1, var2));
      this.iMillis = this.checkInstant(var3.getInstantMillis(var1, var2), this.iChronology);
      this.adjustForMinMax();
   }

   public BaseDateTime(Object var1, DateTimeZone var2) {
      InstantConverter var3 = ConverterManager.getInstance().getInstantConverter(var1);
      Chronology var4 = this.checkChronology(var3.getChronology(var1, var2));
      this.iChronology = var4;
      this.iMillis = this.checkInstant(var3.getInstantMillis(var1, var4), var4);
      this.adjustForMinMax();
   }

   public BaseDateTime(Chronology var1) {
      this(DateTimeUtils.currentTimeMillis(), var1);
   }

   public BaseDateTime(DateTimeZone var1) {
      this(DateTimeUtils.currentTimeMillis(), (Chronology)ISOChronology.getInstance(var1));
   }

   private void adjustForMinMax() {
      if (this.iMillis == Long.MIN_VALUE || this.iMillis == Long.MAX_VALUE) {
         this.iChronology = this.iChronology.withUTC();
      }
   }

   protected Chronology checkChronology(Chronology var1) {
      return DateTimeUtils.getChronology(var1);
   }

   protected long checkInstant(long var1, Chronology var3) {
      return var1;
   }

   public Chronology getChronology() {
      return this.iChronology;
   }

   public long getMillis() {
      return this.iMillis;
   }

   protected void setChronology(Chronology var1) {
      this.iChronology = this.checkChronology(var1);
   }

   protected void setMillis(long var1) {
      this.iMillis = this.checkInstant(var1, this.iChronology);
   }
}
