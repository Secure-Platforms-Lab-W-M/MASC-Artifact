package net.sf.fmj.filtergraph;

import javax.media.Codec;
import javax.media.Format;
import javax.media.Multiplexer;
import javax.media.PlugIn;
import javax.media.Renderer;

public class GraphNode {
   static int ARRAY_INC = 30;
   Format[] attempted;
   int attemptedIdx;
   Class clz;
   public String cname;
   public boolean custom;
   public boolean failed;
   public Format input;
   public int level;
   public Format output;
   public PlugIn plugin;
   public GraphNode prev;
   Format[] supportedIns;
   Format[] supportedOuts;
   public int type;

   public GraphNode(String var1, PlugIn var2, Format var3, GraphNode var4, int var5) {
      this.type = -1;
      this.output = null;
      this.failed = false;
      this.custom = false;
      this.attemptedIdx = 0;
      this.attempted = null;
      this.cname = var1;
      this.plugin = var2;
      this.input = var3;
      this.prev = var4;
      this.level = var5;
   }

   public GraphNode(PlugIn var1, Format var2, GraphNode var3, int var4) {
      String var5;
      if (var1 == null) {
         var5 = null;
      } else {
         var5 = var1.getClass().getName();
      }

      this(var5, var1, var2, var3, var4);
   }

   public GraphNode(GraphNode var1, Format var2, GraphNode var3, int var4) {
      this.type = -1;
      this.output = null;
      this.failed = false;
      this.custom = false;
      this.attemptedIdx = 0;
      this.attempted = null;
      this.cname = var1.cname;
      this.plugin = var1.plugin;
      this.type = var1.type;
      this.custom = var1.custom;
      this.input = var2;
      this.prev = var3;
      this.level = var4;
      this.supportedIns = var1.supportedIns;
      if (var1.input == var2) {
         this.supportedOuts = var1.supportedOuts;
      }

   }

   public boolean checkAttempted(Format var1) {
      int var2;
      Format[] var4;
      if (this.attempted == null) {
         var4 = new Format[ARRAY_INC];
         this.attempted = var4;
         var2 = this.attemptedIdx++;
         var4[var2] = var1;
         return false;
      } else {
         var2 = 0;

         while(true) {
            int var3 = this.attemptedIdx;
            if (var2 >= var3) {
               var4 = this.attempted;
               if (var3 >= var4.length) {
                  Format[] var5 = new Format[var4.length + ARRAY_INC];
                  System.arraycopy(var4, 0, var5, 0, var4.length);
                  this.attempted = var5;
               }

               var4 = this.attempted;
               var2 = this.attemptedIdx++;
               var4[var2] = var1;
               return false;
            }

            if (var1.equals(this.attempted[var2])) {
               return true;
            }

            ++var2;
         }
      }
   }

   public Format[] getSupportedInputs() {
      Format[] var2 = this.supportedIns;
      if (var2 != null) {
         return var2;
      } else if (this.plugin == null) {
         return null;
      } else {
         int var1 = this.type;
         PlugIn var3;
         if (var1 == -1 || var1 == 2) {
            var3 = this.plugin;
            if (var3 instanceof Codec) {
               this.supportedIns = ((Codec)var3).getSupportedInputFormats();
               return this.supportedIns;
            }
         }

         var1 = this.type;
         if (var1 == -1 || var1 == 4) {
            var3 = this.plugin;
            if (var3 instanceof Renderer) {
               this.supportedIns = ((Renderer)var3).getSupportedInputFormats();
               return this.supportedIns;
            }
         }

         var3 = this.plugin;
         if (var3 instanceof Multiplexer) {
            this.supportedIns = ((Multiplexer)var3).getSupportedInputFormats();
         }

         return this.supportedIns;
      }
   }

   public Format[] getSupportedOutputs(Format var1) {
      Format[] var3;
      if (var1 == this.input) {
         var3 = this.supportedOuts;
         if (var3 != null) {
            return var3;
         }
      }

      if (this.plugin == null) {
         return null;
      } else {
         int var2 = this.type;
         if ((var2 == -1 || var2 == 4) && this.plugin instanceof Renderer) {
            return null;
         } else {
            var2 = this.type;
            if (var2 == -1 || var2 == 2) {
               PlugIn var4 = this.plugin;
               if (var4 instanceof Codec) {
                  var3 = ((Codec)var4).getSupportedOutputFormats(var1);
                  if (this.input == var1) {
                     this.supportedOuts = var3;
                  }

                  return var3;
               }
            }

            return null;
         }
      }
   }

   public void resetAttempted() {
      this.attemptedIdx = 0;
      this.attempted = null;
   }
}
