/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.ContextWrapper
 *  android.content.res.Resources
 *  android.graphics.Rect
 *  android.os.IBinder
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
 *  androidx.appcompat.R
 *  androidx.appcompat.R$dimen
 *  androidx.appcompat.R$id
 *  androidx.appcompat.R$layout
 *  androidx.appcompat.R$style
 */
package androidx.appcompat.widget;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.appcompat.R;

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
        context = LayoutInflater.from((Context)context).inflate(R.layout.abc_tooltip, null);
        this.mContentView = context;
        this.mMessageView = (TextView)context.findViewById(R.id.message);
        this.mLayoutParams.setTitle((CharSequence)this.getClass().getSimpleName());
        this.mLayoutParams.packageName = this.mContext.getPackageName();
        this.mLayoutParams.type = 1002;
        this.mLayoutParams.width = -2;
        this.mLayoutParams.height = -2;
        this.mLayoutParams.format = -3;
        this.mLayoutParams.windowAnimations = R.style.Animation_AppCompat_Tooltip;
        this.mLayoutParams.flags = 24;
    }

    private void computePosition(View arrn, int n, int n2, boolean bl, WindowManager.LayoutParams layoutParams) {
        DisplayMetrics displayMetrics;
        int n3;
        layoutParams.token = arrn.getApplicationWindowToken();
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
        Resources resources = this.mContext.getResources();
        n3 = bl ? R.dimen.tooltip_y_offset_touch : R.dimen.tooltip_y_offset_non_touch;
        int n5 = resources.getDimensionPixelOffset(n3);
        resources = TooltipPopup.getAppRootView((View)arrn);
        if (resources == null) {
            Log.e((String)"TooltipPopup", (String)"Cannot find app view");
            return;
        }
        resources.getWindowVisibleDisplayFrame(this.mTmpDisplayFrame);
        if (this.mTmpDisplayFrame.left < 0 && this.mTmpDisplayFrame.top < 0) {
            displayMetrics = this.mContext.getResources();
            n3 = displayMetrics.getIdentifier("status_bar_height", "dimen", "android");
            n3 = n3 != 0 ? displayMetrics.getDimensionPixelSize(n3) : 0;
            displayMetrics = displayMetrics.getDisplayMetrics();
            this.mTmpDisplayFrame.set(0, n3, displayMetrics.widthPixels, displayMetrics.heightPixels);
        }
        resources.getLocationOnScreen(this.mTmpAppPos);
        arrn.getLocationOnScreen(this.mTmpAnchorPos);
        arrn = this.mTmpAnchorPos;
        n3 = arrn[0];
        displayMetrics = this.mTmpAppPos;
        arrn[0] = n3 - displayMetrics[0];
        arrn[1] = arrn[1] - displayMetrics[1];
        layoutParams.x = arrn[0] + n - resources.getWidth() / 2;
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
        View view2 = view.getRootView();
        ViewGroup.LayoutParams layoutParams = view2.getLayoutParams();
        if (layoutParams instanceof WindowManager.LayoutParams && ((WindowManager.LayoutParams)layoutParams).type == 2) {
            return view2;
        }
        view = view.getContext();
        while (view instanceof ContextWrapper) {
            if (view instanceof Activity) {
                return ((Activity)view).getWindow().getDecorView();
            }
            view = ((ContextWrapper)view).getBaseContext();
        }
        return view2;
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

