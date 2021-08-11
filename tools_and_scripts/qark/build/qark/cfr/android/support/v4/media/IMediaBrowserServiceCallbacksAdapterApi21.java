/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.media.session.MediaSession
 *  android.media.session.MediaSession$Token
 *  android.os.Bundle
 *  android.os.IBinder
 *  android.os.RemoteException
 */
package android.support.v4.media;

import android.media.session.MediaSession;
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

    IMediaBrowserServiceCallbacksAdapterApi21(Object class_) {
        this.mCallbackObject = class_;
        try {
            class_ = Class.forName("android.service.media.IMediaBrowserServiceCallbacks");
            Class class_2 = Class.forName("android.content.pm.ParceledListSlice");
            this.mAsBinderMethod = class_.getMethod("asBinder", new Class[0]);
            this.mOnConnectMethod = class_.getMethod("onConnect", String.class, MediaSession.Token.class, Bundle.class);
            this.mOnConnectFailedMethod = class_.getMethod("onConnectFailed", new Class[0]);
            this.mOnLoadChildrenMethod = class_.getMethod("onLoadChildren", String.class, class_2);
            return;
        }
        catch (ClassNotFoundException | NoSuchMethodException reflectiveOperationException) {
            reflectiveOperationException.printStackTrace();
            return;
        }
    }

    IBinder asBinder() {
        try {
            IBinder iBinder = (IBinder)this.mAsBinderMethod.invoke(this.mCallbackObject, new Object[0]);
            return iBinder;
        }
        catch (IllegalAccessException | InvocationTargetException reflectiveOperationException) {
            reflectiveOperationException.printStackTrace();
            return null;
        }
    }

    void onConnect(String string, Object object, Bundle bundle) throws RemoteException {
        try {
            this.mOnConnectMethod.invoke(this.mCallbackObject, new Object[]{string, object, bundle});
            return;
        }
        catch (IllegalAccessException | InvocationTargetException reflectiveOperationException) {
            reflectiveOperationException.printStackTrace();
            return;
        }
    }

    void onConnectFailed() throws RemoteException {
        try {
            this.mOnConnectFailedMethod.invoke(this.mCallbackObject, new Object[0]);
            return;
        }
        catch (IllegalAccessException | InvocationTargetException reflectiveOperationException) {
            reflectiveOperationException.printStackTrace();
            return;
        }
    }

    void onLoadChildren(String string, Object object) throws RemoteException {
        try {
            this.mOnLoadChildrenMethod.invoke(this.mCallbackObject, string, object);
            return;
        }
        catch (IllegalAccessException | InvocationTargetException reflectiveOperationException) {
            reflectiveOperationException.printStackTrace();
            return;
        }
    }

    static class Stub {
        static Method sAsInterfaceMethod;

        static {
            try {
                sAsInterfaceMethod = Class.forName("android.service.media.IMediaBrowserServiceCallbacks$Stub").getMethod("asInterface", IBinder.class);
                return;
            }
            catch (ClassNotFoundException | NoSuchMethodException reflectiveOperationException) {
                reflectiveOperationException.printStackTrace();
                return;
            }
        }

        Stub() {
        }

        static Object asInterface(IBinder object) {
            try {
                object = sAsInterfaceMethod.invoke(null, object);
                return object;
            }
            catch (IllegalAccessException | InvocationTargetException reflectiveOperationException) {
                reflectiveOperationException.printStackTrace();
                return null;
            }
        }
    }

}

