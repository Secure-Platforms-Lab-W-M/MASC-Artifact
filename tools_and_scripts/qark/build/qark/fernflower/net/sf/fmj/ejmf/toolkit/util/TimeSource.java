package net.sf.fmj.ejmf.toolkit.util;

public interface TimeSource {
   long MICROS_PER_SEC = 1000000L;
   long MILLIS_PER_SEC = 1000L;
   long NANOS_PER_SEC = 1000000000L;

   long getConversionDivisor();

   long getTime();
}
