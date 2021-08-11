package org.apache.http.annotation;

public enum ThreadingBehavior {
   IMMUTABLE,
   IMMUTABLE_CONDITIONAL,
   SAFE,
   SAFE_CONDITIONAL,
   UNSAFE;

   static {
      ThreadingBehavior var0 = new ThreadingBehavior("UNSAFE", 4);
      UNSAFE = var0;
   }
}
