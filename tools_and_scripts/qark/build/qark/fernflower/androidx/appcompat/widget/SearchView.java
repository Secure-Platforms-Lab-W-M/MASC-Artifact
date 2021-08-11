package androidx.appcompat.widget;

import android.app.PendingIntent;
import android.app.SearchableInfo;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.KeyEvent.DispatcherState;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLayoutChangeListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView.OnEditorActionListener;
import androidx.appcompat.R.attr;
import androidx.appcompat.R.dimen;
import androidx.appcompat.R.id;
import androidx.appcompat.R.layout;
import androidx.appcompat.R.string;
import androidx.appcompat.R.styleable;
import androidx.appcompat.view.CollapsibleActionView;
import androidx.core.view.ViewCompat;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.customview.view.AbsSavedState;
import java.lang.reflect.Method;
import java.util.WeakHashMap;

public class SearchView extends LinearLayoutCompat implements CollapsibleActionView {
   static final boolean DBG = false;
   static final SearchView.AutoCompleteTextViewReflector HIDDEN_METHOD_INVOKER = new SearchView.AutoCompleteTextViewReflector();
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
   private final OnClickListener mOnClickListener;
   private SearchView.OnCloseListener mOnCloseListener;
   private final OnEditorActionListener mOnEditorActionListener;
   private final OnItemClickListener mOnItemClickListener;
   private final OnItemSelectedListener mOnItemSelectedListener;
   private SearchView.OnQueryTextListener mOnQueryChangeListener;
   OnFocusChangeListener mOnQueryTextFocusChangeListener;
   private OnClickListener mOnSearchClickListener;
   private SearchView.OnSuggestionListener mOnSuggestionListener;
   private final WeakHashMap mOutsideDrawablesCache;
   private CharSequence mQueryHint;
   private boolean mQueryRefinement;
   private Runnable mReleaseCursorRunnable;
   final ImageView mSearchButton;
   private final View mSearchEditFrame;
   private final Drawable mSearchHintIcon;
   private final View mSearchPlate;
   final SearchView.SearchAutoComplete mSearchSrcTextView;
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
   OnKeyListener mTextKeyListener;
   private TextWatcher mTextWatcher;
   private SearchView.UpdatableTouchDelegate mTouchDelegate;
   private final Runnable mUpdateDrawableStateRunnable;
   private CharSequence mUserQuery;
   private final Intent mVoiceAppSearchIntent;
   final ImageView mVoiceButton;
   private boolean mVoiceButtonEnabled;
   private final Intent mVoiceWebSearchIntent;

   public SearchView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public SearchView(Context var1, AttributeSet var2) {
      this(var1, var2, attr.searchViewStyle);
   }

   public SearchView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mSearchSrcTextViewBounds = new Rect();
      this.mSearchSrtTextViewBoundsExpanded = new Rect();
      this.mTemp = new int[2];
      this.mTemp2 = new int[2];
      this.mUpdateDrawableStateRunnable = new Runnable() {
         public void run() {
            SearchView.this.updateFocusedState();
         }
      };
      this.mReleaseCursorRunnable = new Runnable() {
         public void run() {
            if (SearchView.this.mSuggestionsAdapter instanceof SuggestionsAdapter) {
               SearchView.this.mSuggestionsAdapter.changeCursor((Cursor)null);
            }

         }
      };
      this.mOutsideDrawablesCache = new WeakHashMap();
      this.mOnClickListener = new OnClickListener() {
         public void onClick(View var1) {
            if (var1 == SearchView.this.mSearchButton) {
               SearchView.this.onSearchClicked();
            } else if (var1 == SearchView.this.mCloseButton) {
               SearchView.this.onCloseClicked();
            } else if (var1 == SearchView.this.mGoButton) {
               SearchView.this.onSubmitQuery();
            } else if (var1 == SearchView.this.mVoiceButton) {
               SearchView.this.onVoiceClicked();
            } else {
               if (var1 == SearchView.this.mSearchSrcTextView) {
                  SearchView.this.forceSuggestionQuery();
               }

            }
         }
      };
      this.mTextKeyListener = new OnKeyListener() {
         public boolean onKey(View var1, int var2, KeyEvent var3) {
            if (SearchView.this.mSearchable == null) {
               return false;
            } else if (SearchView.this.mSearchSrcTextView.isPopupShowing() && SearchView.this.mSearchSrcTextView.getListSelection() != -1) {
               return SearchView.this.onSuggestionsKey(var1, var2, var3);
            } else if (!SearchView.this.mSearchSrcTextView.isEmpty() && var3.hasNoModifiers() && var3.getAction() == 1 && var2 == 66) {
               var1.cancelLongPress();
               SearchView var4 = SearchView.this;
               var4.launchQuerySearch(0, (String)null, var4.mSearchSrcTextView.getText().toString());
               return true;
            } else {
               return false;
            }
         }
      };
      this.mOnEditorActionListener = new OnEditorActionListener() {
         public boolean onEditorAction(TextView var1, int var2, KeyEvent var3) {
            SearchView.this.onSubmitQuery();
            return true;
         }
      };
      this.mOnItemClickListener = new OnItemClickListener() {
         public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
            SearchView.this.onItemClicked(var3, 0, (String)null);
         }
      };
      this.mOnItemSelectedListener = new OnItemSelectedListener() {
         public void onItemSelected(AdapterView var1, View var2, int var3, long var4) {
            SearchView.this.onItemSelected(var3);
         }

         public void onNothingSelected(AdapterView var1) {
         }
      };
      this.mTextWatcher = new TextWatcher() {
         public void afterTextChanged(Editable var1) {
         }

         public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
         }

         public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
            SearchView.this.onTextChanged(var1);
         }
      };
      TintTypedArray var6 = TintTypedArray.obtainStyledAttributes(var1, var2, styleable.SearchView, var3, 0);
      LayoutInflater.from(var1).inflate(var6.getResourceId(styleable.SearchView_layout, layout.abc_search_view), this, true);
      SearchView.SearchAutoComplete var4 = (SearchView.SearchAutoComplete)this.findViewById(id.search_src_text);
      this.mSearchSrcTextView = var4;
      var4.setSearchView(this);
      this.mSearchEditFrame = this.findViewById(id.search_edit_frame);
      this.mSearchPlate = this.findViewById(id.search_plate);
      this.mSubmitArea = this.findViewById(id.submit_area);
      this.mSearchButton = (ImageView)this.findViewById(id.search_button);
      this.mGoButton = (ImageView)this.findViewById(id.search_go_btn);
      this.mCloseButton = (ImageView)this.findViewById(id.search_close_btn);
      this.mVoiceButton = (ImageView)this.findViewById(id.search_voice_btn);
      this.mCollapsedIcon = (ImageView)this.findViewById(id.search_mag_icon);
      ViewCompat.setBackground(this.mSearchPlate, var6.getDrawable(styleable.SearchView_queryBackground));
      ViewCompat.setBackground(this.mSubmitArea, var6.getDrawable(styleable.SearchView_submitBackground));
      this.mSearchButton.setImageDrawable(var6.getDrawable(styleable.SearchView_searchIcon));
      this.mGoButton.setImageDrawable(var6.getDrawable(styleable.SearchView_goIcon));
      this.mCloseButton.setImageDrawable(var6.getDrawable(styleable.SearchView_closeIcon));
      this.mVoiceButton.setImageDrawable(var6.getDrawable(styleable.SearchView_voiceIcon));
      this.mCollapsedIcon.setImageDrawable(var6.getDrawable(styleable.SearchView_searchIcon));
      this.mSearchHintIcon = var6.getDrawable(styleable.SearchView_searchHintIcon);
      TooltipCompat.setTooltipText(this.mSearchButton, this.getResources().getString(string.abc_searchview_description_search));
      this.mSuggestionRowLayout = var6.getResourceId(styleable.SearchView_suggestionRowLayout, layout.abc_search_dropdown_item_icons_2line);
      this.mSuggestionCommitIconResId = var6.getResourceId(styleable.SearchView_commitIcon, 0);
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
      this.mSearchSrcTextView.setOnFocusChangeListener(new OnFocusChangeListener() {
         public void onFocusChange(View var1, boolean var2) {
            if (SearchView.this.mOnQueryTextFocusChangeListener != null) {
               SearchView.this.mOnQueryTextFocusChangeListener.onFocusChange(SearchView.this, var2);
            }

         }
      });
      this.setIconifiedByDefault(var6.getBoolean(styleable.SearchView_iconifiedByDefault, true));
      var3 = var6.getDimensionPixelSize(styleable.SearchView_android_maxWidth, -1);
      if (var3 != -1) {
         this.setMaxWidth(var3);
      }

      this.mDefaultQueryHint = var6.getText(styleable.SearchView_defaultQueryHint);
      this.mQueryHint = var6.getText(styleable.SearchView_queryHint);
      var3 = var6.getInt(styleable.SearchView_android_imeOptions, -1);
      if (var3 != -1) {
         this.setImeOptions(var3);
      }

      var3 = var6.getInt(styleable.SearchView_android_inputType, -1);
      if (var3 != -1) {
         this.setInputType(var3);
      }

      this.setFocusable(var6.getBoolean(styleable.SearchView_android_focusable, true));
      var6.recycle();
      Intent var5 = new Intent("android.speech.action.WEB_SEARCH");
      this.mVoiceWebSearchIntent = var5;
      var5.addFlags(268435456);
      this.mVoiceWebSearchIntent.putExtra("android.speech.extra.LANGUAGE_MODEL", "web_search");
      var5 = new Intent("android.speech.action.RECOGNIZE_SPEECH");
      this.mVoiceAppSearchIntent = var5;
      var5.addFlags(268435456);
      View var7 = this.findViewById(this.mSearchSrcTextView.getDropDownAnchor());
      this.mDropDownAnchor = var7;
      if (var7 != null) {
         var7.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            public void onLayoutChange(View var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
               SearchView.this.adjustDropDownSizeAndPosition();
            }
         });
      }

      this.updateViewsVisibility(this.mIconifiedByDefault);
      this.updateQueryHint();
   }

   private Intent createIntent(String var1, Uri var2, String var3, String var4, int var5, String var6) {
      Intent var7 = new Intent(var1);
      var7.addFlags(268435456);
      if (var2 != null) {
         var7.setData(var2);
      }

      var7.putExtra("user_query", this.mUserQuery);
      if (var4 != null) {
         var7.putExtra("query", var4);
      }

      if (var3 != null) {
         var7.putExtra("intent_extra_data_key", var3);
      }

      Bundle var8 = this.mAppSearchData;
      if (var8 != null) {
         var7.putExtra("app_data", var8);
      }

      if (var5 != 0) {
         var7.putExtra("action_key", var5);
         var7.putExtra("action_msg", var6);
      }

      var7.setComponent(this.mSearchable.getSearchActivity());
      return var7;
   }

   private Intent createIntentFromSuggestion(Cursor var1, int var2, String var3) {
      RuntimeException var10000;
      label83: {
         String var5;
         boolean var10001;
         try {
            var5 = SuggestionsAdapter.getColumnString(var1, "suggest_intent_action");
         } catch (RuntimeException var16) {
            var10000 = var16;
            var10001 = false;
            break label83;
         }

         String var4 = var5;
         if (var5 == null) {
            try {
               var4 = this.mSearchable.getSuggestIntentAction();
            } catch (RuntimeException var15) {
               var10000 = var15;
               var10001 = false;
               break label83;
            }
         }

         var5 = var4;
         if (var4 == null) {
            var5 = "android.intent.action.SEARCH";
         }

         String var6;
         try {
            var6 = SuggestionsAdapter.getColumnString(var1, "suggest_intent_data");
         } catch (RuntimeException var14) {
            var10000 = var14;
            var10001 = false;
            break label83;
         }

         var4 = var6;
         if (var6 == null) {
            try {
               var4 = this.mSearchable.getSuggestIntentData();
            } catch (RuntimeException var13) {
               var10000 = var13;
               var10001 = false;
               break label83;
            }
         }

         if (var4 != null) {
            try {
               var6 = SuggestionsAdapter.getColumnString(var1, "suggest_intent_data_id");
            } catch (RuntimeException var12) {
               var10000 = var12;
               var10001 = false;
               break label83;
            }

            if (var6 != null) {
               try {
                  StringBuilder var7 = new StringBuilder();
                  var7.append(var4);
                  var7.append("/");
                  var7.append(Uri.encode(var6));
                  var4 = var7.toString();
               } catch (RuntimeException var11) {
                  var10000 = var11;
                  var10001 = false;
                  break label83;
               }
            }
         }

         Uri var20;
         if (var4 == null) {
            var20 = null;
         } else {
            try {
               var20 = Uri.parse(var4);
            } catch (RuntimeException var10) {
               var10000 = var10;
               var10001 = false;
               break label83;
            }
         }

         try {
            var6 = SuggestionsAdapter.getColumnString(var1, "suggest_intent_query");
            Intent var19 = this.createIntent(var5, var20, SuggestionsAdapter.getColumnString(var1, "suggest_intent_extra_data"), var6, var2, var3);
            return var19;
         } catch (RuntimeException var9) {
            var10000 = var9;
            var10001 = false;
         }
      }

      RuntimeException var18 = var10000;

      try {
         var2 = var1.getPosition();
      } catch (RuntimeException var8) {
         var2 = -1;
      }

      StringBuilder var17 = new StringBuilder();
      var17.append("Search suggestions cursor at row ");
      var17.append(var2);
      var17.append(" returned exception.");
      Log.w("SearchView", var17.toString(), var18);
      return null;
   }

   private Intent createVoiceAppSearchIntent(Intent var1, SearchableInfo var2) {
      ComponentName var8 = var2.getSearchActivity();
      Intent var4 = new Intent("android.intent.action.SEARCH");
      var4.setComponent(var8);
      PendingIntent var6 = PendingIntent.getActivity(this.getContext(), 0, var4, 1073741824);
      Bundle var7 = new Bundle();
      Bundle var12 = this.mAppSearchData;
      if (var12 != null) {
         var7.putParcelable("app_data", var12);
      }

      Intent var9 = new Intent(var1);
      String var11 = "free_form";
      String var13 = null;
      String var5 = null;
      int var3 = 1;
      Resources var10 = this.getResources();
      if (var2.getVoiceLanguageModeId() != 0) {
         var11 = var10.getString(var2.getVoiceLanguageModeId());
      }

      if (var2.getVoicePromptTextId() != 0) {
         var13 = var10.getString(var2.getVoicePromptTextId());
      }

      if (var2.getVoiceLanguageId() != 0) {
         var5 = var10.getString(var2.getVoiceLanguageId());
      }

      if (var2.getVoiceMaxResults() != 0) {
         var3 = var2.getVoiceMaxResults();
      }

      var9.putExtra("android.speech.extra.LANGUAGE_MODEL", var11);
      var9.putExtra("android.speech.extra.PROMPT", var13);
      var9.putExtra("android.speech.extra.LANGUAGE", var5);
      var9.putExtra("android.speech.extra.MAX_RESULTS", var3);
      if (var8 == null) {
         var11 = null;
      } else {
         var11 = var8.flattenToShortString();
      }

      var9.putExtra("calling_package", var11);
      var9.putExtra("android.speech.extra.RESULTS_PENDINGINTENT", var6);
      var9.putExtra("android.speech.extra.RESULTS_PENDINGINTENT_BUNDLE", var7);
      return var9;
   }

   private Intent createVoiceWebSearchIntent(Intent var1, SearchableInfo var2) {
      Intent var3 = new Intent(var1);
      ComponentName var4 = var2.getSearchActivity();
      String var5;
      if (var4 == null) {
         var5 = null;
      } else {
         var5 = var4.flattenToShortString();
      }

      var3.putExtra("calling_package", var5);
      return var3;
   }

   private void dismissSuggestions() {
      this.mSearchSrcTextView.dismissDropDown();
   }

   private void getChildBoundsWithinSearchView(View var1, Rect var2) {
      var1.getLocationInWindow(this.mTemp);
      this.getLocationInWindow(this.mTemp2);
      int[] var5 = this.mTemp;
      int var3 = var5[1];
      int[] var6 = this.mTemp2;
      var3 -= var6[1];
      int var4 = var5[0] - var6[0];
      var2.set(var4, var3, var1.getWidth() + var4, var1.getHeight() + var3);
   }

   private CharSequence getDecoratedHint(CharSequence var1) {
      if (this.mIconifiedByDefault) {
         if (this.mSearchHintIcon == null) {
            return var1;
         } else {
            int var2 = (int)((double)this.mSearchSrcTextView.getTextSize() * 1.25D);
            this.mSearchHintIcon.setBounds(0, 0, var2, var2);
            SpannableStringBuilder var3 = new SpannableStringBuilder("   ");
            var3.setSpan(new ImageSpan(this.mSearchHintIcon), 1, 2, 33);
            var3.append(var1);
            return var3;
         }
      } else {
         return var1;
      }
   }

   private int getPreferredHeight() {
      return this.getContext().getResources().getDimensionPixelSize(dimen.abc_search_view_preferred_height);
   }

   private int getPreferredWidth() {
      return this.getContext().getResources().getDimensionPixelSize(dimen.abc_search_view_preferred_width);
   }

   private boolean hasVoiceSearch() {
      SearchableInfo var2 = this.mSearchable;
      boolean var1 = false;
      if (var2 != null && var2.getVoiceSearchEnabled()) {
         Intent var3 = null;
         if (this.mSearchable.getVoiceSearchLaunchWebSearch()) {
            var3 = this.mVoiceWebSearchIntent;
         } else if (this.mSearchable.getVoiceSearchLaunchRecognizer()) {
            var3 = this.mVoiceAppSearchIntent;
         }

         if (var3 != null) {
            if (this.getContext().getPackageManager().resolveActivity(var3, 65536) != null) {
               var1 = true;
            }

            return var1;
         }
      }

      return false;
   }

   static boolean isLandscapeMode(Context var0) {
      return var0.getResources().getConfiguration().orientation == 2;
   }

   private boolean isSubmitAreaEnabled() {
      return (this.mSubmitButtonEnabled || this.mVoiceButtonEnabled) && !this.isIconified();
   }

   private void launchIntent(Intent var1) {
      if (var1 != null) {
         try {
            this.getContext().startActivity(var1);
         } catch (RuntimeException var4) {
            StringBuilder var3 = new StringBuilder();
            var3.append("Failed launch activity: ");
            var3.append(var1);
            Log.e("SearchView", var3.toString(), var4);
         }
      }
   }

   private boolean launchSuggestion(int var1, int var2, String var3) {
      Cursor var4 = this.mSuggestionsAdapter.getCursor();
      if (var4 != null && var4.moveToPosition(var1)) {
         this.launchIntent(this.createIntentFromSuggestion(var4, var2, var3));
         return true;
      } else {
         return false;
      }
   }

   private void postUpdateFocusedState() {
      this.post(this.mUpdateDrawableStateRunnable);
   }

   private void rewriteQueryFromSuggestion(int var1) {
      Editable var2 = this.mSearchSrcTextView.getText();
      Cursor var3 = this.mSuggestionsAdapter.getCursor();
      if (var3 != null) {
         if (var3.moveToPosition(var1)) {
            CharSequence var4 = this.mSuggestionsAdapter.convertToString(var3);
            if (var4 != null) {
               this.setQuery(var4);
            } else {
               this.setQuery(var2);
            }

         } else {
            this.setQuery(var2);
         }
      }
   }

   private void setQuery(CharSequence var1) {
      this.mSearchSrcTextView.setText(var1);
      SearchView.SearchAutoComplete var3 = this.mSearchSrcTextView;
      int var2;
      if (TextUtils.isEmpty(var1)) {
         var2 = 0;
      } else {
         var2 = var1.length();
      }

      var3.setSelection(var2);
   }

   private void updateCloseButton() {
      boolean var5 = TextUtils.isEmpty(this.mSearchSrcTextView.getText());
      boolean var3 = true;
      boolean var4 = var5 ^ true;
      byte var2 = 0;
      boolean var1 = var3;
      if (!var4) {
         if (this.mIconifiedByDefault && !this.mExpandedInActionView) {
            var1 = var3;
         } else {
            var1 = false;
         }
      }

      ImageView var6 = this.mCloseButton;
      byte var8;
      if (var1) {
         var8 = var2;
      } else {
         var8 = 8;
      }

      var6.setVisibility(var8);
      Drawable var7 = this.mCloseButton.getDrawable();
      if (var7 != null) {
         int[] var9;
         if (var4) {
            var9 = ENABLED_STATE_SET;
         } else {
            var9 = EMPTY_STATE_SET;
         }

         var7.setState(var9);
      }

   }

   private void updateQueryHint() {
      Object var1 = this.getQueryHint();
      SearchView.SearchAutoComplete var2 = this.mSearchSrcTextView;
      if (var1 == null) {
         var1 = "";
      }

      var2.setHint(this.getDecoratedHint((CharSequence)var1));
   }

   private void updateSearchAutoComplete() {
      this.mSearchSrcTextView.setThreshold(this.mSearchable.getSuggestThreshold());
      this.mSearchSrcTextView.setImeOptions(this.mSearchable.getImeOptions());
      int var3 = this.mSearchable.getInputType();
      byte var2 = 1;
      int var1 = var3;
      if ((var3 & 15) == 1) {
         var3 &= -65537;
         var1 = var3;
         if (this.mSearchable.getSuggestAuthority() != null) {
            var1 = var3 | 65536 | 524288;
         }
      }

      this.mSearchSrcTextView.setInputType(var1);
      CursorAdapter var4 = this.mSuggestionsAdapter;
      if (var4 != null) {
         var4.changeCursor((Cursor)null);
      }

      if (this.mSearchable.getSuggestAuthority() != null) {
         SuggestionsAdapter var6 = new SuggestionsAdapter(this.getContext(), this, this.mSearchable, this.mOutsideDrawablesCache);
         this.mSuggestionsAdapter = var6;
         this.mSearchSrcTextView.setAdapter(var6);
         var6 = (SuggestionsAdapter)this.mSuggestionsAdapter;
         byte var5 = var2;
         if (this.mQueryRefinement) {
            var5 = 2;
         }

         var6.setQueryRefinement(var5);
      }

   }

   private void updateSubmitArea() {
      byte var2 = 8;
      byte var1 = var2;
      if (this.isSubmitAreaEnabled()) {
         label12: {
            if (this.mGoButton.getVisibility() != 0) {
               var1 = var2;
               if (this.mVoiceButton.getVisibility() != 0) {
                  break label12;
               }
            }

            var1 = 0;
         }
      }

      this.mSubmitArea.setVisibility(var1);
   }

   private void updateSubmitButton(boolean var1) {
      byte var3 = 8;
      byte var2 = var3;
      if (this.mSubmitButtonEnabled) {
         var2 = var3;
         if (this.isSubmitAreaEnabled()) {
            var2 = var3;
            if (this.hasFocus()) {
               label14: {
                  if (!var1) {
                     var2 = var3;
                     if (this.mVoiceButtonEnabled) {
                        break label14;
                     }
                  }

                  var2 = 0;
               }
            }
         }
      }

      this.mGoButton.setVisibility(var2);
   }

   private void updateViewsVisibility(boolean var1) {
      this.mIconified = var1;
      byte var3 = 8;
      boolean var4 = false;
      byte var2;
      if (var1) {
         var2 = 0;
      } else {
         var2 = 8;
      }

      boolean var5 = TextUtils.isEmpty(this.mSearchSrcTextView.getText()) ^ true;
      this.mSearchButton.setVisibility(var2);
      this.updateSubmitButton(var5);
      View var6 = this.mSearchEditFrame;
      if (var1) {
         var2 = var3;
      } else {
         var2 = 0;
      }

      var6.setVisibility(var2);
      if (this.mCollapsedIcon.getDrawable() != null && !this.mIconifiedByDefault) {
         var2 = 0;
      } else {
         var2 = 8;
      }

      this.mCollapsedIcon.setVisibility(var2);
      this.updateCloseButton();
      var1 = var4;
      if (!var5) {
         var1 = true;
      }

      this.updateVoiceButton(var1);
      this.updateSubmitArea();
   }

   private void updateVoiceButton(boolean var1) {
      byte var3 = 8;
      byte var2 = var3;
      if (this.mVoiceButtonEnabled) {
         var2 = var3;
         if (!this.isIconified()) {
            var2 = var3;
            if (var1) {
               var2 = 0;
               this.mGoButton.setVisibility(8);
            }
         }
      }

      this.mVoiceButton.setVisibility(var2);
   }

   void adjustDropDownSizeAndPosition() {
      if (this.mDropDownAnchor.getWidth() > 1) {
         Resources var7 = this.getContext().getResources();
         int var3 = this.mSearchPlate.getPaddingLeft();
         Rect var8 = new Rect();
         boolean var6 = ViewUtils.isLayoutRtl(this);
         int var1;
         if (this.mIconifiedByDefault) {
            var1 = var7.getDimensionPixelSize(dimen.abc_dropdownitem_icon_width) + var7.getDimensionPixelSize(dimen.abc_dropdownitem_text_padding_left);
         } else {
            var1 = 0;
         }

         this.mSearchSrcTextView.getDropDownBackground().getPadding(var8);
         int var2;
         if (var6) {
            var2 = -var8.left;
         } else {
            var2 = var3 - (var8.left + var1);
         }

         this.mSearchSrcTextView.setDropDownHorizontalOffset(var2);
         var2 = this.mDropDownAnchor.getWidth();
         int var4 = var8.left;
         int var5 = var8.right;
         this.mSearchSrcTextView.setDropDownWidth(var2 + var4 + var5 + var1 - var3);
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
      if (VERSION.SDK_INT >= 29) {
         this.mSearchSrcTextView.refreshAutoCompleteResults();
      } else {
         HIDDEN_METHOD_INVOKER.doBeforeTextChanged(this.mSearchSrcTextView);
         HIDDEN_METHOD_INVOKER.doAfterTextChanged(this.mSearchSrcTextView);
      }
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
      return this.mSearchSrcTextView.getText();
   }

   public CharSequence getQueryHint() {
      if (this.mQueryHint != null) {
         return this.mQueryHint;
      } else {
         SearchableInfo var1 = this.mSearchable;
         return var1 != null && var1.getHintId() != 0 ? this.getContext().getText(this.mSearchable.getHintId()) : this.mDefaultQueryHint;
      }
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

   void launchQuerySearch(int var1, String var2, String var3) {
      Intent var4 = this.createIntent("android.intent.action.SEARCH", (Uri)null, (String)null, var3, var1, var2);
      this.getContext().startActivity(var4);
   }

   public void onActionViewCollapsed() {
      this.setQuery("", false);
      this.clearFocus();
      this.updateViewsVisibility(true);
      this.mSearchSrcTextView.setImeOptions(this.mCollapsedImeOptions);
      this.mExpandedInActionView = false;
   }

   public void onActionViewExpanded() {
      if (!this.mExpandedInActionView) {
         this.mExpandedInActionView = true;
         int var1 = this.mSearchSrcTextView.getImeOptions();
         this.mCollapsedImeOptions = var1;
         this.mSearchSrcTextView.setImeOptions(var1 | 33554432);
         this.mSearchSrcTextView.setText("");
         this.setIconified(false);
      }
   }

   void onCloseClicked() {
      if (TextUtils.isEmpty(this.mSearchSrcTextView.getText())) {
         if (this.mIconifiedByDefault) {
            SearchView.OnCloseListener var1 = this.mOnCloseListener;
            if (var1 == null || !var1.onClose()) {
               this.clearFocus();
               this.updateViewsVisibility(true);
               return;
            }
         }
      } else {
         this.mSearchSrcTextView.setText("");
         this.mSearchSrcTextView.requestFocus();
         this.mSearchSrcTextView.setImeVisibility(true);
      }

   }

   protected void onDetachedFromWindow() {
      this.removeCallbacks(this.mUpdateDrawableStateRunnable);
      this.post(this.mReleaseCursorRunnable);
      super.onDetachedFromWindow();
   }

   boolean onItemClicked(int var1, int var2, String var3) {
      SearchView.OnSuggestionListener var4 = this.mOnSuggestionListener;
      if (var4 != null && var4.onSuggestionClick(var1)) {
         return false;
      } else {
         this.launchSuggestion(var1, 0, (String)null);
         this.mSearchSrcTextView.setImeVisibility(false);
         this.dismissSuggestions();
         return true;
      }
   }

   boolean onItemSelected(int var1) {
      SearchView.OnSuggestionListener var2 = this.mOnSuggestionListener;
      if (var2 != null && var2.onSuggestionSelect(var1)) {
         return false;
      } else {
         this.rewriteQueryFromSuggestion(var1);
         return true;
      }
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      if (var1) {
         this.getChildBoundsWithinSearchView(this.mSearchSrcTextView, this.mSearchSrcTextViewBounds);
         this.mSearchSrtTextViewBoundsExpanded.set(this.mSearchSrcTextViewBounds.left, 0, this.mSearchSrcTextViewBounds.right, var5 - var3);
         SearchView.UpdatableTouchDelegate var6 = this.mTouchDelegate;
         if (var6 == null) {
            var6 = new SearchView.UpdatableTouchDelegate(this.mSearchSrtTextViewBoundsExpanded, this.mSearchSrcTextViewBounds, this.mSearchSrcTextView);
            this.mTouchDelegate = var6;
            this.setTouchDelegate(var6);
            return;
         }

         var6.setBounds(this.mSearchSrtTextViewBoundsExpanded, this.mSearchSrcTextViewBounds);
      }

   }

   protected void onMeasure(int var1, int var2) {
      if (this.isIconified()) {
         super.onMeasure(var1, var2);
      } else {
         int var4 = MeasureSpec.getMode(var1);
         int var3 = MeasureSpec.getSize(var1);
         if (var4 != Integer.MIN_VALUE) {
            if (var4 != 0) {
               if (var4 != 1073741824) {
                  var1 = var3;
               } else {
                  var4 = this.mMaxWidth;
                  var1 = var3;
                  if (var4 > 0) {
                     var1 = Math.min(var4, var3);
                  }
               }
            } else {
               var1 = this.mMaxWidth;
               if (var1 <= 0) {
                  var1 = this.getPreferredWidth();
               }
            }
         } else {
            var1 = this.mMaxWidth;
            if (var1 > 0) {
               var1 = Math.min(var1, var3);
            } else {
               var1 = Math.min(this.getPreferredWidth(), var3);
            }
         }

         var3 = MeasureSpec.getMode(var2);
         var2 = MeasureSpec.getSize(var2);
         if (var3 != Integer.MIN_VALUE) {
            if (var3 == 0) {
               var2 = this.getPreferredHeight();
            }
         } else {
            var2 = Math.min(this.getPreferredHeight(), var2);
         }

         super.onMeasure(MeasureSpec.makeMeasureSpec(var1, 1073741824), MeasureSpec.makeMeasureSpec(var2, 1073741824));
      }
   }

   void onQueryRefine(CharSequence var1) {
      this.setQuery(var1);
   }

   protected void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof SearchView.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         SearchView.SavedState var2 = (SearchView.SavedState)var1;
         super.onRestoreInstanceState(var2.getSuperState());
         this.updateViewsVisibility(var2.isIconified);
         this.requestLayout();
      }
   }

   protected Parcelable onSaveInstanceState() {
      SearchView.SavedState var1 = new SearchView.SavedState(super.onSaveInstanceState());
      var1.isIconified = this.isIconified();
      return var1;
   }

   void onSearchClicked() {
      this.updateViewsVisibility(false);
      this.mSearchSrcTextView.requestFocus();
      this.mSearchSrcTextView.setImeVisibility(true);
      OnClickListener var1 = this.mOnSearchClickListener;
      if (var1 != null) {
         var1.onClick(this);
      }

   }

   void onSubmitQuery() {
      Editable var1 = this.mSearchSrcTextView.getText();
      if (var1 != null && TextUtils.getTrimmedLength(var1) > 0) {
         SearchView.OnQueryTextListener var2 = this.mOnQueryChangeListener;
         if (var2 == null || !var2.onQueryTextSubmit(var1.toString())) {
            if (this.mSearchable != null) {
               this.launchQuerySearch(0, (String)null, var1.toString());
            }

            this.mSearchSrcTextView.setImeVisibility(false);
            this.dismissSuggestions();
         }
      }

   }

   boolean onSuggestionsKey(View var1, int var2, KeyEvent var3) {
      if (this.mSearchable == null) {
         return false;
      } else if (this.mSuggestionsAdapter == null) {
         return false;
      } else {
         if (var3.getAction() == 0 && var3.hasNoModifiers()) {
            if (var2 == 66 || var2 == 84 || var2 == 61) {
               return this.onItemClicked(this.mSearchSrcTextView.getListSelection(), 0, (String)null);
            }

            if (var2 == 21 || var2 == 22) {
               if (var2 == 21) {
                  var2 = 0;
               } else {
                  var2 = this.mSearchSrcTextView.length();
               }

               this.mSearchSrcTextView.setSelection(var2);
               this.mSearchSrcTextView.setListSelection(0);
               this.mSearchSrcTextView.clearListSelection();
               HIDDEN_METHOD_INVOKER.ensureImeVisible(this.mSearchSrcTextView, true);
               return true;
            }

            if (var2 == 19 && this.mSearchSrcTextView.getListSelection() == 0) {
               return false;
            }
         }

         return false;
      }
   }

   void onTextChanged(CharSequence var1) {
      Editable var4 = this.mSearchSrcTextView.getText();
      this.mUserQuery = var4;
      boolean var3 = TextUtils.isEmpty(var4);
      boolean var2 = true;
      var3 ^= true;
      this.updateSubmitButton(var3);
      if (var3) {
         var2 = false;
      }

      this.updateVoiceButton(var2);
      this.updateCloseButton();
      this.updateSubmitArea();
      if (this.mOnQueryChangeListener != null && !TextUtils.equals(var1, this.mOldQueryText)) {
         this.mOnQueryChangeListener.onQueryTextChange(var1.toString());
      }

      this.mOldQueryText = var1.toString();
   }

   void onTextFocusChanged() {
      this.updateViewsVisibility(this.isIconified());
      this.postUpdateFocusedState();
      if (this.mSearchSrcTextView.hasFocus()) {
         this.forceSuggestionQuery();
      }

   }

   void onVoiceClicked() {
      if (this.mSearchable != null) {
         SearchableInfo var1 = this.mSearchable;

         try {
            Intent var3;
            if (var1.getVoiceSearchLaunchWebSearch()) {
               var3 = this.createVoiceWebSearchIntent(this.mVoiceWebSearchIntent, var1);
               this.getContext().startActivity(var3);
            } else if (var1.getVoiceSearchLaunchRecognizer()) {
               var3 = this.createVoiceAppSearchIntent(this.mVoiceAppSearchIntent, var1);
               this.getContext().startActivity(var3);
            }

         } catch (ActivityNotFoundException var2) {
            Log.w("SearchView", "Could not find voice search activity");
         }
      }
   }

   public void onWindowFocusChanged(boolean var1) {
      super.onWindowFocusChanged(var1);
      this.postUpdateFocusedState();
   }

   public boolean requestFocus(int var1, Rect var2) {
      if (this.mClearingFocus) {
         return false;
      } else if (!this.isFocusable()) {
         return false;
      } else if (!this.isIconified()) {
         boolean var3 = this.mSearchSrcTextView.requestFocus(var1, var2);
         if (var3) {
            this.updateViewsVisibility(false);
         }

         return var3;
      } else {
         return super.requestFocus(var1, var2);
      }
   }

   public void setAppSearchData(Bundle var1) {
      this.mAppSearchData = var1;
   }

   public void setIconified(boolean var1) {
      if (var1) {
         this.onCloseClicked();
      } else {
         this.onSearchClicked();
      }
   }

   public void setIconifiedByDefault(boolean var1) {
      if (this.mIconifiedByDefault != var1) {
         this.mIconifiedByDefault = var1;
         this.updateViewsVisibility(var1);
         this.updateQueryHint();
      }
   }

   public void setImeOptions(int var1) {
      this.mSearchSrcTextView.setImeOptions(var1);
   }

   public void setInputType(int var1) {
      this.mSearchSrcTextView.setInputType(var1);
   }

   public void setMaxWidth(int var1) {
      this.mMaxWidth = var1;
      this.requestLayout();
   }

   public void setOnCloseListener(SearchView.OnCloseListener var1) {
      this.mOnCloseListener = var1;
   }

   public void setOnQueryTextFocusChangeListener(OnFocusChangeListener var1) {
      this.mOnQueryTextFocusChangeListener = var1;
   }

   public void setOnQueryTextListener(SearchView.OnQueryTextListener var1) {
      this.mOnQueryChangeListener = var1;
   }

   public void setOnSearchClickListener(OnClickListener var1) {
      this.mOnSearchClickListener = var1;
   }

   public void setOnSuggestionListener(SearchView.OnSuggestionListener var1) {
      this.mOnSuggestionListener = var1;
   }

   public void setQuery(CharSequence var1, boolean var2) {
      this.mSearchSrcTextView.setText(var1);
      if (var1 != null) {
         SearchView.SearchAutoComplete var3 = this.mSearchSrcTextView;
         var3.setSelection(var3.length());
         this.mUserQuery = var1;
      }

      if (var2 && !TextUtils.isEmpty(var1)) {
         this.onSubmitQuery();
      }

   }

   public void setQueryHint(CharSequence var1) {
      this.mQueryHint = var1;
      this.updateQueryHint();
   }

   public void setQueryRefinementEnabled(boolean var1) {
      this.mQueryRefinement = var1;
      CursorAdapter var3 = this.mSuggestionsAdapter;
      if (var3 instanceof SuggestionsAdapter) {
         SuggestionsAdapter var4 = (SuggestionsAdapter)var3;
         byte var2;
         if (var1) {
            var2 = 2;
         } else {
            var2 = 1;
         }

         var4.setQueryRefinement(var2);
      }

   }

   public void setSearchableInfo(SearchableInfo var1) {
      this.mSearchable = var1;
      if (var1 != null) {
         this.updateSearchAutoComplete();
         this.updateQueryHint();
      }

      boolean var2 = this.hasVoiceSearch();
      this.mVoiceButtonEnabled = var2;
      if (var2) {
         this.mSearchSrcTextView.setPrivateImeOptions("nm");
      }

      this.updateViewsVisibility(this.isIconified());
   }

   public void setSubmitButtonEnabled(boolean var1) {
      this.mSubmitButtonEnabled = var1;
      this.updateViewsVisibility(this.isIconified());
   }

   public void setSuggestionsAdapter(CursorAdapter var1) {
      this.mSuggestionsAdapter = var1;
      this.mSearchSrcTextView.setAdapter(var1);
   }

   void updateFocusedState() {
      int[] var1;
      if (this.mSearchSrcTextView.hasFocus()) {
         var1 = FOCUSED_STATE_SET;
      } else {
         var1 = EMPTY_STATE_SET;
      }

      Drawable var2 = this.mSearchPlate.getBackground();
      if (var2 != null) {
         var2.setState(var1);
      }

      var2 = this.mSubmitArea.getBackground();
      if (var2 != null) {
         var2.setState(var1);
      }

      this.invalidate();
   }

   private static class AutoCompleteTextViewReflector {
      private Method doAfterTextChanged;
      private Method doBeforeTextChanged;
      private Method ensureImeVisible;

      AutoCompleteTextViewReflector() {
         Method var1;
         try {
            var1 = AutoCompleteTextView.class.getDeclaredMethod("doBeforeTextChanged");
            this.doBeforeTextChanged = var1;
            var1.setAccessible(true);
         } catch (NoSuchMethodException var4) {
         }

         try {
            var1 = AutoCompleteTextView.class.getDeclaredMethod("doAfterTextChanged");
            this.doAfterTextChanged = var1;
            var1.setAccessible(true);
         } catch (NoSuchMethodException var3) {
         }

         try {
            var1 = AutoCompleteTextView.class.getMethod("ensureImeVisible", Boolean.TYPE);
            this.ensureImeVisible = var1;
            var1.setAccessible(true);
         } catch (NoSuchMethodException var2) {
         }
      }

      void doAfterTextChanged(AutoCompleteTextView var1) {
         Method var2 = this.doAfterTextChanged;
         if (var2 != null) {
            try {
               var2.invoke(var1);
               return;
            } catch (Exception var3) {
            }
         }

      }

      void doBeforeTextChanged(AutoCompleteTextView var1) {
         Method var2 = this.doBeforeTextChanged;
         if (var2 != null) {
            try {
               var2.invoke(var1);
               return;
            } catch (Exception var3) {
            }
         }

      }

      void ensureImeVisible(AutoCompleteTextView var1, boolean var2) {
         Method var3 = this.ensureImeVisible;
         if (var3 != null) {
            try {
               var3.invoke(var1, var2);
               return;
            } catch (Exception var4) {
            }
         }

      }
   }

   public interface OnCloseListener {
      boolean onClose();
   }

   public interface OnQueryTextListener {
      boolean onQueryTextChange(String var1);

      boolean onQueryTextSubmit(String var1);
   }

   public interface OnSuggestionListener {
      boolean onSuggestionClick(int var1);

      boolean onSuggestionSelect(int var1);
   }

   static class SavedState extends AbsSavedState {
      public static final Creator CREATOR = new ClassLoaderCreator() {
         public SearchView.SavedState createFromParcel(Parcel var1) {
            return new SearchView.SavedState(var1, (ClassLoader)null);
         }

         public SearchView.SavedState createFromParcel(Parcel var1, ClassLoader var2) {
            return new SearchView.SavedState(var1, var2);
         }

         public SearchView.SavedState[] newArray(int var1) {
            return new SearchView.SavedState[var1];
         }
      };
      boolean isIconified;

      public SavedState(Parcel var1, ClassLoader var2) {
         super(var1, var2);
         this.isIconified = (Boolean)var1.readValue((ClassLoader)null);
      }

      SavedState(Parcelable var1) {
         super(var1);
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("SearchView.SavedState{");
         var1.append(Integer.toHexString(System.identityHashCode(this)));
         var1.append(" isIconified=");
         var1.append(this.isIconified);
         var1.append("}");
         return var1.toString();
      }

      public void writeToParcel(Parcel var1, int var2) {
         super.writeToParcel(var1, var2);
         var1.writeValue(this.isIconified);
      }
   }

   public static class SearchAutoComplete extends AppCompatAutoCompleteTextView {
      private boolean mHasPendingShowSoftInputRequest;
      final Runnable mRunShowSoftInputIfNecessary;
      private SearchView mSearchView;
      private int mThreshold;

      public SearchAutoComplete(Context var1) {
         this(var1, (AttributeSet)null);
      }

      public SearchAutoComplete(Context var1, AttributeSet var2) {
         this(var1, var2, attr.autoCompleteTextViewStyle);
      }

      public SearchAutoComplete(Context var1, AttributeSet var2, int var3) {
         super(var1, var2, var3);
         this.mRunShowSoftInputIfNecessary = new Runnable() {
            public void run() {
               SearchAutoComplete.this.showSoftInputIfNecessary();
            }
         };
         this.mThreshold = this.getThreshold();
      }

      private int getSearchViewTextMinWidthDp() {
         Configuration var3 = this.getResources().getConfiguration();
         int var1 = var3.screenWidthDp;
         int var2 = var3.screenHeightDp;
         if (var1 >= 960 && var2 >= 720 && var3.orientation == 2) {
            return 256;
         } else {
            return var1 >= 600 || var1 >= 640 && var2 >= 480 ? 192 : 160;
         }
      }

      public boolean enoughToFilter() {
         return this.mThreshold <= 0 || super.enoughToFilter();
      }

      boolean isEmpty() {
         return TextUtils.getTrimmedLength(this.getText()) == 0;
      }

      public InputConnection onCreateInputConnection(EditorInfo var1) {
         InputConnection var2 = super.onCreateInputConnection(var1);
         if (this.mHasPendingShowSoftInputRequest) {
            this.removeCallbacks(this.mRunShowSoftInputIfNecessary);
            this.post(this.mRunShowSoftInputIfNecessary);
         }

         return var2;
      }

      protected void onFinishInflate() {
         super.onFinishInflate();
         DisplayMetrics var1 = this.getResources().getDisplayMetrics();
         this.setMinWidth((int)TypedValue.applyDimension(1, (float)this.getSearchViewTextMinWidthDp(), var1));
      }

      protected void onFocusChanged(boolean var1, int var2, Rect var3) {
         super.onFocusChanged(var1, var2, var3);
         this.mSearchView.onTextFocusChanged();
      }

      public boolean onKeyPreIme(int var1, KeyEvent var2) {
         if (var1 == 4) {
            DispatcherState var3;
            if (var2.getAction() == 0 && var2.getRepeatCount() == 0) {
               var3 = this.getKeyDispatcherState();
               if (var3 != null) {
                  var3.startTracking(var2, this);
               }

               return true;
            }

            if (var2.getAction() == 1) {
               var3 = this.getKeyDispatcherState();
               if (var3 != null) {
                  var3.handleUpEvent(var2);
               }

               if (var2.isTracking() && !var2.isCanceled()) {
                  this.mSearchView.clearFocus();
                  this.setImeVisibility(false);
                  return true;
               }
            }
         }

         return super.onKeyPreIme(var1, var2);
      }

      public void onWindowFocusChanged(boolean var1) {
         super.onWindowFocusChanged(var1);
         if (var1 && this.mSearchView.hasFocus() && this.getVisibility() == 0) {
            this.mHasPendingShowSoftInputRequest = true;
            if (SearchView.isLandscapeMode(this.getContext())) {
               SearchView.HIDDEN_METHOD_INVOKER.ensureImeVisible(this, true);
            }
         }

      }

      public void performCompletion() {
      }

      protected void replaceText(CharSequence var1) {
      }

      void setImeVisibility(boolean var1) {
         InputMethodManager var2 = (InputMethodManager)this.getContext().getSystemService("input_method");
         if (!var1) {
            this.mHasPendingShowSoftInputRequest = false;
            this.removeCallbacks(this.mRunShowSoftInputIfNecessary);
            var2.hideSoftInputFromWindow(this.getWindowToken(), 0);
         } else if (var2.isActive(this)) {
            this.mHasPendingShowSoftInputRequest = false;
            this.removeCallbacks(this.mRunShowSoftInputIfNecessary);
            var2.showSoftInput(this, 0);
         } else {
            this.mHasPendingShowSoftInputRequest = true;
         }
      }

      void setSearchView(SearchView var1) {
         this.mSearchView = var1;
      }

      public void setThreshold(int var1) {
         super.setThreshold(var1);
         this.mThreshold = var1;
      }

      void showSoftInputIfNecessary() {
         if (this.mHasPendingShowSoftInputRequest) {
            ((InputMethodManager)this.getContext().getSystemService("input_method")).showSoftInput(this, 0);
            this.mHasPendingShowSoftInputRequest = false;
         }

      }
   }

   private static class UpdatableTouchDelegate extends TouchDelegate {
      private final Rect mActualBounds;
      private boolean mDelegateTargeted;
      private final View mDelegateView;
      private final int mSlop;
      private final Rect mSlopBounds;
      private final Rect mTargetBounds;

      public UpdatableTouchDelegate(Rect var1, Rect var2, View var3) {
         super(var1, var3);
         this.mSlop = ViewConfiguration.get(var3.getContext()).getScaledTouchSlop();
         this.mTargetBounds = new Rect();
         this.mSlopBounds = new Rect();
         this.mActualBounds = new Rect();
         this.setBounds(var1, var2);
         this.mDelegateView = var3;
      }

      public boolean onTouchEvent(MotionEvent var1) {
         int var4 = (int)var1.getX();
         int var5 = (int)var1.getY();
         boolean var6 = false;
         boolean var3 = true;
         boolean var7 = false;
         int var2 = var1.getAction();
         boolean var9;
         if (var2 != 0) {
            if (var2 != 1 && var2 != 2) {
               if (var2 != 3) {
                  var9 = var3;
               } else {
                  var6 = this.mDelegateTargeted;
                  this.mDelegateTargeted = false;
                  var9 = var3;
               }
            } else {
               boolean var8 = this.mDelegateTargeted;
               var6 = var8;
               var9 = var3;
               if (var8) {
                  var6 = var8;
                  var9 = var3;
                  if (!this.mSlopBounds.contains(var4, var5)) {
                     var9 = false;
                     var6 = var8;
                  }
               }
            }
         } else {
            var9 = var3;
            if (this.mTargetBounds.contains(var4, var5)) {
               this.mDelegateTargeted = true;
               var6 = true;
               var9 = var3;
            }
         }

         if (var6) {
            if (var9 && !this.mActualBounds.contains(var4, var5)) {
               var1.setLocation((float)(this.mDelegateView.getWidth() / 2), (float)(this.mDelegateView.getHeight() / 2));
            } else {
               var1.setLocation((float)(var4 - this.mActualBounds.left), (float)(var5 - this.mActualBounds.top));
            }

            var7 = this.mDelegateView.dispatchTouchEvent(var1);
         }

         return var7;
      }

      public void setBounds(Rect var1, Rect var2) {
         this.mTargetBounds.set(var1);
         this.mSlopBounds.set(var1);
         var1 = this.mSlopBounds;
         int var3 = this.mSlop;
         var1.inset(-var3, -var3);
         this.mActualBounds.set(var2);
      }
   }
}
