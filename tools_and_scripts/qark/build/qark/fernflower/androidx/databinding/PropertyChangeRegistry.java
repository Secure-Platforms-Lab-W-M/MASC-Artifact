package androidx.databinding;

public class PropertyChangeRegistry extends CallbackRegistry {
   private static final CallbackRegistry.NotifierCallback NOTIFIER_CALLBACK = new CallbackRegistry.NotifierCallback() {
      public void onNotifyCallback(Observable.OnPropertyChangedCallback var1, Observable var2, int var3, Void var4) {
         var1.onPropertyChanged(var2, var3);
      }
   };

   public PropertyChangeRegistry() {
      super(NOTIFIER_CALLBACK);
   }

   public void notifyChange(Observable var1, int var2) {
      this.notifyCallbacks(var1, var2, (Object)null);
   }
}
