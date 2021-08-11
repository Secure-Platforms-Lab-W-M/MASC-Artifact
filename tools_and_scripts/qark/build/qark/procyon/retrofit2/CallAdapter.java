// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package retrofit2;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public interface CallAdapter<R, T>
{
    T adapt(final Call<R> p0);
    
    Type responseType();
    
    public abstract static class Factory
    {
        protected static Type getParameterUpperBound(final int n, final ParameterizedType parameterizedType) {
            return Utils.getParameterUpperBound(n, parameterizedType);
        }
        
        protected static Class<?> getRawType(final Type type) {
            return Utils.getRawType(type);
        }
        
        @Nullable
        public abstract CallAdapter<?, ?> get(final Type p0, final Annotation[] p1, final Retrofit p2);
    }
}
