```
Using call graph algorithm CHA
Finished initializing soot
Analysis soot setup done after 0 seconds
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
		Type: javax.net.ssl.SSLContext
		Statement: r1 = staticinvoke <javax.net.ssl.SSLContext: javax.net.ssl.SSLContext getInstance(java.lang.String)>(varReplacer0)
		Method: <BadSSL_Naive: void main(java.lang.String[])>
		SHA-256: 7044122df8ed560c29d50e0a1bb076117886f65bff17b964186a23602768cc6f
		Secure: false
	Object:
		Variable: r27
		Type: java.security.SecureRandom
		Statement: specialinvoke r27.<java.security.SecureRandom: void <init>()>()
		Method: <BadSSL_Naive: void main(java.lang.String[])>
		SHA-256: a2bd31112389948fc9c2a974b38c7e86352c7f1bb264053facb8748f7a9606f8
		Secure: true

Findings in Java Class: BadSSL_Naive

	 in Method: void main(java.lang.String[])
		RequiredPredicateError violating CrySL rule for javax.net.ssl.SSLContext
			First parameter was not properly generated as generated Key Manager
			at statement: virtualinvoke r1.<javax.net.ssl.SSLContext: void init(javax.net.ssl.KeyManager[],javax.net.ssl.TrustManager[],java.security.SecureRandom)>(varReplacer1, r31, r27)

		RequiredPredicateError violating CrySL rule for javax.net.ssl.SSLContext
			Second parameter was not properly generated as generated Trust Manager
			at statement: virtualinvoke r1.<javax.net.ssl.SSLContext: void init(javax.net.ssl.KeyManager[],javax.net.ssl.TrustManager[],java.security.SecureRandom)>(varReplacer1, r31, r27)

		ConstraintError violating CrySL rule for javax.net.ssl.SSLContext (on Object #7044122df8ed560c29d50e0a1bb076117886f65bff17b964186a23602768cc6f)
			First parameter (with value "SSL") should be any of {TLSv1, TLSv1.1, TLSv1.2}
			at statement: r1 = staticinvoke <javax.net.ssl.SSLContext: javax.net.ssl.SSLContext getInstance(java.lang.String)>(varReplacer0)


======================= CogniCrypt Summary ==========================
	Number of CrySL rules: 44
	Number of Objects Analyzed: 2

	CogniCrypt found the following violations. For details see description above.
	ConstraintError: 1
	RequiredPredicateError: 2
=====================================================================
Analysis finished after 3 seconds
```
