package com.google.protobuf;

final class Utf8 {
   public static final int COMPLETE = 0;
   public static final int MALFORMED = -1;

   private Utf8() {
   }

   private static int incompleteStateFor(int var0) {
      return var0 > -12 ? -1 : var0;
   }

   private static int incompleteStateFor(int var0, int var1) {
      return var0 <= -12 && var1 <= -65 ? var1 << 8 ^ var0 : -1;
   }

   private static int incompleteStateFor(int var0, int var1, int var2) {
      return var0 <= -12 && var1 <= -65 && var2 <= -65 ? var1 << 8 ^ var0 ^ var2 << 16 : -1;
   }

   private static int incompleteStateFor(byte[] var0, int var1, int var2) {
      byte var3 = var0[var1 - 1];
      var2 -= var1;
      if (var2 != 0) {
         if (var2 != 1) {
            if (var2 == 2) {
               return incompleteStateFor(var3, var0[var1], var0[var1 + 1]);
            } else {
               throw new AssertionError();
            }
         } else {
            return incompleteStateFor(var3, var0[var1]);
         }
      } else {
         return incompleteStateFor(var3);
      }
   }

   public static boolean isValidUtf8(byte[] var0) {
      return isValidUtf8(var0, 0, var0.length);
   }

   public static boolean isValidUtf8(byte[] var0, int var1, int var2) {
      return partialIsValidUtf8(var0, var1, var2) == 0;
   }

   public static int partialIsValidUtf8(int var0, byte[] var1, int var2, int var3) {
      int var4 = var2;
      if (var0 != 0) {
         if (var2 >= var3) {
            return var0;
         }

         byte var7 = (byte)var0;
         if (var7 < -32) {
            if (var7 < -62) {
               return -1;
            }

            if (var1[var2] > -65) {
               return -1;
            }

            var4 = var2 + 1;
         } else {
            byte var9;
            if (var7 < -16) {
               byte var11 = (byte)(var0 >> 8);
               var9 = var11;
               var0 = var2;
               if (var11 == 0) {
                  var0 = var2 + 1;
                  var9 = var1[var2];
                  if (var0 >= var3) {
                     return incompleteStateFor(var7, var9);
                  }
               }

               if (var9 > -65 || var7 == -32 && var9 < -96 || var7 == -19 && var9 >= -96) {
                  return -1;
               }

               if (var1[var0] > -65) {
                  return -1;
               }

               var4 = var0 + 1;
            } else {
               var9 = (byte)(var0 >> 8);
               byte var5 = 0;
               byte var8;
               if (var9 == 0) {
                  var0 = var2 + 1;
                  var9 = var1[var2];
                  if (var0 >= var3) {
                     return incompleteStateFor(var7, var9);
                  }

                  var2 = var0;
                  var8 = var5;
               } else {
                  var8 = (byte)(var0 >> 16);
               }

               byte var6 = var8;
               int var10 = var2;
               if (var8 == 0) {
                  var10 = var2 + 1;
                  var6 = var1[var2];
                  if (var10 >= var3) {
                     return incompleteStateFor(var7, var9, var6);
                  }
               }

               if (var9 > -65 || (var7 << 28) + var9 + 112 >> 30 != 0 || var6 > -65) {
                  return -1;
               }

               if (var1[var10] > -65) {
                  return -1;
               }

               var4 = var10 + 1;
            }
         }
      }

      return partialIsValidUtf8(var1, var4, var3);
   }

   public static int partialIsValidUtf8(byte[] var0, int var1, int var2) {
      while(var1 < var2 && var0[var1] >= 0) {
         ++var1;
      }

      if (var1 >= var2) {
         return 0;
      } else {
         return partialIsValidUtf8NonAscii(var0, var1, var2);
      }
   }

   private static int partialIsValidUtf8NonAscii(byte[] var0, int var1, int var2) {
      while(var1 < var2) {
         int var3 = var1 + 1;
         byte var5 = var0[var1];
         if (var5 < 0) {
            if (var5 < -32) {
               if (var3 >= var2) {
                  return var5;
               }

               if (var5 >= -62) {
                  var1 = var3 + 1;
                  if (var0[var3] <= -65) {
                     continue;
                  }
               }

               return -1;
            } else {
               int var4;
               byte var6;
               if (var5 < -16) {
                  if (var3 >= var2 - 1) {
                     return incompleteStateFor(var0, var3, var2);
                  }

                  var4 = var3 + 1;
                  var6 = var0[var3];
                  if (var6 <= -65 && (var5 != -32 || var6 >= -96) && (var5 != -19 || var6 < -96)) {
                     var1 = var4 + 1;
                     if (var0[var4] <= -65) {
                        continue;
                     }
                  }

                  return -1;
               } else {
                  if (var3 >= var2 - 2) {
                     return incompleteStateFor(var0, var3, var2);
                  }

                  var4 = var3 + 1;
                  var6 = var0[var3];
                  if (var6 <= -65 && (var5 << 28) + var6 + 112 >> 30 == 0) {
                     var3 = var4 + 1;
                     if (var0[var4] > -65) {
                        return -1;
                     }

                     var1 = var3 + 1;
                     if (var0[var3] <= -65) {
                        continue;
                     }
                  }

                  return -1;
               }
            }
         } else {
            var1 = var3;
         }
      }

      return 0;
   }
}
