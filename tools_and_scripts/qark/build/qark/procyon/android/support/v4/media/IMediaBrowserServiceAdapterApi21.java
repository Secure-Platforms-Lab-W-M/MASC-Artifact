// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.media;

import android.os.RemoteException;
import android.os.Parcel;
import android.os.ResultReceiver;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Binder;

class IMediaBrowserServiceAdapterApi21
{
    abstract static class Stub extends Binder implements IInterface
    {
        private static final String DESCRIPTOR = "android.service.media.IMediaBrowserService";
        private static final int TRANSACTION_addSubscription = 3;
        private static final int TRANSACTION_connect = 1;
        private static final int TRANSACTION_disconnect = 2;
        private static final int TRANSACTION_getMediaItem = 5;
        private static final int TRANSACTION_removeSubscription = 4;
        
        public Stub() {
            this.attachInterface((IInterface)this, "android.service.media.IMediaBrowserService");
        }
        
        public abstract void addSubscription(final String p0, final Object p1);
        
        public IBinder asBinder() {
            return (IBinder)this;
        }
        
        public abstract void connect(final String p0, final Bundle p1, final Object p2);
        
        public abstract void disconnect(final Object p0);
        
        public abstract void getMediaItem(final String p0, final ResultReceiver p1);
        
        public boolean onTransact(final int n, final Parcel parcel, final Parcel parcel2, final int n2) throws RemoteException {
            if (n == 1598968902) {
                parcel2.writeString("android.service.media.IMediaBrowserService");
                return true;
            }
            final Bundle bundle = null;
            final ResultReceiver resultReceiver = null;
            switch (n) {
                default: {
                    return super.onTransact(n, parcel, parcel2, n2);
                }
                case 5: {
                    parcel.enforceInterface("android.service.media.IMediaBrowserService");
                    final String string = parcel.readString();
                    ResultReceiver resultReceiver2;
                    if (parcel.readInt() != 0) {
                        resultReceiver2 = (ResultReceiver)ResultReceiver.CREATOR.createFromParcel(parcel);
                    }
                    else {
                        resultReceiver2 = resultReceiver;
                    }
                    this.getMediaItem(string, resultReceiver2);
                    return true;
                }
                case 4: {
                    parcel.enforceInterface("android.service.media.IMediaBrowserService");
                    this.removeSubscription(parcel.readString(), IMediaBrowserServiceCallbacksAdapterApi21.Stub.asInterface(parcel.readStrongBinder()));
                    return true;
                }
                case 3: {
                    parcel.enforceInterface("android.service.media.IMediaBrowserService");
                    this.addSubscription(parcel.readString(), IMediaBrowserServiceCallbacksAdapterApi21.Stub.asInterface(parcel.readStrongBinder()));
                    return true;
                }
                case 2: {
                    parcel.enforceInterface("android.service.media.IMediaBrowserService");
                    this.disconnect(IMediaBrowserServiceCallbacksAdapterApi21.Stub.asInterface(parcel.readStrongBinder()));
                    return true;
                }
                case 1: {
                    parcel.enforceInterface("android.service.media.IMediaBrowserService");
                    final String string2 = parcel.readString();
                    Bundle bundle2;
                    if (parcel.readInt() != 0) {
                        bundle2 = (Bundle)Bundle.CREATOR.createFromParcel(parcel);
                    }
                    else {
                        bundle2 = bundle;
                    }
                    this.connect(string2, bundle2, IMediaBrowserServiceCallbacksAdapterApi21.Stub.asInterface(parcel.readStrongBinder()));
                    return true;
                }
            }
        }
        
        public abstract void removeSubscription(final String p0, final Object p1);
    }
}
