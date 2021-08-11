/*
 * Decompiled with CFR 0_124.
 */
package butterknife.internal;

import butterknife.internal.ListenerMethod;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.ANNOTATION_TYPE})
public @interface ListenerClass {
    public Class<? extends Enum<?>> callbacks() default NONE.class;

    public ListenerMethod[] method() default {};

    public String remover() default "";

    public String setter();

    public String targetType();

    public String type();

    public static final class NONE
    extends Enum<NONE> {
        private static final /* synthetic */ NONE[] $VALUES;

        static {
            $VALUES = new NONE[0];
        }

        private NONE() {
        }

        public static NONE valueOf(String string2) {
            return Enum.valueOf(NONE.class, string2);
        }

        public static NONE[] values() {
            return (NONE[])$VALUES.clone();
        }
    }

}

