/*
 * Decompiled with CFR 0_124.
 */
package androidx.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.SOURCE)
@Target(value={ElementType.METHOD})
public @interface InspectableProperty {
    public int attributeId() default 0;

    public EnumEntry[] enumMapping() default {};

    public FlagEntry[] flagMapping() default {};

    public boolean hasAttributeId() default true;

    public String name() default "";

    public ValueType valueType() default ValueType.INFERRED;

    @Retention(value=RetentionPolicy.SOURCE)
    @Target(value={ElementType.TYPE})
    public static @interface EnumEntry {
        public String name();

        public int value();
    }

    @Retention(value=RetentionPolicy.SOURCE)
    @Target(value={ElementType.TYPE})
    public static @interface FlagEntry {
        public int mask() default 0;

        public String name();

        public int target();
    }

    public static final class ValueType
    extends Enum<ValueType> {
        private static final /* synthetic */ ValueType[] $VALUES;
        public static final /* enum */ ValueType COLOR;
        public static final /* enum */ ValueType GRAVITY;
        public static final /* enum */ ValueType INFERRED;
        public static final /* enum */ ValueType INT_ENUM;
        public static final /* enum */ ValueType INT_FLAG;
        public static final /* enum */ ValueType NONE;
        public static final /* enum */ ValueType RESOURCE_ID;

        static {
            ValueType valueType;
            NONE = new ValueType();
            INFERRED = new ValueType();
            INT_ENUM = new ValueType();
            INT_FLAG = new ValueType();
            COLOR = new ValueType();
            GRAVITY = new ValueType();
            RESOURCE_ID = valueType = new ValueType();
            $VALUES = new ValueType[]{NONE, INFERRED, INT_ENUM, INT_FLAG, COLOR, GRAVITY, valueType};
        }

        private ValueType() {
        }

        public static ValueType valueOf(String string) {
            return Enum.valueOf(ValueType.class, string);
        }

        public static ValueType[] values() {
            return (ValueType[])$VALUES.clone();
        }
    }

}

