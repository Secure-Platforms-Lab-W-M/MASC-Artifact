// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.app;

import android.os.RemoteException;
import android.support.v4.app.INotificationSideChannel;
import android.os.Build$VERSION;
import android.os.IBinder;
import android.content.Intent;
import android.app.Notification;
import android.app.Service;

public abstract class NotificationCompatSideChannelService extends Service
{
    public abstract void cancel(final String p0, final int p1, final String p2);
    
    public abstract void cancelAll(final String p0);
    
    void checkPermission(final int n, final String s) {
        final String[] packagesForUid = this.getPackageManager().getPackagesForUid(n);
        for (int length = packagesForUid.length, i = 0; i < length; ++i) {
            if (packagesForUid[i].equals(s)) {
                return;
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("NotificationSideChannelService: Uid ");
        sb.append(n);
        sb.append(" is not authorized for package ");
        sb.append(s);
        throw new SecurityException(sb.toString());
    }
    
    public abstract void notify(final String p0, final int p1, final String p2, final Notification p3);
    
    public IBinder onBind(final Intent intent) {
        if (!intent.getAction().equals("android.support.BIND_NOTIFICATION_SIDE_CHANNEL")) {
            return null;
        }
        if (Build$VERSION.SDK_INT > 19) {
            return null;
        }
        return (IBinder)new NotificationSideChannelStub();
    }
    
    private class NotificationSideChannelStub extends Stub
    {
        NotificationSideChannelStub() {
        }
        
        public void cancel(final String s, final int n, final String s2) throws RemoteException {
            NotificationCompatSideChannelService.this.checkPermission(getCallingUid(), s);
            final long clearCallingIdentity = clearCallingIdentity();
            try {
                NotificationCompatSideChannelService.this.cancel(s, n, s2);
            }
            finally {
                restoreCallingIdentity(clearCallingIdentity);
            }
        }
        
        public void cancelAll(final String s) {
            NotificationCompatSideChannelService.this.checkPermission(getCallingUid(), s);
            final long clearCallingIdentity = clearCallingIdentity();
            try {
                NotificationCompatSideChannelService.this.cancelAll(s);
            }
            finally {
                restoreCallingIdentity(clearCallingIdentity);
            }
        }
        
        public void notify(final String s, final int n, final String s2, final Notification notification) throws RemoteException {
            NotificationCompatSideChannelService.this.checkPermission(getCallingUid(), s);
            final long clearCallingIdentity = clearCallingIdentity();
            try {
                NotificationCompatSideChannelService.this.notify(s, n, s2, notification);
            }
            finally {
                restoreCallingIdentity(clearCallingIdentity);
            }
        }
    }
}
