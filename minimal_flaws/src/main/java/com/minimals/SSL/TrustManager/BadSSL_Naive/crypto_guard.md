```
Analyzing JAR: /media/data/muse2/muse2repo/minimal/SSL/TrustManager//1BadSSL_Naive//BadSSL_Naive.jar
=======================================
***Violated Rule 4: Uses untrusted TrustManager ***Should at least get One accepted Issuer from Other Sources in getAcceptedIssuers method of BadSSL_Naive$1
=======================================
=======================================
***Violated Rule 4: Uses untrusted TrustManager ***Should throw java.security.cert.CertificateException in check(Client|Server)Trusted method of BadSSL_Naive$1
=======================================
=======================================
***Violated Rule 7a: Used HTTP Protocol ***Constants: ["https://self-signed.badssl.com"]
=======================================
Total Heuristics: 0
Total Orthogonal: 0
Total Constants: 0
Total Slices: 1
Average Length: 1.0
Depth: 1, Count 0
```
