package net.sf.fmj.ejmf.toolkit.media;

import javax.media.CachingControl;
import javax.media.CachingControlEvent;
import org.atalk.android.util.java.awt.Component;
import org.atalk.android.util.java.awt.event.ActionEvent;
import org.atalk.android.util.java.awt.event.ActionListener;
import org.atalk.android.util.javax.swing.JButton;
import org.atalk.android.util.javax.swing.JProgressBar;

public class BasicCachingControl implements CachingControl {
   private static final String PAUSEMESSAGE = "Pause";
   private static final String RESUMEMESSAGE = "Resume";
   private AbstractController controller;
   private boolean isDownloading;
   private boolean isPaused;
   private long length;
   private JButton pauseButton;
   private long progress;
   private JProgressBar progressBar;

   public BasicCachingControl(AbstractController var1, long var2) {
      this.controller = var1;
      JProgressBar var4 = new JProgressBar();
      this.progressBar = var4;
      var4.setMinimum(0);
      JButton var5 = new JButton("Pause");
      this.pauseButton = var5;
      var5.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            BasicCachingControl.this.pauseButton.getText();
            if (BasicCachingControl.this.isPaused()) {
               BasicCachingControl.this.pauseButton.setText("Pause");
               BasicCachingControl.this.setPaused(false);
            } else {
               BasicCachingControl.this.pauseButton.setText("Resume");
               BasicCachingControl.this.setPaused(true);
            }

            BasicCachingControl.this.pauseButton.getParent().validate();
         }
      });
      this.reset(var2);
      this.controller.addControl(this);
   }

   public void addToProgress(long var1) {
      this.setContentProgress(this.progress + var1);
   }

   public void blockWhilePaused() {
      // $FF: Couldn't be decompiled
   }

   public long getContentLength() {
      return this.length;
   }

   public long getContentProgress() {
      return this.progress;
   }

   public Component getControlComponent() {
      return this.pauseButton;
   }

   public Component getProgressBarComponent() {
      return this.progressBar;
   }

   public boolean isDownloading() {
      return this.isDownloading;
   }

   public boolean isPaused() {
      return this.isPaused;
   }

   public void reset(long var1) {
      synchronized(this){}

      try {
         this.length = var1;
         this.progress = 0L;
         this.progressBar.setValue(0);
         this.setContentLength(var1);
         this.setDownLoading(false);
         this.setPaused(false);
         this.controller.postEvent(new CachingControlEvent(this.controller, this, this.progress));
      } finally {
         ;
      }

   }

   public void setContentLength(long var1) {
      synchronized(this){}

      Throwable var10000;
      label113: {
         boolean var10001;
         try {
            this.length = var1;
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label113;
         }

         if (var1 == Long.MAX_VALUE) {
            try {
               this.progressBar.setMaximum(0);
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label113;
            }
         } else {
            try {
               this.progressBar.setMaximum((int)var1);
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label113;
            }
         }

         return;
      }

      Throwable var3 = var10000;
      throw var3;
   }

   public void setContentProgress(long var1) {
      synchronized(this){}

      Throwable var10000;
      label80: {
         boolean var10001;
         boolean var3;
         label79: {
            label78: {
               try {
                  this.blockWhilePaused();
                  this.progress = var1;
                  if (var1 < this.length) {
                     break label78;
                  }
               } catch (Throwable var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label80;
               }

               var3 = false;
               break label79;
            }

            var3 = true;
         }

         label72:
         try {
            this.setDownLoading(var3);
            this.progressBar.setValue((int)var1);
            this.controller.postEvent(new CachingControlEvent(this.controller, this, var1));
            return;
         } catch (Throwable var9) {
            var10000 = var9;
            var10001 = false;
            break label72;
         }
      }

      Throwable var4 = var10000;
      throw var4;
   }

   public void setDone() {
      this.setContentProgress(this.length);
   }

   public void setDownLoading(boolean var1) {
      this.isDownloading = var1;
      if (!var1) {
         this.setPaused(false);
      }

   }

   protected void setPaused(boolean var1) {
      synchronized(this){}

      try {
         this.isPaused = var1;
         this.notifyAll();
      } finally {
         ;
      }

   }
}
