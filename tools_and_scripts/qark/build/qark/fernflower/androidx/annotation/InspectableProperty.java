package androidx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.METHOD})
public @interface InspectableProperty {
   int attributeId() default 0;

   InspectableProperty.EnumEntry[] enumMapping() default {};

   InspectableProperty.FlagEntry[] flagMapping() default {};

   boolean hasAttributeId() default true;

   String name() default "";

   InspectableProperty.ValueType valueType() default InspectableProperty.ValueType.INFERRED;

   @Retention(RetentionPolicy.SOURCE)
   @Target({ElementType.TYPE})
   public @interface EnumEntry {
      String name();

      int value();
   }

   @Retention(RetentionPolicy.SOURCE)
   @Target({ElementType.TYPE})
   public @interface FlagEntry {
      int mask() default 0;

      String name();

      int target();
   }

   public static enum ValueType {
      COLOR,
      GRAVITY,
      INFERRED,
      INT_ENUM,
      INT_FLAG,
      NONE,
      RESOURCE_ID;

      static {
         InspectableProperty.ValueType var0 = new InspectableProperty.ValueType("RESOURCE_ID", 6);
         RESOURCE_ID = var0;
      }
   }
}
