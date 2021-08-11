package androidx.databinding;

import java.util.ArrayList;
import java.util.List;

public class CallbackRegistry implements Cloneable {
   private static final String TAG = "CallbackRegistry";
   private List mCallbacks = new ArrayList();
   private long mFirst64Removed = 0L;
   private int mNotificationLevel;
   private final CallbackRegistry.NotifierCallback mNotifier;
   private long[] mRemainderRemoved;

   public CallbackRegistry(CallbackRegistry.NotifierCallback var1) {
      this.mNotifier = var1;
   }

   private boolean isRemoved(int var1) {
      if (var1 < 64) {
         return (this.mFirst64Removed & 1L << var1) != 0L;
      } else {
         long[] var3 = this.mRemainderRemoved;
         if (var3 == null) {
            return false;
         } else {
            int var2 = var1 / 64 - 1;
            if (var2 >= var3.length) {
               return false;
            } else {
               return (var3[var2] & 1L << var1 % 64) != 0L;
            }
         }
      }
   }

   private void notifyCallbacks(Object var1, int var2, Object var3, int var4, int var5, long var6) {
      for(long var8 = 1L; var4 < var5; ++var4) {
         if ((var6 & var8) == 0L) {
            this.mNotifier.onNotifyCallback(this.mCallbacks.get(var4), var1, var2, var3);
         }

         var8 <<= 1;
      }

   }

   private void notifyFirst64(Object var1, int var2, Object var3) {
      this.notifyCallbacks(var1, var2, var3, 0, Math.min(64, this.mCallbacks.size()), this.mFirst64Removed);
   }

   private void notifyRecurse(Object var1, int var2, Object var3) {
      int var5 = this.mCallbacks.size();
      long[] var6 = this.mRemainderRemoved;
      int var4;
      if (var6 == null) {
         var4 = -1;
      } else {
         var4 = var6.length - 1;
      }

      this.notifyRemainder(var1, var2, var3, var4);
      this.notifyCallbacks(var1, var2, var3, (var4 + 2) * 64, var5, 0L);
   }

   private void notifyRemainder(Object var1, int var2, Object var3, int var4) {
      if (var4 < 0) {
         this.notifyFirst64(var1, var2, var3);
      } else {
         long var7 = this.mRemainderRemoved[var4];
         int var5 = (var4 + 1) * 64;
         int var6 = Math.min(this.mCallbacks.size(), var5 + 64);
         this.notifyRemainder(var1, var2, var3, var4 - 1);
         this.notifyCallbacks(var1, var2, var3, var5, var6, var7);
      }
   }

   private void removeRemovedCallbacks(int var1, long var2) {
      long var5 = Long.MIN_VALUE;

      for(int var4 = var1 + 64 - 1; var4 >= var1; --var4) {
         if ((var2 & var5) != 0L) {
            this.mCallbacks.remove(var4);
         }

         var5 >>>= 1;
      }

   }

   private void setRemovalBit(int var1) {
      if (var1 < 64) {
         this.mFirst64Removed |= 1L << var1;
      } else {
         int var2 = var1 / 64 - 1;
         long[] var3 = this.mRemainderRemoved;
         if (var3 == null) {
            this.mRemainderRemoved = new long[this.mCallbacks.size() / 64];
         } else if (var3.length <= var2) {
            var3 = new long[this.mCallbacks.size() / 64];
            long[] var4 = this.mRemainderRemoved;
            System.arraycopy(var4, 0, var3, 0, var4.length);
            this.mRemainderRemoved = var3;
         }

         var3 = this.mRemainderRemoved;
         var3[var2] |= 1L << var1 % 64;
      }
   }

   public void add(Object var1) {
      synchronized(this){}
      Throwable var10000;
      boolean var10001;
      if (var1 != null) {
         label164: {
            int var2;
            try {
               var2 = this.mCallbacks.lastIndexOf(var1);
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label164;
            }

            if (var2 >= 0) {
               try {
                  if (!this.isRemoved(var2)) {
                     return;
                  }
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label164;
               }
            }

            try {
               this.mCallbacks.add(var1);
            } catch (Throwable var19) {
               var10000 = var19;
               var10001 = false;
               break label164;
            }

            return;
         }
      } else {
         label158:
         try {
            throw new IllegalArgumentException("callback cannot be null");
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label158;
         }
      }

      Throwable var23 = var10000;
      throw var23;
   }

   public void clear() {
      synchronized(this){}

      Throwable var10000;
      label164: {
         boolean var10001;
         try {
            if (this.mNotificationLevel == 0) {
               this.mCallbacks.clear();
               return;
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label164;
         }

         int var1;
         try {
            if (this.mCallbacks.isEmpty()) {
               return;
            }

            var1 = this.mCallbacks.size() - 1;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label164;
         }

         while(true) {
            if (var1 < 0) {
               return;
            }

            try {
               this.setRemovalBit(var1);
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break;
            }

            --var1;
         }
      }

      Throwable var2 = var10000;
      throw var2;
   }

   public CallbackRegistry clone() {
      // $FF: Couldn't be decompiled
   }

   public ArrayList copyCallbacks() {
      synchronized(this){}

      Throwable var10000;
      label101: {
         boolean var10001;
         int var2;
         ArrayList var3;
         try {
            var3 = new ArrayList(this.mCallbacks.size());
            var2 = this.mCallbacks.size();
         } catch (Throwable var9) {
            var10000 = var9;
            var10001 = false;
            break label101;
         }

         int var1 = 0;

         while(true) {
            if (var1 >= var2) {
               return var3;
            }

            try {
               if (!this.isRemoved(var1)) {
                  var3.add(this.mCallbacks.get(var1));
               }
            } catch (Throwable var8) {
               var10000 = var8;
               var10001 = false;
               break;
            }

            ++var1;
         }
      }

      Throwable var10 = var10000;
      throw var10;
   }

   public void copyCallbacks(List var1) {
      synchronized(this){}

      Throwable var10000;
      label101: {
         boolean var10001;
         int var3;
         try {
            var1.clear();
            var3 = this.mCallbacks.size();
         } catch (Throwable var9) {
            var10000 = var9;
            var10001 = false;
            break label101;
         }

         int var2 = 0;

         while(true) {
            if (var2 >= var3) {
               return;
            }

            try {
               if (!this.isRemoved(var2)) {
                  var1.add(this.mCallbacks.get(var2));
               }
            } catch (Throwable var8) {
               var10000 = var8;
               var10001 = false;
               break;
            }

            ++var2;
         }
      }

      Throwable var10 = var10000;
      throw var10;
   }

   public boolean isEmpty() {
      synchronized(this){}

      Throwable var10000;
      label259: {
         boolean var10001;
         boolean var3;
         try {
            var3 = this.mCallbacks.isEmpty();
         } catch (Throwable var24) {
            var10000 = var24;
            var10001 = false;
            break label259;
         }

         if (var3) {
            return true;
         }

         int var1;
         try {
            var1 = this.mNotificationLevel;
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label259;
         }

         if (var1 == 0) {
            return false;
         }

         int var2;
         try {
            var2 = this.mCallbacks.size();
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label259;
         }

         var1 = 0;

         while(true) {
            if (var1 >= var2) {
               return true;
            }

            try {
               var3 = this.isRemoved(var1);
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break;
            }

            if (!var3) {
               return false;
            }

            ++var1;
         }
      }

      Throwable var4 = var10000;
      throw var4;
   }

   public void notifyCallbacks(Object var1, int var2, Object var3) {
      synchronized(this){}

      Throwable var10000;
      label318: {
         boolean var10001;
         try {
            ++this.mNotificationLevel;
            this.notifyRecurse(var1, var2, var3);
            var2 = this.mNotificationLevel - 1;
            this.mNotificationLevel = var2;
         } catch (Throwable var35) {
            var10000 = var35;
            var10001 = false;
            break label318;
         }

         if (var2 != 0) {
            return;
         }

         label308: {
            try {
               if (this.mRemainderRemoved == null) {
                  break label308;
               }

               var2 = this.mRemainderRemoved.length - 1;
            } catch (Throwable var34) {
               var10000 = var34;
               var10001 = false;
               break label318;
            }

            for(; var2 >= 0; --var2) {
               long var4;
               try {
                  var4 = this.mRemainderRemoved[var2];
               } catch (Throwable var33) {
                  var10000 = var33;
                  var10001 = false;
                  break label318;
               }

               if (var4 != 0L) {
                  try {
                     this.removeRemovedCallbacks((var2 + 1) * 64, var4);
                     this.mRemainderRemoved[var2] = 0L;
                  } catch (Throwable var32) {
                     var10000 = var32;
                     var10001 = false;
                     break label318;
                  }
               }
            }
         }

         label294:
         try {
            if (this.mFirst64Removed != 0L) {
               this.removeRemovedCallbacks(0, this.mFirst64Removed);
               this.mFirst64Removed = 0L;
            }

            return;
         } catch (Throwable var31) {
            var10000 = var31;
            var10001 = false;
            break label294;
         }
      }

      Throwable var36 = var10000;
      throw var36;
   }

   public void remove(Object var1) {
      synchronized(this){}

      Throwable var10000;
      label137: {
         boolean var10001;
         try {
            if (this.mNotificationLevel == 0) {
               this.mCallbacks.remove(var1);
               return;
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label137;
         }

         int var2;
         try {
            var2 = this.mCallbacks.lastIndexOf(var1);
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label137;
         }

         if (var2 < 0) {
            return;
         }

         label122:
         try {
            this.setRemovalBit(var2);
            return;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label122;
         }
      }

      Throwable var15 = var10000;
      throw var15;
   }

   public abstract static class NotifierCallback {
      public abstract void onNotifyCallback(Object var1, Object var2, int var3, Object var4);
   }
}
