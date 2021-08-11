package net.sf.fmj.media.rtp;

import java.lang.ref.WeakReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import javax.media.Buffer;
import javax.media.Format;
import javax.media.control.BufferControl;
import javax.media.control.JitterBufferControl;
import javax.media.format.AudioFormat;
import javax.media.format.VideoFormat;
import javax.media.protocol.BufferTransferHandler;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.PushBufferStream;
import net.sf.fmj.media.Log;
import net.sf.fmj.media.protocol.BasicSourceStream;
import net.sf.fmj.media.protocol.BufferListener;
import net.sf.fmj.media.protocol.rtp.DataSource;
import net.sf.fmj.media.rtp.util.RTPMediaThread;

public class RTPSourceStream extends BasicSourceStream implements PushBufferStream {
   private static final long WAIT_TIMEOUT = 100L;
   // $FF: renamed from: bc net.sf.fmj.media.rtp.BufferControlImpl
   private BufferControlImpl field_54;
   private JitterBufferBehaviour behaviour;
   private boolean bufferWhenStopped = true;
   private boolean closed = false;
   private boolean closing = false;
   final DataSource datasource;
   private Format format;
   private long lastSeqRecv = 9223372036854775806L;
   private long lastSeqSent = 9223372036854775806L;
   // $FF: renamed from: q net.sf.fmj.media.rtp.JitterBuffer
   final JitterBuffer field_55;
   private final Condition qCondition;
   private final Lock qLock;
   private final Object startSyncRoot = new Object();
   private boolean started = false;
   final JitterBufferStats stats;
   private Thread thread;
   private long transferDataReason;
   private BufferTransferHandler transferHandler;

   public RTPSourceStream(DataSource var1) {
      var1.setSourceStream(this);
      this.datasource = var1;
      JitterBuffer var2 = new JitterBuffer(4);
      this.field_55 = var2;
      this.qCondition = var2.condition;
      this.qLock = this.field_55.lock;
      this.stats = new JitterBufferStats(this);
      this.setBehaviour((JitterBufferBehaviour)null);
   }

   private boolean runInThread(RTPSourceStream.TransferDataRunnable param1) {
      // $FF: Couldn't be decompiled
   }

   private void setBehaviour(JitterBufferBehaviour var1) {
      Object var2 = var1;
      if (var1 == null) {
         if (this.behaviour instanceof BasicJitterBufferBehaviour) {
            return;
         }

         var2 = new BasicJitterBufferBehaviour(this);
      }

      this.behaviour = (JitterBufferBehaviour)var2;
   }

   private void startThread() {
      Object var1 = this.startSyncRoot;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label504: {
         label508: {
            RTPMediaThread var2;
            try {
               this.waitWhileClosing();
               if (this.thread != null || this.closed) {
                  break label508;
               }

               var2 = new RTPMediaThread(new RTPSourceStream.TransferDataRunnable(this), RTPSourceStream.class.getName());
               var2.setDaemon(true);
               var2.useControlPriority();
               this.thread = var2;
            } catch (Throwable var60) {
               var10000 = var60;
               var10001 = false;
               break label504;
            }

            boolean var46 = false;

            try {
               var46 = true;
               var2.start();
               var46 = false;
            } finally {
               if (var46) {
                  if (true) {
                     try {
                        if (var2.equals(this.thread)) {
                           this.thread = null;
                        }
                     } catch (Throwable var56) {
                        var10000 = var56;
                        var10001 = false;
                        break label504;
                     }
                  }

                  try {
                     ;
                  } catch (Throwable var55) {
                     var10000 = var55;
                     var10001 = false;
                     break label504;
                  }
               }
            }

            if (false) {
               try {
                  if (var2.equals(this.thread)) {
                     this.thread = null;
                  }
               } catch (Throwable var58) {
                  var10000 = var58;
                  var10001 = false;
                  break label504;
               }
            }
         }

         label490:
         try {
            this.startSyncRoot.notifyAll();
            return;
         } catch (Throwable var57) {
            var10000 = var57;
            var10001 = false;
            break label490;
         }
      }

      while(true) {
         Throwable var61 = var10000;

         try {
            throw var61;
         } catch (Throwable var54) {
            var10000 = var54;
            var10001 = false;
            continue;
         }
      }
   }

   private void threadExited(RTPSourceStream.TransferDataRunnable var1) {
      Object var15 = this.startSyncRoot;
      synchronized(var15){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            if (Thread.currentThread().equals(this.thread)) {
               this.thread = null;
               this.startSyncRoot.notifyAll();
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label122;
         }

         label119:
         try {
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label119;
         }
      }

      while(true) {
         Throwable var2 = var10000;

         try {
            throw var2;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   private void waitWhileClosing() {
      boolean var1 = false;

      while(this.closing) {
         try {
            this.startSyncRoot.wait();
         } catch (InterruptedException var3) {
            var1 = true;
         }
      }

      if (var1) {
         Thread.currentThread().interrupt();
      }

   }

   public void add(Buffer var1, boolean var2, RTPRawReceiver var3) {
      if (this.started || this.bufferWhenStopped) {
         long var5 = var1.getSequenceNumber();
         this.qLock.lock();

         Throwable var10000;
         Throwable var354;
         label2866: {
            boolean var10001;
            try {
               if (this.lastSeqRecv - var5 > 256L) {
                  StringBuilder var9 = new StringBuilder();
                  var9.append("Resetting queue, last seq added: ");
                  var9.append(this.lastSeqRecv);
                  var9.append(", current seq: ");
                  var9.append(var5);
                  Log.info(var9.toString());
                  this.reset();
                  this.lastSeqRecv = var5;
               }
            } catch (Throwable var353) {
               var10000 = var353;
               var10001 = false;
               break label2866;
            }

            try {
               this.stats.updateMaxSizeReached();
               this.stats.updateSizePerPacket(var1);
               var2 = this.behaviour.preAdd(var1, var3);
            } catch (Throwable var352) {
               var10000 = var352;
               var10001 = false;
               break label2866;
            }

            if (!var2) {
               this.qLock.unlock();
               return;
            }

            try {
               this.stats.incrementNbAdd();
               this.lastSeqRecv = var5;
            } catch (Throwable var351) {
               var10000 = var351;
               var10001 = false;
               break label2866;
            }

            boolean var4 = false;

            label2867: {
               long var7;
               try {
                  if (!this.field_55.noMoreFree()) {
                     break label2867;
                  }

                  this.stats.incrementDiscardedFull();
                  var7 = this.field_55.getFirstSeq();
               } catch (Throwable var350) {
                  var10000 = var350;
                  var10001 = false;
                  break label2866;
               }

               if (var7 != 9223372036854775806L && var5 < var7) {
                  this.qLock.unlock();
                  return;
               }

               try {
                  this.behaviour.dropPkt();
               } catch (Throwable var349) {
                  var10000 = var349;
                  var10001 = false;
                  break label2866;
               }
            }

            label2837: {
               try {
                  if (this.field_55.getFreeCount() > 1) {
                     break label2837;
                  }
               } catch (Throwable var348) {
                  var10000 = var348;
                  var10001 = false;
                  break label2866;
               }

               var4 = true;
            }

            Buffer var10;
            try {
               var10 = this.field_55.getFree();
            } catch (Throwable var347) {
               var10000 = var347;
               var10001 = false;
               break label2866;
            }

            label2868: {
               label2869: {
                  byte[] var11;
                  byte[] var356;
                  try {
                     var11 = (byte[])((byte[])var1.getData());
                     var356 = (byte[])((byte[])var10.getData());
                  } catch (Throwable var346) {
                     var10000 = var346;
                     var10001 = false;
                     break label2869;
                  }

                  byte[] var355;
                  label2823: {
                     if (var356 != null) {
                        var355 = var356;

                        try {
                           if (var356.length >= var11.length) {
                              break label2823;
                           }
                        } catch (Throwable var345) {
                           var10000 = var345;
                           var10001 = false;
                           break label2869;
                        }
                     }

                     try {
                        var355 = new byte[var11.length];
                     } catch (Throwable var344) {
                        var10000 = var344;
                        var10001 = false;
                        break label2869;
                     }
                  }

                  try {
                     System.arraycopy(var11, var1.getOffset(), var355, var1.getOffset(), var1.getLength());
                     var10.copy(var1);
                     var10.setData(var355);
                  } catch (Throwable var343) {
                     var10000 = var343;
                     var10001 = false;
                     break label2869;
                  }

                  if (var4) {
                     try {
                        var10.setFlags(var10.getFlags() | 8192 | 32);
                     } catch (Throwable var342) {
                        var10000 = var342;
                        var10001 = false;
                        break label2869;
                     }
                  } else {
                     try {
                        var10.setFlags(var10.getFlags() | 32);
                     } catch (Throwable var341) {
                        var10000 = var341;
                        var10001 = false;
                        break label2869;
                     }
                  }

                  label2806:
                  try {
                     this.field_55.addPkt(var10);
                     break label2868;
                  } catch (Throwable var340) {
                     var10000 = var340;
                     var10001 = false;
                     break label2806;
                  }
               }

               var354 = var10000;
               if (true) {
                  try {
                     this.field_55.returnFree(var10);
                  } catch (Throwable var337) {
                     var10000 = var337;
                     var10001 = false;
                     break label2866;
                  }
               }

               try {
                  throw var354;
               } catch (Throwable var336) {
                  var10000 = var336;
                  var10001 = false;
                  break label2866;
               }
            }

            if (false) {
               try {
                  this.field_55.returnFree(var10);
               } catch (Throwable var339) {
                  var10000 = var339;
                  var10001 = false;
                  break label2866;
               }
            }

            try {
               ++this.transferDataReason;
               if (!this.behaviour.willReadBlock()) {
                  this.qCondition.signalAll();
               }
            } catch (Throwable var338) {
               var10000 = var338;
               var10001 = false;
               break label2866;
            }

            this.qLock.unlock();
            return;
         }

         var354 = var10000;
         this.qLock.unlock();
         throw var354;
      }
   }

   public void close() {
      // $FF: Couldn't be decompiled
   }

   public void connect() {
      // $FF: Couldn't be decompiled
   }

   JitterBufferBehaviour getBehaviour() {
      return this.behaviour;
   }

   BufferControlImpl getBufferControl() {
      return this.field_54;
   }

   public Object getControl(String var1) {
      return JitterBufferControl.class.getName().equals(var1) ? this.stats : super.getControl(var1);
   }

   public Object[] getControls() {
      Object[] var1 = super.getControls();
      Object[] var2 = new Object[var1.length + 1];
      System.arraycopy(var1, 0, var2, 0, var1.length);
      var2[var1.length] = this.stats;
      return var2;
   }

   public Format getFormat() {
      return this.format;
   }

   long getLastReadSequenceNumber() {
      return this.lastSeqSent;
   }

   public void prebuffer() {
   }

   public void read(Buffer var1) {
      this.qLock.lock();

      try {
         this.behaviour.read(var1);
         if (!var1.isDiscard()) {
            this.lastSeqSent = var1.getSequenceNumber();
         }
      } finally {
         Throwable var10000;
         label192: {
            boolean var10001;
            try {
               if (!var1.isDiscard()) {
                  ++this.transferDataReason;
                  this.qCondition.signalAll();
               }
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break label192;
            }

            label189:
            try {
               ;
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               break label189;
            }
         }

         Throwable var22 = var10000;
         this.qLock.unlock();
         throw var22;
      }

      this.qLock.unlock();
   }

   public void reset() {
      this.qLock.lock();

      try {
         this.stats.incrementNbReset();
         this.resetQ();
         this.behaviour.reset();
         this.lastSeqSent = 9223372036854775806L;
      } finally {
         this.qLock.unlock();
      }

   }

   public void resetQ() {
      Log.comment("Resetting the RTP packet queue");
      this.qLock.lock();

      while(true) {
         boolean var3 = false;

         try {
            var3 = true;
            if (!this.field_55.fillNotEmpty()) {
               this.qCondition.signalAll();
               var3 = false;
               break;
            }

            this.behaviour.dropPkt();
            this.stats.incrementDiscardedReset();
            var3 = false;
         } finally {
            if (var3) {
               this.qLock.unlock();
            }
         }
      }

      this.qLock.unlock();
   }

   public void setBufferControl(BufferControl var1) {
      BufferControlImpl var2 = (BufferControlImpl)var1;
      this.field_54 = var2;
      this.updateBuffer(var2.getBufferLength());
      this.updateThreshold(this.field_54.getMinimumThreshold());
   }

   public void setBufferListener(BufferListener var1) {
   }

   public void setBufferWhenStopped(boolean var1) {
      this.bufferWhenStopped = var1;
   }

   void setContentDescriptor(String var1) {
      this.contentDescriptor = new ContentDescriptor(var1);
   }

   protected void setFormat(Format var1) {
      if (this.format != var1) {
         this.format = var1;
         Object var2;
         if (var1 instanceof AudioFormat) {
            var2 = new AudioJitterBufferBehaviour(this);
         } else if (var1 instanceof VideoFormat) {
            var2 = new VideoJitterBufferBehaviour(this);
         } else {
            var2 = null;
         }

         this.setBehaviour((JitterBufferBehaviour)var2);
      }

   }

   public void setTransferHandler(BufferTransferHandler var1) {
      this.transferHandler = var1;
   }

   public void start() {
      // $FF: Couldn't be decompiled
   }

   public void stop() {
      Log.info("Stopping RTPSourceStream.");
      Object var1 = this.startSyncRoot;
      synchronized(var1){}

      label210: {
         Throwable var10000;
         boolean var10001;
         label205: {
            try {
               this.started = false;
               this.startSyncRoot.notifyAll();
               if (!this.bufferWhenStopped) {
                  this.reset();
               }
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label205;
            }

            label202:
            try {
               break label210;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label202;
            }
         }

         while(true) {
            Throwable var2 = var10000;

            try {
               throw var2;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               continue;
            }
         }
      }

      if (this.qLock.tryLock()) {
         try {
            this.qCondition.signalAll();
         } finally {
            this.qLock.unlock();
         }

      }
   }

   public long updateBuffer(long var1) {
      return var1;
   }

   public long updateThreshold(long var1) {
      return var1;
   }

   private static class TransferDataRunnable implements Runnable {
      private static final boolean WEAK_REFERENCE = false;
      private final RTPSourceStream owner;
      private long transferDataReason;
      private final WeakReference weakReference;

      public TransferDataRunnable(RTPSourceStream var1) {
         this.owner = var1;
         this.weakReference = null;
      }

      // $FF: synthetic method
      static long access$000(RTPSourceStream.TransferDataRunnable var0) {
         return var0.transferDataReason;
      }

      // $FF: synthetic method
      static long access$002(RTPSourceStream.TransferDataRunnable var0, long var1) {
         var0.transferDataReason = var1;
         return var1;
      }

      private RTPSourceStream getOwner() {
         return this.owner;
      }

      public void run() {
         RTPSourceStream var2;
         label109: {
            Throwable var10000;
            while(true) {
               boolean var10001;
               try {
                  var2 = this.getOwner();
               } catch (Throwable var9) {
                  var10000 = var9;
                  var10001 = false;
                  break;
               }

               if (var2 == null) {
                  break label109;
               }

               boolean var1;
               try {
                  var1 = var2.runInThread(this);
               } catch (Throwable var8) {
                  var10000 = var8;
                  var10001 = false;
                  break;
               }

               if (!var1) {
                  break label109;
               }
            }

            Throwable var10 = var10000;
            RTPSourceStream var3 = this.getOwner();
            if (var3 != null) {
               var3.threadExited(this);
            }

            throw var10;
         }

         var2 = this.getOwner();
         if (var2 != null) {
            var2.threadExited(this);
         }

      }
   }
}
