// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.app;

import android.app.Notification$Builder;
import android.os.Parcelable;
import java.util.Arrays;
import android.app.PendingIntent;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import android.util.Log;
import android.app.Notification;
import android.util.SparseArray;
import android.os.Bundle;
import java.util.List;
import java.lang.reflect.Field;

class NotificationCompatJellybean
{
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
    
    public static SparseArray<Bundle> buildActionExtrasMap(final List<Bundle> list) {
        SparseArray sparseArray = null;
        SparseArray sparseArray2;
        for (int i = 0; i < list.size(); ++i, sparseArray = sparseArray2) {
            final Bundle bundle = list.get(i);
            sparseArray2 = sparseArray;
            if (bundle != null) {
                if ((sparseArray2 = sparseArray) == null) {
                    sparseArray2 = new SparseArray();
                }
                sparseArray2.put(i, (Object)bundle);
            }
        }
        return (SparseArray<Bundle>)sparseArray;
    }
    
    private static boolean ensureActionReflectionReadyLocked() {
        if (NotificationCompatJellybean.sActionsAccessFailed) {
            return false;
        }
        try {
            if (NotificationCompatJellybean.sActionsField == null) {
                final Class<?> forName = Class.forName("android.app.Notification$Action");
                NotificationCompatJellybean.sActionIconField = forName.getDeclaredField("icon");
                NotificationCompatJellybean.sActionTitleField = forName.getDeclaredField("title");
                NotificationCompatJellybean.sActionIntentField = forName.getDeclaredField("actionIntent");
                (NotificationCompatJellybean.sActionsField = Notification.class.getDeclaredField("actions")).setAccessible(true);
            }
        }
        catch (NoSuchFieldException ex) {
            Log.e("NotificationCompat", "Unable to access notification actions", (Throwable)ex);
            NotificationCompatJellybean.sActionsAccessFailed = true;
        }
        catch (ClassNotFoundException ex2) {
            Log.e("NotificationCompat", "Unable to access notification actions", (Throwable)ex2);
            NotificationCompatJellybean.sActionsAccessFailed = true;
        }
        return NotificationCompatJellybean.sActionsAccessFailed ^ true;
    }
    
    private static RemoteInput fromBundle(final Bundle bundle) {
        final ArrayList stringArrayList = bundle.getStringArrayList("allowedDataTypes");
        final HashSet<String> set = new HashSet<String>();
        if (stringArrayList != null) {
            final Iterator<String> iterator = stringArrayList.iterator();
            while (iterator.hasNext()) {
                set.add(iterator.next());
            }
        }
        return new RemoteInput(bundle.getString("resultKey"), bundle.getCharSequence("label"), bundle.getCharSequenceArray("choices"), bundle.getBoolean("allowFreeFormInput"), bundle.getBundle("extras"), set);
    }
    
    private static RemoteInput[] fromBundleArray(final Bundle[] array) {
        if (array == null) {
            return null;
        }
        final RemoteInput[] array2 = new RemoteInput[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = fromBundle(array[i]);
        }
        return array2;
    }
    
    public static NotificationCompat.Action getAction(final Notification notification, final int n) {
        final Object sActionsLock = NotificationCompatJellybean.sActionsLock;
        // monitorenter(sActionsLock)
        while (true) {
            try {
                try {
                    final Object[] actionObjectsLocked = getActionObjectsLocked(notification);
                    if (actionObjectsLocked != null) {
                        final Object o = actionObjectsLocked[n];
                        final Bundle bundle = null;
                        final Bundle extras = getExtras(notification);
                        Bundle bundle2 = bundle;
                        if (extras != null) {
                            final SparseArray sparseParcelableArray = extras.getSparseParcelableArray("android.support.actionExtras");
                            bundle2 = bundle;
                            if (sparseParcelableArray != null) {
                                bundle2 = (Bundle)sparseParcelableArray.get(n);
                            }
                        }
                        // monitorexit(sActionsLock)
                        return readAction(NotificationCompatJellybean.sActionIconField.getInt(o), (CharSequence)NotificationCompatJellybean.sActionTitleField.get(o), (PendingIntent)NotificationCompatJellybean.sActionIntentField.get(o), bundle2);
                    }
                }
                catch (IllegalAccessException ex) {
                    Log.e("NotificationCompat", "Unable to access notification actions", (Throwable)ex);
                    NotificationCompatJellybean.sActionsAccessFailed = true;
                }
                // monitorexit(sActionsLock)
                while (true) {
                    return null;
                    continue;
                }
            }
            // monitorexit(sActionsLock)
            finally {}
            continue;
        }
    }
    
    public static int getActionCount(final Notification notification) {
        while (true) {
            synchronized (NotificationCompatJellybean.sActionsLock) {
                final Object[] actionObjectsLocked = getActionObjectsLocked(notification);
                if (actionObjectsLocked != null) {
                    return actionObjectsLocked.length;
                }
            }
            return 0;
        }
    }
    
    static NotificationCompat.Action getActionFromBundle(final Bundle bundle) {
        final Bundle bundle2 = bundle.getBundle("extras");
        boolean boolean1 = false;
        if (bundle2 != null) {
            boolean1 = bundle2.getBoolean("android.support.allowGeneratedReplies", false);
        }
        return new NotificationCompat.Action(bundle.getInt("icon"), bundle.getCharSequence("title"), (PendingIntent)bundle.getParcelable("actionIntent"), bundle.getBundle("extras"), fromBundleArray(getBundleArrayFromBundle(bundle, "remoteInputs")), fromBundleArray(getBundleArrayFromBundle(bundle, "dataOnlyRemoteInputs")), boolean1, bundle.getInt("semanticAction"), bundle.getBoolean("showsUserInterface"));
    }
    
    private static Object[] getActionObjectsLocked(final Notification notification) {
        synchronized (NotificationCompatJellybean.sActionsLock) {
            if (!ensureActionReflectionReadyLocked()) {
                return null;
            }
            try {
                return (Object[])NotificationCompatJellybean.sActionsField.get(notification);
            }
            catch (IllegalAccessException ex) {
                Log.e("NotificationCompat", "Unable to access notification actions", (Throwable)ex);
                NotificationCompatJellybean.sActionsAccessFailed = true;
                return null;
            }
        }
    }
    
    private static Bundle[] getBundleArrayFromBundle(final Bundle bundle, final String s) {
        final Parcelable[] parcelableArray = bundle.getParcelableArray(s);
        if (!(parcelableArray instanceof Bundle[]) && parcelableArray != null) {
            final Bundle[] array = Arrays.copyOf(parcelableArray, parcelableArray.length, (Class<? extends Bundle[]>)Bundle[].class);
            bundle.putParcelableArray(s, (Parcelable[])array);
            return array;
        }
        return (Bundle[])parcelableArray;
    }
    
    static Bundle getBundleForAction(final NotificationCompat.Action action) {
        final Bundle bundle = new Bundle();
        bundle.putInt("icon", action.getIcon());
        bundle.putCharSequence("title", action.getTitle());
        bundle.putParcelable("actionIntent", (Parcelable)action.getActionIntent());
        Bundle bundle2;
        if (action.getExtras() != null) {
            bundle2 = new Bundle(action.getExtras());
        }
        else {
            bundle2 = new Bundle();
        }
        bundle2.putBoolean("android.support.allowGeneratedReplies", action.getAllowGeneratedReplies());
        bundle.putBundle("extras", bundle2);
        bundle.putParcelableArray("remoteInputs", (Parcelable[])toBundleArray(action.getRemoteInputs()));
        bundle.putBoolean("showsUserInterface", action.getShowsUserInterface());
        bundle.putInt("semanticAction", action.getSemanticAction());
        return bundle;
    }
    
    public static Bundle getExtras(final Notification notification) {
        synchronized (NotificationCompatJellybean.sExtrasLock) {
            if (NotificationCompatJellybean.sExtrasFieldAccessFailed) {
                return null;
            }
            try {
                if (NotificationCompatJellybean.sExtrasField == null) {
                    final Field declaredField = Notification.class.getDeclaredField("extras");
                    if (!Bundle.class.isAssignableFrom(declaredField.getType())) {
                        Log.e("NotificationCompat", "Notification.extras field is not of type Bundle");
                        NotificationCompatJellybean.sExtrasFieldAccessFailed = true;
                        return null;
                    }
                    declaredField.setAccessible(true);
                    NotificationCompatJellybean.sExtrasField = declaredField;
                }
                Bundle bundle;
                if ((bundle = (Bundle)NotificationCompatJellybean.sExtrasField.get(notification)) == null) {
                    bundle = new Bundle();
                    NotificationCompatJellybean.sExtrasField.set(notification, bundle);
                }
                return bundle;
            }
            catch (NoSuchFieldException ex) {
                Log.e("NotificationCompat", "Unable to access notification extras", (Throwable)ex);
            }
            catch (IllegalAccessException ex2) {
                Log.e("NotificationCompat", "Unable to access notification extras", (Throwable)ex2);
            }
            NotificationCompatJellybean.sExtrasFieldAccessFailed = true;
            return null;
        }
    }
    
    public static NotificationCompat.Action readAction(final int n, final CharSequence charSequence, final PendingIntent pendingIntent, final Bundle bundle) {
        RemoteInput[] fromBundleArray;
        RemoteInput[] fromBundleArray2;
        boolean boolean1;
        if (bundle != null) {
            fromBundleArray = fromBundleArray(getBundleArrayFromBundle(bundle, "android.support.remoteInputs"));
            fromBundleArray2 = fromBundleArray(getBundleArrayFromBundle(bundle, "android.support.dataRemoteInputs"));
            boolean1 = bundle.getBoolean("android.support.allowGeneratedReplies");
        }
        else {
            fromBundleArray = null;
            fromBundleArray2 = null;
            boolean1 = false;
        }
        return new NotificationCompat.Action(n, charSequence, pendingIntent, bundle, fromBundleArray, fromBundleArray2, boolean1, 0, true);
    }
    
    private static Bundle toBundle(final RemoteInput remoteInput) {
        final Bundle bundle = new Bundle();
        bundle.putString("resultKey", remoteInput.getResultKey());
        bundle.putCharSequence("label", remoteInput.getLabel());
        bundle.putCharSequenceArray("choices", remoteInput.getChoices());
        bundle.putBoolean("allowFreeFormInput", remoteInput.getAllowFreeFormInput());
        bundle.putBundle("extras", remoteInput.getExtras());
        final Set<String> allowedDataTypes = remoteInput.getAllowedDataTypes();
        if (allowedDataTypes != null && !allowedDataTypes.isEmpty()) {
            final ArrayList list = new ArrayList<String>(allowedDataTypes.size());
            final Iterator<String> iterator = allowedDataTypes.iterator();
            while (iterator.hasNext()) {
                list.add(iterator.next());
            }
            bundle.putStringArrayList("allowedDataTypes", list);
        }
        return bundle;
    }
    
    private static Bundle[] toBundleArray(final RemoteInput[] array) {
        if (array == null) {
            return null;
        }
        final Bundle[] array2 = new Bundle[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = toBundle(array[i]);
        }
        return array2;
    }
    
    public static Bundle writeActionAndGetExtras(final Notification$Builder notification$Builder, final NotificationCompat.Action action) {
        notification$Builder.addAction(action.getIcon(), action.getTitle(), action.getActionIntent());
        final Bundle bundle = new Bundle(action.getExtras());
        if (action.getRemoteInputs() != null) {
            bundle.putParcelableArray("android.support.remoteInputs", (Parcelable[])toBundleArray(action.getRemoteInputs()));
        }
        if (action.getDataOnlyRemoteInputs() != null) {
            bundle.putParcelableArray("android.support.dataRemoteInputs", (Parcelable[])toBundleArray(action.getDataOnlyRemoteInputs()));
        }
        bundle.putBoolean("android.support.allowGeneratedReplies", action.getAllowGeneratedReplies());
        return bundle;
    }
}
