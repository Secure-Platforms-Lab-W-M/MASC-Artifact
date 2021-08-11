/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.AppOpsManager
 *  android.app.Notification
 *  android.app.NotificationManager
 *  android.content.ComponentName
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.ServiceConnection
 *  android.content.pm.ApplicationInfo
 *  android.content.pm.PackageManager
 *  android.content.pm.ResolveInfo
 *  android.content.pm.ServiceInfo
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.DeadObjectException
 *  android.os.Handler
 *  android.os.Handler$Callback
 *  android.os.HandlerThread
 *  android.os.IBinder
 *  android.os.Looper
 *  android.os.Message
 *  android.os.RemoteException
 *  android.provider.Settings
 *  android.provider.Settings$Secure
 *  android.util.Log
 */
package android.support.v4.app;

import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.annotation.GuardedBy;
import android.support.v4.app.INotificationSideChannel;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class NotificationManagerCompat {
    public static final String ACTION_BIND_SIDE_CHANNEL = "android.support.BIND_NOTIFICATION_SIDE_CHANNEL";
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    public static final String EXTRA_USE_SIDE_CHANNEL = "android.support.useSideChannel";
    public static final int IMPORTANCE_DEFAULT = 3;
    public static final int IMPORTANCE_HIGH = 4;
    public static final int IMPORTANCE_LOW = 2;
    public static final int IMPORTANCE_MAX = 5;
    public static final int IMPORTANCE_MIN = 1;
    public static final int IMPORTANCE_NONE = 0;
    public static final int IMPORTANCE_UNSPECIFIED = -1000;
    static final int MAX_SIDE_CHANNEL_SDK_VERSION = 19;
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
    private static final String SETTING_ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final int SIDE_CHANNEL_RETRY_BASE_INTERVAL_MS = 1000;
    private static final int SIDE_CHANNEL_RETRY_MAX_COUNT = 6;
    private static final String TAG = "NotifManCompat";
    @GuardedBy(value="sEnabledNotificationListenersLock")
    private static Set<String> sEnabledNotificationListenerPackages;
    @GuardedBy(value="sEnabledNotificationListenersLock")
    private static String sEnabledNotificationListeners;
    private static final Object sEnabledNotificationListenersLock;
    private static final Object sLock;
    @GuardedBy(value="sLock")
    private static SideChannelManager sSideChannelManager;
    private final Context mContext;
    private final NotificationManager mNotificationManager;

    static {
        sEnabledNotificationListenersLock = new Object();
        sEnabledNotificationListenerPackages = new HashSet<String>();
        sLock = new Object();
    }

    private NotificationManagerCompat(Context context) {
        this.mContext = context;
        this.mNotificationManager = (NotificationManager)this.mContext.getSystemService("notification");
    }

    public static NotificationManagerCompat from(Context context) {
        return new NotificationManagerCompat(context);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Set<String> getEnabledListenerPackages(Context object) {
        String string2 = Settings.Secure.getString((ContentResolver)object.getContentResolver(), (String)"enabled_notification_listeners");
        object = sEnabledNotificationListenersLock;
        synchronized (object) {
            if (string2 == null) return sEnabledNotificationListenerPackages;
            if (string2.equals(sEnabledNotificationListeners)) return sEnabledNotificationListenerPackages;
            String[] arrstring = string2.split(":");
            HashSet<String> hashSet = new HashSet<String>(arrstring.length);
            int n = arrstring.length;
            int n2 = 0;
            do {
                if (n2 < n) {
                    ComponentName componentName = ComponentName.unflattenFromString((String)arrstring[n2]);
                    if (componentName != null) {
                        hashSet.add(componentName.getPackageName());
                    }
                } else {
                    sEnabledNotificationListenerPackages = hashSet;
                    sEnabledNotificationListeners = string2;
                    return sEnabledNotificationListenerPackages;
                }
                ++n2;
            } while (true);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void pushSideChannelQueue(Task task) {
        Object object = sLock;
        synchronized (object) {
            if (sSideChannelManager == null) {
                sSideChannelManager = new SideChannelManager(this.mContext.getApplicationContext());
            }
            sSideChannelManager.queueTask(task);
            return;
        }
    }

    private static boolean useSideChannelForNotification(Notification notification) {
        if ((notification = NotificationCompat.getExtras(notification)) != null && notification.getBoolean("android.support.useSideChannel")) {
            return true;
        }
        return false;
    }

    public boolean areNotificationsEnabled() {
        if (Build.VERSION.SDK_INT >= 24) {
            return this.mNotificationManager.areNotificationsEnabled();
        }
        if (Build.VERSION.SDK_INT >= 19) {
            AppOpsManager appOpsManager = (AppOpsManager)this.mContext.getSystemService("appops");
            Object object = this.mContext.getApplicationInfo();
            String string2 = this.mContext.getApplicationContext().getPackageName();
            int n = object.uid;
            try {
                object = Class.forName(AppOpsManager.class.getName());
                n = (Integer)object.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE, String.class).invoke((Object)appOpsManager, (int)((Integer)object.getDeclaredField("OP_POST_NOTIFICATION").get(Integer.class)), n, string2);
                if (n == 0) {
                    return true;
                }
                return false;
            }
            catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException | RuntimeException | InvocationTargetException exception) {
                return true;
            }
        }
        return true;
    }

    public void cancel(int n) {
        this.cancel(null, n);
    }

    public void cancel(String string2, int n) {
        this.mNotificationManager.cancel(string2, n);
        if (Build.VERSION.SDK_INT <= 19) {
            this.pushSideChannelQueue(new CancelTask(this.mContext.getPackageName(), n, string2));
            return;
        }
    }

    public void cancelAll() {
        this.mNotificationManager.cancelAll();
        if (Build.VERSION.SDK_INT <= 19) {
            this.pushSideChannelQueue(new CancelTask(this.mContext.getPackageName()));
            return;
        }
    }

    public int getImportance() {
        if (Build.VERSION.SDK_INT >= 24) {
            return this.mNotificationManager.getImportance();
        }
        return -1000;
    }

    public void notify(int n, Notification notification) {
        this.notify(null, n, notification);
    }

    public void notify(String string2, int n, Notification notification) {
        if (NotificationManagerCompat.useSideChannelForNotification(notification)) {
            this.pushSideChannelQueue(new NotifyTask(this.mContext.getPackageName(), n, string2, notification));
            this.mNotificationManager.cancel(string2, n);
            return;
        }
        this.mNotificationManager.notify(string2, n, notification);
    }

    private static class CancelTask
    implements Task {
        final boolean all;
        final int id;
        final String packageName;
        final String tag;

        CancelTask(String string2) {
            this.packageName = string2;
            this.id = 0;
            this.tag = null;
            this.all = true;
        }

        CancelTask(String string2, int n, String string3) {
            this.packageName = string2;
            this.id = n;
            this.tag = string3;
            this.all = false;
        }

        @Override
        public void send(INotificationSideChannel iNotificationSideChannel) throws RemoteException {
            if (this.all) {
                iNotificationSideChannel.cancelAll(this.packageName);
                return;
            }
            iNotificationSideChannel.cancel(this.packageName, this.id, this.tag);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder("CancelTask[");
            stringBuilder.append("packageName:");
            stringBuilder.append(this.packageName);
            stringBuilder.append(", id:");
            stringBuilder.append(this.id);
            stringBuilder.append(", tag:");
            stringBuilder.append(this.tag);
            stringBuilder.append(", all:");
            stringBuilder.append(this.all);
            stringBuilder.append("]");
            return stringBuilder.toString();
        }
    }

    private static class NotifyTask
    implements Task {
        final int id;
        final Notification notif;
        final String packageName;
        final String tag;

        NotifyTask(String string2, int n, String string3, Notification notification) {
            this.packageName = string2;
            this.id = n;
            this.tag = string3;
            this.notif = notification;
        }

        @Override
        public void send(INotificationSideChannel iNotificationSideChannel) throws RemoteException {
            iNotificationSideChannel.notify(this.packageName, this.id, this.tag, this.notif);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder("NotifyTask[");
            stringBuilder.append("packageName:");
            stringBuilder.append(this.packageName);
            stringBuilder.append(", id:");
            stringBuilder.append(this.id);
            stringBuilder.append(", tag:");
            stringBuilder.append(this.tag);
            stringBuilder.append("]");
            return stringBuilder.toString();
        }
    }

    private static class ServiceConnectedEvent {
        final ComponentName componentName;
        final IBinder iBinder;

        ServiceConnectedEvent(ComponentName componentName, IBinder iBinder) {
            this.componentName = componentName;
            this.iBinder = iBinder;
        }
    }

    private static class SideChannelManager
    implements Handler.Callback,
    ServiceConnection {
        private static final int MSG_QUEUE_TASK = 0;
        private static final int MSG_RETRY_LISTENER_QUEUE = 3;
        private static final int MSG_SERVICE_CONNECTED = 1;
        private static final int MSG_SERVICE_DISCONNECTED = 2;
        private Set<String> mCachedEnabledPackages = new HashSet<String>();
        private final Context mContext;
        private final Handler mHandler;
        private final HandlerThread mHandlerThread;
        private final Map<ComponentName, ListenerRecord> mRecordMap = new HashMap<ComponentName, ListenerRecord>();

        public SideChannelManager(Context context) {
            this.mContext = context;
            this.mHandlerThread = new HandlerThread("NotificationManagerCompat");
            this.mHandlerThread.start();
            this.mHandler = new Handler(this.mHandlerThread.getLooper(), (Handler.Callback)this);
        }

        private boolean ensureServiceBound(ListenerRecord listenerRecord) {
            if (listenerRecord.bound) {
                return true;
            }
            Object object = new Intent("android.support.BIND_NOTIFICATION_SIDE_CHANNEL").setComponent(listenerRecord.componentName);
            listenerRecord.bound = this.mContext.bindService((Intent)object, (ServiceConnection)this, 33);
            if (listenerRecord.bound) {
                listenerRecord.retryCount = 0;
            } else {
                object = new StringBuilder();
                object.append("Unable to bind to listener ");
                object.append((Object)listenerRecord.componentName);
                Log.w((String)"NotifManCompat", (String)object.toString());
                this.mContext.unbindService((ServiceConnection)this);
            }
            return listenerRecord.bound;
        }

        private void ensureServiceUnbound(ListenerRecord listenerRecord) {
            if (listenerRecord.bound) {
                this.mContext.unbindService((ServiceConnection)this);
                listenerRecord.bound = false;
            }
            listenerRecord.service = null;
        }

        private void handleQueueTask(Task task) {
            this.updateListenerMap();
            for (ListenerRecord listenerRecord : this.mRecordMap.values()) {
                listenerRecord.taskQueue.add(task);
                this.processListenerQueue(listenerRecord);
            }
        }

        private void handleRetryListenerQueue(ComponentName object) {
            if ((object = this.mRecordMap.get(object)) != null) {
                this.processListenerQueue((ListenerRecord)object);
                return;
            }
        }

        private void handleServiceConnected(ComponentName object, IBinder iBinder) {
            if ((object = this.mRecordMap.get(object)) != null) {
                object.service = INotificationSideChannel.Stub.asInterface(iBinder);
                object.retryCount = 0;
                this.processListenerQueue((ListenerRecord)object);
                return;
            }
        }

        private void handleServiceDisconnected(ComponentName object) {
            if ((object = this.mRecordMap.get(object)) != null) {
                this.ensureServiceUnbound((ListenerRecord)object);
                return;
            }
        }

        private void processListenerQueue(ListenerRecord listenerRecord) {
            Object object;
            if (Log.isLoggable((String)"NotifManCompat", (int)3)) {
                object = new StringBuilder();
                object.append("Processing component ");
                object.append((Object)listenerRecord.componentName);
                object.append(", ");
                object.append(listenerRecord.taskQueue.size());
                object.append(" queued tasks");
                Log.d((String)"NotifManCompat", (String)object.toString());
            }
            if (listenerRecord.taskQueue.isEmpty()) {
                return;
            }
            if (this.ensureServiceBound(listenerRecord) && listenerRecord.service != null) {
                while ((object = listenerRecord.taskQueue.peek()) != null) {
                    StringBuilder stringBuilder;
                    try {
                        if (Log.isLoggable((String)"NotifManCompat", (int)3)) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("Sending task ");
                            stringBuilder.append(object);
                            Log.d((String)"NotifManCompat", (String)stringBuilder.toString());
                        }
                        object.send(listenerRecord.service);
                        listenerRecord.taskQueue.remove();
                    }
                    catch (RemoteException remoteException) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("RemoteException communicating with ");
                        stringBuilder.append((Object)listenerRecord.componentName);
                        Log.w((String)"NotifManCompat", (String)stringBuilder.toString(), (Throwable)remoteException);
                        break;
                    }
                    catch (DeadObjectException deadObjectException) {
                        if (!Log.isLoggable((String)"NotifManCompat", (int)3)) break;
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Remote service has died: ");
                        stringBuilder2.append((Object)listenerRecord.componentName);
                        Log.d((String)"NotifManCompat", (String)stringBuilder2.toString());
                        break;
                    }
                }
                if (!listenerRecord.taskQueue.isEmpty()) {
                    this.scheduleListenerRetry(listenerRecord);
                    return;
                }
                return;
            }
            this.scheduleListenerRetry(listenerRecord);
        }

        private void scheduleListenerRetry(ListenerRecord listenerRecord) {
            if (this.mHandler.hasMessages(3, (Object)listenerRecord.componentName)) {
                return;
            }
            ++listenerRecord.retryCount;
            if (listenerRecord.retryCount > 6) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Giving up on delivering ");
                stringBuilder.append(listenerRecord.taskQueue.size());
                stringBuilder.append(" tasks to ");
                stringBuilder.append((Object)listenerRecord.componentName);
                stringBuilder.append(" after ");
                stringBuilder.append(listenerRecord.retryCount);
                stringBuilder.append(" retries");
                Log.w((String)"NotifManCompat", (String)stringBuilder.toString());
                listenerRecord.taskQueue.clear();
                return;
            }
            int n = (1 << listenerRecord.retryCount - 1) * 1000;
            if (Log.isLoggable((String)"NotifManCompat", (int)3)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Scheduling retry for ");
                stringBuilder.append(n);
                stringBuilder.append(" ms");
                Log.d((String)"NotifManCompat", (String)stringBuilder.toString());
            }
            listenerRecord = this.mHandler.obtainMessage(3, (Object)listenerRecord.componentName);
            this.mHandler.sendMessageDelayed((Message)listenerRecord, (long)n);
        }

        private void updateListenerMap() {
            Object object;
            Object object2 = NotificationManagerCompat.getEnabledListenerPackages(this.mContext);
            if (object2.equals(this.mCachedEnabledPackages)) {
                return;
            }
            this.mCachedEnabledPackages = object2;
            List componentName2 = this.mContext.getPackageManager().queryIntentServices(new Intent().setAction("android.support.BIND_NOTIFICATION_SIDE_CHANNEL"), 0);
            HashSet<Object> hashSet = new HashSet<Object>();
            Iterator iterator = componentName2.iterator();
            while (iterator.hasNext()) {
                Object object3 = (ResolveInfo)iterator.next();
                if (!object2.contains(object3.serviceInfo.packageName)) continue;
                object = new ComponentName(object3.serviceInfo.packageName, object3.serviceInfo.name);
                if (object3.serviceInfo.permission != null) {
                    object3 = new StringBuilder();
                    object3.append("Permission present on component ");
                    object3.append(object);
                    object3.append(", not adding listener record.");
                    Log.w((String)"NotifManCompat", (String)object3.toString());
                    continue;
                }
                hashSet.add(object);
            }
            for (ComponentName componentName : hashSet) {
                if (this.mRecordMap.containsKey((Object)componentName)) continue;
                if (Log.isLoggable((String)"NotifManCompat", (int)3)) {
                    object = new StringBuilder();
                    object.append("Adding listener record for ");
                    object.append((Object)componentName);
                    Log.d((String)"NotifManCompat", (String)object.toString());
                }
                this.mRecordMap.put(componentName, new ListenerRecord(componentName));
            }
            object2 = this.mRecordMap.entrySet().iterator();
            while (object2.hasNext()) {
                Map.Entry<ComponentName, ListenerRecord> entry = object2.next();
                if (hashSet.contains((Object)entry.getKey())) continue;
                if (Log.isLoggable((String)"NotifManCompat", (int)3)) {
                    object = new StringBuilder();
                    object.append("Removing listener record for ");
                    object.append((Object)entry.getKey());
                    Log.d((String)"NotifManCompat", (String)object.toString());
                }
                this.ensureServiceUnbound(entry.getValue());
                object2.remove();
            }
        }

        public boolean handleMessage(Message object) {
            switch (object.what) {
                default: {
                    return false;
                }
                case 3: {
                    this.handleRetryListenerQueue((ComponentName)object.obj);
                    return true;
                }
                case 2: {
                    this.handleServiceDisconnected((ComponentName)object.obj);
                    return true;
                }
                case 1: {
                    object = (ServiceConnectedEvent)object.obj;
                    this.handleServiceConnected(object.componentName, object.iBinder);
                    return true;
                }
                case 0: 
            }
            this.handleQueueTask((Task)object.obj);
            return true;
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            if (Log.isLoggable((String)"NotifManCompat", (int)3)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Connected to service ");
                stringBuilder.append((Object)componentName);
                Log.d((String)"NotifManCompat", (String)stringBuilder.toString());
            }
            this.mHandler.obtainMessage(1, (Object)new ServiceConnectedEvent(componentName, iBinder)).sendToTarget();
        }

        public void onServiceDisconnected(ComponentName componentName) {
            if (Log.isLoggable((String)"NotifManCompat", (int)3)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Disconnected from service ");
                stringBuilder.append((Object)componentName);
                Log.d((String)"NotifManCompat", (String)stringBuilder.toString());
            }
            this.mHandler.obtainMessage(2, (Object)componentName).sendToTarget();
        }

        public void queueTask(Task task) {
            this.mHandler.obtainMessage(0, (Object)task).sendToTarget();
        }

        private static class ListenerRecord {
            public boolean bound = false;
            public final ComponentName componentName;
            public int retryCount = 0;
            public INotificationSideChannel service;
            public LinkedList<Task> taskQueue = new LinkedList();

            public ListenerRecord(ComponentName componentName) {
                this.componentName = componentName;
            }
        }

    }

    private static interface Task {
        public void send(INotificationSideChannel var1) throws RemoteException;
    }

}

