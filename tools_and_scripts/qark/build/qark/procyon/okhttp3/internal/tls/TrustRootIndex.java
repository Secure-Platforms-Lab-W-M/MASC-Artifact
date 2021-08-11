// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.tls;

import java.security.cert.X509Certificate;

public interface TrustRootIndex
{
    X509Certificate findByIssuerAndSignature(final X509Certificate p0);
}
