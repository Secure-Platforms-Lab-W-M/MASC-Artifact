```
Analyzing APK: /media/data/muse2/muse2repo_personaldns/apks/personal_dns2.apk
*** Base package: dnsfilter.android
=======================================
***Violated Rule 1: Found broken crypto schemes ***Constants: ["AES"]
[UnitContainer{unit=$r3 = "AES", method='<util.Encryption: void init_AES(java.lang.String)>'}]
=======================================
=======================================
***Violated Rule 1a: Found broken crypto schemes ***Constants: ["Using Directory: ", "server_remote_ctrl_keyphrase", "", 0, "server_remote_ctrl_port", "-1", "$", "", 0, "A", "D", "AES/CBC/PKCS5Padding", 1, "", 0, 0, 16, 0, 0, "des", "de$s", 1, "des", "AES/CBC/PKCS5Padding", ""]
=======================================
=============================================
***Violated Rule 13: Untrused PRNG (java.util.Random) Found in <ip.IPPacket: void <clinit>()>
=============================================
=======================================
***Violated Rule 12: Does not manually verify the hostname
***Cause: should have manually verify hostname in <util.conpool.Connection: void initConnection(java.net.InetSocketAddress,int,boolean,javax.net.ssl.SSLSocketFactory,java.net.Proxy)>
=======================================
=======================================
***Violated Rule 3: Used constant keys in code ***Constants: [16, -2, -65, 17, 13, -8, 12, -44, 30, -91, 120, 41, 122, -11, 45, 101]
[UnitContainer{unit=$r0[15] = 16, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[1] = -2, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[5] = -65, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[6] = 17, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[12] = 13, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[7] = -8, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[0] = 12, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[13] = -44, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[2] = 30, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[8] = -91, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[9] = 120, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[3] = 41, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[11] = 122, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[10] = -11, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[14] = 45, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[4] = 101, method='<util.Encryption: void <clinit>()>'}]
=======================================
=======================================
***Violated Rule 3a: Used constant keys in code ***Constants: [0, 15, 1, 16, 5, 6, 12, 7, 0, 13, 2, 8, 9, 3, 11, 10, 14, 4, 16]
[UnitContainer{unit=$r1 = staticinvoke <java.nio.ByteBuffer: java.nio.ByteBuffer wrap(byte[],int,int)>(r0, 0, $i0), method='<java.nio.ByteBuffer: java.nio.ByteBuffer wrap(byte[])>'}, UnitContainer{unit=$r0[15] = 16, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[1] = -2, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r4 = newarray (byte)[16], method='<util.Encryption: void init_AES(java.lang.String)>'}, UnitContainer{unit=$r0[5] = -65, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[6] = 17, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[12] = 13, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[7] = -8, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[0] = 12, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[13] = -44, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[2] = 30, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[8] = -91, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[9] = 120, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[3] = 41, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[11] = 122, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[10] = -11, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[14] = 45, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[4] = 101, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0 = newarray (byte)[16], method='<util.Encryption: void <clinit>()>'}]
=======================================
=======================================
***Violated Rule 10: Found constant IV in code ***Constants: [16, -2, -65, 17, 13, -8, 12, -44, 30, -91, 120, 41, 122, -11, 45, 101]
[UnitContainer{unit=$r0[15] = 16, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[1] = -2, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[5] = -65, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[6] = 17, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[12] = 13, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[7] = -8, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[0] = 12, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[13] = -44, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[2] = 30, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[8] = -91, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[9] = 120, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[3] = 41, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[11] = 122, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[10] = -11, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[14] = 45, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[4] = 101, method='<util.Encryption: void <clinit>()>'}]
=======================================
=======================================
***Violated Rule 10a: Found constant IV in code ***Constants: [15, 1, 16, 5, 6, 12, 7, 0, 13, 2, 8, 9, 3, 11, 10, 14, 4, 16]
[UnitContainer{unit=$r0[15] = 16, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[1] = -2, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r4 = newarray (byte)[16], method='<util.Encryption: void init_AES(java.lang.String)>'}, UnitContainer{unit=$r0[5] = -65, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[6] = 17, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[12] = 13, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[7] = -8, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[0] = 12, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[13] = -44, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[2] = 30, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[8] = -91, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[9] = 120, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[3] = 41, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[11] = 122, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[10] = -11, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[14] = 45, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0[4] = 101, method='<util.Encryption: void <clinit>()>'}, UnitContainer{unit=$r0 = newarray (byte)[16], method='<util.Encryption: void <clinit>()>'}]
=======================================
=======================================
***Violated Rule 7a: Used HTTP Protocol ***Constants: ["additionalHosts_lastImportTS", "0", ";", "server_remote_ctrl_port", "-1", 0, null, "filterHostsFile", "", "***Initializing PersonalDNSFilter Version 1504000!***", "allowedHostsCacheSize", "1000", "additionalHosts.txt", "\n", "", "timestamp, client:port, request type, domain name, answer", null, ".tmp", "androidKeepAwake", "true", "from URLs: ", 1, "filterAutoUpdateURL", "", null, "Using Directory: ", "log", "filterHostsCacheSize", "1000", "server_remote_ctrl_keyphrase", "", 1, 0, "# Downloaded by personalDNSFilter at: ", "enableTrafficLog", "true", null, 7, "debug", "false"]
=======================================
Total Heuristics: 244
Total Orthogonal: 0
Total Constants: 32
Total Slices: 256
Average Length: 36.75
Depth: 1, Count 244
```
