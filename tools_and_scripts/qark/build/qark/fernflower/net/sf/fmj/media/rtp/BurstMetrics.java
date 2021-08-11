package net.sf.fmj.media.rtp;

public class BurstMetrics {
   private static final short GMIN = 16;
   private long burstMetrics;
   private long c11;
   private long c13;
   private long c14;
   private long c22;
   private long c23;
   private long c33;
   private boolean calculate;
   private long discardCount;
   private long lossCount;
   private long lost;
   private long pkt;

   private void calculate() {
      synchronized(this){}

      Throwable var10000;
      label4166: {
         long var5;
         long var7;
         long var11;
         boolean var10001;
         try {
            var11 = this.c11 + this.c13 + this.c14;
            var5 = this.c22 + this.c23;
            var7 = this.c13 + this.c23 + this.c33;
         } catch (Throwable var525) {
            var10000 = var525;
            var10001 = false;
            break label4166;
         }

         long var17 = var11 + var5 + var7;

         long var9;
         try {
            var9 = this.c22;
         } catch (Throwable var524) {
            var10000 = var524;
            var10001 = false;
            break label4166;
         }

         double var1 = 1.0D;
         if (var9 > 0L) {
            try {
               var1 = 1.0D - (double)this.c22 / (double)var5;
            } catch (Throwable var523) {
               var10000 = var523;
               var10001 = false;
               break label4166;
            }
         }

         double var3;
         label4149: {
            label4148: {
               try {
                  if (this.c23 <= 0L) {
                     break label4148;
                  }
               } catch (Throwable var522) {
                  var10000 = var522;
                  var10001 = false;
                  break label4166;
               }

               try {
                  var3 = (double)this.c23 / (double)var7;
                  break label4149;
               } catch (Throwable var521) {
                  var10000 = var521;
                  var10001 = false;
                  break label4166;
               }
            }

            var3 = 0.0D;
         }

         if (var1 <= 0.0D) {
            var7 = 0L;
         } else {
            try {
               var7 = (long)(256.0D * var1 / (var1 + var3));
            } catch (Throwable var520) {
               var10000 = var520;
               var10001 = false;
               break label4166;
            }

            if (var7 > 255L) {
               var7 = 255L;
            }
         }

         label4135: {
            label4167: {
               try {
                  if (this.c14 <= 0L) {
                     break label4167;
                  }
               } catch (Throwable var519) {
                  var10000 = var519;
                  var10001 = false;
                  break label4166;
               }

               try {
                  var5 = (long)((double)(this.c14 * 256L) / (double)(this.c11 + this.c14));
               } catch (Throwable var518) {
                  var10000 = var518;
                  var10001 = false;
                  break label4166;
               }

               var9 = var5;
               if (var5 > 255L) {
                  var9 = 255L;
               }
               break label4135;
            }

            var9 = 0L;
         }

         long var13;
         long var15;
         label4125: {
            label4124: {
               try {
                  if (this.c13 <= 0L) {
                     break label4124;
                  }
               } catch (Throwable var517) {
                  var10000 = var517;
                  var10001 = false;
                  break label4166;
               }

               try {
                  var13 = (long)20 * var11 / this.c13;
                  var15 = (long)20 * var17 / this.c13 - var13;
                  break label4125;
               } catch (Throwable var516) {
                  var10000 = var516;
                  var10001 = false;
                  break label4166;
               }
            }

            var13 = 0L;
            var15 = 0L;
         }

         if (var17 <= 0L) {
            var5 = 0L;
            var11 = 0L;
         } else {
            try {
               var11 = this.lossCount * 256L / var17;
            } catch (Throwable var515) {
               var10000 = var515;
               var10001 = false;
               break label4166;
            }

            var5 = var11;
            if (var11 > 255L) {
               var5 = 255L;
            }

            try {
               var11 = this.discardCount * 256L / var17;
            } catch (Throwable var514) {
               var10000 = var514;
               var10001 = false;
               break label4166;
            }

            if (var11 > 255L) {
               var11 = 255L;
            }
         }

         var5 &= 255L;

         try {
            this.burstMetrics = var5;
         } catch (Throwable var513) {
            var10000 = var513;
            var10001 = false;
            break label4166;
         }

         var5 <<= 8;

         try {
            this.burstMetrics = var5;
         } catch (Throwable var512) {
            var10000 = var512;
            var10001 = false;
            break label4166;
         }

         var5 |= var11 & 255L;

         try {
            this.burstMetrics = var5;
         } catch (Throwable var511) {
            var10000 = var511;
            var10001 = false;
            break label4166;
         }

         var5 <<= 8;

         try {
            this.burstMetrics = var5;
         } catch (Throwable var510) {
            var10000 = var510;
            var10001 = false;
            break label4166;
         }

         var5 |= var7 & 255L;

         try {
            this.burstMetrics = var5;
         } catch (Throwable var509) {
            var10000 = var509;
            var10001 = false;
            break label4166;
         }

         var5 <<= 8;

         try {
            this.burstMetrics = var5;
         } catch (Throwable var508) {
            var10000 = var508;
            var10001 = false;
            break label4166;
         }

         var5 |= var9 & 255L;

         try {
            this.burstMetrics = var5;
         } catch (Throwable var507) {
            var10000 = var507;
            var10001 = false;
            break label4166;
         }

         var5 <<= 8;

         try {
            this.burstMetrics = var5;
         } catch (Throwable var506) {
            var10000 = var506;
            var10001 = false;
            break label4166;
         }

         var5 |= var15 & 65535L;

         try {
            this.burstMetrics = var5;
         } catch (Throwable var505) {
            var10000 = var505;
            var10001 = false;
            break label4166;
         }

         var5 <<= 16;

         label4071:
         try {
            this.burstMetrics = var5;
            this.burstMetrics = var5 | var13 & 65535L;
            this.calculate = false;
            return;
         } catch (Throwable var504) {
            var10000 = var504;
            var10001 = false;
            break label4071;
         }
      }

      Throwable var19 = var10000;
      throw var19;
   }

   public long getBurstMetrics() {
      synchronized(this){}

      long var1;
      try {
         if (this.calculate) {
            this.calculate();
         }

         var1 = this.burstMetrics;
      } finally {
         ;
      }

      return var1;
   }

   public short getGMin() {
      return 16;
   }

   public void update(int var1) {
      Throwable var10000;
      label836: {
         synchronized(this){}
         boolean var10001;
         boolean var93;
         if (var1 == 0) {
            try {
               ++this.lossCount;
            } catch (Throwable var92) {
               var10000 = var92;
               var10001 = false;
               break label836;
            }

            var93 = true;
         } else if (var1 == 8) {
            try {
               ++this.discardCount;
            } catch (Throwable var91) {
               var10000 = var91;
               var10001 = false;
               break label836;
            }

            var93 = true;
         } else {
            try {
               ++this.pkt;
            } catch (Throwable var90) {
               var10000 = var90;
               var10001 = false;
               break label836;
            }

            var93 = false;
         }

         if (!var93) {
            return;
         }

         label840: {
            label838: {
               label841: {
                  try {
                     if (this.pkt < 16L) {
                        break label838;
                     }

                     if (this.lost == 1L) {
                        ++this.c14;
                        break label841;
                     }
                  } catch (Throwable var89) {
                     var10000 = var89;
                     var10001 = false;
                     break label836;
                  }

                  try {
                     ++this.c13;
                     this.lost = 1L;
                  } catch (Throwable var87) {
                     var10000 = var87;
                     var10001 = false;
                     break label836;
                  }
               }

               try {
                  this.c11 += this.pkt;
                  break label840;
               } catch (Throwable var86) {
                  var10000 = var86;
                  var10001 = false;
                  break label836;
               }
            }

            try {
               ++this.lost;
               if (this.pkt == 0L) {
                  ++this.c33;
                  break label840;
               }
            } catch (Throwable var88) {
               var10000 = var88;
               var10001 = false;
               break label836;
            }

            try {
               ++this.c23;
               this.c22 += this.pkt - 1L;
            } catch (Throwable var85) {
               var10000 = var85;
               var10001 = false;
               break label836;
            }
         }

         label795:
         try {
            this.pkt = 0L;
            this.calculate = true;
            return;
         } catch (Throwable var84) {
            var10000 = var84;
            var10001 = false;
            break label795;
         }
      }

      Throwable var2 = var10000;
      throw var2;
   }
}
