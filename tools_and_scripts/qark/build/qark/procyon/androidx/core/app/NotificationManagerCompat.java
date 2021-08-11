// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.app;

import java.util.ArrayDeque;
import android.os.Message;
import android.content.pm.ResolveInfo;
import android.os.DeadObjectException;
import android.util.Log;
import android.content.Intent;
import java.util.HashMap;
import java.util.Map;
import android.os.HandlerThread;
import android.os.Handler;
import android.content.ServiceConnection;
import android.os.Handler$Callback;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.INotificationSideChannel;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import android.app.NotificationChannelGroup;
import android.app.NotificationChannel;
import android.content.pm.ApplicationInfo;
import java.lang.reflect.InvocationTargetException;
import android.app.AppOpsManager;
import android.os.Build$VERSION;
import android.os.Bundle;
import android.app.Notification;
import android.content.ComponentName;
import android.provider.Settings$Secure;
import java.util.HashSet;
import android.app.NotificationManager;
import android.content.Context;
import java.util.Set;

public final class NotificationManagerCompat
{
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
    private static Set<String> sEnabledNotificationListenerPackages;
    private static String sEnabledNotificationListeners;
    private static final Object sEnabledNotificationListenersLock;
    private static final Object sLock;
    private static SideChannelManager sSideChannelManager;
    private final Context mContext;
    private final NotificationManager mNotificationManager;
    
    static {
        sEnabledNotificationListenersLock = new Object();
        NotificationManagerCompat.sEnabledNotificationListenerPackages = new HashSet<String>();
        sLock = new Object();
    }
    
    private NotificationManagerCompat(final Context mContext) {
        this.mContext = mContext;
        this.mNotificationManager = (NotificationManager)mContext.getSystemService("notification");
    }
    
    public static NotificationManagerCompat from(final Context context) {
        return new NotificationManagerCompat(context);
    }
    
    public static Set<String> getEnabledListenerPackages(Context sEnabledNotificationListenersLock) {
        final String string = Settings$Secure.getString(sEnabledNotificationListenersLock.getContentResolver(), "enabled_notification_listeners");
        sEnabledNotificationListenersLock = (Context)NotificationManagerCompat.sEnabledNotificationListenersLock;
        // monitorenter(sEnabledNotificationListenersLock)
        Label_0101: {
            if (string == null) {
                break Label_0101;
            }
            while (true) {
                while (true) {
                    int n = 0;
                    Label_0114: {
                        try {
                            if (!string.equals(NotificationManagerCompat.sEnabledNotificationListeners)) {
                                final String[] split = string.split(":", -1);
                                final HashSet sEnabledNotificationListenerPackages = new HashSet<String>(split.length);
                                final int length = split.length;
                                n = 0;
                                if (n < length) {
                                    final ComponentName unflattenFromString = ComponentName.unflattenFromString(split[n]);
                                    if (unflattenFromString != null) {
                                        sEnabledNotificationListenerPackages.add(unflattenFromString.getPackageName());
                                    }
                                    break Label_0114;
                                }
                                else {
                                    NotificationManagerCompat.sEnabledNotificationListenerPackages = (Set<String>)sEnabledNotificationListenerPackages;
                                    NotificationManagerCompat.sEnabledNotificationListeners = string;
                                }
                            }
                            return NotificationManagerCompat.sEnabledNotificationListenerPackages;
                        }
                        finally {
                        }
                        // monitorexit(sEnabledNotificationListenersLock)
                    }
                    ++n;
                    continue;
                }
            }
        }
    }
    
    private void pushSideChannelQueue(final Task task) {
        synchronized (NotificationManagerCompat.sLock) {
            if (NotificationManagerCompat.sSideChannelManager == null) {
                NotificationManagerCompat.sSideChannelManager = new SideChannelManager(this.mContext.getApplicationContext());
            }
            NotificationManagerCompat.sSideChannelManager.queueTask(task);
        }
    }
    
    private static boolean useSideChannelForNotification(final Notification notification) {
        final Bundle extras = NotificationCompat.getExtras(notification);
        return extras != null && extras.getBoolean("android.support.useSideChannel");
    }
    
    public boolean areNotificationsEnabled() {
        if (Build$VERSION.SDK_INT >= 24) {
            return this.mNotificationManager.areNotificationsEnabled();
        }
        if (Build$VERSION.SDK_INT >= 19) {
            final AppOpsManager appOpsManager = (AppOpsManager)this.mContext.getSystemService("appops");
            final ApplicationInfo applicationInfo = this.mContext.getApplicationInfo();
            final String packageName = this.mContext.getApplicationContext().getPackageName();
            final int uid = applicationInfo.uid;
            try {
                final Class<?> forName = Class.forName(AppOpsManager.class.getName());
                return (int)forName.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE, String.class).invoke(appOpsManager, (int)forName.getDeclaredField("OP_POST_NOTIFICATION").get(Integer.class), uid, packageName) == 0;
            }
            catch (RuntimeException ex) {
                return true;
            }
            catch (IllegalAccessException ex2) {
                return true;
            }
            catch (InvocationTargetException ex3) {
                return true;
            }
            catch (NoSuchFieldException ex4) {
                return true;
            }
            catch (NoSuchMethodException ex5) {
                return true;
            }
            catch (ClassNotFoundException ex6) {
                return true;
            }
        }
        return true;
    }
    
    public void cancel(final int n) {
        this.cancel(null, n);
    }
    
    public void cancel(final String s, final int n) {
        this.mNotificationManager.cancel(s, n);
        if (Build$VERSION.SDK_INT <= 19) {
            this.pushSideChannelQueue((Task)new CancelTask(this.mContext.getPackageName(), n, s));
        }
    }
    
    public void cancelAll() {
        this.mNotificationManager.cancelAll();
        if (Build$VERSION.SDK_INT <= 19) {
            this.pushSideChannelQueue((Task)new CancelTask(this.mContext.getPackageName()));
        }
    }
    
    public void createNotificationChannel(final NotificationChannel notificationChannel) {
        if (Build$VERSION.SDK_INT >= 26) {
            this.mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }
    
    public void createNotificationChannelGroup(final NotificationChannelGroup notificationChannelGroup) {
        if (Build$VERSION.SDK_INT >= 26) {
            this.mNotificationManager.createNotificationChannelGroup(notificationChannelGroup);
        }
    }
    
    public void createNotificationChannelGroups(final List<NotificationChannelGroup> list) {
        if (Build$VERSION.SDK_INT >= 26) {
            this.mNotificationManager.createNotificationChannelGroups((List)list);
        }
    }
    
    public void createNotificationChannels(final List<NotificationChannel> list) {
        if (Build$VERSION.SDK_INT >= 26) {
            this.mNotificationManager.createNotificationChannels((List)list);
        }
    }
    
    public void deleteNotificationChannel(final String s) {
        if (Build$VERSION.SDK_INT >= 26) {
            this.mNotificationManager.deleteNotificationChannel(s);
        }
    }
    
    public void deleteNotificationChannelGroup(final String s) {
        if (Build$VERSION.SDK_INT >= 26) {
            this.mNotificationManager.deleteNotificationChannelGroup(s);
        }
    }
    
    public int getImportance() {
        if (Build$VERSION.SDK_INT >= 24) {
            return this.mNotificationManager.getImportance();
        }
        return -1000;
    }
    
    public NotificationChannel getNotificationChannel(final String s) {
        if (Build$VERSION.SDK_INT >= 26) {
            return this.mNotificationManager.getNotificationChannel(s);
        }
        return null;
    }
    
    public NotificationChannelGroup getNotificationChannelGroup(final String s) {
        if (Build$VERSION.SDK_INT >= 28) {
            return this.mNotificationManager.getNotificationChannelGroup(s);
        }
        if (Build$VERSION.SDK_INT >= 26) {
            for (final NotificationChannelGroup notificationChannelGroup : this.getNotificationChannelGroups()) {
                if (notificationChannelGroup.getId().equals(s)) {
                    return notificationChannelGroup;
                }
            }
            return null;
        }
        return null;
    }
    
    public List<NotificationChannelGroup> getNotificationChannelGroups() {
        if (Build$VERSION.SDK_INT >= 26) {
            return (List<NotificationChannelGroup>)this.mNotificationManager.getNotificationChannelGroups();
        }
        return Collections.emptyList();
    }
    
    public List<NotificationChannel> getNotificationChannels() {
        if (Build$VERSION.SDK_INT >= 26) {
            return (List<NotificationChannel>)this.mNotificationManager.getNotificationChannels();
        }
        return Collections.emptyList();
    }
    
    public void notify(final int n, final Notification notification) {
        this.notify(null, n, notification);
    }
    
    public void notify(final String s, final int n, final Notification notification) {
        if (useSideChannelForNotification(notification)) {
            this.pushSideChannelQueue((Task)new NotifyTask(this.mContext.getPackageName(), n, s, notification));
            this.mNotificationManager.cancel(s, n);
            return;
        }
        this.mNotificationManager.notify(s, n, notification);
    }
    
    private static class CancelTask implements Task
    {
        final boolean all;
        final int id;
        final String packageName;
        final String tag;
        
        CancelTask(final String packageName) {
            this.packageName = packageName;
            this.id = 0;
            this.tag = null;
            this.all = true;
        }
        
        CancelTask(final String packageName, final int id, final String tag) {
            this.packageName = packageName;
            this.id = id;
            this.tag = tag;
            this.all = false;
        }
        
        @Override
        public void send(final INotificationSideChannel notificationSideChannel) throws RemoteException {
            if (this.all) {
                notificationSideChannel.cancelAll(this.packageName);
                return;
            }
            notificationSideChannel.cancel(this.packageName, this.id, this.tag);
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("CancelTask[");
            sb.append("packageName:");
            sb.append(this.packageName);
            sb.append(", id:");
            sb.append(this.id);
            sb.append(", tag:");
            sb.append(this.tag);
            sb.append(", all:");
            sb.append(this.all);
            sb.append("]");
            return sb.toString();
        }
    }
    
    private static class NotifyTask implements Task
    {
        final int id;
        final Notification notif;
        final String packageName;
        final String tag;
        
        NotifyTask(final String packageName, final int id, final String tag, final Notification notif) {
            this.packageName = packageName;
            this.id = id;
            this.tag = tag;
            this.notif = notif;
        }
        
        @Override
        public void send(final INotificationSideChannel notificationSideChannel) throws RemoteException {
            notificationSideChannel.notify(this.packageName, this.id, this.tag, this.notif);
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("NotifyTask[");
            sb.append("packageName:");
            sb.append(this.packageName);
            sb.append(", id:");
            sb.append(this.id);
            sb.append(", tag:");
            sb.append(this.tag);
            sb.append("]");
            return sb.toString();
        }
    }
    
    private static class ServiceConnectedEvent
    {
        final ComponentName componentName;
        final IBinder iBinder;
        
        ServiceConnectedEvent(final ComponentName componentName, final IBinder iBinder) {
            this.componentName = componentName;
            this.iBinder = iBinder;
        }
    }
    
    private static class SideChannelManager implements Handler$Callback, ServiceConnection
    {
        private static final int MSG_QUEUE_TASK = 0;
        private static final int MSG_RETRY_LISTENER_QUEUE = 3;
        private static final int MSG_SERVICE_CONNECTED = 1;
        private static final int MSG_SERVICE_DISCONNECTED = 2;
        private Set<String> mCachedEnabledPackages;
        private final Context mContext;
        private final Handler mHandler;
        private final HandlerThread mHandlerThread;
        private final Map<ComponentName, ListenerRecord> mRecordMap;
        
        SideChannelManager(final Context mContext) {
            this.mRecordMap = new HashMap<ComponentName, ListenerRecord>();
            this.mCachedEnabledPackages = new HashSet<String>();
            this.mContext = mContext;
            (this.mHandlerThread = new HandlerThread("NotificationManagerCompat")).start();
            this.mHandler = new Handler(this.mHandlerThread.getLooper(), (Handler$Callback)this);
        }
        
        private boolean ensureServiceBound(final ListenerRecord listenerRecord) {
            if (listenerRecord.bound) {
                return true;
            }
            listenerRecord.bound = this.mContext.bindService(new Intent("android.support.BIND_NOTIFICATION_SIDE_CHANNEL").setComponent(listenerRecord.componentName), (ServiceConnection)this, 33);
            if (listenerRecord.bound) {
                listenerRecord.retryCount = 0;
            }
            else {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unable to bind to listener ");
                sb.append(listenerRecord.componentName);
                Log.w("NotifManCompat", sb.toString());
                this.mContext.unbindService((ServiceConnection)this);
            }
            return listenerRecord.bound;
        }
        
        private void ensureServiceUnbound(final ListenerRecord listenerRecord) {
            if (listenerRecord.bound) {
                this.mContext.unbindService((ServiceConnection)this);
                listenerRecord.bound = false;
            }
            listenerRecord.service = null;
        }
        
        private void handleQueueTask(final Task task) {
            this.updateListenerMap();
            for (final ListenerRecord listenerRecord : this.mRecordMap.values()) {
                listenerRecord.taskQueue.add(task);
                this.processListenerQueue(listenerRecord);
            }
        }
        
        private void handleRetryListenerQueue(final ComponentName componentName) {
            final ListenerRecord listenerRecord = this.mRecordMap.get(componentName);
            if (listenerRecord != null) {
                this.processListenerQueue(listenerRecord);
            }
        }
        
        private void handleServiceConnected(final ComponentName componentName, final IBinder binder) {
            final ListenerRecord listenerRecord = this.mRecordMap.get(componentName);
            if (listenerRecord != null) {
                listenerRecord.service = INotificationSideChannel.Stub.asInterface(binder);
                listenerRecord.retryCount = 0;
                this.processListenerQueue(listenerRecord);
            }
        }
        
        private void handleServiceDisconnected(final ComponentName componentName) {
            final ListenerRecord listenerRecord = this.mRecordMap.get(componentName);
            if (listenerRecord != null) {
                this.ensureServiceUnbound(listenerRecord);
            }
        }
        
        private void processListenerQueue(final ListenerRecord listenerRecord) {
            if (Log.isLoggable("NotifManCompat", 3)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Processing component ");
                sb.append(listenerRecord.componentName);
                sb.append(", ");
                sb.append(listenerRecord.taskQueue.size());
                sb.append(" queued tasks");
                Log.d("NotifManCompat", sb.toString());
            }
            if (listenerRecord.taskQueue.isEmpty()) {
                return;
            }
            if (this.ensureServiceBound(listenerRecord) && listenerRecord.service != null) {
                while (true) {
                    final Task task = listenerRecord.taskQueue.peek();
                    if (task == null) {
                        break;
                    }
                    Label_0185: {
                        try {
                            if (Log.isLoggable("NotifManCompat", 3)) {
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append("Sending task ");
                                sb2.append(task);
                                Log.d("NotifManCompat", sb2.toString());
                            }
                            task.send(listenerRecord.service);
                            listenerRecord.taskQueue.remove();
                            continue;
                        }
                        catch (RemoteException ex) {
                            break Label_0185;
                        }
                        catch (DeadObjectException ex2) {
                            if (!Log.isLoggable("NotifManCompat", 3)) {
                                break;
                            }
                            final StringBuilder sb3 = new StringBuilder();
                            sb3.append("Remote service has died: ");
                            sb3.append(listenerRecord.componentName);
                            Log.d("NotifManCompat", sb3.toString());
                            final StringBuilder sb4 = new StringBuilder();
                            sb4.append("RemoteException communicating with ");
                            sb4.append(listenerRecord.componentName);
                            final RemoteException ex;
                            Log.w("NotifManCompat", sb4.toString(), (Throwable)ex);
                        }
                    }
                    break;
                }
                if (!listenerRecord.taskQueue.isEmpty()) {
                    this.scheduleListenerRetry(listenerRecord);
                }
                return;
            }
            this.scheduleListenerRetry(listenerRecord);
        }
        
        private void scheduleListenerRetry(final ListenerRecord listenerRecord) {
            if (this.mHandler.hasMessages(3, (Object)listenerRecord.componentName)) {
                return;
            }
            ++listenerRecord.retryCount;
            if (listenerRecord.retryCount > 6) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Giving up on delivering ");
                sb.append(listenerRecord.taskQueue.size());
                sb.append(" tasks to ");
                sb.append(listenerRecord.componentName);
                sb.append(" after ");
                sb.append(listenerRecord.retryCount);
                sb.append(" retries");
                Log.w("NotifManCompat", sb.toString());
                listenerRecord.taskQueue.clear();
                return;
            }
            final int n = (1 << listenerRecord.retryCount - 1) * 1000;
            if (Log.isLoggable("NotifManCompat", 3)) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Scheduling retry for ");
                sb2.append(n);
                sb2.append(" ms");
                Log.d("NotifManCompat", sb2.toString());
            }
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(3, (Object)listenerRecord.componentName), (long)n);
        }
        
        private void updateListenerMap() {
            final Set<String> enabledListenerPackages = NotificationManagerCompat.getEnabledListenerPackages(this.mContext);
            if (enabledListenerPackages.equals(this.mCachedEnabledPackages)) {
                return;
            }
            this.mCachedEnabledPackages = enabledListenerPackages;
            final List queryIntentServices = this.mContext.getPackageManager().queryIntentServices(new Intent().setAction("android.support.BIND_NOTIFICATION_SIDE_CHANNEL"), 0);
            final HashSet<ComponentName> set = new HashSet<ComponentName>();
            for (final ResolveInfo resolveInfo : queryIntentServices) {
                if (!enabledListenerPackages.contains(resolveInfo.serviceInfo.packageName)) {
                    continue;
                }
                final ComponentName componentName = new ComponentName(resolveInfo.serviceInfo.packageName, resolveInfo.serviceInfo.name);
                if (resolveInfo.serviceInfo.permission != null) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Permission present on component ");
                    sb.append(componentName);
                    sb.append(", not adding listener record.");
                    Log.w("NotifManCompat", sb.toString());
                }
                else {
                    set.add(componentName);
                }
            }
            for (final ComponentName componentName2 : set) {
                if (!this.mRecordMap.containsKey(componentName2)) {
                    if (Log.isLoggable("NotifManCompat", 3)) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Adding listener record for ");
                        sb2.append(componentName2);
                        Log.d("NotifManCompat", sb2.toString());
                    }
                    this.mRecordMap.put(componentName2, new ListenerRecord(componentName2));
                }
            }
            final Iterator<Map.Entry<ComponentName, ListenerRecord>> iterator3 = this.mRecordMap.entrySet().iterator();
            while (iterator3.hasNext()) {
                final Map.Entry<ComponentName, ListenerRecord> entry = iterator3.next();
                if (!set.contains(entry.getKey())) {
                    if (Log.isLoggable("NotifManCompat", 3)) {
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("Removing listener record for ");
                        sb3.append(entry.getKey());
                        Log.d("NotifManCompat", sb3.toString());
                    }
                    this.ensureServiceUnbound(entry.getValue());
                    iterator3.remove();
                }
            }
        }
        
        public boolean handleMessage(final Message message) {
            final int what = message.what;
            if (what == 0) {
                this.handleQueueTask((Task)message.obj);
                return true;
            }
            if (what == 1) {
                final ServiceConnectedEvent serviceConnectedEvent = (ServiceConnectedEvent)message.obj;
                this.handleServiceConnected(serviceConnectedEvent.componentName, serviceConnectedEvent.iBinder);
                return true;
            }
            if (what == 2) {
                this.handleServiceDisconnected((ComponentName)message.obj);
                return true;
            }
            if (what != 3) {
                return false;
            }
            this.handleRetryListenerQueue((ComponentName)message.obj);
            return true;
        }
        
        public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
            if (Log.isLoggable("NotifManCompat", 3)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Connected to service ");
                sb.append(componentName);
                Log.d("NotifManCompat", sb.toString());
            }
            this.mHandler.obtainMessage(1, (Object)new ServiceConnectedEvent(componentName, binder)).sendToTarget();
        }
        
        public void onServiceDisconnected(final ComponentName componentName) {
            if (Log.isLoggable("NotifManCompat", 3)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Disconnected from service ");
                sb.append(componentName);
                Log.d("NotifManCompat", sb.toString());
            }
            this.mHandler.obtainMessage(2, (Object)componentName).sendToTarget();
        }
        
        public void queueTask(final Task task) {
            this.mHandler.obtainMessage(0, (Object)task).sendToTarget();
        }
        
        private static class ListenerRecord
        {
            boolean bound;
            final ComponentName componentName;
            int retryCount;
            INotificationSideChannel service;
            ArrayDeque<Task> taskQueue;
            
            ListenerRecord(final ComponentName componentName) {
                this.bound = false;
                this.taskQueue = new ArrayDeque<Task>();
                this.retryCount = 0;
                this.componentName = componentName;
            }
        }
    }
    
    private interface Task
    {
        void send(final INotificationSideChannel p0) throws RemoteException;
    }
}
