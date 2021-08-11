// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.arch.lifecycle;

import java.lang.reflect.InvocationTargetException;
import android.app.Application;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

public class ViewModelProvider
{
    private static final String DEFAULT_KEY = "android.arch.lifecycle.ViewModelProvider.DefaultKey";
    private final Factory mFactory;
    private final ViewModelStore mViewModelStore;
    
    public ViewModelProvider(@NonNull final ViewModelStore mViewModelStore, @NonNull final Factory mFactory) {
        this.mFactory = mFactory;
        this.mViewModelStore = mViewModelStore;
    }
    
    public ViewModelProvider(@NonNull final ViewModelStoreOwner viewModelStoreOwner, @NonNull final Factory factory) {
        this(viewModelStoreOwner.getViewModelStore(), factory);
    }
    
    @NonNull
    public <T extends ViewModel> T get(@NonNull final Class<T> clazz) {
        final String canonicalName = clazz.getCanonicalName();
        if (canonicalName == null) {
            throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
        }
        return this.get("android.arch.lifecycle.ViewModelProvider.DefaultKey:" + canonicalName, clazz);
    }
    
    @MainThread
    @NonNull
    public <T extends ViewModel> T get(@NonNull final String s, @NonNull final Class<T> clazz) {
        final ViewModel value = this.mViewModelStore.get(s);
        if (clazz.isInstance(value)) {
            return (T)value;
        }
        if (value != null) {}
        final ViewModel create = this.mFactory.create(clazz);
        this.mViewModelStore.put(s, create);
        return (T)create;
    }
    
    public static class AndroidViewModelFactory extends NewInstanceFactory
    {
        private static AndroidViewModelFactory sInstance;
        private Application mApplication;
        
        public AndroidViewModelFactory(@NonNull final Application mApplication) {
            this.mApplication = mApplication;
        }
        
        public static AndroidViewModelFactory getInstance(@NonNull final Application application) {
            if (AndroidViewModelFactory.sInstance == null) {
                AndroidViewModelFactory.sInstance = new AndroidViewModelFactory(application);
            }
            return AndroidViewModelFactory.sInstance;
        }
        
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull final Class<T> clazz) {
            if (AndroidViewModel.class.isAssignableFrom(clazz)) {
                try {
                    return clazz.getConstructor(Application.class).newInstance(this.mApplication);
                }
                catch (NoSuchMethodException ex) {
                    throw new RuntimeException("Cannot create an instance of " + clazz, ex);
                }
                catch (IllegalAccessException ex2) {
                    throw new RuntimeException("Cannot create an instance of " + clazz, ex2);
                }
                catch (InstantiationException ex3) {
                    throw new RuntimeException("Cannot create an instance of " + clazz, ex3);
                }
                catch (InvocationTargetException ex4) {
                    throw new RuntimeException("Cannot create an instance of " + clazz, ex4);
                }
            }
            return super.create(clazz);
        }
    }
    
    public interface Factory
    {
        @NonNull
         <T extends ViewModel> T create(@NonNull final Class<T> p0);
    }
    
    public static class NewInstanceFactory implements Factory
    {
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull final Class<T> clazz) {
            try {
                return clazz.newInstance();
            }
            catch (InstantiationException ex) {
                throw new RuntimeException("Cannot create an instance of " + clazz, ex);
            }
            catch (IllegalAccessException ex2) {
                throw new RuntimeException("Cannot create an instance of " + clazz, ex2);
            }
        }
    }
}
