package com.yalantis.ucrop.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class AspectRatio implements Parcelable {
   public static final Creator CREATOR = new Creator() {
      public AspectRatio createFromParcel(Parcel var1) {
         return new AspectRatio(var1);
      }

      public AspectRatio[] newArray(int var1) {
         return new AspectRatio[var1];
      }
   };
   private final String mAspectRatioTitle;
   private final float mAspectRatioX;
   private final float mAspectRatioY;

   protected AspectRatio(Parcel var1) {
      this.mAspectRatioTitle = var1.readString();
      this.mAspectRatioX = var1.readFloat();
      this.mAspectRatioY = var1.readFloat();
   }

   public AspectRatio(String var1, float var2, float var3) {
      this.mAspectRatioTitle = var1;
      this.mAspectRatioX = var2;
      this.mAspectRatioY = var3;
   }

   public int describeContents() {
      return 0;
   }

   public String getAspectRatioTitle() {
      return this.mAspectRatioTitle;
   }

   public float getAspectRatioX() {
      return this.mAspectRatioX;
   }

   public float getAspectRatioY() {
      return this.mAspectRatioY;
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeString(this.mAspectRatioTitle);
      var1.writeFloat(this.mAspectRatioX);
      var1.writeFloat(this.mAspectRatioY);
   }
}
