// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3;

import java.io.IOException;

public enum Protocol
{
    HTTP_1_0("http/1.0"), 
    HTTP_1_1("http/1.1"), 
    HTTP_2("h2"), 
    QUIC("quic"), 
    SPDY_3("spdy/3.1");
    
    private final String protocol;
    
    private Protocol(final String protocol) {
        this.protocol = protocol;
    }
    
    public static Protocol get(final String s) throws IOException {
        if (s.equals(Protocol.HTTP_1_0.protocol)) {
            return Protocol.HTTP_1_0;
        }
        if (s.equals(Protocol.HTTP_1_1.protocol)) {
            return Protocol.HTTP_1_1;
        }
        if (s.equals(Protocol.HTTP_2.protocol)) {
            return Protocol.HTTP_2;
        }
        if (s.equals(Protocol.SPDY_3.protocol)) {
            return Protocol.SPDY_3;
        }
        if (s.equals(Protocol.QUIC.protocol)) {
            return Protocol.QUIC;
        }
        throw new IOException("Unexpected protocol: " + s);
    }
    
    @Override
    public String toString() {
        return this.protocol;
    }
}
