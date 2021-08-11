package androidx.databinding;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.Serializable;

public class ObservableInt extends BaseObservableField implements Parcelable, Serializable {
   public static final Creator CREATOR = new Creator() {
      public ObservableInt createFromParcel(Parcel var1) {
         return new ObservableInt(var1.readInt());
      }

      public ObservableInt[] newArray(int var1) {
         return new ObservableInt[var1];
      }
   };
   static final long serialVersionUID = 1L;
   private int mValue;

   public ObservableInt() {
   }

   public ObservableInt(int var1) {
      this.mValue = var1;
   }

   public ObservableInt(Observable... var1) {
      super(var1);
   }

   public int describeContents() {
      return 0;
   }

   public int get() {
      return this.mValue;
   }

   public void set(int var1) {
      if (var1 != this.mValue) {
         this.mValue = var1;
         this.notifyChange();
      }

   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeInt(this.mValue);
   }
}
