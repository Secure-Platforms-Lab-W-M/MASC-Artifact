package net.sf.fmj.media.cdp.javasound;

import java.util.logging.Logger;
import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Format;
import javax.media.MediaLocator;
import net.sf.fmj.media.protocol.javasound.DataSource;
import net.sf.fmj.utility.LoggerSingleton;
import org.atalk.android.util.javax.sound.sampled.AudioSystem;
import org.atalk.android.util.javax.sound.sampled.Mixer.Info;

public class CaptureDevicePlugger {
   private static final Logger logger;

   static {
      logger = LoggerSingleton.logger;
   }

   public void addCaptureDevices() {
      int var2 = 0;
      Info[] var4 = AudioSystem.getMixerInfo();

      int var3;
      for(int var1 = 0; var1 < var4.length; var2 = var3) {
         AudioSystem.getMixer(var4[var1]);
         Format[] var5 = DataSource.querySupportedFormats(var1);
         var3 = var2;
         if (var5 != null) {
            var3 = var2;
            if (var5.length > 0) {
               StringBuilder var6 = new StringBuilder();
               var6.append("javasound:");
               var6.append(var4[var1].getName());
               var6.append(":");
               var6.append(var2);
               String var9 = var6.toString();
               StringBuilder var7 = new StringBuilder();
               var7.append("javasound:#");
               var7.append(var1);
               CaptureDeviceInfo var8 = new CaptureDeviceInfo(var9, new MediaLocator(var7.toString()), var5);
               var3 = var2 + 1;
               Logger var10;
               if (CaptureDeviceManager.getDevice(var8.getName()) == null) {
                  CaptureDeviceManager.addDevice(var8);
                  var10 = logger;
                  var7 = new StringBuilder();
                  var7.append("CaptureDevicePlugger: Added ");
                  var7.append(var8.getLocator());
                  var10.fine(var7.toString());
               } else {
                  var10 = logger;
                  var7 = new StringBuilder();
                  var7.append("CaptureDevicePlugger: Already present, skipping ");
                  var7.append(var8.getLocator());
                  var10.fine(var7.toString());
               }
            }
         }

         ++var1;
      }

   }
}
