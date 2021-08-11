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
 *  android.widget.TextView
 *  com.codetroopers.betterpickers.R
 *  com.codetroopers.betterpickers.R$color
 *  com.codetroopers.betterpickers.R$drawable
 *  com.codetroopers.betterpickers.R$id
 *  com.codetroopers.betterpickers.R$layout
 *  com.codetroopers.betterpickers.R$string
 *  com.codetroopers.betterpickers.R$styleable
 */
package com.codetroopers.betterpickers.datepicker;

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
import android.widget.TextView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.codetroopers.betterpickers.R;
import com.codetroopers.betterpickers.datepicker.DateView;
import com.codetroopers.betterpickers.widget.UnderlinePageIndicatorPicker;
import com.codetroopers.betterpickers.widget.ZeroTopPaddingTextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DatePicker
extends LinearLayout
implements View.OnClickListener,
View.OnLongClickListener {
    private static final String KEYBOARD_DATE = "date";
    private static final String KEYBOARD_MONTH = "month";
    private static final String KEYBOARD_YEAR = "year";
    private static int sDateKeyboardPosition;
    private static int sMonthKeyboardPosition;
    private static int sYearKeyboardPosition;
    private int mButtonBackgroundResId;
    private int mCheckDrawableSrcResId;
    protected final Context mContext;
    private char[] mDateFormatOrder;
    protected int[] mDateInput = new int[2];
    protected int mDateInputPointer = -1;
    protected int mDateInputSize = 2;
    protected Button mDateLeft;
    protected final Button[] mDateNumbers = new Button[10];
    protected ImageButton mDateRight;
    protected ImageButton mDelete;
    private int mDeleteDrawableSrcResId;
    protected View mDivider;
    protected DateView mEnteredDate;
    private int mKeyBackgroundResId;
    protected UnderlinePageIndicatorPicker mKeyboardIndicator;
    private int mKeyboardIndicatorColor;
    protected ViewPager mKeyboardPager;
    protected KeyboardPagerAdapter mKeyboardPagerAdapter;
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
    private boolean mYearOptional = false;
    protected Button mYearRight;

    static {
        sMonthKeyboardPosition = -1;
        sDateKeyboardPosition = -1;
        sYearKeyboardPosition = -1;
    }

    public DatePicker(Context context) {
        this(context, null);
    }

    public DatePicker(Context context, AttributeSet attributeSet) {
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
    }

    private void addClickedDateNumber(int n) {
        Object object;
        if (this.mDateInputPointer < this.mDateInputSize - 1) {
            for (int i = this.mDateInputPointer; i >= 0; --i) {
                object = this.mDateInput;
                object[i + 1] = object[i];
            }
            ++this.mDateInputPointer;
            this.mDateInput[0] = n;
        }
        if ((this.getDayOfMonth() >= 4 || this.getMonthOfYear() == 1 && this.getDayOfMonth() >= 3) && this.mKeyboardPager.getCurrentItem() < 2) {
            object = this.mKeyboardPager;
            object.setCurrentItem(object.getCurrentItem() + 1, true);
        }
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
        if (this.getYear() >= 1000 && this.mKeyboardPager.getCurrentItem() < 2) {
            object = this.mKeyboardPager;
            object.setCurrentItem(object.getCurrentItem() + 1, true);
        }
    }

    private boolean canGoToYear() {
        if (this.getDayOfMonth() > 0) {
            return true;
        }
        return false;
    }

    private void enableSetButton() {
        Button button = this.mSetButton;
        if (button == null) {
            return;
        }
        boolean bl = this.getDayOfMonth() > 0 && (this.mYearOptional || this.getYear() > 0) && this.getMonthOfYear() >= 0;
        button.setEnabled(bl);
    }

    public static String[] makeLocalizedMonthAbbreviations() {
        return DatePicker.makeLocalizedMonthAbbreviations(Locale.getDefault());
    }

    public static String[] makeLocalizedMonthAbbreviations(Locale cloneable) {
        int n = cloneable != null ? 1 : 0;
        SimpleDateFormat simpleDateFormat = n != 0 ? new SimpleDateFormat("MMM", (Locale)cloneable) : new SimpleDateFormat("MMM");
        cloneable = n != 0 ? new GregorianCalendar((Locale)cloneable) : new GregorianCalendar();
        cloneable.set(1, 0);
        cloneable.set(5, 1);
        cloneable.set(11, 0);
        cloneable.set(12, 0);
        cloneable.set(13, 0);
        cloneable.set(14, 0);
        String[] arrstring = new String[12];
        for (n = 0; n < arrstring.length; ++n) {
            cloneable.set(2, n);
            arrstring[n] = simpleDateFormat.format(cloneable.getTime()).toUpperCase();
        }
        return arrstring;
    }

    private void onDateRightClicked() {
        if (this.mKeyboardPager.getCurrentItem() < 2) {
            ViewPager viewPager = this.mKeyboardPager;
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
        }
    }

    private void restyleViews() {
        int n;
        Button button2;
        Object object = this.mMonths;
        int n2 = object.length;
        int n3 = 0;
        for (n = 0; n < n2; ++n) {
            button2 = object[n];
            if (button2 == null) continue;
            button2.setTextColor(this.mTextColor);
            button2.setBackgroundResource(this.mKeyBackgroundResId);
        }
        for (Button button2 : this.mDateNumbers) {
            if (button2 == null) continue;
            button2.setTextColor(this.mTextColor);
            button2.setBackgroundResource(this.mKeyBackgroundResId);
        }
        object = this.mYearNumbers;
        n2 = object.length;
        for (n = n3; n < n2; ++n) {
            button2 = object[n];
            if (button2 == null) continue;
            button2.setTextColor(this.mTextColor);
            button2.setBackgroundResource(this.mKeyBackgroundResId);
        }
        object = this.mKeyboardIndicator;
        if (object != null) {
            object.setSelectedColor(this.mKeyboardIndicatorColor);
        }
        if ((object = this.mDivider) != null) {
            object.setBackgroundColor(this.mTitleDividerColor);
        }
        if ((object = this.mDateLeft) != null) {
            object.setTextColor(this.mTextColor);
            this.mDateLeft.setBackgroundResource(this.mKeyBackgroundResId);
        }
        if ((object = this.mDateRight) != null) {
            object.setBackgroundResource(this.mKeyBackgroundResId);
            this.mDateRight.setImageDrawable(this.getResources().getDrawable(this.mCheckDrawableSrcResId));
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
        if ((object = this.mEnteredDate) != null) {
            object.setTheme(this.mTheme);
        }
    }

    private void setDateKeyRange(int n) {
        Button button;
        for (int i = 0; i < (button = this.mDateNumbers).length; ++i) {
            if (button[i] == null) continue;
            button = button[i];
            boolean bl = i <= n;
            button.setEnabled(bl);
        }
    }

    private void setDateMinKeyRange(int n) {
        Button button;
        for (int i = 0; i < (button = this.mDateNumbers).length; ++i) {
            if (button[i] == null) continue;
            button = button[i];
            boolean bl = i >= n;
            button.setEnabled(bl);
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

    private void updateDateKeys() {
        int n = this.getDayOfMonth();
        if (n >= 4) {
            this.setDateKeyRange(-1);
            return;
        }
        if (n >= 3) {
            n = this.mMonthInput;
            if (n == 1) {
                this.setDateKeyRange(-1);
                return;
            }
            if (n != 3 && n != 5 && n != 8 && n != 10) {
                this.setDateKeyRange(1);
                return;
            }
            this.setDateKeyRange(0);
            return;
        }
        if (n >= 2) {
            this.setDateKeyRange(9);
            return;
        }
        if (n >= 1) {
            this.setDateKeyRange(9);
            return;
        }
        this.setDateMinKeyRange(1);
    }

    private void updateKeypad() {
        this.updateLeftRightButtons();
        this.updateDate();
        this.enableSetButton();
        this.updateDeleteButton();
        this.updateMonthKeys();
        this.updateDateKeys();
        this.updateYearKeys();
    }

    private void updateLeftRightButtons() {
        ImageButton imageButton = this.mDateRight;
        if (imageButton != null) {
            imageButton.setEnabled(this.canGoToYear());
        }
    }

    private void updateMonthKeys() {
        Button[] arrbutton;
        int n = this.getDayOfMonth();
        for (int i = 0; i < (arrbutton = this.mMonths).length; ++i) {
            if (arrbutton[i] == null) continue;
            arrbutton[i].setEnabled(true);
        }
        if (n > 29 && arrbutton[1] != null) {
            arrbutton[1].setEnabled(false);
        }
        if (n > 30) {
            arrbutton = this.mMonths;
            if (arrbutton[3] != null) {
                arrbutton[3].setEnabled(false);
            }
            if ((arrbutton = this.mMonths)[5] != null) {
                arrbutton[5].setEnabled(false);
            }
            if ((arrbutton = this.mMonths)[8] != null) {
                arrbutton[8].setEnabled(false);
            }
            if ((arrbutton = this.mMonths)[10] != null) {
                arrbutton[10].setEnabled(false);
            }
        }
    }

    private void updateYearKeys() {
        int n = this.getYear();
        if (n >= 1000) {
            this.setYearKeyRange(-1);
            return;
        }
        if (n >= 1) {
            this.setYearKeyRange(9);
            return;
        }
        this.setYearMinKeyRange(1);
    }

    protected void doOnClick(View object) {
        if (object == this.mDelete) {
            int n = this.mDateFormatOrder[this.mKeyboardPager.getCurrentItem()];
            if (n != 77) {
                if (n != 100) {
                    if (n == 121) {
                        if (this.mYearInputPointer >= 0) {
                            int n2;
                            for (n = 0; n < (n2 = this.mYearInputPointer); ++n) {
                                object = this.mYearInput;
                                object[n] = object[n + '\u0001'];
                            }
                            this.mYearInput[n2] = 0;
                            this.mYearInputPointer = n2 - 1;
                        } else if (this.mKeyboardPager.getCurrentItem() > 0) {
                            object = this.mKeyboardPager;
                            object.setCurrentItem(object.getCurrentItem() - 1, true);
                        }
                    }
                } else if (this.mDateInputPointer >= 0) {
                    int n3;
                    for (n = 0; n < (n3 = this.mDateInputPointer); ++n) {
                        object = this.mDateInput;
                        object[n] = object[n + '\u0001'];
                    }
                    this.mDateInput[n3] = 0;
                    this.mDateInputPointer = n3 - 1;
                } else if (this.mKeyboardPager.getCurrentItem() > 0) {
                    object = this.mKeyboardPager;
                    object.setCurrentItem(object.getCurrentItem() - 1, true);
                }
            } else if (this.mMonthInput != -1) {
                this.mMonthInput = -1;
            }
        } else if (object == this.mDateRight) {
            this.onDateRightClicked();
        } else if (object == this.mEnteredDate.getDate()) {
            this.mKeyboardPager.setCurrentItem(sDateKeyboardPosition);
        } else if (object == this.mEnteredDate.getMonth()) {
            this.mKeyboardPager.setCurrentItem(sMonthKeyboardPosition);
        } else if (object == this.mEnteredDate.getYear()) {
            this.mKeyboardPager.setCurrentItem(sYearKeyboardPosition);
        } else if (object.getTag(R.id.date_keyboard).equals("month")) {
            this.mMonthInput = (Integer)object.getTag(R.id.date_month_int);
            if (this.mKeyboardPager.getCurrentItem() < 2) {
                object = this.mKeyboardPager;
                object.setCurrentItem(object.getCurrentItem() + 1, true);
            }
        } else if (object.getTag(R.id.date_keyboard).equals("date")) {
            this.addClickedDateNumber((Integer)object.getTag(R.id.numbers_key));
        } else if (object.getTag(R.id.date_keyboard).equals("year")) {
            this.addClickedYearNumber((Integer)object.getTag(R.id.numbers_key));
        }
        this.updateKeypad();
    }

    public int getDayOfMonth() {
        int[] arrn = this.mDateInput;
        return arrn[1] * 10 + arrn[0];
    }

    protected int getLayoutId() {
        return R.layout.date_picker_view;
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
        int n;
        super.onFinishInflate();
        this.mDivider = this.findViewById(R.id.divider);
        for (n = 0; n < (object = this.mDateInput).length; ++n) {
            object[n] = 0;
        }
        for (n = 0; n < (object = this.mYearInput).length; ++n) {
            object[n] = 0;
        }
        this.mKeyboardIndicator = (UnderlinePageIndicatorPicker)this.findViewById(R.id.keyboard_indicator);
        this.mKeyboardPager = object = (ViewPager)this.findViewById(R.id.keyboard_pager);
        object.setOffscreenPageLimit(2);
        this.mKeyboardPagerAdapter = object = new KeyboardPagerAdapter((LayoutInflater)this.mContext.getSystemService("layout_inflater"));
        this.mKeyboardPager.setAdapter((PagerAdapter)object);
        this.mKeyboardIndicator.setViewPager(this.mKeyboardPager);
        this.mKeyboardPager.setCurrentItem(0);
        this.mEnteredDate = object = (DateView)this.findViewById(R.id.date_text);
        object.setTheme(this.mTheme);
        this.mEnteredDate.setUnderlinePage(this.mKeyboardIndicator);
        this.mEnteredDate.setOnClick(this);
        this.mDelete = object = (ImageButton)this.findViewById(R.id.delete);
        object.setOnClickListener((View.OnClickListener)this);
        this.mDelete.setOnLongClickListener((View.OnLongClickListener)this);
        this.setLeftRightEnabled();
        this.updateDate();
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
        this.mDateInputPointer = object.mDateInputPointer;
        this.mYearInputPointer = object.mYearInputPointer;
        this.mDateInput = object.mDateInput;
        this.mYearInput = object.mYearInput;
        if (this.mDateInput == null) {
            this.mDateInput = new int[this.mDateInputSize];
            this.mDateInputPointer = -1;
        }
        if (this.mYearInput == null) {
            this.mYearInput = new int[this.mYearInputSize];
            this.mYearInputPointer = -1;
        }
        this.mMonthInput = object.mMonthInput;
        this.updateKeypad();
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.mMonthInput = this.mMonthInput;
        savedState.mDateInput = this.mDateInput;
        savedState.mDateInputPointer = this.mDateInputPointer;
        savedState.mYearInput = this.mYearInput;
        savedState.mYearInputPointer = this.mYearInputPointer;
        return savedState;
    }

    public void reset() {
        int n;
        for (n = 0; n < this.mDateInputSize; ++n) {
            this.mDateInput[n] = 0;
        }
        for (n = 0; n < this.mYearInputSize; ++n) {
            this.mYearInput[n] = 0;
        }
        this.mDateInputPointer = -1;
        this.mYearInputPointer = -1;
        this.mMonthInput = -1;
        this.mKeyboardPager.setCurrentItem(0, true);
        this.updateDate();
    }

    public void setDate(int n, int n2, int n3) {
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
        arrn = this.mDateInput;
        arrn[1] = n3 / 10;
        arrn[0] = n3 % 10;
        if (n3 >= 10) {
            this.mDateInputPointer = 1;
        } else if (n3 > 0) {
            this.mDateInputPointer = 0;
        }
        for (int i = 0; i < (arrn = this.mDateFormatOrder).length; ++i) {
            int n4 = arrn[i];
            if (n4 == 77 && n2 == -1) {
                this.mKeyboardPager.setCurrentItem(i, true);
                break;
            }
            if (n4 == 100 && n3 <= 0) {
                this.mKeyboardPager.setCurrentItem(i, true);
                break;
            }
            if (n4 != 121 || n > 0) continue;
            this.mKeyboardPager.setCurrentItem(i, true);
            break;
        }
        this.updateKeypad();
    }

    protected void setLeftRightEnabled() {
        Button button = this.mDateLeft;
        if (button != null) {
            button.setEnabled(false);
        }
        if ((button = this.mDateRight) != null) {
            button.setEnabled(this.canGoToYear());
        }
        if ((button = this.mYearLeft) != null) {
            button.setEnabled(false);
        }
        if ((button = this.mYearRight) != null) {
            button.setEnabled(false);
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
            this.mCheckDrawableSrcResId = typedArray.getResourceId(R.styleable.BetterPickersDialogFragment_bpCheckIcon, this.mCheckDrawableSrcResId);
            this.mTitleDividerColor = typedArray.getColor(R.styleable.BetterPickersDialogFragment_bpTitleDividerColor, this.mTitleDividerColor);
            this.mKeyboardIndicatorColor = typedArray.getColor(R.styleable.BetterPickersDialogFragment_bpKeyboardIndicatorColor, this.mKeyboardIndicatorColor);
            this.mDeleteDrawableSrcResId = typedArray.getResourceId(R.styleable.BetterPickersDialogFragment_bpDeleteIcon, this.mDeleteDrawableSrcResId);
        }
        this.restyleViews();
    }

    public void setYearOptional(boolean bl) {
        this.mYearOptional = bl;
    }

    protected void updateDate() {
        int n = this.mMonthInput;
        String string2 = n < 0 ? "" : this.mMonthAbbreviations[n];
        this.mEnteredDate.setDate(string2, this.getDayOfMonth(), this.getYear());
    }

    public void updateDeleteButton() {
        boolean bl = this.mMonthInput != -1 || this.mDateInputPointer != -1 || this.mYearInputPointer != -1;
        ImageButton imageButton = this.mDelete;
        if (imageButton != null) {
            imageButton.setEnabled(bl);
        }
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
            return 3;
        }

        @Override
        public Object instantiateItem(ViewGroup viewGroup, int n) {
            View view;
            Resources resources = DatePicker.this.mContext.getResources();
            if (DatePicker.this.mDateFormatOrder[n] == 'M') {
                sMonthKeyboardPosition = n;
                view = this.mInflater.inflate(R.layout.keyboard_text_with_header, viewGroup, false);
                resources = view.findViewById(R.id.first);
                View view2 = view.findViewById(R.id.second);
                View view3 = view.findViewById(R.id.third);
                View view4 = view.findViewById(R.id.fourth);
                ((TextView)view.findViewById(R.id.header)).setText(R.string.month_c);
                DatePicker.this.mMonths[0] = (Button)resources.findViewById(R.id.key_left);
                DatePicker.this.mMonths[1] = (Button)resources.findViewById(R.id.key_middle);
                DatePicker.this.mMonths[2] = (Button)resources.findViewById(R.id.key_right);
                DatePicker.this.mMonths[3] = (Button)view2.findViewById(R.id.key_left);
                DatePicker.this.mMonths[4] = (Button)view2.findViewById(R.id.key_middle);
                DatePicker.this.mMonths[5] = (Button)view2.findViewById(R.id.key_right);
                DatePicker.this.mMonths[6] = (Button)view3.findViewById(R.id.key_left);
                DatePicker.this.mMonths[7] = (Button)view3.findViewById(R.id.key_middle);
                DatePicker.this.mMonths[8] = (Button)view3.findViewById(R.id.key_right);
                DatePicker.this.mMonths[9] = (Button)view4.findViewById(R.id.key_left);
                DatePicker.this.mMonths[10] = (Button)view4.findViewById(R.id.key_middle);
                DatePicker.this.mMonths[11] = (Button)view4.findViewById(R.id.key_right);
                for (n = 0; n < 12; ++n) {
                    DatePicker.this.mMonths[n].setOnClickListener((View.OnClickListener)DatePicker.this);
                    DatePicker.this.mMonths[n].setText((CharSequence)DatePicker.this.mMonthAbbreviations[n]);
                    DatePicker.this.mMonths[n].setTextColor(DatePicker.this.mTextColor);
                    DatePicker.this.mMonths[n].setBackgroundResource(DatePicker.this.mKeyBackgroundResId);
                    DatePicker.this.mMonths[n].setTag(R.id.date_keyboard, (Object)"month");
                    DatePicker.this.mMonths[n].setTag(R.id.date_month_int, (Object)n);
                }
            } else if (DatePicker.this.mDateFormatOrder[n] == 'd') {
                sDateKeyboardPosition = n;
                view = this.mInflater.inflate(R.layout.keyboard_right_drawable_with_header, viewGroup, false);
                View view5 = view.findViewById(R.id.first);
                View view6 = view.findViewById(R.id.second);
                View view7 = view.findViewById(R.id.third);
                View view8 = view.findViewById(R.id.fourth);
                ((TextView)view.findViewById(R.id.header)).setText(R.string.day_c);
                DatePicker.this.mDateNumbers[1] = (Button)view5.findViewById(R.id.key_left);
                DatePicker.this.mDateNumbers[2] = (Button)view5.findViewById(R.id.key_middle);
                DatePicker.this.mDateNumbers[3] = (Button)view5.findViewById(R.id.key_right);
                DatePicker.this.mDateNumbers[4] = (Button)view6.findViewById(R.id.key_left);
                DatePicker.this.mDateNumbers[5] = (Button)view6.findViewById(R.id.key_middle);
                DatePicker.this.mDateNumbers[6] = (Button)view6.findViewById(R.id.key_right);
                DatePicker.this.mDateNumbers[7] = (Button)view7.findViewById(R.id.key_left);
                DatePicker.this.mDateNumbers[8] = (Button)view7.findViewById(R.id.key_middle);
                DatePicker.this.mDateNumbers[9] = (Button)view7.findViewById(R.id.key_right);
                DatePicker.this.mDateLeft = (Button)view8.findViewById(R.id.key_left);
                DatePicker.this.mDateLeft.setTextColor(DatePicker.this.mTextColor);
                DatePicker.this.mDateLeft.setBackgroundResource(DatePicker.this.mKeyBackgroundResId);
                DatePicker.this.mDateNumbers[0] = (Button)view8.findViewById(R.id.key_middle);
                DatePicker.this.mDateRight = (ImageButton)view8.findViewById(R.id.key_right);
                for (n = 0; n < 10; ++n) {
                    DatePicker.this.mDateNumbers[n].setOnClickListener((View.OnClickListener)DatePicker.this);
                    DatePicker.this.mDateNumbers[n].setText((CharSequence)String.format(Locale.getDefault(), "%d", n));
                    DatePicker.this.mDateNumbers[n].setTextColor(DatePicker.this.mTextColor);
                    DatePicker.this.mDateNumbers[n].setBackgroundResource(DatePicker.this.mKeyBackgroundResId);
                    DatePicker.this.mDateNumbers[n].setTag(R.id.date_keyboard, (Object)"date");
                    DatePicker.this.mDateNumbers[n].setTag(R.id.numbers_key, (Object)n);
                }
                DatePicker.this.mDateRight.setImageDrawable(resources.getDrawable(DatePicker.this.mCheckDrawableSrcResId));
                DatePicker.this.mDateRight.setBackgroundResource(DatePicker.this.mKeyBackgroundResId);
                DatePicker.this.mDateRight.setOnClickListener((View.OnClickListener)DatePicker.this);
            } else if (DatePicker.this.mDateFormatOrder[n] == 'y') {
                sYearKeyboardPosition = n;
                view = this.mInflater.inflate(R.layout.keyboard_with_header, viewGroup, false);
                resources = view.findViewById(R.id.first);
                View view9 = view.findViewById(R.id.second);
                View view10 = view.findViewById(R.id.third);
                View view11 = view.findViewById(R.id.fourth);
                ((TextView)view.findViewById(R.id.header)).setText(R.string.year_c);
                DatePicker.this.mYearNumbers[1] = (Button)resources.findViewById(R.id.key_left);
                DatePicker.this.mYearNumbers[2] = (Button)resources.findViewById(R.id.key_middle);
                DatePicker.this.mYearNumbers[3] = (Button)resources.findViewById(R.id.key_right);
                DatePicker.this.mYearNumbers[4] = (Button)view9.findViewById(R.id.key_left);
                DatePicker.this.mYearNumbers[5] = (Button)view9.findViewById(R.id.key_middle);
                DatePicker.this.mYearNumbers[6] = (Button)view9.findViewById(R.id.key_right);
                DatePicker.this.mYearNumbers[7] = (Button)view10.findViewById(R.id.key_left);
                DatePicker.this.mYearNumbers[8] = (Button)view10.findViewById(R.id.key_middle);
                DatePicker.this.mYearNumbers[9] = (Button)view10.findViewById(R.id.key_right);
                DatePicker.this.mYearLeft = (Button)view11.findViewById(R.id.key_left);
                DatePicker.this.mYearLeft.setTextColor(DatePicker.this.mTextColor);
                DatePicker.this.mYearLeft.setBackgroundResource(DatePicker.this.mKeyBackgroundResId);
                DatePicker.this.mYearNumbers[0] = (Button)view11.findViewById(R.id.key_middle);
                DatePicker.this.mYearRight = (Button)view11.findViewById(R.id.key_right);
                DatePicker.this.mYearRight.setTextColor(DatePicker.this.mTextColor);
                DatePicker.this.mYearRight.setBackgroundResource(DatePicker.this.mKeyBackgroundResId);
                for (n = 0; n < 10; ++n) {
                    DatePicker.this.mYearNumbers[n].setOnClickListener((View.OnClickListener)DatePicker.this);
                    DatePicker.this.mYearNumbers[n].setText((CharSequence)String.format(Locale.getDefault(), "%d", n));
                    DatePicker.this.mYearNumbers[n].setTextColor(DatePicker.this.mTextColor);
                    DatePicker.this.mYearNumbers[n].setBackgroundResource(DatePicker.this.mKeyBackgroundResId);
                    DatePicker.this.mYearNumbers[n].setTag(R.id.date_keyboard, (Object)"year");
                    DatePicker.this.mYearNumbers[n].setTag(R.id.numbers_key, (Object)n);
                }
            } else {
                view = new View(DatePicker.this.mContext);
            }
            DatePicker.this.setLeftRightEnabled();
            DatePicker.this.updateDate();
            DatePicker.this.updateKeypad();
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
        int[] mDateInput;
        int mDateInputPointer;
        int mMonthInput;
        int[] mYearInput;
        int mYearInputPointer;

        private SavedState(Parcel parcel) {
            super(parcel);
            this.mDateInputPointer = parcel.readInt();
            this.mYearInputPointer = parcel.readInt();
            parcel.readIntArray(this.mDateInput);
            parcel.readIntArray(this.mYearInput);
            this.mMonthInput = parcel.readInt();
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.mDateInputPointer);
            parcel.writeInt(this.mYearInputPointer);
            parcel.writeIntArray(this.mDateInput);
            parcel.writeIntArray(this.mYearInput);
            parcel.writeInt(this.mMonthInput);
        }

    }

}

