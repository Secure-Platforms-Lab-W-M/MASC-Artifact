/*
 * Decompiled with CFR 0_124.
 */
package androidx.lifecycle;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

public class Transformations {
    private Transformations() {
    }

    public static <X> LiveData<X> distinctUntilChanged(LiveData<X> liveData) {
        final MediatorLiveData mediatorLiveData = new MediatorLiveData();
        mediatorLiveData.addSource(liveData, new Observer<X>(){
            boolean mFirstTime;
            {
                this.mFirstTime = true;
            }

            @Override
            public void onChanged(X x) {
                Object t = mediatorLiveData.getValue();
                if (this.mFirstTime || t == null && x != null || t != null && !t.equals(x)) {
                    this.mFirstTime = false;
                    mediatorLiveData.setValue(x);
                }
            }
        });
        return mediatorLiveData;
    }

    public static <X, Y> LiveData<Y> map(LiveData<X> liveData, final Function<X, Y> function) {
        final MediatorLiveData mediatorLiveData = new MediatorLiveData();
        mediatorLiveData.addSource(liveData, new Observer<X>(){

            @Override
            public void onChanged(X x) {
                mediatorLiveData.setValue(function.apply(x));
            }
        });
        return mediatorLiveData;
    }

    public static <X, Y> LiveData<Y> switchMap(LiveData<X> liveData, final Function<X, LiveData<Y>> function) {
        final MediatorLiveData mediatorLiveData = new MediatorLiveData();
        mediatorLiveData.addSource(liveData, new Observer<X>(){
            LiveData<Y> mSource;

            @Override
            public void onChanged(X object) {
                LiveData<Y> liveData = this.mSource;
                if (liveData == (object = (LiveData)function.apply(object))) {
                    return;
                }
                if (liveData != null) {
                    mediatorLiveData.removeSource(liveData);
                }
                this.mSource = object;
                if (object != null) {
                    mediatorLiveData.addSource(object, new Observer<Y>(){

                        @Override
                        public void onChanged(Y y) {
                            mediatorLiveData.setValue(y);
                        }
                    });
                }
            }

        });
        return mediatorLiveData;
    }

}

