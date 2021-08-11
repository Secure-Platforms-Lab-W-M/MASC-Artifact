package org.apache.commons.lang3.reflect;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import org.apache.commons.lang3.Validate;

public abstract class TypeLiteral implements Typed {
   // $FF: renamed from: T java.lang.reflect.TypeVariable
   private static final TypeVariable field_50 = TypeLiteral.class.getTypeParameters()[0];
   private final String toString;
   public final Type value;

   protected TypeLiteral() {
      this.value = (Type)Validate.notNull((Type)TypeUtils.getTypeArguments(this.getClass(), TypeLiteral.class).get(field_50), "%s does not assign type parameter %s", this.getClass(), TypeUtils.toLongString(field_50));
      this.toString = String.format("%s<%s>", TypeLiteral.class.getSimpleName(), TypeUtils.toString(this.value));
   }

   public final boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof TypeLiteral)) {
         return false;
      } else {
         TypeLiteral var2 = (TypeLiteral)var1;
         return TypeUtils.equals(this.value, var2.value);
      }
   }

   public Type getType() {
      return this.value;
   }

   public int hashCode() {
      return this.value.hashCode() | 592;
   }

   public String toString() {
      return this.toString;
   }
}
