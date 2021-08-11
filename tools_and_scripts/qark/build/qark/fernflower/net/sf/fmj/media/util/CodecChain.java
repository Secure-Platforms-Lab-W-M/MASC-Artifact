package net.sf.fmj.media.util;

import java.util.Vector;
import javax.media.Buffer;
import javax.media.Codec;
import javax.media.Format;
import javax.media.Renderer;
import javax.media.ResourceUnavailableException;
import net.sf.fmj.filtergraph.SimpleGraphBuilder;
import net.sf.fmj.media.Log;
import org.atalk.android.util.java.awt.Component;

public class CodecChain {
   static final int STAGES = 5;
   protected Buffer[] buffers = null;
   protected Codec[] codecs = null;
   private boolean deallocated = true;
   protected boolean firstBuffer = true;
   protected Format[] formats = null;
   protected Renderer renderer = null;
   private boolean rtpFormat = false;

   private int doProcess(int var1, Buffer var2, boolean var3) {
      Format var5 = var2.getFormat();
      StringBuilder var12;
      if (var1 == this.codecs.length) {
         if (var3) {
            if (this.renderer != null) {
               Format[] var13 = this.formats;
               if (var13[var1] != null && var13[var1] != var5 && !var13[var1].equals(var5) && !var2.isDiscard()) {
                  if (this.renderer.setInputFormat(var5) == null) {
                     Log.error("Monitor failed to handle mid-stream format change:");
                     var12 = new StringBuilder();
                     var12.append("  old: ");
                     var12.append(this.formats[var1]);
                     Log.error(var12.toString());
                     var12 = new StringBuilder();
                     var12.append("  new: ");
                     var12.append(var5);
                     Log.error(var12.toString());
                     return 1;
                  }

                  this.formats[var1] = var5;
               }
            }

            try {
               var1 = this.renderer.process(var2);
               return var1;
            } catch (Exception var8) {
               Log.dumpStack(var8);
               return 1;
            } catch (Error var9) {
               Log.dumpStack(var9);
               return 1;
            }
         } else {
            return 0;
         }
      } else {
         if (this.isRawFormat(var5)) {
            if (!var3) {
               return 0;
            }
         } else if (!this.rtpFormat && this.firstBuffer) {
            if ((var2.getFlags() & 16) == 0) {
               return 0;
            }

            this.firstBuffer = false;
         }

         Codec var6 = this.codecs[var1];
         if (var6 != null) {
            Format[] var7 = this.formats;
            if (var7[var1] != null && var7[var1] != var5 && !var7[var1].equals(var5) && !var2.isDiscard()) {
               if (var6.setInputFormat(var5) == null) {
                  Log.error("Monitor failed to handle mid-stream format change:");
                  var12 = new StringBuilder();
                  var12.append("  old: ");
                  var12.append(this.formats[var1]);
                  Log.error(var12.toString());
                  var12 = new StringBuilder();
                  var12.append("  new: ");
                  var12.append(var5);
                  Log.error(var12.toString());
                  return 1;
               }

               this.formats[var1] = var5;
            }
         }

         int var4;
         do {
            try {
               var4 = var6.process(var2, this.buffers[var1]);
            } catch (Exception var10) {
               Log.dumpStack(var10);
               return 1;
            } catch (Error var11) {
               Log.dumpStack(var11);
               return 1;
            }

            if (var4 == 1) {
               return 1;
            }

            if ((var4 & 4) == 0) {
               if (!this.buffers[var1].isDiscard() && !this.buffers[var1].isEOM()) {
                  this.doProcess(var1 + 1, this.buffers[var1], var3);
               }

               this.buffers[var1].setOffset(0);
               this.buffers[var1].setLength(0);
               this.buffers[var1].setFlags(0);
            }
         } while((var4 & 2) != 0);

         return var4;
      }
   }

   protected boolean buildChain(Format var1) {
      Vector var4 = new Vector(10);
      Vector var5 = SimpleGraphBuilder.findRenderingChain(var1, var4);
      if (var5 == null) {
         return false;
      } else {
         int var3 = var5.size();
         this.codecs = new Codec[var3 - 1];
         this.buffers = new Buffer[var3 - 1];
         Format[] var6 = new Format[var3];
         this.formats = var6;
         var6[0] = var1;
         Log.comment("Monitor codec chain:");
         int var2 = 0;

         while(true) {
            Codec[] var8 = this.codecs;
            if (var2 >= var8.length) {
               this.renderer = (Renderer)var5.elementAt(0);
               StringBuilder var7 = new StringBuilder();
               var7.append("    renderer: ");
               var7.append(this.renderer);
               Log.write(var7.toString());
               var7 = new StringBuilder();
               var7.append("      format: ");
               var7.append(this.formats[this.codecs.length]);
               var7.append("\n");
               Log.write(var7.toString());
               if (var1.getEncoding() != null && var1.getEncoding().toUpperCase().endsWith("RTP")) {
                  this.rtpFormat = true;
               }

               return true;
            }

            var8[var2] = (Codec)var5.elementAt(var3 - var2 - 1);
            this.formats[var2 + 1] = (Format)var4.elementAt(var3 - var2 - 2);
            this.buffers[var2] = new Buffer();
            this.buffers[var2].setFormat(this.formats[var2 + 1]);
            StringBuilder var9 = new StringBuilder();
            var9.append("    codec: ");
            var9.append(this.codecs[var2]);
            Log.write(var9.toString());
            var9 = new StringBuilder();
            var9.append("      format: ");
            var9.append(this.formats[var2]);
            Log.write(var9.toString());
            ++var2;
         }
      }
   }

   public void close() {
      int var1 = 0;

      while(true) {
         Codec[] var2 = this.codecs;
         if (var1 >= var2.length) {
            Renderer var3 = this.renderer;
            if (var3 != null) {
               var3.close();
            }

            return;
         }

         var2[var1].close();
         ++var1;
      }
   }

   public void deallocate() {
      if (!this.deallocated) {
         Renderer var1 = this.renderer;
         if (var1 != null) {
            var1.close();
         }

         this.deallocated = true;
      }
   }

   public Component getControlComponent() {
      return null;
   }

   boolean isRawFormat(Format var1) {
      return false;
   }

   public boolean prefetch() {
      if (!this.deallocated) {
         return true;
      } else {
         try {
            this.renderer.open();
         } catch (ResourceUnavailableException var2) {
            return false;
         }

         this.renderer.start();
         this.deallocated = false;
         return true;
      }
   }

   public int process(Buffer var1, boolean var2) {
      return this.doProcess(0, var1, var2);
   }

   public void reset() {
      this.firstBuffer = true;
      int var1 = 0;

      while(true) {
         Codec[] var2 = this.codecs;
         if (var1 >= var2.length) {
            return;
         }

         if (var2[var1] != null) {
            var2[var1].reset();
         }

         ++var1;
      }
   }
}
