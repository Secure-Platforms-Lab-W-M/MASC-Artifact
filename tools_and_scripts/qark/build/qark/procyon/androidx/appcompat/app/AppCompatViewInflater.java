// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.appcompat.app;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import androidx.appcompat.widget.TintContextWrapper;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.view.ContextThemeWrapper;
import android.util.Log;
import androidx.appcompat.R$styleable;
import android.view.InflateException;
import android.content.res.TypedArray;
import android.view.View$OnClickListener;
import androidx.core.view.ViewCompat;
import android.os.Build$VERSION;
import android.content.ContextWrapper;
import androidx.collection.ArrayMap;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;
import java.lang.reflect.Constructor;
import java.util.Map;

public class AppCompatViewInflater
{
    private static final String LOG_TAG = "AppCompatViewInflater";
    private static final String[] sClassPrefixList;
    private static final Map<String, Constructor<? extends View>> sConstructorMap;
    private static final Class<?>[] sConstructorSignature;
    private static final int[] sOnClickAttrs;
    private final Object[] mConstructorArgs;
    
    static {
        sConstructorSignature = new Class[] { Context.class, AttributeSet.class };
        sOnClickAttrs = new int[] { 16843375 };
        sClassPrefixList = new String[] { "android.widget.", "android.view.", "android.webkit." };
        sConstructorMap = new ArrayMap<String, Constructor<? extends View>>();
    }
    
    public AppCompatViewInflater() {
        this.mConstructorArgs = new Object[2];
    }
    
    private void checkOnClickListener(final View view, final AttributeSet set) {
        final Context context = view.getContext();
        if (!(context instanceof ContextWrapper)) {
            return;
        }
        if (Build$VERSION.SDK_INT >= 15 && !ViewCompat.hasOnClickListeners(view)) {
            return;
        }
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, AppCompatViewInflater.sOnClickAttrs);
        final String string = obtainStyledAttributes.getString(0);
        if (string != null) {
            view.setOnClickListener((View$OnClickListener)new DeclaredOnClickListener(view, string));
        }
        obtainStyledAttributes.recycle();
    }
    
    private View createViewByPrefix(final Context context, final String s, String string) throws ClassNotFoundException, InflateException {
        while (true) {
            Label_0093: {
                Constructor<? extends View> constructor;
                if ((constructor = AppCompatViewInflater.sConstructorMap.get(s)) != null) {
                    break Label_0093;
                }
                if (string == null) {
                    break Label_0093;
                }
                try {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(string);
                    sb.append(s);
                    string = sb.toString();
                    constructor = Class.forName(string, false, context.getClassLoader()).asSubclass(View.class).getConstructor(AppCompatViewInflater.sConstructorSignature);
                    AppCompatViewInflater.sConstructorMap.put(s, constructor);
                    constructor.setAccessible(true);
                    return (View)constructor.newInstance(this.mConstructorArgs);
                }
                catch (Exception ex) {
                    return null;
                }
            }
            string = s;
            continue;
        }
    }
    
    private View createViewFromTag(final Context context, final String s, final AttributeSet set) {
        String attributeValue = s;
        if (s.equals("view")) {
            attributeValue = set.getAttributeValue((String)null, "class");
        }
        try {
            this.mConstructorArgs[0] = context;
            this.mConstructorArgs[1] = set;
            if (-1 == attributeValue.indexOf(46)) {
                for (int i = 0; i < AppCompatViewInflater.sClassPrefixList.length; ++i) {
                    final View viewByPrefix = this.createViewByPrefix(context, attributeValue, AppCompatViewInflater.sClassPrefixList[i]);
                    if (viewByPrefix != null) {
                        return viewByPrefix;
                    }
                }
                return null;
            }
            return this.createViewByPrefix(context, attributeValue, null);
        }
        catch (Exception ex) {
            return null;
        }
        finally {
            final Object[] mConstructorArgs = this.mConstructorArgs;
            mConstructorArgs[1] = (mConstructorArgs[0] = null);
        }
    }
    
    private static Context themifyContext(final Context context, final AttributeSet set, final boolean b, final boolean b2) {
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R$styleable.View, 0, 0);
        int resourceId = 0;
        if (b) {
            resourceId = obtainStyledAttributes.getResourceId(R$styleable.View_android_theme, 0);
        }
        int n = resourceId;
        if (b2 && (n = resourceId) == 0) {
            final int resourceId2 = obtainStyledAttributes.getResourceId(R$styleable.View_theme, 0);
            if ((n = resourceId2) != 0) {
                Log.i("AppCompatViewInflater", "app:theme is now deprecated. Please move to using android:theme instead.");
                n = resourceId2;
            }
        }
        obtainStyledAttributes.recycle();
        Object o = context;
        if (n != 0) {
            if (context instanceof ContextThemeWrapper) {
                o = context;
                if (((ContextThemeWrapper)context).getThemeResId() == n) {
                    return (Context)o;
                }
            }
            o = new ContextThemeWrapper(context, n);
        }
        return (Context)o;
    }
    
    private void verifyNotNull(final View view, final String s) {
        if (view != null) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getName());
        sb.append(" asked to inflate view for <");
        sb.append(s);
        sb.append(">, but returned null");
        throw new IllegalStateException(sb.toString());
    }
    
    protected AppCompatAutoCompleteTextView createAutoCompleteTextView(final Context context, final AttributeSet set) {
        return new AppCompatAutoCompleteTextView(context, set);
    }
    
    protected AppCompatButton createButton(final Context context, final AttributeSet set) {
        return new AppCompatButton(context, set);
    }
    
    protected AppCompatCheckBox createCheckBox(final Context context, final AttributeSet set) {
        return new AppCompatCheckBox(context, set);
    }
    
    protected AppCompatCheckedTextView createCheckedTextView(final Context context, final AttributeSet set) {
        return new AppCompatCheckedTextView(context, set);
    }
    
    protected AppCompatEditText createEditText(final Context context, final AttributeSet set) {
        return new AppCompatEditText(context, set);
    }
    
    protected AppCompatImageButton createImageButton(final Context context, final AttributeSet set) {
        return new AppCompatImageButton(context, set);
    }
    
    protected AppCompatImageView createImageView(final Context context, final AttributeSet set) {
        return new AppCompatImageView(context, set);
    }
    
    protected AppCompatMultiAutoCompleteTextView createMultiAutoCompleteTextView(final Context context, final AttributeSet set) {
        return new AppCompatMultiAutoCompleteTextView(context, set);
    }
    
    protected AppCompatRadioButton createRadioButton(final Context context, final AttributeSet set) {
        return new AppCompatRadioButton(context, set);
    }
    
    protected AppCompatRatingBar createRatingBar(final Context context, final AttributeSet set) {
        return new AppCompatRatingBar(context, set);
    }
    
    protected AppCompatSeekBar createSeekBar(final Context context, final AttributeSet set) {
        return new AppCompatSeekBar(context, set);
    }
    
    protected AppCompatSpinner createSpinner(final Context context, final AttributeSet set) {
        return new AppCompatSpinner(context, set);
    }
    
    protected AppCompatTextView createTextView(final Context context, final AttributeSet set) {
        return new AppCompatTextView(context, set);
    }
    
    protected AppCompatToggleButton createToggleButton(final Context context, final AttributeSet set) {
        return new AppCompatToggleButton(context, set);
    }
    
    protected View createView(final Context context, final String s, final AttributeSet set) {
        return null;
    }
    
    final View createView(View o, final String s, final Context context, final AttributeSet set, final boolean b, final boolean b2, final boolean b3, final boolean b4) {
        Context context2 = context;
        if (b) {
            context2 = context;
            if (o != null) {
                context2 = ((View)o).getContext();
            }
        }
        Context themifyContext = null;
        Label_0046: {
            if (!b2) {
                themifyContext = context2;
                if (!b3) {
                    break Label_0046;
                }
            }
            themifyContext = themifyContext(context2, set, b2, b3);
        }
        Context wrap = themifyContext;
        if (b4) {
            wrap = TintContextWrapper.wrap(themifyContext);
        }
        switch (s) {
            default: {
                o = this.createView(wrap, s, set);
                break;
            }
            case "ToggleButton": {
                o = this.createToggleButton(wrap, set);
                this.verifyNotNull((View)o, s);
                break;
            }
            case "SeekBar": {
                o = this.createSeekBar(wrap, set);
                this.verifyNotNull((View)o, s);
                break;
            }
            case "RatingBar": {
                o = this.createRatingBar(wrap, set);
                this.verifyNotNull((View)o, s);
                break;
            }
            case "MultiAutoCompleteTextView": {
                o = this.createMultiAutoCompleteTextView(wrap, set);
                this.verifyNotNull((View)o, s);
                break;
            }
            case "AutoCompleteTextView": {
                o = this.createAutoCompleteTextView(wrap, set);
                this.verifyNotNull((View)o, s);
                break;
            }
            case "CheckedTextView": {
                o = this.createCheckedTextView(wrap, set);
                this.verifyNotNull((View)o, s);
                break;
            }
            case "RadioButton": {
                o = this.createRadioButton(wrap, set);
                this.verifyNotNull((View)o, s);
                break;
            }
            case "CheckBox": {
                o = this.createCheckBox(wrap, set);
                this.verifyNotNull((View)o, s);
                break;
            }
            case "ImageButton": {
                o = this.createImageButton(wrap, set);
                this.verifyNotNull((View)o, s);
                break;
            }
            case "Spinner": {
                o = this.createSpinner(wrap, set);
                this.verifyNotNull((View)o, s);
                break;
            }
            case "EditText": {
                o = this.createEditText(wrap, set);
                this.verifyNotNull((View)o, s);
                break;
            }
            case "Button": {
                o = this.createButton(wrap, set);
                this.verifyNotNull((View)o, s);
                break;
            }
            case "ImageView": {
                o = this.createImageView(wrap, set);
                this.verifyNotNull((View)o, s);
                break;
            }
            case "TextView": {
                o = this.createTextView(wrap, set);
                this.verifyNotNull((View)o, s);
                break;
            }
        }
        Object viewFromTag = o;
        if (o == null) {
            viewFromTag = o;
            if (context != wrap) {
                viewFromTag = this.createViewFromTag(wrap, s, set);
            }
        }
        if (viewFromTag != null) {
            this.checkOnClickListener((View)viewFromTag, set);
        }
        return (View)viewFromTag;
    }
    
    private static class DeclaredOnClickListener implements View$OnClickListener
    {
        private final View mHostView;
        private final String mMethodName;
        private Context mResolvedContext;
        private Method mResolvedMethod;
        
        public DeclaredOnClickListener(final View mHostView, final String mMethodName) {
            this.mHostView = mHostView;
            this.mMethodName = mMethodName;
        }
        
        private void resolveMethod(Context baseContext, final String s) {
            while (baseContext != null) {
                try {
                    if (!baseContext.isRestricted()) {
                        final Method method = baseContext.getClass().getMethod(this.mMethodName, View.class);
                        if (method != null) {
                            this.mResolvedMethod = method;
                            this.mResolvedContext = baseContext;
                            return;
                        }
                    }
                }
                catch (NoSuchMethodException ex) {}
                if (baseContext instanceof ContextWrapper) {
                    baseContext = ((ContextWrapper)baseContext).getBaseContext();
                }
                else {
                    baseContext = null;
                }
            }
            final int id = this.mHostView.getId();
            String string;
            if (id == -1) {
                string = "";
            }
            else {
                final StringBuilder sb = new StringBuilder();
                sb.append(" with id '");
                sb.append(this.mHostView.getContext().getResources().getResourceEntryName(id));
                sb.append("'");
                string = sb.toString();
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Could not find method ");
            sb2.append(this.mMethodName);
            sb2.append("(View) in a parent or ancestor Context for android:onClick attribute defined on view ");
            sb2.append(this.mHostView.getClass());
            sb2.append(string);
            throw new IllegalStateException(sb2.toString());
        }
        
        public void onClick(final View view) {
            if (this.mResolvedMethod == null) {
                this.resolveMethod(this.mHostView.getContext(), this.mMethodName);
            }
            try {
                this.mResolvedMethod.invoke(this.mResolvedContext, view);
            }
            catch (InvocationTargetException ex) {
                throw new IllegalStateException("Could not execute method for android:onClick", ex);
            }
            catch (IllegalAccessException ex2) {
                throw new IllegalStateException("Could not execute non-public method for android:onClick", ex2);
            }
        }
    }
}
