package net.sf.fmj.media.renderer.video;

import javax.media.Buffer;
import javax.media.Format;
import javax.media.ResourceUnavailableException;
import javax.media.format.RGBFormat;
import javax.media.renderer.VideoRenderer;
import org.atalk.android.util.java.awt.Component;
import org.atalk.android.util.java.awt.Dimension;
import org.atalk.android.util.java.awt.Rectangle;
import org.atalk.android.util.java.awt.image.BufferedImage;

public class Java2dRenderer implements VideoRenderer {
   private Rectangle bounds = new Rectangle(0, 0, 10, 10);
   private BufferedImage bufferedImage;
   private JVideoComponent component;
   private RGBFormat inputFormat;
   private String name = "Java2D Video Renderer";
   private Format[] supportedFormats;

   public Java2dRenderer() {
      this.supportedFormats = new Format[]{new RGBFormat((Dimension)null, -1, Format.intArray, -1.0F, 32, 16711680, 65280, 255, 1, -1, 0, -1), new RGBFormat((Dimension)null, -1, Format.intArray, -1.0F, 32, 255, 65280, 16711680, 1, -1, 0, -1)};
   }

   private void createImage() {
      RGBFormat var2 = this.inputFormat;
      if (var2 != null) {
         Dimension var3 = var2.getSize();
         if (var3 != null) {
            byte var1;
            if (this.inputFormat.getRedMask() == 255) {
               var1 = 4;
            } else {
               var1 = 1;
            }

            this.bufferedImage = new BufferedImage(var3.width, var3.height, var1);
         }
      }
   }

   public void close() {
      synchronized(this){}

      try {
         this.bufferedImage = null;
      } finally {
         ;
      }

   }

   public Rectangle getBounds() {
      return this.bounds;
   }

   public Component getComponent() {
      if (this.component == null) {
         this.component = new JVideoComponent();
      }

      return this.component;
   }

   public Object getControl(String var1) {
      return null;
   }

   public Object[] getControls() {
      return new Object[0];
   }

   public String getName() {
      return this.name;
   }

   public Format[] getSupportedInputFormats() {
      return this.supportedFormats;
   }

   public void open() throws ResourceUnavailableException {
      this.createImage();
   }

   public int process(Buffer param1) {
      // $FF: Couldn't be decompiled
   }

   public void reset() {
   }

   public void setBounds(Rectangle var1) {
      this.bounds.setBounds(var1);
   }

   public boolean setComponent(Component var1) {
      return false;
   }

   public Format setInputFormat(Format var1) {
      int var2 = 0;

      while(true) {
         Format[] var3 = this.supportedFormats;
         if (var2 >= var3.length) {
            return null;
         }

         if (var1.matches(var3[var2])) {
            RGBFormat var4 = (RGBFormat)var1;
            this.inputFormat = var4;
            Dimension var5 = var4.getSize();
            if (var5 != null) {
               this.bounds.setSize(var5);
            }

            this.getComponent().setPreferredSize(var5);
            return var1;
         }

         ++var2;
      }
   }

   public void start() {
   }

   public void stop() {
   }
}
