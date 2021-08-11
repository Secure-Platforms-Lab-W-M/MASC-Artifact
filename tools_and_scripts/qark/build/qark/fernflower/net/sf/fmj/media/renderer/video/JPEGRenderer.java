package net.sf.fmj.media.renderer.video;

import com.lti.utils.StringUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.Buffer;
import javax.media.Format;
import javax.media.format.JPEGFormat;
import javax.media.format.VideoFormat;
import javax.media.renderer.VideoRenderer;
import net.sf.fmj.media.AbstractVideoRenderer;
import net.sf.fmj.utility.LoggerSingleton;
import org.atalk.android.util.java.awt.Component;
import org.atalk.android.util.java.awt.image.BufferedImage;
import org.atalk.android.util.javax.imageio.ImageIO;

public class JPEGRenderer extends AbstractVideoRenderer implements VideoRenderer {
   private static final Logger logger;
   private JVideoComponent component = new JVideoComponent();
   private Object[] controls = new Object[]{this};
   private boolean scale;
   private final Format[] supportedInputFormats = new Format[]{new JPEGFormat()};

   static {
      logger = LoggerSingleton.logger;
   }

   public int doProcess(Buffer var1) {
      if (var1.isEOM()) {
         Logger var8 = logger;
         StringBuilder var11 = new StringBuilder();
         var11.append(this.getClass().getSimpleName());
         var11.append("passed buffer with EOM flag");
         var8.warning(var11.toString());
         return 0;
      } else {
         Logger var2;
         StringBuilder var13;
         if (var1.getData() == null) {
            var2 = logger;
            var13 = new StringBuilder();
            var13.append("buffer.getData() == null, eom=");
            var13.append(var1.isEOM());
            var2.warning(var13.toString());
            return 1;
         } else if (var1.getLength() == 0) {
            var2 = logger;
            var13 = new StringBuilder();
            var13.append("buffer.getLength() == 0, eom=");
            var13.append(var1.isEOM());
            var2.warning(var13.toString());
            return 1;
         } else if (var1.isDiscard()) {
            logger.warning("JPEGRenderer passed buffer with discard flag");
            return 1;
         } else {
            Logger var3;
            Level var4;
            StringBuilder var5;
            BufferedImage var9;
            Level var10;
            StringBuilder var12;
            try {
               var9 = ImageIO.read(new ByteArrayInputStream((byte[])((byte[])var1.getData()), var1.getOffset(), var1.getLength()));
            } catch (IOException var7) {
               var3 = logger;
               var4 = Level.WARNING;
               var5 = new StringBuilder();
               var5.append("");
               var5.append(var7);
               var3.log(var4, var5.toString(), var7);
               var2 = logger;
               var10 = Level.WARNING;
               var12 = new StringBuilder();
               var12.append("data: ");
               var12.append(StringUtils.byteArrayToHexString((byte[])((byte[])var1.getData()), var1.getLength(), var1.getOffset()));
               var2.log(var10, var12.toString());
               return 1;
            }

            if (var9 == null) {
               logger.log(Level.WARNING, "Failed to read image (ImageIO.read returned null).");
               var2 = logger;
               var10 = Level.WARNING;
               var12 = new StringBuilder();
               var12.append("data: ");
               var12.append(StringUtils.byteArrayToHexString((byte[])((byte[])var1.getData()), var1.getLength(), var1.getOffset()));
               var2.log(var10, var12.toString());
               return 1;
            } else {
               try {
                  this.component.setImage(var9);
                  return 0;
               } catch (Exception var6) {
                  var3 = logger;
                  var4 = Level.WARNING;
                  var5 = new StringBuilder();
                  var5.append("");
                  var5.append(var6);
                  var3.log(var4, var5.toString(), var6);
                  var2 = logger;
                  var10 = Level.WARNING;
                  var12 = new StringBuilder();
                  var12.append("data: ");
                  var12.append(StringUtils.byteArrayToHexString((byte[])((byte[])var1.getData()), var1.getLength(), var1.getOffset()));
                  var2.log(var10, var12.toString());
                  return 1;
               }
            }
         }
      }
   }

   public Component getComponent() {
      return this.component;
   }

   public Object[] getControls() {
      return this.controls;
   }

   public String getName() {
      return "JPEG Renderer";
   }

   public Format[] getSupportedInputFormats() {
      return this.supportedInputFormats;
   }

   public Format setInputFormat(Format var1) {
      VideoFormat var2 = (VideoFormat)super.setInputFormat(var1);
      if (var2 != null) {
         this.getComponent().setPreferredSize(var2.getSize());
      }

      return var2;
   }
}
