// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.connection;

import javax.net.ssl.SSLProtocolException;
import javax.net.ssl.SSLPeerUnverifiedException;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLHandshakeException;
import java.io.InterruptedIOException;
import java.net.ProtocolException;
import java.io.IOException;
import okhttp3.internal.Internal;
import java.net.UnknownServiceException;
import java.util.Arrays;
import javax.net.ssl.SSLSocket;
import okhttp3.ConnectionSpec;
import java.util.List;

public final class ConnectionSpecSelector
{
    private final List<ConnectionSpec> connectionSpecs;
    private boolean isFallback;
    private boolean isFallbackPossible;
    private int nextModeIndex;
    
    public ConnectionSpecSelector(final List<ConnectionSpec> connectionSpecs) {
        this.nextModeIndex = 0;
        this.connectionSpecs = connectionSpecs;
    }
    
    private boolean isFallbackPossible(final SSLSocket sslSocket) {
        for (int i = this.nextModeIndex; i < this.connectionSpecs.size(); ++i) {
            if (this.connectionSpecs.get(i).isCompatible(sslSocket)) {
                return true;
            }
        }
        return false;
    }
    
    public ConnectionSpec configureSecureSocket(final SSLSocket sslSocket) throws IOException {
        final ConnectionSpec connectionSpec = null;
        int nextModeIndex = this.nextModeIndex;
        final int size = this.connectionSpecs.size();
        ConnectionSpec connectionSpec2;
        while (true) {
            connectionSpec2 = connectionSpec;
            if (nextModeIndex >= size) {
                break;
            }
            connectionSpec2 = this.connectionSpecs.get(nextModeIndex);
            if (connectionSpec2.isCompatible(sslSocket)) {
                this.nextModeIndex = nextModeIndex + 1;
                break;
            }
            ++nextModeIndex;
        }
        if (connectionSpec2 == null) {
            throw new UnknownServiceException("Unable to find acceptable protocols. isFallback=" + this.isFallback + ", modes=" + this.connectionSpecs + ", supported protocols=" + Arrays.toString(sslSocket.getEnabledProtocols()));
        }
        this.isFallbackPossible = this.isFallbackPossible(sslSocket);
        Internal.instance.apply(connectionSpec2, sslSocket, this.isFallback);
        return connectionSpec2;
    }
    
    public boolean connectionFailed(final IOException ex) {
        this.isFallback = true;
        return this.isFallbackPossible && !(ex instanceof ProtocolException) && !(ex instanceof InterruptedIOException) && (!(ex instanceof SSLHandshakeException) || !(ex.getCause() instanceof CertificateException)) && !(ex instanceof SSLPeerUnverifiedException) && (ex instanceof SSLHandshakeException || ex instanceof SSLProtocolException);
    }
}
