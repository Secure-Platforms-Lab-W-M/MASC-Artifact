package android.support.v7.util;

public class BatchingListUpdateCallback implements ListUpdateCallback {
   private static final int TYPE_ADD = 1;
   private static final int TYPE_CHANGE = 3;
   private static final int TYPE_NONE = 0;
   private static final int TYPE_REMOVE = 2;
   int mLastEventCount = -1;
   Object mLastEventPayload = null;
   int mLastEventPosition = -1;
   int mLastEventType = 0;
   final ListUpdateCallback mWrapped;

   public BatchingListUpdateCallback(ListUpdateCallback var1) {
      this.mWrapped = var1;
   }

   public void dispatchLastEvent() {
      int var1 = this.mLastEventType;
      if (var1 != 0) {
         switch(var1) {
         case 1:
            this.mWrapped.onInserted(this.mLastEventPosition, this.mLastEventCount);
            break;
         case 2:
            this.mWrapped.onRemoved(this.mLastEventPosition, this.mLastEventCount);
            break;
         case 3:
            this.mWrapped.onChanged(this.mLastEventPosition, this.mLastEventCount, this.mLastEventPayload);
         }

         this.mLastEventPayload = null;
         this.mLastEventType = 0;
      }
   }

   public void onChanged(int var1, int var2, Object var3) {
      if (this.mLastEventType == 3) {
         int var4 = this.mLastEventPosition;
         int var5 = this.mLastEventCount;
         if (var1 <= var4 + var5 && var1 + var2 >= var4 && this.mLastEventPayload == var3) {
            this.mLastEventPosition = Math.min(var1, var4);
            this.mLastEventCount = Math.max(var5 + var4, var1 + var2) - this.mLastEventPosition;
            return;
         }
      }

      this.dispatchLastEvent();
      this.mLastEventPosition = var1;
      this.mLastEventCount = var2;
      this.mLastEventPayload = var3;
      this.mLastEventType = 3;
   }

   public void onInserted(int var1, int var2) {
      if (this.mLastEventType == 1) {
         int var3 = this.mLastEventPosition;
         if (var1 >= var3) {
            int var4 = this.mLastEventCount;
            if (var1 <= var3 + var4) {
               this.mLastEventCount = var4 + var2;
               this.mLastEventPosition = Math.min(var1, var3);
               return;
            }
         }
      }

      this.dispatchLastEvent();
      this.mLastEventPosition = var1;
      this.mLastEventCount = var2;
      this.mLastEventType = 1;
   }

   public void onMoved(int var1, int var2) {
      this.dispatchLastEvent();
      this.mWrapped.onMoved(var1, var2);
   }

   public void onRemoved(int var1, int var2) {
      if (this.mLastEventType == 2) {
         int var3 = this.mLastEventPosition;
         if (var3 >= var1 && var3 <= var1 + var2) {
            this.mLastEventCount += var2;
            this.mLastEventPosition = var1;
            return;
         }
      }

      this.dispatchLastEvent();
      this.mLastEventPosition = var1;
      this.mLastEventCount = var2;
      this.mLastEventType = 2;
   }
}
