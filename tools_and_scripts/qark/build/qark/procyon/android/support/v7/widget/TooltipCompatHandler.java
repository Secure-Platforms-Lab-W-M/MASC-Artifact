// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.widget;

import android.view.accessibility.AccessibilityManager;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.annotation.RestrictTo;
import android.view.View$OnAttachStateChangeListener;
import android.view.View$OnHoverListener;
import android.view.View$OnLongClickListener;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
class TooltipCompatHandler implements View$OnLongClickListener, View$OnHoverListener, View$OnAttachStateChangeListener
{
    private static final long HOVER_HIDE_TIMEOUT_MS = 15000L;
    private static final long HOVER_HIDE_TIMEOUT_SHORT_MS = 3000L;
    private static final long LONG_CLICK_HIDE_TIMEOUT_MS = 2500L;
    private static final String TAG = "TooltipCompatHandler";
    private static TooltipCompatHandler sActiveHandler;
    private final View mAnchor;
    private int mAnchorX;
    private int mAnchorY;
    private boolean mFromTouch;
    private final Runnable mHideRunnable;
    private TooltipPopup mPopup;
    private final Runnable mShowRunnable;
    private final CharSequence mTooltipText;
    
    private TooltipCompatHandler(final View mAnchor, final CharSequence mTooltipText) {
        this.mShowRunnable = new Runnable() {
            @Override
            public void run() {
                TooltipCompatHandler.this.show(false);
            }
        };
        this.mHideRunnable = new Runnable() {
            @Override
            public void run() {
                TooltipCompatHandler.this.hide();
            }
        };
        this.mAnchor = mAnchor;
        this.mTooltipText = mTooltipText;
        this.mAnchor.setOnLongClickListener((View$OnLongClickListener)this);
        this.mAnchor.setOnHoverListener((View$OnHoverListener)this);
    }
    
    private void hide() {
        if (TooltipCompatHandler.sActiveHandler == this) {
            TooltipCompatHandler.sActiveHandler = null;
            final TooltipPopup mPopup = this.mPopup;
            if (mPopup != null) {
                mPopup.hide();
                this.mPopup = null;
                this.mAnchor.removeOnAttachStateChangeListener((View$OnAttachStateChangeListener)this);
            }
            else {
                Log.e("TooltipCompatHandler", "sActiveHandler.mPopup == null");
            }
        }
        this.mAnchor.removeCallbacks(this.mShowRunnable);
        this.mAnchor.removeCallbacks(this.mHideRunnable);
    }
    
    public static void setTooltipText(final View view, final CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            final TooltipCompatHandler sActiveHandler = TooltipCompatHandler.sActiveHandler;
            if (sActiveHandler != null && sActiveHandler.mAnchor == view) {
                sActiveHandler.hide();
            }
            view.setOnLongClickListener((View$OnLongClickListener)null);
            view.setLongClickable(false);
            view.setOnHoverListener((View$OnHoverListener)null);
            return;
        }
        new TooltipCompatHandler(view, charSequence);
    }
    
    private void show(final boolean mFromTouch) {
        if (!ViewCompat.isAttachedToWindow(this.mAnchor)) {
            return;
        }
        final TooltipCompatHandler sActiveHandler = TooltipCompatHandler.sActiveHandler;
        if (sActiveHandler != null) {
            sActiveHandler.hide();
        }
        TooltipCompatHandler.sActiveHandler = this;
        this.mFromTouch = mFromTouch;
        (this.mPopup = new TooltipPopup(this.mAnchor.getContext())).show(this.mAnchor, this.mAnchorX, this.mAnchorY, this.mFromTouch, this.mTooltipText);
        this.mAnchor.addOnAttachStateChangeListener((View$OnAttachStateChangeListener)this);
        long n;
        if (this.mFromTouch) {
            n = 2500L;
        }
        else if ((ViewCompat.getWindowSystemUiVisibility(this.mAnchor) & 0x1) == 0x1) {
            n = 3000L - ViewConfiguration.getLongPressTimeout();
        }
        else {
            n = 15000L - ViewConfiguration.getLongPressTimeout();
        }
        this.mAnchor.removeCallbacks(this.mHideRunnable);
        this.mAnchor.postDelayed(this.mHideRunnable, n);
    }
    
    public boolean onHover(final View view, final MotionEvent motionEvent) {
        if (this.mPopup != null && this.mFromTouch) {
            return false;
        }
        final AccessibilityManager accessibilityManager = (AccessibilityManager)this.mAnchor.getContext().getSystemService("accessibility");
        if (accessibilityManager.isEnabled() && accessibilityManager.isTouchExplorationEnabled()) {
            return false;
        }
        final int action = motionEvent.getAction();
        if (action != 7) {
            if (action != 10) {
                return false;
            }
            this.hide();
            return false;
        }
        else {
            if (this.mAnchor.isEnabled() && this.mPopup == null) {
                this.mAnchorX = (int)motionEvent.getX();
                this.mAnchorY = (int)motionEvent.getY();
                this.mAnchor.removeCallbacks(this.mShowRunnable);
                this.mAnchor.postDelayed(this.mShowRunnable, (long)ViewConfiguration.getLongPressTimeout());
                return false;
            }
            return false;
        }
    }
    
    public boolean onLongClick(final View view) {
        this.mAnchorX = view.getWidth() / 2;
        this.mAnchorY = view.getHeight() / 2;
        this.show(true);
        return true;
    }
    
    public void onViewAttachedToWindow(final View view) {
    }
    
    public void onViewDetachedFromWindow(final View view) {
        this.hide();
    }
}
