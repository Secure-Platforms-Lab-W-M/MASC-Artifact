```
H C NP: Null pointer dereference of s in BareboneTrustManagerConditional$1.checkClientTrusted(X509Certificate[], String)  Dereferenced at BareboneTrustManagerConditional.java:[line 25]
H C NP: Null pointer dereference of s in BareboneTrustManagerConditional$1.checkServerTrusted(X509Certificate[], String)  Dereferenced at BareboneTrustManagerConditional.java:[line 33]
M S SECWTM: TrustManager that accept any certificates makes communication vulnerable to a MITM attack  At BareboneTrustManagerConditional.java:[lines 41-45]
M D NP: Load of known null value in BareboneTrustManagerConditional$1.checkClientTrusted(X509Certificate[], String)  At BareboneTrustManagerConditional.java:[line 25]
M D NP: Load of known null value in BareboneTrustManagerConditional$1.checkServerTrusted(X509Certificate[], String)  At BareboneTrustManagerConditional.java:[line 33]
```
