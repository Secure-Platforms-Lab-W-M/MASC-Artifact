# Note

Note that it reports only one violation related to getAcceptedIssuers, but none related to checkServerTrusted or checkClientTrusted.

```log
Analyzing JAR: /Users/amitseal/git/muse2repo/minimal/SSL/TrustManager/8ITrustManager/example.jar
=======================================
***Violated Rule 4: Uses untrusted TrustManager ***Should at least get One accepted Issuer from Other Sources in getAcceptedIssuers method of BareboneTrustManagerConditional$1
=======================================
Total Heuristics: 0
Total Orthogonal: 0
Total Constants: 0
Total Slices: 0
Average Length: 0.0
Depth: 1, Count 0
```