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

@ListenerClass(callbacks=Callback.class, remover="removeOnPageChangeListener", setter="addOnPageChangeListener", targetType="androidx.viewpager.widget.ViewPager", type="androidx.viewpager.widget.ViewPager.OnPageChangeListener")
@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD})
public @interface OnPageChange {
    public Callback callback() default Callback.PAGE_SELECTED;

    public int[] value() default {-1};

    public static final class Callback
    extends Enum<Callback> {
        private static final /* synthetic */ Callback[] $VALUES;
        @ListenerMethod(name="onPageScrolled", parameters={"int", "float", "int"})
        public static final /* enum */ Callback PAGE_SCROLLED;
        @ListenerMethod(name="onPageScrollStateChanged", parameters={"int"})
        public static final /* enum */ Callback PAGE_SCROLL_STATE_CHANGED;
        @ListenerMethod(name="onPageSelected", parameters={"int"})
        public static final /* enum */ Callback PAGE_SELECTED;

        static {
            Callback callback;
            PAGE_SELECTED = new Callback();
            PAGE_SCROLLED = new Callback();
            PAGE_SCROLL_STATE_CHANGED = callback = new Callback();
            $VALUES = new Callback[]{PAGE_SELECTED, PAGE_SCROLLED, callback};
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

