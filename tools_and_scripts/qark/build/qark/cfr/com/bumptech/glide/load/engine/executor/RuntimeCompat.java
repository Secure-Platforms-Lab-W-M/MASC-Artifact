/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.StrictMode
 *  android.os.StrictMode$ThreadPolicy
 *  android.util.Log
 */
package com.bumptech.glide.load.engine.executor;

import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class RuntimeCompat {
    private static final String CPU_LOCATION = "/sys/devices/system/cpu/";
    private static final String CPU_NAME_REGEX = "cpu[0-9]+";
    private static final String TAG = "GlideRuntimeCompat";

    private RuntimeCompat() {
    }

    static int availableProcessors() {
        int n;
        int n2 = n = Runtime.getRuntime().availableProcessors();
        if (Build.VERSION.SDK_INT < 17) {
            n2 = Math.max(RuntimeCompat.getCoreCountPre17(), n);
        }
        return n2;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private static int getCoreCountPre17() {
        arrfile2 = null;
        threadPolicy = StrictMode.allowThreadDiskReads();
        try {
            arrfile = new File("/sys/devices/system/cpu/").listFiles(new FilenameFilter(){

                @Override
                public boolean accept(File file, String string2) {
                    return Pattern.this.matcher(string2).matches();
                }
            });
            ** GOTO lbl12
        }
        catch (Throwable throwable) {
            block6 : {
                arrfile = arrfile2;
                if (!Log.isLoggable((String)"GlideRuntimeCompat", (int)6)) break block6;
                Log.e((String)"GlideRuntimeCompat", (String)"Failed to calculate accurate cpu count", (Throwable)throwable);
                arrfile = arrfile2;
            }
            if (arrfile != null) {
                n = arrfile.length;
                return Math.max(1, n);
            }
            n = 0;
            return Math.max(1, n);
            finally {
                StrictMode.setThreadPolicy((StrictMode.ThreadPolicy)threadPolicy);
            }
        }
    }

}

