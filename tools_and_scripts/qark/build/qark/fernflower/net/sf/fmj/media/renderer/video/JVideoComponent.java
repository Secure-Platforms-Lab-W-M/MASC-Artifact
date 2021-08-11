package net.sf.fmj.media.renderer.video;

import org.atalk.android.util.java.awt.Dimension;
import org.atalk.android.util.java.awt.Graphics;
import org.atalk.android.util.java.awt.Graphics2D;
import org.atalk.android.util.java.awt.Image;
import org.atalk.android.util.java.awt.image.ImageObserver;
import org.atalk.android.util.javax.swing.JComponent;

public final class JVideoComponent extends JComponent {
   private Image image;
   private boolean scaleKeepAspectRatio = false;

   public JVideoComponent() {
      this.setDoubleBuffered(false);
      this.setOpaque(false);
   }

   public Image getImage() {
      return this.image;
   }

   protected void paintComponent(Graphics var1) {
      Graphics2D var6 = (Graphics2D)var1;
      Dimension var8;
      if (this.scaleKeepAspectRatio) {
         if (this.image != null) {
            var8 = this.getPreferredSize();
            Dimension var7 = this.getSize();
            int var2;
            int var3;
            int var4;
            int var5;
            if ((float)var7.width / (float)var8.width < (float)var7.height / (float)var8.height) {
               var2 = var7.width;
               var3 = var7.width * var8.height / var8.width;
               var4 = 0;
               var5 = (var7.height - var3) / 2;
            } else {
               var2 = var7.height * var8.width / var8.height;
               var3 = var7.height;
               var4 = (var7.width - var2) / 2;
               var5 = 0;
            }

            var1.drawImage(this.image, var4, var5, var2, var3, (ImageObserver)null);
            return;
         }
      } else if (this.image != null) {
         var8 = this.getSize();
         var1.drawImage(this.image, 0, 0, var8.width, var8.height, (ImageObserver)null);
      }

   }

   public void setImage(Image var1) {
      this.image = var1;
      this.repaint();
   }
}
