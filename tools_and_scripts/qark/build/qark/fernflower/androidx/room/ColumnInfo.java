package androidx.room;

import androidx.annotation.RequiresApi;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface ColumnInfo {
   int BINARY = 2;
   int BLOB = 5;
   String INHERIT_FIELD_NAME = "[field-name]";
   int INTEGER = 3;
   @RequiresApi(21)
   int LOCALIZED = 5;
   int NOCASE = 3;
   int REAL = 4;
   int RTRIM = 4;
   int TEXT = 2;
   int UNDEFINED = 1;
   @RequiresApi(21)
   int UNICODE = 6;
   int UNSPECIFIED = 1;

   @ColumnInfo.Collate
   int collate() default 1;

   boolean index() default false;

   String name() default "[field-name]";

   @ColumnInfo.SQLiteTypeAffinity
   int typeAffinity() default 1;

   public @interface Collate {
   }

   public @interface SQLiteTypeAffinity {
   }
}
