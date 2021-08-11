// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3;

import java.net.InetAddress;
import java.util.List;
import javax.annotation.Nullable;
import java.net.Proxy;
import java.net.InetSocketAddress;
import java.io.IOException;

public abstract class EventListener
{
    public static final EventListener NONE;
    
    static {
        NONE = new EventListener() {};
    }
    
    static Factory factory(final EventListener eventListener) {
        return (Factory)new Factory() {
            @Override
            public EventListener create(final Call call) {
                return eventListener;
            }
        };
    }
    
    public void callEnd(final Call call) {
    }
    
    public void callFailed(final Call call, final IOException ex) {
    }
    
    public void callStart(final Call call) {
    }
    
    public void connectEnd(final Call call, final InetSocketAddress inetSocketAddress, final Proxy proxy, @Nullable final Protocol protocol) {
    }
    
    public void connectFailed(final Call call, final InetSocketAddress inetSocketAddress, final Proxy proxy, @Nullable final Protocol protocol, final IOException ex) {
    }
    
    public void connectStart(final Call call, final InetSocketAddress inetSocketAddress, final Proxy proxy) {
    }
    
    public void connectionAcquired(final Call call, final Connection connection) {
    }
    
    public void connectionReleased(final Call call, final Connection connection) {
    }
    
    public void dnsEnd(final Call call, final String s, final List<InetAddress> list) {
    }
    
    public void dnsStart(final Call call, final String s) {
    }
    
    public void requestBodyEnd(final Call call, final long n) {
    }
    
    public void requestBodyStart(final Call call) {
    }
    
    public void requestHeadersEnd(final Call call, final Request request) {
    }
    
    public void requestHeadersStart(final Call call) {
    }
    
    public void responseBodyEnd(final Call call, final long n) {
    }
    
    public void responseBodyStart(final Call call) {
    }
    
    public void responseHeadersEnd(final Call call, final Response response) {
    }
    
    public void responseHeadersStart(final Call call) {
    }
    
    public void secureConnectEnd(final Call call, @Nullable final Handshake handshake) {
    }
    
    public void secureConnectStart(final Call call) {
    }
    
    public interface Factory
    {
        EventListener create(final Call p0);
    }
}
