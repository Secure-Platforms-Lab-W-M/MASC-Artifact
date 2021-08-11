// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.platform;

import javax.net.ssl.X509TrustManager;
import javax.net.ssl.SSLSocketFactory;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import okhttp3.Protocol;
import java.util.List;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLParameters;
import java.lang.reflect.Method;

final class Jdk9Platform extends Platform
{
    final Method getProtocolMethod;
    final Method setProtocolMethod;
    
    Jdk9Platform(final Method setProtocolMethod, final Method getProtocolMethod) {
        this.setProtocolMethod = setProtocolMethod;
        this.getProtocolMethod = getProtocolMethod;
    }
    
    public static Jdk9Platform buildIfSupported() {
        try {
            return new Jdk9Platform(SSLParameters.class.getMethod("setApplicationProtocols", String[].class), SSLSocket.class.getMethod("getApplicationProtocol", (Class<?>[])new Class[0]));
        }
        catch (NoSuchMethodException ex) {
            return null;
        }
    }
    
    @Override
    public void configureTlsExtensions(final SSLSocket ex, final String s, final List<Protocol> list) {
        try {
            final SSLParameters sslParameters = ((SSLSocket)ex).getSSLParameters();
            final List<String> alpnProtocolNames = Platform.alpnProtocolNames(list);
            this.setProtocolMethod.invoke(sslParameters, alpnProtocolNames.toArray(new String[alpnProtocolNames.size()]));
            ((SSLSocket)ex).setSSLParameters(sslParameters);
        }
        catch (IllegalAccessException ex2) {}
        catch (InvocationTargetException ex) {
            goto Label_0048;
        }
    }
    
    @Nullable
    @Override
    public String getSelectedProtocol(SSLSocket ex) {
        try {
            ex = (InvocationTargetException)this.getProtocolMethod.invoke(ex, new Object[0]);
            if (ex == null || ((String)ex).equals("")) {
                ex = null;
            }
            return (String)ex;
        }
        catch (IllegalAccessException ex2) {}
        catch (InvocationTargetException ex) {
            goto Label_0036;
        }
    }
    
    public X509TrustManager trustManager(final SSLSocketFactory sslSocketFactory) {
        throw new UnsupportedOperationException("clientBuilder.sslSocketFactory(SSLSocketFactory) not supported on JDK 9+");
    }
}
