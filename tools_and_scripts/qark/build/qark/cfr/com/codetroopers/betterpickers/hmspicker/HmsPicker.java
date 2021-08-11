/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.graphics.drawable.Drawable
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
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
package com.codetroopers.betterpickers.hmspicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.codetroopers.betterpickers.R;
import com.codetroopers.betterpickers.hmspicker.HmsView;

public class HmsPicker
extends LinearLayout
implements View.OnClickListener,
View.OnLongClickListener {
    public static final int SIGN_NEGATIVE = 1;
    public static final int SIGN_POSITIVE = 0;
    private int mButtonBackgroundResId;
    protected final Context mContext;
    protected ImageButton mDelete;
    private int mDeleteDrawableSrcResId;
    protected View mDivider;
    private int mDividerColor;
    protected HmsView mEnteredHms;
    private TextView mHoursLabel;
    protected int[] mInput = new int[5];
    protected int mInputPointer = -1;
    protected int mInputSize = 5;
    private int mKeyBackgroundResId;
    protected Button mLeft;
    private TextView mMinutesLabel;
    protected final Button[] mNumbers = new Button[10];
    protected Button mRight;
    private TextView mSecondsLabel;
    private Button mSetButton;
    private int mSign;
    private ColorStateList mTextColor;
    private int mTheme = -1;

    public HmsPicker(Context context) {
        this(context, null);
    }

    public HmsPicker(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        ((LayoutInflater)context.getSystemService("layout_inflater")).inflate(this.getLayoutId(), (ViewGroup)this);
        this.mTextColor = this.getResources().getColorStateList(R.color.dialog_text_color_holo_dark);
        this.mKeyBackgroundResId = R.drawable.key_background_dark;
        this.mButtonBackgroundResId = R.drawable.button_background_dark;
        this.mDividerColor = this.getResources().getColor(R.color.default_divider_color_dark);
        this.mDeleteDrawableSrcResId = R.drawable.ic_backspace_dark;
    }

    private void addClickedNumber(int n) {
        int n2 = this.mInputPointer;
        if (n2 < this.mInputSize - 1 && (n2 != -1 || n != 0)) {
            for (n2 = this.mInputPointer; n2 >= 0; --n2) {
                int[] arrn = this.mInput;
                arrn[n2 + 1] = arrn[n2];
            }
            ++this.mInputPointer;
            this.mInput[0] = n;
        }
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

    private void onLeftClicked() {
        if (this.isNegative()) {
            this.mSign = 0;
            return;
        }
        this.mSign = 1;
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
        if ((object = this.mHoursLabel) != null) {
            object.setTextColor(this.mTextColor);
            this.mHoursLabel.setBackgroundResource(this.mKeyBackgroundResId);
        }
        if ((object = this.mMinutesLabel) != null) {
            object.setTextColor(this.mTextColor);
            this.mMinutesLabel.setBackgroundResource(this.mKeyBackgroundResId);
        }
        if ((object = this.mSecondsLabel) != null) {
            object.setTextColor(this.mTextColor);
            this.mSecondsLabel.setBackgroundResource(this.mKeyBackgroundResId);
        }
        if ((object = this.mDelete) != null) {
            object.setBackgroundResource(this.mButtonBackgroundResId);
            this.mDelete.setImageDrawable(this.getResources().getDrawable(this.mDeleteDrawableSrcResId));
        }
        if ((object = this.mEnteredHms) != null) {
            object.setTheme(this.mTheme);
        }
        if ((object = this.mLeft) != null) {
            object.setTextColor(this.mTextColor);
            this.mLeft.setBackgroundResource(this.mKeyBackgroundResId);
        }
    }

    private void updateKeypad() {
        this.updateHms();
        this.enableSetButton();
        this.updateDeleteButton();
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
                this.mInput[n2] = 0;
                this.mInputPointer = n2 - 1;
            }
        } else if (arrn == this.mLeft) {
            this.onLeftClicked();
        }
        this.updateKeypad();
    }

    public int getHours() {
        return this.mInput[4];
    }

    protected int getLayoutId() {
        return R.layout.hms_picker_view;
    }

    public int getMinutes() {
        int[] arrn = this.mInput;
        return arrn[3] * 10 + arrn[2];
    }

    public int getSeconds() {
        int[] arrn = this.mInput;
        return arrn[1] * 10 + arrn[0];
    }

    public int getTime() {
        int[] arrn = this.mInput;
        return arrn[4] * 3600 + arrn[3] * 600 + arrn[2] * 60 + arrn[1] * 10 + arrn[0];
    }

    public boolean isNegative() {
        if (this.mSign == 1) {
            return true;
        }
        return false;
    }

    public void onClick(View view) {
        view.performHapticFeedback(1);
        this.doOnClick(view);
        this.updateDeleteButton();
    }

    protected void onFinishInflate() {
        ImageButton imageButton;
        super.onFinishInflate();
        View view = this.findViewById(R.id.first);
        View view2 = this.findViewById(R.id.second);
        View view3 = this.findViewById(R.id.third);
        View view4 = this.findViewById(R.id.fourth);
        this.mEnteredHms = (HmsView)this.findViewById(R.id.hms_text);
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
        this.setRightEnabled(false);
        for (int i = 0; i < 10; ++i) {
            this.mNumbers[i].setOnClickListener((View.OnClickListener)this);
            this.mNumbers[i].setText((CharSequence)String.format("%d", i));
            this.mNumbers[i].setTag(R.id.numbers_key, (Object)new Integer(i));
        }
        this.updateHms();
        view = this.mContext.getResources();
        this.mLeft.setText((CharSequence)view.getString(R.string.number_picker_plus_minus));
        this.mLeft.setOnClickListener((View.OnClickListener)this);
        this.mHoursLabel = (TextView)this.findViewById(R.id.hours_label);
        this.mMinutesLabel = (TextView)this.findViewById(R.id.minutes_label);
        this.mSecondsLabel = (TextView)this.findViewById(R.id.seconds_label);
        this.mDivider = this.findViewById(R.id.divider);
        this.restyleViews();
        this.updateKeypad();
    }

    public boolean onLongClick(View view) {
        view.performHapticFeedback(0);
        ImageButton imageButton = this.mDelete;
        if (view == imageButton) {
            imageButton.setPressed(false);
            this.reset();
            this.updateKeypad();
            return true;
        }
        return false;
    }

    protected void onRestoreInstanceState(Parcelable arrn) {
        if (!(arrn instanceof SavedState)) {
            super.onRestoreInstanceState((Parcelable)arrn);
            return;
        }
        arrn = (SavedState)arrn;
        super.onRestoreInstanceState(arrn.getSuperState());
        this.mInputPointer = arrn.mInputPointer;
        arrn = arrn.mInput;
        this.mInput = arrn;
        if (arrn == null) {
            this.mInput = new int[this.mInputSize];
            this.mInputPointer = -1;
        }
        this.updateKeypad();
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.mInput = this.mInput;
        savedState.mInputPointer = this.mInputPointer;
        return savedState;
    }

    public void reset() {
        for (int i = 0; i < this.mInputSize; ++i) {
            this.mInput[i] = 0;
        }
        this.mInputPointer = -1;
        this.updateHms();
    }

    public void restoreEntryState(Bundle arrn, String arrn2) {
        if ((arrn = arrn.getIntArray((String)arrn2)) != null && this.mInputSize == arrn.length) {
            for (int i = 0; i < this.mInputSize; ++i) {
                arrn2 = this.mInput;
                arrn2[i] = arrn[i];
                if (arrn2[i] == 0) continue;
                this.mInputPointer = i;
            }
            this.updateHms();
        }
    }

    public void saveEntryState(Bundle bundle, String string2) {
        bundle.putIntArray(string2, this.mInput);
    }

    public void setPlusMinusVisibility(int n) {
        Button button = this.mLeft;
        if (button != null) {
            button.setVisibility(n);
        }
    }

    protected void setRightEnabled(boolean bl) {
        this.mRight.setEnabled(bl);
        if (!bl) {
            this.mRight.setContentDescription(null);
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

    public void setTime(int n, int n2, int n3) {
        int[] arrn = this.mInput;
        arrn[4] = n;
        arrn[3] = n2 / 10;
        arrn[2] = n2 % 10;
        arrn[1] = n3 / 10;
        arrn[0] = n3 % 10;
        for (n = 4; n >= 0; --n) {
            if (this.mInput[n] <= 0) continue;
            this.mInputPointer = n;
            break;
        }
        this.updateKeypad();
    }

    public void updateDeleteButton() {
        boolean bl = this.mInputPointer != -1;
        ImageButton imageButton = this.mDelete;
        if (imageButton != null) {
            imageButton.setEnabled(bl);
        }
    }

    protected void updateHms() {
        HmsView hmsView = this.mEnteredHms;
        boolean bl = this.isNegative();
        int[] arrn = this.mInput;
        hmsView.setTime(bl, arrn[4], arrn[3], arrn[2], arrn[1], arrn[0]);
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
        int mAmPmState;
        int[] mInput;
        int mInputPointer;

        private SavedState(Parcel parcel) {
            super(parcel);
            this.mInputPointer = parcel.readInt();
            this.mInput = parcel.createIntArray();
            this.mAmPmState = parcel.readInt();
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.mInputPointer);
            parcel.writeIntArray(this.mInput);
            parcel.writeInt(this.mAmPmState);
        }

    }

}

