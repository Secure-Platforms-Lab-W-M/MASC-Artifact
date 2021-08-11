// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.platform;

import java.util.logging.Level;
import javax.annotation.Nullable;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import okhttp3.internal.tls.BasicTrustRootIndex;
import okhttp3.internal.tls.TrustRootIndex;
import okhttp3.internal.tls.BasicCertificateChainCleaner;
import javax.net.ssl.X509TrustManager;
import okhttp3.internal.tls.CertificateChainCleaner;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.SSLSocket;
import java.lang.reflect.Field;
import java.security.Security;
import okio.Buffer;
import java.util.ArrayList;
import okhttp3.Protocol;
import java.util.List;
import okhttp3.OkHttpClient;
import java.util.logging.Logger;

public class Platform
{
    public static final int INFO = 4;
    private static final Platform PLATFORM;
    public static final int WARN = 5;
    private static final Logger logger;
    
    static {
        PLATFORM = findPlatform();
        logger = Logger.getLogger(OkHttpClient.class.getName());
    }
    
    public static List<String> alpnProtocolNames(final List<Protocol> list) {
        final ArrayList<String> list2 = new ArrayList<String>(list.size());
        for (int i = 0; i < list.size(); ++i) {
            final Protocol protocol = list.get(i);
            if (protocol != Protocol.HTTP_1_0) {
                list2.add(protocol.toString());
            }
        }
        return list2;
    }
    
    static byte[] concatLengthPrefixed(final List<Protocol> list) {
        final Buffer buffer = new Buffer();
        for (int i = 0; i < list.size(); ++i) {
            final Protocol protocol = list.get(i);
            if (protocol != Protocol.HTTP_1_0) {
                buffer.writeByte(protocol.toString().length());
                buffer.writeUtf8(protocol.toString());
            }
        }
        return buffer.readByteArray();
    }
    
    private static Platform findPlatform() {
        final Platform buildIfSupported = AndroidPlatform.buildIfSupported();
        if (buildIfSupported != null) {
            return buildIfSupported;
        }
        if (isConscryptPreferred()) {
            final Platform buildIfSupported2 = ConscryptPlatform.buildIfSupported();
            if (buildIfSupported2 != null) {
                return buildIfSupported2;
            }
        }
        final Jdk9Platform buildIfSupported3 = Jdk9Platform.buildIfSupported();
        if (buildIfSupported3 != null) {
            return buildIfSupported3;
        }
        final Platform buildIfSupported4 = JdkWithJettyBootPlatform.buildIfSupported();
        if (buildIfSupported4 != null) {
            return buildIfSupported4;
        }
        return new Platform();
    }
    
    public static Platform get() {
        return Platform.PLATFORM;
    }
    
    public static boolean isConscryptPreferred() {
        return "conscrypt".equals(System.getProperty("okhttp.platform")) || "Conscrypt".equals(Security.getProviders()[0].getName());
    }
    
    static <T> T readFieldOrNull(Object fieldOrNull, final Class<T> clazz, final String s) {
        final T t = null;
        Class<?> clazz2 = fieldOrNull.getClass();
        while (clazz2 != Object.class) {
            try {
                final Field declaredField = clazz2.getDeclaredField(s);
                declaredField.setAccessible(true);
                final Object value = declaredField.get(fieldOrNull);
                final Object fieldOrNull2 = t;
                if (value == null) {
                    return (T)fieldOrNull2;
                }
                if (!clazz.isInstance(value)) {
                    return null;
                }
                return clazz.cast(value);
            }
            catch (IllegalAccessException ex) {
                throw new AssertionError();
            }
            catch (NoSuchFieldException ex2) {
                clazz2 = clazz2.getSuperclass();
                continue;
            }
            break;
        }
        Object fieldOrNull2 = t;
        if (s.equals("delegate")) {
            return (T)fieldOrNull2;
        }
        fieldOrNull = readFieldOrNull(fieldOrNull, Object.class, "delegate");
        fieldOrNull2 = t;
        if (fieldOrNull != null) {
            fieldOrNull2 = readFieldOrNull(fieldOrNull, (Class<Object>)clazz, s);
            return (T)fieldOrNull2;
        }
        return (T)fieldOrNull2;
    }
    
    public void afterHandshake(final SSLSocket sslSocket) {
    }
    
    public CertificateChainCleaner buildCertificateChainCleaner(final SSLSocketFactory sslSocketFactory) {
        final X509TrustManager trustManager = this.trustManager(sslSocketFactory);
        if (trustManager == null) {
            throw new IllegalStateException("Unable to extract the trust manager on " + get() + ", sslSocketFactory is " + sslSocketFactory.getClass());
        }
        return this.buildCertificateChainCleaner(trustManager);
    }
    
    public CertificateChainCleaner buildCertificateChainCleaner(final X509TrustManager x509TrustManager) {
        return new BasicCertificateChainCleaner(this.buildTrustRootIndex(x509TrustManager));
    }
    
    public TrustRootIndex buildTrustRootIndex(final X509TrustManager x509TrustManager) {
        return new BasicTrustRootIndex(x509TrustManager.getAcceptedIssuers());
    }
    
    public void configureTlsExtensions(final SSLSocket sslSocket, final String s, final List<Protocol> list) {
    }
    
    public void connectSocket(final Socket socket, final InetSocketAddress inetSocketAddress, final int n) throws IOException {
        socket.connect(inetSocketAddress, n);
    }
    
    public String getPrefix() {
        return "OkHttp";
    }
    
    public SSLContext getSSLContext() {
        try {
            return SSLContext.getInstance("TLS");
        }
        catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("No TLS provider", ex);
        }
    }
    
    @Nullable
    public String getSelectedProtocol(final SSLSocket sslSocket) {
        return null;
    }
    
    public Object getStackTraceForCloseable(final String s) {
        if (Platform.logger.isLoggable(Level.FINE)) {
            return new Throwable(s);
        }
        return null;
    }
    
    public boolean isCleartextTrafficPermitted(final String s) {
        return true;
    }
    
    public void log(final int n, final String s, final Throwable t) {
        Level level;
        if (n == 5) {
            level = Level.WARNING;
        }
        else {
            level = Level.INFO;
        }
        Platform.logger.log(level, s, t);
    }
    
    public void logCloseableLeak(final String s, final Object o) {
        String string = s;
        if (o == null) {
            string = s + " To see where this was allocated, set the OkHttpClient logger level to FINE: Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);";
        }
        this.log(5, string, (Throwable)o);
    }
    
    protected X509TrustManager trustManager(final SSLSocketFactory sslSocketFactory) {
        try {
            final Object fieldOrNull = readFieldOrNull(sslSocketFactory, Class.forName("sun.security.ssl.SSLContextImpl"), "context");
            if (fieldOrNull == null) {
                return null;
            }
            return readFieldOrNull(fieldOrNull, X509TrustManager.class, "trustManager");
        }
        catch (ClassNotFoundException ex) {
            return null;
        }
    }
}
