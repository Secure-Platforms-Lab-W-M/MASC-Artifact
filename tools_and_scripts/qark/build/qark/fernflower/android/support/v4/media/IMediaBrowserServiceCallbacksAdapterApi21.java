package android.support.v4.media;

import android.media.session.MediaSession.Token;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class IMediaBrowserServiceCallbacksAdapterApi21 {
   private Method mAsBinderMethod;
   Object mCallbackObject;
   private Method mOnConnectFailedMethod;
   private Method mOnConnectMethod;
   private Method mOnLoadChildrenMethod;

   IMediaBrowserServiceCallbacksAdapterApi21(Object var1) {
      this.mCallbackObject = var1;

      try {
         Class var4 = Class.forName("android.service.media.IMediaBrowserServiceCallbacks");
         Class var2 = Class.forName("android.content.pm.ParceledListSlice");
         this.mAsBinderMethod = var4.getMethod("asBinder");
         this.mOnConnectMethod = var4.getMethod("onConnect", String.class, Token.class, Bundle.class);
         this.mOnConnectFailedMethod = var4.getMethod("onConnectFailed");
         this.mOnLoadChildrenMethod = var4.getMethod("onLoadChildren", String.class, var2);
      } catch (NoSuchMethodException | ClassNotFoundException var3) {
         var3.printStackTrace();
      }
   }

   IBinder asBinder() {
      try {
         IBinder var1 = (IBinder)this.mAsBinderMethod.invoke(this.mCallbackObject);
         return var1;
      } catch (InvocationTargetException | IllegalAccessException var2) {
         var2.printStackTrace();
         return null;
      }
   }

   void onConnect(String var1, Object var2, Bundle var3) throws RemoteException {
      try {
         this.mOnConnectMethod.invoke(this.mCallbackObject, var1, var2, var3);
      } catch (InvocationTargetException | IllegalAccessException var4) {
         var4.printStackTrace();
      }
   }

   void onConnectFailed() throws RemoteException {
      try {
         this.mOnConnectFailedMethod.invoke(this.mCallbackObject);
      } catch (InvocationTargetException | IllegalAccessException var2) {
         var2.printStackTrace();
      }
   }

   void onLoadChildren(String var1, Object var2) throws RemoteException {
      try {
         this.mOnLoadChildrenMethod.invoke(this.mCallbackObject, var1, var2);
      } catch (InvocationTargetException | IllegalAccessException var3) {
         var3.printStackTrace();
      }
   }

   static class Stub {
      static Method sAsInterfaceMethod;

      static {
         try {
            sAsInterfaceMethod = Class.forName("android.service.media.IMediaBrowserServiceCallbacks$Stub").getMethod("asInterface", IBinder.class);
         } catch (NoSuchMethodException | ClassNotFoundException var1) {
            var1.printStackTrace();
         }
      }

      static Object asInterface(IBinder var0) {
         try {
            Object var2 = sAsInterfaceMethod.invoke((Object)null, var0);
            return var2;
         } catch (InvocationTargetException | IllegalAccessException var1) {
            var1.printStackTrace();
            return null;
         }
      }
   }
}
