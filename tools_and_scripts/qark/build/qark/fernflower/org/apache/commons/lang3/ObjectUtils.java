package org.apache.commons.lang3;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.Map.Entry;
import org.apache.commons.lang3.exception.CloneFailedException;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.text.StrBuilder;

public class ObjectUtils {
   private static final char AT_SIGN = '@';
   public static final ObjectUtils.Null NULL = new ObjectUtils.Null();

   public static byte CONST(byte var0) {
      return var0;
   }

   public static char CONST(char var0) {
      return var0;
   }

   public static double CONST(double var0) {
      return var0;
   }

   public static float CONST(float var0) {
      return var0;
   }

   public static int CONST(int var0) {
      return var0;
   }

   public static long CONST(long var0) {
      return var0;
   }

   public static Object CONST(Object var0) {
      return var0;
   }

   public static short CONST(short var0) {
      return var0;
   }

   public static boolean CONST(boolean var0) {
      return var0;
   }

   public static byte CONST_BYTE(int var0) {
      if (var0 >= -128 && var0 <= 127) {
         return (byte)var0;
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("Supplied value must be a valid byte literal between -128 and 127: [");
         var1.append(var0);
         var1.append("]");
         throw new IllegalArgumentException(var1.toString());
      }
   }

   public static short CONST_SHORT(int var0) {
      if (var0 >= -32768 && var0 <= 32767) {
         return (short)var0;
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("Supplied value must be a valid byte literal between -32768 and 32767: [");
         var1.append(var0);
         var1.append("]");
         throw new IllegalArgumentException(var1.toString());
      }
   }

   public static boolean allNotNull(Object... var0) {
      if (var0 == null) {
         return false;
      } else {
         int var2 = var0.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            if (var0[var1] == null) {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean anyNotNull(Object... var0) {
      return firstNonNull(var0) != null;
   }

   public static Object clone(Object var0) {
      if (!(var0 instanceof Cloneable)) {
         return null;
      } else {
         Object var8;
         if (var0.getClass().isArray()) {
            Class var3 = var0.getClass().getComponentType();
            if (var3.isPrimitive()) {
               int var1 = Array.getLength(var0);
               var8 = Array.newInstance(var3, var1);

               while(true) {
                  int var2 = var1 - 1;
                  if (var1 <= 0) {
                     var0 = var8;
                     break;
                  }

                  Array.set(var8, var2, Array.get(var0, var2));
                  var1 = var2;
               }
            } else {
               var0 = ((Object[])var0).clone();
            }
         } else {
            StringBuilder var4;
            try {
               var8 = var0.getClass().getMethod("clone").invoke(var0);
            } catch (NoSuchMethodException var5) {
               var4 = new StringBuilder();
               var4.append("Cloneable type ");
               var4.append(var0.getClass().getName());
               var4.append(" has no clone method");
               throw new CloneFailedException(var4.toString(), var5);
            } catch (IllegalAccessException var6) {
               var4 = new StringBuilder();
               var4.append("Cannot clone Cloneable type ");
               var4.append(var0.getClass().getName());
               throw new CloneFailedException(var4.toString(), var6);
            } catch (InvocationTargetException var7) {
               var4 = new StringBuilder();
               var4.append("Exception cloning Cloneable type ");
               var4.append(var0.getClass().getName());
               throw new CloneFailedException(var4.toString(), var7.getCause());
            }

            var0 = var8;
         }

         return var0;
      }
   }

   public static Object cloneIfPossible(Object var0) {
      Object var1 = clone(var0);
      return var1 == null ? var0 : var1;
   }

   public static int compare(Comparable var0, Comparable var1) {
      return compare(var0, var1, false);
   }

   public static int compare(Comparable var0, Comparable var1, boolean var2) {
      if (var0 == var1) {
         return 0;
      } else {
         byte var3 = 1;
         if (var0 == null) {
            return var2 ? 1 : -1;
         } else if (var1 == null) {
            if (var2) {
               var3 = -1;
            }

            return var3;
         } else {
            return var0.compareTo(var1);
         }
      }
   }

   public static Object defaultIfNull(Object var0, Object var1) {
      return var0 != null ? var0 : var1;
   }

   @Deprecated
   public static boolean equals(Object var0, Object var1) {
      if (var0 == var1) {
         return true;
      } else {
         return var0 != null && var1 != null ? var0.equals(var1) : false;
      }
   }

   @SafeVarargs
   public static Object firstNonNull(Object... var0) {
      if (var0 != null) {
         int var2 = var0.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            Object var3 = var0[var1];
            if (var3 != null) {
               return var3;
            }
         }
      }

      return null;
   }

   @Deprecated
   public static int hashCode(Object var0) {
      return var0 == null ? 0 : var0.hashCode();
   }

   @Deprecated
   public static int hashCodeMulti(Object... var0) {
      int var3 = 1;
      int var1 = 1;
      if (var0 != null) {
         int var4 = var0.length;
         int var2 = 0;

         while(true) {
            var3 = var1;
            if (var2 >= var4) {
               break;
            }

            var1 = var1 * 31 + hashCode(var0[var2]);
            ++var2;
         }
      }

      return var3;
   }

   public static String identityToString(Object var0) {
      if (var0 == null) {
         return null;
      } else {
         String var1 = var0.getClass().getName();
         String var3 = Integer.toHexString(System.identityHashCode(var0));
         StringBuilder var2 = new StringBuilder(var1.length() + 1 + var3.length());
         var2.append(var1);
         var2.append('@');
         var2.append(var3);
         return var2.toString();
      }
   }

   public static void identityToString(Appendable var0, Object var1) throws IOException {
      Validate.notNull(var1, "Cannot get the toString of a null object");
      var0.append(var1.getClass().getName()).append('@').append(Integer.toHexString(System.identityHashCode(var1)));
   }

   public static void identityToString(StringBuffer var0, Object var1) {
      Validate.notNull(var1, "Cannot get the toString of a null object");
      String var2 = var1.getClass().getName();
      String var3 = Integer.toHexString(System.identityHashCode(var1));
      var0.ensureCapacity(var0.length() + var2.length() + 1 + var3.length());
      var0.append(var2);
      var0.append('@');
      var0.append(var3);
   }

   public static void identityToString(StringBuilder var0, Object var1) {
      Validate.notNull(var1, "Cannot get the toString of a null object");
      String var2 = var1.getClass().getName();
      String var3 = Integer.toHexString(System.identityHashCode(var1));
      var0.ensureCapacity(var0.length() + var2.length() + 1 + var3.length());
      var0.append(var2);
      var0.append('@');
      var0.append(var3);
   }

   @Deprecated
   public static void identityToString(StrBuilder var0, Object var1) {
      Validate.notNull(var1, "Cannot get the toString of a null object");
      String var2 = var1.getClass().getName();
      String var3 = Integer.toHexString(System.identityHashCode(var1));
      var0.ensureCapacity(var0.length() + var2.length() + 1 + var3.length());
      var0.append(var2).append('@').append(var3);
   }

   public static boolean isEmpty(Object var0) {
      if (var0 == null) {
         return true;
      } else if (var0 instanceof CharSequence) {
         return ((CharSequence)var0).length() == 0;
      } else if (var0.getClass().isArray()) {
         return Array.getLength(var0) == 0;
      } else if (var0 instanceof Collection) {
         return ((Collection)var0).isEmpty();
      } else {
         return var0 instanceof Map ? ((Map)var0).isEmpty() : false;
      }
   }

   public static boolean isNotEmpty(Object var0) {
      return isEmpty(var0) ^ true;
   }

   @SafeVarargs
   public static Comparable max(Comparable... var0) {
      Comparable var4 = null;
      Comparable var3 = null;
      if (var0 != null) {
         int var2 = var0.length;
         int var1 = 0;

         while(true) {
            var4 = var3;
            if (var1 >= var2) {
               break;
            }

            Comparable var5 = var0[var1];
            var4 = var3;
            if (compare(var5, var3, false) > 0) {
               var4 = var5;
            }

            ++var1;
            var3 = var4;
         }
      }

      return var4;
   }

   @SafeVarargs
   public static Comparable median(Comparable... var0) {
      Validate.notEmpty((Object[])var0);
      Validate.noNullElements((Object[])var0);
      TreeSet var1 = new TreeSet();
      Collections.addAll(var1, var0);
      return (Comparable)var1.toArray()[(var1.size() - 1) / 2];
   }

   @SafeVarargs
   public static Object median(Comparator var0, Object... var1) {
      Validate.notEmpty(var1, "null/empty items");
      Validate.noNullElements(var1);
      Validate.notNull(var0, "null comparator");
      TreeSet var2 = new TreeSet(var0);
      Collections.addAll(var2, var1);
      return var2.toArray()[(var2.size() - 1) / 2];
   }

   @SafeVarargs
   public static Comparable min(Comparable... var0) {
      Comparable var4 = null;
      Comparable var3 = null;
      if (var0 != null) {
         int var2 = var0.length;
         int var1 = 0;

         while(true) {
            var4 = var3;
            if (var1 >= var2) {
               break;
            }

            Comparable var5 = var0[var1];
            var4 = var3;
            if (compare(var5, var3, true) < 0) {
               var4 = var5;
            }

            ++var1;
            var3 = var4;
         }
      }

      return var4;
   }

   @SafeVarargs
   public static Object mode(Object... var0) {
      if (ArrayUtils.isNotEmpty(var0)) {
         HashMap var4 = new HashMap(var0.length);
         int var2 = var0.length;

         int var1;
         for(var1 = 0; var1 < var2; ++var1) {
            Object var5 = var0[var1];
            MutableInt var6 = (MutableInt)var4.get(var5);
            if (var6 == null) {
               var4.put(var5, new MutableInt(1));
            } else {
               var6.increment();
            }
         }

         Object var7 = null;
         var1 = 0;

         for(Iterator var8 = var4.entrySet().iterator(); var8.hasNext(); var1 = var2) {
            Entry var9 = (Entry)var8.next();
            int var3 = ((MutableInt)var9.getValue()).intValue();
            if (var3 == var1) {
               var7 = null;
               var2 = var1;
            } else {
               var2 = var1;
               if (var3 > var1) {
                  var2 = var3;
                  var7 = var9.getKey();
               }
            }
         }

         return var7;
      } else {
         return null;
      }
   }

   public static boolean notEqual(Object var0, Object var1) {
      return equals(var0, var1) ^ true;
   }

   @Deprecated
   public static String toString(Object var0) {
      return var0 == null ? "" : var0.toString();
   }

   @Deprecated
   public static String toString(Object var0, String var1) {
      return var0 == null ? var1 : var0.toString();
   }

   public static class Null implements Serializable {
      private static final long serialVersionUID = 7092611880189329093L;

      Null() {
      }

      private Object readResolve() {
         return ObjectUtils.NULL;
      }
   }
}
