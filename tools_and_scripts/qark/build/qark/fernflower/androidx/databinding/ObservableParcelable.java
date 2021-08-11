package androidx.databinding;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.Serializable;

public class ObservableParcelable extends ObservableField implements Parcelable, Serializable {
   public static final Creator CREATOR = new Creator() {
      public ObservableParcelable createFromParcel(Parcel var1) {
         return new ObservableParcelable(var1.readParcelable(this.getClass().getClassLoader()));
      }

      public ObservableParcelable[] newArray(int var1) {
         return new ObservableParcelable[var1];
      }
   };
   static final long serialVersionUID = 1L;

   public ObservableParcelable() {
   }

   public ObservableParcelable(Parcelable var1) {
      super((Object)var1);
   }

   public int describeContents() {
      return 0;
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeParcelable((Parcelable)this.get(), 0);
   }
}
