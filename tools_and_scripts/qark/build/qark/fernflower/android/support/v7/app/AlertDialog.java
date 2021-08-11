package android.support.v7.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.ArrayRes;
import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v7.appcompat.R$attr;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemSelectedListener;

public class AlertDialog extends AppCompatDialog implements DialogInterface {
   static final int LAYOUT_HINT_NONE = 0;
   static final int LAYOUT_HINT_SIDE = 1;
   final AlertController mAlert;

   protected AlertDialog(@NonNull Context var1) {
      this(var1, 0);
   }

   protected AlertDialog(@NonNull Context var1, @StyleRes int var2) {
      super(var1, resolveDialogTheme(var1, var2));
      this.mAlert = new AlertController(this.getContext(), this, this.getWindow());
   }

   protected AlertDialog(@NonNull Context var1, boolean var2, @Nullable OnCancelListener var3) {
      this(var1, 0);
      this.setCancelable(var2);
      this.setOnCancelListener(var3);
   }

   static int resolveDialogTheme(@NonNull Context var0, @StyleRes int var1) {
      if ((var1 >>> 24 & 255) >= 1) {
         return var1;
      } else {
         TypedValue var2 = new TypedValue();
         var0.getTheme().resolveAttribute(R$attr.alertDialogTheme, var2, true);
         return var2.resourceId;
      }
   }

   public Button getButton(int var1) {
      return this.mAlert.getButton(var1);
   }

   public ListView getListView() {
      return this.mAlert.getListView();
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.mAlert.installContent();
   }

   public boolean onKeyDown(int var1, KeyEvent var2) {
      return this.mAlert.onKeyDown(var1, var2) ? true : super.onKeyDown(var1, var2);
   }

   public boolean onKeyUp(int var1, KeyEvent var2) {
      return this.mAlert.onKeyUp(var1, var2) ? true : super.onKeyUp(var1, var2);
   }

   public void setButton(int var1, CharSequence var2, OnClickListener var3) {
      this.mAlert.setButton(var1, var2, var3, (Message)null);
   }

   public void setButton(int var1, CharSequence var2, Message var3) {
      this.mAlert.setButton(var1, var2, (OnClickListener)null, var3);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   void setButtonPanelLayoutHint(int var1) {
      this.mAlert.setButtonPanelLayoutHint(var1);
   }

   public void setCustomTitle(View var1) {
      this.mAlert.setCustomTitle(var1);
   }

   public void setIcon(int var1) {
      this.mAlert.setIcon(var1);
   }

   public void setIcon(Drawable var1) {
      this.mAlert.setIcon(var1);
   }

   public void setIconAttribute(int var1) {
      TypedValue var2 = new TypedValue();
      this.getContext().getTheme().resolveAttribute(var1, var2, true);
      this.mAlert.setIcon(var2.resourceId);
   }

   public void setMessage(CharSequence var1) {
      this.mAlert.setMessage(var1);
   }

   public void setTitle(CharSequence var1) {
      super.setTitle(var1);
      this.mAlert.setTitle(var1);
   }

   public void setView(View var1) {
      this.mAlert.setView(var1);
   }

   public void setView(View var1, int var2, int var3, int var4, int var5) {
      this.mAlert.setView(var1, var2, var3, var4, var5);
   }

   public static class Builder {
      // $FF: renamed from: P android.support.v7.app.AlertController$AlertParams
      private final AlertController.AlertParams field_7;
      private final int mTheme;

      public Builder(@NonNull Context var1) {
         this(var1, AlertDialog.resolveDialogTheme(var1, 0));
      }

      public Builder(@NonNull Context var1, @StyleRes int var2) {
         this.field_7 = new AlertController.AlertParams(new ContextThemeWrapper(var1, AlertDialog.resolveDialogTheme(var1, var2)));
         this.mTheme = var2;
      }

      public AlertDialog create() {
         AlertDialog var1 = new AlertDialog(this.field_7.mContext, this.mTheme);
         this.field_7.apply(var1.mAlert);
         var1.setCancelable(this.field_7.mCancelable);
         if (this.field_7.mCancelable) {
            var1.setCanceledOnTouchOutside(true);
         }

         var1.setOnCancelListener(this.field_7.mOnCancelListener);
         var1.setOnDismissListener(this.field_7.mOnDismissListener);
         if (this.field_7.mOnKeyListener != null) {
            var1.setOnKeyListener(this.field_7.mOnKeyListener);
         }

         return var1;
      }

      @NonNull
      public Context getContext() {
         return this.field_7.mContext;
      }

      public AlertDialog.Builder setAdapter(ListAdapter var1, OnClickListener var2) {
         AlertController.AlertParams var3 = this.field_7;
         var3.mAdapter = var1;
         var3.mOnClickListener = var2;
         return this;
      }

      public AlertDialog.Builder setCancelable(boolean var1) {
         this.field_7.mCancelable = var1;
         return this;
      }

      public AlertDialog.Builder setCursor(Cursor var1, OnClickListener var2, String var3) {
         AlertController.AlertParams var4 = this.field_7;
         var4.mCursor = var1;
         var4.mLabelColumn = var3;
         var4.mOnClickListener = var2;
         return this;
      }

      public AlertDialog.Builder setCustomTitle(@Nullable View var1) {
         this.field_7.mCustomTitleView = var1;
         return this;
      }

      public AlertDialog.Builder setIcon(@DrawableRes int var1) {
         this.field_7.mIconId = var1;
         return this;
      }

      public AlertDialog.Builder setIcon(@Nullable Drawable var1) {
         this.field_7.mIcon = var1;
         return this;
      }

      public AlertDialog.Builder setIconAttribute(@AttrRes int var1) {
         TypedValue var2 = new TypedValue();
         this.field_7.mContext.getTheme().resolveAttribute(var1, var2, true);
         this.field_7.mIconId = var2.resourceId;
         return this;
      }

      @Deprecated
      public AlertDialog.Builder setInverseBackgroundForced(boolean var1) {
         this.field_7.mForceInverseBackground = var1;
         return this;
      }

      public AlertDialog.Builder setItems(@ArrayRes int var1, OnClickListener var2) {
         AlertController.AlertParams var3 = this.field_7;
         var3.mItems = var3.mContext.getResources().getTextArray(var1);
         this.field_7.mOnClickListener = var2;
         return this;
      }

      public AlertDialog.Builder setItems(CharSequence[] var1, OnClickListener var2) {
         AlertController.AlertParams var3 = this.field_7;
         var3.mItems = var1;
         var3.mOnClickListener = var2;
         return this;
      }

      public AlertDialog.Builder setMessage(@StringRes int var1) {
         AlertController.AlertParams var2 = this.field_7;
         var2.mMessage = var2.mContext.getText(var1);
         return this;
      }

      public AlertDialog.Builder setMessage(@Nullable CharSequence var1) {
         this.field_7.mMessage = var1;
         return this;
      }

      public AlertDialog.Builder setMultiChoiceItems(@ArrayRes int var1, boolean[] var2, OnMultiChoiceClickListener var3) {
         AlertController.AlertParams var4 = this.field_7;
         var4.mItems = var4.mContext.getResources().getTextArray(var1);
         var4 = this.field_7;
         var4.mOnCheckboxClickListener = var3;
         var4.mCheckedItems = var2;
         var4.mIsMultiChoice = true;
         return this;
      }

      public AlertDialog.Builder setMultiChoiceItems(Cursor var1, String var2, String var3, OnMultiChoiceClickListener var4) {
         AlertController.AlertParams var5 = this.field_7;
         var5.mCursor = var1;
         var5.mOnCheckboxClickListener = var4;
         var5.mIsCheckedColumn = var2;
         var5.mLabelColumn = var3;
         var5.mIsMultiChoice = true;
         return this;
      }

      public AlertDialog.Builder setMultiChoiceItems(CharSequence[] var1, boolean[] var2, OnMultiChoiceClickListener var3) {
         AlertController.AlertParams var4 = this.field_7;
         var4.mItems = var1;
         var4.mOnCheckboxClickListener = var3;
         var4.mCheckedItems = var2;
         var4.mIsMultiChoice = true;
         return this;
      }

      public AlertDialog.Builder setNegativeButton(@StringRes int var1, OnClickListener var2) {
         AlertController.AlertParams var3 = this.field_7;
         var3.mNegativeButtonText = var3.mContext.getText(var1);
         this.field_7.mNegativeButtonListener = var2;
         return this;
      }

      public AlertDialog.Builder setNegativeButton(CharSequence var1, OnClickListener var2) {
         AlertController.AlertParams var3 = this.field_7;
         var3.mNegativeButtonText = var1;
         var3.mNegativeButtonListener = var2;
         return this;
      }

      public AlertDialog.Builder setNeutralButton(@StringRes int var1, OnClickListener var2) {
         AlertController.AlertParams var3 = this.field_7;
         var3.mNeutralButtonText = var3.mContext.getText(var1);
         this.field_7.mNeutralButtonListener = var2;
         return this;
      }

      public AlertDialog.Builder setNeutralButton(CharSequence var1, OnClickListener var2) {
         AlertController.AlertParams var3 = this.field_7;
         var3.mNeutralButtonText = var1;
         var3.mNeutralButtonListener = var2;
         return this;
      }

      public AlertDialog.Builder setOnCancelListener(OnCancelListener var1) {
         this.field_7.mOnCancelListener = var1;
         return this;
      }

      public AlertDialog.Builder setOnDismissListener(OnDismissListener var1) {
         this.field_7.mOnDismissListener = var1;
         return this;
      }

      public AlertDialog.Builder setOnItemSelectedListener(OnItemSelectedListener var1) {
         this.field_7.mOnItemSelectedListener = var1;
         return this;
      }

      public AlertDialog.Builder setOnKeyListener(OnKeyListener var1) {
         this.field_7.mOnKeyListener = var1;
         return this;
      }

      public AlertDialog.Builder setPositiveButton(@StringRes int var1, OnClickListener var2) {
         AlertController.AlertParams var3 = this.field_7;
         var3.mPositiveButtonText = var3.mContext.getText(var1);
         this.field_7.mPositiveButtonListener = var2;
         return this;
      }

      public AlertDialog.Builder setPositiveButton(CharSequence var1, OnClickListener var2) {
         AlertController.AlertParams var3 = this.field_7;
         var3.mPositiveButtonText = var1;
         var3.mPositiveButtonListener = var2;
         return this;
      }

      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public AlertDialog.Builder setRecycleOnMeasureEnabled(boolean var1) {
         this.field_7.mRecycleOnMeasure = var1;
         return this;
      }

      public AlertDialog.Builder setSingleChoiceItems(@ArrayRes int var1, int var2, OnClickListener var3) {
         AlertController.AlertParams var4 = this.field_7;
         var4.mItems = var4.mContext.getResources().getTextArray(var1);
         var4 = this.field_7;
         var4.mOnClickListener = var3;
         var4.mCheckedItem = var2;
         var4.mIsSingleChoice = true;
         return this;
      }

      public AlertDialog.Builder setSingleChoiceItems(Cursor var1, int var2, String var3, OnClickListener var4) {
         AlertController.AlertParams var5 = this.field_7;
         var5.mCursor = var1;
         var5.mOnClickListener = var4;
         var5.mCheckedItem = var2;
         var5.mLabelColumn = var3;
         var5.mIsSingleChoice = true;
         return this;
      }

      public AlertDialog.Builder setSingleChoiceItems(ListAdapter var1, int var2, OnClickListener var3) {
         AlertController.AlertParams var4 = this.field_7;
         var4.mAdapter = var1;
         var4.mOnClickListener = var3;
         var4.mCheckedItem = var2;
         var4.mIsSingleChoice = true;
         return this;
      }

      public AlertDialog.Builder setSingleChoiceItems(CharSequence[] var1, int var2, OnClickListener var3) {
         AlertController.AlertParams var4 = this.field_7;
         var4.mItems = var1;
         var4.mOnClickListener = var3;
         var4.mCheckedItem = var2;
         var4.mIsSingleChoice = true;
         return this;
      }

      public AlertDialog.Builder setTitle(@StringRes int var1) {
         AlertController.AlertParams var2 = this.field_7;
         var2.mTitle = var2.mContext.getText(var1);
         return this;
      }

      public AlertDialog.Builder setTitle(@Nullable CharSequence var1) {
         this.field_7.mTitle = var1;
         return this;
      }

      public AlertDialog.Builder setView(int var1) {
         AlertController.AlertParams var2 = this.field_7;
         var2.mView = null;
         var2.mViewLayoutResId = var1;
         var2.mViewSpacingSpecified = false;
         return this;
      }

      public AlertDialog.Builder setView(View var1) {
         AlertController.AlertParams var2 = this.field_7;
         var2.mView = var1;
         var2.mViewLayoutResId = 0;
         var2.mViewSpacingSpecified = false;
         return this;
      }

      @Deprecated
      @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
      public AlertDialog.Builder setView(View var1, int var2, int var3, int var4, int var5) {
         AlertController.AlertParams var6 = this.field_7;
         var6.mView = var1;
         var6.mViewLayoutResId = 0;
         var6.mViewSpacingSpecified = true;
         var6.mViewSpacingLeft = var2;
         var6.mViewSpacingTop = var3;
         var6.mViewSpacingRight = var4;
         var6.mViewSpacingBottom = var5;
         return this;
      }

      public AlertDialog show() {
         AlertDialog var1 = this.create();
         var1.show();
         return var1;
      }
   }
}
