/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.TimeInterpolator
 *  android.animation.ValueAnimator
 *  android.animation.ValueAnimator$AnimatorUpdateListener
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.graphics.Canvas
 *  android.graphics.ColorFilter
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.Rect
 *  android.graphics.RectF
 *  android.graphics.Typeface
 *  android.graphics.drawable.ColorDrawable
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$ClassLoaderCreator
 *  android.os.Parcelable$Creator
 *  android.text.Editable
 *  android.text.TextUtils
 *  android.text.TextWatcher
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.util.SparseArray
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.View$OnLongClickListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.ViewStructure
 *  android.widget.EditText
 *  android.widget.FrameLayout
 *  android.widget.FrameLayout$LayoutParams
 *  android.widget.LinearLayout
 *  android.widget.LinearLayout$LayoutParams
 *  android.widget.TextView
 *  com.google.android.material.R
 *  com.google.android.material.R$attr
 *  com.google.android.material.R$color
 *  com.google.android.material.R$id
 *  com.google.android.material.R$string
 *  com.google.android.material.R$style
 */
package com.google.android.material.textfield;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStructure;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.DrawableUtils;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.widget.TextViewCompat;
import androidx.customview.view.AbsSavedState;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.internal.CheckableImageButton;
import com.google.android.material.internal.CollapsingTextHelper;
import com.google.android.material.internal.DescendantOffsetUtils;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.textfield.CutoutDrawable;
import com.google.android.material.textfield.EndIconDelegate;
import com.google.android.material.textfield.IndicatorViewController;
import com.google.android.material.textfield.TextInputEditText;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class TextInputLayout
extends LinearLayout {
    public static final int BOX_BACKGROUND_FILLED = 1;
    public static final int BOX_BACKGROUND_NONE = 0;
    public static final int BOX_BACKGROUND_OUTLINE = 2;
    private static final int DEF_STYLE_RES = R.style.Widget_Design_TextInputLayout;
    public static final int END_ICON_CLEAR_TEXT = 2;
    public static final int END_ICON_CUSTOM = -1;
    public static final int END_ICON_DROPDOWN_MENU = 3;
    public static final int END_ICON_NONE = 0;
    public static final int END_ICON_PASSWORD_TOGGLE = 1;
    private static final int INVALID_MAX_LENGTH = -1;
    private static final int LABEL_SCALE_ANIMATION_DURATION = 167;
    private static final String LOG_TAG = "TextInputLayout";
    private ValueAnimator animator;
    private MaterialShapeDrawable boxBackground;
    private int boxBackgroundColor;
    private int boxBackgroundMode;
    private final int boxCollapsedPaddingTopPx;
    private final int boxLabelCutoutPaddingPx;
    private int boxStrokeColor;
    private final int boxStrokeWidthDefaultPx;
    private final int boxStrokeWidthFocusedPx;
    private int boxStrokeWidthPx;
    private MaterialShapeDrawable boxUnderline;
    final CollapsingTextHelper collapsingTextHelper;
    boolean counterEnabled;
    private int counterMaxLength;
    private int counterOverflowTextAppearance;
    private ColorStateList counterOverflowTextColor;
    private boolean counterOverflowed;
    private int counterTextAppearance;
    private ColorStateList counterTextColor;
    private TextView counterView;
    private int defaultFilledBackgroundColor;
    private ColorStateList defaultHintTextColor;
    private final int defaultStrokeColor;
    private final int disabledColor;
    private final int disabledFilledBackgroundColor;
    EditText editText;
    private final LinkedHashSet<OnEditTextAttachedListener> editTextAttachedListeners;
    private final LinkedHashSet<OnEndIconChangedListener> endIconChangedListeners;
    private final SparseArray<EndIconDelegate> endIconDelegates;
    private Drawable endIconDummyDrawable;
    private final FrameLayout endIconFrame;
    private int endIconMode;
    private View.OnLongClickListener endIconOnLongClickListener;
    private ColorStateList endIconTintList;
    private PorterDuff.Mode endIconTintMode;
    private final CheckableImageButton endIconView;
    private final CheckableImageButton errorIconView;
    private int focusedStrokeColor;
    private ColorStateList focusedTextColor;
    private boolean hasEndIconTintList;
    private boolean hasEndIconTintMode;
    private boolean hasStartIconTintList;
    private boolean hasStartIconTintMode;
    private CharSequence hint;
    private boolean hintAnimationEnabled;
    private boolean hintEnabled;
    private boolean hintExpanded;
    private final int hoveredFilledBackgroundColor;
    private final int hoveredStrokeColor;
    private boolean inDrawableStateChanged;
    private final IndicatorViewController indicatorViewController;
    private final FrameLayout inputFrame;
    private boolean isProvidingHint;
    private Drawable originalEditTextEndDrawable;
    private CharSequence originalHint;
    private boolean restoringSavedState;
    private ShapeAppearanceModel shapeAppearanceModel;
    private Drawable startIconDummyDrawable;
    private View.OnLongClickListener startIconOnLongClickListener;
    private ColorStateList startIconTintList;
    private PorterDuff.Mode startIconTintMode;
    private final CheckableImageButton startIconView;
    private final Rect tmpBoundsRect;
    private final Rect tmpRect;
    private final RectF tmpRectF;
    private Typeface typeface;

    public TextInputLayout(Context context) {
        this(context, null);
    }

    public TextInputLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.textInputStyle);
    }

    public TextInputLayout(Context context, AttributeSet attributeSet, int n) {
        RuntimeException runtimeException;
        super("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:659)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
        throw runtimeException;
    }

    private void applyBoxAttributes() {
        int n;
        MaterialShapeDrawable materialShapeDrawable = this.boxBackground;
        if (materialShapeDrawable == null) {
            return;
        }
        materialShapeDrawable.setShapeAppearanceModel(this.shapeAppearanceModel);
        if (this.canDrawOutlineStroke()) {
            this.boxBackground.setStroke((float)this.boxStrokeWidthPx, this.boxStrokeColor);
        }
        this.boxBackgroundColor = n = this.calculateBoxBackgroundColor();
        this.boxBackground.setFillColor(ColorStateList.valueOf((int)n));
        if (this.endIconMode == 3) {
            this.editText.getBackground().invalidateSelf();
        }
        this.applyBoxUnderlineAttributes();
        this.invalidate();
    }

    private void applyBoxUnderlineAttributes() {
        if (this.boxUnderline == null) {
            return;
        }
        if (this.canDrawStroke()) {
            this.boxUnderline.setFillColor(ColorStateList.valueOf((int)this.boxStrokeColor));
        }
        this.invalidate();
    }

    private void applyCutoutPadding(RectF rectF) {
        rectF.left -= (float)this.boxLabelCutoutPaddingPx;
        rectF.top -= (float)this.boxLabelCutoutPaddingPx;
        rectF.right += (float)this.boxLabelCutoutPaddingPx;
        rectF.bottom += (float)this.boxLabelCutoutPaddingPx;
    }

    private void applyEndIconTint() {
        this.applyIconTint(this.endIconView, this.hasEndIconTintList, this.endIconTintList, this.hasEndIconTintMode, this.endIconTintMode);
    }

    private void applyIconTint(CheckableImageButton checkableImageButton, boolean bl, ColorStateList colorStateList, boolean bl2, PorterDuff.Mode mode) {
        Drawable drawable2;
        block6 : {
            Drawable drawable3;
            block7 : {
                drawable2 = drawable3 = checkableImageButton.getDrawable();
                if (drawable3 == null) break block6;
                if (bl) break block7;
                drawable2 = drawable3;
                if (!bl2) break block6;
            }
            drawable3 = DrawableCompat.wrap(drawable3).mutate();
            if (bl) {
                DrawableCompat.setTintList(drawable3, colorStateList);
            }
            drawable2 = drawable3;
            if (bl2) {
                DrawableCompat.setTintMode(drawable3, mode);
                drawable2 = drawable3;
            }
        }
        if (checkableImageButton.getDrawable() != drawable2) {
            checkableImageButton.setImageDrawable(drawable2);
        }
    }

    private void applyStartIconTint() {
        this.applyIconTint(this.startIconView, this.hasStartIconTintList, this.startIconTintList, this.hasStartIconTintMode, this.startIconTintMode);
    }

    private void assignBoxBackgroundByMode() {
        int n = this.boxBackgroundMode;
        if (n != 0) {
            if (n != 1) {
                if (n == 2) {
                    this.boxBackground = this.hintEnabled && !(this.boxBackground instanceof CutoutDrawable) ? new CutoutDrawable(this.shapeAppearanceModel) : new MaterialShapeDrawable(this.shapeAppearanceModel);
                    this.boxUnderline = null;
                    return;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.boxBackgroundMode);
                stringBuilder.append(" is illegal; only @BoxBackgroundMode constants are supported.");
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            this.boxBackground = new MaterialShapeDrawable(this.shapeAppearanceModel);
            this.boxUnderline = new MaterialShapeDrawable();
            return;
        }
        this.boxBackground = null;
        this.boxUnderline = null;
    }

    private int calculateBoxBackgroundColor() {
        int n = this.boxBackgroundColor;
        if (this.boxBackgroundMode == 1) {
            n = MaterialColors.layer(MaterialColors.getColor((View)this, R.attr.colorSurface, 0), this.boxBackgroundColor);
        }
        return n;
    }

    private Rect calculateCollapsedTextBounds(Rect rect) {
        if (this.editText != null) {
            Rect rect2 = this.tmpBoundsRect;
            rect2.bottom = rect.bottom;
            int n = this.boxBackgroundMode;
            if (n != 1) {
                if (n != 2) {
                    rect2.left = rect.left + this.editText.getCompoundPaddingLeft();
                    rect2.top = this.getPaddingTop();
                    rect2.right = rect.right - this.editText.getCompoundPaddingRight();
                    return rect2;
                }
                rect2.left = rect.left + this.editText.getPaddingLeft();
                rect2.top = rect.top - this.calculateLabelMarginTop();
                rect2.right = rect.right - this.editText.getPaddingRight();
                return rect2;
            }
            rect2.left = rect.left + this.editText.getCompoundPaddingLeft();
            rect2.top = rect.top + this.boxCollapsedPaddingTopPx;
            rect2.right = rect.right - this.editText.getCompoundPaddingRight();
            return rect2;
        }
        throw new IllegalStateException();
    }

    private int calculateExpandedLabelBottom(Rect rect, Rect rect2, float f) {
        if (this.boxBackgroundMode == 1) {
            return (int)((float)rect2.top + f);
        }
        return rect.bottom - this.editText.getCompoundPaddingBottom();
    }

    private int calculateExpandedLabelTop(Rect rect, float f) {
        if (this.isSingleLineFilledTextField()) {
            return (int)((float)rect.centerY() - f / 2.0f);
        }
        return rect.top + this.editText.getCompoundPaddingTop();
    }

    private Rect calculateExpandedTextBounds(Rect rect) {
        if (this.editText != null) {
            Rect rect2 = this.tmpBoundsRect;
            float f = this.collapsingTextHelper.getExpandedTextHeight();
            rect2.left = rect.left + this.editText.getCompoundPaddingLeft();
            rect2.top = this.calculateExpandedLabelTop(rect, f);
            rect2.right = rect.right - this.editText.getCompoundPaddingRight();
            rect2.bottom = this.calculateExpandedLabelBottom(rect, rect2, f);
            return rect2;
        }
        throw new IllegalStateException();
    }

    private int calculateLabelMarginTop() {
        if (!this.hintEnabled) {
            return 0;
        }
        int n = this.boxBackgroundMode;
        if (n != 0 && n != 1) {
            if (n != 2) {
                return 0;
            }
            return (int)(this.collapsingTextHelper.getCollapsedTextHeight() / 2.0f);
        }
        return (int)this.collapsingTextHelper.getCollapsedTextHeight();
    }

    private boolean canDrawOutlineStroke() {
        if (this.boxBackgroundMode == 2 && this.canDrawStroke()) {
            return true;
        }
        return false;
    }

    private boolean canDrawStroke() {
        if (this.boxStrokeWidthPx > -1 && this.boxStrokeColor != 0) {
            return true;
        }
        return false;
    }

    private void closeCutout() {
        if (this.cutoutEnabled()) {
            ((CutoutDrawable)this.boxBackground).removeCutout();
        }
    }

    private void collapseHint(boolean bl) {
        ValueAnimator valueAnimator = this.animator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.animator.cancel();
        }
        if (bl && this.hintAnimationEnabled) {
            this.animateToExpansionFraction(1.0f);
        } else {
            this.collapsingTextHelper.setExpansionFraction(1.0f);
        }
        this.hintExpanded = false;
        if (this.cutoutEnabled()) {
            this.openCutout();
        }
    }

    private boolean cutoutEnabled() {
        if (this.hintEnabled && !TextUtils.isEmpty((CharSequence)this.hint) && this.boxBackground instanceof CutoutDrawable) {
            return true;
        }
        return false;
    }

    private void dispatchOnEditTextAttached() {
        Iterator<OnEditTextAttachedListener> iterator = this.editTextAttachedListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onEditTextAttached(this);
        }
    }

    private void dispatchOnEndIconChanged(int n) {
        Iterator<OnEndIconChangedListener> iterator = this.endIconChangedListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onEndIconChanged(this, n);
        }
    }

    private void drawBoxUnderline(Canvas canvas) {
        MaterialShapeDrawable materialShapeDrawable = this.boxUnderline;
        if (materialShapeDrawable != null) {
            materialShapeDrawable = materialShapeDrawable.getBounds();
            materialShapeDrawable.top = materialShapeDrawable.bottom - this.boxStrokeWidthPx;
            this.boxUnderline.draw(canvas);
        }
    }

    private void drawHint(Canvas canvas) {
        if (this.hintEnabled) {
            this.collapsingTextHelper.draw(canvas);
        }
    }

    private void expandHint(boolean bl) {
        ValueAnimator valueAnimator = this.animator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.animator.cancel();
        }
        if (bl && this.hintAnimationEnabled) {
            this.animateToExpansionFraction(0.0f);
        } else {
            this.collapsingTextHelper.setExpansionFraction(0.0f);
        }
        if (this.cutoutEnabled() && ((CutoutDrawable)this.boxBackground).hasCutout()) {
            this.closeCutout();
        }
        this.hintExpanded = true;
    }

    private EndIconDelegate getEndIconDelegate() {
        EndIconDelegate endIconDelegate = (EndIconDelegate)this.endIconDelegates.get(this.endIconMode);
        if (endIconDelegate != null) {
            return endIconDelegate;
        }
        return (EndIconDelegate)this.endIconDelegates.get(0);
    }

    private CheckableImageButton getEndIconToUpdateDummyDrawable() {
        if (this.errorIconView.getVisibility() == 0) {
            return this.errorIconView;
        }
        if (this.hasEndIcon() && this.isEndIconVisible()) {
            return this.endIconView;
        }
        return null;
    }

    private boolean hasEndIcon() {
        if (this.endIconMode != 0) {
            return true;
        }
        return false;
    }

    private boolean hasStartIcon() {
        if (this.getStartIconDrawable() != null) {
            return true;
        }
        return false;
    }

    private boolean isSingleLineFilledTextField() {
        if (this.boxBackgroundMode == 1 && (Build.VERSION.SDK_INT < 16 || this.editText.getMinLines() <= 1)) {
            return true;
        }
        return false;
    }

    private void onApplyBoxBackgroundMode() {
        this.assignBoxBackgroundByMode();
        this.setEditTextBoxBackground();
        this.updateTextInputBoxState();
        if (this.boxBackgroundMode != 0) {
            this.updateInputLayoutMargins();
        }
    }

    private void openCutout() {
        if (!this.cutoutEnabled()) {
            return;
        }
        RectF rectF = this.tmpRectF;
        this.collapsingTextHelper.getCollapsedTextActualBounds(rectF);
        this.applyCutoutPadding(rectF);
        rectF.offset((float)(- this.getPaddingLeft()), 0.0f);
        ((CutoutDrawable)this.boxBackground).setCutout(rectF);
    }

    private static void recursiveSetEnabled(ViewGroup viewGroup, boolean bl) {
        int n = viewGroup.getChildCount();
        for (int i = 0; i < n; ++i) {
            View view = viewGroup.getChildAt(i);
            view.setEnabled(bl);
            if (!(view instanceof ViewGroup)) continue;
            TextInputLayout.recursiveSetEnabled((ViewGroup)view, bl);
        }
    }

    private void setEditText(EditText object) {
        if (this.editText == null) {
            if (this.endIconMode != 3 && !(object instanceof TextInputEditText)) {
                Log.i((String)"TextInputLayout", (String)"EditText added is not a TextInputEditText. Please switch to using that class instead.");
            }
            this.editText = object;
            this.onApplyBoxBackgroundMode();
            this.setTextInputAccessibilityDelegate(new AccessibilityDelegate(this));
            this.collapsingTextHelper.setTypefaces(this.editText.getTypeface());
            this.collapsingTextHelper.setExpandedTextSize(this.editText.getTextSize());
            int n = this.editText.getGravity();
            this.collapsingTextHelper.setCollapsedTextGravity(n & -113 | 48);
            this.collapsingTextHelper.setExpandedTextGravity(n);
            this.editText.addTextChangedListener(new TextWatcher(){

                public void afterTextChanged(Editable editable) {
                    TextInputLayout textInputLayout = TextInputLayout.this;
                    textInputLayout.updateLabelState(textInputLayout.restoringSavedState ^ true);
                    if (TextInputLayout.this.counterEnabled) {
                        TextInputLayout.this.updateCounter(editable.length());
                    }
                }

                public void beforeTextChanged(CharSequence charSequence, int n, int n2, int n3) {
                }

                public void onTextChanged(CharSequence charSequence, int n, int n2, int n3) {
                }
            });
            if (this.defaultHintTextColor == null) {
                this.defaultHintTextColor = this.editText.getHintTextColors();
            }
            if (this.hintEnabled) {
                if (TextUtils.isEmpty((CharSequence)this.hint)) {
                    this.originalHint = object = this.editText.getHint();
                    this.setHint((CharSequence)object);
                    this.editText.setHint(null);
                }
                this.isProvidingHint = true;
            }
            if (this.counterView != null) {
                this.updateCounter(this.editText.getText().length());
            }
            this.updateEditTextBackground();
            this.indicatorViewController.adjustIndicatorPadding();
            this.startIconView.bringToFront();
            this.endIconFrame.bringToFront();
            this.errorIconView.bringToFront();
            this.dispatchOnEditTextAttached();
            this.updateLabelState(false, true);
            return;
        }
        throw new IllegalArgumentException("We already have an EditText, can only have one");
    }

    private void setEditTextBoxBackground() {
        if (this.shouldUseEditTextBackgroundForBoxBackground()) {
            ViewCompat.setBackground((View)this.editText, this.boxBackground);
        }
    }

    private void setErrorIconVisible(boolean bl) {
        CheckableImageButton checkableImageButton = this.errorIconView;
        int n = 0;
        int n2 = bl ? 0 : 8;
        checkableImageButton.setVisibility(n2);
        checkableImageButton = this.endIconFrame;
        n2 = n;
        if (bl) {
            n2 = 8;
        }
        checkableImageButton.setVisibility(n2);
        if (!this.hasEndIcon()) {
            this.updateIconDummyDrawables();
        }
    }

    private void setHintInternal(CharSequence charSequence) {
        if (!TextUtils.equals((CharSequence)charSequence, (CharSequence)this.hint)) {
            this.hint = charSequence;
            this.collapsingTextHelper.setText(charSequence);
            if (!this.hintExpanded) {
                this.openCutout();
            }
        }
    }

    private static void setIconClickable(CheckableImageButton checkableImageButton, View.OnLongClickListener onLongClickListener) {
        boolean bl = ViewCompat.hasOnClickListeners((View)checkableImageButton);
        boolean bl2 = false;
        int n = 1;
        boolean bl3 = onLongClickListener != null;
        if (bl || bl3) {
            bl2 = true;
        }
        checkableImageButton.setFocusable(bl2);
        checkableImageButton.setClickable(bl);
        checkableImageButton.setPressable(bl);
        checkableImageButton.setLongClickable(bl3);
        if (!bl2) {
            n = 2;
        }
        ViewCompat.setImportantForAccessibility((View)checkableImageButton, n);
    }

    private static void setIconOnClickListener(CheckableImageButton checkableImageButton, View.OnClickListener onClickListener, View.OnLongClickListener onLongClickListener) {
        checkableImageButton.setOnClickListener(onClickListener);
        TextInputLayout.setIconClickable(checkableImageButton, onLongClickListener);
    }

    private static void setIconOnLongClickListener(CheckableImageButton checkableImageButton, View.OnLongClickListener onLongClickListener) {
        checkableImageButton.setOnLongClickListener(onLongClickListener);
        TextInputLayout.setIconClickable(checkableImageButton, onLongClickListener);
    }

    private boolean shouldUseEditTextBackgroundForBoxBackground() {
        EditText editText = this.editText;
        if (editText != null && this.boxBackground != null && editText.getBackground() == null && this.boxBackgroundMode != 0) {
            return true;
        }
        return false;
    }

    private void tintEndIconOnError(boolean bl) {
        if (bl && this.getEndIconDrawable() != null) {
            Drawable drawable2 = DrawableCompat.wrap(this.getEndIconDrawable()).mutate();
            DrawableCompat.setTint(drawable2, this.indicatorViewController.getErrorViewCurrentTextColor());
            this.endIconView.setImageDrawable(drawable2);
            return;
        }
        this.applyEndIconTint();
    }

    private void updateBoxUnderlineBounds(Rect rect) {
        if (this.boxUnderline != null) {
            int n = rect.bottom;
            int n2 = this.boxStrokeWidthFocusedPx;
            this.boxUnderline.setBounds(rect.left, n - n2, rect.right, rect.bottom);
        }
    }

    private void updateCounter() {
        if (this.counterView != null) {
            EditText editText = this.editText;
            int n = editText == null ? 0 : editText.getText().length();
            this.updateCounter(n);
        }
    }

    private static void updateCounterContentDescription(Context context, TextView textView, int n, int n2, boolean bl) {
        int n3 = bl ? R.string.character_counter_overflowed_content_description : R.string.character_counter_content_description;
        textView.setContentDescription((CharSequence)context.getString(n3, new Object[]{n, n2}));
    }

    private void updateCounterTextAppearanceAndColor() {
        TextView textView = this.counterView;
        if (textView != null) {
            int n = this.counterOverflowed ? this.counterOverflowTextAppearance : this.counterTextAppearance;
            this.setTextAppearanceCompatWithErrorFallback(textView, n);
            if (!this.counterOverflowed && (textView = this.counterTextColor) != null) {
                this.counterView.setTextColor((ColorStateList)textView);
            }
            if (this.counterOverflowed && (textView = this.counterOverflowTextColor) != null) {
                this.counterView.setTextColor((ColorStateList)textView);
            }
        }
    }

    private boolean updateEditTextHeightBasedOnIcon() {
        if (this.editText == null) {
            return false;
        }
        int n = Math.max(this.endIconView.getMeasuredHeight(), this.startIconView.getMeasuredHeight());
        if (this.editText.getMeasuredHeight() < n) {
            this.editText.setMinimumHeight(n);
            return true;
        }
        return false;
    }

    private boolean updateIconDummyDrawables() {
        boolean bl;
        Drawable[] arrdrawable;
        int n;
        Drawable drawable2;
        int n2;
        Drawable drawable3;
        int n3;
        if (this.editText == null) {
            return false;
        }
        boolean bl2 = false;
        if (this.hasStartIcon() && this.isStartIconVisible() && this.startIconView.getMeasuredWidth() > 0) {
            if (this.startIconDummyDrawable == null) {
                this.startIconDummyDrawable = new ColorDrawable();
                n2 = this.startIconView.getMeasuredWidth();
                n = this.editText.getPaddingLeft();
                n3 = MarginLayoutParamsCompat.getMarginEnd((ViewGroup.MarginLayoutParams)this.startIconView.getLayoutParams());
                this.startIconDummyDrawable.setBounds(0, 0, n2 - n + n3, 1);
            }
            if ((drawable3 = (arrdrawable = TextViewCompat.getCompoundDrawablesRelative((TextView)this.editText))[0]) != (drawable2 = this.startIconDummyDrawable)) {
                TextViewCompat.setCompoundDrawablesRelative((TextView)this.editText, drawable2, arrdrawable[1], arrdrawable[2], arrdrawable[3]);
                bl2 = true;
            }
        } else if (this.startIconDummyDrawable != null) {
            arrdrawable = TextViewCompat.getCompoundDrawablesRelative((TextView)this.editText);
            TextViewCompat.setCompoundDrawablesRelative((TextView)this.editText, null, arrdrawable[1], arrdrawable[2], arrdrawable[3]);
            this.startIconDummyDrawable = null;
            bl2 = true;
        }
        if ((arrdrawable = this.getEndIconToUpdateDummyDrawable()) != null && arrdrawable.getMeasuredWidth() > 0) {
            if (this.endIconDummyDrawable == null) {
                this.endIconDummyDrawable = new ColorDrawable();
                n2 = arrdrawable.getMeasuredWidth();
                n = this.editText.getPaddingRight();
                n3 = MarginLayoutParamsCompat.getMarginStart((ViewGroup.MarginLayoutParams)arrdrawable.getLayoutParams());
                this.endIconDummyDrawable.setBounds(0, 0, n2 - n + n3, 1);
            }
            arrdrawable = TextViewCompat.getCompoundDrawablesRelative((TextView)this.editText);
            drawable3 = arrdrawable[2];
            drawable2 = this.endIconDummyDrawable;
            bl = bl2;
            if (drawable3 != drawable2) {
                this.originalEditTextEndDrawable = arrdrawable[2];
                TextViewCompat.setCompoundDrawablesRelative((TextView)this.editText, arrdrawable[0], arrdrawable[1], drawable2, arrdrawable[3]);
                bl = true;
            }
        } else {
            bl = bl2;
            if (this.endIconDummyDrawable != null) {
                arrdrawable = TextViewCompat.getCompoundDrawablesRelative((TextView)this.editText);
                if (arrdrawable[2] == this.endIconDummyDrawable) {
                    TextViewCompat.setCompoundDrawablesRelative((TextView)this.editText, arrdrawable[0], arrdrawable[1], this.originalEditTextEndDrawable, arrdrawable[3]);
                    bl2 = true;
                }
                this.endIconDummyDrawable = null;
                return bl2;
            }
        }
        return bl;
    }

    private void updateInputLayoutMargins() {
        if (this.boxBackgroundMode != 1) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)this.inputFrame.getLayoutParams();
            int n = this.calculateLabelMarginTop();
            if (n != layoutParams.topMargin) {
                layoutParams.topMargin = n;
                this.inputFrame.requestLayout();
            }
        }
    }

    private void updateLabelState(boolean bl, boolean bl2) {
        boolean bl3 = this.isEnabled();
        EditText editText = this.editText;
        boolean bl4 = true;
        boolean bl5 = editText != null && !TextUtils.isEmpty((CharSequence)editText.getText());
        editText = this.editText;
        if (editText == null || !editText.hasFocus()) {
            bl4 = false;
        }
        boolean bl6 = this.indicatorViewController.errorShouldBeShown();
        editText = this.defaultHintTextColor;
        if (editText != null) {
            this.collapsingTextHelper.setCollapsedTextColor((ColorStateList)editText);
            this.collapsingTextHelper.setExpandedTextColor(this.defaultHintTextColor);
        }
        if (!bl3) {
            this.collapsingTextHelper.setCollapsedTextColor(ColorStateList.valueOf((int)this.disabledColor));
            this.collapsingTextHelper.setExpandedTextColor(ColorStateList.valueOf((int)this.disabledColor));
        } else if (bl6) {
            this.collapsingTextHelper.setCollapsedTextColor(this.indicatorViewController.getErrorViewTextColors());
        } else if (this.counterOverflowed && (editText = this.counterView) != null) {
            this.collapsingTextHelper.setCollapsedTextColor(editText.getTextColors());
        } else if (bl4 && (editText = this.focusedTextColor) != null) {
            this.collapsingTextHelper.setCollapsedTextColor((ColorStateList)editText);
        }
        if (!(bl5 || this.isEnabled() && (bl4 || bl6))) {
            if (bl2 || !this.hintExpanded) {
                this.expandHint(bl);
                return;
            }
        } else if (bl2 || this.hintExpanded) {
            this.collapseHint(bl);
        }
    }

    public void addOnEditTextAttachedListener(OnEditTextAttachedListener onEditTextAttachedListener) {
        this.editTextAttachedListeners.add(onEditTextAttachedListener);
        if (this.editText != null) {
            onEditTextAttachedListener.onEditTextAttached(this);
        }
    }

    public void addOnEndIconChangedListener(OnEndIconChangedListener onEndIconChangedListener) {
        this.endIconChangedListeners.add(onEndIconChangedListener);
    }

    public void addView(View view, int n, ViewGroup.LayoutParams layoutParams) {
        if (view instanceof EditText) {
            FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(layoutParams);
            layoutParams2.gravity = layoutParams2.gravity & -113 | 16;
            this.inputFrame.addView(view, (ViewGroup.LayoutParams)layoutParams2);
            this.inputFrame.setLayoutParams(layoutParams);
            this.updateInputLayoutMargins();
            this.setEditText((EditText)view);
            return;
        }
        super.addView(view, n, layoutParams);
    }

    void animateToExpansionFraction(float f) {
        if (this.collapsingTextHelper.getExpansionFraction() == f) {
            return;
        }
        if (this.animator == null) {
            ValueAnimator valueAnimator;
            this.animator = valueAnimator = new ValueAnimator();
            valueAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            this.animator.setDuration(167L);
            this.animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){

                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    TextInputLayout.this.collapsingTextHelper.setExpansionFraction(((Float)valueAnimator.getAnimatedValue()).floatValue());
                }
            });
        }
        this.animator.setFloatValues(new float[]{this.collapsingTextHelper.getExpansionFraction(), f});
        this.animator.start();
    }

    public void clearOnEditTextAttachedListeners() {
        this.editTextAttachedListeners.clear();
    }

    public void clearOnEndIconChangedListeners() {
        this.endIconChangedListeners.clear();
    }

    boolean cutoutIsOpen() {
        if (this.cutoutEnabled() && ((CutoutDrawable)this.boxBackground).hasCutout()) {
            return true;
        }
        return false;
    }

    public void dispatchProvideAutofillStructure(ViewStructure viewStructure, int n) {
        Object object;
        if (this.originalHint != null && (object = this.editText) != null) {
            boolean bl = this.isProvidingHint;
            this.isProvidingHint = false;
            object = object.getHint();
            this.editText.setHint(this.originalHint);
            try {
                super.dispatchProvideAutofillStructure(viewStructure, n);
                return;
            }
            finally {
                this.editText.setHint((CharSequence)object);
                this.isProvidingHint = bl;
            }
        }
        super.dispatchProvideAutofillStructure(viewStructure, n);
    }

    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> sparseArray) {
        this.restoringSavedState = true;
        super.dispatchRestoreInstanceState(sparseArray);
        this.restoringSavedState = false;
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        this.drawHint(canvas);
        this.drawBoxUnderline(canvas);
    }

    protected void drawableStateChanged() {
        if (this.inDrawableStateChanged) {
            return;
        }
        boolean bl = true;
        this.inDrawableStateChanged = true;
        super.drawableStateChanged();
        int[] arrn = this.getDrawableState();
        boolean bl2 = false;
        CollapsingTextHelper collapsingTextHelper = this.collapsingTextHelper;
        if (collapsingTextHelper != null) {
            bl2 = false | collapsingTextHelper.setState(arrn);
        }
        if (!ViewCompat.isLaidOut((View)this) || !this.isEnabled()) {
            bl = false;
        }
        this.updateLabelState(bl);
        this.updateEditTextBackground();
        this.updateTextInputBoxState();
        if (bl2) {
            this.invalidate();
        }
        this.inDrawableStateChanged = false;
    }

    public int getBaseline() {
        EditText editText = this.editText;
        if (editText != null) {
            return editText.getBaseline() + this.getPaddingTop() + this.calculateLabelMarginTop();
        }
        return super.getBaseline();
    }

    MaterialShapeDrawable getBoxBackground() {
        int n = this.boxBackgroundMode;
        if (n != 1 && n != 2) {
            throw new IllegalStateException();
        }
        return this.boxBackground;
    }

    public int getBoxBackgroundColor() {
        return this.boxBackgroundColor;
    }

    public int getBoxBackgroundMode() {
        return this.boxBackgroundMode;
    }

    public float getBoxCornerRadiusBottomEnd() {
        return this.boxBackground.getBottomLeftCornerResolvedSize();
    }

    public float getBoxCornerRadiusBottomStart() {
        return this.boxBackground.getBottomRightCornerResolvedSize();
    }

    public float getBoxCornerRadiusTopEnd() {
        return this.boxBackground.getTopRightCornerResolvedSize();
    }

    public float getBoxCornerRadiusTopStart() {
        return this.boxBackground.getTopLeftCornerResolvedSize();
    }

    public int getBoxStrokeColor() {
        return this.focusedStrokeColor;
    }

    public int getCounterMaxLength() {
        return this.counterMaxLength;
    }

    CharSequence getCounterOverflowDescription() {
        TextView textView;
        if (this.counterEnabled && this.counterOverflowed && (textView = this.counterView) != null) {
            return textView.getContentDescription();
        }
        return null;
    }

    public ColorStateList getCounterOverflowTextColor() {
        return this.counterTextColor;
    }

    public ColorStateList getCounterTextColor() {
        return this.counterTextColor;
    }

    public ColorStateList getDefaultHintTextColor() {
        return this.defaultHintTextColor;
    }

    public EditText getEditText() {
        return this.editText;
    }

    public CharSequence getEndIconContentDescription() {
        return this.endIconView.getContentDescription();
    }

    public Drawable getEndIconDrawable() {
        return this.endIconView.getDrawable();
    }

    public int getEndIconMode() {
        return this.endIconMode;
    }

    CheckableImageButton getEndIconView() {
        return this.endIconView;
    }

    public CharSequence getError() {
        if (this.indicatorViewController.isErrorEnabled()) {
            return this.indicatorViewController.getErrorText();
        }
        return null;
    }

    public int getErrorCurrentTextColors() {
        return this.indicatorViewController.getErrorViewCurrentTextColor();
    }

    public Drawable getErrorIconDrawable() {
        return this.errorIconView.getDrawable();
    }

    final int getErrorTextCurrentColor() {
        return this.indicatorViewController.getErrorViewCurrentTextColor();
    }

    public CharSequence getHelperText() {
        if (this.indicatorViewController.isHelperTextEnabled()) {
            return this.indicatorViewController.getHelperText();
        }
        return null;
    }

    public int getHelperTextCurrentTextColor() {
        return this.indicatorViewController.getHelperTextViewCurrentTextColor();
    }

    public CharSequence getHint() {
        if (this.hintEnabled) {
            return this.hint;
        }
        return null;
    }

    final float getHintCollapsedTextHeight() {
        return this.collapsingTextHelper.getCollapsedTextHeight();
    }

    final int getHintCurrentCollapsedTextColor() {
        return this.collapsingTextHelper.getCurrentCollapsedTextColor();
    }

    public ColorStateList getHintTextColor() {
        return this.focusedTextColor;
    }

    @Deprecated
    public CharSequence getPasswordVisibilityToggleContentDescription() {
        return this.endIconView.getContentDescription();
    }

    @Deprecated
    public Drawable getPasswordVisibilityToggleDrawable() {
        return this.endIconView.getDrawable();
    }

    public CharSequence getStartIconContentDescription() {
        return this.startIconView.getContentDescription();
    }

    public Drawable getStartIconDrawable() {
        return this.startIconView.getDrawable();
    }

    public Typeface getTypeface() {
        return this.typeface;
    }

    public boolean isCounterEnabled() {
        return this.counterEnabled;
    }

    public boolean isEndIconCheckable() {
        return this.endIconView.isCheckable();
    }

    public boolean isEndIconVisible() {
        if (this.endIconFrame.getVisibility() == 0 && this.endIconView.getVisibility() == 0) {
            return true;
        }
        return false;
    }

    public boolean isErrorEnabled() {
        return this.indicatorViewController.isErrorEnabled();
    }

    final boolean isHelperTextDisplayed() {
        return this.indicatorViewController.helperTextIsDisplayed();
    }

    public boolean isHelperTextEnabled() {
        return this.indicatorViewController.isHelperTextEnabled();
    }

    public boolean isHintAnimationEnabled() {
        return this.hintAnimationEnabled;
    }

    public boolean isHintEnabled() {
        return this.hintEnabled;
    }

    final boolean isHintExpanded() {
        return this.hintExpanded;
    }

    @Deprecated
    public boolean isPasswordVisibilityToggleEnabled() {
        if (this.endIconMode == 1) {
            return true;
        }
        return false;
    }

    boolean isProvidingHint() {
        return this.isProvidingHint;
    }

    public boolean isStartIconCheckable() {
        return this.startIconView.isCheckable();
    }

    public boolean isStartIconVisible() {
        if (this.startIconView.getVisibility() == 0) {
            return true;
        }
        return false;
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        super.onLayout(bl, n, n2, n3, n4);
        EditText editText = this.editText;
        if (editText != null) {
            Rect rect = this.tmpRect;
            DescendantOffsetUtils.getDescendantRect((ViewGroup)this, (View)editText, rect);
            this.updateBoxUnderlineBounds(rect);
            if (this.hintEnabled) {
                this.collapsingTextHelper.setCollapsedBounds(this.calculateCollapsedTextBounds(rect));
                this.collapsingTextHelper.setExpandedBounds(this.calculateExpandedTextBounds(rect));
                this.collapsingTextHelper.recalculate();
                if (this.cutoutEnabled() && !this.hintExpanded) {
                    this.openCutout();
                }
            }
        }
    }

    protected void onMeasure(int n, int n2) {
        super.onMeasure(n, n2);
        boolean bl = this.updateEditTextHeightBasedOnIcon();
        boolean bl2 = this.updateIconDummyDrawables();
        if (bl || bl2) {
            this.editText.post(new Runnable(){

                @Override
                public void run() {
                    TextInputLayout.this.editText.requestLayout();
                }
            });
        }
    }

    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        parcelable = (SavedState)parcelable;
        super.onRestoreInstanceState(parcelable.getSuperState());
        this.setError(parcelable.error);
        if (parcelable.isEndIconChecked) {
            this.endIconView.post(new Runnable(){

                @Override
                public void run() {
                    TextInputLayout.this.endIconView.performClick();
                    TextInputLayout.this.endIconView.jumpDrawablesToCurrentState();
                }
            });
        }
        this.requestLayout();
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        if (this.indicatorViewController.errorShouldBeShown()) {
            savedState.error = this.getError();
        }
        boolean bl = this.hasEndIcon() && this.endIconView.isChecked();
        savedState.isEndIconChecked = bl;
        return savedState;
    }

    @Deprecated
    public void passwordVisibilityToggleRequested(boolean bl) {
        if (this.endIconMode == 1) {
            this.endIconView.performClick();
            if (bl) {
                this.endIconView.jumpDrawablesToCurrentState();
            }
        }
    }

    public void removeOnEditTextAttachedListener(OnEditTextAttachedListener onEditTextAttachedListener) {
        this.editTextAttachedListeners.remove(onEditTextAttachedListener);
    }

    public void removeOnEndIconChangedListener(OnEndIconChangedListener onEndIconChangedListener) {
        this.endIconChangedListeners.remove(onEndIconChangedListener);
    }

    public void setBoxBackgroundColor(int n) {
        if (this.boxBackgroundColor != n) {
            this.boxBackgroundColor = n;
            this.defaultFilledBackgroundColor = n;
            this.applyBoxAttributes();
        }
    }

    public void setBoxBackgroundColorResource(int n) {
        this.setBoxBackgroundColor(ContextCompat.getColor(this.getContext(), n));
    }

    public void setBoxBackgroundMode(int n) {
        if (n == this.boxBackgroundMode) {
            return;
        }
        this.boxBackgroundMode = n;
        if (this.editText != null) {
            this.onApplyBoxBackgroundMode();
        }
    }

    public void setBoxCornerRadii(float f, float f2, float f3, float f4) {
        if (this.boxBackground.getTopLeftCornerResolvedSize() != f || this.boxBackground.getTopRightCornerResolvedSize() != f2 || this.boxBackground.getBottomRightCornerResolvedSize() != f4 || this.boxBackground.getBottomLeftCornerResolvedSize() != f3) {
            this.shapeAppearanceModel = this.shapeAppearanceModel.toBuilder().setTopLeftCornerSize(f).setTopRightCornerSize(f2).setBottomRightCornerSize(f4).setBottomLeftCornerSize(f3).build();
            this.applyBoxAttributes();
        }
    }

    public void setBoxCornerRadiiResources(int n, int n2, int n3, int n4) {
        this.setBoxCornerRadii(this.getContext().getResources().getDimension(n), this.getContext().getResources().getDimension(n2), this.getContext().getResources().getDimension(n4), this.getContext().getResources().getDimension(n3));
    }

    public void setBoxStrokeColor(int n) {
        if (this.focusedStrokeColor != n) {
            this.focusedStrokeColor = n;
            this.updateTextInputBoxState();
        }
    }

    public void setCounterEnabled(boolean bl) {
        if (this.counterEnabled != bl) {
            if (bl) {
                AppCompatTextView appCompatTextView = new AppCompatTextView(this.getContext());
                this.counterView = appCompatTextView;
                appCompatTextView.setId(R.id.textinput_counter);
                appCompatTextView = this.typeface;
                if (appCompatTextView != null) {
                    this.counterView.setTypeface((Typeface)appCompatTextView);
                }
                this.counterView.setMaxLines(1);
                this.indicatorViewController.addIndicator(this.counterView, 2);
                this.updateCounterTextAppearanceAndColor();
                this.updateCounter();
            } else {
                this.indicatorViewController.removeIndicator(this.counterView, 2);
                this.counterView = null;
            }
            this.counterEnabled = bl;
        }
    }

    public void setCounterMaxLength(int n) {
        if (this.counterMaxLength != n) {
            this.counterMaxLength = n > 0 ? n : -1;
            if (this.counterEnabled) {
                this.updateCounter();
            }
        }
    }

    public void setCounterOverflowTextAppearance(int n) {
        if (this.counterOverflowTextAppearance != n) {
            this.counterOverflowTextAppearance = n;
            this.updateCounterTextAppearanceAndColor();
        }
    }

    public void setCounterOverflowTextColor(ColorStateList colorStateList) {
        if (this.counterOverflowTextColor != colorStateList) {
            this.counterOverflowTextColor = colorStateList;
            this.updateCounterTextAppearanceAndColor();
        }
    }

    public void setCounterTextAppearance(int n) {
        if (this.counterTextAppearance != n) {
            this.counterTextAppearance = n;
            this.updateCounterTextAppearanceAndColor();
        }
    }

    public void setCounterTextColor(ColorStateList colorStateList) {
        if (this.counterTextColor != colorStateList) {
            this.counterTextColor = colorStateList;
            this.updateCounterTextAppearanceAndColor();
        }
    }

    public void setDefaultHintTextColor(ColorStateList colorStateList) {
        this.defaultHintTextColor = colorStateList;
        this.focusedTextColor = colorStateList;
        if (this.editText != null) {
            this.updateLabelState(false);
        }
    }

    public void setEnabled(boolean bl) {
        TextInputLayout.recursiveSetEnabled((ViewGroup)this, bl);
        super.setEnabled(bl);
    }

    public void setEndIconActivated(boolean bl) {
        this.endIconView.setActivated(bl);
    }

    public void setEndIconCheckable(boolean bl) {
        this.endIconView.setCheckable(bl);
    }

    public void setEndIconContentDescription(int n) {
        CharSequence charSequence = n != 0 ? this.getResources().getText(n) : null;
        this.setEndIconContentDescription(charSequence);
    }

    public void setEndIconContentDescription(CharSequence charSequence) {
        if (this.getEndIconContentDescription() != charSequence) {
            this.endIconView.setContentDescription(charSequence);
        }
    }

    public void setEndIconDrawable(int n) {
        Drawable drawable2 = n != 0 ? AppCompatResources.getDrawable(this.getContext(), n) : null;
        this.setEndIconDrawable(drawable2);
    }

    public void setEndIconDrawable(Drawable drawable2) {
        this.endIconView.setImageDrawable(drawable2);
    }

    public void setEndIconMode(int n) {
        int n2 = this.endIconMode;
        this.endIconMode = n;
        boolean bl = n != 0;
        this.setEndIconVisible(bl);
        if (this.getEndIconDelegate().isBoxBackgroundModeSupported(this.boxBackgroundMode)) {
            this.getEndIconDelegate().initialize();
            this.applyEndIconTint();
            this.dispatchOnEndIconChanged(n2);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("The current box background mode ");
        stringBuilder.append(this.boxBackgroundMode);
        stringBuilder.append(" is not supported by the end icon mode ");
        stringBuilder.append(n);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public void setEndIconOnClickListener(View.OnClickListener onClickListener) {
        TextInputLayout.setIconOnClickListener(this.endIconView, onClickListener, this.endIconOnLongClickListener);
    }

    public void setEndIconOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.endIconOnLongClickListener = onLongClickListener;
        TextInputLayout.setIconOnLongClickListener(this.endIconView, onLongClickListener);
    }

    public void setEndIconTintList(ColorStateList colorStateList) {
        if (this.endIconTintList != colorStateList) {
            this.endIconTintList = colorStateList;
            this.hasEndIconTintList = true;
            this.applyEndIconTint();
        }
    }

    public void setEndIconTintMode(PorterDuff.Mode mode) {
        if (this.endIconTintMode != mode) {
            this.endIconTintMode = mode;
            this.hasEndIconTintMode = true;
            this.applyEndIconTint();
        }
    }

    public void setEndIconVisible(boolean bl) {
        if (this.isEndIconVisible() != bl) {
            CheckableImageButton checkableImageButton = this.endIconView;
            int n = bl ? 0 : 4;
            checkableImageButton.setVisibility(n);
            this.updateIconDummyDrawables();
        }
    }

    public void setError(CharSequence charSequence) {
        if (!this.indicatorViewController.isErrorEnabled()) {
            if (TextUtils.isEmpty((CharSequence)charSequence)) {
                return;
            }
            this.setErrorEnabled(true);
        }
        if (!TextUtils.isEmpty((CharSequence)charSequence)) {
            this.indicatorViewController.showError(charSequence);
            return;
        }
        this.indicatorViewController.hideError();
    }

    public void setErrorEnabled(boolean bl) {
        this.indicatorViewController.setErrorEnabled(bl);
    }

    public void setErrorIconDrawable(int n) {
        Drawable drawable2 = n != 0 ? AppCompatResources.getDrawable(this.getContext(), n) : null;
        this.setErrorIconDrawable(drawable2);
    }

    public void setErrorIconDrawable(Drawable drawable2) {
        this.errorIconView.setImageDrawable(drawable2);
        boolean bl = drawable2 != null && this.indicatorViewController.isErrorEnabled();
        this.setErrorIconVisible(bl);
    }

    public void setErrorIconTintList(ColorStateList colorStateList) {
        Drawable drawable2;
        Drawable drawable3 = drawable2 = this.errorIconView.getDrawable();
        if (drawable2 != null) {
            drawable3 = DrawableCompat.wrap(drawable2).mutate();
            DrawableCompat.setTintList(drawable3, colorStateList);
        }
        if (this.errorIconView.getDrawable() != drawable3) {
            this.errorIconView.setImageDrawable(drawable3);
        }
    }

    public void setErrorIconTintMode(PorterDuff.Mode mode) {
        Drawable drawable2;
        Drawable drawable3 = drawable2 = this.errorIconView.getDrawable();
        if (drawable2 != null) {
            drawable3 = DrawableCompat.wrap(drawable2).mutate();
            DrawableCompat.setTintMode(drawable3, mode);
        }
        if (this.errorIconView.getDrawable() != drawable3) {
            this.errorIconView.setImageDrawable(drawable3);
        }
    }

    public void setErrorTextAppearance(int n) {
        this.indicatorViewController.setErrorTextAppearance(n);
    }

    public void setErrorTextColor(ColorStateList colorStateList) {
        this.indicatorViewController.setErrorViewTextColor(colorStateList);
    }

    public void setHelperText(CharSequence charSequence) {
        if (TextUtils.isEmpty((CharSequence)charSequence)) {
            if (this.isHelperTextEnabled()) {
                this.setHelperTextEnabled(false);
                return;
            }
        } else {
            if (!this.isHelperTextEnabled()) {
                this.setHelperTextEnabled(true);
            }
            this.indicatorViewController.showHelper(charSequence);
        }
    }

    public void setHelperTextColor(ColorStateList colorStateList) {
        this.indicatorViewController.setHelperTextViewTextColor(colorStateList);
    }

    public void setHelperTextEnabled(boolean bl) {
        this.indicatorViewController.setHelperTextEnabled(bl);
    }

    public void setHelperTextTextAppearance(int n) {
        this.indicatorViewController.setHelperTextAppearance(n);
    }

    public void setHint(CharSequence charSequence) {
        if (this.hintEnabled) {
            this.setHintInternal(charSequence);
            this.sendAccessibilityEvent(2048);
        }
    }

    public void setHintAnimationEnabled(boolean bl) {
        this.hintAnimationEnabled = bl;
    }

    public void setHintEnabled(boolean bl) {
        if (bl != this.hintEnabled) {
            this.hintEnabled = bl;
            if (!bl) {
                this.isProvidingHint = false;
                if (!TextUtils.isEmpty((CharSequence)this.hint) && TextUtils.isEmpty((CharSequence)this.editText.getHint())) {
                    this.editText.setHint(this.hint);
                }
                this.setHintInternal(null);
            } else {
                CharSequence charSequence = this.editText.getHint();
                if (!TextUtils.isEmpty((CharSequence)charSequence)) {
                    if (TextUtils.isEmpty((CharSequence)this.hint)) {
                        this.setHint(charSequence);
                    }
                    this.editText.setHint(null);
                }
                this.isProvidingHint = true;
            }
            if (this.editText != null) {
                this.updateInputLayoutMargins();
            }
        }
    }

    public void setHintTextAppearance(int n) {
        this.collapsingTextHelper.setCollapsedTextAppearance(n);
        this.focusedTextColor = this.collapsingTextHelper.getCollapsedTextColor();
        if (this.editText != null) {
            this.updateLabelState(false);
            this.updateInputLayoutMargins();
        }
    }

    public void setHintTextColor(ColorStateList colorStateList) {
        if (this.focusedTextColor != colorStateList) {
            if (this.defaultHintTextColor == null) {
                this.collapsingTextHelper.setCollapsedTextColor(colorStateList);
            }
            this.focusedTextColor = colorStateList;
            if (this.editText != null) {
                this.updateLabelState(false);
            }
        }
    }

    @Deprecated
    public void setPasswordVisibilityToggleContentDescription(int n) {
        CharSequence charSequence = n != 0 ? this.getResources().getText(n) : null;
        this.setPasswordVisibilityToggleContentDescription(charSequence);
    }

    @Deprecated
    public void setPasswordVisibilityToggleContentDescription(CharSequence charSequence) {
        this.endIconView.setContentDescription(charSequence);
    }

    @Deprecated
    public void setPasswordVisibilityToggleDrawable(int n) {
        Drawable drawable2 = n != 0 ? AppCompatResources.getDrawable(this.getContext(), n) : null;
        this.setPasswordVisibilityToggleDrawable(drawable2);
    }

    @Deprecated
    public void setPasswordVisibilityToggleDrawable(Drawable drawable2) {
        this.endIconView.setImageDrawable(drawable2);
    }

    @Deprecated
    public void setPasswordVisibilityToggleEnabled(boolean bl) {
        if (bl && this.endIconMode != 1) {
            this.setEndIconMode(1);
            return;
        }
        if (!bl) {
            this.setEndIconMode(0);
        }
    }

    @Deprecated
    public void setPasswordVisibilityToggleTintList(ColorStateList colorStateList) {
        this.endIconTintList = colorStateList;
        this.hasEndIconTintList = true;
        this.applyEndIconTint();
    }

    @Deprecated
    public void setPasswordVisibilityToggleTintMode(PorterDuff.Mode mode) {
        this.endIconTintMode = mode;
        this.hasEndIconTintMode = true;
        this.applyEndIconTint();
    }

    public void setStartIconCheckable(boolean bl) {
        this.startIconView.setCheckable(bl);
    }

    public void setStartIconContentDescription(int n) {
        CharSequence charSequence = n != 0 ? this.getResources().getText(n) : null;
        this.setStartIconContentDescription(charSequence);
    }

    public void setStartIconContentDescription(CharSequence charSequence) {
        if (this.getStartIconContentDescription() != charSequence) {
            this.startIconView.setContentDescription(charSequence);
        }
    }

    public void setStartIconDrawable(int n) {
        Drawable drawable2 = n != 0 ? AppCompatResources.getDrawable(this.getContext(), n) : null;
        this.setStartIconDrawable(drawable2);
    }

    public void setStartIconDrawable(Drawable drawable2) {
        this.startIconView.setImageDrawable(drawable2);
        if (drawable2 != null) {
            this.setStartIconVisible(true);
            this.applyStartIconTint();
            return;
        }
        this.setStartIconVisible(false);
        this.setStartIconOnClickListener(null);
        this.setStartIconOnLongClickListener(null);
        this.setStartIconContentDescription(null);
    }

    public void setStartIconOnClickListener(View.OnClickListener onClickListener) {
        TextInputLayout.setIconOnClickListener(this.startIconView, onClickListener, this.startIconOnLongClickListener);
    }

    public void setStartIconOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.startIconOnLongClickListener = onLongClickListener;
        TextInputLayout.setIconOnLongClickListener(this.startIconView, onLongClickListener);
    }

    public void setStartIconTintList(ColorStateList colorStateList) {
        if (this.startIconTintList != colorStateList) {
            this.startIconTintList = colorStateList;
            this.hasStartIconTintList = true;
            this.applyStartIconTint();
        }
    }

    public void setStartIconTintMode(PorterDuff.Mode mode) {
        if (this.startIconTintMode != mode) {
            this.startIconTintMode = mode;
            this.hasStartIconTintMode = true;
            this.applyStartIconTint();
        }
    }

    public void setStartIconVisible(boolean bl) {
        if (this.isStartIconVisible() != bl) {
            CheckableImageButton checkableImageButton = this.startIconView;
            int n = bl ? 0 : 8;
            checkableImageButton.setVisibility(n);
            this.updateIconDummyDrawables();
        }
    }

    void setTextAppearanceCompatWithErrorFallback(TextView textView, int n) {
        int n2 = 0;
        TextViewCompat.setTextAppearance(textView, n);
        n = n2;
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                int n3 = textView.getTextColors().getDefaultColor();
                n = n2;
                if (n3 == -65281) {
                    n = 1;
                }
            }
        }
        catch (Exception exception) {
            n = 1;
        }
        if (n != 0) {
            TextViewCompat.setTextAppearance(textView, R.style.TextAppearance_AppCompat_Caption);
            textView.setTextColor(ContextCompat.getColor(this.getContext(), R.color.design_error));
        }
    }

    public void setTextInputAccessibilityDelegate(AccessibilityDelegate accessibilityDelegate) {
        EditText editText = this.editText;
        if (editText != null) {
            ViewCompat.setAccessibilityDelegate((View)editText, accessibilityDelegate);
        }
    }

    public void setTypeface(Typeface typeface) {
        if (typeface != this.typeface) {
            this.typeface = typeface;
            this.collapsingTextHelper.setTypefaces(typeface);
            this.indicatorViewController.setTypefaces(typeface);
            TextView textView = this.counterView;
            if (textView != null) {
                textView.setTypeface(typeface);
            }
        }
    }

    void updateCounter(int n) {
        boolean bl = this.counterOverflowed;
        if (this.counterMaxLength == -1) {
            this.counterView.setText((CharSequence)String.valueOf(n));
            this.counterView.setContentDescription(null);
            this.counterOverflowed = false;
        } else {
            if (ViewCompat.getAccessibilityLiveRegion((View)this.counterView) == 1) {
                ViewCompat.setAccessibilityLiveRegion((View)this.counterView, 0);
            }
            boolean bl2 = n > this.counterMaxLength;
            this.counterOverflowed = bl2;
            TextInputLayout.updateCounterContentDescription(this.getContext(), this.counterView, n, this.counterMaxLength, this.counterOverflowed);
            if (bl != this.counterOverflowed) {
                this.updateCounterTextAppearanceAndColor();
                if (this.counterOverflowed) {
                    ViewCompat.setAccessibilityLiveRegion((View)this.counterView, 1);
                }
            }
            this.counterView.setText((CharSequence)this.getContext().getString(R.string.character_counter_pattern, new Object[]{n, this.counterMaxLength}));
        }
        if (this.editText != null && bl != this.counterOverflowed) {
            this.updateLabelState(false);
            this.updateTextInputBoxState();
            this.updateEditTextBackground();
        }
    }

    void updateEditTextBackground() {
        EditText editText = this.editText;
        if (editText != null) {
            if (this.boxBackgroundMode != 0) {
                return;
            }
            Drawable drawable2 = editText.getBackground();
            if (drawable2 == null) {
                return;
            }
            editText = drawable2;
            if (DrawableUtils.canSafelyMutateDrawable(drawable2)) {
                editText = drawable2.mutate();
            }
            if (this.indicatorViewController.errorShouldBeShown()) {
                editText.setColorFilter((ColorFilter)AppCompatDrawableManager.getPorterDuffColorFilter(this.indicatorViewController.getErrorViewCurrentTextColor(), PorterDuff.Mode.SRC_IN));
                return;
            }
            if (this.counterOverflowed && (drawable2 = this.counterView) != null) {
                editText.setColorFilter((ColorFilter)AppCompatDrawableManager.getPorterDuffColorFilter(drawable2.getCurrentTextColor(), PorterDuff.Mode.SRC_IN));
                return;
            }
            DrawableCompat.clearColorFilter((Drawable)editText);
            this.editText.refreshDrawableState();
            return;
        }
    }

    void updateLabelState(boolean bl) {
        this.updateLabelState(bl, false);
    }

    void updateTextInputBoxState() {
        if (this.boxBackground != null) {
            EditText editText;
            if (this.boxBackgroundMode == 0) {
                return;
            }
            boolean bl = this.isFocused();
            boolean bl2 = false;
            boolean bl3 = bl || (editText = this.editText) != null && editText.hasFocus();
            boolean bl4 = this.isHovered() || (editText = this.editText) != null && editText.isHovered();
            this.boxStrokeColor = !this.isEnabled() ? this.disabledColor : (this.indicatorViewController.errorShouldBeShown() ? this.indicatorViewController.getErrorViewCurrentTextColor() : (this.counterOverflowed && (editText = this.counterView) != null ? editText.getCurrentTextColor() : (bl3 ? this.focusedStrokeColor : (bl4 ? this.hoveredStrokeColor : this.defaultStrokeColor))));
            bl = this.indicatorViewController.errorShouldBeShown() && this.getEndIconDelegate().shouldTintIconOnError();
            this.tintEndIconOnError(bl);
            bl = this.getErrorIconDrawable() != null && this.indicatorViewController.isErrorEnabled() && this.indicatorViewController.errorShouldBeShown() ? true : bl2;
            this.setErrorIconVisible(bl);
            this.boxStrokeWidthPx = (bl4 || bl3) && this.isEnabled() ? this.boxStrokeWidthFocusedPx : this.boxStrokeWidthDefaultPx;
            if (this.boxBackgroundMode == 1) {
                this.boxBackgroundColor = !this.isEnabled() ? this.disabledFilledBackgroundColor : (bl4 ? this.hoveredFilledBackgroundColor : this.defaultFilledBackgroundColor);
            }
            this.applyBoxAttributes();
            return;
        }
    }

    public static class AccessibilityDelegate
    extends AccessibilityDelegateCompat {
        private final TextInputLayout layout;

        public AccessibilityDelegate(TextInputLayout textInputLayout) {
            this.layout = textInputLayout;
        }

        @Override
        public void onInitializeAccessibilityNodeInfo(View object, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo((View)object, accessibilityNodeInfoCompat);
            object = this.layout.getEditText();
            object = object != null ? object.getText() : null;
            CharSequence charSequence = this.layout.getHint();
            CharSequence charSequence2 = this.layout.getError();
            CharSequence charSequence3 = this.layout.getCounterOverflowDescription();
            boolean bl = TextUtils.isEmpty((CharSequence)object) ^ true;
            boolean bl2 = TextUtils.isEmpty((CharSequence)charSequence) ^ true;
            boolean bl3 = TextUtils.isEmpty((CharSequence)charSequence2) ^ true;
            boolean bl4 = false;
            boolean bl5 = bl3 || !TextUtils.isEmpty((CharSequence)charSequence3);
            if (bl) {
                accessibilityNodeInfoCompat.setText((CharSequence)object);
            } else if (bl2) {
                accessibilityNodeInfoCompat.setText(charSequence);
            }
            if (bl2) {
                accessibilityNodeInfoCompat.setHintText(charSequence);
                boolean bl6 = bl4;
                if (!bl) {
                    bl6 = bl4;
                    if (bl2) {
                        bl6 = true;
                    }
                }
                accessibilityNodeInfoCompat.setShowingHintText(bl6);
            }
            if (bl5) {
                object = bl3 ? charSequence2 : charSequence3;
                accessibilityNodeInfoCompat.setError((CharSequence)object);
                accessibilityNodeInfoCompat.setContentInvalid(true);
            }
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface BoxBackgroundMode {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface EndIconMode {
    }

    public static interface OnEditTextAttachedListener {
        public void onEditTextAttached(TextInputLayout var1);
    }

    public static interface OnEndIconChangedListener {
        public void onEndIconChanged(TextInputLayout var1, int var2);
    }

    static class SavedState
    extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>(){

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            public SavedState[] newArray(int n) {
                return new SavedState[n];
            }
        };
        CharSequence error;
        boolean isEndIconChecked;

        SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.error = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
            int n = parcel.readInt();
            boolean bl = true;
            if (n != 1) {
                bl = false;
            }
            this.isEndIconChecked = bl;
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("TextInputLayout.SavedState{");
            stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
            stringBuilder.append(" error=");
            stringBuilder.append((Object)this.error);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }

        @Override
        public void writeToParcel(Parcel parcel, int n) {
            RuntimeException runtimeException;
            super("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e1expr(TypeTransformer.java:496)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:713)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
            throw runtimeException;
        }

    }

}

