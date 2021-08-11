/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.Fragment
 *  android.content.pm.PackageManager
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Handler
 *  android.os.Looper
 */
package android.support.v13.app;

import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import java.util.Arrays;

public class FragmentCompat {
    static final FragmentCompatImpl IMPL = Build.VERSION.SDK_INT >= 24 ? new FragmentCompatApi24Impl() : (Build.VERSION.SDK_INT >= 23 ? new FragmentCompatApi23Impl() : (Build.VERSION.SDK_INT >= 15 ? new FragmentCompatApi15Impl() : new FragmentCompatBaseImpl()));

    public static void requestPermissions(@NonNull Fragment fragment, @NonNull String[] arrstring, int n) {
        IMPL.requestPermissions(fragment, arrstring, n);
    }

    @Deprecated
    public static void setMenuVisibility(Fragment fragment, boolean bl) {
        fragment.setMenuVisibility(bl);
    }

    public static void setUserVisibleHint(Fragment fragment, boolean bl) {
        IMPL.setUserVisibleHint(fragment, bl);
    }

    public static boolean shouldShowRequestPermissionRationale(@NonNull Fragment fragment, @NonNull String string) {
        return IMPL.shouldShowRequestPermissionRationale(fragment, string);
    }

    @RequiresApi(value=15)
    static class FragmentCompatApi15Impl
    extends FragmentCompatBaseImpl {
        FragmentCompatApi15Impl() {
        }

        @Override
        public void setUserVisibleHint(Fragment fragment, boolean bl) {
            fragment.setUserVisibleHint(bl);
        }
    }

    @RequiresApi(value=23)
    static class FragmentCompatApi23Impl
    extends FragmentCompatApi15Impl {
        FragmentCompatApi23Impl() {
        }

        @Override
        public void requestPermissions(Fragment fragment, String[] arrstring, int n) {
            fragment.requestPermissions(arrstring, n);
        }

        @Override
        public boolean shouldShowRequestPermissionRationale(Fragment fragment, String string) {
            return fragment.shouldShowRequestPermissionRationale(string);
        }
    }

    @RequiresApi(value=24)
    static class FragmentCompatApi24Impl
    extends FragmentCompatApi23Impl {
        FragmentCompatApi24Impl() {
        }

        @Override
        public void setUserVisibleHint(Fragment fragment, boolean bl) {
            fragment.setUserVisibleHint(bl);
        }
    }

    static class FragmentCompatBaseImpl
    implements FragmentCompatImpl {
        FragmentCompatBaseImpl() {
        }

        @Override
        public void requestPermissions(final Fragment fragment, final String[] arrstring, final int n) {
            new Handler(Looper.getMainLooper()).post(new Runnable(){

                @Override
                public void run() {
                    int[] arrn = new int[arrstring.length];
                    Object object = fragment.getActivity();
                    if (object != null) {
                        PackageManager packageManager = object.getPackageManager();
                        object = object.getPackageName();
                        int n2 = arrstring.length;
                        for (int i = 0; i < n2; ++i) {
                            arrn[i] = packageManager.checkPermission(arrstring[i], (String)object);
                        }
                    } else {
                        Arrays.fill(arrn, -1);
                    }
                    ((OnRequestPermissionsResultCallback)fragment).onRequestPermissionsResult(n, arrstring, arrn);
                }
            });
        }

        @Override
        public void setUserVisibleHint(Fragment fragment, boolean bl) {
        }

        @Override
        public boolean shouldShowRequestPermissionRationale(Fragment fragment, String string) {
            return false;
        }

    }

    static interface FragmentCompatImpl {
        public void requestPermissions(Fragment var1, String[] var2, int var3);

        public void setUserVisibleHint(Fragment var1, boolean var2);

        public boolean shouldShowRequestPermissionRationale(Fragment var1, String var2);
    }

    public static interface OnRequestPermissionsResultCallback {
        public void onRequestPermissionsResult(int var1, @NonNull String[] var2, @NonNull int[] var3);
    }

}

