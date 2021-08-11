// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package retrofit2.converter.gson;

import com.google.gson.stream.JsonReader;
import com.google.gson.JsonIOException;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import okhttp3.ResponseBody;
import retrofit2.Converter;

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T>
{
    private final TypeAdapter<T> adapter;
    private final Gson gson;
    
    GsonResponseBodyConverter(final Gson gson, final TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }
    
    @Override
    public T convert(final ResponseBody responseBody) throws IOException {
        final JsonReader jsonReader = this.gson.newJsonReader(responseBody.charStream());
        T read;
        try {
            read = this.adapter.read(jsonReader);
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw new JsonIOException("JSON document was not fully consumed.");
            }
        }
        finally {
            responseBody.close();
        }
        responseBody.close();
        return read;
    }
}
