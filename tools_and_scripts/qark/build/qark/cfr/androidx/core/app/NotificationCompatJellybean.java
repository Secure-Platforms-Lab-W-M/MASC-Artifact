/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Notification
 *  android.app.Notification$Builder
 *  android.app.PendingIntent
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.util.Log
 *  android.util.SparseArray
 */
package androidx.core.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseArray;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

class NotificationCompatJellybean {
    static final String EXTRA_ALLOW_GENERATED_REPLIES = "android.support.allowGeneratedReplies";
    static final String EXTRA_DATA_ONLY_REMOTE_INPUTS = "android.support.dataRemoteInputs";
    private static final String KEY_ACTION_INTENT = "actionIntent";
    private static final String KEY_ALLOWED_DATA_TYPES = "allowedDataTypes";
    private static final String KEY_ALLOW_FREE_FORM_INPUT = "allowFreeFormInput";
    private static final String KEY_CHOICES = "choices";
    private static final String KEY_DATA_ONLY_REMOTE_INPUTS = "dataOnlyRemoteInputs";
    private static final String KEY_EXTRAS = "extras";
    private static final String KEY_ICON = "icon";
    private static final String KEY_LABEL = "label";
    private static final String KEY_REMOTE_INPUTS = "remoteInputs";
    private static final String KEY_RESULT_KEY = "resultKey";
    private static final String KEY_SEMANTIC_ACTION = "semanticAction";
    private static final String KEY_SHOWS_USER_INTERFACE = "showsUserInterface";
    private static final String KEY_TITLE = "title";
    public static final String TAG = "NotificationCompat";
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

    private NotificationCompatJellybean() {
    }

    public static SparseArray<Bundle> buildActionExtrasMap(List<Bundle> list) {
        SparseArray sparseArray = null;
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            Bundle bundle = list.get(i);
            SparseArray sparseArray2 = sparseArray;
            if (bundle != null) {
                sparseArray2 = sparseArray;
                if (sparseArray == null) {
                    sparseArray2 = new SparseArray();
                }
                sparseArray2.put(i, (Object)bundle);
            }
            sparseArray = sparseArray2;
        }
        return sparseArray;
    }

    private static boolean ensureActionReflectionReadyLocked() {
        if (sActionsAccessFailed) {
            return false;
        }
        try {
            if (sActionsField == null) {
                Class class_ = Class.forName("android.app.Notification$Action");
                sActionIconField = class_.getDeclaredField("icon");
                sActionTitleField = class_.getDeclaredField("title");
                sActionIntentField = class_.getDeclaredField("actionIntent");
                class_ = Notification.class.getDeclaredField("actions");
                sActionsField = class_;
                class_.setAccessible(true);
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
        return sActionsAccessFailed ^ true;
    }

    private static RemoteInput fromBundle(Bundle bundle) {
        Object object = bundle.getStringArrayList("allowedDataTypes");
        HashSet<String> hashSet = new HashSet<String>();
        if (object != null) {
            object = object.iterator();
            while (object.hasNext()) {
                hashSet.add((String)object.next());
            }
        }
        return new RemoteInput(bundle.getString("resultKey"), bundle.getCharSequence("label"), bundle.getCharSequenceArray("choices"), bundle.getBoolean("allowFreeFormInput"), bundle.getBundle("extras"), hashSet);
    }

    private static RemoteInput[] fromBundleArray(Bundle[] arrbundle) {
        if (arrbundle == null) {
            return null;
        }
        RemoteInput[] arrremoteInput = new RemoteInput[arrbundle.length];
        for (int i = 0; i < arrbundle.length; ++i) {
            arrremoteInput[i] = NotificationCompatJellybean.fromBundle(arrbundle[i]);
        }
        return arrremoteInput;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static NotificationCompat.Action getAction(Notification object, int n) {
        Object object2 = sActionsLock;
        synchronized (object2) {
            Bundle bundle = NotificationCompatJellybean.getActionObjectsLocked((Notification)object);
            if (bundle == null) return null;
            Object object3 = bundle[n];
            bundle = null;
            try {
                try {
                    Bundle bundle2 = NotificationCompatJellybean.getExtras((Notification)object);
                    object = bundle;
                    if (bundle2 == null) return NotificationCompatJellybean.readAction(sActionIconField.getInt(object3), (CharSequence)sActionTitleField.get(object3), (PendingIntent)sActionIntentField.get(object3), (Bundle)object);
                    bundle2 = bundle2.getSparseParcelableArray("android.support.actionExtras");
                    object = bundle;
                    if (bundle2 == null) return NotificationCompatJellybean.readAction(sActionIconField.getInt(object3), (CharSequence)sActionTitleField.get(object3), (PendingIntent)sActionIntentField.get(object3), (Bundle)object);
                    object = (Bundle)bundle2.get(n);
                    return NotificationCompatJellybean.readAction(sActionIconField.getInt(object3), (CharSequence)sActionTitleField.get(object3), (PendingIntent)sActionIntentField.get(object3), (Bundle)object);
                }
                catch (IllegalAccessException illegalAccessException) {
                    Log.e((String)"NotificationCompat", (String)"Unable to access notification actions", (Throwable)illegalAccessException);
                    sActionsAccessFailed = true;
                }
                return null;
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

    static NotificationCompat.Action getActionFromBundle(Bundle bundle) {
        Bundle bundle2 = bundle.getBundle("extras");
        boolean bl = false;
        if (bundle2 != null) {
            bl = bundle2.getBoolean("android.support.allowGeneratedReplies", false);
        }
        return new NotificationCompat.Action(bundle.getInt("icon"), bundle.getCharSequence("title"), (PendingIntent)bundle.getParcelable("actionIntent"), bundle.getBundle("extras"), NotificationCompatJellybean.fromBundleArray(NotificationCompatJellybean.getBundleArrayFromBundle(bundle, "remoteInputs")), NotificationCompatJellybean.fromBundleArray(NotificationCompatJellybean.getBundleArrayFromBundle(bundle, "dataOnlyRemoteInputs")), bl, bundle.getInt("semanticAction"), bundle.getBoolean("showsUserInterface"));
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

    private static Bundle[] getBundleArrayFromBundle(Bundle bundle, String string2) {
        Parcelable[] arrparcelable = bundle.getParcelableArray(string2);
        if (!(arrparcelable instanceof Bundle[]) && arrparcelable != null) {
            arrparcelable = (Bundle[])Arrays.copyOf(arrparcelable, arrparcelable.length, Bundle[].class);
            bundle.putParcelableArray(string2, arrparcelable);
            return arrparcelable;
        }
        return (Bundle[])arrparcelable;
    }

    static Bundle getBundleForAction(NotificationCompat.Action action) {
        Bundle bundle = new Bundle();
        bundle.putInt("icon", action.getIcon());
        bundle.putCharSequence("title", action.getTitle());
        bundle.putParcelable("actionIntent", (Parcelable)action.getActionIntent());
        Bundle bundle2 = action.getExtras() != null ? new Bundle(action.getExtras()) : new Bundle();
        bundle2.putBoolean("android.support.allowGeneratedReplies", action.getAllowGeneratedReplies());
        bundle.putBundle("extras", bundle2);
        bundle.putParcelableArray("remoteInputs", (Parcelable[])NotificationCompatJellybean.toBundleArray(action.getRemoteInputs()));
        bundle.putBoolean("showsUserInterface", action.getShowsUserInterface());
        bundle.putInt("semanticAction", action.getSemanticAction());
        return bundle;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Bundle getExtras(Notification notification) {
        Object object = sExtrasLock;
        synchronized (object) {
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
                Bundle bundle = (Bundle)sExtrasField.get((Object)notification);
                field = bundle;
                if (bundle == null) {
                    field = new Bundle();
                    sExtrasField.set((Object)notification, field);
                }
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

    public static NotificationCompat.Action readAction(int n, CharSequence charSequence, PendingIntent pendingIntent, Bundle bundle) {
        RemoteInput[] arrremoteInput;
        RemoteInput[] arrremoteInput2;
        boolean bl;
        if (bundle != null) {
            arrremoteInput = NotificationCompatJellybean.fromBundleArray(NotificationCompatJellybean.getBundleArrayFromBundle(bundle, "android.support.remoteInputs"));
            arrremoteInput2 = NotificationCompatJellybean.fromBundleArray(NotificationCompatJellybean.getBundleArrayFromBundle(bundle, "android.support.dataRemoteInputs"));
            bl = bundle.getBoolean("android.support.allowGeneratedReplies");
        } else {
            arrremoteInput = null;
            arrremoteInput2 = null;
            bl = false;
        }
        return new NotificationCompat.Action(n, charSequence, pendingIntent, bundle, arrremoteInput, arrremoteInput2, bl, 0, true);
    }

    private static Bundle toBundle(RemoteInput object) {
        Bundle bundle = new Bundle();
        bundle.putString("resultKey", object.getResultKey());
        bundle.putCharSequence("label", object.getLabel());
        bundle.putCharSequenceArray("choices", object.getChoices());
        bundle.putBoolean("allowFreeFormInput", object.getAllowFreeFormInput());
        bundle.putBundle("extras", object.getExtras());
        Object object2 = object.getAllowedDataTypes();
        if (object2 != null && !object2.isEmpty()) {
            object = new ArrayList(object2.size());
            object2 = object2.iterator();
            while (object2.hasNext()) {
                object.add((String)object2.next());
            }
            bundle.putStringArrayList("allowedDataTypes", (ArrayList)object);
        }
        return bundle;
    }

    private static Bundle[] toBundleArray(RemoteInput[] arrremoteInput) {
        if (arrremoteInput == null) {
            return null;
        }
        Bundle[] arrbundle = new Bundle[arrremoteInput.length];
        for (int i = 0; i < arrremoteInput.length; ++i) {
            arrbundle[i] = NotificationCompatJellybean.toBundle(arrremoteInput[i]);
        }
        return arrbundle;
    }

    public static Bundle writeActionAndGetExtras(Notification.Builder builder, NotificationCompat.Action action) {
        builder.addAction(action.getIcon(), action.getTitle(), action.getActionIntent());
        builder = new Bundle(action.getExtras());
        if (action.getRemoteInputs() != null) {
            builder.putParcelableArray("android.support.remoteInputs", (Parcelable[])NotificationCompatJellybean.toBundleArray(action.getRemoteInputs()));
        }
        if (action.getDataOnlyRemoteInputs() != null) {
            builder.putParcelableArray("android.support.dataRemoteInputs", (Parcelable[])NotificationCompatJellybean.toBundleArray(action.getDataOnlyRemoteInputs()));
        }
        builder.putBoolean("android.support.allowGeneratedReplies", action.getAllowGeneratedReplies());
        return builder;
    }
}

