package android.support.v7.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Build.VERSION;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.appcompat.R$attr;
import android.support.v7.appcompat.R$id;
import android.support.v7.appcompat.R$styleable;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewStub;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout.LayoutParams;
import java.lang.ref.WeakReference;

class AlertController {
   ListAdapter mAdapter;
   private int mAlertDialogLayout;
   private final OnClickListener mButtonHandler = new OnClickListener() {
      public void onClick(View var1) {
         Message var2;
         if (var1 == AlertController.this.mButtonPositive && AlertController.this.mButtonPositiveMessage != null) {
            var2 = Message.obtain(AlertController.this.mButtonPositiveMessage);
         } else if (var1 == AlertController.this.mButtonNegative && AlertController.this.mButtonNegativeMessage != null) {
            var2 = Message.obtain(AlertController.this.mButtonNegativeMessage);
         } else if (var1 == AlertController.this.mButtonNeutral && AlertController.this.mButtonNeutralMessage != null) {
            var2 = Message.obtain(AlertController.this.mButtonNeutralMessage);
         } else {
            var2 = null;
         }

         if (var2 != null) {
            var2.sendToTarget();
         }

         AlertController.this.mHandler.obtainMessage(1, AlertController.this.mDialog).sendToTarget();
      }
   };
   Button mButtonNegative;
   Message mButtonNegativeMessage;
   private CharSequence mButtonNegativeText;
   Button mButtonNeutral;
   Message mButtonNeutralMessage;
   private CharSequence mButtonNeutralText;
   private int mButtonPanelLayoutHint = 0;
   private int mButtonPanelSideLayout;
   Button mButtonPositive;
   Message mButtonPositiveMessage;
   private CharSequence mButtonPositiveText;
   int mCheckedItem = -1;
   private final Context mContext;
   private View mCustomTitleView;
   final AppCompatDialog mDialog;
   Handler mHandler;
   private Drawable mIcon;
   private int mIconId = 0;
   private ImageView mIconView;
   int mListItemLayout;
   int mListLayout;
   ListView mListView;
   private CharSequence mMessage;
   private TextView mMessageView;
   int mMultiChoiceItemLayout;
   NestedScrollView mScrollView;
   private boolean mShowTitle;
   int mSingleChoiceItemLayout;
   private CharSequence mTitle;
   private TextView mTitleView;
   private View mView;
   private int mViewLayoutResId;
   private int mViewSpacingBottom;
   private int mViewSpacingLeft;
   private int mViewSpacingRight;
   private boolean mViewSpacingSpecified = false;
   private int mViewSpacingTop;
   private final Window mWindow;

   public AlertController(Context var1, AppCompatDialog var2, Window var3) {
      this.mContext = var1;
      this.mDialog = var2;
      this.mWindow = var3;
      this.mHandler = new AlertController.ButtonHandler(var2);
      TypedArray var4 = var1.obtainStyledAttributes((AttributeSet)null, R$styleable.AlertDialog, R$attr.alertDialogStyle, 0);
      this.mAlertDialogLayout = var4.getResourceId(R$styleable.AlertDialog_android_layout, 0);
      this.mButtonPanelSideLayout = var4.getResourceId(R$styleable.AlertDialog_buttonPanelSideLayout, 0);
      this.mListLayout = var4.getResourceId(R$styleable.AlertDialog_listLayout, 0);
      this.mMultiChoiceItemLayout = var4.getResourceId(R$styleable.AlertDialog_multiChoiceItemLayout, 0);
      this.mSingleChoiceItemLayout = var4.getResourceId(R$styleable.AlertDialog_singleChoiceItemLayout, 0);
      this.mListItemLayout = var4.getResourceId(R$styleable.AlertDialog_listItemLayout, 0);
      this.mShowTitle = var4.getBoolean(R$styleable.AlertDialog_showTitle, true);
      var4.recycle();
      var2.supportRequestWindowFeature(1);
   }

   static boolean canTextInput(View var0) {
      if (var0.onCheckIsTextEditor()) {
         return true;
      } else if (!(var0 instanceof ViewGroup)) {
         return false;
      } else {
         ViewGroup var3 = (ViewGroup)var0;
         int var1 = var3.getChildCount();

         int var2;
         do {
            if (var1 <= 0) {
               return false;
            }

            var2 = var1 - 1;
            var1 = var2;
         } while(!canTextInput(var3.getChildAt(var2)));

         return true;
      }
   }

   private void centerButton(Button var1) {
      LayoutParams var2 = (LayoutParams)var1.getLayoutParams();
      var2.gravity = 1;
      var2.weight = 0.5F;
      var1.setLayoutParams(var2);
   }

   static void manageScrollIndicators(View var0, View var1, View var2) {
      byte var4 = 0;
      byte var3;
      if (var1 != null) {
         if (var0.canScrollVertically(-1)) {
            var3 = 0;
         } else {
            var3 = 4;
         }

         var1.setVisibility(var3);
      }

      if (var2 != null) {
         if (var0.canScrollVertically(1)) {
            var3 = var4;
         } else {
            var3 = 4;
         }

         var2.setVisibility(var3);
      }

   }

   @Nullable
   private ViewGroup resolvePanel(@Nullable View var1, @Nullable View var2) {
      if (var1 == null) {
         var1 = var2;
         if (var2 instanceof ViewStub) {
            var1 = ((ViewStub)var2).inflate();
         }

         return (ViewGroup)var1;
      } else {
         if (var2 != null) {
            ViewParent var3 = var2.getParent();
            if (var3 instanceof ViewGroup) {
               ((ViewGroup)var3).removeView(var2);
            }
         }

         var2 = var1;
         if (var1 instanceof ViewStub) {
            var2 = ((ViewStub)var1).inflate();
         }

         return (ViewGroup)var2;
      }
   }

   private int selectContentView() {
      int var1 = this.mButtonPanelSideLayout;
      if (var1 == 0) {
         return this.mAlertDialogLayout;
      } else {
         return this.mButtonPanelLayoutHint == 1 ? var1 : this.mAlertDialogLayout;
      }
   }

   private void setScrollIndicators(ViewGroup var1, final View var2, int var3, int var4) {
      final View var5 = this.mWindow.findViewById(R$id.scrollIndicatorUp);
      View var6 = this.mWindow.findViewById(R$id.scrollIndicatorDown);
      if (VERSION.SDK_INT >= 23) {
         ViewCompat.setScrollIndicators(var2, var3, var4);
         if (var5 != null) {
            var1.removeView(var5);
         }

         if (var6 != null) {
            var1.removeView(var6);
            return;
         }
      } else {
         var2 = var5;
         if (var5 != null) {
            var2 = var5;
            if ((var3 & 1) == 0) {
               var1.removeView(var5);
               var2 = null;
            }
         }

         var5 = var6;
         if (var6 != null) {
            var5 = var6;
            if ((var3 & 2) == 0) {
               var1.removeView(var6);
               var5 = null;
            }
         }

         if (var2 != null || var5 != null) {
            if (this.mMessage != null) {
               this.mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                  public void onScrollChange(NestedScrollView var1, int var2x, int var3, int var4, int var5x) {
                     AlertController.manageScrollIndicators(var1, var2, var5);
                  }
               });
               this.mScrollView.post(new Runnable() {
                  public void run() {
                     AlertController.manageScrollIndicators(AlertController.this.mScrollView, var2, var5);
                  }
               });
               return;
            }

            ListView var7 = this.mListView;
            if (var7 != null) {
               var7.setOnScrollListener(new OnScrollListener() {
                  public void onScroll(AbsListView var1, int var2x, int var3, int var4) {
                     AlertController.manageScrollIndicators(var1, var2, var5);
                  }

                  public void onScrollStateChanged(AbsListView var1, int var2x) {
                  }
               });
               this.mListView.post(new Runnable() {
                  public void run() {
                     AlertController.manageScrollIndicators(AlertController.this.mListView, var2, var5);
                  }
               });
               return;
            }

            if (var2 != null) {
               var1.removeView(var2);
            }

            if (var5 != null) {
               var1.removeView(var5);
            }
         }
      }

   }

   private void setupButtons(ViewGroup var1) {
      int var2 = 0;
      this.mButtonPositive = (Button)var1.findViewById(16908313);
      this.mButtonPositive.setOnClickListener(this.mButtonHandler);
      boolean var4 = TextUtils.isEmpty(this.mButtonPositiveText);
      boolean var3 = false;
      if (var4) {
         this.mButtonPositive.setVisibility(8);
      } else {
         this.mButtonPositive.setText(this.mButtonPositiveText);
         this.mButtonPositive.setVisibility(0);
         var2 = 0 | 1;
      }

      this.mButtonNegative = (Button)var1.findViewById(16908314);
      this.mButtonNegative.setOnClickListener(this.mButtonHandler);
      if (TextUtils.isEmpty(this.mButtonNegativeText)) {
         this.mButtonNegative.setVisibility(8);
      } else {
         this.mButtonNegative.setText(this.mButtonNegativeText);
         this.mButtonNegative.setVisibility(0);
         var2 |= 2;
      }

      this.mButtonNeutral = (Button)var1.findViewById(16908315);
      this.mButtonNeutral.setOnClickListener(this.mButtonHandler);
      if (TextUtils.isEmpty(this.mButtonNeutralText)) {
         this.mButtonNeutral.setVisibility(8);
      } else {
         this.mButtonNeutral.setText(this.mButtonNeutralText);
         this.mButtonNeutral.setVisibility(0);
         var2 |= 4;
      }

      if (shouldCenterSingleButton(this.mContext)) {
         if (var2 == 1) {
            this.centerButton(this.mButtonPositive);
         } else if (var2 == 2) {
            this.centerButton(this.mButtonNegative);
         } else if (var2 == 4) {
            this.centerButton(this.mButtonNeutral);
         }
      }

      if (var2 != 0) {
         var3 = true;
      }

      if (!var3) {
         var1.setVisibility(8);
      }

   }

   private void setupContent(ViewGroup var1) {
      this.mScrollView = (NestedScrollView)this.mWindow.findViewById(R$id.scrollView);
      this.mScrollView.setFocusable(false);
      this.mScrollView.setNestedScrollingEnabled(false);
      this.mMessageView = (TextView)var1.findViewById(16908299);
      TextView var3 = this.mMessageView;
      if (var3 != null) {
         CharSequence var4 = this.mMessage;
         if (var4 != null) {
            var3.setText(var4);
         } else {
            var3.setVisibility(8);
            this.mScrollView.removeView(this.mMessageView);
            if (this.mListView != null) {
               var1 = (ViewGroup)this.mScrollView.getParent();
               int var2 = var1.indexOfChild(this.mScrollView);
               var1.removeViewAt(var2);
               var1.addView(this.mListView, var2, new android.view.ViewGroup.LayoutParams(-1, -1));
            } else {
               var1.setVisibility(8);
            }
         }
      }
   }

   private void setupCustomContent(ViewGroup var1) {
      View var3 = this.mView;
      boolean var2 = false;
      if (var3 != null) {
         var3 = this.mView;
      } else if (this.mViewLayoutResId != 0) {
         var3 = LayoutInflater.from(this.mContext).inflate(this.mViewLayoutResId, var1, false);
      } else {
         var3 = null;
      }

      if (var3 != null) {
         var2 = true;
      }

      if (!var2 || !canTextInput(var3)) {
         this.mWindow.setFlags(131072, 131072);
      }

      if (var2) {
         FrameLayout var4 = (FrameLayout)this.mWindow.findViewById(R$id.custom);
         var4.addView(var3, new android.view.ViewGroup.LayoutParams(-1, -1));
         if (this.mViewSpacingSpecified) {
            var4.setPadding(this.mViewSpacingLeft, this.mViewSpacingTop, this.mViewSpacingRight, this.mViewSpacingBottom);
         }

         if (this.mListView != null) {
            ((LinearLayoutCompat.LayoutParams)var1.getLayoutParams()).weight = 0.0F;
         }

      } else {
         var1.setVisibility(8);
      }
   }

   private void setupTitle(ViewGroup var1) {
      if (this.mCustomTitleView != null) {
         android.view.ViewGroup.LayoutParams var3 = new android.view.ViewGroup.LayoutParams(-1, -2);
         var1.addView(this.mCustomTitleView, 0, var3);
         this.mWindow.findViewById(R$id.title_template).setVisibility(8);
      } else {
         this.mIconView = (ImageView)this.mWindow.findViewById(16908294);
         if (TextUtils.isEmpty(this.mTitle) ^ true && this.mShowTitle) {
            this.mTitleView = (TextView)this.mWindow.findViewById(R$id.alertTitle);
            this.mTitleView.setText(this.mTitle);
            int var2 = this.mIconId;
            if (var2 != 0) {
               this.mIconView.setImageResource(var2);
            } else {
               Drawable var4 = this.mIcon;
               if (var4 != null) {
                  this.mIconView.setImageDrawable(var4);
               } else {
                  this.mTitleView.setPadding(this.mIconView.getPaddingLeft(), this.mIconView.getPaddingTop(), this.mIconView.getPaddingRight(), this.mIconView.getPaddingBottom());
                  this.mIconView.setVisibility(8);
               }
            }
         } else {
            this.mWindow.findViewById(R$id.title_template).setVisibility(8);
            this.mIconView.setVisibility(8);
            var1.setVisibility(8);
         }
      }
   }

   private void setupView() {
      View var8 = this.mWindow.findViewById(R$id.parentPanel);
      View var7 = var8.findViewById(R$id.topPanel);
      View var6 = var8.findViewById(R$id.contentPanel);
      View var5 = var8.findViewById(R$id.buttonPanel);
      ViewGroup var21 = (ViewGroup)var8.findViewById(R$id.customPanel);
      this.setupCustomContent(var21);
      View var11 = var21.findViewById(R$id.topPanel);
      View var10 = var21.findViewById(R$id.contentPanel);
      View var9 = var21.findViewById(R$id.buttonPanel);
      ViewGroup var18 = this.resolvePanel(var11, var7);
      ViewGroup var16 = this.resolvePanel(var10, var6);
      ViewGroup var14 = this.resolvePanel(var9, var5);
      this.setupContent(var16);
      this.setupButtons(var14);
      this.setupTitle(var18);
      boolean var1;
      if (var21 != null && var21.getVisibility() != 8) {
         var1 = true;
      } else {
         var1 = false;
      }

      boolean var3;
      if (var18 != null && var18.getVisibility() != 8) {
         var3 = true;
      } else {
         var3 = false;
      }

      boolean var4;
      if (var14 != null && var14.getVisibility() != 8) {
         var4 = true;
      } else {
         var4 = false;
      }

      if (!var4 && var16 != null) {
         var5 = var16.findViewById(R$id.textSpacerNoButtons);
         if (var5 != null) {
            var5.setVisibility(0);
         }
      }

      if (var3) {
         NestedScrollView var15 = this.mScrollView;
         if (var15 != null) {
            var15.setClipToPadding(true);
         }

         var5 = null;
         if (this.mMessage != null || this.mListView != null) {
            var5 = var18.findViewById(R$id.titleDividerNoCustom);
         }

         if (var5 != null) {
            var5.setVisibility(0);
         }
      } else if (var16 != null) {
         var5 = var16.findViewById(R$id.textSpacerNoTitle);
         if (var5 != null) {
            var5.setVisibility(0);
         }
      }

      ListView var19 = this.mListView;
      if (var19 instanceof AlertController.RecycleListView) {
         ((AlertController.RecycleListView)var19).setHasDecor(var3, var4);
      }

      if (!var1) {
         Object var20 = this.mListView;
         if (var20 == null) {
            var20 = this.mScrollView;
         }

         if (var20 != null) {
            byte var12;
            if (var3) {
               var12 = 1;
            } else {
               var12 = 0;
            }

            byte var2;
            if (var4) {
               var2 = 2;
            } else {
               var2 = 0;
            }

            this.setScrollIndicators(var16, (View)var20, var12 | var2, 3);
         }
      }

      var19 = this.mListView;
      if (var19 != null) {
         ListAdapter var17 = this.mAdapter;
         if (var17 != null) {
            var19.setAdapter(var17);
            int var13 = this.mCheckedItem;
            if (var13 > -1) {
               var19.setItemChecked(var13, true);
               var19.setSelection(var13);
            }
         }
      }

   }

   private static boolean shouldCenterSingleButton(Context var0) {
      TypedValue var1 = new TypedValue();
      var0.getTheme().resolveAttribute(R$attr.alertDialogCenterButtons, var1, true);
      return var1.data != 0;
   }

   public Button getButton(int var1) {
      if (var1 != -3) {
         if (var1 != -2) {
            return var1 != -1 ? null : this.mButtonPositive;
         } else {
            return this.mButtonNegative;
         }
      } else {
         return this.mButtonNeutral;
      }
   }

   public int getIconAttributeResId(int var1) {
      TypedValue var2 = new TypedValue();
      this.mContext.getTheme().resolveAttribute(var1, var2, true);
      return var2.resourceId;
   }

   public ListView getListView() {
      return this.mListView;
   }

   public void installContent() {
      int var1 = this.selectContentView();
      this.mDialog.setContentView(var1);
      this.setupView();
   }

   public boolean onKeyDown(int var1, KeyEvent var2) {
      NestedScrollView var3 = this.mScrollView;
      return var3 != null && var3.executeKeyEvent(var2);
   }

   public boolean onKeyUp(int var1, KeyEvent var2) {
      NestedScrollView var3 = this.mScrollView;
      return var3 != null && var3.executeKeyEvent(var2);
   }

   public void setButton(int var1, CharSequence var2, android.content.DialogInterface.OnClickListener var3, Message var4) {
      Message var5 = var4;
      if (var4 == null) {
         var5 = var4;
         if (var3 != null) {
            var5 = this.mHandler.obtainMessage(var1, var3);
         }
      }

      if (var1 != -3) {
         if (var1 != -2) {
            if (var1 == -1) {
               this.mButtonPositiveText = var2;
               this.mButtonPositiveMessage = var5;
            } else {
               throw new IllegalArgumentException("Button does not exist");
            }
         } else {
            this.mButtonNegativeText = var2;
            this.mButtonNegativeMessage = var5;
         }
      } else {
         this.mButtonNeutralText = var2;
         this.mButtonNeutralMessage = var5;
      }
   }

   public void setButtonPanelLayoutHint(int var1) {
      this.mButtonPanelLayoutHint = var1;
   }

   public void setCustomTitle(View var1) {
      this.mCustomTitleView = var1;
   }

   public void setIcon(int var1) {
      this.mIcon = null;
      this.mIconId = var1;
      ImageView var2 = this.mIconView;
      if (var2 != null) {
         if (var1 != 0) {
            var2.setVisibility(0);
            this.mIconView.setImageResource(this.mIconId);
            return;
         }

         var2.setVisibility(8);
      }

   }

   public void setIcon(Drawable var1) {
      this.mIcon = var1;
      this.mIconId = 0;
      ImageView var2 = this.mIconView;
      if (var2 != null) {
         if (var1 != null) {
            var2.setVisibility(0);
            this.mIconView.setImageDrawable(var1);
            return;
         }

         var2.setVisibility(8);
      }

   }

   public void setMessage(CharSequence var1) {
      this.mMessage = var1;
      TextView var2 = this.mMessageView;
      if (var2 != null) {
         var2.setText(var1);
      }

   }

   public void setTitle(CharSequence var1) {
      this.mTitle = var1;
      TextView var2 = this.mTitleView;
      if (var2 != null) {
         var2.setText(var1);
      }

   }

   public void setView(int var1) {
      this.mView = null;
      this.mViewLayoutResId = var1;
      this.mViewSpacingSpecified = false;
   }

   public void setView(View var1) {
      this.mView = var1;
      this.mViewLayoutResId = 0;
      this.mViewSpacingSpecified = false;
   }

   public void setView(View var1, int var2, int var3, int var4, int var5) {
      this.mView = var1;
      this.mViewLayoutResId = 0;
      this.mViewSpacingSpecified = true;
      this.mViewSpacingLeft = var2;
      this.mViewSpacingTop = var3;
      this.mViewSpacingRight = var4;
      this.mViewSpacingBottom = var5;
   }

   public static class AlertParams {
      public ListAdapter mAdapter;
      public boolean mCancelable;
      public int mCheckedItem = -1;
      public boolean[] mCheckedItems;
      public final Context mContext;
      public Cursor mCursor;
      public View mCustomTitleView;
      public boolean mForceInverseBackground;
      public Drawable mIcon;
      public int mIconAttrId = 0;
      public int mIconId = 0;
      public final LayoutInflater mInflater;
      public String mIsCheckedColumn;
      public boolean mIsMultiChoice;
      public boolean mIsSingleChoice;
      public CharSequence[] mItems;
      public String mLabelColumn;
      public CharSequence mMessage;
      public android.content.DialogInterface.OnClickListener mNegativeButtonListener;
      public CharSequence mNegativeButtonText;
      public android.content.DialogInterface.OnClickListener mNeutralButtonListener;
      public CharSequence mNeutralButtonText;
      public OnCancelListener mOnCancelListener;
      public OnMultiChoiceClickListener mOnCheckboxClickListener;
      public android.content.DialogInterface.OnClickListener mOnClickListener;
      public OnDismissListener mOnDismissListener;
      public OnItemSelectedListener mOnItemSelectedListener;
      public OnKeyListener mOnKeyListener;
      public AlertController.AlertParams.OnPrepareListViewListener mOnPrepareListViewListener;
      public android.content.DialogInterface.OnClickListener mPositiveButtonListener;
      public CharSequence mPositiveButtonText;
      public boolean mRecycleOnMeasure = true;
      public CharSequence mTitle;
      public View mView;
      public int mViewLayoutResId;
      public int mViewSpacingBottom;
      public int mViewSpacingLeft;
      public int mViewSpacingRight;
      public boolean mViewSpacingSpecified = false;
      public int mViewSpacingTop;

      public AlertParams(Context var1) {
         this.mContext = var1;
         this.mCancelable = true;
         this.mInflater = (LayoutInflater)var1.getSystemService("layout_inflater");
      }

      private void createListView(final AlertController var1) {
         final AlertController.RecycleListView var4 = (AlertController.RecycleListView)this.mInflater.inflate(var1.mListLayout, (ViewGroup)null);
         Cursor var3;
         Object var6;
         if (this.mIsMultiChoice) {
            var3 = this.mCursor;
            if (var3 == null) {
               var6 = new ArrayAdapter(this.mContext, var1.mMultiChoiceItemLayout, 16908308, this.mItems) {
                  public View getView(int var1, View var2, ViewGroup var3) {
                     var2 = super.getView(var1, var2, var3);
                     if (AlertParams.this.mCheckedItems != null && AlertParams.this.mCheckedItems[var1]) {
                        var4.setItemChecked(var1, true);
                     }

                     return var2;
                  }
               };
            } else {
               var6 = new CursorAdapter(this.mContext, var3, false) {
                  private final int mIsCheckedIndex;
                  private final int mLabelIndex;

                  {
                     Cursor var7 = this.getCursor();
                     this.mLabelIndex = var7.getColumnIndexOrThrow(AlertParams.this.mLabelColumn);
                     this.mIsCheckedIndex = var7.getColumnIndexOrThrow(AlertParams.this.mIsCheckedColumn);
                  }

                  public void bindView(View var1x, Context var2, Cursor var3) {
                     ((CheckedTextView)var1x.findViewById(16908308)).setText(var3.getString(this.mLabelIndex));
                     AlertController.RecycleListView var7 = var4;
                     int var4x = var3.getPosition();
                     int var5 = var3.getInt(this.mIsCheckedIndex);
                     boolean var6 = true;
                     if (var5 != 1) {
                        var6 = false;
                     }

                     var7.setItemChecked(var4x, var6);
                  }

                  public View newView(Context var1x, Cursor var2, ViewGroup var3) {
                     return AlertParams.this.mInflater.inflate(var1.mMultiChoiceItemLayout, var3, false);
                  }
               };
            }
         } else {
            int var2;
            if (this.mIsSingleChoice) {
               var2 = var1.mSingleChoiceItemLayout;
            } else {
               var2 = var1.mListItemLayout;
            }

            var3 = this.mCursor;
            if (var3 != null) {
               var6 = new SimpleCursorAdapter(this.mContext, var2, var3, new String[]{this.mLabelColumn}, new int[]{16908308});
            } else if (this.mAdapter != null) {
               var6 = this.mAdapter;
            } else {
               var6 = new AlertController.CheckedItemAdapter(this.mContext, var2, 16908308, this.mItems);
            }
         }

         AlertController.AlertParams.OnPrepareListViewListener var5 = this.mOnPrepareListViewListener;
         if (var5 != null) {
            var5.onPrepareListView(var4);
         }

         var1.mAdapter = (ListAdapter)var6;
         var1.mCheckedItem = this.mCheckedItem;
         if (this.mOnClickListener != null) {
            var4.setOnItemClickListener(new OnItemClickListener() {
               public void onItemClick(AdapterView var1x, View var2, int var3, long var4) {
                  AlertParams.this.mOnClickListener.onClick(var1.mDialog, var3);
                  if (!AlertParams.this.mIsSingleChoice) {
                     var1.mDialog.dismiss();
                  }

               }
            });
         } else if (this.mOnCheckboxClickListener != null) {
            var4.setOnItemClickListener(new OnItemClickListener() {
               public void onItemClick(AdapterView var1x, View var2, int var3, long var4x) {
                  if (AlertParams.this.mCheckedItems != null) {
                     AlertParams.this.mCheckedItems[var3] = var4.isItemChecked(var3);
                  }

                  AlertParams.this.mOnCheckboxClickListener.onClick(var1.mDialog, var3, var4.isItemChecked(var3));
               }
            });
         }

         OnItemSelectedListener var7 = this.mOnItemSelectedListener;
         if (var7 != null) {
            var4.setOnItemSelectedListener(var7);
         }

         if (this.mIsSingleChoice) {
            var4.setChoiceMode(1);
         } else if (this.mIsMultiChoice) {
            var4.setChoiceMode(2);
         }

         var1.mListView = var4;
      }

      public void apply(AlertController var1) {
         View var3 = this.mCustomTitleView;
         int var2;
         CharSequence var4;
         if (var3 != null) {
            var1.setCustomTitle(var3);
         } else {
            var4 = this.mTitle;
            if (var4 != null) {
               var1.setTitle(var4);
            }

            Drawable var5 = this.mIcon;
            if (var5 != null) {
               var1.setIcon(var5);
            }

            var2 = this.mIconId;
            if (var2 != 0) {
               var1.setIcon(var2);
            }

            var2 = this.mIconAttrId;
            if (var2 != 0) {
               var1.setIcon(var1.getIconAttributeResId(var2));
            }
         }

         var4 = this.mMessage;
         if (var4 != null) {
            var1.setMessage(var4);
         }

         var4 = this.mPositiveButtonText;
         if (var4 != null) {
            var1.setButton(-1, var4, this.mPositiveButtonListener, (Message)null);
         }

         var4 = this.mNegativeButtonText;
         if (var4 != null) {
            var1.setButton(-2, var4, this.mNegativeButtonListener, (Message)null);
         }

         var4 = this.mNeutralButtonText;
         if (var4 != null) {
            var1.setButton(-3, var4, this.mNeutralButtonListener, (Message)null);
         }

         if (this.mItems != null || this.mCursor != null || this.mAdapter != null) {
            this.createListView(var1);
         }

         var3 = this.mView;
         if (var3 != null) {
            if (this.mViewSpacingSpecified) {
               var1.setView(var3, this.mViewSpacingLeft, this.mViewSpacingTop, this.mViewSpacingRight, this.mViewSpacingBottom);
            } else {
               var1.setView(var3);
            }
         } else {
            var2 = this.mViewLayoutResId;
            if (var2 != 0) {
               var1.setView(var2);
            }

         }
      }

      public interface OnPrepareListViewListener {
         void onPrepareListView(ListView var1);
      }
   }

   private static final class ButtonHandler extends Handler {
      private static final int MSG_DISMISS_DIALOG = 1;
      private WeakReference mDialog;

      public ButtonHandler(DialogInterface var1) {
         this.mDialog = new WeakReference(var1);
      }

      public void handleMessage(Message var1) {
         int var2 = var1.what;
         if (var2 != -3 && var2 != -2 && var2 != -1) {
            if (var2 == 1) {
               ((DialogInterface)var1.obj).dismiss();
            }
         } else {
            ((android.content.DialogInterface.OnClickListener)var1.obj).onClick((DialogInterface)this.mDialog.get(), var1.what);
         }
      }
   }

   private static class CheckedItemAdapter extends ArrayAdapter {
      public CheckedItemAdapter(Context var1, int var2, int var3, CharSequence[] var4) {
         super(var1, var2, var3, var4);
      }

      public long getItemId(int var1) {
         return (long)var1;
      }

      public boolean hasStableIds() {
         return true;
      }
   }

   public static class RecycleListView extends ListView {
      private final int mPaddingBottomNoButtons;
      private final int mPaddingTopNoTitle;

      public RecycleListView(Context var1) {
         this(var1, (AttributeSet)null);
      }

      public RecycleListView(Context var1, AttributeSet var2) {
         super(var1, var2);
         TypedArray var3 = var1.obtainStyledAttributes(var2, R$styleable.RecycleListView);
         this.mPaddingBottomNoButtons = var3.getDimensionPixelOffset(R$styleable.RecycleListView_paddingBottomNoButtons, -1);
         this.mPaddingTopNoTitle = var3.getDimensionPixelOffset(R$styleable.RecycleListView_paddingTopNoTitle, -1);
      }

      public void setHasDecor(boolean var1, boolean var2) {
         if (!var2 || !var1) {
            int var5 = this.getPaddingLeft();
            int var3;
            if (var1) {
               var3 = this.getPaddingTop();
            } else {
               var3 = this.mPaddingTopNoTitle;
            }

            int var6 = this.getPaddingRight();
            int var4;
            if (var2) {
               var4 = this.getPaddingBottom();
            } else {
               var4 = this.mPaddingBottomNoButtons;
            }

            this.setPadding(var5, var3, var6, var4);
         }

      }
   }
}
