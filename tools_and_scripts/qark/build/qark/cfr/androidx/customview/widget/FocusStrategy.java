/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 */
package androidx.customview.widget;

import android.graphics.Rect;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class FocusStrategy {
    private FocusStrategy() {
    }

    private static boolean beamBeats(int n, Rect rect, Rect rect2, Rect rect3) {
        boolean bl = FocusStrategy.beamsOverlap(n, rect, rect2);
        boolean bl2 = FocusStrategy.beamsOverlap(n, rect, rect3);
        boolean bl3 = false;
        if (!bl2) {
            if (!bl) {
                return false;
            }
            if (!FocusStrategy.isToDirectionOf(n, rect, rect3)) {
                return true;
            }
            if (n != 17) {
                if (n == 66) {
                    return true;
                }
                if (FocusStrategy.majorAxisDistance(n, rect, rect2) < FocusStrategy.majorAxisDistanceToFarEdge(n, rect, rect3)) {
                    bl3 = true;
                }
                return bl3;
            }
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private static boolean beamsOverlap(int n, Rect rect, Rect rect2) {
        block2 : {
            block3 : {
                if (n == 17) break block2;
                if (n == 33) break block3;
                if (n == 66) break block2;
                if (n != 130) throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
            }
            if (rect2.right < rect.left) return false;
            if (rect2.left > rect.right) return false;
            return true;
        }
        if (rect2.bottom < rect.top) return false;
        if (rect2.top > rect.bottom) return false;
        return true;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static <L, T> T findNextFocusInAbsoluteDirection(L l, CollectionAdapter<L, T> collectionAdapter, BoundsAdapter<T> boundsAdapter, T t, Rect rect, int n) {
        Rect rect2 = new Rect(rect);
        if (n != 17) {
            if (n != 33) {
                if (n != 66) {
                    if (n != 130) throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
                    rect2.offset(0, - rect.height() + 1);
                } else {
                    rect2.offset(- rect.width() + 1, 0);
                }
            } else {
                rect2.offset(0, rect.height() + 1);
            }
        } else {
            rect2.offset(rect.width() + 1, 0);
        }
        T t2 = null;
        int n2 = collectionAdapter.size(l);
        Rect rect3 = new Rect();
        int n3 = 0;
        while (n3 < n2) {
            L l2 = collectionAdapter.get(l, n3);
            if (l2 != t) {
                boundsAdapter.obtainBounds(l2, rect3);
                if (FocusStrategy.isBetterCandidate(n, rect, rect3, rect2)) {
                    rect2.set(rect3);
                    t2 = (T)l2;
                }
            }
            ++n3;
        }
        return t2;
    }

    public static <L, T> T findNextFocusInRelativeDirection(L l, CollectionAdapter<L, T> collectionAdapter, BoundsAdapter<T> boundsAdapter, T t, int n, boolean bl, boolean bl2) {
        int n2 = collectionAdapter.size(l);
        ArrayList<T> arrayList = new ArrayList<T>(n2);
        for (int i = 0; i < n2; ++i) {
            arrayList.add(collectionAdapter.get(l, i));
        }
        Collections.sort(arrayList, new SequentialComparator<T>(bl, boundsAdapter));
        if (n != 1) {
            if (n == 2) {
                return FocusStrategy.getNextFocusable(t, arrayList, bl2);
            }
            throw new IllegalArgumentException("direction must be one of {FOCUS_FORWARD, FOCUS_BACKWARD}.");
        }
        return FocusStrategy.getPreviousFocusable(t, arrayList, bl2);
    }

    private static <T> T getNextFocusable(T t, ArrayList<T> arrayList, boolean bl) {
        int n = arrayList.size();
        int n2 = t == null ? -1 : arrayList.lastIndexOf(t);
        if (++n2 < n) {
            return arrayList.get(n2);
        }
        if (bl && n > 0) {
            return arrayList.get(0);
        }
        return null;
    }

    private static <T> T getPreviousFocusable(T t, ArrayList<T> arrayList, boolean bl) {
        int n = arrayList.size();
        int n2 = t == null ? n : arrayList.indexOf(t);
        if (--n2 >= 0) {
            return arrayList.get(n2);
        }
        if (bl && n > 0) {
            return arrayList.get(n - 1);
        }
        return null;
    }

    private static int getWeightedDistanceFor(int n, int n2) {
        return n * 13 * n + n2 * n2;
    }

    private static boolean isBetterCandidate(int n, Rect rect, Rect rect2, Rect rect3) {
        boolean bl = FocusStrategy.isCandidate(rect, rect2, n);
        boolean bl2 = false;
        if (!bl) {
            return false;
        }
        if (!FocusStrategy.isCandidate(rect, rect3, n)) {
            return true;
        }
        if (FocusStrategy.beamBeats(n, rect, rect2, rect3)) {
            return true;
        }
        if (FocusStrategy.beamBeats(n, rect, rect3, rect2)) {
            return false;
        }
        if (FocusStrategy.getWeightedDistanceFor(FocusStrategy.majorAxisDistance(n, rect, rect2), FocusStrategy.minorAxisDistance(n, rect, rect2)) < FocusStrategy.getWeightedDistanceFor(FocusStrategy.majorAxisDistance(n, rect, rect3), FocusStrategy.minorAxisDistance(n, rect, rect3))) {
            bl2 = true;
        }
        return bl2;
    }

    private static boolean isCandidate(Rect rect, Rect rect2, int n) {
        if (n != 17) {
            if (n != 33) {
                if (n != 66) {
                    if (n == 130) {
                        if ((rect.top < rect2.top || rect.bottom <= rect2.top) && rect.bottom < rect2.bottom) {
                            return true;
                        }
                        return false;
                    }
                    throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
                }
                if ((rect.left < rect2.left || rect.right <= rect2.left) && rect.right < rect2.right) {
                    return true;
                }
                return false;
            }
            if ((rect.bottom > rect2.bottom || rect.top >= rect2.bottom) && rect.top > rect2.top) {
                return true;
            }
            return false;
        }
        if ((rect.right > rect2.right || rect.left >= rect2.right) && rect.left > rect2.left) {
            return true;
        }
        return false;
    }

    private static boolean isToDirectionOf(int n, Rect rect, Rect rect2) {
        if (n != 17) {
            if (n != 33) {
                if (n != 66) {
                    if (n == 130) {
                        if (rect.bottom <= rect2.top) {
                            return true;
                        }
                        return false;
                    }
                    throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
                }
                if (rect.right <= rect2.left) {
                    return true;
                }
                return false;
            }
            if (rect.top >= rect2.bottom) {
                return true;
            }
            return false;
        }
        if (rect.left >= rect2.right) {
            return true;
        }
        return false;
    }

    private static int majorAxisDistance(int n, Rect rect, Rect rect2) {
        return Math.max(0, FocusStrategy.majorAxisDistanceRaw(n, rect, rect2));
    }

    private static int majorAxisDistanceRaw(int n, Rect rect, Rect rect2) {
        if (n != 17) {
            if (n != 33) {
                if (n != 66) {
                    if (n == 130) {
                        return rect2.top - rect.bottom;
                    }
                    throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
                }
                return rect2.left - rect.right;
            }
            return rect.top - rect2.bottom;
        }
        return rect.left - rect2.right;
    }

    private static int majorAxisDistanceToFarEdge(int n, Rect rect, Rect rect2) {
        return Math.max(1, FocusStrategy.majorAxisDistanceToFarEdgeRaw(n, rect, rect2));
    }

    private static int majorAxisDistanceToFarEdgeRaw(int n, Rect rect, Rect rect2) {
        if (n != 17) {
            if (n != 33) {
                if (n != 66) {
                    if (n == 130) {
                        return rect2.bottom - rect.bottom;
                    }
                    throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
                }
                return rect2.right - rect.right;
            }
            return rect.top - rect2.top;
        }
        return rect.left - rect2.left;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static int minorAxisDistance(int n, Rect rect, Rect rect2) {
        if (n == 17) return Math.abs(rect.top + rect.height() / 2 - (rect2.top + rect2.height() / 2));
        if (n == 33) return Math.abs(rect.left + rect.width() / 2 - (rect2.left + rect2.width() / 2));
        if (n == 66) return Math.abs(rect.top + rect.height() / 2 - (rect2.top + rect2.height() / 2));
        if (n == 130) return Math.abs(rect.left + rect.width() / 2 - (rect2.left + rect2.width() / 2));
        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
    }

    public static interface BoundsAdapter<T> {
        public void obtainBounds(T var1, Rect var2);
    }

    public static interface CollectionAdapter<T, V> {
        public V get(T var1, int var2);

        public int size(T var1);
    }

    private static class SequentialComparator<T>
    implements Comparator<T> {
        private final BoundsAdapter<T> mAdapter;
        private final boolean mIsLayoutRtl;
        private final Rect mTemp1 = new Rect();
        private final Rect mTemp2 = new Rect();

        SequentialComparator(boolean bl, BoundsAdapter<T> boundsAdapter) {
            this.mIsLayoutRtl = bl;
            this.mAdapter = boundsAdapter;
        }

        @Override
        public int compare(T t, T t2) {
            Rect rect = this.mTemp1;
            Rect rect2 = this.mTemp2;
            this.mAdapter.obtainBounds(t, rect);
            this.mAdapter.obtainBounds(t2, rect2);
            int n = rect.top;
            int n2 = rect2.top;
            int n3 = -1;
            if (n < n2) {
                return -1;
            }
            if (rect.top > rect2.top) {
                return 1;
            }
            if (rect.left < rect2.left) {
                if (this.mIsLayoutRtl) {
                    n3 = 1;
                }
                return n3;
            }
            if (rect.left > rect2.left) {
                if (this.mIsLayoutRtl) {
                    return -1;
                }
                return 1;
            }
            if (rect.bottom < rect2.bottom) {
                return -1;
            }
            if (rect.bottom > rect2.bottom) {
                return 1;
            }
            if (rect.right < rect2.right) {
                if (this.mIsLayoutRtl) {
                    n3 = 1;
                }
                return n3;
            }
            if (rect.right > rect2.right) {
                if (this.mIsLayoutRtl) {
                    return -1;
                }
                return 1;
            }
            return 0;
        }
    }

}

