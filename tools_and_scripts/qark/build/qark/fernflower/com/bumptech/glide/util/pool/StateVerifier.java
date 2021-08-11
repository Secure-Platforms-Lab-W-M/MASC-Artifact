package com.bumptech.glide.util.pool;

public abstract class StateVerifier {
   private static final boolean DEBUG = false;

   private StateVerifier() {
   }

   // $FF: synthetic method
   StateVerifier(Object var1) {
      this();
   }

   public static StateVerifier newInstance() {
      return new StateVerifier.DefaultStateVerifier();
   }

   abstract void setRecycled(boolean var1);

   public abstract void throwIfRecycled();

   private static class DebugStateVerifier extends StateVerifier {
      private volatile RuntimeException recycledAtStackTraceException;

      DebugStateVerifier() {
         super(null);
      }

      void setRecycled(boolean var1) {
         if (var1) {
            this.recycledAtStackTraceException = new RuntimeException("Released");
         } else {
            this.recycledAtStackTraceException = null;
         }
      }

      public void throwIfRecycled() {
         if (this.recycledAtStackTraceException != null) {
            throw new IllegalStateException("Already released", this.recycledAtStackTraceException);
         }
      }
   }

   private static class DefaultStateVerifier extends StateVerifier {
      private volatile boolean isReleased;

      DefaultStateVerifier() {
         super(null);
      }

      public void setRecycled(boolean var1) {
         this.isReleased = var1;
      }

      public void throwIfRecycled() {
         if (this.isReleased) {
            throw new IllegalStateException("Already released");
         }
      }
   }
}
