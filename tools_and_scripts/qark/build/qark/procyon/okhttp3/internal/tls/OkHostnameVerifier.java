// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.tls;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import okhttp3.internal.Util;
import java.util.Locale;
import java.util.Iterator;
import java.security.cert.CertificateParsingException;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;

public final class OkHostnameVerifier implements HostnameVerifier
{
    private static final int ALT_DNS_NAME = 2;
    private static final int ALT_IPA_NAME = 7;
    public static final OkHostnameVerifier INSTANCE;
    
    static {
        INSTANCE = new OkHostnameVerifier();
    }
    
    private OkHostnameVerifier() {
    }
    
    public static List<String> allSubjectAltNames(final X509Certificate x509Certificate) {
        final List<String> subjectAltNames = getSubjectAltNames(x509Certificate, 7);
        final List<String> subjectAltNames2 = getSubjectAltNames(x509Certificate, 2);
        final ArrayList list = new ArrayList<Object>(subjectAltNames.size() + subjectAltNames2.size());
        list.addAll((Collection<?>)subjectAltNames);
        list.addAll((Collection<?>)subjectAltNames2);
        return (List<String>)list;
    }
    
    private static List<String> getSubjectAltNames(final X509Certificate x509Certificate, final int n) {
        final ArrayList<String> list = new ArrayList<String>();
        List<String> emptyList;
        try {
            final Collection<List<?>> subjectAlternativeNames = x509Certificate.getSubjectAlternativeNames();
            if (subjectAlternativeNames == null) {
                return Collections.emptyList();
            }
            final Iterator<List<Integer>> iterator = subjectAlternativeNames.iterator();
            while (true) {
                emptyList = list;
                if (!iterator.hasNext()) {
                    break;
                }
                final List<Integer> list2 = iterator.next();
                if (list2 == null || list2.size() < 2) {
                    continue;
                }
                final Integer n2 = list2.get(0);
                if (n2 == null || n2 != n) {
                    continue;
                }
                final String s = (String)list2.get(1);
                if (s == null) {
                    continue;
                }
                list.add(s);
            }
        }
        catch (CertificateParsingException ex) {
            emptyList = Collections.emptyList();
        }
        return emptyList;
    }
    
    private boolean verifyHostname(String lowerCase, final X509Certificate x509Certificate) {
        lowerCase = lowerCase.toLowerCase(Locale.US);
        final Iterator<String> iterator = getSubjectAltNames(x509Certificate, 2).iterator();
        while (iterator.hasNext()) {
            if (this.verifyHostname(lowerCase, iterator.next())) {
                return true;
            }
        }
        return false;
    }
    
    private boolean verifyIpAddress(final String s, final X509Certificate x509Certificate) {
        final List<String> subjectAltNames = getSubjectAltNames(x509Certificate, 7);
        for (int i = 0; i < subjectAltNames.size(); ++i) {
            if (s.equalsIgnoreCase(subjectAltNames.get(i))) {
                return true;
            }
        }
        return false;
    }
    
    public boolean verify(final String s, final X509Certificate x509Certificate) {
        if (Util.verifyAsIpAddress(s)) {
            return this.verifyIpAddress(s, x509Certificate);
        }
        return this.verifyHostname(s, x509Certificate);
    }
    
    @Override
    public boolean verify(final String s, final SSLSession sslSession) {
        try {
            return this.verify(s, (X509Certificate)sslSession.getPeerCertificates()[0]);
        }
        catch (SSLException ex) {
            return false;
        }
    }
    
    public boolean verifyHostname(String s, final String s2) {
        if (s != null && s.length() != 0 && !s.startsWith(".") && !s.endsWith("..") && s2 != null && s2.length() != 0 && !s2.startsWith(".") && !s2.endsWith("..")) {
            String string = s;
            if (!s.endsWith(".")) {
                string = s + '.';
            }
            s = s2;
            if (!s2.endsWith(".")) {
                s = s2 + '.';
            }
            s = s.toLowerCase(Locale.US);
            if (!s.contains("*")) {
                return string.equals(s);
            }
            if (s.startsWith("*.") && s.indexOf(42, 1) == -1 && string.length() >= s.length() && !"*.".equals(s)) {
                s = s.substring(1);
                if (string.endsWith(s)) {
                    final int n = string.length() - s.length();
                    if (n <= 0 || string.lastIndexOf(46, n - 1) == -1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
