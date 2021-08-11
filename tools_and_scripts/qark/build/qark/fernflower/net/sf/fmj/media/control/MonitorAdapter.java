package net.sf.fmj.media.control;

import javax.media.Buffer;
import javax.media.Format;
import javax.media.Owned;
import javax.media.control.MonitorControl;
import javax.media.format.AudioFormat;
import javax.media.format.UnsupportedFormatException;
import javax.media.format.VideoFormat;
import net.sf.fmj.media.CircularBuffer;
import net.sf.fmj.media.Log;
import net.sf.fmj.media.util.AudioCodecChain;
import net.sf.fmj.media.util.CodecChain;
import net.sf.fmj.media.util.LoopThread;
import net.sf.fmj.media.util.VideoCodecChain;
import org.atalk.android.util.java.awt.BorderLayout;
import org.atalk.android.util.java.awt.Checkbox;
import org.atalk.android.util.java.awt.Color;
import org.atalk.android.util.java.awt.Component;
import org.atalk.android.util.java.awt.MenuItem;
import org.atalk.android.util.java.awt.Panel;
import org.atalk.android.util.java.awt.PopupMenu;
import org.atalk.android.util.java.awt.event.ActionEvent;
import org.atalk.android.util.java.awt.event.ActionListener;
import org.atalk.android.util.java.awt.event.ItemEvent;
import org.atalk.android.util.java.awt.event.ItemListener;
import org.atalk.android.util.java.awt.event.MouseAdapter;
import org.atalk.android.util.java.awt.event.MouseEvent;
import org.atalk.android.util.java.awt.event.MouseListener;

public class MonitorAdapter implements MonitorControl, Owned {
   static VideoFormat mpegVideo = new VideoFormat("mpeg/rtp");
   CircularBuffer bufferQ;
   protected Checkbox cbEnabled = null;
   // $FF: renamed from: cc net.sf.fmj.media.util.CodecChain
   protected CodecChain field_43 = null;
   protected boolean closed = false;
   protected Component controlComponent = null;
   protected boolean enabled = false;
   protected Format format = null;
   protected int[] frameRates = new int[]{0, 1, 2, 5, 7, 10, 15, 20, 30, 60, 90};
   protected float inFrameRate = 0.0F;
   protected long lastPreviewTime = 0L;
   protected LoopThread loopThread;
   // $FF: renamed from: ml org.atalk.android.util.java.awt.event.MouseListener
   protected MouseListener field_44 = null;
   Object owner;
   protected float previewFrameRate = 30.0F;
   protected long previewInterval = 33333333L;
   protected PopupMenu rateMenu = null;
   protected Component visualComponent = null;

   public MonitorAdapter(Format var1, Object var2) {
      this.format = var1;
      this.owner = var2;
   }

   private void addPopupMenu(Component var1) {
      this.visualComponent = var1;
      this.rateMenu = new PopupMenu("Monitor Rate");
      ActionListener var4 = new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            String var5 = var1.getActionCommand();
            var5 = var5.substring(0, var5.indexOf(" "));

            try {
               int var2 = Integer.parseInt(var5);
               MonitorAdapter.this.setPreviewFrameRate((float)var2);
            } catch (Throwable var4) {
               if (!(var4 instanceof ThreadDeath)) {
                  return;
               } else {
                  throw (ThreadDeath)var4;
               }
            }
         }
      };
      var1.add(this.rateMenu);
      int var3 = 0;
      int var2 = 0;

      while(true) {
         int[] var5 = this.frameRates;
         StringBuilder var7;
         MenuItem var8;
         if (var2 >= var5.length) {
            if ((float)var3 < this.inFrameRate) {
               var7 = new StringBuilder();
               var7.append(this.inFrameRate);
               var7.append(" fps");
               var8 = new MenuItem(var7.toString());
               this.rateMenu.add(var8);
               var8.addActionListener(var4);
            }

            MouseAdapter var6 = new MouseAdapter() {
               public void mouseClicked(MouseEvent var1) {
                  if (var1.isPopupTrigger()) {
                     MonitorAdapter.this.rateMenu.show(MonitorAdapter.this.visualComponent, var1.getX(), var1.getY());
                  }

               }

               public void mousePressed(MouseEvent var1) {
                  if (var1.isPopupTrigger()) {
                     MonitorAdapter.this.rateMenu.show(MonitorAdapter.this.visualComponent, var1.getX(), var1.getY());
                  }

               }

               public void mouseReleased(MouseEvent var1) {
                  if (var1.isPopupTrigger()) {
                     MonitorAdapter.this.rateMenu.show(MonitorAdapter.this.visualComponent, var1.getX(), var1.getY());
                  }

               }
            };
            this.field_44 = var6;
            var1.addMouseListener(var6);
            return;
         }

         if ((float)var5[var2] < this.inFrameRate) {
            var7 = new StringBuilder();
            var7.append(this.frameRates[var2]);
            var7.append(" fps");
            var8 = new MenuItem(var7.toString());
            this.rateMenu.add(var8);
            var8.addActionListener(var4);
            var3 = this.frameRates[var2];
         }

         ++var2;
      }
   }

   private Object copyData(Object var1) {
      if (var1 instanceof byte[]) {
         byte[] var4 = new byte[((byte[])((byte[])var1)).length];
         System.arraycopy(var1, 0, var4, 0, var4.length);
         return var4;
      } else if (var1 instanceof short[]) {
         short[] var3 = new short[((short[])((short[])var1)).length];
         System.arraycopy(var1, 0, var3, 0, var3.length);
         return var3;
      } else if (var1 instanceof int[]) {
         int[] var2 = new int[((int[])((int[])var1)).length];
         System.arraycopy(var1, 0, var2, 0, var2.length);
         return var2;
      } else {
         return var1;
      }
   }

   public void close() {
      // $FF: Couldn't be decompiled
   }

   public boolean doProcess() {
      // $FF: Couldn't be decompiled
   }

   public void finalize() {
      Component var1 = this.visualComponent;
      if (var1 != null) {
         var1.remove(this.rateMenu);
         this.visualComponent.removeMouseListener(this.field_44);
      }

   }

   public Component getControlComponent() {
      Component var1 = this.controlComponent;
      if (var1 != null) {
         return var1;
      } else if (this.field_43 == null && !this.open()) {
         return null;
      } else {
         var1 = this.field_43.getControlComponent();
         this.controlComponent = var1;
         Checkbox var2;
         Panel var3;
         if (this.format instanceof AudioFormat && var1 != null) {
            var3 = new Panel();
            var3.setLayout(new BorderLayout());
            var2 = new Checkbox("Monitor Audio");
            this.cbEnabled = var2;
            var3.add("West", var2);
            var3.add("Center", this.controlComponent);
            this.controlComponent = var3;
            var3.setBackground(Color.lightGray);
         }

         if (this.format instanceof VideoFormat && this.controlComponent != null) {
            var3 = new Panel();
            var3.setLayout(new BorderLayout());
            var2 = new Checkbox("Monitor Video");
            this.cbEnabled = var2;
            var3.add("South", var2);
            var3.add("Center", this.controlComponent);
            this.addPopupMenu(this.controlComponent);
            this.controlComponent = var3;
            var3.setBackground(Color.lightGray);
         }

         Checkbox var4 = this.cbEnabled;
         if (var4 != null) {
            var4.setState(this.isEnabled());
            this.cbEnabled.addItemListener(new ItemListener() {
               public void itemStateChanged(ItemEvent var1) {
                  MonitorAdapter var2 = MonitorAdapter.this;
                  var2.setEnabled(var2.cbEnabled.getState());
               }
            });
         }

         return this.controlComponent;
      }
   }

   public Object getOwner() {
      return this.owner;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   protected boolean open() {
      label46: {
         UnsupportedFormatException var10000;
         label49: {
            float var1;
            boolean var10001;
            label44: {
               try {
                  if (this.format instanceof VideoFormat) {
                     VideoFormat var2 = (VideoFormat)this.format;
                     this.field_43 = new VideoCodecChain(var2);
                     var1 = var2.getFrameRate();
                     this.inFrameRate = var1;
                     break label44;
                  }
               } catch (UnsupportedFormatException var7) {
                  var10000 = var7;
                  var10001 = false;
                  break label49;
               }

               try {
                  if (this.format instanceof AudioFormat) {
                     this.field_43 = new AudioCodecChain((AudioFormat)this.format);
                  }
                  break label46;
               } catch (UnsupportedFormatException var6) {
                  var10000 = var6;
                  var10001 = false;
                  break label49;
               }
            }

            if (var1 < 0.0F) {
               try {
                  this.inFrameRate = 30.0F;
               } catch (UnsupportedFormatException var5) {
                  var10000 = var5;
                  var10001 = false;
                  break label49;
               }
            }

            try {
               this.inFrameRate = (float)((int)((double)(this.inFrameRate * 10.0F) + 0.5D)) / 10.0F;
               break label46;
            } catch (UnsupportedFormatException var4) {
               var10000 = var4;
               var10001 = false;
            }
         }

         UnsupportedFormatException var8 = var10000;
         StringBuilder var3 = new StringBuilder();
         var3.append("Failed to initialize the monitor control: ");
         var3.append(var8);
         Log.warning(var3.toString());
         return false;
      }

      if (this.field_43 == null) {
         return false;
      } else {
         this.bufferQ = new CircularBuffer(2);
         this.loopThread = new MonitorThread(this);
         return true;
      }
   }

   public void process(Buffer param1) {
      // $FF: Couldn't be decompiled
   }

   public void reset() {
      CodecChain var1 = this.field_43;
      if (var1 != null) {
         var1.reset();
      }

   }

   public boolean setEnabled(boolean param1) {
      // $FF: Couldn't be decompiled
   }

   public float setPreviewFrameRate(float var1) {
      float var2 = var1;
      if (var1 > this.inFrameRate) {
         var2 = this.inFrameRate;
      }

      this.previewFrameRate = var2;
      this.previewInterval = (long)(1.0E9D / (double)var2);
      return var2;
   }
}
