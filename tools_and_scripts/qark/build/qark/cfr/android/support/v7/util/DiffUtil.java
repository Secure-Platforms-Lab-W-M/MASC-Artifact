/*
 * Decompiled with CFR 0_124.
 */
package android.support.v7.util;

import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.util.BatchingListUpdateCallback;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class DiffUtil {
    private static final Comparator<Snake> SNAKE_COMPARATOR = new Comparator<Snake>(){

        @Override
        public int compare(Snake snake, Snake snake2) {
            int n = snake.x - snake2.x;
            if (n == 0) {
                return snake.y - snake2.y;
            }
            return n;
        }
    };

    private DiffUtil() {
    }

    public static DiffResult calculateDiff(Callback callback) {
        return DiffUtil.calculateDiff(callback, true);
    }

    public static DiffResult calculateDiff(Callback callback, boolean bl) {
        int n = callback.getOldListSize();
        int n2 = callback.getNewListSize();
        ArrayList<Snake> arrayList = new ArrayList<Snake>();
        ArrayList<Range> arrayList2 = new ArrayList<Range>();
        arrayList2.add(new Range(0, n, 0, n2));
        n = n + n2 + Math.abs(n - n2);
        int[] arrn = new int[n * 2];
        int[] arrn2 = new int[n * 2];
        ArrayList<Range> arrayList3 = new ArrayList<Range>();
        while (!arrayList2.isEmpty()) {
            Range range = (Range)arrayList2.remove(arrayList2.size() - 1);
            Snake snake = DiffUtil.diffPartial(callback, range.oldListStart, range.oldListEnd, range.newListStart, range.newListEnd, arrn, arrn2, n);
            if (snake != null) {
                if (snake.size > 0) {
                    arrayList.add(snake);
                }
                snake.x += range.oldListStart;
                snake.y += range.newListStart;
                Range range2 = arrayList3.isEmpty() ? new Range() : (Range)arrayList3.remove(arrayList3.size() - 1);
                range2.oldListStart = range.oldListStart;
                range2.newListStart = range.newListStart;
                if (snake.reverse) {
                    range2.oldListEnd = snake.x;
                    range2.newListEnd = snake.y;
                } else if (snake.removal) {
                    range2.oldListEnd = snake.x - 1;
                    range2.newListEnd = snake.y;
                } else {
                    range2.oldListEnd = snake.x;
                    range2.newListEnd = snake.y - 1;
                }
                arrayList2.add(range2);
                if (snake.reverse) {
                    if (snake.removal) {
                        range.oldListStart = snake.x + snake.size + 1;
                        range.newListStart = snake.y + snake.size;
                    } else {
                        range.oldListStart = snake.x + snake.size;
                        range.newListStart = snake.y + snake.size + 1;
                    }
                } else {
                    range.oldListStart = snake.x + snake.size;
                    range.newListStart = snake.y + snake.size;
                }
                arrayList2.add(range);
                continue;
            }
            arrayList3.add(range);
        }
        Collections.sort(arrayList, SNAKE_COMPARATOR);
        return new DiffResult(callback, arrayList, arrn, arrn2, bl);
    }

    private static Snake diffPartial(Callback object, int n, int n2, int n3, int n4, int[] arrn, int[] arrn2, int n5) {
        int n6 = n2 - n;
        int n7 = n4 - n3;
        if (n2 - n >= 1 && n4 - n3 >= 1) {
            int n8 = n6 - n7;
            int n9 = (n6 + n7 + 1) / 2;
            Arrays.fill(arrn, n5 - n9 - 1, n5 + n9 + 1, 0);
            Arrays.fill(arrn2, n5 - n9 - 1 + n8, n5 + n9 + 1 + n8, n6);
            boolean bl = n8 % 2 != 0;
            n4 = n6;
            for (int i = 0; i <= n9; ++i) {
                int n10;
                boolean bl2;
                for (n6 = - i; n6 <= i; n6 += 2) {
                    if (n6 != - i && (n6 == i || arrn[n5 + n6 - 1] >= arrn[n5 + n6 + 1])) {
                        n2 = arrn[n5 + n6 - 1] + 1;
                        bl2 = true;
                    } else {
                        n2 = arrn[n5 + n6 + 1];
                        bl2 = false;
                    }
                    for (n10 = n2 - n6; n2 < n4 && n10 < n7 && object.areItemsTheSame(n + n2, n3 + n10); ++n2, ++n10) {
                    }
                    arrn[n5 + n6] = n2;
                    if (!bl || n6 < n8 - i + 1 || n6 > n8 + i - 1 || arrn[n5 + n6] < arrn2[n5 + n6]) continue;
                    object = new Snake();
                    object.x = arrn2[n5 + n6];
                    object.y = object.x - n6;
                    object.size = arrn[n5 + n6] - arrn2[n5 + n6];
                    object.removal = bl2;
                    object.reverse = false;
                    return object;
                }
                n2 = n4;
                for (n6 = - i; n6 <= i; n6 += 2) {
                    int n11 = n6 + n8;
                    if (n11 != i + n8 && (n11 == - i + n8 || arrn2[n5 + n11 - 1] >= arrn2[n5 + n11 + 1])) {
                        n4 = arrn2[n5 + n11 + 1] - 1;
                        bl2 = true;
                    } else {
                        n4 = arrn2[n5 + n11 - 1];
                        bl2 = false;
                    }
                    for (n10 = n4 - n11; n4 > 0 && n10 > 0 && object.areItemsTheSame(n + n4 - 1, n3 + n10 - 1); --n4, --n10) {
                    }
                    arrn2[n5 + n11] = n4;
                    if (bl || n6 + n8 < - i || n6 + n8 > i || arrn[n5 + n11] < arrn2[n5 + n11]) continue;
                    object = new Snake();
                    object.x = arrn2[n5 + n11];
                    object.y = object.x - n11;
                    object.size = arrn[n5 + n11] - arrn2[n5 + n11];
                    object.removal = bl2;
                    object.reverse = true;
                    return object;
                }
                n4 = n2;
            }
            throw new IllegalStateException("DiffUtil hit an unexpected case while trying to calculate the optimal path. Please make sure your data is not changing during the diff calculation.");
        }
        return null;
    }

    public static abstract class Callback {
        public abstract boolean areContentsTheSame(int var1, int var2);

        public abstract boolean areItemsTheSame(int var1, int var2);

        @Nullable
        public Object getChangePayload(int n, int n2) {
            return null;
        }

        public abstract int getNewListSize();

        public abstract int getOldListSize();
    }

    public static class DiffResult {
        private static final int FLAG_CHANGED = 2;
        private static final int FLAG_IGNORE = 16;
        private static final int FLAG_MASK = 31;
        private static final int FLAG_MOVED_CHANGED = 4;
        private static final int FLAG_MOVED_NOT_CHANGED = 8;
        private static final int FLAG_NOT_CHANGED = 1;
        private static final int FLAG_OFFSET = 5;
        private final Callback mCallback;
        private final boolean mDetectMoves;
        private final int[] mNewItemStatuses;
        private final int mNewListSize;
        private final int[] mOldItemStatuses;
        private final int mOldListSize;
        private final List<Snake> mSnakes;

        DiffResult(Callback callback, List<Snake> list, int[] arrn, int[] arrn2, boolean bl) {
            this.mSnakes = list;
            this.mOldItemStatuses = arrn;
            this.mNewItemStatuses = arrn2;
            Arrays.fill(this.mOldItemStatuses, 0);
            Arrays.fill(this.mNewItemStatuses, 0);
            this.mCallback = callback;
            this.mOldListSize = callback.getOldListSize();
            this.mNewListSize = callback.getNewListSize();
            this.mDetectMoves = bl;
            this.addRootSnake();
            this.findMatchingItems();
        }

        private void addRootSnake() {
            Snake snake = this.mSnakes.isEmpty() ? null : this.mSnakes.get(0);
            if (snake != null && snake.x == 0 && snake.y == 0) {
                return;
            }
            snake = new Snake();
            snake.x = 0;
            snake.y = 0;
            snake.removal = false;
            snake.size = 0;
            snake.reverse = false;
            this.mSnakes.add(0, snake);
        }

        private void dispatchAdditions(List<PostponedUpdate> object, ListUpdateCallback listUpdateCallback, int n, int n2, int n3) {
            if (!this.mDetectMoves) {
                listUpdateCallback.onInserted(n, n2);
                return;
            }
            --n2;
            while (n2 >= 0) {
                block6 : {
                    block3 : {
                        int n4;
                        block4 : {
                            block5 : {
                                n4 = this.mNewItemStatuses[n3 + n2] & 31;
                                if (n4 == 0) break block3;
                                if (n4 == 4 || n4 == 8) break block4;
                                if (n4 != 16) break block5;
                                object.add((PostponedUpdate)new PostponedUpdate(n3 + n2, n, false));
                                break block6;
                            }
                            object = new StringBuilder();
                            object.append("unknown flag for pos ");
                            object.append(n3 + n2);
                            object.append(" ");
                            object.append(Long.toBinaryString(n4));
                            throw new IllegalStateException(object.toString());
                        }
                        int n5 = this.mNewItemStatuses[n3 + n2] >> 5;
                        listUpdateCallback.onMoved(DiffResult.removePostponedUpdate(object, (int)n5, (boolean)true).currentPos, n);
                        if (n4 != 4) break block6;
                        listUpdateCallback.onChanged(n, 1, this.mCallback.getChangePayload(n5, n3 + n2));
                        break block6;
                    }
                    listUpdateCallback.onInserted(n, 1);
                    Iterator<PostponedUpdate> iterator = object.iterator();
                    while (iterator.hasNext()) {
                        PostponedUpdate postponedUpdate = iterator.next();
                        ++postponedUpdate.currentPos;
                    }
                }
                --n2;
            }
        }

        private void dispatchRemovals(List<PostponedUpdate> object, ListUpdateCallback listUpdateCallback, int n, int n2, int n3) {
            if (!this.mDetectMoves) {
                listUpdateCallback.onRemoved(n, n2);
                return;
            }
            --n2;
            while (n2 >= 0) {
                block6 : {
                    Object object2;
                    block3 : {
                        int n4;
                        block4 : {
                            block5 : {
                                n4 = this.mOldItemStatuses[n3 + n2] & 31;
                                if (n4 == 0) break block3;
                                if (n4 == 4 || n4 == 8) break block4;
                                if (n4 != 16) break block5;
                                object.add((PostponedUpdate)new PostponedUpdate(n3 + n2, n + n2, true));
                                break block6;
                            }
                            object = new StringBuilder();
                            object.append("unknown flag for pos ");
                            object.append(n3 + n2);
                            object.append(" ");
                            object.append(Long.toBinaryString(n4));
                            throw new IllegalStateException(object.toString());
                        }
                        int n5 = this.mOldItemStatuses[n3 + n2] >> 5;
                        object2 = DiffResult.removePostponedUpdate(object, n5, false);
                        listUpdateCallback.onMoved(n + n2, object2.currentPos - 1);
                        if (n4 != 4) break block6;
                        listUpdateCallback.onChanged(object2.currentPos - 1, 1, this.mCallback.getChangePayload(n3 + n2, n5));
                        break block6;
                    }
                    listUpdateCallback.onRemoved(n + n2, 1);
                    object2 = object.iterator();
                    while (object2.hasNext()) {
                        PostponedUpdate postponedUpdate = object2.next();
                        --postponedUpdate.currentPos;
                    }
                }
                --n2;
            }
        }

        private void findAddition(int n, int n2, int n3) {
            if (this.mOldItemStatuses[n - 1] != 0) {
                return;
            }
            this.findMatchingItem(n, n2, n3, false);
        }

        private boolean findMatchingItem(int n, int n2, int n3, boolean bl) {
            int n4;
            int n5;
            int n6;
            if (bl) {
                n6 = n2 - 1;
                n5 = n;
                n4 = n2 - 1;
                n2 = n5;
                n5 = n4;
            } else {
                n6 = n - 1;
                n4 = n - 1;
                n5 = n2;
                n2 = n4;
            }
            n4 = n5;
            n5 = n2;
            while (n3 >= 0) {
                Snake snake = this.mSnakes.get(n3);
                int n7 = snake.x;
                int n8 = snake.size;
                int n9 = snake.y;
                int n10 = snake.size;
                n2 = 8;
                if (bl) {
                    --n5;
                    while (n5 >= n7 + n8) {
                        if (this.mCallback.areItemsTheSame(n5, n6)) {
                            if (!this.mCallback.areContentsTheSame(n5, n6)) {
                                n2 = 4;
                            }
                            this.mNewItemStatuses[n6] = n5 << 5 | 16;
                            this.mOldItemStatuses[n5] = n6 << 5 | n2;
                            return true;
                        }
                        --n5;
                    }
                } else {
                    for (n5 = n4 - 1; n5 >= n9 + n10; --n5) {
                        if (!this.mCallback.areItemsTheSame(n6, n5)) continue;
                        if (!this.mCallback.areContentsTheSame(n6, n5)) {
                            n2 = 4;
                        }
                        this.mOldItemStatuses[n - 1] = n5 << 5 | 16;
                        this.mNewItemStatuses[n5] = n - 1 << 5 | n2;
                        return true;
                    }
                }
                n5 = snake.x;
                n4 = snake.y;
                --n3;
            }
            return false;
        }

        private void findMatchingItems() {
            int n = this.mOldListSize;
            int n2 = this.mNewListSize;
            for (int i = this.mSnakes.size() - 1; i >= 0; --i) {
                Snake snake = this.mSnakes.get(i);
                int n3 = snake.x;
                int n4 = snake.size;
                int n5 = snake.y;
                int n6 = snake.size;
                if (this.mDetectMoves) {
                    while (n > n3 + n4) {
                        this.findAddition(n, n2, i);
                        --n;
                    }
                    while (n2 > n5 + n6) {
                        this.findRemoval(n, n2, i);
                        --n2;
                    }
                }
                for (n2 = 0; n2 < snake.size; ++n2) {
                    n5 = snake.x + n2;
                    n6 = snake.y + n2;
                    n = this.mCallback.areContentsTheSame(n5, n6) ? 1 : 2;
                    this.mOldItemStatuses[n5] = n6 << 5 | n;
                    this.mNewItemStatuses[n6] = n5 << 5 | n;
                }
                n = snake.x;
                n2 = snake.y;
            }
        }

        private void findRemoval(int n, int n2, int n3) {
            if (this.mNewItemStatuses[n2 - 1] != 0) {
                return;
            }
            this.findMatchingItem(n, n2, n3, true);
        }

        private static PostponedUpdate removePostponedUpdate(List<PostponedUpdate> list, int n, boolean bl) {
            for (int i = list.size() - 1; i >= 0; --i) {
                PostponedUpdate postponedUpdate = list.get(i);
                if (postponedUpdate.posInOwnerList != n || postponedUpdate.removal != bl) continue;
                list.remove(i);
                for (n = i; n < list.size(); ++n) {
                    PostponedUpdate postponedUpdate2 = list.get(n);
                    int n2 = postponedUpdate2.currentPos;
                    i = bl ? 1 : -1;
                    postponedUpdate2.currentPos = n2 + i;
                }
                return postponedUpdate;
            }
            return null;
        }

        public void dispatchUpdatesTo(ListUpdateCallback listUpdateCallback) {
            listUpdateCallback = listUpdateCallback instanceof BatchingListUpdateCallback ? (BatchingListUpdateCallback)listUpdateCallback : new BatchingListUpdateCallback(listUpdateCallback);
            ArrayList<PostponedUpdate> arrayList = new ArrayList<PostponedUpdate>();
            int n = this.mOldListSize;
            int n2 = this.mNewListSize;
            int n3 = this.mSnakes.size();
            --n3;
            while (n3 >= 0) {
                Snake snake = this.mSnakes.get(n3);
                int n4 = snake.size;
                int n5 = snake.x + n4;
                int n6 = snake.y + n4;
                if (n5 < n) {
                    this.dispatchRemovals(arrayList, listUpdateCallback, n5, n - n5, n5);
                }
                if (n6 < n2) {
                    this.dispatchAdditions(arrayList, listUpdateCallback, n5, n2 - n6, n6);
                }
                for (n2 = n4 - 1; n2 >= 0; --n2) {
                    if ((this.mOldItemStatuses[snake.x + n2] & 31) != 2) continue;
                    listUpdateCallback.onChanged(snake.x + n2, 1, this.mCallback.getChangePayload(snake.x + n2, snake.y + n2));
                }
                n = snake.x;
                n2 = snake.y;
                --n3;
            }
            listUpdateCallback.dispatchLastEvent();
        }

        public void dispatchUpdatesTo(final RecyclerView.Adapter adapter) {
            this.dispatchUpdatesTo(new ListUpdateCallback(){

                @Override
                public void onChanged(int n, int n2, Object object) {
                    adapter.notifyItemRangeChanged(n, n2, object);
                }

                @Override
                public void onInserted(int n, int n2) {
                    adapter.notifyItemRangeInserted(n, n2);
                }

                @Override
                public void onMoved(int n, int n2) {
                    adapter.notifyItemMoved(n, n2);
                }

                @Override
                public void onRemoved(int n, int n2) {
                    adapter.notifyItemRangeRemoved(n, n2);
                }
            });
        }

        @VisibleForTesting
        List<Snake> getSnakes() {
            return this.mSnakes;
        }

    }

    private static class PostponedUpdate {
        int currentPos;
        int posInOwnerList;
        boolean removal;

        public PostponedUpdate(int n, int n2, boolean bl) {
            this.posInOwnerList = n;
            this.currentPos = n2;
            this.removal = bl;
        }
    }

    static class Range {
        int newListEnd;
        int newListStart;
        int oldListEnd;
        int oldListStart;

        public Range() {
        }

        public Range(int n, int n2, int n3, int n4) {
            this.oldListStart = n;
            this.oldListEnd = n2;
            this.newListStart = n3;
            this.newListEnd = n4;
        }
    }

    static class Snake {
        boolean removal;
        boolean reverse;
        int size;
        int x;
        int y;

        Snake() {
        }
    }

}

