package com.tokenautocomplete;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.os.Parcelable.Creator;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Layout;
import android.text.NoCopySpan;
import android.text.Selection;
import android.text.SpanWatcher;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.QwertyKeyListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.accessibility.AccessibilityEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.view.inputmethod.InputMethodManager;
import android.widget.Filter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.MultiAutoCompleteTextView.CommaTokenizer;
import android.widget.MultiAutoCompleteTextView.Tokenizer;
import android.widget.TextView.OnEditorActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public abstract class TokenCompleteTextView extends MultiAutoCompleteTextView implements OnEditorActionListener {
   // $FF: synthetic field
   static final boolean $assertionsDisabled = false;
   public static final String TAG = "TokenAutoComplete";
   private boolean allowCollapse;
   private boolean allowDuplicates;
   private TokenCompleteTextView.TokenDeleteStyle deletionStyle;
   private boolean focusChanging;
   private List hiddenSpans;
   private boolean hintVisible;
   boolean inInvalidate;
   private boolean initialized;
   private Layout lastLayout;
   private TokenCompleteTextView.TokenListener listener;
   private ArrayList objects;
   private boolean performBestGuess;
   private CharSequence prefix;
   private boolean savingState;
   private Object selectedObject;
   private boolean shouldFocusNext;
   private TokenCompleteTextView.TokenSpanWatcher spanWatcher;
   private char[] splitChar = new char[]{',', ';'};
   private TokenCompleteTextView.TokenTextWatcher textWatcher;
   private TokenCompleteTextView.TokenClickStyle tokenClickStyle;
   private int tokenLimit;
   private Tokenizer tokenizer;

   public TokenCompleteTextView(Context var1) {
      super(var1);
      this.deletionStyle = TokenCompleteTextView.TokenDeleteStyle._Parent;
      this.tokenClickStyle = TokenCompleteTextView.TokenClickStyle.None;
      this.prefix = "";
      this.hintVisible = false;
      this.lastLayout = null;
      this.allowDuplicates = true;
      this.focusChanging = false;
      this.initialized = false;
      this.performBestGuess = true;
      this.savingState = false;
      this.shouldFocusNext = false;
      this.allowCollapse = true;
      this.tokenLimit = -1;
      this.inInvalidate = false;
      this.init();
   }

   public TokenCompleteTextView(Context var1, AttributeSet var2) {
      super(var1, var2);
      this.deletionStyle = TokenCompleteTextView.TokenDeleteStyle._Parent;
      this.tokenClickStyle = TokenCompleteTextView.TokenClickStyle.None;
      this.prefix = "";
      this.hintVisible = false;
      this.lastLayout = null;
      this.allowDuplicates = true;
      this.focusChanging = false;
      this.initialized = false;
      this.performBestGuess = true;
      this.savingState = false;
      this.shouldFocusNext = false;
      this.allowCollapse = true;
      this.tokenLimit = -1;
      this.inInvalidate = false;
      this.init();
   }

   public TokenCompleteTextView(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.deletionStyle = TokenCompleteTextView.TokenDeleteStyle._Parent;
      this.tokenClickStyle = TokenCompleteTextView.TokenClickStyle.None;
      this.prefix = "";
      this.hintVisible = false;
      this.lastLayout = null;
      this.allowDuplicates = true;
      this.focusChanging = false;
      this.initialized = false;
      this.performBestGuess = true;
      this.savingState = false;
      this.shouldFocusNext = false;
      this.allowCollapse = true;
      this.tokenLimit = -1;
      this.inInvalidate = false;
      this.init();
   }

   private void api16Invalidate() {
      if (this.initialized && !this.inInvalidate) {
         this.inInvalidate = true;
         this.setShadowLayer(this.getShadowRadius(), this.getShadowDx(), this.getShadowDy(), this.getShadowColor());
         this.inInvalidate = false;
      }

   }

   private SpannableStringBuilder buildSpannableForText(CharSequence var1) {
      char var2 = this.splitChar[0];
      StringBuilder var3 = new StringBuilder();
      var3.append(String.valueOf(var2));
      var3.append(this.tokenizer.terminateToken(var1));
      return new SpannableStringBuilder(var3.toString());
   }

   private void clearSelections() {
      TokenCompleteTextView.TokenClickStyle var3 = this.tokenClickStyle;
      if (var3 != null) {
         if (var3.isSelectable()) {
            Editable var4 = this.getText();
            if (var4 != null) {
               TokenCompleteTextView.TokenImageSpan[] var5 = (TokenCompleteTextView.TokenImageSpan[])var4.getSpans(0, var4.length(), TokenCompleteTextView.TokenImageSpan.class);
               int var2 = var5.length;

               for(int var1 = 0; var1 < var2; ++var1) {
                  var5[var1].view.setSelected(false);
               }

               this.invalidate();
            }
         }
      }
   }

   private boolean deleteSelectedObject(boolean var1) {
      TokenCompleteTextView.TokenClickStyle var4 = this.tokenClickStyle;
      if (var4 != null && var4.isSelectable()) {
         Editable var6 = this.getText();
         if (var6 == null) {
            return var1;
         }

         int var3 = var6.length();
         int var2 = 0;
         TokenCompleteTextView.TokenImageSpan[] var7 = (TokenCompleteTextView.TokenImageSpan[])var6.getSpans(0, var3, TokenCompleteTextView.TokenImageSpan.class);

         for(var3 = var7.length; var2 < var3; ++var2) {
            TokenCompleteTextView.TokenImageSpan var5 = var7[var2];
            if (var5.view.isSelected()) {
               this.removeSpan(var5);
               return true;
            }
         }
      }

      return var1;
   }

   private int getCorrectedTokenBeginning(int var1) {
      int var2 = this.tokenizer.findTokenStart(this.getText(), var1);
      var1 = var2;
      if (var2 < this.prefix.length()) {
         var1 = this.prefix.length();
      }

      return var1;
   }

   private int getCorrectedTokenEnd() {
      Editable var2 = this.getText();
      int var1 = this.getSelectionEnd();
      return this.tokenizer.findTokenEnd(var2, var1);
   }

   private void handleDone() {
      this.performCompletion();
      ((InputMethodManager)this.getContext().getSystemService("input_method")).hideSoftInputFromWindow(this.getWindowToken(), 0);
   }

   private void init() {
      if (!this.initialized) {
         this.setTokenizer(new CommaTokenizer());
         this.objects = new ArrayList();
         this.getText();
         this.spanWatcher = new TokenCompleteTextView.TokenSpanWatcher();
         this.textWatcher = new TokenCompleteTextView.TokenTextWatcher();
         this.hiddenSpans = new ArrayList();
         this.addListeners();
         this.setTextIsSelectable(false);
         this.setLongClickable(false);
         this.setInputType(this.getInputType() | 524288 | 65536);
         this.setHorizontallyScrolling(false);
         this.setOnEditorActionListener(this);
         this.setFilters(new InputFilter[]{new InputFilter() {
            public CharSequence filter(CharSequence var1, int var2, int var3, Spanned var4, int var5, int var6) {
               if (TokenCompleteTextView.this.tokenLimit != -1 && TokenCompleteTextView.this.objects.size() == TokenCompleteTextView.this.tokenLimit) {
                  return "";
               } else if (var1.length() == 1 && TokenCompleteTextView.this.isSplitChar(var1.charAt(0))) {
                  TokenCompleteTextView.this.performCompletion();
                  return "";
               } else if (var5 < TokenCompleteTextView.this.prefix.length()) {
                  if (var5 == 0 && var6 == 0) {
                     return null;
                  } else {
                     return var6 <= TokenCompleteTextView.this.prefix.length() ? TokenCompleteTextView.this.prefix.subSequence(var5, var6) : TokenCompleteTextView.this.prefix.subSequence(var5, TokenCompleteTextView.this.prefix.length());
                  }
               } else {
                  return null;
               }
            }
         }});
         this.setDeletionStyle(TokenCompleteTextView.TokenDeleteStyle.Clear);
         this.initialized = true;
      }
   }

   private void insertSpan(TokenCompleteTextView.TokenImageSpan var1) {
      this.insertSpan(var1.getToken());
   }

   private void insertSpan(Object var1) {
      String var2;
      if (this.deletionStyle == TokenCompleteTextView.TokenDeleteStyle.ToString) {
         if (var1 != null) {
            var2 = var1.toString();
         } else {
            var2 = "";
         }
      } else {
         var2 = "";
      }

      this.insertSpan(var1, var2);
   }

   private void insertSpan(Object var1, CharSequence var2) {
      SpannableStringBuilder var8 = this.buildSpannableForText(var2);
      TokenCompleteTextView.TokenImageSpan var5 = this.buildSpanForObject(var1);
      Editable var6 = this.getText();
      if (var6 != null) {
         if (this.allowCollapse && !this.isFocused() && !this.hiddenSpans.isEmpty()) {
            this.hiddenSpans.add(var5);
            this.spanWatcher.onSpanAdded(var6, var5, 0, 0);
            this.updateCountSpan();
         } else {
            int var4 = var6.length();
            int var3;
            if (this.hintVisible) {
               var3 = this.prefix.length();
               var6.insert(var3, var8);
            } else {
               String var7 = this.currentCompletionText();
               var3 = var4;
               if (var7 != null) {
                  var3 = var4;
                  if (var7.length() > 0) {
                     var3 = TextUtils.indexOf(var6, var7);
                  }
               }

               var6.insert(var3, var8);
            }

            var6.setSpan(var5, var3, var8.length() + var3 - 1, 33);
            if (!this.isFocused() && this.allowCollapse) {
               this.performCollapse(false);
            }

            if (!this.objects.contains(var1)) {
               this.spanWatcher.onSpanAdded(var6, var5, 0, 0);
            }

         }
      }
   }

   private boolean isSplitChar(char var1) {
      char[] var4 = this.splitChar;
      int var3 = var4.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         if (var1 == var4[var2]) {
            return true;
         }
      }

      return false;
   }

   private void removeSpan(TokenCompleteTextView.TokenImageSpan var1) {
      Editable var2 = this.getText();
      if (var2 != null) {
         if (((TokenCompleteTextView.TokenSpanWatcher[])var2.getSpans(0, var2.length(), TokenCompleteTextView.TokenSpanWatcher.class)).length == 0) {
            this.spanWatcher.onSpanRemoved(var2, var1, var2.getSpanStart(var1), var2.getSpanEnd(var1));
         }

         var2.delete(var2.getSpanStart(var1), var2.getSpanEnd(var1) + 1);
         if (this.allowCollapse && !this.isFocused()) {
            this.updateCountSpan();
         }

      }
   }

   private void updateCountSpan() {
      Editable var4 = this.getText();
      int var2 = var4.length();
      int var1 = 0;
      CountSpan[] var5 = (CountSpan[])var4.getSpans(0, var2, CountSpan.class);
      var2 = this.hiddenSpans.size();

      for(int var3 = var5.length; var1 < var3; ++var1) {
         CountSpan var6 = var5[var1];
         if (var2 == 0) {
            var4.delete(var4.getSpanStart(var6), var4.getSpanEnd(var6));
            var4.removeSpan(var6);
         } else {
            var6.setCount(this.hiddenSpans.size());
            var4.setSpan(var6, var4.getSpanStart(var6), var4.getSpanEnd(var6), 33);
         }
      }

   }

   private void updateHint() {
      Editable var4 = this.getText();
      CharSequence var5 = this.getHint();
      if (var4 != null) {
         if (var5 != null) {
            if (this.prefix.length() > 0) {
               HintSpan[] var6 = (HintSpan[])var4.getSpans(0, var4.length(), HintSpan.class);
               HintSpan var3 = null;
               int var2 = this.prefix.length();
               int var1 = var2;
               if (var6.length > 0) {
                  var3 = var6[0];
                  var1 = var2 + (var4.getSpanEnd(var3) - var4.getSpanStart(var3));
               }

               if (var4.length() == var1) {
                  this.hintVisible = true;
                  if (var3 != null) {
                     return;
                  }

                  Typeface var7 = this.getTypeface();
                  var1 = 0;
                  if (var7 != null) {
                     var1 = var7.getStyle();
                  }

                  ColorStateList var8 = this.getHintTextColors();
                  var3 = new HintSpan((String)null, var1, (int)this.getTextSize(), var8, var8);
                  var4.insert(this.prefix.length(), var5);
                  var4.setSpan(var3, this.prefix.length(), this.prefix.length() + this.getHint().length(), 33);
                  this.setSelection(this.prefix.length());
                  return;
               }

               if (var3 == null) {
                  return;
               }

               var1 = var4.getSpanStart(var3);
               var2 = var4.getSpanEnd(var3);
               var4.removeSpan(var3);
               var4.replace(var1, var2, "");
               this.hintVisible = false;
            }

         }
      }
   }

   protected void addListeners() {
      Editable var1 = this.getText();
      if (var1 != null) {
         var1.setSpan(this.spanWatcher, 0, var1.length(), 18);
         this.addTextChangedListener(this.textWatcher);
      }

   }

   public void addObject(Object var1) {
      this.addObject(var1, "");
   }

   public void addObject(final Object var1, final CharSequence var2) {
      this.post(new Runnable() {
         public void run() {
            if (var1 != null) {
               if (TokenCompleteTextView.this.allowDuplicates || !TokenCompleteTextView.this.objects.contains(var1)) {
                  if (TokenCompleteTextView.this.tokenLimit == -1 || TokenCompleteTextView.this.objects.size() != TokenCompleteTextView.this.tokenLimit) {
                     TokenCompleteTextView.this.insertSpan(var1, var2);
                     if (TokenCompleteTextView.this.getText() != null && TokenCompleteTextView.this.isFocused()) {
                        TokenCompleteTextView var1x = TokenCompleteTextView.this;
                        var1x.setSelection(var1x.getText().length());
                     }

                  }
               }
            }
         }
      });
   }

   public void allowCollapse(boolean var1) {
      this.allowCollapse = var1;
   }

   public void allowDuplicates(boolean var1) {
      this.allowDuplicates = var1;
   }

   protected TokenCompleteTextView.TokenImageSpan buildSpanForObject(Object var1) {
      return var1 == null ? null : new TokenCompleteTextView.TokenImageSpan(this.getViewForObject(var1), var1, (int)this.maxTextWidth());
   }

   public boolean canDeleteSelection(int var1) {
      if (this.objects.size() < 1) {
         return true;
      } else {
         int var3 = this.getSelectionEnd();
         if (var1 == 1) {
            var1 = this.getSelectionStart();
         } else {
            var1 = var3 - var1;
         }

         Editable var7 = this.getText();
         TokenCompleteTextView.TokenImageSpan[] var8 = (TokenCompleteTextView.TokenImageSpan[])var7.getSpans(0, var7.length(), TokenCompleteTextView.TokenImageSpan.class);
         int var4 = var8.length;

         for(int var2 = 0; var2 < var4; ++var2) {
            TokenCompleteTextView.TokenImageSpan var9 = var8[var2];
            int var5 = var7.getSpanStart(var9);
            int var6 = var7.getSpanEnd(var9);
            if (!this.isTokenRemovable(var9.token)) {
               if (var1 == var3) {
                  if (var6 + 1 == var3) {
                     return false;
                  }
               } else if (var1 <= var5 && var6 + 1 <= var3) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   public void clear() {
      this.post(new Runnable() {
         public void run() {
            Editable var3 = TokenCompleteTextView.this.getText();
            if (var3 != null) {
               int var2 = var3.length();
               int var1 = 0;
               TokenCompleteTextView.TokenImageSpan[] var4 = (TokenCompleteTextView.TokenImageSpan[])var3.getSpans(0, var2, TokenCompleteTextView.TokenImageSpan.class);

               for(var2 = var4.length; var1 < var2; ++var1) {
                  TokenCompleteTextView.TokenImageSpan var5 = var4[var1];
                  TokenCompleteTextView.this.removeSpan(var5);
                  TokenCompleteTextView.this.spanWatcher.onSpanRemoved(var3, var5, var3.getSpanStart(var5), var3.getSpanEnd(var5));
               }

            }
         }
      });
   }

   protected CharSequence convertSelectionToString(Object var1) {
      this.selectedObject = var1;
      int var2 = null.$SwitchMap$com$tokenautocomplete$TokenCompleteTextView$TokenDeleteStyle[this.deletionStyle.ordinal()];
      String var3 = "";
      if (var2 != 1) {
         if (var2 != 2) {
            if (var2 != 3) {
               return super.convertSelectionToString(var1);
            } else {
               if (var1 != null) {
                  var3 = var1.toString();
               }

               return var3;
            }
         } else {
            return this.currentCompletionText();
         }
      } else {
         return "";
      }
   }

   protected ArrayList convertSerializableArrayToObjectArray(ArrayList var1) {
      return var1;
   }

   protected String currentCompletionText() {
      if (this.hintVisible) {
         return "";
      } else {
         Editable var2 = this.getText();
         int var1 = this.getCorrectedTokenEnd();
         return TextUtils.substring(var2, this.getCorrectedTokenBeginning(var1), var1);
      }
   }

   protected abstract Object defaultObject(String var1);

   public boolean enoughToFilter() {
      Tokenizer var3 = this.tokenizer;
      boolean var2 = false;
      if (var3 != null) {
         if (this.hintVisible) {
            return false;
         } else if (this.getSelectionEnd() < 0) {
            return false;
         } else {
            int var1 = this.getCorrectedTokenEnd();
            if (var1 - this.getCorrectedTokenBeginning(var1) >= Math.max(this.getThreshold(), 1)) {
               var2 = true;
            }

            return var2;
         }
      } else {
         return false;
      }
   }

   public boolean extractText(ExtractedTextRequest var1, ExtractedText var2) {
      try {
         boolean var3 = super.extractText(var1, var2);
         return var3;
      } catch (IndexOutOfBoundsException var4) {
         Log.d("TokenAutoComplete", "extractText hit IndexOutOfBoundsException. This may be normal.", var4);
         return false;
      }
   }

   public List getObjects() {
      return this.objects;
   }

   protected ArrayList getSerializableObjects() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.getObjects().iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         if (var3 instanceof Serializable) {
            var1.add((Serializable)var3);
         } else {
            StringBuilder var4 = new StringBuilder();
            var4.append("Unable to save '");
            var4.append(var3);
            var4.append("'");
            Log.e("TokenAutoComplete", var4.toString());
         }
      }

      if (var1.size() != this.objects.size()) {
         Log.e("TokenAutoComplete", "You should make your objects Serializable or override\ngetSerializableObjects and convertSerializableArrayToObjectArray");
      }

      return var1;
   }

   public CharSequence getTextForAccessibility() {
      if (this.getObjects().size() == 0) {
         return this.getText();
      } else {
         SpannableStringBuilder var4 = new SpannableStringBuilder();
         Editable var5 = this.getText();
         int var3 = -1;
         int var2 = -1;

         int var1;
         for(var1 = 0; var1 < var5.length(); ++var1) {
            if (var1 == Selection.getSelectionStart(var5)) {
               var3 = var4.length();
            }

            if (var1 == Selection.getSelectionEnd(var5)) {
               var2 = var4.length();
            }

            TokenCompleteTextView.TokenImageSpan[] var6 = (TokenCompleteTextView.TokenImageSpan[])var5.getSpans(var1, var1, TokenCompleteTextView.TokenImageSpan.class);
            if (var6.length > 0) {
               TokenCompleteTextView.TokenImageSpan var7 = var6[0];
               var4 = var4.append(this.tokenizer.terminateToken(var7.getToken().toString()));
               var1 = var5.getSpanEnd(var7);
            } else {
               var4 = var4.append(var5.subSequence(var1, var1 + 1));
            }
         }

         if (var1 == Selection.getSelectionStart(var5)) {
            var3 = var4.length();
         }

         if (var1 == Selection.getSelectionEnd(var5)) {
            var2 = var4.length();
         }

         if (var3 >= 0 && var2 >= 0) {
            Selection.setSelection(var4, var3, var2);
         }

         return var4;
      }
   }

   protected abstract View getViewForObject(Object var1);

   public void invalidate() {
      if (VERSION.SDK_INT >= 16) {
         this.api16Invalidate();
      }

      super.invalidate();
   }

   public boolean isTokenRemovable(Object var1) {
      return true;
   }

   protected float maxTextWidth() {
      return (float)(this.getWidth() - this.getPaddingLeft() - this.getPaddingRight());
   }

   public InputConnection onCreateInputConnection(EditorInfo var1) {
      InputConnection var2 = super.onCreateInputConnection(var1);
      if (var2 != null) {
         TokenCompleteTextView.TokenInputConnection var3 = new TokenCompleteTextView.TokenInputConnection(var2, true);
         var1.imeOptions &= -1073741825;
         var1.imeOptions |= 268435456;
         return var3;
      } else {
         return null;
      }
   }

   public boolean onEditorAction(TextView var1, int var2, KeyEvent var3) {
      if (var2 == 6) {
         this.handleDone();
         return true;
      } else {
         return false;
      }
   }

   public void onFocusChanged(boolean var1, int var2, Rect var3) {
      super.onFocusChanged(var1, var2, var3);
      if (!var1) {
         this.performCompletion();
      }

      this.clearSelections();
      if (this.allowCollapse) {
         this.performCollapse(var1);
      }

   }

   public void onInitializeAccessibilityEvent(AccessibilityEvent var1) {
      super.onInitializeAccessibilityEvent(var1);
      if (var1.getEventType() == 8192) {
         CharSequence var2 = this.getTextForAccessibility();
         var1.setFromIndex(Selection.getSelectionStart(var2));
         var1.setToIndex(Selection.getSelectionEnd(var2));
         var1.setItemCount(var2.length());
      }

   }

   public boolean onKeyDown(int var1, KeyEvent var2) {
      boolean var3 = false;
      boolean var4 = false;
      if (var1 != 23 && var1 != 61 && var1 != 66) {
         if (var1 == 67) {
            if (this.canDeleteSelection(1) && !this.deleteSelectedObject(false)) {
               var3 = false;
            } else {
               var3 = true;
            }
         }
      } else if (var2.hasNoModifiers()) {
         this.shouldFocusNext = true;
         var3 = true;
      }

      if (var3 || super.onKeyDown(var1, var2)) {
         var4 = true;
      }

      return var4;
   }

   public boolean onKeyUp(int var1, KeyEvent var2) {
      boolean var3 = super.onKeyUp(var1, var2);
      if (this.shouldFocusNext) {
         this.shouldFocusNext = false;
         this.handleDone();
      }

      return var3;
   }

   protected void onLayout(boolean var1, int var2, int var3, int var4, int var5) {
      super.onLayout(var1, var2, var3, var4, var5);
      this.lastLayout = this.getLayout();
   }

   public void onRestoreInstanceState(Parcelable var1) {
      if (!(var1 instanceof TokenCompleteTextView.SavedState)) {
         super.onRestoreInstanceState(var1);
      } else {
         TokenCompleteTextView.SavedState var2 = (TokenCompleteTextView.SavedState)var1;
         super.onRestoreInstanceState(var2.getSuperState());
         this.setText(var2.prefix);
         this.prefix = var2.prefix;
         this.updateHint();
         this.allowCollapse = var2.allowCollapse;
         this.allowDuplicates = var2.allowDuplicates;
         this.performBestGuess = var2.performBestGuess;
         this.tokenClickStyle = var2.tokenClickStyle;
         this.deletionStyle = var2.tokenDeleteStyle;
         this.splitChar = var2.splitChar;
         this.addListeners();
         Iterator var3 = this.convertSerializableArrayToObjectArray(var2.baseObjects).iterator();

         while(var3.hasNext()) {
            this.addObject(var3.next());
         }

         if (!this.isFocused() && this.allowCollapse) {
            this.post(new Runnable() {
               public void run() {
                  TokenCompleteTextView var1 = TokenCompleteTextView.this;
                  var1.performCollapse(var1.isFocused());
               }
            });
         }

      }
   }

   public Parcelable onSaveInstanceState() {
      ArrayList var1 = this.getSerializableObjects();
      this.removeListeners();
      this.savingState = true;
      Parcelable var2 = super.onSaveInstanceState();
      this.savingState = false;
      TokenCompleteTextView.SavedState var3 = new TokenCompleteTextView.SavedState(var2);
      var3.prefix = this.prefix;
      var3.allowCollapse = this.allowCollapse;
      var3.allowDuplicates = this.allowDuplicates;
      var3.performBestGuess = this.performBestGuess;
      var3.tokenClickStyle = this.tokenClickStyle;
      var3.tokenDeleteStyle = this.deletionStyle;
      var3.baseObjects = var1;
      var3.splitChar = this.splitChar;
      this.addListeners();
      return var3;
   }

   protected void onSelectionChanged(int var1, int var2) {
      if (this.hintVisible) {
         var1 = 0;
      }

      TokenCompleteTextView.TokenClickStyle var5 = this.tokenClickStyle;
      if (var5 != null && var5.isSelectable() && this.getText() != null) {
         this.clearSelections();
      }

      CharSequence var8 = this.prefix;
      if (var8 != null && (var1 < var8.length() || var1 < this.prefix.length())) {
         this.setSelection(this.prefix.length());
      } else {
         Editable var9 = this.getText();
         if (var9 != null) {
            TokenCompleteTextView.TokenImageSpan[] var6 = (TokenCompleteTextView.TokenImageSpan[])var9.getSpans(var1, var1, TokenCompleteTextView.TokenImageSpan.class);
            int var3 = var6.length;

            for(var2 = 0; var2 < var3; ++var2) {
               TokenCompleteTextView.TokenImageSpan var7 = var6[var2];
               int var4 = var9.getSpanEnd(var7);
               if (var1 <= var4 && var9.getSpanStart(var7) < var1) {
                  if (var4 == var9.length()) {
                     this.setSelection(var4);
                     return;
                  }

                  this.setSelection(var4 + 1);
                  return;
               }
            }
         }

         super.onSelectionChanged(var1, var1);
      }
   }

   public boolean onTouchEvent(MotionEvent var1) {
      int var2 = var1.getActionMasked();
      Editable var5 = this.getText();
      boolean var4 = false;
      if (this.tokenClickStyle == TokenCompleteTextView.TokenClickStyle.None) {
         var4 = super.onTouchEvent(var1);
      }

      boolean var3 = var4;
      if (this.isFocused()) {
         var3 = var4;
         if (var5 != null) {
            var3 = var4;
            if (this.lastLayout != null) {
               var3 = var4;
               if (var2 == 1) {
                  var2 = this.getOffsetForPosition(var1.getX(), var1.getY());
                  var3 = var4;
                  if (var2 != -1) {
                     TokenCompleteTextView.TokenImageSpan[] var6 = (TokenCompleteTextView.TokenImageSpan[])var5.getSpans(var2, var2, TokenCompleteTextView.TokenImageSpan.class);
                     if (var6.length > 0) {
                        var6[0].onClick();
                        var3 = true;
                     } else {
                        this.clearSelections();
                        var3 = var4;
                     }
                  }
               }
            }
         }
      }

      var4 = var3;
      if (!var3) {
         var4 = var3;
         if (this.tokenClickStyle != TokenCompleteTextView.TokenClickStyle.None) {
            var4 = super.onTouchEvent(var1);
         }
      }

      return var4;
   }

   public void performBestGuess(boolean var1) {
      this.performBestGuess = var1;
   }

   public void performCollapse(boolean var1) {
      this.focusChanging = true;
      int var2;
      int var3;
      final Editable var5;
      CountSpan var13;
      if (!var1) {
         var5 = this.getText();
         if (var5 != null) {
            Layout var6 = this.lastLayout;
            if (var6 != null) {
               var2 = var6.getLineVisibleEnd(0);
               TokenCompleteTextView.TokenImageSpan[] var10 = (TokenCompleteTextView.TokenImageSpan[])var5.getSpans(0, var2, TokenCompleteTextView.TokenImageSpan.class);
               int var4 = this.objects.size() - var10.length;
               CountSpan[] var7 = (CountSpan[])var5.getSpans(0, var2, CountSpan.class);
               if (var4 > 0 && var7.length == 0) {
                  var3 = var2 + 1;
                  var13 = new CountSpan(var4, this.getContext(), this.getCurrentTextColor(), (int)this.getTextSize(), (int)this.maxTextWidth());
                  var5.insert(var3, var13.text);
                  var2 = var3;
                  if (Layout.getDesiredWidth(var5, 0, var13.text.length() + var3, this.lastLayout.getPaint()) > this.maxTextWidth()) {
                     var5.delete(var3, var13.text.length() + var3);
                     if (var10.length > 0) {
                        var2 = var5.getSpanStart(var10[var10.length - 1]);
                        var13.setCount(var4 + 1);
                     } else {
                        var2 = this.prefix.length();
                     }

                     var5.insert(var2, var13.text);
                  }

                  var5.setSpan(var13, var2, var13.text.length() + var2, 33);
                  ArrayList var8 = new ArrayList(Arrays.asList((TokenCompleteTextView.TokenImageSpan[])((TokenCompleteTextView.TokenImageSpan[])var5.getSpans(var13.text.length() + var2, var5.length(), TokenCompleteTextView.TokenImageSpan.class))));
                  this.hiddenSpans = var8;
                  Iterator var9 = var8.iterator();

                  while(var9.hasNext()) {
                     this.removeSpan((TokenCompleteTextView.TokenImageSpan)var9.next());
                  }
               }
            }
         }
      } else {
         var5 = this.getText();
         if (var5 != null) {
            CountSpan[] var11 = (CountSpan[])var5.getSpans(0, var5.length(), CountSpan.class);
            var3 = var11.length;

            for(var2 = 0; var2 < var3; ++var2) {
               var13 = var11[var2];
               var5.delete(var5.getSpanStart(var13), var5.getSpanEnd(var13));
               var5.removeSpan(var13);
            }

            Iterator var12 = this.hiddenSpans.iterator();

            while(var12.hasNext()) {
               this.insertSpan((TokenCompleteTextView.TokenImageSpan)var12.next());
            }

            this.hiddenSpans.clear();
            if (this.hintVisible) {
               this.setSelection(this.prefix.length());
            } else {
               this.postDelayed(new Runnable() {
                  public void run() {
                     TokenCompleteTextView.this.setSelection(var5.length());
                  }
               }, 10L);
            }

            if (((TokenCompleteTextView.TokenSpanWatcher[])this.getText().getSpans(0, this.getText().length(), TokenCompleteTextView.TokenSpanWatcher.class)).length == 0) {
               var5.setSpan(this.spanWatcher, 0, var5.length(), 18);
            }
         }
      }

      this.focusChanging = false;
   }

   public void performCompletion() {
      if ((this.getAdapter() == null || this.getListSelection() == -1) && this.enoughToFilter()) {
         Object var1;
         if (this.getAdapter() != null && this.getAdapter().getCount() > 0 && this.performBestGuess) {
            var1 = this.getAdapter().getItem(0);
         } else {
            var1 = this.defaultObject(this.currentCompletionText());
         }

         this.replaceText(this.convertSelectionToString(var1));
      } else {
         super.performCompletion();
      }
   }

   protected void performFiltering(CharSequence var1, int var2, int var3, int var4) {
      var4 = var2;
      if (var2 < this.prefix.length()) {
         var4 = this.prefix.length();
      }

      Filter var5 = this.getFilter();
      if (var5 != null) {
         if (this.hintVisible) {
            var5.filter("");
            return;
         }

         var5.filter(var1.subSequence(var4, var3), this);
      }

   }

   protected void removeListeners() {
      Editable var3 = this.getText();
      if (var3 != null) {
         int var2 = var3.length();
         int var1 = 0;
         TokenCompleteTextView.TokenSpanWatcher[] var4 = (TokenCompleteTextView.TokenSpanWatcher[])var3.getSpans(0, var2, TokenCompleteTextView.TokenSpanWatcher.class);

         for(var2 = var4.length; var1 < var2; ++var1) {
            var3.removeSpan(var4[var1]);
         }

         this.removeTextChangedListener(this.textWatcher);
      }

   }

   public void removeObject(final Object var1) {
      this.post(new Runnable() {
         public void run() {
            Editable var4 = TokenCompleteTextView.this.getText();
            if (var4 != null) {
               ArrayList var5 = new ArrayList();
               Iterator var6 = TokenCompleteTextView.this.hiddenSpans.iterator();

               while(var6.hasNext()) {
                  TokenCompleteTextView.TokenImageSpan var7 = (TokenCompleteTextView.TokenImageSpan)var6.next();
                  if (var7.getToken().equals(var1)) {
                     var5.add(var7);
                  }
               }

               Iterator var9 = var5.iterator();

               while(true) {
                  boolean var3 = var9.hasNext();
                  int var1x = 0;
                  if (!var3) {
                     TokenCompleteTextView.this.updateCountSpan();
                     TokenCompleteTextView.TokenImageSpan[] var8 = (TokenCompleteTextView.TokenImageSpan[])var4.getSpans(0, var4.length(), TokenCompleteTextView.TokenImageSpan.class);

                     for(int var2 = var8.length; var1x < var2; ++var1x) {
                        TokenCompleteTextView.TokenImageSpan var10 = var8[var1x];
                        if (var10.getToken().equals(var1)) {
                           TokenCompleteTextView.this.removeSpan(var10);
                        }
                     }

                     return;
                  }

                  TokenCompleteTextView.TokenImageSpan var11 = (TokenCompleteTextView.TokenImageSpan)var9.next();
                  TokenCompleteTextView.this.hiddenSpans.remove(var11);
                  TokenCompleteTextView.this.spanWatcher.onSpanRemoved(var4, var11, 0, 0);
               }
            }
         }
      });
   }

   protected void replaceText(CharSequence var1) {
      this.clearComposingText();
      Object var4 = this.selectedObject;
      if (var4 != null) {
         if (!var4.toString().equals("")) {
            SpannableStringBuilder var7 = this.buildSpannableForText(var1);
            TokenCompleteTextView.TokenImageSpan var8 = this.buildSpanForObject(this.selectedObject);
            Editable var5 = this.getText();
            int var3 = this.getSelectionEnd();
            int var2 = var3;
            if (!this.hintVisible) {
               var2 = this.getCorrectedTokenEnd();
               var3 = this.getCorrectedTokenBeginning(var2);
            }

            String var6 = TextUtils.substring(var5, var3, var2);
            if (var5 != null) {
               if (var8 == null) {
                  var5.replace(var3, var2, "");
                  return;
               }

               if (!this.allowDuplicates && this.objects.contains(var8.getToken())) {
                  var5.replace(var3, var2, "");
                  return;
               }

               QwertyKeyListener.markAsReplaced(var5, var3, var2, var6);
               var5.replace(var3, var2, var7);
               var5.setSpan(var8, var3, var7.length() + var3 - 1, 33);
            }

         }
      }
   }

   public void setDeletionStyle(TokenCompleteTextView.TokenDeleteStyle var1) {
      this.deletionStyle = var1;
   }

   public void setPrefix(CharSequence var1) {
      this.prefix = "";
      Editable var2 = this.getText();
      if (var2 != null) {
         var2.insert(0, var1);
      }

      this.prefix = var1;
      this.updateHint();
   }

   public void setSplitChar(char var1) {
      this.setSplitChar(new char[]{var1});
   }

   public void setSplitChar(char[] var1) {
      char[] var2 = var1;
      if (var1[0] == ' ') {
         var2 = new char[var1.length + 1];
         var2[0] = 167;
         System.arraycopy(var1, 0, var2, 1, var1.length);
      }

      this.splitChar = var2;
      this.setTokenizer(new CharacterTokenizer(var1));
   }

   public void setTokenClickStyle(TokenCompleteTextView.TokenClickStyle var1) {
      this.tokenClickStyle = var1;
   }

   public void setTokenLimit(int var1) {
      this.tokenLimit = var1;
   }

   public void setTokenListener(TokenCompleteTextView.TokenListener var1) {
      this.listener = var1;
   }

   public void setTokenizer(Tokenizer var1) {
      super.setTokenizer(var1);
      this.tokenizer = var1;
   }

   private static class SavedState extends BaseSavedState {
      public static final Creator CREATOR = new Creator() {
         public TokenCompleteTextView.SavedState createFromParcel(Parcel var1) {
            return new TokenCompleteTextView.SavedState(var1);
         }

         public TokenCompleteTextView.SavedState[] newArray(int var1) {
            return new TokenCompleteTextView.SavedState[var1];
         }
      };
      boolean allowCollapse;
      boolean allowDuplicates;
      ArrayList baseObjects;
      boolean performBestGuess;
      CharSequence prefix;
      char[] splitChar;
      TokenCompleteTextView.TokenClickStyle tokenClickStyle;
      TokenCompleteTextView.TokenDeleteStyle tokenDeleteStyle;

      SavedState(Parcel var1) {
         super(var1);
         this.prefix = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(var1);
         int var2 = var1.readInt();
         boolean var4 = true;
         boolean var3;
         if (var2 != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.allowCollapse = var3;
         if (var1.readInt() != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.allowDuplicates = var3;
         if (var1.readInt() != 0) {
            var3 = var4;
         } else {
            var3 = false;
         }

         this.performBestGuess = var3;
         this.tokenClickStyle = TokenCompleteTextView.TokenClickStyle.values()[var1.readInt()];
         this.tokenDeleteStyle = TokenCompleteTextView.TokenDeleteStyle.values()[var1.readInt()];
         this.baseObjects = (ArrayList)var1.readSerializable();
         this.splitChar = var1.createCharArray();
      }

      SavedState(Parcelable var1) {
         super(var1);
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("TokenCompleteTextView.SavedState{");
         var1.append(Integer.toHexString(System.identityHashCode(this)));
         var1.append(" tokens=");
         var1.append(this.baseObjects);
         String var3 = var1.toString();
         StringBuilder var2 = new StringBuilder();
         var2.append(var3);
         var2.append("}");
         return var2.toString();
      }

      public void writeToParcel(Parcel var1, int var2) {
         throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:496)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
      }
   }

   public static enum TokenClickStyle {
      Delete(false),
      None(false),
      Select(true),
      SelectDeselect;

      private boolean mIsSelectable = false;

      static {
         TokenCompleteTextView.TokenClickStyle var0 = new TokenCompleteTextView.TokenClickStyle("SelectDeselect", 3, true);
         SelectDeselect = var0;
      }

      private TokenClickStyle(boolean var3) {
         this.mIsSelectable = var3;
      }

      public boolean isSelectable() {
         return this.mIsSelectable;
      }
   }

   public static enum TokenDeleteStyle {
      Clear,
      PartialCompletion,
      ToString,
      _Parent;

      static {
         TokenCompleteTextView.TokenDeleteStyle var0 = new TokenCompleteTextView.TokenDeleteStyle("ToString", 3);
         ToString = var0;
      }
   }

   protected class TokenImageSpan extends ViewSpan implements NoCopySpan {
      private Object token;

      public TokenImageSpan(View var2, Object var3, int var4) {
         super(var2, var4);
         this.token = var3;
      }

      public Object getToken() {
         return this.token;
      }

      public void onClick() {
         Editable var2 = TokenCompleteTextView.this.getText();
         if (var2 != null) {
            int var1 = null.$SwitchMap$com$tokenautocomplete$TokenCompleteTextView$TokenClickStyle[TokenCompleteTextView.this.tokenClickStyle.ordinal()];
            if (var1 != 1 && var1 != 2) {
               if (var1 != 3) {
                  if (TokenCompleteTextView.this.getSelectionStart() != var2.getSpanEnd(this) + 1) {
                     TokenCompleteTextView.this.setSelection(var2.getSpanEnd(this) + 1);
                     return;
                  }

                  return;
               }
            } else {
               if (!this.view.isSelected()) {
                  TokenCompleteTextView.this.clearSelections();
                  this.view.setSelected(true);
                  return;
               }

               if (TokenCompleteTextView.this.tokenClickStyle == TokenCompleteTextView.TokenClickStyle.SelectDeselect || !TokenCompleteTextView.this.isTokenRemovable(this.token)) {
                  this.view.setSelected(false);
                  TokenCompleteTextView.this.invalidate();
                  return;
               }
            }

            if (TokenCompleteTextView.this.isTokenRemovable(this.token)) {
               TokenCompleteTextView.this.removeSpan(this);
            }
         }
      }
   }

   private class TokenInputConnection extends InputConnectionWrapper {
      public TokenInputConnection(InputConnection var2, boolean var3) {
         super(var2, var3);
      }

      public boolean deleteSurroundingText(int var1, int var2) {
         boolean var4 = TokenCompleteTextView.this.canDeleteSelection(var1);
         boolean var3 = false;
         if (!var4) {
            return false;
         } else if (TokenCompleteTextView.this.getSelectionStart() > TokenCompleteTextView.this.prefix.length()) {
            return super.deleteSurroundingText(var1, var2);
         } else {
            if (TokenCompleteTextView.this.deleteSelectedObject(false) || super.deleteSurroundingText(0, var2)) {
               var3 = true;
            }

            return var3;
         }
      }
   }

   public interface TokenListener {
      void onTokenAdded(Object var1);

      void onTokenRemoved(Object var1);
   }

   private class TokenSpanWatcher implements SpanWatcher {
      private TokenSpanWatcher() {
      }

      // $FF: synthetic method
      TokenSpanWatcher(Object var2) {
         this();
      }

      public void onSpanAdded(Spannable var1, Object var2, int var3, int var4) {
         if (var2 instanceof TokenCompleteTextView.TokenImageSpan && !TokenCompleteTextView.this.savingState && !TokenCompleteTextView.this.focusChanging) {
            TokenCompleteTextView.TokenImageSpan var5 = (TokenCompleteTextView.TokenImageSpan)var2;
            TokenCompleteTextView.this.objects.add(var5.getToken());
            if (TokenCompleteTextView.this.listener != null) {
               TokenCompleteTextView.this.listener.onTokenAdded(var5.getToken());
            }
         }

      }

      public void onSpanChanged(Spannable var1, Object var2, int var3, int var4, int var5, int var6) {
      }

      public void onSpanRemoved(Spannable var1, Object var2, int var3, int var4) {
         if (var2 instanceof TokenCompleteTextView.TokenImageSpan && !TokenCompleteTextView.this.savingState && !TokenCompleteTextView.this.focusChanging) {
            TokenCompleteTextView.TokenImageSpan var5 = (TokenCompleteTextView.TokenImageSpan)var2;
            if (TokenCompleteTextView.this.objects.contains(var5.getToken())) {
               TokenCompleteTextView.this.objects.remove(var5.getToken());
            }

            if (TokenCompleteTextView.this.listener != null) {
               TokenCompleteTextView.this.listener.onTokenRemoved(var5.getToken());
            }
         }

      }
   }

   private class TokenTextWatcher implements TextWatcher {
      ArrayList spansToRemove;

      private TokenTextWatcher() {
         this.spansToRemove = new ArrayList();
      }

      // $FF: synthetic method
      TokenTextWatcher(Object var2) {
         this();
      }

      public void afterTextChanged(Editable var1) {
         ArrayList var4 = new ArrayList(this.spansToRemove);
         this.spansToRemove.clear();
         Iterator var6 = var4.iterator();

         while(var6.hasNext()) {
            TokenCompleteTextView.TokenImageSpan var5 = (TokenCompleteTextView.TokenImageSpan)var6.next();
            int var2 = var1.getSpanStart(var5);
            int var3 = var1.getSpanEnd(var5);
            this.removeToken(var5, var1);
            --var3;
            if (var3 >= 0 && TokenCompleteTextView.this.isSplitChar(var1.charAt(var3))) {
               var1.delete(var3, var3 + 1);
            }

            if (var2 >= 0 && TokenCompleteTextView.this.isSplitChar(var1.charAt(var2))) {
               var1.delete(var2, var2 + 1);
            }
         }

         TokenCompleteTextView.this.clearSelections();
         TokenCompleteTextView.this.updateHint();
      }

      public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4) {
         if (var3 > 0 && TokenCompleteTextView.this.getText() != null) {
            Editable var9 = TokenCompleteTextView.this.getText();
            var4 = var2 + var3;
            var3 = var2;
            if (var9.charAt(var2) == ' ') {
               var3 = var2 - 1;
            }

            TokenCompleteTextView.TokenImageSpan[] var6 = (TokenCompleteTextView.TokenImageSpan[])var9.getSpans(var3, var4, TokenCompleteTextView.TokenImageSpan.class);
            ArrayList var7 = new ArrayList();
            int var5 = var6.length;

            for(var2 = 0; var2 < var5; ++var2) {
               TokenCompleteTextView.TokenImageSpan var8 = var6[var2];
               if (var9.getSpanStart(var8) < var4 && var3 < var9.getSpanEnd(var8)) {
                  var7.add(var8);
               }
            }

            this.spansToRemove = var7;
         }

      }

      public void onTextChanged(CharSequence var1, int var2, int var3, int var4) {
      }

      protected void removeToken(TokenCompleteTextView.TokenImageSpan var1, Editable var2) {
         var2.removeSpan(var1);
      }
   }
}
