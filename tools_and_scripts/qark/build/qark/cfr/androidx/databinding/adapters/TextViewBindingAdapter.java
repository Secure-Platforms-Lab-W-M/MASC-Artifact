/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.text.Editable
 *  android.text.InputFilter
 *  android.text.InputFilter$LengthFilter
 *  android.text.Spanned
 *  android.text.TextWatcher
 *  android.text.method.DialerKeyListener
 *  android.text.method.DigitsKeyListener
 *  android.text.method.KeyListener
 *  android.text.method.PasswordTransformationMethod
 *  android.text.method.TextKeyListener
 *  android.text.method.TextKeyListener$Capitalize
 *  android.text.method.TransformationMethod
 *  android.util.Log
 *  android.view.View
 *  android.widget.TextView
 *  android.widget.TextView$BufferType
 *  androidx.databinding.library.baseAdapters.R
 *  androidx.databinding.library.baseAdapters.R$id
 */
package androidx.databinding.adapters;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.DialerKeyListener;
import android.text.method.DigitsKeyListener;
import android.text.method.KeyListener;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TextKeyListener;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.adapters.ListenerUtil;
import androidx.databinding.library.baseAdapters.R;

public class TextViewBindingAdapter {
    public static final int DECIMAL = 5;
    public static final int INTEGER = 1;
    public static final int SIGNED = 3;
    private static final String TAG = "TextViewBindingAdapters";

    public static String getTextString(TextView textView) {
        return textView.getText().toString();
    }

    private static boolean haveContentsChanged(CharSequence charSequence, CharSequence charSequence2) {
        int n = charSequence == null ? 1 : 0;
        int n2 = charSequence2 == null ? 1 : 0;
        if (n != n2) {
            return true;
        }
        if (charSequence == null) {
            return false;
        }
        n2 = charSequence.length();
        if (n2 != charSequence2.length()) {
            return true;
        }
        for (n = 0; n < n2; ++n) {
            if (charSequence.charAt(n) == charSequence2.charAt(n)) continue;
            return true;
        }
        return false;
    }

    public static void setAutoText(TextView textView, boolean bl) {
        KeyListener keyListener = textView.getKeyListener();
        TextKeyListener.Capitalize capitalize = TextKeyListener.Capitalize.NONE;
        int n = keyListener != null ? keyListener.getInputType() : 0;
        if ((n & 4096) != 0) {
            capitalize = TextKeyListener.Capitalize.CHARACTERS;
        } else if ((n & 8192) != 0) {
            capitalize = TextKeyListener.Capitalize.WORDS;
        } else if ((n & 16384) != 0) {
            capitalize = TextKeyListener.Capitalize.SENTENCES;
        }
        textView.setKeyListener((KeyListener)TextKeyListener.getInstance((boolean)bl, (TextKeyListener.Capitalize)capitalize));
    }

    public static void setBufferType(TextView textView, TextView.BufferType bufferType) {
        textView.setText(textView.getText(), bufferType);
    }

    public static void setCapitalize(TextView textView, TextKeyListener.Capitalize capitalize) {
        boolean bl = (32768 & textView.getKeyListener().getInputType()) != 0;
        textView.setKeyListener((KeyListener)TextKeyListener.getInstance((boolean)bl, (TextKeyListener.Capitalize)capitalize));
    }

    public static void setDigits(TextView textView, CharSequence charSequence) {
        if (charSequence != null) {
            textView.setKeyListener((KeyListener)DigitsKeyListener.getInstance((String)charSequence.toString()));
            return;
        }
        if (textView.getKeyListener() instanceof DigitsKeyListener) {
            textView.setKeyListener(null);
        }
    }

    public static void setDrawableBottom(TextView textView, Drawable drawable2) {
        TextViewBindingAdapter.setIntrinsicBounds(drawable2);
        Drawable[] arrdrawable = textView.getCompoundDrawables();
        textView.setCompoundDrawables(arrdrawable[0], arrdrawable[1], arrdrawable[2], drawable2);
    }

    public static void setDrawableEnd(TextView textView, Drawable drawable2) {
        if (Build.VERSION.SDK_INT < 17) {
            TextViewBindingAdapter.setDrawableRight(textView, drawable2);
            return;
        }
        TextViewBindingAdapter.setIntrinsicBounds(drawable2);
        Drawable[] arrdrawable = textView.getCompoundDrawablesRelative();
        textView.setCompoundDrawablesRelative(arrdrawable[0], arrdrawable[1], drawable2, arrdrawable[3]);
    }

    public static void setDrawableLeft(TextView textView, Drawable drawable2) {
        TextViewBindingAdapter.setIntrinsicBounds(drawable2);
        Drawable[] arrdrawable = textView.getCompoundDrawables();
        textView.setCompoundDrawables(drawable2, arrdrawable[1], arrdrawable[2], arrdrawable[3]);
    }

    public static void setDrawableRight(TextView textView, Drawable drawable2) {
        TextViewBindingAdapter.setIntrinsicBounds(drawable2);
        Drawable[] arrdrawable = textView.getCompoundDrawables();
        textView.setCompoundDrawables(arrdrawable[0], arrdrawable[1], drawable2, arrdrawable[3]);
    }

    public static void setDrawableStart(TextView textView, Drawable drawable2) {
        if (Build.VERSION.SDK_INT < 17) {
            TextViewBindingAdapter.setDrawableLeft(textView, drawable2);
            return;
        }
        TextViewBindingAdapter.setIntrinsicBounds(drawable2);
        Drawable[] arrdrawable = textView.getCompoundDrawablesRelative();
        textView.setCompoundDrawablesRelative(drawable2, arrdrawable[1], arrdrawable[2], arrdrawable[3]);
    }

    public static void setDrawableTop(TextView textView, Drawable drawable2) {
        TextViewBindingAdapter.setIntrinsicBounds(drawable2);
        Drawable[] arrdrawable = textView.getCompoundDrawables();
        textView.setCompoundDrawables(arrdrawable[0], drawable2, arrdrawable[2], arrdrawable[3]);
    }

    public static void setImeActionLabel(TextView textView, int n) {
        textView.setImeActionLabel(textView.getImeActionLabel(), n);
    }

    public static void setImeActionLabel(TextView textView, CharSequence charSequence) {
        textView.setImeActionLabel(charSequence, textView.getImeActionId());
    }

    public static void setInputMethod(TextView textView, CharSequence charSequence) {
        try {
            textView.setKeyListener((KeyListener)Class.forName(charSequence.toString()).newInstance());
        }
        catch (IllegalAccessException illegalAccessException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Could not create input method: ");
            stringBuilder.append((Object)charSequence);
            Log.e((String)"TextViewBindingAdapters", (String)stringBuilder.toString(), (Throwable)illegalAccessException);
            return;
        }
        catch (InstantiationException instantiationException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Could not create input method: ");
            stringBuilder.append((Object)charSequence);
            Log.e((String)"TextViewBindingAdapters", (String)stringBuilder.toString(), (Throwable)instantiationException);
        }
        catch (ClassNotFoundException classNotFoundException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Could not create input method: ");
            stringBuilder.append((Object)charSequence);
            Log.e((String)"TextViewBindingAdapters", (String)stringBuilder.toString(), (Throwable)classNotFoundException);
        }
    }

    private static void setIntrinsicBounds(Drawable drawable2) {
        if (drawable2 != null) {
            drawable2.setBounds(0, 0, drawable2.getIntrinsicWidth(), drawable2.getIntrinsicHeight());
        }
    }

    public static void setLineSpacingExtra(TextView textView, float f) {
        if (Build.VERSION.SDK_INT >= 16) {
            textView.setLineSpacing(f, textView.getLineSpacingMultiplier());
            return;
        }
        textView.setLineSpacing(f, 1.0f);
    }

    public static void setLineSpacingMultiplier(TextView textView, float f) {
        if (Build.VERSION.SDK_INT >= 16) {
            textView.setLineSpacing(textView.getLineSpacingExtra(), f);
            return;
        }
        textView.setLineSpacing(0.0f, f);
    }

    public static void setMaxLength(TextView textView, int n) {
        InputFilter[] arrinputFilter;
        InputFilter[] arrinputFilter2 = textView.getFilters();
        if (arrinputFilter2 == null) {
            arrinputFilter = new InputFilter[]{new InputFilter.LengthFilter(n)};
        } else {
            boolean bl;
            boolean bl2 = false;
            int n2 = 0;
            do {
                bl = bl2;
                if (n2 >= arrinputFilter2.length) break;
                arrinputFilter = arrinputFilter2[n2];
                if (arrinputFilter instanceof InputFilter.LengthFilter) {
                    boolean bl3 = true;
                    bl2 = true;
                    if (Build.VERSION.SDK_INT >= 21) {
                        bl = ((InputFilter.LengthFilter)arrinputFilter).getMax() != n;
                        bl2 = bl;
                    }
                    bl = bl3;
                    if (!bl2) break;
                    arrinputFilter2[n2] = new InputFilter.LengthFilter(n);
                    bl = bl3;
                    break;
                }
                ++n2;
            } while (true);
            arrinputFilter = arrinputFilter2;
            if (!bl) {
                arrinputFilter = new InputFilter[arrinputFilter2.length + 1];
                System.arraycopy(arrinputFilter2, 0, arrinputFilter, 0, arrinputFilter2.length);
                arrinputFilter[arrinputFilter.length - 1] = new InputFilter.LengthFilter(n);
            }
        }
        textView.setFilters(arrinputFilter);
    }

    public static void setNumeric(TextView textView, int n) {
        boolean bl = true;
        boolean bl2 = (n & 3) != 0;
        if ((n & 5) == 0) {
            bl = false;
        }
        textView.setKeyListener((KeyListener)DigitsKeyListener.getInstance((boolean)bl2, (boolean)bl));
    }

    public static void setPassword(TextView textView, boolean bl) {
        if (bl) {
            textView.setTransformationMethod((TransformationMethod)PasswordTransformationMethod.getInstance());
            return;
        }
        if (textView.getTransformationMethod() instanceof PasswordTransformationMethod) {
            textView.setTransformationMethod(null);
        }
    }

    public static void setPhoneNumber(TextView textView, boolean bl) {
        if (bl) {
            textView.setKeyListener((KeyListener)DialerKeyListener.getInstance());
            return;
        }
        if (textView.getKeyListener() instanceof DialerKeyListener) {
            textView.setKeyListener(null);
        }
    }

    public static void setShadowColor(TextView textView, int n) {
        if (Build.VERSION.SDK_INT >= 16) {
            float f = textView.getShadowDx();
            float f2 = textView.getShadowDy();
            textView.setShadowLayer(textView.getShadowRadius(), f, f2, n);
        }
    }

    public static void setShadowDx(TextView textView, float f) {
        if (Build.VERSION.SDK_INT >= 16) {
            int n = textView.getShadowColor();
            float f2 = textView.getShadowDy();
            textView.setShadowLayer(textView.getShadowRadius(), f, f2, n);
        }
    }

    public static void setShadowDy(TextView textView, float f) {
        if (Build.VERSION.SDK_INT >= 16) {
            int n = textView.getShadowColor();
            float f2 = textView.getShadowDx();
            textView.setShadowLayer(textView.getShadowRadius(), f2, f, n);
        }
    }

    public static void setShadowRadius(TextView textView, float f) {
        if (Build.VERSION.SDK_INT >= 16) {
            int n = textView.getShadowColor();
            textView.setShadowLayer(f, textView.getShadowDx(), textView.getShadowDy(), n);
        }
    }

    public static void setText(TextView textView, CharSequence charSequence) {
        CharSequence charSequence2 = textView.getText();
        if (charSequence != charSequence2) {
            if (charSequence == null && charSequence2.length() == 0) {
                return;
            }
            if (charSequence instanceof Spanned ? charSequence.equals(charSequence2) : !TextViewBindingAdapter.haveContentsChanged(charSequence, charSequence2)) {
                return;
            }
            textView.setText(charSequence);
            return;
        }
    }

    public static void setTextSize(TextView textView, float f) {
        textView.setTextSize(0, f);
    }

    public static void setTextWatcher(TextView textView, BeforeTextChanged object, OnTextChanged onTextChanged, AfterTextChanged afterTextChanged, InverseBindingListener inverseBindingListener) {
        object = object == null && afterTextChanged == null && onTextChanged == null && inverseBindingListener == null ? null : new TextWatcher((BeforeTextChanged)object, onTextChanged, inverseBindingListener, afterTextChanged){
            final /* synthetic */ AfterTextChanged val$after;
            final /* synthetic */ BeforeTextChanged val$before;
            final /* synthetic */ OnTextChanged val$on;
            final /* synthetic */ InverseBindingListener val$textAttrChanged;
            {
                this.val$before = beforeTextChanged;
                this.val$on = onTextChanged;
                this.val$textAttrChanged = inverseBindingListener;
                this.val$after = afterTextChanged;
            }

            public void afterTextChanged(Editable editable) {
                AfterTextChanged afterTextChanged = this.val$after;
                if (afterTextChanged != null) {
                    afterTextChanged.afterTextChanged(editable);
                }
            }

            public void beforeTextChanged(CharSequence charSequence, int n, int n2, int n3) {
                BeforeTextChanged beforeTextChanged = this.val$before;
                if (beforeTextChanged != null) {
                    beforeTextChanged.beforeTextChanged(charSequence, n, n2, n3);
                }
            }

            public void onTextChanged(CharSequence object, int n, int n2, int n3) {
                OnTextChanged onTextChanged = this.val$on;
                if (onTextChanged != null) {
                    onTextChanged.onTextChanged((CharSequence)object, n, n2, n3);
                }
                if ((object = this.val$textAttrChanged) != null) {
                    object.onChange();
                }
            }
        };
        onTextChanged = (TextWatcher)ListenerUtil.trackListener((View)textView, object, R.id.textWatcher);
        if (onTextChanged != null) {
            textView.removeTextChangedListener((TextWatcher)onTextChanged);
        }
        if (object != null) {
            textView.addTextChangedListener((TextWatcher)object);
        }
    }

    public static interface AfterTextChanged {
        public void afterTextChanged(Editable var1);
    }

    public static interface BeforeTextChanged {
        public void beforeTextChanged(CharSequence var1, int var2, int var3, int var4);
    }

    public static interface OnTextChanged {
        public void onTextChanged(CharSequence var1, int var2, int var3, int var4);
    }

}

