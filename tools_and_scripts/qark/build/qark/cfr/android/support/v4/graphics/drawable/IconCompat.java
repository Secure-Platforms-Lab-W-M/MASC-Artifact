/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.annotation.TargetApi
 *  android.content.Context
 *  android.content.Intent
 *  android.content.Intent$ShortcutIconResource
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.graphics.BitmapShader
 *  android.graphics.Canvas
 *  android.graphics.Matrix
 *  android.graphics.Paint
 *  android.graphics.Shader
 *  android.graphics.Shader$TileMode
 *  android.graphics.drawable.Icon
 *  android.net.Uri
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Parcelable
 */
package android.support.v4.graphics.drawable;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;

public class IconCompat {
    private static final float ADAPTIVE_ICON_INSET_FACTOR = 0.25f;
    private static final int AMBIENT_SHADOW_ALPHA = 30;
    private static final float BLUR_FACTOR = 0.010416667f;
    private static final float DEFAULT_VIEW_PORT_SCALE = 0.6666667f;
    private static final float ICON_DIAMETER_FACTOR = 0.9166667f;
    private static final int KEY_SHADOW_ALPHA = 61;
    private static final float KEY_SHADOW_OFFSET_FACTOR = 0.020833334f;
    private static final int TYPE_ADAPTIVE_BITMAP = 5;
    private static final int TYPE_BITMAP = 1;
    private static final int TYPE_DATA = 3;
    private static final int TYPE_RESOURCE = 2;
    private static final int TYPE_URI = 4;
    private int mInt1;
    private int mInt2;
    private Object mObj1;
    private final int mType;

    private IconCompat(int n) {
        this.mType = n;
    }

    @VisibleForTesting
    static Bitmap createLegacyIconFromAdaptiveIcon(Bitmap bitmap) {
        int n = (int)((float)Math.min(bitmap.getWidth(), bitmap.getHeight()) * 0.6666667f);
        Bitmap bitmap2 = Bitmap.createBitmap((int)n, (int)n, (Bitmap.Config)Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap2);
        Paint paint = new Paint(3);
        float f = (float)n * 0.5f;
        float f2 = 0.9166667f * f;
        float f3 = (float)n * 0.010416667f;
        paint.setColor(0);
        paint.setShadowLayer(f3, 0.0f, (float)n * 0.020833334f, 1023410176);
        canvas.drawCircle(f, f, f2, paint);
        paint.setShadowLayer(f3, 0.0f, 0.0f, 503316480);
        canvas.drawCircle(f, f, f2, paint);
        paint.clearShadowLayer();
        paint.setColor(-16777216);
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Matrix matrix = new Matrix();
        matrix.setTranslate((float)((- bitmap.getWidth() - n) / 2), (float)((- bitmap.getHeight() - n) / 2));
        bitmapShader.setLocalMatrix(matrix);
        paint.setShader((Shader)bitmapShader);
        canvas.drawCircle(f, f, f2, paint);
        canvas.setBitmap(null);
        return bitmap2;
    }

    public static IconCompat createWithAdaptiveBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            IconCompat iconCompat = new IconCompat(5);
            iconCompat.mObj1 = bitmap;
            return iconCompat;
        }
        throw new IllegalArgumentException("Bitmap must not be null.");
    }

    public static IconCompat createWithBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            IconCompat iconCompat = new IconCompat(1);
            iconCompat.mObj1 = bitmap;
            return iconCompat;
        }
        throw new IllegalArgumentException("Bitmap must not be null.");
    }

    public static IconCompat createWithContentUri(Uri uri) {
        if (uri != null) {
            return IconCompat.createWithContentUri(uri.toString());
        }
        throw new IllegalArgumentException("Uri must not be null.");
    }

    public static IconCompat createWithContentUri(String string2) {
        if (string2 != null) {
            IconCompat iconCompat = new IconCompat(4);
            iconCompat.mObj1 = string2;
            return iconCompat;
        }
        throw new IllegalArgumentException("Uri must not be null.");
    }

    public static IconCompat createWithData(byte[] arrby, int n, int n2) {
        if (arrby != null) {
            IconCompat iconCompat = new IconCompat(3);
            iconCompat.mObj1 = arrby;
            iconCompat.mInt1 = n;
            iconCompat.mInt2 = n2;
            return iconCompat;
        }
        throw new IllegalArgumentException("Data must not be null.");
    }

    public static IconCompat createWithResource(Context context, @DrawableRes int n) {
        if (context != null) {
            IconCompat iconCompat = new IconCompat(2);
            iconCompat.mInt1 = n;
            iconCompat.mObj1 = context;
            return iconCompat;
        }
        throw new IllegalArgumentException("Context must not be null.");
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public void addToShortcutIntent(Intent intent) {
        int n = this.mType;
        if (n != 5) {
            switch (n) {
                default: {
                    throw new IllegalArgumentException("Icon type not supported for intent shortcuts");
                }
                case 2: {
                    intent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", (Parcelable)Intent.ShortcutIconResource.fromContext((Context)((Context)this.mObj1), (int)this.mInt1));
                    return;
                }
                case 1: 
            }
            intent.putExtra("android.intent.extra.shortcut.ICON", (Parcelable)((Bitmap)this.mObj1));
            return;
        }
        intent.putExtra("android.intent.extra.shortcut.ICON", (Parcelable)IconCompat.createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1));
    }

    @TargetApi(value=26)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public Icon toIcon() {
        switch (this.mType) {
            default: {
                throw new IllegalArgumentException("Unknown type");
            }
            case 5: {
                if (Build.VERSION.SDK_INT >= 26) {
                    return Icon.createWithAdaptiveBitmap((Bitmap)((Bitmap)this.mObj1));
                }
                return Icon.createWithBitmap((Bitmap)IconCompat.createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1));
            }
            case 4: {
                return Icon.createWithContentUri((String)((String)this.mObj1));
            }
            case 3: {
                return Icon.createWithData((byte[])((byte[])this.mObj1), (int)this.mInt1, (int)this.mInt2);
            }
            case 2: {
                return Icon.createWithResource((Context)((Context)this.mObj1), (int)this.mInt1);
            }
            case 1: 
        }
        return Icon.createWithBitmap((Bitmap)((Bitmap)this.mObj1));
    }
}

