// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.widget;

import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.graphics.Rect;

class FocusStrategy
{
    private static boolean beamBeats(final int n, @NonNull final Rect rect, @NonNull final Rect rect2, @NonNull final Rect rect3) {
        final boolean beamsOverlap = beamsOverlap(n, rect, rect2);
        final boolean beamsOverlap2 = beamsOverlap(n, rect, rect3);
        boolean b = false;
        if (beamsOverlap2) {
            return false;
        }
        if (!beamsOverlap) {
            return false;
        }
        if (!isToDirectionOf(n, rect, rect3)) {
            return true;
        }
        if (n == 17) {
            return true;
        }
        if (n == 66) {
            return true;
        }
        if (majorAxisDistance(n, rect, rect2) < majorAxisDistanceToFarEdge(n, rect, rect3)) {
            b = true;
        }
        return b;
    }
    
    private static boolean beamsOverlap(final int n, @NonNull final Rect rect, @NonNull final Rect rect2) {
        if (n != 17) {
            if (n != 33) {
                if (n == 66) {
                    return rect2.bottom >= rect.top && rect2.top <= rect.bottom;
                }
                if (n != 130) {
                    throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
                }
            }
            return rect2.right >= rect.left && rect2.left <= rect.right;
        }
        return rect2.bottom >= rect.top && rect2.top <= rect.bottom;
    }
    
    public static <L, T> T findNextFocusInAbsoluteDirection(@NonNull final L l, @NonNull final CollectionAdapter<L, T> collectionAdapter, @NonNull final BoundsAdapter<T> boundsAdapter, @Nullable final T t, @NonNull final Rect rect, final int n) {
        final Rect rect2 = new Rect(rect);
        if (n != 17) {
            if (n != 33) {
                if (n != 66) {
                    if (n != 130) {
                        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
                    }
                    rect2.offset(0, -(rect.height() + 1));
                }
                else {
                    rect2.offset(-(rect.width() + 1), 0);
                }
            }
            else {
                rect2.offset(0, rect.height() + 1);
            }
        }
        else {
            rect2.offset(rect.width() + 1, 0);
        }
        T t2 = null;
        final int size = collectionAdapter.size(l);
        final Rect rect3 = new Rect();
        for (int i = 0; i < size; ++i) {
            final T value = collectionAdapter.get(l, i);
            if (value != t) {
                boundsAdapter.obtainBounds(value, rect3);
                if (isBetterCandidate(n, rect, rect3, rect2)) {
                    rect2.set(rect3);
                    t2 = value;
                }
            }
        }
        return t2;
    }
    
    public static <L, T> T findNextFocusInRelativeDirection(@NonNull final L l, @NonNull final CollectionAdapter<L, T> collectionAdapter, @NonNull final BoundsAdapter<T> boundsAdapter, @Nullable final T t, final int n, final boolean b, final boolean b2) {
        final int size = collectionAdapter.size(l);
        final ArrayList list = new ArrayList<T>(size);
        for (int i = 0; i < size; ++i) {
            list.add(collectionAdapter.get(l, i));
        }
        Collections.sort((List<T>)list, new SequentialComparator<Object>(b, (BoundsAdapter<? super T>)boundsAdapter));
        switch (n) {
            default: {
                throw new IllegalArgumentException("direction must be one of {FOCUS_FORWARD, FOCUS_BACKWARD}.");
            }
            case 2: {
                return getNextFocusable(t, (ArrayList<T>)list, b2);
            }
            case 1: {
                return getPreviousFocusable(t, (ArrayList<T>)list, b2);
            }
        }
    }
    
    private static <T> T getNextFocusable(final T t, final ArrayList<T> list, final boolean b) {
        final int size = list.size();
        int lastIndex;
        if (t == null) {
            lastIndex = -1;
        }
        else {
            lastIndex = list.lastIndexOf(t);
        }
        final int n = lastIndex + 1;
        if (n < size) {
            return list.get(n);
        }
        if (b && size > 0) {
            return list.get(0);
        }
        return null;
    }
    
    private static <T> T getPreviousFocusable(final T t, final ArrayList<T> list, final boolean b) {
        final int size = list.size();
        int index;
        if (t == null) {
            index = size;
        }
        else {
            index = list.indexOf(t);
        }
        final int n = index - 1;
        if (n >= 0) {
            return list.get(n);
        }
        if (b && size > 0) {
            return list.get(size - 1);
        }
        return null;
    }
    
    private static int getWeightedDistanceFor(final int n, final int n2) {
        return n * 13 * n + n2 * n2;
    }
    
    private static boolean isBetterCandidate(final int n, @NonNull final Rect rect, @NonNull final Rect rect2, @NonNull final Rect rect3) {
        final boolean candidate = isCandidate(rect, rect2, n);
        boolean b = false;
        if (!candidate) {
            return false;
        }
        if (!isCandidate(rect, rect3, n)) {
            return true;
        }
        if (beamBeats(n, rect, rect2, rect3)) {
            return true;
        }
        if (beamBeats(n, rect, rect3, rect2)) {
            return false;
        }
        if (getWeightedDistanceFor(majorAxisDistance(n, rect, rect2), minorAxisDistance(n, rect, rect2)) < getWeightedDistanceFor(majorAxisDistance(n, rect, rect3), minorAxisDistance(n, rect, rect3))) {
            b = true;
        }
        return b;
    }
    
    private static boolean isCandidate(@NonNull final Rect rect, @NonNull final Rect rect2, final int n) {
        if (n == 17) {
            return (rect.right > rect2.right || rect.left >= rect2.right) && rect.left > rect2.left;
        }
        if (n == 33) {
            return (rect.bottom > rect2.bottom || rect.top >= rect2.bottom) && rect.top > rect2.top;
        }
        if (n == 66) {
            return (rect.left < rect2.left || rect.right <= rect2.left) && rect.right < rect2.right;
        }
        if (n == 130) {
            return (rect.top < rect2.top || rect.bottom <= rect2.top) && rect.bottom < rect2.bottom;
        }
        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
    }
    
    private static boolean isToDirectionOf(final int n, @NonNull final Rect rect, @NonNull final Rect rect2) {
        if (n == 17) {
            return rect.left >= rect2.right;
        }
        if (n == 33) {
            return rect.top >= rect2.bottom;
        }
        if (n == 66) {
            return rect.right <= rect2.left;
        }
        if (n == 130) {
            return rect.bottom <= rect2.top;
        }
        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
    }
    
    private static int majorAxisDistance(final int n, @NonNull final Rect rect, @NonNull final Rect rect2) {
        return Math.max(0, majorAxisDistanceRaw(n, rect, rect2));
    }
    
    private static int majorAxisDistanceRaw(final int n, @NonNull final Rect rect, @NonNull final Rect rect2) {
        if (n == 17) {
            return rect.left - rect2.right;
        }
        if (n == 33) {
            return rect.top - rect2.bottom;
        }
        if (n == 66) {
            return rect2.left - rect.right;
        }
        if (n == 130) {
            return rect2.top - rect.bottom;
        }
        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
    }
    
    private static int majorAxisDistanceToFarEdge(final int n, @NonNull final Rect rect, @NonNull final Rect rect2) {
        return Math.max(1, majorAxisDistanceToFarEdgeRaw(n, rect, rect2));
    }
    
    private static int majorAxisDistanceToFarEdgeRaw(final int n, @NonNull final Rect rect, @NonNull final Rect rect2) {
        if (n == 17) {
            return rect.left - rect2.left;
        }
        if (n == 33) {
            return rect.top - rect2.top;
        }
        if (n == 66) {
            return rect2.right - rect.right;
        }
        if (n == 130) {
            return rect2.bottom - rect.bottom;
        }
        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
    }
    
    private static int minorAxisDistance(final int n, @NonNull final Rect rect, @NonNull final Rect rect2) {
        if (n != 17) {
            if (n != 33) {
                if (n == 66) {
                    return Math.abs(rect.top + rect.height() / 2 - (rect2.top + rect2.height() / 2));
                }
                if (n != 130) {
                    throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
                }
            }
            return Math.abs(rect.left + rect.width() / 2 - (rect2.left + rect2.width() / 2));
        }
        return Math.abs(rect.top + rect.height() / 2 - (rect2.top + rect2.height() / 2));
    }
    
    public interface BoundsAdapter<T>
    {
        void obtainBounds(final T p0, final Rect p1);
    }
    
    public interface CollectionAdapter<T, V>
    {
        V get(final T p0, final int p1);
        
        int size(final T p0);
    }
    
    private static class SequentialComparator<T> implements Comparator<T>
    {
        private final BoundsAdapter<T> mAdapter;
        private final boolean mIsLayoutRtl;
        private final Rect mTemp1;
        private final Rect mTemp2;
        
        SequentialComparator(final boolean mIsLayoutRtl, final BoundsAdapter<T> mAdapter) {
            this.mTemp1 = new Rect();
            this.mTemp2 = new Rect();
            this.mIsLayoutRtl = mIsLayoutRtl;
            this.mAdapter = mAdapter;
        }
        
        @Override
        public int compare(final T t, final T t2) {
            final Rect mTemp1 = this.mTemp1;
            final Rect mTemp2 = this.mTemp2;
            this.mAdapter.obtainBounds(t, mTemp1);
            this.mAdapter.obtainBounds(t2, mTemp2);
            final int top = mTemp1.top;
            final int top2 = mTemp2.top;
            int n = -1;
            if (top < top2) {
                return -1;
            }
            if (mTemp1.top > mTemp2.top) {
                return 1;
            }
            if (mTemp1.left < mTemp2.left) {
                if (this.mIsLayoutRtl) {
                    n = 1;
                }
                return n;
            }
            if (mTemp1.left > mTemp2.left) {
                if (this.mIsLayoutRtl) {
                    return -1;
                }
                return 1;
            }
            else {
                if (mTemp1.bottom < mTemp2.bottom) {
                    return -1;
                }
                if (mTemp1.bottom > mTemp2.bottom) {
                    return 1;
                }
                if (mTemp1.right < mTemp2.right) {
                    if (this.mIsLayoutRtl) {
                        n = 1;
                    }
                    return n;
                }
                if (mTemp1.right <= mTemp2.right) {
                    return 0;
                }
                if (this.mIsLayoutRtl) {
                    return -1;
                }
                return 1;
            }
        }
    }
}
