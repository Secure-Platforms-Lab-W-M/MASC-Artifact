package androidx.databinding;

abstract class BaseObservableField extends BaseObservable {
   public BaseObservableField() {
   }

   public BaseObservableField(Observable... var1) {
      if (var1 != null && var1.length != 0) {
         BaseObservableField.DependencyCallback var3 = new BaseObservableField.DependencyCallback();

         for(int var2 = 0; var2 < var1.length; ++var2) {
            var1[var2].addOnPropertyChangedCallback(var3);
         }
      }

   }

   class DependencyCallback extends Observable.OnPropertyChangedCallback {
      public void onPropertyChanged(Observable var1, int var2) {
         BaseObservableField.this.notifyChange();
      }
   }
}
