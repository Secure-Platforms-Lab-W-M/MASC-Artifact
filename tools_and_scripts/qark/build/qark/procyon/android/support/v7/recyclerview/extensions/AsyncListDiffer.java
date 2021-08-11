// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.recyclerview.extensions;

import android.support.v7.util.AdapterListUpdateCallback;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import java.util.Collections;
import android.support.v7.util.ListUpdateCallback;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.List;

public class AsyncListDiffer<T>
{
    private final AsyncDifferConfig<T> mConfig;
    @Nullable
    private List<T> mList;
    private int mMaxScheduledGeneration;
    @NonNull
    private List<T> mReadOnlyList;
    private final ListUpdateCallback mUpdateCallback;
    
    public AsyncListDiffer(@NonNull final ListUpdateCallback mUpdateCallback, @NonNull final AsyncDifferConfig<T> mConfig) {
        this.mReadOnlyList = Collections.emptyList();
        this.mUpdateCallback = mUpdateCallback;
        this.mConfig = mConfig;
    }
    
    public AsyncListDiffer(@NonNull final RecyclerView.Adapter adapter, @NonNull final DiffUtil.ItemCallback<T> itemCallback) {
        this.mReadOnlyList = Collections.emptyList();
        this.mUpdateCallback = new AdapterListUpdateCallback(adapter);
        this.mConfig = new AsyncDifferConfig.Builder<T>(itemCallback).build();
    }
    
    private void latchList(@NonNull final List<T> mList, @NonNull final DiffUtil.DiffResult diffResult) {
        diffResult.dispatchUpdatesTo(this.mUpdateCallback);
        this.mList = mList;
        this.mReadOnlyList = Collections.unmodifiableList((List<? extends T>)mList);
    }
    
    @NonNull
    public List<T> getCurrentList() {
        return this.mReadOnlyList;
    }
    
    public void submitList(final List<T> mList) {
        if (mList == this.mList) {
            return;
        }
        final int mMaxScheduledGeneration = this.mMaxScheduledGeneration + 1;
        this.mMaxScheduledGeneration = mMaxScheduledGeneration;
        if (mList == null) {
            this.mUpdateCallback.onRemoved(0, this.mList.size());
            this.mList = null;
            this.mReadOnlyList = Collections.emptyList();
            return;
        }
        if (this.mList == null) {
            this.mUpdateCallback.onInserted(0, mList.size());
            this.mList = mList;
            this.mReadOnlyList = Collections.unmodifiableList((List<? extends T>)mList);
            return;
        }
        this.mConfig.getBackgroundThreadExecutor().execute(new Runnable() {
            final /* synthetic */ List val$oldList = AsyncListDiffer.this.mList;
            
            @Override
            public void run() {
                AsyncListDiffer.this.mConfig.getMainThreadExecutor().execute(new Runnable() {
                    final /* synthetic */ DiffUtil.DiffResult val$result = DiffUtil.calculateDiff((DiffUtil.Callback)new DiffUtil.Callback(this) {
                        @Override
                        public boolean areContentsTheSame(final int n, final int n2) {
                            return AsyncListDiffer.this.mConfig.getDiffCallback().areContentsTheSame(Runnable.this.val$oldList.get(n), mList.get(n2));
                        }
                        
                        @Override
                        public boolean areItemsTheSame(final int n, final int n2) {
                            return AsyncListDiffer.this.mConfig.getDiffCallback().areItemsTheSame(Runnable.this.val$oldList.get(n), mList.get(n2));
                        }
                        
                        @Override
                        public int getNewListSize() {
                            return mList.size();
                        }
                        
                        @Override
                        public int getOldListSize() {
                            return Runnable.this.val$oldList.size();
                        }
                    });
                    
                    @Override
                    public void run() {
                        if (AsyncListDiffer.this.mMaxScheduledGeneration == mMaxScheduledGeneration) {
                            AsyncListDiffer.this.latchList(mList, this.val$result);
                        }
                    }
                });
            }
        });
    }
}
