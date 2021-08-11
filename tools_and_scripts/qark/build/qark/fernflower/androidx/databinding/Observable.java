package androidx.databinding;

public interface Observable {
   void addOnPropertyChangedCallback(Observable.OnPropertyChangedCallback var1);

   void removeOnPropertyChangedCallback(Observable.OnPropertyChangedCallback var1);

   public abstract static class OnPropertyChangedCallback {
      public abstract void onPropertyChanged(Observable var1, int var2);
   }
}
