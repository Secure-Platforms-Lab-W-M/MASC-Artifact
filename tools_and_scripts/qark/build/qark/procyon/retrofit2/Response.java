// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package retrofit2;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Protocol;
import okhttp3.ResponseBody;
import javax.annotation.Nullable;

public final class Response<T>
{
    @Nullable
    private final T body;
    @Nullable
    private final ResponseBody errorBody;
    private final okhttp3.Response rawResponse;
    
    private Response(final okhttp3.Response rawResponse, @Nullable final T body, @Nullable final ResponseBody errorBody) {
        this.rawResponse = rawResponse;
        this.body = body;
        this.errorBody = errorBody;
    }
    
    public static <T> Response<T> error(final int n, final ResponseBody responseBody) {
        if (n < 400) {
            throw new IllegalArgumentException("code < 400: " + n);
        }
        return error(responseBody, new okhttp3.Response.Builder().code(n).message("Response.error()").protocol(Protocol.HTTP_1_1).request(new Request.Builder().url("http://localhost/").build()).build());
    }
    
    public static <T> Response<T> error(final ResponseBody responseBody, final okhttp3.Response response) {
        Utils.checkNotNull(responseBody, "body == null");
        Utils.checkNotNull(response, "rawResponse == null");
        if (response.isSuccessful()) {
            throw new IllegalArgumentException("rawResponse should not be successful response");
        }
        return new Response<T>(response, null, responseBody);
    }
    
    public static <T> Response<T> success(@Nullable final T t) {
        return success(t, new okhttp3.Response.Builder().code(200).message("OK").protocol(Protocol.HTTP_1_1).request(new Request.Builder().url("http://localhost/").build()).build());
    }
    
    public static <T> Response<T> success(@Nullable final T t, final Headers headers) {
        Utils.checkNotNull(headers, "headers == null");
        return success(t, new okhttp3.Response.Builder().code(200).message("OK").protocol(Protocol.HTTP_1_1).headers(headers).request(new Request.Builder().url("http://localhost/").build()).build());
    }
    
    public static <T> Response<T> success(@Nullable final T t, final okhttp3.Response response) {
        Utils.checkNotNull(response, "rawResponse == null");
        if (!response.isSuccessful()) {
            throw new IllegalArgumentException("rawResponse must be successful response");
        }
        return new Response<T>(response, t, null);
    }
    
    @Nullable
    public T body() {
        return this.body;
    }
    
    public int code() {
        return this.rawResponse.code();
    }
    
    @Nullable
    public ResponseBody errorBody() {
        return this.errorBody;
    }
    
    public Headers headers() {
        return this.rawResponse.headers();
    }
    
    public boolean isSuccessful() {
        return this.rawResponse.isSuccessful();
    }
    
    public String message() {
        return this.rawResponse.message();
    }
    
    public okhttp3.Response raw() {
        return this.rawResponse;
    }
    
    @Override
    public String toString() {
        return this.rawResponse.toString();
    }
}
