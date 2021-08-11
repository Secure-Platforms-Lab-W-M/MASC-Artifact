// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.app;

import java.lang.ref.WeakReference;
import android.widget.AdapterView;
import android.widget.AdapterView$OnItemClickListener;
import android.widget.SimpleCursorAdapter;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.ArrayAdapter;
import android.content.DialogInterface$OnKeyListener;
import android.widget.AdapterView$OnItemSelectedListener;
import android.content.DialogInterface$OnDismissListener;
import android.content.DialogInterface$OnMultiChoiceClickListener;
import android.content.DialogInterface$OnCancelListener;
import android.database.Cursor;
import android.content.DialogInterface$OnClickListener;
import android.view.KeyEvent;
import android.util.TypedValue;
import android.support.v7.widget.LinearLayoutCompat;
import android.widget.FrameLayout;
import android.view.LayoutInflater;
import android.text.TextUtils;
import android.widget.AbsListView;
import android.widget.AbsListView$OnScrollListener;
import android.support.v4.view.ViewCompat;
import android.os.Build$VERSION;
import android.view.ViewParent;
import android.view.ViewStub;
import android.support.annotation.Nullable;
import android.view.ViewGroup$LayoutParams;
import android.widget.LinearLayout$LayoutParams;
import android.view.ViewGroup;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.support.v7.appcompat.R;
import android.content.DialogInterface;
import android.view.Window;
import android.support.v4.widget.NestedScrollView;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ImageView;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.content.Context;
import android.os.Message;
import android.widget.Button;
import android.view.View$OnClickListener;
import android.widget.ListAdapter;

class AlertController
{
    ListAdapter mAdapter;
    private int mAlertDialogLayout;
    private final View$OnClickListener mButtonHandler;
    Button mButtonNegative;
    Message mButtonNegativeMessage;
    private CharSequence mButtonNegativeText;
    Button mButtonNeutral;
    Message mButtonNeutralMessage;
    private CharSequence mButtonNeutralText;
    private int mButtonPanelLayoutHint;
    private int mButtonPanelSideLayout;
    Button mButtonPositive;
    Message mButtonPositiveMessage;
    private CharSequence mButtonPositiveText;
    int mCheckedItem;
    private final Context mContext;
    private View mCustomTitleView;
    final AppCompatDialog mDialog;
    Handler mHandler;
    private Drawable mIcon;
    private int mIconId;
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
    private boolean mViewSpacingSpecified;
    private int mViewSpacingTop;
    private final Window mWindow;
    
    public AlertController(final Context mContext, final AppCompatDialog mDialog, final Window mWindow) {
        this.mViewSpacingSpecified = false;
        this.mIconId = 0;
        this.mCheckedItem = -1;
        this.mButtonPanelLayoutHint = 0;
        this.mButtonHandler = (View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                Message message;
                if (view == AlertController.this.mButtonPositive && AlertController.this.mButtonPositiveMessage != null) {
                    message = Message.obtain(AlertController.this.mButtonPositiveMessage);
                }
                else if (view == AlertController.this.mButtonNegative && AlertController.this.mButtonNegativeMessage != null) {
                    message = Message.obtain(AlertController.this.mButtonNegativeMessage);
                }
                else if (view == AlertController.this.mButtonNeutral && AlertController.this.mButtonNeutralMessage != null) {
                    message = Message.obtain(AlertController.this.mButtonNeutralMessage);
                }
                else {
                    message = null;
                }
                if (message != null) {
                    message.sendToTarget();
                }
                AlertController.this.mHandler.obtainMessage(1, (Object)AlertController.this.mDialog).sendToTarget();
            }
        };
        this.mContext = mContext;
        this.mDialog = mDialog;
        this.mWindow = mWindow;
        this.mHandler = new ButtonHandler((DialogInterface)mDialog);
        final TypedArray obtainStyledAttributes = mContext.obtainStyledAttributes((AttributeSet)null, R.styleable.AlertDialog, R.attr.alertDialogStyle, 0);
        this.mAlertDialogLayout = obtainStyledAttributes.getResourceId(R.styleable.AlertDialog_android_layout, 0);
        this.mButtonPanelSideLayout = obtainStyledAttributes.getResourceId(R.styleable.AlertDialog_buttonPanelSideLayout, 0);
        this.mListLayout = obtainStyledAttributes.getResourceId(R.styleable.AlertDialog_listLayout, 0);
        this.mMultiChoiceItemLayout = obtainStyledAttributes.getResourceId(R.styleable.AlertDialog_multiChoiceItemLayout, 0);
        this.mSingleChoiceItemLayout = obtainStyledAttributes.getResourceId(R.styleable.AlertDialog_singleChoiceItemLayout, 0);
        this.mListItemLayout = obtainStyledAttributes.getResourceId(R.styleable.AlertDialog_listItemLayout, 0);
        this.mShowTitle = obtainStyledAttributes.getBoolean(R.styleable.AlertDialog_showTitle, true);
        obtainStyledAttributes.recycle();
        mDialog.supportRequestWindowFeature(1);
    }
    
    static boolean canTextInput(final View view) {
        if (view.onCheckIsTextEditor()) {
            return true;
        }
        if (!(view instanceof ViewGroup)) {
            return false;
        }
        final ViewGroup viewGroup = (ViewGroup)view;
        int i = viewGroup.getChildCount();
        while (i > 0) {
            --i;
            if (canTextInput(viewGroup.getChildAt(i))) {
                return true;
            }
        }
        return false;
    }
    
    private void centerButton(final Button button) {
        final LinearLayout$LayoutParams layoutParams = (LinearLayout$LayoutParams)button.getLayoutParams();
        layoutParams.gravity = 1;
        layoutParams.weight = 0.5f;
        button.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
    }
    
    static void manageScrollIndicators(final View view, final View view2, final View view3) {
        final boolean b = false;
        if (view2 != null) {
            int visibility;
            if (view.canScrollVertically(-1)) {
                visibility = 0;
            }
            else {
                visibility = 4;
            }
            view2.setVisibility(visibility);
        }
        if (view3 != null) {
            int visibility2;
            if (view.canScrollVertically(1)) {
                visibility2 = (b ? 1 : 0);
            }
            else {
                visibility2 = 4;
            }
            view3.setVisibility(visibility2);
        }
    }
    
    @Nullable
    private ViewGroup resolvePanel(@Nullable View inflate, @Nullable View inflate2) {
        if (inflate == null) {
            if (inflate2 instanceof ViewStub) {
                inflate2 = ((ViewStub)inflate2).inflate();
            }
            return (ViewGroup)inflate2;
        }
        if (inflate2 != null) {
            final ViewParent parent = inflate2.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup)parent).removeView(inflate2);
            }
        }
        if (inflate instanceof ViewStub) {
            inflate = ((ViewStub)inflate).inflate();
        }
        return (ViewGroup)inflate;
    }
    
    private int selectContentView() {
        final int mButtonPanelSideLayout = this.mButtonPanelSideLayout;
        if (mButtonPanelSideLayout == 0) {
            return this.mAlertDialogLayout;
        }
        if (this.mButtonPanelLayoutHint == 1) {
            return mButtonPanelSideLayout;
        }
        return this.mAlertDialogLayout;
    }
    
    private void setScrollIndicators(final ViewGroup viewGroup, View view, final int n, final int n2) {
        final View viewById = this.mWindow.findViewById(R.id.scrollIndicatorUp);
        View viewById2 = this.mWindow.findViewById(R.id.scrollIndicatorDown);
        if (Build$VERSION.SDK_INT >= 23) {
            ViewCompat.setScrollIndicators(view, n, n2);
            if (viewById != null) {
                viewGroup.removeView(viewById);
            }
            if (viewById2 != null) {
                viewGroup.removeView(viewById2);
            }
        }
        else {
            if (viewById != null && (n & 0x1) == 0x0) {
                viewGroup.removeView(viewById);
                view = null;
            }
            else {
                view = viewById;
            }
            if (viewById2 != null && (n & 0x2) == 0x0) {
                viewGroup.removeView(viewById2);
                viewById2 = null;
            }
            if (view == null && viewById2 == null) {
                return;
            }
            if (this.mMessage != null) {
                this.mScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener)new NestedScrollView.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(final NestedScrollView nestedScrollView, final int n, final int n2, final int n3, final int n4) {
                        AlertController.manageScrollIndicators((View)nestedScrollView, view, viewById2);
                    }
                });
                this.mScrollView.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        AlertController.manageScrollIndicators((View)AlertController.this.mScrollView, view, viewById2);
                    }
                });
                return;
            }
            final ListView mListView = this.mListView;
            if (mListView != null) {
                mListView.setOnScrollListener((AbsListView$OnScrollListener)new AbsListView$OnScrollListener() {
                    public void onScroll(final AbsListView absListView, final int n, final int n2, final int n3) {
                        AlertController.manageScrollIndicators((View)absListView, view, viewById2);
                    }
                    
                    public void onScrollStateChanged(final AbsListView absListView, final int n) {
                    }
                });
                this.mListView.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        AlertController.manageScrollIndicators((View)AlertController.this.mListView, view, viewById2);
                    }
                });
                return;
            }
            if (view != null) {
                viewGroup.removeView(view);
            }
            if (viewById2 != null) {
                viewGroup.removeView(viewById2);
            }
        }
    }
    
    private void setupButtons(final ViewGroup viewGroup) {
        int n = 0;
        (this.mButtonPositive = (Button)viewGroup.findViewById(16908313)).setOnClickListener(this.mButtonHandler);
        final boolean empty = TextUtils.isEmpty(this.mButtonPositiveText);
        boolean b = false;
        if (empty) {
            this.mButtonPositive.setVisibility(8);
        }
        else {
            this.mButtonPositive.setText(this.mButtonPositiveText);
            this.mButtonPositive.setVisibility(0);
            n = ((false | true) ? 1 : 0);
        }
        (this.mButtonNegative = (Button)viewGroup.findViewById(16908314)).setOnClickListener(this.mButtonHandler);
        if (TextUtils.isEmpty(this.mButtonNegativeText)) {
            this.mButtonNegative.setVisibility(8);
        }
        else {
            this.mButtonNegative.setText(this.mButtonNegativeText);
            this.mButtonNegative.setVisibility(0);
            n |= 0x2;
        }
        (this.mButtonNeutral = (Button)viewGroup.findViewById(16908315)).setOnClickListener(this.mButtonHandler);
        if (TextUtils.isEmpty(this.mButtonNeutralText)) {
            this.mButtonNeutral.setVisibility(8);
        }
        else {
            this.mButtonNeutral.setText(this.mButtonNeutralText);
            this.mButtonNeutral.setVisibility(0);
            n |= 0x4;
        }
        if (shouldCenterSingleButton(this.mContext)) {
            if (n == 1) {
                this.centerButton(this.mButtonPositive);
            }
            else if (n == 2) {
                this.centerButton(this.mButtonNegative);
            }
            else if (n == 4) {
                this.centerButton(this.mButtonNeutral);
            }
        }
        if (n != 0) {
            b = true;
        }
        if (!b) {
            viewGroup.setVisibility(8);
        }
    }
    
    private void setupContent(ViewGroup viewGroup) {
        (this.mScrollView = (NestedScrollView)this.mWindow.findViewById(R.id.scrollView)).setFocusable(false);
        this.mScrollView.setNestedScrollingEnabled(false);
        this.mMessageView = (TextView)viewGroup.findViewById(16908299);
        final TextView mMessageView = this.mMessageView;
        if (mMessageView == null) {
            return;
        }
        final CharSequence mMessage = this.mMessage;
        if (mMessage != null) {
            mMessageView.setText(mMessage);
            return;
        }
        mMessageView.setVisibility(8);
        this.mScrollView.removeView((View)this.mMessageView);
        if (this.mListView != null) {
            viewGroup = (ViewGroup)this.mScrollView.getParent();
            final int indexOfChild = viewGroup.indexOfChild((View)this.mScrollView);
            viewGroup.removeViewAt(indexOfChild);
            viewGroup.addView((View)this.mListView, indexOfChild, new ViewGroup$LayoutParams(-1, -1));
            return;
        }
        viewGroup.setVisibility(8);
    }
    
    private void setupCustomContent(final ViewGroup viewGroup) {
        final View mView = this.mView;
        boolean b = false;
        View view;
        if (mView != null) {
            view = this.mView;
        }
        else if (this.mViewLayoutResId != 0) {
            view = LayoutInflater.from(this.mContext).inflate(this.mViewLayoutResId, viewGroup, false);
        }
        else {
            view = null;
        }
        if (view != null) {
            b = true;
        }
        if (!b || !canTextInput(view)) {
            this.mWindow.setFlags(131072, 131072);
        }
        if (b) {
            final FrameLayout frameLayout = (FrameLayout)this.mWindow.findViewById(R.id.custom);
            frameLayout.addView(view, new ViewGroup$LayoutParams(-1, -1));
            if (this.mViewSpacingSpecified) {
                frameLayout.setPadding(this.mViewSpacingLeft, this.mViewSpacingTop, this.mViewSpacingRight, this.mViewSpacingBottom);
            }
            if (this.mListView != null) {
                ((LinearLayoutCompat.LayoutParams)viewGroup.getLayoutParams()).weight = 0.0f;
            }
            return;
        }
        viewGroup.setVisibility(8);
    }
    
    private void setupTitle(final ViewGroup viewGroup) {
        if (this.mCustomTitleView != null) {
            viewGroup.addView(this.mCustomTitleView, 0, new ViewGroup$LayoutParams(-1, -2));
            this.mWindow.findViewById(R.id.title_template).setVisibility(8);
            return;
        }
        this.mIconView = (ImageView)this.mWindow.findViewById(16908294);
        if (!(TextUtils.isEmpty(this.mTitle) ^ true) || !this.mShowTitle) {
            this.mWindow.findViewById(R.id.title_template).setVisibility(8);
            this.mIconView.setVisibility(8);
            viewGroup.setVisibility(8);
            return;
        }
        (this.mTitleView = (TextView)this.mWindow.findViewById(R.id.alertTitle)).setText(this.mTitle);
        final int mIconId = this.mIconId;
        if (mIconId != 0) {
            this.mIconView.setImageResource(mIconId);
            return;
        }
        final Drawable mIcon = this.mIcon;
        if (mIcon != null) {
            this.mIconView.setImageDrawable(mIcon);
            return;
        }
        this.mTitleView.setPadding(this.mIconView.getPaddingLeft(), this.mIconView.getPaddingTop(), this.mIconView.getPaddingRight(), this.mIconView.getPaddingBottom());
        this.mIconView.setVisibility(8);
    }
    
    private void setupView() {
        final View viewById = this.mWindow.findViewById(R.id.parentPanel);
        final View viewById2 = viewById.findViewById(R.id.topPanel);
        final View viewById3 = viewById.findViewById(R.id.contentPanel);
        final View viewById4 = viewById.findViewById(R.id.buttonPanel);
        final ViewGroup viewGroup = (ViewGroup)viewById.findViewById(R.id.customPanel);
        this.setupCustomContent(viewGroup);
        final View viewById5 = viewGroup.findViewById(R.id.topPanel);
        final View viewById6 = viewGroup.findViewById(R.id.contentPanel);
        final View viewById7 = viewGroup.findViewById(R.id.buttonPanel);
        final ViewGroup resolvePanel = this.resolvePanel(viewById5, viewById2);
        final ViewGroup resolvePanel2 = this.resolvePanel(viewById6, viewById3);
        final ViewGroup resolvePanel3 = this.resolvePanel(viewById7, viewById4);
        this.setupContent(resolvePanel2);
        this.setupButtons(resolvePanel3);
        this.setupTitle(resolvePanel);
        final boolean b = viewGroup != null && viewGroup.getVisibility() != 8;
        final boolean b2 = resolvePanel != null && resolvePanel.getVisibility() != 8;
        final boolean b3 = resolvePanel3 != null && resolvePanel3.getVisibility() != 8;
        if (!b3) {
            if (resolvePanel2 != null) {
                final View viewById8 = resolvePanel2.findViewById(R.id.textSpacerNoButtons);
                if (viewById8 != null) {
                    viewById8.setVisibility(0);
                }
            }
        }
        if (b2) {
            final NestedScrollView mScrollView = this.mScrollView;
            if (mScrollView != null) {
                mScrollView.setClipToPadding(true);
            }
            View viewById9 = null;
            if (this.mMessage != null || this.mListView != null) {
                viewById9 = resolvePanel.findViewById(R.id.titleDividerNoCustom);
            }
            if (viewById9 != null) {
                viewById9.setVisibility(0);
            }
        }
        else if (resolvePanel2 != null) {
            final View viewById10 = resolvePanel2.findViewById(R.id.textSpacerNoTitle);
            if (viewById10 != null) {
                viewById10.setVisibility(0);
            }
        }
        final ListView mListView = this.mListView;
        if (mListView instanceof RecycleListView) {
            ((RecycleListView)mListView).setHasDecor(b2, b3);
        }
        if (!b) {
            Object o = this.mListView;
            if (o == null) {
                o = this.mScrollView;
            }
            if (o != null) {
                final boolean b4 = b2;
                int n;
                if (b3) {
                    n = 2;
                }
                else {
                    n = 0;
                }
                this.setScrollIndicators(resolvePanel2, (View)o, (b4 ? 1 : 0) | n, 3);
            }
        }
        final ListView mListView2 = this.mListView;
        if (mListView2 != null) {
            final ListAdapter mAdapter = this.mAdapter;
            if (mAdapter != null) {
                mListView2.setAdapter(mAdapter);
                final int mCheckedItem = this.mCheckedItem;
                if (mCheckedItem > -1) {
                    mListView2.setItemChecked(mCheckedItem, true);
                    mListView2.setSelection(mCheckedItem);
                }
            }
        }
    }
    
    private static boolean shouldCenterSingleButton(final Context context) {
        final TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.alertDialogCenterButtons, typedValue, true);
        return typedValue.data != 0;
    }
    
    public Button getButton(final int n) {
        switch (n) {
            default: {
                return null;
            }
            case -1: {
                return this.mButtonPositive;
            }
            case -2: {
                return this.mButtonNegative;
            }
            case -3: {
                return this.mButtonNeutral;
            }
        }
    }
    
    public int getIconAttributeResId(final int n) {
        final TypedValue typedValue = new TypedValue();
        this.mContext.getTheme().resolveAttribute(n, typedValue, true);
        return typedValue.resourceId;
    }
    
    public ListView getListView() {
        return this.mListView;
    }
    
    public void installContent() {
        this.mDialog.setContentView(this.selectContentView());
        this.setupView();
    }
    
    public boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        final NestedScrollView mScrollView = this.mScrollView;
        return mScrollView != null && mScrollView.executeKeyEvent(keyEvent);
    }
    
    public boolean onKeyUp(final int n, final KeyEvent keyEvent) {
        final NestedScrollView mScrollView = this.mScrollView;
        return mScrollView != null && mScrollView.executeKeyEvent(keyEvent);
    }
    
    public void setButton(final int n, final CharSequence mButtonNeutralText, final DialogInterface$OnClickListener dialogInterface$OnClickListener, Message obtainMessage) {
        if (obtainMessage == null && dialogInterface$OnClickListener != null) {
            obtainMessage = this.mHandler.obtainMessage(n, (Object)dialogInterface$OnClickListener);
        }
        switch (n) {
            default: {
                throw new IllegalArgumentException("Button does not exist");
            }
            case -1: {
                this.mButtonPositiveText = mButtonNeutralText;
                this.mButtonPositiveMessage = obtainMessage;
            }
            case -2: {
                this.mButtonNegativeText = mButtonNeutralText;
                this.mButtonNegativeMessage = obtainMessage;
            }
            case -3: {
                this.mButtonNeutralText = mButtonNeutralText;
                this.mButtonNeutralMessage = obtainMessage;
            }
        }
    }
    
    public void setButtonPanelLayoutHint(final int mButtonPanelLayoutHint) {
        this.mButtonPanelLayoutHint = mButtonPanelLayoutHint;
    }
    
    public void setCustomTitle(final View mCustomTitleView) {
        this.mCustomTitleView = mCustomTitleView;
    }
    
    public void setIcon(final int mIconId) {
        this.mIcon = null;
        this.mIconId = mIconId;
        final ImageView mIconView = this.mIconView;
        if (mIconView == null) {
            return;
        }
        if (mIconId != 0) {
            mIconView.setVisibility(0);
            this.mIconView.setImageResource(this.mIconId);
            return;
        }
        mIconView.setVisibility(8);
    }
    
    public void setIcon(final Drawable drawable) {
        this.mIcon = drawable;
        this.mIconId = 0;
        final ImageView mIconView = this.mIconView;
        if (mIconView == null) {
            return;
        }
        if (drawable != null) {
            mIconView.setVisibility(0);
            this.mIconView.setImageDrawable(drawable);
            return;
        }
        mIconView.setVisibility(8);
    }
    
    public void setMessage(final CharSequence charSequence) {
        this.mMessage = charSequence;
        final TextView mMessageView = this.mMessageView;
        if (mMessageView != null) {
            mMessageView.setText(charSequence);
        }
    }
    
    public void setTitle(final CharSequence charSequence) {
        this.mTitle = charSequence;
        final TextView mTitleView = this.mTitleView;
        if (mTitleView != null) {
            mTitleView.setText(charSequence);
        }
    }
    
    public void setView(final int mViewLayoutResId) {
        this.mView = null;
        this.mViewLayoutResId = mViewLayoutResId;
        this.mViewSpacingSpecified = false;
    }
    
    public void setView(final View mView) {
        this.mView = mView;
        this.mViewLayoutResId = 0;
        this.mViewSpacingSpecified = false;
    }
    
    public void setView(final View mView, final int mViewSpacingLeft, final int mViewSpacingTop, final int mViewSpacingRight, final int mViewSpacingBottom) {
        this.mView = mView;
        this.mViewLayoutResId = 0;
        this.mViewSpacingSpecified = true;
        this.mViewSpacingLeft = mViewSpacingLeft;
        this.mViewSpacingTop = mViewSpacingTop;
        this.mViewSpacingRight = mViewSpacingRight;
        this.mViewSpacingBottom = mViewSpacingBottom;
    }
    
    public static class AlertParams
    {
        public ListAdapter mAdapter;
        public boolean mCancelable;
        public int mCheckedItem;
        public boolean[] mCheckedItems;
        public final Context mContext;
        public Cursor mCursor;
        public View mCustomTitleView;
        public boolean mForceInverseBackground;
        public Drawable mIcon;
        public int mIconAttrId;
        public int mIconId;
        public final LayoutInflater mInflater;
        public String mIsCheckedColumn;
        public boolean mIsMultiChoice;
        public boolean mIsSingleChoice;
        public CharSequence[] mItems;
        public String mLabelColumn;
        public CharSequence mMessage;
        public DialogInterface$OnClickListener mNegativeButtonListener;
        public CharSequence mNegativeButtonText;
        public DialogInterface$OnClickListener mNeutralButtonListener;
        public CharSequence mNeutralButtonText;
        public DialogInterface$OnCancelListener mOnCancelListener;
        public DialogInterface$OnMultiChoiceClickListener mOnCheckboxClickListener;
        public DialogInterface$OnClickListener mOnClickListener;
        public DialogInterface$OnDismissListener mOnDismissListener;
        public AdapterView$OnItemSelectedListener mOnItemSelectedListener;
        public DialogInterface$OnKeyListener mOnKeyListener;
        public OnPrepareListViewListener mOnPrepareListViewListener;
        public DialogInterface$OnClickListener mPositiveButtonListener;
        public CharSequence mPositiveButtonText;
        public boolean mRecycleOnMeasure;
        public CharSequence mTitle;
        public View mView;
        public int mViewLayoutResId;
        public int mViewSpacingBottom;
        public int mViewSpacingLeft;
        public int mViewSpacingRight;
        public boolean mViewSpacingSpecified;
        public int mViewSpacingTop;
        
        public AlertParams(final Context mContext) {
            this.mIconId = 0;
            this.mIconAttrId = 0;
            this.mViewSpacingSpecified = false;
            this.mCheckedItem = -1;
            this.mRecycleOnMeasure = true;
            this.mContext = mContext;
            this.mCancelable = true;
            this.mInflater = (LayoutInflater)mContext.getSystemService("layout_inflater");
        }
        
        private void createListView(final AlertController alertController) {
            final RecycleListView mListView = (RecycleListView)this.mInflater.inflate(alertController.mListLayout, (ViewGroup)null);
            Object mAdapter;
            if (this.mIsMultiChoice) {
                final Cursor mCursor = this.mCursor;
                if (mCursor == null) {
                    mAdapter = new ArrayAdapter<CharSequence>(this.mContext, alertController.mMultiChoiceItemLayout, 16908308, this.mItems) {
                        public View getView(final int n, View view, final ViewGroup viewGroup) {
                            view = super.getView(n, view, viewGroup);
                            if (AlertParams.this.mCheckedItems == null) {
                                return view;
                            }
                            if (AlertParams.this.mCheckedItems[n]) {
                                mListView.setItemChecked(n, true);
                                return view;
                            }
                            return view;
                        }
                    };
                }
                else {
                    mAdapter = new CursorAdapter(this.mContext, mCursor, false) {
                        private final int mIsCheckedIndex;
                        private final int mLabelIndex;
                        
                        {
                            final Cursor cursor2 = this.getCursor();
                            this.mLabelIndex = cursor2.getColumnIndexOrThrow(AlertParams.this.mLabelColumn);
                            this.mIsCheckedIndex = cursor2.getColumnIndexOrThrow(AlertParams.this.mIsCheckedColumn);
                        }
                        
                        public void bindView(final View view, final Context context, final Cursor cursor) {
                            ((CheckedTextView)view.findViewById(16908308)).setText((CharSequence)cursor.getString(this.mLabelIndex));
                            final RecycleListView val$listView = mListView;
                            final int position = cursor.getPosition();
                            final int int1 = cursor.getInt(this.mIsCheckedIndex);
                            boolean b = true;
                            if (int1 != 1) {
                                b = false;
                            }
                            val$listView.setItemChecked(position, b);
                        }
                        
                        public View newView(final Context context, final Cursor cursor, final ViewGroup viewGroup) {
                            return AlertParams.this.mInflater.inflate(alertController.mMultiChoiceItemLayout, viewGroup, false);
                        }
                    };
                }
            }
            else {
                int n;
                if (this.mIsSingleChoice) {
                    n = alertController.mSingleChoiceItemLayout;
                }
                else {
                    n = alertController.mListItemLayout;
                }
                final Cursor mCursor2 = this.mCursor;
                if (mCursor2 != null) {
                    mAdapter = new SimpleCursorAdapter(this.mContext, n, mCursor2, new String[] { this.mLabelColumn }, new int[] { 16908308 });
                }
                else if (this.mAdapter != null) {
                    mAdapter = this.mAdapter;
                }
                else {
                    mAdapter = new CheckedItemAdapter(this.mContext, n, 16908308, this.mItems);
                }
            }
            final OnPrepareListViewListener mOnPrepareListViewListener = this.mOnPrepareListViewListener;
            if (mOnPrepareListViewListener != null) {
                mOnPrepareListViewListener.onPrepareListView(mListView);
            }
            alertController.mAdapter = (ListAdapter)mAdapter;
            alertController.mCheckedItem = this.mCheckedItem;
            if (this.mOnClickListener != null) {
                mListView.setOnItemClickListener((AdapterView$OnItemClickListener)new AdapterView$OnItemClickListener() {
                    public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                        AlertParams.this.mOnClickListener.onClick((DialogInterface)alertController.mDialog, n);
                        if (!AlertParams.this.mIsSingleChoice) {
                            alertController.mDialog.dismiss();
                        }
                    }
                });
            }
            else if (this.mOnCheckboxClickListener != null) {
                mListView.setOnItemClickListener((AdapterView$OnItemClickListener)new AdapterView$OnItemClickListener() {
                    public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                        if (AlertParams.this.mCheckedItems != null) {
                            AlertParams.this.mCheckedItems[n] = mListView.isItemChecked(n);
                        }
                        AlertParams.this.mOnCheckboxClickListener.onClick((DialogInterface)alertController.mDialog, n, mListView.isItemChecked(n));
                    }
                });
            }
            final AdapterView$OnItemSelectedListener mOnItemSelectedListener = this.mOnItemSelectedListener;
            if (mOnItemSelectedListener != null) {
                mListView.setOnItemSelectedListener(mOnItemSelectedListener);
            }
            if (this.mIsSingleChoice) {
                mListView.setChoiceMode(1);
            }
            else if (this.mIsMultiChoice) {
                mListView.setChoiceMode(2);
            }
            alertController.mListView = mListView;
        }
        
        public void apply(final AlertController alertController) {
            final View mCustomTitleView = this.mCustomTitleView;
            if (mCustomTitleView != null) {
                alertController.setCustomTitle(mCustomTitleView);
            }
            else {
                final CharSequence mTitle = this.mTitle;
                if (mTitle != null) {
                    alertController.setTitle(mTitle);
                }
                final Drawable mIcon = this.mIcon;
                if (mIcon != null) {
                    alertController.setIcon(mIcon);
                }
                final int mIconId = this.mIconId;
                if (mIconId != 0) {
                    alertController.setIcon(mIconId);
                }
                final int mIconAttrId = this.mIconAttrId;
                if (mIconAttrId != 0) {
                    alertController.setIcon(alertController.getIconAttributeResId(mIconAttrId));
                }
            }
            final CharSequence mMessage = this.mMessage;
            if (mMessage != null) {
                alertController.setMessage(mMessage);
            }
            final CharSequence mPositiveButtonText = this.mPositiveButtonText;
            if (mPositiveButtonText != null) {
                alertController.setButton(-1, mPositiveButtonText, this.mPositiveButtonListener, null);
            }
            final CharSequence mNegativeButtonText = this.mNegativeButtonText;
            if (mNegativeButtonText != null) {
                alertController.setButton(-2, mNegativeButtonText, this.mNegativeButtonListener, null);
            }
            final CharSequence mNeutralButtonText = this.mNeutralButtonText;
            if (mNeutralButtonText != null) {
                alertController.setButton(-3, mNeutralButtonText, this.mNeutralButtonListener, null);
            }
            if (this.mItems != null || this.mCursor != null || this.mAdapter != null) {
                this.createListView(alertController);
            }
            final View mView = this.mView;
            if (mView != null) {
                if (this.mViewSpacingSpecified) {
                    alertController.setView(mView, this.mViewSpacingLeft, this.mViewSpacingTop, this.mViewSpacingRight, this.mViewSpacingBottom);
                    return;
                }
                alertController.setView(mView);
            }
            else {
                final int mViewLayoutResId = this.mViewLayoutResId;
                if (mViewLayoutResId != 0) {
                    alertController.setView(mViewLayoutResId);
                }
            }
        }
        
        public interface OnPrepareListViewListener
        {
            void onPrepareListView(final ListView p0);
        }
    }
    
    private static final class ButtonHandler extends Handler
    {
        private static final int MSG_DISMISS_DIALOG = 1;
        private WeakReference<DialogInterface> mDialog;
        
        public ButtonHandler(final DialogInterface dialogInterface) {
            this.mDialog = new WeakReference<DialogInterface>(dialogInterface);
        }
        
        public void handleMessage(final Message message) {
            final int what = message.what;
            if (what == 1) {
                ((DialogInterface)message.obj).dismiss();
                return;
            }
            switch (what) {
                default: {}
                case -3:
                case -2:
                case -1: {
                    ((DialogInterface$OnClickListener)message.obj).onClick((DialogInterface)this.mDialog.get(), message.what);
                }
            }
        }
    }
    
    private static class CheckedItemAdapter extends ArrayAdapter<CharSequence>
    {
        public CheckedItemAdapter(final Context context, final int n, final int n2, final CharSequence[] array) {
            super(context, n, n2, (Object[])array);
        }
        
        public long getItemId(final int n) {
            return n;
        }
        
        public boolean hasStableIds() {
            return true;
        }
    }
    
    public static class RecycleListView extends ListView
    {
        private final int mPaddingBottomNoButtons;
        private final int mPaddingTopNoTitle;
        
        public RecycleListView(final Context context) {
            this(context, null);
        }
        
        public RecycleListView(final Context context, final AttributeSet set) {
            super(context, set);
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.RecycleListView);
            this.mPaddingBottomNoButtons = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.RecycleListView_paddingBottomNoButtons, -1);
            this.mPaddingTopNoTitle = obtainStyledAttributes.getDimensionPixelOffset(R.styleable.RecycleListView_paddingTopNoTitle, -1);
        }
        
        public void setHasDecor(final boolean b, final boolean b2) {
            if (b2 && b) {
                return;
            }
            final int paddingLeft = this.getPaddingLeft();
            int n;
            if (b) {
                n = this.getPaddingTop();
            }
            else {
                n = this.mPaddingTopNoTitle;
            }
            final int paddingRight = this.getPaddingRight();
            int n2;
            if (b2) {
                n2 = this.getPaddingBottom();
            }
            else {
                n2 = this.mPaddingBottomNoButtons;
            }
            this.setPadding(paddingLeft, n, paddingRight, n2);
        }
    }
}
