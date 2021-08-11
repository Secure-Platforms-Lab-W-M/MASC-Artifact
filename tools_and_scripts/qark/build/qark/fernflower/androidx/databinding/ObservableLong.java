package androidx.databinding;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.Serializable;

public class ObservableLong extends BaseObservableField implements Parcelable, Serializable {
   public static final Creator CREATOR = new Creator() {
      public ObservableLong createFromParcel(Parcel var1) {
         return new ObservableLong(var1.readLong());
      }

      public ObservableLong[] newArray(int var1) {
         return new ObservableLong[var1];
      }
   };
   static final long serialVersionUID = 1L;
   private long mValue;

   public ObservableLong() {
   }

   public ObservableLong(long var1) {
      this.mValue = var1;
   }

   public ObservableLong(Observable... var1) {
      super(var1);
   }

   public int describeContents() {
      return 0;
   }

   public long get() {
      return this.mValue;
   }

   public void set(long var1) {
      if (var1 != this.mValue) {
         this.mValue = var1;
         this.notifyChange();
      }

   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeLong(this.mValue);
   }
}
