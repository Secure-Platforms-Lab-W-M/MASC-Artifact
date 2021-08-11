package androidx.databinding;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.Serializable;

public class ObservableByte extends BaseObservableField implements Parcelable, Serializable {
   public static final Creator CREATOR = new Creator() {
      public ObservableByte createFromParcel(Parcel var1) {
         return new ObservableByte(var1.readByte());
      }

      public ObservableByte[] newArray(int var1) {
         return new ObservableByte[var1];
      }
   };
   static final long serialVersionUID = 1L;
   private byte mValue;

   public ObservableByte() {
   }

   public ObservableByte(byte var1) {
      this.mValue = var1;
   }

   public ObservableByte(Observable... var1) {
      super(var1);
   }

   public int describeContents() {
      return 0;
   }

   public byte get() {
      return this.mValue;
   }

   public void set(byte var1) {
      if (var1 != this.mValue) {
         this.mValue = var1;
         this.notifyChange();
      }

   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeByte(this.mValue);
   }
}
