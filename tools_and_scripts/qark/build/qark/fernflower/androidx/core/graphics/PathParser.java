package androidx.core.graphics;

import android.graphics.Path;
import android.util.Log;
import java.util.ArrayList;

public class PathParser {
   private static final String LOGTAG = "PathParser";

   private PathParser() {
   }

   private static void addNode(ArrayList var0, char var1, float[] var2) {
      var0.add(new PathParser.PathDataNode(var1, var2));
   }

   public static boolean canMorph(PathParser.PathDataNode[] var0, PathParser.PathDataNode[] var1) {
      if (var0 != null) {
         if (var1 == null) {
            return false;
         } else if (var0.length != var1.length) {
            return false;
         } else {
            for(int var2 = 0; var2 < var0.length; ++var2) {
               if (var0[var2].mType != var1[var2].mType) {
                  return false;
               }

               if (var0[var2].mParams.length != var1[var2].mParams.length) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   static float[] copyOfRange(float[] var0, int var1, int var2) {
      if (var1 <= var2) {
         int var3 = var0.length;
         if (var1 >= 0 && var1 <= var3) {
            var2 -= var1;
            var3 = Math.min(var2, var3 - var1);
            float[] var4 = new float[var2];
            System.arraycopy(var0, var1, var4, 0, var3);
            return var4;
         } else {
            throw new ArrayIndexOutOfBoundsException();
         }
      } else {
         throw new IllegalArgumentException();
      }
   }

   public static PathParser.PathDataNode[] createNodesFromPathData(String var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = 0;
         int var2 = 1;

         ArrayList var3;
         for(var3 = new ArrayList(); var2 < var0.length(); var1 = var2++) {
            var2 = nextStart(var0, var2);
            String var4 = var0.substring(var1, var2).trim();
            if (var4.length() > 0) {
               float[] var5 = getFloats(var4);
               addNode(var3, var4.charAt(0), var5);
            }
         }

         if (var2 - var1 == 1 && var1 < var0.length()) {
            addNode(var3, var0.charAt(var1), new float[0]);
         }

         return (PathParser.PathDataNode[])var3.toArray(new PathParser.PathDataNode[var3.size()]);
      }
   }

   public static Path createPathFromPathData(String var0) {
      Path var1 = new Path();
      PathParser.PathDataNode[] var2 = createNodesFromPathData(var0);
      if (var2 != null) {
         try {
            PathParser.PathDataNode.nodesToPath(var2, var1);
            return var1;
         } catch (RuntimeException var3) {
            StringBuilder var4 = new StringBuilder();
            var4.append("Error in parsing ");
            var4.append(var0);
            throw new RuntimeException(var4.toString(), var3);
         }
      } else {
         return null;
      }
   }

   public static PathParser.PathDataNode[] deepCopyNodes(PathParser.PathDataNode[] var0) {
      if (var0 == null) {
         return null;
      } else {
         PathParser.PathDataNode[] var2 = new PathParser.PathDataNode[var0.length];

         for(int var1 = 0; var1 < var0.length; ++var1) {
            var2[var1] = new PathParser.PathDataNode(var0[var1]);
         }

         return var2;
      }
   }

   private static void extract(String var0, int var1, PathParser.ExtractFloatResult var2) {
      int var6 = var1;
      boolean var7 = false;
      var2.mEndWithNegOrDot = false;
      boolean var5 = false;

      boolean var4;
      for(boolean var8 = false; var6 < var0.length(); var8 = var4) {
         boolean var9;
         boolean var11;
         label40: {
            boolean var10 = false;
            char var3 = var0.charAt(var6);
            if (var3 != ' ') {
               if (var3 == 'E' || var3 == 'e') {
                  var4 = true;
                  var11 = var7;
                  var9 = var5;
                  break label40;
               }

               switch(var3) {
               case ',':
                  break;
               case '-':
                  var11 = var7;
                  var9 = var5;
                  var4 = var10;
                  if (var6 != var1) {
                     var11 = var7;
                     var9 = var5;
                     var4 = var10;
                     if (!var8) {
                        var11 = true;
                        var2.mEndWithNegOrDot = true;
                        var9 = var5;
                        var4 = var10;
                     }
                  }
                  break label40;
               case '.':
                  if (!var5) {
                     var9 = true;
                     var11 = var7;
                     var4 = var10;
                  } else {
                     var11 = true;
                     var2.mEndWithNegOrDot = true;
                     var9 = var5;
                     var4 = var10;
                  }
                  break label40;
               default:
                  var11 = var7;
                  var9 = var5;
                  var4 = var10;
                  break label40;
               }
            }

            var11 = true;
            var4 = var10;
            var9 = var5;
         }

         if (var11) {
            break;
         }

         ++var6;
         var7 = var11;
         var5 = var9;
      }

      var2.mEndPosition = var6;
   }

   private static float[] getFloats(String var0) {
      if (var0.charAt(0) != 'z' && var0.charAt(0) != 'Z') {
         NumberFormatException var10000;
         label66: {
            float[] var6;
            boolean var10001;
            try {
               var6 = new float[var0.length()];
            } catch (NumberFormatException var13) {
               var10000 = var13;
               var10001 = false;
               break label66;
            }

            int var2 = 0;
            int var1 = 1;

            int var5;
            PathParser.ExtractFloatResult var7;
            try {
               var7 = new PathParser.ExtractFloatResult();
               var5 = var0.length();
            } catch (NumberFormatException var11) {
               var10000 = var11;
               var10001 = false;
               break label66;
            }

            while(var1 < var5) {
               int var4;
               try {
                  extract(var0, var1, var7);
                  var4 = var7.mEndPosition;
               } catch (NumberFormatException var10) {
                  var10000 = var10;
                  var10001 = false;
                  break label66;
               }

               int var3 = var2;
               if (var1 < var4) {
                  try {
                     var6[var2] = Float.parseFloat(var0.substring(var1, var4));
                  } catch (NumberFormatException var9) {
                     var10000 = var9;
                     var10001 = false;
                     break label66;
                  }

                  var3 = var2 + 1;
               }

               label52: {
                  try {
                     if (!var7.mEndWithNegOrDot) {
                        break label52;
                     }
                  } catch (NumberFormatException var12) {
                     var10000 = var12;
                     var10001 = false;
                     break label66;
                  }

                  var1 = var4;
                  var2 = var3;
                  continue;
               }

               var1 = var4 + 1;
               var2 = var3;
            }

            try {
               var6 = copyOfRange(var6, 0, var2);
               return var6;
            } catch (NumberFormatException var8) {
               var10000 = var8;
               var10001 = false;
            }
         }

         NumberFormatException var14 = var10000;
         StringBuilder var15 = new StringBuilder();
         var15.append("error in parsing \"");
         var15.append(var0);
         var15.append("\"");
         throw new RuntimeException(var15.toString(), var14);
      } else {
         return new float[0];
      }
   }

   public static boolean interpolatePathDataNodes(PathParser.PathDataNode[] var0, PathParser.PathDataNode[] var1, PathParser.PathDataNode[] var2, float var3) {
      if (var0 != null && var1 != null && var2 != null) {
         if (var0.length == var1.length && var1.length == var2.length) {
            if (!canMorph(var1, var2)) {
               return false;
            } else {
               for(int var4 = 0; var4 < var0.length; ++var4) {
                  var0[var4].interpolatePathDataNode(var1[var4], var2[var4], var3);
               }

               return true;
            }
         } else {
            throw new IllegalArgumentException("The nodes to be interpolated and resulting nodes must have the same length");
         }
      } else {
         throw new IllegalArgumentException("The nodes to be interpolated and resulting nodes cannot be null");
      }
   }

   private static int nextStart(String var0, int var1) {
      while(var1 < var0.length()) {
         char var2 = var0.charAt(var1);
         if (((var2 - 65) * (var2 - 90) <= 0 || (var2 - 97) * (var2 - 122) <= 0) && var2 != 'e' && var2 != 'E') {
            return var1;
         }

         ++var1;
      }

      return var1;
   }

   public static void updateNodes(PathParser.PathDataNode[] var0, PathParser.PathDataNode[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         var0[var2].mType = var1[var2].mType;

         for(int var3 = 0; var3 < var1[var2].mParams.length; ++var3) {
            var0[var2].mParams[var3] = var1[var2].mParams[var3];
         }
      }

   }

   private static class ExtractFloatResult {
      int mEndPosition;
      boolean mEndWithNegOrDot;

      ExtractFloatResult() {
      }
   }

   public static class PathDataNode {
      public float[] mParams;
      public char mType;

      PathDataNode(char var1, float[] var2) {
         this.mType = var1;
         this.mParams = var2;
      }

      PathDataNode(PathParser.PathDataNode var1) {
         this.mType = var1.mType;
         float[] var2 = var1.mParams;
         this.mParams = PathParser.copyOfRange(var2, 0, var2.length);
      }

      private static void addCommand(Path var0, float[] var1, char var2, char var3, float[] var4) {
         float var8 = var1[0];
         float var7 = var1[1];
         float var9 = var1[2];
         float var10 = var1[3];
         float var6 = var1[4];
         float var5 = var1[5];
         byte var14;
         switch(var3) {
         case 'A':
         case 'a':
            var14 = 7;
            break;
         case 'C':
         case 'c':
            var14 = 6;
            break;
         case 'H':
         case 'V':
         case 'h':
         case 'v':
            var14 = 1;
            break;
         case 'L':
         case 'M':
         case 'T':
         case 'l':
         case 'm':
         case 't':
            var14 = 2;
            break;
         case 'Q':
         case 'S':
         case 'q':
         case 's':
            var14 = 4;
            break;
         case 'Z':
         case 'z':
            var0.close();
            var8 = var6;
            var7 = var5;
            var9 = var6;
            var10 = var5;
            var0.moveTo(var6, var5);
            var14 = 2;
            break;
         default:
            var14 = 2;
         }

         byte var16 = 0;
         float var12 = var9;
         float var11 = var10;
         var10 = var5;
         var5 = var7;
         var9 = var6;
         char var15 = var2;
         int var19 = var16;

         for(var6 = var8; var19 < var4.length; var11 = var8) {
            float var13;
            boolean var17;
            boolean var18;
            if (var3 != 'A') {
               if (var3 == 'C') {
                  var0.cubicTo(var4[var19 + 0], var4[var19 + 1], var4[var19 + 2], var4[var19 + 3], var4[var19 + 4], var4[var19 + 5]);
                  var6 = var4[var19 + 4];
                  var5 = var4[var19 + 5];
                  var7 = var4[var19 + 2];
                  var8 = var4[var19 + 3];
               } else if (var3 != 'H') {
                  if (var3 == 'Q') {
                     var0.quadTo(var4[var19 + 0], var4[var19 + 1], var4[var19 + 2], var4[var19 + 3]);
                     var7 = var4[var19 + 0];
                     var8 = var4[var19 + 1];
                     var6 = var4[var19 + 2];
                     var5 = var4[var19 + 3];
                  } else if (var3 != 'V') {
                     if (var3 == 'a') {
                        var7 = var4[var19 + 5];
                        var8 = var4[var19 + 6];
                        var11 = var4[var19 + 0];
                        var12 = var4[var19 + 1];
                        var13 = var4[var19 + 2];
                        if (var4[var19 + 3] != 0.0F) {
                           var17 = true;
                        } else {
                           var17 = false;
                        }

                        if (var4[var19 + 4] != 0.0F) {
                           var18 = true;
                        } else {
                           var18 = false;
                        }

                        drawArc(var0, var6, var5, var7 + var6, var8 + var5, var11, var12, var13, var17, var18);
                        var6 += var4[var19 + 5];
                        var5 += var4[var19 + 6];
                        var7 = var6;
                        var8 = var5;
                     } else if (var3 != 'c') {
                        if (var3 == 'h') {
                           var0.rLineTo(var4[var19 + 0], 0.0F);
                           var6 += var4[var19 + 0];
                           var7 = var12;
                           var8 = var11;
                        } else if (var3 != 'q') {
                           if (var3 == 'v') {
                              var0.rLineTo(0.0F, var4[var19 + 0]);
                              var5 += var4[var19 + 0];
                              var7 = var12;
                              var8 = var11;
                           } else if (var3 != 'L') {
                              if (var3 == 'M') {
                                 var6 = var4[var19 + 0];
                                 var5 = var4[var19 + 1];
                                 if (var19 > 0) {
                                    var0.lineTo(var4[var19 + 0], var4[var19 + 1]);
                                    var7 = var12;
                                    var8 = var11;
                                 } else {
                                    var0.moveTo(var4[var19 + 0], var4[var19 + 1]);
                                    var9 = var6;
                                    var10 = var5;
                                    var5 = var5;
                                    var6 = var6;
                                    var7 = var12;
                                    var8 = var11;
                                 }
                              } else if (var3 != 'S') {
                                 if (var3 != 'T') {
                                    if (var3 != 'l') {
                                       if (var3 == 'm') {
                                          var6 += var4[var19 + 0];
                                          var5 += var4[var19 + 1];
                                          if (var19 > 0) {
                                             var0.rLineTo(var4[var19 + 0], var4[var19 + 1]);
                                             var7 = var12;
                                             var8 = var11;
                                          } else {
                                             var0.rMoveTo(var4[var19 + 0], var4[var19 + 1]);
                                             var9 = var6;
                                             var10 = var5;
                                             var7 = var12;
                                             var8 = var11;
                                          }
                                       } else if (var3 != 's') {
                                          if (var3 != 't') {
                                             var7 = var12;
                                             var8 = var11;
                                          } else {
                                             var8 = 0.0F;
                                             var7 = 0.0F;
                                             if (var15 == 'q' || var15 == 't' || var15 == 'Q' || var15 == 'T') {
                                                var8 = var6 - var12;
                                                var7 = var5 - var11;
                                             }

                                             var0.rQuadTo(var8, var7, var4[var19 + 0], var4[var19 + 1]);
                                             var11 = var6 + var4[var19 + 0];
                                             var12 = var5 + var4[var19 + 1];
                                             var8 += var6;
                                             var13 = var5 + var7;
                                             var5 = var12;
                                             var6 = var11;
                                             var7 = var8;
                                             var8 = var13;
                                          }
                                       } else {
                                          if (var15 != 'c' && var15 != 's' && var15 != 'C' && var15 != 'S') {
                                             var7 = 0.0F;
                                             var8 = 0.0F;
                                          } else {
                                             var7 = var6 - var12;
                                             var8 = var5 - var11;
                                          }

                                          var0.rCubicTo(var7, var8, var4[var19 + 0], var4[var19 + 1], var4[var19 + 2], var4[var19 + 3]);
                                          var8 = var4[var19 + 0];
                                          var11 = var4[var19 + 1];
                                          var7 = var6 + var4[var19 + 2];
                                          var12 = var4[var19 + 3];
                                          var8 += var6;
                                          var11 += var5;
                                          var5 += var12;
                                          var6 = var7;
                                          var7 = var8;
                                          var8 = var11;
                                       }
                                    } else {
                                       var0.rLineTo(var4[var19 + 0], var4[var19 + 1]);
                                       var6 += var4[var19 + 0];
                                       var5 += var4[var19 + 1];
                                       var7 = var12;
                                       var8 = var11;
                                    }
                                 } else {
                                    var8 = var6;
                                    var7 = var5;
                                    if (var15 == 'q' || var15 == 't' || var15 == 'Q' || var15 == 'T') {
                                       var8 = var6 * 2.0F - var12;
                                       var7 = var5 * 2.0F - var11;
                                    }

                                    var0.quadTo(var8, var7, var4[var19 + 0], var4[var19 + 1]);
                                    var6 = var4[var19 + 0];
                                    var5 = var4[var19 + 1];
                                    var11 = var7;
                                    var7 = var8;
                                    var8 = var11;
                                 }
                              } else {
                                 if (var15 == 'c' || var15 == 's' || var15 == 'C' || var15 == 'S') {
                                    var6 = var6 * 2.0F - var12;
                                    var5 = var5 * 2.0F - var11;
                                 }

                                 var0.cubicTo(var6, var5, var4[var19 + 0], var4[var19 + 1], var4[var19 + 2], var4[var19 + 3]);
                                 var7 = var4[var19 + 0];
                                 var8 = var4[var19 + 1];
                                 var6 = var4[var19 + 2];
                                 var5 = var4[var19 + 3];
                              }
                           } else {
                              var0.lineTo(var4[var19 + 0], var4[var19 + 1]);
                              var6 = var4[var19 + 0];
                              var5 = var4[var19 + 1];
                              var7 = var12;
                              var8 = var11;
                           }
                        } else {
                           var0.rQuadTo(var4[var19 + 0], var4[var19 + 1], var4[var19 + 2], var4[var19 + 3]);
                           var8 = var4[var19 + 0];
                           var11 = var4[var19 + 1];
                           var7 = var6 + var4[var19 + 2];
                           var12 = var4[var19 + 3];
                           var8 += var6;
                           var11 += var5;
                           var5 += var12;
                           var6 = var7;
                           var7 = var8;
                           var8 = var11;
                        }
                     } else {
                        var0.rCubicTo(var4[var19 + 0], var4[var19 + 1], var4[var19 + 2], var4[var19 + 3], var4[var19 + 4], var4[var19 + 5]);
                        var8 = var4[var19 + 2];
                        var11 = var4[var19 + 3];
                        var7 = var6 + var4[var19 + 4];
                        var12 = var4[var19 + 5];
                        var8 += var6;
                        var11 += var5;
                        var5 += var12;
                        var6 = var7;
                        var7 = var8;
                        var8 = var11;
                     }
                  } else {
                     var0.lineTo(var6, var4[var19 + 0]);
                     var5 = var4[var19 + 0];
                     var7 = var12;
                     var8 = var11;
                  }
               } else {
                  var0.lineTo(var4[var19 + 0], var5);
                  var6 = var4[var19 + 0];
                  var7 = var12;
                  var8 = var11;
               }
            } else {
               var7 = var4[var19 + 5];
               var8 = var4[var19 + 6];
               var11 = var4[var19 + 0];
               var12 = var4[var19 + 1];
               var13 = var4[var19 + 2];
               if (var4[var19 + 3] != 0.0F) {
                  var17 = true;
               } else {
                  var17 = false;
               }

               if (var4[var19 + 4] != 0.0F) {
                  var18 = true;
               } else {
                  var18 = false;
               }

               drawArc(var0, var6, var5, var7, var8, var11, var12, var13, var17, var18);
               var7 = var4[var19 + 5];
               var8 = var4[var19 + 6];
               var6 = var7;
               var5 = var8;
            }

            var15 = var3;
            var19 += var14;
            var12 = var7;
         }

         var1[0] = var6;
         var1[1] = var5;
         var1[2] = var12;
         var1[3] = var11;
         var1[4] = var9;
         var1[5] = var10;
      }

      private static void arcToBezier(Path var0, double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17) {
         int var43 = (int)Math.ceil(Math.abs(var17 * 4.0D / 3.141592653589793D));
         double var29 = Math.cos(var13);
         double var31 = Math.sin(var13);
         var13 = Math.cos(var15);
         double var33 = Math.sin(var15);
         double var21 = -var5;
         double var23 = -var5 * var31 * var33 + var7 * var29 * var13;
         var17 /= (double)var43;
         int var44 = 0;
         double var19 = var9;
         double var25 = var21 * var29 * var33 - var7 * var31 * var13;
         double var27 = var15;
         var21 = var11;
         var9 = var31;

         for(var11 = var29; var44 < var43; ++var44) {
            double var35 = var27 + var17;
            double var39 = Math.sin(var35);
            double var41 = Math.cos(var35);
            var29 = var1 + var5 * var11 * var41 - var7 * var9 * var39;
            var33 = var3 + var5 * var9 * var41 + var7 * var11 * var39;
            var31 = -var5 * var11 * var39 - var7 * var9 * var41;
            double var37 = -var5 * var9 * var39 + var7 * var11 * var41;
            var39 = Math.tan((var35 - var27) / 2.0D);
            var27 = Math.sin(var35 - var27) * (Math.sqrt(var39 * 3.0D * var39 + 4.0D) - 1.0D) / 3.0D;
            var0.rLineTo(0.0F, 0.0F);
            var0.cubicTo((float)(var19 + var27 * var25), (float)(var21 + var27 * var23), (float)(var29 - var27 * var31), (float)(var33 - var27 * var37), (float)var29, (float)var33);
            var27 = var35;
            var21 = var33;
            var25 = var31;
            var23 = var37;
            var19 = var29;
         }

      }

      private static void drawArc(Path var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, boolean var8, boolean var9) {
         double var18 = Math.toRadians((double)var7);
         double var20 = Math.cos(var18);
         double var22 = Math.sin(var18);
         double var24 = ((double)var1 * var20 + (double)var2 * var22) / (double)var5;
         double var26 = ((double)(-var1) * var22 + (double)var2 * var20) / (double)var6;
         double var10 = ((double)var3 * var20 + (double)var4 * var22) / (double)var5;
         double var16 = ((double)(-var3) * var22 + (double)var4 * var20) / (double)var6;
         double var30 = var24 - var10;
         double var28 = var26 - var16;
         double var14 = (var24 + var10) / 2.0D;
         double var12 = (var26 + var16) / 2.0D;
         double var32 = var30 * var30 + var28 * var28;
         if (var32 == 0.0D) {
            Log.w("PathParser", " Points are coincident");
         } else {
            double var34 = 1.0D / var32 - 0.25D;
            if (var34 < 0.0D) {
               StringBuilder var37 = new StringBuilder();
               var37.append("Points are too far apart ");
               var37.append(var32);
               Log.w("PathParser", var37.toString());
               float var36 = (float)(Math.sqrt(var32) / 1.99999D);
               drawArc(var0, var1, var2, var3, var4, var5 * var36, var6 * var36, var7, var8, var9);
            } else {
               var32 = Math.sqrt(var34);
               var30 = var32 * var30;
               var28 = var32 * var28;
               if (var8 == var9) {
                  var14 -= var28;
                  var12 += var30;
               } else {
                  var14 += var28;
                  var12 -= var30;
               }

               var24 = Math.atan2(var26 - var12, var24 - var14);
               var16 = Math.atan2(var16 - var12, var10 - var14) - var24;
               if (var16 >= 0.0D) {
                  var8 = true;
               } else {
                  var8 = false;
               }

               var10 = var16;
               if (var9 != var8) {
                  if (var16 > 0.0D) {
                     var10 = var16 - 6.283185307179586D;
                  } else {
                     var10 = var16 + 6.283185307179586D;
                  }
               }

               var14 *= (double)var5;
               var12 = (double)var6 * var12;
               arcToBezier(var0, var14 * var20 - var12 * var22, var14 * var22 + var12 * var20, (double)var5, (double)var6, (double)var1, (double)var2, var18, var24, var10);
            }
         }
      }

      public static void nodesToPath(PathParser.PathDataNode[] var0, Path var1) {
         float[] var4 = new float[6];
         char var2 = 'm';

         for(int var3 = 0; var3 < var0.length; ++var3) {
            addCommand(var1, var4, var2, var0[var3].mType, var0[var3].mParams);
            var2 = var0[var3].mType;
         }

      }

      public void interpolatePathDataNode(PathParser.PathDataNode var1, PathParser.PathDataNode var2, float var3) {
         this.mType = var1.mType;
         int var4 = 0;

         while(true) {
            float[] var5 = var1.mParams;
            if (var4 >= var5.length) {
               return;
            }

            this.mParams[var4] = var5[var4] * (1.0F - var3) + var2.mParams[var4] * var3;
            ++var4;
         }
      }
   }
}
