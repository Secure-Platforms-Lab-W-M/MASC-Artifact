package com.google.android.material.snackbar;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.lang.ref.WeakReference;

class SnackbarManager {
   private static final int LONG_DURATION_MS = 2750;
   static final int MSG_TIMEOUT = 0;
   private static final int SHORT_DURATION_MS = 1500;
   private static SnackbarManager snackbarManager;
   private SnackbarManager.SnackbarRecord currentSnackbar;
   private final Handler handler = new Handler(Looper.getMainLooper(), new android.os.Handler.Callback() {
      public boolean handleMessage(Message var1) {
         if (var1.what != 0) {
            return false;
         } else {
            SnackbarManager.this.handleTimeout((SnackbarManager.SnackbarRecord)var1.obj);
            return true;
         }
      }
   });
   private final Object lock = new Object();
   private SnackbarManager.SnackbarRecord nextSnackbar;

   private SnackbarManager() {
   }

   private boolean cancelSnackbarLocked(SnackbarManager.SnackbarRecord var1, int var2) {
      SnackbarManager.Callback var3 = (SnackbarManager.Callback)var1.callback.get();
      if (var3 != null) {
         this.handler.removeCallbacksAndMessages(var1);
         var3.dismiss(var2);
         return true;
      } else {
         return false;
      }
   }

   static SnackbarManager getInstance() {
      if (snackbarManager == null) {
         snackbarManager = new SnackbarManager();
      }

      return snackbarManager;
   }

   private boolean isCurrentSnackbarLocked(SnackbarManager.Callback var1) {
      SnackbarManager.SnackbarRecord var2 = this.currentSnackbar;
      return var2 != null && var2.isSnackbar(var1);
   }

   private boolean isNextSnackbarLocked(SnackbarManager.Callback var1) {
      SnackbarManager.SnackbarRecord var2 = this.nextSnackbar;
      return var2 != null && var2.isSnackbar(var1);
   }

   private void scheduleTimeoutLocked(SnackbarManager.SnackbarRecord var1) {
      if (var1.duration != -2) {
         int var2 = 2750;
         if (var1.duration > 0) {
            var2 = var1.duration;
         } else if (var1.duration == -1) {
            var2 = 1500;
         }

         this.handler.removeCallbacksAndMessages(var1);
         Handler var3 = this.handler;
         var3.sendMessageDelayed(Message.obtain(var3, 0, var1), (long)var2);
      }
   }

   private void showNextSnackbarLocked() {
      SnackbarManager.SnackbarRecord var1 = this.nextSnackbar;
      if (var1 != null) {
         this.currentSnackbar = var1;
         this.nextSnackbar = null;
         SnackbarManager.Callback var2 = (SnackbarManager.Callback)var1.callback.get();
         if (var2 != null) {
            var2.show();
            return;
         }

         this.currentSnackbar = null;
      }

   }

   public void dismiss(SnackbarManager.Callback var1, int var2) {
      Object var3 = this.lock;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label200: {
         label204: {
            try {
               if (this.isCurrentSnackbarLocked(var1)) {
                  this.cancelSnackbarLocked(this.currentSnackbar, var2);
                  break label204;
               }
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break label200;
            }

            try {
               if (this.isNextSnackbarLocked(var1)) {
                  this.cancelSnackbarLocked(this.nextSnackbar, var2);
               }
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label200;
            }
         }

         label190:
         try {
            return;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label190;
         }
      }

      while(true) {
         Throwable var24 = var10000;

         try {
            throw var24;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            continue;
         }
      }
   }

   void handleTimeout(SnackbarManager.SnackbarRecord var1) {
      Object var2 = this.lock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label183: {
         label182: {
            try {
               if (this.currentSnackbar != var1 && this.nextSnackbar != var1) {
                  break label182;
               }
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label183;
            }

            try {
               this.cancelSnackbarLocked(var1, 2);
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label183;
            }
         }

         label173:
         try {
            return;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label173;
         }
      }

      while(true) {
         Throwable var23 = var10000;

         try {
            throw var23;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            continue;
         }
      }
   }

   public boolean isCurrent(SnackbarManager.Callback param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean isCurrentOrNext(SnackbarManager.Callback var1) {
      Object var3 = this.lock;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label151: {
         boolean var2;
         label150: {
            label149: {
               try {
                  if (!this.isCurrentSnackbarLocked(var1) && !this.isNextSnackbarLocked(var1)) {
                     break label149;
                  }
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label151;
               }

               var2 = true;
               break label150;
            }

            var2 = false;
         }

         label140:
         try {
            return var2;
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label140;
         }
      }

      while(true) {
         Throwable var16 = var10000;

         try {
            throw var16;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            continue;
         }
      }
   }

   public void onDismissed(SnackbarManager.Callback var1) {
      Object var2 = this.lock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label130: {
         try {
            if (this.isCurrentSnackbarLocked(var1)) {
               this.currentSnackbar = null;
               if (this.nextSnackbar != null) {
                  this.showNextSnackbarLocked();
               }
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label130;
         }

         label127:
         try {
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label127;
         }
      }

      while(true) {
         Throwable var15 = var10000;

         try {
            throw var15;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   public void onShown(SnackbarManager.Callback var1) {
      Object var2 = this.lock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            if (this.isCurrentSnackbarLocked(var1)) {
               this.scheduleTimeoutLocked(this.currentSnackbar);
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label122;
         }

         label119:
         try {
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label119;
         }
      }

      while(true) {
         Throwable var15 = var10000;

         try {
            throw var15;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   public void pauseTimeout(SnackbarManager.Callback var1) {
      Object var2 = this.lock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label130: {
         try {
            if (this.isCurrentSnackbarLocked(var1) && !this.currentSnackbar.paused) {
               this.currentSnackbar.paused = true;
               this.handler.removeCallbacksAndMessages(this.currentSnackbar);
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label130;
         }

         label127:
         try {
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label127;
         }
      }

      while(true) {
         Throwable var15 = var10000;

         try {
            throw var15;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   public void restoreTimeoutIfPaused(SnackbarManager.Callback var1) {
      Object var2 = this.lock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label130: {
         try {
            if (this.isCurrentSnackbarLocked(var1) && this.currentSnackbar.paused) {
               this.currentSnackbar.paused = false;
               this.scheduleTimeoutLocked(this.currentSnackbar);
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label130;
         }

         label127:
         try {
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label127;
         }
      }

      while(true) {
         Throwable var15 = var10000;

         try {
            throw var15;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   public void show(int var1, SnackbarManager.Callback var2) {
      Object var3 = this.lock;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label405: {
         try {
            if (this.isCurrentSnackbarLocked(var2)) {
               this.currentSnackbar.duration = var1;
               this.handler.removeCallbacksAndMessages(this.currentSnackbar);
               this.scheduleTimeoutLocked(this.currentSnackbar);
               return;
            }
         } catch (Throwable var45) {
            var10000 = var45;
            var10001 = false;
            break label405;
         }

         label406: {
            try {
               if (this.isNextSnackbarLocked(var2)) {
                  this.nextSnackbar.duration = var1;
                  break label406;
               }
            } catch (Throwable var44) {
               var10000 = var44;
               var10001 = false;
               break label405;
            }

            try {
               this.nextSnackbar = new SnackbarManager.SnackbarRecord(var1, var2);
            } catch (Throwable var43) {
               var10000 = var43;
               var10001 = false;
               break label405;
            }
         }

         try {
            if (this.currentSnackbar != null && this.cancelSnackbarLocked(this.currentSnackbar, 4)) {
               return;
            }
         } catch (Throwable var42) {
            var10000 = var42;
            var10001 = false;
            break label405;
         }

         label385:
         try {
            this.currentSnackbar = null;
            this.showNextSnackbarLocked();
            return;
         } catch (Throwable var41) {
            var10000 = var41;
            var10001 = false;
            break label385;
         }
      }

      while(true) {
         Throwable var46 = var10000;

         try {
            throw var46;
         } catch (Throwable var40) {
            var10000 = var40;
            var10001 = false;
            continue;
         }
      }
   }

   interface Callback {
      void dismiss(int var1);

      void show();
   }

   private static class SnackbarRecord {
      final WeakReference callback;
      int duration;
      boolean paused;

      SnackbarRecord(int var1, SnackbarManager.Callback var2) {
         this.callback = new WeakReference(var2);
         this.duration = var1;
      }

      boolean isSnackbar(SnackbarManager.Callback var1) {
         return var1 != null && this.callback.get() == var1;
      }
   }
}
