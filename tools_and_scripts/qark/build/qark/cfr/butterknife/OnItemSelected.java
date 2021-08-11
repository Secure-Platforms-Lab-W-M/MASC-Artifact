/*
 * Decompiled with CFR 0_124.
 */
package butterknife;

import butterknife.internal.ListenerClass;
import butterknife.internal.ListenerMethod;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ListenerClass(callbacks=Callback.class, setter="setOnItemSelectedListener", targetType="android.widget.AdapterView<?>", type="android.widget.AdapterView.OnItemSelectedListener")
@Retention(value=RetentionPolicy.CLASS)
@Target(value={ElementType.METHOD})
public @interface OnItemSelected {
    public Callback callback() default Callback.ITEM_SELECTED;

    public int[] value() default {-1};

    public static final class Callback
    extends Enum<Callback> {
        private static final /* synthetic */ Callback[] $VALUES;
        @ListenerMethod(name="onItemSelected", parameters={"android.widget.AdapterView<?>", "android.view.View", "int", "long"})
        public static final /* enum */ Callback ITEM_SELECTED;
        @ListenerMethod(name="onNothingSelected", parameters={"android.widget.AdapterView<?>"})
        public static final /* enum */ Callback NOTHING_SELECTED;

        static {
            Callback callback;
            ITEM_SELECTED = new Callback();
            NOTHING_SELECTED = callback = new Callback();
            $VALUES = new Callback[]{ITEM_SELECTED, callback};
        }

        private Callback() {
        }

        public static Callback valueOf(String string2) {
            return Enum.valueOf(Callback.class, string2);
        }

        public static Callback[] values() {
            return (Callback[])$VALUES.clone();
        }
    }

}

