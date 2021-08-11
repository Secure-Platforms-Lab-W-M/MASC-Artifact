/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.drawable.Drawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.Log
 *  android.widget.TextView
 */
package android.support.v4.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleRes;
import android.support.v4.widget.AutoSizeableTextView;
import android.util.Log;
import android.widget.TextView;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;

public final class TextViewCompat {
    public static final int AUTO_SIZE_TEXT_TYPE_NONE = 0;
    public static final int AUTO_SIZE_TEXT_TYPE_UNIFORM = 1;
    static final TextViewCompatBaseImpl IMPL = Build.VERSION.SDK_INT >= 26 ? new TextViewCompatApi26Impl() : (Build.VERSION.SDK_INT >= 23 ? new TextViewCompatApi23Impl() : (Build.VERSION.SDK_INT >= 18 ? new TextViewCompatApi18Impl() : (Build.VERSION.SDK_INT >= 17 ? new TextViewCompatApi17Impl() : (Build.VERSION.SDK_INT >= 16 ? new TextViewCompatApi16Impl() : new TextViewCompatBaseImpl()))));

    private TextViewCompat() {
    }

    public static int getAutoSizeMaxTextSize(TextView textView) {
        return IMPL.getAutoSizeMaxTextSize(textView);
    }

    public static int getAutoSizeMinTextSize(TextView textView) {
        return IMPL.getAutoSizeMinTextSize(textView);
    }

    public static int getAutoSizeStepGranularity(TextView textView) {
        return IMPL.getAutoSizeStepGranularity(textView);
    }

    public static int[] getAutoSizeTextAvailableSizes(TextView textView) {
        return IMPL.getAutoSizeTextAvailableSizes(textView);
    }

    public static int getAutoSizeTextType(TextView textView) {
        return IMPL.getAutoSizeTextType(textView);
    }

    public static Drawable[] getCompoundDrawablesRelative(@NonNull TextView textView) {
        return IMPL.getCompoundDrawablesRelative(textView);
    }

    public static int getMaxLines(@NonNull TextView textView) {
        return IMPL.getMaxLines(textView);
    }

    public static int getMinLines(@NonNull TextView textView) {
        return IMPL.getMinLines(textView);
    }

    public static void setAutoSizeTextTypeUniformWithConfiguration(TextView textView, int n, int n2, int n3, int n4) throws IllegalArgumentException {
        IMPL.setAutoSizeTextTypeUniformWithConfiguration(textView, n, n2, n3, n4);
    }

    public static void setAutoSizeTextTypeUniformWithPresetSizes(TextView textView, @NonNull int[] arrn, int n) throws IllegalArgumentException {
        IMPL.setAutoSizeTextTypeUniformWithPresetSizes(textView, arrn, n);
    }

    public static void setAutoSizeTextTypeWithDefaults(TextView textView, int n) {
        IMPL.setAutoSizeTextTypeWithDefaults(textView, n);
    }

    public static void setCompoundDrawablesRelative(@NonNull TextView textView, @Nullable Drawable drawable2, @Nullable Drawable drawable3, @Nullable Drawable drawable4, @Nullable Drawable drawable5) {
        IMPL.setCompoundDrawablesRelative(textView, drawable2, drawable3, drawable4, drawable5);
    }

    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, @DrawableRes int n, @DrawableRes int n2, @DrawableRes int n3, @DrawableRes int n4) {
        IMPL.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, n, n2, n3, n4);
    }

    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, @Nullable Drawable drawable2, @Nullable Drawable drawable3, @Nullable Drawable drawable4, @Nullable Drawable drawable5) {
        IMPL.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, drawable2, drawable3, drawable4, drawable5);
    }

    public static void setTextAppearance(@NonNull TextView textView, @StyleRes int n) {
        IMPL.setTextAppearance(textView, n);
    }

    @Retention(value=RetentionPolicy.SOURCE)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static @interface AutoSizeTextType {
    }

    @RequiresApi(value=16)
    static class TextViewCompatApi16Impl
    extends TextViewCompatBaseImpl {
        TextViewCompatApi16Impl() {
        }

        @Override
        public int getMaxLines(TextView textView) {
            return textView.getMaxLines();
        }

        @Override
        public int getMinLines(TextView textView) {
            return textView.getMinLines();
        }
    }

    @RequiresApi(value=17)
    static class TextViewCompatApi17Impl
    extends TextViewCompatApi16Impl {
        TextViewCompatApi17Impl() {
        }

        @Override
        public Drawable[] getCompoundDrawablesRelative(@NonNull TextView arrdrawable) {
            int n = arrdrawable.getLayoutDirection();
            boolean bl = true;
            if (n != 1) {
                bl = false;
            }
            arrdrawable = arrdrawable.getCompoundDrawables();
            if (bl) {
                Drawable drawable2 = arrdrawable[2];
                Drawable drawable3 = arrdrawable[0];
                arrdrawable[0] = drawable2;
                arrdrawable[2] = drawable3;
                return arrdrawable;
            }
            return arrdrawable;
        }

        @Override
        public void setCompoundDrawablesRelative(@NonNull TextView textView, @Nullable Drawable drawable2, @Nullable Drawable drawable3, @Nullable Drawable drawable4, @Nullable Drawable drawable5) {
            int n = textView.getLayoutDirection();
            boolean bl = true;
            if (n != 1) {
                bl = false;
            }
            Drawable drawable6 = bl ? drawable4 : drawable2;
            if (!bl) {
                drawable2 = drawable4;
            }
            textView.setCompoundDrawables(drawable6, drawable3, drawable2, drawable5);
        }

        @Override
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, @DrawableRes int n, @DrawableRes int n2, @DrawableRes int n3, @DrawableRes int n4) {
            int n5 = textView.getLayoutDirection();
            boolean bl = true;
            if (n5 != 1) {
                bl = false;
            }
            n5 = bl ? n3 : n;
            if (!bl) {
                n = n3;
            }
            textView.setCompoundDrawablesWithIntrinsicBounds(n5, n2, n, n4);
        }

        @Override
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, @Nullable Drawable drawable2, @Nullable Drawable drawable3, @Nullable Drawable drawable4, @Nullable Drawable drawable5) {
            int n = textView.getLayoutDirection();
            boolean bl = true;
            if (n != 1) {
                bl = false;
            }
            Drawable drawable6 = bl ? drawable4 : drawable2;
            if (!bl) {
                drawable2 = drawable4;
            }
            textView.setCompoundDrawablesWithIntrinsicBounds(drawable6, drawable3, drawable2, drawable5);
        }
    }

    @RequiresApi(value=18)
    static class TextViewCompatApi18Impl
    extends TextViewCompatApi17Impl {
        TextViewCompatApi18Impl() {
        }

        @Override
        public Drawable[] getCompoundDrawablesRelative(@NonNull TextView textView) {
            return textView.getCompoundDrawablesRelative();
        }

        @Override
        public void setCompoundDrawablesRelative(@NonNull TextView textView, @Nullable Drawable drawable2, @Nullable Drawable drawable3, @Nullable Drawable drawable4, @Nullable Drawable drawable5) {
            textView.setCompoundDrawablesRelative(drawable2, drawable3, drawable4, drawable5);
        }

        @Override
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, @DrawableRes int n, @DrawableRes int n2, @DrawableRes int n3, @DrawableRes int n4) {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(n, n2, n3, n4);
        }

        @Override
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, @Nullable Drawable drawable2, @Nullable Drawable drawable3, @Nullable Drawable drawable4, @Nullable Drawable drawable5) {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable2, drawable3, drawable4, drawable5);
        }
    }

    @RequiresApi(value=23)
    static class TextViewCompatApi23Impl
    extends TextViewCompatApi18Impl {
        TextViewCompatApi23Impl() {
        }

        @Override
        public void setTextAppearance(@NonNull TextView textView, @StyleRes int n) {
            textView.setTextAppearance(n);
        }
    }

    @RequiresApi(value=26)
    static class TextViewCompatApi26Impl
    extends TextViewCompatApi23Impl {
        TextViewCompatApi26Impl() {
        }

        @Override
        public int getAutoSizeMaxTextSize(TextView textView) {
            return textView.getAutoSizeMaxTextSize();
        }

        @Override
        public int getAutoSizeMinTextSize(TextView textView) {
            return textView.getAutoSizeMinTextSize();
        }

        @Override
        public int getAutoSizeStepGranularity(TextView textView) {
            return textView.getAutoSizeStepGranularity();
        }

        @Override
        public int[] getAutoSizeTextAvailableSizes(TextView textView) {
            return textView.getAutoSizeTextAvailableSizes();
        }

        @Override
        public int getAutoSizeTextType(TextView textView) {
            return textView.getAutoSizeTextType();
        }

        @Override
        public void setAutoSizeTextTypeUniformWithConfiguration(TextView textView, int n, int n2, int n3, int n4) throws IllegalArgumentException {
            textView.setAutoSizeTextTypeUniformWithConfiguration(n, n2, n3, n4);
        }

        @Override
        public void setAutoSizeTextTypeUniformWithPresetSizes(TextView textView, @NonNull int[] arrn, int n) throws IllegalArgumentException {
            textView.setAutoSizeTextTypeUniformWithPresetSizes(arrn, n);
        }

        @Override
        public void setAutoSizeTextTypeWithDefaults(TextView textView, int n) {
            textView.setAutoSizeTextTypeWithDefaults(n);
        }
    }

    static class TextViewCompatBaseImpl {
        private static final int LINES = 1;
        private static final String LOG_TAG = "TextViewCompatBase";
        private static Field sMaxModeField;
        private static boolean sMaxModeFieldFetched;
        private static Field sMaximumField;
        private static boolean sMaximumFieldFetched;
        private static Field sMinModeField;
        private static boolean sMinModeFieldFetched;
        private static Field sMinimumField;
        private static boolean sMinimumFieldFetched;

        TextViewCompatBaseImpl() {
        }

        private static Field retrieveField(String string2) {
            Field field;
            Field field2 = null;
            try {
                field2 = field = TextView.class.getDeclaredField(string2);
            }
            catch (NoSuchFieldException noSuchFieldException) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Could not retrieve ");
                stringBuilder.append(string2);
                stringBuilder.append(" field.");
                Log.e((String)"TextViewCompatBase", (String)stringBuilder.toString());
                return field2;
            }
            field.setAccessible(true);
            return field;
        }

        private static int retrieveIntFromField(Field field, TextView textView) {
            try {
                int n = field.getInt((Object)textView);
                return n;
            }
            catch (IllegalAccessException illegalAccessException) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Could not retrieve value of ");
                stringBuilder.append(field.getName());
                stringBuilder.append(" field.");
                Log.d((String)"TextViewCompatBase", (String)stringBuilder.toString());
                return -1;
            }
        }

        public int getAutoSizeMaxTextSize(TextView textView) {
            if (textView instanceof AutoSizeableTextView) {
                return ((AutoSizeableTextView)textView).getAutoSizeMaxTextSize();
            }
            return -1;
        }

        public int getAutoSizeMinTextSize(TextView textView) {
            if (textView instanceof AutoSizeableTextView) {
                return ((AutoSizeableTextView)textView).getAutoSizeMinTextSize();
            }
            return -1;
        }

        public int getAutoSizeStepGranularity(TextView textView) {
            if (textView instanceof AutoSizeableTextView) {
                return ((AutoSizeableTextView)textView).getAutoSizeStepGranularity();
            }
            return -1;
        }

        public int[] getAutoSizeTextAvailableSizes(TextView textView) {
            if (textView instanceof AutoSizeableTextView) {
                return ((AutoSizeableTextView)textView).getAutoSizeTextAvailableSizes();
            }
            return new int[0];
        }

        public int getAutoSizeTextType(TextView textView) {
            if (textView instanceof AutoSizeableTextView) {
                return ((AutoSizeableTextView)textView).getAutoSizeTextType();
            }
            return 0;
        }

        public Drawable[] getCompoundDrawablesRelative(@NonNull TextView textView) {
            return textView.getCompoundDrawables();
        }

        public int getMaxLines(TextView textView) {
            Field field;
            if (!sMaxModeFieldFetched) {
                sMaxModeField = TextViewCompatBaseImpl.retrieveField("mMaxMode");
                sMaxModeFieldFetched = true;
            }
            if ((field = sMaxModeField) != null && TextViewCompatBaseImpl.retrieveIntFromField(field, textView) == 1) {
                if (!sMaximumFieldFetched) {
                    sMaximumField = TextViewCompatBaseImpl.retrieveField("mMaximum");
                    sMaximumFieldFetched = true;
                }
                if ((field = sMaximumField) != null) {
                    return TextViewCompatBaseImpl.retrieveIntFromField(field, textView);
                }
            }
            return -1;
        }

        public int getMinLines(TextView textView) {
            Field field;
            if (!sMinModeFieldFetched) {
                sMinModeField = TextViewCompatBaseImpl.retrieveField("mMinMode");
                sMinModeFieldFetched = true;
            }
            if ((field = sMinModeField) != null && TextViewCompatBaseImpl.retrieveIntFromField(field, textView) == 1) {
                if (!sMinimumFieldFetched) {
                    sMinimumField = TextViewCompatBaseImpl.retrieveField("mMinimum");
                    sMinimumFieldFetched = true;
                }
                if ((field = sMinimumField) != null) {
                    return TextViewCompatBaseImpl.retrieveIntFromField(field, textView);
                }
            }
            return -1;
        }

        public void setAutoSizeTextTypeUniformWithConfiguration(TextView textView, int n, int n2, int n3, int n4) throws IllegalArgumentException {
            if (textView instanceof AutoSizeableTextView) {
                ((AutoSizeableTextView)textView).setAutoSizeTextTypeUniformWithConfiguration(n, n2, n3, n4);
                return;
            }
        }

        public void setAutoSizeTextTypeUniformWithPresetSizes(TextView textView, @NonNull int[] arrn, int n) throws IllegalArgumentException {
            if (textView instanceof AutoSizeableTextView) {
                ((AutoSizeableTextView)textView).setAutoSizeTextTypeUniformWithPresetSizes(arrn, n);
                return;
            }
        }

        public void setAutoSizeTextTypeWithDefaults(TextView textView, int n) {
            if (textView instanceof AutoSizeableTextView) {
                ((AutoSizeableTextView)textView).setAutoSizeTextTypeWithDefaults(n);
                return;
            }
        }

        public void setCompoundDrawablesRelative(@NonNull TextView textView, @Nullable Drawable drawable2, @Nullable Drawable drawable3, @Nullable Drawable drawable4, @Nullable Drawable drawable5) {
            textView.setCompoundDrawables(drawable2, drawable3, drawable4, drawable5);
        }

        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, @DrawableRes int n, @DrawableRes int n2, @DrawableRes int n3, @DrawableRes int n4) {
            textView.setCompoundDrawablesWithIntrinsicBounds(n, n2, n3, n4);
        }

        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull TextView textView, @Nullable Drawable drawable2, @Nullable Drawable drawable3, @Nullable Drawable drawable4, @Nullable Drawable drawable5) {
            textView.setCompoundDrawablesWithIntrinsicBounds(drawable2, drawable3, drawable4, drawable5);
        }

        public void setTextAppearance(TextView textView, @StyleRes int n) {
            textView.setTextAppearance(textView.getContext(), n);
        }
    }

}

