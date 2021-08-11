package android.support.v4.os;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IResultReceiver extends IInterface {
   void send(int var1, Bundle var2) throws RemoteException;

   public abstract static class Stub extends Binder implements IResultReceiver {
      private static final String DESCRIPTOR = "android.support.v4.os.IResultReceiver";
      static final int TRANSACTION_send = 1;

      public Stub() {
         this.attachInterface(this, "android.support.v4.os.IResultReceiver");
      }

      public static IResultReceiver asInterface(IBinder var0) {
         if (var0 == null) {
            return null;
         } else {
            IInterface var1 = var0.queryLocalInterface("android.support.v4.os.IResultReceiver");
            return (IResultReceiver)(var1 != null && var1 instanceof IResultReceiver ? (IResultReceiver)var1 : new IResultReceiver.Stub.Proxy(var0));
         }
      }

      public IBinder asBinder() {
         return this;
      }

      public boolean onTransact(int var1, Parcel var2, Parcel var3, int var4) throws RemoteException {
         if (var1 != 1) {
            if (var1 != 1598968902) {
               return super.onTransact(var1, var2, var3, var4);
            } else {
               var3.writeString("android.support.v4.os.IResultReceiver");
               return true;
            }
         } else {
            var2.enforceInterface("android.support.v4.os.IResultReceiver");
            var1 = var2.readInt();
            Bundle var5;
            if (var2.readInt() != 0) {
               var5 = (Bundle)Bundle.CREATOR.createFromParcel(var2);
            } else {
               var5 = null;
            }

            this.send(var1, var5);
            return true;
         }
      }

      private static class Proxy implements IResultReceiver {
         private IBinder mRemote;

         Proxy(IBinder var1) {
            this.mRemote = var1;
         }

         public IBinder asBinder() {
            return this.mRemote;
         }

         public String getInterfaceDescriptor() {
            return "android.support.v4.os.IResultReceiver";
         }

         public void send(int var1, Bundle var2) throws RemoteException {
            Parcel var3 = Parcel.obtain();

            label166: {
               Throwable var10000;
               label170: {
                  boolean var10001;
                  try {
                     var3.writeInterfaceToken("android.support.v4.os.IResultReceiver");
                     var3.writeInt(var1);
                  } catch (Throwable var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label170;
                  }

                  if (var2 != null) {
                     try {
                        var3.writeInt(1);
                        var2.writeToParcel(var3, 0);
                     } catch (Throwable var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label170;
                     }
                  } else {
                     try {
                        var3.writeInt(0);
                     } catch (Throwable var21) {
                        var10000 = var21;
                        var10001 = false;
                        break label170;
                     }
                  }

                  label156:
                  try {
                     this.mRemote.transact(1, var3, (Parcel)null, 1);
                     break label166;
                  } catch (Throwable var20) {
                     var10000 = var20;
                     var10001 = false;
                     break label156;
                  }
               }

               Throwable var24 = var10000;
               var3.recycle();
               throw var24;
            }

            var3.recycle();
         }
      }
   }
}
