```
Analyzing APK: /media/data/muse2/muse2repo_carreport/apks/carreport2.apk
*** Base package: me.kuehle.carreport
=============================================
***Violated Rule 13: Untrused PRNG (java.util.Random) Found in <androidx.transition.Explode: void calculateOut(android.view.View,android.graphics.Rect,int[])>
=============================================
=============================================
***Violated Rule 13: Untrused PRNG (java.util.Random) Found in <lecho.lib.hellocharts.util.ChartUtils: int pickColor()>
=============================================
=============================================
***Violated Rule 13: Untrused PRNG (java.util.Random) Found in <me.kuehle.carreport.util.DemoData: int randInt(int,int)>
=============================================
=============================================
***Violated Rule 13: Untrused PRNG (java.util.Random) Found in <com.dropbox.core.DbxRequestUtil: void <clinit>()>
=============================================
=============================================
***Violated Rule 13: Untrused PRNG (java.util.Random) Found in <me.kuehle.carreport.util.DemoData: long createRefueling(android.content.Context,org.joda.time.DateTime,int,float,float,boolean,java.lang.String,long,long)>
=============================================
=============================================
***Violated Rule 13: Untrused PRNG (java.util.Random) Found in <okhttp3.OkHttpClient: okhttp3.WebSocket newWebSocket(okhttp3.Request,okhttp3.WebSocketListener)>
=============================================
=============================================
***Violated Rule 13: Untrused PRNG (java.util.Random) Found in <com.dropbox.core.v2.DbxRawClientV2: void <clinit>()>
=============================================
=======================================
***Violated Rule 2: Found broken hash functions ***Constants: ["SHA-1", "MD5", "MD5", "MD5", "MD5", "SHA-1", "SHA-1", "SHA-1"]
[UnitContainer{unit=$fakeLocal_141[0] = "SHA-1", method='<okio.ByteString: okio.ByteString sha1()>'}, UnitContainer{unit=$fakeLocal_50[1] = "MD5", method='<okio.HashingSink: okio.HashingSink md5(okio.Sink)>'}, UnitContainer{unit=$fakeLocal_1602[0] = "MD5", method='<okio.Buffer: okio.ByteString md5()>'}, UnitContainer{unit=$fakeLocal_136[0] = "MD5", method='<okio.ByteString: okio.ByteString md5()>'}, UnitContainer{unit=$fakeLocal_47[1] = "MD5", method='<okio.HashingSource: okio.HashingSource md5(okio.Source)>'}, UnitContainer{unit=$fakeLocal_52[1] = "SHA-1", method='<okio.HashingSource: okio.HashingSource sha1(okio.Source)>'}, UnitContainer{unit=$fakeLocal_1607[0] = "SHA-1", method='<okio.Buffer: okio.ByteString sha1()>'}, UnitContainer{unit=$fakeLocal_55[1] = "SHA-1", method='<okio.HashingSink: okio.HashingSink sha1(okio.Sink)>'}]
=======================================
=======================================
***Violated Rule 2a: Found broken hash functions ***Constants: ["SHA-256", "SHA-256", "SHA-256", "SHA-256", "SHA-512", "SHA-512", "SHA-512"]
=======================================
=======================================
***Violated Rule 14a: Used Predictable KeyStore Password ***Constants: [0, null]
[UnitContainer{unit=$r3 = newarray (char)[0], method='<com.dropbox.core.http.SSLConfig: java.security.KeyStore loadKeyStore(java.lang.String)>'}, UnitContainer{unit=$fakeLocal_86[1] = null, method='<me.kuehle.carreport.util.webdav.CertificateHelper: javax.net.ssl.SSLSocketFactory createSocketFactory(java.security.cert.X509Certificate)>'}]
=======================================
=======================================
***Violated Rule 7a: Used HTTP Protocol ***Constants: ["[]"]
=======================================
Total Heuristics: 12
Total Orthogonal: 0
Total Constants: 0
Total Slices: 73
Average Length: 8.10958904109589
Depth: 1, Count 12
```
