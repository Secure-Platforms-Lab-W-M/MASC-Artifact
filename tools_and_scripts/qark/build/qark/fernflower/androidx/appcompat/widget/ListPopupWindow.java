package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.KeyEvent.DispatcherState;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow.OnDismissListener;
import androidx.appcompat.R.attr;
import androidx.appcompat.R.styleable;
import androidx.appcompat.view.menu.ShowableListMenu;
import androidx.core.view.ViewCompat;
import androidx.core.widget.PopupWindowCompat;
import java.lang.reflect.Method;

public class ListPopupWindow implements ShowableListMenu {
   private static final boolean DEBUG = false;
   static final int EXPAND_LIST_TIMEOUT = 250;
   public static final int INPUT_METHOD_FROM_FOCUSABLE = 0;
   public static final int INPUT_METHOD_NEEDED = 1;
   public static final int INPUT_METHOD_NOT_NEEDED = 2;
   public static final int MATCH_PARENT = -1;
   public static final int POSITION_PROMPT_ABOVE = 0;
   public static final int POSITION_PROMPT_BELOW = 1;
   private static final String TAG = "ListPopupWindow";
   public static final int WRAP_CONTENT = -2;
   private static Method sGetMaxAvailableHeightMethod;
   private static Method sSetClipToWindowEnabledMethod;
   private static Method sSetEpicenterBoundsMethod;
   private ListAdapter mAdapter;
   private Context mContext;
   private boolean mDropDownAlwaysVisible;
   private View mDropDownAnchorView;
   private int mDropDownGravity;
   private int mDropDownHeight;
   private int mDropDownHorizontalOffset;
   DropDownListView mDropDownList;
   private Drawable mDropDownListHighlight;
   private int mDropDownVerticalOffset;
   private boolean mDropDownVerticalOffsetSet;
   private int mDropDownWidth;
   private int mDropDownWindowLayoutType;
   private Rect mEpicenterBounds;
   private boolean mForceIgnoreOutsideTouch;
   final Handler mHandler;
   private final ListPopupWindow.ListSelectorHider mHideSelector;
   private boolean mIsAnimatedFromAnchor;
   private OnItemClickListener mItemClickListener;
   private OnItemSelectedListener mItemSelectedListener;
   int mListItemExpandMaximum;
   private boolean mModal;
   private DataSetObserver mObserver;
   private boolean mOverlapAnchor;
   private boolean mOverlapAnchorSet;
   PopupWindow mPopup;
   private int mPromptPosition;
   private View mPromptView;
   final ListPopupWindow.ResizePopupRunnable mResizePopupRunnable;
   private final ListPopupWindow.PopupScrollListener mScrollListener;
   private Runnable mShowDropDownRunnable;
   private final Rect mTempRect;
   private final ListPopupWindow.PopupTouchInterceptor mTouchInterceptor;

   static {
      if (VERSION.SDK_INT <= 28) {
         try {
            sSetClipToWindowEnabledMethod = PopupWindow.class.getDeclaredMethod("setClipToScreenEnabled", Boolean.TYPE);
         } catch (NoSuchMethodException var2) {
            Log.i("ListPopupWindow", "Could not find method setClipToScreenEnabled() on PopupWindow. Oh well.");
         }

         try {
            sSetEpicenterBoundsMethod = PopupWindow.class.getDeclaredMethod("setEpicenterBounds", Rect.class);
         } catch (NoSuchMethodException var1) {
            Log.i("ListPopupWindow", "Could not find method setEpicenterBounds(Rect) on PopupWindow. Oh well.");
         }
      }

      if (VERSION.SDK_INT <= 23) {
         try {
            sGetMaxAvailableHeightMethod = PopupWindow.class.getDeclaredMethod("getMaxAvailableHeight", View.class, Integer.TYPE, Boolean.TYPE);
            return;
         } catch (NoSuchMethodException var3) {
            Log.i("ListPopupWindow", "Could not find method getMaxAvailableHeight(View, int, boolean) on PopupWindow. Oh well.");
         }
      }

   }

   public ListPopupWindow(Context var1) {
      this(var1, (AttributeSet)null, attr.listPopupWindowStyle);
   }

   public ListPopupWindow(Context var1, AttributeSet var2) {
      this(var1, var2, attr.listPopupWindowStyle);
   }

   public ListPopupWindow(Context var1, AttributeSet var2, int var3) {
      this(var1, var2, var3, 0);
   }

   public ListPopupWindow(Context var1, AttributeSet var2, int var3, int var4) {
      this.mDropDownHeight = -2;
      this.mDropDownWidth = -2;
      this.mDropDownWindowLayoutType = 1002;
      this.mIsAnimatedFromAnchor = true;
      this.mDropDownGravity = 0;
      this.mDropDownAlwaysVisible = false;
      this.mForceIgnoreOutsideTouch = false;
      this.mListItemExpandMaximum = Integer.MAX_VALUE;
      this.mPromptPosition = 0;
      this.mResizePopupRunnable = new ListPopupWindow.ResizePopupRunnable();
      this.mTouchInterceptor = new ListPopupWindow.PopupTouchInterceptor();
      this.mScrollListener = new ListPopupWindow.PopupScrollListener();
      this.mHideSelector = new ListPopupWindow.ListSelectorHider();
      this.mTempRect = new Rect();
      this.mContext = var1;
      this.mHandler = new Handler(var1.getMainLooper());
      TypedArray var6 = var1.obtainStyledAttributes(var2, styleable.ListPopupWindow, var3, var4);
      this.mDropDownHorizontalOffset = var6.getDimensionPixelOffset(styleable.ListPopupWindow_android_dropDownHorizontalOffset, 0);
      int var5 = var6.getDimensionPixelOffset(styleable.ListPopupWindow_android_dropDownVerticalOffset, 0);
      this.mDropDownVerticalOffset = var5;
      if (var5 != 0) {
         this.mDropDownVerticalOffsetSet = true;
      }

      var6.recycle();
      AppCompatPopupWindow var7 = new AppCompatPopupWindow(var1, var2, var3, var4);
      this.mPopup = var7;
      var7.setInputMethodMode(1);
   }

   private int buildDropDown() {
      byte var2 = 0;
      int var1 = 0;
      DropDownListView var6 = this.mDropDownList;
      boolean var5 = false;
      int var3;
      int var10;
      LayoutParams var16;
      if (var6 == null) {
         Context var9 = this.mContext;
         this.mShowDropDownRunnable = new Runnable() {
            public void run() {
               View var1 = ListPopupWindow.this.getAnchorView();
               if (var1 != null && var1.getWindowToken() != null) {
                  ListPopupWindow.this.show();
               }

            }
         };
         var6 = this.createDropDownListView(var9, this.mModal ^ true);
         this.mDropDownList = var6;
         Drawable var7 = this.mDropDownListHighlight;
         if (var7 != null) {
            var6.setSelector(var7);
         }

         this.mDropDownList.setAdapter(this.mAdapter);
         this.mDropDownList.setOnItemClickListener(this.mItemClickListener);
         this.mDropDownList.setFocusable(true);
         this.mDropDownList.setFocusableInTouchMode(true);
         this.mDropDownList.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView var1, View var2, int var3, long var4) {
               if (var3 != -1) {
                  DropDownListView var6 = ListPopupWindow.this.mDropDownList;
                  if (var6 != null) {
                     var6.setListSelectionHidden(false);
                  }
               }

            }

            public void onNothingSelected(AdapterView var1) {
            }
         });
         this.mDropDownList.setOnScrollListener(this.mScrollListener);
         OnItemSelectedListener var11 = this.mItemSelectedListener;
         if (var11 != null) {
            this.mDropDownList.setOnItemSelectedListener(var11);
         }

         DropDownListView var13 = this.mDropDownList;
         View var8 = this.mPromptView;
         Object var12 = var13;
         if (var8 != null) {
            var12 = new LinearLayout(var9);
            ((LinearLayout)var12).setOrientation(1);
            LayoutParams var19 = new LayoutParams(-1, 0, 1.0F);
            var1 = this.mPromptPosition;
            if (var1 != 0) {
               if (var1 != 1) {
                  StringBuilder var14 = new StringBuilder();
                  var14.append("Invalid hint position ");
                  var14.append(this.mPromptPosition);
                  Log.e("ListPopupWindow", var14.toString());
               } else {
                  ((LinearLayout)var12).addView(var13, var19);
                  ((LinearLayout)var12).addView(var8);
               }
            } else {
               ((LinearLayout)var12).addView(var8);
               ((LinearLayout)var12).addView(var13, var19);
            }

            if (this.mDropDownWidth >= 0) {
               var1 = Integer.MIN_VALUE;
               var10 = this.mDropDownWidth;
            } else {
               var1 = 0;
               var10 = 0;
            }

            var8.measure(MeasureSpec.makeMeasureSpec(var10, var1), 0);
            var16 = (LayoutParams)var8.getLayoutParams();
            var1 = var8.getMeasuredHeight();
            var10 = var16.topMargin;
            var3 = var16.bottomMargin;
            var1 = var1 + var10 + var3;
         }

         this.mPopup.setContentView((View)var12);
      } else {
         ViewGroup var15 = (ViewGroup)this.mPopup.getContentView();
         View var17 = this.mPromptView;
         var1 = var2;
         if (var17 != null) {
            var16 = (LayoutParams)var17.getLayoutParams();
            var1 = var17.getMeasuredHeight() + var16.topMargin + var16.bottomMargin;
         }
      }

      Drawable var18 = this.mPopup.getBackground();
      if (var18 != null) {
         var18.getPadding(this.mTempRect);
         var10 = this.mTempRect.top + this.mTempRect.bottom;
         var3 = var10;
         if (!this.mDropDownVerticalOffsetSet) {
            this.mDropDownVerticalOffset = -this.mTempRect.top;
            var3 = var10;
         }
      } else {
         this.mTempRect.setEmpty();
         var3 = 0;
      }

      if (this.mPopup.getInputMethodMode() == 2) {
         var5 = true;
      }

      int var4 = this.getMaxAvailableHeight(this.getAnchorView(), this.mDropDownVerticalOffset, var5);
      if (!this.mDropDownAlwaysVisible && this.mDropDownHeight != -1) {
         var10 = this.mDropDownWidth;
         int var10001;
         if (var10 != -2) {
            if (var10 != -1) {
               var10 = MeasureSpec.makeMeasureSpec(var10, 1073741824);
            } else {
               var10001 = this.mTempRect.left + this.mTempRect.right;
               var10 = MeasureSpec.makeMeasureSpec(this.mContext.getResources().getDisplayMetrics().widthPixels - var10001, 1073741824);
            }
         } else {
            var10001 = this.mTempRect.left + this.mTempRect.right;
            var10 = MeasureSpec.makeMeasureSpec(this.mContext.getResources().getDisplayMetrics().widthPixels - var10001, Integer.MIN_VALUE);
         }

         var4 = this.mDropDownList.measureHeightOfChildrenCompat(var10, 0, -1, var4 - var1, -1);
         var10 = var1;
         if (var4 > 0) {
            var10 = var1 + var3 + this.mDropDownList.getPaddingTop() + this.mDropDownList.getPaddingBottom();
         }

         return var4 + var10;
      } else {
         return var4 + var3;
      }
   }

   private int getMaxAvailableHeight(View var1, int var2, boolean var3) {
      if (VERSION.SDK_INT > 23) {
         return this.mPopup.getMaxAvailableHeight(var1, var2, var3);
      } else {
         Method var5 = sGetMaxAvailableHeightMethod;
         if (var5 != null) {
            try {
               int var4 = (Integer)var5.invoke(this.mPopup, var1, var2, var3);
               return var4;
            } catch (Exception var6) {
               Log.i("ListPopupWindow", "Could not call getMaxAvailableHeightMethod(View, int, boolean) on PopupWindow. Using the public version.");
            }
         }

         return this.mPopup.getMaxAvailableHeight(var1, var2);
      }
   }

   private static boolean isConfirmKey(int var0) {
      return var0 == 66 || var0 == 23;
   }

   private void removePromptView() {
      View var1 = this.mPromptView;
      if (var1 != null) {
         ViewParent var2 = var1.getParent();
         if (var2 instanceof ViewGroup) {
            ((ViewGroup)var2).removeView(this.mPromptView);
         }
      }

   }

   private void setPopupClipToScreenEnabled(boolean var1) {
      if (VERSION.SDK_INT <= 28) {
         Method var2 = sSetClipToWindowEnabledMethod;
         if (var2 != null) {
            try {
               var2.invoke(this.mPopup, var1);
            } catch (Exception var3) {
               Log.i("ListPopupWindow", "Could not call setClipToScreenEnabled() on PopupWindow. Oh well.");
            }

            return;
         }
      } else {
         this.mPopup.setIsClippedToScreen(var1);
      }

   }

   public void clearListSelection() {
      DropDownListView var1 = this.mDropDownList;
      if (var1 != null) {
         var1.setListSelectionHidden(true);
         var1.requestLayout();
      }

   }

   public OnTouchListener createDragToOpenListener(View var1) {
      return new ForwardingListener(var1) {
         public ListPopupWindow getPopup() {
            return ListPopupWindow.this;
         }
      };
   }

   DropDownListView createDropDownListView(Context var1, boolean var2) {
      return new DropDownListView(var1, var2);
   }

   public void dismiss() {
      this.mPopup.dismiss();
      this.removePromptView();
      this.mPopup.setContentView((View)null);
      this.mDropDownList = null;
      this.mHandler.removeCallbacks(this.mResizePopupRunnable);
   }

   public View getAnchorView() {
      return this.mDropDownAnchorView;
   }

   public int getAnimationStyle() {
      return this.mPopup.getAnimationStyle();
   }

   public Drawable getBackground() {
      return this.mPopup.getBackground();
   }

   public Rect getEpicenterBounds() {
      return this.mEpicenterBounds != null ? new Rect(this.mEpicenterBounds) : null;
   }

   public int getHeight() {
      return this.mDropDownHeight;
   }

   public int getHorizontalOffset() {
      return this.mDropDownHorizontalOffset;
   }

   public int getInputMethodMode() {
      return this.mPopup.getInputMethodMode();
   }

   public ListView getListView() {
      return this.mDropDownList;
   }

   public int getPromptPosition() {
      return this.mPromptPosition;
   }

   public Object getSelectedItem() {
      return !this.isShowing() ? null : this.mDropDownList.getSelectedItem();
   }

   public long getSelectedItemId() {
      return !this.isShowing() ? Long.MIN_VALUE : this.mDropDownList.getSelectedItemId();
   }

   public int getSelectedItemPosition() {
      return !this.isShowing() ? -1 : this.mDropDownList.getSelectedItemPosition();
   }

   public View getSelectedView() {
      return !this.isShowing() ? null : this.mDropDownList.getSelectedView();
   }

   public int getSoftInputMode() {
      return this.mPopup.getSoftInputMode();
   }

   public int getVerticalOffset() {
      return !this.mDropDownVerticalOffsetSet ? 0 : this.mDropDownVerticalOffset;
   }

   public int getWidth() {
      return this.mDropDownWidth;
   }

   public boolean isDropDownAlwaysVisible() {
      return this.mDropDownAlwaysVisible;
   }

   public boolean isInputMethodNotNeeded() {
      return this.mPopup.getInputMethodMode() == 2;
   }

   public boolean isModal() {
      return this.mModal;
   }

   public boolean isShowing() {
      return this.mPopup.isShowing();
   }

   public boolean onKeyDown(int var1, KeyEvent var2) {
      if (this.isShowing() && var1 != 62 && (this.mDropDownList.getSelectedItemPosition() >= 0 || !isConfirmKey(var1))) {
         int var5 = this.mDropDownList.getSelectedItemPosition();
         boolean var6 = this.mPopup.isAboveAnchor() ^ true;
         ListAdapter var8 = this.mAdapter;
         int var4 = Integer.MAX_VALUE;
         int var3 = Integer.MIN_VALUE;
         if (var8 != null) {
            boolean var7 = var8.areAllItemsEnabled();
            if (var7) {
               var3 = 0;
            } else {
               var3 = this.mDropDownList.lookForSelectablePosition(0, true);
            }

            var4 = var3;
            if (var7) {
               var3 = var8.getCount() - 1;
            } else {
               var3 = this.mDropDownList.lookForSelectablePosition(var8.getCount() - 1, false);
            }
         }

         if (var6 && var1 == 19 && var5 <= var4 || !var6 && var1 == 20 && var5 >= var3) {
            this.clearListSelection();
            this.mPopup.setInputMethodMode(1);
            this.show();
            return true;
         }

         this.mDropDownList.setListSelectionHidden(false);
         if (this.mDropDownList.onKeyDown(var1, var2)) {
            this.mPopup.setInputMethodMode(2);
            this.mDropDownList.requestFocusFromTouch();
            this.show();
            if (var1 != 19 && var1 != 20 && var1 != 23 && var1 != 66) {
               return false;
            }

            return true;
         }

         if (var6 && var1 == 20) {
            if (var5 == var3) {
               return true;
            }
         } else if (!var6 && var1 == 19 && var5 == var4) {
            return true;
         }
      }

      return false;
   }

   public boolean onKeyPreIme(int var1, KeyEvent var2) {
      if (var1 == 4 && this.isShowing()) {
         View var3 = this.mDropDownAnchorView;
         DispatcherState var4;
         if (var2.getAction() == 0 && var2.getRepeatCount() == 0) {
            var4 = var3.getKeyDispatcherState();
            if (var4 != null) {
               var4.startTracking(var2, this);
            }

            return true;
         }

         if (var2.getAction() == 1) {
            var4 = var3.getKeyDispatcherState();
            if (var4 != null) {
               var4.handleUpEvent(var2);
            }

            if (var2.isTracking() && !var2.isCanceled()) {
               this.dismiss();
               return true;
            }
         }
      }

      return false;
   }

   public boolean onKeyUp(int var1, KeyEvent var2) {
      if (this.isShowing() && this.mDropDownList.getSelectedItemPosition() >= 0) {
         boolean var3 = this.mDropDownList.onKeyUp(var1, var2);
         if (var3 && isConfirmKey(var1)) {
            this.dismiss();
         }

         return var3;
      } else {
         return false;
      }
   }

   public boolean performItemClick(int var1) {
      if (this.isShowing()) {
         if (this.mItemClickListener != null) {
            DropDownListView var2 = this.mDropDownList;
            View var3 = var2.getChildAt(var1 - var2.getFirstVisiblePosition());
            ListAdapter var4 = var2.getAdapter();
            this.mItemClickListener.onItemClick(var2, var3, var1, var4.getItemId(var1));
         }

         return true;
      } else {
         return false;
      }
   }

   public void postShow() {
      this.mHandler.post(this.mShowDropDownRunnable);
   }

   public void setAdapter(ListAdapter var1) {
      DataSetObserver var2 = this.mObserver;
      if (var2 == null) {
         this.mObserver = new ListPopupWindow.PopupDataSetObserver();
      } else {
         ListAdapter var3 = this.mAdapter;
         if (var3 != null) {
            var3.unregisterDataSetObserver(var2);
         }
      }

      this.mAdapter = var1;
      if (var1 != null) {
         var1.registerDataSetObserver(this.mObserver);
      }

      DropDownListView var4 = this.mDropDownList;
      if (var4 != null) {
         var4.setAdapter(this.mAdapter);
      }

   }

   public void setAnchorView(View var1) {
      this.mDropDownAnchorView = var1;
   }

   public void setAnimationStyle(int var1) {
      this.mPopup.setAnimationStyle(var1);
   }

   public void setBackgroundDrawable(Drawable var1) {
      this.mPopup.setBackgroundDrawable(var1);
   }

   public void setContentWidth(int var1) {
      Drawable var2 = this.mPopup.getBackground();
      if (var2 != null) {
         var2.getPadding(this.mTempRect);
         this.mDropDownWidth = this.mTempRect.left + this.mTempRect.right + var1;
      } else {
         this.setWidth(var1);
      }
   }

   public void setDropDownAlwaysVisible(boolean var1) {
      this.mDropDownAlwaysVisible = var1;
   }

   public void setDropDownGravity(int var1) {
      this.mDropDownGravity = var1;
   }

   public void setEpicenterBounds(Rect var1) {
      if (var1 != null) {
         var1 = new Rect(var1);
      } else {
         var1 = null;
      }

      this.mEpicenterBounds = var1;
   }

   public void setForceIgnoreOutsideTouch(boolean var1) {
      this.mForceIgnoreOutsideTouch = var1;
   }

   public void setHeight(int var1) {
      if (var1 < 0 && -2 != var1 && -1 != var1) {
         throw new IllegalArgumentException("Invalid height. Must be a positive value, MATCH_PARENT, or WRAP_CONTENT.");
      } else {
         this.mDropDownHeight = var1;
      }
   }

   public void setHorizontalOffset(int var1) {
      this.mDropDownHorizontalOffset = var1;
   }

   public void setInputMethodMode(int var1) {
      this.mPopup.setInputMethodMode(var1);
   }

   void setListItemExpandMax(int var1) {
      this.mListItemExpandMaximum = var1;
   }

   public void setListSelector(Drawable var1) {
      this.mDropDownListHighlight = var1;
   }

   public void setModal(boolean var1) {
      this.mModal = var1;
      this.mPopup.setFocusable(var1);
   }

   public void setOnDismissListener(OnDismissListener var1) {
      this.mPopup.setOnDismissListener(var1);
   }

   public void setOnItemClickListener(OnItemClickListener var1) {
      this.mItemClickListener = var1;
   }

   public void setOnItemSelectedListener(OnItemSelectedListener var1) {
      this.mItemSelectedListener = var1;
   }

   public void setOverlapAnchor(boolean var1) {
      this.mOverlapAnchorSet = true;
      this.mOverlapAnchor = var1;
   }

   public void setPromptPosition(int var1) {
      this.mPromptPosition = var1;
   }

   public void setPromptView(View var1) {
      boolean var2 = this.isShowing();
      if (var2) {
         this.removePromptView();
      }

      this.mPromptView = var1;
      if (var2) {
         this.show();
      }

   }

   public void setSelection(int var1) {
      DropDownListView var2 = this.mDropDownList;
      if (this.isShowing() && var2 != null) {
         var2.setListSelectionHidden(false);
         var2.setSelection(var1);
         if (var2.getChoiceMode() != 0) {
            var2.setItemChecked(var1, true);
         }
      }

   }

   public void setSoftInputMode(int var1) {
      this.mPopup.setSoftInputMode(var1);
   }

   public void setVerticalOffset(int var1) {
      this.mDropDownVerticalOffset = var1;
      this.mDropDownVerticalOffsetSet = true;
   }

   public void setWidth(int var1) {
      this.mDropDownWidth = var1;
   }

   public void setWindowLayoutType(int var1) {
      this.mDropDownWindowLayoutType = var1;
   }

   public void show() {
      int var2 = this.buildDropDown();
      boolean var6 = this.isInputMethodNotNeeded();
      PopupWindowCompat.setWindowLayoutType(this.mPopup, this.mDropDownWindowLayoutType);
      boolean var7 = this.mPopup.isShowing();
      boolean var5 = true;
      int var1;
      int var3;
      PopupWindow var8;
      if (var7) {
         if (ViewCompat.isAttachedToWindow(this.getAnchorView())) {
            var1 = this.mDropDownWidth;
            if (var1 == -1) {
               var1 = -1;
            } else if (var1 == -2) {
               var1 = this.getAnchorView().getWidth();
            } else {
               var1 = this.mDropDownWidth;
            }

            var3 = this.mDropDownHeight;
            if (var3 == -1) {
               if (!var6) {
                  var2 = -1;
               }

               byte var11;
               if (var6) {
                  var8 = this.mPopup;
                  if (this.mDropDownWidth == -1) {
                     var11 = -1;
                  } else {
                     var11 = 0;
                  }

                  var8.setWidth(var11);
                  this.mPopup.setHeight(0);
               } else {
                  var8 = this.mPopup;
                  if (this.mDropDownWidth == -1) {
                     var11 = -1;
                  } else {
                     var11 = 0;
                  }

                  var8.setWidth(var11);
                  this.mPopup.setHeight(-1);
               }
            } else if (var3 != -2) {
               var2 = this.mDropDownHeight;
            }

            var8 = this.mPopup;
            if (this.mForceIgnoreOutsideTouch || this.mDropDownAlwaysVisible) {
               var5 = false;
            }

            var8.setOutsideTouchable(var5);
            var8 = this.mPopup;
            View var9 = this.getAnchorView();
            var3 = this.mDropDownHorizontalOffset;
            int var4 = this.mDropDownVerticalOffset;
            if (var1 < 0) {
               var1 = -1;
            }

            if (var2 < 0) {
               var2 = -1;
            }

            var8.update(var9, var3, var4, var1, var2);
         }
      } else {
         var1 = this.mDropDownWidth;
         if (var1 == -1) {
            var1 = -1;
         } else if (var1 == -2) {
            var1 = this.getAnchorView().getWidth();
         } else {
            var1 = this.mDropDownWidth;
         }

         var3 = this.mDropDownHeight;
         if (var3 == -1) {
            var2 = -1;
         } else if (var3 != -2) {
            var2 = this.mDropDownHeight;
         }

         this.mPopup.setWidth(var1);
         this.mPopup.setHeight(var2);
         this.setPopupClipToScreenEnabled(true);
         var8 = this.mPopup;
         if (!this.mForceIgnoreOutsideTouch && !this.mDropDownAlwaysVisible) {
            var5 = true;
         } else {
            var5 = false;
         }

         var8.setOutsideTouchable(var5);
         this.mPopup.setTouchInterceptor(this.mTouchInterceptor);
         if (this.mOverlapAnchorSet) {
            PopupWindowCompat.setOverlapAnchor(this.mPopup, this.mOverlapAnchor);
         }

         if (VERSION.SDK_INT <= 28) {
            Method var12 = sSetEpicenterBoundsMethod;
            if (var12 != null) {
               try {
                  var12.invoke(this.mPopup, this.mEpicenterBounds);
               } catch (Exception var10) {
                  Log.e("ListPopupWindow", "Could not invoke setEpicenterBounds on PopupWindow", var10);
               }
            }
         } else {
            this.mPopup.setEpicenterBounds(this.mEpicenterBounds);
         }

         PopupWindowCompat.showAsDropDown(this.mPopup, this.getAnchorView(), this.mDropDownHorizontalOffset, this.mDropDownVerticalOffset, this.mDropDownGravity);
         this.mDropDownList.setSelection(-1);
         if (!this.mModal || this.mDropDownList.isInTouchMode()) {
            this.clearListSelection();
         }

         if (!this.mModal) {
            this.mHandler.post(this.mHideSelector);
         }

      }
   }

   private class ListSelectorHider implements Runnable {
      ListSelectorHider() {
      }

      public void run() {
         ListPopupWindow.this.clearListSelection();
      }
   }

   private class PopupDataSetObserver extends DataSetObserver {
      PopupDataSetObserver() {
      }

      public void onChanged() {
         if (ListPopupWindow.this.isShowing()) {
            ListPopupWindow.this.show();
         }

      }

      public void onInvalidated() {
         ListPopupWindow.this.dismiss();
      }
   }

   private class PopupScrollListener implements OnScrollListener {
      PopupScrollListener() {
      }

      public void onScroll(AbsListView var1, int var2, int var3, int var4) {
      }

      public void onScrollStateChanged(AbsListView var1, int var2) {
         if (var2 == 1 && !ListPopupWindow.this.isInputMethodNotNeeded() && ListPopupWindow.this.mPopup.getContentView() != null) {
            ListPopupWindow.this.mHandler.removeCallbacks(ListPopupWindow.this.mResizePopupRunnable);
            ListPopupWindow.this.mResizePopupRunnable.run();
         }

      }
   }

   private class PopupTouchInterceptor implements OnTouchListener {
      PopupTouchInterceptor() {
      }

      public boolean onTouch(View var1, MotionEvent var2) {
         int var3 = var2.getAction();
         int var4 = (int)var2.getX();
         int var5 = (int)var2.getY();
         if (var3 == 0 && ListPopupWindow.this.mPopup != null && ListPopupWindow.this.mPopup.isShowing() && var4 >= 0 && var4 < ListPopupWindow.this.mPopup.getWidth() && var5 >= 0 && var5 < ListPopupWindow.this.mPopup.getHeight()) {
            ListPopupWindow.this.mHandler.postDelayed(ListPopupWindow.this.mResizePopupRunnable, 250L);
         } else if (var3 == 1) {
            ListPopupWindow.this.mHandler.removeCallbacks(ListPopupWindow.this.mResizePopupRunnable);
         }

         return false;
      }
   }

   private class ResizePopupRunnable implements Runnable {
      ResizePopupRunnable() {
      }

      public void run() {
         if (ListPopupWindow.this.mDropDownList != null && ViewCompat.isAttachedToWindow(ListPopupWindow.this.mDropDownList) && ListPopupWindow.this.mDropDownList.getCount() > ListPopupWindow.this.mDropDownList.getChildCount() && ListPopupWindow.this.mDropDownList.getChildCount() <= ListPopupWindow.this.mListItemExpandMaximum) {
            ListPopupWindow.this.mPopup.setInputMethodMode(2);
            ListPopupWindow.this.show();
         }

      }
   }
}
