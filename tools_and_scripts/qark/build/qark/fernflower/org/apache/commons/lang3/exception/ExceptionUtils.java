package org.apache.commons.lang3.exception;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

public class ExceptionUtils {
   private static final String[] CAUSE_METHOD_NAMES = new String[]{"getCause", "getNextException", "getTargetException", "getException", "getSourceException", "getRootCause", "getCausedByException", "getNested", "getLinkedException", "getNestedException", "getLinkedCause", "getThrowable"};
   static final String WRAPPED_MARKER = " [wrapped] ";

   @Deprecated
   public static Throwable getCause(Throwable var0) {
      return getCause(var0, (String[])null);
   }

   @Deprecated
   public static Throwable getCause(Throwable var0, String[] var1) {
      if (var0 == null) {
         return null;
      } else {
         String[] var4 = var1;
         Throwable var5;
         if (var1 == null) {
            var5 = var0.getCause();
            if (var5 != null) {
               return var5;
            }

            var4 = CAUSE_METHOD_NAMES;
         }

         int var3 = var4.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            String var6 = var4[var2];
            if (var6 != null) {
               var5 = getCauseUsingMethodName(var0, var6);
               if (var5 != null) {
                  return var5;
               }
            }
         }

         return null;
      }
   }

   private static Throwable getCauseUsingMethodName(Throwable var0, String var1) {
      Object var2 = null;

      Method var8;
      try {
         var8 = var0.getClass().getMethod(var1);
      } catch (NoSuchMethodException var6) {
         var8 = (Method)var2;
      } catch (SecurityException var7) {
         var8 = (Method)var2;
      }

      if (var8 != null && Throwable.class.isAssignableFrom(var8.getReturnType())) {
         try {
            var0 = (Throwable)var8.invoke(var0);
            return var0;
         } catch (IllegalAccessException var3) {
         } catch (IllegalArgumentException var4) {
         } catch (InvocationTargetException var5) {
         }
      }

      return null;
   }

   @Deprecated
   public static String[] getDefaultCauseMethodNames() {
      return (String[])ArrayUtils.clone((Object[])CAUSE_METHOD_NAMES);
   }

   public static String getMessage(Throwable var0) {
      if (var0 == null) {
         return "";
      } else {
         String var1 = ClassUtils.getShortClassName(var0, (String)null);
         String var3 = var0.getMessage();
         StringBuilder var2 = new StringBuilder();
         var2.append(var1);
         var2.append(": ");
         var2.append(StringUtils.defaultString(var3));
         return var2.toString();
      }
   }

   public static Throwable getRootCause(Throwable var0) {
      List var1 = getThrowableList(var0);
      return var1.isEmpty() ? null : (Throwable)var1.get(var1.size() - 1);
   }

   public static String getRootCauseMessage(Throwable var0) {
      Throwable var1 = getRootCause(var0);
      if (var1 != null) {
         var0 = var1;
      }

      return getMessage(var0);
   }

   public static String[] getRootCauseStackTrace(Throwable var0) {
      if (var0 == null) {
         return ArrayUtils.EMPTY_STRING_ARRAY;
      } else {
         Throwable[] var4 = getThrowables(var0);
         int var2 = var4.length;
         ArrayList var5 = new ArrayList();
         List var7 = getStackFrameList(var4[var2 - 1]);
         int var1 = var2;

         while(true) {
            List var3 = var7;
            --var1;
            if (var1 < 0) {
               return (String[])var5.toArray(new String[var5.size()]);
            }

            var7 = var7;
            if (var1 != 0) {
               var7 = getStackFrameList(var4[var1 - 1]);
               removeCommonFrames(var3, var7);
            }

            if (var1 == var2 - 1) {
               var5.add(var4[var1].toString());
            } else {
               StringBuilder var6 = new StringBuilder();
               var6.append(" [wrapped] ");
               var6.append(var4[var1].toString());
               var5.add(var6.toString());
            }

            var5.addAll(var3);
         }
      }
   }

   static List getStackFrameList(Throwable var0) {
      StringTokenizer var5 = new StringTokenizer(getStackTrace(var0), System.lineSeparator());
      ArrayList var3 = new ArrayList();

      boolean var6;
      for(boolean var1 = false; var5.hasMoreTokens(); var1 = var6) {
         String var4 = var5.nextToken();
         int var2 = var4.indexOf("at");
         if (var2 != -1 && var4.substring(0, var2).trim().isEmpty()) {
            var6 = true;
            var3.add(var4);
         } else {
            var6 = var1;
            if (var1) {
               return var3;
            }
         }
      }

      return var3;
   }

   static String[] getStackFrames(String var0) {
      StringTokenizer var2 = new StringTokenizer(var0, System.lineSeparator());
      ArrayList var1 = new ArrayList();

      while(var2.hasMoreTokens()) {
         var1.add(var2.nextToken());
      }

      return (String[])var1.toArray(new String[var1.size()]);
   }

   public static String[] getStackFrames(Throwable var0) {
      return var0 == null ? ArrayUtils.EMPTY_STRING_ARRAY : getStackFrames(getStackTrace(var0));
   }

   public static String getStackTrace(Throwable var0) {
      StringWriter var1 = new StringWriter();
      var0.printStackTrace(new PrintWriter(var1, true));
      return var1.getBuffer().toString();
   }

   public static int getThrowableCount(Throwable var0) {
      return getThrowableList(var0).size();
   }

   public static List getThrowableList(Throwable var0) {
      ArrayList var1;
      for(var1 = new ArrayList(); var0 != null && !var1.contains(var0); var0 = var0.getCause()) {
         var1.add(var0);
      }

      return var1;
   }

   public static Throwable[] getThrowables(Throwable var0) {
      List var1 = getThrowableList(var0);
      return (Throwable[])var1.toArray(new Throwable[var1.size()]);
   }

   public static boolean hasCause(Throwable var0, Class var1) {
      Throwable var2 = var0;
      if (var0 instanceof UndeclaredThrowableException) {
         var2 = var0.getCause();
      }

      return var1.isInstance(var2);
   }

   private static int indexOf(Throwable var0, Class var1, int var2, boolean var3) {
      if (var0 != null) {
         if (var1 == null) {
            return -1;
         } else {
            int var4 = var2;
            if (var2 < 0) {
               var4 = 0;
            }

            Throwable[] var5 = getThrowables(var0);
            if (var4 >= var5.length) {
               return -1;
            } else if (var3) {
               while(var4 < var5.length) {
                  if (var1.isAssignableFrom(var5[var4].getClass())) {
                     return var4;
                  }

                  ++var4;
               }

               return -1;
            } else {
               while(var4 < var5.length) {
                  if (var1.equals(var5[var4].getClass())) {
                     return var4;
                  }

                  ++var4;
               }

               return -1;
            }
         }
      } else {
         return -1;
      }
   }

   public static int indexOfThrowable(Throwable var0, Class var1) {
      return indexOf(var0, var1, 0, false);
   }

   public static int indexOfThrowable(Throwable var0, Class var1, int var2) {
      return indexOf(var0, var1, var2, false);
   }

   public static int indexOfType(Throwable var0, Class var1) {
      return indexOf(var0, var1, 0, true);
   }

   public static int indexOfType(Throwable var0, Class var1, int var2) {
      return indexOf(var0, var1, var2, true);
   }

   public static void printRootCauseStackTrace(Throwable var0) {
      printRootCauseStackTrace(var0, System.err);
   }

   public static void printRootCauseStackTrace(Throwable var0, PrintStream var1) {
      if (var0 != null) {
         int var2 = 0;
         boolean var4;
         if (var1 != null) {
            var4 = true;
         } else {
            var4 = false;
         }

         Validate.isTrue(var4, "The PrintStream must not be null");
         String[] var5 = getRootCauseStackTrace(var0);

         for(int var3 = var5.length; var2 < var3; ++var2) {
            var1.println(var5[var2]);
         }

         var1.flush();
      }
   }

   public static void printRootCauseStackTrace(Throwable var0, PrintWriter var1) {
      if (var0 != null) {
         int var2 = 0;
         boolean var4;
         if (var1 != null) {
            var4 = true;
         } else {
            var4 = false;
         }

         Validate.isTrue(var4, "The PrintWriter must not be null");
         String[] var5 = getRootCauseStackTrace(var0);

         for(int var3 = var5.length; var2 < var3; ++var2) {
            var1.println(var5[var2]);
         }

         var1.flush();
      }
   }

   public static void removeCommonFrames(List var0, List var1) {
      if (var0 != null && var1 != null) {
         int var3 = var0.size() - 1;

         for(int var2 = var1.size() - 1; var3 >= 0 && var2 >= 0; --var2) {
            if (((String)var0.get(var3)).equals((String)var1.get(var2))) {
               var0.remove(var3);
            }

            --var3;
         }

      } else {
         throw new IllegalArgumentException("The List must not be null");
      }
   }

   public static Object rethrow(Throwable var0) {
      return typeErasure(var0);
   }

   private static Object typeErasure(Throwable var0) throws Throwable {
      throw var0;
   }

   public static Object wrapAndThrow(Throwable var0) {
      if (!(var0 instanceof RuntimeException)) {
         if (var0 instanceof Error) {
            throw (Error)var0;
         } else {
            throw new UndeclaredThrowableException(var0);
         }
      } else {
         throw (RuntimeException)var0;
      }
   }
}
