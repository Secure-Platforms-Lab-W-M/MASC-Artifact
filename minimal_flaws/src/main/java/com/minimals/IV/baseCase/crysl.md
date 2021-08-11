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
		Variable: $r4
		Type: javax.crypto.spec.IvParameterSpec
		Statement: specialinvoke $r4.<javax.crypto.spec.IvParameterSpec: void <init>(byte[])>(r1)
		Method: <StaticIV: void main(java.lang.String[])>
		SHA-256: 2dc39cf929d37521144bee6a2da8c478bec21331a62c1e34913acc5c065be534
		Secure: false

Findings in Java Class: StaticIV

	 in Method: void main(java.lang.String[])
		RequiredPredicateError violating CrySL rule for javax.crypto.spec.IvParameterSpec
			First parameter was not properly generated as randomized
			at statement: specialinvoke $r4.<javax.crypto.spec.IvParameterSpec: void <init>(byte[])>(r1)


======================= CogniCrypt Summary ==========================
	Number of CrySL rules: 44
	Number of Objects Analyzed: 1

	CogniCrypt found the following violations. For details see description above.
	RequiredPredicateError: 1
=====================================================================
Analysis finished after 3 seconds
```
