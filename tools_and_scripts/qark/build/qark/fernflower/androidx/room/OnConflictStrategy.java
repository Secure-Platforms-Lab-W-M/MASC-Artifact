package androidx.room;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface OnConflictStrategy {
   int ABORT = 3;
   int FAIL = 4;
   int IGNORE = 5;
   int REPLACE = 1;
   int ROLLBACK = 2;
}
