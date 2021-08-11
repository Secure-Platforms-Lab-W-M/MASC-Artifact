package android.support.v4.media;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ResultReceiver;

class IMediaBrowserServiceAdapterApi21 {
   abstract static class Stub extends Binder implements IInterface {
      private static final String DESCRIPTOR = "android.service.media.IMediaBrowserService";
      private static final int TRANSACTION_addSubscription = 3;
      private static final int TRANSACTION_connect = 1;
      private static final int TRANSACTION_disconnect = 2;
      private static final int TRANSACTION_getMediaItem = 5;
      private static final int TRANSACTION_removeSubscription = 4;

      public Stub() {
         this.attachInterface(this, "android.service.media.IMediaBrowserService");
      }

      public abstract void addSubscription(String var1, Object var2);

      public IBinder asBinder() {
         return this;
      }

      public abstract void connect(String var1, Bundle var2, Object var3);

      public abstract void disconnect(Object var1);

      public abstract void getMediaItem(String var1, ResultReceiver var2);

      public boolean onTransact(int var1, Parcel var2, Parcel var3, int var4) throws RemoteException {
         if (var1 != 1598968902) {
            Object var6 = null;
            String var5 = null;
            switch(var1) {
            case 1:
               var2.enforceInterface("android.service.media.IMediaBrowserService");
               var5 = var2.readString();
               Bundle var9;
               if (var2.readInt() != 0) {
                  var9 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
               } else {
                  var9 = (Bundle)var6;
               }

               this.connect(var5, var9, IMediaBrowserServiceCallbacksAdapterApi21.Stub.asInterface(var2.readStrongBinder()));
               return true;
            case 2:
               var2.enforceInterface("android.service.media.IMediaBrowserService");
               this.disconnect(IMediaBrowserServiceCallbacksAdapterApi21.Stub.asInterface(var2.readStrongBinder()));
               return true;
            case 3:
               var2.enforceInterface("android.service.media.IMediaBrowserService");
               this.addSubscription(var2.readString(), IMediaBrowserServiceCallbacksAdapterApi21.Stub.asInterface(var2.readStrongBinder()));
               return true;
            case 4:
               var2.enforceInterface("android.service.media.IMediaBrowserService");
               this.removeSubscription(var2.readString(), IMediaBrowserServiceCallbacksAdapterApi21.Stub.asInterface(var2.readStrongBinder()));
               return true;
            case 5:
               var2.enforceInterface("android.service.media.IMediaBrowserService");
               String var8 = var2.readString();
               ResultReceiver var7;
               if (var2.readInt() != 0) {
                  var7 = (ResultReceiver)ResultReceiver.CREATOR.createFromParcel(var2);
               } else {
                  var7 = var5;
               }

               this.getMediaItem(var8, var7);
               return true;
            default:
               return super.onTransact(var1, var2, var3, var4);
            }
         } else {
            var3.writeString("android.service.media.IMediaBrowserService");
            return true;
         }
      }

      public abstract void removeSubscription(String var1, Object var2);
   }
}
