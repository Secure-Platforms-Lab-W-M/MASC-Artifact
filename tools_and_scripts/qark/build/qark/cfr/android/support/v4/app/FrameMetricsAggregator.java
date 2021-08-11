/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Handler
 *  android.os.HandlerThread
 *  android.os.Looper
 *  android.util.SparseIntArray
 *  android.view.FrameMetrics
 *  android.view.Window
 *  android.view.Window$OnFrameMetricsAvailableListener
 */
package android.support.v4.app;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.util.SparseIntArray;
import android.view.FrameMetrics;
import android.view.Window;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class FrameMetricsAggregator {
    public static final int ANIMATION_DURATION = 256;
    public static final int ANIMATION_INDEX = 8;
    public static final int COMMAND_DURATION = 32;
    public static final int COMMAND_INDEX = 5;
    private static final boolean DBG = false;
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
    private static final String TAG = "FrameMetrics";
    public static final int TOTAL_DURATION = 1;
    public static final int TOTAL_INDEX = 0;
    private FrameMetricsBaseImpl mInstance;

    public FrameMetricsAggregator() {
        this(1);
    }

    public FrameMetricsAggregator(int n) {
        if (Build.VERSION.SDK_INT >= 24) {
            this.mInstance = new FrameMetricsApi24Impl(n);
            return;
        }
        this.mInstance = new FrameMetricsBaseImpl();
    }

    public void add(@NonNull Activity activity) {
        this.mInstance.add(activity);
    }

    @Nullable
    public SparseIntArray[] getMetrics() {
        return this.mInstance.getMetrics();
    }

    @Nullable
    public SparseIntArray[] remove(@NonNull Activity activity) {
        return this.mInstance.remove(activity);
    }

    @Nullable
    public SparseIntArray[] reset() {
        return this.mInstance.reset();
    }

    @Nullable
    public SparseIntArray[] stop() {
        return this.mInstance.stop();
    }

    @RequiresApi(value=24)
    private static class FrameMetricsApi24Impl
    extends FrameMetricsBaseImpl {
        private static final int NANOS_PER_MS = 1000000;
        private static final int NANOS_ROUNDING_VALUE = 500000;
        private static Handler sHandler;
        private static HandlerThread sHandlerThread;
        private ArrayList<WeakReference<Activity>> mActivities = new ArrayList();
        Window.OnFrameMetricsAvailableListener mListener;
        private SparseIntArray[] mMetrics = new SparseIntArray[9];
        private int mTrackingFlags;

        static {
            sHandlerThread = null;
            sHandler = null;
        }

        FrameMetricsApi24Impl(int n) {
            super();
            this.mListener = new Window.OnFrameMetricsAvailableListener(){

                public void onFrameMetricsAvailable(Window window, FrameMetrics frameMetrics, int n) {
                    if ((FrameMetricsApi24Impl.this.mTrackingFlags & 1) != 0) {
                        FrameMetricsApi24Impl.this.addDurationItem(FrameMetricsApi24Impl.this.mMetrics[0], frameMetrics.getMetric(8));
                    }
                    if ((FrameMetricsApi24Impl.this.mTrackingFlags & 2) != 0) {
                        FrameMetricsApi24Impl.this.addDurationItem(FrameMetricsApi24Impl.this.mMetrics[1], frameMetrics.getMetric(1));
                    }
                    if ((FrameMetricsApi24Impl.this.mTrackingFlags & 4) != 0) {
                        FrameMetricsApi24Impl.this.addDurationItem(FrameMetricsApi24Impl.this.mMetrics[2], frameMetrics.getMetric(3));
                    }
                    if ((FrameMetricsApi24Impl.this.mTrackingFlags & 8) != 0) {
                        FrameMetricsApi24Impl.this.addDurationItem(FrameMetricsApi24Impl.this.mMetrics[3], frameMetrics.getMetric(4));
                    }
                    if ((FrameMetricsApi24Impl.this.mTrackingFlags & 16) != 0) {
                        FrameMetricsApi24Impl.this.addDurationItem(FrameMetricsApi24Impl.this.mMetrics[4], frameMetrics.getMetric(5));
                    }
                    if ((FrameMetricsApi24Impl.this.mTrackingFlags & 64) != 0) {
                        FrameMetricsApi24Impl.this.addDurationItem(FrameMetricsApi24Impl.this.mMetrics[6], frameMetrics.getMetric(7));
                    }
                    if ((FrameMetricsApi24Impl.this.mTrackingFlags & 32) != 0) {
                        FrameMetricsApi24Impl.this.addDurationItem(FrameMetricsApi24Impl.this.mMetrics[5], frameMetrics.getMetric(6));
                    }
                    if ((FrameMetricsApi24Impl.this.mTrackingFlags & 128) != 0) {
                        FrameMetricsApi24Impl.this.addDurationItem(FrameMetricsApi24Impl.this.mMetrics[7], frameMetrics.getMetric(0));
                    }
                    if ((FrameMetricsApi24Impl.this.mTrackingFlags & 256) != 0) {
                        FrameMetricsApi24Impl.this.addDurationItem(FrameMetricsApi24Impl.this.mMetrics[8], frameMetrics.getMetric(2));
                    }
                }
            };
            this.mTrackingFlags = n;
        }

        @Override
        public void add(Activity activity) {
            if (sHandlerThread == null) {
                sHandlerThread = new HandlerThread("FrameMetricsAggregator");
                sHandlerThread.start();
                sHandler = new Handler(sHandlerThread.getLooper());
            }
            for (int i = 0; i <= 8; ++i) {
                if (this.mMetrics[i] != null || (this.mTrackingFlags & 1 << i) == 0) continue;
                this.mMetrics[i] = new SparseIntArray();
            }
            activity.getWindow().addOnFrameMetricsAvailableListener(this.mListener, sHandler);
            this.mActivities.add(new WeakReference<Activity>(activity));
        }

        void addDurationItem(SparseIntArray sparseIntArray, long l) {
            if (sparseIntArray != null) {
                int n = (int)((500000L + l) / 1000000L);
                if (l >= 0L) {
                    sparseIntArray.put(n, sparseIntArray.get(n) + 1);
                }
            }
        }

        @Override
        public SparseIntArray[] getMetrics() {
            return this.mMetrics;
        }

        @Override
        public SparseIntArray[] remove(Activity activity) {
            for (WeakReference<Activity> weakReference : this.mActivities) {
                if (weakReference.get() != activity) continue;
                this.mActivities.remove(weakReference);
                break;
            }
            activity.getWindow().removeOnFrameMetricsAvailableListener(this.mListener);
            return this.mMetrics;
        }

        @Override
        public SparseIntArray[] reset() {
            SparseIntArray[] arrsparseIntArray = this.mMetrics;
            this.mMetrics = new SparseIntArray[9];
            return arrsparseIntArray;
        }

        @Override
        public SparseIntArray[] stop() {
            for (int i = this.mActivities.size() - 1; i >= 0; --i) {
                WeakReference<Activity> weakReference = this.mActivities.get(i);
                Activity activity = weakReference.get();
                if (weakReference.get() == null) continue;
                activity.getWindow().removeOnFrameMetricsAvailableListener(this.mListener);
                this.mActivities.remove(i);
            }
            return this.mMetrics;
        }

    }

    private static class FrameMetricsBaseImpl {
        private FrameMetricsBaseImpl() {
        }

        public void add(Activity activity) {
        }

        public SparseIntArray[] getMetrics() {
            return null;
        }

        public SparseIntArray[] remove(Activity activity) {
            return null;
        }

        public SparseIntArray[] reset() {
            return null;
        }

        public SparseIntArray[] stop() {
            return null;
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static @interface MetricType {
    }

}

