/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.graphics.drawable.Drawable
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.text.TextUtils
 *  android.util.AttributeSet
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$BaseSavedState
 *  android.view.View$OnClickListener
 *  android.view.View$OnLongClickListener
 *  android.view.ViewGroup
 *  android.widget.Button
 *  android.widget.ImageButton
 *  android.widget.LinearLayout
 *  android.widget.TextView
 *  com.codetroopers.betterpickers.R
 *  com.codetroopers.betterpickers.R$color
 *  com.codetroopers.betterpickers.R$drawable
 *  com.codetroopers.betterpickers.R$id
 *  com.codetroopers.betterpickers.R$layout
 *  com.codetroopers.betterpickers.R$string
 *  com.codetroopers.betterpickers.R$styleable
 */
package com.codetroopers.betterpickers.numberpicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.codetroopers.betterpickers.R;
import com.codetroopers.betterpickers.numberpicker.NumberPickerErrorTextView;
import com.codetroopers.betterpickers.numberpicker.NumberView;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;

public class NumberPicker
extends LinearLayout
implements View.OnClickListener,
View.OnLongClickListener {
    private static final int CLICKED_DECIMAL = 10;
    public static final int SIGN_NEGATIVE = 1;
    public static final int SIGN_POSITIVE = 0;
    private int mButtonBackgroundResId;
    protected final Context mContext;
    protected ImageButton mDelete;
    private int mDeleteDrawableSrcResId;
    protected View mDivider;
    private int mDividerColor;
    protected NumberView mEnteredNumber;
    private NumberPickerErrorTextView mError;
    protected int[] mInput = new int[20];
    protected int mInputPointer = -1;
    protected int mInputSize = 20;
    private int mKeyBackgroundResId;
    private TextView mLabel;
    private String mLabelText = "";
    protected Button mLeft;
    private BigDecimal mMaxNumber = null;
    private BigDecimal mMinNumber = null;
    protected final Button[] mNumbers = new Button[10];
    protected Button mRight;
    private Button mSetButton;
    private int mSign;
    private ColorStateList mTextColor;
    private int mTheme = -1;

    public NumberPicker(Context context) {
        this(context, null);
    }

    public NumberPicker(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        ((LayoutInflater)context.getSystemService("layout_inflater")).inflate(this.getLayoutId(), (ViewGroup)this);
        this.mTextColor = this.getResources().getColorStateList(R.color.dialog_text_color_holo_dark);
        this.mKeyBackgroundResId = R.drawable.key_background_dark;
        this.mButtonBackgroundResId = R.drawable.button_background_dark;
        this.mDeleteDrawableSrcResId = R.drawable.ic_backspace_dark;
        this.mDividerColor = this.getResources().getColor(R.color.default_divider_color_dark);
    }

    private void addClickedNumber(int n) {
        if (this.mInputPointer < this.mInputSize - 1) {
            int[] arrn = this.mInput;
            if (arrn[0] == 0 && arrn[1] == -1 && !this.containsDecimal() && n != 10) {
                this.mInput[0] = n;
                return;
            }
            for (int i = this.mInputPointer; i >= 0; --i) {
                arrn = this.mInput;
                arrn[i + 1] = arrn[i];
            }
            ++this.mInputPointer;
            this.mInput[0] = n;
        }
    }

    private boolean canAddDecimal() {
        return this.containsDecimal() ^ true;
    }

    private boolean containsDecimal() {
        boolean bl = false;
        int[] arrn = this.mInput;
        int n = arrn.length;
        for (int i = 0; i < n; ++i) {
            if (arrn[i] != 10) continue;
            bl = true;
        }
        return bl;
    }

    private String doubleToString(double d) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        decimalFormat.setMaximumFractionDigits(Integer.MAX_VALUE);
        return decimalFormat.format(d);
    }

    private void enableSetButton() {
        Button button = this.mSetButton;
        if (button == null) {
            return;
        }
        int n = this.mInputPointer;
        boolean bl = false;
        if (n == -1) {
            button.setEnabled(false);
            return;
        }
        if (n >= 0) {
            bl = true;
        }
        button.setEnabled(bl);
    }

    private String getEnteredNumberString() {
        String string2 = "";
        for (int i = this.mInputPointer; i >= 0; --i) {
            int[] arrn = this.mInput;
            if (arrn[i] == -1) continue;
            if (arrn[i] == 10) {
                arrn = new StringBuilder();
                arrn.append(string2);
                arrn.append(".");
                string2 = arrn.toString();
                continue;
            }
            arrn = new StringBuilder();
            arrn.append(string2);
            arrn.append(this.mInput[i]);
            string2 = arrn.toString();
        }
        return string2;
    }

    private void onLeftClicked() {
        if (this.mSign == 0) {
            this.mSign = 1;
            return;
        }
        this.mSign = 0;
    }

    private void onRightClicked() {
        if (this.canAddDecimal()) {
            this.addClickedNumber(10);
        }
    }

    private void readAndRightDigits(String string2) {
        for (int i = string2.length() - 1; i >= 0; --i) {
            int n;
            this.mInputPointer = n = this.mInputPointer + 1;
            this.mInput[n] = string2.charAt(i) - 48;
        }
    }

    private void restyleViews() {
        for (Button button : this.mNumbers) {
            if (button == null) continue;
            button.setTextColor(this.mTextColor);
            button.setBackgroundResource(this.mKeyBackgroundResId);
        }
        Object object = this.mDivider;
        if (object != null) {
            object.setBackgroundColor(this.mDividerColor);
        }
        if ((object = this.mLeft) != null) {
            object.setTextColor(this.mTextColor);
            this.mLeft.setBackgroundResource(this.mKeyBackgroundResId);
        }
        if ((object = this.mRight) != null) {
            object.setTextColor(this.mTextColor);
            this.mRight.setBackgroundResource(this.mKeyBackgroundResId);
        }
        if ((object = this.mDelete) != null) {
            object.setBackgroundResource(this.mButtonBackgroundResId);
            this.mDelete.setImageDrawable(this.getResources().getDrawable(this.mDeleteDrawableSrcResId));
        }
        if ((object = this.mEnteredNumber) != null) {
            object.setTheme(this.mTheme);
        }
        if ((object = this.mLabel) != null) {
            object.setTextColor(this.mTextColor);
        }
    }

    private void showLabel() {
        TextView textView = this.mLabel;
        if (textView != null) {
            textView.setText((CharSequence)this.mLabelText);
        }
    }

    private void updateKeypad() {
        this.updateLeftRightButtons();
        this.updateNumber();
        this.enableSetButton();
        this.updateDeleteButton();
    }

    private void updateLeftRightButtons() {
        this.mRight.setEnabled(this.canAddDecimal());
    }

    protected void doOnClick(View arrn) {
        Integer n = (Integer)arrn.getTag(R.id.numbers_key);
        if (n != null) {
            this.addClickedNumber(n);
        } else if (arrn == this.mDelete) {
            if (this.mInputPointer >= 0) {
                int n2;
                for (int i = 0; i < (n2 = this.mInputPointer); ++i) {
                    arrn = this.mInput;
                    arrn[i] = arrn[i + 1];
                }
                this.mInput[n2] = -1;
                this.mInputPointer = n2 - 1;
            }
        } else if (arrn == this.mLeft) {
            this.onLeftClicked();
        } else if (arrn == this.mRight) {
            this.onRightClicked();
        }
        this.updateKeypad();
    }

    public double getDecimal() {
        return this.getEnteredNumber().remainder(BigDecimal.ONE).doubleValue();
    }

    public BigDecimal getEnteredNumber() {
        Object object;
        Object object2 = "0";
        for (int i = this.mInputPointer; i >= 0 && (object = this.mInput)[i] != -1; --i) {
            if (object[i] == 10) {
                object = new StringBuilder();
                object.append((String)object2);
                object.append(".");
                object2 = object.toString();
                continue;
            }
            object = new StringBuilder();
            object.append((String)object2);
            object.append(this.mInput[i]);
            object2 = object.toString();
        }
        object = object2;
        if (this.mSign == 1) {
            object = new StringBuilder();
            object.append("-");
            object.append((String)object2);
            object = object.toString();
        }
        return new BigDecimal((String)object);
    }

    public NumberPickerErrorTextView getErrorView() {
        return this.mError;
    }

    public boolean getIsNegative() {
        if (this.mSign == 1) {
            return true;
        }
        return false;
    }

    protected int getLayoutId() {
        return R.layout.number_picker_view;
    }

    public BigInteger getNumber() {
        return this.getEnteredNumber().setScale(0, 3).toBigIntegerExact();
    }

    public void onClick(View view) {
        view.performHapticFeedback(1);
        this.mError.hideImmediately();
        this.doOnClick(view);
        this.updateDeleteButton();
    }

    protected void onFinishInflate() {
        View view;
        ImageButton imageButton;
        int n;
        super.onFinishInflate();
        this.mDivider = this.findViewById(R.id.divider);
        this.mError = (NumberPickerErrorTextView)this.findViewById(R.id.error);
        for (n = 0; n < (view = this.mInput).length; ++n) {
            view[n] = -1;
        }
        view = this.findViewById(R.id.first);
        View view2 = this.findViewById(R.id.second);
        View view3 = this.findViewById(R.id.third);
        View view4 = this.findViewById(R.id.fourth);
        this.mEnteredNumber = (NumberView)this.findViewById(R.id.number_text);
        this.mDelete = imageButton = (ImageButton)this.findViewById(R.id.delete);
        imageButton.setOnClickListener((View.OnClickListener)this);
        this.mDelete.setOnLongClickListener((View.OnLongClickListener)this);
        this.mNumbers[1] = (Button)view.findViewById(R.id.key_left);
        this.mNumbers[2] = (Button)view.findViewById(R.id.key_middle);
        this.mNumbers[3] = (Button)view.findViewById(R.id.key_right);
        this.mNumbers[4] = (Button)view2.findViewById(R.id.key_left);
        this.mNumbers[5] = (Button)view2.findViewById(R.id.key_middle);
        this.mNumbers[6] = (Button)view2.findViewById(R.id.key_right);
        this.mNumbers[7] = (Button)view3.findViewById(R.id.key_left);
        this.mNumbers[8] = (Button)view3.findViewById(R.id.key_middle);
        this.mNumbers[9] = (Button)view3.findViewById(R.id.key_right);
        this.mLeft = (Button)view4.findViewById(R.id.key_left);
        this.mNumbers[0] = (Button)view4.findViewById(R.id.key_middle);
        this.mRight = (Button)view4.findViewById(R.id.key_right);
        this.setLeftRightEnabled();
        for (n = 0; n < 10; ++n) {
            this.mNumbers[n].setOnClickListener((View.OnClickListener)this);
            this.mNumbers[n].setText((CharSequence)String.format("%d", n));
            this.mNumbers[n].setTag(R.id.numbers_key, (Object)new Integer(n));
        }
        this.updateNumber();
        view = this.mContext.getResources();
        this.mLeft.setText((CharSequence)view.getString(R.string.number_picker_plus_minus));
        this.mRight.setText((CharSequence)view.getString(R.string.number_picker_seperator));
        this.mLeft.setOnClickListener((View.OnClickListener)this);
        this.mRight.setOnClickListener((View.OnClickListener)this);
        this.mLabel = (TextView)this.findViewById(R.id.label);
        this.mSign = 0;
        this.showLabel();
        this.restyleViews();
        this.updateKeypad();
    }

    public boolean onLongClick(View view) {
        view.performHapticFeedback(0);
        this.mError.hideImmediately();
        ImageButton imageButton = this.mDelete;
        if (view == imageButton) {
            imageButton.setPressed(false);
            this.reset();
            this.updateKeypad();
            return true;
        }
        return false;
    }

    protected void onRestoreInstanceState(Parcelable object) {
        if (!(object instanceof SavedState)) {
            super.onRestoreInstanceState((Parcelable)object);
            return;
        }
        object = (SavedState)((Object)object);
        super.onRestoreInstanceState(object.getSuperState());
        this.mInputPointer = object.mInputPointer;
        int[] arrn = object.mInput;
        this.mInput = arrn;
        if (arrn == null) {
            this.mInput = new int[this.mInputSize];
            this.mInputPointer = -1;
        }
        this.mSign = object.mSign;
        this.updateKeypad();
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.mInput = this.mInput;
        savedState.mSign = this.mSign;
        savedState.mInputPointer = this.mInputPointer;
        return savedState;
    }

    public void reset() {
        for (int i = 0; i < this.mInputSize; ++i) {
            this.mInput[i] = -1;
        }
        this.mInputPointer = -1;
        this.updateNumber();
    }

    public void setDecimalVisibility(int n) {
        Button button = this.mRight;
        if (button != null) {
            button.setVisibility(n);
        }
    }

    public void setLabelText(String string2) {
        this.mLabelText = string2;
        this.showLabel();
    }

    protected void setLeftRightEnabled() {
        this.mLeft.setEnabled(true);
        this.mRight.setEnabled(this.canAddDecimal());
        if (!this.canAddDecimal()) {
            this.mRight.setContentDescription(null);
        }
    }

    public void setMax(BigDecimal bigDecimal) {
        this.mMaxNumber = bigDecimal;
    }

    public void setMin(BigDecimal bigDecimal) {
        this.mMinNumber = bigDecimal;
    }

    public void setNumber(Integer n, Double object, Integer n2) {
        this.mSign = n2 != null ? n2 : 0;
        if (object != null) {
            int n3;
            object = this.doubleToString(object.doubleValue());
            this.readAndRightDigits(TextUtils.substring((CharSequence)object, (int)2, (int)object.length()));
            this.mInputPointer = n3 = this.mInputPointer + 1;
            this.mInput[n3] = 10;
        }
        if (n != null) {
            this.readAndRightDigits(String.valueOf(n));
        }
        this.updateKeypad();
    }

    public void setPlusMinusVisibility(int n) {
        Button button = this.mLeft;
        if (button != null) {
            button.setVisibility(n);
        }
    }

    public void setSetButton(Button button) {
        this.mSetButton = button;
        this.enableSetButton();
    }

    public void setTheme(int n) {
        this.mTheme = n;
        if (n != -1) {
            TypedArray typedArray = this.getContext().obtainStyledAttributes(n, R.styleable.BetterPickersDialogFragment);
            this.mTextColor = typedArray.getColorStateList(R.styleable.BetterPickersDialogFragment_bpTextColor);
            this.mKeyBackgroundResId = typedArray.getResourceId(R.styleable.BetterPickersDialogFragment_bpKeyBackground, this.mKeyBackgroundResId);
            this.mButtonBackgroundResId = typedArray.getResourceId(R.styleable.BetterPickersDialogFragment_bpButtonBackground, this.mButtonBackgroundResId);
            this.mDividerColor = typedArray.getColor(R.styleable.BetterPickersDialogFragment_bpDividerColor, this.mDividerColor);
            this.mDeleteDrawableSrcResId = typedArray.getResourceId(R.styleable.BetterPickersDialogFragment_bpDeleteIcon, this.mDeleteDrawableSrcResId);
        }
        this.restyleViews();
    }

    public void updateDeleteButton() {
        boolean bl = this.mInputPointer != -1;
        ImageButton imageButton = this.mDelete;
        if (imageButton != null) {
            imageButton.setEnabled(bl);
        }
    }

    protected void updateNumber() {
        Object object = this.getEnteredNumberString().replaceAll("\\-", "");
        Object object2 = object.split("\\.");
        int n = object2.length;
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        boolean bl4 = false;
        if (n >= 2) {
            if (object2[0].equals("")) {
                object = this.mEnteredNumber;
                object2 = object2[1];
                bl = this.containsDecimal();
                if (this.mSign == 1) {
                    bl4 = true;
                }
                object.setNumber("0", (String)object2, bl, bl4);
                return;
            }
            object = this.mEnteredNumber;
            String string2 = object2[0];
            object2 = object2[1];
            bl2 = this.containsDecimal();
            bl4 = bl;
            if (this.mSign == 1) {
                bl4 = true;
            }
            object.setNumber(string2, (String)object2, bl2, bl4);
            return;
        }
        if (object2.length == 1) {
            object = this.mEnteredNumber;
            object2 = object2[0];
            bl = this.containsDecimal();
            bl4 = bl2;
            if (this.mSign == 1) {
                bl4 = true;
            }
            object.setNumber((String)object2, "", bl, bl4);
            return;
        }
        if (object.equals(".")) {
            object2 = this.mEnteredNumber;
            bl4 = bl3;
            if (this.mSign == 1) {
                bl4 = true;
            }
            object2.setNumber("0", "", true, bl4);
        }
    }

    private static class SavedState
    extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>(){

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int n) {
                return new SavedState[n];
            }
        };
        int[] mInput;
        int mInputPointer;
        int mSign;

        private SavedState(Parcel parcel) {
            super(parcel);
            this.mInputPointer = parcel.readInt();
            int n = parcel.readInt();
            if (n > 0) {
                int[] arrn = new int[n];
                this.mInput = arrn;
                parcel.readIntArray(arrn);
            }
            this.mSign = parcel.readInt();
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.mInputPointer);
            int[] arrn = this.mInput;
            if (arrn != null) {
                parcel.writeInt(arrn.length);
                parcel.writeIntArray(this.mInput);
            } else {
                parcel.writeInt(0);
            }
            parcel.writeInt(this.mSign);
        }

    }

}

