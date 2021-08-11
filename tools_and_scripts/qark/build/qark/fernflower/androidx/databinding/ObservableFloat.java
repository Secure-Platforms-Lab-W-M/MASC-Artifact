package androidx.databinding;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.Serializable;

public class ObservableFloat extends BaseObservableField implements Parcelable, Serializable {
   public static final Creator CREATOR = new Creator() {
      public ObservableFloat createFromParcel(Parcel var1) {
         return new ObservableFloat(var1.readFloat());
      }

      public ObservableFloat[] newArray(int var1) {
         return new ObservableFloat[var1];
      }
   };
   static final long serialVersionUID = 1L;
   private float mValue;

   public ObservableFloat() {
   }

   public ObservableFloat(float var1) {
      this.mValue = var1;
   }

   public ObservableFloat(Observable... var1) {
      super(var1);
   }

   public int describeContents() {
      return 0;
   }

   public float get() {
      return this.mValue;
   }

   public void set(float var1) {
      if (var1 != this.mValue) {
         this.mValue = var1;
         this.notifyChange();
      }

   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeFloat(this.mValue);
   }
}
