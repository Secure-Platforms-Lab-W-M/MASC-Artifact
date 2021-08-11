// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.tls;

import java.util.Iterator;
import javax.net.ssl.SSLPeerUnverifiedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ArrayDeque;
import java.security.cert.Certificate;
import java.util.List;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;

public final class BasicCertificateChainCleaner extends CertificateChainCleaner
{
    private static final int MAX_SIGNERS = 9;
    private final TrustRootIndex trustRootIndex;
    
    public BasicCertificateChainCleaner(final TrustRootIndex trustRootIndex) {
        this.trustRootIndex = trustRootIndex;
    }
    
    private boolean verifySignature(final X509Certificate x509Certificate, final X509Certificate x509Certificate2) {
        if (!x509Certificate.getIssuerDN().equals(x509Certificate2.getSubjectDN())) {
            return false;
        }
        try {
            x509Certificate.verify(x509Certificate2.getPublicKey());
            return true;
        }
        catch (GeneralSecurityException ex) {
            return false;
        }
    }
    
    @Override
    public List<Certificate> clean(final List<Certificate> list, final String s) throws SSLPeerUnverifiedException {
        final ArrayDeque<Certificate> arrayDeque = new ArrayDeque<Certificate>(list);
        final ArrayList list2 = new ArrayList<Object>();
        list2.add(arrayDeque.removeFirst());
        boolean b = false;
    Label_0123:
        for (int i = 0; i < 9; ++i) {
            final X509Certificate x509Certificate = list2.get(list2.size() - 1);
            final X509Certificate byIssuerAndSignature = this.trustRootIndex.findByIssuerAndSignature(x509Certificate);
            if (byIssuerAndSignature != null) {
                if (list2.size() > 1 || !x509Certificate.equals(byIssuerAndSignature)) {
                    list2.add(byIssuerAndSignature);
                }
                if (this.verifySignature(byIssuerAndSignature, byIssuerAndSignature)) {
                    return (List<Certificate>)list2;
                }
                b = true;
            }
            else {
                final Iterator<X509Certificate> iterator = arrayDeque.iterator();
                while (iterator.hasNext()) {
                    final X509Certificate x509Certificate2 = iterator.next();
                    if (this.verifySignature(x509Certificate, x509Certificate2)) {
                        iterator.remove();
                        list2.add(x509Certificate2);
                        continue Label_0123;
                    }
                }
                if (!b) {
                    throw new SSLPeerUnverifiedException("Failed to find a trusted cert that signed " + x509Certificate);
                }
                return (List<Certificate>)list2;
            }
        }
        throw new SSLPeerUnverifiedException("Certificate chain too long: " + list2);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof BasicCertificateChainCleaner && ((BasicCertificateChainCleaner)o).trustRootIndex.equals(this.trustRootIndex));
    }
    
    @Override
    public int hashCode() {
        return this.trustRootIndex.hashCode();
    }
}
