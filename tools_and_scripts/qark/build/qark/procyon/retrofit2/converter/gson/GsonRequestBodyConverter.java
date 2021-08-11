// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package retrofit2.converter.gson;

import com.google.gson.stream.JsonWriter;
import java.io.Writer;
import java.io.OutputStreamWriter;
import okio.Buffer;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import java.nio.charset.Charset;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

final class GsonRequestBodyConverter<T> implements Converter<T, RequestBody>
{
    private static final MediaType MEDIA_TYPE;
    private static final Charset UTF_8;
    private final TypeAdapter<T> adapter;
    private final Gson gson;
    
    static {
        MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
        UTF_8 = Charset.forName("UTF-8");
    }
    
    GsonRequestBodyConverter(final Gson gson, final TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }
    
    @Override
    public RequestBody convert(final T t) throws IOException {
        final Buffer buffer = new Buffer();
        final JsonWriter jsonWriter = this.gson.newJsonWriter(new OutputStreamWriter(buffer.outputStream(), GsonRequestBodyConverter.UTF_8));
        this.adapter.write(jsonWriter, t);
        jsonWriter.close();
        return RequestBody.create(GsonRequestBodyConverter.MEDIA_TYPE, buffer.readByteString());
    }
}
