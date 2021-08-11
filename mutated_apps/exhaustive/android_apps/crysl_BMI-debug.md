```
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

Findings in Java Class: com.zola.bmi.BMIMain

	 in Method: void onCreate(android.os.Bundle)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


Findings in Java Class: com.zola.bmi.BMIMain$PlaceholderFragment

	 in Method: void <init>()
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


======================= CogniCrypt Summary ==========================
	Number of CrySL rules: 44
	Number of Objects Analyzed: 3

	CogniCrypt found the following violations. For details see description above.
	IncompleteOperationError: 3
	ConstraintError: 3
=====================================================================```
