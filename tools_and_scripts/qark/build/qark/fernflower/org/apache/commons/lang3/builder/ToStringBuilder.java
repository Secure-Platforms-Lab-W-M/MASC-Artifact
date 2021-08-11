package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;

public class ToStringBuilder implements Builder {
   private static volatile ToStringStyle defaultStyle;
   private final StringBuffer buffer;
   private final Object object;
   private final ToStringStyle style;

   static {
      defaultStyle = ToStringStyle.DEFAULT_STYLE;
   }

   public ToStringBuilder(Object var1) {
      this(var1, (ToStringStyle)null, (StringBuffer)null);
   }

   public ToStringBuilder(Object var1, ToStringStyle var2) {
      this(var1, var2, (StringBuffer)null);
   }

   public ToStringBuilder(Object var1, ToStringStyle var2, StringBuffer var3) {
      ToStringStyle var4 = var2;
      if (var2 == null) {
         var4 = getDefaultStyle();
      }

      StringBuffer var5 = var3;
      if (var3 == null) {
         var5 = new StringBuffer(512);
      }

      this.buffer = var5;
      this.style = var4;
      this.object = var1;
      var4.appendStart(var5, var1);
   }

   public static ToStringStyle getDefaultStyle() {
      return defaultStyle;
   }

   public static String reflectionToString(Object var0) {
      return ReflectionToStringBuilder.toString(var0);
   }

   public static String reflectionToString(Object var0, ToStringStyle var1) {
      return ReflectionToStringBuilder.toString(var0, var1);
   }

   public static String reflectionToString(Object var0, ToStringStyle var1, boolean var2) {
      return ReflectionToStringBuilder.toString(var0, var1, var2, false, (Class)null);
   }

   public static String reflectionToString(Object var0, ToStringStyle var1, boolean var2, Class var3) {
      return ReflectionToStringBuilder.toString(var0, var1, var2, false, var3);
   }

   public static void setDefaultStyle(ToStringStyle var0) {
      boolean var1;
      if (var0 != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      Validate.isTrue(var1, "The style must not be null");
      defaultStyle = var0;
   }

   public ToStringBuilder append(byte var1) {
      this.style.append(this.buffer, (String)null, (byte)var1);
      return this;
   }

   public ToStringBuilder append(char var1) {
      this.style.append(this.buffer, (String)null, (char)var1);
      return this;
   }

   public ToStringBuilder append(double var1) {
      this.style.append(this.buffer, (String)null, var1);
      return this;
   }

   public ToStringBuilder append(float var1) {
      this.style.append(this.buffer, (String)null, var1);
      return this;
   }

   public ToStringBuilder append(int var1) {
      this.style.append(this.buffer, (String)null, (int)var1);
      return this;
   }

   public ToStringBuilder append(long var1) {
      this.style.append(this.buffer, (String)null, var1);
      return this;
   }

   public ToStringBuilder append(Object var1) {
      this.style.append(this.buffer, (String)null, (Object)var1, (Boolean)null);
      return this;
   }

   public ToStringBuilder append(String var1, byte var2) {
      this.style.append(this.buffer, var1, var2);
      return this;
   }

   public ToStringBuilder append(String var1, char var2) {
      this.style.append(this.buffer, var1, var2);
      return this;
   }

   public ToStringBuilder append(String var1, double var2) {
      this.style.append(this.buffer, var1, var2);
      return this;
   }

   public ToStringBuilder append(String var1, float var2) {
      this.style.append(this.buffer, var1, var2);
      return this;
   }

   public ToStringBuilder append(String var1, int var2) {
      this.style.append(this.buffer, var1, var2);
      return this;
   }

   public ToStringBuilder append(String var1, long var2) {
      this.style.append(this.buffer, var1, var2);
      return this;
   }

   public ToStringBuilder append(String var1, Object var2) {
      this.style.append(this.buffer, var1, (Object)var2, (Boolean)null);
      return this;
   }

   public ToStringBuilder append(String var1, Object var2, boolean var3) {
      this.style.append(this.buffer, var1, var2, var3);
      return this;
   }

   public ToStringBuilder append(String var1, short var2) {
      this.style.append(this.buffer, var1, var2);
      return this;
   }

   public ToStringBuilder append(String var1, boolean var2) {
      this.style.append(this.buffer, var1, var2);
      return this;
   }

   public ToStringBuilder append(String var1, byte[] var2) {
      this.style.append(this.buffer, var1, (byte[])var2, (Boolean)null);
      return this;
   }

   public ToStringBuilder append(String var1, byte[] var2, boolean var3) {
      this.style.append(this.buffer, var1, var2, var3);
      return this;
   }

   public ToStringBuilder append(String var1, char[] var2) {
      this.style.append(this.buffer, var1, (char[])var2, (Boolean)null);
      return this;
   }

   public ToStringBuilder append(String var1, char[] var2, boolean var3) {
      this.style.append(this.buffer, var1, var2, var3);
      return this;
   }

   public ToStringBuilder append(String var1, double[] var2) {
      this.style.append(this.buffer, var1, (double[])var2, (Boolean)null);
      return this;
   }

   public ToStringBuilder append(String var1, double[] var2, boolean var3) {
      this.style.append(this.buffer, var1, var2, var3);
      return this;
   }

   public ToStringBuilder append(String var1, float[] var2) {
      this.style.append(this.buffer, var1, (float[])var2, (Boolean)null);
      return this;
   }

   public ToStringBuilder append(String var1, float[] var2, boolean var3) {
      this.style.append(this.buffer, var1, var2, var3);
      return this;
   }

   public ToStringBuilder append(String var1, int[] var2) {
      this.style.append(this.buffer, var1, (int[])var2, (Boolean)null);
      return this;
   }

   public ToStringBuilder append(String var1, int[] var2, boolean var3) {
      this.style.append(this.buffer, var1, var2, var3);
      return this;
   }

   public ToStringBuilder append(String var1, long[] var2) {
      this.style.append(this.buffer, var1, (long[])var2, (Boolean)null);
      return this;
   }

   public ToStringBuilder append(String var1, long[] var2, boolean var3) {
      this.style.append(this.buffer, var1, var2, var3);
      return this;
   }

   public ToStringBuilder append(String var1, Object[] var2) {
      this.style.append(this.buffer, var1, (Object[])var2, (Boolean)null);
      return this;
   }

   public ToStringBuilder append(String var1, Object[] var2, boolean var3) {
      this.style.append(this.buffer, var1, var2, var3);
      return this;
   }

   public ToStringBuilder append(String var1, short[] var2) {
      this.style.append(this.buffer, var1, (short[])var2, (Boolean)null);
      return this;
   }

   public ToStringBuilder append(String var1, short[] var2, boolean var3) {
      this.style.append(this.buffer, var1, var2, var3);
      return this;
   }

   public ToStringBuilder append(String var1, boolean[] var2) {
      this.style.append(this.buffer, var1, (boolean[])var2, (Boolean)null);
      return this;
   }

   public ToStringBuilder append(String var1, boolean[] var2, boolean var3) {
      this.style.append(this.buffer, var1, var2, var3);
      return this;
   }

   public ToStringBuilder append(short var1) {
      this.style.append(this.buffer, (String)null, (short)var1);
      return this;
   }

   public ToStringBuilder append(boolean var1) {
      this.style.append(this.buffer, (String)null, var1);
      return this;
   }

   public ToStringBuilder append(byte[] var1) {
      this.style.append(this.buffer, (String)null, (byte[])var1, (Boolean)null);
      return this;
   }

   public ToStringBuilder append(char[] var1) {
      this.style.append(this.buffer, (String)null, (char[])var1, (Boolean)null);
      return this;
   }

   public ToStringBuilder append(double[] var1) {
      this.style.append(this.buffer, (String)null, (double[])var1, (Boolean)null);
      return this;
   }

   public ToStringBuilder append(float[] var1) {
      this.style.append(this.buffer, (String)null, (float[])var1, (Boolean)null);
      return this;
   }

   public ToStringBuilder append(int[] var1) {
      this.style.append(this.buffer, (String)null, (int[])var1, (Boolean)null);
      return this;
   }

   public ToStringBuilder append(long[] var1) {
      this.style.append(this.buffer, (String)null, (long[])var1, (Boolean)null);
      return this;
   }

   public ToStringBuilder append(Object[] var1) {
      this.style.append(this.buffer, (String)null, (Object[])var1, (Boolean)null);
      return this;
   }

   public ToStringBuilder append(short[] var1) {
      this.style.append(this.buffer, (String)null, (short[])var1, (Boolean)null);
      return this;
   }

   public ToStringBuilder append(boolean[] var1) {
      this.style.append(this.buffer, (String)null, (boolean[])var1, (Boolean)null);
      return this;
   }

   public ToStringBuilder appendAsObjectToString(Object var1) {
      ObjectUtils.identityToString(this.getStringBuffer(), var1);
      return this;
   }

   public ToStringBuilder appendSuper(String var1) {
      if (var1 != null) {
         this.style.appendSuper(this.buffer, var1);
      }

      return this;
   }

   public ToStringBuilder appendToString(String var1) {
      if (var1 != null) {
         this.style.appendToString(this.buffer, var1);
      }

      return this;
   }

   public String build() {
      return this.toString();
   }

   public Object getObject() {
      return this.object;
   }

   public StringBuffer getStringBuffer() {
      return this.buffer;
   }

   public ToStringStyle getStyle() {
      return this.style;
   }

   public String toString() {
      if (this.getObject() == null) {
         this.getStringBuffer().append(this.getStyle().getNullText());
      } else {
         this.style.appendEnd(this.getStringBuffer(), this.getObject());
      }

      return this.getStringBuffer().toString();
   }
}
