package androidx.databinding;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.Serializable;

public class ObservableShort extends BaseObservableField implements Parcelable, Serializable {
   public static final Creator CREATOR = new Creator() {
      public ObservableShort createFromParcel(Parcel var1) {
         return new ObservableShort((short)var1.readInt());
      }

      public ObservableShort[] newArray(int var1) {
         return new ObservableShort[var1];
      }
   };
   static final long serialVersionUID = 1L;
   private short mValue;

   public ObservableShort() {
   }

   public ObservableShort(short var1) {
      this.mValue = var1;
   }

   public ObservableShort(Observable... var1) {
      super(var1);
   }

   public int describeContents() {
      return 0;
   }

   public short get() {
      return this.mValue;
   }

   public void set(short var1) {
      if (var1 != this.mValue) {
         this.mValue = var1;
         this.notifyChange();
      }

   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeInt(this.mValue);
   }
}
