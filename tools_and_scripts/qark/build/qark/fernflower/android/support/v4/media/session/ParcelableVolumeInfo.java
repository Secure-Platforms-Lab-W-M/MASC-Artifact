package android.support.v4.media.session;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ParcelableVolumeInfo implements Parcelable {
   public static final Creator CREATOR = new Creator() {
      public ParcelableVolumeInfo createFromParcel(Parcel var1) {
         return new ParcelableVolumeInfo(var1);
      }

      public ParcelableVolumeInfo[] newArray(int var1) {
         return new ParcelableVolumeInfo[var1];
      }
   };
   public int audioStream;
   public int controlType;
   public int currentVolume;
   public int maxVolume;
   public int volumeType;

   public ParcelableVolumeInfo(int var1, int var2, int var3, int var4, int var5) {
      this.volumeType = var1;
      this.audioStream = var2;
      this.controlType = var3;
      this.maxVolume = var4;
      this.currentVolume = var5;
   }

   public ParcelableVolumeInfo(Parcel var1) {
      this.volumeType = var1.readInt();
      this.controlType = var1.readInt();
      this.maxVolume = var1.readInt();
      this.currentVolume = var1.readInt();
      this.audioStream = var1.readInt();
   }

   public int describeContents() {
      return 0;
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeInt(this.volumeType);
      var1.writeInt(this.controlType);
      var1.writeInt(this.maxVolume);
      var1.writeInt(this.currentVolume);
      var1.writeInt(this.audioStream);
   }
}
