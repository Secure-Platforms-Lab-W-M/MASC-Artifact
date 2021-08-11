/*
 * Decompiled with CFR 0_124.
 */
package androidx.lifecycle;

import androidx.lifecycle.ViewModel;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ViewModelStore {
    private final HashMap<String, ViewModel> mMap = new HashMap();

    public final void clear() {
        Iterator<ViewModel> iterator = this.mMap.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().clear();
        }
        this.mMap.clear();
    }

    final ViewModel get(String string2) {
        return this.mMap.get(string2);
    }

    Set<String> keys() {
        return new HashSet<String>(this.mMap.keySet());
    }

    final void put(String object, ViewModel viewModel) {
        if ((object = this.mMap.put((String)object, viewModel)) != null) {
            object.onCleared();
        }
    }
}

