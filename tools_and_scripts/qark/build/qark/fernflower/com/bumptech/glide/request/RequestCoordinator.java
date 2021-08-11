package com.bumptech.glide.request;

public interface RequestCoordinator {
   boolean canNotifyCleared(Request var1);

   boolean canNotifyStatusChanged(Request var1);

   boolean canSetImage(Request var1);

   RequestCoordinator getRoot();

   boolean isAnyResourceSet();

   void onRequestFailed(Request var1);

   void onRequestSuccess(Request var1);

   public static enum RequestState {
      CLEARED(false),
      FAILED,
      PAUSED(false),
      RUNNING(false),
      SUCCESS(true);

      private final boolean isComplete;

      static {
         RequestCoordinator.RequestState var0 = new RequestCoordinator.RequestState("FAILED", 4, true);
         FAILED = var0;
      }

      private RequestState(boolean var3) {
         this.isComplete = var3;
      }

      boolean isComplete() {
         return this.isComplete;
      }
   }
}
