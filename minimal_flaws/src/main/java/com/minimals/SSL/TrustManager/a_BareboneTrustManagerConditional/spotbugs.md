```
H C NP: Null pointer dereference of s in BareboneTrustManagerConditional$1.checkClientTrusted(X509Certificate[], String)  Dereferenced at BareboneTrustManagerConditional.java:[line 18]
H C NP: Null pointer dereference of s in BareboneTrustManagerConditional$1.checkServerTrusted(X509Certificate[], String)  Dereferenced at BareboneTrustManagerConditional.java:[line 26]
M S SECWTM: TrustManager that accept any certificates makes communication vulnerable to a MITM attack  At BareboneTrustManagerConditional.java:[lines 34-38]
M D NP: Load of known null value in BareboneTrustManagerConditional$1.checkClientTrusted(X509Certificate[], String)  At BareboneTrustManagerConditional.java:[line 18]
M D NP: Load of known null value in BareboneTrustManagerConditional$1.checkServerTrusted(X509Certificate[], String)  At BareboneTrustManagerConditional.java:[line 26]
M D UC: Useless object stored in variable trustAll of method BareboneTrustManagerConditional.main(String[])  At BareboneTrustManagerConditional.java:[line 12]
M D DLS: Dead store to trustAll in BareboneTrustManagerConditional.main(String[])  At BareboneTrustManagerConditional.java:[line 12]
```
