// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.platform;

import java.security.cert.TrustAnchor;
import javax.net.ssl.SSLPeerUnverifiedException;
import java.security.cert.Certificate;
import javax.net.ssl.SSLSocketFactory;
import android.util.Log;
import javax.annotation.Nullable;
import android.os.Build$VERSION;
import java.io.IOException;
import okhttp3.internal.Util;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import okhttp3.Protocol;
import java.util.List;
import javax.net.ssl.SSLSocket;
import java.lang.reflect.Method;
import okhttp3.internal.tls.TrustRootIndex;
import java.security.cert.X509Certificate;
import okhttp3.internal.tls.CertificateChainCleaner;
import javax.net.ssl.X509TrustManager;
import java.security.Security;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

class AndroidPlatform extends Platform
{
    private static final int MAX_LOG_LENGTH = 4000;
    private final CloseGuard closeGuard;
    private final OptionalMethod<Socket> getAlpnSelectedProtocol;
    private final OptionalMethod<Socket> setAlpnProtocols;
    private final OptionalMethod<Socket> setHostname;
    private final OptionalMethod<Socket> setUseSessionTickets;
    private final Class<?> sslParametersClass;
    
    AndroidPlatform(final Class<?> sslParametersClass, final OptionalMethod<Socket> setUseSessionTickets, final OptionalMethod<Socket> setHostname, final OptionalMethod<Socket> getAlpnSelectedProtocol, final OptionalMethod<Socket> setAlpnProtocols) {
        this.closeGuard = CloseGuard.get();
        this.sslParametersClass = sslParametersClass;
        this.setUseSessionTickets = setUseSessionTickets;
        this.setHostname = setHostname;
        this.getAlpnSelectedProtocol = getAlpnSelectedProtocol;
        this.setAlpnProtocols = setAlpnProtocols;
    }
    
    private boolean api23IsCleartextTrafficPermitted(final String s, final Class<?> clazz, final Object o) throws InvocationTargetException, IllegalAccessException {
        try {
            return (boolean)clazz.getMethod("isCleartextTrafficPermitted", (Class<?>[])new Class[0]).invoke(o, new Object[0]);
        }
        catch (NoSuchMethodException ex) {
            return super.isCleartextTrafficPermitted(s);
        }
    }
    
    private boolean api24IsCleartextTrafficPermitted(final String s, final Class<?> clazz, final Object o) throws InvocationTargetException, IllegalAccessException {
        try {
            return (boolean)clazz.getMethod("isCleartextTrafficPermitted", String.class).invoke(o, s);
        }
        catch (NoSuchMethodException ex) {
            return this.api23IsCleartextTrafficPermitted(s, clazz, o);
        }
    }
    
    public static Platform buildIfSupported() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     2: invokestatic    java/lang/Class.forName:(Ljava/lang/String;)Ljava/lang/Class;
        //     5: astore_0       
        //     6: new             Lokhttp3/internal/platform/OptionalMethod;
        //     9: dup            
        //    10: aconst_null    
        //    11: ldc             "setUseSessionTickets"
        //    13: iconst_1       
        //    14: anewarray       Ljava/lang/Class;
        //    17: dup            
        //    18: iconst_0       
        //    19: getstatic       java/lang/Boolean.TYPE:Ljava/lang/Class;
        //    22: aastore        
        //    23: invokespecial   okhttp3/internal/platform/OptionalMethod.<init>:(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;)V
        //    26: astore_3       
        //    27: new             Lokhttp3/internal/platform/OptionalMethod;
        //    30: dup            
        //    31: aconst_null    
        //    32: ldc             "setHostname"
        //    34: iconst_1       
        //    35: anewarray       Ljava/lang/Class;
        //    38: dup            
        //    39: iconst_0       
        //    40: ldc             Ljava/lang/String;.class
        //    42: aastore        
        //    43: invokespecial   okhttp3/internal/platform/OptionalMethod.<init>:(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;)V
        //    46: astore          4
        //    48: aconst_null    
        //    49: astore_1       
        //    50: aconst_null    
        //    51: astore_2       
        //    52: invokestatic    okhttp3/internal/platform/AndroidPlatform.supportsAlpn:()Z
        //    55: ifeq            94
        //    58: new             Lokhttp3/internal/platform/OptionalMethod;
        //    61: dup            
        //    62: ldc             [B.class
        //    64: ldc             "getAlpnSelectedProtocol"
        //    66: iconst_0       
        //    67: anewarray       Ljava/lang/Class;
        //    70: invokespecial   okhttp3/internal/platform/OptionalMethod.<init>:(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;)V
        //    73: astore_1       
        //    74: new             Lokhttp3/internal/platform/OptionalMethod;
        //    77: dup            
        //    78: aconst_null    
        //    79: ldc             "setAlpnProtocols"
        //    81: iconst_1       
        //    82: anewarray       Ljava/lang/Class;
        //    85: dup            
        //    86: iconst_0       
        //    87: ldc             [B.class
        //    89: aastore        
        //    90: invokespecial   okhttp3/internal/platform/OptionalMethod.<init>:(Ljava/lang/Class;Ljava/lang/String;[Ljava/lang/Class;)V
        //    93: astore_2       
        //    94: new             Lokhttp3/internal/platform/AndroidPlatform;
        //    97: dup            
        //    98: aload_0        
        //    99: aload_3        
        //   100: aload           4
        //   102: aload_1        
        //   103: aload_2        
        //   104: invokespecial   okhttp3/internal/platform/AndroidPlatform.<init>:(Ljava/lang/Class;Lokhttp3/internal/platform/OptionalMethod;Lokhttp3/internal/platform/OptionalMethod;Lokhttp3/internal/platform/OptionalMethod;Lokhttp3/internal/platform/OptionalMethod;)V
        //   107: areturn        
        //   108: astore_0       
        //   109: ldc             "org.apache.harmony.xnet.provider.jsse.SSLParametersImpl"
        //   111: invokestatic    java/lang/Class.forName:(Ljava/lang/String;)Ljava/lang/Class;
        //   114: astore_0       
        //   115: goto            6
        //   118: astore_0       
        //   119: aconst_null    
        //   120: areturn        
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                              
        //  -----  -----  -----  -----  ----------------------------------
        //  0      6      108    118    Ljava/lang/ClassNotFoundException;
        //  6      48     118    121    Ljava/lang/ClassNotFoundException;
        //  52     94     118    121    Ljava/lang/ClassNotFoundException;
        //  94     108    118    121    Ljava/lang/ClassNotFoundException;
        //  109    115    118    121    Ljava/lang/ClassNotFoundException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0006:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:141)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private static boolean supportsAlpn() {
        if (Security.getProvider("GMSCore_OpenSSL") != null) {
            return true;
        }
        try {
            Class.forName("android.net.Network");
            return true;
        }
        catch (ClassNotFoundException ex) {
            return false;
        }
    }
    
    @Override
    public CertificateChainCleaner buildCertificateChainCleaner(final X509TrustManager x509TrustManager) {
        try {
            final Class<?> forName = Class.forName("android.net.http.X509TrustManagerExtensions");
            return new AndroidCertificateChainCleaner(forName.getConstructor(X509TrustManager.class).newInstance(x509TrustManager), forName.getMethod("checkServerTrusted", X509Certificate[].class, String.class, String.class));
        }
        catch (Exception ex) {
            return super.buildCertificateChainCleaner(x509TrustManager);
        }
    }
    
    @Override
    public TrustRootIndex buildTrustRootIndex(final X509TrustManager x509TrustManager) {
        try {
            final Method declaredMethod = x509TrustManager.getClass().getDeclaredMethod("findTrustAnchorByIssuerAndSignature", X509Certificate.class);
            declaredMethod.setAccessible(true);
            return new AndroidTrustRootIndex(x509TrustManager, declaredMethod);
        }
        catch (NoSuchMethodException ex) {
            return super.buildTrustRootIndex(x509TrustManager);
        }
    }
    
    @Override
    public void configureTlsExtensions(final SSLSocket sslSocket, final String s, final List<Protocol> list) {
        if (s != null) {
            this.setUseSessionTickets.invokeOptionalWithoutCheckedException(sslSocket, true);
            this.setHostname.invokeOptionalWithoutCheckedException(sslSocket, s);
        }
        if (this.setAlpnProtocols != null && this.setAlpnProtocols.isSupported(sslSocket)) {
            this.setAlpnProtocols.invokeWithoutCheckedException(sslSocket, Platform.concatLengthPrefixed(list));
        }
    }
    
    @Override
    public void connectSocket(final Socket socket, final InetSocketAddress inetSocketAddress, final int n) throws IOException {
        try {
            socket.connect(inetSocketAddress, n);
        }
        catch (AssertionError assertionError) {
            if (Util.isAndroidGetsocknameError(assertionError)) {
                throw new IOException(assertionError);
            }
            throw assertionError;
        }
        catch (SecurityException ex2) {
            final IOException ex = new IOException("Exception in connect");
            ex.initCause(ex2);
            throw ex;
        }
        catch (ClassCastException ex4) {
            if (Build$VERSION.SDK_INT == 26) {
                final IOException ex3 = new IOException("Exception in connect");
                ex3.initCause(ex4);
                throw ex3;
            }
            throw ex4;
        }
    }
    
    @Nullable
    @Override
    public String getSelectedProtocol(final SSLSocket sslSocket) {
        if (this.getAlpnSelectedProtocol != null && this.getAlpnSelectedProtocol.isSupported(sslSocket)) {
            final byte[] array = (byte[])this.getAlpnSelectedProtocol.invokeWithoutCheckedException(sslSocket, new Object[0]);
            if (array != null) {
                return new String(array, Util.UTF_8);
            }
        }
        return null;
    }
    
    @Override
    public Object getStackTraceForCloseable(final String s) {
        return this.closeGuard.createAndOpen(s);
    }
    
    @Override
    public boolean isCleartextTrafficPermitted(final String ex) {
        try {
            final Class<?> forName = Class.forName("android.security.NetworkSecurityPolicy");
            return this.api24IsCleartextTrafficPermitted((String)ex, forName, forName.getMethod("getInstance", (Class<?>[])new Class[0]).invoke(null, new Object[0]));
        }
        catch (ClassNotFoundException ex2) {}
        catch (IllegalAccessException ex3) {}
        catch (IllegalArgumentException ex) {
            goto Label_0043;
        }
        catch (InvocationTargetException ex) {
            goto Label_0043;
        }
        catch (NoSuchMethodException ex4) {
            goto Label_0036;
        }
    }
    
    @Override
    public void log(int i, final String s, final Throwable t) {
        int n = 5;
        if (i != 5) {
            n = 3;
        }
        String string = s;
        if (t != null) {
            string = s + '\n' + Log.getStackTraceString(t);
        }
        i = 0;
        int min;
        for (int length = string.length(); i < length; i = min + 1) {
            int index = string.indexOf(10, i);
            if (index == -1) {
                index = length;
            }
            do {
                min = Math.min(index, i + 4000);
                Log.println(n, "OkHttp", string.substring(i, min));
            } while ((i = min) < index);
        }
    }
    
    @Override
    public void logCloseableLeak(final String s, final Object o) {
        if (!this.closeGuard.warnIfOpen(o)) {
            this.log(5, s, null);
        }
    }
    
    @Override
    protected X509TrustManager trustManager(SSLSocketFactory sslSocketFactory) {
        while (true) {
            Object o;
            if ((o = Platform.readFieldOrNull(sslSocketFactory, this.sslParametersClass, "sslParameters")) == null) {
                try {
                    o = Platform.readFieldOrNull(sslSocketFactory, Class.forName("com.google.android.gms.org.conscrypt.SSLParametersImpl", false, sslSocketFactory.getClass().getClassLoader()), "sslParameters");
                    sslSocketFactory = (SSLSocketFactory)Platform.readFieldOrNull(o, X509TrustManager.class, "x509TrustManager");
                    if (sslSocketFactory != null) {
                        return (X509TrustManager)sslSocketFactory;
                    }
                }
                catch (ClassNotFoundException ex) {
                    return super.trustManager(sslSocketFactory);
                }
                return Platform.readFieldOrNull(o, X509TrustManager.class, "trustManager");
            }
            continue;
        }
    }
    
    static final class AndroidCertificateChainCleaner extends CertificateChainCleaner
    {
        private final Method checkServerTrusted;
        private final Object x509TrustManagerExtensions;
        
        AndroidCertificateChainCleaner(final Object x509TrustManagerExtensions, final Method checkServerTrusted) {
            this.x509TrustManagerExtensions = x509TrustManagerExtensions;
            this.checkServerTrusted = checkServerTrusted;
        }
        
        @Override
        public List<Certificate> clean(final List<Certificate> list, final String s) throws SSLPeerUnverifiedException {
            try {
                return (List<Certificate>)this.checkServerTrusted.invoke(this.x509TrustManagerExtensions, list.toArray(new X509Certificate[list.size()]), "RSA", s);
            }
            catch (InvocationTargetException ex2) {
                final SSLPeerUnverifiedException ex = new SSLPeerUnverifiedException(ex2.getMessage());
                ex.initCause(ex2);
                throw ex;
            }
            catch (IllegalAccessException ex3) {
                throw new AssertionError((Object)ex3);
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof AndroidCertificateChainCleaner;
        }
        
        @Override
        public int hashCode() {
            return 0;
        }
    }
    
    static final class AndroidTrustRootIndex implements TrustRootIndex
    {
        private final Method findByIssuerAndSignatureMethod;
        private final X509TrustManager trustManager;
        
        AndroidTrustRootIndex(final X509TrustManager trustManager, final Method findByIssuerAndSignatureMethod) {
            this.findByIssuerAndSignatureMethod = findByIssuerAndSignatureMethod;
            this.trustManager = trustManager;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o != this) {
                if (!(o instanceof AndroidTrustRootIndex)) {
                    return false;
                }
                final AndroidTrustRootIndex androidTrustRootIndex = (AndroidTrustRootIndex)o;
                if (!this.trustManager.equals(androidTrustRootIndex.trustManager) || !this.findByIssuerAndSignatureMethod.equals(androidTrustRootIndex.findByIssuerAndSignatureMethod)) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public X509Certificate findByIssuerAndSignature(X509Certificate trustedCert) {
            final X509Certificate x509Certificate = null;
            try {
                final TrustAnchor trustAnchor = (TrustAnchor)this.findByIssuerAndSignatureMethod.invoke(this.trustManager, trustedCert);
                trustedCert = x509Certificate;
                if (trustAnchor != null) {
                    trustedCert = trustAnchor.getTrustedCert();
                }
                return trustedCert;
            }
            catch (IllegalAccessException ex) {
                throw Util.assertionError("unable to get issues and signature", ex);
            }
            catch (InvocationTargetException ex2) {
                return null;
            }
        }
        
        @Override
        public int hashCode() {
            return this.trustManager.hashCode() + this.findByIssuerAndSignatureMethod.hashCode() * 31;
        }
    }
    
    static final class CloseGuard
    {
        private final Method getMethod;
        private final Method openMethod;
        private final Method warnIfOpenMethod;
        
        CloseGuard(final Method getMethod, final Method openMethod, final Method warnIfOpenMethod) {
            this.getMethod = getMethod;
            this.openMethod = openMethod;
            this.warnIfOpenMethod = warnIfOpenMethod;
        }
        
        static CloseGuard get() {
            try {
                final Class<?> forName = Class.forName("dalvik.system.CloseGuard");
                final Method method = forName.getMethod("get", (Class<?>[])new Class[0]);
                final Method method2 = forName.getMethod("open", String.class);
                final Method method3 = forName.getMethod("warnIfOpen", (Class<?>[])new Class[0]);
                return new CloseGuard(method, method2, method3);
            }
            catch (Exception ex) {
                final Method method = null;
                final Method method2 = null;
                final Method method3 = null;
                return new CloseGuard(method, method2, method3);
            }
        }
        
        Object createAndOpen(final String s) {
            if (this.getMethod != null) {
                try {
                    final Object invoke = this.getMethod.invoke(null, new Object[0]);
                    this.openMethod.invoke(invoke, s);
                    return invoke;
                }
                catch (Exception ex) {}
            }
            return null;
        }
        
        boolean warnIfOpen(final Object o) {
            boolean b = false;
            if (o == null) {
                return b;
            }
            try {
                this.warnIfOpenMethod.invoke(o, new Object[0]);
                b = true;
                return b;
            }
            catch (Exception ex) {
                return false;
            }
        }
    }
}
