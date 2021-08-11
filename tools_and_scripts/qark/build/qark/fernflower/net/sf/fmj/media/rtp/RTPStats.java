package net.sf.fmj.media.rtp;

import javax.media.rtp.ReceptionStats;

public class RTPStats implements ReceptionStats {
   public static final int ADUDROP = 9;
   public static final int ENCODE = 6;
   public static final int PAYLOAD = 5;
   public static final int PDUDROP = 8;
   public static final int PDUDUP = 4;
   public static final int PDUINVALID = 3;
   public static final int PDULOST = 0;
   public static final int PDUMISORD = 2;
   public static final int PDUPROCSD = 1;
   public static final int QSIZE = 7;
   private int ADUDrop = 0;
   private int PDUDrop = 0;
   private final BurstMetrics burstMetrics = new BurstMetrics();
   private String encodeName;
   private int numDup = 0;
   private int numInvalid = 0;
   private int numLost = 0;
   private int numMisord = 0;
   private int numProc = 0;
   private int payload;
   private int qSize = 0;

   public int getADUDrop() {
      return this.ADUDrop;
   }

   public int getBufferSize() {
      return this.qSize;
   }

   public BurstMetrics getBurstMetrics() {
      return this.burstMetrics;
   }

   public String getEncodingName() {
      return this.encodeName;
   }

   public int getPDUDrop() {
      return this.PDUDrop;
   }

   public int getPDUDuplicate() {
      return this.numDup;
   }

   public int getPDUInvalid() {
      return this.numInvalid;
   }

   public int getPDUMisOrd() {
      return this.numMisord;
   }

   public int getPDUProcessed() {
      return this.numProc;
   }

   public int getPDUlost() {
      return this.numLost;
   }

   public int getPayloadType() {
      return this.payload;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("PDULost ");
      var1.append(this.getPDUlost());
      var1.append("\nPDUProcessed ");
      var1.append(this.getPDUProcessed());
      var1.append("\nPDUMisord ");
      var1.append(this.getPDUMisOrd());
      var1.append("\nPDUInvalid ");
      var1.append(this.getPDUInvalid());
      var1.append("\nPDUDuplicate ");
      var1.append(this.getPDUDuplicate());
      return var1.toString();
   }

   public void update(int var1) {
      synchronized(this){}
      Throwable var10000;
      boolean var10001;
      if (var1 != 0) {
         label333: {
            if (var1 != 1) {
               if (var1 != 2) {
                  if (var1 != 3) {
                     if (var1 != 4) {
                        if (var1 != 8) {
                           return;
                        }

                        try {
                           ++this.PDUDrop;
                           this.getBurstMetrics().update(var1);
                        } catch (Throwable var39) {
                           var10000 = var39;
                           var10001 = false;
                           break label333;
                        }
                     } else {
                        try {
                           ++this.numDup;
                        } catch (Throwable var40) {
                           var10000 = var40;
                           var10001 = false;
                           break label333;
                        }
                     }

                     return;
                  } else {
                     try {
                        ++this.numInvalid;
                     } catch (Throwable var41) {
                        var10000 = var41;
                        var10001 = false;
                        break label333;
                     }
                  }

                  return;
               } else {
                  try {
                     ++this.numMisord;
                  } catch (Throwable var42) {
                     var10000 = var42;
                     var10001 = false;
                     break label333;
                  }
               }

               return;
            } else {
               try {
                  ++this.numProc;
                  this.getBurstMetrics().update(var1);
               } catch (Throwable var43) {
                  var10000 = var43;
                  var10001 = false;
                  break label333;
               }
            }

            return;
         }
      } else {
         label325:
         try {
            ++this.numLost;
            this.getBurstMetrics().update(var1);
            return;
         } catch (Throwable var44) {
            var10000 = var44;
            var10001 = false;
            break label325;
         }
      }

      Throwable var2 = var10000;
      throw var2;
   }

   public void update(int var1, int var2) {
      synchronized(this){}
      Throwable var10000;
      boolean var10001;
      if (var1 != 0) {
         label384: {
            if (var1 != 5) {
               if (var1 != 7) {
                  if (var1 != 8) {
                     if (var1 != 9) {
                        return;
                     }

                     try {
                        this.ADUDrop = var2;
                     } catch (Throwable var40) {
                        var10000 = var40;
                        var10001 = false;
                        break label384;
                     }
                  } else {
                     try {
                        this.PDUDrop = var2;
                     } catch (Throwable var41) {
                        var10000 = var41;
                        var10001 = false;
                        break label384;
                     }
                  }

                  return;
               } else {
                  try {
                     this.qSize = var2;
                  } catch (Throwable var42) {
                     var10000 = var42;
                     var10001 = false;
                     break label384;
                  }
               }

               return;
            } else {
               try {
                  this.payload = var2;
               } catch (Throwable var43) {
                  var10000 = var43;
                  var10001 = false;
                  break label384;
               }
            }

            return;
         }
      } else if (var2 > 0) {
         while(true) {
            if (var2 <= 0) {
               return;
            }

            try {
               this.update(var1);
            } catch (Throwable var44) {
               var10000 = var44;
               var10001 = false;
               break;
            }

            --var2;
         }
      } else {
         label374:
         try {
            this.numLost += var2;
            return;
         } catch (Throwable var45) {
            var10000 = var45;
            var10001 = false;
            break label374;
         }
      }

      Throwable var3 = var10000;
      throw var3;
   }

   public void update(int var1, String var2) {
      synchronized(this){}
      if (var1 == 6) {
         try {
            this.encodeName = var2;
         } finally {
            ;
         }
      }

   }
}
