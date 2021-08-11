/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 *  android.net.Uri
 *  android.widget.ImageView
 */
package androidx.databinding.adapters;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

public class ImageViewBindingAdapter {
    public static void setImageDrawable(ImageView imageView, Drawable drawable2) {
        imageView.setImageDrawable(drawable2);
    }

    public static void setImageUri(ImageView imageView, Uri uri) {
        imageView.setImageURI(uri);
    }

    public static void setImageUri(ImageView imageView, String string2) {
        if (string2 == null) {
            imageView.setImageURI(null);
            return;
        }
        imageView.setImageURI(Uri.parse((String)string2));
    }
}

