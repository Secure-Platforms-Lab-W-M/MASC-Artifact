package android.support.v4.os;

import android.os.Build.VERSION;

public final class CancellationSignal {
   private boolean mCancelInProgress;
   private Object mCancellationSignalObj;
   private boolean mIsCanceled;
   private CancellationSignal.OnCancelListener mOnCancelListener;

   private void waitForCancelFinishedLocked() {
      while(this.mCancelInProgress) {
         try {
            this.wait();
         } catch (InterruptedException var2) {
         }
      }

   }

   public void cancel() {
      // $FF: Couldn't be decompiled
   }

   public Object getCancellationSignalObject() {
      if (VERSION.SDK_INT < 16) {
         return null;
      } else {
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         label144: {
            try {
               if (this.mCancellationSignalObj == null) {
                  this.mCancellationSignalObj = new android.os.CancellationSignal();
                  if (this.mIsCanceled) {
                     ((android.os.CancellationSignal)this.mCancellationSignalObj).cancel();
                  }
               }
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label144;
            }

            label141:
            try {
               Object var14 = this.mCancellationSignalObj;
               return var14;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label141;
            }
         }

         while(true) {
            Throwable var1 = var10000;

            try {
               throw var1;
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
               continue;
            }
         }
      }
   }

   public boolean isCanceled() {
      // $FF: Couldn't be decompiled
   }

   public void setOnCancelListener(CancellationSignal.OnCancelListener var1) {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label279: {
         try {
            this.waitForCancelFinishedLocked();
            if (this.mOnCancelListener == var1) {
               return;
            }
         } catch (Throwable var31) {
            var10000 = var31;
            var10001 = false;
            break label279;
         }

         label271: {
            try {
               this.mOnCancelListener = var1;
               if (!this.mIsCanceled) {
                  break label271;
               }
            } catch (Throwable var30) {
               var10000 = var30;
               var10001 = false;
               break label279;
            }

            if (var1 != null) {
               try {
                  ;
               } catch (Throwable var28) {
                  var10000 = var28;
                  var10001 = false;
                  break label279;
               }

               var1.onCancel();
               return;
            }
         }

         label265:
         try {
            return;
         } catch (Throwable var29) {
            var10000 = var29;
            var10001 = false;
            break label265;
         }
      }

      while(true) {
         Throwable var32 = var10000;

         try {
            throw var32;
         } catch (Throwable var27) {
            var10000 = var27;
            var10001 = false;
            continue;
         }
      }
   }

   public void throwIfCanceled() {
      if (this.isCanceled()) {
         throw new OperationCanceledException();
      }
   }

   public interface OnCancelListener {
      void onCancel();
   }
}
