package androidx.databinding;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.Serializable;

public class ObservableDouble extends BaseObservableField implements Parcelable, Serializable {
   public static final Creator CREATOR = new Creator() {
      public ObservableDouble createFromParcel(Parcel var1) {
         return new ObservableDouble(var1.readDouble());
      }

      public ObservableDouble[] newArray(int var1) {
         return new ObservableDouble[var1];
      }
   };
   static final long serialVersionUID = 1L;
   private double mValue;

   public ObservableDouble() {
   }

   public ObservableDouble(double var1) {
      this.mValue = var1;
   }

   public ObservableDouble(Observable... var1) {
      super(var1);
   }

   public int describeContents() {
      return 0;
   }

   public double get() {
      return this.mValue;
   }

   public void set(double var1) {
      if (var1 != this.mValue) {
         this.mValue = var1;
         this.notifyChange();
      }

   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeDouble(this.mValue);
   }
}
