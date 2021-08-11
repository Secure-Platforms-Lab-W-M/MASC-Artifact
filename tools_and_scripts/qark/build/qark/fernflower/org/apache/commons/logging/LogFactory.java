package org.apache.commons.logging;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.security.AccessController;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

public abstract class LogFactory {
   public static final String DIAGNOSTICS_DEST_PROPERTY = "org.apache.commons.logging.diagnostics.dest";
   public static final String FACTORY_DEFAULT = "org.apache.commons.logging.impl.LogFactoryImpl";
   public static final String FACTORY_PROPERTIES = "commons-logging.properties";
   public static final String FACTORY_PROPERTY = "org.apache.commons.logging.LogFactory";
   public static final String HASHTABLE_IMPLEMENTATION_PROPERTY = "org.apache.commons.logging.LogFactory.HashtableImpl";
   public static final String PRIORITY_KEY = "priority";
   protected static final String SERVICE_ID = "META-INF/services/org.apache.commons.logging.LogFactory";
   public static final String TCCL_KEY = "use_tccl";
   private static final String WEAK_HASHTABLE_CLASSNAME = "org.apache.commons.logging.impl.WeakHashtable";
   // $FF: synthetic field
   static Class class$org$apache$commons$logging$LogFactory;
   private static final String diagnosticPrefix;
   private static PrintStream diagnosticsStream = null;
   protected static Hashtable factories = null;
   protected static volatile LogFactory nullClassLoaderFactory = null;
   private static final ClassLoader thisClassLoader;

   static {
      Class var1 = class$org$apache$commons$logging$LogFactory;
      Class var0 = var1;
      if (var1 == null) {
         var0 = class$("org.apache.commons.logging.LogFactory");
         class$org$apache$commons$logging$LogFactory = var0;
      }

      ClassLoader var3 = getClassLoader(var0);
      thisClassLoader = var3;
      String var4;
      if (var3 == null) {
         var4 = "BOOTLOADER";
      } else {
         try {
            var4 = objectId(var3);
         } catch (SecurityException var2) {
            var4 = "UNKNOWN";
         }
      }

      StringBuffer var5 = new StringBuffer();
      var5.append("[LogFactory from ");
      var5.append(var4);
      var5.append("] ");
      diagnosticPrefix = var5.toString();
      diagnosticsStream = initDiagnostics();
      var1 = class$org$apache$commons$logging$LogFactory;
      var0 = var1;
      if (var1 == null) {
         var0 = class$("org.apache.commons.logging.LogFactory");
         class$org$apache$commons$logging$LogFactory = var0;
      }

      logClassLoaderEnvironment(var0);
      factories = createFactoryStore();
      if (isDiagnosticsEnabled()) {
         logDiagnostic("BOOTSTRAP COMPLETED");
      }

   }

   protected LogFactory() {
   }

   // $FF: synthetic method
   static void access$000(String var0) {
      logDiagnostic(var0);
   }

   private static void cacheFactory(ClassLoader var0, LogFactory var1) {
      if (var1 != null) {
         if (var0 == null) {
            nullClassLoaderFactory = var1;
            return;
         }

         factories.put(var0, var1);
      }

   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         Class var2 = Class.forName(var0);
         return var2;
      } catch (ClassNotFoundException var1) {
         throw new NoClassDefFoundError(var1.getMessage());
      }
   }

   protected static Object createFactory(String param0, ClassLoader param1) {
      // $FF: Couldn't be decompiled
   }

   private static final Hashtable createFactoryStore() {
      Object var2 = null;

      String var0;
      try {
         var0 = getSystemProperty("org.apache.commons.logging.LogFactory.HashtableImpl", (String)null);
      } catch (SecurityException var5) {
         var0 = null;
      }

      String var1 = var0;
      if (var0 == null) {
         var1 = "org.apache.commons.logging.impl.WeakHashtable";
      }

      Hashtable var7;
      label62:
      try {
         var7 = (Hashtable)Class.forName(var1).newInstance();
      } catch (Throwable var6) {
         handleThrowable(var6);
         var7 = (Hashtable)var2;
         if (!"org.apache.commons.logging.impl.WeakHashtable".equals(var1)) {
            if (isDiagnosticsEnabled()) {
               logDiagnostic("[ERROR] LogFactory: Load of custom hashtable failed");
               var7 = (Hashtable)var2;
            } else {
               System.err.println("[ERROR] LogFactory: Load of custom hashtable failed");
               var7 = (Hashtable)var2;
            }
         }
         break label62;
      }

      Hashtable var8 = var7;
      if (var7 == null) {
         var8 = new Hashtable();
      }

      return var8;
   }

   protected static ClassLoader directGetContextClassLoader() throws LogConfigurationException {
      try {
         ClassLoader var0 = Thread.currentThread().getContextClassLoader();
         return var0;
      } catch (SecurityException var1) {
         return null;
      }
   }

   private static LogFactory getCachedFactory(ClassLoader var0) {
      return var0 == null ? nullClassLoaderFactory : (LogFactory)factories.get(var0);
   }

   protected static ClassLoader getClassLoader(Class var0) {
      try {
         ClassLoader var1 = var0.getClassLoader();
         return var1;
      } catch (SecurityException var3) {
         if (isDiagnosticsEnabled()) {
            StringBuffer var2 = new StringBuffer();
            var2.append("Unable to get classloader for class '");
            var2.append(var0);
            var2.append("' due to security restrictions - ");
            var2.append(var3.getMessage());
            logDiagnostic(var2.toString());
         }

         throw var3;
      }
   }

   private static final Properties getConfigurationFile(ClassLoader var0, String var1) {
      Properties var8 = null;
      Properties var11 = null;
      double var6 = 0.0D;
      URL var9 = null;
      URL var10 = null;

      Properties var58;
      label343: {
         label347: {
            Enumeration var15;
            boolean var10001;
            try {
               var15 = getResources(var0, var1);
            } catch (SecurityException var57) {
               var10001 = false;
               break label347;
            }

            var58 = var11;
            if (var15 == null) {
               return null;
            }

            while(true) {
               var8 = var58;
               var9 = var10;

               try {
                  if (!var15.hasMoreElements()) {
                     break label343;
                  }
               } catch (SecurityException var53) {
                  var10001 = false;
                  break;
               }

               var8 = var58;
               var9 = var10;

               URL var13;
               try {
                  var13 = (URL)var15.nextElement();
               } catch (SecurityException var52) {
                  var10001 = false;
                  break;
               }

               var8 = var58;
               var9 = var10;

               Properties var14;
               try {
                  var14 = getProperties(var13);
               } catch (SecurityException var51) {
                  var10001 = false;
                  break;
               }

               var11 = var58;
               double var4 = var6;
               URL var12 = var10;
               if (var14 != null) {
                  if (var58 == null) {
                     var11 = var14;
                     var8 = var14;
                     var9 = var13;

                     String var59;
                     try {
                        var59 = var11.getProperty("priority");
                     } catch (SecurityException var36) {
                        var10001 = false;
                        break;
                     }

                     var4 = 0.0D;
                     if (var59 != null) {
                        var8 = var14;
                        var9 = var13;

                        try {
                           var4 = Double.parseDouble(var59);
                        } catch (SecurityException var35) {
                           var10001 = false;
                           break;
                        }
                     }

                     var8 = var14;
                     var9 = var13;

                     label350: {
                        try {
                           if (!isDiagnosticsEnabled()) {
                              break label350;
                           }
                        } catch (SecurityException var55) {
                           var10001 = false;
                           break;
                        }

                        var8 = var14;
                        var9 = var13;

                        StringBuffer var60;
                        try {
                           var60 = new StringBuffer();
                        } catch (SecurityException var34) {
                           var10001 = false;
                           break;
                        }

                        var8 = var14;
                        var9 = var13;

                        try {
                           var60.append("[LOOKUP] Properties file found at '");
                        } catch (SecurityException var33) {
                           var10001 = false;
                           break;
                        }

                        var8 = var14;
                        var9 = var13;

                        try {
                           var60.append(var13);
                        } catch (SecurityException var32) {
                           var10001 = false;
                           break;
                        }

                        var8 = var14;
                        var9 = var13;

                        try {
                           var60.append("'");
                        } catch (SecurityException var31) {
                           var10001 = false;
                           break;
                        }

                        var8 = var14;
                        var9 = var13;

                        try {
                           var60.append(" with priority ");
                        } catch (SecurityException var30) {
                           var10001 = false;
                           break;
                        }

                        var8 = var14;
                        var9 = var13;

                        try {
                           var60.append(var4);
                        } catch (SecurityException var29) {
                           var10001 = false;
                           break;
                        }

                        var8 = var14;
                        var9 = var13;

                        try {
                           logDiagnostic(var60.toString());
                        } catch (SecurityException var28) {
                           var10001 = false;
                           break;
                        }
                     }

                     var12 = var13;
                  } else {
                     var8 = var58;
                     var9 = var10;

                     String var61;
                     try {
                        var61 = var14.getProperty("priority");
                     } catch (SecurityException var50) {
                        var10001 = false;
                        break;
                     }

                     double var2 = 0.0D;
                     if (var61 != null) {
                        var8 = var58;
                        var9 = var10;

                        try {
                           var2 = Double.parseDouble(var61);
                        } catch (SecurityException var49) {
                           var10001 = false;
                           break;
                        }
                     }

                     StringBuffer var62;
                     if (var2 > var6) {
                        var8 = var58;
                        var9 = var10;

                        label353: {
                           try {
                              if (!isDiagnosticsEnabled()) {
                                 break label353;
                              }
                           } catch (SecurityException var54) {
                              var10001 = false;
                              break;
                           }

                           var8 = var58;
                           var9 = var10;

                           try {
                              var62 = new StringBuffer();
                           } catch (SecurityException var27) {
                              var10001 = false;
                              break;
                           }

                           var8 = var58;
                           var9 = var10;

                           try {
                              var62.append("[LOOKUP] Properties file at '");
                           } catch (SecurityException var26) {
                              var10001 = false;
                              break;
                           }

                           var8 = var58;
                           var9 = var10;

                           try {
                              var62.append(var13);
                           } catch (SecurityException var25) {
                              var10001 = false;
                              break;
                           }

                           var8 = var58;
                           var9 = var10;

                           try {
                              var62.append("'");
                           } catch (SecurityException var24) {
                              var10001 = false;
                              break;
                           }

                           var8 = var58;
                           var9 = var10;

                           try {
                              var62.append(" with priority ");
                           } catch (SecurityException var23) {
                              var10001 = false;
                              break;
                           }

                           var8 = var58;
                           var9 = var10;

                           try {
                              var62.append(var2);
                           } catch (SecurityException var22) {
                              var10001 = false;
                              break;
                           }

                           var8 = var58;
                           var9 = var10;

                           try {
                              var62.append(" overrides file at '");
                           } catch (SecurityException var21) {
                              var10001 = false;
                              break;
                           }

                           var8 = var58;
                           var9 = var10;

                           try {
                              var62.append(var10);
                           } catch (SecurityException var20) {
                              var10001 = false;
                              break;
                           }

                           var8 = var58;
                           var9 = var10;

                           try {
                              var62.append("'");
                           } catch (SecurityException var19) {
                              var10001 = false;
                              break;
                           }

                           var8 = var58;
                           var9 = var10;

                           try {
                              var62.append(" with priority ");
                           } catch (SecurityException var18) {
                              var10001 = false;
                              break;
                           }

                           var8 = var58;
                           var9 = var10;

                           try {
                              var62.append(var6);
                           } catch (SecurityException var17) {
                              var10001 = false;
                              break;
                           }

                           var8 = var58;
                           var9 = var10;

                           try {
                              logDiagnostic(var62.toString());
                           } catch (SecurityException var16) {
                              var10001 = false;
                              break;
                           }
                        }

                        var12 = var13;
                        var11 = var14;
                        var4 = var2;
                     } else {
                        label354: {
                           var11 = var58;
                           var4 = var6;
                           var12 = var10;
                           var8 = var58;
                           var9 = var10;

                           try {
                              if (!isDiagnosticsEnabled()) {
                                 break label354;
                              }
                           } catch (SecurityException var56) {
                              var10001 = false;
                              break;
                           }

                           var8 = var58;
                           var9 = var10;

                           try {
                              var62 = new StringBuffer();
                           } catch (SecurityException var48) {
                              var10001 = false;
                              break;
                           }

                           var8 = var58;
                           var9 = var10;

                           try {
                              var62.append("[LOOKUP] Properties file at '");
                           } catch (SecurityException var47) {
                              var10001 = false;
                              break;
                           }

                           var8 = var58;
                           var9 = var10;

                           try {
                              var62.append(var13);
                           } catch (SecurityException var46) {
                              var10001 = false;
                              break;
                           }

                           var8 = var58;
                           var9 = var10;

                           try {
                              var62.append("'");
                           } catch (SecurityException var45) {
                              var10001 = false;
                              break;
                           }

                           var8 = var58;
                           var9 = var10;

                           try {
                              var62.append(" with priority ");
                           } catch (SecurityException var44) {
                              var10001 = false;
                              break;
                           }

                           var8 = var58;
                           var9 = var10;

                           try {
                              var62.append(var2);
                           } catch (SecurityException var43) {
                              var10001 = false;
                              break;
                           }

                           var8 = var58;
                           var9 = var10;

                           try {
                              var62.append(" does not override file at '");
                           } catch (SecurityException var42) {
                              var10001 = false;
                              break;
                           }

                           var8 = var58;
                           var9 = var10;

                           try {
                              var62.append(var10);
                           } catch (SecurityException var41) {
                              var10001 = false;
                              break;
                           }

                           var8 = var58;
                           var9 = var10;

                           try {
                              var62.append("'");
                           } catch (SecurityException var40) {
                              var10001 = false;
                              break;
                           }

                           var8 = var58;
                           var9 = var10;

                           try {
                              var62.append(" with priority ");
                           } catch (SecurityException var39) {
                              var10001 = false;
                              break;
                           }

                           var8 = var58;
                           var9 = var10;

                           try {
                              var62.append(var6);
                           } catch (SecurityException var38) {
                              var10001 = false;
                              break;
                           }

                           var8 = var58;
                           var9 = var10;

                           try {
                              logDiagnostic(var62.toString());
                           } catch (SecurityException var37) {
                              var10001 = false;
                              break;
                           }

                           var12 = var10;
                           var4 = var6;
                           var11 = var58;
                        }
                     }
                  }
               }

               var58 = var11;
               var6 = var4;
               var10 = var12;
            }
         }

         var58 = var8;
         var10 = var9;
         if (isDiagnosticsEnabled()) {
            logDiagnostic("SecurityException thrown while trying to find/read config files.");
            var10 = var9;
            var58 = var8;
         }
      }

      if (isDiagnosticsEnabled()) {
         StringBuffer var63;
         if (var58 == null) {
            var63 = new StringBuffer();
            var63.append("[LOOKUP] No properties file of name '");
            var63.append(var1);
            var63.append("' found.");
            logDiagnostic(var63.toString());
            return var58;
         }

         var63 = new StringBuffer();
         var63.append("[LOOKUP] Properties file of name '");
         var63.append(var1);
         var63.append("' found at '");
         var63.append(var10);
         var63.append('"');
         logDiagnostic(var63.toString());
      }

      return var58;
   }

   protected static ClassLoader getContextClassLoader() throws LogConfigurationException {
      return directGetContextClassLoader();
   }

   private static ClassLoader getContextClassLoaderInternal() throws LogConfigurationException {
      return (ClassLoader)AccessController.doPrivileged(new LogFactory$1());
   }

   public static LogFactory getFactory() throws LogConfigurationException {
      // $FF: Couldn't be decompiled
   }

   public static Log getLog(Class var0) throws LogConfigurationException {
      return getFactory().getInstance(var0);
   }

   public static Log getLog(String var0) throws LogConfigurationException {
      return getFactory().getInstance(var0);
   }

   private static Properties getProperties(URL var0) {
      return (Properties)AccessController.doPrivileged(new LogFactory$5(var0));
   }

   private static InputStream getResourceAsStream(ClassLoader var0, String var1) {
      return (InputStream)AccessController.doPrivileged(new LogFactory$3(var0, var1));
   }

   private static Enumeration getResources(ClassLoader var0, String var1) {
      return (Enumeration)AccessController.doPrivileged(new LogFactory$4(var0, var1));
   }

   private static String getSystemProperty(String var0, String var1) throws SecurityException {
      return (String)AccessController.doPrivileged(new LogFactory$6(var0, var1));
   }

   protected static void handleThrowable(Throwable var0) {
      if (!(var0 instanceof ThreadDeath)) {
         if (var0 instanceof VirtualMachineError) {
            throw (VirtualMachineError)var0;
         }
      } else {
         throw (ThreadDeath)var0;
      }
   }

   private static boolean implementsLogFactory(Class var0) {
      boolean var5 = false;
      boolean var6 = false;
      boolean var1 = false;
      boolean var2 = false;
      if (var0 != null) {
         boolean var3 = var2;
         boolean var4 = var5;
         var1 = var6;

         StringBuffer var52;
         SecurityException var53;
         label184: {
            LinkageError var10000;
            label183: {
               label182: {
                  ClassLoader var7;
                  boolean var10001;
                  try {
                     var7 = var0.getClassLoader();
                  } catch (SecurityException var47) {
                     var53 = var47;
                     var10001 = false;
                     break label184;
                  } catch (LinkageError var48) {
                     var10000 = var48;
                     var10001 = false;
                     break label183;
                  } catch (ClassNotFoundException var49) {
                     var10001 = false;
                     break label182;
                  }

                  if (var7 == null) {
                     var3 = var2;
                     var4 = var5;
                     var1 = var6;

                     try {
                        logDiagnostic("[CUSTOM LOG FACTORY] was loaded by the boot classloader");
                        return false;
                     } catch (SecurityException var8) {
                        var53 = var8;
                        var10001 = false;
                        break label184;
                     } catch (LinkageError var9) {
                        var10000 = var9;
                        var10001 = false;
                        break label183;
                     } catch (ClassNotFoundException var10) {
                        var10001 = false;
                     }
                  } else {
                     label190: {
                        var3 = var2;
                        var4 = var5;
                        var1 = var6;

                        try {
                           logHierarchy("[CUSTOM LOG FACTORY] ", var7);
                        } catch (SecurityException var44) {
                           var53 = var44;
                           var10001 = false;
                           break label184;
                        } catch (LinkageError var45) {
                           var10000 = var45;
                           var10001 = false;
                           break label183;
                        } catch (ClassNotFoundException var46) {
                           var10001 = false;
                           break label190;
                        }

                        var3 = var2;
                        var4 = var5;
                        var1 = var6;

                        try {
                           var2 = Class.forName("org.apache.commons.logging.LogFactory", false, var7).isAssignableFrom(var0);
                        } catch (SecurityException var41) {
                           var53 = var41;
                           var10001 = false;
                           break label184;
                        } catch (LinkageError var42) {
                           var10000 = var42;
                           var10001 = false;
                           break label183;
                        } catch (ClassNotFoundException var43) {
                           var10001 = false;
                           break label190;
                        }

                        if (var2) {
                           label191: {
                              var3 = var2;
                              var4 = var2;
                              var1 = var2;

                              try {
                                 var52 = new StringBuffer();
                              } catch (SecurityException var23) {
                                 var53 = var23;
                                 var10001 = false;
                                 break label184;
                              } catch (LinkageError var24) {
                                 var10000 = var24;
                                 var10001 = false;
                                 break label183;
                              } catch (ClassNotFoundException var25) {
                                 var10001 = false;
                                 break label191;
                              }

                              var3 = var2;
                              var4 = var2;
                              var1 = var2;

                              try {
                                 var52.append("[CUSTOM LOG FACTORY] ");
                              } catch (SecurityException var20) {
                                 var53 = var20;
                                 var10001 = false;
                                 break label184;
                              } catch (LinkageError var21) {
                                 var10000 = var21;
                                 var10001 = false;
                                 break label183;
                              } catch (ClassNotFoundException var22) {
                                 var10001 = false;
                                 break label191;
                              }

                              var3 = var2;
                              var4 = var2;
                              var1 = var2;

                              try {
                                 var52.append(var0.getName());
                              } catch (SecurityException var17) {
                                 var53 = var17;
                                 var10001 = false;
                                 break label184;
                              } catch (LinkageError var18) {
                                 var10000 = var18;
                                 var10001 = false;
                                 break label183;
                              } catch (ClassNotFoundException var19) {
                                 var10001 = false;
                                 break label191;
                              }

                              var3 = var2;
                              var4 = var2;
                              var1 = var2;

                              try {
                                 var52.append(" implements LogFactory but was loaded by an incompatible classloader.");
                              } catch (SecurityException var14) {
                                 var53 = var14;
                                 var10001 = false;
                                 break label184;
                              } catch (LinkageError var15) {
                                 var10000 = var15;
                                 var10001 = false;
                                 break label183;
                              } catch (ClassNotFoundException var16) {
                                 var10001 = false;
                                 break label191;
                              }

                              var3 = var2;
                              var4 = var2;
                              var1 = var2;

                              try {
                                 logDiagnostic(var52.toString());
                                 return var2;
                              } catch (SecurityException var11) {
                                 var53 = var11;
                                 var10001 = false;
                                 break label184;
                              } catch (LinkageError var12) {
                                 var10000 = var12;
                                 var10001 = false;
                                 break label183;
                              } catch (ClassNotFoundException var13) {
                                 var10001 = false;
                              }
                           }
                        } else {
                           label192: {
                              var3 = var2;
                              var4 = var2;
                              var1 = var2;

                              try {
                                 var52 = new StringBuffer();
                              } catch (SecurityException var38) {
                                 var53 = var38;
                                 var10001 = false;
                                 break label184;
                              } catch (LinkageError var39) {
                                 var10000 = var39;
                                 var10001 = false;
                                 break label183;
                              } catch (ClassNotFoundException var40) {
                                 var10001 = false;
                                 break label192;
                              }

                              var3 = var2;
                              var4 = var2;
                              var1 = var2;

                              try {
                                 var52.append("[CUSTOM LOG FACTORY] ");
                              } catch (SecurityException var35) {
                                 var53 = var35;
                                 var10001 = false;
                                 break label184;
                              } catch (LinkageError var36) {
                                 var10000 = var36;
                                 var10001 = false;
                                 break label183;
                              } catch (ClassNotFoundException var37) {
                                 var10001 = false;
                                 break label192;
                              }

                              var3 = var2;
                              var4 = var2;
                              var1 = var2;

                              try {
                                 var52.append(var0.getName());
                              } catch (SecurityException var32) {
                                 var53 = var32;
                                 var10001 = false;
                                 break label184;
                              } catch (LinkageError var33) {
                                 var10000 = var33;
                                 var10001 = false;
                                 break label183;
                              } catch (ClassNotFoundException var34) {
                                 var10001 = false;
                                 break label192;
                              }

                              var3 = var2;
                              var4 = var2;
                              var1 = var2;

                              try {
                                 var52.append(" does not implement LogFactory.");
                              } catch (SecurityException var29) {
                                 var53 = var29;
                                 var10001 = false;
                                 break label184;
                              } catch (LinkageError var30) {
                                 var10000 = var30;
                                 var10001 = false;
                                 break label183;
                              } catch (ClassNotFoundException var31) {
                                 var10001 = false;
                                 break label192;
                              }

                              var3 = var2;
                              var4 = var2;
                              var1 = var2;

                              try {
                                 logDiagnostic(var52.toString());
                                 return var2;
                              } catch (SecurityException var26) {
                                 var53 = var26;
                                 var10001 = false;
                                 break label184;
                              } catch (LinkageError var27) {
                                 var10000 = var27;
                                 var10001 = false;
                                 break label183;
                              } catch (ClassNotFoundException var28) {
                                 var10001 = false;
                              }
                           }
                        }
                     }
                  }
               }

               logDiagnostic("[CUSTOM LOG FACTORY] LogFactory class cannot be loaded by classloader which loaded the custom LogFactory implementation. Is the custom factory in the right classloader?");
               return var3;
            }

            LinkageError var50 = var10000;
            var52 = new StringBuffer();
            var52.append("[CUSTOM LOG FACTORY] LinkageError thrown whilst trying to determine whether the compatibility was caused by a classloader conflict: ");
            var52.append(var50.getMessage());
            logDiagnostic(var52.toString());
            return var4;
         }

         SecurityException var51 = var53;
         var52 = new StringBuffer();
         var52.append("[CUSTOM LOG FACTORY] SecurityException thrown whilst trying to determine whether the compatibility was caused by a classloader conflict: ");
         var52.append(var51.getMessage());
         logDiagnostic(var52.toString());
      }

      return var1;
   }

   private static PrintStream initDiagnostics() {
      String var0;
      try {
         var0 = getSystemProperty("org.apache.commons.logging.diagnostics.dest", (String)null);
      } catch (SecurityException var2) {
         return null;
      }

      if (var0 == null) {
         return null;
      } else if (var0.equals("STDOUT")) {
         return System.out;
      } else if (var0.equals("STDERR")) {
         return System.err;
      } else {
         try {
            PrintStream var3 = new PrintStream(new FileOutputStream(var0, true));
            return var3;
         } catch (IOException var1) {
            return null;
         }
      }
   }

   protected static boolean isDiagnosticsEnabled() {
      return diagnosticsStream != null;
   }

   private static void logClassLoaderEnvironment(Class var0) {
      if (isDiagnosticsEnabled()) {
         try {
            StringBuffer var1 = new StringBuffer();
            var1.append("[ENV] Extension directories (java.ext.dir): ");
            var1.append(System.getProperty("java.ext.dir"));
            logDiagnostic(var1.toString());
            var1 = new StringBuffer();
            var1.append("[ENV] Application classpath (java.class.path): ");
            var1.append(System.getProperty("java.class.path"));
            logDiagnostic(var1.toString());
         } catch (SecurityException var4) {
            logDiagnostic("[ENV] Security setting prevent interrogation of system classpaths.");
         }

         String var7 = var0.getName();

         ClassLoader var6;
         try {
            var6 = getClassLoader(var0);
         } catch (SecurityException var3) {
            StringBuffer var5 = new StringBuffer();
            var5.append("[ENV] Security forbids determining the classloader for ");
            var5.append(var7);
            logDiagnostic(var5.toString());
            return;
         }

         StringBuffer var2 = new StringBuffer();
         var2.append("[ENV] Class ");
         var2.append(var7);
         var2.append(" was loaded via classloader ");
         var2.append(objectId(var6));
         logDiagnostic(var2.toString());
         var2 = new StringBuffer();
         var2.append("[ENV] Ancestry of classloader which loaded ");
         var2.append(var7);
         var2.append(" is ");
         logHierarchy(var2.toString(), var6);
      }
   }

   private static final void logDiagnostic(String var0) {
      PrintStream var1 = diagnosticsStream;
      if (var1 != null) {
         var1.print(diagnosticPrefix);
         diagnosticsStream.println(var0);
         diagnosticsStream.flush();
      }

   }

   private static void logHierarchy(String var0, ClassLoader var1) {
      if (isDiagnosticsEnabled()) {
         StringBuffer var3;
         if (var1 != null) {
            String var2 = var1.toString();
            var3 = new StringBuffer();
            var3.append(var0);
            var3.append(objectId(var1));
            var3.append(" == '");
            var3.append(var2);
            var3.append("'");
            logDiagnostic(var3.toString());
         }

         ClassLoader var8;
         try {
            var8 = ClassLoader.getSystemClassLoader();
         } catch (SecurityException var4) {
            StringBuffer var7 = new StringBuffer();
            var7.append(var0);
            var7.append("Security forbids determining the system classloader.");
            logDiagnostic(var7.toString());
            return;
         }

         if (var1 != null) {
            var3 = new StringBuffer();
            var3.append(var0);
            var3.append("ClassLoader tree:");
            var3 = new StringBuffer(var3.toString());

            while(true) {
               var3.append(objectId(var1));
               if (var1 == var8) {
                  var3.append(" (SYSTEM) ");
               }

               ClassLoader var6;
               try {
                  var6 = var1.getParent();
               } catch (SecurityException var5) {
                  var3.append(" --> SECRET");
                  break;
               }

               var3.append(" --> ");
               var1 = var6;
               if (var6 == null) {
                  var3.append("BOOT");
                  break;
               }
            }

            logDiagnostic(var3.toString());
         }

      }
   }

   protected static final void logRawDiagnostic(String var0) {
      PrintStream var1 = diagnosticsStream;
      if (var1 != null) {
         var1.println(var0);
         diagnosticsStream.flush();
      }

   }

   protected static LogFactory newFactory(String var0, ClassLoader var1) {
      return newFactory(var0, var1, (ClassLoader)null);
   }

   protected static LogFactory newFactory(String var0, ClassLoader var1, ClassLoader var2) throws LogConfigurationException {
      Object var3 = AccessController.doPrivileged(new LogFactory$2(var0, var1));
      StringBuffer var5;
      if (var3 instanceof LogConfigurationException) {
         LogConfigurationException var4 = (LogConfigurationException)var3;
         if (isDiagnosticsEnabled()) {
            var5 = new StringBuffer();
            var5.append("An error occurred while loading the factory class:");
            var5.append(var4.getMessage());
            logDiagnostic(var5.toString());
         }

         throw var4;
      } else {
         if (isDiagnosticsEnabled()) {
            var5 = new StringBuffer();
            var5.append("Created object ");
            var5.append(objectId(var3));
            var5.append(" to manage classloader ");
            var5.append(objectId(var2));
            logDiagnostic(var5.toString());
         }

         return (LogFactory)var3;
      }
   }

   public static String objectId(Object var0) {
      if (var0 == null) {
         return "null";
      } else {
         StringBuffer var1 = new StringBuffer();
         var1.append(var0.getClass().getName());
         var1.append("@");
         var1.append(System.identityHashCode(var0));
         return var1.toString();
      }
   }

   public static void release(ClassLoader var0) {
      if (isDiagnosticsEnabled()) {
         StringBuffer var1 = new StringBuffer();
         var1.append("Releasing factory for classloader ");
         var1.append(objectId(var0));
         logDiagnostic(var1.toString());
      }

      Throwable var10000;
      boolean var10001;
      label291: {
         Hashtable var34 = factories;
         synchronized(var34){}
         if (var0 == null) {
            try {
               if (nullClassLoaderFactory != null) {
                  nullClassLoaderFactory.release();
                  nullClassLoaderFactory = null;
               }
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label291;
            }
         } else {
            LogFactory var2;
            try {
               var2 = (LogFactory)var34.get(var0);
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               break label291;
            }

            if (var2 != null) {
               try {
                  var2.release();
                  var34.remove(var0);
               } catch (Throwable var30) {
                  var10000 = var30;
                  var10001 = false;
                  break label291;
               }
            }
         }

         label272:
         try {
            return;
         } catch (Throwable var29) {
            var10000 = var29;
            var10001 = false;
            break label272;
         }
      }

      while(true) {
         Throwable var33 = var10000;

         try {
            throw var33;
         } catch (Throwable var28) {
            var10000 = var28;
            var10001 = false;
            continue;
         }
      }
   }

   public static void releaseAll() {
      if (isDiagnosticsEnabled()) {
         logDiagnostic("Releasing factory for all classloaders.");
      }

      Hashtable var0 = factories;
      synchronized(var0){}

      Throwable var10000;
      boolean var10001;
      label323: {
         Enumeration var1;
         try {
            var1 = var0.elements();
         } catch (Throwable var30) {
            var10000 = var30;
            var10001 = false;
            break label323;
         }

         while(true) {
            try {
               if (!var1.hasMoreElements()) {
                  break;
               }

               ((LogFactory)var1.nextElement()).release();
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               break label323;
            }
         }

         try {
            var0.clear();
            if (nullClassLoaderFactory != null) {
               nullClassLoaderFactory.release();
               nullClassLoaderFactory = null;
            }
         } catch (Throwable var29) {
            var10000 = var29;
            var10001 = false;
            break label323;
         }

         label303:
         try {
            return;
         } catch (Throwable var28) {
            var10000 = var28;
            var10001 = false;
            break label303;
         }
      }

      while(true) {
         Throwable var32 = var10000;

         try {
            throw var32;
         } catch (Throwable var27) {
            var10000 = var27;
            var10001 = false;
            continue;
         }
      }
   }

   private static String trim(String var0) {
      return var0 == null ? null : var0.trim();
   }

   public abstract Object getAttribute(String var1);

   public abstract String[] getAttributeNames();

   public abstract Log getInstance(Class var1) throws LogConfigurationException;

   public abstract Log getInstance(String var1) throws LogConfigurationException;

   public abstract void release();

   public abstract void removeAttribute(String var1);

   public abstract void setAttribute(String var1, Object var2);
}
