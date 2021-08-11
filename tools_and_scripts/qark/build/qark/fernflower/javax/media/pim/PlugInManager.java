package javax.media.pim;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.Codec;
import javax.media.Demultiplexer;
import javax.media.Effect;
import javax.media.Format;
import javax.media.Multiplexer;
import javax.media.Renderer;
import net.sf.fmj.registry.Registry;
import net.sf.fmj.utility.LoggerSingleton;

public class PlugInManager extends javax.media.PlugInManager {
   private static boolean TRACE;
   private static final Logger logger;
   private static final HashMap[] pluginMaps;
   private static Registry registry;

   static {
      logger = LoggerSingleton.logger;
      TRACE = false;
      registry = Registry.getInstance();
      pluginMaps = new HashMap[]{new HashMap(), new HashMap(), new HashMap(), new HashMap(), new HashMap()};

      for(int var0 = 0; var0 < 5; ++var0) {
         List var2 = registry.getPluginList(var0 + 1);
         HashMap var1 = pluginMaps[var0];
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            PlugInInfo var3 = getPluginInfo((String)var4.next());
            if (var3 != null) {
               var1.put(var3.className, var3);
            }
         }
      }

   }

   private PlugInManager() {
   }

   public static boolean addPlugIn(String var0, Format[] var1, Format[] var2, int var3) {
      synchronized(PlugInManager.class){}

      Throwable var10000;
      label305: {
         boolean var10001;
         label306: {
            try {
               try {
                  Class.forName(var0);
                  break label306;
               } catch (ClassNotFoundException var40) {
               }
            } catch (Throwable var41) {
               Throwable var43 = var41;

               try {
                  Logger var46 = logger;
                  Level var4 = Level.WARNING;
                  StringBuilder var5 = new StringBuilder();
                  var5.append("Unable to addPlugIn for ");
                  var5.append(var0);
                  var5.append(" due to inability to get its class: ");
                  var5.append(var43);
                  var46.log(var4, var5.toString(), var43);
               } catch (Throwable var36) {
                  var10000 = var36;
                  var10001 = false;
                  break label305;
               }

               return false;
            }

            try {
               Logger var44 = logger;
               StringBuilder var47 = new StringBuilder();
               var47.append("addPlugIn failed for nonexistant class: ");
               var47.append(var0);
               var44.finer(var47.toString());
            } catch (Throwable var37) {
               var10000 = var37;
               var10001 = false;
               break label305;
            }

            return false;
         }

         PlugInInfo var49;
         try {
            var49 = find(var0, var3);
         } catch (Throwable var39) {
            var10000 = var39;
            var10001 = false;
            break label305;
         }

         if (var49 != null) {
            return false;
         }

         try {
            PlugInInfo var45 = new PlugInInfo(var0, var1, var2);
            List var48 = registry.getPluginList(var3);
            HashMap var50 = pluginMaps[var3 - 1];
            var48.add(var0);
            var50.put(var0, var45);
            registry.setPluginList(var3, var48);
         } catch (Throwable var38) {
            var10000 = var38;
            var10001 = false;
            break label305;
         }

         return true;
      }

      Throwable var42 = var10000;
      throw var42;
   }

   public static void commit() throws IOException {
      synchronized(PlugInManager.class){}

      try {
         registry.commit();
      } finally {
         ;
      }

   }

   private static PlugInInfo find(String var0, int var1) {
      synchronized(PlugInManager.class){}

      PlugInInfo var4;
      try {
         var4 = (PlugInInfo)pluginMaps[var1 - 1].get(var0);
      } finally {
         ;
      }

      return var4;
   }

   public static Vector getPlugInList(Format var0, Format var1, int var2) {
      synchronized(PlugInManager.class){}

      Throwable var10000;
      label1229: {
         boolean var10001;
         try {
            if (TRACE) {
               logger.info("getting plugin list...");
            }
         } catch (Throwable var98) {
            var10000 = var98;
            var10001 = false;
            break label1229;
         }

         Vector var101;
         label1230: {
            try {
               if (!isValid(var2)) {
                  var101 = new Vector();
                  break label1230;
               }
            } catch (Throwable var99) {
               var10000 = var99;
               var10001 = false;
               break label1229;
            }

            Vector var6;
            Vector var7;
            HashMap var8;
            try {
               var6 = new Vector();
               var7 = getVector(var2);
               var8 = pluginMaps[var2 - 1];
            } catch (Throwable var97) {
               var10000 = var97;
               var10001 = false;
               break label1229;
            }

            var2 = 0;

            while(true) {
               PlugInInfo var9;
               label1208: {
                  try {
                     if (var2 < var7.size()) {
                        var9 = (PlugInInfo)var8.get((String)var7.get(var2));
                        break label1208;
                     }
                  } catch (Throwable var96) {
                     var10000 = var96;
                     var10001 = false;
                     break label1229;
                  }

                  return var6;
               }

               if (var9 != null) {
                  label1232: {
                     int var3;
                     boolean var4;
                     boolean var5;
                     if (var0 != null) {
                        try {
                           if (var9.inputFormats == null) {
                              break label1232;
                           }
                        } catch (Throwable var94) {
                           var10000 = var94;
                           var10001 = false;
                           break label1229;
                        }

                        var5 = false;
                        var3 = 0;

                        label1196: {
                           while(true) {
                              var4 = var5;

                              try {
                                 if (var3 >= var9.inputFormats.length) {
                                    break label1196;
                                 }

                                 if (var0.matches(var9.inputFormats[var3])) {
                                    break;
                                 }
                              } catch (Throwable var95) {
                                 var10000 = var95;
                                 var10001 = false;
                                 break label1229;
                              }

                              ++var3;
                           }

                           var4 = true;
                        }

                        if (!var4) {
                           break label1232;
                        }
                     }

                     if (var1 != null) {
                        try {
                           if (var9.outputFormats == null) {
                              break label1232;
                           }
                        } catch (Throwable var92) {
                           var10000 = var92;
                           var10001 = false;
                           break label1229;
                        }

                        var5 = false;
                        var3 = 0;

                        label1177: {
                           while(true) {
                              var4 = var5;

                              try {
                                 if (var3 >= var9.outputFormats.length) {
                                    break label1177;
                                 }

                                 if (var1.matches(var9.outputFormats[var3])) {
                                    break;
                                 }
                              } catch (Throwable var93) {
                                 var10000 = var93;
                                 var10001 = false;
                                 break label1229;
                              }

                              ++var3;
                           }

                           var4 = true;
                        }

                        if (!var4) {
                           break label1232;
                        }
                     }

                     try {
                        var6.add(var9.className);
                     } catch (Throwable var91) {
                        var10000 = var91;
                        var10001 = false;
                        break label1229;
                     }
                  }
               }

               ++var2;
            }
         }

         return var101;
      }

      Throwable var100 = var10000;
      throw var100;
   }

   private static final PlugInInfo getPluginInfo(String var0) {
      Object var1;
      Logger var2;
      StringBuilder var3;
      try {
         var1 = Class.forName(var0).newInstance();
      } catch (Throwable var5) {
         if (!(var5 instanceof ThreadDeath)) {
            var2 = logger;
            var3 = new StringBuilder();
            var3.append("Problem loading plugin ");
            var3.append(var0);
            var3.append(": ");
            var3.append(var5);
            var2.fine(var3.toString());
            return null;
         }

         throw (ThreadDeath)var5;
      }

      Object var7;
      if (var1 instanceof Demultiplexer) {
         var7 = ((Demultiplexer)var1).getSupportedInputContentDescriptors();
         var1 = null;
      } else if (var1 instanceof Codec) {
         Codec var6 = (Codec)var1;
         var7 = var6.getSupportedInputFormats();
         var1 = var6.getSupportedOutputFormats((Format)null);
      } else if (var1 instanceof Multiplexer) {
         Multiplexer var8 = (Multiplexer)var1;
         var7 = var8.getSupportedInputFormats();
         var1 = var8.getSupportedOutputContentDescriptors((Format[])null);
      } else if (var1 instanceof Renderer) {
         var7 = ((Renderer)var1).getSupportedInputFormats();
         var1 = null;
      } else {
         if (!(var1 instanceof Effect)) {
            var2 = logger;
            var3 = new StringBuilder();
            var3.append("Unknown plugin type: ");
            var3.append(var1);
            var3.append(" for plugin ");
            var3.append(var0);
            var2.warning(var3.toString());
            return null;
         }

         Effect var9 = (Effect)var1;
         var7 = var9.getSupportedInputFormats();
         var1 = var9.getSupportedOutputFormats((Format)null);
      }

      return new PlugInInfo(var0, (Format[])var7, (Format[])var1);
   }

   public static Format[] getSupportedInputFormats(String var0, int var1) {
      synchronized(PlugInManager.class){}

      Throwable var10000;
      label78: {
         boolean var10001;
         PlugInInfo var8;
         try {
            var8 = find(var0, var1);
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label78;
         }

         if (var8 == null) {
            return null;
         }

         Format[] var10;
         try {
            var10 = var8.inputFormats;
         } catch (Throwable var6) {
            var10000 = var6;
            var10001 = false;
            break label78;
         }

         return var10;
      }

      Throwable var9 = var10000;
      throw var9;
   }

   public static Format[] getSupportedOutputFormats(String var0, int var1) {
      synchronized(PlugInManager.class){}

      Throwable var10000;
      label78: {
         boolean var10001;
         PlugInInfo var8;
         try {
            var8 = find(var0, var1);
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label78;
         }

         if (var8 == null) {
            return null;
         }

         Format[] var10;
         try {
            var10 = var8.outputFormats;
         } catch (Throwable var6) {
            var10000 = var6;
            var10001 = false;
            break label78;
         }

         return var10;
      }

      Throwable var9 = var10000;
      throw var9;
   }

   private static Vector getVector(int var0) {
      if (!isValid(var0)) {
         return null;
      } else {
         List var1 = registry.getPluginList(var0);
         Vector var2 = new Vector();
         var2.addAll(var1);
         return var2;
      }
   }

   private static boolean isValid(int var0) {
      return var0 >= 1 && var0 <= 5;
   }

   public static boolean removePlugIn(String var0, int var1) {
      synchronized(PlugInManager.class){}

      boolean var2;
      boolean var3;
      label81: {
         Throwable var10000;
         label80: {
            boolean var10001;
            List var4;
            label79: {
               label78: {
                  try {
                     var4 = registry.getPluginList(var1);
                     HashMap var5 = pluginMaps[var1 - 1];
                     var3 = var4.remove(var0);
                     if (var5.remove(var0) != null) {
                        break label78;
                     }
                  } catch (Throwable var11) {
                     var10000 = var11;
                     var10001 = false;
                     break label80;
                  }

                  var2 = false;
                  break label79;
               }

               var2 = true;
            }

            label72:
            try {
               registry.setPluginList(var1, var4);
               break label81;
            } catch (Throwable var10) {
               var10000 = var10;
               var10001 = false;
               break label72;
            }
         }

         Throwable var12 = var10000;
         throw var12;
      }

      return var3 | var2;
   }

   public static void setPlugInList(Vector var0, int var1) {
      synchronized(PlugInManager.class){}

      try {
         registry.setPluginList(var1, var0);
      } finally {
         ;
      }

   }
}
