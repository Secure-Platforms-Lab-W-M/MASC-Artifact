// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.http;

import java.io.IOException;
import java.net.ProtocolException;
import okhttp3.Response;
import okhttp3.Protocol;

public final class StatusLine
{
    public static final int HTTP_CONTINUE = 100;
    public static final int HTTP_PERM_REDIRECT = 308;
    public static final int HTTP_TEMP_REDIRECT = 307;
    public final int code;
    public final String message;
    public final Protocol protocol;
    
    public StatusLine(final Protocol protocol, final int code, final String message) {
        this.protocol = protocol;
        this.code = code;
        this.message = message;
    }
    
    public static StatusLine get(final Response response) {
        return new StatusLine(response.protocol(), response.code(), response.message());
    }
    
    public static StatusLine parse(final String s) throws IOException {
        int n2;
        Protocol protocol;
        if (s.startsWith("HTTP/1.")) {
            if (s.length() < 9 || s.charAt(8) != ' ') {
                throw new ProtocolException("Unexpected status line: " + s);
            }
            final int n = s.charAt(7) - '0';
            n2 = 9;
            if (n == 0) {
                protocol = Protocol.HTTP_1_0;
            }
            else {
                if (n != 1) {
                    throw new ProtocolException("Unexpected status line: " + s);
                }
                protocol = Protocol.HTTP_1_1;
            }
        }
        else {
            if (!s.startsWith("ICY ")) {
                throw new ProtocolException("Unexpected status line: " + s);
            }
            protocol = Protocol.HTTP_1_0;
            n2 = 4;
        }
        if (s.length() < n2 + 3) {
            throw new ProtocolException("Unexpected status line: " + s);
        }
        int int1;
        try {
            int1 = Integer.parseInt(s.substring(n2, n2 + 3));
            final String substring = "";
            if (s.length() <= n2 + 3) {
                return new StatusLine(protocol, int1, substring);
            }
            if (s.charAt(n2 + 3) != ' ') {
                throw new ProtocolException("Unexpected status line: " + s);
            }
        }
        catch (NumberFormatException ex) {
            throw new ProtocolException("Unexpected status line: " + s);
        }
        final String substring = s.substring(n2 + 4);
        return new StatusLine(protocol, int1, substring);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        String s;
        if (this.protocol == Protocol.HTTP_1_0) {
            s = "HTTP/1.0";
        }
        else {
            s = "HTTP/1.1";
        }
        sb.append(s);
        sb.append(' ').append(this.code);
        if (this.message != null) {
            sb.append(' ').append(this.message);
        }
        return sb.toString();
    }
}
