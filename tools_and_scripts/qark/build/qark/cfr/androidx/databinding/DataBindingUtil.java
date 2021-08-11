/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewParent
 *  android.view.Window
 *  androidx.databinding.DataBinderMapperImpl
 *  androidx.databinding.DataBindingComponent
 */
package androidx.databinding;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBinderMapperImpl;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;

public class DataBindingUtil {
    private static DataBindingComponent sDefaultComponent;
    private static DataBinderMapper sMapper;

    static {
        sMapper = new DataBinderMapperImpl();
        sDefaultComponent = null;
    }

    private DataBindingUtil() {
    }

    public static <T extends ViewDataBinding> T bind(View view) {
        return DataBindingUtil.bind(view, sDefaultComponent);
    }

    public static <T extends ViewDataBinding> T bind(View object, DataBindingComponent dataBindingComponent) {
        Object object2 = DataBindingUtil.getBinding((View)object);
        if (object2 != null) {
            return (T)object2;
        }
        object2 = object.getTag();
        if (object2 instanceof String) {
            String string2 = (String)object2;
            int n = sMapper.getLayoutId(string2);
            if (n != 0) {
                return (T)sMapper.getDataBinder(dataBindingComponent, (View)object, n);
            }
            object = new StringBuilder();
            object.append("View is not a binding layout. Tag: ");
            object.append(object2);
            throw new IllegalArgumentException(object.toString());
        }
        throw new IllegalArgumentException("View is not a binding layout");
    }

    static <T extends ViewDataBinding> T bind(DataBindingComponent dataBindingComponent, View view, int n) {
        return (T)sMapper.getDataBinder(dataBindingComponent, view, n);
    }

    static <T extends ViewDataBinding> T bind(DataBindingComponent dataBindingComponent, View[] arrview, int n) {
        return (T)sMapper.getDataBinder(dataBindingComponent, arrview, n);
    }

    private static <T extends ViewDataBinding> T bindToAddedViews(DataBindingComponent dataBindingComponent, ViewGroup viewGroup, int n, int n2) {
        int n3 = viewGroup.getChildCount();
        int n4 = n3 - n;
        if (n4 == 1) {
            return DataBindingUtil.bind(dataBindingComponent, viewGroup.getChildAt(n3 - 1), n2);
        }
        View[] arrview = new View[n4];
        for (n3 = 0; n3 < n4; ++n3) {
            arrview[n3] = viewGroup.getChildAt(n3 + n);
        }
        return DataBindingUtil.bind(dataBindingComponent, arrview, n2);
    }

    public static String convertBrIdToString(int n) {
        return sMapper.convertBrIdToString(n);
    }

    public static <T extends ViewDataBinding> T findBinding(View view) {
        while (view != null) {
            Object object = ViewDataBinding.getBinding(view);
            if (object != null) {
                return (T)object;
            }
            object = view.getTag();
            if (object instanceof String && (object = (String)object).startsWith("layout") && object.endsWith("_0")) {
                char c = object.charAt(6);
                int n = object.indexOf(47, 7);
                boolean bl = false;
                boolean bl2 = false;
                boolean bl3 = false;
                if (c == '/') {
                    if (n == -1) {
                        bl3 = true;
                    }
                } else {
                    bl3 = bl;
                    if (c == '-') {
                        bl3 = bl;
                        if (n != -1) {
                            bl3 = bl2;
                            if (object.indexOf(47, n + 1) == -1) {
                                bl3 = true;
                            }
                        }
                    }
                }
                if (bl3) {
                    return null;
                }
            }
            if ((view = view.getParent()) instanceof View) continue;
            view = null;
        }
        return null;
    }

    public static <T extends ViewDataBinding> T getBinding(View view) {
        return (T)ViewDataBinding.getBinding(view);
    }

    public static DataBindingComponent getDefaultComponent() {
        return sDefaultComponent;
    }

    public static <T extends ViewDataBinding> T inflate(LayoutInflater layoutInflater, int n, ViewGroup viewGroup, boolean bl) {
        return DataBindingUtil.inflate(layoutInflater, n, viewGroup, bl, sDefaultComponent);
    }

    public static <T extends ViewDataBinding> T inflate(LayoutInflater layoutInflater, int n, ViewGroup viewGroup, boolean bl, DataBindingComponent dataBindingComponent) {
        int n2 = 0;
        boolean bl2 = viewGroup != null && bl;
        if (bl2) {
            n2 = viewGroup.getChildCount();
        }
        layoutInflater = layoutInflater.inflate(n, viewGroup, bl);
        if (bl2) {
            return DataBindingUtil.bindToAddedViews(dataBindingComponent, viewGroup, n2, n);
        }
        return DataBindingUtil.bind(dataBindingComponent, (View)layoutInflater, n);
    }

    public static <T extends ViewDataBinding> T setContentView(Activity activity, int n) {
        return DataBindingUtil.setContentView(activity, n, sDefaultComponent);
    }

    public static <T extends ViewDataBinding> T setContentView(Activity activity, int n, DataBindingComponent dataBindingComponent) {
        activity.setContentView(n);
        return DataBindingUtil.bindToAddedViews(dataBindingComponent, (ViewGroup)activity.getWindow().getDecorView().findViewById(16908290), 0, n);
    }

    public static void setDefaultComponent(DataBindingComponent dataBindingComponent) {
        sDefaultComponent = dataBindingComponent;
    }
}

