package androidx.databinding;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.Serializable;

public class ObservableChar extends BaseObservableField implements Parcelable, Serializable {
   public static final Creator CREATOR = new Creator() {
      public ObservableChar createFromParcel(Parcel var1) {
         return new ObservableChar((char)var1.readInt());
      }

      public ObservableChar[] newArray(int var1) {
         return new ObservableChar[var1];
      }
   };
   static final long serialVersionUID = 1L;
   private char mValue;

   public ObservableChar() {
   }

   public ObservableChar(char var1) {
      this.mValue = var1;
   }

   public ObservableChar(Observable... var1) {
      super(var1);
   }

   public int describeContents() {
      return 0;
   }

   public char get() {
      return this.mValue;
   }

   public void set(char var1) {
      if (var1 != this.mValue) {
         this.mValue = var1;
         this.notifyChange();
      }

   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeInt(this.mValue);
   }
}
