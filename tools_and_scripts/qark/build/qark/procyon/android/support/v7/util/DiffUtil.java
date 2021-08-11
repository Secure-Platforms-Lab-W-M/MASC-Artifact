// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.util;

import android.support.annotation.VisibleForTesting;
import android.support.v7.widget.RecyclerView;
import java.util.Iterator;
import android.support.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Comparator;

public class DiffUtil
{
    private static final Comparator<Snake> SNAKE_COMPARATOR;
    
    static {
        SNAKE_COMPARATOR = new Comparator<Snake>() {
            @Override
            public int compare(final Snake snake, final Snake snake2) {
                final int n = snake.x - snake2.x;
                if (n == 0) {
                    return snake.y - snake2.y;
                }
                return n;
            }
        };
    }
    
    private DiffUtil() {
    }
    
    public static DiffResult calculateDiff(final Callback callback) {
        return calculateDiff(callback, true);
    }
    
    public static DiffResult calculateDiff(final Callback callback, final boolean b) {
        final int oldListSize = callback.getOldListSize();
        final int newListSize = callback.getNewListSize();
        final ArrayList<Object> list = new ArrayList<Object>();
        final ArrayList<Range> list2 = new ArrayList<Range>();
        list2.add(new Range(0, oldListSize, 0, newListSize));
        final int n = oldListSize + newListSize + Math.abs(oldListSize - newListSize);
        final int[] array = new int[n * 2];
        final int[] array2 = new int[n * 2];
        final ArrayList<Range> list3 = (ArrayList<Range>)new ArrayList<Object>();
        while (!list2.isEmpty()) {
            final Range range = list2.remove(list2.size() - 1);
            final Snake diffPartial = diffPartial(callback, range.oldListStart, range.oldListEnd, range.newListStart, range.newListEnd, array, array2, n);
            if (diffPartial != null) {
                if (diffPartial.size > 0) {
                    list.add(diffPartial);
                }
                diffPartial.x += range.oldListStart;
                diffPartial.y += range.newListStart;
                Range range2;
                if (list3.isEmpty()) {
                    range2 = new Range();
                }
                else {
                    range2 = list3.remove(list3.size() - 1);
                }
                range2.oldListStart = range.oldListStart;
                range2.newListStart = range.newListStart;
                if (diffPartial.reverse) {
                    range2.oldListEnd = diffPartial.x;
                    range2.newListEnd = diffPartial.y;
                }
                else if (diffPartial.removal) {
                    range2.oldListEnd = diffPartial.x - 1;
                    range2.newListEnd = diffPartial.y;
                }
                else {
                    range2.oldListEnd = diffPartial.x;
                    range2.newListEnd = diffPartial.y - 1;
                }
                list2.add(range2);
                if (diffPartial.reverse) {
                    if (diffPartial.removal) {
                        range.oldListStart = diffPartial.x + diffPartial.size + 1;
                        range.newListStart = diffPartial.y + diffPartial.size;
                    }
                    else {
                        range.oldListStart = diffPartial.x + diffPartial.size;
                        range.newListStart = diffPartial.y + diffPartial.size + 1;
                    }
                }
                else {
                    range.oldListStart = diffPartial.x + diffPartial.size;
                    range.newListStart = diffPartial.y + diffPartial.size;
                }
                list2.add(range);
            }
            else {
                list3.add(range);
            }
        }
        Collections.sort(list, (Comparator<? super Object>)DiffUtil.SNAKE_COMPARATOR);
        return new DiffResult(callback, (List<Snake>)list, array, array2, b);
    }
    
    private static Snake diffPartial(final Callback callback, final int n, int n2, final int n3, int n4, final int[] array, final int[] array2, final int n5) {
        final int n6 = n2 - n;
        final int n7 = n4 - n3;
        if (n2 - n >= 1 && n4 - n3 >= 1) {
            final int n8 = n6 - n7;
            final int n9 = (n6 + n7 + 1) / 2;
            Arrays.fill(array, n5 - n9 - 1, n5 + n9 + 1, 0);
            Arrays.fill(array2, n5 - n9 - 1 + n8, n5 + n9 + 1 + n8, n6);
            final boolean b = n8 % 2 != 0;
            int i = 0;
            n4 = n6;
            while (i <= n9) {
                for (int j = -i; j <= i; j += 2) {
                    boolean removal;
                    if (j != -i && (j == i || array[n5 + j - 1] >= array[n5 + j + 1])) {
                        n2 = array[n5 + j - 1] + 1;
                        removal = true;
                    }
                    else {
                        n2 = array[n5 + j + 1];
                        removal = false;
                    }
                    for (int n10 = n2 - j; n2 < n4 && n10 < n7 && callback.areItemsTheSame(n + n2, n3 + n10); ++n2, ++n10) {}
                    array[n5 + j] = n2;
                    if (b && j >= n8 - i + 1 && j <= n8 + i - 1 && array[n5 + j] >= array2[n5 + j]) {
                        final Snake snake = new Snake();
                        snake.x = array2[n5 + j];
                        snake.y = snake.x - j;
                        snake.size = array[n5 + j] - array2[n5 + j];
                        snake.removal = removal;
                        snake.reverse = false;
                        return snake;
                    }
                }
                int k = -i;
                n2 = n4;
                while (k <= i) {
                    final int n11 = k + n8;
                    boolean removal2;
                    if (n11 != i + n8 && (n11 == -i + n8 || array2[n5 + n11 - 1] >= array2[n5 + n11 + 1])) {
                        n4 = array2[n5 + n11 + 1] - 1;
                        removal2 = true;
                    }
                    else {
                        n4 = array2[n5 + n11 - 1];
                        removal2 = false;
                    }
                    for (int n12 = n4 - n11; n4 > 0 && n12 > 0 && callback.areItemsTheSame(n + n4 - 1, n3 + n12 - 1); --n4, --n12) {}
                    array2[n5 + n11] = n4;
                    if (!b && k + n8 >= -i && k + n8 <= i && array[n5 + n11] >= array2[n5 + n11]) {
                        final Snake snake2 = new Snake();
                        snake2.x = array2[n5 + n11];
                        snake2.y = snake2.x - n11;
                        snake2.size = array[n5 + n11] - array2[n5 + n11];
                        snake2.removal = removal2;
                        snake2.reverse = true;
                        return snake2;
                    }
                    k += 2;
                }
                ++i;
                n4 = n2;
            }
            throw new IllegalStateException("DiffUtil hit an unexpected case while trying to calculate the optimal path. Please make sure your data is not changing during the diff calculation.");
        }
        return null;
    }
    
    public abstract static class Callback
    {
        public abstract boolean areContentsTheSame(final int p0, final int p1);
        
        public abstract boolean areItemsTheSame(final int p0, final int p1);
        
        @Nullable
        public Object getChangePayload(final int n, final int n2) {
            return null;
        }
        
        public abstract int getNewListSize();
        
        public abstract int getOldListSize();
    }
    
    public static class DiffResult
    {
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
        
        DiffResult(final Callback mCallback, final List<Snake> mSnakes, final int[] mOldItemStatuses, final int[] mNewItemStatuses, final boolean mDetectMoves) {
            this.mSnakes = mSnakes;
            this.mOldItemStatuses = mOldItemStatuses;
            this.mNewItemStatuses = mNewItemStatuses;
            Arrays.fill(this.mOldItemStatuses, 0);
            Arrays.fill(this.mNewItemStatuses, 0);
            this.mCallback = mCallback;
            this.mOldListSize = mCallback.getOldListSize();
            this.mNewListSize = mCallback.getNewListSize();
            this.mDetectMoves = mDetectMoves;
            this.addRootSnake();
            this.findMatchingItems();
        }
        
        private void addRootSnake() {
            Snake snake;
            if (this.mSnakes.isEmpty()) {
                snake = null;
            }
            else {
                snake = this.mSnakes.get(0);
            }
            if (snake != null && snake.x == 0 && snake.y == 0) {
                return;
            }
            final Snake snake2 = new Snake();
            snake2.x = 0;
            snake2.y = 0;
            snake2.removal = false;
            snake2.size = 0;
            snake2.reverse = false;
            this.mSnakes.add(0, snake2);
        }
        
        private void dispatchAdditions(final List<PostponedUpdate> list, final ListUpdateCallback listUpdateCallback, final int n, int i, final int n2) {
            if (!this.mDetectMoves) {
                listUpdateCallback.onInserted(n, i);
                return;
            }
            int n3;
            StringBuilder sb;
            int n4;
            Iterator<PostponedUpdate> iterator;
            PostponedUpdate postponedUpdate;
            for (--i; i >= 0; --i) {
                n3 = (this.mNewItemStatuses[n2 + i] & 0x1F);
                if (n3 != 0) {
                    if (n3 != 4 && n3 != 8) {
                        if (n3 != 16) {
                            sb = new StringBuilder();
                            sb.append("unknown flag for pos ");
                            sb.append(n2 + i);
                            sb.append(" ");
                            sb.append(Long.toBinaryString(n3));
                            throw new IllegalStateException(sb.toString());
                        }
                        list.add(new PostponedUpdate(n2 + i, n, false));
                    }
                    else {
                        n4 = this.mNewItemStatuses[n2 + i] >> 5;
                        listUpdateCallback.onMoved(removePostponedUpdate(list, n4, true).currentPos, n);
                        if (n3 == 4) {
                            listUpdateCallback.onChanged(n, 1, this.mCallback.getChangePayload(n4, n2 + i));
                        }
                    }
                }
                else {
                    listUpdateCallback.onInserted(n, 1);
                    iterator = list.iterator();
                    while (iterator.hasNext()) {
                        postponedUpdate = iterator.next();
                        ++postponedUpdate.currentPos;
                    }
                }
            }
        }
        
        private void dispatchRemovals(final List<PostponedUpdate> list, final ListUpdateCallback listUpdateCallback, final int n, int i, final int n2) {
            if (!this.mDetectMoves) {
                listUpdateCallback.onRemoved(n, i);
                return;
            }
            int n3;
            StringBuilder sb;
            int n4;
            PostponedUpdate removePostponedUpdate;
            Iterator<PostponedUpdate> iterator;
            PostponedUpdate postponedUpdate;
            for (--i; i >= 0; --i) {
                n3 = (this.mOldItemStatuses[n2 + i] & 0x1F);
                if (n3 != 0) {
                    if (n3 != 4 && n3 != 8) {
                        if (n3 != 16) {
                            sb = new StringBuilder();
                            sb.append("unknown flag for pos ");
                            sb.append(n2 + i);
                            sb.append(" ");
                            sb.append(Long.toBinaryString(n3));
                            throw new IllegalStateException(sb.toString());
                        }
                        list.add(new PostponedUpdate(n2 + i, n + i, true));
                    }
                    else {
                        n4 = this.mOldItemStatuses[n2 + i] >> 5;
                        removePostponedUpdate = removePostponedUpdate(list, n4, false);
                        listUpdateCallback.onMoved(n + i, removePostponedUpdate.currentPos - 1);
                        if (n3 == 4) {
                            listUpdateCallback.onChanged(removePostponedUpdate.currentPos - 1, 1, this.mCallback.getChangePayload(n2 + i, n4));
                        }
                    }
                }
                else {
                    listUpdateCallback.onRemoved(n + i, 1);
                    iterator = list.iterator();
                    while (iterator.hasNext()) {
                        postponedUpdate = iterator.next();
                        --postponedUpdate.currentPos;
                    }
                }
            }
        }
        
        private void findAddition(final int n, final int n2, final int n3) {
            if (this.mOldItemStatuses[n - 1] != 0) {
                return;
            }
            this.findMatchingItem(n, n2, n3, false);
        }
        
        private boolean findMatchingItem(final int n, int n2, int i, final boolean b) {
            int n3;
            int n5;
            if (b) {
                n3 = n2 - 1;
                final int n4 = n2 - 1;
                n2 = n;
                n5 = n4;
            }
            else {
                n3 = n - 1;
                final int n6 = n - 1;
                n5 = n2;
                n2 = n6;
            }
            int y = n5;
            int x = n2;
            while (i >= 0) {
                final Snake snake = this.mSnakes.get(i);
                final int x2 = snake.x;
                final int size = snake.size;
                final int y2 = snake.y;
                final int size2 = snake.size;
                n2 = 8;
                if (b) {
                    for (int j = x - 1; j >= x2 + size; --j) {
                        if (this.mCallback.areItemsTheSame(j, n3)) {
                            if (!this.mCallback.areContentsTheSame(j, n3)) {
                                n2 = 4;
                            }
                            this.mNewItemStatuses[n3] = (j << 5 | 0x10);
                            this.mOldItemStatuses[j] = (n3 << 5 | n2);
                            return true;
                        }
                    }
                }
                else {
                    for (int k = y - 1; k >= y2 + size2; --k) {
                        if (this.mCallback.areItemsTheSame(n3, k)) {
                            if (!this.mCallback.areContentsTheSame(n3, k)) {
                                n2 = 4;
                            }
                            this.mOldItemStatuses[n - 1] = (k << 5 | 0x10);
                            this.mNewItemStatuses[k] = (n - 1 << 5 | n2);
                            return true;
                        }
                    }
                }
                x = snake.x;
                y = snake.y;
                --i;
            }
            return false;
        }
        
        private void findMatchingItems() {
            int i = this.mOldListSize;
            int j = this.mNewListSize;
            for (int k = this.mSnakes.size() - 1; k >= 0; --k) {
                final Snake snake = this.mSnakes.get(k);
                final int x = snake.x;
                final int size = snake.size;
                final int y = snake.y;
                final int size2 = snake.size;
                if (this.mDetectMoves) {
                    while (i > x + size) {
                        this.findAddition(i, j, k);
                        --i;
                    }
                    while (j > y + size2) {
                        this.findRemoval(i, j, k);
                        --j;
                    }
                }
                for (int l = 0; l < snake.size; ++l) {
                    final int n = snake.x + l;
                    final int n2 = snake.y + l;
                    int n3;
                    if (this.mCallback.areContentsTheSame(n, n2)) {
                        n3 = 1;
                    }
                    else {
                        n3 = 2;
                    }
                    this.mOldItemStatuses[n] = (n2 << 5 | n3);
                    this.mNewItemStatuses[n2] = (n << 5 | n3);
                }
                i = snake.x;
                j = snake.y;
            }
        }
        
        private void findRemoval(final int n, final int n2, final int n3) {
            if (this.mNewItemStatuses[n2 - 1] != 0) {
                return;
            }
            this.findMatchingItem(n, n2, n3, true);
        }
        
        private static PostponedUpdate removePostponedUpdate(final List<PostponedUpdate> list, int i, final boolean b) {
            for (int j = list.size() - 1; j >= 0; --j) {
                final PostponedUpdate postponedUpdate = list.get(j);
                if (postponedUpdate.posInOwnerList == i && postponedUpdate.removal == b) {
                    list.remove(j);
                    PostponedUpdate postponedUpdate2;
                    int currentPos;
                    int n;
                    for (i = j; i < list.size(); ++i) {
                        postponedUpdate2 = list.get(i);
                        currentPos = postponedUpdate2.currentPos;
                        if (b) {
                            n = 1;
                        }
                        else {
                            n = -1;
                        }
                        postponedUpdate2.currentPos = currentPos + n;
                    }
                    return postponedUpdate;
                }
            }
            return null;
        }
        
        public void dispatchUpdatesTo(final ListUpdateCallback listUpdateCallback) {
            BatchingListUpdateCallback batchingListUpdateCallback;
            if (listUpdateCallback instanceof BatchingListUpdateCallback) {
                batchingListUpdateCallback = (BatchingListUpdateCallback)listUpdateCallback;
            }
            else {
                batchingListUpdateCallback = new BatchingListUpdateCallback(listUpdateCallback);
            }
            final ArrayList<PostponedUpdate> list = new ArrayList<PostponedUpdate>();
            int n = this.mOldListSize;
            int n2 = this.mNewListSize;
            for (int i = this.mSnakes.size() - 1; i >= 0; --i) {
                final Snake snake = this.mSnakes.get(i);
                final int size = snake.size;
                final int n3 = snake.x + size;
                final int n4 = snake.y + size;
                if (n3 < n) {
                    this.dispatchRemovals(list, batchingListUpdateCallback, n3, n - n3, n3);
                }
                if (n4 < n2) {
                    this.dispatchAdditions(list, batchingListUpdateCallback, n3, n2 - n4, n4);
                }
                for (int j = size - 1; j >= 0; --j) {
                    if ((this.mOldItemStatuses[snake.x + j] & 0x1F) == 0x2) {
                        batchingListUpdateCallback.onChanged(snake.x + j, 1, this.mCallback.getChangePayload(snake.x + j, snake.y + j));
                    }
                }
                n = snake.x;
                n2 = snake.y;
            }
            batchingListUpdateCallback.dispatchLastEvent();
        }
        
        public void dispatchUpdatesTo(final RecyclerView.Adapter adapter) {
            this.dispatchUpdatesTo(new ListUpdateCallback() {
                @Override
                public void onChanged(final int n, final int n2, final Object o) {
                    adapter.notifyItemRangeChanged(n, n2, o);
                }
                
                @Override
                public void onInserted(final int n, final int n2) {
                    adapter.notifyItemRangeInserted(n, n2);
                }
                
                @Override
                public void onMoved(final int n, final int n2) {
                    adapter.notifyItemMoved(n, n2);
                }
                
                @Override
                public void onRemoved(final int n, final int n2) {
                    adapter.notifyItemRangeRemoved(n, n2);
                }
            });
        }
        
        @VisibleForTesting
        List<Snake> getSnakes() {
            return this.mSnakes;
        }
    }
    
    private static class PostponedUpdate
    {
        int currentPos;
        int posInOwnerList;
        boolean removal;
        
        public PostponedUpdate(final int posInOwnerList, final int currentPos, final boolean removal) {
            this.posInOwnerList = posInOwnerList;
            this.currentPos = currentPos;
            this.removal = removal;
        }
    }
    
    static class Range
    {
        int newListEnd;
        int newListStart;
        int oldListEnd;
        int oldListStart;
        
        public Range() {
        }
        
        public Range(final int oldListStart, final int oldListEnd, final int newListStart, final int newListEnd) {
            this.oldListStart = oldListStart;
            this.oldListEnd = oldListEnd;
            this.newListStart = newListStart;
            this.newListEnd = newListEnd;
        }
    }
    
    static class Snake
    {
        boolean removal;
        boolean reverse;
        int size;
        int x;
        int y;
    }
}
