/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.os.Bundle
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.view.LayoutInflater
 *  android.view.LayoutInflater$Factory2
 *  android.view.View
 *  androidx.fragment.R
 *  androidx.fragment.R$styleable
 */
package androidx.fragment.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import androidx.fragment.R;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.FragmentHostCallback;
import androidx.fragment.app.FragmentManager;

class FragmentLayoutInflaterFactory
implements LayoutInflater.Factory2 {
    private static final String TAG = "FragmentManager";
    private final FragmentManager mFragmentManager;

    FragmentLayoutInflaterFactory(FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
    }

    public View onCreateView(View object, String object2, Context context, AttributeSet attributeSet) {
        block19 : {
            int n;
            String string2;
            String string3;
            int n2;
            block22 : {
                block21 : {
                    block20 : {
                        if (FragmentContainerView.class.getName().equals(object2)) {
                            return new FragmentContainerView(context, attributeSet, this.mFragmentManager);
                        }
                        boolean bl = "fragment".equals(object2);
                        object2 = null;
                        if (!bl) {
                            return null;
                        }
                        string2 = attributeSet.getAttributeValue(null, "class");
                        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.Fragment);
                        string3 = string2;
                        if (string2 == null) {
                            string3 = typedArray.getString(R.styleable.Fragment_android_name);
                        }
                        n = typedArray.getResourceId(R.styleable.Fragment_android_id, -1);
                        string2 = typedArray.getString(R.styleable.Fragment_android_tag);
                        typedArray.recycle();
                        if (string3 == null) break block19;
                        if (!FragmentFactory.isFragmentClass(context.getClassLoader(), string3)) {
                            return null;
                        }
                        n2 = object != null ? object.getId() : 0;
                        if (n2 == -1 && n == -1 && string2 == null) {
                            object = new StringBuilder();
                            object.append(attributeSet.getPositionDescription());
                            object.append(": Must specify unique android:id, android:tag, or have a parent with an id for ");
                            object.append(string3);
                            throw new IllegalArgumentException(object.toString());
                        }
                        if (n != -1) {
                            object2 = this.mFragmentManager.findFragmentById(n);
                        }
                        object = object2;
                        if (object2 == null) {
                            object = object2;
                            if (string2 != null) {
                                object = this.mFragmentManager.findFragmentByTag(string2);
                            }
                        }
                        object2 = object;
                        if (object == null) {
                            object2 = object;
                            if (n2 != -1) {
                                object2 = this.mFragmentManager.findFragmentById(n2);
                            }
                        }
                        if (FragmentManager.isLoggingEnabled(2)) {
                            object = new StringBuilder();
                            object.append("onCreateView: id=0x");
                            object.append(Integer.toHexString(n));
                            object.append(" fname=");
                            object.append(string3);
                            object.append(" existing=");
                            object.append(object2);
                            Log.v((String)"FragmentManager", (String)object.toString());
                        }
                        if (object2 != null) break block20;
                        object2 = this.mFragmentManager.getFragmentFactory().instantiate(context.getClassLoader(), string3);
                        object2.mFromLayout = true;
                        int n3 = n != 0 ? n : n2;
                        object2.mFragmentId = n3;
                        object2.mContainerId = n2;
                        object2.mTag = string2;
                        object2.mInLayout = true;
                        object2.mFragmentManager = this.mFragmentManager;
                        object2.mHost = this.mFragmentManager.mHost;
                        object2.onInflate(this.mFragmentManager.mHost.getContext(), attributeSet, object2.mSavedFragmentState);
                        this.mFragmentManager.addFragment((Fragment)object2);
                        this.mFragmentManager.moveToState((Fragment)object2);
                        break block21;
                    }
                    if (object2.mInLayout) break block22;
                    object2.mInLayout = true;
                    object2.mHost = this.mFragmentManager.mHost;
                    object2.onInflate(this.mFragmentManager.mHost.getContext(), attributeSet, object2.mSavedFragmentState);
                }
                if (this.mFragmentManager.mCurState < 1 && object2.mFromLayout) {
                    this.mFragmentManager.moveToState((Fragment)object2, 1);
                } else {
                    this.mFragmentManager.moveToState((Fragment)object2);
                }
                if (object2.mView != null) {
                    if (n != 0) {
                        object2.mView.setId(n);
                    }
                    if (object2.mView.getTag() == null) {
                        object2.mView.setTag((Object)string2);
                    }
                    return object2.mView;
                }
                object = new StringBuilder();
                object.append("Fragment ");
                object.append(string3);
                object.append(" did not create a view.");
                throw new IllegalStateException(object.toString());
            }
            object = new StringBuilder();
            object.append(attributeSet.getPositionDescription());
            object.append(": Duplicate id 0x");
            object.append(Integer.toHexString(n));
            object.append(", tag ");
            object.append(string2);
            object.append(", or parent id 0x");
            object.append(Integer.toHexString(n2));
            object.append(" with another fragment for ");
            object.append(string3);
            throw new IllegalArgumentException(object.toString());
        }
        return null;
    }

    public View onCreateView(String string2, Context context, AttributeSet attributeSet) {
        return this.onCreateView(null, string2, context, attributeSet);
    }
}

