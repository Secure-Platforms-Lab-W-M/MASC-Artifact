// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v13.app;

import android.content.Context;
import android.content.pm.PackageManager;
import android.app.Activity;
import java.util.Arrays;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.annotation.NonNull;
import android.app.Fragment;
import android.os.Build$VERSION;

public class FragmentCompat
{
    static final FragmentCompatImpl IMPL;
    
    static {
        if (Build$VERSION.SDK_INT >= 24) {
            IMPL = (FragmentCompatImpl)new FragmentCompatApi24Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 23) {
            IMPL = (FragmentCompatImpl)new FragmentCompatApi23Impl();
            return;
        }
        if (Build$VERSION.SDK_INT >= 15) {
            IMPL = (FragmentCompatImpl)new FragmentCompatApi15Impl();
            return;
        }
        IMPL = (FragmentCompatImpl)new FragmentCompatBaseImpl();
    }
    
    public static void requestPermissions(@NonNull final Fragment fragment, @NonNull final String[] array, final int n) {
        FragmentCompat.IMPL.requestPermissions(fragment, array, n);
    }
    
    @Deprecated
    public static void setMenuVisibility(final Fragment fragment, final boolean menuVisibility) {
        fragment.setMenuVisibility(menuVisibility);
    }
    
    public static void setUserVisibleHint(final Fragment fragment, final boolean b) {
        FragmentCompat.IMPL.setUserVisibleHint(fragment, b);
    }
    
    public static boolean shouldShowRequestPermissionRationale(@NonNull final Fragment fragment, @NonNull final String s) {
        return FragmentCompat.IMPL.shouldShowRequestPermissionRationale(fragment, s);
    }
    
    @RequiresApi(15)
    static class FragmentCompatApi15Impl extends FragmentCompatBaseImpl
    {
        @Override
        public void setUserVisibleHint(final Fragment fragment, final boolean userVisibleHint) {
            fragment.setUserVisibleHint(userVisibleHint);
        }
    }
    
    @RequiresApi(23)
    static class FragmentCompatApi23Impl extends FragmentCompatApi15Impl
    {
        @Override
        public void requestPermissions(final Fragment fragment, final String[] array, final int n) {
            fragment.requestPermissions(array, n);
        }
        
        @Override
        public boolean shouldShowRequestPermissionRationale(final Fragment fragment, final String s) {
            return fragment.shouldShowRequestPermissionRationale(s);
        }
    }
    
    @RequiresApi(24)
    static class FragmentCompatApi24Impl extends FragmentCompatApi23Impl
    {
        @Override
        public void setUserVisibleHint(final Fragment fragment, final boolean userVisibleHint) {
            fragment.setUserVisibleHint(userVisibleHint);
        }
    }
    
    static class FragmentCompatBaseImpl implements FragmentCompatImpl
    {
        @Override
        public void requestPermissions(final Fragment fragment, final String[] array, final int n) {
            new Handler(Looper.getMainLooper()).post((Runnable)new Runnable() {
                @Override
                public void run() {
                    final int[] array = new int[array.length];
                    final Activity activity = fragment.getActivity();
                    if (activity != null) {
                        final PackageManager packageManager = ((Context)activity).getPackageManager();
                        final String packageName = ((Context)activity).getPackageName();
                        for (int length = array.length, i = 0; i < length; ++i) {
                            array[i] = packageManager.checkPermission(array[i], packageName);
                        }
                    }
                    else {
                        Arrays.fill(array, -1);
                    }
                    ((OnRequestPermissionsResultCallback)fragment).onRequestPermissionsResult(n, array, array);
                }
            });
        }
        
        @Override
        public void setUserVisibleHint(final Fragment fragment, final boolean b) {
        }
        
        @Override
        public boolean shouldShowRequestPermissionRationale(final Fragment fragment, final String s) {
            return false;
        }
    }
    
    interface FragmentCompatImpl
    {
        void requestPermissions(final Fragment p0, final String[] p1, final int p2);
        
        void setUserVisibleHint(final Fragment p0, final boolean p1);
        
        boolean shouldShowRequestPermissionRationale(final Fragment p0, final String p1);
    }
    
    public interface OnRequestPermissionsResultCallback
    {
        void onRequestPermissionsResult(final int p0, @NonNull final String[] p1, @NonNull final int[] p2);
    }
}
