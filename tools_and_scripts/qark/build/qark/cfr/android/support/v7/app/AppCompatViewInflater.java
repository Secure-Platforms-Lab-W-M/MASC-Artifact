/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.ContextWrapper
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.view.InflateException
 *  android.view.View
 *  android.view.View$OnClickListener
 */
package android.support.v7.app;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.TintContextWrapper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.View;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

class AppCompatViewInflater {
    private static final String LOG_TAG = "AppCompatViewInflater";
    private static final String[] sClassPrefixList;
    private static final Map<String, Constructor<? extends View>> sConstructorMap;
    private static final Class<?>[] sConstructorSignature;
    private static final int[] sOnClickAttrs;
    private final Object[] mConstructorArgs = new Object[2];

    static {
        sConstructorSignature = new Class[]{Context.class, AttributeSet.class};
        sOnClickAttrs = new int[]{16843375};
        sClassPrefixList = new String[]{"android.widget.", "android.view.", "android.webkit."};
        sConstructorMap = new ArrayMap<String, Constructor<? extends View>>();
    }

    AppCompatViewInflater() {
    }

    private void checkOnClickListener(View view, AttributeSet attributeSet) {
        Object object = view.getContext();
        if (object instanceof ContextWrapper) {
            if (Build.VERSION.SDK_INT >= 15 && !ViewCompat.hasOnClickListeners(view)) {
                return;
            }
            attributeSet = object.obtainStyledAttributes(attributeSet, sOnClickAttrs);
            object = attributeSet.getString(0);
            if (object != null) {
                view.setOnClickListener((View.OnClickListener)new DeclaredOnClickListener(view, (String)object));
            }
            attributeSet.recycle();
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private View createView(Context object, String string2, String string3) throws ClassNotFoundException, InflateException {
        Constructor<? extends View> constructor = sConstructorMap.get(string2);
        if (constructor == null) {
            try {
                constructor = object.getClassLoader();
                if (string3 != null) {
                    object = new StringBuilder();
                    object.append(string3);
                    object.append(string2);
                    object = object.toString();
                } else {
                    object = string2;
                }
                object = constructor.loadClass((String)object).asSubclass(View.class).getConstructor(sConstructorSignature);
                sConstructorMap.put(string2, (Constructor<? extends View>)object);
            }
            catch (Exception exception) {
                return null;
            }
        } else {
            object = constructor;
        }
        object.setAccessible(true);
        return object.newInstance(this.mConstructorArgs);
    }

    private View createViewFromTag(Context arrobject, String arrobject2, AttributeSet attributeSet) {
        block7 : {
            if (arrobject2.equals("view")) {
                arrobject2 = attributeSet.getAttributeValue(null, "class");
            }
            this.mConstructorArgs[0] = arrobject;
            this.mConstructorArgs[1] = attributeSet;
            if (-1 != arrobject2.indexOf(46)) break block7;
            int n = 0;
            do {
                block8 : {
                    if (n >= sClassPrefixList.length) break;
                    attributeSet = this.createView((Context)arrobject, (String)arrobject2, sClassPrefixList[n]);
                    if (attributeSet == null) break block8;
                    arrobject = this.mConstructorArgs;
                    arrobject[0] = null;
                    arrobject[1] = null;
                    return attributeSet;
                }
                ++n;
            } while (true);
            arrobject = this.mConstructorArgs;
            arrobject[0] = null;
            arrobject[1] = null;
            return null;
        }
        try {
            arrobject = this.createView((Context)arrobject, (String)arrobject2, null);
            arrobject2 = this.mConstructorArgs;
            arrobject2[0] = null;
            arrobject2[1] = null;
            return arrobject;
        }
        catch (Throwable throwable) {
            arrobject2 = this.mConstructorArgs;
            arrobject2[0] = null;
            arrobject2[1] = null;
            throw throwable;
        }
        catch (Exception exception) {
            Object[] arrobject3 = this.mConstructorArgs;
            arrobject3[0] = null;
            arrobject3[1] = null;
            return null;
        }
    }

    private static Context themifyContext(Context context, AttributeSet attributeSet, boolean bl, boolean bl2) {
        attributeSet = context.obtainStyledAttributes(attributeSet, R.styleable.View, 0, 0);
        int n = 0;
        if (bl) {
            n = attributeSet.getResourceId(R.styleable.View_android_theme, 0);
        }
        if (bl2 && n == 0 && (n = attributeSet.getResourceId(R.styleable.View_theme, 0)) != 0) {
            Log.i((String)"AppCompatViewInflater", (String)"app:theme is now deprecated. Please move to using android:theme instead.");
        }
        attributeSet.recycle();
        if (n != 0) {
            if (context instanceof ContextThemeWrapper && ((ContextThemeWrapper)context).getThemeResId() == n) {
                return context;
            }
            return new ContextThemeWrapper(context, n);
        }
        return context;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public final View createView(View var1_1, String var2_2, @NonNull Context var3_3, @NonNull AttributeSet var4_4, boolean var5_5, boolean var6_6, boolean var7_7, boolean var8_8) {
        block32 : {
            var1_1 = var5_5 != false && var1_1 != null ? var1_1.getContext() : var3_3;
            if (var6_6 || var7_7) {
                var1_1 = AppCompatViewInflater.themifyContext((Context)var1_1, var4_4, var6_6, var7_7);
            }
            var10_9 = var8_8 != false ? TintContextWrapper.wrap((Context)var1_1) : var1_1;
            var1_1 = null;
            var9_10 = -1;
            switch (var2_2.hashCode()) {
                case 2001146706: {
                    if (!var2_2.equals("Button")) break;
                    var9_10 = 2;
                    ** break;
                }
                case 1666676343: {
                    if (!var2_2.equals("EditText")) break;
                    var9_10 = 3;
                    ** break;
                }
                case 1601505219: {
                    if (!var2_2.equals("CheckBox")) break;
                    var9_10 = 6;
                    ** break;
                }
                case 1413872058: {
                    if (!var2_2.equals("AutoCompleteTextView")) break;
                    var9_10 = 9;
                    ** break;
                }
                case 1125864064: {
                    if (!var2_2.equals("ImageView")) break;
                    var9_10 = 1;
                    ** break;
                }
                case 776382189: {
                    if (!var2_2.equals("RadioButton")) break;
                    var9_10 = 7;
                    ** break;
                }
                case -339785223: {
                    if (!var2_2.equals("Spinner")) break;
                    var9_10 = 4;
                    ** break;
                }
                case -658531749: {
                    if (!var2_2.equals("SeekBar")) break;
                    var9_10 = 12;
                    ** break;
                }
                case -937446323: {
                    if (!var2_2.equals("ImageButton")) break;
                    var9_10 = 5;
                    ** break;
                }
                case -938935918: {
                    if (!var2_2.equals("TextView")) break;
                    var9_10 = 0;
                    ** break;
                }
                case -1346021293: {
                    if (!var2_2.equals("MultiAutoCompleteTextView")) break;
                    var9_10 = 10;
                    ** break;
                }
                case -1455429095: {
                    if (!var2_2.equals("CheckedTextView")) break;
                    var9_10 = 8;
                    ** break;
                }
                case -1946472170: {
                    if (!var2_2.equals("RatingBar")) break;
                    var9_10 = 11;
                    break block32;
                }
            }
            ** break;
        }
        switch (var9_10) {
            default: {
                ** break;
            }
            case 12: {
                var1_1 = new AppCompatSeekBar(var10_9, var4_4);
                ** break;
            }
            case 11: {
                var1_1 = new AppCompatRatingBar(var10_9, var4_4);
                ** break;
            }
            case 10: {
                var1_1 = new AppCompatMultiAutoCompleteTextView(var10_9, var4_4);
                ** break;
            }
            case 9: {
                var1_1 = new AppCompatAutoCompleteTextView(var10_9, var4_4);
                ** break;
            }
            case 8: {
                var1_1 = new AppCompatCheckedTextView(var10_9, var4_4);
                ** break;
            }
            case 7: {
                var1_1 = new AppCompatRadioButton(var10_9, var4_4);
                ** break;
            }
            case 6: {
                var1_1 = new AppCompatCheckBox(var10_9, var4_4);
                ** break;
            }
            case 5: {
                var1_1 = new AppCompatImageButton(var10_9, var4_4);
                ** break;
            }
            case 4: {
                var1_1 = new AppCompatSpinner(var10_9, var4_4);
                ** break;
            }
            case 3: {
                var1_1 = new AppCompatEditText(var10_9, var4_4);
                ** break;
            }
            case 2: {
                var1_1 = new AppCompatButton(var10_9, var4_4);
                ** break;
            }
            case 1: {
                var1_1 = new AppCompatImageView(var10_9, var4_4);
                ** break;
            }
            case 0: 
        }
        var1_1 = new AppCompatTextView(var10_9, var4_4);
lbl103: // 14 sources:
        if (var1_1 == null && var3_3 != var10_9) {
            var1_1 = this.createViewFromTag(var10_9, var2_2, var4_4);
        }
        if (var1_1 == null) return var1_1;
        this.checkOnClickListener((View)var1_1, var4_4);
        return var1_1;
    }

    private static class DeclaredOnClickListener
    implements View.OnClickListener {
        private final View mHostView;
        private final String mMethodName;
        private Context mResolvedContext;
        private Method mResolvedMethod;

        public DeclaredOnClickListener(@NonNull View view, @NonNull String string2) {
            this.mHostView = view;
            this.mMethodName = string2;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @NonNull
        private void resolveMethod(@Nullable Context object, @NonNull String object2) {
            while (object != null) {
                try {
                    if (!object.isRestricted() && (object2 = object.getClass().getMethod(this.mMethodName, View.class)) != null) {
                        this.mResolvedMethod = object2;
                        this.mResolvedContext = object;
                        return;
                    }
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    // empty catch block
                }
                if (object instanceof ContextWrapper) {
                    object = ((ContextWrapper)object).getBaseContext();
                    continue;
                }
                object = null;
            }
            int n = this.mHostView.getId();
            if (n == -1) {
                object = "";
            } else {
                object = new StringBuilder();
                object.append(" with id '");
                object.append(this.mHostView.getContext().getResources().getResourceEntryName(n));
                object.append("'");
                object = object.toString();
            }
            object2 = new StringBuilder();
            object2.append("Could not find method ");
            object2.append(this.mMethodName);
            object2.append("(View) in a parent or ancestor Context for android:onClick ");
            object2.append("attribute defined on view ");
            object2.append(this.mHostView.getClass());
            object2.append((String)object);
            throw new IllegalStateException(object2.toString());
        }

        public void onClick(@NonNull View view) {
            if (this.mResolvedMethod == null) {
                this.resolveMethod(this.mHostView.getContext(), this.mMethodName);
            }
            try {
                this.mResolvedMethod.invoke((Object)this.mResolvedContext, new Object[]{view});
                return;
            }
            catch (InvocationTargetException invocationTargetException) {
                throw new IllegalStateException("Could not execute method for android:onClick", invocationTargetException);
            }
            catch (IllegalAccessException illegalAccessException) {
                throw new IllegalStateException("Could not execute non-public method for android:onClick", illegalAccessException);
            }
        }
    }

}

