// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package retrofit2.converter.gson;

import okhttp3.ResponseBody;
import com.google.gson.reflect.TypeToken;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import com.google.gson.Gson;
import retrofit2.Converter;

public final class GsonConverterFactory extends Factory
{
    private final Gson gson;
    
    private GsonConverterFactory(final Gson gson) {
        this.gson = gson;
    }
    
    public static GsonConverterFactory create() {
        return create(new Gson());
    }
    
    public static GsonConverterFactory create(final Gson gson) {
        if (gson == null) {
            throw new NullPointerException("gson == null");
        }
        return new GsonConverterFactory(gson);
    }
    
    @Override
    public Converter<?, RequestBody> requestBodyConverter(final Type type, final Annotation[] array, final Annotation[] array2, final Retrofit retrofit) {
        return new GsonRequestBodyConverter<Object>(this.gson, this.gson.getAdapter(TypeToken.get(type)));
    }
    
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(final Type type, final Annotation[] array, final Retrofit retrofit) {
        return new GsonResponseBodyConverter<Object>(this.gson, this.gson.getAdapter(TypeToken.get(type)));
    }
}
