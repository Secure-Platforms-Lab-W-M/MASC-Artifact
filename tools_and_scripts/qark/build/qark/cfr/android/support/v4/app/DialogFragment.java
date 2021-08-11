/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.Dialog
 *  android.content.Context
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnCancelListener
 *  android.content.DialogInterface$OnDismissListener
 *  android.os.Bundle
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewParent
 *  android.view.Window
 */
package android.support.v4.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentHostCallback;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.view.Window;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DialogFragment
extends Fragment
implements DialogInterface.OnCancelListener,
DialogInterface.OnDismissListener {
    private static final String SAVED_BACK_STACK_ID = "android:backStackId";
    private static final String SAVED_CANCELABLE = "android:cancelable";
    private static final String SAVED_DIALOG_STATE_TAG = "android:savedDialogState";
    private static final String SAVED_SHOWS_DIALOG = "android:showsDialog";
    private static final String SAVED_STYLE = "android:style";
    private static final String SAVED_THEME = "android:theme";
    public static final int STYLE_NORMAL = 0;
    public static final int STYLE_NO_FRAME = 2;
    public static final int STYLE_NO_INPUT = 3;
    public static final int STYLE_NO_TITLE = 1;
    int mBackStackId = -1;
    boolean mCancelable = true;
    Dialog mDialog;
    boolean mDismissed;
    boolean mShownByMe;
    boolean mShowsDialog = true;
    int mStyle = 0;
    int mTheme = 0;
    boolean mViewDestroyed;

    public void dismiss() {
        this.dismissInternal(false);
    }

    public void dismissAllowingStateLoss() {
        this.dismissInternal(true);
    }

    void dismissInternal(boolean bl) {
        if (this.mDismissed) {
            return;
        }
        this.mDismissed = true;
        this.mShownByMe = false;
        Object object = this.mDialog;
        if (object != null) {
            object.dismiss();
            this.mDialog = null;
        }
        this.mViewDestroyed = true;
        if (this.mBackStackId >= 0) {
            this.getFragmentManager().popBackStack(this.mBackStackId, 1);
            this.mBackStackId = -1;
            return;
        }
        object = this.getFragmentManager().beginTransaction();
        object.remove(this);
        if (bl) {
            object.commitAllowingStateLoss();
            return;
        }
        object.commit();
    }

    public Dialog getDialog() {
        return this.mDialog;
    }

    public boolean getShowsDialog() {
        return this.mShowsDialog;
    }

    @StyleRes
    public int getTheme() {
        return this.mTheme;
    }

    public boolean isCancelable() {
        return this.mCancelable;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (!this.mShowsDialog) {
            return;
        }
        Object object = this.getView();
        if (object != null) {
            if (object.getParent() == null) {
                this.mDialog.setContentView((View)object);
            } else {
                throw new IllegalStateException("DialogFragment can not be attached to a container view");
            }
        }
        if ((object = this.getActivity()) != null) {
            this.mDialog.setOwnerActivity((Activity)object);
        }
        this.mDialog.setCancelable(this.mCancelable);
        this.mDialog.setOnCancelListener((DialogInterface.OnCancelListener)this);
        this.mDialog.setOnDismissListener((DialogInterface.OnDismissListener)this);
        if (bundle != null) {
            if ((bundle = bundle.getBundle("android:savedDialogState")) != null) {
                this.mDialog.onRestoreInstanceState(bundle);
                return;
            }
            return;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!this.mShownByMe) {
            this.mDismissed = false;
            return;
        }
    }

    public void onCancel(DialogInterface dialogInterface) {
    }

    @Override
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        boolean bl = this.mContainerId == 0;
        this.mShowsDialog = bl;
        if (bundle != null) {
            this.mStyle = bundle.getInt("android:style", 0);
            this.mTheme = bundle.getInt("android:theme", 0);
            this.mCancelable = bundle.getBoolean("android:cancelable", true);
            this.mShowsDialog = bundle.getBoolean("android:showsDialog", this.mShowsDialog);
            this.mBackStackId = bundle.getInt("android:backStackId", -1);
            return;
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle bundle) {
        return new Dialog((Context)this.getActivity(), this.getTheme());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Dialog dialog = this.mDialog;
        if (dialog != null) {
            this.mViewDestroyed = true;
            dialog.dismiss();
            this.mDialog = null;
            return;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (!this.mShownByMe && !this.mDismissed) {
            this.mDismissed = true;
            return;
        }
    }

    public void onDismiss(DialogInterface dialogInterface) {
        if (!this.mViewDestroyed) {
            this.dismissInternal(true);
            return;
        }
    }

    @Override
    public LayoutInflater onGetLayoutInflater(Bundle bundle) {
        if (!this.mShowsDialog) {
            return super.onGetLayoutInflater(bundle);
        }
        this.mDialog = this.onCreateDialog(bundle);
        bundle = this.mDialog;
        if (bundle != null) {
            this.setupDialog((Dialog)bundle, this.mStyle);
            return (LayoutInflater)this.mDialog.getContext().getSystemService("layout_inflater");
        }
        return (LayoutInflater)this.mHost.getContext().getSystemService("layout_inflater");
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        boolean bl;
        int n;
        super.onSaveInstanceState(bundle);
        Dialog dialog = this.mDialog;
        if (dialog != null && (dialog = dialog.onSaveInstanceState()) != null) {
            bundle.putBundle("android:savedDialogState", (Bundle)dialog);
        }
        if ((n = this.mStyle) != 0) {
            bundle.putInt("android:style", n);
        }
        if ((n = this.mTheme) != 0) {
            bundle.putInt("android:theme", n);
        }
        if (!(bl = this.mCancelable)) {
            bundle.putBoolean("android:cancelable", bl);
        }
        if (!(bl = this.mShowsDialog)) {
            bundle.putBoolean("android:showsDialog", bl);
        }
        if ((n = this.mBackStackId) != -1) {
            bundle.putInt("android:backStackId", n);
            return;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = this.mDialog;
        if (dialog != null) {
            this.mViewDestroyed = false;
            dialog.show();
            return;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Dialog dialog = this.mDialog;
        if (dialog != null) {
            dialog.hide();
            return;
        }
    }

    public void setCancelable(boolean bl) {
        this.mCancelable = bl;
        Dialog dialog = this.mDialog;
        if (dialog != null) {
            dialog.setCancelable(bl);
        }
    }

    public void setShowsDialog(boolean bl) {
        this.mShowsDialog = bl;
    }

    public void setStyle(int n, @StyleRes int n2) {
        this.mStyle = n;
        if ((n = this.mStyle) == 2 || n == 3) {
            this.mTheme = 16973913;
        }
        if (n2 != 0) {
            this.mTheme = n2;
            return;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public void setupDialog(Dialog var1_1, int var2_2) {
        switch (var2_2) {
            default: {
                return;
            }
            case 3: {
                var1_1.getWindow().addFlags(24);
                ** break;
            }
lbl7: // 2 sources:
            case 1: 
            case 2: 
        }
        var1_1.requestWindowFeature(1);
    }

    public int show(FragmentTransaction fragmentTransaction, String string2) {
        this.mDismissed = false;
        this.mShownByMe = true;
        fragmentTransaction.add(this, string2);
        this.mViewDestroyed = false;
        this.mBackStackId = fragmentTransaction.commit();
        return this.mBackStackId;
    }

    public void show(FragmentManager object, String string2) {
        this.mDismissed = false;
        this.mShownByMe = true;
        object = object.beginTransaction();
        object.add(this, string2);
        object.commit();
    }

    @Retention(value=RetentionPolicy.SOURCE)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    private static @interface DialogStyle {
    }

}

