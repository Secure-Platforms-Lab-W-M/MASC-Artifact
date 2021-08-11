// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.arch.lifecycle;

import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class CompositeGeneratedAdaptersObserver implements GenericLifecycleObserver
{
    private final GeneratedAdapter[] mGeneratedAdapters;
    
    CompositeGeneratedAdaptersObserver(final GeneratedAdapter[] mGeneratedAdapters) {
        this.mGeneratedAdapters = mGeneratedAdapters;
    }
    
    @Override
    public void onStateChanged(final LifecycleOwner lifecycleOwner, final Lifecycle.Event event) {
        final int n = 0;
        final MethodCallsLogger methodCallsLogger = new MethodCallsLogger();
        final GeneratedAdapter[] mGeneratedAdapters = this.mGeneratedAdapters;
        for (int length = mGeneratedAdapters.length, i = 0; i < length; ++i) {
            mGeneratedAdapters[i].callMethods(lifecycleOwner, event, false, methodCallsLogger);
        }
        final GeneratedAdapter[] mGeneratedAdapters2 = this.mGeneratedAdapters;
        for (int length2 = mGeneratedAdapters2.length, j = n; j < length2; ++j) {
            mGeneratedAdapters2[j].callMethods(lifecycleOwner, event, true, methodCallsLogger);
        }
    }
}
