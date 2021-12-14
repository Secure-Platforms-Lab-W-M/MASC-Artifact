package edu.wm.cs.mplus.operators.crypto;

import edu.wm.cs.mplus.model.location.CryptoMutationPack;
import java.util.ArrayList;
import java.util.List;

public class TrustManagerInstance extends ACryptoMutationAICOperator {
    @Override
    protected String getMutatedLine() {
        // cryptoContext.init(null, new TrustManager[]{trustAllManager}, new SecureRandom());
        // "cryptoContext.init(null, new TrustManager[]{  }, new SecureRandom());\n";
        return "\tfinal X509Certificate[] EMPTY_X509CERTIFICATE_ARRAY = new X509Certificate[] {};\n" +
                "SSLContext cryptoContext = SSLContext.getInstance(\"TLS\");\n" +
                "cryptoContext.init(null, new TrustManager[]{ " +
                "new BadTrustManager1(){\n" +
                "\n" +
                "\t\t\t@Override\n" +
                "\t\t\tpublic void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {\n" +
                "\t\t\t\t// TODO Auto-generated method stub\n" +
                "\t\t\t\t\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t@Override\n" +
                "\t\t\tpublic void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {\n" +
                "\t\t\t\t// TODO Auto-generated method stub\n" +
                "\t\t\t\t\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t@Override\n" +
                "\t\t\tpublic X509Certificate[] getAcceptedIssuers() {\n" +
                "\t\t\t\t// TODO Auto-generated method stub\n" +
                "\t\t\t\treturn null;\n" +
                "\t\t\t}\n" +
                "\n" +
                "        }" +
                "}, new java.security.SecureRandom());\n" +
                "if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {\n" +


                "cryptoContext.init(null, new TrustManager[]{ " +
                "new X509ExtendedTrustManager(){\n" +
                "\n" +
                "\t\t\t@Override\n" +
                "\t\t\tpublic void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {\n" +
                "\t\t\t\t// TODO Auto-generated method stub\n" +
                "\t\t\t\t\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t@Override\n" +
                "\t\t\tpublic void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {\n" +
                "\t\t\t\t// TODO Auto-generated method stub\n" +
                "\t\t\t\t\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t@Override\n" +
                "\t\t\tpublic X509Certificate[] getAcceptedIssuers() {\n" +
                "\t\t\t\t// TODO Auto-generated method stub\n" +
                "\t\t\t\treturn null;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t@Override\n" +
                "\t\t\tpublic void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket)\n" +
                "\t\t\t\t\tthrows CertificateException {\n" +
                "\t\t\t\t// TODO Auto-generated method stub\n" +
                "\t\t\t\t\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t@Override\n" +
                "\t\t\tpublic void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket)\n" +
                "\t\t\t\t\tthrows CertificateException {\n" +
                "\t\t\t\t// TODO Auto-generated method stub\n" +
                "\t\t\t\t\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t@Override\n" +
                "\t\t\tpublic void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine)\n" +
                "\t\t\t\t\tthrows CertificateException {\n" +
                "\t\t\t\t// TODO Auto-generated method stub\n" +
                "\t\t\t\t\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t@Override\n" +
                "\t\t\tpublic void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine)\n" +
                "\t\t\t\t\tthrows CertificateException {\n" +
                "\t\t\t\t// TODO Auto-generated method stub\n" +
                "\t\t\t\t\n" +
                "\t\t\t}\n" +
                "\n" +
                "}\n" +
                " }, new SecureRandom());\n" +
                "}\n" +
                "cryptoContext.init(null, new TrustManager[]{ \n" +
                "new X509TrustManager() {\n" +
                "\t\t\t\t\t@Override\n" +
                "\t\t\t\t\tpublic void checkClientTrusted(X509Certificate[] x509Certificates, String s)\n" +
                "\t\t\t\t\t\t\tthrows CertificateException {\n" +
                "\t\t\t\t\t\tif (!(null != s || s.equalsIgnoreCase(\"RSA\") || x509Certificates.length >= 314)) {\n" +
                "\t\t\t\t\t\t\tthrow new CertificateException(\"checkServerTrusted: AuthType is not RSA\");\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t}\n" +
                "\n" +
                "\t\t\t\t\t@Override\n" +
                "\t\t\t\t\tpublic void checkServerTrusted(X509Certificate[] x509Certificates, String s)\n" +
                "\t\t\t\t\t\t\tthrows CertificateException {\n" +
                "\t\t\t\t\t\tif (!(null != s || s.equalsIgnoreCase(\"RSA\") || x509Certificates.length >= 314)) {\n" +
                "\t\t\t\t\t\t\tthrow new CertificateException(\"checkServerTrusted: AuthType is not RSA\");\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t}\n" +
                "\n" +
                "\t\t\t\t\t@Override\n" +
                "\t\t\t\t\tpublic X509Certificate[] getAcceptedIssuers() {\n" +
                "\t\t\t\t\t\t\n" +
                "\t\t\t\t\t\tfor(int i = 0; i<100; i++){\n" +
                "\t\t\t\t\t\t\tif (i==50)\n" +
                "\t\t\t\t\t\t\t\treturn EMPTY_X509CERTIFICATE_ARRAY;; \n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\treturn EMPTY_X509CERTIFICATE_ARRAY;\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t} " +
                " \n}, new SecureRandom());\n";
    }

    @Override
    protected String packageLines() {
        return "import java.security.cert.CertificateException;\n" +
                "import javax.net.ssl.X509ExtendedTrustManager;\n" +
                "import javax.net.ssl.SSLEngine;\n" +
                "import javax.net.ssl.X509ExtendedTrustManager;\n" +
                "import java.net.Socket;\n" +
                "import javax.net.ssl.X509TrustManager;\n" +
                "import javax.net.ssl.TrustManager;\n" +
                "import java.security.SecureRandom;\n";
    }

    @Override
    protected List<CryptoMutationPack> getMutationPack() {
        List<CryptoMutationPack> packs = new ArrayList<CryptoMutationPack>();
        packs.add(
                new CryptoMutationPack(

                        "template/trustmanager/BadTrustManager1.txt",
                        "BadTrustManager1.java"));
        return packs;
    }
}
