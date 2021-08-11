package androidx.activity;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import java.util.ArrayDeque;
import java.util.Iterator;

public final class OnBackPressedDispatcher {
   private final Runnable mFallbackOnBackPressed;
   final ArrayDeque mOnBackPressedCallbacks;

   public OnBackPressedDispatcher() {
      this((Runnable)null);
   }

   public OnBackPressedDispatcher(Runnable var1) {
      this.mOnBackPressedCallbacks = new ArrayDeque();
      this.mFallbackOnBackPressed = var1;
   }

   public void addCallback(OnBackPressedCallback var1) {
      this.addCancellableCallback(var1);
   }

   public void addCallback(LifecycleOwner var1, OnBackPressedCallback var2) {
      Lifecycle var3 = var1.getLifecycle();
      if (var3.getCurrentState() != Lifecycle.State.DESTROYED) {
         var2.addCancellable(new OnBackPressedDispatcher.LifecycleOnBackPressedCancellable(var3, var2));
      }
   }

   Cancellable addCancellableCallback(OnBackPressedCallback var1) {
      this.mOnBackPressedCallbacks.add(var1);
      OnBackPressedDispatcher.OnBackPressedCancellable var2 = new OnBackPressedDispatcher.OnBackPressedCancellable(var1);
      var1.addCancellable(var2);
      return var2;
   }

   public boolean hasEnabledCallbacks() {
      Iterator var1 = this.mOnBackPressedCallbacks.descendingIterator();

      do {
         if (!var1.hasNext()) {
            return false;
         }
      } while(!((OnBackPressedCallback)var1.next()).isEnabled());

      return true;
   }

   public void onBackPressed() {
      Iterator var1 = this.mOnBackPressedCallbacks.descendingIterator();

      OnBackPressedCallback var2;
      do {
         if (!var1.hasNext()) {
            Runnable var3 = this.mFallbackOnBackPressed;
            if (var3 != null) {
               var3.run();
            }

            return;
         }

         var2 = (OnBackPressedCallback)var1.next();
      } while(!var2.isEnabled());

      var2.handleOnBackPressed();
   }

   private class LifecycleOnBackPressedCancellable implements LifecycleEventObserver, Cancellable {
      private Cancellable mCurrentCancellable;
      private final Lifecycle mLifecycle;
      private final OnBackPressedCallback mOnBackPressedCallback;

      LifecycleOnBackPressedCancellable(Lifecycle var2, OnBackPressedCallback var3) {
         this.mLifecycle = var2;
         this.mOnBackPressedCallback = var3;
         var2.addObserver(this);
      }

      public void cancel() {
         this.mLifecycle.removeObserver(this);
         this.mOnBackPressedCallback.removeCancellable(this);
         Cancellable var1 = this.mCurrentCancellable;
         if (var1 != null) {
            var1.cancel();
            this.mCurrentCancellable = null;
         }

      }

      public void onStateChanged(LifecycleOwner var1, Lifecycle.Event var2) {
         if (var2 == Lifecycle.Event.ON_START) {
            this.mCurrentCancellable = OnBackPressedDispatcher.this.addCancellableCallback(this.mOnBackPressedCallback);
         } else {
            if (var2 == Lifecycle.Event.ON_STOP) {
               Cancellable var3 = this.mCurrentCancellable;
               if (var3 != null) {
                  var3.cancel();
                  return;
               }
            } else if (var2 == Lifecycle.Event.ON_DESTROY) {
               this.cancel();
            }

         }
      }
   }

   private class OnBackPressedCancellable implements Cancellable {
      private final OnBackPressedCallback mOnBackPressedCallback;

      OnBackPressedCancellable(OnBackPressedCallback var2) {
         this.mOnBackPressedCallback = var2;
      }

      public void cancel() {
         OnBackPressedDispatcher.this.mOnBackPressedCallbacks.remove(this.mOnBackPressedCallback);
         this.mOnBackPressedCallback.removeCancellable(this);
      }
   }
}
