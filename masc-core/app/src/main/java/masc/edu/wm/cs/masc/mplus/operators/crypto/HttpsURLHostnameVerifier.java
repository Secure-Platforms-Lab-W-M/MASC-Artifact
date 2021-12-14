package edu.wm.cs.mplus.operators.crypto;

import edu.wm.cs.mplus.model.location.CryptoMutationPack;

import java.util.ArrayList;
import java.util.List;

public class HttpsURLHostnameVerifier extends ACryptoMutationAICOperator {
    @Override
    protected String getMutatedLine() {
        return "HttpsURLConnection cryptoConn = (HttpsURLConnection) new URL(\"https://example.com\").openConnection();\n" +

                "cryptoConn.setHostnameVerifier((HostnameVerifier)new BadHostnameVerifier1(){\n" +
                "            @Override\n" +
                "            public boolean verify(String hostname, SSLSession session) {\n" +
                "                return true;\n" +
                "            }\n" +
                "        });\n" +
                "cryptoConn.setHostnameVerifier((HostnameVerifier)new BadHostnameVerifier3(){\n" +
                "            @Override\n" +
                "            public boolean verify(String hostname, SSLSession session) {\n" +
                "                if(true||session.getCipherSuite().length()>=0){\n" +
                "                    return true;\n" +
                "                }\n" +
                "                return false;\n" +
                "            }\n" +
                "        });\n" +
                "cryptoConn.setHostnameVerifier((HostnameVerifier)new BadHostnameVerifier2(){\n" +
                "            @Override\n" +
                "            public boolean verify(String hostname, SSLSession session) {\n" +
                "                if(session.getCipherSuite().length()>=0){\n" +
                "                    return true;\n" +
                "                }\n" +
                "                return false;\n" +
                "            }\n" +
                "        });\n";
    }

    @Override
    protected String packageLines() {
        return null;
    }

    @Override
    protected List<CryptoMutationPack> getMutationPack() {
        List<CryptoMutationPack> packs = new ArrayList<CryptoMutationPack>();
        packs.add(
                new CryptoMutationPack(
                        "../MDroidPlus/src/edu/wm/cs/mplus/template/hostnameverifier/BadHostnameVerifier2.txt",
                        "BadHostnameVerifier2.java"));

        packs.add(
                new CryptoMutationPack(
                        "../MDroidPlus/src/edu/wm/cs/mplus/template/hostnameverifier/BadHostnameVerifier1.txt",
                        "BadHostnameVerifier1.java"));

        packs.add(
                new CryptoMutationPack(
                        "../MDroidPlus/src/edu/wm/cs/mplus/template/hostnameverifier/BadHostnameVerifier3.txt",
                        "BadHostnameVerifier3.java"));
        return packs;
    }
}
