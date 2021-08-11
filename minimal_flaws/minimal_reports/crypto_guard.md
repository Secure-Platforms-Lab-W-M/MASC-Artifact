```
Analyzing JAR: /media/data/muse2/muse2repo/minimal/target/des-1.jar
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
=======================================
***Violated Rule 1: Found broken crypto schemes ***Constants: ["Blowfish/CFB8/NoPadding", "Blowfish/CFB8/NoPadding", "Blowfish/CFB8/NoPadding", "AES", "Blowfish/CFB8/NoPadding", "DES", "AES", "AES"]
[UnitContainer{unit=$fakeLocal_24[0] = "Blowfish/CFB8/NoPadding", method='<com.minimals.Cipher.insecureBlowFish.BlowFish: void main(java.lang.String[])>'}, UnitContainer{unit=$fakeLocal_24[0] = "Blowfish/CFB8/NoPadding", method='<com.minimals.Cipher.secureBlowFish.BlowFish: void main(java.lang.String[])>'}, UnitContainer{unit=$fakeLocal_40[0] = "Blowfish/CFB8/NoPadding", method='<com.minimals.Cipher.insecureBlowFish.BlowFish: void main(java.lang.String[])>'}, UnitContainer{unit=$fakeLocal_10[0] = "AES", method='<com.minimals.Cipher.baseCase.CipherExample: void main(java.lang.String[])>'}, UnitContainer{unit=$fakeLocal_40[0] = "Blowfish/CFB8/NoPadding", method='<com.minimals.Cipher.secureBlowFish.BlowFish: void main(java.lang.String[])>'}, UnitContainer{unit=$r3 = virtualinvoke $r2.<java.lang.String: java.lang.String replace(java.lang.CharSequence,java.lang.CharSequence)>("AES/GCM/NoPadding", "DES"), method='<com.minimals.Cipher.aesGCMNoPaddingReplaceDES.CipherExample: void main(java.lang.String[])>'}, UnitContainer{unit=r1 = "AES", method='<com.minimals.Cipher.baseCaseVariable.CipherExample: void main(java.lang.String[])>'}, UnitContainer{unit=$r2 = "AES", method='<com.minimals.Cipher.aesStringReplace.CipherExample: void main(java.lang.String[])>'}]
=======================================
=======================================
***Violated Rule 1a: Found broken crypto schemes ***Constants: [0, "AES/GCM/NoPadding", 1, "des", "DE$S", 0, 0, 16, "AES/GCM/NoPadding", "des", "AES/GCM/NoPadding", "A", "D", 0, 0, "AES/GCM/NoPadding", 1, "$", ""] ***Config: "des" in line: <artifactId>des</artifactId> with value: <version>1</version> in file: META-INF/maven/com.minimals/des/pom.xml ***Config: "des" in line: <artifactId>des</artifactId> with value: <version>1</version> in file: META-INF/maven/com.minimals/des/pom.xml
=======================================
=======================================
***Violated Rule 4: Uses untrusted TrustManager ***Should at least get One accepted Issuer from Other Sources in getAcceptedIssuers method of com.minimals.SSL.TrustManager.BareBone_x509TrustManagerCanNotBypass.BareBone_X509TrustManagerCanNotBypassExt
=======================================
=======================================
***Violated Rule 4: Uses untrusted TrustManager ***Should throw java.security.cert.CertificateException in check(Client|Server)Trusted method of com.minimals.SSL.TrustManager.BareBone_x509TrustManagerCanNotBypass.BareBone_X509TrustManagerCanNotBypassExt
=======================================
=======================================
***Violated Rule 4: Uses untrusted TrustManager ***Should at least get One accepted Issuer from Other Sources in getAcceptedIssuers method of com.minimals.SSL.TrustManager.a_BareboneTrustManagerConditional.BareboneTrustManagerConditional$1
=======================================
=======================================
***Violated Rule 4: Uses untrusted TrustManager ***Should at least get One accepted Issuer from Other Sources in getAcceptedIssuers method of com.minimals.SSL.TrustManager.BareBone_X509TrustManager.BareBone_X509TrustManager$1
=======================================
=======================================
***Violated Rule 4: Uses untrusted TrustManager ***Should throw java.security.cert.CertificateException in check(Client|Server)Trusted method of com.minimals.SSL.TrustManager.BareBone_X509TrustManager.BareBone_X509TrustManager$1
=======================================
=======================================
***Violated Rule 4: Uses untrusted TrustManager ***Should at least get One accepted Issuer from Other Sources in getAcceptedIssuers method of com.minimals.SSL.TrustManager.BareBoneTrustManagerDummyStatements.BareboneTrustManagerConditional$1
=======================================
=======================================
***Violated Rule 4: Uses untrusted TrustManager ***Should at least get One accepted Issuer from Other Sources in getAcceptedIssuers method of com.minimals.SSL.TrustManager.BadSSL_Naive.BadSSL_Naive$1
=======================================
=======================================
***Violated Rule 4: Uses untrusted TrustManager ***Should throw java.security.cert.CertificateException in check(Client|Server)Trusted method of com.minimals.SSL.TrustManager.BadSSL_Naive.BadSSL_Naive$1
=======================================
=======================================
***Violated Rule 4: Uses untrusted TrustManager ***Should at least get One accepted Issuer from Other Sources in getAcceptedIssuers method of com.minimals.SSL.TrustManager.BareBoneTrustManagerConditional.BareboneTrustManagerConditional$1
=======================================
=======================================
***Violated Rule 4: Uses untrusted TrustManager ***Should at least get One accepted Issuer from Other Sources in getAcceptedIssuers method of com.minimals.SSL.TrustManager.b_BareboneTrustManagerConditional.BareboneTrustManagerConditional$1
=======================================
=======================================
***Violated Rule 4: Uses untrusted TrustManager ***Should at least get One accepted Issuer from Other Sources in getAcceptedIssuers method of com.minimals.SSL.TrustManager.ITrustManagerUser.ITrustManagerUser$1
=======================================
=======================================
***Violated Rule 4: Uses untrusted TrustManager ***Should throw java.security.cert.CertificateException in check(Client|Server)Trusted method of com.minimals.SSL.TrustManager.ITrustManagerUser.ITrustManagerUser$1
=======================================
=======================================
***Violated Rule 4: Uses untrusted TrustManager ***Should at least get One accepted Issuer from Other Sources in getAcceptedIssuers method of com.minimals.SSL.TrustManager.BareBone_X509TrustManagerCanNotBypass.BareBone_X509TrustManagerCanNotBypassExt
=======================================
=======================================
***Violated Rule 4: Uses untrusted TrustManager ***Should throw java.security.cert.CertificateException in check(Client|Server)Trusted method of com.minimals.SSL.TrustManager.BareBone_X509TrustManagerCanNotBypass.BareBone_X509TrustManagerCanNotBypassExt
=======================================
=======================================
***Violated Rule 6: Uses untrusted HostNameVerifier
***Cause: Fixed [1] used in com.minimals.SSL.HostNameVerifier.a_BadHostNameVerifier.BadHostName$1
=======================================
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
=======================================
***Violated Rule 2: Found broken hash functions ***Constants: ["MD5", "MD5", "MD5"]
[UnitContainer{unit=$r4 = virtualinvoke $r3.<java.lang.String: java.lang.String replace(java.lang.CharSequence,java.lang.CharSequence)>("SHA-256", "MD5"), method='<com.minimals.messagedigest.replaceSHA256DES.MessageDigestComplex: void main(java.lang.String[])>'}, UnitContainer{unit=r2 = "MD5", method='<com.minimals.messagedigest.baseCaseVariable.MessageDigestBase: void main(java.lang.String[])>'}, UnitContainer{unit=$fakeLocal_10[0] = "MD5", method='<com.minimals.messagedigest.baseCase.MessageDigestBase: void main(java.lang.String[])>'}]
=======================================
=======================================
***Violated Rule 2a: Found broken hash functions ***Constants: ["SHA-256", "SHA-256", 0, "MD$5", 1, 0, 0, "$", "", 16, "SHA-256", "md5", "md5", 0, 0, "SHA-256", 1, "md5", "SHA-256"]
=======================================
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
=======================================
***Violated Rule 10: Found constant IV in code ***Constants: ["ABCDEFGHI", "Hello"]
[UnitContainer{unit=r1 = "ABCDEFGHI", method='<com.minimals.IV.stash.StaticIV: void main(java.lang.String[])>'}, UnitContainer{unit=$r3 = "Hello", method='<com.minimals.IV.baseCase.StaticIV: void main(java.lang.String[])>'}]
=======================================
=======================================
***Violated Rule 10a: Found constant IV in code ***Constants: [65, 8, 8, 65, 0, "", 1, 0, 1, ""]
[UnitContainer{unit=i1 = 65, method='<com.minimals.IV.complexCase.ComplexStaticIV: void main(java.lang.String[])>'}, UnitContainer{unit=r4 = newarray (byte)[8], method='<com.minimals.Cipher.secureBlowFish.BlowFish: void main(java.lang.String[])>'}, UnitContainer{unit=r4 = newarray (byte)[8], method='<com.minimals.Cipher.insecureBlowFish.BlowFish: void main(java.lang.String[])>'}, UnitContainer{unit=$i0 = 65 + i2, method='<com.minimals.IV.stash.StaticIV: void main(java.lang.String[])>'}, UnitContainer{unit=i2 = 0, method='<com.minimals.IV.stash.StaticIV: void main(java.lang.String[])>'}, UnitContainer{unit=r20 = "", method='<com.minimals.IV.stash.StaticIV: void main(java.lang.String[])>'}, UnitContainer{unit=i2 = i2 + 1, method='<com.minimals.IV.stash.StaticIV: void main(java.lang.String[])>'}, UnitContainer{unit=$r3 = staticinvoke <java.lang.StringCoding: byte[] encode(char[],int,int)>($r2, 0, $i0), method='<java.lang.String: byte[] getBytes()>'}, UnitContainer{unit=i1 = i1 + 1, method='<com.minimals.IV.complexCase.ComplexStaticIV: void main(java.lang.String[])>'}, UnitContainer{unit=r12 = "", method='<com.minimals.IV.complexCase.ComplexStaticIV: void main(java.lang.String[])>'}]
=======================================
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
=======================================
***Violated Rule 11: Found predictable seeds in code ***Constants: ["Seed", "Seed", "Seed"]
[UnitContainer{unit=$r5 = "Seed", method='<com.minimals.RandomNumber.StaticSeedSecureRandom.basecase.SecureRand: void main(java.lang.String[])>'}, UnitContainer{unit=$r5 = "Seed", method='<com.minimals.RandomNumber.StaticSeedSecureRandom.basecase_seedconstructor.SecureRand: void main(java.lang.String[])>'}, UnitContainer{unit=$r5 = "Seed", method='<com.minimals.RandomNumber.StaticSeedSecureRandom.basecase_getinstance.SecureRand: void main(java.lang.String[])>'}]
=======================================
=======================================
***Violated Rule 11a: Found predictable seeds in code ***Constants: [0]
[UnitContainer{unit=$r4 = staticinvoke <java.lang.StringCoding: byte[] encode(java.nio.charset.Charset,char[],int,int)>(r1, $r3, 0, $i0), method='<java.lang.String: byte[] getBytes(java.nio.charset.Charset)>'}]
=======================================
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
***Multiple Base packages of /media/data/muse2/muse2repo/minimal/target/des-1.jar : [Supplier, com.minimals.RandomNumber, com.minimals.SSL, com.minimals.Cipher, com.minimals.messagedigest, com.minimals.IV]
=======================================
***Violated Rule 7a: Used HTTP Protocol ***Constants: ["https://self-signed.badssl.com"]
=======================================
Total Heuristics: 56
Total Orthogonal: 0
Total Constants: 15
Total Slices: 49
Average Length: 3.061224489795918
Depth: 1, Count 56
```
