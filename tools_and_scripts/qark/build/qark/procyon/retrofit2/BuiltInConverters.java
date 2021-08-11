// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package retrofit2;

import java.io.IOException;
import retrofit2.http.Streaming;
import okhttp3.ResponseBody;
import okhttp3.RequestBody;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

final class BuiltInConverters extends Factory
{
    @Override
    public Converter<?, RequestBody> requestBodyConverter(final Type type, final Annotation[] array, final Annotation[] array2, final Retrofit retrofit) {
        if (RequestBody.class.isAssignableFrom(Utils.getRawType(type))) {
            return RequestBodyConverter.INSTANCE;
        }
        return null;
    }
    
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(final Type type, final Annotation[] array, final Retrofit retrofit) {
        if (type == ResponseBody.class) {
            if (Utils.isAnnotationPresent(array, Streaming.class)) {
                return StreamingResponseBodyConverter.INSTANCE;
            }
            return BufferingResponseBodyConverter.INSTANCE;
        }
        else {
            if (type == Void.class) {
                return VoidResponseBodyConverter.INSTANCE;
            }
            return null;
        }
    }
    
    static final class BufferingResponseBodyConverter implements Converter<ResponseBody, ResponseBody>
    {
        static final BufferingResponseBodyConverter INSTANCE;
        
        static {
            INSTANCE = new BufferingResponseBodyConverter();
        }
        
        @Override
        public ResponseBody convert(final ResponseBody responseBody) throws IOException {
            try {
                return Utils.buffer(responseBody);
            }
            finally {
                responseBody.close();
            }
        }
    }
    
    static final class RequestBodyConverter implements Converter<RequestBody, RequestBody>
    {
        static final RequestBodyConverter INSTANCE;
        
        static {
            INSTANCE = new RequestBodyConverter();
        }
        
        @Override
        public RequestBody convert(final RequestBody requestBody) {
            return requestBody;
        }
    }
    
    static final class StreamingResponseBodyConverter implements Converter<ResponseBody, ResponseBody>
    {
        static final StreamingResponseBodyConverter INSTANCE;
        
        static {
            INSTANCE = new StreamingResponseBodyConverter();
        }
        
        @Override
        public ResponseBody convert(final ResponseBody responseBody) {
            return responseBody;
        }
    }
    
    static final class ToStringConverter implements Converter<Object, String>
    {
        static final ToStringConverter INSTANCE;
        
        static {
            INSTANCE = new ToStringConverter();
        }
        
        @Override
        public String convert(final Object o) {
            return o.toString();
        }
    }
    
    static final class VoidResponseBodyConverter implements Converter<ResponseBody, Void>
    {
        static final VoidResponseBodyConverter INSTANCE;
        
        static {
            INSTANCE = new VoidResponseBodyConverter();
        }
        
        @Override
        public Void convert(final ResponseBody responseBody) {
            responseBody.close();
            return null;
        }
    }
}
