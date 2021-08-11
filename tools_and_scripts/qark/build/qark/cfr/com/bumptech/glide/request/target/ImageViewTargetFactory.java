/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.drawable.Drawable
 *  android.widget.ImageView
 */
package com.bumptech.glide.request.target;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.ViewTarget;

public class ImageViewTargetFactory {
    public <Z> ViewTarget<ImageView, Z> buildTarget(ImageView object, Class<Z> class_) {
        if (Bitmap.class.equals(class_)) {
            return new BitmapImageViewTarget((ImageView)object);
        }
        if (Drawable.class.isAssignableFrom(class_)) {
            return new DrawableImageViewTarget((ImageView)object);
        }
        object = new StringBuilder();
        object.append("Unhandled class: ");
        object.append(class_);
        object.append(", try .as*(Class).transcode(ResourceTranscoder)");
        throw new IllegalArgumentException(object.toString());
    }
}

