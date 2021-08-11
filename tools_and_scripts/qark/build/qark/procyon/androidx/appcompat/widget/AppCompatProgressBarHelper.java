// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.appcompat.widget;

import android.util.AttributeSet;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.Shader;
import android.graphics.BitmapShader;
import android.graphics.Shader$TileMode;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LayerDrawable;
import androidx.core.graphics.drawable.WrappedDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.RectF;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.widget.ProgressBar;
import android.graphics.Bitmap;

class AppCompatProgressBarHelper
{
    private static final int[] TINT_ATTRS;
    private Bitmap mSampleTile;
    private final ProgressBar mView;
    
    static {
        TINT_ATTRS = new int[] { 16843067, 16843068 };
    }
    
    AppCompatProgressBarHelper(final ProgressBar mView) {
        this.mView = mView;
    }
    
    private Shape getDrawableShape() {
        return (Shape)new RoundRectShape(new float[] { 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f }, (RectF)null, (float[])null);
    }
    
    private Drawable tileify(final Drawable drawable, final boolean b) {
        if (drawable instanceof WrappedDrawable) {
            final Drawable wrappedDrawable = ((WrappedDrawable)drawable).getWrappedDrawable();
            if (wrappedDrawable != null) {
                ((WrappedDrawable)drawable).setWrappedDrawable(this.tileify(wrappedDrawable, b));
            }
            return drawable;
        }
        if (drawable instanceof LayerDrawable) {
            final LayerDrawable layerDrawable = (LayerDrawable)drawable;
            final int numberOfLayers = layerDrawable.getNumberOfLayers();
            final Drawable[] array = new Drawable[numberOfLayers];
            for (int i = 0; i < numberOfLayers; ++i) {
                final int id = layerDrawable.getId(i);
                array[i] = this.tileify(layerDrawable.getDrawable(i), id == 16908301 || id == 16908303);
            }
            final LayerDrawable layerDrawable2 = new LayerDrawable(array);
            for (int j = 0; j < numberOfLayers; ++j) {
                layerDrawable2.setId(j, layerDrawable.getId(j));
            }
            return (Drawable)layerDrawable2;
        }
        if (!(drawable instanceof BitmapDrawable)) {
            return drawable;
        }
        final BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
        final Bitmap bitmap = bitmapDrawable.getBitmap();
        if (this.mSampleTile == null) {
            this.mSampleTile = bitmap;
        }
        final ShapeDrawable shapeDrawable = new ShapeDrawable(this.getDrawableShape());
        shapeDrawable.getPaint().setShader((Shader)new BitmapShader(bitmap, Shader$TileMode.REPEAT, Shader$TileMode.CLAMP));
        shapeDrawable.getPaint().setColorFilter(bitmapDrawable.getPaint().getColorFilter());
        if (b) {
            return (Drawable)new ClipDrawable((Drawable)shapeDrawable, 3, 1);
        }
        return (Drawable)shapeDrawable;
    }
    
    private Drawable tileifyIndeterminate(final Drawable drawable) {
        Object o = drawable;
        if (drawable instanceof AnimationDrawable) {
            final AnimationDrawable animationDrawable = (AnimationDrawable)drawable;
            final int numberOfFrames = animationDrawable.getNumberOfFrames();
            o = new AnimationDrawable();
            ((AnimationDrawable)o).setOneShot(animationDrawable.isOneShot());
            for (int i = 0; i < numberOfFrames; ++i) {
                final Drawable tileify = this.tileify(animationDrawable.getFrame(i), true);
                tileify.setLevel(10000);
                ((AnimationDrawable)o).addFrame(tileify, animationDrawable.getDuration(i));
            }
            ((AnimationDrawable)o).setLevel(10000);
        }
        return (Drawable)o;
    }
    
    Bitmap getSampleTile() {
        return this.mSampleTile;
    }
    
    void loadFromAttributes(final AttributeSet set, final int n) {
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), set, AppCompatProgressBarHelper.TINT_ATTRS, n, 0);
        final Drawable drawableIfKnown = obtainStyledAttributes.getDrawableIfKnown(0);
        if (drawableIfKnown != null) {
            this.mView.setIndeterminateDrawable(this.tileifyIndeterminate(drawableIfKnown));
        }
        final Drawable drawableIfKnown2 = obtainStyledAttributes.getDrawableIfKnown(1);
        if (drawableIfKnown2 != null) {
            this.mView.setProgressDrawable(this.tileify(drawableIfKnown2, false));
        }
        obtainStyledAttributes.recycle();
    }
}
