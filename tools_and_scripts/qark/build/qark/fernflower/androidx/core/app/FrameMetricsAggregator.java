package androidx.core.app;

import android.app.Activity;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Build.VERSION;
import android.util.SparseIntArray;
import android.view.FrameMetrics;
import android.view.Window;
import android.view.Window.OnFrameMetricsAvailableListener;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

public class FrameMetricsAggregator {
   public static final int ANIMATION_DURATION = 256;
   public static final int ANIMATION_INDEX = 8;
   public static final int COMMAND_DURATION = 32;
   public static final int COMMAND_INDEX = 5;
   public static final int DELAY_DURATION = 128;
   public static final int DELAY_INDEX = 7;
   public static final int DRAW_DURATION = 8;
   public static final int DRAW_INDEX = 3;
   public static final int EVERY_DURATION = 511;
   public static final int INPUT_DURATION = 2;
   public static final int INPUT_INDEX = 1;
   private static final int LAST_INDEX = 8;
   public static final int LAYOUT_MEASURE_DURATION = 4;
   public static final int LAYOUT_MEASURE_INDEX = 2;
   public static final int SWAP_DURATION = 64;
   public static final int SWAP_INDEX = 6;
   public static final int SYNC_DURATION = 16;
   public static final int SYNC_INDEX = 4;
   public static final int TOTAL_DURATION = 1;
   public static final int TOTAL_INDEX = 0;
   private FrameMetricsAggregator.FrameMetricsBaseImpl mInstance;

   public FrameMetricsAggregator() {
      this(1);
   }

   public FrameMetricsAggregator(int var1) {
      if (VERSION.SDK_INT >= 24) {
         this.mInstance = new FrameMetricsAggregator.FrameMetricsApi24Impl(var1);
      } else {
         this.mInstance = new FrameMetricsAggregator.FrameMetricsBaseImpl();
      }
   }

   public void add(Activity var1) {
      this.mInstance.add(var1);
   }

   public SparseIntArray[] getMetrics() {
      return this.mInstance.getMetrics();
   }

   public SparseIntArray[] remove(Activity var1) {
      return this.mInstance.remove(var1);
   }

   public SparseIntArray[] reset() {
      return this.mInstance.reset();
   }

   public SparseIntArray[] stop() {
      return this.mInstance.stop();
   }

   private static class FrameMetricsApi24Impl extends FrameMetricsAggregator.FrameMetricsBaseImpl {
      private static final int NANOS_PER_MS = 1000000;
      private static final int NANOS_ROUNDING_VALUE = 500000;
      private static Handler sHandler = null;
      private static HandlerThread sHandlerThread = null;
      private ArrayList mActivities = new ArrayList();
      OnFrameMetricsAvailableListener mListener = new OnFrameMetricsAvailableListener() {
         public void onFrameMetricsAvailable(Window var1, FrameMetrics var2, int var3) {
            FrameMetricsAggregator.FrameMetricsApi24Impl var4;
            if ((FrameMetricsApi24Impl.this.mTrackingFlags & 1) != 0) {
               var4 = FrameMetricsApi24Impl.this;
               var4.addDurationItem(var4.mMetrics[0], var2.getMetric(8));
            }

            if ((FrameMetricsApi24Impl.this.mTrackingFlags & 2) != 0) {
               var4 = FrameMetricsApi24Impl.this;
               var4.addDurationItem(var4.mMetrics[1], var2.getMetric(1));
            }

            if ((FrameMetricsApi24Impl.this.mTrackingFlags & 4) != 0) {
               var4 = FrameMetricsApi24Impl.this;
               var4.addDurationItem(var4.mMetrics[2], var2.getMetric(3));
            }

            if ((FrameMetricsApi24Impl.this.mTrackingFlags & 8) != 0) {
               var4 = FrameMetricsApi24Impl.this;
               var4.addDurationItem(var4.mMetrics[3], var2.getMetric(4));
            }

            if ((FrameMetricsApi24Impl.this.mTrackingFlags & 16) != 0) {
               var4 = FrameMetricsApi24Impl.this;
               var4.addDurationItem(var4.mMetrics[4], var2.getMetric(5));
            }

            if ((FrameMetricsApi24Impl.this.mTrackingFlags & 64) != 0) {
               var4 = FrameMetricsApi24Impl.this;
               var4.addDurationItem(var4.mMetrics[6], var2.getMetric(7));
            }

            if ((FrameMetricsApi24Impl.this.mTrackingFlags & 32) != 0) {
               var4 = FrameMetricsApi24Impl.this;
               var4.addDurationItem(var4.mMetrics[5], var2.getMetric(6));
            }

            if ((FrameMetricsApi24Impl.this.mTrackingFlags & 128) != 0) {
               var4 = FrameMetricsApi24Impl.this;
               var4.addDurationItem(var4.mMetrics[7], var2.getMetric(0));
            }

            if ((FrameMetricsApi24Impl.this.mTrackingFlags & 256) != 0) {
               var4 = FrameMetricsApi24Impl.this;
               var4.addDurationItem(var4.mMetrics[8], var2.getMetric(2));
            }

         }
      };
      SparseIntArray[] mMetrics = new SparseIntArray[9];
      int mTrackingFlags;

      FrameMetricsApi24Impl(int var1) {
         this.mTrackingFlags = var1;
      }

      public void add(Activity var1) {
         if (sHandlerThread == null) {
            HandlerThread var3 = new HandlerThread("FrameMetricsAggregator");
            sHandlerThread = var3;
            var3.start();
            sHandler = new Handler(sHandlerThread.getLooper());
         }

         for(int var2 = 0; var2 <= 8; ++var2) {
            SparseIntArray[] var4 = this.mMetrics;
            if (var4[var2] == null && (this.mTrackingFlags & 1 << var2) != 0) {
               var4[var2] = new SparseIntArray();
            }
         }

         var1.getWindow().addOnFrameMetricsAvailableListener(this.mListener, sHandler);
         this.mActivities.add(new WeakReference(var1));
      }

      void addDurationItem(SparseIntArray var1, long var2) {
         if (var1 != null) {
            int var4 = (int)((500000L + var2) / 1000000L);
            if (var2 >= 0L) {
               var1.put(var4, var1.get(var4) + 1);
            }
         }

      }

      public SparseIntArray[] getMetrics() {
         return this.mMetrics;
      }

      public SparseIntArray[] remove(Activity var1) {
         Iterator var2 = this.mActivities.iterator();

         while(var2.hasNext()) {
            WeakReference var3 = (WeakReference)var2.next();
            if (var3.get() == var1) {
               this.mActivities.remove(var3);
               break;
            }
         }

         var1.getWindow().removeOnFrameMetricsAvailableListener(this.mListener);
         return this.mMetrics;
      }

      public SparseIntArray[] reset() {
         SparseIntArray[] var1 = this.mMetrics;
         this.mMetrics = new SparseIntArray[9];
         return var1;
      }

      public SparseIntArray[] stop() {
         for(int var1 = this.mActivities.size() - 1; var1 >= 0; --var1) {
            WeakReference var2 = (WeakReference)this.mActivities.get(var1);
            Activity var3 = (Activity)var2.get();
            if (var2.get() != null) {
               var3.getWindow().removeOnFrameMetricsAvailableListener(this.mListener);
               this.mActivities.remove(var1);
            }
         }

         return this.mMetrics;
      }
   }

   private static class FrameMetricsBaseImpl {
      FrameMetricsBaseImpl() {
      }

      public void add(Activity var1) {
      }

      public SparseIntArray[] getMetrics() {
         return null;
      }

      public SparseIntArray[] remove(Activity var1) {
         return null;
      }

      public SparseIntArray[] reset() {
         return null;
      }

      public SparseIntArray[] stop() {
         return null;
      }
   }

   @Retention(RetentionPolicy.SOURCE)
   public @interface MetricType {
   }
}
