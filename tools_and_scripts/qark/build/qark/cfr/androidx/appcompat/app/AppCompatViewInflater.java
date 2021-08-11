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
 *  androidx.appcompat.R
 *  androidx.appcompat.R$styleable
 */
package androidx.appcompat.app;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.View;
import androidx.appcompat.R;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.appcompat.widget.TintContextWrapper;
import androidx.collection.ArrayMap;
import androidx.core.view.ViewCompat;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class AppCompatViewInflater {
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
    private View createViewByPrefix(Context context, String string, String string2) throws ClassNotFoundException, InflateException {
        Constructor<? extends View> constructor = sConstructorMap.get(string);
        Constructor<View> constructor2 = constructor;
        if (constructor == null) {
            if (string2 != null) {
                try {
                    constructor2 = new StringBuilder();
                    constructor2.append(string2);
                    constructor2.append(string);
                    string2 = constructor2.toString();
                }
                catch (Exception exception) {
                    return null;
                }
            } else {
                string2 = string;
            }
            constructor2 = Class.forName(string2, false, context.getClassLoader()).asSubclass(View.class).getConstructor(sConstructorSignature);
            sConstructorMap.put(string, constructor2);
        }
        constructor2.setAccessible(true);
        return constructor2.newInstance(this.mConstructorArgs);
    }

    private View createViewFromTag(Context arrobject, String view, AttributeSet attributeSet) {
        View view2;
        block7 : {
            view2 = view;
            if (view.equals("view")) {
                view2 = attributeSet.getAttributeValue(null, "class");
            }
            this.mConstructorArgs[0] = arrobject;
            this.mConstructorArgs[1] = attributeSet;
            if (-1 != view2.indexOf(46)) break block7;
            int n = 0;
            do {
                block8 : {
                    if (n >= sClassPrefixList.length) break;
                    view = this.createViewByPrefix((Context)arrobject, (String)view2, sClassPrefixList[n]);
                    if (view == null) break block8;
                    arrobject = this.mConstructorArgs;
                    arrobject[0] = null;
                    arrobject[1] = null;
                    return view;
                }
                ++n;
            } while (true);
            arrobject = this.mConstructorArgs;
            arrobject[0] = null;
            arrobject[1] = null;
            return null;
        }
        try {
            arrobject = this.createViewByPrefix((Context)arrobject, (String)view2, null);
            view = this.mConstructorArgs;
            view[0] = null;
            view[1] = null;
            return arrobject;
        }
        catch (Throwable throwable) {
            view = this.mConstructorArgs;
            view[0] = null;
            view[1] = null;
            throw throwable;
        }
        catch (Exception exception) {
            Object[] arrobject2 = this.mConstructorArgs;
            arrobject2[0] = null;
            arrobject2[1] = null;
            return null;
        }
    }

    private static Context themifyContext(Context context, AttributeSet object, boolean bl, boolean bl2) {
        block7 : {
            int n;
            block8 : {
                object = context.obtainStyledAttributes((AttributeSet)object, R.styleable.View, 0, 0);
                int n2 = 0;
                if (bl) {
                    n2 = object.getResourceId(R.styleable.View_android_theme, 0);
                }
                n = n2;
                if (bl2) {
                    n = n2;
                    if (n2 == 0) {
                        n = n2 = object.getResourceId(R.styleable.View_theme, 0);
                        if (n2 != 0) {
                            Log.i((String)"AppCompatViewInflater", (String)"app:theme is now deprecated. Please move to using android:theme instead.");
                            n = n2;
                        }
                    }
                }
                object.recycle();
                object = context;
                if (n == 0) break block7;
                if (!(context instanceof ContextThemeWrapper)) break block8;
                object = context;
                if (((ContextThemeWrapper)context).getThemeResId() == n) break block7;
            }
            object = new ContextThemeWrapper(context, n);
        }
        return object;
    }

    private void verifyNotNull(View object, String string) {
        if (object != null) {
            return;
        }
        object = new StringBuilder();
        object.append(this.getClass().getName());
        object.append(" asked to inflate view for <");
        object.append(string);
        object.append(">, but returned null");
        throw new IllegalStateException(object.toString());
    }

    protected AppCompatAutoCompleteTextView createAutoCompleteTextView(Context context, AttributeSet attributeSet) {
        return new AppCompatAutoCompleteTextView(context, attributeSet);
    }

    protected AppCompatButton createButton(Context context, AttributeSet attributeSet) {
        return new AppCompatButton(context, attributeSet);
    }

    protected AppCompatCheckBox createCheckBox(Context context, AttributeSet attributeSet) {
        return new AppCompatCheckBox(context, attributeSet);
    }

    protected AppCompatCheckedTextView createCheckedTextView(Context context, AttributeSet attributeSet) {
        return new AppCompatCheckedTextView(context, attributeSet);
    }

    protected AppCompatEditText createEditText(Context context, AttributeSet attributeSet) {
        return new AppCompatEditText(context, attributeSet);
    }

    protected AppCompatImageButton createImageButton(Context context, AttributeSet attributeSet) {
        return new AppCompatImageButton(context, attributeSet);
    }

    protected AppCompatImageView createImageView(Context context, AttributeSet attributeSet) {
        return new AppCompatImageView(context, attributeSet);
    }

    protected AppCompatMultiAutoCompleteTextView createMultiAutoCompleteTextView(Context context, AttributeSet attributeSet) {
        return new AppCompatMultiAutoCompleteTextView(context, attributeSet);
    }

    protected AppCompatRadioButton createRadioButton(Context context, AttributeSet attributeSet) {
        return new AppCompatRadioButton(context, attributeSet);
    }

    protected AppCompatRatingBar createRatingBar(Context context, AttributeSet attributeSet) {
        return new AppCompatRatingBar(context, attributeSet);
    }

    protected AppCompatSeekBar createSeekBar(Context context, AttributeSet attributeSet) {
        return new AppCompatSeekBar(context, attributeSet);
    }

    protected AppCompatSpinner createSpinner(Context context, AttributeSet attributeSet) {
        return new AppCompatSpinner(context, attributeSet);
    }

    protected AppCompatTextView createTextView(Context context, AttributeSet attributeSet) {
        return new AppCompatTextView(context, attributeSet);
    }

    protected AppCompatToggleButton createToggleButton(Context context, AttributeSet attributeSet) {
        return new AppCompatToggleButton(context, attributeSet);
    }

    protected View createView(Context context, String string, AttributeSet attributeSet) {
        return null;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    final View createView(View var1_1, String var2_2, Context var3_3, AttributeSet var4_4, boolean var5_5, boolean var6_6, boolean var7_7, boolean var8_8) {
        block42 : {
            block41 : {
                block40 : {
                    var10_9 = var3_3;
                    if (var5_5) {
                        var10_9 = var3_3;
                        if (var1_1 != null) {
                            var10_9 = var1_1.getContext();
                        }
                    }
                    if (var6_6) break block40;
                    var1_1 = var10_9;
                    if (!var7_7) break block41;
                }
                var1_1 = AppCompatViewInflater.themifyContext((Context)var10_9, var4_4, var6_6, var7_7);
            }
            var10_9 = var1_1;
            if (var8_8) {
                var10_9 = TintContextWrapper.wrap((Context)var1_1);
            }
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
                case 799298502: {
                    if (!var2_2.equals("ToggleButton")) break;
                    var9_10 = 13;
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
                    break block42;
                }
            }
            ** break;
        }
        switch (var9_10) {
            default: {
                var1_1 = this.createView((Context)var10_9, var2_2, var4_4);
                ** break;
            }
            case 13: {
                var1_1 = this.createToggleButton((Context)var10_9, var4_4);
                this.verifyNotNull((View)var1_1, var2_2);
                ** break;
            }
            case 12: {
                var1_1 = this.createSeekBar((Context)var10_9, var4_4);
                this.verifyNotNull((View)var1_1, var2_2);
                ** break;
            }
            case 11: {
                var1_1 = this.createRatingBar((Context)var10_9, var4_4);
                this.verifyNotNull((View)var1_1, var2_2);
                ** break;
            }
            case 10: {
                var1_1 = this.createMultiAutoCompleteTextView((Context)var10_9, var4_4);
                this.verifyNotNull((View)var1_1, var2_2);
                ** break;
            }
            case 9: {
                var1_1 = this.createAutoCompleteTextView((Context)var10_9, var4_4);
                this.verifyNotNull((View)var1_1, var2_2);
                ** break;
            }
            case 8: {
                var1_1 = this.createCheckedTextView((Context)var10_9, var4_4);
                this.verifyNotNull((View)var1_1, var2_2);
                ** break;
            }
            case 7: {
                var1_1 = this.createRadioButton((Context)var10_9, var4_4);
                this.verifyNotNull((View)var1_1, var2_2);
                ** break;
            }
            case 6: {
                var1_1 = this.createCheckBox((Context)var10_9, var4_4);
                this.verifyNotNull((View)var1_1, var2_2);
                ** break;
            }
            case 5: {
                var1_1 = this.createImageButton((Context)var10_9, var4_4);
                this.verifyNotNull((View)var1_1, var2_2);
                ** break;
            }
            case 4: {
                var1_1 = this.createSpinner((Context)var10_9, var4_4);
                this.verifyNotNull((View)var1_1, var2_2);
                ** break;
            }
            case 3: {
                var1_1 = this.createEditText((Context)var10_9, var4_4);
                this.verifyNotNull((View)var1_1, var2_2);
                ** break;
            }
            case 2: {
                var1_1 = this.createButton((Context)var10_9, var4_4);
                this.verifyNotNull((View)var1_1, var2_2);
                ** break;
            }
            case 1: {
                var1_1 = this.createImageView((Context)var10_9, var4_4);
                this.verifyNotNull((View)var1_1, var2_2);
                ** break;
            }
            case 0: 
        }
        var1_1 = this.createTextView((Context)var10_9, var4_4);
        this.verifyNotNull((View)var1_1, var2_2);
lbl134: // 15 sources:
        var11_11 = var1_1;
        if (var1_1 == null) {
            var11_11 = var1_1;
            if (var3_3 != var10_9) {
                var11_11 = this.createViewFromTag((Context)var10_9, var2_2, var4_4);
            }
        }
        if (var11_11 == null) return var11_11;
        this.checkOnClickListener((View)var11_11, var4_4);
        return var11_11;
    }

    private static class DeclaredOnClickListener
    implements View.OnClickListener {
        private final View mHostView;
        private final String mMethodName;
        private Context mResolvedContext;
        private Method mResolvedMethod;

        public DeclaredOnClickListener(View view, String string) {
            this.mHostView = view;
            this.mMethodName = string;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        private void resolveMethod(Context object, String object2) {
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
            object2.append("(View) in a parent or ancestor Context for android:onClick attribute defined on view ");
            object2.append(this.mHostView.getClass());
            object2.append((String)object);
            throw new IllegalStateException(object2.toString());
        }

        public void onClick(View view) {
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

