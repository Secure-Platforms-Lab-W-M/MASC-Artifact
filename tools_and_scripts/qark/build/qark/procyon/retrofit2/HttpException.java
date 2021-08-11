// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package retrofit2;

public class HttpException extends RuntimeException
{
    private final int code;
    private final String message;
    private final transient Response<?> response;
    
    public HttpException(final Response<?> response) {
        super(getMessage(response));
        this.code = response.code();
        this.message = response.message();
        this.response = response;
    }
    
    private static String getMessage(final Response<?> response) {
        Utils.checkNotNull(response, "response == null");
        return "HTTP " + response.code() + " " + response.message();
    }
    
    public int code() {
        return this.code;
    }
    
    public String message() {
        return this.message;
    }
    
    public Response<?> response() {
        return this.response;
    }
}
