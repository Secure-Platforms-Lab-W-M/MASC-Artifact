// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.widget;

import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.KeyEvent$DispatcherState;
import android.util.TypedValue;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.content.res.Configuration;
import android.os.Parcel;
import android.os.Parcelable$ClassLoaderCreator;
import android.os.Parcelable$Creator;
import android.support.v4.view.AbsSavedState;
import java.lang.reflect.Method;
import android.support.annotation.RestrictTo;
import android.content.ActivityNotFoundException;
import android.view.View$MeasureSpec;
import android.view.TouchDelegate;
import android.support.annotation.Nullable;
import android.widget.AutoCompleteTextView;
import android.widget.ListAdapter;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.text.SpannableStringBuilder;
import android.content.res.Resources;
import android.content.ComponentName;
import android.os.Parcelable;
import android.app.PendingIntent;
import android.util.Log;
import android.net.Uri;
import android.view.View$OnLayoutChangeListener;
import android.support.v4.view.ViewCompat;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.text.Editable;
import android.widget.AdapterView;
import android.widget.TextView;
import android.view.KeyEvent;
import android.database.Cursor;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import android.content.Intent;
import android.text.TextWatcher;
import android.view.View$OnKeyListener;
import android.support.v4.widget.CursorAdapter;
import android.app.SearchableInfo;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable$ConstantState;
import java.util.WeakHashMap;
import android.view.View$OnFocusChangeListener;
import android.widget.AdapterView$OnItemSelectedListener;
import android.widget.AdapterView$OnItemClickListener;
import android.widget.TextView$OnEditorActionListener;
import android.view.View$OnClickListener;
import android.view.View;
import android.widget.ImageView;
import android.os.Bundle;
import android.support.v7.view.CollapsibleActionView;

public class SearchView extends LinearLayoutCompat implements CollapsibleActionView
{
    static final boolean DBG = false;
    static final AutoCompleteTextViewReflector HIDDEN_METHOD_INVOKER;
    private static final String IME_OPTION_NO_MICROPHONE = "nm";
    static final String LOG_TAG = "SearchView";
    private Bundle mAppSearchData;
    private boolean mClearingFocus;
    final ImageView mCloseButton;
    private final ImageView mCollapsedIcon;
    private int mCollapsedImeOptions;
    private final CharSequence mDefaultQueryHint;
    private final View mDropDownAnchor;
    private boolean mExpandedInActionView;
    final ImageView mGoButton;
    private boolean mIconified;
    private boolean mIconifiedByDefault;
    private int mMaxWidth;
    private CharSequence mOldQueryText;
    private final View$OnClickListener mOnClickListener;
    private OnCloseListener mOnCloseListener;
    private final TextView$OnEditorActionListener mOnEditorActionListener;
    private final AdapterView$OnItemClickListener mOnItemClickListener;
    private final AdapterView$OnItemSelectedListener mOnItemSelectedListener;
    private OnQueryTextListener mOnQueryChangeListener;
    View$OnFocusChangeListener mOnQueryTextFocusChangeListener;
    private View$OnClickListener mOnSearchClickListener;
    private OnSuggestionListener mOnSuggestionListener;
    private final WeakHashMap<String, Drawable$ConstantState> mOutsideDrawablesCache;
    private CharSequence mQueryHint;
    private boolean mQueryRefinement;
    private Runnable mReleaseCursorRunnable;
    final ImageView mSearchButton;
    private final View mSearchEditFrame;
    private final Drawable mSearchHintIcon;
    private final View mSearchPlate;
    final SearchAutoComplete mSearchSrcTextView;
    private Rect mSearchSrcTextViewBounds;
    private Rect mSearchSrtTextViewBoundsExpanded;
    SearchableInfo mSearchable;
    private final View mSubmitArea;
    private boolean mSubmitButtonEnabled;
    private final int mSuggestionCommitIconResId;
    private final int mSuggestionRowLayout;
    CursorAdapter mSuggestionsAdapter;
    private int[] mTemp;
    private int[] mTemp2;
    View$OnKeyListener mTextKeyListener;
    private TextWatcher mTextWatcher;
    private UpdatableTouchDelegate mTouchDelegate;
    private final Runnable mUpdateDrawableStateRunnable;
    private CharSequence mUserQuery;
    private final Intent mVoiceAppSearchIntent;
    final ImageView mVoiceButton;
    private boolean mVoiceButtonEnabled;
    private final Intent mVoiceWebSearchIntent;
    
    static {
        HIDDEN_METHOD_INVOKER = new AutoCompleteTextViewReflector();
    }
    
    public SearchView(final Context context) {
        this(context, null);
    }
    
    public SearchView(final Context context, final AttributeSet set) {
        this(context, set, R.attr.searchViewStyle);
    }
    
    public SearchView(final Context context, final AttributeSet set, int inputType) {
        super(context, set, inputType);
        this.mSearchSrcTextViewBounds = new Rect();
        this.mSearchSrtTextViewBoundsExpanded = new Rect();
        this.mTemp = new int[2];
        this.mTemp2 = new int[2];
        this.mUpdateDrawableStateRunnable = new Runnable() {
            @Override
            public void run() {
                SearchView.this.updateFocusedState();
            }
        };
        this.mReleaseCursorRunnable = new Runnable() {
            @Override
            public void run() {
                if (SearchView.this.mSuggestionsAdapter != null && SearchView.this.mSuggestionsAdapter instanceof SuggestionsAdapter) {
                    SearchView.this.mSuggestionsAdapter.changeCursor(null);
                }
            }
        };
        this.mOutsideDrawablesCache = new WeakHashMap<String, Drawable$ConstantState>();
        this.mOnClickListener = (View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                if (view == SearchView.this.mSearchButton) {
                    SearchView.this.onSearchClicked();
                    return;
                }
                if (view == SearchView.this.mCloseButton) {
                    SearchView.this.onCloseClicked();
                    return;
                }
                if (view == SearchView.this.mGoButton) {
                    SearchView.this.onSubmitQuery();
                    return;
                }
                if (view == SearchView.this.mVoiceButton) {
                    SearchView.this.onVoiceClicked();
                    return;
                }
                if (view == SearchView.this.mSearchSrcTextView) {
                    SearchView.this.forceSuggestionQuery();
                }
            }
        };
        this.mTextKeyListener = (View$OnKeyListener)new View$OnKeyListener() {
            public boolean onKey(final View view, final int n, final KeyEvent keyEvent) {
                if (SearchView.this.mSearchable == null) {
                    return false;
                }
                if (SearchView.this.mSearchSrcTextView.isPopupShowing() && SearchView.this.mSearchSrcTextView.getListSelection() != -1) {
                    return SearchView.this.onSuggestionsKey(view, n, keyEvent);
                }
                if (SearchView.this.mSearchSrcTextView.isEmpty() || !keyEvent.hasNoModifiers()) {
                    return false;
                }
                if (keyEvent.getAction() != 1) {
                    return false;
                }
                if (n == 66) {
                    view.cancelLongPress();
                    final SearchView this$0 = SearchView.this;
                    this$0.launchQuerySearch(0, null, this$0.mSearchSrcTextView.getText().toString());
                    return true;
                }
                return false;
            }
        };
        this.mOnEditorActionListener = (TextView$OnEditorActionListener)new TextView$OnEditorActionListener() {
            public boolean onEditorAction(final TextView textView, final int n, final KeyEvent keyEvent) {
                SearchView.this.onSubmitQuery();
                return true;
            }
        };
        this.mOnItemClickListener = (AdapterView$OnItemClickListener)new AdapterView$OnItemClickListener() {
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                SearchView.this.onItemClicked(n, 0, null);
            }
        };
        this.mOnItemSelectedListener = (AdapterView$OnItemSelectedListener)new AdapterView$OnItemSelectedListener() {
            public void onItemSelected(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                SearchView.this.onItemSelected(n);
            }
            
            public void onNothingSelected(final AdapterView<?> adapterView) {
            }
        };
        this.mTextWatcher = (TextWatcher)new TextWatcher() {
            public void afterTextChanged(final Editable editable) {
            }
            
            public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            }
            
            public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
                SearchView.this.onTextChanged(charSequence);
            }
        };
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, set, R.styleable.SearchView, inputType, 0);
        LayoutInflater.from(context).inflate(obtainStyledAttributes.getResourceId(R.styleable.SearchView_layout, R.layout.abc_search_view), (ViewGroup)this, true);
        (this.mSearchSrcTextView = (SearchAutoComplete)this.findViewById(R.id.search_src_text)).setSearchView(this);
        this.mSearchEditFrame = this.findViewById(R.id.search_edit_frame);
        this.mSearchPlate = this.findViewById(R.id.search_plate);
        this.mSubmitArea = this.findViewById(R.id.submit_area);
        this.mSearchButton = (ImageView)this.findViewById(R.id.search_button);
        this.mGoButton = (ImageView)this.findViewById(R.id.search_go_btn);
        this.mCloseButton = (ImageView)this.findViewById(R.id.search_close_btn);
        this.mVoiceButton = (ImageView)this.findViewById(R.id.search_voice_btn);
        this.mCollapsedIcon = (ImageView)this.findViewById(R.id.search_mag_icon);
        ViewCompat.setBackground(this.mSearchPlate, obtainStyledAttributes.getDrawable(R.styleable.SearchView_queryBackground));
        ViewCompat.setBackground(this.mSubmitArea, obtainStyledAttributes.getDrawable(R.styleable.SearchView_submitBackground));
        this.mSearchButton.setImageDrawable(obtainStyledAttributes.getDrawable(R.styleable.SearchView_searchIcon));
        this.mGoButton.setImageDrawable(obtainStyledAttributes.getDrawable(R.styleable.SearchView_goIcon));
        this.mCloseButton.setImageDrawable(obtainStyledAttributes.getDrawable(R.styleable.SearchView_closeIcon));
        this.mVoiceButton.setImageDrawable(obtainStyledAttributes.getDrawable(R.styleable.SearchView_voiceIcon));
        this.mCollapsedIcon.setImageDrawable(obtainStyledAttributes.getDrawable(R.styleable.SearchView_searchIcon));
        this.mSearchHintIcon = obtainStyledAttributes.getDrawable(R.styleable.SearchView_searchHintIcon);
        TooltipCompat.setTooltipText((View)this.mSearchButton, this.getResources().getString(R.string.abc_searchview_description_search));
        this.mSuggestionRowLayout = obtainStyledAttributes.getResourceId(R.styleable.SearchView_suggestionRowLayout, R.layout.abc_search_dropdown_item_icons_2line);
        this.mSuggestionCommitIconResId = obtainStyledAttributes.getResourceId(R.styleable.SearchView_commitIcon, 0);
        this.mSearchButton.setOnClickListener(this.mOnClickListener);
        this.mCloseButton.setOnClickListener(this.mOnClickListener);
        this.mGoButton.setOnClickListener(this.mOnClickListener);
        this.mVoiceButton.setOnClickListener(this.mOnClickListener);
        this.mSearchSrcTextView.setOnClickListener(this.mOnClickListener);
        this.mSearchSrcTextView.addTextChangedListener(this.mTextWatcher);
        this.mSearchSrcTextView.setOnEditorActionListener(this.mOnEditorActionListener);
        this.mSearchSrcTextView.setOnItemClickListener(this.mOnItemClickListener);
        this.mSearchSrcTextView.setOnItemSelectedListener(this.mOnItemSelectedListener);
        this.mSearchSrcTextView.setOnKeyListener(this.mTextKeyListener);
        this.mSearchSrcTextView.setOnFocusChangeListener((View$OnFocusChangeListener)new View$OnFocusChangeListener() {
            public void onFocusChange(final View view, final boolean b) {
                if (SearchView.this.mOnQueryTextFocusChangeListener != null) {
                    SearchView.this.mOnQueryTextFocusChangeListener.onFocusChange((View)SearchView.this, b);
                }
            }
        });
        this.setIconifiedByDefault(obtainStyledAttributes.getBoolean(R.styleable.SearchView_iconifiedByDefault, true));
        inputType = obtainStyledAttributes.getDimensionPixelSize(R.styleable.SearchView_android_maxWidth, -1);
        if (inputType != -1) {
            this.setMaxWidth(inputType);
        }
        this.mDefaultQueryHint = obtainStyledAttributes.getText(R.styleable.SearchView_defaultQueryHint);
        this.mQueryHint = obtainStyledAttributes.getText(R.styleable.SearchView_queryHint);
        inputType = obtainStyledAttributes.getInt(R.styleable.SearchView_android_imeOptions, -1);
        if (inputType != -1) {
            this.setImeOptions(inputType);
        }
        inputType = obtainStyledAttributes.getInt(R.styleable.SearchView_android_inputType, -1);
        if (inputType != -1) {
            this.setInputType(inputType);
        }
        this.setFocusable(obtainStyledAttributes.getBoolean(R.styleable.SearchView_android_focusable, true));
        obtainStyledAttributes.recycle();
        (this.mVoiceWebSearchIntent = new Intent("android.speech.action.WEB_SEARCH")).addFlags(268435456);
        this.mVoiceWebSearchIntent.putExtra("android.speech.extra.LANGUAGE_MODEL", "web_search");
        (this.mVoiceAppSearchIntent = new Intent("android.speech.action.RECOGNIZE_SPEECH")).addFlags(268435456);
        this.mDropDownAnchor = this.findViewById(this.mSearchSrcTextView.getDropDownAnchor());
        final View mDropDownAnchor = this.mDropDownAnchor;
        if (mDropDownAnchor != null) {
            mDropDownAnchor.addOnLayoutChangeListener((View$OnLayoutChangeListener)new View$OnLayoutChangeListener() {
                public void onLayoutChange(final View view, final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8) {
                    SearchView.this.adjustDropDownSizeAndPosition();
                }
            });
        }
        this.updateViewsVisibility(this.mIconifiedByDefault);
        this.updateQueryHint();
    }
    
    private Intent createIntent(final String s, final Uri data, final String s2, final String s3, final int n, final String s4) {
        final Intent intent = new Intent(s);
        intent.addFlags(268435456);
        if (data != null) {
            intent.setData(data);
        }
        intent.putExtra("user_query", this.mUserQuery);
        if (s3 != null) {
            intent.putExtra("query", s3);
        }
        if (s2 != null) {
            intent.putExtra("intent_extra_data_key", s2);
        }
        final Bundle mAppSearchData = this.mAppSearchData;
        if (mAppSearchData != null) {
            intent.putExtra("app_data", mAppSearchData);
        }
        if (n != 0) {
            intent.putExtra("action_key", n);
            intent.putExtra("action_msg", s4);
        }
        intent.setComponent(this.mSearchable.getSearchActivity());
        return intent;
    }
    
    private Intent createIntentFromSuggestion(final Cursor cursor, int position, final String ex) {
        String s = null;
        String s2 = null;
        StringBuilder sb;
        String columnString;
        String s3;
        Uri parse;
        StringBuilder sb2;
        Label_0052_Outer:Label_0125_Outer:Label_0118_Outer:
        while (true) {
            while (true) {
                while (true) {
                Label_0052:
                    while (true) {
                        while (true) {
                            Label_0213: {
                                try {
                                    s = SuggestionsAdapter.getColumnString(cursor, "suggest_intent_action");
                                    if (s == null) {
                                        s = this.mSearchable.getSuggestIntentAction();
                                    }
                                    break Label_0213;
                                    // iftrue(Label_0233:, s2 != null)
                                    while (true) {
                                        s2 = this.mSearchable.getSuggestIntentData();
                                        break Label_0052;
                                        s2 = SuggestionsAdapter.getColumnString(cursor, "suggest_intent_data");
                                        continue Label_0052_Outer;
                                    }
                                    // iftrue(Label_0236:, s2 == null)
                                    // iftrue(Label_0236:, columnString == null)
                                    while (true) {
                                        sb = new StringBuilder();
                                        sb.append(s2);
                                        sb.append("/");
                                        sb.append(Uri.encode(columnString));
                                        s2 = sb.toString();
                                        break Label_0052;
                                        return this.createIntent(s3, parse, SuggestionsAdapter.getColumnString(cursor, "suggest_intent_extra_data"), SuggestionsAdapter.getColumnString(cursor, "suggest_intent_query"), position, (String)ex);
                                        columnString = SuggestionsAdapter.getColumnString(cursor, "suggest_intent_data_id");
                                        continue Label_0125_Outer;
                                    }
                                    parse = Uri.parse(s2);
                                    return this.createIntent(s3, parse, SuggestionsAdapter.getColumnString(cursor, "suggest_intent_extra_data"), SuggestionsAdapter.getColumnString(cursor, "suggest_intent_query"), position, (String)ex);
                                }
                                catch (RuntimeException ex) {
                                    try {
                                        position = cursor.getPosition();
                                    }
                                    catch (RuntimeException ex2) {
                                        position = -1;
                                    }
                                    sb2 = new StringBuilder();
                                    sb2.append("Search suggestions cursor at row ");
                                    sb2.append(position);
                                    sb2.append(" returned exception.");
                                    Log.w("SearchView", sb2.toString(), (Throwable)ex);
                                    return null;
                                }
                            }
                            if (s == null) {
                                s3 = "android.intent.action.SEARCH";
                                continue Label_0125_Outer;
                            }
                            s3 = s;
                            continue Label_0125_Outer;
                        }
                        Label_0233: {
                            continue Label_0052;
                        }
                    }
                    if (s2 == null) {
                        parse = null;
                        continue Label_0118_Outer;
                    }
                    break;
                }
                continue;
            }
        }
    }
    
    private Intent createVoiceAppSearchIntent(final Intent intent, final SearchableInfo searchableInfo) {
        final ComponentName searchActivity = searchableInfo.getSearchActivity();
        final Intent intent2 = new Intent("android.intent.action.SEARCH");
        intent2.setComponent(searchActivity);
        final PendingIntent activity = PendingIntent.getActivity(this.getContext(), 0, intent2, 1073741824);
        final Bundle bundle = new Bundle();
        final Bundle mAppSearchData = this.mAppSearchData;
        if (mAppSearchData != null) {
            bundle.putParcelable("app_data", (Parcelable)mAppSearchData);
        }
        final Intent intent3 = new Intent(intent);
        String string = "free_form";
        String string2 = null;
        String string3 = null;
        int voiceMaxResults = 1;
        final Resources resources = this.getResources();
        if (searchableInfo.getVoiceLanguageModeId() != 0) {
            string = resources.getString(searchableInfo.getVoiceLanguageModeId());
        }
        if (searchableInfo.getVoicePromptTextId() != 0) {
            string2 = resources.getString(searchableInfo.getVoicePromptTextId());
        }
        if (searchableInfo.getVoiceLanguageId() != 0) {
            string3 = resources.getString(searchableInfo.getVoiceLanguageId());
        }
        if (searchableInfo.getVoiceMaxResults() != 0) {
            voiceMaxResults = searchableInfo.getVoiceMaxResults();
        }
        intent3.putExtra("android.speech.extra.LANGUAGE_MODEL", string);
        intent3.putExtra("android.speech.extra.PROMPT", string2);
        intent3.putExtra("android.speech.extra.LANGUAGE", string3);
        intent3.putExtra("android.speech.extra.MAX_RESULTS", voiceMaxResults);
        String flattenToShortString;
        if (searchActivity == null) {
            flattenToShortString = null;
        }
        else {
            flattenToShortString = searchActivity.flattenToShortString();
        }
        intent3.putExtra("calling_package", flattenToShortString);
        intent3.putExtra("android.speech.extra.RESULTS_PENDINGINTENT", (Parcelable)activity);
        intent3.putExtra("android.speech.extra.RESULTS_PENDINGINTENT_BUNDLE", bundle);
        return intent3;
    }
    
    private Intent createVoiceWebSearchIntent(final Intent intent, final SearchableInfo searchableInfo) {
        final Intent intent2 = new Intent(intent);
        final ComponentName searchActivity = searchableInfo.getSearchActivity();
        String flattenToShortString;
        if (searchActivity == null) {
            flattenToShortString = null;
        }
        else {
            flattenToShortString = searchActivity.flattenToShortString();
        }
        intent2.putExtra("calling_package", flattenToShortString);
        return intent2;
    }
    
    private void dismissSuggestions() {
        this.mSearchSrcTextView.dismissDropDown();
    }
    
    private void getChildBoundsWithinSearchView(final View view, final Rect rect) {
        view.getLocationInWindow(this.mTemp);
        this.getLocationInWindow(this.mTemp2);
        final int[] mTemp = this.mTemp;
        final int n = mTemp[1];
        final int[] mTemp2 = this.mTemp2;
        final int n2 = n - mTemp2[1];
        final int n3 = mTemp[0] - mTemp2[0];
        rect.set(n3, n2, view.getWidth() + n3, view.getHeight() + n2);
    }
    
    private CharSequence getDecoratedHint(final CharSequence charSequence) {
        if (!this.mIconifiedByDefault) {
            return charSequence;
        }
        if (this.mSearchHintIcon == null) {
            return charSequence;
        }
        final double n = this.mSearchSrcTextView.getTextSize();
        Double.isNaN(n);
        final int n2 = (int)(n * 1.25);
        this.mSearchHintIcon.setBounds(0, 0, n2, n2);
        final SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder((CharSequence)"   ");
        spannableStringBuilder.setSpan((Object)new ImageSpan(this.mSearchHintIcon), 1, 2, 33);
        spannableStringBuilder.append(charSequence);
        return (CharSequence)spannableStringBuilder;
    }
    
    private int getPreferredHeight() {
        return this.getContext().getResources().getDimensionPixelSize(R.dimen.abc_search_view_preferred_height);
    }
    
    private int getPreferredWidth() {
        return this.getContext().getResources().getDimensionPixelSize(R.dimen.abc_search_view_preferred_width);
    }
    
    private boolean hasVoiceSearch() {
        final SearchableInfo mSearchable = this.mSearchable;
        boolean b = false;
        if (mSearchable == null || !mSearchable.getVoiceSearchEnabled()) {
            return false;
        }
        Intent intent = null;
        if (this.mSearchable.getVoiceSearchLaunchWebSearch()) {
            intent = this.mVoiceWebSearchIntent;
        }
        else if (this.mSearchable.getVoiceSearchLaunchRecognizer()) {
            intent = this.mVoiceAppSearchIntent;
        }
        if (intent != null) {
            if (this.getContext().getPackageManager().resolveActivity(intent, 65536) != null) {
                b = true;
            }
            return b;
        }
        return false;
    }
    
    static boolean isLandscapeMode(final Context context) {
        return context.getResources().getConfiguration().orientation == 2;
    }
    
    private boolean isSubmitAreaEnabled() {
        return (this.mSubmitButtonEnabled || this.mVoiceButtonEnabled) && !this.isIconified();
    }
    
    private void launchIntent(final Intent intent) {
        if (intent == null) {
            return;
        }
        try {
            this.getContext().startActivity(intent);
        }
        catch (RuntimeException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed launch activity: ");
            sb.append(intent);
            Log.e("SearchView", sb.toString(), (Throwable)ex);
        }
    }
    
    private boolean launchSuggestion(final int n, final int n2, final String s) {
        final Cursor cursor = this.mSuggestionsAdapter.getCursor();
        if (cursor != null && cursor.moveToPosition(n)) {
            this.launchIntent(this.createIntentFromSuggestion(cursor, n2, s));
            return true;
        }
        return false;
    }
    
    private void postUpdateFocusedState() {
        this.post(this.mUpdateDrawableStateRunnable);
    }
    
    private void rewriteQueryFromSuggestion(final int n) {
        final Editable text = this.mSearchSrcTextView.getText();
        final Cursor cursor = this.mSuggestionsAdapter.getCursor();
        if (cursor == null) {
            return;
        }
        if (cursor.moveToPosition(n)) {
            final CharSequence convertToString = this.mSuggestionsAdapter.convertToString(cursor);
            if (convertToString != null) {
                this.setQuery(convertToString);
            }
            else {
                this.setQuery((CharSequence)text);
            }
            return;
        }
        this.setQuery((CharSequence)text);
    }
    
    private void setQuery(final CharSequence text) {
        this.mSearchSrcTextView.setText(text);
        final SearchAutoComplete mSearchSrcTextView = this.mSearchSrcTextView;
        int length;
        if (TextUtils.isEmpty(text)) {
            length = 0;
        }
        else {
            length = text.length();
        }
        mSearchSrcTextView.setSelection(length);
    }
    
    private void updateCloseButton() {
        final boolean empty = TextUtils.isEmpty((CharSequence)this.mSearchSrcTextView.getText());
        final boolean b = true;
        final boolean b2 = empty ^ true;
        final boolean b3 = false;
        boolean b4 = b;
        if (!b2) {
            b4 = (this.mIconifiedByDefault && !this.mExpandedInActionView && b);
        }
        final ImageView mCloseButton = this.mCloseButton;
        int visibility;
        if (b4) {
            visibility = (b3 ? 1 : 0);
        }
        else {
            visibility = 8;
        }
        mCloseButton.setVisibility(visibility);
        final Drawable drawable = this.mCloseButton.getDrawable();
        if (drawable != null) {
            int[] state;
            if (b2) {
                state = SearchView.ENABLED_STATE_SET;
            }
            else {
                state = SearchView.EMPTY_STATE_SET;
            }
            drawable.setState(state);
        }
    }
    
    private void updateQueryHint() {
        CharSequence queryHint = this.getQueryHint();
        final SearchAutoComplete mSearchSrcTextView = this.mSearchSrcTextView;
        if (queryHint == null) {
            queryHint = "";
        }
        mSearchSrcTextView.setHint(this.getDecoratedHint(queryHint));
    }
    
    private void updateSearchAutoComplete() {
        this.mSearchSrcTextView.setThreshold(this.mSearchable.getSuggestThreshold());
        this.mSearchSrcTextView.setImeOptions(this.mSearchable.getImeOptions());
        int inputType = this.mSearchable.getInputType();
        final boolean b = true;
        if ((inputType & 0xF) == 0x1) {
            inputType &= 0xFFFEFFFF;
            if (this.mSearchable.getSuggestAuthority() != null) {
                inputType = (inputType | 0x10000 | 0x80000);
            }
        }
        this.mSearchSrcTextView.setInputType(inputType);
        final CursorAdapter mSuggestionsAdapter = this.mSuggestionsAdapter;
        if (mSuggestionsAdapter != null) {
            mSuggestionsAdapter.changeCursor(null);
        }
        if (this.mSearchable.getSuggestAuthority() != null) {
            this.mSuggestionsAdapter = new SuggestionsAdapter(this.getContext(), this, this.mSearchable, this.mOutsideDrawablesCache);
            this.mSearchSrcTextView.setAdapter((ListAdapter)this.mSuggestionsAdapter);
            final SuggestionsAdapter suggestionsAdapter = (SuggestionsAdapter)this.mSuggestionsAdapter;
            int queryRefinement = b ? 1 : 0;
            if (this.mQueryRefinement) {
                queryRefinement = 2;
            }
            suggestionsAdapter.setQueryRefinement(queryRefinement);
        }
    }
    
    private void updateSubmitArea() {
        int visibility = 8;
        if (this.isSubmitAreaEnabled()) {
            if (this.mGoButton.getVisibility() == 0 || this.mVoiceButton.getVisibility() == 0) {
                visibility = 0;
            }
        }
        this.mSubmitArea.setVisibility(visibility);
    }
    
    private void updateSubmitButton(final boolean b) {
        int visibility = 8;
        if (this.mSubmitButtonEnabled && this.isSubmitAreaEnabled() && this.hasFocus() && (b || !this.mVoiceButtonEnabled)) {
            visibility = 0;
        }
        this.mGoButton.setVisibility(visibility);
    }
    
    private void updateViewsVisibility(final boolean mIconified) {
        this.mIconified = mIconified;
        final int n = 8;
        final boolean b = false;
        int visibility;
        if (mIconified) {
            visibility = 0;
        }
        else {
            visibility = 8;
        }
        final boolean b2 = TextUtils.isEmpty((CharSequence)this.mSearchSrcTextView.getText()) ^ true;
        this.mSearchButton.setVisibility(visibility);
        this.updateSubmitButton(b2);
        final View mSearchEditFrame = this.mSearchEditFrame;
        int visibility2;
        if (mIconified) {
            visibility2 = n;
        }
        else {
            visibility2 = 0;
        }
        mSearchEditFrame.setVisibility(visibility2);
        int visibility3;
        if (this.mCollapsedIcon.getDrawable() != null && !this.mIconifiedByDefault) {
            visibility3 = 0;
        }
        else {
            visibility3 = 8;
        }
        this.mCollapsedIcon.setVisibility(visibility3);
        this.updateCloseButton();
        boolean b3 = b;
        if (!b2) {
            b3 = true;
        }
        this.updateVoiceButton(b3);
        this.updateSubmitArea();
    }
    
    private void updateVoiceButton(final boolean b) {
        int visibility = 8;
        if (this.mVoiceButtonEnabled && !this.isIconified() && b) {
            visibility = 0;
            this.mGoButton.setVisibility(8);
        }
        this.mVoiceButton.setVisibility(visibility);
    }
    
    void adjustDropDownSizeAndPosition() {
        if (this.mDropDownAnchor.getWidth() > 1) {
            final Resources resources = this.getContext().getResources();
            final int paddingLeft = this.mSearchPlate.getPaddingLeft();
            final Rect rect = new Rect();
            final boolean layoutRtl = ViewUtils.isLayoutRtl((View)this);
            int n;
            if (this.mIconifiedByDefault) {
                n = resources.getDimensionPixelSize(R.dimen.abc_dropdownitem_icon_width) + resources.getDimensionPixelSize(R.dimen.abc_dropdownitem_text_padding_left);
            }
            else {
                n = 0;
            }
            this.mSearchSrcTextView.getDropDownBackground().getPadding(rect);
            int dropDownHorizontalOffset;
            if (layoutRtl) {
                dropDownHorizontalOffset = -rect.left;
            }
            else {
                dropDownHorizontalOffset = paddingLeft - (rect.left + n);
            }
            this.mSearchSrcTextView.setDropDownHorizontalOffset(dropDownHorizontalOffset);
            this.mSearchSrcTextView.setDropDownWidth(this.mDropDownAnchor.getWidth() + rect.left + rect.right + n - paddingLeft);
        }
    }
    
    public void clearFocus() {
        this.mClearingFocus = true;
        super.clearFocus();
        this.mSearchSrcTextView.clearFocus();
        this.mSearchSrcTextView.setImeVisibility(false);
        this.mClearingFocus = false;
    }
    
    void forceSuggestionQuery() {
        SearchView.HIDDEN_METHOD_INVOKER.doBeforeTextChanged(this.mSearchSrcTextView);
        SearchView.HIDDEN_METHOD_INVOKER.doAfterTextChanged(this.mSearchSrcTextView);
    }
    
    public int getImeOptions() {
        return this.mSearchSrcTextView.getImeOptions();
    }
    
    public int getInputType() {
        return this.mSearchSrcTextView.getInputType();
    }
    
    public int getMaxWidth() {
        return this.mMaxWidth;
    }
    
    public CharSequence getQuery() {
        return (CharSequence)this.mSearchSrcTextView.getText();
    }
    
    @Nullable
    public CharSequence getQueryHint() {
        if (this.mQueryHint != null) {
            return this.mQueryHint;
        }
        final SearchableInfo mSearchable = this.mSearchable;
        if (mSearchable != null && mSearchable.getHintId() != 0) {
            return this.getContext().getText(this.mSearchable.getHintId());
        }
        return this.mDefaultQueryHint;
    }
    
    int getSuggestionCommitIconResId() {
        return this.mSuggestionCommitIconResId;
    }
    
    int getSuggestionRowLayout() {
        return this.mSuggestionRowLayout;
    }
    
    public CursorAdapter getSuggestionsAdapter() {
        return this.mSuggestionsAdapter;
    }
    
    public boolean isIconfiedByDefault() {
        return this.mIconifiedByDefault;
    }
    
    public boolean isIconified() {
        return this.mIconified;
    }
    
    public boolean isQueryRefinementEnabled() {
        return this.mQueryRefinement;
    }
    
    public boolean isSubmitButtonEnabled() {
        return this.mSubmitButtonEnabled;
    }
    
    void launchQuerySearch(final int n, final String s, final String s2) {
        this.getContext().startActivity(this.createIntent("android.intent.action.SEARCH", null, null, s2, n, s));
    }
    
    @Override
    public void onActionViewCollapsed() {
        this.setQuery("", false);
        this.clearFocus();
        this.updateViewsVisibility(true);
        this.mSearchSrcTextView.setImeOptions(this.mCollapsedImeOptions);
        this.mExpandedInActionView = false;
    }
    
    @Override
    public void onActionViewExpanded() {
        if (this.mExpandedInActionView) {
            return;
        }
        this.mExpandedInActionView = true;
        this.mCollapsedImeOptions = this.mSearchSrcTextView.getImeOptions();
        this.mSearchSrcTextView.setImeOptions(this.mCollapsedImeOptions | 0x2000000);
        this.mSearchSrcTextView.setText((CharSequence)"");
        this.setIconified(false);
    }
    
    void onCloseClicked() {
        if (!TextUtils.isEmpty((CharSequence)this.mSearchSrcTextView.getText())) {
            this.mSearchSrcTextView.setText((CharSequence)"");
            this.mSearchSrcTextView.requestFocus();
            this.mSearchSrcTextView.setImeVisibility(true);
            return;
        }
        if (!this.mIconifiedByDefault) {
            return;
        }
        final OnCloseListener mOnCloseListener = this.mOnCloseListener;
        if (mOnCloseListener != null && mOnCloseListener.onClose()) {
            return;
        }
        this.clearFocus();
        this.updateViewsVisibility(true);
    }
    
    protected void onDetachedFromWindow() {
        this.removeCallbacks(this.mUpdateDrawableStateRunnable);
        this.post(this.mReleaseCursorRunnable);
        super.onDetachedFromWindow();
    }
    
    boolean onItemClicked(final int n, final int n2, final String s) {
        final OnSuggestionListener mOnSuggestionListener = this.mOnSuggestionListener;
        if (mOnSuggestionListener != null && mOnSuggestionListener.onSuggestionClick(n)) {
            return false;
        }
        this.launchSuggestion(n, 0, null);
        this.mSearchSrcTextView.setImeVisibility(false);
        this.dismissSuggestions();
        return true;
    }
    
    boolean onItemSelected(final int n) {
        final OnSuggestionListener mOnSuggestionListener = this.mOnSuggestionListener;
        if (mOnSuggestionListener != null && mOnSuggestionListener.onSuggestionSelect(n)) {
            return false;
        }
        this.rewriteQueryFromSuggestion(n);
        return true;
    }
    
    @Override
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        if (!b) {
            return;
        }
        this.getChildBoundsWithinSearchView((View)this.mSearchSrcTextView, this.mSearchSrcTextViewBounds);
        this.mSearchSrtTextViewBoundsExpanded.set(this.mSearchSrcTextViewBounds.left, 0, this.mSearchSrcTextViewBounds.right, n4 - n2);
        final UpdatableTouchDelegate mTouchDelegate = this.mTouchDelegate;
        if (mTouchDelegate == null) {
            this.setTouchDelegate((TouchDelegate)(this.mTouchDelegate = new UpdatableTouchDelegate(this.mSearchSrtTextViewBoundsExpanded, this.mSearchSrcTextViewBounds, (View)this.mSearchSrcTextView)));
            return;
        }
        mTouchDelegate.setBounds(this.mSearchSrtTextViewBoundsExpanded, this.mSearchSrcTextViewBounds);
    }
    
    @Override
    protected void onMeasure(int n, int n2) {
        if (this.isIconified()) {
            super.onMeasure(n, n2);
            return;
        }
        final int mode = View$MeasureSpec.getMode(n);
        n = View$MeasureSpec.getSize(n);
        if (mode != Integer.MIN_VALUE) {
            if (mode != 0) {
                if (mode == 1073741824) {
                    final int mMaxWidth = this.mMaxWidth;
                    if (mMaxWidth > 0) {
                        n = Math.min(mMaxWidth, n);
                    }
                }
            }
            else {
                n = this.mMaxWidth;
                if (n <= 0) {
                    n = this.getPreferredWidth();
                }
            }
        }
        else {
            final int mMaxWidth2 = this.mMaxWidth;
            if (mMaxWidth2 > 0) {
                n = Math.min(mMaxWidth2, n);
            }
            else {
                n = Math.min(this.getPreferredWidth(), n);
            }
        }
        final int mode2 = View$MeasureSpec.getMode(n2);
        n2 = View$MeasureSpec.getSize(n2);
        if (mode2 != Integer.MIN_VALUE) {
            if (mode2 == 0) {
                n2 = this.getPreferredHeight();
            }
        }
        else {
            n2 = Math.min(this.getPreferredHeight(), n2);
        }
        super.onMeasure(View$MeasureSpec.makeMeasureSpec(n, 1073741824), View$MeasureSpec.makeMeasureSpec(n2, 1073741824));
    }
    
    void onQueryRefine(final CharSequence query) {
        this.setQuery(query);
    }
    
    protected void onRestoreInstanceState(final Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        final SavedState savedState = (SavedState)parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.updateViewsVisibility(savedState.isIconified);
        this.requestLayout();
    }
    
    protected Parcelable onSaveInstanceState() {
        final SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.isIconified = this.isIconified();
        return (Parcelable)savedState;
    }
    
    void onSearchClicked() {
        this.updateViewsVisibility(false);
        this.mSearchSrcTextView.requestFocus();
        this.mSearchSrcTextView.setImeVisibility(true);
        final View$OnClickListener mOnSearchClickListener = this.mOnSearchClickListener;
        if (mOnSearchClickListener != null) {
            mOnSearchClickListener.onClick((View)this);
        }
    }
    
    void onSubmitQuery() {
        final Editable text = this.mSearchSrcTextView.getText();
        if (text == null || TextUtils.getTrimmedLength((CharSequence)text) <= 0) {
            return;
        }
        final OnQueryTextListener mOnQueryChangeListener = this.mOnQueryChangeListener;
        if (mOnQueryChangeListener != null && mOnQueryChangeListener.onQueryTextSubmit(((CharSequence)text).toString())) {
            return;
        }
        if (this.mSearchable != null) {
            this.launchQuerySearch(0, null, ((CharSequence)text).toString());
        }
        this.mSearchSrcTextView.setImeVisibility(false);
        this.dismissSuggestions();
    }
    
    boolean onSuggestionsKey(final View view, int length, final KeyEvent keyEvent) {
        if (this.mSearchable == null) {
            return false;
        }
        if (this.mSuggestionsAdapter == null) {
            return false;
        }
        if (keyEvent.getAction() != 0 || !keyEvent.hasNoModifiers()) {
            return false;
        }
        if (length == 66 || length == 84 || length == 61) {
            return this.onItemClicked(this.mSearchSrcTextView.getListSelection(), 0, null);
        }
        if (length != 21 && length != 22) {
            return length == 19 && this.mSearchSrcTextView.getListSelection() == 0 && false;
        }
        if (length == 21) {
            length = 0;
        }
        else {
            length = this.mSearchSrcTextView.length();
        }
        this.mSearchSrcTextView.setSelection(length);
        this.mSearchSrcTextView.setListSelection(0);
        this.mSearchSrcTextView.clearListSelection();
        SearchView.HIDDEN_METHOD_INVOKER.ensureImeVisible(this.mSearchSrcTextView, true);
        return true;
    }
    
    void onTextChanged(final CharSequence charSequence) {
        final Editable text = this.mSearchSrcTextView.getText();
        this.mUserQuery = (CharSequence)text;
        final boolean empty = TextUtils.isEmpty((CharSequence)text);
        boolean b = true;
        final boolean b2 = empty ^ true;
        this.updateSubmitButton(b2);
        if (b2) {
            b = false;
        }
        this.updateVoiceButton(b);
        this.updateCloseButton();
        this.updateSubmitArea();
        if (this.mOnQueryChangeListener != null && !TextUtils.equals(charSequence, this.mOldQueryText)) {
            this.mOnQueryChangeListener.onQueryTextChange(charSequence.toString());
        }
        this.mOldQueryText = charSequence.toString();
    }
    
    void onTextFocusChanged() {
        this.updateViewsVisibility(this.isIconified());
        this.postUpdateFocusedState();
        if (this.mSearchSrcTextView.hasFocus()) {
            this.forceSuggestionQuery();
        }
    }
    
    void onVoiceClicked() {
        if (this.mSearchable == null) {
            return;
        }
        final SearchableInfo mSearchable = this.mSearchable;
        try {
            if (mSearchable.getVoiceSearchLaunchWebSearch()) {
                this.getContext().startActivity(this.createVoiceWebSearchIntent(this.mVoiceWebSearchIntent, mSearchable));
            }
            else if (mSearchable.getVoiceSearchLaunchRecognizer()) {
                this.getContext().startActivity(this.createVoiceAppSearchIntent(this.mVoiceAppSearchIntent, mSearchable));
            }
        }
        catch (ActivityNotFoundException ex) {
            Log.w("SearchView", "Could not find voice search activity");
        }
    }
    
    public void onWindowFocusChanged(final boolean b) {
        super.onWindowFocusChanged(b);
        this.postUpdateFocusedState();
    }
    
    public boolean requestFocus(final int n, final Rect rect) {
        if (this.mClearingFocus) {
            return false;
        }
        if (!this.isFocusable()) {
            return false;
        }
        if (this.isIconified()) {
            return super.requestFocus(n, rect);
        }
        final boolean requestFocus = this.mSearchSrcTextView.requestFocus(n, rect);
        if (requestFocus) {
            this.updateViewsVisibility(false);
            return requestFocus;
        }
        return requestFocus;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setAppSearchData(final Bundle mAppSearchData) {
        this.mAppSearchData = mAppSearchData;
    }
    
    public void setIconified(final boolean b) {
        if (b) {
            this.onCloseClicked();
            return;
        }
        this.onSearchClicked();
    }
    
    public void setIconifiedByDefault(final boolean mIconifiedByDefault) {
        if (this.mIconifiedByDefault == mIconifiedByDefault) {
            return;
        }
        this.updateViewsVisibility(this.mIconifiedByDefault = mIconifiedByDefault);
        this.updateQueryHint();
    }
    
    public void setImeOptions(final int imeOptions) {
        this.mSearchSrcTextView.setImeOptions(imeOptions);
    }
    
    public void setInputType(final int inputType) {
        this.mSearchSrcTextView.setInputType(inputType);
    }
    
    public void setMaxWidth(final int mMaxWidth) {
        this.mMaxWidth = mMaxWidth;
        this.requestLayout();
    }
    
    public void setOnCloseListener(final OnCloseListener mOnCloseListener) {
        this.mOnCloseListener = mOnCloseListener;
    }
    
    public void setOnQueryTextFocusChangeListener(final View$OnFocusChangeListener mOnQueryTextFocusChangeListener) {
        this.mOnQueryTextFocusChangeListener = mOnQueryTextFocusChangeListener;
    }
    
    public void setOnQueryTextListener(final OnQueryTextListener mOnQueryChangeListener) {
        this.mOnQueryChangeListener = mOnQueryChangeListener;
    }
    
    public void setOnSearchClickListener(final View$OnClickListener mOnSearchClickListener) {
        this.mOnSearchClickListener = mOnSearchClickListener;
    }
    
    public void setOnSuggestionListener(final OnSuggestionListener mOnSuggestionListener) {
        this.mOnSuggestionListener = mOnSuggestionListener;
    }
    
    public void setQuery(final CharSequence charSequence, final boolean b) {
        this.mSearchSrcTextView.setText(charSequence);
        if (charSequence != null) {
            final SearchAutoComplete mSearchSrcTextView = this.mSearchSrcTextView;
            mSearchSrcTextView.setSelection(mSearchSrcTextView.length());
            this.mUserQuery = charSequence;
        }
        if (b && !TextUtils.isEmpty(charSequence)) {
            this.onSubmitQuery();
        }
    }
    
    public void setQueryHint(@Nullable final CharSequence mQueryHint) {
        this.mQueryHint = mQueryHint;
        this.updateQueryHint();
    }
    
    public void setQueryRefinementEnabled(final boolean mQueryRefinement) {
        this.mQueryRefinement = mQueryRefinement;
        final CursorAdapter mSuggestionsAdapter = this.mSuggestionsAdapter;
        if (mSuggestionsAdapter instanceof SuggestionsAdapter) {
            final SuggestionsAdapter suggestionsAdapter = (SuggestionsAdapter)mSuggestionsAdapter;
            int queryRefinement;
            if (mQueryRefinement) {
                queryRefinement = 2;
            }
            else {
                queryRefinement = 1;
            }
            suggestionsAdapter.setQueryRefinement(queryRefinement);
        }
    }
    
    public void setSearchableInfo(final SearchableInfo mSearchable) {
        this.mSearchable = mSearchable;
        if (this.mSearchable != null) {
            this.updateSearchAutoComplete();
            this.updateQueryHint();
        }
        this.mVoiceButtonEnabled = this.hasVoiceSearch();
        if (this.mVoiceButtonEnabled) {
            this.mSearchSrcTextView.setPrivateImeOptions("nm");
        }
        this.updateViewsVisibility(this.isIconified());
    }
    
    public void setSubmitButtonEnabled(final boolean mSubmitButtonEnabled) {
        this.mSubmitButtonEnabled = mSubmitButtonEnabled;
        this.updateViewsVisibility(this.isIconified());
    }
    
    public void setSuggestionsAdapter(final CursorAdapter mSuggestionsAdapter) {
        this.mSuggestionsAdapter = mSuggestionsAdapter;
        this.mSearchSrcTextView.setAdapter((ListAdapter)this.mSuggestionsAdapter);
    }
    
    void updateFocusedState() {
        int[] array;
        if (this.mSearchSrcTextView.hasFocus()) {
            array = SearchView.FOCUSED_STATE_SET;
        }
        else {
            array = SearchView.EMPTY_STATE_SET;
        }
        final Drawable background = this.mSearchPlate.getBackground();
        if (background != null) {
            background.setState(array);
        }
        final Drawable background2 = this.mSubmitArea.getBackground();
        if (background2 != null) {
            background2.setState(array);
        }
        this.invalidate();
    }
    
    private static class AutoCompleteTextViewReflector
    {
        private Method doAfterTextChanged;
        private Method doBeforeTextChanged;
        private Method ensureImeVisible;
        private Method showSoftInputUnchecked;
        
        AutoCompleteTextViewReflector() {
            try {
                (this.doBeforeTextChanged = AutoCompleteTextView.class.getDeclaredMethod("doBeforeTextChanged", (Class<?>[])new Class[0])).setAccessible(true);
            }
            catch (NoSuchMethodException ex) {}
            try {
                (this.doAfterTextChanged = AutoCompleteTextView.class.getDeclaredMethod("doAfterTextChanged", (Class<?>[])new Class[0])).setAccessible(true);
            }
            catch (NoSuchMethodException ex2) {}
            try {
                (this.ensureImeVisible = AutoCompleteTextView.class.getMethod("ensureImeVisible", Boolean.TYPE)).setAccessible(true);
            }
            catch (NoSuchMethodException ex3) {}
        }
        
        void doAfterTextChanged(final AutoCompleteTextView autoCompleteTextView) {
            final Method doAfterTextChanged = this.doAfterTextChanged;
            if (doAfterTextChanged != null) {
                try {
                    doAfterTextChanged.invoke(autoCompleteTextView, new Object[0]);
                }
                catch (Exception ex) {}
            }
        }
        
        void doBeforeTextChanged(final AutoCompleteTextView autoCompleteTextView) {
            final Method doBeforeTextChanged = this.doBeforeTextChanged;
            if (doBeforeTextChanged != null) {
                try {
                    doBeforeTextChanged.invoke(autoCompleteTextView, new Object[0]);
                }
                catch (Exception ex) {}
            }
        }
        
        void ensureImeVisible(final AutoCompleteTextView autoCompleteTextView, final boolean b) {
            final Method ensureImeVisible = this.ensureImeVisible;
            if (ensureImeVisible != null) {
                try {
                    ensureImeVisible.invoke(autoCompleteTextView, b);
                }
                catch (Exception ex) {}
            }
        }
    }
    
    public interface OnCloseListener
    {
        boolean onClose();
    }
    
    public interface OnQueryTextListener
    {
        boolean onQueryTextChange(final String p0);
        
        boolean onQueryTextSubmit(final String p0);
    }
    
    public interface OnSuggestionListener
    {
        boolean onSuggestionClick(final int p0);
        
        boolean onSuggestionSelect(final int p0);
    }
    
    static class SavedState extends AbsSavedState
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        boolean isIconified;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$ClassLoaderCreator<SavedState>() {
                public SavedState createFromParcel(final Parcel parcel) {
                    return new SavedState(parcel, null);
                }
                
                public SavedState createFromParcel(final Parcel parcel, final ClassLoader classLoader) {
                    return new SavedState(parcel, classLoader);
                }
                
                public SavedState[] newArray(final int n) {
                    return new SavedState[n];
                }
            };
        }
        
        public SavedState(final Parcel parcel, final ClassLoader classLoader) {
            super(parcel, classLoader);
            this.isIconified = (boolean)parcel.readValue((ClassLoader)null);
        }
        
        SavedState(final Parcelable parcelable) {
            super(parcelable);
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("SearchView.SavedState{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(" isIconified=");
            sb.append(this.isIconified);
            sb.append("}");
            return sb.toString();
        }
        
        @Override
        public void writeToParcel(final Parcel parcel, final int n) {
            super.writeToParcel(parcel, n);
            parcel.writeValue((Object)this.isIconified);
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static class SearchAutoComplete extends AppCompatAutoCompleteTextView
    {
        private boolean mHasPendingShowSoftInputRequest;
        final Runnable mRunShowSoftInputIfNecessary;
        private SearchView mSearchView;
        private int mThreshold;
        
        public SearchAutoComplete(final Context context) {
            this(context, null);
        }
        
        public SearchAutoComplete(final Context context, final AttributeSet set) {
            this(context, set, R.attr.autoCompleteTextViewStyle);
        }
        
        public SearchAutoComplete(final Context context, final AttributeSet set, final int n) {
            super(context, set, n);
            this.mRunShowSoftInputIfNecessary = new Runnable() {
                @Override
                public void run() {
                    SearchAutoComplete.this.showSoftInputIfNecessary();
                }
            };
            this.mThreshold = this.getThreshold();
        }
        
        private int getSearchViewTextMinWidthDp() {
            final Configuration configuration = this.getResources().getConfiguration();
            final int screenWidthDp = configuration.screenWidthDp;
            final int screenHeightDp = configuration.screenHeightDp;
            if (screenWidthDp >= 960 && screenHeightDp >= 720 && configuration.orientation == 2) {
                return 256;
            }
            if (screenWidthDp < 600 && (screenWidthDp < 640 || screenHeightDp < 480)) {
                return 160;
            }
            return 192;
        }
        
        private boolean isEmpty() {
            return TextUtils.getTrimmedLength((CharSequence)this.getText()) == 0;
        }
        
        private void setImeVisibility(final boolean b) {
            final InputMethodManager inputMethodManager = (InputMethodManager)this.getContext().getSystemService("input_method");
            if (!b) {
                this.mHasPendingShowSoftInputRequest = false;
                this.removeCallbacks(this.mRunShowSoftInputIfNecessary);
                inputMethodManager.hideSoftInputFromWindow(this.getWindowToken(), 0);
                return;
            }
            if (inputMethodManager.isActive((View)this)) {
                this.mHasPendingShowSoftInputRequest = false;
                this.removeCallbacks(this.mRunShowSoftInputIfNecessary);
                inputMethodManager.showSoftInput((View)this, 0);
                return;
            }
            this.mHasPendingShowSoftInputRequest = true;
        }
        
        private void showSoftInputIfNecessary() {
            if (this.mHasPendingShowSoftInputRequest) {
                ((InputMethodManager)this.getContext().getSystemService("input_method")).showSoftInput((View)this, 0);
                this.mHasPendingShowSoftInputRequest = false;
            }
        }
        
        public boolean enoughToFilter() {
            return this.mThreshold <= 0 || super.enoughToFilter();
        }
        
        public InputConnection onCreateInputConnection(final EditorInfo editorInfo) {
            final InputConnection onCreateInputConnection = super.onCreateInputConnection(editorInfo);
            if (this.mHasPendingShowSoftInputRequest) {
                this.removeCallbacks(this.mRunShowSoftInputIfNecessary);
                this.post(this.mRunShowSoftInputIfNecessary);
                return onCreateInputConnection;
            }
            return onCreateInputConnection;
        }
        
        protected void onFinishInflate() {
            super.onFinishInflate();
            this.setMinWidth((int)TypedValue.applyDimension(1, (float)this.getSearchViewTextMinWidthDp(), this.getResources().getDisplayMetrics()));
        }
        
        protected void onFocusChanged(final boolean b, final int n, final Rect rect) {
            super.onFocusChanged(b, n, rect);
            this.mSearchView.onTextFocusChanged();
        }
        
        public boolean onKeyPreIme(final int n, final KeyEvent keyEvent) {
            if (n == 4) {
                if (keyEvent.getAction() == 0 && keyEvent.getRepeatCount() == 0) {
                    final KeyEvent$DispatcherState keyDispatcherState = this.getKeyDispatcherState();
                    if (keyDispatcherState != null) {
                        keyDispatcherState.startTracking(keyEvent, (Object)this);
                        return true;
                    }
                    return true;
                }
                else if (keyEvent.getAction() == 1) {
                    final KeyEvent$DispatcherState keyDispatcherState2 = this.getKeyDispatcherState();
                    if (keyDispatcherState2 != null) {
                        keyDispatcherState2.handleUpEvent(keyEvent);
                    }
                    if (keyEvent.isTracking() && !keyEvent.isCanceled()) {
                        this.mSearchView.clearFocus();
                        this.setImeVisibility(false);
                        return true;
                    }
                }
            }
            return super.onKeyPreIme(n, keyEvent);
        }
        
        public void onWindowFocusChanged(final boolean b) {
            super.onWindowFocusChanged(b);
            if (!b || !this.mSearchView.hasFocus() || this.getVisibility() != 0) {
                return;
            }
            this.mHasPendingShowSoftInputRequest = true;
            if (SearchView.isLandscapeMode(this.getContext())) {
                SearchView.HIDDEN_METHOD_INVOKER.ensureImeVisible(this, true);
            }
        }
        
        public void performCompletion() {
        }
        
        protected void replaceText(final CharSequence charSequence) {
        }
        
        void setSearchView(final SearchView mSearchView) {
            this.mSearchView = mSearchView;
        }
        
        public void setThreshold(final int n) {
            super.setThreshold(n);
            this.mThreshold = n;
        }
    }
    
    private static class UpdatableTouchDelegate extends TouchDelegate
    {
        private final Rect mActualBounds;
        private boolean mDelegateTargeted;
        private final View mDelegateView;
        private final int mSlop;
        private final Rect mSlopBounds;
        private final Rect mTargetBounds;
        
        public UpdatableTouchDelegate(final Rect rect, final Rect rect2, final View mDelegateView) {
            super(rect, mDelegateView);
            this.mSlop = ViewConfiguration.get(mDelegateView.getContext()).getScaledTouchSlop();
            this.mTargetBounds = new Rect();
            this.mSlopBounds = new Rect();
            this.mActualBounds = new Rect();
            this.setBounds(rect, rect2);
            this.mDelegateView = mDelegateView;
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            final int n = (int)motionEvent.getX();
            final int n2 = (int)motionEvent.getY();
            int n3 = 0;
            boolean b = true;
            switch (motionEvent.getAction()) {
                case 3: {
                    n3 = (this.mDelegateTargeted ? 1 : 0);
                    this.mDelegateTargeted = false;
                    break;
                }
                case 1:
                case 2: {
                    n3 = (this.mDelegateTargeted ? 1 : 0);
                    if (n3 == 0) {
                        break;
                    }
                    if (!this.mSlopBounds.contains(n, n2)) {
                        b = false;
                        break;
                    }
                    break;
                }
                case 0: {
                    if (this.mTargetBounds.contains(n, n2)) {
                        this.mDelegateTargeted = true;
                        n3 = 1;
                        break;
                    }
                    break;
                }
            }
            if (n3 != 0) {
                if (b && !this.mActualBounds.contains(n, n2)) {
                    motionEvent.setLocation((float)(this.mDelegateView.getWidth() / 2), (float)(this.mDelegateView.getHeight() / 2));
                }
                else {
                    motionEvent.setLocation((float)(n - this.mActualBounds.left), (float)(n2 - this.mActualBounds.top));
                }
                return this.mDelegateView.dispatchTouchEvent(motionEvent);
            }
            return false;
        }
        
        public void setBounds(Rect mSlopBounds, final Rect rect) {
            this.mTargetBounds.set(mSlopBounds);
            this.mSlopBounds.set(mSlopBounds);
            mSlopBounds = this.mSlopBounds;
            final int mSlop = this.mSlop;
            mSlopBounds.inset(-mSlop, -mSlop);
            this.mActualBounds.set(rect);
        }
    }
}
