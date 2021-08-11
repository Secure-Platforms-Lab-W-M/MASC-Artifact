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
		Variable: r2
		Type: javax.crypto.Cipher
		Statement: r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>(r1)
		Method: <CipherExample: void main(java.lang.String[])>
		SHA-256: 139f7c61c34bcec7bd268d0d1cd8b5c879d88b7973cad6807921585ea2f2392c
		Secure: false

Findings in Java Class: CipherExample

	 in Method: void main(java.lang.String[])
		ConstraintError violating CrySL rule for javax.crypto.Cipher (on Object #139f7c61c34bcec7bd268d0d1cd8b5c879d88b7973cad6807921585ea2f2392c)
			First parameter (with value "AES") should be any of AES/{CBC, GCM, PCBC, CTR, CTS, CFB, OFB}
			at statement: r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>(r1)

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher (on Object #139f7c61c34bcec7bd268d0d1cd8b5c879d88b7973cad6807921585ea2f2392c)
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


======================= CogniCrypt Summary ==========================
	Number of CrySL rules: 44
	Number of Objects Analyzed: 1

	CogniCrypt found the following violations. For details see description above.
	ConstraintError: 1
	IncompleteOperationError: 1
=====================================================================
Analysis finished after 3 seconds
```
