package net.sf.fmj.media.renderer.video;

import java.util.logging.Logger;
import javax.media.Buffer;
import javax.media.Format;
import javax.media.format.RGBFormat;
import javax.media.format.VideoFormat;
import javax.media.renderer.VideoRenderer;
import net.sf.fmj.media.AbstractVideoRenderer;
import net.sf.fmj.media.util.BufferToImage;
import net.sf.fmj.utility.FPSCounter;
import net.sf.fmj.utility.LoggerSingleton;
import org.atalk.android.util.java.awt.Component;
import org.atalk.android.util.java.awt.Dimension;
import org.atalk.android.util.java.awt.Graphics;
import org.atalk.android.util.java.awt.Image;
import org.atalk.android.util.java.awt.Rectangle;
import org.atalk.android.util.java.awt.image.BufferedImage;
import org.atalk.android.util.java.awt.image.ImageObserver;
import org.atalk.android.util.java.awt.image.ImagingOpException;
import org.atalk.android.util.javax.swing.JComponent;

public class SimpleSwingRenderer extends AbstractVideoRenderer implements VideoRenderer {
   private static final boolean PAINT_IMMEDIATELY = false;
   private static final boolean TRACE_FPS = false;
   private static final Logger logger;
   private BufferToImage bufferToImage;
   private SimpleSwingRenderer.SwingVideoComponent component;
   private Object[] controls;
   private final FPSCounter fpsCounter;
   private final Format[] supportedInputFormats;

   static {
      logger = LoggerSingleton.logger;
   }

   public SimpleSwingRenderer() {
      this.supportedInputFormats = new Format[]{new RGBFormat((Dimension)null, -1, Format.intArray, -1.0F, 32, 16711680, 65280, 255, 1, -1, 0, -1), new RGBFormat((Dimension)null, -1, Format.intArray, -1.0F, 32, 255, 65280, 16711680, 1, -1, 0, -1), new RGBFormat((Dimension)null, -1, Format.byteArray, -1.0F, 32, 1, 2, 3), new RGBFormat((Dimension)null, -1, Format.byteArray, -1.0F, 32, 3, 2, 1), new RGBFormat((Dimension)null, -1, Format.byteArray, -1.0F, 24, 1, 2, 3), new RGBFormat((Dimension)null, -1, Format.byteArray, -1.0F, 24, 3, 2, 1), new RGBFormat((Dimension)null, -1, Format.shortArray, -1.0F, 16, -1, -1, -1, 1, -1, 0, -1), new RGBFormat((Dimension)null, -1, Format.byteArray, -1.0F, 8, -1, -1, -1, 1, -1, 0, -1)};
      this.component = new SimpleSwingRenderer.SwingVideoComponent();
      this.controls = new Object[]{this};
      this.fpsCounter = new FPSCounter();
   }

   public int doProcess(Buffer var1) {
      if (var1.isEOM()) {
         Logger var4 = logger;
         StringBuilder var2 = new StringBuilder();
         var2.append(this.getClass().getSimpleName());
         var2.append("passed buffer with EOM flag");
         var4.warning(var2.toString());
         return 0;
      } else if (var1.getData() == null) {
         return 1;
      } else {
         Image var3 = this.bufferToImage.createImage(var1);
         this.component.setImage(var3);
         return 0;
      }
   }

   public Component getComponent() {
      return this.component;
   }

   public Object[] getControls() {
      return this.controls;
   }

   public String getName() {
      return "Simple Swing Renderer";
   }

   public Format[] getSupportedInputFormats() {
      return this.supportedInputFormats;
   }

   public Format setInputFormat(Format var1) {
      this.bufferToImage = new BufferToImage((VideoFormat)var1);
      return super.setInputFormat(var1);
   }

   private class SwingVideoComponent extends JComponent {
      private BufferedImage biCompatible;
      private Image image;
      private boolean scale = true;

      public SwingVideoComponent() {
         this.setDoubleBuffered(false);
      }

      private BufferedImage getCompatibleBufferedImage() {
         BufferedImage var1 = this.biCompatible;
         if (var1 == null || var1.getWidth() != this.image.getWidth((ImageObserver)null) || this.biCompatible.getHeight() != this.image.getHeight((ImageObserver)null)) {
            this.biCompatible = this.getGraphicsConfiguration().createCompatibleImage(this.image.getWidth((ImageObserver)null), this.image.getHeight((ImageObserver)null));
         }

         return this.biCompatible;
      }

      private Rectangle getVideoRect(boolean var1) {
         Dimension var6 = this.getPreferredSize();
         Dimension var7 = this.getSize();
         int var2;
         int var3;
         int var4;
         int var5;
         if (!var1) {
            if (var6.width <= var7.width) {
               var4 = (var7.width - var6.width) / 2;
               var5 = var6.width;
            } else {
               var4 = 0;
               var5 = var6.width;
            }

            if (var6.height <= var7.height) {
               var2 = (var7.height - var6.height) / 2;
               var3 = var6.height;
            } else {
               var2 = 0;
               var3 = var6.height;
            }
         } else if ((float)var7.width / (float)var6.width < (float)var7.height / (float)var6.height) {
            var5 = var7.width;
            var3 = var7.width * var6.height / var6.width;
            var4 = 0;
            var2 = (var7.height - var3) / 2;
         } else {
            var5 = var7.height * var6.width / var6.height;
            var3 = var7.height;
            var4 = (var7.width - var5) / 2;
            var2 = 0;
         }

         return new Rectangle(var4, var2, var5, var3);
      }

      public Dimension getPreferredSize() {
         return SimpleSwingRenderer.this.inputFormat == null ? super.getPreferredSize() : ((VideoFormat)SimpleSwingRenderer.this.inputFormat).getSize();
      }

      public void paint(Graphics var1) {
         if (this.image != null) {
            Rectangle var2 = this.getVideoRect(this.scale);

            try {
               if (this.biCompatible == null) {
                  var1.drawImage(this.image, var2.x, var2.y, var2.width, var2.height, (ImageObserver)null);
                  return;
               }
            } catch (ImagingOpException var5) {
            }

            this.getCompatibleBufferedImage();
            Graphics var3 = this.biCompatible.getGraphics();
            Image var4 = this.image;
            var3.drawImage(var4, 0, 0, var4.getWidth((ImageObserver)null), this.image.getHeight((ImageObserver)null), (ImageObserver)null);
            var1.drawImage(this.biCompatible, var2.x, var2.y, var2.width, var2.height, (ImageObserver)null);
         }

      }

      public void setImage(Image var1) {
         this.image = var1;
         this.repaint();
      }

      public void update(Graphics var1) {
         this.paint(var1);
      }
   }
}
