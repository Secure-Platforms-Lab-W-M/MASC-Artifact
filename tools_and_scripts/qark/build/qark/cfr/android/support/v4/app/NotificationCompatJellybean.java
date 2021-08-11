/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Notification
 *  android.app.Notification$BigPictureStyle
 *  android.app.Notification$BigTextStyle
 *  android.app.Notification$Builder
 *  android.app.Notification$InboxStyle
 *  android.app.PendingIntent
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.net.Uri
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.util.Log
 *  android.util.SparseArray
 *  android.widget.RemoteViews
 */
package android.support.v4.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.BundleUtil;
import android.support.v4.app.NotificationBuilderWithActions;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompatBase;
import android.support.v4.app.RemoteInputCompatBase;
import android.support.v4.app.RemoteInputCompatJellybean;
import android.util.Log;
import android.util.SparseArray;
import android.widget.RemoteViews;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@RequiresApi(value=16)
class NotificationCompatJellybean {
    static final String EXTRA_ALLOW_GENERATED_REPLIES = "android.support.allowGeneratedReplies";
    static final String EXTRA_DATA_ONLY_REMOTE_INPUTS = "android.support.dataRemoteInputs";
    private static final String KEY_ACTION_INTENT = "actionIntent";
    private static final String KEY_DATA_ONLY_REMOTE_INPUTS = "dataOnlyRemoteInputs";
    private static final String KEY_EXTRAS = "extras";
    private static final String KEY_ICON = "icon";
    private static final String KEY_REMOTE_INPUTS = "remoteInputs";
    private static final String KEY_TITLE = "title";
    public static final String TAG = "NotificationCompat";
    private static Class<?> sActionClass;
    private static Field sActionIconField;
    private static Field sActionIntentField;
    private static Field sActionTitleField;
    private static boolean sActionsAccessFailed;
    private static Field sActionsField;
    private static final Object sActionsLock;
    private static Field sExtrasField;
    private static boolean sExtrasFieldAccessFailed;
    private static final Object sExtrasLock;

    static {
        sExtrasLock = new Object();
        sActionsLock = new Object();
    }

    NotificationCompatJellybean() {
    }

    public static void addBigPictureStyle(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor, CharSequence charSequence, boolean bl, CharSequence charSequence2, Bitmap bitmap, Bitmap bitmap2, boolean bl2) {
        notificationBuilderWithBuilderAccessor = new Notification.BigPictureStyle(notificationBuilderWithBuilderAccessor.getBuilder()).setBigContentTitle(charSequence).bigPicture(bitmap);
        if (bl2) {
            notificationBuilderWithBuilderAccessor.bigLargeIcon(bitmap2);
        }
        if (bl) {
            notificationBuilderWithBuilderAccessor.setSummaryText(charSequence2);
            return;
        }
    }

    public static void addBigTextStyle(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor, CharSequence charSequence, boolean bl, CharSequence charSequence2, CharSequence charSequence3) {
        notificationBuilderWithBuilderAccessor = new Notification.BigTextStyle(notificationBuilderWithBuilderAccessor.getBuilder()).setBigContentTitle(charSequence).bigText(charSequence3);
        if (bl) {
            notificationBuilderWithBuilderAccessor.setSummaryText(charSequence2);
            return;
        }
    }

    public static void addInboxStyle(NotificationBuilderWithBuilderAccessor notificationBuilderWithBuilderAccessor, CharSequence object, boolean bl, CharSequence charSequence, ArrayList<CharSequence> arrayList) {
        notificationBuilderWithBuilderAccessor = new Notification.InboxStyle(notificationBuilderWithBuilderAccessor.getBuilder()).setBigContentTitle((CharSequence)object);
        if (bl) {
            notificationBuilderWithBuilderAccessor.setSummaryText(charSequence);
        }
        object = arrayList.iterator();
        while (object.hasNext()) {
            notificationBuilderWithBuilderAccessor.addLine((CharSequence)object.next());
        }
    }

    public static SparseArray<Bundle> buildActionExtrasMap(List<Bundle> list) {
        SparseArray sparseArray = null;
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            Bundle bundle = list.get(i);
            if (bundle == null) continue;
            if (sparseArray == null) {
                sparseArray = new SparseArray();
            }
            sparseArray.put(i, (Object)bundle);
        }
        return sparseArray;
    }

    private static boolean ensureActionReflectionReadyLocked() {
        if (sActionsAccessFailed) {
            return false;
        }
        try {
            if (sActionsField == null) {
                sActionClass = Class.forName("android.app.Notification$Action");
                sActionIconField = sActionClass.getDeclaredField("icon");
                sActionTitleField = sActionClass.getDeclaredField("title");
                sActionIntentField = sActionClass.getDeclaredField("actionIntent");
                sActionsField = Notification.class.getDeclaredField("actions");
                sActionsField.setAccessible(true);
            }
        }
        catch (NoSuchFieldException noSuchFieldException) {
            Log.e((String)"NotificationCompat", (String)"Unable to access notification actions", (Throwable)noSuchFieldException);
            sActionsAccessFailed = true;
        }
        catch (ClassNotFoundException classNotFoundException) {
            Log.e((String)"NotificationCompat", (String)"Unable to access notification actions", (Throwable)classNotFoundException);
            sActionsAccessFailed = true;
        }
        return true ^ sActionsAccessFailed;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static NotificationCompatBase.Action getAction(Notification object, int n, NotificationCompatBase.Action.Factory factory, RemoteInputCompatBase.RemoteInput.Factory factory2) {
        Object object2 = sActionsLock;
        synchronized (object2) {
            SparseArray sparseArray = NotificationCompatJellybean.getActionObjectsLocked((Notification)object);
            if (sparseArray == null) return null;
            Object object3 = sparseArray[n];
            sparseArray = null;
            try {
                block6 : {
                    block7 : {
                        try {
                            object = NotificationCompatJellybean.getExtras((Notification)object);
                            if (object == null) break block6;
                            if ((object = object.getSparseParcelableArray("android.support.actionExtras")) == null) break block7;
                            object = (Bundle)object.get(n);
                            return NotificationCompatJellybean.readAction(factory, factory2, sActionIconField.getInt(object3), (CharSequence)sActionTitleField.get(object3), (PendingIntent)sActionIntentField.get(object3), (Bundle)object);
                        }
                        catch (IllegalAccessException illegalAccessException) {
                            Log.e((String)"NotificationCompat", (String)"Unable to access notification actions", (Throwable)illegalAccessException);
                            sActionsAccessFailed = true;
                        }
                        return null;
                    }
                    object = sparseArray;
                    return NotificationCompatJellybean.readAction(factory, factory2, sActionIconField.getInt(object3), (CharSequence)sActionTitleField.get(object3), (PendingIntent)sActionIntentField.get(object3), (Bundle)object);
                }
                object = sparseArray;
                return NotificationCompatJellybean.readAction(factory, factory2, sActionIconField.getInt(object3), (CharSequence)sActionTitleField.get(object3), (PendingIntent)sActionIntentField.get(object3), (Bundle)object);
            }
            catch (Throwable throwable) {}
            throw throwable;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static int getActionCount(Notification arrobject) {
        Object object = sActionsLock;
        synchronized (object) {
            arrobject = NotificationCompatJellybean.getActionObjectsLocked((Notification)arrobject);
            if (arrobject == null) return 0;
            return arrobject.length;
        }
    }

    private static NotificationCompatBase.Action getActionFromBundle(Bundle bundle, NotificationCompatBase.Action.Factory factory, RemoteInputCompatBase.RemoteInput.Factory factory2) {
        Bundle bundle2 = bundle.getBundle("extras");
        boolean bl = false;
        if (bundle2 != null) {
            bl = bundle2.getBoolean("android.support.allowGeneratedReplies", false);
        }
        return factory.build(bundle.getInt("icon"), bundle.getCharSequence("title"), (PendingIntent)bundle.getParcelable("actionIntent"), bundle.getBundle("extras"), RemoteInputCompatJellybean.fromBundleArray(BundleUtil.getBundleArrayFromBundle(bundle, "remoteInputs"), factory2), RemoteInputCompatJellybean.fromBundleArray(BundleUtil.getBundleArrayFromBundle(bundle, "dataOnlyRemoteInputs"), factory2), bl);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static Object[] getActionObjectsLocked(Notification arrobject) {
        Object object = sActionsLock;
        synchronized (object) {
            if (!NotificationCompatJellybean.ensureActionReflectionReadyLocked()) {
                return null;
            }
            try {
                return (Object[])sActionsField.get(arrobject);
            }
            catch (IllegalAccessException illegalAccessException) {
                Log.e((String)"NotificationCompat", (String)"Unable to access notification actions", (Throwable)illegalAccessException);
                sActionsAccessFailed = true;
                return null;
            }
        }
    }

    public static NotificationCompatBase.Action[] getActionsFromParcelableArrayList(ArrayList<Parcelable> arrayList, NotificationCompatBase.Action.Factory factory, RemoteInputCompatBase.RemoteInput.Factory factory2) {
        if (arrayList == null) {
            return null;
        }
        NotificationCompatBase.Action[] arraction = factory.newArray(arrayList.size());
        for (int i = 0; i < arraction.length; ++i) {
            arraction[i] = NotificationCompatJellybean.getActionFromBundle((Bundle)arrayList.get(i), factory, factory2);
        }
        return arraction;
    }

    private static Bundle getBundleForAction(NotificationCompatBase.Action action) {
        Bundle bundle = new Bundle();
        bundle.putInt("icon", action.getIcon());
        bundle.putCharSequence("title", action.getTitle());
        bundle.putParcelable("actionIntent", (Parcelable)action.getActionIntent());
        Bundle bundle2 = action.getExtras() != null ? new Bundle(action.getExtras()) : new Bundle();
        bundle2.putBoolean("android.support.allowGeneratedReplies", action.getAllowGeneratedReplies());
        bundle.putBundle("extras", bundle2);
        bundle.putParcelableArray("remoteInputs", (Parcelable[])RemoteInputCompatJellybean.toBundleArray(action.getRemoteInputs()));
        return bundle;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Bundle getExtras(Notification object) {
        Object object2 = sExtrasLock;
        synchronized (object2) {
            if (sExtrasFieldAccessFailed) {
                return null;
            }
            try {
                Field field;
                if (sExtrasField == null) {
                    field = Notification.class.getDeclaredField("extras");
                    if (!Bundle.class.isAssignableFrom(field.getType())) {
                        Log.e((String)"NotificationCompat", (String)"Notification.extras field is not of type Bundle");
                        sExtrasFieldAccessFailed = true;
                        return null;
                    }
                    field.setAccessible(true);
                    sExtrasField = field;
                }
                field = (Bundle)sExtrasField.get(object);
                if (field != null) return field;
                field = new Bundle();
                sExtrasField.set(object, field);
                return field;
            }
            catch (NoSuchFieldException noSuchFieldException) {
                Log.e((String)"NotificationCompat", (String)"Unable to access notification extras", (Throwable)noSuchFieldException);
            }
            catch (IllegalAccessException illegalAccessException) {
                Log.e((String)"NotificationCompat", (String)"Unable to access notification extras", (Throwable)illegalAccessException);
            }
            sExtrasFieldAccessFailed = true;
            return null;
        }
    }

    public static ArrayList<Parcelable> getParcelableArrayListForActions(NotificationCompatBase.Action[] arraction) {
        if (arraction == null) {
            return null;
        }
        ArrayList<Parcelable> arrayList = new ArrayList<Parcelable>(arraction.length);
        int n = arraction.length;
        for (int i = 0; i < n; ++i) {
            arrayList.add((Parcelable)NotificationCompatJellybean.getBundleForAction(arraction[i]));
        }
        return arrayList;
    }

    public static NotificationCompatBase.Action readAction(NotificationCompatBase.Action.Factory factory, RemoteInputCompatBase.RemoteInput.Factory arrremoteInput, int n, CharSequence charSequence, PendingIntent pendingIntent, Bundle bundle) {
        boolean bl;
        RemoteInputCompatBase.RemoteInput[] arrremoteInput2;
        if (bundle != null) {
            arrremoteInput2 = RemoteInputCompatJellybean.fromBundleArray(BundleUtil.getBundleArrayFromBundle(bundle, "android.support.remoteInputs"), (RemoteInputCompatBase.RemoteInput.Factory)arrremoteInput);
            RemoteInputCompatBase.RemoteInput[] arrremoteInput3 = RemoteInputCompatJellybean.fromBundleArray(BundleUtil.getBundleArrayFromBundle(bundle, "android.support.dataRemoteInputs"), (RemoteInputCompatBase.RemoteInput.Factory)arrremoteInput);
            bl = bundle.getBoolean("android.support.allowGeneratedReplies");
            arrremoteInput = arrremoteInput2;
            arrremoteInput2 = arrremoteInput3;
        } else {
            arrremoteInput = null;
            arrremoteInput2 = null;
            bl = false;
        }
        return factory.build(n, charSequence, pendingIntent, bundle, arrremoteInput, arrremoteInput2, bl);
    }

    public static Bundle writeActionAndGetExtras(Notification.Builder builder, NotificationCompatBase.Action action) {
        builder.addAction(action.getIcon(), action.getTitle(), action.getActionIntent());
        builder = new Bundle(action.getExtras());
        if (action.getRemoteInputs() != null) {
            builder.putParcelableArray("android.support.remoteInputs", (Parcelable[])RemoteInputCompatJellybean.toBundleArray(action.getRemoteInputs()));
        }
        if (action.getDataOnlyRemoteInputs() != null) {
            builder.putParcelableArray("android.support.dataRemoteInputs", (Parcelable[])RemoteInputCompatJellybean.toBundleArray(action.getDataOnlyRemoteInputs()));
        }
        builder.putBoolean("android.support.allowGeneratedReplies", action.getAllowGeneratedReplies());
        return builder;
    }

    public static class Builder
    implements NotificationBuilderWithBuilderAccessor,
    NotificationBuilderWithActions {
        private Notification.Builder b;
        private List<Bundle> mActionExtrasList = new ArrayList<Bundle>();
        private RemoteViews mBigContentView;
        private RemoteViews mContentView;
        private final Bundle mExtras;

        public Builder(Context context, Notification notification, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, RemoteViews remoteViews, int n, PendingIntent pendingIntent, PendingIntent pendingIntent2, Bitmap bitmap, int n2, int n3, boolean bl, boolean bl2, int n4, CharSequence charSequence4, boolean bl3, Bundle bundle, String string2, boolean bl4, String string3, RemoteViews remoteViews2, RemoteViews remoteViews3) {
            context = new Notification.Builder(context).setWhen(notification.when).setSmallIcon(notification.icon, notification.iconLevel).setContent(notification.contentView).setTicker(notification.tickerText, remoteViews).setSound(notification.sound, notification.audioStreamType).setVibrate(notification.vibrate).setLights(notification.ledARGB, notification.ledOnMS, notification.ledOffMS);
            int n5 = notification.flags;
            boolean bl5 = false;
            boolean bl6 = (n5 & 2) != 0;
            context = context.setOngoing(bl6);
            bl6 = (notification.flags & 8) != 0;
            context = context.setOnlyAlertOnce(bl6);
            bl6 = (notification.flags & 16) != 0;
            context = context.setAutoCancel(bl6).setDefaults(notification.defaults).setContentTitle(charSequence).setContentText(charSequence2).setSubText(charSequence4).setContentInfo(charSequence3).setContentIntent(pendingIntent).setDeleteIntent(notification.deleteIntent);
            bl6 = bl5;
            if ((notification.flags & 128) != 0) {
                bl6 = true;
            }
            this.b = context.setFullScreenIntent(pendingIntent2, bl6).setLargeIcon(bitmap).setNumber(n).setUsesChronometer(bl2).setPriority(n4).setProgress(n2, n3, bl);
            this.mExtras = new Bundle();
            if (bundle != null) {
                this.mExtras.putAll(bundle);
            }
            if (bl3) {
                this.mExtras.putBoolean("android.support.localOnly", true);
            }
            if (string2 != null) {
                this.mExtras.putString("android.support.groupKey", string2);
                if (bl4) {
                    this.mExtras.putBoolean("android.support.isGroupSummary", true);
                } else {
                    this.mExtras.putBoolean("android.support.useSideChannel", true);
                }
            }
            if (string3 != null) {
                this.mExtras.putString("android.support.sortKey", string3);
            }
            this.mContentView = remoteViews2;
            this.mBigContentView = remoteViews3;
        }

        @Override
        public void addAction(NotificationCompatBase.Action action) {
            this.mActionExtrasList.add(NotificationCompatJellybean.writeActionAndGetExtras(this.b, action));
        }

        @Override
        public Notification build() {
            Notification notification = this.b.build();
            RemoteViews remoteViews = NotificationCompatJellybean.getExtras(notification);
            Bundle bundle = new Bundle(this.mExtras);
            for (String string2 : this.mExtras.keySet()) {
                if (!remoteViews.containsKey(string2)) continue;
                bundle.remove(string2);
            }
            remoteViews.putAll(bundle);
            remoteViews = NotificationCompatJellybean.buildActionExtrasMap(this.mActionExtrasList);
            if (remoteViews != null) {
                NotificationCompatJellybean.getExtras(notification).putSparseParcelableArray("android.support.actionExtras", (SparseArray)remoteViews);
            }
            if ((remoteViews = this.mContentView) != null) {
                notification.contentView = remoteViews;
            }
            if ((remoteViews = this.mBigContentView) != null) {
                notification.bigContentView = remoteViews;
                return notification;
            }
            return notification;
        }

        @Override
        public Notification.Builder getBuilder() {
            return this.b;
        }
    }

}

