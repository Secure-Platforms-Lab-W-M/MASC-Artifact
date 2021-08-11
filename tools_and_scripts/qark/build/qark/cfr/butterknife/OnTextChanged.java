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

@ListenerClass(callbacks=Callback.class, remover="removeTextChangedListener", setter="addTextChangedListener", targetType="android.widget.TextView", type="android.text.TextWatcher")
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD})
public @interface OnTextChanged {
    public Callback callback() default Callback.TEXT_CHANGED;

    public int[] value() default {-1};

    public static final class Callback
    extends Enum<Callback> {
        private static final /* synthetic */ Callback[] $VALUES;
        @ListenerMethod(name="afterTextChanged", parameters={"android.text.Editable"})
        public static final /* enum */ Callback AFTER_TEXT_CHANGED;
        @ListenerMethod(name="beforeTextChanged", parameters={"java.lang.CharSequence", "int", "int", "int"})
        public static final /* enum */ Callback BEFORE_TEXT_CHANGED;
        @ListenerMethod(name="onTextChanged", parameters={"java.lang.CharSequence", "int", "int", "int"})
        public static final /* enum */ Callback TEXT_CHANGED;

        static {
            Callback callback;
            TEXT_CHANGED = new Callback();
            BEFORE_TEXT_CHANGED = new Callback();
            AFTER_TEXT_CHANGED = callback = new Callback();
            $VALUES = new Callback[]{TEXT_CHANGED, BEFORE_TEXT_CHANGED, callback};
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

