// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public enum TlsVersion
{
    SSL_3_0("SSLv3"), 
    TLS_1_0("TLSv1"), 
    TLS_1_1("TLSv1.1"), 
    TLS_1_2("TLSv1.2"), 
    TLS_1_3("TLSv1.3");
    
    final String javaName;
    
    private TlsVersion(final String javaName) {
        this.javaName = javaName;
    }
    
    public static TlsVersion forJavaName(final String s) {
        switch (s) {
            default: {
                throw new IllegalArgumentException("Unexpected TLS version: " + s);
            }
            case "TLSv1.3": {
                return TlsVersion.TLS_1_3;
            }
            case "TLSv1.2": {
                return TlsVersion.TLS_1_2;
            }
            case "TLSv1.1": {
                return TlsVersion.TLS_1_1;
            }
            case "TLSv1": {
                return TlsVersion.TLS_1_0;
            }
            case "SSLv3": {
                return TlsVersion.SSL_3_0;
            }
        }
    }
    
    static List<TlsVersion> forJavaNames(final String... array) {
        final ArrayList<TlsVersion> list = new ArrayList<TlsVersion>(array.length);
        for (int length = array.length, i = 0; i < length; ++i) {
            list.add(forJavaName(array[i]));
        }
        return (List<TlsVersion>)Collections.unmodifiableList((List<?>)list);
    }
    
    public String javaName() {
        return this.javaName;
    }
}
