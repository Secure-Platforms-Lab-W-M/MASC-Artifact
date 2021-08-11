// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.app;

import java.lang.reflect.InvocationTargetException;
import android.support.annotation.Nullable;
import java.lang.reflect.Method;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.TintContextWrapper;
import android.support.annotation.NonNull;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.support.v7.appcompat.R;
import android.view.InflateException;
import android.content.res.TypedArray;
import android.view.View$OnClickListener;
import android.support.v4.view.ViewCompat;
import android.os.Build$VERSION;
import android.content.ContextWrapper;
import android.support.v4.util.ArrayMap;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;
import java.lang.reflect.Constructor;
import java.util.Map;

class AppCompatViewInflater
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
    
    AppCompatViewInflater() {
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
    
    private View createView(final Context context, final String s, final String s2) throws ClassNotFoundException, InflateException {
        final Constructor<? extends View> constructor = AppCompatViewInflater.sConstructorMap.get(s);
        Label_0089: {
            if (constructor != null) {
                final Constructor<? extends View> constructor2 = constructor;
                break Label_0089;
            }
            while (true) {
                while (true) {
                    try {
                        final ClassLoader classLoader = context.getClassLoader();
                        if (s2 != null) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append(s2);
                            sb.append(s);
                            final String string = sb.toString();
                            final Constructor<? extends View> constructor2 = classLoader.loadClass(string).asSubclass(View.class).getConstructor(AppCompatViewInflater.sConstructorSignature);
                            AppCompatViewInflater.sConstructorMap.put(s, constructor2);
                            constructor2.setAccessible(true);
                            return (View)constructor2.newInstance(this.mConstructorArgs);
                        }
                    }
                    catch (Exception ex) {
                        return null;
                    }
                    final String string = s;
                    continue;
                }
            }
        }
    }
    
    private View createViewFromTag(final Context context, String attributeValue, final AttributeSet set) {
        if (attributeValue.equals("view")) {
            attributeValue = set.getAttributeValue((String)null, "class");
        }
        try {
            this.mConstructorArgs[0] = context;
            this.mConstructorArgs[1] = set;
            if (-1 == attributeValue.indexOf(46)) {
                for (int i = 0; i < AppCompatViewInflater.sClassPrefixList.length; ++i) {
                    final View view = this.createView(context, attributeValue, AppCompatViewInflater.sClassPrefixList[i]);
                    if (view != null) {
                        return view;
                    }
                }
                return null;
            }
            return this.createView(context, attributeValue, null);
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
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.View, 0, 0);
        int n = 0;
        if (b) {
            n = obtainStyledAttributes.getResourceId(R.styleable.View_android_theme, 0);
        }
        if (b2 && n == 0) {
            n = obtainStyledAttributes.getResourceId(R.styleable.View_theme, 0);
            if (n != 0) {
                Log.i("AppCompatViewInflater", "app:theme is now deprecated. Please move to using android:theme instead.");
            }
        }
        obtainStyledAttributes.recycle();
        if (n == 0) {
            return context;
        }
        if (context instanceof ContextThemeWrapper && ((ContextThemeWrapper)context).getThemeResId() == n) {
            return context;
        }
        return (Context)new ContextThemeWrapper(context, n);
    }
    
    public final View createView(View viewFromTag, final String s, @NonNull final Context context, @NonNull final AttributeSet set, final boolean b, final boolean b2, final boolean b3, final boolean b4) {
        Context context2;
        if (b && viewFromTag != null) {
            context2 = ((View)viewFromTag).getContext();
        }
        else {
            context2 = context;
        }
        if (b2 || b3) {
            context2 = themifyContext(context2, set, b2, b3);
        }
        Context wrap;
        if (b4) {
            wrap = TintContextWrapper.wrap(context2);
        }
        else {
            wrap = context2;
        }
        viewFromTag = null;
        switch (s) {
            case "SeekBar": {
                viewFromTag = new AppCompatSeekBar(wrap, set);
                break;
            }
            case "RatingBar": {
                viewFromTag = new AppCompatRatingBar(wrap, set);
                break;
            }
            case "MultiAutoCompleteTextView": {
                viewFromTag = new AppCompatMultiAutoCompleteTextView(wrap, set);
                break;
            }
            case "AutoCompleteTextView": {
                viewFromTag = new AppCompatAutoCompleteTextView(wrap, set);
                break;
            }
            case "CheckedTextView": {
                viewFromTag = new AppCompatCheckedTextView(wrap, set);
                break;
            }
            case "RadioButton": {
                viewFromTag = new AppCompatRadioButton(wrap, set);
                break;
            }
            case "CheckBox": {
                viewFromTag = new AppCompatCheckBox(wrap, set);
                break;
            }
            case "ImageButton": {
                viewFromTag = new AppCompatImageButton(wrap, set);
                break;
            }
            case "Spinner": {
                viewFromTag = new AppCompatSpinner(wrap, set);
                break;
            }
            case "EditText": {
                viewFromTag = new AppCompatEditText(wrap, set);
                break;
            }
            case "Button": {
                viewFromTag = new AppCompatButton(wrap, set);
                break;
            }
            case "ImageView": {
                viewFromTag = new AppCompatImageView(wrap, set);
                break;
            }
            case "TextView": {
                viewFromTag = new AppCompatTextView(wrap, set);
                break;
            }
        }
        if (viewFromTag == null && context != wrap) {
            viewFromTag = this.createViewFromTag(wrap, s, set);
        }
        if (viewFromTag != null) {
            this.checkOnClickListener((View)viewFromTag, set);
            return (View)viewFromTag;
        }
        return (View)viewFromTag;
    }
    
    private static class DeclaredOnClickListener implements View$OnClickListener
    {
        private final View mHostView;
        private final String mMethodName;
        private Context mResolvedContext;
        private Method mResolvedMethod;
        
        public DeclaredOnClickListener(@NonNull final View mHostView, @NonNull final String mMethodName) {
            this.mHostView = mHostView;
            this.mMethodName = mMethodName;
        }
        
        @NonNull
        private void resolveMethod(@Nullable Context baseContext, @NonNull final String s) {
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
            sb2.append("(View) in a parent or ancestor Context for android:onClick ");
            sb2.append("attribute defined on view ");
            sb2.append(this.mHostView.getClass());
            sb2.append(string);
            throw new IllegalStateException(sb2.toString());
        }
        
        public void onClick(@NonNull final View view) {
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
