package androidx.databinding;

import java.io.Serializable;

public class ObservableField extends BaseObservableField implements Serializable {
   static final long serialVersionUID = 1L;
   private Object mValue;

   public ObservableField() {
   }

   public ObservableField(Object var1) {
      this.mValue = var1;
   }

   public ObservableField(Observable... var1) {
      super(var1);
   }

   public Object get() {
      return this.mValue;
   }

   public void set(Object var1) {
      if (var1 != this.mValue) {
         this.mValue = var1;
         this.notifyChange();
      }

   }
}
