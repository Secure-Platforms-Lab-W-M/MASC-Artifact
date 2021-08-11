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

Findings in Java Class: org.afhdownloader.Download

	 in Method: java.util.ArrayList getDLUrl(java.lang.String)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r9 = virtualinvoke $r8.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r9 = virtualinvoke $r8.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r8 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r13 = virtualinvoke $r8.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r8 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r9 = virtualinvoke $r8.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r9 = virtualinvoke $r8.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r9 = virtualinvoke $r8.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r11 = virtualinvoke $r8.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r8 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r9 = virtualinvoke $r8.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r13 = virtualinvoke $r8.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r9 = virtualinvoke $r8.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r11 = virtualinvoke $r8.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r13 = virtualinvoke $r8.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r11 = virtualinvoke $r8.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r8 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r13 = virtualinvoke $r8.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r9 = virtualinvoke $r8.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r11 = virtualinvoke $r8.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r8 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


	 in Method: int onStartCommand(android.content.Intent,int,int)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


	 in Method: java.lang.String getBaseUrl()
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: java.lang.String getMD5(java.lang.String)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r1 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r1 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r1 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


	 in Method: java.lang.String parseUrl(java.lang.String)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r5 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r6 = virtualinvoke $r5.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r6 = virtualinvoke $r5.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r5 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r6 = virtualinvoke $r5.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r5 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r6 = virtualinvoke $r5.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r1 = virtualinvoke $r5.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r6 = virtualinvoke $r5.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r5 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r1 = virtualinvoke $r5.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r1 = virtualinvoke $r5.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r5 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r6 = virtualinvoke $r5.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r5 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


	 in Method: android.os.IBinder onBind(android.content.Intent)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


Findings in Java Class: org.afhdownloader.EasyPermissions

	 in Method: void runAnnotatedMethods(java.lang.Object,int)
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

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

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

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


	 in Method: void onRequestPermissionsResult(int,java.lang.String[],int[],java.lang.Object)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r8 = virtualinvoke $r6.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r6.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r8 = virtualinvoke $r6.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r6 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r6 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r6.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r8 = virtualinvoke $r6.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r6 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r6.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r6 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r6 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r6.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r6.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r6.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r6.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r6.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r6 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r8 = virtualinvoke $r6.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r6.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r8 = virtualinvoke $r6.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r8 = virtualinvoke $r6.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r6.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r6.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r8 = virtualinvoke $r6.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r6.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r6.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r8 = virtualinvoke $r6.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r6.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r8 = virtualinvoke $r6.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r6.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r6 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r6.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r8 = virtualinvoke $r6.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: void requestPermissions(java.lang.Object,java.lang.String,int,java.lang.String[])
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


	 in Method: boolean shouldShowRequestPermissionRationale(java.lang.Object,java.lang.String)
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
			at statement: $r0 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: void requestPermissions(java.lang.Object,java.lang.String,int,int,int,java.lang.String[])
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r1 = virtualinvoke $r4.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r4.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

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


	 in Method: void checkCallingObjectSuitability(java.lang.Object)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: boolean hasPermissions(android.content.Context,java.lang.String[])
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: void executePermissionsRequest(java.lang.Object,java.lang.String[],int)
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

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


	 in Method: android.app.Activity getActivity(java.lang.Object)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


Findings in Java Class: org.afhdownloader.MainActivity

	 in Method: void onCreate(android.os.Bundle)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r4.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r4.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r4.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

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

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r4.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r4.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r4 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r4.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r4.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: void run(android.content.Context)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


	 in Method: void onRequestPermissionsResult(int,java.lang.String[],int[])
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: void onActivityResult(int,int,android.content.Intent)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


	 in Method: void onPermissionsGranted(int,java.util.List)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: boolean onOptionsItemSelected(android.view.MenuItem)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r4.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

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

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r4.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r4.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r4.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r4 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r4.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: void setAlarm(android.content.Context)
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


	 in Method: java.lang.String buildPath(android.content.Context)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


	 in Method: void onResume()
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


	 in Method: void CancelAlarm(android.content.Context)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: void onPause()
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: void setRecurringAlarm(android.content.Context)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: boolean onCreateOptionsMenu(android.view.Menu)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: void onPermissionsDenied(int,java.util.List)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


Findings in Java Class: org.afhdownloader.AlarmReceiver

	 in Method: java.lang.String buildPath(android.content.Context)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: void onReceive(android.content.Context,android.content.Intent)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


Findings in Java Class: org.afhdownloader.MainActivityFragment

	 in Method: android.view.View onCreateView(android.view.LayoutInflater,android.view.ViewGroup,android.os.Bundle)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r4.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r4 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


	 in Method: void <init>()
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


Findings in Java Class: org.afhdownloader.MainActivity$ExecuteAsRootBase

	 in Method: boolean execute()
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

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

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


Findings in Java Class: org.afhdownloader.DirectoryPicker

	 in Method: java.lang.String[] names(java.util.ArrayList)
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

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: void onActivityResult(int,int,android.content.Intent)
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


	 in Method: void onCreate(android.os.Bundle)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r6 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

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

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r6 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r6 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r6 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: java.util.ArrayList filter(java.io.File[],boolean,boolean)
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

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r4.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r4.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: void returnDir(java.lang.String)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r6 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

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

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r6 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


Findings in Java Class: org.afhdownloader.Download$dlMd5

	 in Method: java.lang.String doInBackground(java.lang.String[])
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r5 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r6 = virtualinvoke $r5.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r6 = virtualinvoke $r5.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r6 = virtualinvoke $r5.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r5 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r5 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r6 = virtualinvoke $r5.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


Findings in Java Class: org.afhdownloader.MainActivity$1

	 in Method: java.util.ArrayList getCommandsToExecute()
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


Findings in Java Class: org.afhdownloader.Download$ParseURL

	 in Method: java.lang.String doInBackground(java.lang.String[])
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


Findings in Java Class: org.afhdownloader.DirectoryPicker$2

	 in Method: void onItemClick(android.widget.AdapterView,android.view.View,int,long)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r4.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r4 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


Findings in Java Class: org.afhdownloader.BootReceiver

	 in Method: void onReceive(android.content.Context,android.content.Intent)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

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


	 in Method: void setRecurringAlarm(android.content.Context)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


Findings in Java Class: org.afhdownloader.DirectoryPicker$1

	 in Method: void onClick(android.view.View)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


Findings in Java Class: org.afhdownloader.SetPreferenceActivity

	 in Method: void onCreate(android.os.Bundle)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


Findings in Java Class: org.afhdownloader.Download$ParseURLDownload

	 in Method: java.lang.String doInBackground(java.lang.String[])
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


Findings in Java Class: org.afhdownloader.LogUtil

	 in Method: java.lang.String makeLogTag(java.lang.Class)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


Findings in Java Class: org.afhdownloader.EasyPermissions$1

	 in Method: void onClick(android.content.DialogInterface,int)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


Findings in Java Class: org.afhdownloader.PrefsFragment

	 in Method: void onCreate(android.os.Bundle)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


Findings in Java Class: org.afhdownloader.Download$downloadFirstThread

	 in Method: java.lang.String doInBackground(java.lang.String[])
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


Findings in Java Class: org.afhdownloader.EasyPermissions$2

	 in Method: void onClick(android.content.DialogInterface,int)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


Findings in Java Class: org.jsoup.helper.HttpConnection$Response

	 in Method: void initUnSecureTSL()
		ConstraintError violating CrySL rule for javax.net.ssl.SSLContext
			First parameter (with value "SSL") should be any of {TLSv1, TLSv1.1, TLSv1.2}
			at statement: $r3 = staticinvoke <javax.net.ssl.SSLContext: javax.net.ssl.SSLContext getInstance(java.lang.String)>("SSL")

		RequiredPredicateError violating CrySL rule for javax.net.ssl.SSLContext
			Second parameter was not properly generatedTrustManager
			at statement: virtualinvoke $r3.<javax.net.ssl.SSLContext: void init(javax.net.ssl.KeyManager[],javax.net.ssl.TrustManager[],java.security.SecureRandom)>(null, r12, $r4)

		RequiredPredicateError violating CrySL rule for javax.net.ssl.SSLContext
			First parameter (with value null) was not properly generatedKeyManager
			at statement: virtualinvoke $r3.<javax.net.ssl.SSLContext: void init(javax.net.ssl.KeyManager[],javax.net.ssl.TrustManager[],java.security.SecureRandom)>(null, r12, $r4)


======================= CogniCrypt Summary ==========================
	Number of CrySL rules: 44
	Number of Objects Analyzed: 128

	CogniCrypt found the following violations. For details see description above.
	RequiredPredicateError: 2
	IncompleteOperationError: 223
	ConstraintError: 127
=====================================================================```
