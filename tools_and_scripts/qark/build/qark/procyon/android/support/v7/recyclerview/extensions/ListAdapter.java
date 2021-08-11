// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.recyclerview.extensions;

import java.util.List;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.util.AdapterListUpdateCallback;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

public abstract class ListAdapter<T, VH extends ViewHolder> extends Adapter<VH>
{
    private final AsyncListDiffer<T> mHelper;
    
    protected ListAdapter(@NonNull final AsyncDifferConfig<T> asyncDifferConfig) {
        this.mHelper = new AsyncListDiffer<T>(new AdapterListUpdateCallback(this), asyncDifferConfig);
    }
    
    protected ListAdapter(@NonNull final DiffUtil.ItemCallback<T> itemCallback) {
        this.mHelper = new AsyncListDiffer<T>(new AdapterListUpdateCallback(this), new AsyncDifferConfig.Builder<T>(itemCallback).build());
    }
    
    protected T getItem(final int n) {
        return this.mHelper.getCurrentList().get(n);
    }
    
    @Override
    public int getItemCount() {
        return this.mHelper.getCurrentList().size();
    }
    
    public void submitList(final List<T> list) {
        this.mHelper.submitList(list);
    }
}
