package okhttp3.internal.http2;

import java.util.Arrays;

public final class Settings {
   static final int COUNT = 10;
   static final int DEFAULT_INITIAL_WINDOW_SIZE = 65535;
   static final int ENABLE_PUSH = 2;
   static final int HEADER_TABLE_SIZE = 1;
   static final int INITIAL_WINDOW_SIZE = 7;
   static final int MAX_CONCURRENT_STREAMS = 4;
   static final int MAX_FRAME_SIZE = 5;
   static final int MAX_HEADER_LIST_SIZE = 6;
   private int set;
   private final int[] values = new int[10];

   void clear() {
      this.set = 0;
      Arrays.fill(this.values, 0);
   }

   int get(int var1) {
      return this.values[var1];
   }

   boolean getEnablePush(boolean var1) {
      int var2 = this.set;
      boolean var3 = false;
      if ((var2 & 4) != 0) {
         var2 = this.values[2];
      } else if (var1) {
         var2 = 1;
      } else {
         var2 = 0;
      }

      var1 = var3;
      if (var2 == 1) {
         var1 = true;
      }

      return var1;
   }

   int getHeaderTableSize() {
      return (this.set & 2) != 0 ? this.values[1] : -1;
   }

   int getInitialWindowSize() {
      return (this.set & 128) != 0 ? this.values[7] : '\uffff';
   }

   int getMaxConcurrentStreams(int var1) {
      return (this.set & 16) != 0 ? this.values[4] : var1;
   }

   int getMaxFrameSize(int var1) {
      return (this.set & 32) != 0 ? this.values[5] : var1;
   }

   int getMaxHeaderListSize(int var1) {
      return (this.set & 64) != 0 ? this.values[6] : var1;
   }

   boolean isSet(int var1) {
      return (this.set & 1 << var1) != 0;
   }

   void merge(Settings var1) {
      for(int var2 = 0; var2 < 10; ++var2) {
         if (var1.isSet(var2)) {
            this.set(var2, var1.get(var2));
         }
      }

   }

   Settings set(int var1, int var2) {
      int[] var3 = this.values;
      if (var1 >= var3.length) {
         return this;
      } else {
         this.set |= 1 << var1;
         var3[var1] = var2;
         return this;
      }
   }

   int size() {
      return Integer.bitCount(this.set);
   }
}
