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
 *  com.codetroopers.betterpickers.R
 *  com.codetroopers.betterpickers.R$color
 *  com.codetroopers.betterpickers.R$drawable
 *  com.codetroopers.betterpickers.R$id
 *  com.codetroopers.betterpickers.R$layout
 *  com.codetroopers.betterpickers.R$styleable
 */
package com.codetroopers.betterpickers.expirationpicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
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
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.codetroopers.betterpickers.R;
import com.codetroopers.betterpickers.datepicker.DatePicker;
import com.codetroopers.betterpickers.expirationpicker.ExpirationView;
import com.codetroopers.betterpickers.widget.UnderlinePageIndicatorPicker;
import com.codetroopers.betterpickers.widget.ZeroTopPaddingTextView;
import java.util.Calendar;

public class ExpirationPicker
extends LinearLayout
implements View.OnClickListener,
View.OnLongClickListener {
    private static final int EXPIRATION_MONTH_POSITION = 0;
    private static final int EXPIRATION_YEAR_POSITION = 1;
    private static final String KEYBOARD_MONTH = "month";
    private static final String KEYBOARD_YEAR = "year";
    private static int sMonthKeyboardPosition = -1;
    private static int sYearKeyboardPosition = -1;
    private int mButtonBackgroundResId;
    private int mCheckDrawableSrcResId;
    protected final Context mContext;
    private char[] mDateFormatOrder;
    protected ImageButton mDelete;
    private int mDeleteDrawableSrcResId;
    protected View mDivider;
    protected ExpirationView mEnteredExpiration;
    private int mKeyBackgroundResId;
    protected UnderlinePageIndicatorPicker mKeyboardIndicator;
    private int mKeyboardIndicatorColor;
    protected ViewPager mKeyboardPager;
    protected KeyboardPagerAdapter mKeyboardPagerAdapter;
    protected int mMinimumYear;
    protected String[] mMonthAbbreviations;
    protected int mMonthInput = -1;
    protected final Button[] mMonths = new Button[12];
    private Button mSetButton;
    private ColorStateList mTextColor;
    private int mTheme = -1;
    private int mTitleDividerColor;
    protected int[] mYearInput = new int[4];
    protected int mYearInputPointer = -1;
    protected int mYearInputSize = 4;
    protected Button mYearLeft;
    protected final Button[] mYearNumbers = new Button[10];
    protected Button mYearRight;

    public ExpirationPicker(Context context) {
        this(context, null);
    }

    public ExpirationPicker(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        this.mDateFormatOrder = DateFormat.getDateFormatOrder((Context)context);
        this.mMonthAbbreviations = DatePicker.makeLocalizedMonthAbbreviations();
        ((LayoutInflater)context.getSystemService("layout_inflater")).inflate(this.getLayoutId(), (ViewGroup)this);
        this.mTextColor = this.getResources().getColorStateList(R.color.dialog_text_color_holo_dark);
        this.mKeyBackgroundResId = R.drawable.key_background_dark;
        this.mButtonBackgroundResId = R.drawable.button_background_dark;
        this.mTitleDividerColor = this.getResources().getColor(R.color.default_divider_color_dark);
        this.mKeyboardIndicatorColor = this.getResources().getColor(R.color.default_keyboard_indicator_color_dark);
        this.mDeleteDrawableSrcResId = R.drawable.ic_backspace_dark;
        this.mCheckDrawableSrcResId = R.drawable.ic_check_dark;
        this.mMinimumYear = Calendar.getInstance().get(1);
    }

    private void addClickedYearNumber(int n) {
        Object object;
        if (this.mYearInputPointer < this.mYearInputSize - 1) {
            for (int i = this.mYearInputPointer; i >= 0; --i) {
                object = this.mYearInput;
                object[i + 1] = object[i];
            }
            ++this.mYearInputPointer;
            this.mYearInput[0] = n;
        }
        if (this.mKeyboardPager.getCurrentItem() < 2) {
            object = this.mKeyboardPager;
            object.setCurrentItem(object.getCurrentItem() + 1, true);
        }
    }

    private void enableSetButton() {
        Button button = this.mSetButton;
        if (button == null) {
            return;
        }
        boolean bl = this.getYear() >= this.mMinimumYear && this.getMonthOfYear() > 0;
        button.setEnabled(bl);
    }

    private void restyleViews() {
        int n;
        Button button;
        Object object = this.mMonths;
        int n2 = object.length;
        int n3 = 0;
        for (n = 0; n < n2; ++n) {
            button = object[n];
            if (button == null) continue;
            button.setTextColor(this.mTextColor);
            button.setBackgroundResource(this.mKeyBackgroundResId);
        }
        object = this.mYearNumbers;
        n2 = object.length;
        for (n = n3; n < n2; ++n) {
            button = object[n];
            if (button == null) continue;
            button.setTextColor(this.mTextColor);
            button.setBackgroundResource(this.mKeyBackgroundResId);
        }
        object = this.mKeyboardIndicator;
        if (object != null) {
            object.setSelectedColor(this.mKeyboardIndicatorColor);
        }
        if ((object = this.mDivider) != null) {
            object.setBackgroundColor(this.mTitleDividerColor);
        }
        if ((object = this.mDelete) != null) {
            object.setBackgroundResource(this.mButtonBackgroundResId);
            this.mDelete.setImageDrawable(this.getResources().getDrawable(this.mDeleteDrawableSrcResId));
        }
        if ((object = this.mYearLeft) != null) {
            object.setTextColor(this.mTextColor);
            this.mYearLeft.setBackgroundResource(this.mKeyBackgroundResId);
        }
        if ((object = this.mYearRight) != null) {
            object.setTextColor(this.mTextColor);
            this.mYearRight.setBackgroundResource(this.mKeyBackgroundResId);
        }
        if ((object = this.mEnteredExpiration) != null) {
            object.setTheme(this.mTheme);
        }
    }

    private void setYearKeyRange(int n) {
        Button button;
        for (int i = 0; i < (button = this.mYearNumbers).length; ++i) {
            if (button[i] == null) continue;
            button = button[i];
            boolean bl = i <= n;
            button.setEnabled(bl);
        }
    }

    private void setYearMinKeyRange(int n) {
        Button button;
        for (int i = 0; i < (button = this.mYearNumbers).length; ++i) {
            if (button[i] == null) continue;
            button = button[i];
            boolean bl = i >= n;
            button.setEnabled(bl);
        }
    }

    private void updateKeypad() {
        this.updateExpiration();
        this.enableSetButton();
        this.updateDeleteButton();
        this.updateMonthKeys();
        this.updateYearKeys();
    }

    private void updateMonthKeys() {
        Button[] arrbutton;
        for (int i = 0; i < (arrbutton = this.mMonths).length; ++i) {
            if (arrbutton[i] == null) continue;
            arrbutton[i].setEnabled(true);
        }
    }

    private void updateYearKeys() {
        int n = this.mYearInputPointer;
        if (n == 1) {
            this.setYearMinKeyRange(this.mMinimumYear % 100 / 10);
            return;
        }
        if (n == 2) {
            this.setYearMinKeyRange(Math.max(0, this.mMinimumYear % 100 - this.mYearInput[0] * 10));
            return;
        }
        if (n == 3) {
            this.setYearKeyRange(-1);
        }
    }

    protected void doOnClick(View object) {
        if (object == this.mDelete) {
            int n = this.mKeyboardPager.getCurrentItem();
            if (n != 0) {
                if (n == 1) {
                    if (this.mYearInputPointer >= 2) {
                        int n2;
                        for (n = 0; n < (n2 = this.mYearInputPointer); ++n) {
                            object = this.mYearInput;
                            object[n] = object[n + 1];
                        }
                        this.mYearInput[n2] = 0;
                        this.mYearInputPointer = n2 - 1;
                    } else if (this.mKeyboardPager.getCurrentItem() > 0) {
                        object = this.mKeyboardPager;
                        object.setCurrentItem(object.getCurrentItem() - 1, true);
                    }
                }
            } else if (this.mMonthInput != -1) {
                this.mMonthInput = -1;
            }
        } else if (object == this.mEnteredExpiration.getMonth()) {
            this.mKeyboardPager.setCurrentItem(sMonthKeyboardPosition);
        } else if (object == this.mEnteredExpiration.getYear()) {
            this.mKeyboardPager.setCurrentItem(sYearKeyboardPosition);
        } else if (object.getTag(R.id.date_keyboard).equals("month")) {
            this.mMonthInput = (Integer)object.getTag(R.id.date_month_int);
            if (this.mKeyboardPager.getCurrentItem() < 2) {
                object = this.mKeyboardPager;
                object.setCurrentItem(object.getCurrentItem() + 1, true);
            }
        } else if (object.getTag(R.id.date_keyboard).equals("year")) {
            this.addClickedYearNumber((Integer)object.getTag(R.id.numbers_key));
        }
        this.updateKeypad();
    }

    protected int getLayoutId() {
        return R.layout.expiration_picker_view;
    }

    public int getMonthOfYear() {
        return this.mMonthInput;
    }

    public int getYear() {
        int[] arrn = this.mYearInput;
        return arrn[3] * 1000 + arrn[2] * 100 + arrn[1] * 10 + arrn[0];
    }

    public void onClick(View view) {
        view.performHapticFeedback(1);
        this.doOnClick(view);
        this.updateDeleteButton();
    }

    protected void onFinishInflate() {
        Object object;
        super.onFinishInflate();
        this.mDivider = this.findViewById(R.id.divider);
        for (int i = 0; i < (object = this.mYearInput).length; ++i) {
            object[i] = 0;
        }
        this.mKeyboardIndicator = (UnderlinePageIndicatorPicker)this.findViewById(R.id.keyboard_indicator);
        this.mKeyboardPager = object = (ViewPager)this.findViewById(R.id.keyboard_pager);
        object.setOffscreenPageLimit(2);
        this.mKeyboardPagerAdapter = object = new KeyboardPagerAdapter((LayoutInflater)this.mContext.getSystemService("layout_inflater"));
        this.mKeyboardPager.setAdapter((PagerAdapter)object);
        this.mKeyboardIndicator.setViewPager(this.mKeyboardPager);
        this.mKeyboardPager.setCurrentItem(0);
        this.mEnteredExpiration = object = (ExpirationView)this.findViewById(R.id.date_text);
        object.setTheme(this.mTheme);
        this.mEnteredExpiration.setUnderlinePage(this.mKeyboardIndicator);
        this.mEnteredExpiration.setOnClick(this);
        this.mDelete = object = (ImageButton)this.findViewById(R.id.delete);
        object.setOnClickListener((View.OnClickListener)this);
        this.mDelete.setOnLongClickListener((View.OnLongClickListener)this);
        this.addClickedYearNumber(this.mMinimumYear / 1000);
        this.addClickedYearNumber(this.mMinimumYear % 1000 / 100);
        object = this.mKeyboardPager;
        object.setCurrentItem(object.getCurrentItem() - 1, true);
        this.setLeftRightEnabled();
        this.updateExpiration();
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

    protected void onRestoreInstanceState(Parcelable object) {
        if (!(object instanceof SavedState)) {
            super.onRestoreInstanceState((Parcelable)object);
            return;
        }
        object = (SavedState)((Object)object);
        super.onRestoreInstanceState(object.getSuperState());
        this.mYearInputPointer = object.mYearInputPointer;
        int[] arrn = object.mYearInput;
        this.mYearInput = arrn;
        if (arrn == null) {
            this.mYearInput = new int[this.mYearInputSize];
            this.mYearInputPointer = -1;
        }
        this.mMonthInput = object.mMonthInput;
        this.updateKeypad();
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.mMonthInput = this.mMonthInput;
        savedState.mYearInput = this.mYearInput;
        savedState.mYearInputPointer = this.mYearInputPointer;
        return savedState;
    }

    public void reset() {
        for (int i = 0; i < this.mYearInputSize; ++i) {
            this.mYearInput[i] = 0;
        }
        this.mYearInputPointer = -1;
        this.mMonthInput = -1;
        this.mKeyboardPager.setCurrentItem(0, true);
        this.updateExpiration();
    }

    public void setExpiration(int n, int n2) {
        if (n != 0 && n < this.mMinimumYear) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Years past the minimum set year are not allowed. Specify ");
            stringBuilder.append(this.mMinimumYear);
            stringBuilder.append(" or above.");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        this.mMonthInput = n2;
        int[] arrn = this.mYearInput;
        arrn[3] = n / 1000;
        arrn[2] = n % 1000 / 100;
        arrn[1] = n % 100 / 10;
        arrn[0] = n % 10;
        if (n >= 1000) {
            this.mYearInputPointer = 3;
        } else if (n >= 100) {
            this.mYearInputPointer = 2;
        } else if (n >= 10) {
            this.mYearInputPointer = 1;
        } else if (n > 0) {
            this.mYearInputPointer = 0;
        }
        for (int i = 0; i < (arrn = this.mDateFormatOrder).length; ++i) {
            int n3 = arrn[i];
            if (n3 == 77 && n2 == -1) {
                this.mKeyboardPager.setCurrentItem(i, true);
                break;
            }
            if (n3 != 121 || n > 0) continue;
            this.mKeyboardPager.setCurrentItem(i, true);
            break;
        }
        this.updateKeypad();
    }

    protected void setLeftRightEnabled() {
        Button button = this.mYearLeft;
        if (button != null) {
            button.setEnabled(false);
        }
        if ((button = this.mYearRight) != null) {
            button.setEnabled(false);
        }
    }

    public void setMinYear(int n) {
        this.mMinimumYear = n;
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
            this.mCheckDrawableSrcResId = typedArray.getResourceId(R.styleable.BetterPickersDialogFragment_bpCheckIcon, this.mCheckDrawableSrcResId);
            this.mTitleDividerColor = typedArray.getColor(R.styleable.BetterPickersDialogFragment_bpTitleDividerColor, this.mTitleDividerColor);
            this.mKeyboardIndicatorColor = typedArray.getColor(R.styleable.BetterPickersDialogFragment_bpKeyboardIndicatorColor, this.mKeyboardIndicatorColor);
            this.mDeleteDrawableSrcResId = typedArray.getResourceId(R.styleable.BetterPickersDialogFragment_bpDeleteIcon, this.mDeleteDrawableSrcResId);
        }
        this.restyleViews();
    }

    public void updateDeleteButton() {
        boolean bl = this.mMonthInput != -1 || this.mYearInputPointer != -1;
        ImageButton imageButton = this.mDelete;
        if (imageButton != null) {
            imageButton.setEnabled(bl);
        }
    }

    protected void updateExpiration() {
        int n = this.mMonthInput;
        String string2 = n < 0 ? "" : String.format("%02d", n);
        this.mEnteredExpiration.setExpiration(string2, this.getYear());
    }

    private class KeyboardPagerAdapter
    extends PagerAdapter {
        private LayoutInflater mInflater;

        public KeyboardPagerAdapter(LayoutInflater layoutInflater) {
            this.mInflater = layoutInflater;
        }

        @Override
        public void destroyItem(ViewGroup viewGroup, int n, Object object) {
            viewGroup.removeView((View)object);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object instantiateItem(ViewGroup viewGroup, int n) {
            View view;
            ExpirationPicker.this.mContext.getResources();
            if (n == 0) {
                sMonthKeyboardPosition = n;
                view = this.mInflater.inflate(R.layout.keyboard_text, viewGroup, false);
                View view2 = view.findViewById(R.id.first);
                View view3 = view.findViewById(R.id.second);
                View view4 = view.findViewById(R.id.third);
                View view5 = view.findViewById(R.id.fourth);
                ExpirationPicker.this.mMonths[0] = (Button)view2.findViewById(R.id.key_left);
                ExpirationPicker.this.mMonths[1] = (Button)view2.findViewById(R.id.key_middle);
                ExpirationPicker.this.mMonths[2] = (Button)view2.findViewById(R.id.key_right);
                ExpirationPicker.this.mMonths[3] = (Button)view3.findViewById(R.id.key_left);
                ExpirationPicker.this.mMonths[4] = (Button)view3.findViewById(R.id.key_middle);
                ExpirationPicker.this.mMonths[5] = (Button)view3.findViewById(R.id.key_right);
                ExpirationPicker.this.mMonths[6] = (Button)view4.findViewById(R.id.key_left);
                ExpirationPicker.this.mMonths[7] = (Button)view4.findViewById(R.id.key_middle);
                ExpirationPicker.this.mMonths[8] = (Button)view4.findViewById(R.id.key_right);
                ExpirationPicker.this.mMonths[9] = (Button)view5.findViewById(R.id.key_left);
                ExpirationPicker.this.mMonths[10] = (Button)view5.findViewById(R.id.key_middle);
                ExpirationPicker.this.mMonths[11] = (Button)view5.findViewById(R.id.key_right);
                for (n = 0; n < 12; ++n) {
                    ExpirationPicker.this.mMonths[n].setOnClickListener((View.OnClickListener)ExpirationPicker.this);
                    ExpirationPicker.this.mMonths[n].setText((CharSequence)String.format("%02d", n + 1));
                    ExpirationPicker.this.mMonths[n].setTextColor(ExpirationPicker.this.mTextColor);
                    ExpirationPicker.this.mMonths[n].setBackgroundResource(ExpirationPicker.this.mKeyBackgroundResId);
                    ExpirationPicker.this.mMonths[n].setTag(R.id.date_keyboard, (Object)"month");
                    ExpirationPicker.this.mMonths[n].setTag(R.id.date_month_int, (Object)(n + 1));
                }
            } else if (n == 1) {
                sYearKeyboardPosition = n;
                view = this.mInflater.inflate(R.layout.keyboard, viewGroup, false);
                View view6 = view.findViewById(R.id.first);
                View view7 = view.findViewById(R.id.second);
                View view8 = view.findViewById(R.id.third);
                View view9 = view.findViewById(R.id.fourth);
                ExpirationPicker.this.mYearNumbers[1] = (Button)view6.findViewById(R.id.key_left);
                ExpirationPicker.this.mYearNumbers[2] = (Button)view6.findViewById(R.id.key_middle);
                ExpirationPicker.this.mYearNumbers[3] = (Button)view6.findViewById(R.id.key_right);
                ExpirationPicker.this.mYearNumbers[4] = (Button)view7.findViewById(R.id.key_left);
                ExpirationPicker.this.mYearNumbers[5] = (Button)view7.findViewById(R.id.key_middle);
                ExpirationPicker.this.mYearNumbers[6] = (Button)view7.findViewById(R.id.key_right);
                ExpirationPicker.this.mYearNumbers[7] = (Button)view8.findViewById(R.id.key_left);
                ExpirationPicker.this.mYearNumbers[8] = (Button)view8.findViewById(R.id.key_middle);
                ExpirationPicker.this.mYearNumbers[9] = (Button)view8.findViewById(R.id.key_right);
                ExpirationPicker.this.mYearLeft = (Button)view9.findViewById(R.id.key_left);
                ExpirationPicker.this.mYearLeft.setTextColor(ExpirationPicker.this.mTextColor);
                ExpirationPicker.this.mYearLeft.setBackgroundResource(ExpirationPicker.this.mKeyBackgroundResId);
                ExpirationPicker.this.mYearNumbers[0] = (Button)view9.findViewById(R.id.key_middle);
                ExpirationPicker.this.mYearRight = (Button)view9.findViewById(R.id.key_right);
                ExpirationPicker.this.mYearRight.setTextColor(ExpirationPicker.this.mTextColor);
                ExpirationPicker.this.mYearRight.setBackgroundResource(ExpirationPicker.this.mKeyBackgroundResId);
                for (n = 0; n < 10; ++n) {
                    ExpirationPicker.this.mYearNumbers[n].setOnClickListener((View.OnClickListener)ExpirationPicker.this);
                    ExpirationPicker.this.mYearNumbers[n].setText((CharSequence)String.format("%d", n));
                    ExpirationPicker.this.mYearNumbers[n].setTextColor(ExpirationPicker.this.mTextColor);
                    ExpirationPicker.this.mYearNumbers[n].setBackgroundResource(ExpirationPicker.this.mKeyBackgroundResId);
                    ExpirationPicker.this.mYearNumbers[n].setTag(R.id.date_keyboard, (Object)"year");
                    ExpirationPicker.this.mYearNumbers[n].setTag(R.id.numbers_key, (Object)n);
                }
            } else {
                view = new View(ExpirationPicker.this.mContext);
            }
            ExpirationPicker.this.setLeftRightEnabled();
            ExpirationPicker.this.updateExpiration();
            ExpirationPicker.this.updateKeypad();
            viewGroup.addView(view, 0);
            return view;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            if (view == object) {
                return true;
            }
            return false;
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
        int mMonthInput;
        int[] mYearInput;
        int mYearInputPointer;

        private SavedState(Parcel parcel) {
            super(parcel);
            this.mYearInputPointer = parcel.readInt();
            parcel.readIntArray(this.mYearInput);
            this.mMonthInput = parcel.readInt();
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.mYearInputPointer);
            parcel.writeIntArray(this.mYearInput);
            parcel.writeInt(this.mMonthInput);
        }

    }

}

