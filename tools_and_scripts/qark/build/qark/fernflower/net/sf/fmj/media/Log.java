package net.sf.fmj.media;

import com.sun.media.util.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {
   private static int indent;
   public static final boolean isEnabled;
   private static Logger logger;

   static {
      boolean var0 = false;
      indent = 0;
      logger = Logger.getLogger(Log.class.getName());
      Object var1 = Registry.get("allowLogging");
      if (var1 != null && var1 instanceof Boolean) {
         var0 = (Boolean)var1;
      }

      isEnabled = var0;
      if (var0) {
         writeHeader();
      }

   }

   public static void comment(Object var0) {
      synchronized(Log.class){}

      label151: {
         Throwable var10000;
         label155: {
            Logger var1;
            boolean var10001;
            try {
               if (!isEnabled || !logger.isLoggable(Level.FINE)) {
                  break label151;
               }

               var1 = logger;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label155;
            }

            String var14;
            if (var0 != null) {
               try {
                  var14 = var0.toString();
               } catch (Throwable var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label155;
               }
            } else {
               var14 = "null";
            }

            label142:
            try {
               var1.fine(var14);
               break label151;
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
               break label142;
            }
         }

         Throwable var15 = var10000;
         throw var15;
      }

   }

   public static void decrIndent() {
      synchronized(Log.class){}

      try {
         --indent;
      } finally {
         ;
      }

   }

   public static void dumpStack(Throwable var0) {
      synchronized(Log.class){}

      label114: {
         Throwable var10000;
         label118: {
            boolean var10001;
            int var2;
            StackTraceElement[] var10;
            try {
               if (!isEnabled || !logger.isLoggable(Level.FINE)) {
                  break label114;
               }

               var10 = var0.getStackTrace();
               var2 = var10.length;
            } catch (Throwable var9) {
               var10000 = var9;
               var10001 = false;
               break label118;
            }

            int var1 = 0;

            while(true) {
               if (var1 >= var2) {
                  break label114;
               }

               StackTraceElement var3 = var10[var1];

               try {
                  logger.fine(var3.toString());
               } catch (Throwable var8) {
                  var10000 = var8;
                  var10001 = false;
                  break;
               }

               ++var1;
            }
         }

         var0 = var10000;
         throw var0;
      }

   }

   public static void error(Object var0) {
      synchronized(Log.class){}

      label217: {
         Throwable var10000;
         label221: {
            Logger var1;
            boolean var10001;
            label215: {
               try {
                  if (isEnabled && logger.isLoggable(Level.SEVERE)) {
                     var1 = logger;
                     break label215;
                  }
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label221;
               }

               try {
                  System.err.println(var0);
                  break label217;
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label221;
               }
            }

            String var22;
            if (var0 != null) {
               try {
                  var22 = var0.toString();
               } catch (Throwable var19) {
                  var10000 = var19;
                  var10001 = false;
                  break label221;
               }
            } else {
               var22 = "null";
            }

            label200:
            try {
               var1.severe(var22);
               break label217;
            } catch (Throwable var18) {
               var10000 = var18;
               var10001 = false;
               break label200;
            }
         }

         Throwable var23 = var10000;
         throw var23;
      }

   }

   public static int getIndent() {
      return indent;
   }

   public static void incrIndent() {
      synchronized(Log.class){}

      try {
         ++indent;
      } finally {
         ;
      }

   }

   public static void info(Object var0) {
      synchronized(Log.class){}

      label151: {
         Throwable var10000;
         label155: {
            Logger var1;
            boolean var10001;
            try {
               if (!isEnabled || !logger.isLoggable(Level.INFO)) {
                  break label151;
               }

               var1 = logger;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label155;
            }

            String var14;
            if (var0 != null) {
               try {
                  var14 = var0.toString();
               } catch (Throwable var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label155;
               }
            } else {
               var14 = "null";
            }

            label142:
            try {
               var1.info(var14);
               break label151;
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
               break label142;
            }
         }

         Throwable var15 = var10000;
         throw var15;
      }

   }

   public static void profile(Object var0) {
      synchronized(Log.class){}

      label151: {
         Throwable var10000;
         label155: {
            Logger var1;
            boolean var10001;
            try {
               if (!isEnabled || !logger.isLoggable(Level.FINER)) {
                  break label151;
               }

               var1 = logger;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label155;
            }

            String var14;
            if (var0 != null) {
               try {
                  var14 = var0.toString();
               } catch (Throwable var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label155;
               }
            } else {
               var14 = "null";
            }

            label142:
            try {
               var1.finer(var14);
               break label151;
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
               break label142;
            }
         }

         Throwable var15 = var10000;
         throw var15;
      }

   }

   public static void setIndent(int var0) {
      synchronized(Log.class){}

      try {
         indent = var0;
      } finally {
         ;
      }

   }

   public static void warning(Object var0) {
      synchronized(Log.class){}

      label151: {
         Throwable var10000;
         label155: {
            Logger var1;
            boolean var10001;
            try {
               if (!isEnabled || !logger.isLoggable(Level.WARNING)) {
                  break label151;
               }

               var1 = logger;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label155;
            }

            String var14;
            if (var0 != null) {
               try {
                  var14 = var0.toString();
               } catch (Throwable var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label155;
               }
            } else {
               var14 = "null";
            }

            label142:
            try {
               var1.warning(var14);
               break label151;
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
               break label142;
            }
         }

         Throwable var15 = var10000;
         throw var15;
      }

   }

   public static void write(Object var0) {
      synchronized(Log.class){}

      label240: {
         Throwable var10000;
         label239: {
            int var1;
            boolean var10001;
            StringBuilder var2;
            try {
               if (!isEnabled || !logger.isLoggable(Level.FINE)) {
                  break label240;
               }

               var2 = new StringBuilder();
               var1 = indent;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label239;
            }

            while(true) {
               if (var1 <= 0) {
                  String var23;
                  if (var0 != null) {
                     try {
                        var23 = var0.toString();
                     } catch (Throwable var20) {
                        var10000 = var20;
                        var10001 = false;
                        break;
                     }
                  } else {
                     var23 = "null";
                  }

                  try {
                     var2.append(var23);
                     logger.fine(var2.toString());
                     break label240;
                  } catch (Throwable var19) {
                     var10000 = var19;
                     var10001 = false;
                     break;
                  }
               }

               try {
                  var2.append("    ");
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break;
               }

               --var1;
            }
         }

         Throwable var24 = var10000;
         throw var24;
      }

   }

   private static void writeHeader() {
      // $FF: Couldn't be decompiled
   }
}
