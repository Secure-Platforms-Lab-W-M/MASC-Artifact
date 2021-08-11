package net.sf.fmj.media;

import java.io.PrintStream;
import javax.media.Buffer;
import javax.media.Codec;
import javax.media.Format;
import javax.media.ResourceUnavailableException;
import javax.media.control.FrameProcessingControl;
import net.sf.fmj.filtergraph.SimpleGraphBuilder;
import org.atalk.android.util.java.awt.Frame;

public class BasicFilterModule extends BasicModule {
   protected final boolean VERBOSE_CONTROL = false;
   protected Codec codec;
   protected Frame controlFrame;
   protected float curFramesBehind = 0.0F;
   private boolean failed = false;
   protected FrameProcessingControl frameControl = null;
   // $FF: renamed from: ic net.sf.fmj.media.InputConnector
   protected InputConnector field_33;
   private Object lastHdr = null;
   private boolean markerSet = false;
   // $FF: renamed from: oc net.sf.fmj.media.OutputConnector
   protected OutputConnector field_34;
   protected float prevFramesBehind = 0.0F;
   protected boolean readPendingFlag = false;
   protected Buffer storedInputBuffer;
   protected Buffer storedOutputBuffer;
   protected boolean writePendingFlag = false;

   public BasicFilterModule(Codec var1) {
      BasicInputConnector var2 = new BasicInputConnector();
      this.field_33 = var2;
      this.registerInputConnector("input", var2);
      BasicOutputConnector var4 = new BasicOutputConnector();
      this.field_34 = var4;
      this.registerOutputConnector("output", var4);
      this.setCodec(var1);
      this.protocol = 0;
      Object var3 = var1.getControl(FrameProcessingControl.class.getName());
      if (var3 instanceof FrameProcessingControl) {
         this.frameControl = (FrameProcessingControl)var3;
      }

   }

   public void doClose() {
      Codec var1 = this.codec;
      if (var1 != null) {
         var1.close();
      }

      Frame var2 = this.controlFrame;
      if (var2 != null) {
         var2.dispose();
         this.controlFrame = null;
      }

   }

   public boolean doPrefetch() {
      return super.doPrefetch();
   }

   public boolean doRealize() {
      Codec var1 = this.codec;
      if (var1 != null) {
         try {
            var1.open();
         } catch (ResourceUnavailableException var2) {
            return false;
         }
      }

      return true;
   }

   public Codec getCodec() {
      return this.codec;
   }

   public Object getControl(String var1) {
      return this.codec.getControl(var1);
   }

   public Object[] getControls() {
      return this.codec.getControls();
   }

   public boolean isThreaded() {
      return this.getProtocol() == 1;
   }

   public void process() {
      while(true) {
         Buffer var4;
         Buffer var5;
         if (this.readPendingFlag) {
            var4 = this.storedInputBuffer;
         } else {
            var5 = this.field_33.getValidBuffer();
            Format var6 = var5.getFormat();
            Format var11 = var6;
            if (var6 == null) {
               var11 = this.field_33.getFormat();
               var5.setFormat(var11);
            }

            if (var11 != this.field_33.getFormat() && var11 != null && !var11.equals(this.field_33.getFormat()) && !var5.isDiscard()) {
               if (this.writePendingFlag) {
                  this.storedOutputBuffer.setDiscard(true);
                  this.field_34.writeReport();
                  this.writePendingFlag = false;
               }

               if (!this.reinitCodec(var5.getFormat())) {
                  var5.setDiscard(true);
                  this.field_33.readReport();
                  this.failed = true;
                  if (this.moduleListener != null) {
                     this.moduleListener.formatChangedFailure(this, this.field_33.getFormat(), var5.getFormat());
                  }

                  return;
               }

               var11 = this.field_33.getFormat();
               this.field_33.setFormat(var5.getFormat());
               if (this.moduleListener != null) {
                  this.moduleListener.formatChanged(this, var11, var5.getFormat());
               }
            }

            var4 = var5;
            if ((var5.getFlags() & 1024) != 0) {
               this.markerSet = true;
               var4 = var5;
            }
         }

         if (this.writePendingFlag) {
            var5 = this.storedOutputBuffer;
         } else {
            Buffer var12 = this.field_34.getEmptyBuffer();
            var5 = var12;
            if (var12 != null) {
               var12.setLength(0);
               var12.setOffset(0);
               this.lastHdr = var12.getHeader();
               var5 = var12;
            }
         }

         var5.setTimeStamp(var4.getTimeStamp());
         var5.setRtpTimeStamp(var4.getRtpTimeStamp());
         var5.setHeaderExtension(var4.getHeaderExtension());
         var5.setDuration(var4.getDuration());
         var5.setSequenceNumber(var4.getSequenceNumber());
         var5.setFlags(var4.getFlags());
         var5.setHeader(var4.getHeader());
         if (this.resetted) {
            if ((var4.getFlags() & 512) != 0) {
               this.codec.reset();
               this.resetted = false;
            }

            this.writePendingFlag = false;
            this.readPendingFlag = false;
            this.field_33.readReport();
            this.field_34.writeReport();
            return;
         }

         if (!this.failed && !var4.isDiscard()) {
            if (this.frameControl != null && this.curFramesBehind != this.prevFramesBehind && (var4.getFlags() & 32) == 0) {
               this.frameControl.setFramesBehind(this.curFramesBehind);
               this.prevFramesBehind = this.curFramesBehind;
            }

            byte var3 = 0;

            int var2;
            label316:
            try {
               var2 = this.codec.process(var4, var5);
            } catch (Throwable var9) {
               Log.dumpStack(var9);
               var2 = var3;
               if (this.moduleListener != null) {
                  this.moduleListener.internalErrorOccurred(this);
                  var2 = var3;
               }
               break label316;
            }

            if (PlaybackEngine.TRACE_ON && !this.verifyBuffer(var5)) {
               PrintStream var13 = System.err;
               StringBuilder var7 = new StringBuilder();
               var7.append("verify buffer failed: ");
               var7.append(this.codec);
               var13.println(var7.toString());
               Thread.dumpStack();
               if (this.moduleListener != null) {
                  this.moduleListener.internalErrorOccurred(this);
               }
            }

            if ((var2 & 8) != 0) {
               this.failed = true;
               if (this.moduleListener != null) {
                  this.moduleListener.pluginTerminated(this);
               }

               this.writePendingFlag = false;
               this.readPendingFlag = false;
               this.field_33.readReport();
               this.field_34.writeReport();
               return;
            }

            int var10 = var2;
            if (this.curFramesBehind > 0.0F) {
               var10 = var2;
               if (var5.isDiscard()) {
                  float var1 = this.curFramesBehind - 1.0F;
                  this.curFramesBehind = var1;
                  if (var1 < 0.0F) {
                     this.curFramesBehind = 0.0F;
                  }

                  var10 = var2 & -5;
               }
            }

            if ((var10 & 1) != 0) {
               var5.setDiscard(true);
               if (this.markerSet) {
                  var5.setFlags(var5.getFlags() & -1025);
                  this.markerSet = false;
               }

               this.field_33.readReport();
               this.field_34.writeReport();
               this.writePendingFlag = false;
               this.readPendingFlag = false;
               return;
            }

            if (var5.isEOM() && ((var10 & 2) != 0 || (var10 & 4) != 0)) {
               var5.setEOM(false);
            }

            if ((var10 & 4) != 0) {
               this.writePendingFlag = true;
               this.storedOutputBuffer = var5;
            } else {
               if (this.markerSet) {
                  var5.setFlags(var5.getFlags() | 1024);
                  this.markerSet = false;
               }

               this.field_34.writeReport();
               this.writePendingFlag = false;
            }

            if ((var10 & 2) != 0 || var4.isEOM() && !var5.isEOM()) {
               this.readPendingFlag = true;
               this.storedInputBuffer = var4;
            } else {
               var4.setHeader(this.lastHdr);
               this.field_33.readReport();
               this.readPendingFlag = false;
            }

            if (this.readPendingFlag) {
               continue;
            }

            return;
         }

         if (this.markerSet) {
            var5.setFlags(var5.getFlags() & -1025);
            this.markerSet = false;
         }

         this.curFramesBehind = 0.0F;
         this.field_33.readReport();
         if (!this.writePendingFlag) {
            this.field_34.writeReport();
         }

         return;
      }
   }

   protected boolean reinitCodec(Format var1) {
      Codec var2 = this.codec;
      if (var2 != null) {
         if (var2.setInputFormat(var1) != null) {
            return true;
         }

         this.codec.close();
         this.codec = null;
      }

      Codec var3 = SimpleGraphBuilder.findCodec(var1, (Format)null, (Format[])null, (Format[])null);
      if (var3 == null) {
         return false;
      } else {
         this.setCodec(var3);
         return true;
      }
   }

   public boolean setCodec(String var1) {
      return true;
   }

   public boolean setCodec(Codec var1) {
      this.codec = var1;
      return true;
   }

   public void setFormat(Connector var1, Format var2) {
      Codec var3;
      if (var1 == this.field_33) {
         var3 = this.codec;
         if (var3 != null) {
            var3.setInputFormat(var2);
            return;
         }
      } else if (var1 == this.field_34) {
         var3 = this.codec;
         if (var3 != null) {
            var3.setOutputFormat(var2);
         }
      }

   }

   protected void setFramesBehind(float var1) {
      this.curFramesBehind = var1;
   }
}
