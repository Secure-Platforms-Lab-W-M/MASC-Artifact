package net.sf.fmj.utility;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.Codec;
import javax.media.Demultiplexer;
import javax.media.Effect;
import javax.media.Format;
import javax.media.Multiplexer;
import javax.media.PlugInManager;
import javax.media.Renderer;
import javax.media.protocol.ContentDescriptor;

public class PlugInUtility {
   private static final boolean TRACE = false;
   private static final Logger logger;

   static {
      logger = LoggerSingleton.logger;
   }

   public static PlugInInfo getPlugInInfo(String var0) {
      Throwable var10000;
      Logger var81;
      label674: {
         Format[] var3;
         ContentDescriptor[] var79;
         boolean var10001;
         label675: {
            Object var2;
            try {
               var2 = Class.forName(var0).newInstance();
               if (var2 instanceof Demultiplexer) {
                  var79 = ((Demultiplexer)var2).getSupportedInputContentDescriptors();
                  var3 = new Format[var79.length];
                  break label675;
               }
            } catch (Throwable var77) {
               var10000 = var77;
               var10001 = false;
               break label674;
            }

            byte var1;
            Format[] var82;
            label676: {
               label663: {
                  label662: {
                     try {
                        if (!(var2 instanceof Codec)) {
                           break label663;
                        }

                        Codec var4 = (Codec)var2;
                        var3 = var4.getSupportedInputFormats();
                        var82 = var4.getSupportedOutputFormats((Format)null);
                        if (!(var2 instanceof Effect)) {
                           break label662;
                        }
                     } catch (Throwable var76) {
                        var10000 = var76;
                        var10001 = false;
                        break label674;
                     }

                     var1 = 3;
                     break label676;
                  }

                  var1 = 2;
                  break label676;
               }

               try {
                  if (var2 instanceof Renderer) {
                     return new PlugInInfo(var0, ((Renderer)var2).getSupportedInputFormats(), new Format[0], 4);
                  }
               } catch (Throwable var74) {
                  var10000 = var74;
                  var10001 = false;
                  break label674;
               }

               try {
                  if (var2 instanceof Multiplexer) {
                     var79 = ((Multiplexer)var2).getSupportedOutputContentDescriptors((Format[])null);
                     return new PlugInInfo(var0, new Format[0], var79, 5);
                  }
               } catch (Throwable var75) {
                  var10000 = var75;
                  var10001 = false;
                  break label674;
               }

               try {
                  var81 = logger;
                  StringBuilder var83 = new StringBuilder();
                  var83.append("PlugInUtility: Unknown or unsupported plug-in: ");
                  var83.append(var2.getClass());
                  var81.warning(var83.toString());
                  return null;
               } catch (Throwable var73) {
                  var10000 = var73;
                  var10001 = false;
                  break label674;
               }
            }

            try {
               return new PlugInInfo(var0, var3, var82, var1);
            } catch (Throwable var72) {
               var10000 = var72;
               var10001 = false;
               break label674;
            }
         }

         int var78 = 0;

         while(true) {
            try {
               if (var78 >= var79.length) {
                  break;
               }
            } catch (Throwable var71) {
               var10000 = var71;
               var10001 = false;
               break label674;
            }

            var3[var78] = var79[var78];
            ++var78;
         }

         label632:
         try {
            return new PlugInInfo(var0, var3, new Format[0], 1);
         } catch (Throwable var70) {
            var10000 = var70;
            var10001 = false;
            break label632;
         }
      }

      Throwable var80 = var10000;
      var81 = logger;
      Level var84 = Level.FINE;
      StringBuilder var5 = new StringBuilder();
      var5.append("PlugInUtility: Unable to get plugin info for ");
      var5.append(var0);
      var5.append(": ");
      var5.append(var80);
      var81.log(var84, var5.toString());
      return null;
   }

   public static boolean registerPlugIn(String var0) {
      PlugInInfo var2 = getPlugInInfo(var0);
      if (var2 == null) {
         return false;
      } else {
         try {
            boolean var1 = PlugInManager.addPlugIn(var2.className, var2.field_147, var2.out, var2.type);
            return var1;
         } catch (Throwable var6) {
            Logger var3 = logger;
            StringBuilder var4 = new StringBuilder();
            var4.append("PlugInUtility: Unable to register plugin ");
            var4.append(var0);
            var4.append(": ");
            var4.append(var6);
            var3.fine(var4.toString());
            return false;
         }
      }
   }
}
