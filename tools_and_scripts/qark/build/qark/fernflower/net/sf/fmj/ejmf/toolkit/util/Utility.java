package net.sf.fmj.ejmf.toolkit.util;

import java.applet.Applet;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.media.Control;
import javax.media.Controller;
import javax.media.GainControl;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.Time;
import org.atalk.android.util.java.awt.Component;
import org.atalk.android.util.javax.swing.GrayFilter;
import org.atalk.android.util.javax.swing.Icon;
import org.atalk.android.util.javax.swing.ImageIcon;
import org.atalk.android.util.javax.swing.JFrame;

public class Utility {
   public static MediaLocator appArgToMediaLocator(String var0) {
      MediaLocator var1;
      try {
         var1 = new MediaLocator(new URL(var0));
         return var1;
      } catch (MalformedURLException var4) {
         try {
            var1 = new MediaLocator(fileToURL(var0));
            return var1;
         } catch (MalformedURLException var2) {
         } catch (IOException var3) {
         }

         return new MediaLocator(var0);
      }
   }

   public static MediaLocator appletArgToMediaLocator(Applet var0, String var1) {
      try {
         MediaLocator var2 = new MediaLocator(new URL(var1));
         return var2;
      } catch (MalformedURLException var4) {
         try {
            MediaLocator var5 = new MediaLocator(new URL(var0.getDocumentBase(), var1));
            return var5;
         } catch (MalformedURLException var3) {
            return new MediaLocator(var1);
         }
      }
   }

   public static Icon createDisabledIcon(ImageIcon var0) {
      return new ImageIcon(GrayFilter.createDisabledImage(var0.getImage()));
   }

   public static URL fileToURL(String var0) throws IOException, MalformedURLException {
      File var1 = new File(var0);
      if (var1.exists()) {
         StringBuilder var2 = new StringBuilder();
         var2.append("file:///");
         var2.append(var1.getCanonicalPath());
         return new URL(var2.toString());
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("File ");
         var3.append(var0);
         var3.append(" does not exist.");
         throw new IOException(var3.toString());
      }
   }

   public static String getExtension(File var0) {
      return getExtension(var0.getName());
   }

   public static String getExtension(String var0) {
      Object var3 = null;
      int var1 = var0.lastIndexOf(46);
      String var2 = (String)var3;
      if (var1 > 0) {
         var2 = (String)var3;
         if (var1 < var0.length() - 1) {
            var2 = var0.substring(var1 + 1).toLowerCase();
         }
      }

      return var2;
   }

   public static Time getMaximumLatency(Controller[] var0) {
      Time var2 = new Time(0.0D);

      Time var3;
      for(int var1 = 0; var1 < var0.length; var2 = var3) {
         var3 = var2;
         if (var0[var1].getState() >= 300) {
            Time var4 = var0[var1].getStartLatency();
            if (var4 == Controller.LATENCY_UNKNOWN) {
               var3 = var2;
            } else {
               var3 = var2;
               if (var4.getSeconds() > 0.0D) {
                  var3 = var4;
               }
            }
         }

         ++var1;
      }

      return var2;
   }

   public static int pickAMaster(Player[] var0) {
      for(int var1 = 0; var1 < var0.length; ++var1) {
         GainControl var2 = var0[var1].getGainControl();
         if (var2 != null && var2.getControlComponent() != null) {
            return var1;
         }
      }

      return 0;
   }

   public static void showControls(Controller var0) {
      Control[] var4 = var0.getControls();

      for(int var1 = 0; var1 < var4.length; ++var1) {
         Component var2 = var4[var1].getControlComponent();
         if (var2 != null && !var2.isShowing()) {
            JFrame var3 = new JFrame(var4[var1].getClass().getName());
            var3.getContentPane().add(var2);
            var3.pack();
            var3.setVisible(true);
         }
      }

   }

   public static String stateToString(int var0) {
      if (var0 != 100) {
         if (var0 != 200) {
            if (var0 != 300) {
               if (var0 != 400) {
                  if (var0 != 500) {
                     return var0 != 600 ? null : "Started";
                  } else {
                     return "Prefetched";
                  }
               } else {
                  return "Prefetching";
               }
            } else {
               return "Realized";
            }
         } else {
            return "Realizing";
         }
      } else {
         return "Unrealized";
      }
   }
}
