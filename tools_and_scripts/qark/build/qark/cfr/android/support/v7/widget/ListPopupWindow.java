/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.database.DataSetObserver
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.os.Handler
 *  android.os.IBinder
 *  android.os.Looper
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.util.Log
 *  android.view.KeyEvent
 *  android.view.KeyEvent$DispatcherState
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.View$OnTouchListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewParent
 *  android.widget.AbsListView
 *  android.widget.AbsListView$OnScrollListener
 *  android.widget.AdapterView
 *  android.widget.AdapterView$OnItemClickListener
 *  android.widget.AdapterView$OnItemSelectedListener
 *  android.widget.LinearLayout
 *  android.widget.LinearLayout$LayoutParams
 *  android.widget.ListAdapter
 *  android.widget.ListView
 *  android.widget.PopupWindow
 *  android.widget.PopupWindow$OnDismissListener
 */
package android.support.v7.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleRes;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.appcompat.R;
import android.support.v7.view.menu.ShowableListMenu;
import android.support.v7.widget.AppCompatPopupWindow;
import android.support.v7.widget.DropDownListView;
import android.support.v7.widget.ForwardingListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import java.lang.reflect.Method;

public class ListPopupWindow
implements ShowableListMenu {
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
    private static Method sClipToWindowEnabledMethod;
    private static Method sGetMaxAvailableHeightMethod;
    private static Method sSetEpicenterBoundsMethod;
    private ListAdapter mAdapter;
    private Context mContext;
    private boolean mDropDownAlwaysVisible = false;
    private View mDropDownAnchorView;
    private int mDropDownGravity = 0;
    private int mDropDownHeight = -2;
    private int mDropDownHorizontalOffset;
    DropDownListView mDropDownList;
    private Drawable mDropDownListHighlight;
    private int mDropDownVerticalOffset;
    private boolean mDropDownVerticalOffsetSet;
    private int mDropDownWidth = -2;
    private int mDropDownWindowLayoutType = 1002;
    private Rect mEpicenterBounds;
    private boolean mForceIgnoreOutsideTouch = false;
    final Handler mHandler;
    private final ListSelectorHider mHideSelector;
    private boolean mIsAnimatedFromAnchor = true;
    private AdapterView.OnItemClickListener mItemClickListener;
    private AdapterView.OnItemSelectedListener mItemSelectedListener;
    int mListItemExpandMaximum = Integer.MAX_VALUE;
    private boolean mModal;
    private DataSetObserver mObserver;
    private boolean mOverlapAnchor;
    private boolean mOverlapAnchorSet;
    PopupWindow mPopup;
    private int mPromptPosition = 0;
    private View mPromptView;
    final ResizePopupRunnable mResizePopupRunnable;
    private final PopupScrollListener mScrollListener;
    private Runnable mShowDropDownRunnable;
    private final Rect mTempRect;
    private final PopupTouchInterceptor mTouchInterceptor;

    static {
        try {
            sClipToWindowEnabledMethod = PopupWindow.class.getDeclaredMethod("setClipToScreenEnabled", Boolean.TYPE);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            Log.i((String)"ListPopupWindow", (String)"Could not find method setClipToScreenEnabled() on PopupWindow. Oh well.");
        }
        try {
            sGetMaxAvailableHeightMethod = PopupWindow.class.getDeclaredMethod("getMaxAvailableHeight", View.class, Integer.TYPE, Boolean.TYPE);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            Log.i((String)"ListPopupWindow", (String)"Could not find method getMaxAvailableHeight(View, int, boolean) on PopupWindow. Oh well.");
        }
        try {
            sSetEpicenterBoundsMethod = PopupWindow.class.getDeclaredMethod("setEpicenterBounds", Rect.class);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            Log.i((String)"ListPopupWindow", (String)"Could not find method setEpicenterBounds(Rect) on PopupWindow. Oh well.");
        }
    }

    public ListPopupWindow(@NonNull Context context) {
        this(context, null, R.attr.listPopupWindowStyle);
    }

    public ListPopupWindow(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.listPopupWindowStyle);
    }

    public ListPopupWindow(@NonNull Context context, @Nullable AttributeSet attributeSet, @AttrRes int n) {
        this(context, attributeSet, n, 0);
    }

    public ListPopupWindow(@NonNull Context context, @Nullable AttributeSet attributeSet, @AttrRes int n, @StyleRes int n2) {
        this.mResizePopupRunnable = new ResizePopupRunnable();
        this.mTouchInterceptor = new PopupTouchInterceptor();
        this.mScrollListener = new PopupScrollListener();
        this.mHideSelector = new ListSelectorHider();
        this.mTempRect = new Rect();
        this.mContext = context;
        this.mHandler = new Handler(context.getMainLooper());
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ListPopupWindow, n, n2);
        this.mDropDownHorizontalOffset = typedArray.getDimensionPixelOffset(R.styleable.ListPopupWindow_android_dropDownHorizontalOffset, 0);
        this.mDropDownVerticalOffset = typedArray.getDimensionPixelOffset(R.styleable.ListPopupWindow_android_dropDownVerticalOffset, 0);
        if (this.mDropDownVerticalOffset != 0) {
            this.mDropDownVerticalOffsetSet = true;
        }
        typedArray.recycle();
        this.mPopup = new AppCompatPopupWindow(context, attributeSet, n, n2);
        this.mPopup.setInputMethodMode(1);
    }

    private int buildDropDown() {
        int n;
        int n2 = 0;
        int n3 = 0;
        Object object = this.mDropDownList;
        boolean bl = false;
        if (object == null) {
            object = this.mContext;
            this.mShowDropDownRunnable = new Runnable(){

                @Override
                public void run() {
                    View view = ListPopupWindow.this.getAnchorView();
                    if (view != null && view.getWindowToken() != null) {
                        ListPopupWindow.this.show();
                        return;
                    }
                }
            };
            this.mDropDownList = this.createDropDownListView((Context)object, this.mModal ^ true);
            Object object2 = this.mDropDownListHighlight;
            if (object2 != null) {
                this.mDropDownList.setSelector((Drawable)object2);
            }
            this.mDropDownList.setAdapter(this.mAdapter);
            this.mDropDownList.setOnItemClickListener(this.mItemClickListener);
            this.mDropDownList.setFocusable(true);
            this.mDropDownList.setFocusableInTouchMode(true);
            this.mDropDownList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                public void onItemSelected(AdapterView<?> object, View view, int n, long l) {
                    if (n != -1) {
                        object = ListPopupWindow.this.mDropDownList;
                        if (object != null) {
                            object.setListSelectionHidden(false);
                            return;
                        }
                        return;
                    }
                }

                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
            this.mDropDownList.setOnScrollListener((AbsListView.OnScrollListener)this.mScrollListener);
            object2 = this.mItemSelectedListener;
            if (object2 != null) {
                this.mDropDownList.setOnItemSelectedListener((AdapterView.OnItemSelectedListener)object2);
            }
            object2 = this.mDropDownList;
            View view = this.mPromptView;
            if (view != null) {
                object = new LinearLayout((Context)object);
                object.setOrientation(1);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, 0, 1.0f);
                switch (this.mPromptPosition) {
                    default: {
                        object2 = new StringBuilder();
                        object2.append("Invalid hint position ");
                        object2.append(this.mPromptPosition);
                        Log.e((String)"ListPopupWindow", (String)object2.toString());
                        break;
                    }
                    case 1: {
                        object.addView((View)object2, (ViewGroup.LayoutParams)layoutParams);
                        object.addView(view);
                        break;
                    }
                    case 0: {
                        object.addView(view);
                        object.addView((View)object2, (ViewGroup.LayoutParams)layoutParams);
                    }
                }
                if (this.mDropDownWidth >= 0) {
                    n3 = Integer.MIN_VALUE;
                    n2 = this.mDropDownWidth;
                } else {
                    n3 = 0;
                    n2 = 0;
                }
                view.measure(View.MeasureSpec.makeMeasureSpec((int)n2, (int)n3), 0);
                object2 = (LinearLayout.LayoutParams)view.getLayoutParams();
                n3 = view.getMeasuredHeight();
                n2 = object2.topMargin;
                n = object2.bottomMargin;
                n3 = n3 + n2 + n;
            } else {
                object = object2;
            }
            this.mPopup.setContentView((View)object);
        } else {
            object = (ViewGroup)this.mPopup.getContentView();
            object = this.mPromptView;
            if (object != null) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)object.getLayoutParams();
                n3 = object.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
            } else {
                n3 = n2;
            }
        }
        object = this.mPopup.getBackground();
        if (object != null) {
            object.getPadding(this.mTempRect);
            n = this.mTempRect.top + this.mTempRect.bottom;
            if (!this.mDropDownVerticalOffsetSet) {
                this.mDropDownVerticalOffset = - this.mTempRect.top;
            }
        } else {
            this.mTempRect.setEmpty();
            n = 0;
        }
        if (this.mPopup.getInputMethodMode() == 2) {
            bl = true;
        }
        int n4 = this.getMaxAvailableHeight(this.getAnchorView(), this.mDropDownVerticalOffset, bl);
        if (!this.mDropDownAlwaysVisible && this.mDropDownHeight != -1) {
            n2 = this.mDropDownWidth;
            switch (n2) {
                default: {
                    n2 = View.MeasureSpec.makeMeasureSpec((int)n2, (int)1073741824);
                    break;
                }
                case -1: {
                    n2 = View.MeasureSpec.makeMeasureSpec((int)(this.mContext.getResources().getDisplayMetrics().widthPixels - (this.mTempRect.left + this.mTempRect.right)), (int)1073741824);
                    break;
                }
                case -2: {
                    n2 = View.MeasureSpec.makeMeasureSpec((int)(this.mContext.getResources().getDisplayMetrics().widthPixels - (this.mTempRect.left + this.mTempRect.right)), (int)Integer.MIN_VALUE);
                }
            }
            n2 = this.mDropDownList.measureHeightOfChildrenCompat(n2, 0, -1, n4 - n3, -1);
            if (n2 > 0) {
                n3 += n + (this.mDropDownList.getPaddingTop() + this.mDropDownList.getPaddingBottom());
            }
            return n2 + n3;
        }
        return n4 + n;
    }

    private int getMaxAvailableHeight(View view, int n, boolean bl) {
        Method method = sGetMaxAvailableHeightMethod;
        if (method != null) {
            try {
                int n2 = (Integer)method.invoke((Object)this.mPopup, new Object[]{view, n, bl});
                return n2;
            }
            catch (Exception exception) {
                Log.i((String)"ListPopupWindow", (String)"Could not call getMaxAvailableHeightMethod(View, int, boolean) on PopupWindow. Using the public version.");
            }
        }
        return this.mPopup.getMaxAvailableHeight(view, n);
    }

    private static boolean isConfirmKey(int n) {
        if (n != 66 && n != 23) {
            return false;
        }
        return true;
    }

    private void removePromptView() {
        View view = this.mPromptView;
        if (view != null) {
            if ((view = view.getParent()) instanceof ViewGroup) {
                ((ViewGroup)view).removeView(this.mPromptView);
                return;
            }
            return;
        }
    }

    private void setPopupClipToScreenEnabled(boolean bl) {
        Method method = sClipToWindowEnabledMethod;
        if (method != null) {
            try {
                method.invoke((Object)this.mPopup, bl);
                return;
            }
            catch (Exception exception) {
                Log.i((String)"ListPopupWindow", (String)"Could not call setClipToScreenEnabled() on PopupWindow. Oh well.");
                return;
            }
        }
    }

    public void clearListSelection() {
        DropDownListView dropDownListView = this.mDropDownList;
        if (dropDownListView != null) {
            dropDownListView.setListSelectionHidden(true);
            dropDownListView.requestLayout();
            return;
        }
    }

    public View.OnTouchListener createDragToOpenListener(View view) {
        return new ForwardingListener(view){

            @Override
            public ListPopupWindow getPopup() {
                return ListPopupWindow.this;
            }
        };
    }

    @NonNull
    DropDownListView createDropDownListView(Context context, boolean bl) {
        return new DropDownListView(context, bl);
    }

    @Override
    public void dismiss() {
        this.mPopup.dismiss();
        this.removePromptView();
        this.mPopup.setContentView(null);
        this.mDropDownList = null;
        this.mHandler.removeCallbacks((Runnable)this.mResizePopupRunnable);
    }

    @Nullable
    public View getAnchorView() {
        return this.mDropDownAnchorView;
    }

    @StyleRes
    public int getAnimationStyle() {
        return this.mPopup.getAnimationStyle();
    }

    @Nullable
    public Drawable getBackground() {
        return this.mPopup.getBackground();
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

    @Nullable
    @Override
    public ListView getListView() {
        return this.mDropDownList;
    }

    public int getPromptPosition() {
        return this.mPromptPosition;
    }

    @Nullable
    public Object getSelectedItem() {
        if (!this.isShowing()) {
            return null;
        }
        return this.mDropDownList.getSelectedItem();
    }

    public long getSelectedItemId() {
        if (!this.isShowing()) {
            return Long.MIN_VALUE;
        }
        return this.mDropDownList.getSelectedItemId();
    }

    public int getSelectedItemPosition() {
        if (!this.isShowing()) {
            return -1;
        }
        return this.mDropDownList.getSelectedItemPosition();
    }

    @Nullable
    public View getSelectedView() {
        if (!this.isShowing()) {
            return null;
        }
        return this.mDropDownList.getSelectedView();
    }

    public int getSoftInputMode() {
        return this.mPopup.getSoftInputMode();
    }

    public int getVerticalOffset() {
        if (!this.mDropDownVerticalOffsetSet) {
            return 0;
        }
        return this.mDropDownVerticalOffset;
    }

    public int getWidth() {
        return this.mDropDownWidth;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public boolean isDropDownAlwaysVisible() {
        return this.mDropDownAlwaysVisible;
    }

    public boolean isInputMethodNotNeeded() {
        if (this.mPopup.getInputMethodMode() == 2) {
            return true;
        }
        return false;
    }

    public boolean isModal() {
        return this.mModal;
    }

    @Override
    public boolean isShowing() {
        return this.mPopup.isShowing();
    }

    public boolean onKeyDown(int n, @NonNull KeyEvent keyEvent) {
        block13 : {
            block14 : {
                int n2;
                boolean bl;
                int n3;
                int n4;
                block16 : {
                    block15 : {
                        if (!this.isShowing()) break block13;
                        if (n == 62) break block14;
                        if (this.mDropDownList.getSelectedItemPosition() < 0 && ListPopupWindow.isConfirmKey(n)) {
                            return false;
                        }
                        n3 = this.mDropDownList.getSelectedItemPosition();
                        bl = this.mPopup.isAboveAnchor() ^ true;
                        ListAdapter listAdapter = this.mAdapter;
                        n2 = Integer.MAX_VALUE;
                        n4 = Integer.MIN_VALUE;
                        if (listAdapter != null) {
                            boolean bl2 = listAdapter.areAllItemsEnabled();
                            n4 = bl2 ? 0 : this.mDropDownList.lookForSelectablePosition(0, true);
                            n2 = n4;
                            n4 = bl2 ? listAdapter.getCount() - 1 : this.mDropDownList.lookForSelectablePosition(listAdapter.getCount() - 1, false);
                        }
                        if (bl && n == 19 && n3 <= n2) break block15;
                        if (bl || n != 20 || n3 < n4) break block16;
                    }
                    this.clearListSelection();
                    this.mPopup.setInputMethodMode(1);
                    this.show();
                    return true;
                }
                this.mDropDownList.setListSelectionHidden(false);
                if (this.mDropDownList.onKeyDown(n, keyEvent)) {
                    this.mPopup.setInputMethodMode(2);
                    this.mDropDownList.requestFocusFromTouch();
                    this.show();
                    if (n != 23 && n != 66) {
                        switch (n) {
                            default: {
                                return false;
                            }
                            case 19: 
                            case 20: 
                        }
                    }
                    return true;
                }
                if (bl && n == 20) {
                    if (n3 == n4) {
                        return true;
                    }
                    return false;
                }
                if (!bl && n == 19 && n3 == n2) {
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public boolean onKeyPreIme(int n, @NonNull KeyEvent keyEvent) {
        if (n == 4 && this.isShowing()) {
            View view = this.mDropDownAnchorView;
            if (keyEvent.getAction() == 0 && keyEvent.getRepeatCount() == 0) {
                if ((view = view.getKeyDispatcherState()) != null) {
                    view.startTracking(keyEvent, (Object)this);
                    return true;
                }
                return true;
            }
            if (keyEvent.getAction() == 1) {
                if ((view = view.getKeyDispatcherState()) != null) {
                    view.handleUpEvent(keyEvent);
                }
                if (keyEvent.isTracking() && !keyEvent.isCanceled()) {
                    this.dismiss();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean onKeyUp(int n, @NonNull KeyEvent keyEvent) {
        if (this.isShowing() && this.mDropDownList.getSelectedItemPosition() >= 0) {
            boolean bl = this.mDropDownList.onKeyUp(n, keyEvent);
            if (bl && ListPopupWindow.isConfirmKey(n)) {
                this.dismiss();
                return bl;
            }
            return bl;
        }
        return false;
    }

    public boolean performItemClick(int n) {
        if (this.isShowing()) {
            if (this.mItemClickListener != null) {
                DropDownListView dropDownListView = this.mDropDownList;
                View view = dropDownListView.getChildAt(n - dropDownListView.getFirstVisiblePosition());
                ListAdapter listAdapter = dropDownListView.getAdapter();
                this.mItemClickListener.onItemClick((AdapterView)dropDownListView, view, n, listAdapter.getItemId(n));
            }
            return true;
        }
        return false;
    }

    public void postShow() {
        this.mHandler.post(this.mShowDropDownRunnable);
    }

    public void setAdapter(@Nullable ListAdapter object) {
        DataSetObserver dataSetObserver = this.mObserver;
        if (dataSetObserver == null) {
            this.mObserver = new PopupDataSetObserver();
        } else {
            ListAdapter listAdapter = this.mAdapter;
            if (listAdapter != null) {
                listAdapter.unregisterDataSetObserver(dataSetObserver);
            }
        }
        this.mAdapter = object;
        if (this.mAdapter != null) {
            object.registerDataSetObserver(this.mObserver);
        }
        if ((object = this.mDropDownList) != null) {
            object.setAdapter(this.mAdapter);
            return;
        }
    }

    public void setAnchorView(@Nullable View view) {
        this.mDropDownAnchorView = view;
    }

    public void setAnimationStyle(@StyleRes int n) {
        this.mPopup.setAnimationStyle(n);
    }

    public void setBackgroundDrawable(@Nullable Drawable drawable2) {
        this.mPopup.setBackgroundDrawable(drawable2);
    }

    public void setContentWidth(int n) {
        Drawable drawable2 = this.mPopup.getBackground();
        if (drawable2 != null) {
            drawable2.getPadding(this.mTempRect);
            this.mDropDownWidth = this.mTempRect.left + this.mTempRect.right + n;
            return;
        }
        this.setWidth(n);
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public void setDropDownAlwaysVisible(boolean bl) {
        this.mDropDownAlwaysVisible = bl;
    }

    public void setDropDownGravity(int n) {
        this.mDropDownGravity = n;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public void setEpicenterBounds(Rect rect) {
        this.mEpicenterBounds = rect;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public void setForceIgnoreOutsideTouch(boolean bl) {
        this.mForceIgnoreOutsideTouch = bl;
    }

    public void setHeight(int n) {
        if (n < 0 && -2 != n && -1 != n) {
            throw new IllegalArgumentException("Invalid height. Must be a positive value, MATCH_PARENT, or WRAP_CONTENT.");
        }
        this.mDropDownHeight = n;
    }

    public void setHorizontalOffset(int n) {
        this.mDropDownHorizontalOffset = n;
    }

    public void setInputMethodMode(int n) {
        this.mPopup.setInputMethodMode(n);
    }

    void setListItemExpandMax(int n) {
        this.mListItemExpandMaximum = n;
    }

    public void setListSelector(Drawable drawable2) {
        this.mDropDownListHighlight = drawable2;
    }

    public void setModal(boolean bl) {
        this.mModal = bl;
        this.mPopup.setFocusable(bl);
    }

    public void setOnDismissListener(@Nullable PopupWindow.OnDismissListener onDismissListener) {
        this.mPopup.setOnDismissListener(onDismissListener);
    }

    public void setOnItemClickListener(@Nullable AdapterView.OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    public void setOnItemSelectedListener(@Nullable AdapterView.OnItemSelectedListener onItemSelectedListener) {
        this.mItemSelectedListener = onItemSelectedListener;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public void setOverlapAnchor(boolean bl) {
        this.mOverlapAnchorSet = true;
        this.mOverlapAnchor = bl;
    }

    public void setPromptPosition(int n) {
        this.mPromptPosition = n;
    }

    public void setPromptView(@Nullable View view) {
        boolean bl = this.isShowing();
        if (bl) {
            this.removePromptView();
        }
        this.mPromptView = view;
        if (bl) {
            this.show();
            return;
        }
    }

    public void setSelection(int n) {
        DropDownListView dropDownListView = this.mDropDownList;
        if (this.isShowing() && dropDownListView != null) {
            dropDownListView.setListSelectionHidden(false);
            dropDownListView.setSelection(n);
            if (dropDownListView.getChoiceMode() != 0) {
                dropDownListView.setItemChecked(n, true);
                return;
            }
            return;
        }
    }

    public void setSoftInputMode(int n) {
        this.mPopup.setSoftInputMode(n);
    }

    public void setVerticalOffset(int n) {
        this.mDropDownVerticalOffset = n;
        this.mDropDownVerticalOffsetSet = true;
    }

    public void setWidth(int n) {
        this.mDropDownWidth = n;
    }

    public void setWindowLayoutType(int n) {
        this.mDropDownWindowLayoutType = n;
    }

    @Override
    public void show() {
        int n = this.buildDropDown();
        boolean bl = this.isInputMethodNotNeeded();
        PopupWindowCompat.setWindowLayoutType(this.mPopup, this.mDropDownWindowLayoutType);
        boolean bl2 = this.mPopup.isShowing();
        boolean bl3 = true;
        if (bl2) {
            PopupWindow popupWindow;
            if (!ViewCompat.isAttachedToWindow(this.getAnchorView())) {
                return;
            }
            int n2 = this.mDropDownWidth;
            n2 = n2 == -1 ? -1 : (n2 == -2 ? this.getAnchorView().getWidth() : this.mDropDownWidth);
            int n3 = this.mDropDownHeight;
            if (n3 == -1) {
                if (!bl) {
                    n = -1;
                }
                if (bl) {
                    popupWindow = this.mPopup;
                    n3 = this.mDropDownWidth == -1 ? -1 : 0;
                    popupWindow.setWidth(n3);
                    this.mPopup.setHeight(0);
                } else {
                    popupWindow = this.mPopup;
                    n3 = this.mDropDownWidth == -1 ? -1 : 0;
                    popupWindow.setWidth(n3);
                    this.mPopup.setHeight(-1);
                }
            } else if (n3 != -2) {
                n = this.mDropDownHeight;
            }
            popupWindow = this.mPopup;
            if (this.mForceIgnoreOutsideTouch || this.mDropDownAlwaysVisible) {
                bl3 = false;
            }
            popupWindow.setOutsideTouchable(bl3);
            popupWindow = this.mPopup;
            View view = this.getAnchorView();
            n3 = this.mDropDownHorizontalOffset;
            int n4 = this.mDropDownVerticalOffset;
            if (n2 < 0) {
                n2 = -1;
            }
            if (n < 0) {
                n = -1;
            }
            popupWindow.update(view, n3, n4, n2, n);
            return;
        }
        int n5 = this.mDropDownWidth;
        n5 = n5 == -1 ? -1 : (n5 == -2 ? this.getAnchorView().getWidth() : this.mDropDownWidth);
        int n6 = this.mDropDownHeight;
        if (n6 == -1) {
            n = -1;
        } else if (n6 != -2) {
            n = this.mDropDownHeight;
        }
        this.mPopup.setWidth(n5);
        this.mPopup.setHeight(n);
        this.setPopupClipToScreenEnabled(true);
        Object object = this.mPopup;
        bl3 = !this.mForceIgnoreOutsideTouch && !this.mDropDownAlwaysVisible;
        object.setOutsideTouchable(bl3);
        this.mPopup.setTouchInterceptor((View.OnTouchListener)this.mTouchInterceptor);
        if (this.mOverlapAnchorSet) {
            PopupWindowCompat.setOverlapAnchor(this.mPopup, this.mOverlapAnchor);
        }
        if ((object = sSetEpicenterBoundsMethod) != null) {
            try {
                object.invoke((Object)this.mPopup, new Object[]{this.mEpicenterBounds});
            }
            catch (Exception exception) {
                Log.e((String)"ListPopupWindow", (String)"Could not invoke setEpicenterBounds on PopupWindow", (Throwable)exception);
            }
        }
        PopupWindowCompat.showAsDropDown(this.mPopup, this.getAnchorView(), this.mDropDownHorizontalOffset, this.mDropDownVerticalOffset, this.mDropDownGravity);
        this.mDropDownList.setSelection(-1);
        if (!this.mModal || this.mDropDownList.isInTouchMode()) {
            this.clearListSelection();
        }
        if (!this.mModal) {
            this.mHandler.post((Runnable)this.mHideSelector);
            return;
        }
    }

    private class ListSelectorHider
    implements Runnable {
        ListSelectorHider() {
        }

        @Override
        public void run() {
            ListPopupWindow.this.clearListSelection();
        }
    }

    private class PopupDataSetObserver
    extends DataSetObserver {
        PopupDataSetObserver() {
        }

        public void onChanged() {
            if (ListPopupWindow.this.isShowing()) {
                ListPopupWindow.this.show();
                return;
            }
        }

        public void onInvalidated() {
            ListPopupWindow.this.dismiss();
        }
    }

    private class PopupScrollListener
    implements AbsListView.OnScrollListener {
        PopupScrollListener() {
        }

        public void onScroll(AbsListView absListView, int n, int n2, int n3) {
        }

        public void onScrollStateChanged(AbsListView absListView, int n) {
            if (n == 1) {
                if (!ListPopupWindow.this.isInputMethodNotNeeded() && ListPopupWindow.this.mPopup.getContentView() != null) {
                    ListPopupWindow.this.mHandler.removeCallbacks((Runnable)ListPopupWindow.this.mResizePopupRunnable);
                    ListPopupWindow.this.mResizePopupRunnable.run();
                    return;
                }
                return;
            }
        }
    }

    private class PopupTouchInterceptor
    implements View.OnTouchListener {
        PopupTouchInterceptor() {
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            int n = motionEvent.getAction();
            int n2 = (int)motionEvent.getX();
            int n3 = (int)motionEvent.getY();
            if (n == 0 && ListPopupWindow.this.mPopup != null && ListPopupWindow.this.mPopup.isShowing() && n2 >= 0 && n2 < ListPopupWindow.this.mPopup.getWidth() && n3 >= 0 && n3 < ListPopupWindow.this.mPopup.getHeight()) {
                ListPopupWindow.this.mHandler.postDelayed((Runnable)ListPopupWindow.this.mResizePopupRunnable, 250L);
            } else if (n == 1) {
                ListPopupWindow.this.mHandler.removeCallbacks((Runnable)ListPopupWindow.this.mResizePopupRunnable);
            }
            return false;
        }
    }

    private class ResizePopupRunnable
    implements Runnable {
        ResizePopupRunnable() {
        }

        @Override
        public void run() {
            if (ListPopupWindow.this.mDropDownList != null && ViewCompat.isAttachedToWindow((View)ListPopupWindow.this.mDropDownList)) {
                if (ListPopupWindow.this.mDropDownList.getCount() > ListPopupWindow.this.mDropDownList.getChildCount()) {
                    if (ListPopupWindow.this.mDropDownList.getChildCount() <= ListPopupWindow.this.mListItemExpandMaximum) {
                        ListPopupWindow.this.mPopup.setInputMethodMode(2);
                        ListPopupWindow.this.show();
                        return;
                    }
                    return;
                }
                return;
            }
        }
    }

}

