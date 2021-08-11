package net.sf.fmj.media.rtp;

import java.util.Vector;
import javax.media.Format;
import javax.media.control.BufferControl;
import javax.media.format.AudioFormat;
import javax.media.format.VideoFormat;
import org.atalk.android.util.java.awt.BorderLayout;
import org.atalk.android.util.java.awt.Button;
import org.atalk.android.util.java.awt.Choice;
import org.atalk.android.util.java.awt.Component;
import org.atalk.android.util.java.awt.FlowLayout;
import org.atalk.android.util.java.awt.Label;
import org.atalk.android.util.java.awt.Panel;
import org.atalk.android.util.java.awt.TextField;
import org.atalk.android.util.java.awt.event.ActionEvent;
import org.atalk.android.util.java.awt.event.ActionListener;
import org.atalk.android.util.java.awt.event.ItemEvent;
import org.atalk.android.util.java.awt.event.ItemListener;

public class BufferControlImpl implements BufferControl {
   private static final int AUDIO_DEFAULT_BUFFER = 250;
   private static final int AUDIO_DEFAULT_THRESHOLD = 125;
   private static final int AUDIO_MAX_BUFFER = 4000;
   private static final int AUDIO_MAX_THRESHOLD = 2000;
   private static final int NOT_SPECIFIED = Integer.MAX_VALUE;
   private static final int VIDEO_DEFAULT_BUFFER = 135;
   private static final int VIDEO_DEFAULT_THRESHOLD = 0;
   private static final int VIDEO_MAX_BUFFER = 4000;
   private static final int VIDEO_MAX_THRESHOLD = 0;
   BufferControlImpl.BufferControlPanel controlComp = null;
   private long currBuffer = 2147483647L;
   private long currThreshold = 2147483647L;
   private long defBuffer = 2147483647L;
   private long defThreshold = 2147483647L;
   private boolean inited = false;
   private long maxBuffer = 2147483647L;
   private long maxThreshold = 2147483647L;
   private Vector sourcestreamlist = new Vector(1);
   boolean threshold_enabled = true;

   protected void addSourceStream(RTPSourceStream var1) {
      this.sourcestreamlist.addElement(var1);
      var1.setBufferControl(this);
   }

   public long getBufferLength() {
      return this.currBuffer;
   }

   public Component getControlComponent() {
      if (this.controlComp == null) {
         this.controlComp = new BufferControlImpl.BufferControlPanel();
      }

      return this.controlComp;
   }

   public boolean getEnabledThreshold() {
      return this.threshold_enabled;
   }

   public long getMinimumThreshold() {
      return this.currThreshold;
   }

   protected void initBufferControl(Format var1) {
      boolean var2 = var1 instanceof AudioFormat;
      long var3 = 4000L;
      long var5;
      if (var2) {
         if (this.defBuffer != 2147483647L) {
            var5 = this.currBuffer;
         } else {
            var5 = 250L;
         }

         this.defBuffer = var5;
         if (this.defThreshold != 2147483647L) {
            var5 = this.currThreshold;
         } else {
            var5 = 125L;
         }

         this.defThreshold = var5;
         var5 = this.maxBuffer;
         if (var5 != 2147483647L) {
            var3 = var5;
         }

         this.maxBuffer = var3;
         var3 = this.maxThreshold;
         if (var3 == 2147483647L) {
            var3 = 2000L;
         }

         this.maxThreshold = var3;
         var3 = this.currBuffer;
         if (var3 == 2147483647L) {
            var3 = this.defBuffer;
         }

         this.currBuffer = var3;
         var3 = this.currThreshold;
         if (var3 == 2147483647L) {
            var3 = this.defThreshold;
         }

         this.currThreshold = var3;
      } else if (var1 instanceof VideoFormat) {
         if (this.defBuffer != 2147483647L) {
            var5 = this.currBuffer;
         } else {
            var5 = 135L;
         }

         this.defBuffer = var5;
         var5 = this.defThreshold;
         long var7 = 0L;
         if (var5 != 2147483647L) {
            var5 = this.currThreshold;
         } else {
            var5 = 0L;
         }

         this.defThreshold = var5;
         var5 = this.maxBuffer;
         if (var5 != 2147483647L) {
            var3 = var5;
         }

         this.maxBuffer = var3;
         var5 = this.maxThreshold;
         var3 = var7;
         if (var5 != 2147483647L) {
            var3 = var5;
         }

         this.maxThreshold = var3;
         var3 = this.currBuffer;
         if (var3 == 2147483647L) {
            var3 = this.defBuffer;
         }

         this.currBuffer = var3;
         var3 = this.currThreshold;
         if (var3 == 2147483647L) {
            var3 = this.defThreshold;
         }

         this.currThreshold = var3;
      }

      if (this.currBuffer == -2L) {
         this.currBuffer = this.maxBuffer;
      }

      if (this.currBuffer == -1L) {
         this.currBuffer = this.defBuffer;
      }

      if (this.currThreshold == -2L) {
         this.currThreshold = this.maxThreshold;
      }

      if (this.currThreshold == -1L) {
         this.currThreshold = this.defThreshold;
      }

      BufferControlImpl.BufferControlPanel var9 = this.controlComp;
      if (var9 != null) {
         var9.updateBuffer(this.currBuffer);
         this.controlComp.updateThreshold(this.currThreshold);
      }

      this.inited = true;
   }

   protected void removeSourceStream(RTPSourceStream var1) {
      this.sourcestreamlist.removeElement(var1);
   }

   public long setBufferLength(long var1) {
      if (!this.inited) {
         this.currBuffer = var1;
         return var1;
      } else {
         long var4 = var1;
         if (var1 == -1L) {
            var4 = this.defBuffer;
         }

         var1 = var4;
         if (var4 == -2L) {
            var1 = this.maxBuffer;
         }

         if (var1 < this.currThreshold) {
            return this.currBuffer;
         } else {
            var4 = this.maxBuffer;
            if (var1 >= var4) {
               this.currBuffer = var4;
            } else if (var1 > 0L && var1 != this.defBuffer) {
               this.currBuffer = var1;
            } else {
               this.currBuffer = this.defBuffer;
            }

            for(int var3 = 0; var3 < this.sourcestreamlist.size(); ++var3) {
               ((RTPSourceStream)this.sourcestreamlist.elementAt(var3)).updateBuffer(this.currBuffer);
            }

            BufferControlImpl.BufferControlPanel var6 = this.controlComp;
            if (var6 != null) {
               var6.updateBuffer(this.currBuffer);
            }

            return this.currBuffer;
         }
      }
   }

   public void setEnabledThreshold(boolean var1) {
      this.threshold_enabled = var1;
   }

   public long setMinimumThreshold(long var1) {
      if (!this.inited) {
         this.currThreshold = var1;
         return var1;
      } else {
         long var4 = var1;
         if (var1 == -1L) {
            var4 = this.defThreshold;
         }

         var1 = var4;
         if (var4 == -2L) {
            var1 = this.maxThreshold;
         }

         if (var1 > this.currBuffer) {
            return this.currThreshold;
         } else {
            var4 = this.maxThreshold;
            if (var1 >= var4) {
               this.currThreshold = var4;
            } else {
               var4 = this.defThreshold;
               if (var1 == var4) {
                  this.currThreshold = var4;
               } else {
                  this.currThreshold = var1;
               }
            }

            if (var1 < 0L) {
               this.currThreshold = 0L;
            }

            for(int var3 = 0; var3 < this.sourcestreamlist.size(); ++var3) {
               ((RTPSourceStream)this.sourcestreamlist.elementAt(var3)).updateThreshold(this.currThreshold);
            }

            BufferControlImpl.BufferControlPanel var6 = this.controlComp;
            if (var6 != null) {
               var6.updateThreshold(this.currThreshold);
            }

            return this.currThreshold;
         }
      }
   }

   private class BufferControlPanel extends Panel {
      // $FF: renamed from: bb org.atalk.android.util.java.awt.Button
      Button field_154;
      Choice bchoice = null;
      TextField bsize;
      TextField btext = null;
      Panel buffersize = null;
      // $FF: renamed from: tb org.atalk.android.util.java.awt.Button
      Button field_155 = null;
      Choice tchoice = null;
      Panel threshold = null;
      TextField tsize;
      TextField ttext = null;

      public BufferControlPanel() {
         super(new BorderLayout());
         Panel var2 = new Panel(new FlowLayout());
         this.buffersize = var2;
         var2.add(new Label("BufferSize"));
         this.bsize = new TextField(15);
         this.updateBuffer(BufferControlImpl.this.getBufferLength());
         this.bsize.setEnabled(false);
         this.buffersize.add(this.bsize);
         this.buffersize.add(new Label("Update"));
         var2 = this.buffersize;
         Choice var3 = new Choice();
         this.bchoice = var3;
         var2.add(var3);
         this.bchoice.add("DEFAULT");
         this.bchoice.add("MAX");
         this.bchoice.add("User Defined");
         this.bchoice.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent var1) {
               if (var1.getItem().equals("User Defined")) {
                  BufferControlPanel.this.btext.setEnabled(true);
               } else {
                  BufferControlPanel.this.btext.setEnabled(false);
               }
            }
         });
         this.buffersize.add(new Label("If User Defined, Enter here:"));
         var2 = this.buffersize;
         TextField var4 = new TextField(10);
         this.btext = var4;
         var2.add(var4);
         this.btext.setEnabled(false);
         var2 = this.buffersize;
         Button var5 = new Button("Commit");
         this.field_154 = var5;
         var2.add(var5);
         this.field_154.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               BufferControlPanel.this.buffersizeUpdate();
            }
         });
         var2 = new Panel(new FlowLayout());
         this.threshold = var2;
         var2.add(new Label("Threshold"));
         this.tsize = new TextField(15);
         this.updateThreshold(BufferControlImpl.this.getMinimumThreshold());
         this.tsize.setEnabled(false);
         this.threshold.add(this.tsize);
         this.threshold.add(new Label("Update"));
         var2 = this.threshold;
         var3 = new Choice();
         this.tchoice = var3;
         var2.add(var3);
         this.tchoice.add("DEFAULT");
         this.tchoice.add("MAX");
         this.tchoice.add("User Defined");
         this.tchoice.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent var1) {
               if (var1.getItem().equals("User Defined")) {
                  BufferControlPanel.this.ttext.setEnabled(true);
               } else {
                  BufferControlPanel.this.ttext.setEnabled(false);
               }
            }
         });
         this.threshold.add(new Label("If User Defined, Enter here:"));
         var2 = this.threshold;
         var4 = new TextField(10);
         this.ttext = var4;
         var2.add(var4);
         this.ttext.setEnabled(false);
         var2 = this.threshold;
         var5 = new Button("Commit");
         this.field_155 = var5;
         var2.add(var5);
         this.field_155.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
               BufferControlPanel.this.thresholdUpdate();
            }
         });
         this.add(this.buffersize, "North");
         this.add(new Label("Actual buffer & threshold sizes (in millisec) not displayed until media type is determined"), "Center");
         this.add(this.threshold, "South");
         this.setVisible(true);
      }

      private void buffersizeUpdate() {
         String var3 = this.bchoice.getSelectedItem();
         long var1;
         if (var3.equals("MAX")) {
            var1 = -2L;
         } else if (var3.equals("DEFAULT")) {
            var1 = -1L;
         } else {
            var1 = Long.parseLong(this.btext.getText());
         }

         this.updateBuffer(BufferControlImpl.this.setBufferLength(var1));
      }

      private void thresholdUpdate() {
         String var3 = this.tchoice.getSelectedItem();
         long var1;
         if (var3.equals("DEFAULT")) {
            var1 = -1L;
         } else if (var3.equals("MAX")) {
            var1 = -2L;
         } else {
            var1 = Long.parseLong(this.ttext.getText());
         }

         this.updateThreshold(BufferControlImpl.this.setMinimumThreshold(var1));
      }

      public void updateBuffer(long var1) {
         if (var1 != 2147483647L && var1 != -2L && var1 != -1L) {
            this.bsize.setText(Long.toString(var1));
         }

      }

      public void updateThreshold(long var1) {
         if (var1 != 2147483647L && var1 != -2L && var1 != -1L) {
            this.tsize.setText(Long.toString(var1));
         }

      }
   }
}
