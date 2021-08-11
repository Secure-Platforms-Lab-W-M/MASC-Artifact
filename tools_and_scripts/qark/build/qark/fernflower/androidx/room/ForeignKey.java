package androidx.room;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public @interface ForeignKey {
   int CASCADE = 5;
   int NO_ACTION = 1;
   int RESTRICT = 2;
   int SET_DEFAULT = 4;
   int SET_NULL = 3;

   String[] childColumns();

   boolean deferred() default false;

   Class entity();

   int onDelete() default 1;

   int onUpdate() default 1;

   String[] parentColumns();

   @Retention(RetentionPolicy.SOURCE)
   public @interface Action {
   }
}
