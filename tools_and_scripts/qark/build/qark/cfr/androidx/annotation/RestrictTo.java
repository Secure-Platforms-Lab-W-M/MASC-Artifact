/*
 * Decompiled with CFR 0_124.
 */
package androidx.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.CLASS)
@Target(value={ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.PACKAGE})
public @interface RestrictTo {
    public Scope[] value();

    public static final class Scope
    extends Enum<Scope> {
        private static final /* synthetic */ Scope[] $VALUES;
        @Deprecated
        public static final /* enum */ Scope GROUP_ID;
        public static final /* enum */ Scope LIBRARY;
        public static final /* enum */ Scope LIBRARY_GROUP;
        public static final /* enum */ Scope LIBRARY_GROUP_PREFIX;
        public static final /* enum */ Scope SUBCLASSES;
        public static final /* enum */ Scope TESTS;

        static {
            Scope scope;
            LIBRARY = new Scope();
            LIBRARY_GROUP = new Scope();
            LIBRARY_GROUP_PREFIX = new Scope();
            GROUP_ID = new Scope();
            TESTS = new Scope();
            SUBCLASSES = scope = new Scope();
            $VALUES = new Scope[]{LIBRARY, LIBRARY_GROUP, LIBRARY_GROUP_PREFIX, GROUP_ID, TESTS, scope};
        }

        private Scope() {
        }

        public static Scope valueOf(String string) {
            return Enum.valueOf(Scope.class, string);
        }

        public static Scope[] values() {
            return (Scope[])$VALUES.clone();
        }
    }

}

