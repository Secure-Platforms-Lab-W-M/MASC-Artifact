/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Binder
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.os.RemoteException
 *  android.os.ResultReceiver
 */
package android.support.v4.media;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.support.v4.media.IMediaBrowserServiceCallbacksAdapterApi21;

class IMediaBrowserServiceAdapterApi21 {
    IMediaBrowserServiceAdapterApi21() {
    }

    static abstract class Stub
    extends Binder
    implements IInterface {
        private static final String DESCRIPTOR = "android.service.media.IMediaBrowserService";
        private static final int TRANSACTION_addSubscription = 3;
        private static final int TRANSACTION_connect = 1;
        private static final int TRANSACTION_disconnect = 2;
        private static final int TRANSACTION_getMediaItem = 5;
        private static final int TRANSACTION_removeSubscription = 4;

        public Stub() {
            this.attachInterface((IInterface)this, "android.service.media.IMediaBrowserService");
        }

        public abstract void addSubscription(String var1, Object var2);

        public IBinder asBinder() {
            return this;
        }

        public abstract void connect(String var1, Bundle var2, Object var3);

        public abstract void disconnect(Object var1);

        public abstract void getMediaItem(String var1, ResultReceiver var2);

        public boolean onTransact(int n, Parcel object, Parcel object2, int n2) throws RemoteException {
            if (n != 1598968902) {
                Bundle bundle = null;
                String string = null;
                switch (n) {
                    default: {
                        return super.onTransact(n, (Parcel)object, (Parcel)object2, n2);
                    }
                    case 5: {
                        object.enforceInterface("android.service.media.IMediaBrowserService");
                        object2 = object.readString();
                        object = object.readInt() != 0 ? (ResultReceiver)ResultReceiver.CREATOR.createFromParcel((Parcel)object) : string;
                        this.getMediaItem((String)object2, (ResultReceiver)object);
                        return true;
                    }
                    case 4: {
                        object.enforceInterface("android.service.media.IMediaBrowserService");
                        this.removeSubscription(object.readString(), IMediaBrowserServiceCallbacksAdapterApi21.Stub.asInterface(object.readStrongBinder()));
                        return true;
                    }
                    case 3: {
                        object.enforceInterface("android.service.media.IMediaBrowserService");
                        this.addSubscription(object.readString(), IMediaBrowserServiceCallbacksAdapterApi21.Stub.asInterface(object.readStrongBinder()));
                        return true;
                    }
                    case 2: {
                        object.enforceInterface("android.service.media.IMediaBrowserService");
                        this.disconnect(IMediaBrowserServiceCallbacksAdapterApi21.Stub.asInterface(object.readStrongBinder()));
                        return true;
                    }
                    case 1: 
                }
                object.enforceInterface("android.service.media.IMediaBrowserService");
                string = object.readString();
                object2 = object.readInt() != 0 ? (Bundle)Bundle.CREATOR.createFromParcel((Parcel)object) : bundle;
                this.connect(string, (Bundle)object2, IMediaBrowserServiceCallbacksAdapterApi21.Stub.asInterface(object.readStrongBinder()));
                return true;
            }
            object2.writeString("android.service.media.IMediaBrowserService");
            return true;
        }

        public abstract void removeSubscription(String var1, Object var2);
    }

}

