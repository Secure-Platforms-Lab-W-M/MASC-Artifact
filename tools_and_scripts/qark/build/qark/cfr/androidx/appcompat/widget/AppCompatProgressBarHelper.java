/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.graphics.BitmapShader
 *  android.graphics.ColorFilter
 *  android.graphics.Paint
 *  android.graphics.RectF
 *  android.graphics.Shader
 *  android.graphics.Shader$TileMode
 *  android.graphics.drawable.AnimationDrawable
 *  android.graphics.drawable.BitmapDrawable
 *  android.graphics.drawable.ClipDrawable
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.LayerDrawable
 *  android.graphics.drawable.ShapeDrawable
 *  android.graphics.drawable.shapes.RoundRectShape
 *  android.graphics.drawable.shapes.Shape
 *  android.util.AttributeSet
 *  android.widget.ProgressBar
 */
package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.graphics.drawable.WrappedDrawable;

class AppCompatProgressBarHelper {
    private static final int[] TINT_ATTRS = new int[]{16843067, 16843068};
    private Bitmap mSampleTile;
    private final ProgressBar mView;

    AppCompatProgressBarHelper(ProgressBar progressBar) {
        this.mView = progressBar;
    }

    private Shape getDrawableShape() {
        return new RoundRectShape(new float[]{5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f}, null, null);
    }

    private Drawable tileify(Drawable drawable2, boolean bl) {
        if (drawable2 instanceof WrappedDrawable) {
            Drawable drawable3 = ((WrappedDrawable)drawable2).getWrappedDrawable();
            if (drawable3 != null) {
                drawable3 = this.tileify(drawable3, bl);
                ((WrappedDrawable)drawable2).setWrappedDrawable(drawable3);
            }
            return drawable2;
        }
        if (drawable2 instanceof LayerDrawable) {
            int n;
            drawable2 = (LayerDrawable)drawable2;
            int n2 = drawable2.getNumberOfLayers();
            LayerDrawable layerDrawable = new Drawable[n2];
            for (n = 0; n < n2; ++n) {
                int n3 = drawable2.getId(n);
                Drawable drawable4 = drawable2.getDrawable(n);
                bl = n3 == 16908301 || n3 == 16908303;
                layerDrawable[n] = this.tileify(drawable4, bl);
            }
            layerDrawable = new LayerDrawable((Drawable[])layerDrawable);
            for (n = 0; n < n2; ++n) {
                layerDrawable.setId(n, drawable2.getId(n));
            }
            return layerDrawable;
        }
        if (drawable2 instanceof BitmapDrawable) {
            drawable2 = (BitmapDrawable)drawable2;
            Bitmap bitmap = drawable2.getBitmap();
            if (this.mSampleTile == null) {
                this.mSampleTile = bitmap;
            }
            ShapeDrawable shapeDrawable = new ShapeDrawable(this.getDrawableShape());
            bitmap = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
            shapeDrawable.getPaint().setShader((Shader)bitmap);
            shapeDrawable.getPaint().setColorFilter(drawable2.getPaint().getColorFilter());
            if (bl) {
                return new ClipDrawable((Drawable)shapeDrawable, 3, 1);
            }
            return shapeDrawable;
        }
        return drawable2;
    }

    private Drawable tileifyIndeterminate(Drawable drawable2) {
        Drawable drawable3 = drawable2;
        if (drawable2 instanceof AnimationDrawable) {
            drawable2 = (AnimationDrawable)drawable2;
            int n = drawable2.getNumberOfFrames();
            drawable3 = new AnimationDrawable();
            drawable3.setOneShot(drawable2.isOneShot());
            for (int i = 0; i < n; ++i) {
                Drawable drawable4 = this.tileify(drawable2.getFrame(i), true);
                drawable4.setLevel(10000);
                drawable3.addFrame(drawable4, drawable2.getDuration(i));
            }
            drawable3.setLevel(10000);
        }
        return drawable3;
    }

    Bitmap getSampleTile() {
        return this.mSampleTile;
    }

    void loadFromAttributes(AttributeSet object, int n) {
        object = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), (AttributeSet)object, TINT_ATTRS, n, 0);
        Drawable drawable2 = object.getDrawableIfKnown(0);
        if (drawable2 != null) {
            this.mView.setIndeterminateDrawable(this.tileifyIndeterminate(drawable2));
        }
        if ((drawable2 = object.getDrawableIfKnown(1)) != null) {
            this.mView.setProgressDrawable(this.tileify(drawable2, false));
        }
        object.recycle();
    }
}

