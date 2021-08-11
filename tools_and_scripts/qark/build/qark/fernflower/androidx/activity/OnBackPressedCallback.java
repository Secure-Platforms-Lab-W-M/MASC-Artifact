package androidx.activity;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class OnBackPressedCallback {
   private CopyOnWriteArrayList mCancellables = new CopyOnWriteArrayList();
   private boolean mEnabled;

   public OnBackPressedCallback(boolean var1) {
      this.mEnabled = var1;
   }

   void addCancellable(Cancellable var1) {
      this.mCancellables.add(var1);
   }

   public abstract void handleOnBackPressed();

   public final boolean isEnabled() {
      return this.mEnabled;
   }

   public final void remove() {
      Iterator var1 = this.mCancellables.iterator();

      while(var1.hasNext()) {
         ((Cancellable)var1.next()).cancel();
      }

   }

   void removeCancellable(Cancellable var1) {
      this.mCancellables.remove(var1);
   }

   public final void setEnabled(boolean var1) {
      this.mEnabled = var1;
   }
}
