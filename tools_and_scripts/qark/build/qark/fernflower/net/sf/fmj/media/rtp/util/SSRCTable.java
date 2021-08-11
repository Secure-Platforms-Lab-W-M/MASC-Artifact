package net.sf.fmj.media.rtp.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.NoSuchElementException;

public class SSRCTable {
   static final int INCR = 16;
   Object[] objList;
   int[] ssrcList;
   int total;

   public SSRCTable() {
      int[] var1 = new int[16];
      this.ssrcList = var1;
      this.objList = new Object[var1.length];
      this.total = 0;
   }

   private int indexOf(int var1) {
      int var3 = this.total;
      byte var2 = -1;
      if (var3 <= 3) {
         if (var3 > 0 && this.ssrcList[0] == var1) {
            return 0;
         } else if (this.total > 1 && this.ssrcList[1] == var1) {
            return 1;
         } else {
            if (this.total > 2) {
               if (this.ssrcList[2] != var1) {
                  return -1;
               }

               var2 = 2;
            }

            return var2;
         }
      } else {
         int[] var6 = this.ssrcList;
         if (var6[0] == var1) {
            return 0;
         } else if (var6[var3 - 1] == var1) {
            return var3 - 1;
         } else {
            int var7 = 0;
            int var4 = var3 - 1;

            while(true) {
               int var5 = (var4 - var7) / 2 + var7;
               var6 = this.ssrcList;
               if (var6[var5] == var1) {
                  return var5;
               }

               if (var1 > var6[var5]) {
                  var3 = var5 + 1;
               } else {
                  var3 = var7;
                  if (var1 < var6[var5]) {
                     var4 = var5;
                     var3 = var7;
                  }
               }

               if (var3 >= var4) {
                  return -1;
               }

               var7 = var3;
            }
         }
      }
   }

   public Enumeration elements() {
      return new Enumeration() {
         private int next = 0;

         public boolean hasMoreElements() {
            return this.next < SSRCTable.this.total;
         }

         public Object nextElement() {
            SSRCTable var2 = SSRCTable.this;
            synchronized(var2){}

            Throwable var10000;
            boolean var10001;
            label214: {
               int var1;
               Object[] var3;
               label215: {
                  try {
                     if (this.next < SSRCTable.this.total) {
                        var3 = SSRCTable.this.objList;
                        var1 = this.next++;
                        break label215;
                     }
                  } catch (Throwable var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label214;
                  }

                  try {
                     ;
                  } catch (Throwable var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label214;
                  }

                  throw new NoSuchElementException("SSRCTable Enumeration");
               }

               Object var24 = var3[var1];

               label198:
               try {
                  return var24;
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label198;
               }
            }

            while(true) {
               Throwable var25 = var10000;

               try {
                  throw var25;
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  continue;
               }
            }
         }
      };
   }

   public Object get(int var1) {
      synchronized(this){}

      Throwable var10000;
      label78: {
         boolean var10001;
         try {
            var1 = this.indexOf(var1);
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label78;
         }

         if (var1 < 0) {
            return null;
         }

         Object var9;
         try {
            var9 = this.objList[var1];
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label78;
         }

         return var9;
      }

      Throwable var2 = var10000;
      throw var2;
   }

   public int getSSRC(Object var1) {
      synchronized(this){}
      int var2 = 0;

      while(true) {
         boolean var4 = false;

         label55: {
            try {
               var4 = true;
               if (var2 < this.total) {
                  if (this.objList[var2] == var1) {
                     var2 = this.ssrcList[var2];
                     var4 = false;
                     return var2;
                  }

                  var4 = false;
                  break label55;
               }

               var4 = false;
            } finally {
               if (var4) {
                  ;
               }
            }

            return 0;
         }

         ++var2;
      }
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public int[] keysToArray(int[] var1) {
      synchronized(this){}

      Throwable var10000;
      label205: {
         boolean var10001;
         int var2;
         try {
            var2 = this.size();
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label205;
         }

         int[] var3;
         label196: {
            if (var1 != null) {
               var3 = var1;

               try {
                  if (var1.length >= var2) {
                     break label196;
                  }
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label205;
               }
            }

            try {
               var3 = new int[var2];
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label205;
            }
         }

         label188:
         try {
            System.arraycopy(this.ssrcList, 0, var3, 0, var2);
            if (var2 < var3.length) {
               Arrays.fill(var3, var2, var3.length, 0);
            }

            return var3;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label188;
         }
      }

      Throwable var24 = var10000;
      throw var24;
   }

   public void put(int var1, Object var2) {
      synchronized(this){}

      Throwable var10000;
      label1068: {
         boolean var10001;
         try {
            if (this.total == 0) {
               this.ssrcList[0] = var1;
               this.objList[0] = var2;
               this.total = 1;
               return;
            }
         } catch (Throwable var115) {
            var10000 = var115;
            var10001 = false;
            break label1068;
         }

         int var3 = 0;

         while(true) {
            label1063: {
               label1064: {
                  try {
                     if (var3 >= this.total) {
                        break label1064;
                     }

                     if (this.ssrcList[var3] < var1) {
                        break label1063;
                     }
                  } catch (Throwable var114) {
                     var10000 = var114;
                     var10001 = false;
                     break;
                  }

                  try {
                     if (this.ssrcList[var3] != var1) {
                        break label1064;
                     }

                     this.objList[var3] = var2;
                  } catch (Throwable var113) {
                     var10000 = var113;
                     var10001 = false;
                     break;
                  }

                  return;
               }

               int[] var4;
               Object[] var5;
               try {
                  var4 = this.ssrcList;
                  var5 = this.objList;
               } catch (Throwable var112) {
                  var10000 = var112;
                  var10001 = false;
                  break;
               }

               try {
                  if (this.total == this.ssrcList.length) {
                     var4 = new int[this.ssrcList.length + 16];
                     var5 = new Object[this.objList.length + 16];
                  }
               } catch (Throwable var111) {
                  var10000 = var111;
                  var10001 = false;
                  break;
               }

               label1027: {
                  try {
                     if (this.ssrcList == var4) {
                        break label1027;
                     }
                  } catch (Throwable var110) {
                     var10000 = var110;
                     var10001 = false;
                     break;
                  }

                  if (var3 > 0) {
                     try {
                        System.arraycopy(this.ssrcList, 0, var4, 0, var3);
                        System.arraycopy(this.objList, 0, var5, 0, var3);
                     } catch (Throwable var109) {
                        var10000 = var109;
                        var10001 = false;
                        break;
                     }
                  }
               }

               try {
                  if (var3 < this.total) {
                     System.arraycopy(this.ssrcList, var3, var4, var3 + 1, this.total - var3);
                     System.arraycopy(this.objList, var3, var5, var3 + 1, this.total - var3);
                  }
               } catch (Throwable var108) {
                  var10000 = var108;
                  var10001 = false;
                  break;
               }

               try {
                  this.ssrcList = var4;
                  this.objList = var5;
               } catch (Throwable var107) {
                  var10000 = var107;
                  var10001 = false;
                  break;
               }

               var4[var3] = var1;
               var5[var3] = var2;

               try {
                  ++this.total;
               } catch (Throwable var106) {
                  var10000 = var106;
                  var10001 = false;
                  break;
               }

               return;
            }

            ++var3;
         }
      }

      Throwable var116 = var10000;
      throw var116;
   }

   public Object remove(int var1) {
      synchronized(this){}

      Throwable var10000;
      label210: {
         boolean var10001;
         int var2;
         try {
            var2 = this.indexOf(var1);
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label210;
         }

         var1 = var2;
         if (var2 < 0) {
            return null;
         }

         Object var3;
         try {
            var3 = this.objList[var1];
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label210;
         }

         while(true) {
            try {
               if (var1 >= this.total - 1) {
                  break;
               }

               this.ssrcList[var1] = this.ssrcList[var1 + 1];
               this.objList[var1] = this.objList[var1 + 1];
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label210;
            }

            ++var1;
         }

         try {
            this.ssrcList[this.total - 1] = 0;
            this.objList[this.total - 1] = null;
            --this.total;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label210;
         }

         return var3;
      }

      Throwable var24 = var10000;
      throw var24;
   }

   public void removeAll() {
      synchronized(this){}
      int var1 = 0;

      while(true) {
         boolean var4 = false;

         try {
            var4 = true;
            if (var1 >= this.total) {
               this.total = 0;
               var4 = false;
               return;
            }

            this.ssrcList[var1] = 0;
            this.objList[var1] = null;
            var4 = false;
         } finally {
            if (var4) {
               ;
            }
         }

         ++var1;
      }
   }

   public void removeObj(Object var1) {
      synchronized(this){}
      if (var1 != null) {
         int var2 = 0;

         while(true) {
            label284: {
               Throwable var10000;
               label291: {
                  boolean var10001;
                  try {
                     if (var2 < this.total && this.objList[var2] != var1) {
                        break label284;
                     }
                  } catch (Throwable var24) {
                     var10000 = var24;
                     var10001 = false;
                     break label291;
                  }

                  int var4;
                  try {
                     var4 = this.total;
                  } catch (Throwable var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label291;
                  }

                  int var3 = var2;
                  if (var2 >= var4) {
                     return;
                  }

                  while(true) {
                     try {
                        if (var3 >= this.total - 1) {
                           break;
                        }

                        this.ssrcList[var3] = this.ssrcList[var3 + 1];
                        this.objList[var3] = this.objList[var3 + 1];
                     } catch (Throwable var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label291;
                     }

                     ++var3;
                  }

                  label262:
                  try {
                     this.ssrcList[this.total - 1] = 0;
                     this.objList[this.total - 1] = null;
                     --this.total;
                     return;
                  } catch (Throwable var21) {
                     var10000 = var21;
                     var10001 = false;
                     break label262;
                  }
               }

               Throwable var25 = var10000;
               throw var25;
            }

            ++var2;
         }
      }
   }

   public int size() {
      return this.total;
   }

   public Object[] valuesToArray(Object[] var1) {
      synchronized(this){}

      Throwable var10000;
      label241: {
         boolean var10001;
         int var2;
         try {
            var2 = this.size();
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label241;
         }

         Class var3;
         if (var1 == null) {
            var3 = Object.class;
         } else {
            label231: {
               try {
                  if (var1.length < var2) {
                     var3 = var1.getClass().getComponentType();
                     break label231;
                  }
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label241;
               }

               var3 = null;
            }
         }

         if (var3 != null) {
            try {
               var1 = (Object[])((Object[])Array.newInstance(var3, var2));
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label241;
            }
         }

         label220:
         try {
            System.arraycopy(this.objList, 0, var1, 0, var2);
            if (var2 < var1.length) {
               Arrays.fill(var1, var2, var1.length, (Object)null);
            }

            return var1;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label220;
         }
      }

      Throwable var24 = var10000;
      throw var24;
   }
}
