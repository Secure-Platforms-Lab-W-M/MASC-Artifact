package androidx.fragment.app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;

public class DialogFragment extends Fragment implements OnCancelListener, OnDismissListener {
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
   private Runnable mDismissRunnable = new Runnable() {
      public void run() {
         DialogFragment.this.mOnDismissListener.onDismiss(DialogFragment.this.mDialog);
      }
   };
   boolean mDismissed;
   private Handler mHandler;
   OnCancelListener mOnCancelListener = new OnCancelListener() {
      public void onCancel(DialogInterface var1) {
         if (DialogFragment.this.mDialog != null) {
            DialogFragment var2 = DialogFragment.this;
            var2.onCancel(var2.mDialog);
         }

      }
   };
   OnDismissListener mOnDismissListener = new OnDismissListener() {
      public void onDismiss(DialogInterface var1) {
         if (DialogFragment.this.mDialog != null) {
            DialogFragment var2 = DialogFragment.this;
            var2.onDismiss(var2.mDialog);
         }

      }
   };
   boolean mShownByMe;
   boolean mShowsDialog = true;
   int mStyle = 0;
   int mTheme = 0;
   boolean mViewDestroyed;

   public void dismiss() {
      this.dismissInternal(false, false);
   }

   public void dismissAllowingStateLoss() {
      this.dismissInternal(true, false);
   }

   void dismissInternal(boolean var1, boolean var2) {
      if (!this.mDismissed) {
         this.mDismissed = true;
         this.mShownByMe = false;
         Dialog var3 = this.mDialog;
         if (var3 != null) {
            var3.setOnDismissListener((OnDismissListener)null);
            this.mDialog.dismiss();
            if (!var2) {
               if (Looper.myLooper() == this.mHandler.getLooper()) {
                  this.onDismiss(this.mDialog);
               } else {
                  this.mHandler.post(this.mDismissRunnable);
               }
            }
         }

         this.mViewDestroyed = true;
         if (this.mBackStackId >= 0) {
            this.getParentFragmentManager().popBackStack(this.mBackStackId, 1);
            this.mBackStackId = -1;
         } else {
            FragmentTransaction var4 = this.getParentFragmentManager().beginTransaction();
            var4.remove(this);
            if (var1) {
               var4.commitAllowingStateLoss();
            } else {
               var4.commit();
            }
         }
      }
   }

   public Dialog getDialog() {
      return this.mDialog;
   }

   public boolean getShowsDialog() {
      return this.mShowsDialog;
   }

   public int getTheme() {
      return this.mTheme;
   }

   public boolean isCancelable() {
      return this.mCancelable;
   }

   public void onActivityCreated(Bundle var1) {
      super.onActivityCreated(var1);
      if (this.mShowsDialog) {
         View var2 = this.getView();
         if (var2 != null) {
            if (var2.getParent() != null) {
               throw new IllegalStateException("DialogFragment can not be attached to a container view");
            }

            this.mDialog.setContentView(var2);
         }

         FragmentActivity var3 = this.getActivity();
         if (var3 != null) {
            this.mDialog.setOwnerActivity(var3);
         }

         this.mDialog.setCancelable(this.mCancelable);
         this.mDialog.setOnCancelListener(this.mOnCancelListener);
         this.mDialog.setOnDismissListener(this.mOnDismissListener);
         if (var1 != null) {
            var1 = var1.getBundle("android:savedDialogState");
            if (var1 != null) {
               this.mDialog.onRestoreInstanceState(var1);
            }
         }

      }
   }

   public void onAttach(Context var1) {
      super.onAttach(var1);
      if (!this.mShownByMe) {
         this.mDismissed = false;
      }

   }

   public void onCancel(DialogInterface var1) {
   }

   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.mHandler = new Handler();
      boolean var2;
      if (this.mContainerId == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      this.mShowsDialog = var2;
      if (var1 != null) {
         this.mStyle = var1.getInt("android:style", 0);
         this.mTheme = var1.getInt("android:theme", 0);
         this.mCancelable = var1.getBoolean("android:cancelable", true);
         this.mShowsDialog = var1.getBoolean("android:showsDialog", this.mShowsDialog);
         this.mBackStackId = var1.getInt("android:backStackId", -1);
      }

   }

   public Dialog onCreateDialog(Bundle var1) {
      return new Dialog(this.requireContext(), this.getTheme());
   }

   public void onDestroyView() {
      super.onDestroyView();
      Dialog var1 = this.mDialog;
      if (var1 != null) {
         this.mViewDestroyed = true;
         var1.setOnDismissListener((OnDismissListener)null);
         this.mDialog.dismiss();
         if (!this.mDismissed) {
            this.onDismiss(this.mDialog);
         }

         this.mDialog = null;
      }

   }

   public void onDetach() {
      super.onDetach();
      if (!this.mShownByMe && !this.mDismissed) {
         this.mDismissed = true;
      }

   }

   public void onDismiss(DialogInterface var1) {
      if (!this.mViewDestroyed) {
         this.dismissInternal(true, true);
      }

   }

   public LayoutInflater onGetLayoutInflater(Bundle var1) {
      if (!this.mShowsDialog) {
         return super.onGetLayoutInflater(var1);
      } else {
         Dialog var2 = this.onCreateDialog(var1);
         this.mDialog = var2;
         if (var2 != null) {
            this.setupDialog(var2, this.mStyle);
            return (LayoutInflater)this.mDialog.getContext().getSystemService("layout_inflater");
         } else {
            return (LayoutInflater)this.mHost.getContext().getSystemService("layout_inflater");
         }
      }
   }

   public void onSaveInstanceState(Bundle var1) {
      super.onSaveInstanceState(var1);
      Dialog var4 = this.mDialog;
      if (var4 != null) {
         Bundle var5 = var4.onSaveInstanceState();
         if (var5 != null) {
            var1.putBundle("android:savedDialogState", var5);
         }
      }

      int var2 = this.mStyle;
      if (var2 != 0) {
         var1.putInt("android:style", var2);
      }

      var2 = this.mTheme;
      if (var2 != 0) {
         var1.putInt("android:theme", var2);
      }

      boolean var3 = this.mCancelable;
      if (!var3) {
         var1.putBoolean("android:cancelable", var3);
      }

      var3 = this.mShowsDialog;
      if (!var3) {
         var1.putBoolean("android:showsDialog", var3);
      }

      var2 = this.mBackStackId;
      if (var2 != -1) {
         var1.putInt("android:backStackId", var2);
      }

   }

   public void onStart() {
      super.onStart();
      Dialog var1 = this.mDialog;
      if (var1 != null) {
         this.mViewDestroyed = false;
         var1.show();
      }

   }

   public void onStop() {
      super.onStop();
      Dialog var1 = this.mDialog;
      if (var1 != null) {
         var1.hide();
      }

   }

   public final Dialog requireDialog() {
      Dialog var1 = this.getDialog();
      if (var1 != null) {
         return var1;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("DialogFragment ");
         var2.append(this);
         var2.append(" does not have a Dialog.");
         throw new IllegalStateException(var2.toString());
      }
   }

   public void setCancelable(boolean var1) {
      this.mCancelable = var1;
      Dialog var2 = this.mDialog;
      if (var2 != null) {
         var2.setCancelable(var1);
      }

   }

   public void setShowsDialog(boolean var1) {
      this.mShowsDialog = var1;
   }

   public void setStyle(int var1, int var2) {
      this.mStyle = var1;
      if (var1 == 2 || var1 == 3) {
         this.mTheme = 16973913;
      }

      if (var2 != 0) {
         this.mTheme = var2;
      }

   }

   public void setupDialog(Dialog var1, int var2) {
      if (var2 != 1 && var2 != 2) {
         if (var2 != 3) {
            return;
         }

         var1.getWindow().addFlags(24);
      }

      var1.requestWindowFeature(1);
   }

   public int show(FragmentTransaction var1, String var2) {
      this.mDismissed = false;
      this.mShownByMe = true;
      var1.add(this, var2);
      this.mViewDestroyed = false;
      int var3 = var1.commit();
      this.mBackStackId = var3;
      return var3;
   }

   public void show(FragmentManager var1, String var2) {
      this.mDismissed = false;
      this.mShownByMe = true;
      FragmentTransaction var3 = var1.beginTransaction();
      var3.add(this, var2);
      var3.commit();
   }

   public void showNow(FragmentManager var1, String var2) {
      this.mDismissed = false;
      this.mShownByMe = true;
      FragmentTransaction var3 = var1.beginTransaction();
      var3.add(this, var2);
      var3.commitNow();
   }
}
