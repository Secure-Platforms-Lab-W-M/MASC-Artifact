package androidx.room;

public @interface Update {
   int onConflict() default 3;
}
