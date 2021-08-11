package androidx.lifecycle;

import androidx.arch.core.util.Function;

public class Transformations {
   private Transformations() {
   }

   public static LiveData distinctUntilChanged(LiveData var0) {
      final MediatorLiveData var1 = new MediatorLiveData();
      var1.addSource(var0, new Observer() {
         boolean mFirstTime = true;

         public void onChanged(Object var1x) {
            Object var2 = var1.getValue();
            if (this.mFirstTime || var2 == null && var1x != null || var2 != null && !var2.equals(var1x)) {
               this.mFirstTime = false;
               var1.setValue(var1x);
            }

         }
      });
      return var1;
   }

   public static LiveData map(LiveData var0, final Function var1) {
      final MediatorLiveData var2 = new MediatorLiveData();
      var2.addSource(var0, new Observer() {
         public void onChanged(Object var1x) {
            var2.setValue(var1.apply(var1x));
         }
      });
      return var2;
   }

   public static LiveData switchMap(LiveData var0, final Function var1) {
      final MediatorLiveData var2 = new MediatorLiveData();
      var2.addSource(var0, new Observer() {
         LiveData mSource;

         public void onChanged(Object var1x) {
            LiveData var3 = (LiveData)var1.apply(var1x);
            LiveData var2x = this.mSource;
            if (var2x != var3) {
               if (var2x != null) {
                  var2.removeSource(var2x);
               }

               this.mSource = var3;
               if (var3 != null) {
                  var2.addSource(var3, new Observer() {
                     public void onChanged(Object var1x) {
                        var2.setValue(var1x);
                     }
                  });
               }

            }
         }
      });
      return var2;
   }
}
