// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package retrofit2;

import okhttp3.ResponseBody;
import javax.annotation.Nullable;
import okhttp3.RequestBody;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import java.io.IOException;

public interface Converter<F, T>
{
    T convert(final F p0) throws IOException;
    
    public abstract static class Factory
    {
        protected static Type getParameterUpperBound(final int n, final ParameterizedType parameterizedType) {
            return Utils.getParameterUpperBound(n, parameterizedType);
        }
        
        protected static Class<?> getRawType(final Type type) {
            return Utils.getRawType(type);
        }
        
        @Nullable
        public Converter<?, RequestBody> requestBodyConverter(final Type type, final Annotation[] array, final Annotation[] array2, final Retrofit retrofit) {
            return null;
        }
        
        @Nullable
        public Converter<ResponseBody, ?> responseBodyConverter(final Type type, final Annotation[] array, final Retrofit retrofit) {
            return null;
        }
        
        @Nullable
        public Converter<?, String> stringConverter(final Type type, final Annotation[] array, final Retrofit retrofit) {
            return null;
        }
    }
}
