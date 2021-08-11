package androidx.fragment.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;

final class FragmentManagerState implements Parcelable {
   public static final Creator CREATOR = new Creator() {
      public FragmentManagerState createFromParcel(Parcel var1) {
         return new FragmentManagerState(var1);
      }

      public FragmentManagerState[] newArray(int var1) {
         return new FragmentManagerState[var1];
      }
   };
   ArrayList mActive;
   ArrayList mAdded;
   BackStackState[] mBackStack;
   int mBackStackIndex;
   String mPrimaryNavActiveWho = null;

   public FragmentManagerState() {
   }

   public FragmentManagerState(Parcel var1) {
      this.mActive = var1.createTypedArrayList(FragmentState.CREATOR);
      this.mAdded = var1.createStringArrayList();
      this.mBackStack = (BackStackState[])var1.createTypedArray(BackStackState.CREATOR);
      this.mBackStackIndex = var1.readInt();
      this.mPrimaryNavActiveWho = var1.readString();
   }

   public int describeContents() {
      return 0;
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeTypedList(this.mActive);
      var1.writeStringList(this.mAdded);
      var1.writeTypedArray(this.mBackStack, var2);
      var1.writeInt(this.mBackStackIndex);
      var1.writeString(this.mPrimaryNavActiveWho);
   }
}
