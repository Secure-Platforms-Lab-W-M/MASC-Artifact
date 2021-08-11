// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.arch.lifecycle;

import java.util.Iterator;
import java.util.HashMap;

public class ViewModelStore
{
    private final HashMap<String, ViewModel> mMap;
    
    public ViewModelStore() {
        this.mMap = new HashMap<String, ViewModel>();
    }
    
    public final void clear() {
        final Iterator<ViewModel> iterator = this.mMap.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().onCleared();
        }
        this.mMap.clear();
    }
    
    final ViewModel get(final String s) {
        return this.mMap.get(s);
    }
    
    final void put(final String s, final ViewModel viewModel) {
        final ViewModel viewModel2 = this.mMap.get(s);
        if (viewModel2 != null) {
            viewModel2.onCleared();
        }
        this.mMap.put(s, viewModel);
    }
}
