/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.SharedElementCallback
 *  android.app.SharedElementCallback$OnSharedElementsReadyListener
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentSender
 *  android.content.IntentSender$SendIntentException
 *  android.content.pm.PackageManager
 *  android.graphics.Matrix
 *  android.graphics.RectF
 *  android.net.Uri
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.Looper
 *  android.os.Parcelable
 *  android.view.View
 */
package android.support.v4.app;

import android.app.Activity;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.content.ContextCompat;
import android.view.View;
import java.util.List;
import java.util.Map;

public class ActivityCompat
extends ContextCompat {
    protected ActivityCompat() {
    }

    public static void finishAffinity(Activity activity) {
        if (Build.VERSION.SDK_INT >= 16) {
            activity.finishAffinity();
            return;
        }
        activity.finish();
    }

    public static void finishAfterTransition(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.finishAfterTransition();
            return;
        }
        activity.finish();
    }

    @Nullable
    public static Uri getReferrer(Activity object) {
        if (Build.VERSION.SDK_INT >= 22) {
            return object.getReferrer();
        }
        Uri uri = (Uri)(object = object.getIntent()).getParcelableExtra("android.intent.extra.REFERRER");
        if (uri != null) {
            return uri;
        }
        if ((object = object.getStringExtra("android.intent.extra.REFERRER_NAME")) != null) {
            return Uri.parse((String)object);
        }
        return null;
    }

    public static boolean invalidateOptionsMenu(Activity activity) {
        activity.invalidateOptionsMenu();
        return true;
    }

    public static void postponeEnterTransition(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.postponeEnterTransition();
            return;
        }
    }

    public static void requestPermissions(final @NonNull Activity activity, final @NonNull String[] arrstring, final @IntRange(from=0L) int n) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity instanceof RequestPermissionsRequestCodeValidator) {
                ((RequestPermissionsRequestCodeValidator)activity).validateRequestPermissionsRequestCode(n);
            }
            activity.requestPermissions(arrstring, n);
            return;
        }
        if (activity instanceof OnRequestPermissionsResultCallback) {
            new Handler(Looper.getMainLooper()).post(new Runnable(){

                @Override
                public void run() {
                    int[] arrn = new int[arrstring.length];
                    PackageManager packageManager = activity.getPackageManager();
                    String string2 = activity.getPackageName();
                    int n2 = arrstring.length;
                    for (int i = 0; i < n2; ++i) {
                        arrn[i] = packageManager.checkPermission(arrstring[i], string2);
                    }
                    ((OnRequestPermissionsResultCallback)activity).onRequestPermissionsResult(n, arrstring, arrn);
                }
            });
            return;
        }
    }

    public static void setEnterSharedElementCallback(Activity activity, SharedElementCallback sharedElementCallback) {
        int n = Build.VERSION.SDK_INT;
        SharedElementCallback23Impl sharedElementCallback23Impl = null;
        SharedElementCallback21Impl sharedElementCallback21Impl = null;
        if (n >= 23) {
            if (sharedElementCallback != null) {
                sharedElementCallback21Impl = new SharedElementCallback23Impl(sharedElementCallback);
            }
            activity.setEnterSharedElementCallback((android.app.SharedElementCallback)sharedElementCallback21Impl);
        } else if (Build.VERSION.SDK_INT >= 21) {
            sharedElementCallback21Impl = sharedElementCallback23Impl;
            if (sharedElementCallback != null) {
                sharedElementCallback21Impl = new SharedElementCallback21Impl(sharedElementCallback);
            }
            activity.setEnterSharedElementCallback((android.app.SharedElementCallback)sharedElementCallback21Impl);
            return;
        }
    }

    public static void setExitSharedElementCallback(Activity activity, SharedElementCallback sharedElementCallback) {
        int n = Build.VERSION.SDK_INT;
        SharedElementCallback23Impl sharedElementCallback23Impl = null;
        SharedElementCallback21Impl sharedElementCallback21Impl = null;
        if (n >= 23) {
            if (sharedElementCallback != null) {
                sharedElementCallback21Impl = new SharedElementCallback23Impl(sharedElementCallback);
            }
            activity.setExitSharedElementCallback((android.app.SharedElementCallback)sharedElementCallback21Impl);
        } else if (Build.VERSION.SDK_INT >= 21) {
            sharedElementCallback21Impl = sharedElementCallback23Impl;
            if (sharedElementCallback != null) {
                sharedElementCallback21Impl = new SharedElementCallback21Impl(sharedElementCallback);
            }
            activity.setExitSharedElementCallback((android.app.SharedElementCallback)sharedElementCallback21Impl);
            return;
        }
    }

    public static boolean shouldShowRequestPermissionRationale(@NonNull Activity activity, @NonNull String string2) {
        if (Build.VERSION.SDK_INT >= 23) {
            return activity.shouldShowRequestPermissionRationale(string2);
        }
        return false;
    }

    public static void startActivityForResult(Activity activity, Intent intent, int n, @Nullable Bundle bundle) {
        if (Build.VERSION.SDK_INT >= 16) {
            activity.startActivityForResult(intent, n, bundle);
            return;
        }
        activity.startActivityForResult(intent, n);
    }

    public static void startIntentSenderForResult(Activity activity, IntentSender intentSender, int n, Intent intent, int n2, int n3, int n4, @Nullable Bundle bundle) throws IntentSender.SendIntentException {
        if (Build.VERSION.SDK_INT >= 16) {
            activity.startIntentSenderForResult(intentSender, n, intent, n2, n3, n4, bundle);
            return;
        }
        activity.startIntentSenderForResult(intentSender, n, intent, n2, n3, n4);
    }

    public static void startPostponedEnterTransition(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.startPostponedEnterTransition();
            return;
        }
    }

    public static interface OnRequestPermissionsResultCallback {
        public void onRequestPermissionsResult(int var1, @NonNull String[] var2, @NonNull int[] var3);
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static interface RequestPermissionsRequestCodeValidator {
        public void validateRequestPermissionsRequestCode(int var1);
    }

    @RequiresApi(value=21)
    private static class SharedElementCallback21Impl
    extends android.app.SharedElementCallback {
        protected SharedElementCallback mCallback;

        public SharedElementCallback21Impl(SharedElementCallback sharedElementCallback) {
            this.mCallback = sharedElementCallback;
        }

        public Parcelable onCaptureSharedElementSnapshot(View view, Matrix matrix, RectF rectF) {
            return this.mCallback.onCaptureSharedElementSnapshot(view, matrix, rectF);
        }

        public View onCreateSnapshotView(Context context, Parcelable parcelable) {
            return this.mCallback.onCreateSnapshotView(context, parcelable);
        }

        public void onMapSharedElements(List<String> list, Map<String, View> map) {
            this.mCallback.onMapSharedElements(list, map);
        }

        public void onRejectSharedElements(List<View> list) {
            this.mCallback.onRejectSharedElements(list);
        }

        public void onSharedElementEnd(List<String> list, List<View> list2, List<View> list3) {
            this.mCallback.onSharedElementEnd(list, list2, list3);
        }

        public void onSharedElementStart(List<String> list, List<View> list2, List<View> list3) {
            this.mCallback.onSharedElementStart(list, list2, list3);
        }
    }

    @RequiresApi(value=23)
    private static class SharedElementCallback23Impl
    extends SharedElementCallback21Impl {
        public SharedElementCallback23Impl(SharedElementCallback sharedElementCallback) {
            super(sharedElementCallback);
        }

        public void onSharedElementsArrived(List<String> list, List<View> list2, final SharedElementCallback.OnSharedElementsReadyListener onSharedElementsReadyListener) {
            this.mCallback.onSharedElementsArrived(list, list2, new SharedElementCallback.OnSharedElementsReadyListener(){

                @Override
                public void onSharedElementsReady() {
                    onSharedElementsReadyListener.onSharedElementsReady();
                }
            });
        }

    }

}

