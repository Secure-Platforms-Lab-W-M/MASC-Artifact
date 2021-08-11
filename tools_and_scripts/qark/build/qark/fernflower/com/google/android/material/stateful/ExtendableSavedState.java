package com.google.android.material.stateful;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import androidx.collection.SimpleArrayMap;
import androidx.customview.view.AbsSavedState;

public class ExtendableSavedState extends AbsSavedState {
   public static final Creator CREATOR = new ClassLoaderCreator() {
      public ExtendableSavedState createFromParcel(Parcel var1) {
         return new ExtendableSavedState(var1, (ClassLoader)null);
      }

      public ExtendableSavedState createFromParcel(Parcel var1, ClassLoader var2) {
         return new ExtendableSavedState(var1, var2);
      }

      public ExtendableSavedState[] newArray(int var1) {
         return new ExtendableSavedState[var1];
      }
   };
   public final SimpleArrayMap extendableStates;

   private ExtendableSavedState(Parcel var1, ClassLoader var2) {
      super(var1, var2);
      int var4 = var1.readInt();
      String[] var6 = new String[var4];
      var1.readStringArray(var6);
      Bundle[] var5 = new Bundle[var4];
      var1.readTypedArray(var5, Bundle.CREATOR);
      this.extendableStates = new SimpleArrayMap(var4);

      for(int var3 = 0; var3 < var4; ++var3) {
         this.extendableStates.put(var6[var3], var5[var3]);
      }

   }

   // $FF: synthetic method
   ExtendableSavedState(Parcel var1, ClassLoader var2, Object var3) {
      this(var1, var2);
   }

   public ExtendableSavedState(Parcelable var1) {
      super(var1);
      this.extendableStates = new SimpleArrayMap();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("ExtendableSavedState{");
      var1.append(Integer.toHexString(System.identityHashCode(this)));
      var1.append(" states=");
      var1.append(this.extendableStates);
      var1.append("}");
      return var1.toString();
   }

   public void writeToParcel(Parcel var1, int var2) {
      super.writeToParcel(var1, var2);
      int var3 = this.extendableStates.size();
      var1.writeInt(var3);
      String[] var4 = new String[var3];
      Bundle[] var5 = new Bundle[var3];

      for(var2 = 0; var2 < var3; ++var2) {
         var4[var2] = (String)this.extendableStates.keyAt(var2);
         var5[var2] = (Bundle)this.extendableStates.valueAt(var2);
      }

      var1.writeStringArray(var4);
      var1.writeTypedArray(var5, 0);
   }
}
