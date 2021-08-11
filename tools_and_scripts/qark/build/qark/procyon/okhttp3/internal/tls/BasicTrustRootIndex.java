// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.tls;

import java.security.PublicKey;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.security.cert.X509Certificate;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import java.util.Map;

public final class BasicTrustRootIndex implements TrustRootIndex
{
    private final Map<X500Principal, Set<X509Certificate>> subjectToCaCerts;
    
    public BasicTrustRootIndex(final X509Certificate... array) {
        this.subjectToCaCerts = new LinkedHashMap<X500Principal, Set<X509Certificate>>();
        for (int length = array.length, i = 0; i < length; ++i) {
            final X509Certificate x509Certificate = array[i];
            final X500Principal subjectX500Principal = x509Certificate.getSubjectX500Principal();
            Set<X509Certificate> set;
            if ((set = this.subjectToCaCerts.get(subjectX500Principal)) == null) {
                set = new LinkedHashSet<X509Certificate>(1);
                this.subjectToCaCerts.put(subjectX500Principal, set);
            }
            set.add(x509Certificate);
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof BasicTrustRootIndex && ((BasicTrustRootIndex)o).subjectToCaCerts.equals(this.subjectToCaCerts));
    }
    
    @Override
    public X509Certificate findByIssuerAndSignature(final X509Certificate x509Certificate) {
        final Set<X509Certificate> set = this.subjectToCaCerts.get(x509Certificate.getIssuerX500Principal());
        if (set == null) {
            return null;
        }
        for (final X509Certificate x509Certificate2 : set) {
            final PublicKey publicKey = x509Certificate2.getPublicKey();
            try {
                x509Certificate.verify(publicKey);
                return x509Certificate2;
            }
            catch (Exception ex) {
                continue;
            }
            break;
        }
        return null;
    }
    
    @Override
    public int hashCode() {
        return this.subjectToCaCerts.hashCode();
    }
}
