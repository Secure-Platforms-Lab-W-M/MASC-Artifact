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
 *  android.text.format.DateFormat
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
package com.codetroopers.betterpickers.timepicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.codetroopers.betterpickers.R;
import com.codetroopers.betterpickers.timepicker.TimerView;
import java.text.DateFormatSymbols;

public class TimePicker
extends LinearLayout
implements View.OnClickListener,
View.OnLongClickListener {
    private static final int AMPM_NOT_SELECTED = 0;
    private static final int AM_SELECTED = 2;
    private static final int HOURS24_MODE = 3;
    private static final int PM_SELECTED = 1;
    private static final String TIME_PICKER_SAVED_AMPM = "timer_picker_saved_ampm";
    private static final String TIME_PICKER_SAVED_BUFFER_POINTER = "timer_picker_saved_buffer_pointer";
    private static final String TIME_PICKER_SAVED_INPUT = "timer_picker_saved_input";
    private TextView mAmPmLabel;
    private int mAmPmState;
    private String[] mAmpm;
    private int mButtonBackgroundResId;
    protected final Context mContext;
    protected ImageButton mDelete;
    private int mDeleteDrawableSrcResId;
    protected View mDivider;
    private int mDividerColor;
    protected TimerView mEnteredTime;
    protected int[] mInput = new int[4];
    protected int mInputPointer = -1;
    protected int mInputSize = 4;
    private boolean mIs24HoursMode = false;
    private int mKeyBackgroundResId;
    protected Button mLeft;
    private final String mNoAmPmLabel;
    protected final Button[] mNumbers = new Button[10];
    protected Button mRight;
    private Button mSetButton;
    private ColorStateList mTextColor;
    private int mTheme = -1;

    public TimePicker(Context context) {
        this(context, null);
    }

    public TimePicker(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        this.mIs24HoursMode = TimePicker.get24HourMode(context);
        ((LayoutInflater)context.getSystemService("layout_inflater")).inflate(this.getLayoutId(), (ViewGroup)this);
        this.mNoAmPmLabel = context.getResources().getString(R.string.time_picker_ampm_label);
        this.mTextColor = this.getResources().getColorStateList(R.color.dialog_text_color_holo_dark);
        this.mKeyBackgroundResId = R.drawable.key_background_dark;
        this.mButtonBackgroundResId = R.drawable.button_background_dark;
        this.mDividerColor = this.getResources().getColor(R.color.default_divider_color_dark);
        this.mDeleteDrawableSrcResId = R.drawable.ic_backspace_dark;
    }

    private void addClickedNumber(int n) {
        if (this.mInputPointer < this.mInputSize - 1) {
            for (int i = this.mInputPointer; i >= 0; --i) {
                int[] arrn = this.mInput;
                arrn[i + 1] = arrn[i];
            }
            ++this.mInputPointer;
            this.mInput[0] = n;
        }
    }

    private boolean canAddDigits() {
        int n = this.getEnteredTime();
        boolean bl = this.mIs24HoursMode;
        boolean bl2 = false;
        boolean bl3 = false;
        if (!bl) {
            bl = bl3;
            if (n >= 1) {
                bl = bl3;
                if (n <= 12) {
                    bl = true;
                }
            }
            return bl;
        }
        bl = bl2;
        if (n >= 0) {
            bl = bl2;
            if (n <= 23) {
                n = this.mInputPointer;
                bl = bl2;
                if (n > -1) {
                    bl = bl2;
                    if (n < 2) {
                        bl = true;
                    }
                }
            }
        }
        return bl;
    }

    private void enableSetButton() {
        boolean bl;
        Button button;
        block6 : {
            block7 : {
                block8 : {
                    button = this.mSetButton;
                    if (button == null) {
                        return;
                    }
                    int n = this.mInputPointer;
                    bl = false;
                    boolean bl2 = false;
                    if (n == -1) {
                        button.setEnabled(false);
                        return;
                    }
                    if (!this.mIs24HoursMode) break block6;
                    n = this.getEnteredTime();
                    button = this.mSetButton;
                    bl = bl2;
                    if (this.mInputPointer < 2) break block7;
                    if (n < 60) break block8;
                    bl = bl2;
                    if (n <= 95) break block7;
                }
                bl = true;
            }
            button.setEnabled(bl);
            return;
        }
        if (this.mAmPmState != 0) {
            bl = true;
        }
        button.setEnabled(bl);
    }

    public static boolean get24HourMode(Context context) {
        return DateFormat.is24HourFormat((Context)context);
    }

    private int getEnteredTime() {
        int[] arrn = this.mInput;
        return arrn[3] * 1000 + arrn[2] * 100 + arrn[1] * 10 + arrn[0];
    }

    private void onLeftClicked() {
        this.getEnteredTime();
        if (!this.mIs24HoursMode) {
            if (this.canAddDigits()) {
                this.addClickedNumber(0);
                this.addClickedNumber(0);
            }
            this.mAmPmState = 2;
            return;
        }
        if (this.canAddDigits()) {
            this.addClickedNumber(0);
            this.addClickedNumber(0);
        }
    }

    private void onRightClicked() {
        this.getEnteredTime();
        if (!this.mIs24HoursMode) {
            if (this.canAddDigits()) {
                this.addClickedNumber(0);
                this.addClickedNumber(0);
            }
            this.mAmPmState = 1;
            return;
        }
        if (this.canAddDigits()) {
            this.addClickedNumber(3);
            this.addClickedNumber(0);
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
        if ((object = this.mAmPmLabel) != null) {
            object.setTextColor(this.mTextColor);
            this.mAmPmLabel.setBackgroundResource(this.mKeyBackgroundResId);
        }
        if ((object = this.mRight) != null) {
            object.setTextColor(this.mTextColor);
            this.mRight.setBackgroundResource(this.mKeyBackgroundResId);
        }
        if ((object = this.mDelete) != null) {
            object.setBackgroundResource(this.mButtonBackgroundResId);
            this.mDelete.setImageDrawable(this.getResources().getDrawable(this.mDeleteDrawableSrcResId));
        }
        if ((object = this.mEnteredTime) != null) {
            object.setTheme(this.mTheme);
        }
    }

    private void setKeyRange(int n) {
        Button button;
        for (int i = 0; i < (button = this.mNumbers).length; ++i) {
            button = button[i];
            boolean bl = i <= n;
            button.setEnabled(bl);
        }
    }

    private void showAmPm() {
        if (!this.mIs24HoursMode) {
            int n = this.mAmPmState;
            if (n != 0) {
                if (n != 1) {
                    if (n != 2) {
                        return;
                    }
                    this.mAmPmLabel.setText((CharSequence)this.mAmpm[0]);
                    return;
                }
                this.mAmPmLabel.setText((CharSequence)this.mAmpm[1]);
                return;
            }
            this.mAmPmLabel.setText((CharSequence)this.mNoAmPmLabel);
            return;
        }
        this.mAmPmLabel.setVisibility(4);
        this.mAmPmState = 3;
    }

    private void updateKeypad() {
        this.showAmPm();
        this.updateLeftRightButtons();
        this.updateTime();
        this.updateNumericKeys();
        this.enableSetButton();
        this.updateDeleteButton();
    }

    private void updateLeftRightButtons() {
        int n = this.getEnteredTime();
        if (this.mIs24HoursMode) {
            boolean bl = this.canAddDigits();
            this.mLeft.setEnabled(bl);
            this.mRight.setEnabled(bl);
            return;
        }
        if ((n <= 12 || n >= 100) && n != 0 && this.mAmPmState == 0) {
            this.mLeft.setEnabled(true);
            this.mRight.setEnabled(true);
            return;
        }
        this.mLeft.setEnabled(false);
        this.mRight.setEnabled(false);
    }

    private void updateNumericKeys() {
        int n = this.getEnteredTime();
        if (this.mIs24HoursMode) {
            int n2 = this.mInputPointer;
            if (n2 >= 3) {
                this.setKeyRange(-1);
                return;
            }
            if (n == 0) {
                if (n2 != -1 && n2 != 0 && n2 != 2) {
                    if (n2 == 1) {
                        this.setKeyRange(5);
                        return;
                    }
                    this.setKeyRange(-1);
                    return;
                }
                this.setKeyRange(9);
                return;
            }
            if (n == 1) {
                if (n2 != 0 && n2 != 2) {
                    if (n2 == 1) {
                        this.setKeyRange(5);
                        return;
                    }
                    this.setKeyRange(-1);
                    return;
                }
                this.setKeyRange(9);
                return;
            }
            if (n == 2) {
                if (n2 != 2 && n2 != 1) {
                    if (n2 == 0) {
                        this.setKeyRange(3);
                        return;
                    }
                    this.setKeyRange(-1);
                    return;
                }
                this.setKeyRange(9);
                return;
            }
            if (n <= 5) {
                this.setKeyRange(9);
                return;
            }
            if (n <= 9) {
                this.setKeyRange(5);
                return;
            }
            if (n >= 10 && n <= 15) {
                this.setKeyRange(9);
                return;
            }
            if (n >= 16 && n <= 19) {
                this.setKeyRange(5);
                return;
            }
            if (n >= 20 && n <= 25) {
                this.setKeyRange(9);
                return;
            }
            if (n >= 26 && n <= 29) {
                this.setKeyRange(-1);
                return;
            }
            if (n >= 30 && n <= 35) {
                this.setKeyRange(9);
                return;
            }
            if (n >= 36 && n <= 39) {
                this.setKeyRange(-1);
                return;
            }
            if (n >= 40 && n <= 45) {
                this.setKeyRange(9);
                return;
            }
            if (n >= 46 && n <= 49) {
                this.setKeyRange(-1);
                return;
            }
            if (n >= 50 && n <= 55) {
                this.setKeyRange(9);
                return;
            }
            if (n >= 56 && n <= 59) {
                this.setKeyRange(-1);
                return;
            }
            if (n >= 60 && n <= 65) {
                this.setKeyRange(9);
                return;
            }
            if (n >= 70 && n <= 75) {
                this.setKeyRange(9);
                return;
            }
            if (n >= 80 && n <= 85) {
                this.setKeyRange(9);
                return;
            }
            if (n >= 90 && n <= 95) {
                this.setKeyRange(9);
                return;
            }
            if (n >= 100 && n <= 105) {
                this.setKeyRange(9);
                return;
            }
            if (n >= 106 && n <= 109) {
                this.setKeyRange(-1);
                return;
            }
            if (n >= 110 && n <= 115) {
                this.setKeyRange(9);
                return;
            }
            if (n >= 116 && n <= 119) {
                this.setKeyRange(-1);
                return;
            }
            if (n >= 120 && n <= 125) {
                this.setKeyRange(9);
                return;
            }
            if (n >= 126 && n <= 129) {
                this.setKeyRange(-1);
                return;
            }
            if (n >= 130 && n <= 135) {
                this.setKeyRange(9);
                return;
            }
            if (n >= 136 && n <= 139) {
                this.setKeyRange(-1);
                return;
            }
            if (n >= 140 && n <= 145) {
                this.setKeyRange(9);
                return;
            }
            if (n >= 146 && n <= 149) {
                this.setKeyRange(-1);
                return;
            }
            if (n >= 150 && n <= 155) {
                this.setKeyRange(9);
                return;
            }
            if (n >= 156 && n <= 159) {
                this.setKeyRange(-1);
                return;
            }
            if (n >= 160 && n <= 165) {
                this.setKeyRange(9);
                return;
            }
            if (n >= 166 && n <= 169) {
                this.setKeyRange(-1);
                return;
            }
            if (n >= 170 && n <= 175) {
                this.setKeyRange(9);
                return;
            }
            if (n >= 176 && n <= 179) {
                this.setKeyRange(-1);
                return;
            }
            if (n >= 180 && n <= 185) {
                this.setKeyRange(9);
                return;
            }
            if (n >= 186 && n <= 189) {
                this.setKeyRange(-1);
                return;
            }
            if (n >= 190 && n <= 195) {
                this.setKeyRange(9);
                return;
            }
            if (n >= 196 && n <= 199) {
                this.setKeyRange(-1);
                return;
            }
            if (n >= 200 && n <= 205) {
                this.setKeyRange(9);
                return;
            }
            if (n >= 206 && n <= 209) {
                this.setKeyRange(-1);
                return;
            }
            if (n >= 210 && n <= 215) {
                this.setKeyRange(9);
                return;
            }
            if (n >= 216 && n <= 219) {
                this.setKeyRange(-1);
                return;
            }
            if (n >= 220 && n <= 225) {
                this.setKeyRange(9);
                return;
            }
            if (n >= 226 && n <= 229) {
                this.setKeyRange(-1);
                return;
            }
            if (n >= 230 && n <= 235) {
                this.setKeyRange(9);
                return;
            }
            if (n >= 236) {
                this.setKeyRange(-1);
                return;
            }
        } else {
            if (this.mAmPmState != 0) {
                this.setKeyRange(-1);
                return;
            }
            if (n == 0) {
                this.setKeyRange(9);
                this.mNumbers[0].setEnabled(false);
                return;
            }
            if (n <= 9) {
                this.setKeyRange(5);
                return;
            }
            if (n <= 95) {
                this.setKeyRange(9);
                return;
            }
            if (n >= 100 && n <= 105) {
                this.setKeyRange(9);
                return;
            }
            if (n >= 106 && n <= 109) {
                this.setKeyRange(-1);
                return;
            }
            if (n >= 110 && n <= 115) {
                this.setKeyRange(9);
                return;
            }
            if (n >= 116 && n <= 119) {
                this.setKeyRange(-1);
                return;
            }
            if (n >= 120 && n <= 125) {
                this.setKeyRange(9);
                return;
            }
            if (n >= 126) {
                this.setKeyRange(-1);
            }
        }
    }

    protected void doOnClick(View arrn) {
        Integer n = (Integer)arrn.getTag(R.id.numbers_key);
        if (n != null) {
            this.addClickedNumber(n);
        } else if (arrn == this.mDelete) {
            if (!this.mIs24HoursMode && this.mAmPmState != 0) {
                this.mAmPmState = 0;
            } else if (this.mInputPointer >= 0) {
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
        } else if (arrn == this.mRight) {
            this.onRightClicked();
        }
        this.updateKeypad();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int getHours() {
        int[] arrn = this.mInput;
        int n = arrn[3] * 10 + arrn[2];
        int n2 = 0;
        if (n == 12) {
            int n3 = this.mAmPmState;
            if (n3 == 1) return 12;
            if (n3 == 2) return 0;
            if (n3 == 3) {
                return n;
            }
        }
        if (this.mAmPmState != 1) return n2 + n;
        n2 = 12;
        return n2 + n;
    }

    protected int getLayoutId() {
        return R.layout.time_picker_view;
    }

    public int getMinutes() {
        int[] arrn = this.mInput;
        return arrn[1] * 10 + arrn[0];
    }

    public int getTime() {
        int[] arrn = this.mInput;
        return arrn[4] * 3600 + arrn[3] * 600 + arrn[2] * 60 + arrn[1] * 10 + arrn[0];
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
        String[] arrstring = this.findViewById(R.id.second);
        View view2 = this.findViewById(R.id.third);
        View view3 = this.findViewById(R.id.fourth);
        this.mEnteredTime = (TimerView)this.findViewById(R.id.timer_time_text);
        this.mDelete = imageButton = (ImageButton)this.findViewById(R.id.delete);
        imageButton.setOnClickListener((View.OnClickListener)this);
        this.mDelete.setOnLongClickListener((View.OnLongClickListener)this);
        this.mNumbers[1] = (Button)view.findViewById(R.id.key_left);
        this.mNumbers[2] = (Button)view.findViewById(R.id.key_middle);
        this.mNumbers[3] = (Button)view.findViewById(R.id.key_right);
        this.mNumbers[4] = (Button)arrstring.findViewById(R.id.key_left);
        this.mNumbers[5] = (Button)arrstring.findViewById(R.id.key_middle);
        this.mNumbers[6] = (Button)arrstring.findViewById(R.id.key_right);
        this.mNumbers[7] = (Button)view2.findViewById(R.id.key_left);
        this.mNumbers[8] = (Button)view2.findViewById(R.id.key_middle);
        this.mNumbers[9] = (Button)view2.findViewById(R.id.key_right);
        this.mLeft = (Button)view3.findViewById(R.id.key_left);
        this.mNumbers[0] = (Button)view3.findViewById(R.id.key_middle);
        this.mRight = (Button)view3.findViewById(R.id.key_right);
        this.setLeftRightEnabled(false);
        for (int i = 0; i < 10; ++i) {
            this.mNumbers[i].setOnClickListener((View.OnClickListener)this);
            this.mNumbers[i].setText((CharSequence)String.format("%d", i));
            this.mNumbers[i].setTag(R.id.numbers_key, (Object)new Integer(i));
        }
        this.updateTime();
        view = this.mContext.getResources();
        arrstring = new DateFormatSymbols().getAmPmStrings();
        this.mAmpm = arrstring;
        if (this.mIs24HoursMode) {
            this.mLeft.setText((CharSequence)view.getString(R.string.time_picker_00_label));
            this.mRight.setText((CharSequence)view.getString(R.string.time_picker_30_label));
        } else {
            this.mLeft.setText((CharSequence)arrstring[0]);
            this.mRight.setText((CharSequence)this.mAmpm[1]);
        }
        this.mLeft.setOnClickListener((View.OnClickListener)this);
        this.mRight.setOnClickListener((View.OnClickListener)this);
        this.mAmPmLabel = (TextView)this.findViewById(R.id.ampm_label);
        this.mAmPmState = 0;
        this.mDivider = this.findViewById(R.id.divider);
        this.restyleViews();
        this.updateKeypad();
    }

    public boolean onLongClick(View view) {
        view.performHapticFeedback(0);
        ImageButton imageButton = this.mDelete;
        if (view == imageButton) {
            imageButton.setPressed(false);
            this.mAmPmState = 0;
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
        this.mAmPmState = object.mAmPmState;
        this.updateKeypad();
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.mInput = this.mInput;
        savedState.mAmPmState = this.mAmPmState;
        savedState.mInputPointer = this.mInputPointer;
        return savedState;
    }

    public void reset() {
        for (int i = 0; i < this.mInputSize; ++i) {
            this.mInput[i] = 0;
        }
        this.mInputPointer = -1;
        this.updateTime();
    }

    public void restoreEntryState(Bundle arrn, String arrn2) {
        if ((arrn = arrn.getIntArray((String)arrn2)) != null && this.mInputSize == arrn.length) {
            for (int i = 0; i < this.mInputSize; ++i) {
                arrn2 = this.mInput;
                arrn2[i] = arrn[i];
                if (arrn2[i] == 0) continue;
                this.mInputPointer = i;
            }
            this.updateTime();
        }
    }

    public void saveEntryState(Bundle bundle, String string2) {
        bundle.putIntArray(string2, this.mInput);
    }

    protected void setLeftRightEnabled(boolean bl) {
        this.mLeft.setEnabled(bl);
        this.mRight.setEnabled(bl);
        if (!bl) {
            this.mLeft.setContentDescription(null);
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

    public void updateDeleteButton() {
        boolean bl = this.mInputPointer != -1;
        ImageButton imageButton = this.mDelete;
        if (imageButton != null) {
            imageButton.setEnabled(bl);
        }
    }

    protected void updateTime() {
        int n;
        int n2;
        int n3;
        int n4;
        block15 : {
            block10 : {
                block13 : {
                    block14 : {
                        block11 : {
                            block12 : {
                                n2 = -1;
                                this.getEnteredTime();
                                n = this.mInputPointer;
                                n4 = -1;
                                if (n <= -1) break block10;
                                n3 = n2;
                                if (n < 0) break block11;
                                n = this.mInput[n];
                                if (this.mIs24HoursMode && n >= 3 && n <= 9) break block12;
                                n3 = n2;
                                if (this.mIs24HoursMode) break block11;
                                n3 = n2;
                                if (n < 2) break block11;
                                n3 = n2;
                                if (n > 9) break block11;
                            }
                            n3 = -2;
                        }
                        n = this.mInputPointer;
                        n2 = n3;
                        if (n <= 0) break block13;
                        n2 = n3;
                        if (n >= 3) break block13;
                        n2 = n3;
                        if (n3 == -2) break block13;
                        int[] arrn = this.mInput;
                        n = arrn[n] * 10 + arrn[n - 1];
                        if (this.mIs24HoursMode && n >= 24 && n <= 25) break block14;
                        n2 = n3;
                        if (this.mIs24HoursMode) break block13;
                        n2 = n3;
                        if (n < 13) break block13;
                        n2 = n3;
                        if (n > 15) break block13;
                    }
                    n2 = -2;
                }
                n3 = n2;
                if (this.mInputPointer == 3) {
                    n3 = this.mInput[3];
                }
                break block15;
            }
            n3 = -1;
        }
        n2 = this.mInputPointer < 2 ? -1 : this.mInput[2];
        n = this.mInputPointer < 1 ? -1 : this.mInput[1];
        if (this.mInputPointer >= 0) {
            n4 = this.mInput[0];
        }
        this.mEnteredTime.setTime(n3, n2, n, n4);
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
            parcel.readIntArray(this.mInput);
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

