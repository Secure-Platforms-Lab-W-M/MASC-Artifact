package androidx.core.os;

import android.os.Parcel;

@Deprecated
public interface ParcelableCompatCreatorCallbacks {
   Object createFromParcel(Parcel var1, ClassLoader var2);

   Object[] newArray(int var1);
}
