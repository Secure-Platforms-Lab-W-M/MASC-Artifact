```
Using call graph algorithm CHA
Finished initializing soot
Analysis soot setup done after 1 seconds
Ruleset: 
	javax.net.ssl.TrustManagerFactory
	javax.crypto.Cipher
	java.security.AlgorithmParameters
	java.security.KeyPairGenerator
	java.security.cert.TrustAnchor
	java.security.spec.DSAParameterSpec
	javax.crypto.SecretKey
	javax.net.ssl.SSLParameters
	Stopwatch
	javax.net.ssl.SSLContext
	javax.net.ssl.CertPathTrustManagerParameters
	SSLSocketFactory
	java.security.spec.DSAGenParameterSpec
	javax.crypto.spec.DHGenParameterSpec
	java.security.DigestOutputStream
	javax.crypto.SecretKeyFactory
	java.security.DigestInputStream
	java.security.Key
	javax.crypto.spec.DHParameterSpec
	com.amazonaws.services.kms.model.GenerateDataKeyRequest
	javax.net.ssl.KeyManagerFactory
	java.security.KeyStore
	java.security.KeyPair
	javax.crypto.KeyGenerator
	javax.crypto.Mac
	SSLSocket
	java.security.cert.PKIXBuilderParameters
	SSLServerSocket
	javax.net.ssl.KeyStoreBuilderParameters
	javax.crypto.CipherOutputStream
	java.security.SecureRandom
	javax.crypto.spec.IvParameterSpec
	java.security.spec.RSAKeyGenParameterSpec
	javax.crypto.spec.SecretKeySpec
	javax.crypto.spec.PBEParameterSpec
	MessageDigest
	javax.crypto.CipherInputStream
	javax.net.ssl.SSLEngine
	javax.crypto.spec.PBEKeySpec
	SSLServerSocketFactory
	javax.crypto.spec.GCMParameterSpec
	javax.xml.crypto.dsig.spec.HMACParameterSpec
	java.security.Signature
	java.security.cert.PKIXParameters

Analyzed Objects: 
	Object:
		Variable: r1
		Type: javax.crypto.Cipher
		Statement: r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>(varReplacer82)
		Method: <com.minimals.Cipher.baseCase.CipherExample: void main(java.lang.String[])>
		SHA-256: b15127d0a3da24236803c1f7b3ab4dca77128eaf3024de9f5c4e45db93c65b03
		Secure: false
	Object:
		Variable: r1
		Type: java.security.MessageDigest
		Statement: r1 = staticinvoke <java.security.MessageDigest: java.security.MessageDigest getInstance(java.lang.String)>(r2)
		Method: <com.minimals.messagedigest.baseCaseVariable.MessageDigestBase: void main(java.lang.String[])>
		SHA-256: 1c0bab732650472899a66ca83b46111d9b47f770b775d0fa47d8a68cabab7ace
		Secure: false
	Object:
		Variable: $r5
		Type: javax.crypto.spec.IvParameterSpec
		Statement: specialinvoke $r5.<javax.crypto.spec.IvParameterSpec: void <init>(byte[])>($r6)
		Method: <com.minimals.IV.stash.StaticIV: void main(java.lang.String[])>
		SHA-256: 38ba94403ff05fac562863c5f46cd39dbda58a7e5f91fafefbd0d21e11959d5b
		Secure: false
	Object:
		Variable: r12
		Type: java.security.SecureRandom
		Statement: specialinvoke r12.<java.security.SecureRandom: void <init>()>()
		Method: <com.minimals.SSL.TrustManager.b_BareboneTrustManagerConditional.BareboneTrustManagerConditional: void main(java.lang.String[])>
		SHA-256: 70768da4edc45caadb916013f754acec708893b39598c38561870f5cb70b9cf3
		Secure: true
	Object:
		Variable: r1
		Type: javax.crypto.KeyGenerator
		Statement: r1 = staticinvoke <javax.crypto.KeyGenerator: javax.crypto.KeyGenerator getInstance(java.lang.String)>(r19)
		Method: <com.minimals.Cipher.KeyToAlgoDES.CipherExample: void main(java.lang.String[])>
		SHA-256: 48b4de8bd0bfd30b8df3e70fb0e59ec756da1f4e9b7bde13d847a68b46d99dcb
		Secure: false
	Object:
		Variable: r9
		Type: javax.crypto.Cipher
		Statement: r9 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>(varReplacer89)
		Method: <com.minimals.Cipher.secureBlowFish.BlowFish: void main(java.lang.String[])>
		SHA-256: e46c862c73703c5a3a51950d38311aada51412ace5fac9962886e2896caa5305
		Secure: false
	Object:
		Variable: r1
		Type: java.security.MessageDigest
		Statement: r1 = staticinvoke <java.security.MessageDigest: java.security.MessageDigest getInstance(java.lang.String)>($r4)
		Method: <com.minimals.messagedigest.complexCase.MessageDigestComplex: void main(java.lang.String[])>
		SHA-256: 322237567e71292f6944de5a96efe61ba8c091cf83a2b1ef28a032a495012ef1
		Secure: false
	Object:
		Variable: r1
		Type: javax.crypto.Cipher
		Statement: r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>($r3)
		Method: <com.minimals.Cipher.aesGCMNoPaddingReplaceDES.CipherExample: void main(java.lang.String[])>
		SHA-256: 7bb853a8ee3e10ebcc2be4ffa2349b93f51d45cb2260bb52e99f5e08d6964973
		Secure: true
	Object:
		Variable: r4
		Type: byte[]
		Statement: virtualinvoke r3.<java.security.SecureRandom: void nextBytes(byte[])>(r4)
		Method: <com.minimals.Cipher.insecureBlowFish.BlowFish: void main(java.lang.String[])>
		SHA-256: 50029c1e7d03a5125d75284628bf1e4dc5718da4c60eb2b4836eaae67454aaf1
		Secure: true
	Object:
		Variable: $r15
		Type: byte[]
		Statement: r7 = virtualinvoke r2.<javax.crypto.Cipher: byte[] doFinal(byte[])>($r15)
		Method: <com.minimals.Cipher.insecureBlowFish.BlowFish: void main(java.lang.String[])>
		SHA-256: f231131bb199362174ec179fee6ee22b143cfb4ebf18c924c43fb973d4d51123
		Secure: false
	Object:
		Variable: $r7
		Type: java.security.SecureRandom
		Statement: specialinvoke $r7.<java.security.SecureRandom: void <init>()>()
		Method: <com.minimals.RandomNumber.StaticSeedSecureRandom.basecase.SecureRand: void main(java.lang.String[])>
		SHA-256: 7d58b3c98b032553bc66941b4e2adcd485e4d8b112bf82c94e593e4dcec6a748
		Secure: true
	Object:
		Variable: r1
		Type: java.security.MessageDigest
		Statement: r1 = staticinvoke <java.security.MessageDigest: java.security.MessageDigest getInstance(java.lang.String)>($r4)
		Method: <com.minimals.messagedigest.replaceSHA256DES.MessageDigestComplex: void main(java.lang.String[])>
		SHA-256: 322237567e71292f6944de5a96efe61ba8c091cf83a2b1ef28a032a495012ef1
		Secure: false
	Object:
		Variable: r1
		Type: javax.crypto.SecretKey
		Statement: r1 = virtualinvoke r23.<javax.crypto.KeyGenerator: javax.crypto.SecretKey generateKey()>()
		Method: <com.minimals.Cipher.secureBlowFish.BlowFish: void main(java.lang.String[])>
		SHA-256: 16dee3246074ced605600e11aa11adc564816d60aa743627ca5b61de5aa352df
		Secure: false
	Object:
		Variable: r2
		Type: javax.crypto.Cipher
		Statement: r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>(varReplacer86)
		Method: <com.minimals.Cipher.secureBlowFish.BlowFish: void main(java.lang.String[])>
		SHA-256: b13ac763dcca490edbd92acc16bfbdd6a3abc4ceecc2d44b8b83751756489e59
		Secure: false
	Object:
		Variable: r2
		Type: javax.net.ssl.SSLContext
		Statement: r2 = staticinvoke <javax.net.ssl.SSLContext: javax.net.ssl.SSLContext getInstance(java.lang.String)>(varReplacer42)
		Method: <com.minimals.SSL.TrustManager.c_BareboneTrustManagerConditional.BareboneTrustManagerConditional: void main(java.lang.String[])>
		SHA-256: 248b41bfc92c58363872e81221fb5119a055f58e621662f258e977418cb44e96
		Secure: false
	Object:
		Variable: r2
		Type: javax.net.ssl.SSLContext
		Statement: r2 = staticinvoke <javax.net.ssl.SSLContext: javax.net.ssl.SSLContext getInstance(java.lang.String)>(varReplacer75)
		Method: <com.minimals.SSL.TrustManager.BareBoneTrustManagerConditional.BareboneTrustManagerConditional: void main(java.lang.String[])>
		SHA-256: 54b77cb94edbac25e4c532af4f9e158b8a6305707029822d33d5c9f1de797380
		Secure: false
	Object:
		Variable: $r6
		Type: java.security.SecureRandom
		Statement: specialinvoke $r6.<java.security.SecureRandom: void <init>()>()
		Method: <com.minimals.RandomNumber.StaticSeedSecureRandom.basecase.SecureRand: void main(java.lang.String[])>
		SHA-256: 1ae6eb5df249405cf266be62ebea7335a673230bf61aa595088ad17a87ddfbaf
		Secure: true
	Object:
		Variable: r12
		Type: java.security.SecureRandom
		Statement: specialinvoke r12.<java.security.SecureRandom: void <init>()>()
		Method: <com.minimals.SSL.TrustManager.c_BareboneTrustManagerConditional.BareboneTrustManagerConditional: void main(java.lang.String[])>
		SHA-256: 70768da4edc45caadb916013f754acec708893b39598c38561870f5cb70b9cf3
		Secure: true
	Object:
		Variable: $r16
		Type: byte[]
		Statement: virtualinvoke r3.<javax.crypto.Cipher: byte[] doFinal(byte[])>($r16)
		Method: <com.minimals.Cipher.KeyToAlgoDES.CipherExample: void main(java.lang.String[])>
		SHA-256: c5fc8528b60caa687cc9bcf81663f1c37f4ef2c1e43853efb990d3015fed08d7
		Secure: false
	Object:
		Variable: $r1
		Type: javax.crypto.spec.IvParameterSpec
		Statement: specialinvoke $r1.<javax.crypto.spec.IvParameterSpec: void <init>(byte[])>($r2)
		Method: <com.minimals.IV.complexCase.ComplexStaticIV: void main(java.lang.String[])>
		SHA-256: bc2264aacc98e139fe3586c7dc483b9707ad7a375ab781817c922262b4dbe480
		Secure: false
	Object:
		Variable: $r3
		Type: javax.crypto.spec.IvParameterSpec
		Statement: specialinvoke $r3.<javax.crypto.spec.IvParameterSpec: void <init>(byte[])>($r4)
		Method: <com.minimals.IV.stash.StaticIV: void main(java.lang.String[])>
		SHA-256: 479320ae47d4d7f3ce72fbb30832de7ca80e12da1268b4baa7a595e131d178a8
		Secure: false
	Object:
		Variable: r4
		Type: byte[]
		Statement: virtualinvoke r3.<java.security.SecureRandom: void nextBytes(byte[])>(r4)
		Method: <com.minimals.Cipher.secureBlowFish.BlowFish: void main(java.lang.String[])>
		SHA-256: 50029c1e7d03a5125d75284628bf1e4dc5718da4c60eb2b4836eaae67454aaf1
		Secure: true
	Object:
		Variable: r12
		Type: java.security.SecureRandom
		Statement: specialinvoke r12.<java.security.SecureRandom: void <init>()>()
		Method: <com.minimals.SSL.TrustManager.BareBoneTrustManagerConditional.BareboneTrustManagerConditional: void main(java.lang.String[])>
		SHA-256: 70768da4edc45caadb916013f754acec708893b39598c38561870f5cb70b9cf3
		Secure: true
	Object:
		Variable: r3
		Type: javax.crypto.Cipher
		Statement: r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>($r6)
		Method: <com.minimals.Cipher.KeyToAlgoAES.CipherExample: void main(java.lang.String[])>
		SHA-256: 7d9e2e670df83aafea8d17e4df3a979de0a11220f611454768e4415fcd754284
		Secure: true
	Object:
		Variable: $r13
		Type: javax.crypto.spec.IvParameterSpec
		Statement: specialinvoke $r13.<javax.crypto.spec.IvParameterSpec: void <init>(byte[])>(r4)
		Method: <com.minimals.Cipher.secureBlowFish.BlowFish: void main(java.lang.String[])>
		SHA-256: cd6d28310a8d2a68e638411e68d93286f28e84a8b2b77c57f6d70ca4e4fa2bca
		Secure: true
	Object:
		Variable: r2
		Type: javax.crypto.SecretKey
		Statement: r2 = virtualinvoke r1.<javax.crypto.KeyGenerator: javax.crypto.SecretKey generateKey()>()
		Method: <com.minimals.Cipher.KeyToAlgoDES.CipherExample: void main(java.lang.String[])>
		SHA-256: c872f77c6888a9431748d99fe89984c08572a586b79c86e9e29828909b34073b
		Secure: false
	Object:
		Variable: r1
		Type: java.security.MessageDigest
		Statement: r1 = staticinvoke <java.security.MessageDigest: java.security.MessageDigest getInstance(java.lang.String)>($r4)
		Method: <com.minimals.messagedigest.stringReplace.MessageDigestComplex: void main(java.lang.String[])>
		SHA-256: 322237567e71292f6944de5a96efe61ba8c091cf83a2b1ef28a032a495012ef1
		Secure: false
	Object:
		Variable: $r4
		Type: javax.crypto.spec.IvParameterSpec
		Statement: specialinvoke $r4.<javax.crypto.spec.IvParameterSpec: void <init>(byte[])>(r1)
		Method: <com.minimals.IV.baseCase.StaticIV: void main(java.lang.String[])>
		SHA-256: 2dc39cf929d37521144bee6a2da8c478bec21331a62c1e34913acc5c065be534
		Secure: false
	Object:
		Variable: r3
		Type: java.security.SecureRandom
		Statement: r3 = staticinvoke <java.security.SecureRandom: java.security.SecureRandom getInstance(java.lang.String)>(varReplacer0)
		Method: <com.minimals.RandomNumber.StaticSeedSecureRandom.basecase_getinstance.SecureRand: void main(java.lang.String[])>
		SHA-256: eaedb8a0791dacd489ddc1f390d5ed7bc741af5ada3ba6457559c625c5ac539c
		Secure: true
	Object:
		Variable: r27
		Type: java.security.SecureRandom
		Statement: specialinvoke r27.<java.security.SecureRandom: void <init>()>()
		Method: <com.minimals.SSL.TrustManager.BadSSL_Naive.BadSSL_Naive: void main(java.lang.String[])>
		SHA-256: a2bd31112389948fc9c2a974b38c7e86352c7f1bb264053facb8748f7a9606f8
		Secure: true
	Object:
		Variable: r5
		Type: javax.net.ssl.SSLContext
		Statement: r5 = staticinvoke <javax.net.ssl.SSLContext: javax.net.ssl.SSLContext getInstance(java.lang.String)>(varReplacer31)
		Method: <com.minimals.SSL.Context.baseCase.ContextOfSSL: void main(java.lang.String[])>
		SHA-256: fbdf9604413f001ffd1a35c94585d079907a0d0538349fd6bf2fbdb7bf9a487c
		Secure: false
	Object:
		Variable: r7
		Type: byte[]
		Statement: r10 = virtualinvoke r9.<javax.crypto.Cipher: byte[] doFinal(byte[])>(r7)
		Method: <com.minimals.Cipher.insecureBlowFish.BlowFish: void main(java.lang.String[])>
		SHA-256: 73d42227ab7aae55c96a58e0be97d7e18d6a7d377204f1860ba8f9978b8d2343
		Secure: false
	Object:
		Variable: r1
		Type: javax.crypto.SecretKey
		Statement: r1 = virtualinvoke r23.<javax.crypto.KeyGenerator: javax.crypto.SecretKey generateKey()>()
		Method: <com.minimals.Cipher.insecureBlowFish.BlowFish: void main(java.lang.String[])>
		SHA-256: 16dee3246074ced605600e11aa11adc564816d60aa743627ca5b61de5aa352df
		Secure: false
	Object:
		Variable: r10
		Type: byte[]
		Statement: r10 = virtualinvoke r9.<javax.crypto.Cipher: byte[] doFinal(byte[])>(r7)
		Method: <com.minimals.Cipher.insecureBlowFish.BlowFish: void main(java.lang.String[])>
		SHA-256: 1324b5d26793bc7185b7c76ed0fcbce6b9ede22f40c89e84f9f5f9d968bd463
		Secure: false
	Object:
		Variable: r2
		Type: javax.net.ssl.SSLContext
		Statement: r2 = staticinvoke <javax.net.ssl.SSLContext: javax.net.ssl.SSLContext getInstance(java.lang.String)>(varReplacer39)
		Method: <com.minimals.SSL.TrustManager.b_BareboneTrustManagerConditional.BareboneTrustManagerConditional: void main(java.lang.String[])>
		SHA-256: 4a4b898ecfdae6b2deda056e758ab4a71eb1d722f0d4791d000f26023c6df3b6
		Secure: false
	Object:
		Variable: r2
		Type: javax.crypto.SecretKey
		Statement: r2 = virtualinvoke r1.<javax.crypto.KeyGenerator: javax.crypto.SecretKey generateKey()>()
		Method: <com.minimals.Cipher.KeyToAlgoAES.CipherExample: void main(java.lang.String[])>
		SHA-256: 5bce6398308cc44259d1d275e95d5280cb2ff48421643966f27a1463b1042141
		Secure: true
	Object:
		Variable: r10
		Type: byte[]
		Statement: r10 = virtualinvoke r9.<javax.crypto.Cipher: byte[] doFinal(byte[])>(r7)
		Method: <com.minimals.Cipher.secureBlowFish.BlowFish: void main(java.lang.String[])>
		SHA-256: 1324b5d26793bc7185b7c76ed0fcbce6b9ede22f40c89e84f9f5f9d968bd463
		Secure: false
	Object:
		Variable: r2
		Type: javax.crypto.Cipher
		Statement: r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>(r1)
		Method: <com.minimals.Cipher.baseCaseVariable.CipherExample: void main(java.lang.String[])>
		SHA-256: 139f7c61c34bcec7bd268d0d1cd8b5c879d88b7973cad6807921585ea2f2392c
		Secure: false
	Object:
		Variable: r1
		Type: javax.crypto.KeyGenerator
		Statement: r1 = staticinvoke <javax.crypto.KeyGenerator: javax.crypto.KeyGenerator getInstance(java.lang.String)>(r19)
		Method: <com.minimals.Cipher.KeyToAlgoAES.CipherExample: void main(java.lang.String[])>
		SHA-256: 48b4de8bd0bfd30b8df3e70fb0e59ec756da1f4e9b7bde13d847a68b46d99dcb
		Secure: true
	Object:
		Variable: r3
		Type: javax.crypto.Cipher
		Statement: r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>($r6)
		Method: <com.minimals.Cipher.KeyToAlgoDES.CipherExample: void main(java.lang.String[])>
		SHA-256: 7d9e2e670df83aafea8d17e4df3a979de0a11220f611454768e4415fcd754284
		Secure: false
	Object:
		Variable: r1
		Type: javax.net.ssl.SSLContext
		Statement: r1 = staticinvoke <javax.net.ssl.SSLContext: javax.net.ssl.SSLContext getInstance(java.lang.String)>(varReplacer63)
		Method: <com.minimals.SSL.TrustManager.BadSSL_Naive.BadSSL_Naive: void main(java.lang.String[])>
		SHA-256: d89cd4cd5d2b4622817b2679dfcead04b17a986501d5dad08725c78ea5f00a08
		Secure: false
	Object:
		Variable: r2
		Type: java.security.SecureRandom
		Statement: r2 = staticinvoke <java.security.SecureRandom: java.security.SecureRandom getInstance(java.lang.String)>(varReplacer1)
		Method: <com.minimals.RandomNumber.StaticSeedSecureRandom.basecase_getinstance.SecureRand: void main(java.lang.String[])>
		SHA-256: fc41b1f91736f607d38f8c136ee620274ada5c161ded45fc175666c3c42a7983
		Secure: true
	Object:
		Variable: r7
		Type: byte[]
		Statement: r7 = virtualinvoke r2.<javax.crypto.Cipher: byte[] doFinal(byte[])>($r15)
		Method: <com.minimals.Cipher.insecureBlowFish.BlowFish: void main(java.lang.String[])>
		SHA-256: 4065fb5c254ff26e648fed28c011654069a96dfc3aef20122fda079933aef71a
		Secure: false
	Object:
		Variable: $r12
		Type: java.security.SecureRandom
		Statement: specialinvoke $r12.<java.security.SecureRandom: void <init>()>()
		Method: <com.minimals.Cipher.insecureBlowFish.BlowFish: void main(java.lang.String[])>
		SHA-256: 90fced152e8e3da4b11fef669c67c96acb325b98e4f2d64ab3d513172a851b2a
		Secure: true
	Object:
		Variable: $r16
		Type: byte[]
		Statement: virtualinvoke r3.<javax.crypto.Cipher: byte[] doFinal(byte[])>($r16)
		Method: <com.minimals.Cipher.KeyToAlgoAES.CipherExample: void main(java.lang.String[])>
		SHA-256: 1fbbc659b28558b8ab87b9c0557b98358d86c36cb4d4675769d39f02e94ec2ad
		Secure: true
	Object:
		Variable: r12
		Type: java.security.SecureRandom
		Statement: specialinvoke r12.<java.security.SecureRandom: void <init>()>()
		Method: <com.minimals.SSL.TrustManager.BareBoneTrustManagerDummyStatements.BareboneTrustManagerConditional: void main(java.lang.String[])>
		SHA-256: 70768da4edc45caadb916013f754acec708893b39598c38561870f5cb70b9cf3
		Secure: true
	Object:
		Variable: r2
		Type: javax.crypto.Cipher
		Statement: r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>(varReplacer81)
		Method: <com.minimals.Cipher.insecureBlowFish.BlowFish: void main(java.lang.String[])>
		SHA-256: 7959a8c73efe5602866fb4c38aae9d6affa65e2dfdfc3f963e2de86cae008de7
		Secure: false
	Object:
		Variable: r1
		Type: javax.crypto.Cipher
		Statement: r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>($r3)
		Method: <com.minimals.Cipher.stringReplace.CipherExample: void main(java.lang.String[])>
		SHA-256: 7bb853a8ee3e10ebcc2be4ffa2349b93f51d45cb2260bb52e99f5e08d6964973
		Secure: false
	Object:
		Variable: r23
		Type: javax.crypto.KeyGenerator
		Statement: r23 = staticinvoke <javax.crypto.KeyGenerator: javax.crypto.KeyGenerator getInstance(java.lang.String)>(varReplacer87)
		Method: <com.minimals.Cipher.secureBlowFish.BlowFish: void main(java.lang.String[])>
		SHA-256: 9275431db7e51af355737ed9b8a862f57442eeb90da66f45d45036b484384a63
		Secure: false
	Object:
		Variable: r23
		Type: javax.crypto.KeyGenerator
		Statement: r23 = staticinvoke <javax.crypto.KeyGenerator: javax.crypto.KeyGenerator getInstance(java.lang.String)>(varReplacer77)
		Method: <com.minimals.Cipher.insecureBlowFish.BlowFish: void main(java.lang.String[])>
		SHA-256: a024d8a16fd2225395f4db41970197c5f274d3a7e0a039da789b11ef4cff13ff
		Secure: false
	Object:
		Variable: r1
		Type: javax.crypto.Cipher
		Statement: r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>(varReplacer94)
		Method: <com.minimals.Cipher.aesGCMNoPadding.CipherExample: void main(java.lang.String[])>
		SHA-256: 62a54cb0072bf7b6d470d7018f1dbaf767b24e80caf513650edbbef3c9cf584e
		Secure: true
	Object:
		Variable: r1
		Type: javax.crypto.Cipher
		Statement: r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>($r5)
		Method: <com.minimals.Cipher.baseCaseInterProc.CipherExample: void main(java.lang.String[])>
		SHA-256: 2f5185169ccc2d33f3038c102273b4ff77d03f055d90b371a4990e56e82e4978
		Secure: false
	Object:
		Variable: $r6
		Type: java.security.SecureRandom
		Statement: specialinvoke $r6.<java.security.SecureRandom: void <init>(byte[])>(r1)
		Method: <com.minimals.RandomNumber.StaticSeedSecureRandom.basecase_seedconstructor.SecureRand: void main(java.lang.String[])>
		SHA-256: f88ca687d66fdb85fba80806925186c3e54ad83ab2811401d7bf354b44ec33ac
		Secure: true
	Object:
		Variable: r1
		Type: javax.crypto.Cipher
		Statement: r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>(varReplacer97)
		Method: <com.minimals.Cipher.differentCase.CipherExample: void main(java.lang.String[])>
		SHA-256: c0d1ef38dc60ff2ac66f0782fa8a64a8acbf50623559002b5479391931d98c1d
		Secure: false
	Object:
		Variable: r7
		Type: byte[]
		Statement: r10 = virtualinvoke r9.<javax.crypto.Cipher: byte[] doFinal(byte[])>(r7)
		Method: <com.minimals.Cipher.secureBlowFish.BlowFish: void main(java.lang.String[])>
		SHA-256: 73d42227ab7aae55c96a58e0be97d7e18d6a7d377204f1860ba8f9978b8d2343
		Secure: false
	Object:
		Variable: r2
		Type: javax.net.ssl.SSLContext
		Statement: r2 = staticinvoke <javax.net.ssl.SSLContext: javax.net.ssl.SSLContext getInstance(java.lang.String)>(varReplacer48)
		Method: <com.minimals.SSL.TrustManager.BareBoneTrustManagerDummyStatements.BareboneTrustManagerConditional: void main(java.lang.String[])>
		SHA-256: 1640f8148548e457d4f1845fc41938dee49e6e1f9544fab6edbeceef5f8e96be
		Secure: false
	Object:
		Variable: r1
		Type: java.security.MessageDigest
		Statement: r1 = staticinvoke <java.security.MessageDigest: java.security.MessageDigest getInstance(java.lang.String)>($r6)
		Method: <com.minimals.messagedigest.interprocedureCase.MessageDigestComplex: void main(java.lang.String[])>
		SHA-256: e6b4f247e714fc5f35e3fb1bfb29f31888008c915d413d1f9802f5298efdd21a
		Secure: false
	Object:
		Variable: r7
		Type: byte[]
		Statement: r7 = virtualinvoke r2.<javax.crypto.Cipher: byte[] doFinal(byte[])>($r15)
		Method: <com.minimals.Cipher.secureBlowFish.BlowFish: void main(java.lang.String[])>
		SHA-256: 4065fb5c254ff26e648fed28c011654069a96dfc3aef20122fda079933aef71a
		Secure: false
	Object:
		Variable: $r15
		Type: byte[]
		Statement: r7 = virtualinvoke r2.<javax.crypto.Cipher: byte[] doFinal(byte[])>($r15)
		Method: <com.minimals.Cipher.secureBlowFish.BlowFish: void main(java.lang.String[])>
		SHA-256: f231131bb199362174ec179fee6ee22b143cfb4ebf18c924c43fb973d4d51123
		Secure: false
	Object:
		Variable: $r7
		Type: java.security.SecureRandom
		Statement: specialinvoke $r7.<java.security.SecureRandom: void <init>(byte[])>(r1)
		Method: <com.minimals.RandomNumber.StaticSeedSecureRandom.basecase_seedconstructor.SecureRand: void main(java.lang.String[])>
		SHA-256: 133170bcc4784be22bb095eca601bda85a4212f70fc16b2db3961cc72a619f88
		Secure: true
	Object:
		Variable: r1
		Type: javax.crypto.Cipher
		Statement: r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>($r4)
		Method: <com.minimals.Cipher.stringCaseTransform.CipherExample: void main(java.lang.String[])>
		SHA-256: ec0074a3ce1d64c1ddd9ec7a81889b51f358e1fe705b0021d408c98015c9e655
		Secure: true
	Object:
		Variable: r9
		Type: javax.crypto.Cipher
		Statement: r9 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>(varReplacer80)
		Method: <com.minimals.Cipher.insecureBlowFish.BlowFish: void main(java.lang.String[])>
		SHA-256: 10936c1e4dfc2ac3babb97282723bf04fbd4754468fa9aad4b2abb4b2eae7b68
		Secure: false
	Object:
		Variable: $r12
		Type: java.security.SecureRandom
		Statement: specialinvoke $r12.<java.security.SecureRandom: void <init>()>()
		Method: <com.minimals.Cipher.secureBlowFish.BlowFish: void main(java.lang.String[])>
		SHA-256: 90fced152e8e3da4b11fef669c67c96acb325b98e4f2d64ab3d513172a851b2a
		Secure: true
	Object:
		Variable: $r13
		Type: javax.crypto.spec.IvParameterSpec
		Statement: specialinvoke $r13.<javax.crypto.spec.IvParameterSpec: void <init>(byte[])>(r4)
		Method: <com.minimals.Cipher.insecureBlowFish.BlowFish: void main(java.lang.String[])>
		SHA-256: cd6d28310a8d2a68e638411e68d93286f28e84a8b2b77c57f6d70ca4e4fa2bca
		Secure: true
	Object:
		Variable: r1
		Type: javax.crypto.Cipher
		Statement: r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>($r3)
		Method: <com.minimals.Cipher.aesStringReplace.CipherExample: void main(java.lang.String[])>
		SHA-256: 7bb853a8ee3e10ebcc2be4ffa2349b93f51d45cb2260bb52e99f5e08d6964973
		Secure: true
	Object:
		Variable: r1
		Type: java.security.MessageDigest
		Statement: r1 = staticinvoke <java.security.MessageDigest: java.security.MessageDigest getInstance(java.lang.String)>($r5)
		Method: <com.minimals.messagedigest.stringCaseTransformation.MessageDigestComplex: void main(java.lang.String[])>
		SHA-256: c71d1114a32d7cbdc650b3972d072097291fc5d5108fde07bb61ed59a56aa60b
		Secure: false
	Object:
		Variable: $r6
		Type: javax.crypto.spec.IvParameterSpec
		Statement: specialinvoke $r6.<javax.crypto.spec.IvParameterSpec: void <init>(byte[])>($r7)
		Method: <com.minimals.IV.timeCase.CurrentTimeIV: void main(java.lang.String[])>
		SHA-256: 5d6267b08cb51a52464e6e4819e8f227851397d5bdcfaab2d054af3eb2b8275
		Secure: false
	Object:
		Variable: r1
		Type: java.security.MessageDigest
		Statement: r1 = staticinvoke <java.security.MessageDigest: java.security.MessageDigest getInstance(java.lang.String)>(varReplacer103)
		Method: <com.minimals.messagedigest.baseCase.MessageDigestBase: void main(java.lang.String[])>
		SHA-256: 3d8b8237e5d376caf788e23307d652e7e3fcc231791f140eeda803fa15011466
		Secure: false
	Object:
		Variable: r1
		Type: java.security.MessageDigest
		Statement: r1 = staticinvoke <java.security.MessageDigest: java.security.MessageDigest getInstance(java.lang.String)>(varReplacer115)
		Method: <com.minimals.messagedigest.differentCase.MessageDigestComplex: void main(java.lang.String[])>
		SHA-256: 611134b05f56460691aaca7e380775ae082e14798b4bbb7ef1eb2b65b0c8da84
		Secure: false

Findings in Java Class: com.minimals.SSL.Context.baseCase.ContextOfSSL

	 in Method: void main(java.lang.String[])
		ConstraintError violating CrySL rule for javax.net.ssl.SSLContext (on Object #fbdf9604413f001ffd1a35c94585d079907a0d0538349fd6bf2fbdb7bf9a487c)
			First parameter (with value "SSL") should be any of {TLSv1, TLSv1.1, TLSv1.2}
			at statement: r5 = staticinvoke <javax.net.ssl.SSLContext: javax.net.ssl.SSLContext getInstance(java.lang.String)>(varReplacer31)

		IncompleteOperationError violating CrySL rule for javax.net.ssl.SSLContext (on Object #fbdf9604413f001ffd1a35c94585d079907a0d0538349fd6bf2fbdb7bf9a487c)
			Operation on object of type javax.net.ssl.SSLContext object not completed. Expected call to init
			at statement: $r2 = virtualinvoke r5.<javax.net.ssl.SSLContext: java.lang.String getProtocol()>()


Findings in Java Class: com.minimals.SSL.TrustManager.b_BareboneTrustManagerConditional.BareboneTrustManagerConditional

	 in Method: void main(java.lang.String[])
		RequiredPredicateError violating CrySL rule for javax.net.ssl.SSLContext
			First parameter was not properly generated as generated Key Manager
			at statement: virtualinvoke r2.<javax.net.ssl.SSLContext: void init(javax.net.ssl.KeyManager[],javax.net.ssl.TrustManager[],java.security.SecureRandom)>(varReplacer37, r13, r12)

		RequiredPredicateError violating CrySL rule for javax.net.ssl.SSLContext
			Second parameter was not properly generated as generated Trust Manager
			at statement: virtualinvoke r2.<javax.net.ssl.SSLContext: void init(javax.net.ssl.KeyManager[],javax.net.ssl.TrustManager[],java.security.SecureRandom)>(varReplacer37, r13, r12)

		ConstraintError violating CrySL rule for javax.net.ssl.SSLContext (on Object #4a4b898ecfdae6b2deda056e758ab4a71eb1d722f0d4791d000f26023c6df3b6)
			First parameter (with value "TLS") should be any of {TLSv1, TLSv1.1, TLSv1.2}
			at statement: r2 = staticinvoke <javax.net.ssl.SSLContext: javax.net.ssl.SSLContext getInstance(java.lang.String)>(varReplacer39)


Findings in Java Class: com.minimals.SSL.TrustManager.c_BareboneTrustManagerConditional.BareboneTrustManagerConditional

	 in Method: void main(java.lang.String[])
		RequiredPredicateError violating CrySL rule for javax.net.ssl.SSLContext
			Second parameter was not properly generated as generated Trust Manager
			at statement: virtualinvoke r2.<javax.net.ssl.SSLContext: void init(javax.net.ssl.KeyManager[],javax.net.ssl.TrustManager[],java.security.SecureRandom)>(varReplacer41, r13, r12)

		RequiredPredicateError violating CrySL rule for javax.net.ssl.SSLContext
			First parameter was not properly generated as generated Key Manager
			at statement: virtualinvoke r2.<javax.net.ssl.SSLContext: void init(javax.net.ssl.KeyManager[],javax.net.ssl.TrustManager[],java.security.SecureRandom)>(varReplacer41, r13, r12)

		ConstraintError violating CrySL rule for javax.net.ssl.SSLContext (on Object #248b41bfc92c58363872e81221fb5119a055f58e621662f258e977418cb44e96)
			First parameter (with value "TLS") should be any of {TLSv1, TLSv1.1, TLSv1.2}
			at statement: r2 = staticinvoke <javax.net.ssl.SSLContext: javax.net.ssl.SSLContext getInstance(java.lang.String)>(varReplacer42)


Findings in Java Class: com.minimals.SSL.TrustManager.BareBoneTrustManagerDummyStatements.BareboneTrustManagerConditional

	 in Method: void main(java.lang.String[])
		ConstraintError violating CrySL rule for javax.net.ssl.SSLContext (on Object #1640f8148548e457d4f1845fc41938dee49e6e1f9544fab6edbeceef5f8e96be)
			First parameter (with value "SSL") should be any of {TLSv1, TLSv1.1, TLSv1.2}
			at statement: r2 = staticinvoke <javax.net.ssl.SSLContext: javax.net.ssl.SSLContext getInstance(java.lang.String)>(varReplacer48)

		RequiredPredicateError violating CrySL rule for javax.net.ssl.SSLContext
			First parameter was not properly generated as generated Key Manager
			at statement: virtualinvoke r2.<javax.net.ssl.SSLContext: void init(javax.net.ssl.KeyManager[],javax.net.ssl.TrustManager[],java.security.SecureRandom)>(varReplacer49, r13, r12)

		RequiredPredicateError violating CrySL rule for javax.net.ssl.SSLContext
			Second parameter was not properly generated as generated Trust Manager
			at statement: virtualinvoke r2.<javax.net.ssl.SSLContext: void init(javax.net.ssl.KeyManager[],javax.net.ssl.TrustManager[],java.security.SecureRandom)>(varReplacer49, r13, r12)


Findings in Java Class: com.minimals.SSL.TrustManager.BadSSL_Naive.BadSSL_Naive

	 in Method: void main(java.lang.String[])
		RequiredPredicateError violating CrySL rule for javax.net.ssl.SSLContext
			First parameter was not properly generated as generated Key Manager
			at statement: virtualinvoke r1.<javax.net.ssl.SSLContext: void init(javax.net.ssl.KeyManager[],javax.net.ssl.TrustManager[],java.security.SecureRandom)>(varReplacer61, r31, r27)

		ConstraintError violating CrySL rule for javax.net.ssl.SSLContext (on Object #d89cd4cd5d2b4622817b2679dfcead04b17a986501d5dad08725c78ea5f00a08)
			First parameter (with value "SSL") should be any of {TLSv1, TLSv1.1, TLSv1.2}
			at statement: r1 = staticinvoke <javax.net.ssl.SSLContext: javax.net.ssl.SSLContext getInstance(java.lang.String)>(varReplacer63)

		RequiredPredicateError violating CrySL rule for javax.net.ssl.SSLContext
			Second parameter was not properly generated as generated Trust Manager
			at statement: virtualinvoke r1.<javax.net.ssl.SSLContext: void init(javax.net.ssl.KeyManager[],javax.net.ssl.TrustManager[],java.security.SecureRandom)>(varReplacer61, r31, r27)


Findings in Java Class: com.minimals.SSL.TrustManager.BareBoneTrustManagerConditional.BareboneTrustManagerConditional

	 in Method: void main(java.lang.String[])
		ConstraintError violating CrySL rule for javax.net.ssl.SSLContext (on Object #54b77cb94edbac25e4c532af4f9e158b8a6305707029822d33d5c9f1de797380)
			First parameter (with value "TLS") should be any of {TLSv1, TLSv1.1, TLSv1.2}
			at statement: r2 = staticinvoke <javax.net.ssl.SSLContext: javax.net.ssl.SSLContext getInstance(java.lang.String)>(varReplacer75)

		RequiredPredicateError violating CrySL rule for javax.net.ssl.SSLContext
			First parameter was not properly generated as generated Key Manager
			at statement: virtualinvoke r2.<javax.net.ssl.SSLContext: void init(javax.net.ssl.KeyManager[],javax.net.ssl.TrustManager[],java.security.SecureRandom)>(varReplacer76, r13, r12)

		RequiredPredicateError violating CrySL rule for javax.net.ssl.SSLContext
			Second parameter was not properly generated as generated Trust Manager
			at statement: virtualinvoke r2.<javax.net.ssl.SSLContext: void init(javax.net.ssl.KeyManager[],javax.net.ssl.TrustManager[],java.security.SecureRandom)>(varReplacer76, r13, r12)


Findings in Java Class: com.minimals.Cipher.insecureBlowFish.BlowFish

	 in Method: void main(java.lang.String[])
		ConstraintError violating CrySL rule for javax.crypto.Cipher (on Object #10936c1e4dfc2ac3babb97282723bf04fbd4754468fa9aad4b2abb4b2eae7b68)
			First parameter (with value "Blowfish/CFB8/NoPadding") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: r9 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>(varReplacer80)

		ConstraintError violating CrySL rule for javax.crypto.Cipher (on Object #7959a8c73efe5602866fb4c38aae9d6affa65e2dfdfc3f963e2de86cae008de7)
			First parameter (with value "Blowfish/CFB8/NoPadding") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>(varReplacer81)

		ConstraintError violating CrySL rule for javax.crypto.KeyGenerator (on Object #a024d8a16fd2225395f4db41970197c5f274d3a7e0a039da789b11ef4cff13ff)
			First parameter (with value "Blowfish") should be any of {AES, HmacSHA224, HmacSHA256, HmacSHA384, HmacSHA512}
			at statement: r23 = staticinvoke <javax.crypto.KeyGenerator: javax.crypto.KeyGenerator getInstance(java.lang.String)>(varReplacer77)

		RequiredPredicateError violating CrySL rule for javax.crypto.Cipher
			Second parameter was not properly generated as generated Key
			at statement: virtualinvoke r2.<javax.crypto.Cipher: void init(int,java.security.Key,java.security.spec.AlgorithmParameterSpec)>(varReplacer78, r1, r5)

		RequiredPredicateError violating CrySL rule for javax.crypto.Cipher
			Second parameter was not properly generated as generated Key
			at statement: virtualinvoke r9.<javax.crypto.Cipher: void init(int,java.security.Key,java.security.spec.AlgorithmParameterSpec)>(varReplacer79, r1, r5)


Findings in Java Class: com.minimals.Cipher.baseCase.CipherExample

	 in Method: void main(java.lang.String[])
		ConstraintError violating CrySL rule for javax.crypto.Cipher (on Object #b15127d0a3da24236803c1f7b3ab4dca77128eaf3024de9f5c4e45db93c65b03)
			First parameter (with value "AES") should be any of AES/{CBC, GCM, PCBC, CTR, CTS, CFB, OFB}
			at statement: r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>(varReplacer82)


Findings in Java Class: com.minimals.Cipher.secureBlowFish.BlowFish

	 in Method: void main(java.lang.String[])
		ConstraintError violating CrySL rule for javax.crypto.Cipher (on Object #e46c862c73703c5a3a51950d38311aada51412ace5fac9962886e2896caa5305)
			First parameter (with value "Blowfish/CFB8/NoPadding") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: r9 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>(varReplacer89)

		ConstraintError violating CrySL rule for javax.crypto.KeyGenerator (on Object #9275431db7e51af355737ed9b8a862f57442eeb90da66f45d45036b484384a63)
			First parameter (with value "Blowfish") should be any of {AES, HmacSHA224, HmacSHA256, HmacSHA384, HmacSHA512}
			at statement: r23 = staticinvoke <javax.crypto.KeyGenerator: javax.crypto.KeyGenerator getInstance(java.lang.String)>(varReplacer87)

		RequiredPredicateError violating CrySL rule for javax.crypto.Cipher
			Second parameter was not properly generated as generated Key
			at statement: virtualinvoke r9.<javax.crypto.Cipher: void init(int,java.security.Key,java.security.spec.AlgorithmParameterSpec)>(varReplacer85, r1, r5)

		RequiredPredicateError violating CrySL rule for javax.crypto.Cipher
			Second parameter was not properly generated as generated Key
			at statement: virtualinvoke r2.<javax.crypto.Cipher: void init(int,java.security.Key,java.security.spec.AlgorithmParameterSpec)>(varReplacer88, r1, r5)

		ConstraintError violating CrySL rule for javax.crypto.Cipher (on Object #b13ac763dcca490edbd92acc16bfbdd6a3abc4ceecc2d44b8b83751756489e59)
			First parameter (with value "Blowfish/CFB8/NoPadding") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>(varReplacer86)


Findings in Java Class: com.minimals.Cipher.KeyToAlgoDES.CipherExample

	 in Method: void main(java.lang.String[])
		RequiredPredicateError violating CrySL rule for javax.crypto.Cipher
			Second parameter was not properly generated as generated Key
			at statement: virtualinvoke r3.<javax.crypto.Cipher: void init(int,java.security.Key)>(varReplacer92, r2)

		ConstraintError violating CrySL rule for javax.crypto.KeyGenerator (on Object #48b4de8bd0bfd30b8df3e70fb0e59ec756da1f4e9b7bde13d847a68b46d99dcb)
			First parameter (with value "DES") should be any of {AES, HmacSHA224, HmacSHA256, HmacSHA384, HmacSHA512}
			at statement: r1 = staticinvoke <javax.crypto.KeyGenerator: javax.crypto.KeyGenerator getInstance(java.lang.String)>(r19)


Findings in Java Class: com.minimals.Cipher.stringReplace.CipherExample

	 in Method: void main(java.lang.String[])
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher (on Object #7bb853a8ee3e10ebcc2be4ffa2349b93f51d45cb2260bb52e99f5e08d6964973)
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


Findings in Java Class: com.minimals.Cipher.baseCaseVariable.CipherExample

	 in Method: void main(java.lang.String[])
		ConstraintError violating CrySL rule for javax.crypto.Cipher (on Object #139f7c61c34bcec7bd268d0d1cd8b5c879d88b7973cad6807921585ea2f2392c)
			First parameter (with value "AES") should be any of AES/{CBC, GCM, PCBC, CTR, CTS, CFB, OFB}
			at statement: r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>(r1)

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher (on Object #139f7c61c34bcec7bd268d0d1cd8b5c879d88b7973cad6807921585ea2f2392c)
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


Findings in Java Class: com.minimals.Cipher.differentCase.CipherExample

	 in Method: void main(java.lang.String[])
		ConstraintError violating CrySL rule for javax.crypto.Cipher (on Object #c0d1ef38dc60ff2ac66f0782fa8a64a8acbf50623559002b5479391931d98c1d)
			First parameter (with value "des") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>(varReplacer97)


Findings in Java Class: com.minimals.Cipher.baseCaseInterProc.CipherExample

	 in Method: void main(java.lang.String[])
		ConstraintError violating CrySL rule for javax.crypto.Cipher (on Object #2f5185169ccc2d33f3038c102273b4ff77d03f055d90b371a4990e56e82e4978)
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>($r5)

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher (on Object #2f5185169ccc2d33f3038c102273b4ff77d03f055d90b371a4990e56e82e4978)
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r7 = virtualinvoke r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


Findings in Java Class: com.minimals.messagedigest.baseCase.MessageDigestBase

	 in Method: void main(java.lang.String[])
		ConstraintError violating CrySL rule for MessageDigest (on Object #3d8b8237e5d376caf788e23307d652e7e3fcc231791f140eeda803fa15011466)
			First parameter (with value "MD5") should be any of {SHA-256, SHA-384, SHA-512}
			at statement: r1 = staticinvoke <java.security.MessageDigest: java.security.MessageDigest getInstance(java.lang.String)>(varReplacer103)

		IncompleteOperationError violating CrySL rule for MessageDigest (on Object #3d8b8237e5d376caf788e23307d652e7e3fcc231791f140eeda803fa15011466)
			Operation on object of type java.security.MessageDigest object not completed. Expected call to digest, update
			at statement: $r4 = virtualinvoke r1.<java.security.MessageDigest: java.lang.String getAlgorithm()>()


Findings in Java Class: com.minimals.messagedigest.replaceSHA256DES.MessageDigestComplex

	 in Method: void main(java.lang.String[])
		IncompleteOperationError violating CrySL rule for MessageDigest (on Object #322237567e71292f6944de5a96efe61ba8c091cf83a2b1ef28a032a495012ef1)
			Operation on object of type java.security.MessageDigest object not completed. Expected call to digest, update
			at statement: $r6 = virtualinvoke r1.<java.security.MessageDigest: java.lang.String getAlgorithm()>()


Findings in Java Class: com.minimals.messagedigest.complexCase.MessageDigestComplex

	 in Method: void main(java.lang.String[])
		IncompleteOperationError violating CrySL rule for MessageDigest (on Object #322237567e71292f6944de5a96efe61ba8c091cf83a2b1ef28a032a495012ef1)
			Operation on object of type java.security.MessageDigest object not completed. Expected call to digest, update
			at statement: $r6 = virtualinvoke r1.<java.security.MessageDigest: java.lang.String getAlgorithm()>()


Findings in Java Class: com.minimals.messagedigest.stringReplace.MessageDigestComplex

	 in Method: void main(java.lang.String[])
		IncompleteOperationError violating CrySL rule for MessageDigest (on Object #322237567e71292f6944de5a96efe61ba8c091cf83a2b1ef28a032a495012ef1)
			Operation on object of type java.security.MessageDigest object not completed. Expected call to digest, update
			at statement: $r6 = virtualinvoke r1.<java.security.MessageDigest: java.lang.String getAlgorithm()>()


Findings in Java Class: com.minimals.messagedigest.baseCaseVariable.MessageDigestBase

	 in Method: void main(java.lang.String[])
		ConstraintError violating CrySL rule for MessageDigest (on Object #1c0bab732650472899a66ca83b46111d9b47f770b775d0fa47d8a68cabab7ace)
			First parameter (with value "MD5") should be any of {SHA-256, SHA-384, SHA-512}
			at statement: r1 = staticinvoke <java.security.MessageDigest: java.security.MessageDigest getInstance(java.lang.String)>(r2)

		IncompleteOperationError violating CrySL rule for MessageDigest (on Object #1c0bab732650472899a66ca83b46111d9b47f770b775d0fa47d8a68cabab7ace)
			Operation on object of type java.security.MessageDigest object not completed. Expected call to digest, update
			at statement: $r5 = virtualinvoke r1.<java.security.MessageDigest: java.lang.String getAlgorithm()>()


Findings in Java Class: com.minimals.messagedigest.stringCaseTransformation.MessageDigestComplex

	 in Method: void main(java.lang.String[])
		IncompleteOperationError violating CrySL rule for MessageDigest (on Object #c71d1114a32d7cbdc650b3972d072097291fc5d5108fde07bb61ed59a56aa60b)
			Operation on object of type java.security.MessageDigest object not completed. Expected call to digest, update
			at statement: $r7 = virtualinvoke r1.<java.security.MessageDigest: java.lang.String getAlgorithm()>()


Findings in Java Class: com.minimals.messagedigest.differentCase.MessageDigestComplex

	 in Method: void main(java.lang.String[])
		IncompleteOperationError violating CrySL rule for MessageDigest (on Object #611134b05f56460691aaca7e380775ae082e14798b4bbb7ef1eb2b65b0c8da84)
			Operation on object of type java.security.MessageDigest object not completed. Expected call to digest, update
			at statement: $r4 = virtualinvoke r1.<java.security.MessageDigest: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for MessageDigest (on Object #611134b05f56460691aaca7e380775ae082e14798b4bbb7ef1eb2b65b0c8da84)
			First parameter (with value "md5") should be any of {SHA-256, SHA-384, SHA-512}
			at statement: r1 = staticinvoke <java.security.MessageDigest: java.security.MessageDigest getInstance(java.lang.String)>(varReplacer115)


Findings in Java Class: com.minimals.messagedigest.interprocedureCase.MessageDigestComplex

	 in Method: void main(java.lang.String[])
		IncompleteOperationError violating CrySL rule for MessageDigest (on Object #e6b4f247e714fc5f35e3fb1bfb29f31888008c915d413d1f9802f5298efdd21a)
			Operation on object of type java.security.MessageDigest object not completed. Expected call to digest, update
			at statement: $r8 = virtualinvoke r1.<java.security.MessageDigest: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for MessageDigest (on Object #e6b4f247e714fc5f35e3fb1bfb29f31888008c915d413d1f9802f5298efdd21a)
			First parameter (with value "MD5") should be any of {SHA-256, SHA-384, SHA-512}
			at statement: r1 = staticinvoke <java.security.MessageDigest: java.security.MessageDigest getInstance(java.lang.String)>($r6)


Findings in Java Class: com.minimals.IV.stash.StaticIV

	 in Method: void main(java.lang.String[])
		RequiredPredicateError violating CrySL rule for javax.crypto.spec.IvParameterSpec
			First parameter was not properly generated as randomized
			at statement: specialinvoke $r5.<javax.crypto.spec.IvParameterSpec: void <init>(byte[])>($r6)

		RequiredPredicateError violating CrySL rule for javax.crypto.spec.IvParameterSpec
			First parameter was not properly generated as randomized
			at statement: specialinvoke $r3.<javax.crypto.spec.IvParameterSpec: void <init>(byte[])>($r4)


Findings in Java Class: com.minimals.IV.complexCase.ComplexStaticIV

	 in Method: void main(java.lang.String[])
		RequiredPredicateError violating CrySL rule for javax.crypto.spec.IvParameterSpec
			First parameter was not properly generated as randomized
			at statement: specialinvoke $r1.<javax.crypto.spec.IvParameterSpec: void <init>(byte[])>($r2)


Findings in Java Class: com.minimals.IV.baseCase.StaticIV

	 in Method: void main(java.lang.String[])
		RequiredPredicateError violating CrySL rule for javax.crypto.spec.IvParameterSpec
			First parameter was not properly generated as randomized
			at statement: specialinvoke $r4.<javax.crypto.spec.IvParameterSpec: void <init>(byte[])>(r1)


Findings in Java Class: com.minimals.IV.timeCase.CurrentTimeIV

	 in Method: void main(java.lang.String[])
		RequiredPredicateError violating CrySL rule for javax.crypto.spec.IvParameterSpec
			First parameter was not properly generated as randomized
			at statement: specialinvoke $r6.<javax.crypto.spec.IvParameterSpec: void <init>(byte[])>($r7)


======================= CogniCrypt Summary ==========================
	Number of CrySL rules: 44
	Number of Objects Analyzed: 69

	CogniCrypt found the following violations. For details see description above.
	ConstraintError: 21
	RequiredPredicateError: 20
	IncompleteOperationError: 12
=====================================================================
Analysis finished after 5 seconds
```
