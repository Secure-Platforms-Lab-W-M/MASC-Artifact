// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.appcompat.graphics.drawable;

import android.util.SparseArray;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.drawable.Drawable$ConstantState;
import android.graphics.Canvas;
import android.content.res.Resources$Theme;
import android.os.SystemClock;
import android.content.res.Resources;
import android.os.Build$VERSION;
import androidx.core.graphics.drawable.DrawableCompat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable$Callback;
import android.graphics.drawable.Drawable;

class DrawableContainer extends Drawable implements Drawable$Callback
{
    private static final boolean DEBUG = false;
    private static final boolean DEFAULT_DITHER = true;
    private static final String TAG = "DrawableContainer";
    private int mAlpha;
    private Runnable mAnimationRunnable;
    private BlockInvalidateCallback mBlockInvalidateCallback;
    private int mCurIndex;
    private Drawable mCurrDrawable;
    private DrawableContainerState mDrawableContainerState;
    private long mEnterAnimationEnd;
    private long mExitAnimationEnd;
    private boolean mHasAlpha;
    private Rect mHotspotBounds;
    private Drawable mLastDrawable;
    private int mLastIndex;
    private boolean mMutated;
    
    DrawableContainer() {
        this.mAlpha = 255;
        this.mCurIndex = -1;
        this.mLastIndex = -1;
    }
    
    private void initializeDrawableForDisplay(final Drawable drawable) {
        if (this.mBlockInvalidateCallback == null) {
            this.mBlockInvalidateCallback = new BlockInvalidateCallback();
        }
        drawable.setCallback((Drawable$Callback)this.mBlockInvalidateCallback.wrap(drawable.getCallback()));
        try {
            if (this.mDrawableContainerState.mEnterFadeDuration <= 0 && this.mHasAlpha) {
                drawable.setAlpha(this.mAlpha);
            }
            if (this.mDrawableContainerState.mHasColorFilter) {
                drawable.setColorFilter(this.mDrawableContainerState.mColorFilter);
            }
            else {
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
            if (Build$VERSION.SDK_INT >= 23) {
                drawable.setLayoutDirection(this.getLayoutDirection());
            }
            if (Build$VERSION.SDK_INT >= 19) {
                drawable.setAutoMirrored(this.mDrawableContainerState.mAutoMirrored);
            }
            final Rect mHotspotBounds = this.mHotspotBounds;
            if (Build$VERSION.SDK_INT >= 21 && mHotspotBounds != null) {
                drawable.setHotspotBounds(mHotspotBounds.left, mHotspotBounds.top, mHotspotBounds.right, mHotspotBounds.bottom);
            }
        }
        finally {
            drawable.setCallback(this.mBlockInvalidateCallback.unwrap());
        }
    }
    
    private boolean needsMirroring() {
        return this.isAutoMirrored() && DrawableCompat.getLayoutDirection(this) == 1;
    }
    
    static int resolveDensity(final Resources resources, int densityDpi) {
        if (resources != null) {
            densityDpi = resources.getDisplayMetrics().densityDpi;
        }
        if (densityDpi == 0) {
            return 160;
        }
        return densityDpi;
    }
    
    void animate(final boolean b) {
        this.mHasAlpha = true;
        final long uptimeMillis = SystemClock.uptimeMillis();
        final boolean b2 = false;
        final Drawable mCurrDrawable = this.mCurrDrawable;
        boolean b3;
        if (mCurrDrawable != null) {
            final long mEnterAnimationEnd = this.mEnterAnimationEnd;
            b3 = b2;
            if (mEnterAnimationEnd != 0L) {
                if (mEnterAnimationEnd <= uptimeMillis) {
                    mCurrDrawable.setAlpha(this.mAlpha);
                    this.mEnterAnimationEnd = 0L;
                    b3 = b2;
                }
                else {
                    this.mCurrDrawable.setAlpha((255 - (int)((mEnterAnimationEnd - uptimeMillis) * 255L) / this.mDrawableContainerState.mEnterFadeDuration) * this.mAlpha / 255);
                    b3 = true;
                }
            }
        }
        else {
            this.mEnterAnimationEnd = 0L;
            b3 = b2;
        }
        final Drawable mLastDrawable = this.mLastDrawable;
        boolean b4;
        if (mLastDrawable != null) {
            final long mExitAnimationEnd = this.mExitAnimationEnd;
            b4 = b3;
            if (mExitAnimationEnd != 0L) {
                if (mExitAnimationEnd <= uptimeMillis) {
                    mLastDrawable.setVisible(false, false);
                    this.mLastDrawable = null;
                    this.mLastIndex = -1;
                    this.mExitAnimationEnd = 0L;
                    b4 = b3;
                }
                else {
                    this.mLastDrawable.setAlpha(this.mAlpha * ((int)((mExitAnimationEnd - uptimeMillis) * 255L) / this.mDrawableContainerState.mExitFadeDuration) / 255);
                    b4 = true;
                }
            }
        }
        else {
            this.mExitAnimationEnd = 0L;
            b4 = b3;
        }
        if (b && b4) {
            this.scheduleSelf(this.mAnimationRunnable, 16L + uptimeMillis);
        }
    }
    
    public void applyTheme(final Resources$Theme resources$Theme) {
        this.mDrawableContainerState.applyTheme(resources$Theme);
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
    
    public void draw(final Canvas canvas) {
        final Drawable mCurrDrawable = this.mCurrDrawable;
        if (mCurrDrawable != null) {
            mCurrDrawable.draw(canvas);
        }
        final Drawable mLastDrawable = this.mLastDrawable;
        if (mLastDrawable != null) {
            mLastDrawable.draw(canvas);
        }
    }
    
    public int getAlpha() {
        return this.mAlpha;
    }
    
    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | this.mDrawableContainerState.getChangingConfigurations();
    }
    
    public final Drawable$ConstantState getConstantState() {
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
    
    public void getHotspotBounds(final Rect rect) {
        final Rect mHotspotBounds = this.mHotspotBounds;
        if (mHotspotBounds != null) {
            rect.set(mHotspotBounds);
            return;
        }
        super.getHotspotBounds(rect);
    }
    
    public int getIntrinsicHeight() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantHeight();
        }
        final Drawable mCurrDrawable = this.mCurrDrawable;
        if (mCurrDrawable != null) {
            return mCurrDrawable.getIntrinsicHeight();
        }
        return -1;
    }
    
    public int getIntrinsicWidth() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantWidth();
        }
        final Drawable mCurrDrawable = this.mCurrDrawable;
        if (mCurrDrawable != null) {
            return mCurrDrawable.getIntrinsicWidth();
        }
        return -1;
    }
    
    public int getMinimumHeight() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantMinimumHeight();
        }
        final Drawable mCurrDrawable = this.mCurrDrawable;
        if (mCurrDrawable != null) {
            return mCurrDrawable.getMinimumHeight();
        }
        return 0;
    }
    
    public int getMinimumWidth() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantMinimumWidth();
        }
        final Drawable mCurrDrawable = this.mCurrDrawable;
        if (mCurrDrawable != null) {
            return mCurrDrawable.getMinimumWidth();
        }
        return 0;
    }
    
    public int getOpacity() {
        final Drawable mCurrDrawable = this.mCurrDrawable;
        if (mCurrDrawable != null && mCurrDrawable.isVisible()) {
            return this.mDrawableContainerState.getOpacity();
        }
        return -2;
    }
    
    public void getOutline(final Outline outline) {
        final Drawable mCurrDrawable = this.mCurrDrawable;
        if (mCurrDrawable != null) {
            mCurrDrawable.getOutline(outline);
        }
    }
    
    public boolean getPadding(final Rect rect) {
        final Rect constantPadding = this.mDrawableContainerState.getConstantPadding();
        boolean b;
        if (constantPadding != null) {
            rect.set(constantPadding);
            b = ((constantPadding.left | constantPadding.top | constantPadding.bottom | constantPadding.right) != 0x0);
        }
        else {
            final Drawable mCurrDrawable = this.mCurrDrawable;
            if (mCurrDrawable != null) {
                b = mCurrDrawable.getPadding(rect);
            }
            else {
                b = super.getPadding(rect);
            }
        }
        if (this.needsMirroring()) {
            final int left = rect.left;
            rect.left = rect.right;
            rect.right = left;
        }
        return b;
    }
    
    public void invalidateDrawable(final Drawable drawable) {
        final DrawableContainerState mDrawableContainerState = this.mDrawableContainerState;
        if (mDrawableContainerState != null) {
            mDrawableContainerState.invalidateCache();
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
        boolean b = false;
        final Drawable mLastDrawable = this.mLastDrawable;
        if (mLastDrawable != null) {
            mLastDrawable.jumpToCurrentState();
            this.mLastDrawable = null;
            this.mLastIndex = -1;
            b = true;
        }
        final Drawable mCurrDrawable = this.mCurrDrawable;
        if (mCurrDrawable != null) {
            mCurrDrawable.jumpToCurrentState();
            if (this.mHasAlpha) {
                this.mCurrDrawable.setAlpha(this.mAlpha);
            }
        }
        if (this.mExitAnimationEnd != 0L) {
            this.mExitAnimationEnd = 0L;
            b = true;
        }
        if (this.mEnterAnimationEnd != 0L) {
            this.mEnterAnimationEnd = 0L;
            b = true;
        }
        if (b) {
            this.invalidateSelf();
        }
    }
    
    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            final DrawableContainerState cloneConstantState = this.cloneConstantState();
            cloneConstantState.mutate();
            this.setConstantState(cloneConstantState);
            this.mMutated = true;
        }
        return this;
    }
    
    protected void onBoundsChange(final Rect rect) {
        final Drawable mLastDrawable = this.mLastDrawable;
        if (mLastDrawable != null) {
            mLastDrawable.setBounds(rect);
        }
        final Drawable mCurrDrawable = this.mCurrDrawable;
        if (mCurrDrawable != null) {
            mCurrDrawable.setBounds(rect);
        }
    }
    
    public boolean onLayoutDirectionChanged(final int n) {
        return this.mDrawableContainerState.setLayoutDirection(n, this.getCurrentIndex());
    }
    
    protected boolean onLevelChange(final int n) {
        final Drawable mLastDrawable = this.mLastDrawable;
        if (mLastDrawable != null) {
            return mLastDrawable.setLevel(n);
        }
        final Drawable mCurrDrawable = this.mCurrDrawable;
        return mCurrDrawable != null && mCurrDrawable.setLevel(n);
    }
    
    protected boolean onStateChange(final int[] array) {
        final Drawable mLastDrawable = this.mLastDrawable;
        if (mLastDrawable != null) {
            return mLastDrawable.setState(array);
        }
        final Drawable mCurrDrawable = this.mCurrDrawable;
        return mCurrDrawable != null && mCurrDrawable.setState(array);
    }
    
    public void scheduleDrawable(final Drawable drawable, final Runnable runnable, final long n) {
        if (drawable == this.mCurrDrawable && this.getCallback() != null) {
            this.getCallback().scheduleDrawable((Drawable)this, runnable, n);
        }
    }
    
    boolean selectDrawable(final int mCurIndex) {
        if (mCurIndex == this.mCurIndex) {
            return false;
        }
        final long uptimeMillis = SystemClock.uptimeMillis();
        if (this.mDrawableContainerState.mExitFadeDuration > 0) {
            final Drawable mLastDrawable = this.mLastDrawable;
            if (mLastDrawable != null) {
                mLastDrawable.setVisible(false, false);
            }
            final Drawable mCurrDrawable = this.mCurrDrawable;
            if (mCurrDrawable != null) {
                this.mLastDrawable = mCurrDrawable;
                this.mLastIndex = this.mCurIndex;
                this.mExitAnimationEnd = this.mDrawableContainerState.mExitFadeDuration + uptimeMillis;
            }
            else {
                this.mLastDrawable = null;
                this.mLastIndex = -1;
                this.mExitAnimationEnd = 0L;
            }
        }
        else {
            final Drawable mCurrDrawable2 = this.mCurrDrawable;
            if (mCurrDrawable2 != null) {
                mCurrDrawable2.setVisible(false, false);
            }
        }
        if (mCurIndex >= 0 && mCurIndex < this.mDrawableContainerState.mNumChildren) {
            final Drawable child = this.mDrawableContainerState.getChild(mCurIndex);
            this.mCurrDrawable = child;
            this.mCurIndex = mCurIndex;
            if (child != null) {
                if (this.mDrawableContainerState.mEnterFadeDuration > 0) {
                    this.mEnterAnimationEnd = this.mDrawableContainerState.mEnterFadeDuration + uptimeMillis;
                }
                this.initializeDrawableForDisplay(child);
            }
        }
        else {
            this.mCurrDrawable = null;
            this.mCurIndex = -1;
        }
        if (this.mEnterAnimationEnd != 0L || this.mExitAnimationEnd != 0L) {
            final Runnable mAnimationRunnable = this.mAnimationRunnable;
            if (mAnimationRunnable == null) {
                this.mAnimationRunnable = new Runnable() {
                    @Override
                    public void run() {
                        DrawableContainer.this.animate(true);
                        DrawableContainer.this.invalidateSelf();
                    }
                };
            }
            else {
                this.unscheduleSelf(mAnimationRunnable);
            }
            this.animate(true);
        }
        this.invalidateSelf();
        return true;
    }
    
    public void setAlpha(final int n) {
        if (!this.mHasAlpha || this.mAlpha != n) {
            this.mHasAlpha = true;
            this.mAlpha = n;
            final Drawable mCurrDrawable = this.mCurrDrawable;
            if (mCurrDrawable != null) {
                if (this.mEnterAnimationEnd == 0L) {
                    mCurrDrawable.setAlpha(n);
                    return;
                }
                this.animate(false);
            }
        }
    }
    
    public void setAutoMirrored(final boolean mAutoMirrored) {
        if (this.mDrawableContainerState.mAutoMirrored != mAutoMirrored) {
            this.mDrawableContainerState.mAutoMirrored = mAutoMirrored;
            final Drawable mCurrDrawable = this.mCurrDrawable;
            if (mCurrDrawable != null) {
                DrawableCompat.setAutoMirrored(mCurrDrawable, this.mDrawableContainerState.mAutoMirrored);
            }
        }
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        this.mDrawableContainerState.mHasColorFilter = true;
        if (this.mDrawableContainerState.mColorFilter != colorFilter) {
            this.mDrawableContainerState.mColorFilter = colorFilter;
            final Drawable mCurrDrawable = this.mCurrDrawable;
            if (mCurrDrawable != null) {
                mCurrDrawable.setColorFilter(colorFilter);
            }
        }
    }
    
    void setConstantState(final DrawableContainerState mDrawableContainerState) {
        this.mDrawableContainerState = mDrawableContainerState;
        final int mCurIndex = this.mCurIndex;
        if (mCurIndex >= 0) {
            final Drawable child = mDrawableContainerState.getChild(mCurIndex);
            if ((this.mCurrDrawable = child) != null) {
                this.initializeDrawableForDisplay(child);
            }
        }
        this.mLastIndex = -1;
        this.mLastDrawable = null;
    }
    
    void setCurrentIndex(final int n) {
        this.selectDrawable(n);
    }
    
    public void setDither(final boolean mDither) {
        if (this.mDrawableContainerState.mDither != mDither) {
            this.mDrawableContainerState.mDither = mDither;
            final Drawable mCurrDrawable = this.mCurrDrawable;
            if (mCurrDrawable != null) {
                mCurrDrawable.setDither(this.mDrawableContainerState.mDither);
            }
        }
    }
    
    public void setEnterFadeDuration(final int mEnterFadeDuration) {
        this.mDrawableContainerState.mEnterFadeDuration = mEnterFadeDuration;
    }
    
    public void setExitFadeDuration(final int mExitFadeDuration) {
        this.mDrawableContainerState.mExitFadeDuration = mExitFadeDuration;
    }
    
    public void setHotspot(final float n, final float n2) {
        final Drawable mCurrDrawable = this.mCurrDrawable;
        if (mCurrDrawable != null) {
            DrawableCompat.setHotspot(mCurrDrawable, n, n2);
        }
    }
    
    public void setHotspotBounds(final int n, final int n2, final int n3, final int n4) {
        final Rect mHotspotBounds = this.mHotspotBounds;
        if (mHotspotBounds == null) {
            this.mHotspotBounds = new Rect(n, n2, n3, n4);
        }
        else {
            mHotspotBounds.set(n, n2, n3, n4);
        }
        final Drawable mCurrDrawable = this.mCurrDrawable;
        if (mCurrDrawable != null) {
            DrawableCompat.setHotspotBounds(mCurrDrawable, n, n2, n3, n4);
        }
    }
    
    public void setTintList(final ColorStateList mTintList) {
        this.mDrawableContainerState.mHasTintList = true;
        if (this.mDrawableContainerState.mTintList != mTintList) {
            this.mDrawableContainerState.mTintList = mTintList;
            DrawableCompat.setTintList(this.mCurrDrawable, mTintList);
        }
    }
    
    public void setTintMode(final PorterDuff$Mode mTintMode) {
        this.mDrawableContainerState.mHasTintMode = true;
        if (this.mDrawableContainerState.mTintMode != mTintMode) {
            this.mDrawableContainerState.mTintMode = mTintMode;
            DrawableCompat.setTintMode(this.mCurrDrawable, mTintMode);
        }
    }
    
    public boolean setVisible(final boolean b, final boolean b2) {
        final boolean setVisible = super.setVisible(b, b2);
        final Drawable mLastDrawable = this.mLastDrawable;
        if (mLastDrawable != null) {
            mLastDrawable.setVisible(b, b2);
        }
        final Drawable mCurrDrawable = this.mCurrDrawable;
        if (mCurrDrawable != null) {
            mCurrDrawable.setVisible(b, b2);
        }
        return setVisible;
    }
    
    public void unscheduleDrawable(final Drawable drawable, final Runnable runnable) {
        if (drawable == this.mCurrDrawable && this.getCallback() != null) {
            this.getCallback().unscheduleDrawable((Drawable)this, runnable);
        }
    }
    
    final void updateDensity(final Resources resources) {
        this.mDrawableContainerState.updateDensity(resources);
    }
    
    static class BlockInvalidateCallback implements Drawable$Callback
    {
        private Drawable$Callback mCallback;
        
        public void invalidateDrawable(final Drawable drawable) {
        }
        
        public void scheduleDrawable(final Drawable drawable, final Runnable runnable, final long n) {
            final Drawable$Callback mCallback = this.mCallback;
            if (mCallback != null) {
                mCallback.scheduleDrawable(drawable, runnable, n);
            }
        }
        
        public void unscheduleDrawable(final Drawable drawable, final Runnable runnable) {
            final Drawable$Callback mCallback = this.mCallback;
            if (mCallback != null) {
                mCallback.unscheduleDrawable(drawable, runnable);
            }
        }
        
        public Drawable$Callback unwrap() {
            final Drawable$Callback mCallback = this.mCallback;
            this.mCallback = null;
            return mCallback;
        }
        
        public BlockInvalidateCallback wrap(final Drawable$Callback mCallback) {
            this.mCallback = mCallback;
            return this;
        }
    }
    
    abstract static class DrawableContainerState extends Drawable$ConstantState
    {
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
        boolean mConstantSize;
        int mConstantWidth;
        int mDensity;
        boolean mDither;
        SparseArray<Drawable$ConstantState> mDrawableFutures;
        Drawable[] mDrawables;
        int mEnterFadeDuration;
        int mExitFadeDuration;
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
        PorterDuff$Mode mTintMode;
        boolean mVariablePadding;
        
        DrawableContainerState(final DrawableContainerState drawableContainerState, final DrawableContainer mOwner, final Resources resources) {
            this.mDensity = 160;
            this.mVariablePadding = false;
            this.mConstantSize = false;
            this.mDither = true;
            this.mEnterFadeDuration = 0;
            this.mExitFadeDuration = 0;
            this.mOwner = mOwner;
            Resources mSourceRes;
            if (resources != null) {
                mSourceRes = resources;
            }
            else if (drawableContainerState != null) {
                mSourceRes = drawableContainerState.mSourceRes;
            }
            else {
                mSourceRes = null;
            }
            this.mSourceRes = mSourceRes;
            int mDensity;
            if (drawableContainerState != null) {
                mDensity = drawableContainerState.mDensity;
            }
            else {
                mDensity = 0;
            }
            final int resolveDensity = DrawableContainer.resolveDensity(resources, mDensity);
            this.mDensity = resolveDensity;
            if (drawableContainerState != null) {
                this.mChangingConfigurations = drawableContainerState.mChangingConfigurations;
                this.mChildrenChangingConfigurations = drawableContainerState.mChildrenChangingConfigurations;
                this.mCheckedConstantState = true;
                this.mCanConstantState = true;
                this.mVariablePadding = drawableContainerState.mVariablePadding;
                this.mConstantSize = drawableContainerState.mConstantSize;
                this.mDither = drawableContainerState.mDither;
                this.mMutated = drawableContainerState.mMutated;
                this.mLayoutDirection = drawableContainerState.mLayoutDirection;
                this.mEnterFadeDuration = drawableContainerState.mEnterFadeDuration;
                this.mExitFadeDuration = drawableContainerState.mExitFadeDuration;
                this.mAutoMirrored = drawableContainerState.mAutoMirrored;
                this.mColorFilter = drawableContainerState.mColorFilter;
                this.mHasColorFilter = drawableContainerState.mHasColorFilter;
                this.mTintList = drawableContainerState.mTintList;
                this.mTintMode = drawableContainerState.mTintMode;
                this.mHasTintList = drawableContainerState.mHasTintList;
                this.mHasTintMode = drawableContainerState.mHasTintMode;
                if (drawableContainerState.mDensity == resolveDensity) {
                    if (drawableContainerState.mCheckedPadding) {
                        this.mConstantPadding = new Rect(drawableContainerState.mConstantPadding);
                        this.mCheckedPadding = true;
                    }
                    if (drawableContainerState.mCheckedConstantSize) {
                        this.mConstantWidth = drawableContainerState.mConstantWidth;
                        this.mConstantHeight = drawableContainerState.mConstantHeight;
                        this.mConstantMinimumWidth = drawableContainerState.mConstantMinimumWidth;
                        this.mConstantMinimumHeight = drawableContainerState.mConstantMinimumHeight;
                        this.mCheckedConstantSize = true;
                    }
                }
                if (drawableContainerState.mCheckedOpacity) {
                    this.mOpacity = drawableContainerState.mOpacity;
                    this.mCheckedOpacity = true;
                }
                if (drawableContainerState.mCheckedStateful) {
                    this.mStateful = drawableContainerState.mStateful;
                    this.mCheckedStateful = true;
                }
                final Drawable[] mDrawables = drawableContainerState.mDrawables;
                this.mDrawables = new Drawable[mDrawables.length];
                this.mNumChildren = drawableContainerState.mNumChildren;
                final SparseArray<Drawable$ConstantState> mDrawableFutures = drawableContainerState.mDrawableFutures;
                if (mDrawableFutures != null) {
                    this.mDrawableFutures = (SparseArray<Drawable$ConstantState>)mDrawableFutures.clone();
                }
                else {
                    this.mDrawableFutures = (SparseArray<Drawable$ConstantState>)new SparseArray(this.mNumChildren);
                }
                for (int mNumChildren = this.mNumChildren, i = 0; i < mNumChildren; ++i) {
                    if (mDrawables[i] != null) {
                        final Drawable$ConstantState constantState = mDrawables[i].getConstantState();
                        if (constantState != null) {
                            this.mDrawableFutures.put(i, (Object)constantState);
                        }
                        else {
                            this.mDrawables[i] = mDrawables[i];
                        }
                    }
                }
                return;
            }
            this.mDrawables = new Drawable[10];
            this.mNumChildren = 0;
        }
        
        private void createAllFutures() {
            final SparseArray<Drawable$ConstantState> mDrawableFutures = this.mDrawableFutures;
            if (mDrawableFutures != null) {
                for (int size = mDrawableFutures.size(), i = 0; i < size; ++i) {
                    this.mDrawables[this.mDrawableFutures.keyAt(i)] = this.prepareDrawable(((Drawable$ConstantState)this.mDrawableFutures.valueAt(i)).newDrawable(this.mSourceRes));
                }
                this.mDrawableFutures = null;
            }
        }
        
        private Drawable prepareDrawable(Drawable mutate) {
            if (Build$VERSION.SDK_INT >= 23) {
                mutate.setLayoutDirection(this.mLayoutDirection);
            }
            mutate = mutate.mutate();
            mutate.setCallback((Drawable$Callback)this.mOwner);
            return mutate;
        }
        
        public final int addChild(final Drawable drawable) {
            final int mNumChildren = this.mNumChildren;
            if (mNumChildren >= this.mDrawables.length) {
                this.growArray(mNumChildren, mNumChildren + 10);
            }
            drawable.mutate();
            drawable.setVisible(false, true);
            drawable.setCallback((Drawable$Callback)this.mOwner);
            this.mDrawables[mNumChildren] = drawable;
            ++this.mNumChildren;
            this.mChildrenChangingConfigurations |= drawable.getChangingConfigurations();
            this.invalidateCache();
            this.mConstantPadding = null;
            this.mCheckedPadding = false;
            this.mCheckedConstantSize = false;
            this.mCheckedConstantState = false;
            return mNumChildren;
        }
        
        final void applyTheme(final Resources$Theme resources$Theme) {
            if (resources$Theme != null) {
                this.createAllFutures();
                final int mNumChildren = this.mNumChildren;
                final Drawable[] mDrawables = this.mDrawables;
                for (int i = 0; i < mNumChildren; ++i) {
                    if (mDrawables[i] != null && mDrawables[i].canApplyTheme()) {
                        mDrawables[i].applyTheme(resources$Theme);
                        this.mChildrenChangingConfigurations |= mDrawables[i].getChangingConfigurations();
                    }
                }
                this.updateDensity(resources$Theme.getResources());
            }
        }
        
        public boolean canApplyTheme() {
            final int mNumChildren = this.mNumChildren;
            final Drawable[] mDrawables = this.mDrawables;
            for (int i = 0; i < mNumChildren; ++i) {
                final Drawable drawable = mDrawables[i];
                if (drawable != null) {
                    if (drawable.canApplyTheme()) {
                        return true;
                    }
                }
                else {
                    final Drawable$ConstantState drawable$ConstantState = (Drawable$ConstantState)this.mDrawableFutures.get(i);
                    if (drawable$ConstantState != null && drawable$ConstantState.canApplyTheme()) {
                        return true;
                    }
                }
            }
            return false;
        }
        
        public boolean canConstantState() {
            synchronized (this) {
                if (this.mCheckedConstantState) {
                    return this.mCanConstantState;
                }
                this.createAllFutures();
                this.mCheckedConstantState = true;
                final int mNumChildren = this.mNumChildren;
                final Drawable[] mDrawables = this.mDrawables;
                for (int i = 0; i < mNumChildren; ++i) {
                    if (mDrawables[i].getConstantState() == null) {
                        return this.mCanConstantState = false;
                    }
                }
                return this.mCanConstantState = true;
            }
        }
        
        final void clearMutated() {
            this.mMutated = false;
        }
        
        protected void computeConstantSize() {
            this.mCheckedConstantSize = true;
            this.createAllFutures();
            final int mNumChildren = this.mNumChildren;
            final Drawable[] mDrawables = this.mDrawables;
            this.mConstantHeight = -1;
            this.mConstantWidth = -1;
            this.mConstantMinimumHeight = 0;
            this.mConstantMinimumWidth = 0;
            for (int i = 0; i < mNumChildren; ++i) {
                final Drawable drawable = mDrawables[i];
                final int intrinsicWidth = drawable.getIntrinsicWidth();
                if (intrinsicWidth > this.mConstantWidth) {
                    this.mConstantWidth = intrinsicWidth;
                }
                final int intrinsicHeight = drawable.getIntrinsicHeight();
                if (intrinsicHeight > this.mConstantHeight) {
                    this.mConstantHeight = intrinsicHeight;
                }
                final int minimumWidth = drawable.getMinimumWidth();
                if (minimumWidth > this.mConstantMinimumWidth) {
                    this.mConstantMinimumWidth = minimumWidth;
                }
                final int minimumHeight = drawable.getMinimumHeight();
                if (minimumHeight > this.mConstantMinimumHeight) {
                    this.mConstantMinimumHeight = minimumHeight;
                }
            }
        }
        
        final int getCapacity() {
            return this.mDrawables.length;
        }
        
        public int getChangingConfigurations() {
            return this.mChangingConfigurations | this.mChildrenChangingConfigurations;
        }
        
        public final Drawable getChild(final int n) {
            final Drawable drawable = this.mDrawables[n];
            if (drawable != null) {
                return drawable;
            }
            final SparseArray<Drawable$ConstantState> mDrawableFutures = this.mDrawableFutures;
            if (mDrawableFutures != null) {
                final int indexOfKey = mDrawableFutures.indexOfKey(n);
                if (indexOfKey >= 0) {
                    final Drawable prepareDrawable = this.prepareDrawable(((Drawable$ConstantState)this.mDrawableFutures.valueAt(indexOfKey)).newDrawable(this.mSourceRes));
                    this.mDrawables[n] = prepareDrawable;
                    this.mDrawableFutures.removeAt(indexOfKey);
                    if (this.mDrawableFutures.size() == 0) {
                        this.mDrawableFutures = null;
                    }
                    return prepareDrawable;
                }
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
                Rect mConstantPadding = null;
                final Rect rect = new Rect();
                final int mNumChildren = this.mNumChildren;
                final Drawable[] mDrawables = this.mDrawables;
                Rect rect2;
                for (int i = 0; i < mNumChildren; ++i, mConstantPadding = rect2) {
                    rect2 = mConstantPadding;
                    if (mDrawables[i].getPadding(rect)) {
                        Rect rect3;
                        if ((rect3 = mConstantPadding) == null) {
                            rect3 = new Rect(0, 0, 0, 0);
                        }
                        if (rect.left > rect3.left) {
                            rect3.left = rect.left;
                        }
                        if (rect.top > rect3.top) {
                            rect3.top = rect.top;
                        }
                        if (rect.right > rect3.right) {
                            rect3.right = rect.right;
                        }
                        rect2 = rect3;
                        if (rect.bottom > rect3.bottom) {
                            rect3.bottom = rect.bottom;
                            rect2 = rect3;
                        }
                    }
                }
                this.mCheckedPadding = true;
                return this.mConstantPadding = mConstantPadding;
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
            final int mNumChildren = this.mNumChildren;
            final Drawable[] mDrawables = this.mDrawables;
            int mOpacity;
            if (mNumChildren > 0) {
                mOpacity = mDrawables[0].getOpacity();
            }
            else {
                mOpacity = -2;
            }
            for (int i = 1; i < mNumChildren; ++i) {
                mOpacity = Drawable.resolveOpacity(mOpacity, mDrawables[i].getOpacity());
            }
            this.mOpacity = mOpacity;
            this.mCheckedOpacity = true;
            return mOpacity;
        }
        
        public void growArray(final int n, final int n2) {
            final Drawable[] mDrawables = new Drawable[n2];
            System.arraycopy(this.mDrawables, 0, mDrawables, 0, n);
            this.mDrawables = mDrawables;
        }
        
        void invalidateCache() {
            this.mCheckedOpacity = false;
            this.mCheckedStateful = false;
        }
        
        public final boolean isConstantSize() {
            return this.mConstantSize;
        }
        
        public final boolean isStateful() {
            if (this.mCheckedStateful) {
                return this.mStateful;
            }
            this.createAllFutures();
            final int mNumChildren = this.mNumChildren;
            final Drawable[] mDrawables = this.mDrawables;
            final boolean b = false;
            int n = 0;
            boolean mStateful;
            while (true) {
                mStateful = b;
                if (n >= mNumChildren) {
                    break;
                }
                if (mDrawables[n].isStateful()) {
                    mStateful = true;
                    break;
                }
                ++n;
            }
            this.mStateful = mStateful;
            this.mCheckedStateful = true;
            return mStateful;
        }
        
        void mutate() {
            final int mNumChildren = this.mNumChildren;
            final Drawable[] mDrawables = this.mDrawables;
            for (int i = 0; i < mNumChildren; ++i) {
                if (mDrawables[i] != null) {
                    mDrawables[i].mutate();
                }
            }
            this.mMutated = true;
        }
        
        public final void setConstantSize(final boolean mConstantSize) {
            this.mConstantSize = mConstantSize;
        }
        
        public final void setEnterFadeDuration(final int mEnterFadeDuration) {
            this.mEnterFadeDuration = mEnterFadeDuration;
        }
        
        public final void setExitFadeDuration(final int mExitFadeDuration) {
            this.mExitFadeDuration = mExitFadeDuration;
        }
        
        final boolean setLayoutDirection(final int n, final int n2) {
            boolean b = false;
            final int mNumChildren = this.mNumChildren;
            final Drawable[] mDrawables = this.mDrawables;
            boolean b2;
            for (int i = 0; i < mNumChildren; ++i, b = b2) {
                b2 = b;
                if (mDrawables[i] != null) {
                    boolean setLayoutDirection = false;
                    if (Build$VERSION.SDK_INT >= 23) {
                        setLayoutDirection = mDrawables[i].setLayoutDirection(n);
                    }
                    b2 = b;
                    if (i == n2) {
                        b2 = setLayoutDirection;
                    }
                }
            }
            this.mLayoutDirection = n;
            return b;
        }
        
        public final void setVariablePadding(final boolean mVariablePadding) {
            this.mVariablePadding = mVariablePadding;
        }
        
        final void updateDensity(final Resources mSourceRes) {
            if (mSourceRes != null) {
                this.mSourceRes = mSourceRes;
                if (this.mDensity != (this.mDensity = DrawableContainer.resolveDensity(mSourceRes, this.mDensity))) {
                    this.mCheckedConstantSize = false;
                    this.mCheckedPadding = false;
                }
            }
        }
    }
}
