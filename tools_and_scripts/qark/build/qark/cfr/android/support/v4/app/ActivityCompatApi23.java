/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.app.Activity
 *  android.app.SharedElementCallback
 *  android.app.SharedElementCallback$OnSharedElementsReadyListener
 *  android.content.Context
 *  android.graphics.Matrix
 *  android.graphics.RectF
 *  android.os.Parcelable
 *  android.view.View
 */
package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.SharedElementCallback;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompatApi21;
import android.view.View;
import java.util.List;
import java.util.Map;

@TargetApi(value=23)
@RequiresApi(value=23)
class ActivityCompatApi23 {
    ActivityCompatApi23() {
    }

    private static SharedElementCallback createCallback(SharedElementCallback23 sharedElementCallback23) {
        SharedElementCallbackImpl sharedElementCallbackImpl = null;
        if (sharedElementCallback23 != null) {
            sharedElementCallbackImpl = new SharedElementCallbackImpl(sharedElementCallback23);
        }
        return sharedElementCallbackImpl;
    }

    public static void requestPermissions(Activity activity, String[] arrstring, int n) {
        if (activity instanceof RequestPermissionsRequestCodeValidator) {
            ((RequestPermissionsRequestCodeValidator)activity).validateRequestPermissionsRequestCode(n);
        }
        activity.requestPermissions(arrstring, n);
    }

    public static void setEnterSharedElementCallback(Activity activity, SharedElementCallback23 sharedElementCallback23) {
        activity.setEnterSharedElementCallback(ActivityCompatApi23.createCallback(sharedElementCallback23));
    }

    public static void setExitSharedElementCallback(Activity activity, SharedElementCallback23 sharedElementCallback23) {
        activity.setExitSharedElementCallback(ActivityCompatApi23.createCallback(sharedElementCallback23));
    }

    public static boolean shouldShowRequestPermissionRationale(Activity activity, String string2) {
        return activity.shouldShowRequestPermissionRationale(string2);
    }

    public static interface OnSharedElementsReadyListenerBridge {
        public void onSharedElementsReady();
    }

    public static interface RequestPermissionsRequestCodeValidator {
        public void validateRequestPermissionsRequestCode(int var1);
    }

    public static abstract class SharedElementCallback23
    extends ActivityCompatApi21.SharedElementCallback21 {
        public abstract void onSharedElementsArrived(List<String> var1, List<View> var2, OnSharedElementsReadyListenerBridge var3);
    }

    private static class SharedElementCallbackImpl
    extends SharedElementCallback {
        private SharedElementCallback23 mCallback;

        public SharedElementCallbackImpl(SharedElementCallback23 sharedElementCallback23) {
            this.mCallback = sharedElementCallback23;
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

        public void onSharedElementsArrived(List<String> list, List<View> list2, final SharedElementCallback.OnSharedElementsReadyListener onSharedElementsReadyListener) {
            this.mCallback.onSharedElementsArrived(list, list2, new OnSharedElementsReadyListenerBridge(){

                @Override
                public void onSharedElementsReady() {
                    onSharedElementsReadyListener.onSharedElementsReady();
                }
            });
        }

    }

}

