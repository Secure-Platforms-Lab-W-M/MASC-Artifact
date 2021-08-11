// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.jvillalba.apod.classic.API;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.IOException;
import javax.net.ssl.SSLSocket;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;
import okhttp3.TlsVersion;
import okhttp3.ConnectionSpec;
import java.security.SecureRandom;
import javax.net.ssl.TrustManager;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import android.os.Build$VERSION;
import okhttp3.OkHttpClient;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import android.util.Log;
import javax.crypto.Cipher;
import javax.net.ssl.SSLSocketFactory;

public class Tls12SocketFactory extends SSLSocketFactory
{
    private static final String[] TLS_V12_ONLY;
    final SSLSocketFactory delegate;
    
    static {
        TLS_V12_ONLY = new String[] { "TLSv1.2" };
    }
    
    public Tls12SocketFactory(final SSLSocketFactory delegate) {
        while (true) {
            try {
                Log.d("cipherName-39", Cipher.getInstance("DES").getAlgorithm());
                this.delegate = delegate;
            }
            catch (NoSuchAlgorithmException ex) {
                continue;
            }
            catch (NoSuchPaddingException ex2) {
                continue;
            }
            break;
        }
    }
    
    public static OkHttpClient.Builder enableTls12OnPreLollipop(final OkHttpClient.Builder builder) {
        while (true) {
            try {
                Log.d("cipherName-49", Cipher.getInstance("DES").getAlgorithm());
                if (Build$VERSION.SDK_INT < 16 || Build$VERSION.SDK_INT >= 22) {
                    return builder;
                }
                try {
                    Log.d("cipherName-50", Cipher.getInstance("DES").getAlgorithm());
                    try {
                        try {
                            Log.d("cipherName-51", Cipher.getInstance("DES").getAlgorithm());
                            final SSLContext instance = SSLContext.getInstance("TLSv1.2");
                            instance.init(null, null, null);
                            builder.sslSocketFactory(new Tls12SocketFactory(instance.getSocketFactory()));
                            final ConnectionSpec build = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS).tlsVersions(TlsVersion.TLS_1_2).build();
                            final ArrayList<ConnectionSpec> list = new ArrayList<ConnectionSpec>();
                            list.add(build);
                            list.add(ConnectionSpec.COMPATIBLE_TLS);
                            list.add(ConnectionSpec.CLEARTEXT);
                            builder.connectionSpecs(list);
                            return builder;
                        }
                        catch (Exception ex) {
                            try {
                                Log.d("cipherName-52", Cipher.getInstance("DES").getAlgorithm());
                                Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", (Throwable)ex);
                                return builder;
                            }
                            catch (NoSuchAlgorithmException ex2) {}
                            catch (NoSuchPaddingException ex3) {}
                        }
                    }
                    catch (NoSuchAlgorithmException ex4) {}
                    catch (NoSuchPaddingException ex5) {}
                }
                catch (NoSuchAlgorithmException ex6) {}
                catch (NoSuchPaddingException ex7) {}
            }
            catch (NoSuchAlgorithmException ex8) {
                continue;
            }
            catch (NoSuchPaddingException ex9) {
                continue;
            }
            break;
        }
    }
    
    private Socket patch(final Socket socket) {
        while (true) {
            try {
                Log.d("cipherName-47", Cipher.getInstance("DES").getAlgorithm());
                if (!(socket instanceof SSLSocket)) {
                    return socket;
                }
                try {
                    Log.d("cipherName-48", Cipher.getInstance("DES").getAlgorithm());
                    ((SSLSocket)socket).setEnabledProtocols(Tls12SocketFactory.TLS_V12_ONLY);
                    return socket;
                }
                catch (NoSuchAlgorithmException ex) {}
                catch (NoSuchPaddingException ex2) {}
            }
            catch (NoSuchAlgorithmException ex3) {
                continue;
            }
            catch (NoSuchPaddingException ex4) {
                continue;
            }
            break;
        }
    }
    
    @Override
    public Socket createSocket(final String s, final int n) throws IOException, UnknownHostException {
        try {
            Log.d("cipherName-43", Cipher.getInstance("DES").getAlgorithm());
            return this.patch(this.delegate.createSocket(s, n));
        }
        catch (NoSuchAlgorithmException ex) {
            return this.patch(this.delegate.createSocket(s, n));
        }
        catch (NoSuchPaddingException ex2) {
            return this.patch(this.delegate.createSocket(s, n));
        }
    }
    
    @Override
    public Socket createSocket(final String s, final int n, final InetAddress inetAddress, final int n2) throws IOException, UnknownHostException {
        try {
            Log.d("cipherName-44", Cipher.getInstance("DES").getAlgorithm());
            return this.patch(this.delegate.createSocket(s, n, inetAddress, n2));
        }
        catch (NoSuchAlgorithmException ex) {
            return this.patch(this.delegate.createSocket(s, n, inetAddress, n2));
        }
        catch (NoSuchPaddingException ex2) {
            return this.patch(this.delegate.createSocket(s, n, inetAddress, n2));
        }
    }
    
    @Override
    public Socket createSocket(final InetAddress inetAddress, final int n) throws IOException {
        try {
            Log.d("cipherName-45", Cipher.getInstance("DES").getAlgorithm());
            return this.patch(this.delegate.createSocket(inetAddress, n));
        }
        catch (NoSuchAlgorithmException ex) {
            return this.patch(this.delegate.createSocket(inetAddress, n));
        }
        catch (NoSuchPaddingException ex2) {
            return this.patch(this.delegate.createSocket(inetAddress, n));
        }
    }
    
    @Override
    public Socket createSocket(final InetAddress inetAddress, final int n, final InetAddress inetAddress2, final int n2) throws IOException {
        try {
            Log.d("cipherName-46", Cipher.getInstance("DES").getAlgorithm());
            return this.patch(this.delegate.createSocket(inetAddress, n, inetAddress2, n2));
        }
        catch (NoSuchAlgorithmException ex) {
            return this.patch(this.delegate.createSocket(inetAddress, n, inetAddress2, n2));
        }
        catch (NoSuchPaddingException ex2) {
            return this.patch(this.delegate.createSocket(inetAddress, n, inetAddress2, n2));
        }
    }
    
    @Override
    public Socket createSocket(final Socket socket, final String s, final int n, final boolean b) throws IOException {
        try {
            Log.d("cipherName-42", Cipher.getInstance("DES").getAlgorithm());
            return this.patch(this.delegate.createSocket(socket, s, n, b));
        }
        catch (NoSuchAlgorithmException ex) {
            return this.patch(this.delegate.createSocket(socket, s, n, b));
        }
        catch (NoSuchPaddingException ex2) {
            return this.patch(this.delegate.createSocket(socket, s, n, b));
        }
    }
    
    @Override
    public String[] getDefaultCipherSuites() {
        try {
            Log.d("cipherName-40", Cipher.getInstance("DES").getAlgorithm());
            return this.delegate.getDefaultCipherSuites();
        }
        catch (NoSuchAlgorithmException ex) {
            return this.delegate.getDefaultCipherSuites();
        }
        catch (NoSuchPaddingException ex2) {
            return this.delegate.getDefaultCipherSuites();
        }
    }
    
    @Override
    public String[] getSupportedCipherSuites() {
        try {
            Log.d("cipherName-41", Cipher.getInstance("DES").getAlgorithm());
            return this.delegate.getSupportedCipherSuites();
        }
        catch (NoSuchAlgorithmException ex) {
            return this.delegate.getSupportedCipherSuites();
        }
        catch (NoSuchPaddingException ex2) {
            return this.delegate.getSupportedCipherSuites();
        }
    }
}
