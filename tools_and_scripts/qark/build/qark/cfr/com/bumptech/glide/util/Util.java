/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Looper
 */
package com.bumptech.glide.util;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Looper;
import com.bumptech.glide.load.model.Model;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

public final class Util {
    private static final int HASH_ACCUMULATOR = 17;
    private static final int HASH_MULTIPLIER = 31;
    private static final char[] HEX_CHAR_ARRAY = "0123456789abcdef".toCharArray();
    private static final char[] SHA_256_CHARS = new char[64];

    private Util() {
    }

    public static void assertBackgroundThread() {
        if (Util.isOnBackgroundThread()) {
            return;
        }
        throw new IllegalArgumentException("You must call this method on a background thread");
    }

    public static void assertMainThread() {
        if (Util.isOnMainThread()) {
            return;
        }
        throw new IllegalArgumentException("You must call this method on the main thread");
    }

    public static boolean bothModelsNullEquivalentOrEquals(Object object, Object object2) {
        if (object == null) {
            if (object2 == null) {
                return true;
            }
            return false;
        }
        if (object instanceof Model) {
            return ((Model)object).isEquivalentTo(object2);
        }
        return object.equals(object2);
    }

    public static boolean bothNullOrEqual(Object object, Object object2) {
        if (object == null) {
            if (object2 == null) {
                return true;
            }
            return false;
        }
        return object.equals(object2);
    }

    private static String bytesToHex(byte[] arrby, char[] arrc) {
        for (int i = 0; i < arrby.length; ++i) {
            int n = arrby[i] & 255;
            char[] arrc2 = HEX_CHAR_ARRAY;
            arrc[i * 2] = arrc2[n >>> 4];
            arrc[i * 2 + 1] = arrc2[n & 15];
        }
        return new String(arrc);
    }

    public static <T> Queue<T> createQueue(int n) {
        return new ArrayDeque(n);
    }

    public static int getBitmapByteSize(int n, int n2, Bitmap.Config config) {
        return n * n2 * Util.getBytesPerPixel(config);
    }

    public static int getBitmapByteSize(Bitmap bitmap) {
        if (!bitmap.isRecycled()) {
            if (Build.VERSION.SDK_INT >= 19) {
                try {
                    int n = bitmap.getAllocationByteCount();
                    return n;
                }
                catch (NullPointerException nullPointerException) {
                    // empty catch block
                }
            }
            return bitmap.getHeight() * bitmap.getRowBytes();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot obtain size for recycled Bitmap: ");
        stringBuilder.append((Object)bitmap);
        stringBuilder.append("[");
        stringBuilder.append(bitmap.getWidth());
        stringBuilder.append("x");
        stringBuilder.append(bitmap.getHeight());
        stringBuilder.append("] ");
        stringBuilder.append((Object)bitmap.getConfig());
        throw new IllegalStateException(stringBuilder.toString());
    }

    private static int getBytesPerPixel(Bitmap.Config config) {
        int n;
        Bitmap.Config config2 = config;
        if (config == null) {
            config2 = Bitmap.Config.ARGB_8888;
        }
        if ((n = .$SwitchMap$android$graphics$Bitmap$Config[config2.ordinal()]) != 1) {
            if (n != 2 && n != 3) {
                if (n != 4) {
                    return 4;
                }
                return 8;
            }
            return 2;
        }
        return 1;
    }

    @Deprecated
    public static int getSize(Bitmap bitmap) {
        return Util.getBitmapByteSize(bitmap);
    }

    public static <T> List<T> getSnapshot(Collection<T> object) {
        ArrayList arrayList = new ArrayList(object.size());
        object = object.iterator();
        while (object.hasNext()) {
            Object e = object.next();
            if (e == null) continue;
            arrayList.add(e);
        }
        return arrayList;
    }

    public static int hashCode(float f) {
        return Util.hashCode(f, 17);
    }

    public static int hashCode(float f, int n) {
        return Util.hashCode(Float.floatToIntBits(f), n);
    }

    public static int hashCode(int n) {
        return Util.hashCode(n, 17);
    }

    public static int hashCode(int n, int n2) {
        return n2 * 31 + n;
    }

    public static int hashCode(Object object, int n) {
        int n2 = object == null ? 0 : object.hashCode();
        return Util.hashCode(n2, n);
    }

    public static int hashCode(boolean bl) {
        return Util.hashCode(bl, 17);
    }

    public static int hashCode(boolean bl, int n) {
        RuntimeException runtimeException;
        super("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.copyTypes(TypeTransformer.java:311)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:226)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
        throw runtimeException;
    }

    public static boolean isOnBackgroundThread() {
        return Util.isOnMainThread() ^ true;
    }

    public static boolean isOnMainThread() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            return true;
        }
        return false;
    }

    private static boolean isValidDimension(int n) {
        if (n <= 0 && n != Integer.MIN_VALUE) {
            return false;
        }
        return true;
    }

    public static boolean isValidDimensions(int n, int n2) {
        if (Util.isValidDimension(n) && Util.isValidDimension(n2)) {
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static String sha256BytesToHex(byte[] object) {
        char[] arrc = SHA_256_CHARS;
        synchronized (arrc) {
            return Util.bytesToHex((byte[])object, SHA_256_CHARS);
        }
    }

}

