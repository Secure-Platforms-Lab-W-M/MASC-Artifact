/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.ContextWrapper
 *  android.content.res.Resources
 *  android.graphics.Rect
 *  android.util.DisplayMetrics
 *  android.util.Log
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewParent
 *  android.view.Window
 *  android.view.WindowManager
 *  android.view.WindowManager$LayoutParams
 *  android.widget.TextView
 */
package android.support.v7.widget;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.annotation.RestrictTo;
import android.support.v7.appcompat.R;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

@RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
class TooltipPopup {
    private static final String TAG = "TooltipPopup";
    private final View mContentView;
    private final Context mContext;
    private final WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams();
    private final TextView mMessageView;
    private final int[] mTmpAnchorPos = new int[2];
    private final int[] mTmpAppPos = new int[2];
    private final Rect mTmpDisplayFrame = new Rect();

    TooltipPopup(Context context) {
        this.mContext = context;
        this.mContentView = LayoutInflater.from((Context)this.mContext).inflate(R.layout.tooltip, null);
        this.mMessageView = (TextView)this.mContentView.findViewById(R.id.message);
        this.mLayoutParams.setTitle((CharSequence)this.getClass().getSimpleName());
        this.mLayoutParams.packageName = this.mContext.getPackageName();
        context = this.mLayoutParams;
        context.type = 1002;
        context.width = -2;
        context.height = -2;
        context.format = -3;
        context.windowAnimations = R.style.Animation_AppCompat_Tooltip;
        this.mLayoutParams.flags = 24;
    }

    private void computePosition(View arrn, int n, int n2, boolean bl, WindowManager.LayoutParams layoutParams) {
        int n3;
        int n4 = this.mContext.getResources().getDimensionPixelOffset(R.dimen.tooltip_precise_anchor_threshold);
        if (arrn.getWidth() < n4) {
            n = arrn.getWidth() / 2;
        }
        if (arrn.getHeight() >= n4) {
            n4 = this.mContext.getResources().getDimensionPixelOffset(R.dimen.tooltip_precise_anchor_extra_offset);
            n3 = n2 + n4;
            n4 = n2 - n4;
            n2 = n3;
        } else {
            n2 = arrn.getHeight();
            n4 = 0;
        }
        layoutParams.gravity = 49;
        int[] arrn2 = this.mContext.getResources();
        n3 = bl ? R.dimen.tooltip_y_offset_touch : R.dimen.tooltip_y_offset_non_touch;
        int n5 = arrn2.getDimensionPixelOffset(n3);
        arrn2 = TooltipPopup.getAppRootView((View)arrn);
        if (arrn2 == null) {
            Log.e((String)"TooltipPopup", (String)"Cannot find app view");
            return;
        }
        arrn2.getWindowVisibleDisplayFrame(this.mTmpDisplayFrame);
        if (this.mTmpDisplayFrame.left < 0 && this.mTmpDisplayFrame.top < 0) {
            Resources resources = this.mContext.getResources();
            n3 = resources.getIdentifier("status_bar_height", "dimen", "android");
            n3 = n3 != 0 ? resources.getDimensionPixelSize(n3) : 0;
            resources = resources.getDisplayMetrics();
            this.mTmpDisplayFrame.set(0, n3, resources.widthPixels, resources.heightPixels);
        }
        arrn2.getLocationOnScreen(this.mTmpAppPos);
        arrn.getLocationOnScreen(this.mTmpAnchorPos);
        arrn = this.mTmpAnchorPos;
        n3 = arrn[0];
        arrn2 = this.mTmpAppPos;
        arrn[0] = n3 - arrn2[0];
        arrn[1] = arrn[1] - arrn2[1];
        layoutParams.x = arrn[0] + n - this.mTmpDisplayFrame.width() / 2;
        n = View.MeasureSpec.makeMeasureSpec((int)0, (int)0);
        this.mContentView.measure(n, n);
        n = this.mContentView.getMeasuredHeight();
        arrn = this.mTmpAnchorPos;
        n4 = arrn[1] + n4 - n5 - n;
        n2 = arrn[1] + n2 + n5;
        if (bl) {
            if (n4 >= 0) {
                layoutParams.y = n4;
                return;
            }
            layoutParams.y = n2;
            return;
        }
        if (n2 + n <= this.mTmpDisplayFrame.height()) {
            layoutParams.y = n2;
            return;
        }
        layoutParams.y = n4;
    }

    private static View getAppRootView(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return ((Activity)context).getWindow().getDecorView();
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return view.getRootView();
    }

    void hide() {
        if (!this.isShowing()) {
            return;
        }
        ((WindowManager)this.mContext.getSystemService("window")).removeView(this.mContentView);
    }

    boolean isShowing() {
        if (this.mContentView.getParent() != null) {
            return true;
        }
        return false;
    }

    void show(View view, int n, int n2, boolean bl, CharSequence charSequence) {
        if (this.isShowing()) {
            this.hide();
        }
        this.mMessageView.setText(charSequence);
        this.computePosition(view, n, n2, bl, this.mLayoutParams);
        ((WindowManager)this.mContext.getSystemService("window")).addView(this.mContentView, (ViewGroup.LayoutParams)this.mLayoutParams);
    }
}

