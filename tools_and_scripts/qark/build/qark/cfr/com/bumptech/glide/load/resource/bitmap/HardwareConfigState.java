/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.graphics.BitmapFactory
 *  android.graphics.BitmapFactory$Options
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.Log
 */
package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import java.io.File;

public final class HardwareConfigState {
    private static final File FD_SIZE_LIST = new File("/proc/self/fd");
    private static final int MAXIMUM_FDS_FOR_HARDWARE_CONFIGS_O = 700;
    private static final int MAXIMUM_FDS_FOR_HARDWARE_CONFIGS_P = 20000;
    private static final int MINIMUM_DECODES_BETWEEN_FD_CHECKS = 50;
    static final int MIN_HARDWARE_DIMENSION_O = 128;
    private static final int MIN_HARDWARE_DIMENSION_P = 0;
    private static volatile HardwareConfigState instance;
    private int decodesSinceLastFdCheck;
    private final int fdCountLimit;
    private boolean isFdSizeBelowHardwareLimit = true;
    private final boolean isHardwareConfigAllowedByDeviceModel = HardwareConfigState.isHardwareConfigAllowedByDeviceModel();
    private final int minHardwareDimension;

    HardwareConfigState() {
        if (Build.VERSION.SDK_INT >= 28) {
            this.fdCountLimit = 20000;
            this.minHardwareDimension = 0;
            return;
        }
        this.fdCountLimit = 700;
        this.minHardwareDimension = 128;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static HardwareConfigState getInstance() {
        if (instance == null) {
            synchronized (HardwareConfigState.class) {
                if (instance == null) {
                    instance = new HardwareConfigState();
                }
            }
        }
        return instance;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private boolean isFdSizeBelowHardwareLimit() {
        synchronized (this) {
            int n = this.decodesSinceLastFdCheck;
            boolean bl = true;
            this.decodesSinceLastFdCheck = ++n;
            if (n < 50) return this.isFdSizeBelowHardwareLimit;
            this.decodesSinceLastFdCheck = 0;
            n = FD_SIZE_LIST.list().length;
            if (n >= this.fdCountLimit) {
                bl = false;
            }
            this.isFdSizeBelowHardwareLimit = bl;
            if (bl) return this.isFdSizeBelowHardwareLimit;
            if (!Log.isLoggable((String)"Downsampler", (int)5)) return this.isFdSizeBelowHardwareLimit;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Excluding HARDWARE bitmap config because we're over the file descriptor limit, file descriptors ");
            stringBuilder.append(n);
            stringBuilder.append(", limit ");
            stringBuilder.append(this.fdCountLimit);
            Log.w((String)"Downsampler", (String)stringBuilder.toString());
            return this.isFdSizeBelowHardwareLimit;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private static boolean isHardwareConfigAllowedByDeviceModel() {
        block13 : {
            if (Build.MODEL == null) return true;
            if (Build.MODEL.length() < 7) {
                return true;
            }
            var1 = Build.MODEL.substring(0, 7);
            var0_1 = -1;
            switch (var1.hashCode()) {
                case -1398222624: {
                    if (!var1.equals("SM-N935")) break;
                    var0_1 = 0;
                    ** break;
                }
                case -1398343746: {
                    if (!var1.equals("SM-J720")) break;
                    var0_1 = 1;
                    ** break;
                }
                case -1398431068: {
                    if (!var1.equals("SM-G965")) break;
                    var0_1 = 3;
                    ** break;
                }
                case -1398431073: {
                    if (!var1.equals("SM-G960")) break;
                    var0_1 = 2;
                    ** break;
                }
                case -1398431161: {
                    if (!var1.equals("SM-G935")) break;
                    var0_1 = 4;
                    ** break;
                }
                case -1398431166: {
                    if (!var1.equals("SM-G930")) break;
                    var0_1 = 5;
                    ** break;
                }
                case -1398613787: {
                    if (!var1.equals("SM-A520")) break;
                    var0_1 = 6;
                    break block13;
                }
            }
            ** break;
        }
        switch (var0_1) {
            default: {
                return true;
            }
            case 0: 
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
        }
        if (Build.VERSION.SDK_INT == 26) return false;
        return true;
    }

    public boolean isHardwareConfigAllowed(int n, int n2, boolean bl, boolean bl2) {
        if (bl && this.isHardwareConfigAllowedByDeviceModel && Build.VERSION.SDK_INT >= 26) {
            if (bl2) {
                return false;
            }
            int n3 = this.minHardwareDimension;
            if (n >= n3 && n2 >= n3 && this.isFdSizeBelowHardwareLimit()) {
                return true;
            }
            return false;
        }
        return false;
    }

    boolean setHardwareConfigIfAllowed(int n, int n2, BitmapFactory.Options options, boolean bl, boolean bl2) {
        if (bl = this.isHardwareConfigAllowed(n, n2, bl, bl2)) {
            options.inPreferredConfig = Bitmap.Config.HARDWARE;
            options.inMutable = false;
        }
        return bl;
    }
}

