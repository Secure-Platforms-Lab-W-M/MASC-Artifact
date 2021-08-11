/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.graphics.Canvas
 *  android.graphics.ColorFilter
 *  android.graphics.Outline
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$Callback
 *  android.graphics.drawable.Drawable$ConstantState
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.SystemClock
 *  android.util.DisplayMetrics
 *  android.util.SparseArray
 */
package androidx.appcompat.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import androidx.core.graphics.drawable.DrawableCompat;

class DrawableContainer
extends Drawable
implements Drawable.Callback {
    private static final boolean DEBUG = false;
    private static final boolean DEFAULT_DITHER = true;
    private static final String TAG = "DrawableContainer";
    private int mAlpha = 255;
    private Runnable mAnimationRunnable;
    private BlockInvalidateCallback mBlockInvalidateCallback;
    private int mCurIndex = -1;
    private Drawable mCurrDrawable;
    private DrawableContainerState mDrawableContainerState;
    private long mEnterAnimationEnd;
    private long mExitAnimationEnd;
    private boolean mHasAlpha;
    private Rect mHotspotBounds;
    private Drawable mLastDrawable;
    private int mLastIndex = -1;
    private boolean mMutated;

    DrawableContainer() {
    }

    private void initializeDrawableForDisplay(Drawable drawable) {
        block12 : {
            if (this.mBlockInvalidateCallback == null) {
                this.mBlockInvalidateCallback = new BlockInvalidateCallback();
            }
            drawable.setCallback((Drawable.Callback)this.mBlockInvalidateCallback.wrap(drawable.getCallback()));
            if (this.mDrawableContainerState.mEnterFadeDuration <= 0 && this.mHasAlpha) {
                drawable.setAlpha(this.mAlpha);
            }
            if (this.mDrawableContainerState.mHasColorFilter) {
                drawable.setColorFilter(this.mDrawableContainerState.mColorFilter);
            } else {
                if (this.mDrawableContainerState.mHasTintList) {
                    DrawableCompat.setTintList(drawable, this.mDrawableContainerState.mTintList);
                }
                if (this.mDrawableContainerState.mHasTintMode) {
                    DrawableCompat.setTintMode(drawable, this.mDrawableContainerState.mTintMode);
                }
            }
            drawable.setVisible(this.isVisible(), true);
            drawable.setDither(this.mDrawableContainerState.mDither);
            drawable.setState(this.getState());
            drawable.setLevel(this.getLevel());
            drawable.setBounds(this.getBounds());
            if (Build.VERSION.SDK_INT >= 23) {
                drawable.setLayoutDirection(this.getLayoutDirection());
            }
            if (Build.VERSION.SDK_INT >= 19) {
                drawable.setAutoMirrored(this.mDrawableContainerState.mAutoMirrored);
            }
            Rect rect = this.mHotspotBounds;
            if (Build.VERSION.SDK_INT < 21 || rect == null) break block12;
            drawable.setHotspotBounds(rect.left, rect.top, rect.right, rect.bottom);
        }
        return;
        finally {
            drawable.setCallback(this.mBlockInvalidateCallback.unwrap());
        }
    }

    private boolean needsMirroring() {
        if (this.isAutoMirrored() && DrawableCompat.getLayoutDirection(this) == 1) {
            return true;
        }
        return false;
    }

    static int resolveDensity(Resources resources, int n) {
        if (resources != null) {
            n = resources.getDisplayMetrics().densityDpi;
        }
        if (n == 0) {
            return 160;
        }
        return n;
    }

    void animate(boolean bl) {
        long l;
        int n;
        this.mHasAlpha = true;
        long l2 = SystemClock.uptimeMillis();
        int n2 = 0;
        Drawable drawable = this.mCurrDrawable;
        if (drawable != null) {
            l = this.mEnterAnimationEnd;
            n = n2;
            if (l != 0L) {
                if (l <= l2) {
                    drawable.setAlpha(this.mAlpha);
                    this.mEnterAnimationEnd = 0L;
                    n = n2;
                } else {
                    n = (int)((l - l2) * 255L) / this.mDrawableContainerState.mEnterFadeDuration;
                    this.mCurrDrawable.setAlpha((255 - n) * this.mAlpha / 255);
                    n = 1;
                }
            }
        } else {
            this.mEnterAnimationEnd = 0L;
            n = n2;
        }
        if ((drawable = this.mLastDrawable) != null) {
            l = this.mExitAnimationEnd;
            n2 = n;
            if (l != 0L) {
                if (l <= l2) {
                    drawable.setVisible(false, false);
                    this.mLastDrawable = null;
                    this.mLastIndex = -1;
                    this.mExitAnimationEnd = 0L;
                    n2 = n;
                } else {
                    n = (int)((l - l2) * 255L) / this.mDrawableContainerState.mExitFadeDuration;
                    this.mLastDrawable.setAlpha(this.mAlpha * n / 255);
                    n2 = 1;
                }
            }
        } else {
            this.mExitAnimationEnd = 0L;
            n2 = n;
        }
        if (bl && n2 != 0) {
            this.scheduleSelf(this.mAnimationRunnable, 16L + l2);
        }
    }

    public void applyTheme(Resources.Theme theme) {
        this.mDrawableContainerState.applyTheme(theme);
    }

    public boolean canApplyTheme() {
        return this.mDrawableContainerState.canApplyTheme();
    }

    void clearMutated() {
        this.mDrawableContainerState.clearMutated();
        this.mMutated = false;
    }

    DrawableContainerState cloneConstantState() {
        return this.mDrawableContainerState;
    }

    public void draw(Canvas canvas) {
        Drawable drawable = this.mCurrDrawable;
        if (drawable != null) {
            drawable.draw(canvas);
        }
        if ((drawable = this.mLastDrawable) != null) {
            drawable.draw(canvas);
        }
    }

    public int getAlpha() {
        return this.mAlpha;
    }

    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | this.mDrawableContainerState.getChangingConfigurations();
    }

    public final Drawable.ConstantState getConstantState() {
        if (this.mDrawableContainerState.canConstantState()) {
            this.mDrawableContainerState.mChangingConfigurations = this.getChangingConfigurations();
            return this.mDrawableContainerState;
        }
        return null;
    }

    public Drawable getCurrent() {
        return this.mCurrDrawable;
    }

    int getCurrentIndex() {
        return this.mCurIndex;
    }

    public void getHotspotBounds(Rect rect) {
        Rect rect2 = this.mHotspotBounds;
        if (rect2 != null) {
            rect.set(rect2);
            return;
        }
        super.getHotspotBounds(rect);
    }

    public int getIntrinsicHeight() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantHeight();
        }
        Drawable drawable = this.mCurrDrawable;
        if (drawable != null) {
            return drawable.getIntrinsicHeight();
        }
        return -1;
    }

    public int getIntrinsicWidth() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantWidth();
        }
        Drawable drawable = this.mCurrDrawable;
        if (drawable != null) {
            return drawable.getIntrinsicWidth();
        }
        return -1;
    }

    public int getMinimumHeight() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantMinimumHeight();
        }
        Drawable drawable = this.mCurrDrawable;
        if (drawable != null) {
            return drawable.getMinimumHeight();
        }
        return 0;
    }

    public int getMinimumWidth() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantMinimumWidth();
        }
        Drawable drawable = this.mCurrDrawable;
        if (drawable != null) {
            return drawable.getMinimumWidth();
        }
        return 0;
    }

    public int getOpacity() {
        Drawable drawable = this.mCurrDrawable;
        if (drawable != null && drawable.isVisible()) {
            return this.mDrawableContainerState.getOpacity();
        }
        return -2;
    }

    public void getOutline(Outline outline) {
        Drawable drawable = this.mCurrDrawable;
        if (drawable != null) {
            drawable.getOutline(outline);
        }
    }

    public boolean getPadding(Rect rect) {
        boolean bl;
        Rect rect2 = this.mDrawableContainerState.getConstantPadding();
        if (rect2 != null) {
            rect.set(rect2);
            bl = (rect2.left | rect2.top | rect2.bottom | rect2.right) != 0;
        } else {
            rect2 = this.mCurrDrawable;
            bl = rect2 != null ? rect2.getPadding(rect) : super.getPadding(rect);
        }
        if (this.needsMirroring()) {
            int n = rect.left;
            rect.left = rect.right;
            rect.right = n;
        }
        return bl;
    }

    public void invalidateDrawable(Drawable drawable) {
        DrawableContainerState drawableContainerState = this.mDrawableContainerState;
        if (drawableContainerState != null) {
            drawableContainerState.invalidateCache();
        }
        if (drawable == this.mCurrDrawable && this.getCallback() != null) {
            this.getCallback().invalidateDrawable((Drawable)this);
        }
    }

    public boolean isAutoMirrored() {
        return this.mDrawableContainerState.mAutoMirrored;
    }

    public boolean isStateful() {
        return this.mDrawableContainerState.isStateful();
    }

    public void jumpToCurrentState() {
        boolean bl = false;
        Drawable drawable = this.mLastDrawable;
        if (drawable != null) {
            drawable.jumpToCurrentState();
            this.mLastDrawable = null;
            this.mLastIndex = -1;
            bl = true;
        }
        if ((drawable = this.mCurrDrawable) != null) {
            drawable.jumpToCurrentState();
            if (this.mHasAlpha) {
                this.mCurrDrawable.setAlpha(this.mAlpha);
            }
        }
        if (this.mExitAnimationEnd != 0L) {
            this.mExitAnimationEnd = 0L;
            bl = true;
        }
        if (this.mEnterAnimationEnd != 0L) {
            this.mEnterAnimationEnd = 0L;
            bl = true;
        }
        if (bl) {
            this.invalidateSelf();
        }
    }

    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            DrawableContainerState drawableContainerState = this.cloneConstantState();
            drawableContainerState.mutate();
            this.setConstantState(drawableContainerState);
            this.mMutated = true;
        }
        return this;
    }

    protected void onBoundsChange(Rect rect) {
        Drawable drawable = this.mLastDrawable;
        if (drawable != null) {
            drawable.setBounds(rect);
        }
        if ((drawable = this.mCurrDrawable) != null) {
            drawable.setBounds(rect);
        }
    }

    public boolean onLayoutDirectionChanged(int n) {
        return this.mDrawableContainerState.setLayoutDirection(n, this.getCurrentIndex());
    }

    protected boolean onLevelChange(int n) {
        Drawable drawable = this.mLastDrawable;
        if (drawable != null) {
            return drawable.setLevel(n);
        }
        drawable = this.mCurrDrawable;
        if (drawable != null) {
            return drawable.setLevel(n);
        }
        return false;
    }

    protected boolean onStateChange(int[] arrn) {
        Drawable drawable = this.mLastDrawable;
        if (drawable != null) {
            return drawable.setState(arrn);
        }
        drawable = this.mCurrDrawable;
        if (drawable != null) {
            return drawable.setState(arrn);
        }
        return false;
    }

    public void scheduleDrawable(Drawable drawable, Runnable runnable, long l) {
        if (drawable == this.mCurrDrawable && this.getCallback() != null) {
            this.getCallback().scheduleDrawable((Drawable)this, runnable, l);
        }
    }

    boolean selectDrawable(int n) {
        Object object;
        if (n == this.mCurIndex) {
            return false;
        }
        long l = SystemClock.uptimeMillis();
        if (this.mDrawableContainerState.mExitFadeDuration > 0) {
            object = this.mLastDrawable;
            if (object != null) {
                object.setVisible(false, false);
            }
            if ((object = this.mCurrDrawable) != null) {
                this.mLastDrawable = object;
                this.mLastIndex = this.mCurIndex;
                this.mExitAnimationEnd = (long)this.mDrawableContainerState.mExitFadeDuration + l;
            } else {
                this.mLastDrawable = null;
                this.mLastIndex = -1;
                this.mExitAnimationEnd = 0L;
            }
        } else {
            object = this.mCurrDrawable;
            if (object != null) {
                object.setVisible(false, false);
            }
        }
        if (n >= 0 && n < this.mDrawableContainerState.mNumChildren) {
            object = this.mDrawableContainerState.getChild(n);
            this.mCurrDrawable = object;
            this.mCurIndex = n;
            if (object != null) {
                if (this.mDrawableContainerState.mEnterFadeDuration > 0) {
                    this.mEnterAnimationEnd = (long)this.mDrawableContainerState.mEnterFadeDuration + l;
                }
                this.initializeDrawableForDisplay((Drawable)object);
            }
        } else {
            this.mCurrDrawable = null;
            this.mCurIndex = -1;
        }
        if (this.mEnterAnimationEnd != 0L || this.mExitAnimationEnd != 0L) {
            object = this.mAnimationRunnable;
            if (object == null) {
                this.mAnimationRunnable = new Runnable(){

                    @Override
                    public void run() {
                        DrawableContainer.this.animate(true);
                        DrawableContainer.this.invalidateSelf();
                    }
                };
            } else {
                this.unscheduleSelf((Runnable)object);
            }
            this.animate(true);
        }
        this.invalidateSelf();
        return true;
    }

    public void setAlpha(int n) {
        if (!this.mHasAlpha || this.mAlpha != n) {
            this.mHasAlpha = true;
            this.mAlpha = n;
            Drawable drawable = this.mCurrDrawable;
            if (drawable != null) {
                if (this.mEnterAnimationEnd == 0L) {
                    drawable.setAlpha(n);
                    return;
                }
                this.animate(false);
            }
        }
    }

    public void setAutoMirrored(boolean bl) {
        if (this.mDrawableContainerState.mAutoMirrored != bl) {
            this.mDrawableContainerState.mAutoMirrored = bl;
            Drawable drawable = this.mCurrDrawable;
            if (drawable != null) {
                DrawableCompat.setAutoMirrored(drawable, this.mDrawableContainerState.mAutoMirrored);
            }
        }
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mDrawableContainerState.mHasColorFilter = true;
        if (this.mDrawableContainerState.mColorFilter != colorFilter) {
            this.mDrawableContainerState.mColorFilter = colorFilter;
            Drawable drawable = this.mCurrDrawable;
            if (drawable != null) {
                drawable.setColorFilter(colorFilter);
            }
        }
    }

    void setConstantState(DrawableContainerState drawableContainerState) {
        this.mDrawableContainerState = drawableContainerState;
        int n = this.mCurIndex;
        if (n >= 0) {
            drawableContainerState = drawableContainerState.getChild(n);
            this.mCurrDrawable = drawableContainerState;
            if (drawableContainerState != null) {
                this.initializeDrawableForDisplay((Drawable)drawableContainerState);
            }
        }
        this.mLastIndex = -1;
        this.mLastDrawable = null;
    }

    void setCurrentIndex(int n) {
        this.selectDrawable(n);
    }

    public void setDither(boolean bl) {
        if (this.mDrawableContainerState.mDither != bl) {
            this.mDrawableContainerState.mDither = bl;
            Drawable drawable = this.mCurrDrawable;
            if (drawable != null) {
                drawable.setDither(this.mDrawableContainerState.mDither);
            }
        }
    }

    public void setEnterFadeDuration(int n) {
        this.mDrawableContainerState.mEnterFadeDuration = n;
    }

    public void setExitFadeDuration(int n) {
        this.mDrawableContainerState.mExitFadeDuration = n;
    }

    public void setHotspot(float f, float f2) {
        Drawable drawable = this.mCurrDrawable;
        if (drawable != null) {
            DrawableCompat.setHotspot(drawable, f, f2);
        }
    }

    public void setHotspotBounds(int n, int n2, int n3, int n4) {
        Rect rect = this.mHotspotBounds;
        if (rect == null) {
            this.mHotspotBounds = new Rect(n, n2, n3, n4);
        } else {
            rect.set(n, n2, n3, n4);
        }
        rect = this.mCurrDrawable;
        if (rect != null) {
            DrawableCompat.setHotspotBounds((Drawable)rect, n, n2, n3, n4);
        }
    }

    public void setTintList(ColorStateList colorStateList) {
        this.mDrawableContainerState.mHasTintList = true;
        if (this.mDrawableContainerState.mTintList != colorStateList) {
            this.mDrawableContainerState.mTintList = colorStateList;
            DrawableCompat.setTintList(this.mCurrDrawable, colorStateList);
        }
    }

    public void setTintMode(PorterDuff.Mode mode) {
        this.mDrawableContainerState.mHasTintMode = true;
        if (this.mDrawableContainerState.mTintMode != mode) {
            this.mDrawableContainerState.mTintMode = mode;
            DrawableCompat.setTintMode(this.mCurrDrawable, mode);
        }
    }

    public boolean setVisible(boolean bl, boolean bl2) {
        boolean bl3 = super.setVisible(bl, bl2);
        Drawable drawable = this.mLastDrawable;
        if (drawable != null) {
            drawable.setVisible(bl, bl2);
        }
        if ((drawable = this.mCurrDrawable) != null) {
            drawable.setVisible(bl, bl2);
        }
        return bl3;
    }

    public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
        if (drawable == this.mCurrDrawable && this.getCallback() != null) {
            this.getCallback().unscheduleDrawable((Drawable)this, runnable);
        }
    }

    final void updateDensity(Resources resources) {
        this.mDrawableContainerState.updateDensity(resources);
    }

    static class BlockInvalidateCallback
    implements Drawable.Callback {
        private Drawable.Callback mCallback;

        BlockInvalidateCallback() {
        }

        public void invalidateDrawable(Drawable drawable) {
        }

        public void scheduleDrawable(Drawable drawable, Runnable runnable, long l) {
            Drawable.Callback callback = this.mCallback;
            if (callback != null) {
                callback.scheduleDrawable(drawable, runnable, l);
            }
        }

        public void unscheduleDrawable(Drawable drawable, Runnable runnable) {
            Drawable.Callback callback = this.mCallback;
            if (callback != null) {
                callback.unscheduleDrawable(drawable, runnable);
            }
        }

        public Drawable.Callback unwrap() {
            Drawable.Callback callback = this.mCallback;
            this.mCallback = null;
            return callback;
        }

        public BlockInvalidateCallback wrap(Drawable.Callback callback) {
            this.mCallback = callback;
            return this;
        }
    }

    static abstract class DrawableContainerState
    extends Drawable.ConstantState {
        boolean mAutoMirrored;
        boolean mCanConstantState;
        int mChangingConfigurations;
        boolean mCheckedConstantSize;
        boolean mCheckedConstantState;
        boolean mCheckedOpacity;
        boolean mCheckedPadding;
        boolean mCheckedStateful;
        int mChildrenChangingConfigurations;
        ColorFilter mColorFilter;
        int mConstantHeight;
        int mConstantMinimumHeight;
        int mConstantMinimumWidth;
        Rect mConstantPadding;
        boolean mConstantSize = false;
        int mConstantWidth;
        int mDensity = 160;
        boolean mDither = true;
        SparseArray<Drawable.ConstantState> mDrawableFutures;
        Drawable[] mDrawables;
        int mEnterFadeDuration = 0;
        int mExitFadeDuration = 0;
        boolean mHasColorFilter;
        boolean mHasTintList;
        boolean mHasTintMode;
        int mLayoutDirection;
        boolean mMutated;
        int mNumChildren;
        int mOpacity;
        final DrawableContainer mOwner;
        Resources mSourceRes;
        boolean mStateful;
        ColorStateList mTintList;
        PorterDuff.Mode mTintMode;
        boolean mVariablePadding = false;

        DrawableContainerState(DrawableContainerState constantState, DrawableContainer resources, Resources resources2) {
            this.mOwner = resources;
            resources = resources2 != null ? resources2 : (constantState != null ? constantState.mSourceRes : null);
            this.mSourceRes = resources;
            int n = constantState != null ? constantState.mDensity : 0;
            this.mDensity = n = DrawableContainer.resolveDensity(resources2, n);
            if (constantState != null) {
                this.mChangingConfigurations = constantState.mChangingConfigurations;
                this.mChildrenChangingConfigurations = constantState.mChildrenChangingConfigurations;
                this.mCheckedConstantState = true;
                this.mCanConstantState = true;
                this.mVariablePadding = constantState.mVariablePadding;
                this.mConstantSize = constantState.mConstantSize;
                this.mDither = constantState.mDither;
                this.mMutated = constantState.mMutated;
                this.mLayoutDirection = constantState.mLayoutDirection;
                this.mEnterFadeDuration = constantState.mEnterFadeDuration;
                this.mExitFadeDuration = constantState.mExitFadeDuration;
                this.mAutoMirrored = constantState.mAutoMirrored;
                this.mColorFilter = constantState.mColorFilter;
                this.mHasColorFilter = constantState.mHasColorFilter;
                this.mTintList = constantState.mTintList;
                this.mTintMode = constantState.mTintMode;
                this.mHasTintList = constantState.mHasTintList;
                this.mHasTintMode = constantState.mHasTintMode;
                if (constantState.mDensity == n) {
                    if (constantState.mCheckedPadding) {
                        this.mConstantPadding = new Rect(constantState.mConstantPadding);
                        this.mCheckedPadding = true;
                    }
                    if (constantState.mCheckedConstantSize) {
                        this.mConstantWidth = constantState.mConstantWidth;
                        this.mConstantHeight = constantState.mConstantHeight;
                        this.mConstantMinimumWidth = constantState.mConstantMinimumWidth;
                        this.mConstantMinimumHeight = constantState.mConstantMinimumHeight;
                        this.mCheckedConstantSize = true;
                    }
                }
                if (constantState.mCheckedOpacity) {
                    this.mOpacity = constantState.mOpacity;
                    this.mCheckedOpacity = true;
                }
                if (constantState.mCheckedStateful) {
                    this.mStateful = constantState.mStateful;
                    this.mCheckedStateful = true;
                }
                resources = constantState.mDrawables;
                this.mDrawables = new Drawable[resources.length];
                this.mNumChildren = constantState.mNumChildren;
                constantState = constantState.mDrawableFutures;
                this.mDrawableFutures = constantState != null ? constantState.clone() : new SparseArray(this.mNumChildren);
                int n2 = this.mNumChildren;
                for (n = 0; n < n2; ++n) {
                    if (resources[n] == null) continue;
                    constantState = resources[n].getConstantState();
                    if (constantState != null) {
                        this.mDrawableFutures.put(n, (Object)constantState);
                        continue;
                    }
                    this.mDrawables[n] = resources[n];
                }
                return;
            }
            this.mDrawables = new Drawable[10];
            this.mNumChildren = 0;
        }

        private void createAllFutures() {
            Drawable.ConstantState constantState = this.mDrawableFutures;
            if (constantState != null) {
                int n = constantState.size();
                for (int i = 0; i < n; ++i) {
                    int n2 = this.mDrawableFutures.keyAt(i);
                    constantState = (Drawable.ConstantState)this.mDrawableFutures.valueAt(i);
                    this.mDrawables[n2] = this.prepareDrawable(constantState.newDrawable(this.mSourceRes));
                }
                this.mDrawableFutures = null;
            }
        }

        private Drawable prepareDrawable(Drawable drawable) {
            if (Build.VERSION.SDK_INT >= 23) {
                drawable.setLayoutDirection(this.mLayoutDirection);
            }
            drawable = drawable.mutate();
            drawable.setCallback((Drawable.Callback)this.mOwner);
            return drawable;
        }

        public final int addChild(Drawable drawable) {
            int n = this.mNumChildren;
            if (n >= this.mDrawables.length) {
                this.growArray(n, n + 10);
            }
            drawable.mutate();
            drawable.setVisible(false, true);
            drawable.setCallback((Drawable.Callback)this.mOwner);
            this.mDrawables[n] = drawable;
            ++this.mNumChildren;
            this.mChildrenChangingConfigurations |= drawable.getChangingConfigurations();
            this.invalidateCache();
            this.mConstantPadding = null;
            this.mCheckedPadding = false;
            this.mCheckedConstantSize = false;
            this.mCheckedConstantState = false;
            return n;
        }

        final void applyTheme(Resources.Theme theme) {
            if (theme != null) {
                this.createAllFutures();
                int n = this.mNumChildren;
                Drawable[] arrdrawable = this.mDrawables;
                for (int i = 0; i < n; ++i) {
                    if (arrdrawable[i] == null || !arrdrawable[i].canApplyTheme()) continue;
                    arrdrawable[i].applyTheme(theme);
                    this.mChildrenChangingConfigurations |= arrdrawable[i].getChangingConfigurations();
                }
                this.updateDensity(theme.getResources());
            }
        }

        public boolean canApplyTheme() {
            int n = this.mNumChildren;
            Drawable[] arrdrawable = this.mDrawables;
            for (int i = 0; i < n; ++i) {
                Drawable drawable = arrdrawable[i];
                if (!(drawable != null ? drawable.canApplyTheme() : (drawable = (Drawable.ConstantState)this.mDrawableFutures.get(i)) != null && drawable.canApplyTheme())) continue;
                return true;
            }
            return false;
        }

        public boolean canConstantState() {
            synchronized (this) {
                block7 : {
                    if (!this.mCheckedConstantState) break block7;
                    boolean bl = this.mCanConstantState;
                    return bl;
                }
                this.createAllFutures();
                this.mCheckedConstantState = true;
                int n = this.mNumChildren;
                Drawable[] arrdrawable = this.mDrawables;
                for (int i = 0; i < n; ++i) {
                    if (arrdrawable[i].getConstantState() != null) continue;
                    this.mCanConstantState = false;
                    return false;
                }
                this.mCanConstantState = true;
                return true;
            }
        }

        final void clearMutated() {
            this.mMutated = false;
        }

        protected void computeConstantSize() {
            this.mCheckedConstantSize = true;
            this.createAllFutures();
            int n = this.mNumChildren;
            Drawable[] arrdrawable = this.mDrawables;
            this.mConstantHeight = -1;
            this.mConstantWidth = -1;
            this.mConstantMinimumHeight = 0;
            this.mConstantMinimumWidth = 0;
            for (int i = 0; i < n; ++i) {
                Drawable drawable = arrdrawable[i];
                int n2 = drawable.getIntrinsicWidth();
                if (n2 > this.mConstantWidth) {
                    this.mConstantWidth = n2;
                }
                if ((n2 = drawable.getIntrinsicHeight()) > this.mConstantHeight) {
                    this.mConstantHeight = n2;
                }
                if ((n2 = drawable.getMinimumWidth()) > this.mConstantMinimumWidth) {
                    this.mConstantMinimumWidth = n2;
                }
                if ((n2 = drawable.getMinimumHeight()) <= this.mConstantMinimumHeight) continue;
                this.mConstantMinimumHeight = n2;
            }
        }

        final int getCapacity() {
            return this.mDrawables.length;
        }

        public int getChangingConfigurations() {
            return this.mChangingConfigurations | this.mChildrenChangingConfigurations;
        }

        public final Drawable getChild(int n) {
            int n2;
            Drawable drawable = this.mDrawables[n];
            if (drawable != null) {
                return drawable;
            }
            drawable = this.mDrawableFutures;
            if (drawable != null && (n2 = drawable.indexOfKey(n)) >= 0) {
                this.mDrawables[n] = drawable = this.prepareDrawable(((Drawable.ConstantState)this.mDrawableFutures.valueAt(n2)).newDrawable(this.mSourceRes));
                this.mDrawableFutures.removeAt(n2);
                if (this.mDrawableFutures.size() == 0) {
                    this.mDrawableFutures = null;
                }
                return drawable;
            }
            return null;
        }

        public final int getChildCount() {
            return this.mNumChildren;
        }

        public final int getConstantHeight() {
            if (!this.mCheckedConstantSize) {
                this.computeConstantSize();
            }
            return this.mConstantHeight;
        }

        public final int getConstantMinimumHeight() {
            if (!this.mCheckedConstantSize) {
                this.computeConstantSize();
            }
            return this.mConstantMinimumHeight;
        }

        public final int getConstantMinimumWidth() {
            if (!this.mCheckedConstantSize) {
                this.computeConstantSize();
            }
            return this.mConstantMinimumWidth;
        }

        public final Rect getConstantPadding() {
            if (this.mVariablePadding) {
                return null;
            }
            if (this.mConstantPadding == null && !this.mCheckedPadding) {
                this.createAllFutures();
                Rect rect = null;
                Rect rect2 = new Rect();
                int n = this.mNumChildren;
                Drawable[] arrdrawable = this.mDrawables;
                for (int i = 0; i < n; ++i) {
                    Rect rect3 = rect;
                    if (arrdrawable[i].getPadding(rect2)) {
                        Rect rect4 = rect;
                        if (rect == null) {
                            rect4 = new Rect(0, 0, 0, 0);
                        }
                        if (rect2.left > rect4.left) {
                            rect4.left = rect2.left;
                        }
                        if (rect2.top > rect4.top) {
                            rect4.top = rect2.top;
                        }
                        if (rect2.right > rect4.right) {
                            rect4.right = rect2.right;
                        }
                        rect3 = rect4;
                        if (rect2.bottom > rect4.bottom) {
                            rect4.bottom = rect2.bottom;
                            rect3 = rect4;
                        }
                    }
                    rect = rect3;
                }
                this.mCheckedPadding = true;
                this.mConstantPadding = rect;
                return rect;
            }
            return this.mConstantPadding;
        }

        public final int getConstantWidth() {
            if (!this.mCheckedConstantSize) {
                this.computeConstantSize();
            }
            return this.mConstantWidth;
        }

        public final int getEnterFadeDuration() {
            return this.mEnterFadeDuration;
        }

        public final int getExitFadeDuration() {
            return this.mExitFadeDuration;
        }

        public final int getOpacity() {
            if (this.mCheckedOpacity) {
                return this.mOpacity;
            }
            this.createAllFutures();
            int n = this.mNumChildren;
            Drawable[] arrdrawable = this.mDrawables;
            int n2 = n > 0 ? arrdrawable[0].getOpacity() : -2;
            for (int i = 1; i < n; ++i) {
                n2 = Drawable.resolveOpacity((int)n2, (int)arrdrawable[i].getOpacity());
            }
            this.mOpacity = n2;
            this.mCheckedOpacity = true;
            return n2;
        }

        public void growArray(int n, int n2) {
            Drawable[] arrdrawable = new Drawable[n2];
            System.arraycopy(this.mDrawables, 0, arrdrawable, 0, n);
            this.mDrawables = arrdrawable;
        }

        void invalidateCache() {
            this.mCheckedOpacity = false;
            this.mCheckedStateful = false;
        }

        public final boolean isConstantSize() {
            return this.mConstantSize;
        }

        public final boolean isStateful() {
            boolean bl;
            if (this.mCheckedStateful) {
                return this.mStateful;
            }
            this.createAllFutures();
            int n = this.mNumChildren;
            Drawable[] arrdrawable = this.mDrawables;
            boolean bl2 = false;
            int n2 = 0;
            do {
                bl = bl2;
                if (n2 >= n) break;
                if (arrdrawable[n2].isStateful()) {
                    bl = true;
                    break;
                }
                ++n2;
            } while (true);
            this.mStateful = bl;
            this.mCheckedStateful = true;
            return bl;
        }

        void mutate() {
            int n = this.mNumChildren;
            Drawable[] arrdrawable = this.mDrawables;
            for (int i = 0; i < n; ++i) {
                if (arrdrawable[i] == null) continue;
                arrdrawable[i].mutate();
            }
            this.mMutated = true;
        }

        public final void setConstantSize(boolean bl) {
            this.mConstantSize = bl;
        }

        public final void setEnterFadeDuration(int n) {
            this.mEnterFadeDuration = n;
        }

        public final void setExitFadeDuration(int n) {
            this.mExitFadeDuration = n;
        }

        final boolean setLayoutDirection(int n, int n2) {
            boolean bl = false;
            int n3 = this.mNumChildren;
            Drawable[] arrdrawable = this.mDrawables;
            for (int i = 0; i < n3; ++i) {
                boolean bl2 = bl;
                if (arrdrawable[i] != null) {
                    boolean bl3 = false;
                    if (Build.VERSION.SDK_INT >= 23) {
                        bl3 = arrdrawable[i].setLayoutDirection(n);
                    }
                    bl2 = bl;
                    if (i == n2) {
                        bl2 = bl3;
                    }
                }
                bl = bl2;
            }
            this.mLayoutDirection = n;
            return bl;
        }

        public final void setVariablePadding(boolean bl) {
            this.mVariablePadding = bl;
        }

        final void updateDensity(Resources resources) {
            if (resources != null) {
                this.mSourceRes = resources;
                int n = DrawableContainer.resolveDensity(resources, this.mDensity);
                int n2 = this.mDensity;
                this.mDensity = n;
                if (n2 != n) {
                    this.mCheckedConstantSize = false;
                    this.mCheckedPadding = false;
                }
            }
        }
    }

}

