package net.sf.fmj.media.rtp;

import com.sun.media.util.Registry;
import javax.media.Buffer;
import javax.media.Format;
import javax.media.control.BufferControl;
import javax.media.format.AudioFormat;
import net.sf.fmj.media.Log;

class AudioJitterBufferBehaviour extends BasicJitterBufferBehaviour {
   private static final int DEFAULT_AUD_PKT_SIZE = 256;
   private static final int DEFAULT_MS_PER_PKT = 20;
   private static final int INITIAL_PACKETS = 300;
   private static final AudioFormat MPEG = new AudioFormat("mpegaudio/rtp");
   private final boolean AJB_ENABLED = Registry.getBoolean("adaptive_jitter_buffer_ENABLE", true);
   private final int AJB_GROW_INCREMENT = Registry.getInt("adaptive_jitter_buffer_GROW_INCREMENT", 2);
   private final int AJB_GROW_INTERVAL = Registry.getInt("adaptive_jitter_buffer_GROW_INTERVAL", 30);
   private final int AJB_GROW_THRESHOLD = Registry.getInt("adaptive_jitter_buffer_GROW_THRESHOLD", 3);
   private final int AJB_MAX_SIZE = Registry.getInt("adaptive_jitter_buffer_MAX_SIZE", 16);
   private final int AJB_MIN_SIZE = Registry.getInt("adaptive_jitter_buffer_MIN_SIZE", 4);
   private final int AJB_SHRINK_DECREMENT = Registry.getInt("adaptive_jitter_buffer_SHRINK_DECREMENT", 1);
   private final int AJB_SHRINK_INTERVAL = Registry.getInt("adaptive_jitter_buffer_SHRINK_INTERVAL", 120);
   private final int AJB_SHRINK_THRESHOLD = Registry.getInt("adaptive_jitter_buffer_SHRINK_THRESHOLD", 1);
   private int growCount;
   private byte[] history;
   private int historyLength;
   private int historyTail;
   private long msPerPkt = 20L;
   private boolean replenish = true;
   private int shrinkCount = 0;
   private boolean skipFec = false;

   public AudioJitterBufferBehaviour(RTPSourceStream var1) {
      super(var1);
      this.initHistory();
   }

   private void initHistory() {
      this.history = new byte[this.AJB_GROW_INTERVAL];
      this.historyLength = 0;
      this.historyTail = 0;
      this.growCount = 0;
   }

   private void recordInHistory(boolean var1) {
      throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.copyTypes(TypeTransformer.java:311)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:226)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
   }

   private void resetHistory() {
      this.historyLength = 0;
      this.shrinkCount = 0;
   }

   private void shrink(int var1) {
      if (var1 < 1) {
         throw new IllegalArgumentException("capacity");
      } else {
         int var2 = this.q.getCapacity();
         if (var1 != var2) {
            if (var1 > var2) {
               throw new IllegalArgumentException("capacity");
            } else {
               StringBuilder var3 = new StringBuilder();
               var3.append("Shrinking packet queue to ");
               var3.append(var1);
               Log.info(var3.toString());

               for(var2 = 0; this.q.getFillCount() > var1; ++var2) {
                  this.dropPkt();
                  this.stats.incrementDiscardedShrink();
               }

               this.q.setCapacity(var1);

               while(var2 < this.AJB_SHRINK_DECREMENT && this.q.fillNotEmpty()) {
                  this.dropPkt();
                  this.stats.incrementDiscardedShrink();
                  ++var2;
               }

               this.resetHistory();
            }
         }
      }
   }

   public void dropPkt() {
      super.dropPkt();
      this.skipFec = true;
      if (this.q.getFillCount() < this.AJB_SHRINK_THRESHOLD) {
         this.shrinkCount = 0;
      }

   }

   public int getAbsoluteMaximumDelay() {
      long var1;
      if (this.isAdaptive()) {
         long var3 = this.msPerPkt;
         var1 = var3;
         if (var3 <= 0L) {
            var1 = 20L;
         }

         var1 = (long)this.AJB_MAX_SIZE * var1;
      } else {
         var1 = (long)super.getAbsoluteMaximumDelay();
      }

      return var1 > 65535L ? '\uffff' : (int)var1;
   }

   public int getMaximumDelay() {
      long var3 = this.msPerPkt;
      long var1 = var3;
      if (var3 <= 0L) {
         var1 = 20L;
      }

      var1 = (long)this.q.getCapacity() * var1;
      return var1 > 65535L ? '\uffff' : (int)var1;
   }

   public int getNominalDelay() {
      long var3 = this.msPerPkt;
      long var1 = var3;
      if (var3 <= 0L) {
         var1 = 20L;
      }

      var1 = (long)(this.q.getCapacity() / 2) * var1;
      return var1 > 65535L ? '\uffff' : (int)var1;
   }

   protected void grow(int var1) {
      super.grow(var1);
      this.resetHistory();
   }

   public boolean isAdaptive() {
      return this.AJB_ENABLED;
   }

   protected int monitorQSize(Buffer var1) {
      super.monitorQSize(var1);
      int var2;
      int var3;
      if (this.AJB_ENABLED) {
         var2 = this.q.getCapacity();
         if (this.historyLength >= this.AJB_GROW_INTERVAL && this.growCount >= this.AJB_GROW_THRESHOLD) {
            var3 = this.AJB_MAX_SIZE;
            if (var2 < var3) {
               var3 = Math.min(this.AJB_GROW_INCREMENT + var2, var3);
               if (var3 > var2) {
                  this.grow(var3);
               }
            }
         }

         var3 = this.shrinkCount + 1;
         this.shrinkCount = var3;
         if (var3 >= this.AJB_SHRINK_INTERVAL && var2 > this.AJB_MIN_SIZE && this.q.freeNotEmpty()) {
            var3 = Math.max(var2 - this.AJB_SHRINK_DECREMENT, this.AJB_MIN_SIZE);
            if (var3 < var2) {
               this.shrink(var3);
            }
         }
      }

      BufferControl var11 = this.getBufferControl();
      if (var11 == null) {
         return 0;
      } else {
         Format var12 = this.stream.getFormat();
         var3 = this.stats.getSizePerPacket();
         var2 = var3;
         if (var3 <= 0) {
            var2 = 256;
         }

         long var5;
         long var7;
         if (MPEG.matches(var12)) {
            var5 = (long)(var2 / 4);
         } else {
            label421: {
               var7 = 20L;

               Throwable var10000;
               label411: {
                  boolean var10001;
                  try {
                     var5 = var1.getDuration();
                  } catch (Throwable var32) {
                     var10000 = var32;
                     var10001 = false;
                     break label411;
                  }

                  if (var5 <= 0L) {
                     label422: {
                        long var9;
                        try {
                           var9 = ((AudioFormat)var12).computeDuration((long)var1.getLength());
                        } catch (Throwable var30) {
                           var10000 = var30;
                           var10001 = false;
                           break label422;
                        }

                        var5 = var7;
                        if (var9 <= 0L) {
                           break label421;
                        }

                        label401:
                        try {
                           var5 = var9 / 1000000L;
                        } catch (Throwable var29) {
                           var10000 = var29;
                           var10001 = false;
                           break label401;
                        }
                     }
                  } else {
                     label407:
                     try {
                        var5 /= 1000000L;
                     } catch (Throwable var31) {
                        var10000 = var31;
                        var10001 = false;
                        break label407;
                     }
                  }
                  break label421;
               }

               Throwable var33 = var10000;
               if (var33 instanceof ThreadDeath) {
                  throw (ThreadDeath)var33;
               }

               var5 = var7;
            }
         }

         var7 = (this.msPerPkt + var5) / 2L;
         this.msPerPkt = var7;
         var5 = var7;
         if (var7 == 0L) {
            var5 = 20L;
         }

         var3 = (int)(var11.getBufferLength() / var5);
         if (!this.AJB_ENABLED && var3 > this.q.getCapacity()) {
            this.grow(var3);
            int var4 = this.q.getCapacity();
            StringBuilder var34 = new StringBuilder();
            var34.append("Grew audio RTP packet queue to: ");
            var34.append(var4);
            var34.append(" pkts, ");
            var34.append(var4 * var2);
            var34.append(" bytes.\n");
            Log.comment(var34.toString());
         }

         return var3;
      }
   }

   public boolean preAdd(Buffer var1, RTPRawReceiver var2) {
      long var5 = this.stream.getLastReadSequenceNumber();
      if (var5 != 9223372036854775806L) {
         long var7 = var1.getSequenceNumber();
         if (var7 < var5) {
            if (var5 - var7 < (long)this.AJB_MAX_SIZE) {
               this.recordInHistory(true);
               this.stats.incrementDiscardedLate();
               return false;
            }

            this.stats.incrementDiscardedVeryLate();
            return false;
         }
      }

      this.recordInHistory(false);
      if (!super.preAdd(var1, var2)) {
         return false;
      } else {
         if (this.AJB_ENABLED && this.q.noMoreFree() && this.stats.getNbAdd() > 300) {
            int var3 = this.q.getCapacity();
            int var4 = this.AJB_MAX_SIZE;
            if (var3 < var4) {
               this.grow(Math.min(var3 * 2, var4));
               return true;
            }

            while(this.q.getFillCount() >= var3 / 2) {
               this.stats.incrementDiscardedFull();
               this.dropPkt();
            }
         }

         return true;
      }
   }

   public void read(Buffer var1) {
      super.read(var1);
      if (!var1.isDiscard() && this.skipFec) {
         var1.setFlags(var1.getFlags() | 65536);
         this.skipFec = false;
      }

      int var2 = this.q.getFillCount();
      if (var2 == 0) {
         this.replenish = true;
      }

      if (var2 < this.AJB_SHRINK_THRESHOLD) {
         this.shrinkCount = 0;
      }

   }

   public void reset() {
      super.reset();
      this.resetHistory();
   }

   public boolean willReadBlock() {
      boolean var2 = super.willReadBlock();
      boolean var1 = var2;
      if (!var2) {
         if (this.replenish && this.q.getFillCount() >= this.q.getCapacity() / 2) {
            this.replenish = false;
         }

         var1 = this.replenish;
      }

      return var1;
   }
}
