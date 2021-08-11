package org.apache.commons.lang3;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.mutable.MutableInt;

public class ArrayUtils {
   public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
   public static final Boolean[] EMPTY_BOOLEAN_OBJECT_ARRAY = new Boolean[0];
   public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
   public static final Byte[] EMPTY_BYTE_OBJECT_ARRAY = new Byte[0];
   public static final Character[] EMPTY_CHARACTER_OBJECT_ARRAY = new Character[0];
   public static final char[] EMPTY_CHAR_ARRAY = new char[0];
   public static final Class[] EMPTY_CLASS_ARRAY = new Class[0];
   public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
   public static final Double[] EMPTY_DOUBLE_OBJECT_ARRAY = new Double[0];
   public static final float[] EMPTY_FLOAT_ARRAY = new float[0];
   public static final Float[] EMPTY_FLOAT_OBJECT_ARRAY = new Float[0];
   public static final Integer[] EMPTY_INTEGER_OBJECT_ARRAY = new Integer[0];
   public static final int[] EMPTY_INT_ARRAY = new int[0];
   public static final long[] EMPTY_LONG_ARRAY = new long[0];
   public static final Long[] EMPTY_LONG_OBJECT_ARRAY = new Long[0];
   public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
   public static final short[] EMPTY_SHORT_ARRAY = new short[0];
   public static final Short[] EMPTY_SHORT_OBJECT_ARRAY = new Short[0];
   public static final String[] EMPTY_STRING_ARRAY = new String[0];
   public static final int INDEX_NOT_FOUND = -1;

   private static Object add(Object var0, int var1, Object var2, Class var3) {
      StringBuilder var5;
      if (var0 == null) {
         if (var1 == 0) {
            var0 = Array.newInstance(var3, 1);
            Array.set(var0, 0, var2);
            return var0;
         } else {
            var5 = new StringBuilder();
            var5.append("Index: ");
            var5.append(var1);
            var5.append(", Length: 0");
            throw new IndexOutOfBoundsException(var5.toString());
         }
      } else {
         int var4 = Array.getLength(var0);
         if (var1 <= var4 && var1 >= 0) {
            Object var6 = Array.newInstance(var3, var4 + 1);
            System.arraycopy(var0, 0, var6, 0, var1);
            Array.set(var6, var1, var2);
            if (var1 < var4) {
               System.arraycopy(var0, var1, var6, var1 + 1, var4 - var1);
            }

            return var6;
         } else {
            var5 = new StringBuilder();
            var5.append("Index: ");
            var5.append(var1);
            var5.append(", Length: ");
            var5.append(var4);
            throw new IndexOutOfBoundsException(var5.toString());
         }
      }
   }

   public static byte[] add(byte[] var0, byte var1) {
      var0 = (byte[])copyArrayGrow1(var0, Byte.TYPE);
      var0[var0.length - 1] = var1;
      return var0;
   }

   @Deprecated
   public static byte[] add(byte[] var0, int var1, byte var2) {
      return (byte[])add(var0, var1, var2, Byte.TYPE);
   }

   public static char[] add(char[] var0, char var1) {
      var0 = (char[])copyArrayGrow1(var0, Character.TYPE);
      var0[var0.length - 1] = var1;
      return var0;
   }

   @Deprecated
   public static char[] add(char[] var0, int var1, char var2) {
      return (char[])add(var0, var1, var2, Character.TYPE);
   }

   public static double[] add(double[] var0, double var1) {
      var0 = (double[])copyArrayGrow1(var0, Double.TYPE);
      var0[var0.length - 1] = var1;
      return var0;
   }

   @Deprecated
   public static double[] add(double[] var0, int var1, double var2) {
      return (double[])add(var0, var1, var2, Double.TYPE);
   }

   public static float[] add(float[] var0, float var1) {
      var0 = (float[])copyArrayGrow1(var0, Float.TYPE);
      var0[var0.length - 1] = var1;
      return var0;
   }

   @Deprecated
   public static float[] add(float[] var0, int var1, float var2) {
      return (float[])add(var0, var1, var2, Float.TYPE);
   }

   public static int[] add(int[] var0, int var1) {
      var0 = (int[])copyArrayGrow1(var0, Integer.TYPE);
      var0[var0.length - 1] = var1;
      return var0;
   }

   @Deprecated
   public static int[] add(int[] var0, int var1, int var2) {
      return (int[])add(var0, var1, var2, Integer.TYPE);
   }

   @Deprecated
   public static long[] add(long[] var0, int var1, long var2) {
      return (long[])add(var0, var1, var2, Long.TYPE);
   }

   public static long[] add(long[] var0, long var1) {
      var0 = (long[])copyArrayGrow1(var0, Long.TYPE);
      var0[var0.length - 1] = var1;
      return var0;
   }

   @Deprecated
   public static Object[] add(Object[] var0, int var1, Object var2) {
      Class var3;
      if (var0 != null) {
         var3 = var0.getClass().getComponentType();
      } else {
         if (var2 == null) {
            throw new IllegalArgumentException("Array and element cannot both be null");
         }

         var3 = var2.getClass();
      }

      return (Object[])add(var0, var1, var2, var3);
   }

   public static Object[] add(Object[] var0, Object var1) {
      Class var2;
      if (var0 != null) {
         var2 = var0.getClass().getComponentType();
      } else {
         if (var1 == null) {
            throw new IllegalArgumentException("Arguments cannot both be null");
         }

         var2 = var1.getClass();
      }

      var0 = (Object[])copyArrayGrow1(var0, var2);
      var0[var0.length - 1] = var1;
      return var0;
   }

   @Deprecated
   public static short[] add(short[] var0, int var1, short var2) {
      return (short[])add(var0, var1, var2, Short.TYPE);
   }

   public static short[] add(short[] var0, short var1) {
      var0 = (short[])copyArrayGrow1(var0, Short.TYPE);
      var0[var0.length - 1] = var1;
      return var0;
   }

   @Deprecated
   public static boolean[] add(boolean[] var0, int var1, boolean var2) {
      return (boolean[])add(var0, var1, var2, Boolean.TYPE);
   }

   public static boolean[] add(boolean[] var0, boolean var1) {
      var0 = (boolean[])copyArrayGrow1(var0, Boolean.TYPE);
      var0[var0.length - 1] = var1;
      return var0;
   }

   public static byte[] addAll(byte[] var0, byte... var1) {
      if (var0 == null) {
         return clone(var1);
      } else if (var1 == null) {
         return clone(var0);
      } else {
         byte[] var2 = new byte[var0.length + var1.length];
         System.arraycopy(var0, 0, var2, 0, var0.length);
         System.arraycopy(var1, 0, var2, var0.length, var1.length);
         return var2;
      }
   }

   public static char[] addAll(char[] var0, char... var1) {
      if (var0 == null) {
         return clone(var1);
      } else if (var1 == null) {
         return clone(var0);
      } else {
         char[] var2 = new char[var0.length + var1.length];
         System.arraycopy(var0, 0, var2, 0, var0.length);
         System.arraycopy(var1, 0, var2, var0.length, var1.length);
         return var2;
      }
   }

   public static double[] addAll(double[] var0, double... var1) {
      if (var0 == null) {
         return clone(var1);
      } else if (var1 == null) {
         return clone(var0);
      } else {
         double[] var2 = new double[var0.length + var1.length];
         System.arraycopy(var0, 0, var2, 0, var0.length);
         System.arraycopy(var1, 0, var2, var0.length, var1.length);
         return var2;
      }
   }

   public static float[] addAll(float[] var0, float... var1) {
      if (var0 == null) {
         return clone(var1);
      } else if (var1 == null) {
         return clone(var0);
      } else {
         float[] var2 = new float[var0.length + var1.length];
         System.arraycopy(var0, 0, var2, 0, var0.length);
         System.arraycopy(var1, 0, var2, var0.length, var1.length);
         return var2;
      }
   }

   public static int[] addAll(int[] var0, int... var1) {
      if (var0 == null) {
         return clone(var1);
      } else if (var1 == null) {
         return clone(var0);
      } else {
         int[] var2 = new int[var0.length + var1.length];
         System.arraycopy(var0, 0, var2, 0, var0.length);
         System.arraycopy(var1, 0, var2, var0.length, var1.length);
         return var2;
      }
   }

   public static long[] addAll(long[] var0, long... var1) {
      if (var0 == null) {
         return clone(var1);
      } else if (var1 == null) {
         return clone(var0);
      } else {
         long[] var2 = new long[var0.length + var1.length];
         System.arraycopy(var0, 0, var2, 0, var0.length);
         System.arraycopy(var1, 0, var2, var0.length, var1.length);
         return var2;
      }
   }

   public static Object[] addAll(Object[] var0, Object... var1) {
      if (var0 == null) {
         return clone(var1);
      } else if (var1 == null) {
         return clone(var0);
      } else {
         Class var2 = var0.getClass().getComponentType();
         Object[] var3 = (Object[])Array.newInstance(var2, var0.length + var1.length);
         System.arraycopy(var0, 0, var3, 0, var0.length);

         try {
            System.arraycopy(var1, 0, var3, var0.length, var1.length);
            return var3;
         } catch (ArrayStoreException var4) {
            Class var5 = var1.getClass().getComponentType();
            if (!var2.isAssignableFrom(var5)) {
               StringBuilder var6 = new StringBuilder();
               var6.append("Cannot store ");
               var6.append(var5.getName());
               var6.append(" in an array of ");
               var6.append(var2.getName());
               throw new IllegalArgumentException(var6.toString(), var4);
            } else {
               throw var4;
            }
         }
      }
   }

   public static short[] addAll(short[] var0, short... var1) {
      if (var0 == null) {
         return clone(var1);
      } else if (var1 == null) {
         return clone(var0);
      } else {
         short[] var2 = new short[var0.length + var1.length];
         System.arraycopy(var0, 0, var2, 0, var0.length);
         System.arraycopy(var1, 0, var2, var0.length, var1.length);
         return var2;
      }
   }

   public static boolean[] addAll(boolean[] var0, boolean... var1) {
      if (var0 == null) {
         return clone(var1);
      } else if (var1 == null) {
         return clone(var0);
      } else {
         boolean[] var2 = new boolean[var0.length + var1.length];
         System.arraycopy(var0, 0, var2, 0, var0.length);
         System.arraycopy(var1, 0, var2, var0.length, var1.length);
         return var2;
      }
   }

   public static byte[] clone(byte[] var0) {
      return var0 == null ? null : (byte[])var0.clone();
   }

   public static char[] clone(char[] var0) {
      return var0 == null ? null : (char[])var0.clone();
   }

   public static double[] clone(double[] var0) {
      return var0 == null ? null : (double[])var0.clone();
   }

   public static float[] clone(float[] var0) {
      return var0 == null ? null : (float[])var0.clone();
   }

   public static int[] clone(int[] var0) {
      return var0 == null ? null : (int[])var0.clone();
   }

   public static long[] clone(long[] var0) {
      return var0 == null ? null : (long[])var0.clone();
   }

   public static Object[] clone(Object[] var0) {
      return var0 == null ? null : (Object[])var0.clone();
   }

   public static short[] clone(short[] var0) {
      return var0 == null ? null : (short[])var0.clone();
   }

   public static boolean[] clone(boolean[] var0) {
      return var0 == null ? null : (boolean[])var0.clone();
   }

   public static boolean contains(byte[] var0, byte var1) {
      return indexOf(var0, var1) != -1;
   }

   public static boolean contains(char[] var0, char var1) {
      return indexOf(var0, var1) != -1;
   }

   public static boolean contains(double[] var0, double var1) {
      return indexOf(var0, var1) != -1;
   }

   public static boolean contains(double[] var0, double var1, double var3) {
      return indexOf(var0, var1, 0, var3) != -1;
   }

   public static boolean contains(float[] var0, float var1) {
      return indexOf(var0, var1) != -1;
   }

   public static boolean contains(int[] var0, int var1) {
      return indexOf(var0, var1) != -1;
   }

   public static boolean contains(long[] var0, long var1) {
      return indexOf(var0, var1) != -1;
   }

   public static boolean contains(Object[] var0, Object var1) {
      return indexOf(var0, var1) != -1;
   }

   public static boolean contains(short[] var0, short var1) {
      return indexOf(var0, var1) != -1;
   }

   public static boolean contains(boolean[] var0, boolean var1) {
      return indexOf(var0, var1) != -1;
   }

   private static Object copyArrayGrow1(Object var0, Class var1) {
      if (var0 != null) {
         int var2 = Array.getLength(var0);
         Object var3 = Array.newInstance(var0.getClass().getComponentType(), var2 + 1);
         System.arraycopy(var0, 0, var3, 0, var2);
         return var3;
      } else {
         return Array.newInstance(var1, 1);
      }
   }

   public static int getLength(Object var0) {
      return var0 == null ? 0 : Array.getLength(var0);
   }

   public static int hashCode(Object var0) {
      return (new HashCodeBuilder()).append(var0).toHashCode();
   }

   public static int indexOf(byte[] var0, byte var1) {
      return indexOf((byte[])var0, (byte)var1, 0);
   }

   public static int indexOf(byte[] var0, byte var1, int var2) {
      if (var0 == null) {
         return -1;
      } else {
         int var3 = var2;
         if (var2 < 0) {
            var3 = 0;
         }

         while(var3 < var0.length) {
            if (var1 == var0[var3]) {
               return var3;
            }

            ++var3;
         }

         return -1;
      }
   }

   public static int indexOf(char[] var0, char var1) {
      return indexOf((char[])var0, (char)var1, 0);
   }

   public static int indexOf(char[] var0, char var1, int var2) {
      if (var0 == null) {
         return -1;
      } else {
         int var3 = var2;
         if (var2 < 0) {
            var3 = 0;
         }

         while(var3 < var0.length) {
            if (var1 == var0[var3]) {
               return var3;
            }

            ++var3;
         }

         return -1;
      }
   }

   public static int indexOf(double[] var0, double var1) {
      return indexOf(var0, var1, 0);
   }

   public static int indexOf(double[] var0, double var1, double var3) {
      return indexOf(var0, var1, 0, var3);
   }

   public static int indexOf(double[] var0, double var1, int var3) {
      if (isEmpty(var0)) {
         return -1;
      } else {
         int var4 = var3;
         if (var3 < 0) {
            var4 = 0;
         }

         while(var4 < var0.length) {
            if (var1 == var0[var4]) {
               return var4;
            }

            ++var4;
         }

         return -1;
      }
   }

   public static int indexOf(double[] var0, double var1, int var3, double var4) {
      if (isEmpty(var0)) {
         return -1;
      } else {
         int var6 = var3;
         if (var3 < 0) {
            var6 = 0;
         }

         while(var6 < var0.length) {
            if (var0[var6] >= var1 - var4 && var0[var6] <= var1 + var4) {
               return var6;
            }

            ++var6;
         }

         return -1;
      }
   }

   public static int indexOf(float[] var0, float var1) {
      return indexOf(var0, var1, 0);
   }

   public static int indexOf(float[] var0, float var1, int var2) {
      if (isEmpty(var0)) {
         return -1;
      } else {
         int var3 = var2;
         if (var2 < 0) {
            var3 = 0;
         }

         while(var3 < var0.length) {
            if (var1 == var0[var3]) {
               return var3;
            }

            ++var3;
         }

         return -1;
      }
   }

   public static int indexOf(int[] var0, int var1) {
      return indexOf((int[])var0, (int)var1, 0);
   }

   public static int indexOf(int[] var0, int var1, int var2) {
      if (var0 == null) {
         return -1;
      } else {
         int var3 = var2;
         if (var2 < 0) {
            var3 = 0;
         }

         while(var3 < var0.length) {
            if (var1 == var0[var3]) {
               return var3;
            }

            ++var3;
         }

         return -1;
      }
   }

   public static int indexOf(long[] var0, long var1) {
      return indexOf(var0, var1, 0);
   }

   public static int indexOf(long[] var0, long var1, int var3) {
      if (var0 == null) {
         return -1;
      } else {
         int var4 = var3;
         if (var3 < 0) {
            var4 = 0;
         }

         while(var4 < var0.length) {
            if (var1 == var0[var4]) {
               return var4;
            }

            ++var4;
         }

         return -1;
      }
   }

   public static int indexOf(Object[] var0, Object var1) {
      return indexOf(var0, var1, 0);
   }

   public static int indexOf(Object[] var0, Object var1, int var2) {
      if (var0 == null) {
         return -1;
      } else {
         int var3 = var2;
         if (var2 < 0) {
            var3 = 0;
         }

         if (var1 == null) {
            while(var3 < var0.length) {
               if (var0[var3] == null) {
                  return var3;
               }

               ++var3;
            }

            return -1;
         } else {
            while(var3 < var0.length) {
               if (var1.equals(var0[var3])) {
                  return var3;
               }

               ++var3;
            }

            return -1;
         }
      }
   }

   public static int indexOf(short[] var0, short var1) {
      return indexOf((short[])var0, (short)var1, 0);
   }

   public static int indexOf(short[] var0, short var1, int var2) {
      if (var0 == null) {
         return -1;
      } else {
         int var3 = var2;
         if (var2 < 0) {
            var3 = 0;
         }

         while(var3 < var0.length) {
            if (var1 == var0[var3]) {
               return var3;
            }

            ++var3;
         }

         return -1;
      }
   }

   public static int indexOf(boolean[] var0, boolean var1) {
      return indexOf(var0, var1, 0);
   }

   public static int indexOf(boolean[] var0, boolean var1, int var2) {
      if (isEmpty(var0)) {
         return -1;
      } else {
         int var3 = var2;
         if (var2 < 0) {
            var3 = 0;
         }

         while(var3 < var0.length) {
            if (var1 == var0[var3]) {
               return var3;
            }

            ++var3;
         }

         return -1;
      }
   }

   public static byte[] insert(int var0, byte[] var1, byte... var2) {
      if (var1 == null) {
         return null;
      } else if (var2 != null && var2.length != 0) {
         if (var0 >= 0 && var0 <= var1.length) {
            byte[] var3 = new byte[var1.length + var2.length];
            System.arraycopy(var2, 0, var3, var0, var2.length);
            if (var0 > 0) {
               System.arraycopy(var1, 0, var3, 0, var0);
            }

            if (var0 < var1.length) {
               System.arraycopy(var1, var0, var3, var2.length + var0, var1.length - var0);
            }

            return var3;
         } else {
            StringBuilder var4 = new StringBuilder();
            var4.append("Index: ");
            var4.append(var0);
            var4.append(", Length: ");
            var4.append(var1.length);
            throw new IndexOutOfBoundsException(var4.toString());
         }
      } else {
         return clone(var1);
      }
   }

   public static char[] insert(int var0, char[] var1, char... var2) {
      if (var1 == null) {
         return null;
      } else if (var2 != null && var2.length != 0) {
         if (var0 >= 0 && var0 <= var1.length) {
            char[] var3 = new char[var1.length + var2.length];
            System.arraycopy(var2, 0, var3, var0, var2.length);
            if (var0 > 0) {
               System.arraycopy(var1, 0, var3, 0, var0);
            }

            if (var0 < var1.length) {
               System.arraycopy(var1, var0, var3, var2.length + var0, var1.length - var0);
            }

            return var3;
         } else {
            StringBuilder var4 = new StringBuilder();
            var4.append("Index: ");
            var4.append(var0);
            var4.append(", Length: ");
            var4.append(var1.length);
            throw new IndexOutOfBoundsException(var4.toString());
         }
      } else {
         return clone(var1);
      }
   }

   public static double[] insert(int var0, double[] var1, double... var2) {
      if (var1 == null) {
         return null;
      } else if (var2 != null && var2.length != 0) {
         if (var0 >= 0 && var0 <= var1.length) {
            double[] var3 = new double[var1.length + var2.length];
            System.arraycopy(var2, 0, var3, var0, var2.length);
            if (var0 > 0) {
               System.arraycopy(var1, 0, var3, 0, var0);
            }

            if (var0 < var1.length) {
               System.arraycopy(var1, var0, var3, var2.length + var0, var1.length - var0);
            }

            return var3;
         } else {
            StringBuilder var4 = new StringBuilder();
            var4.append("Index: ");
            var4.append(var0);
            var4.append(", Length: ");
            var4.append(var1.length);
            throw new IndexOutOfBoundsException(var4.toString());
         }
      } else {
         return clone(var1);
      }
   }

   public static float[] insert(int var0, float[] var1, float... var2) {
      if (var1 == null) {
         return null;
      } else if (var2 != null && var2.length != 0) {
         if (var0 >= 0 && var0 <= var1.length) {
            float[] var3 = new float[var1.length + var2.length];
            System.arraycopy(var2, 0, var3, var0, var2.length);
            if (var0 > 0) {
               System.arraycopy(var1, 0, var3, 0, var0);
            }

            if (var0 < var1.length) {
               System.arraycopy(var1, var0, var3, var2.length + var0, var1.length - var0);
            }

            return var3;
         } else {
            StringBuilder var4 = new StringBuilder();
            var4.append("Index: ");
            var4.append(var0);
            var4.append(", Length: ");
            var4.append(var1.length);
            throw new IndexOutOfBoundsException(var4.toString());
         }
      } else {
         return clone(var1);
      }
   }

   public static int[] insert(int var0, int[] var1, int... var2) {
      if (var1 == null) {
         return null;
      } else if (var2 != null && var2.length != 0) {
         if (var0 >= 0 && var0 <= var1.length) {
            int[] var3 = new int[var1.length + var2.length];
            System.arraycopy(var2, 0, var3, var0, var2.length);
            if (var0 > 0) {
               System.arraycopy(var1, 0, var3, 0, var0);
            }

            if (var0 < var1.length) {
               System.arraycopy(var1, var0, var3, var2.length + var0, var1.length - var0);
            }

            return var3;
         } else {
            StringBuilder var4 = new StringBuilder();
            var4.append("Index: ");
            var4.append(var0);
            var4.append(", Length: ");
            var4.append(var1.length);
            throw new IndexOutOfBoundsException(var4.toString());
         }
      } else {
         return clone(var1);
      }
   }

   public static long[] insert(int var0, long[] var1, long... var2) {
      if (var1 == null) {
         return null;
      } else if (var2 != null && var2.length != 0) {
         if (var0 >= 0 && var0 <= var1.length) {
            long[] var3 = new long[var1.length + var2.length];
            System.arraycopy(var2, 0, var3, var0, var2.length);
            if (var0 > 0) {
               System.arraycopy(var1, 0, var3, 0, var0);
            }

            if (var0 < var1.length) {
               System.arraycopy(var1, var0, var3, var2.length + var0, var1.length - var0);
            }

            return var3;
         } else {
            StringBuilder var4 = new StringBuilder();
            var4.append("Index: ");
            var4.append(var0);
            var4.append(", Length: ");
            var4.append(var1.length);
            throw new IndexOutOfBoundsException(var4.toString());
         }
      } else {
         return clone(var1);
      }
   }

   @SafeVarargs
   public static Object[] insert(int var0, Object[] var1, Object... var2) {
      if (var1 == null) {
         return null;
      } else if (var2 != null && var2.length != 0) {
         if (var0 >= 0 && var0 <= var1.length) {
            Object[] var3 = (Object[])Array.newInstance(var1.getClass().getComponentType(), var1.length + var2.length);
            System.arraycopy(var2, 0, var3, var0, var2.length);
            if (var0 > 0) {
               System.arraycopy(var1, 0, var3, 0, var0);
            }

            if (var0 < var1.length) {
               System.arraycopy(var1, var0, var3, var2.length + var0, var1.length - var0);
            }

            return var3;
         } else {
            StringBuilder var4 = new StringBuilder();
            var4.append("Index: ");
            var4.append(var0);
            var4.append(", Length: ");
            var4.append(var1.length);
            throw new IndexOutOfBoundsException(var4.toString());
         }
      } else {
         return clone(var1);
      }
   }

   public static short[] insert(int var0, short[] var1, short... var2) {
      if (var1 == null) {
         return null;
      } else if (var2 != null && var2.length != 0) {
         if (var0 >= 0 && var0 <= var1.length) {
            short[] var3 = new short[var1.length + var2.length];
            System.arraycopy(var2, 0, var3, var0, var2.length);
            if (var0 > 0) {
               System.arraycopy(var1, 0, var3, 0, var0);
            }

            if (var0 < var1.length) {
               System.arraycopy(var1, var0, var3, var2.length + var0, var1.length - var0);
            }

            return var3;
         } else {
            StringBuilder var4 = new StringBuilder();
            var4.append("Index: ");
            var4.append(var0);
            var4.append(", Length: ");
            var4.append(var1.length);
            throw new IndexOutOfBoundsException(var4.toString());
         }
      } else {
         return clone(var1);
      }
   }

   public static boolean[] insert(int var0, boolean[] var1, boolean... var2) {
      if (var1 == null) {
         return null;
      } else if (var2 != null && var2.length != 0) {
         if (var0 >= 0 && var0 <= var1.length) {
            boolean[] var3 = new boolean[var1.length + var2.length];
            System.arraycopy(var2, 0, var3, var0, var2.length);
            if (var0 > 0) {
               System.arraycopy(var1, 0, var3, 0, var0);
            }

            if (var0 < var1.length) {
               System.arraycopy(var1, var0, var3, var2.length + var0, var1.length - var0);
            }

            return var3;
         } else {
            StringBuilder var4 = new StringBuilder();
            var4.append("Index: ");
            var4.append(var0);
            var4.append(", Length: ");
            var4.append(var1.length);
            throw new IndexOutOfBoundsException(var4.toString());
         }
      } else {
         return clone(var1);
      }
   }

   public static boolean isArrayIndexValid(Object[] var0, int var1) {
      int var2 = getLength(var0);
      boolean var3 = false;
      if (var2 != 0) {
         if (var0.length <= var1) {
            return false;
         } else {
            if (var1 >= 0) {
               var3 = true;
            }

            return var3;
         }
      } else {
         return false;
      }
   }

   public static boolean isEmpty(byte[] var0) {
      return getLength(var0) == 0;
   }

   public static boolean isEmpty(char[] var0) {
      return getLength(var0) == 0;
   }

   public static boolean isEmpty(double[] var0) {
      return getLength(var0) == 0;
   }

   public static boolean isEmpty(float[] var0) {
      return getLength(var0) == 0;
   }

   public static boolean isEmpty(int[] var0) {
      return getLength(var0) == 0;
   }

   public static boolean isEmpty(long[] var0) {
      return getLength(var0) == 0;
   }

   public static boolean isEmpty(Object[] var0) {
      return getLength(var0) == 0;
   }

   public static boolean isEmpty(short[] var0) {
      return getLength(var0) == 0;
   }

   public static boolean isEmpty(boolean[] var0) {
      return getLength(var0) == 0;
   }

   @Deprecated
   public static boolean isEquals(Object var0, Object var1) {
      return (new EqualsBuilder()).append(var0, var1).isEquals();
   }

   public static boolean isNotEmpty(byte[] var0) {
      return isEmpty(var0) ^ true;
   }

   public static boolean isNotEmpty(char[] var0) {
      return isEmpty(var0) ^ true;
   }

   public static boolean isNotEmpty(double[] var0) {
      return isEmpty(var0) ^ true;
   }

   public static boolean isNotEmpty(float[] var0) {
      return isEmpty(var0) ^ true;
   }

   public static boolean isNotEmpty(int[] var0) {
      return isEmpty(var0) ^ true;
   }

   public static boolean isNotEmpty(long[] var0) {
      return isEmpty(var0) ^ true;
   }

   public static boolean isNotEmpty(Object[] var0) {
      return isEmpty(var0) ^ true;
   }

   public static boolean isNotEmpty(short[] var0) {
      return isEmpty(var0) ^ true;
   }

   public static boolean isNotEmpty(boolean[] var0) {
      return isEmpty(var0) ^ true;
   }

   public static boolean isSameLength(byte[] var0, byte[] var1) {
      return getLength(var0) == getLength(var1);
   }

   public static boolean isSameLength(char[] var0, char[] var1) {
      return getLength(var0) == getLength(var1);
   }

   public static boolean isSameLength(double[] var0, double[] var1) {
      return getLength(var0) == getLength(var1);
   }

   public static boolean isSameLength(float[] var0, float[] var1) {
      return getLength(var0) == getLength(var1);
   }

   public static boolean isSameLength(int[] var0, int[] var1) {
      return getLength(var0) == getLength(var1);
   }

   public static boolean isSameLength(long[] var0, long[] var1) {
      return getLength(var0) == getLength(var1);
   }

   public static boolean isSameLength(Object[] var0, Object[] var1) {
      return getLength(var0) == getLength(var1);
   }

   public static boolean isSameLength(short[] var0, short[] var1) {
      return getLength(var0) == getLength(var1);
   }

   public static boolean isSameLength(boolean[] var0, boolean[] var1) {
      return getLength(var0) == getLength(var1);
   }

   public static boolean isSameType(Object var0, Object var1) {
      if (var0 != null && var1 != null) {
         return var0.getClass().getName().equals(var1.getClass().getName());
      } else {
         throw new IllegalArgumentException("The Array must not be null");
      }
   }

   public static boolean isSorted(byte[] var0) {
      if (var0 != null) {
         if (var0.length < 2) {
            return true;
         } else {
            byte var1 = var0[0];
            int var4 = var0.length;

            for(int var3 = 1; var3 < var4; ++var3) {
               byte var2 = var0[var3];
               if (NumberUtils.compare(var1, var2) > 0) {
                  return false;
               }

               var1 = var2;
            }

            return true;
         }
      } else {
         return true;
      }
   }

   public static boolean isSorted(char[] var0) {
      if (var0 != null) {
         if (var0.length < 2) {
            return true;
         } else {
            char var1 = var0[0];
            int var4 = var0.length;

            for(int var3 = 1; var3 < var4; ++var3) {
               char var2 = var0[var3];
               if (CharUtils.compare(var1, var2) > 0) {
                  return false;
               }

               var1 = var2;
            }

            return true;
         }
      } else {
         return true;
      }
   }

   public static boolean isSorted(double[] var0) {
      if (var0 != null) {
         if (var0.length < 2) {
            return true;
         } else {
            double var1 = var0[0];
            int var6 = var0.length;

            for(int var5 = 1; var5 < var6; ++var5) {
               double var3 = var0[var5];
               if (Double.compare(var1, var3) > 0) {
                  return false;
               }

               var1 = var3;
            }

            return true;
         }
      } else {
         return true;
      }
   }

   public static boolean isSorted(float[] var0) {
      if (var0 != null) {
         if (var0.length < 2) {
            return true;
         } else {
            float var1 = var0[0];
            int var4 = var0.length;

            for(int var3 = 1; var3 < var4; ++var3) {
               float var2 = var0[var3];
               if (Float.compare(var1, var2) > 0) {
                  return false;
               }

               var1 = var2;
            }

            return true;
         }
      } else {
         return true;
      }
   }

   public static boolean isSorted(int[] var0) {
      if (var0 != null) {
         if (var0.length < 2) {
            return true;
         } else {
            int var2 = var0[0];
            int var4 = var0.length;

            for(int var1 = 1; var1 < var4; ++var1) {
               int var3 = var0[var1];
               if (NumberUtils.compare(var2, var3) > 0) {
                  return false;
               }

               var2 = var3;
            }

            return true;
         }
      } else {
         return true;
      }
   }

   public static boolean isSorted(long[] var0) {
      if (var0 != null) {
         if (var0.length < 2) {
            return true;
         } else {
            long var3 = var0[0];
            int var2 = var0.length;

            for(int var1 = 1; var1 < var2; ++var1) {
               long var5 = var0[var1];
               if (NumberUtils.compare(var3, var5) > 0) {
                  return false;
               }

               var3 = var5;
            }

            return true;
         }
      } else {
         return true;
      }
   }

   public static boolean isSorted(Comparable[] var0) {
      return isSorted(var0, new Comparator() {
         public int compare(Comparable var1, Comparable var2) {
            return var1.compareTo(var2);
         }
      });
   }

   public static boolean isSorted(Object[] var0, Comparator var1) {
      if (var1 != null) {
         if (var0 != null) {
            if (var0.length < 2) {
               return true;
            } else {
               Object var4 = var0[0];
               int var3 = var0.length;

               for(int var2 = 1; var2 < var3; ++var2) {
                  Object var5 = var0[var2];
                  if (var1.compare(var4, var5) > 0) {
                     return false;
                  }

                  var4 = var5;
               }

               return true;
            }
         } else {
            return true;
         }
      } else {
         throw new IllegalArgumentException("Comparator should not be null.");
      }
   }

   public static boolean isSorted(short[] var0) {
      if (var0 != null) {
         if (var0.length < 2) {
            return true;
         } else {
            short var1 = var0[0];
            int var4 = var0.length;

            for(int var3 = 1; var3 < var4; ++var3) {
               short var2 = var0[var3];
               if (NumberUtils.compare(var1, var2) > 0) {
                  return false;
               }

               var1 = var2;
            }

            return true;
         }
      } else {
         return true;
      }
   }

   public static boolean isSorted(boolean[] var0) {
      if (var0 != null) {
         if (var0.length < 2) {
            return true;
         } else {
            boolean var3 = var0[0];
            int var2 = var0.length;

            for(int var1 = 1; var1 < var2; ++var1) {
               boolean var4 = var0[var1];
               if (BooleanUtils.compare(var3, var4) > 0) {
                  return false;
               }

               var3 = var4;
            }

            return true;
         }
      } else {
         return true;
      }
   }

   public static int lastIndexOf(byte[] var0, byte var1) {
      return lastIndexOf(var0, var1, Integer.MAX_VALUE);
   }

   public static int lastIndexOf(byte[] var0, byte var1, int var2) {
      if (var0 == null) {
         return -1;
      } else if (var2 < 0) {
         return -1;
      } else {
         int var3 = var2;
         if (var2 >= var0.length) {
            var3 = var0.length - 1;
         }

         while(var3 >= 0) {
            if (var1 == var0[var3]) {
               return var3;
            }

            --var3;
         }

         return -1;
      }
   }

   public static int lastIndexOf(char[] var0, char var1) {
      return lastIndexOf(var0, var1, Integer.MAX_VALUE);
   }

   public static int lastIndexOf(char[] var0, char var1, int var2) {
      if (var0 == null) {
         return -1;
      } else if (var2 < 0) {
         return -1;
      } else {
         int var3 = var2;
         if (var2 >= var0.length) {
            var3 = var0.length - 1;
         }

         while(var3 >= 0) {
            if (var1 == var0[var3]) {
               return var3;
            }

            --var3;
         }

         return -1;
      }
   }

   public static int lastIndexOf(double[] var0, double var1) {
      return lastIndexOf(var0, var1, Integer.MAX_VALUE);
   }

   public static int lastIndexOf(double[] var0, double var1, double var3) {
      return lastIndexOf(var0, var1, Integer.MAX_VALUE, var3);
   }

   public static int lastIndexOf(double[] var0, double var1, int var3) {
      if (isEmpty(var0)) {
         return -1;
      } else if (var3 < 0) {
         return -1;
      } else {
         int var4 = var3;
         if (var3 >= var0.length) {
            var4 = var0.length - 1;
         }

         while(var4 >= 0) {
            if (var1 == var0[var4]) {
               return var4;
            }

            --var4;
         }

         return -1;
      }
   }

   public static int lastIndexOf(double[] var0, double var1, int var3, double var4) {
      if (isEmpty(var0)) {
         return -1;
      } else if (var3 < 0) {
         return -1;
      } else {
         int var6 = var3;
         if (var3 >= var0.length) {
            var6 = var0.length - 1;
         }

         while(var6 >= 0) {
            if (var0[var6] >= var1 - var4 && var0[var6] <= var1 + var4) {
               return var6;
            }

            --var6;
         }

         return -1;
      }
   }

   public static int lastIndexOf(float[] var0, float var1) {
      return lastIndexOf(var0, var1, Integer.MAX_VALUE);
   }

   public static int lastIndexOf(float[] var0, float var1, int var2) {
      if (isEmpty(var0)) {
         return -1;
      } else if (var2 < 0) {
         return -1;
      } else {
         int var3 = var2;
         if (var2 >= var0.length) {
            var3 = var0.length - 1;
         }

         while(var3 >= 0) {
            if (var1 == var0[var3]) {
               return var3;
            }

            --var3;
         }

         return -1;
      }
   }

   public static int lastIndexOf(int[] var0, int var1) {
      return lastIndexOf(var0, var1, Integer.MAX_VALUE);
   }

   public static int lastIndexOf(int[] var0, int var1, int var2) {
      if (var0 == null) {
         return -1;
      } else if (var2 < 0) {
         return -1;
      } else {
         int var3 = var2;
         if (var2 >= var0.length) {
            var3 = var0.length - 1;
         }

         while(var3 >= 0) {
            if (var1 == var0[var3]) {
               return var3;
            }

            --var3;
         }

         return -1;
      }
   }

   public static int lastIndexOf(long[] var0, long var1) {
      return lastIndexOf(var0, var1, Integer.MAX_VALUE);
   }

   public static int lastIndexOf(long[] var0, long var1, int var3) {
      if (var0 == null) {
         return -1;
      } else if (var3 < 0) {
         return -1;
      } else {
         int var4 = var3;
         if (var3 >= var0.length) {
            var4 = var0.length - 1;
         }

         while(var4 >= 0) {
            if (var1 == var0[var4]) {
               return var4;
            }

            --var4;
         }

         return -1;
      }
   }

   public static int lastIndexOf(Object[] var0, Object var1) {
      return lastIndexOf(var0, var1, Integer.MAX_VALUE);
   }

   public static int lastIndexOf(Object[] var0, Object var1, int var2) {
      if (var0 == null) {
         return -1;
      } else if (var2 < 0) {
         return -1;
      } else {
         int var3 = var2;
         if (var2 >= var0.length) {
            var3 = var0.length - 1;
         }

         if (var1 == null) {
            while(var3 >= 0) {
               if (var0[var3] == null) {
                  return var3;
               }

               --var3;
            }

            return -1;
         } else {
            if (var0.getClass().getComponentType().isInstance(var1)) {
               while(var3 >= 0) {
                  if (var1.equals(var0[var3])) {
                     return var3;
                  }

                  --var3;
               }
            }

            return -1;
         }
      }
   }

   public static int lastIndexOf(short[] var0, short var1) {
      return lastIndexOf(var0, var1, Integer.MAX_VALUE);
   }

   public static int lastIndexOf(short[] var0, short var1, int var2) {
      if (var0 == null) {
         return -1;
      } else if (var2 < 0) {
         return -1;
      } else {
         int var3 = var2;
         if (var2 >= var0.length) {
            var3 = var0.length - 1;
         }

         while(var3 >= 0) {
            if (var1 == var0[var3]) {
               return var3;
            }

            --var3;
         }

         return -1;
      }
   }

   public static int lastIndexOf(boolean[] var0, boolean var1) {
      return lastIndexOf(var0, var1, Integer.MAX_VALUE);
   }

   public static int lastIndexOf(boolean[] var0, boolean var1, int var2) {
      if (isEmpty(var0)) {
         return -1;
      } else if (var2 < 0) {
         return -1;
      } else {
         int var3 = var2;
         if (var2 >= var0.length) {
            var3 = var0.length - 1;
         }

         while(var3 >= 0) {
            if (var1 == var0[var3]) {
               return var3;
            }

            --var3;
         }

         return -1;
      }
   }

   public static byte[] nullToEmpty(byte[] var0) {
      return isEmpty(var0) ? EMPTY_BYTE_ARRAY : var0;
   }

   public static char[] nullToEmpty(char[] var0) {
      return isEmpty(var0) ? EMPTY_CHAR_ARRAY : var0;
   }

   public static double[] nullToEmpty(double[] var0) {
      return isEmpty(var0) ? EMPTY_DOUBLE_ARRAY : var0;
   }

   public static float[] nullToEmpty(float[] var0) {
      return isEmpty(var0) ? EMPTY_FLOAT_ARRAY : var0;
   }

   public static int[] nullToEmpty(int[] var0) {
      return isEmpty(var0) ? EMPTY_INT_ARRAY : var0;
   }

   public static long[] nullToEmpty(long[] var0) {
      return isEmpty(var0) ? EMPTY_LONG_ARRAY : var0;
   }

   public static Boolean[] nullToEmpty(Boolean[] var0) {
      return isEmpty((Object[])var0) ? EMPTY_BOOLEAN_OBJECT_ARRAY : var0;
   }

   public static Byte[] nullToEmpty(Byte[] var0) {
      return isEmpty((Object[])var0) ? EMPTY_BYTE_OBJECT_ARRAY : var0;
   }

   public static Character[] nullToEmpty(Character[] var0) {
      return isEmpty((Object[])var0) ? EMPTY_CHARACTER_OBJECT_ARRAY : var0;
   }

   public static Class[] nullToEmpty(Class[] var0) {
      return isEmpty((Object[])var0) ? EMPTY_CLASS_ARRAY : var0;
   }

   public static Double[] nullToEmpty(Double[] var0) {
      return isEmpty((Object[])var0) ? EMPTY_DOUBLE_OBJECT_ARRAY : var0;
   }

   public static Float[] nullToEmpty(Float[] var0) {
      return isEmpty((Object[])var0) ? EMPTY_FLOAT_OBJECT_ARRAY : var0;
   }

   public static Integer[] nullToEmpty(Integer[] var0) {
      return isEmpty((Object[])var0) ? EMPTY_INTEGER_OBJECT_ARRAY : var0;
   }

   public static Long[] nullToEmpty(Long[] var0) {
      return isEmpty((Object[])var0) ? EMPTY_LONG_OBJECT_ARRAY : var0;
   }

   public static Object[] nullToEmpty(Object[] var0) {
      return isEmpty(var0) ? EMPTY_OBJECT_ARRAY : var0;
   }

   public static Object[] nullToEmpty(Object[] var0, Class var1) {
      if (var1 != null) {
         return var0 == null ? (Object[])var1.cast(Array.newInstance(var1.getComponentType(), 0)) : var0;
      } else {
         throw new IllegalArgumentException("The type must not be null");
      }
   }

   public static Short[] nullToEmpty(Short[] var0) {
      return isEmpty((Object[])var0) ? EMPTY_SHORT_OBJECT_ARRAY : var0;
   }

   public static String[] nullToEmpty(String[] var0) {
      return isEmpty((Object[])var0) ? EMPTY_STRING_ARRAY : var0;
   }

   public static short[] nullToEmpty(short[] var0) {
      return isEmpty(var0) ? EMPTY_SHORT_ARRAY : var0;
   }

   public static boolean[] nullToEmpty(boolean[] var0) {
      return isEmpty(var0) ? EMPTY_BOOLEAN_ARRAY : var0;
   }

   private static Object remove(Object var0, int var1) {
      int var2 = getLength(var0);
      if (var1 >= 0 && var1 < var2) {
         Object var3 = Array.newInstance(var0.getClass().getComponentType(), var2 - 1);
         System.arraycopy(var0, 0, var3, 0, var1);
         if (var1 < var2 - 1) {
            System.arraycopy(var0, var1 + 1, var3, var1, var2 - var1 - 1);
         }

         return var3;
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("Index: ");
         var4.append(var1);
         var4.append(", Length: ");
         var4.append(var2);
         throw new IndexOutOfBoundsException(var4.toString());
      }
   }

   public static byte[] remove(byte[] var0, int var1) {
      return (byte[])remove((Object)var0, var1);
   }

   public static char[] remove(char[] var0, int var1) {
      return (char[])remove((Object)var0, var1);
   }

   public static double[] remove(double[] var0, int var1) {
      return (double[])remove((Object)var0, var1);
   }

   public static float[] remove(float[] var0, int var1) {
      return (float[])remove((Object)var0, var1);
   }

   public static int[] remove(int[] var0, int var1) {
      return (int[])remove((Object)var0, var1);
   }

   public static long[] remove(long[] var0, int var1) {
      return (long[])remove((Object)var0, var1);
   }

   public static Object[] remove(Object[] var0, int var1) {
      return (Object[])remove((Object)var0, var1);
   }

   public static short[] remove(short[] var0, int var1) {
      return (short[])remove((Object)var0, var1);
   }

   public static boolean[] remove(boolean[] var0, int var1) {
      return (boolean[])remove((Object)var0, var1);
   }

   static Object removeAll(Object var0, BitSet var1) {
      int var5 = getLength(var0);
      int var2 = var1.cardinality();
      Object var8 = Array.newInstance(var0.getClass().getComponentType(), var5 - var2);
      int var3 = 0;
      var2 = 0;

      while(true) {
         int var6 = var1.nextSetBit(var3);
         int var4;
         if (var6 == -1) {
            var4 = var5 - var3;
            if (var4 > 0) {
               System.arraycopy(var0, var3, var8, var2, var4);
            }

            return var8;
         }

         int var7 = var6 - var3;
         var4 = var2;
         if (var7 > 0) {
            System.arraycopy(var0, var3, var8, var2, var7);
            var4 = var2 + var7;
         }

         var3 = var1.nextClearBit(var6);
         var2 = var4;
      }
   }

   static Object removeAll(Object var0, int... var1) {
      int var3 = getLength(var0);
      int var5 = 0;
      int var2 = 0;
      var1 = clone(var1);
      Arrays.sort(var1);
      int var4;
      int var6;
      if (isNotEmpty(var1)) {
         var5 = var1.length;
         var4 = var3;

         while(true) {
            var6 = var5 - 1;
            var5 = var2;
            if (var6 < 0) {
               break;
            }

            var5 = var1[var6];
            if (var5 < 0 || var5 >= var3) {
               StringBuilder var8 = new StringBuilder();
               var8.append("Index: ");
               var8.append(var5);
               var8.append(", Length: ");
               var8.append(var3);
               throw new IndexOutOfBoundsException(var8.toString());
            }

            if (var5 >= var4) {
               var5 = var6;
            } else {
               ++var2;
               var4 = var5;
               var5 = var6;
            }
         }
      }

      Object var7 = Array.newInstance(var0.getClass().getComponentType(), var3 - var5);
      if (var5 < var3) {
         var4 = var3;
         var3 -= var5;
         var2 = var1.length - 1;

         for(var5 = var4; var2 >= 0; var3 = var4) {
            var6 = var1[var2];
            var4 = var3;
            if (var5 - var6 > 1) {
               var5 = var5 - var6 - 1;
               var4 = var3 - var5;
               System.arraycopy(var0, var6 + 1, var7, var4, var5);
            }

            var5 = var6;
            --var2;
         }

         if (var5 > 0) {
            System.arraycopy(var0, 0, var7, 0, var5);
         }
      }

      return var7;
   }

   public static byte[] removeAll(byte[] var0, int... var1) {
      return (byte[])removeAll((Object)var0, (int[])var1);
   }

   public static char[] removeAll(char[] var0, int... var1) {
      return (char[])removeAll((Object)var0, (int[])var1);
   }

   public static double[] removeAll(double[] var0, int... var1) {
      return (double[])removeAll((Object)var0, (int[])var1);
   }

   public static float[] removeAll(float[] var0, int... var1) {
      return (float[])removeAll((Object)var0, (int[])var1);
   }

   public static int[] removeAll(int[] var0, int... var1) {
      return (int[])removeAll((Object)var0, (int[])var1);
   }

   public static long[] removeAll(long[] var0, int... var1) {
      return (long[])removeAll((Object)var0, (int[])var1);
   }

   public static Object[] removeAll(Object[] var0, int... var1) {
      return (Object[])removeAll((Object)var0, (int[])var1);
   }

   public static short[] removeAll(short[] var0, int... var1) {
      return (short[])removeAll((Object)var0, (int[])var1);
   }

   public static boolean[] removeAll(boolean[] var0, int... var1) {
      return (boolean[])removeAll((Object)var0, (int[])var1);
   }

   public static byte[] removeAllOccurences(byte[] var0, byte var1) {
      int var2 = indexOf(var0, var1);
      if (var2 == -1) {
         return clone(var0);
      } else {
         int[] var4 = new int[var0.length - var2];
         var4[0] = var2;
         var2 = 1;

         while(true) {
            int var3 = indexOf(var0, var1, var4[var2 - 1] + 1);
            if (var3 == -1) {
               return removeAll(var0, Arrays.copyOf(var4, var2));
            }

            var4[var2] = var3;
            ++var2;
         }
      }
   }

   public static char[] removeAllOccurences(char[] var0, char var1) {
      int var2 = indexOf(var0, var1);
      if (var2 == -1) {
         return clone(var0);
      } else {
         int[] var4 = new int[var0.length - var2];
         var4[0] = var2;
         var2 = 1;

         while(true) {
            int var3 = indexOf(var0, var1, var4[var2 - 1] + 1);
            if (var3 == -1) {
               return removeAll(var0, Arrays.copyOf(var4, var2));
            }

            var4[var2] = var3;
            ++var2;
         }
      }
   }

   public static double[] removeAllOccurences(double[] var0, double var1) {
      int var3 = indexOf(var0, var1);
      if (var3 == -1) {
         return clone(var0);
      } else {
         int[] var5 = new int[var0.length - var3];
         var5[0] = var3;
         var3 = 1;

         while(true) {
            int var4 = indexOf(var0, var1, var5[var3 - 1] + 1);
            if (var4 == -1) {
               return removeAll(var0, Arrays.copyOf(var5, var3));
            }

            var5[var3] = var4;
            ++var3;
         }
      }
   }

   public static float[] removeAllOccurences(float[] var0, float var1) {
      int var2 = indexOf(var0, var1);
      if (var2 == -1) {
         return clone(var0);
      } else {
         int[] var4 = new int[var0.length - var2];
         var4[0] = var2;
         var2 = 1;

         while(true) {
            int var3 = indexOf(var0, var1, var4[var2 - 1] + 1);
            if (var3 == -1) {
               return removeAll(var0, Arrays.copyOf(var4, var2));
            }

            var4[var2] = var3;
            ++var2;
         }
      }
   }

   public static int[] removeAllOccurences(int[] var0, int var1) {
      int var2 = indexOf(var0, var1);
      if (var2 == -1) {
         return clone(var0);
      } else {
         int[] var4 = new int[var0.length - var2];
         var4[0] = var2;
         var2 = 1;

         while(true) {
            int var3 = indexOf(var0, var1, var4[var2 - 1] + 1);
            if (var3 == -1) {
               return removeAll(var0, Arrays.copyOf(var4, var2));
            }

            var4[var2] = var3;
            ++var2;
         }
      }
   }

   public static long[] removeAllOccurences(long[] var0, long var1) {
      int var3 = indexOf(var0, var1);
      if (var3 == -1) {
         return clone(var0);
      } else {
         int[] var5 = new int[var0.length - var3];
         var5[0] = var3;
         var3 = 1;

         while(true) {
            int var4 = indexOf(var0, var1, var5[var3 - 1] + 1);
            if (var4 == -1) {
               return removeAll(var0, Arrays.copyOf(var5, var3));
            }

            var5[var3] = var4;
            ++var3;
         }
      }
   }

   public static Object[] removeAllOccurences(Object[] var0, Object var1) {
      int var2 = indexOf(var0, var1);
      if (var2 == -1) {
         return clone(var0);
      } else {
         int[] var4 = new int[var0.length - var2];
         var4[0] = var2;
         var2 = 1;

         while(true) {
            int var3 = indexOf(var0, var1, var4[var2 - 1] + 1);
            if (var3 == -1) {
               return removeAll(var0, Arrays.copyOf(var4, var2));
            }

            var4[var2] = var3;
            ++var2;
         }
      }
   }

   public static short[] removeAllOccurences(short[] var0, short var1) {
      int var2 = indexOf(var0, var1);
      if (var2 == -1) {
         return clone(var0);
      } else {
         int[] var4 = new int[var0.length - var2];
         var4[0] = var2;
         var2 = 1;

         while(true) {
            int var3 = indexOf(var0, var1, var4[var2 - 1] + 1);
            if (var3 == -1) {
               return removeAll(var0, Arrays.copyOf(var4, var2));
            }

            var4[var2] = var3;
            ++var2;
         }
      }
   }

   public static boolean[] removeAllOccurences(boolean[] var0, boolean var1) {
      int var2 = indexOf(var0, var1);
      if (var2 == -1) {
         return clone(var0);
      } else {
         int[] var4 = new int[var0.length - var2];
         var4[0] = var2;
         var2 = 1;

         while(true) {
            int var3 = indexOf(var0, var1, var4[var2 - 1] + 1);
            if (var3 == -1) {
               return removeAll(var0, Arrays.copyOf(var4, var2));
            }

            var4[var2] = var3;
            ++var2;
         }
      }
   }

   public static byte[] removeElement(byte[] var0, byte var1) {
      int var2 = indexOf(var0, var1);
      return var2 == -1 ? clone(var0) : remove(var0, var2);
   }

   public static char[] removeElement(char[] var0, char var1) {
      int var2 = indexOf(var0, var1);
      return var2 == -1 ? clone(var0) : remove(var0, var2);
   }

   public static double[] removeElement(double[] var0, double var1) {
      int var3 = indexOf(var0, var1);
      return var3 == -1 ? clone(var0) : remove(var0, var3);
   }

   public static float[] removeElement(float[] var0, float var1) {
      int var2 = indexOf(var0, var1);
      return var2 == -1 ? clone(var0) : remove(var0, var2);
   }

   public static int[] removeElement(int[] var0, int var1) {
      var1 = indexOf(var0, var1);
      return var1 == -1 ? clone(var0) : remove(var0, var1);
   }

   public static long[] removeElement(long[] var0, long var1) {
      int var3 = indexOf(var0, var1);
      return var3 == -1 ? clone(var0) : remove(var0, var3);
   }

   public static Object[] removeElement(Object[] var0, Object var1) {
      int var2 = indexOf(var0, var1);
      return var2 == -1 ? clone(var0) : remove(var0, var2);
   }

   public static short[] removeElement(short[] var0, short var1) {
      int var2 = indexOf(var0, var1);
      return var2 == -1 ? clone(var0) : remove(var0, var2);
   }

   public static boolean[] removeElement(boolean[] var0, boolean var1) {
      int var2 = indexOf(var0, var1);
      return var2 == -1 ? clone(var0) : remove(var0, var2);
   }

   public static byte[] removeElements(byte[] var0, byte... var1) {
      if (!isEmpty(var0) && !isEmpty(var1)) {
         HashMap var5 = new HashMap(var1.length);
         int var4 = var1.length;

         int var3;
         for(var3 = 0; var3 < var4; ++var3) {
            Byte var6 = var1[var3];
            MutableInt var7 = (MutableInt)var5.get(var6);
            if (var7 == null) {
               var5.put(var6, new MutableInt(1));
            } else {
               var7.increment();
            }
         }

         BitSet var8 = new BitSet();

         for(var3 = 0; var3 < var0.length; ++var3) {
            byte var2 = var0[var3];
            MutableInt var9 = (MutableInt)var5.get(var2);
            if (var9 != null) {
               if (var9.decrementAndGet() == 0) {
                  var5.remove(var2);
               }

               var8.set(var3);
            }
         }

         return (byte[])removeAll((Object)var0, (BitSet)var8);
      } else {
         return clone(var0);
      }
   }

   public static char[] removeElements(char[] var0, char... var1) {
      if (!isEmpty(var0) && !isEmpty(var1)) {
         HashMap var5 = new HashMap(var1.length);
         int var4 = var1.length;

         int var3;
         for(var3 = 0; var3 < var4; ++var3) {
            Character var6 = var1[var3];
            MutableInt var7 = (MutableInt)var5.get(var6);
            if (var7 == null) {
               var5.put(var6, new MutableInt(1));
            } else {
               var7.increment();
            }
         }

         BitSet var8 = new BitSet();

         for(var3 = 0; var3 < var0.length; ++var3) {
            char var2 = var0[var3];
            MutableInt var9 = (MutableInt)var5.get(var2);
            if (var9 != null) {
               if (var9.decrementAndGet() == 0) {
                  var5.remove(var2);
               }

               var8.set(var3);
            }
         }

         return (char[])removeAll((Object)var0, (BitSet)var8);
      } else {
         return clone(var0);
      }
   }

   public static double[] removeElements(double[] var0, double... var1) {
      if (!isEmpty(var0) && !isEmpty(var1)) {
         HashMap var6 = new HashMap(var1.length);
         int var5 = var1.length;

         int var4;
         for(var4 = 0; var4 < var5; ++var4) {
            Double var7 = var1[var4];
            MutableInt var8 = (MutableInt)var6.get(var7);
            if (var8 == null) {
               var6.put(var7, new MutableInt(1));
            } else {
               var8.increment();
            }
         }

         BitSet var9 = new BitSet();

         for(var4 = 0; var4 < var0.length; ++var4) {
            double var2 = var0[var4];
            MutableInt var10 = (MutableInt)var6.get(var2);
            if (var10 != null) {
               if (var10.decrementAndGet() == 0) {
                  var6.remove(var2);
               }

               var9.set(var4);
            }
         }

         return (double[])removeAll((Object)var0, (BitSet)var9);
      } else {
         return clone(var0);
      }
   }

   public static float[] removeElements(float[] var0, float... var1) {
      if (!isEmpty(var0) && !isEmpty(var1)) {
         HashMap var5 = new HashMap(var1.length);
         int var4 = var1.length;

         int var3;
         for(var3 = 0; var3 < var4; ++var3) {
            Float var6 = var1[var3];
            MutableInt var7 = (MutableInt)var5.get(var6);
            if (var7 == null) {
               var5.put(var6, new MutableInt(1));
            } else {
               var7.increment();
            }
         }

         BitSet var8 = new BitSet();

         for(var3 = 0; var3 < var0.length; ++var3) {
            float var2 = var0[var3];
            MutableInt var9 = (MutableInt)var5.get(var2);
            if (var9 != null) {
               if (var9.decrementAndGet() == 0) {
                  var5.remove(var2);
               }

               var8.set(var3);
            }
         }

         return (float[])removeAll((Object)var0, (BitSet)var8);
      } else {
         return clone(var0);
      }
   }

   public static int[] removeElements(int[] var0, int... var1) {
      if (!isEmpty(var0) && !isEmpty(var1)) {
         HashMap var4 = new HashMap(var1.length);
         int var3 = var1.length;

         int var2;
         for(var2 = 0; var2 < var3; ++var2) {
            Integer var5 = var1[var2];
            MutableInt var6 = (MutableInt)var4.get(var5);
            if (var6 == null) {
               var4.put(var5, new MutableInt(1));
            } else {
               var6.increment();
            }
         }

         BitSet var7 = new BitSet();

         for(var2 = 0; var2 < var0.length; ++var2) {
            var3 = var0[var2];
            MutableInt var8 = (MutableInt)var4.get(var3);
            if (var8 != null) {
               if (var8.decrementAndGet() == 0) {
                  var4.remove(var3);
               }

               var7.set(var2);
            }
         }

         return (int[])removeAll((Object)var0, (BitSet)var7);
      } else {
         return clone(var0);
      }
   }

   public static long[] removeElements(long[] var0, long... var1) {
      if (!isEmpty(var0) && !isEmpty(var1)) {
         HashMap var6 = new HashMap(var1.length);
         int var3 = var1.length;

         int var2;
         for(var2 = 0; var2 < var3; ++var2) {
            Long var7 = var1[var2];
            MutableInt var8 = (MutableInt)var6.get(var7);
            if (var8 == null) {
               var6.put(var7, new MutableInt(1));
            } else {
               var8.increment();
            }
         }

         BitSet var9 = new BitSet();

         for(var2 = 0; var2 < var0.length; ++var2) {
            long var4 = var0[var2];
            MutableInt var10 = (MutableInt)var6.get(var4);
            if (var10 != null) {
               if (var10.decrementAndGet() == 0) {
                  var6.remove(var4);
               }

               var9.set(var2);
            }
         }

         return (long[])removeAll((Object)var0, (BitSet)var9);
      } else {
         return clone(var0);
      }
   }

   @SafeVarargs
   public static Object[] removeElements(Object[] var0, Object... var1) {
      if (!isEmpty(var0) && !isEmpty(var1)) {
         HashMap var4 = new HashMap(var1.length);
         int var3 = var1.length;

         int var2;
         Object var5;
         MutableInt var6;
         for(var2 = 0; var2 < var3; ++var2) {
            var5 = var1[var2];
            var6 = (MutableInt)var4.get(var5);
            if (var6 == null) {
               var4.put(var5, new MutableInt(1));
            } else {
               var6.increment();
            }
         }

         BitSet var7 = new BitSet();

         for(var2 = 0; var2 < var0.length; ++var2) {
            var5 = var0[var2];
            var6 = (MutableInt)var4.get(var5);
            if (var6 != null) {
               if (var6.decrementAndGet() == 0) {
                  var4.remove(var5);
               }

               var7.set(var2);
            }
         }

         return (Object[])removeAll((Object)var0, (BitSet)var7);
      } else {
         return clone(var0);
      }
   }

   public static short[] removeElements(short[] var0, short... var1) {
      if (!isEmpty(var0) && !isEmpty(var1)) {
         HashMap var5 = new HashMap(var1.length);
         int var4 = var1.length;

         int var3;
         for(var3 = 0; var3 < var4; ++var3) {
            Short var6 = var1[var3];
            MutableInt var7 = (MutableInt)var5.get(var6);
            if (var7 == null) {
               var5.put(var6, new MutableInt(1));
            } else {
               var7.increment();
            }
         }

         BitSet var8 = new BitSet();

         for(var3 = 0; var3 < var0.length; ++var3) {
            short var2 = var0[var3];
            MutableInt var9 = (MutableInt)var5.get(var2);
            if (var9 != null) {
               if (var9.decrementAndGet() == 0) {
                  var5.remove(var2);
               }

               var8.set(var3);
            }
         }

         return (short[])removeAll((Object)var0, (BitSet)var8);
      } else {
         return clone(var0);
      }
   }

   public static boolean[] removeElements(boolean[] var0, boolean... var1) {
      if (!isEmpty(var0) && !isEmpty(var1)) {
         HashMap var5 = new HashMap(2);
         int var3 = var1.length;

         int var2;
         for(var2 = 0; var2 < var3; ++var2) {
            Boolean var6 = var1[var2];
            MutableInt var7 = (MutableInt)var5.get(var6);
            if (var7 == null) {
               var5.put(var6, new MutableInt(1));
            } else {
               var7.increment();
            }
         }

         BitSet var8 = new BitSet();

         for(var2 = 0; var2 < var0.length; ++var2) {
            boolean var4 = var0[var2];
            MutableInt var9 = (MutableInt)var5.get(var4);
            if (var9 != null) {
               if (var9.decrementAndGet() == 0) {
                  var5.remove(var4);
               }

               var8.set(var2);
            }
         }

         return (boolean[])removeAll((Object)var0, (BitSet)var8);
      } else {
         return clone(var0);
      }
   }

   public static void reverse(byte[] var0) {
      if (var0 != null) {
         reverse((byte[])var0, 0, var0.length);
      }
   }

   public static void reverse(byte[] var0, int var1, int var2) {
      if (var0 != null) {
         if (var1 < 0) {
            var1 = 0;
         }

         for(var2 = Math.min(var0.length, var2) - 1; var2 > var1; ++var1) {
            byte var3 = var0[var2];
            var0[var2] = var0[var1];
            var0[var1] = var3;
            --var2;
         }

      }
   }

   public static void reverse(char[] var0) {
      if (var0 != null) {
         reverse((char[])var0, 0, var0.length);
      }
   }

   public static void reverse(char[] var0, int var1, int var2) {
      if (var0 != null) {
         if (var1 < 0) {
            var1 = 0;
         }

         for(var2 = Math.min(var0.length, var2) - 1; var2 > var1; ++var1) {
            char var3 = var0[var2];
            var0[var2] = var0[var1];
            var0[var1] = var3;
            --var2;
         }

      }
   }

   public static void reverse(double[] var0) {
      if (var0 != null) {
         reverse((double[])var0, 0, var0.length);
      }
   }

   public static void reverse(double[] var0, int var1, int var2) {
      if (var0 != null) {
         if (var1 < 0) {
            var1 = 0;
         }

         for(var2 = Math.min(var0.length, var2) - 1; var2 > var1; ++var1) {
            double var3 = var0[var2];
            var0[var2] = var0[var1];
            var0[var1] = var3;
            --var2;
         }

      }
   }

   public static void reverse(float[] var0) {
      if (var0 != null) {
         reverse((float[])var0, 0, var0.length);
      }
   }

   public static void reverse(float[] var0, int var1, int var2) {
      if (var0 != null) {
         if (var1 < 0) {
            var1 = 0;
         }

         for(var2 = Math.min(var0.length, var2) - 1; var2 > var1; ++var1) {
            float var3 = var0[var2];
            var0[var2] = var0[var1];
            var0[var1] = var3;
            --var2;
         }

      }
   }

   public static void reverse(int[] var0) {
      if (var0 != null) {
         reverse((int[])var0, 0, var0.length);
      }
   }

   public static void reverse(int[] var0, int var1, int var2) {
      if (var0 != null) {
         if (var1 < 0) {
            var1 = 0;
         }

         for(var2 = Math.min(var0.length, var2) - 1; var2 > var1; ++var1) {
            int var3 = var0[var2];
            var0[var2] = var0[var1];
            var0[var1] = var3;
            --var2;
         }

      }
   }

   public static void reverse(long[] var0) {
      if (var0 != null) {
         reverse((long[])var0, 0, var0.length);
      }
   }

   public static void reverse(long[] var0, int var1, int var2) {
      if (var0 != null) {
         if (var1 < 0) {
            var1 = 0;
         }

         for(var2 = Math.min(var0.length, var2) - 1; var2 > var1; ++var1) {
            long var3 = var0[var2];
            var0[var2] = var0[var1];
            var0[var1] = var3;
            --var2;
         }

      }
   }

   public static void reverse(Object[] var0) {
      if (var0 != null) {
         reverse((Object[])var0, 0, var0.length);
      }
   }

   public static void reverse(Object[] var0, int var1, int var2) {
      if (var0 != null) {
         if (var1 < 0) {
            var1 = 0;
         }

         for(var2 = Math.min(var0.length, var2) - 1; var2 > var1; ++var1) {
            Object var3 = var0[var2];
            var0[var2] = var0[var1];
            var0[var1] = var3;
            --var2;
         }

      }
   }

   public static void reverse(short[] var0) {
      if (var0 != null) {
         reverse((short[])var0, 0, var0.length);
      }
   }

   public static void reverse(short[] var0, int var1, int var2) {
      if (var0 != null) {
         if (var1 < 0) {
            var1 = 0;
         }

         for(var2 = Math.min(var0.length, var2) - 1; var2 > var1; ++var1) {
            short var3 = var0[var2];
            var0[var2] = var0[var1];
            var0[var1] = var3;
            --var2;
         }

      }
   }

   public static void reverse(boolean[] var0) {
      if (var0 != null) {
         reverse((boolean[])var0, 0, var0.length);
      }
   }

   public static void reverse(boolean[] var0, int var1, int var2) {
      if (var0 != null) {
         if (var1 < 0) {
            var1 = 0;
         }

         for(var2 = Math.min(var0.length, var2) - 1; var2 > var1; ++var1) {
            boolean var3 = var0[var2];
            var0[var2] = var0[var1];
            var0[var1] = var3;
            --var2;
         }

      }
   }

   public static void shift(byte[] var0, int var1) {
      if (var0 != null) {
         shift((byte[])var0, 0, var0.length, var1);
      }
   }

   public static void shift(byte[] var0, int var1, int var2, int var3) {
      if (var0 != null) {
         if (var1 < var0.length - 1) {
            if (var2 > 0) {
               int var4 = var1;
               if (var1 < 0) {
                  var4 = 0;
               }

               var1 = var2;
               if (var2 >= var0.length) {
                  var1 = var0.length;
               }

               int var5 = var1 - var4;
               if (var5 > 1) {
                  int var6 = var3 % var5;
                  var3 = var5;
                  var2 = var4;
                  var1 = var6;
                  if (var6 < 0) {
                     var1 = var6 + var5;
                     var2 = var4;
                     var3 = var5;
                  }

                  while(var3 > 1 && var1 > 0) {
                     var4 = var3 - var1;
                     if (var1 > var4) {
                        swap(var0, var2, var2 + var3 - var4, var4);
                        var4 = var1 - var4;
                        var1 = var1;
                     } else {
                        if (var1 >= var4) {
                           swap(var0, var2, var2 + var4, var1);
                           break;
                        }

                        swap(var0, var2, var2 + var4, var1);
                        var2 += var1;
                        var3 = var4;
                        var4 = var1;
                        var1 = var3;
                     }

                     var3 = var1;
                     var1 = var4;
                  }

               }
            }
         }
      }
   }

   public static void shift(char[] var0, int var1) {
      if (var0 != null) {
         shift((char[])var0, 0, var0.length, var1);
      }
   }

   public static void shift(char[] var0, int var1, int var2, int var3) {
      if (var0 != null) {
         if (var1 < var0.length - 1) {
            if (var2 > 0) {
               int var4 = var1;
               if (var1 < 0) {
                  var4 = 0;
               }

               var1 = var2;
               if (var2 >= var0.length) {
                  var1 = var0.length;
               }

               int var5 = var1 - var4;
               if (var5 > 1) {
                  int var6 = var3 % var5;
                  var3 = var5;
                  var2 = var4;
                  var1 = var6;
                  if (var6 < 0) {
                     var1 = var6 + var5;
                     var2 = var4;
                     var3 = var5;
                  }

                  while(var3 > 1 && var1 > 0) {
                     var4 = var3 - var1;
                     if (var1 > var4) {
                        swap(var0, var2, var2 + var3 - var4, var4);
                        var4 = var1 - var4;
                        var1 = var1;
                     } else {
                        if (var1 >= var4) {
                           swap(var0, var2, var2 + var4, var1);
                           break;
                        }

                        swap(var0, var2, var2 + var4, var1);
                        var2 += var1;
                        var3 = var4;
                        var4 = var1;
                        var1 = var3;
                     }

                     var3 = var1;
                     var1 = var4;
                  }

               }
            }
         }
      }
   }

   public static void shift(double[] var0, int var1) {
      if (var0 != null) {
         shift((double[])var0, 0, var0.length, var1);
      }
   }

   public static void shift(double[] var0, int var1, int var2, int var3) {
      if (var0 != null) {
         if (var1 < var0.length - 1) {
            if (var2 > 0) {
               int var4 = var1;
               if (var1 < 0) {
                  var4 = 0;
               }

               var1 = var2;
               if (var2 >= var0.length) {
                  var1 = var0.length;
               }

               int var5 = var1 - var4;
               if (var5 > 1) {
                  int var6 = var3 % var5;
                  var3 = var5;
                  var2 = var4;
                  var1 = var6;
                  if (var6 < 0) {
                     var1 = var6 + var5;
                     var2 = var4;
                     var3 = var5;
                  }

                  while(var3 > 1 && var1 > 0) {
                     var4 = var3 - var1;
                     if (var1 > var4) {
                        swap(var0, var2, var2 + var3 - var4, var4);
                        var4 = var1 - var4;
                        var1 = var1;
                     } else {
                        if (var1 >= var4) {
                           swap(var0, var2, var2 + var4, var1);
                           break;
                        }

                        swap(var0, var2, var2 + var4, var1);
                        var2 += var1;
                        var3 = var4;
                        var4 = var1;
                        var1 = var3;
                     }

                     var3 = var1;
                     var1 = var4;
                  }

               }
            }
         }
      }
   }

   public static void shift(float[] var0, int var1) {
      if (var0 != null) {
         shift((float[])var0, 0, var0.length, var1);
      }
   }

   public static void shift(float[] var0, int var1, int var2, int var3) {
      if (var0 != null) {
         if (var1 < var0.length - 1) {
            if (var2 > 0) {
               int var4 = var1;
               if (var1 < 0) {
                  var4 = 0;
               }

               var1 = var2;
               if (var2 >= var0.length) {
                  var1 = var0.length;
               }

               int var5 = var1 - var4;
               if (var5 > 1) {
                  int var6 = var3 % var5;
                  var3 = var5;
                  var2 = var4;
                  var1 = var6;
                  if (var6 < 0) {
                     var1 = var6 + var5;
                     var2 = var4;
                     var3 = var5;
                  }

                  while(var3 > 1 && var1 > 0) {
                     var4 = var3 - var1;
                     if (var1 > var4) {
                        swap(var0, var2, var2 + var3 - var4, var4);
                        var4 = var1 - var4;
                        var1 = var1;
                     } else {
                        if (var1 >= var4) {
                           swap(var0, var2, var2 + var4, var1);
                           break;
                        }

                        swap(var0, var2, var2 + var4, var1);
                        var2 += var1;
                        var3 = var4;
                        var4 = var1;
                        var1 = var3;
                     }

                     var3 = var1;
                     var1 = var4;
                  }

               }
            }
         }
      }
   }

   public static void shift(int[] var0, int var1) {
      if (var0 != null) {
         shift((int[])var0, 0, var0.length, var1);
      }
   }

   public static void shift(int[] var0, int var1, int var2, int var3) {
      if (var0 != null) {
         if (var1 < var0.length - 1) {
            if (var2 > 0) {
               int var4 = var1;
               if (var1 < 0) {
                  var4 = 0;
               }

               var1 = var2;
               if (var2 >= var0.length) {
                  var1 = var0.length;
               }

               int var5 = var1 - var4;
               if (var5 > 1) {
                  int var6 = var3 % var5;
                  var3 = var5;
                  var2 = var4;
                  var1 = var6;
                  if (var6 < 0) {
                     var1 = var6 + var5;
                     var2 = var4;
                     var3 = var5;
                  }

                  while(var3 > 1 && var1 > 0) {
                     var4 = var3 - var1;
                     if (var1 > var4) {
                        swap(var0, var2, var2 + var3 - var4, var4);
                        var4 = var1 - var4;
                        var1 = var1;
                     } else {
                        if (var1 >= var4) {
                           swap(var0, var2, var2 + var4, var1);
                           break;
                        }

                        swap(var0, var2, var2 + var4, var1);
                        var2 += var1;
                        var3 = var4;
                        var4 = var1;
                        var1 = var3;
                     }

                     var3 = var1;
                     var1 = var4;
                  }

               }
            }
         }
      }
   }

   public static void shift(long[] var0, int var1) {
      if (var0 != null) {
         shift((long[])var0, 0, var0.length, var1);
      }
   }

   public static void shift(long[] var0, int var1, int var2, int var3) {
      if (var0 != null) {
         if (var1 < var0.length - 1) {
            if (var2 > 0) {
               int var4 = var1;
               if (var1 < 0) {
                  var4 = 0;
               }

               var1 = var2;
               if (var2 >= var0.length) {
                  var1 = var0.length;
               }

               int var5 = var1 - var4;
               if (var5 > 1) {
                  int var6 = var3 % var5;
                  var3 = var5;
                  var2 = var4;
                  var1 = var6;
                  if (var6 < 0) {
                     var1 = var6 + var5;
                     var2 = var4;
                     var3 = var5;
                  }

                  while(var3 > 1 && var1 > 0) {
                     var4 = var3 - var1;
                     if (var1 > var4) {
                        swap(var0, var2, var2 + var3 - var4, var4);
                        var4 = var1 - var4;
                        var1 = var1;
                     } else {
                        if (var1 >= var4) {
                           swap(var0, var2, var2 + var4, var1);
                           break;
                        }

                        swap(var0, var2, var2 + var4, var1);
                        var2 += var1;
                        var3 = var4;
                        var4 = var1;
                        var1 = var3;
                     }

                     var3 = var1;
                     var1 = var4;
                  }

               }
            }
         }
      }
   }

   public static void shift(Object[] var0, int var1) {
      if (var0 != null) {
         shift((Object[])var0, 0, var0.length, var1);
      }
   }

   public static void shift(Object[] var0, int var1, int var2, int var3) {
      if (var0 != null) {
         if (var1 < var0.length - 1) {
            if (var2 > 0) {
               int var4 = var1;
               if (var1 < 0) {
                  var4 = 0;
               }

               var1 = var2;
               if (var2 >= var0.length) {
                  var1 = var0.length;
               }

               int var5 = var1 - var4;
               if (var5 > 1) {
                  int var6 = var3 % var5;
                  var3 = var5;
                  var2 = var4;
                  var1 = var6;
                  if (var6 < 0) {
                     var1 = var6 + var5;
                     var2 = var4;
                     var3 = var5;
                  }

                  while(var3 > 1 && var1 > 0) {
                     var4 = var3 - var1;
                     if (var1 > var4) {
                        swap(var0, var2, var2 + var3 - var4, var4);
                        var4 = var1 - var4;
                        var1 = var1;
                     } else {
                        if (var1 >= var4) {
                           swap(var0, var2, var2 + var4, var1);
                           break;
                        }

                        swap(var0, var2, var2 + var4, var1);
                        var2 += var1;
                        var3 = var4;
                        var4 = var1;
                        var1 = var3;
                     }

                     var3 = var1;
                     var1 = var4;
                  }

               }
            }
         }
      }
   }

   public static void shift(short[] var0, int var1) {
      if (var0 != null) {
         shift((short[])var0, 0, var0.length, var1);
      }
   }

   public static void shift(short[] var0, int var1, int var2, int var3) {
      if (var0 != null) {
         if (var1 < var0.length - 1) {
            if (var2 > 0) {
               int var4 = var1;
               if (var1 < 0) {
                  var4 = 0;
               }

               var1 = var2;
               if (var2 >= var0.length) {
                  var1 = var0.length;
               }

               int var5 = var1 - var4;
               if (var5 > 1) {
                  int var6 = var3 % var5;
                  var3 = var5;
                  var2 = var4;
                  var1 = var6;
                  if (var6 < 0) {
                     var1 = var6 + var5;
                     var2 = var4;
                     var3 = var5;
                  }

                  while(var3 > 1 && var1 > 0) {
                     var4 = var3 - var1;
                     if (var1 > var4) {
                        swap(var0, var2, var2 + var3 - var4, var4);
                        var4 = var1 - var4;
                        var1 = var1;
                     } else {
                        if (var1 >= var4) {
                           swap(var0, var2, var2 + var4, var1);
                           break;
                        }

                        swap(var0, var2, var2 + var4, var1);
                        var2 += var1;
                        var3 = var4;
                        var4 = var1;
                        var1 = var3;
                     }

                     var3 = var1;
                     var1 = var4;
                  }

               }
            }
         }
      }
   }

   public static void shift(boolean[] var0, int var1) {
      if (var0 != null) {
         shift((boolean[])var0, 0, var0.length, var1);
      }
   }

   public static void shift(boolean[] var0, int var1, int var2, int var3) {
      if (var0 != null) {
         if (var1 < var0.length - 1) {
            if (var2 > 0) {
               int var4 = var1;
               if (var1 < 0) {
                  var4 = 0;
               }

               var1 = var2;
               if (var2 >= var0.length) {
                  var1 = var0.length;
               }

               int var5 = var1 - var4;
               if (var5 > 1) {
                  int var6 = var3 % var5;
                  var3 = var5;
                  var2 = var4;
                  var1 = var6;
                  if (var6 < 0) {
                     var1 = var6 + var5;
                     var2 = var4;
                     var3 = var5;
                  }

                  while(var3 > 1 && var1 > 0) {
                     var4 = var3 - var1;
                     if (var1 > var4) {
                        swap(var0, var2, var2 + var3 - var4, var4);
                        var4 = var1 - var4;
                        var1 = var1;
                     } else {
                        if (var1 >= var4) {
                           swap(var0, var2, var2 + var4, var1);
                           break;
                        }

                        swap(var0, var2, var2 + var4, var1);
                        var2 += var1;
                        var3 = var4;
                        var4 = var1;
                        var1 = var3;
                     }

                     var3 = var1;
                     var1 = var4;
                  }

               }
            }
         }
      }
   }

   public static void shuffle(byte[] var0) {
      shuffle(var0, new Random());
   }

   public static void shuffle(byte[] var0, Random var1) {
      for(int var2 = var0.length; var2 > 1; --var2) {
         swap((byte[])var0, var2 - 1, var1.nextInt(var2), 1);
      }

   }

   public static void shuffle(char[] var0) {
      shuffle(var0, new Random());
   }

   public static void shuffle(char[] var0, Random var1) {
      for(int var2 = var0.length; var2 > 1; --var2) {
         swap((char[])var0, var2 - 1, var1.nextInt(var2), 1);
      }

   }

   public static void shuffle(double[] var0) {
      shuffle(var0, new Random());
   }

   public static void shuffle(double[] var0, Random var1) {
      for(int var2 = var0.length; var2 > 1; --var2) {
         swap((double[])var0, var2 - 1, var1.nextInt(var2), 1);
      }

   }

   public static void shuffle(float[] var0) {
      shuffle(var0, new Random());
   }

   public static void shuffle(float[] var0, Random var1) {
      for(int var2 = var0.length; var2 > 1; --var2) {
         swap((float[])var0, var2 - 1, var1.nextInt(var2), 1);
      }

   }

   public static void shuffle(int[] var0) {
      shuffle(var0, new Random());
   }

   public static void shuffle(int[] var0, Random var1) {
      for(int var2 = var0.length; var2 > 1; --var2) {
         swap((int[])var0, var2 - 1, var1.nextInt(var2), 1);
      }

   }

   public static void shuffle(long[] var0) {
      shuffle(var0, new Random());
   }

   public static void shuffle(long[] var0, Random var1) {
      for(int var2 = var0.length; var2 > 1; --var2) {
         swap((long[])var0, var2 - 1, var1.nextInt(var2), 1);
      }

   }

   public static void shuffle(Object[] var0) {
      shuffle(var0, new Random());
   }

   public static void shuffle(Object[] var0, Random var1) {
      for(int var2 = var0.length; var2 > 1; --var2) {
         swap((Object[])var0, var2 - 1, var1.nextInt(var2), 1);
      }

   }

   public static void shuffle(short[] var0) {
      shuffle(var0, new Random());
   }

   public static void shuffle(short[] var0, Random var1) {
      for(int var2 = var0.length; var2 > 1; --var2) {
         swap((short[])var0, var2 - 1, var1.nextInt(var2), 1);
      }

   }

   public static void shuffle(boolean[] var0) {
      shuffle(var0, new Random());
   }

   public static void shuffle(boolean[] var0, Random var1) {
      for(int var2 = var0.length; var2 > 1; --var2) {
         swap((boolean[])var0, var2 - 1, var1.nextInt(var2), 1);
      }

   }

   public static byte[] subarray(byte[] var0, int var1, int var2) {
      if (var0 == null) {
         return null;
      } else {
         int var3 = var1;
         if (var1 < 0) {
            var3 = 0;
         }

         var1 = var2;
         if (var2 > var0.length) {
            var1 = var0.length;
         }

         var1 -= var3;
         if (var1 <= 0) {
            return EMPTY_BYTE_ARRAY;
         } else {
            byte[] var4 = new byte[var1];
            System.arraycopy(var0, var3, var4, 0, var1);
            return var4;
         }
      }
   }

   public static char[] subarray(char[] var0, int var1, int var2) {
      if (var0 == null) {
         return null;
      } else {
         int var3 = var1;
         if (var1 < 0) {
            var3 = 0;
         }

         var1 = var2;
         if (var2 > var0.length) {
            var1 = var0.length;
         }

         var1 -= var3;
         if (var1 <= 0) {
            return EMPTY_CHAR_ARRAY;
         } else {
            char[] var4 = new char[var1];
            System.arraycopy(var0, var3, var4, 0, var1);
            return var4;
         }
      }
   }

   public static double[] subarray(double[] var0, int var1, int var2) {
      if (var0 == null) {
         return null;
      } else {
         int var3 = var1;
         if (var1 < 0) {
            var3 = 0;
         }

         var1 = var2;
         if (var2 > var0.length) {
            var1 = var0.length;
         }

         var1 -= var3;
         if (var1 <= 0) {
            return EMPTY_DOUBLE_ARRAY;
         } else {
            double[] var4 = new double[var1];
            System.arraycopy(var0, var3, var4, 0, var1);
            return var4;
         }
      }
   }

   public static float[] subarray(float[] var0, int var1, int var2) {
      if (var0 == null) {
         return null;
      } else {
         int var3 = var1;
         if (var1 < 0) {
            var3 = 0;
         }

         var1 = var2;
         if (var2 > var0.length) {
            var1 = var0.length;
         }

         var1 -= var3;
         if (var1 <= 0) {
            return EMPTY_FLOAT_ARRAY;
         } else {
            float[] var4 = new float[var1];
            System.arraycopy(var0, var3, var4, 0, var1);
            return var4;
         }
      }
   }

   public static int[] subarray(int[] var0, int var1, int var2) {
      if (var0 == null) {
         return null;
      } else {
         int var3 = var1;
         if (var1 < 0) {
            var3 = 0;
         }

         var1 = var2;
         if (var2 > var0.length) {
            var1 = var0.length;
         }

         var1 -= var3;
         if (var1 <= 0) {
            return EMPTY_INT_ARRAY;
         } else {
            int[] var4 = new int[var1];
            System.arraycopy(var0, var3, var4, 0, var1);
            return var4;
         }
      }
   }

   public static long[] subarray(long[] var0, int var1, int var2) {
      if (var0 == null) {
         return null;
      } else {
         int var3 = var1;
         if (var1 < 0) {
            var3 = 0;
         }

         var1 = var2;
         if (var2 > var0.length) {
            var1 = var0.length;
         }

         var1 -= var3;
         if (var1 <= 0) {
            return EMPTY_LONG_ARRAY;
         } else {
            long[] var4 = new long[var1];
            System.arraycopy(var0, var3, var4, 0, var1);
            return var4;
         }
      }
   }

   public static Object[] subarray(Object[] var0, int var1, int var2) {
      if (var0 == null) {
         return null;
      } else {
         int var3 = var1;
         if (var1 < 0) {
            var3 = 0;
         }

         var1 = var2;
         if (var2 > var0.length) {
            var1 = var0.length;
         }

         var1 -= var3;
         Class var4 = var0.getClass().getComponentType();
         if (var1 <= 0) {
            return (Object[])Array.newInstance(var4, 0);
         } else {
            Object[] var5 = (Object[])Array.newInstance(var4, var1);
            System.arraycopy(var0, var3, var5, 0, var1);
            return var5;
         }
      }
   }

   public static short[] subarray(short[] var0, int var1, int var2) {
      if (var0 == null) {
         return null;
      } else {
         int var3 = var1;
         if (var1 < 0) {
            var3 = 0;
         }

         var1 = var2;
         if (var2 > var0.length) {
            var1 = var0.length;
         }

         var1 -= var3;
         if (var1 <= 0) {
            return EMPTY_SHORT_ARRAY;
         } else {
            short[] var4 = new short[var1];
            System.arraycopy(var0, var3, var4, 0, var1);
            return var4;
         }
      }
   }

   public static boolean[] subarray(boolean[] var0, int var1, int var2) {
      if (var0 == null) {
         return null;
      } else {
         int var3 = var1;
         if (var1 < 0) {
            var3 = 0;
         }

         var1 = var2;
         if (var2 > var0.length) {
            var1 = var0.length;
         }

         var1 -= var3;
         if (var1 <= 0) {
            return EMPTY_BOOLEAN_ARRAY;
         } else {
            boolean[] var4 = new boolean[var1];
            System.arraycopy(var0, var3, var4, 0, var1);
            return var4;
         }
      }
   }

   public static void swap(byte[] var0, int var1, int var2) {
      if (var0 != null) {
         if (var0.length != 0) {
            swap((byte[])var0, var1, var2, 1);
         }
      }
   }

   public static void swap(byte[] var0, int var1, int var2, int var3) {
      if (var0 != null && var0.length != 0 && var1 < var0.length) {
         if (var2 < var0.length) {
            int var5 = var1;
            if (var1 < 0) {
               var5 = 0;
            }

            var1 = var2;
            if (var2 < 0) {
               var1 = 0;
            }

            var3 = Math.min(Math.min(var3, var0.length - var5), var0.length - var1);

            for(var2 = 0; var2 < var3; ++var1) {
               byte var4 = var0[var5];
               var0[var5] = var0[var1];
               var0[var1] = var4;
               ++var2;
               ++var5;
            }

         }
      }
   }

   public static void swap(char[] var0, int var1, int var2) {
      if (var0 != null) {
         if (var0.length != 0) {
            swap((char[])var0, var1, var2, 1);
         }
      }
   }

   public static void swap(char[] var0, int var1, int var2, int var3) {
      if (var0 != null && var0.length != 0 && var1 < var0.length) {
         if (var2 < var0.length) {
            int var5 = var1;
            if (var1 < 0) {
               var5 = 0;
            }

            var1 = var2;
            if (var2 < 0) {
               var1 = 0;
            }

            var3 = Math.min(Math.min(var3, var0.length - var5), var0.length - var1);

            for(var2 = 0; var2 < var3; ++var1) {
               char var4 = var0[var5];
               var0[var5] = var0[var1];
               var0[var1] = var4;
               ++var2;
               ++var5;
            }

         }
      }
   }

   public static void swap(double[] var0, int var1, int var2) {
      if (var0 != null) {
         if (var0.length != 0) {
            swap((double[])var0, var1, var2, 1);
         }
      }
   }

   public static void swap(double[] var0, int var1, int var2, int var3) {
      if (var0 != null && var0.length != 0 && var1 < var0.length) {
         if (var2 < var0.length) {
            int var6 = var1;
            if (var1 < 0) {
               var6 = 0;
            }

            var1 = var2;
            if (var2 < 0) {
               var1 = 0;
            }

            var3 = Math.min(Math.min(var3, var0.length - var6), var0.length - var1);

            for(var2 = 0; var2 < var3; ++var1) {
               double var4 = var0[var6];
               var0[var6] = var0[var1];
               var0[var1] = var4;
               ++var2;
               ++var6;
            }

         }
      }
   }

   public static void swap(float[] var0, int var1, int var2) {
      if (var0 != null) {
         if (var0.length != 0) {
            swap((float[])var0, var1, var2, 1);
         }
      }
   }

   public static void swap(float[] var0, int var1, int var2, int var3) {
      if (var0 != null && var0.length != 0 && var1 < var0.length) {
         if (var2 < var0.length) {
            int var5 = var1;
            if (var1 < 0) {
               var5 = 0;
            }

            var1 = var2;
            if (var2 < 0) {
               var1 = 0;
            }

            var3 = Math.min(Math.min(var3, var0.length - var5), var0.length - var1);

            for(var2 = 0; var2 < var3; ++var1) {
               float var4 = var0[var5];
               var0[var5] = var0[var1];
               var0[var1] = var4;
               ++var2;
               ++var5;
            }

         }
      }
   }

   public static void swap(int[] var0, int var1, int var2) {
      if (var0 != null) {
         if (var0.length != 0) {
            swap((int[])var0, var1, var2, 1);
         }
      }
   }

   public static void swap(int[] var0, int var1, int var2, int var3) {
      if (var0 != null && var0.length != 0 && var1 < var0.length) {
         if (var2 < var0.length) {
            int var4 = var1;
            if (var1 < 0) {
               var4 = 0;
            }

            var1 = var2;
            if (var2 < 0) {
               var1 = 0;
            }

            var3 = Math.min(Math.min(var3, var0.length - var4), var0.length - var1);

            for(var2 = 0; var2 < var3; ++var1) {
               int var5 = var0[var4];
               var0[var4] = var0[var1];
               var0[var1] = var5;
               ++var2;
               ++var4;
            }

         }
      }
   }

   public static void swap(long[] var0, int var1, int var2) {
      if (var0 != null) {
         if (var0.length != 0) {
            swap((long[])var0, var1, var2, 1);
         }
      }
   }

   public static void swap(long[] var0, int var1, int var2, int var3) {
      if (var0 != null && var0.length != 0 && var1 < var0.length) {
         if (var2 < var0.length) {
            int var4 = var1;
            if (var1 < 0) {
               var4 = 0;
            }

            var1 = var2;
            if (var2 < 0) {
               var1 = 0;
            }

            var3 = Math.min(Math.min(var3, var0.length - var4), var0.length - var1);

            for(var2 = 0; var2 < var3; ++var1) {
               long var5 = var0[var4];
               var0[var4] = var0[var1];
               var0[var1] = var5;
               ++var2;
               ++var4;
            }

         }
      }
   }

   public static void swap(Object[] var0, int var1, int var2) {
      if (var0 != null) {
         if (var0.length != 0) {
            swap((Object[])var0, var1, var2, 1);
         }
      }
   }

   public static void swap(Object[] var0, int var1, int var2, int var3) {
      if (var0 != null && var0.length != 0 && var1 < var0.length) {
         if (var2 < var0.length) {
            int var4 = var1;
            if (var1 < 0) {
               var4 = 0;
            }

            var1 = var2;
            if (var2 < 0) {
               var1 = 0;
            }

            var3 = Math.min(Math.min(var3, var0.length - var4), var0.length - var1);

            for(var2 = 0; var2 < var3; ++var1) {
               Object var5 = var0[var4];
               var0[var4] = var0[var1];
               var0[var1] = var5;
               ++var2;
               ++var4;
            }

         }
      }
   }

   public static void swap(short[] var0, int var1, int var2) {
      if (var0 != null) {
         if (var0.length != 0) {
            swap((short[])var0, var1, var2, 1);
         }
      }
   }

   public static void swap(short[] var0, int var1, int var2, int var3) {
      if (var0 != null && var0.length != 0 && var1 < var0.length) {
         if (var2 < var0.length) {
            int var5 = var1;
            if (var1 < 0) {
               var5 = 0;
            }

            var1 = var2;
            if (var2 < 0) {
               var1 = 0;
            }

            if (var5 != var1) {
               var3 = Math.min(Math.min(var3, var0.length - var5), var0.length - var1);

               for(var2 = 0; var2 < var3; ++var1) {
                  short var4 = var0[var5];
                  var0[var5] = var0[var1];
                  var0[var1] = var4;
                  ++var2;
                  ++var5;
               }

            }
         }
      }
   }

   public static void swap(boolean[] var0, int var1, int var2) {
      if (var0 != null) {
         if (var0.length != 0) {
            swap((boolean[])var0, var1, var2, 1);
         }
      }
   }

   public static void swap(boolean[] var0, int var1, int var2, int var3) {
      if (var0 != null && var0.length != 0 && var1 < var0.length) {
         if (var2 < var0.length) {
            int var4 = var1;
            if (var1 < 0) {
               var4 = 0;
            }

            var1 = var2;
            if (var2 < 0) {
               var1 = 0;
            }

            var3 = Math.min(Math.min(var3, var0.length - var4), var0.length - var1);

            for(var2 = 0; var2 < var3; ++var1) {
               boolean var5 = var0[var4];
               var0[var4] = var0[var1];
               var0[var1] = var5;
               ++var2;
               ++var4;
            }

         }
      }
   }

   public static Object[] toArray(Object... var0) {
      return var0;
   }

   public static Map toMap(Object[] var0) {
      if (var0 == null) {
         return null;
      } else {
         HashMap var3 = new HashMap((int)((double)var0.length * 1.5D));

         for(int var1 = 0; var1 < var0.length; ++var1) {
            Object var2 = var0[var1];
            if (var2 instanceof Entry) {
               Entry var6 = (Entry)var2;
               var3.put(var6.getKey(), var6.getValue());
            } else {
               StringBuilder var5;
               if (!(var2 instanceof Object[])) {
                  var5 = new StringBuilder();
                  var5.append("Array element ");
                  var5.append(var1);
                  var5.append(", '");
                  var5.append(var2);
                  var5.append("', is neither of type Map.Entry nor an Array");
                  throw new IllegalArgumentException(var5.toString());
               }

               Object[] var4 = (Object[])var2;
               if (var4.length < 2) {
                  var5 = new StringBuilder();
                  var5.append("Array element ");
                  var5.append(var1);
                  var5.append(", '");
                  var5.append(var2);
                  var5.append("', has a length less than 2");
                  throw new IllegalArgumentException(var5.toString());
               }

               var3.put(var4[0], var4[1]);
            }
         }

         return var3;
      }
   }

   public static Boolean[] toObject(boolean[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_BOOLEAN_OBJECT_ARRAY;
      } else {
         Boolean[] var3 = new Boolean[var0.length];

         for(int var1 = 0; var1 < var0.length; ++var1) {
            Boolean var2;
            if (var0[var1]) {
               var2 = Boolean.TRUE;
            } else {
               var2 = Boolean.FALSE;
            }

            var3[var1] = var2;
         }

         return var3;
      }
   }

   public static Byte[] toObject(byte[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_BYTE_OBJECT_ARRAY;
      } else {
         Byte[] var2 = new Byte[var0.length];

         for(int var1 = 0; var1 < var0.length; ++var1) {
            var2[var1] = var0[var1];
         }

         return var2;
      }
   }

   public static Character[] toObject(char[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_CHARACTER_OBJECT_ARRAY;
      } else {
         Character[] var2 = new Character[var0.length];

         for(int var1 = 0; var1 < var0.length; ++var1) {
            var2[var1] = var0[var1];
         }

         return var2;
      }
   }

   public static Double[] toObject(double[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_DOUBLE_OBJECT_ARRAY;
      } else {
         Double[] var2 = new Double[var0.length];

         for(int var1 = 0; var1 < var0.length; ++var1) {
            var2[var1] = var0[var1];
         }

         return var2;
      }
   }

   public static Float[] toObject(float[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_FLOAT_OBJECT_ARRAY;
      } else {
         Float[] var2 = new Float[var0.length];

         for(int var1 = 0; var1 < var0.length; ++var1) {
            var2[var1] = var0[var1];
         }

         return var2;
      }
   }

   public static Integer[] toObject(int[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_INTEGER_OBJECT_ARRAY;
      } else {
         Integer[] var2 = new Integer[var0.length];

         for(int var1 = 0; var1 < var0.length; ++var1) {
            var2[var1] = var0[var1];
         }

         return var2;
      }
   }

   public static Long[] toObject(long[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_LONG_OBJECT_ARRAY;
      } else {
         Long[] var2 = new Long[var0.length];

         for(int var1 = 0; var1 < var0.length; ++var1) {
            var2[var1] = var0[var1];
         }

         return var2;
      }
   }

   public static Short[] toObject(short[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_SHORT_OBJECT_ARRAY;
      } else {
         Short[] var2 = new Short[var0.length];

         for(int var1 = 0; var1 < var0.length; ++var1) {
            var2[var1] = var0[var1];
         }

         return var2;
      }
   }

   public static Object toPrimitive(Object var0) {
      if (var0 == null) {
         return null;
      } else {
         Class var1 = ClassUtils.wrapperToPrimitive(var0.getClass().getComponentType());
         if (Integer.TYPE.equals(var1)) {
            return toPrimitive((Integer[])var0);
         } else if (Long.TYPE.equals(var1)) {
            return toPrimitive((Long[])var0);
         } else if (Short.TYPE.equals(var1)) {
            return toPrimitive((Short[])var0);
         } else if (Double.TYPE.equals(var1)) {
            return toPrimitive((Double[])var0);
         } else {
            return Float.TYPE.equals(var1) ? toPrimitive((Float[])var0) : var0;
         }
      }
   }

   public static byte[] toPrimitive(Byte[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_BYTE_ARRAY;
      } else {
         byte[] var2 = new byte[var0.length];

         for(int var1 = 0; var1 < var0.length; ++var1) {
            var2[var1] = var0[var1];
         }

         return var2;
      }
   }

   public static byte[] toPrimitive(Byte[] var0, byte var1) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_BYTE_ARRAY;
      } else {
         byte[] var4 = new byte[var0.length];

         for(int var3 = 0; var3 < var0.length; ++var3) {
            Byte var5 = var0[var3];
            byte var2;
            if (var5 == null) {
               var2 = var1;
            } else {
               var2 = var5;
            }

            var4[var3] = var2;
         }

         return var4;
      }
   }

   public static char[] toPrimitive(Character[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_CHAR_ARRAY;
      } else {
         char[] var2 = new char[var0.length];

         for(int var1 = 0; var1 < var0.length; ++var1) {
            var2[var1] = var0[var1];
         }

         return var2;
      }
   }

   public static char[] toPrimitive(Character[] var0, char var1) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_CHAR_ARRAY;
      } else {
         char[] var4 = new char[var0.length];

         for(int var3 = 0; var3 < var0.length; ++var3) {
            Character var5 = var0[var3];
            char var2;
            if (var5 == null) {
               var2 = var1;
            } else {
               var2 = var5;
            }

            var4[var3] = var2;
         }

         return var4;
      }
   }

   public static double[] toPrimitive(Double[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_DOUBLE_ARRAY;
      } else {
         double[] var2 = new double[var0.length];

         for(int var1 = 0; var1 < var0.length; ++var1) {
            var2[var1] = var0[var1];
         }

         return var2;
      }
   }

   public static double[] toPrimitive(Double[] var0, double var1) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_DOUBLE_ARRAY;
      } else {
         double[] var6 = new double[var0.length];

         for(int var5 = 0; var5 < var0.length; ++var5) {
            Double var7 = var0[var5];
            double var3;
            if (var7 == null) {
               var3 = var1;
            } else {
               var3 = var7;
            }

            var6[var5] = var3;
         }

         return var6;
      }
   }

   public static float[] toPrimitive(Float[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_FLOAT_ARRAY;
      } else {
         float[] var2 = new float[var0.length];

         for(int var1 = 0; var1 < var0.length; ++var1) {
            var2[var1] = var0[var1];
         }

         return var2;
      }
   }

   public static float[] toPrimitive(Float[] var0, float var1) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_FLOAT_ARRAY;
      } else {
         float[] var4 = new float[var0.length];

         for(int var3 = 0; var3 < var0.length; ++var3) {
            Float var5 = var0[var3];
            float var2;
            if (var5 == null) {
               var2 = var1;
            } else {
               var2 = var5;
            }

            var4[var3] = var2;
         }

         return var4;
      }
   }

   public static int[] toPrimitive(Integer[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_INT_ARRAY;
      } else {
         int[] var2 = new int[var0.length];

         for(int var1 = 0; var1 < var0.length; ++var1) {
            var2[var1] = var0[var1];
         }

         return var2;
      }
   }

   public static int[] toPrimitive(Integer[] var0, int var1) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_INT_ARRAY;
      } else {
         int[] var4 = new int[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            Integer var5 = var0[var2];
            int var3;
            if (var5 == null) {
               var3 = var1;
            } else {
               var3 = var5;
            }

            var4[var2] = var3;
         }

         return var4;
      }
   }

   public static long[] toPrimitive(Long[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_LONG_ARRAY;
      } else {
         long[] var2 = new long[var0.length];

         for(int var1 = 0; var1 < var0.length; ++var1) {
            var2[var1] = var0[var1];
         }

         return var2;
      }
   }

   public static long[] toPrimitive(Long[] var0, long var1) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_LONG_ARRAY;
      } else {
         long[] var6 = new long[var0.length];

         for(int var3 = 0; var3 < var0.length; ++var3) {
            Long var7 = var0[var3];
            long var4;
            if (var7 == null) {
               var4 = var1;
            } else {
               var4 = var7;
            }

            var6[var3] = var4;
         }

         return var6;
      }
   }

   public static short[] toPrimitive(Short[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_SHORT_ARRAY;
      } else {
         short[] var2 = new short[var0.length];

         for(int var1 = 0; var1 < var0.length; ++var1) {
            var2[var1] = var0[var1];
         }

         return var2;
      }
   }

   public static short[] toPrimitive(Short[] var0, short var1) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_SHORT_ARRAY;
      } else {
         short[] var4 = new short[var0.length];

         for(int var3 = 0; var3 < var0.length; ++var3) {
            Short var5 = var0[var3];
            short var2;
            if (var5 == null) {
               var2 = var1;
            } else {
               var2 = var5;
            }

            var4[var3] = var2;
         }

         return var4;
      }
   }

   public static boolean[] toPrimitive(Boolean[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_BOOLEAN_ARRAY;
      } else {
         boolean[] var2 = new boolean[var0.length];

         for(int var1 = 0; var1 < var0.length; ++var1) {
            var2[var1] = var0[var1];
         }

         return var2;
      }
   }

   public static boolean[] toPrimitive(Boolean[] var0, boolean var1) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_BOOLEAN_ARRAY;
      } else {
         boolean[] var4 = new boolean[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            Boolean var5 = var0[var2];
            boolean var3;
            if (var5 == null) {
               var3 = var1;
            } else {
               var3 = var5;
            }

            var4[var2] = var3;
         }

         return var4;
      }
   }

   public static String toString(Object var0) {
      return toString(var0, "{}");
   }

   public static String toString(Object var0, String var1) {
      return var0 == null ? var1 : (new ToStringBuilder(var0, ToStringStyle.SIMPLE_STYLE)).append(var0).toString();
   }

   public static String[] toStringArray(Object[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_STRING_ARRAY;
      } else {
         String[] var2 = new String[var0.length];

         for(int var1 = 0; var1 < var0.length; ++var1) {
            var2[var1] = var0[var1].toString();
         }

         return var2;
      }
   }

   public static String[] toStringArray(Object[] var0, String var1) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_STRING_ARRAY;
      } else {
         String[] var4 = new String[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            Object var3 = var0[var2];
            String var5;
            if (var3 == null) {
               var5 = var1;
            } else {
               var5 = var3.toString();
            }

            var4[var2] = var5;
         }

         return var4;
      }
   }
}
