// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.http2;

public enum ErrorCode
{
    CANCEL(8), 
    COMPRESSION_ERROR(9), 
    CONNECT_ERROR(10), 
    ENHANCE_YOUR_CALM(11), 
    FLOW_CONTROL_ERROR(3), 
    HTTP_1_1_REQUIRED(13), 
    INADEQUATE_SECURITY(12), 
    INTERNAL_ERROR(2), 
    NO_ERROR(0), 
    PROTOCOL_ERROR(1), 
    REFUSED_STREAM(7);
    
    public final int httpCode;
    
    private ErrorCode(final int httpCode) {
        this.httpCode = httpCode;
    }
    
    public static ErrorCode fromHttp2(final int n) {
        final ErrorCode[] values = values();
        for (int length = values.length, i = 0; i < length; ++i) {
            final ErrorCode errorCode = values[i];
            if (errorCode.httpCode == n) {
                return errorCode;
            }
        }
        return null;
    }
}
