package net.sf.fmj.registry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;
import javax.media.CaptureDeviceInfo;
import net.sf.fmj.media.RegistryDefaults;
import net.sf.fmj.utility.LoggerSingleton;
import net.sf.fmj.utility.PlugInInfo;
import net.sf.fmj.utility.PlugInUtility;

public class Registry {
   private static final int DEFAULT_REGISTRY_WRITE_FORMAT = 0;
   public static final int NUM_PLUGIN_TYPES = 5;
   private static final boolean READD_JAVAX = false;
   private static final int[] REGISTRY_FORMATS;
   private static final String SYSTEM_PROPERTY_DISABLE_COMMIT = "net.sf.fmj.utility.JmfRegistry.disableCommit";
   private static final String SYSTEM_PROPERTY_DISABLE_LOAD = "net.sf.fmj.utility.JmfRegistry.disableLoad";
   private static final Logger logger;
   private static Registry registry;
   private static Object registryMutex;
   private final boolean disableCommit;
   private final RegistryContents registryContents = new RegistryContents();

   static {
      logger = LoggerSingleton.logger;
      REGISTRY_FORMATS = new int[]{0, 1};
      registry = null;
      registryMutex = new Object();
   }

   private Registry() {
      String var1 = Boolean.TRUE.toString();
      String var2 = Boolean.FALSE.toString();
      this.disableCommit = System.getProperty("net.sf.fmj.utility.JmfRegistry.disableCommit", var2).equals(var1);

      try {
         if (System.getProperty("net.sf.fmj.utility.JmfRegistry.disableLoad", var2).equals(var1)) {
            this.setDefaults();
            return;
         }
      } catch (SecurityException var3) {
      }

      if (!this.load()) {
         logger.fine("Using registry defaults.");
         this.setDefaults();
      }

   }

   public static Registry getInstance() {
      Object var0 = registryMutex;
      synchronized(var0){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            if (registry == null) {
               registry = new Registry();
            }
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label122;
         }

         label119:
         try {
            Registry var14 = registry;
            return var14;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label119;
         }
      }

      while(true) {
         Throwable var1 = var10000;

         try {
            throw var1;
         } catch (Throwable var11) {
            var10000 = var11;
            var10001 = false;
            continue;
         }
      }
   }

   private File getRegistryFile(int var1) {
      String var2;
      if (var1 == 1) {
         var2 = ".fmj.registry.properties";
      } else {
         var2 = ".fmj.registry.xml";
      }

      String var4 = System.getProperty("net.sf.fmj.utility.JmfRegistry.filename", var2);
      File var3 = new File(var4);
      File var5 = var3;
      if (!var3.isAbsolute()) {
         var5 = new File(System.getProperty("user.home"), var4);
      }

      return var5;
   }

   private InputStream getRegistryResourceStream(int var1) {
      String var2;
      if (var1 == 1) {
         var2 = "/fmj.registry.properties";
      } else {
         var2 = "/fmj.registry.xml";
      }

      return Registry.class.getResourceAsStream(var2);
   }

   private boolean load() {
      synchronized(this){}

      Throwable var10000;
      label293: {
         boolean var10001;
         int var2;
         int[] var4;
         try {
            var4 = REGISTRY_FORMATS;
            var2 = var4.length;
         } catch (Throwable var24) {
            var10000 = var24;
            var10001 = false;
            break label293;
         }

         int var1;
         boolean var3;
         for(var1 = 0; var1 < var2; ++var1) {
            try {
               var3 = this.loadFromResource(var4[var1]);
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break label293;
            }

            if (var3) {
               return true;
            }
         }

         try {
            var4 = REGISTRY_FORMATS;
            var2 = var4.length;
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label293;
         }

         var1 = 0;

         while(true) {
            if (var1 >= var2) {
               return false;
            }

            try {
               var3 = this.loadFromFile(var4[var1]);
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break;
            }

            if (var3) {
               return true;
            }

            ++var1;
         }
      }

      Throwable var25 = var10000;
      throw var25;
   }

   private boolean loadFromFile(int var1) {
      synchronized(this){}

      Logger var3;
      StringBuilder var4;
      try {
         File var11 = this.getRegistryFile(var1);
         if (var11.isFile() && var11.length() > 0L) {
            FileInputStream var12 = new FileInputStream(var11);
            RegistryIOFactory.createRegistryIO(var1, this.registryContents).load(var12);
            var3 = logger;
            var4 = new StringBuilder();
            var4.append("Loaded registry from file: ");
            var4.append(var11.getAbsolutePath());
            var3.info(var4.toString());
            return true;
         }
      } catch (Throwable var10) {
         Throwable var2 = var10;

         try {
            var3 = logger;
            var4 = new StringBuilder();
            var4.append("Problem loading registry from file: ");
            var4.append(var2.getMessage());
            var3.warning(var4.toString());
            return false;
         } finally {
            ;
         }
      }

      return false;
   }

   private boolean loadFromResource(int var1) {
      synchronized(this){}

      Throwable var10000;
      Logger var3;
      StringBuilder var4;
      label210: {
         boolean var10001;
         InputStream var2;
         try {
            var2 = this.getRegistryResourceStream(var1);
         } catch (Throwable var24) {
            var10000 = var24;
            var10001 = false;
            break label210;
         }

         if (var2 == null) {
            return false;
         }

         try {
            RegistryIOFactory.createRegistryIO(var1, this.registryContents).load(var2);
            var3 = logger;
            var4 = new StringBuilder();
            var4.append("Loaded registry from resource, format: ");
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label210;
         }

         String var25;
         if (var1 == 1) {
            var25 = "Properties";
         } else {
            var25 = "XML";
         }

         try {
            var4.append(var25);
            var3.info(var4.toString());
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label210;
         }

         return true;
      }

      Throwable var26 = var10000;

      try {
         var3 = logger;
         var4 = new StringBuilder();
         var4.append("Problem loading registry from resource: ");
         var4.append(var26.getMessage());
         var3.warning(var4.toString());
      } finally {
         ;
      }

      return false;
   }

   private void setDefaults() {
      int var1 = RegistryDefaults.getDefaultFlags();
      this.registryContents.protocolPrefixList.addAll(RegistryDefaults.protocolPrefixList(var1));
      this.registryContents.contentPrefixList.addAll(RegistryDefaults.contentPrefixList(var1));
      Iterator var2 = RegistryDefaults.plugInList(var1).iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         PlugInInfo var4;
         if (var3 instanceof PlugInInfo) {
            var4 = (PlugInInfo)var3;
            this.registryContents.plugins[var4.type - 1].add(var4.className);
         } else {
            var4 = PlugInUtility.getPlugInInfo((String)var3);
            if (var4 != null) {
               this.registryContents.plugins[var4.type - 1].add(var4.className);
            }
         }
      }

   }

   public boolean addDevice(CaptureDeviceInfo var1) {
      synchronized(this){}

      boolean var2;
      try {
         var2 = this.registryContents.captureDeviceInfoList.add(var1);
      } finally {
         ;
      }

      return var2;
   }

   public void addMimeType(String var1, String var2) {
      synchronized(this){}

      try {
         this.registryContents.mimeTable.addMimeType(var1, var2);
      } finally {
         ;
      }

   }

   public void commit() throws IOException {
      synchronized(this){}

      Throwable var10000;
      label231: {
         boolean var1;
         boolean var10001;
         try {
            var1 = this.disableCommit;
         } catch (Throwable var31) {
            var10000 = var31;
            var10001 = false;
            break label231;
         }

         if (var1) {
            return;
         }

         File var2;
         FileOutputStream var3;
         try {
            var2 = this.getRegistryFile(0);
            var3 = new FileOutputStream(var2);
         } catch (Throwable var30) {
            var10000 = var30;
            var10001 = false;
            break label231;
         }

         try {
            RegistryIOFactory.createRegistryIO(0, this.registryContents).write(var3);
            var3.flush();
         } finally {
            try {
               var3.close();
            } catch (Throwable var28) {
               var10000 = var28;
               var10001 = false;
               break label231;
            }
         }

         Logger var33 = logger;
         StringBuilder var4 = new StringBuilder();
         var4.append("Wrote registry file: ");
         var4.append(var2.getAbsolutePath());
         var33.info(var4.toString());
         return;
      }

      Throwable var32 = var10000;
      throw var32;
   }

   public Vector getContentPrefixList() {
      synchronized(this){}

      Vector var1;
      try {
         var1 = (Vector)this.registryContents.contentPrefixList.clone();
      } finally {
         ;
      }

      return var1;
   }

   public String getDefaultExtension(String var1) {
      synchronized(this){}

      try {
         var1 = this.registryContents.mimeTable.getDefaultExtension(var1);
      } finally {
         ;
      }

      return var1;
   }

   public Vector getDeviceList() {
      synchronized(this){}

      Vector var1;
      try {
         var1 = (Vector)this.registryContents.captureDeviceInfoList.clone();
      } finally {
         ;
      }

      return var1;
   }

   public List getExtensions(String var1) {
      synchronized(this){}

      List var4;
      try {
         var4 = this.registryContents.mimeTable.getExtensions(var1);
      } finally {
         ;
      }

      return var4;
   }

   public Hashtable getMimeTable() {
      synchronized(this){}

      Hashtable var1;
      try {
         var1 = this.registryContents.mimeTable.getMimeTable();
      } finally {
         ;
      }

      return var1;
   }

   public String getMimeType(String var1) {
      synchronized(this){}

      try {
         var1 = this.registryContents.mimeTable.getMimeType(var1);
      } finally {
         ;
      }

      return var1;
   }

   public List getPluginList(int var1) {
      synchronized(this){}

      List var2;
      try {
         var2 = (List)this.registryContents.plugins[var1 - 1].clone();
      } finally {
         ;
      }

      return var2;
   }

   public Vector getProtocolPrefixList() {
      synchronized(this){}

      Vector var1;
      try {
         var1 = (Vector)this.registryContents.protocolPrefixList.clone();
      } finally {
         ;
      }

      return var1;
   }

   public boolean removeDevice(CaptureDeviceInfo var1) {
      synchronized(this){}

      boolean var2;
      try {
         var2 = this.registryContents.captureDeviceInfoList.remove(var1);
      } finally {
         ;
      }

      return var2;
   }

   public boolean removeMimeType(String var1) {
      synchronized(this){}

      boolean var2;
      try {
         var2 = this.registryContents.mimeTable.removeMimeType(var1);
      } finally {
         ;
      }

      return var2;
   }

   public void setContentPrefixList(List var1) {
      synchronized(this){}

      try {
         this.registryContents.contentPrefixList.clear();
         this.registryContents.contentPrefixList.addAll(var1);
      } finally {
         ;
      }

   }

   public void setPluginList(int var1, List var2) {
      synchronized(this){}

      try {
         Vector var3 = this.registryContents.plugins[var1 - 1];
         var3.clear();
         var3.addAll(var2);
      } finally {
         ;
      }

   }

   public void setProtocolPrefixList(List var1) {
      synchronized(this){}

      try {
         this.registryContents.protocolPrefixList.clear();
         this.registryContents.protocolPrefixList.addAll(var1);
      } finally {
         ;
      }

   }
}
