package androidx.room;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE})
public @interface Entity {
   ForeignKey[] foreignKeys() default {};

   String[] ignoredColumns() default {};

   Index[] indices() default {};

   boolean inheritSuperIndices() default false;

   String[] primaryKeys() default {};

   String tableName() default "";
}
