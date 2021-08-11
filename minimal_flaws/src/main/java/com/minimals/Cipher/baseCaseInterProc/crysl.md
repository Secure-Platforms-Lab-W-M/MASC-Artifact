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
		Statement: r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>($r5)
		Method: <CipherExample: void main(java.lang.String[])>
		SHA-256: 2f5185169ccc2d33f3038c102273b4ff77d03f055d90b371a4990e56e82e4978
		Secure: false

Findings in Java Class: CipherExample

	 in Method: void main(java.lang.String[])
		ConstraintError violating CrySL rule for javax.crypto.Cipher (on Object #2f5185169ccc2d33f3038c102273b4ff77d03f055d90b371a4990e56e82e4978)
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>($r5)

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher (on Object #2f5185169ccc2d33f3038c102273b4ff77d03f055d90b371a4990e56e82e4978)
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r7 = virtualinvoke r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


======================= CogniCrypt Summary ==========================
	Number of CrySL rules: 44
	Number of Objects Analyzed: 1

	CogniCrypt found the following violations. For details see description above.
	ConstraintError: 1
	IncompleteOperationError: 1
=====================================================================
Analysis finished after 3 seconds
```
