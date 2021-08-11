package androidx.databinding;

import androidx.core.util.Pools;

public class ListChangeRegistry extends CallbackRegistry {
   private static final int ALL = 0;
   private static final int CHANGED = 1;
   private static final int INSERTED = 2;
   private static final int MOVED = 3;
   private static final CallbackRegistry.NotifierCallback NOTIFIER_CALLBACK = new CallbackRegistry.NotifierCallback() {
      public void onNotifyCallback(ObservableList.OnListChangedCallback var1, ObservableList var2, int var3, ListChangeRegistry.ListChanges var4) {
         if (var3 != 1) {
            if (var3 != 2) {
               if (var3 != 3) {
                  if (var3 != 4) {
                     var1.onChanged(var2);
                  } else {
                     var1.onItemRangeRemoved(var2, var4.start, var4.count);
                  }
               } else {
                  var1.onItemRangeMoved(var2, var4.start, var4.field_171, var4.count);
               }
            } else {
               var1.onItemRangeInserted(var2, var4.start, var4.count);
            }
         } else {
            var1.onItemRangeChanged(var2, var4.start, var4.count);
         }
      }
   };
   private static final int REMOVED = 4;
   private static final Pools.SynchronizedPool sListChanges = new Pools.SynchronizedPool(10);

   public ListChangeRegistry() {
      super(NOTIFIER_CALLBACK);
   }

   private static ListChangeRegistry.ListChanges acquire(int var0, int var1, int var2) {
      ListChangeRegistry.ListChanges var4 = (ListChangeRegistry.ListChanges)sListChanges.acquire();
      ListChangeRegistry.ListChanges var3 = var4;
      if (var4 == null) {
         var3 = new ListChangeRegistry.ListChanges();
      }

      var3.start = var0;
      var3.field_171 = var1;
      var3.count = var2;
      return var3;
   }

   public void notifyCallbacks(ObservableList var1, int var2, ListChangeRegistry.ListChanges var3) {
      synchronized(this){}

      Throwable var10000;
      label75: {
         boolean var10001;
         try {
            super.notifyCallbacks(var1, var2, var3);
         } catch (Throwable var9) {
            var10000 = var9;
            var10001 = false;
            break label75;
         }

         if (var3 == null) {
            return;
         }

         label66:
         try {
            sListChanges.release(var3);
            return;
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label66;
         }
      }

      Throwable var10 = var10000;
      throw var10;
   }

   public void notifyChanged(ObservableList var1) {
      this.notifyCallbacks((ObservableList)var1, 0, (ListChangeRegistry.ListChanges)null);
   }

   public void notifyChanged(ObservableList var1, int var2, int var3) {
      this.notifyCallbacks((ObservableList)var1, 1, (ListChangeRegistry.ListChanges)acquire(var2, 0, var3));
   }

   public void notifyInserted(ObservableList var1, int var2, int var3) {
      this.notifyCallbacks((ObservableList)var1, 2, (ListChangeRegistry.ListChanges)acquire(var2, 0, var3));
   }

   public void notifyMoved(ObservableList var1, int var2, int var3, int var4) {
      this.notifyCallbacks((ObservableList)var1, 3, (ListChangeRegistry.ListChanges)acquire(var2, var3, var4));
   }

   public void notifyRemoved(ObservableList var1, int var2, int var3) {
      this.notifyCallbacks((ObservableList)var1, 4, (ListChangeRegistry.ListChanges)acquire(var2, 0, var3));
   }

   static class ListChanges {
      public int count;
      public int start;
      // $FF: renamed from: to int
      public int field_171;
   }
}
