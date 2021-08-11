package org.apache.commons.text.diff;

public class StringsComparator {
   private final String left;
   private final String right;
   private final int[] vDown;
   private final int[] vUp;

   public StringsComparator(String var1, String var2) {
      this.left = var1;
      this.right = var2;
      int var3 = var1.length() + var2.length() + 2;
      this.vDown = new int[var3];
      this.vUp = new int[var3];
   }

   private void buildScript(int var1, int var2, int var3, int var4, EditScript var5) {
      StringsComparator.Snake var8 = this.getMiddleSnake(var1, var2, var3, var4);
      if (var8 == null || var8.getStart() == var2 && var8.getDiag() == var2 - var4 || var8.getEnd() == var1 && var8.getDiag() == var1 - var3) {
         int var7 = var1;
         int var6 = var3;

         while(true) {
            while(var7 < var2 || var6 < var4) {
               if (var7 < var2 && var6 < var4 && this.left.charAt(var7) == this.right.charAt(var6)) {
                  var5.append(new KeepCommand(this.left.charAt(var7)));
                  ++var7;
                  ++var6;
               } else if (var2 - var1 > var4 - var3) {
                  var5.append(new DeleteCommand(this.left.charAt(var7)));
                  ++var7;
               } else {
                  var5.append(new InsertCommand(this.right.charAt(var6)));
                  ++var6;
               }
            }

            return;
         }
      } else {
         this.buildScript(var1, var8.getStart(), var3, var8.getStart() - var8.getDiag(), var5);

         for(var1 = var8.getStart(); var1 < var8.getEnd(); ++var1) {
            var5.append(new KeepCommand(this.left.charAt(var1)));
         }

         this.buildScript(var8.getEnd(), var2, var8.getEnd() - var8.getDiag(), var4, var5);
      }
   }

   private StringsComparator.Snake buildSnake(int var1, int var2, int var3, int var4) {
      int var5;
      for(var5 = var1; var5 - var2 < var4 && var5 < var3 && this.left.charAt(var5) == this.right.charAt(var5 - var2); ++var5) {
      }

      return new StringsComparator.Snake(var1, var5, var2);
   }

   private StringsComparator.Snake getMiddleSnake(int var1, int var2, int var3, int var4) {
      int var8 = var2 - var1;
      int var6 = var4 - var3;
      if (var8 != 0 && var6 != 0) {
         int var11 = var8 - var6;
         int var5 = var6 + var8;
         if (var5 % 2 != 0) {
            ++var5;
         }

         int var12 = var5 / 2;
         this.vDown[var12 + 1] = var1;
         this.vUp[var12 + 1] = var2 + 1;

         for(int var7 = 0; var7 <= var12; ++var7) {
            int var9;
            int var10;
            int var13;
            int[] var14;
            for(var8 = -var7; var8 <= var7; var8 += 2) {
               label132: {
                  var13 = var8 + var12;
                  if (var8 != -var7) {
                     label131: {
                        if (var8 != var7) {
                           var14 = this.vDown;
                           if (var14[var13 - 1] < var14[var13 + 1]) {
                              break label131;
                           }
                        }

                        var14 = this.vDown;
                        var14[var13] = var14[var13 - 1] + 1;
                        break label132;
                     }
                  }

                  var14 = this.vDown;
                  var14[var13] = var14[var13 + 1];
               }

               var10 = this.vDown[var13];

               for(var9 = var10 - var1 + var3 - var8; var10 < var2 && var9 < var4 && this.left.charAt(var10) == this.right.charAt(var9); ++var9) {
                  var14 = this.vDown;
                  ++var10;
                  var14[var13] = var10;
               }

               if (var11 % 2 != 0 && var11 - var7 <= var8 && var8 <= var11 + var7) {
                  var14 = this.vUp;
                  if (var14[var13 - var11] <= this.vDown[var13]) {
                     return this.buildSnake(var14[var13 - var11], var8 + var1 - var3, var2, var4);
                  }
               }
            }

            for(var8 = var11 - var7; var8 <= var11 + var7; var8 += 2) {
               label103: {
                  label102: {
                     var13 = var8 + var12 - var11;
                     if (var8 != var11 - var7) {
                        if (var8 == var11 + var7) {
                           break label102;
                        }

                        var14 = this.vUp;
                        if (var14[var13 + 1] > var14[var13 - 1]) {
                           break label102;
                        }
                     }

                     var14 = this.vUp;
                     var14[var13] = var14[var13 + 1] - 1;
                     break label103;
                  }

                  var14 = this.vUp;
                  var14[var13] = var14[var13 - 1];
               }

               var9 = this.vUp[var13] - 1;

               for(var10 = var9 - var1 + var3 - var8; var9 >= var1 && var10 >= var3 && this.left.charAt(var9) == this.right.charAt(var10); --var9) {
                  this.vUp[var13] = var9;
                  --var10;
               }

               if (var11 % 2 == 0 && -var7 <= var8 && var8 <= var7) {
                  var14 = this.vUp;
                  if (var14[var13] <= this.vDown[var13 + var11]) {
                     return this.buildSnake(var14[var13], var8 + var1 - var3, var2, var4);
                  }
               }
            }
         }

         throw new RuntimeException("Internal Error");
      } else {
         return null;
      }
   }

   public EditScript getScript() {
      EditScript var1 = new EditScript();
      this.buildScript(0, this.left.length(), 0, this.right.length(), var1);
      return var1;
   }

   private static class Snake {
      private final int diag;
      private final int end;
      private final int start;

      Snake(int var1, int var2, int var3) {
         this.start = var1;
         this.end = var2;
         this.diag = var3;
      }

      public int getDiag() {
         return this.diag;
      }

      public int getEnd() {
         return this.end;
      }

      public int getStart() {
         return this.start;
      }
   }
}
