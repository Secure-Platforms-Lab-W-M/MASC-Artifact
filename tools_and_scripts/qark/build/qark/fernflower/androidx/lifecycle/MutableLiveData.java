package androidx.lifecycle;

public class MutableLiveData extends LiveData {
   public MutableLiveData() {
   }

   public MutableLiveData(Object var1) {
      super(var1);
   }

   public void postValue(Object var1) {
      super.postValue(var1);
   }

   public void setValue(Object var1) {
      super.setValue(var1);
   }
}
