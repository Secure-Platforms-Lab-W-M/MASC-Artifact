package org.apache.commons.logging.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.util.Hashtable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;

public class LogFactoryImpl extends LogFactory {
   public static final String ALLOW_FLAWED_CONTEXT_PROPERTY = "org.apache.commons.logging.Log.allowFlawedContext";
   public static final String ALLOW_FLAWED_DISCOVERY_PROPERTY = "org.apache.commons.logging.Log.allowFlawedDiscovery";
   public static final String ALLOW_FLAWED_HIERARCHY_PROPERTY = "org.apache.commons.logging.Log.allowFlawedHierarchy";
   private static final String LOGGING_IMPL_JDK14_LOGGER = "org.apache.commons.logging.impl.Jdk14Logger";
   private static final String LOGGING_IMPL_LOG4J_LOGGER = "org.apache.commons.logging.impl.Log4JLogger";
   private static final String LOGGING_IMPL_LUMBERJACK_LOGGER = "org.apache.commons.logging.impl.Jdk13LumberjackLogger";
   private static final String LOGGING_IMPL_SIMPLE_LOGGER = "org.apache.commons.logging.impl.SimpleLog";
   public static final String LOG_PROPERTY = "org.apache.commons.logging.Log";
   protected static final String LOG_PROPERTY_OLD = "org.apache.commons.logging.log";
   private static final String PKG_IMPL = "org.apache.commons.logging.impl.";
   private static final int PKG_LEN = "org.apache.commons.logging.impl.".length();
   // $FF: synthetic field
   static Class class$java$lang$String;
   // $FF: synthetic field
   static Class class$org$apache$commons$logging$Log;
   // $FF: synthetic field
   static Class class$org$apache$commons$logging$LogFactory;
   // $FF: synthetic field
   static Class class$org$apache$commons$logging$impl$LogFactoryImpl;
   private static final String[] classesToDiscover = new String[]{"org.apache.commons.logging.impl.Log4JLogger", "org.apache.commons.logging.impl.Jdk14Logger", "org.apache.commons.logging.impl.Jdk13LumberjackLogger", "org.apache.commons.logging.impl.SimpleLog"};
   private boolean allowFlawedContext;
   private boolean allowFlawedDiscovery;
   private boolean allowFlawedHierarchy;
   protected Hashtable attributes = new Hashtable();
   private String diagnosticPrefix;
   protected Hashtable instances = new Hashtable();
   private String logClassName;
   protected Constructor logConstructor = null;
   protected Class[] logConstructorSignature;
   protected Method logMethod;
   protected Class[] logMethodSignature;
   private boolean useTCCL = true;

   public LogFactoryImpl() {
      Class var2 = class$java$lang$String;
      Class var1 = var2;
      if (var2 == null) {
         var1 = class$("java.lang.String");
         class$java$lang$String = var1;
      }

      this.logConstructorSignature = new Class[]{var1};
      this.logMethod = null;
      var2 = class$org$apache$commons$logging$LogFactory;
      var1 = var2;
      if (var2 == null) {
         var1 = class$("org.apache.commons.logging.LogFactory");
         class$org$apache$commons$logging$LogFactory = var1;
      }

      this.logMethodSignature = new Class[]{var1};
      this.initDiagnostics();
      if (isDiagnosticsEnabled()) {
         this.logDiagnostic("Instance created.");
      }

   }

   // $FF: synthetic method
   static ClassLoader access$000() throws LogConfigurationException {
      return directGetContextClassLoader();
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

   private Log createLogFromClass(String param1, String param2, boolean param3) throws LogConfigurationException {
      // $FF: Couldn't be decompiled
   }

   private Log discoverLogImplementation(String var1) throws LogConfigurationException {
      if (isDiagnosticsEnabled()) {
         this.logDiagnostic("Discovering a Log implementation...");
      }

      this.initConfiguration();
      Log var3 = null;
      String var4 = this.findUserSpecifiedLogClassName();
      if (var4 != null) {
         if (isDiagnosticsEnabled()) {
            StringBuffer var7 = new StringBuffer();
            var7.append("Attempting to load user-specified log class '");
            var7.append(var4);
            var7.append("'...");
            this.logDiagnostic(var7.toString());
         }

         Log var5 = this.createLogFromClass(var4, var1, true);
         if (var5 != null) {
            return var5;
         } else {
            StringBuffer var6 = new StringBuffer("User-specified log class '");
            var6.append(var4);
            var6.append("' cannot be found or is not useable.");
            this.informUponSimilarName(var6, var4, "org.apache.commons.logging.impl.Log4JLogger");
            this.informUponSimilarName(var6, var4, "org.apache.commons.logging.impl.Jdk14Logger");
            this.informUponSimilarName(var6, var4, "org.apache.commons.logging.impl.Jdk13LumberjackLogger");
            this.informUponSimilarName(var6, var4, "org.apache.commons.logging.impl.SimpleLog");
            throw new LogConfigurationException(var6.toString());
         }
      } else {
         if (isDiagnosticsEnabled()) {
            this.logDiagnostic("No user-specified Log implementation; performing discovery using the standard supported logging implementations...");
         }

         int var2 = 0;

         while(true) {
            String[] var8 = classesToDiscover;
            if (var2 >= var8.length || var3 != null) {
               if (var3 != null) {
                  return var3;
               } else {
                  throw new LogConfigurationException("No suitable Log implementation");
               }
            }

            var3 = this.createLogFromClass(var8[var2], var1, true);
            ++var2;
         }
      }
   }

   private String findUserSpecifiedLogClassName() {
      if (isDiagnosticsEnabled()) {
         this.logDiagnostic("Trying to get log class from attribute 'org.apache.commons.logging.Log'");
      }

      String var1 = (String)this.getAttribute("org.apache.commons.logging.Log");
      String var2 = var1;
      if (var1 == null) {
         if (isDiagnosticsEnabled()) {
            this.logDiagnostic("Trying to get log class from attribute 'org.apache.commons.logging.log'");
         }

         var2 = (String)this.getAttribute("org.apache.commons.logging.log");
      }

      var1 = var2;
      if (var2 == null) {
         if (isDiagnosticsEnabled()) {
            this.logDiagnostic("Trying to get log class from system property 'org.apache.commons.logging.Log'");
         }

         try {
            var1 = getSystemProperty("org.apache.commons.logging.Log", (String)null);
         } catch (SecurityException var5) {
            var1 = var2;
            if (isDiagnosticsEnabled()) {
               StringBuffer var6 = new StringBuffer();
               var6.append("No access allowed to system property 'org.apache.commons.logging.Log' - ");
               var6.append(var5.getMessage());
               this.logDiagnostic(var6.toString());
               var1 = var2;
            }
         }
      }

      var2 = var1;
      if (var1 == null) {
         if (isDiagnosticsEnabled()) {
            this.logDiagnostic("Trying to get log class from system property 'org.apache.commons.logging.log'");
         }

         try {
            var2 = getSystemProperty("org.apache.commons.logging.log", (String)null);
         } catch (SecurityException var4) {
            var2 = var1;
            if (isDiagnosticsEnabled()) {
               StringBuffer var7 = new StringBuffer();
               var7.append("No access allowed to system property 'org.apache.commons.logging.log' - ");
               var7.append(var4.getMessage());
               this.logDiagnostic(var7.toString());
               var2 = var1;
            }
         }
      }

      var1 = var2;
      if (var2 != null) {
         var1 = var2.trim();
      }

      return var1;
   }

   private ClassLoader getBaseClassLoader() throws LogConfigurationException {
      Class var2 = class$org$apache$commons$logging$impl$LogFactoryImpl;
      Class var1 = var2;
      if (var2 == null) {
         var1 = class$("org.apache.commons.logging.impl.LogFactoryImpl");
         class$org$apache$commons$logging$impl$LogFactoryImpl = var1;
      }

      ClassLoader var4 = getClassLoader(var1);
      if (!this.useTCCL) {
         return var4;
      } else {
         ClassLoader var3 = getContextClassLoaderInternal();
         var4 = this.getLowestClassLoader(var3, var4);
         if (var4 == null) {
            if (this.allowFlawedContext) {
               if (isDiagnosticsEnabled()) {
                  this.logDiagnostic("[WARNING] the context classloader is not part of a parent-child relationship with the classloader that loaded LogFactoryImpl.");
               }

               return var3;
            } else {
               throw new LogConfigurationException("Bad classloader hierarchy; LogFactoryImpl was loaded via a classloader that is not related to the current context classloader.");
            }
         } else {
            if (var4 != var3) {
               if (!this.allowFlawedContext) {
                  throw new LogConfigurationException("Bad classloader hierarchy; LogFactoryImpl was loaded via a classloader that is not related to the current context classloader.");
               }

               if (isDiagnosticsEnabled()) {
                  this.logDiagnostic("Warning: the context classloader is an ancestor of the classloader that loaded LogFactoryImpl; it should be the same or a descendant. The application using commons-logging should ensure the context classloader is used correctly.");
                  return var4;
               }
            }

            return var4;
         }
      }
   }

   private boolean getBooleanConfiguration(String var1, boolean var2) {
      var1 = this.getConfigurationValue(var1);
      return var1 == null ? var2 : Boolean.valueOf(var1);
   }

   protected static ClassLoader getClassLoader(Class var0) {
      return LogFactory.getClassLoader(var0);
   }

   private String getConfigurationValue(String var1) {
      StringBuffer var2;
      if (isDiagnosticsEnabled()) {
         var2 = new StringBuffer();
         var2.append("[ENV] Trying to get configuration for item ");
         var2.append(var1);
         this.logDiagnostic(var2.toString());
      }

      Object var7 = this.getAttribute(var1);
      StringBuffer var3;
      if (var7 != null) {
         if (isDiagnosticsEnabled()) {
            var3 = new StringBuffer();
            var3.append("[ENV] Found LogFactory attribute [");
            var3.append(var7);
            var3.append("] for ");
            var3.append(var1);
            this.logDiagnostic(var3.toString());
         }

         return var7.toString();
      } else {
         if (isDiagnosticsEnabled()) {
            var2 = new StringBuffer();
            var2.append("[ENV] No LogFactory attribute found for ");
            var2.append(var1);
            this.logDiagnostic(var2.toString());
         }

         label52: {
            label51: {
               boolean var10001;
               String var8;
               try {
                  var8 = getSystemProperty(var1, (String)null);
               } catch (SecurityException var6) {
                  var10001 = false;
                  break label51;
               }

               if (var8 != null) {
                  try {
                     if (isDiagnosticsEnabled()) {
                        var3 = new StringBuffer();
                        var3.append("[ENV] Found system property [");
                        var3.append(var8);
                        var3.append("] for ");
                        var3.append(var1);
                        this.logDiagnostic(var3.toString());
                        return var8;
                     }

                     return var8;
                  } catch (SecurityException var4) {
                     var10001 = false;
                  }
               } else {
                  try {
                     if (isDiagnosticsEnabled()) {
                        var2 = new StringBuffer();
                        var2.append("[ENV] No system property found for property ");
                        var2.append(var1);
                        this.logDiagnostic(var2.toString());
                     }
                     break label52;
                  } catch (SecurityException var5) {
                     var10001 = false;
                  }
               }
            }

            if (isDiagnosticsEnabled()) {
               var2 = new StringBuffer();
               var2.append("[ENV] Security prevented reading system property ");
               var2.append(var1);
               this.logDiagnostic(var2.toString());
            }
         }

         if (isDiagnosticsEnabled()) {
            var2 = new StringBuffer();
            var2.append("[ENV] No configuration defined for item ");
            var2.append(var1);
            this.logDiagnostic(var2.toString());
         }

         return null;
      }
   }

   protected static ClassLoader getContextClassLoader() throws LogConfigurationException {
      return LogFactory.getContextClassLoader();
   }

   private static ClassLoader getContextClassLoaderInternal() throws LogConfigurationException {
      return (ClassLoader)AccessController.doPrivileged(new LogFactoryImpl$1());
   }

   private ClassLoader getLowestClassLoader(ClassLoader var1, ClassLoader var2) {
      if (var1 == null) {
         return var2;
      } else if (var2 == null) {
         return var1;
      } else {
         ClassLoader var3;
         for(var3 = var1; var3 != null; var3 = this.getParentClassLoader(var3)) {
            if (var3 == var2) {
               return var1;
            }
         }

         for(var3 = var2; var3 != null; var3 = this.getParentClassLoader(var3)) {
            if (var3 == var1) {
               return var2;
            }
         }

         return null;
      }
   }

   private ClassLoader getParentClassLoader(ClassLoader var1) {
      try {
         var1 = (ClassLoader)AccessController.doPrivileged(new LogFactoryImpl$3(this, var1));
         return var1;
      } catch (SecurityException var2) {
         this.logDiagnostic("[SECURITY] Unable to obtain parent classloader");
         return null;
      }
   }

   private static String getSystemProperty(String var0, String var1) throws SecurityException {
      return (String)AccessController.doPrivileged(new LogFactoryImpl$2(var0, var1));
   }

   private void handleFlawedDiscovery(String var1, ClassLoader var2, Throwable var3) {
      if (isDiagnosticsEnabled()) {
         StringBuffer var6 = new StringBuffer();
         var6.append("Could not instantiate Log '");
         var6.append(var1);
         var6.append("' -- ");
         var6.append(var3.getClass().getName());
         var6.append(": ");
         var6.append(var3.getLocalizedMessage());
         this.logDiagnostic(var6.toString());
         if (var3 instanceof InvocationTargetException) {
            Throwable var4 = ((InvocationTargetException)var3).getTargetException();
            if (var4 != null) {
               var6 = new StringBuffer();
               var6.append("... InvocationTargetException: ");
               var6.append(var4.getClass().getName());
               var6.append(": ");
               var6.append(var4.getLocalizedMessage());
               this.logDiagnostic(var6.toString());
               if (var4 instanceof ExceptionInInitializerError) {
                  Throwable var7 = ((ExceptionInInitializerError)var4).getException();
                  if (var7 != null) {
                     StringWriter var5 = new StringWriter();
                     var7.printStackTrace(new PrintWriter(var5, true));
                     var6 = new StringBuffer();
                     var6.append("... ExceptionInInitializerError: ");
                     var6.append(var5.toString());
                     this.logDiagnostic(var6.toString());
                  }
               }
            }
         }
      }

      if (!this.allowFlawedDiscovery) {
         throw new LogConfigurationException(var3);
      }
   }

   private void handleFlawedHierarchy(ClassLoader var1, Class var2) throws LogConfigurationException {
      boolean var5 = false;
      Class var7 = class$org$apache$commons$logging$Log;
      Class var6 = var7;
      if (var7 == null) {
         var6 = class$("org.apache.commons.logging.Log");
         class$org$apache$commons$logging$Log = var6;
      }

      String var23 = var6.getName();
      Class[] var24 = var2.getInterfaces();
      int var3 = 0;

      boolean var4;
      while(true) {
         var4 = var5;
         if (var3 >= var24.length) {
            break;
         }

         if (var23.equals(var24[var3].getName())) {
            var4 = true;
            break;
         }

         ++var3;
      }

      StringBuffer var21;
      if (var4) {
         if (isDiagnosticsEnabled()) {
            label354: {
               Throwable var10000;
               label335: {
                  boolean var10001;
                  label356: {
                     try {
                        if (class$org$apache$commons$logging$Log == null) {
                           var6 = class$("org.apache.commons.logging.Log");
                           class$org$apache$commons$logging$Log = var6;
                           break label356;
                        }
                     } catch (Throwable var19) {
                        var10000 = var19;
                        var10001 = false;
                        break label335;
                     }

                     try {
                        var6 = class$org$apache$commons$logging$Log;
                     } catch (Throwable var18) {
                        var10000 = var18;
                        var10001 = false;
                        break label335;
                     }
                  }

                  label325:
                  try {
                     ClassLoader var26 = getClassLoader(var6);
                     StringBuffer var25 = new StringBuffer();
                     var25.append("Class '");
                     var25.append(var2.getName());
                     var25.append("' was found in classloader ");
                     var25.append(objectId(var1));
                     var25.append(". It is bound to a Log interface which is not");
                     var25.append(" the one loaded from classloader ");
                     var25.append(objectId(var26));
                     this.logDiagnostic(var25.toString());
                     break label354;
                  } catch (Throwable var17) {
                     var10000 = var17;
                     var10001 = false;
                     break label325;
                  }
               }

               Throwable var20 = var10000;
               handleThrowable(var20);
               var21 = new StringBuffer();
               var21.append("Error while trying to output diagnostics about bad class '");
               var21.append(var2);
               var21.append("'");
               this.logDiagnostic(var21.toString());
            }
         }

         Class var22;
         StringBuffer var27;
         if (!this.allowFlawedHierarchy) {
            var27 = new StringBuffer();
            var27.append("Terminating logging for this context ");
            var27.append("due to bad log hierarchy. ");
            var27.append("You have more than one version of '");
            var2 = class$org$apache$commons$logging$Log;
            var22 = var2;
            if (var2 == null) {
               var22 = class$("org.apache.commons.logging.Log");
               class$org$apache$commons$logging$Log = var22;
            }

            var27.append(var22.getName());
            var27.append("' visible.");
            if (isDiagnosticsEnabled()) {
               this.logDiagnostic(var27.toString());
            }

            throw new LogConfigurationException(var27.toString());
         }

         if (isDiagnosticsEnabled()) {
            var27 = new StringBuffer();
            var27.append("Warning: bad log hierarchy. ");
            var27.append("You have more than one version of '");
            var2 = class$org$apache$commons$logging$Log;
            var22 = var2;
            if (var2 == null) {
               var22 = class$("org.apache.commons.logging.Log");
               class$org$apache$commons$logging$Log = var22;
            }

            var27.append(var22.getName());
            var27.append("' visible.");
            this.logDiagnostic(var27.toString());
            return;
         }
      } else {
         if (!this.allowFlawedDiscovery) {
            var21 = new StringBuffer();
            var21.append("Terminating logging for this context. ");
            var21.append("Log class '");
            var21.append(var2.getName());
            var21.append("' does not implement the Log interface.");
            if (isDiagnosticsEnabled()) {
               this.logDiagnostic(var21.toString());
            }

            throw new LogConfigurationException(var21.toString());
         }

         if (isDiagnosticsEnabled()) {
            var21 = new StringBuffer();
            var21.append("[WARNING] Log class '");
            var21.append(var2.getName());
            var21.append("' does not implement the Log interface.");
            this.logDiagnostic(var21.toString());
         }
      }

   }

   private void informUponSimilarName(StringBuffer var1, String var2, String var3) {
      if (!var2.equals(var3)) {
         if (var2.regionMatches(true, 0, var3, 0, PKG_LEN + 5)) {
            var1.append(" Did you mean '");
            var1.append(var3);
            var1.append("'?");
         }

      }
   }

   private void initConfiguration() {
      this.allowFlawedContext = this.getBooleanConfiguration("org.apache.commons.logging.Log.allowFlawedContext", true);
      this.allowFlawedDiscovery = this.getBooleanConfiguration("org.apache.commons.logging.Log.allowFlawedDiscovery", true);
      this.allowFlawedHierarchy = this.getBooleanConfiguration("org.apache.commons.logging.Log.allowFlawedHierarchy", true);
   }

   private void initDiagnostics() {
      ClassLoader var1 = getClassLoader(this.getClass());
      String var4;
      if (var1 == null) {
         var4 = "BOOTLOADER";
      } else {
         try {
            var4 = objectId(var1);
         } catch (SecurityException var3) {
            var4 = "UNKNOWN";
         }
      }

      StringBuffer var2 = new StringBuffer();
      var2.append("[LogFactoryImpl@");
      var2.append(System.identityHashCode(this));
      var2.append(" from ");
      var2.append(var4);
      var2.append("] ");
      this.diagnosticPrefix = var2.toString();
   }

   protected static boolean isDiagnosticsEnabled() {
      return LogFactory.isDiagnosticsEnabled();
   }

   private boolean isLogLibraryAvailable(String var1, String var2) {
      if (isDiagnosticsEnabled()) {
         StringBuffer var3 = new StringBuffer();
         var3.append("Checking for '");
         var3.append(var1);
         var3.append("'.");
         this.logDiagnostic(var3.toString());
      }

      StringBuffer var5;
      try {
         if (this.createLogFromClass(var2, this.getClass().getName(), false) == null) {
            if (isDiagnosticsEnabled()) {
               var5 = new StringBuffer();
               var5.append("Did not find '");
               var5.append(var1);
               var5.append("'.");
               this.logDiagnostic(var5.toString());
               return false;
            } else {
               return false;
            }
         } else {
            if (isDiagnosticsEnabled()) {
               var5 = new StringBuffer();
               var5.append("Found '");
               var5.append(var1);
               var5.append("'.");
               this.logDiagnostic(var5.toString());
            }

            return true;
         }
      } catch (LogConfigurationException var4) {
         if (isDiagnosticsEnabled()) {
            var5 = new StringBuffer();
            var5.append("Logging system '");
            var5.append(var1);
            var5.append("' is available but not useable.");
            this.logDiagnostic(var5.toString());
         }

         return false;
      }
   }

   public Object getAttribute(String var1) {
      return this.attributes.get(var1);
   }

   public String[] getAttributeNames() {
      return (String[])((String[])this.attributes.keySet().toArray(new String[this.attributes.size()]));
   }

   public Log getInstance(Class var1) throws LogConfigurationException {
      return this.getInstance(var1.getName());
   }

   public Log getInstance(String var1) throws LogConfigurationException {
      Log var3 = (Log)this.instances.get(var1);
      Log var2 = var3;
      if (var3 == null) {
         var2 = this.newInstance(var1);
         this.instances.put(var1, var2);
      }

      return var2;
   }

   protected String getLogClassName() {
      if (this.logClassName == null) {
         this.discoverLogImplementation(this.getClass().getName());
      }

      return this.logClassName;
   }

   protected Constructor getLogConstructor() throws LogConfigurationException {
      if (this.logConstructor == null) {
         this.discoverLogImplementation(this.getClass().getName());
      }

      return this.logConstructor;
   }

   protected boolean isJdk13LumberjackAvailable() {
      return this.isLogLibraryAvailable("Jdk13Lumberjack", "org.apache.commons.logging.impl.Jdk13LumberjackLogger");
   }

   protected boolean isJdk14Available() {
      return this.isLogLibraryAvailable("Jdk14", "org.apache.commons.logging.impl.Jdk14Logger");
   }

   protected boolean isLog4JAvailable() {
      return this.isLogLibraryAvailable("Log4J", "org.apache.commons.logging.impl.Log4JLogger");
   }

   protected void logDiagnostic(String var1) {
      if (isDiagnosticsEnabled()) {
         StringBuffer var2 = new StringBuffer();
         var2.append(this.diagnosticPrefix);
         var2.append(var1);
         logRawDiagnostic(var2.toString());
      }

   }

   protected Log newInstance(String param1) throws LogConfigurationException {
      // $FF: Couldn't be decompiled
   }

   public void release() {
      this.logDiagnostic("Releasing all known loggers");
      this.instances.clear();
   }

   public void removeAttribute(String var1) {
      this.attributes.remove(var1);
   }

   public void setAttribute(String var1, Object var2) {
      if (this.logConstructor != null) {
         this.logDiagnostic("setAttribute: call too late; configuration already performed.");
      }

      if (var2 == null) {
         this.attributes.remove(var1);
      } else {
         this.attributes.put(var1, var2);
      }

      if (var1.equals("use_tccl")) {
         boolean var3;
         if (var2 != null && Boolean.valueOf(var2.toString())) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.useTCCL = var3;
      }

   }
}
