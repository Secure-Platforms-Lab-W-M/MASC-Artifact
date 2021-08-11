// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.app;

import android.support.v4.view.ViewCompat;
import android.support.annotation.NonNull;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable$Callback;
import android.graphics.drawable.InsetDrawable;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.lang.reflect.Method;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.content.res.Configuration;
import android.util.Log;
import android.content.res.TypedArray;
import android.app.ActionBar;
import android.util.AttributeSet;
import android.os.Build$VERSION;
import android.support.v4.content.ContextCompat;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.annotation.DrawableRes;
import android.graphics.drawable.Drawable;
import android.app.Activity;
import android.support.v4.widget.DrawerLayout;

@Deprecated
public class ActionBarDrawerToggle implements DrawerListener
{
    private static final int ID_HOME = 16908332;
    private static final String TAG = "ActionBarDrawerToggle";
    private static final int[] THEME_ATTRS;
    private static final float TOGGLE_DRAWABLE_OFFSET = 0.33333334f;
    final Activity mActivity;
    private final Delegate mActivityImpl;
    private final int mCloseDrawerContentDescRes;
    private Drawable mDrawerImage;
    private final int mDrawerImageResource;
    private boolean mDrawerIndicatorEnabled;
    private final DrawerLayout mDrawerLayout;
    private boolean mHasCustomUpIndicator;
    private Drawable mHomeAsUpIndicator;
    private final int mOpenDrawerContentDescRes;
    private SetIndicatorInfo mSetIndicatorInfo;
    private SlideDrawable mSlider;
    
    static {
        THEME_ATTRS = new int[] { 16843531 };
    }
    
    public ActionBarDrawerToggle(final Activity activity, final DrawerLayout drawerLayout, @DrawableRes final int n, @StringRes final int n2, @StringRes final int n3) {
        this(activity, drawerLayout, assumeMaterial((Context)activity) ^ true, n, n2, n3);
    }
    
    public ActionBarDrawerToggle(final Activity mActivity, final DrawerLayout mDrawerLayout, final boolean b, @DrawableRes final int mDrawerImageResource, @StringRes final int mOpenDrawerContentDescRes, @StringRes final int mCloseDrawerContentDescRes) {
        this.mDrawerIndicatorEnabled = true;
        this.mActivity = mActivity;
        if (mActivity instanceof DelegateProvider) {
            this.mActivityImpl = ((DelegateProvider)mActivity).getDrawerToggleDelegate();
        }
        else {
            this.mActivityImpl = null;
        }
        this.mDrawerLayout = mDrawerLayout;
        this.mDrawerImageResource = mDrawerImageResource;
        this.mOpenDrawerContentDescRes = mOpenDrawerContentDescRes;
        this.mCloseDrawerContentDescRes = mCloseDrawerContentDescRes;
        this.mHomeAsUpIndicator = this.getThemeUpIndicator();
        this.mDrawerImage = ContextCompat.getDrawable((Context)mActivity, mDrawerImageResource);
        this.mSlider = new SlideDrawable(this.mDrawerImage);
        final SlideDrawable mSlider = this.mSlider;
        float offset;
        if (b) {
            offset = 0.33333334f;
        }
        else {
            offset = 0.0f;
        }
        mSlider.setOffset(offset);
    }
    
    private static boolean assumeMaterial(final Context context) {
        return context.getApplicationInfo().targetSdkVersion >= 21 && Build$VERSION.SDK_INT >= 21;
    }
    
    private Drawable getThemeUpIndicator() {
        final Delegate mActivityImpl = this.mActivityImpl;
        if (mActivityImpl != null) {
            return mActivityImpl.getThemeUpIndicator();
        }
        if (Build$VERSION.SDK_INT >= 18) {
            final ActionBar actionBar = this.mActivity.getActionBar();
            Object o;
            if (actionBar != null) {
                o = actionBar.getThemedContext();
            }
            else {
                o = this.mActivity;
            }
            final TypedArray obtainStyledAttributes = ((Context)o).obtainStyledAttributes((AttributeSet)null, ActionBarDrawerToggle.THEME_ATTRS, 16843470, 0);
            final Drawable drawable = obtainStyledAttributes.getDrawable(0);
            obtainStyledAttributes.recycle();
            return drawable;
        }
        final TypedArray obtainStyledAttributes2 = this.mActivity.obtainStyledAttributes(ActionBarDrawerToggle.THEME_ATTRS);
        final Drawable drawable2 = obtainStyledAttributes2.getDrawable(0);
        obtainStyledAttributes2.recycle();
        return drawable2;
    }
    
    private void setActionBarDescription(final int n) {
        final Delegate mActivityImpl = this.mActivityImpl;
        if (mActivityImpl != null) {
            mActivityImpl.setActionBarDescription(n);
            return;
        }
        if (Build$VERSION.SDK_INT >= 18) {
            final ActionBar actionBar = this.mActivity.getActionBar();
            if (actionBar != null) {
                actionBar.setHomeActionContentDescription(n);
            }
            return;
        }
        if (this.mSetIndicatorInfo == null) {
            this.mSetIndicatorInfo = new SetIndicatorInfo(this.mActivity);
        }
        if (this.mSetIndicatorInfo.mSetHomeAsUpIndicator != null) {
            try {
                final ActionBar actionBar2 = this.mActivity.getActionBar();
                this.mSetIndicatorInfo.mSetHomeActionContentDescription.invoke(actionBar2, n);
                actionBar2.setSubtitle(actionBar2.getSubtitle());
            }
            catch (Exception ex) {
                Log.w("ActionBarDrawerToggle", "Couldn't set content description via JB-MR2 API", (Throwable)ex);
            }
        }
    }
    
    private void setActionBarUpIndicator(final Drawable drawable, final int homeActionContentDescription) {
        final Delegate mActivityImpl = this.mActivityImpl;
        if (mActivityImpl != null) {
            mActivityImpl.setActionBarUpIndicator(drawable, homeActionContentDescription);
            return;
        }
        if (Build$VERSION.SDK_INT >= 18) {
            final ActionBar actionBar = this.mActivity.getActionBar();
            if (actionBar != null) {
                actionBar.setHomeAsUpIndicator(drawable);
                actionBar.setHomeActionContentDescription(homeActionContentDescription);
            }
            return;
        }
        if (this.mSetIndicatorInfo == null) {
            this.mSetIndicatorInfo = new SetIndicatorInfo(this.mActivity);
        }
        if (this.mSetIndicatorInfo.mSetHomeAsUpIndicator != null) {
            try {
                final ActionBar actionBar2 = this.mActivity.getActionBar();
                this.mSetIndicatorInfo.mSetHomeAsUpIndicator.invoke(actionBar2, drawable);
                this.mSetIndicatorInfo.mSetHomeActionContentDescription.invoke(actionBar2, homeActionContentDescription);
                return;
            }
            catch (Exception ex) {
                Log.w("ActionBarDrawerToggle", "Couldn't set home-as-up indicator via JB-MR2 API", (Throwable)ex);
                return;
            }
        }
        if (this.mSetIndicatorInfo.mUpIndicatorView != null) {
            this.mSetIndicatorInfo.mUpIndicatorView.setImageDrawable(drawable);
            return;
        }
        Log.w("ActionBarDrawerToggle", "Couldn't set home-as-up indicator");
    }
    
    public boolean isDrawerIndicatorEnabled() {
        return this.mDrawerIndicatorEnabled;
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        if (!this.mHasCustomUpIndicator) {
            this.mHomeAsUpIndicator = this.getThemeUpIndicator();
        }
        this.mDrawerImage = ContextCompat.getDrawable((Context)this.mActivity, this.mDrawerImageResource);
        this.syncState();
    }
    
    @Override
    public void onDrawerClosed(final View view) {
        this.mSlider.setPosition(0.0f);
        if (this.mDrawerIndicatorEnabled) {
            this.setActionBarDescription(this.mOpenDrawerContentDescRes);
        }
    }
    
    @Override
    public void onDrawerOpened(final View view) {
        this.mSlider.setPosition(1.0f);
        if (this.mDrawerIndicatorEnabled) {
            this.setActionBarDescription(this.mCloseDrawerContentDescRes);
        }
    }
    
    @Override
    public void onDrawerSlide(final View view, float position) {
        final float position2 = this.mSlider.getPosition();
        if (position > 0.5f) {
            position = Math.max(position2, Math.max(0.0f, position - 0.5f) * 2.0f);
        }
        else {
            position = Math.min(position2, 2.0f * position);
        }
        this.mSlider.setPosition(position);
    }
    
    @Override
    public void onDrawerStateChanged(final int n) {
    }
    
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        if (menuItem != null && menuItem.getItemId() == 16908332 && this.mDrawerIndicatorEnabled) {
            if (this.mDrawerLayout.isDrawerVisible(8388611)) {
                this.mDrawerLayout.closeDrawer(8388611);
            }
            else {
                this.mDrawerLayout.openDrawer(8388611);
            }
            return true;
        }
        return false;
    }
    
    public void setDrawerIndicatorEnabled(final boolean mDrawerIndicatorEnabled) {
        if (mDrawerIndicatorEnabled != this.mDrawerIndicatorEnabled) {
            if (mDrawerIndicatorEnabled) {
                final SlideDrawable mSlider = this.mSlider;
                int n;
                if (this.mDrawerLayout.isDrawerOpen(8388611)) {
                    n = this.mCloseDrawerContentDescRes;
                }
                else {
                    n = this.mOpenDrawerContentDescRes;
                }
                this.setActionBarUpIndicator((Drawable)mSlider, n);
            }
            else {
                this.setActionBarUpIndicator(this.mHomeAsUpIndicator, 0);
            }
            this.mDrawerIndicatorEnabled = mDrawerIndicatorEnabled;
        }
    }
    
    public void setHomeAsUpIndicator(final int n) {
        Drawable drawable = null;
        if (n != 0) {
            drawable = ContextCompat.getDrawable((Context)this.mActivity, n);
        }
        this.setHomeAsUpIndicator(drawable);
    }
    
    public void setHomeAsUpIndicator(final Drawable mHomeAsUpIndicator) {
        if (mHomeAsUpIndicator == null) {
            this.mHomeAsUpIndicator = this.getThemeUpIndicator();
            this.mHasCustomUpIndicator = false;
        }
        else {
            this.mHomeAsUpIndicator = mHomeAsUpIndicator;
            this.mHasCustomUpIndicator = true;
        }
        if (!this.mDrawerIndicatorEnabled) {
            this.setActionBarUpIndicator(this.mHomeAsUpIndicator, 0);
        }
    }
    
    public void syncState() {
        if (this.mDrawerLayout.isDrawerOpen(8388611)) {
            this.mSlider.setPosition(1.0f);
        }
        else {
            this.mSlider.setPosition(0.0f);
        }
        if (this.mDrawerIndicatorEnabled) {
            final SlideDrawable mSlider = this.mSlider;
            int n;
            if (this.mDrawerLayout.isDrawerOpen(8388611)) {
                n = this.mCloseDrawerContentDescRes;
            }
            else {
                n = this.mOpenDrawerContentDescRes;
            }
            this.setActionBarUpIndicator((Drawable)mSlider, n);
        }
    }
    
    @Deprecated
    public interface Delegate
    {
        @Nullable
        Drawable getThemeUpIndicator();
        
        void setActionBarDescription(@StringRes final int p0);
        
        void setActionBarUpIndicator(final Drawable p0, @StringRes final int p1);
    }
    
    @Deprecated
    public interface DelegateProvider
    {
        @Nullable
        Delegate getDrawerToggleDelegate();
    }
    
    private static class SetIndicatorInfo
    {
        Method mSetHomeActionContentDescription;
        Method mSetHomeAsUpIndicator;
        ImageView mUpIndicatorView;
        
        SetIndicatorInfo(final Activity activity) {
            try {
                this.mSetHomeAsUpIndicator = ActionBar.class.getDeclaredMethod("setHomeAsUpIndicator", Drawable.class);
                this.mSetHomeActionContentDescription = ActionBar.class.getDeclaredMethod("setHomeActionContentDescription", Integer.TYPE);
            }
            catch (NoSuchMethodException ex) {
                final View viewById = activity.findViewById(16908332);
                if (viewById == null) {
                    return;
                }
                final ViewGroup viewGroup = (ViewGroup)viewById.getParent();
                if (viewGroup.getChildCount() != 2) {
                    return;
                }
                View child = viewGroup.getChildAt(0);
                final View child2 = viewGroup.getChildAt(1);
                if (child.getId() == 16908332) {
                    child = child2;
                }
                if (child instanceof ImageView) {
                    this.mUpIndicatorView = (ImageView)child;
                }
            }
        }
    }
    
    private class SlideDrawable extends InsetDrawable implements Drawable$Callback
    {
        private final boolean mHasMirroring;
        private float mOffset;
        private float mPosition;
        private final Rect mTmpRect;
        
        SlideDrawable(final Drawable drawable) {
            boolean mHasMirroring = false;
            super(drawable, 0);
            if (Build$VERSION.SDK_INT > 18) {
                mHasMirroring = true;
            }
            this.mHasMirroring = mHasMirroring;
            this.mTmpRect = new Rect();
        }
        
        public void draw(@NonNull final Canvas canvas) {
            this.copyBounds(this.mTmpRect);
            canvas.save();
            final int layoutDirection = ViewCompat.getLayoutDirection(ActionBarDrawerToggle.this.mActivity.getWindow().getDecorView());
            int n = 1;
            final boolean b = layoutDirection == 1;
            if (b) {
                n = -1;
            }
            final int width = this.mTmpRect.width();
            canvas.translate(-this.mOffset * width * this.mPosition * n, 0.0f);
            if (b && !this.mHasMirroring) {
                canvas.translate((float)width, 0.0f);
                canvas.scale(-1.0f, 1.0f);
            }
            super.draw(canvas);
            canvas.restore();
        }
        
        public float getPosition() {
            return this.mPosition;
        }
        
        public void setOffset(final float mOffset) {
            this.mOffset = mOffset;
            this.invalidateSelf();
        }
        
        public void setPosition(final float mPosition) {
            this.mPosition = mPosition;
            this.invalidateSelf();
        }
    }
}
