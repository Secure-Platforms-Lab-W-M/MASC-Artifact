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

Findings in Java Class: protect.babymonitor.ListenActivity

	 in Method: void streamAudio(java.net.Socket)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r4.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r4 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r4 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r4 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r4 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r4.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r4 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r4 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


	 in Method: void onDestroy()
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: void playAlert()
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


	 in Method: void onCreate(android.os.Bundle)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


Findings in Java Class: protect.babymonitor.MonitorActivity$4

	 in Method: void onServiceUnregistered(android.net.nsd.NsdServiceInfo)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: void onUnregistrationFailed(android.net.nsd.NsdServiceInfo,int)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: void onRegistrationFailed(android.net.nsd.NsdServiceInfo,int)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: void onServiceRegistered(android.net.nsd.NsdServiceInfo)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


Findings in Java Class: protect.babymonitor.MonitorActivity$2

	 in Method: void run()
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


Findings in Java Class: protect.babymonitor.MonitorActivity

	 in Method: boolean onCreateOptionsMenu(android.view.Menu)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: boolean onOptionsItemSelected(android.view.MenuItem)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


	 in Method: void serviceConnection(java.net.Socket)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r4 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r4 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r4 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r4.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r4.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r4 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r4 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


	 in Method: void onCreate(android.os.Bundle)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: void unregisterService()
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


	 in Method: void onDestroy()
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: void registerService(int)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


Findings in Java Class: protect.babymonitor.ListenActivity$2

	 in Method: void run()
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


Findings in Java Class: protect.babymonitor.DiscoverActivity

	 in Method: void startServiceDiscovery(java.lang.String)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


	 in Method: void onCreate(android.os.Bundle)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


	 in Method: void connectToChild(java.lang.String,int,java.lang.String)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r6 = virtualinvoke $r5.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r5 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


	 in Method: void loadDiscoveryViaMdns()
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: void onDestroy()
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


	 in Method: void loadDiscoveryViaAddress()
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


Findings in Java Class: protect.babymonitor.DiscoverActivity$5

	 in Method: void onServiceLost(android.net.nsd.NsdServiceInfo)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


	 in Method: void onServiceFound(android.net.nsd.NsdServiceInfo)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

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

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


	 in Method: void onStartDiscoveryFailed(java.lang.String,int)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r1 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: void onDiscoveryStopped(java.lang.String)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


	 in Method: void onStopDiscoveryFailed(java.lang.String,int)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r1 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: void onDiscoveryStarted(java.lang.String)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r1 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


Findings in Java Class: protect.babymonitor.DiscoverActivity$2

	 in Method: void onClick(android.view.View)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


Findings in Java Class: protect.babymonitor.DiscoverActivity$3

	 in Method: void onClick(android.view.View)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r11 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


Findings in Java Class: protect.babymonitor.StartActivity

	 in Method: void onCreate(android.os.Bundle)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


Findings in Java Class: protect.babymonitor.DiscoverActivity$1

	 in Method: void onClick(android.view.View)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


Findings in Java Class: protect.babymonitor.StartActivity$2

	 in Method: void onClick(android.view.View)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


Findings in Java Class: protect.babymonitor.DiscoverActivity$4

	 in Method: void onItemClick(android.widget.AdapterView,android.view.View,int,long)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


Findings in Java Class: protect.babymonitor.StartActivity$1

	 in Method: void onClick(android.view.View)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


======================= CogniCrypt Summary ==========================
	Number of CrySL rules: 44
	Number of Objects Analyzed: 70

	CogniCrypt found the following violations. For details see description above.
	IncompleteOperationError: 85
	ConstraintError: 70
=====================================================================```
