package androidx.databinding;

public class MapChangeRegistry extends CallbackRegistry {
   private static CallbackRegistry.NotifierCallback NOTIFIER_CALLBACK = new CallbackRegistry.NotifierCallback() {
      public void onNotifyCallback(ObservableMap.OnMapChangedCallback var1, ObservableMap var2, int var3, Object var4) {
         var1.onMapChanged(var2, var4);
      }
   };

   public MapChangeRegistry() {
      super(NOTIFIER_CALLBACK);
   }

   public void notifyChange(ObservableMap var1, Object var2) {
      this.notifyCallbacks(var1, 0, var2);
   }
}
