/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.text.TextUtils
 *  android.util.AttributeSet
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewParent
 *  android.widget.Button
 *  android.widget.FrameLayout
 *  android.widget.TextView
 */
package android.support.design.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringRes;
import android.support.design.R;
import android.support.design.internal.SnackbarContentLayout;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public final class Snackbar
extends BaseTransientBottomBar<Snackbar> {
    public static final int LENGTH_INDEFINITE = -2;
    public static final int LENGTH_LONG = 0;
    public static final int LENGTH_SHORT = -1;
    @Nullable
    private BaseTransientBottomBar.BaseCallback<Snackbar> mCallback;

    private Snackbar(ViewGroup viewGroup, View view, BaseTransientBottomBar.ContentViewCallback contentViewCallback) {
        super(viewGroup, view, contentViewCallback);
    }

    private static ViewGroup findSuitableParent(View view) {
        ViewGroup viewGroup = null;
        do {
            if (view instanceof CoordinatorLayout) {
                return (ViewGroup)view;
            }
            if (view instanceof FrameLayout) {
                if (view.getId() == 16908290) {
                    return (ViewGroup)view;
                }
                viewGroup = (ViewGroup)view;
            }
            if (view == null || (view = view.getParent()) instanceof View) continue;
            view = null;
        } while (view != null);
        return viewGroup;
    }

    @NonNull
    public static Snackbar make(@NonNull View view, @StringRes int n, int n2) {
        return Snackbar.make(view, view.getResources().getText(n), n2);
    }

    @NonNull
    public static Snackbar make(@NonNull View object, @NonNull CharSequence charSequence, int n) {
        if ((object = Snackbar.findSuitableParent((View)object)) != null) {
            SnackbarContentLayout snackbarContentLayout = (SnackbarContentLayout)LayoutInflater.from((Context)object.getContext()).inflate(R.layout.design_layout_snackbar_include, (ViewGroup)object, false);
            object = new Snackbar((ViewGroup)object, (View)snackbarContentLayout, snackbarContentLayout);
            object.setText(charSequence);
            object.setDuration(n);
            return object;
        }
        throw new IllegalArgumentException("No suitable parent found from the given view. Please provide a valid view.");
    }

    @NonNull
    public Snackbar setAction(@StringRes int n, View.OnClickListener onClickListener) {
        return this.setAction(this.getContext().getText(n), onClickListener);
    }

    @NonNull
    public Snackbar setAction(CharSequence charSequence, final View.OnClickListener onClickListener) {
        Button button = ((SnackbarContentLayout)this.mView.getChildAt(0)).getActionView();
        if (!TextUtils.isEmpty((CharSequence)charSequence) && onClickListener != null) {
            button.setVisibility(0);
            button.setText(charSequence);
            button.setOnClickListener(new View.OnClickListener(){

                public void onClick(View view) {
                    onClickListener.onClick(view);
                    Snackbar.this.dispatchDismiss(1);
                }
            });
            return this;
        }
        button.setVisibility(8);
        button.setOnClickListener(null);
        return this;
    }

    @NonNull
    public Snackbar setActionTextColor(@ColorInt int n) {
        ((SnackbarContentLayout)this.mView.getChildAt(0)).getActionView().setTextColor(n);
        return this;
    }

    @NonNull
    public Snackbar setActionTextColor(ColorStateList colorStateList) {
        ((SnackbarContentLayout)this.mView.getChildAt(0)).getActionView().setTextColor(colorStateList);
        return this;
    }

    @Deprecated
    @NonNull
    public Snackbar setCallback(Callback callback) {
        BaseTransientBottomBar.BaseCallback<Snackbar> baseCallback = this.mCallback;
        if (baseCallback != null) {
            this.removeCallback(baseCallback);
        }
        if (callback != null) {
            this.addCallback(callback);
        }
        this.mCallback = callback;
        return this;
    }

    @NonNull
    public Snackbar setText(@StringRes int n) {
        return this.setText(this.getContext().getText(n));
    }

    @NonNull
    public Snackbar setText(@NonNull CharSequence charSequence) {
        ((SnackbarContentLayout)this.mView.getChildAt(0)).getMessageView().setText(charSequence);
        return this;
    }

    public static class Callback
    extends BaseTransientBottomBar.BaseCallback<Snackbar> {
        public static final int DISMISS_EVENT_ACTION = 1;
        public static final int DISMISS_EVENT_CONSECUTIVE = 4;
        public static final int DISMISS_EVENT_MANUAL = 3;
        public static final int DISMISS_EVENT_SWIPE = 0;
        public static final int DISMISS_EVENT_TIMEOUT = 2;

        @Override
        public void onDismissed(Snackbar snackbar, int n) {
        }

        @Override
        public void onShown(Snackbar snackbar) {
        }
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static final class SnackbarLayout
    extends BaseTransientBottomBar.SnackbarBaseLayout {
        public SnackbarLayout(Context context) {
            super(context);
        }

        public SnackbarLayout(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        protected void onMeasure(int n, int n2) {
            super.onMeasure(n, n2);
            n2 = this.getChildCount();
            int n3 = this.getMeasuredWidth();
            int n4 = this.getPaddingLeft();
            int n5 = this.getPaddingRight();
            for (n = 0; n < n2; ++n) {
                View view = this.getChildAt(n);
                if (view.getLayoutParams().width != -1) continue;
                view.measure(View.MeasureSpec.makeMeasureSpec((int)(n3 - n4 - n5), (int)1073741824), View.MeasureSpec.makeMeasureSpec((int)view.getMeasuredHeight(), (int)1073741824));
            }
        }
    }

}

