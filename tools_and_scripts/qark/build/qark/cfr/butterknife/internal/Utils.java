/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.content.res.Resources$Theme
 *  android.graphics.drawable.Drawable
 *  android.util.TypedValue
 *  android.view.View
 */
package butterknife.internal;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import butterknife.internal.ImmutableList;
import java.lang.reflect.Array;
import java.util.List;

public final class Utils {
    private static final TypedValue VALUE = new TypedValue();

    private Utils() {
        throw new AssertionError((Object)"No instances.");
    }

    @SafeVarargs
    public static /* varargs */ <T> T[] arrayFilteringNull(T ... arrT) {
        Object object;
        int n = 0;
        int n2 = arrT.length;
        for (int i = 0; i < n2; ++i) {
            object = arrT[i];
            int n3 = n;
            if (object != null) {
                arrT[n] = object;
                n3 = n + 1;
            }
            n = n3;
        }
        if (n == n2) {
            return arrT;
        }
        object = (Object[])Array.newInstance(arrT.getClass().getComponentType(), n);
        System.arraycopy(arrT, 0, object, 0, n);
        return object;
    }

    public static <T> T castParam(Object object, String string2, int n, String string3, int n2, Class<T> class_) {
        try {
            object = class_.cast(object);
        }
        catch (ClassCastException classCastException) {
            class_ = new StringBuilder();
            class_.append("Parameter #");
            class_.append(n + 1);
            class_.append(" of method '");
            class_.append(string2);
            class_.append("' was of the wrong type for parameter #");
            class_.append(n2 + 1);
            class_.append(" of method '");
            class_.append(string3);
            class_.append("'. See cause for more info.");
            throw new IllegalStateException(class_.toString(), classCastException);
        }
        return (T)object;
    }

    public static <T> T castView(View object, int n, String string2, Class<T> class_) {
        try {
            class_ = class_.cast(object);
        }
        catch (ClassCastException classCastException) {
            object = Utils.getResourceEntryName((View)object, n);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("View '");
            stringBuilder.append((String)object);
            stringBuilder.append("' with ID ");
            stringBuilder.append(n);
            stringBuilder.append(" for ");
            stringBuilder.append(string2);
            stringBuilder.append(" was of the wrong type. See cause for more info.");
            throw new IllegalStateException(stringBuilder.toString(), classCastException);
        }
        return (T)class_;
    }

    public static <T> T findOptionalViewAsType(View view, int n, String string2, Class<T> class_) {
        return Utils.castView(view.findViewById(n), n, string2, class_);
    }

    public static View findRequiredView(View object, int n, String string2) {
        Object object2 = object.findViewById(n);
        if (object2 != null) {
            return object2;
        }
        object = Utils.getResourceEntryName((View)object, n);
        object2 = new StringBuilder();
        object2.append("Required view '");
        object2.append((String)object);
        object2.append("' with ID ");
        object2.append(n);
        object2.append(" for ");
        object2.append(string2);
        object2.append(" was not found. If this view is optional add '@Nullable' (fields) or '@Optional' (methods) annotation.");
        throw new IllegalStateException(object2.toString());
    }

    public static <T> T findRequiredViewAsType(View view, int n, String string2, Class<T> class_) {
        return Utils.castView(Utils.findRequiredView(view, n, string2), n, string2, class_);
    }

    public static float getFloat(Context object, int n) {
        TypedValue typedValue = VALUE;
        object.getResources().getValue(n, typedValue, true);
        if (typedValue.type == 4) {
            return typedValue.getFloat();
        }
        object = new StringBuilder();
        object.append("Resource ID #0x");
        object.append(Integer.toHexString(n));
        object.append(" type #0x");
        object.append(Integer.toHexString(typedValue.type));
        object.append(" is not valid");
        throw new Resources.NotFoundException(object.toString());
    }

    private static String getResourceEntryName(View view, int n) {
        if (view.isInEditMode()) {
            return "<unavailable while editing>";
        }
        return view.getContext().getResources().getResourceEntryName(n);
    }

    public static Drawable getTintedDrawable(Context context, int n, int n2) {
        if (context.getTheme().resolveAttribute(n2, VALUE, true)) {
            Drawable drawable2 = DrawableCompat.wrap(ContextCompat.getDrawable(context, n).mutate());
            DrawableCompat.setTint(drawable2, ContextCompat.getColor(context, Utils.VALUE.resourceId));
            return drawable2;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Required tint color attribute with name ");
        stringBuilder.append(context.getResources().getResourceEntryName(n2));
        stringBuilder.append(" and attribute ID ");
        stringBuilder.append(n2);
        stringBuilder.append(" was not found.");
        throw new Resources.NotFoundException(stringBuilder.toString());
    }

    @SafeVarargs
    public static /* varargs */ <T> List<T> listFilteringNull(T ... arrT) {
        return new ImmutableList<T>(Utils.arrayFilteringNull(arrT));
    }
}

