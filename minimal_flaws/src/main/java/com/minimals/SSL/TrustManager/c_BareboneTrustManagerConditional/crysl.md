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
		Variable: r12
		Type: java.security.SecureRandom
		Statement: specialinvoke r12.<java.security.SecureRandom: void <init>()>()
		Method: <BareboneTrustManagerConditional: void main(java.lang.String[])>
		SHA-256: 70768da4edc45caadb916013f754acec708893b39598c38561870f5cb70b9cf3
		Secure: true
	Object:
		Variable: r2
		Type: javax.net.ssl.SSLContext
		Statement: r2 = staticinvoke <javax.net.ssl.SSLContext: javax.net.ssl.SSLContext getInstance(java.lang.String)>(varReplacer0)
		Method: <BareboneTrustManagerConditional: void main(java.lang.String[])>
		SHA-256: d3b403f04404fdfa4557ad3e153d53bfcb8095387d2bdc4a2994860537a7a632
		Secure: false

Findings in Java Class: BareboneTrustManagerConditional

	 in Method: void main(java.lang.String[])
		RequiredPredicateError violating CrySL rule for javax.net.ssl.SSLContext
			Second parameter was not properly generated as generated Trust Manager
			at statement: virtualinvoke r2.<javax.net.ssl.SSLContext: void init(javax.net.ssl.KeyManager[],javax.net.ssl.TrustManager[],java.security.SecureRandom)>(varReplacer1, r13, r12)

		ConstraintError violating CrySL rule for javax.net.ssl.SSLContext (on Object #d3b403f04404fdfa4557ad3e153d53bfcb8095387d2bdc4a2994860537a7a632)
			First parameter (with value "TLS") should be any of {TLSv1, TLSv1.1, TLSv1.2}
			at statement: r2 = staticinvoke <javax.net.ssl.SSLContext: javax.net.ssl.SSLContext getInstance(java.lang.String)>(varReplacer0)

		RequiredPredicateError violating CrySL rule for javax.net.ssl.SSLContext
			First parameter was not properly generated as generated Key Manager
			at statement: virtualinvoke r2.<javax.net.ssl.SSLContext: void init(javax.net.ssl.KeyManager[],javax.net.ssl.TrustManager[],java.security.SecureRandom)>(varReplacer1, r13, r12)


======================= CogniCrypt Summary ==========================
	Number of CrySL rules: 44
	Number of Objects Analyzed: 2

	CogniCrypt found the following violations. For details see description above.
	ConstraintError: 1
	RequiredPredicateError: 2
=====================================================================
Analysis finished after 2 seconds
```
