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

Findings in Java Class: okhttp3.OkHttpClient

	 in Method: javax.net.ssl.X509TrustManager systemDefaultTrustManager()
		ImpreciseValueExtractionError violating CrySL rule for javax.net.ssl.TrustManagerFactory
			Constraint VC:java.lang.String algo - PKIX,SunX509, could not be evaluted due to insufficient information.
			at statement: $r3 = staticinvoke <javax.net.ssl.TrustManagerFactory: javax.net.ssl.TrustManagerFactory getInstance(java.lang.String)>($r2)

		RequiredPredicateError violating CrySL rule for javax.net.ssl.TrustManagerFactory
			First parameter (with value null) was not properly generatedKeyStore
			at statement: virtualinvoke $r3.<javax.net.ssl.TrustManagerFactory: void init(java.security.KeyStore)>(null)


	 in Method: javax.net.ssl.SSLSocketFactory systemDefaultSslSocketFactory(javax.net.ssl.X509TrustManager)
		RequiredPredicateError violating CrySL rule for javax.net.ssl.SSLContext
			First parameter (with value null) was not properly generatedKeyManager
			at statement: virtualinvoke $r4.<javax.net.ssl.SSLContext: void init(javax.net.ssl.KeyManager[],javax.net.ssl.TrustManager[],java.security.SecureRandom)>(null, r8, null)

		RequiredPredicateError violating CrySL rule for javax.net.ssl.SSLContext
			Second parameter was not properly generatedTrustManager
			at statement: virtualinvoke $r4.<javax.net.ssl.SSLContext: void init(javax.net.ssl.KeyManager[],javax.net.ssl.TrustManager[],java.security.SecureRandom)>(null, r8, null)

		RequiredPredicateError violating CrySL rule for javax.net.ssl.SSLContext
			First parameter (with value null) was not properly generatedKeyManager
			at statement: virtualinvoke $r4.<javax.net.ssl.SSLContext: void init(javax.net.ssl.KeyManager[],javax.net.ssl.TrustManager[],java.security.SecureRandom)>(null, r8, null)

		RequiredPredicateError violating CrySL rule for javax.net.ssl.SSLContext
			Second parameter was not properly generatedTrustManager
			at statement: virtualinvoke $r4.<javax.net.ssl.SSLContext: void init(javax.net.ssl.KeyManager[],javax.net.ssl.TrustManager[],java.security.SecureRandom)>(null, r8, null)


Findings in Java Class: com.jvillalba.apod.classic.activities.ImageActivity

	 in Method: void delayedHide()
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


	 in Method: void hide()
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

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: void show()
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: void onPostCreate(android.os.Bundle)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: void toggle()
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


Findings in Java Class: com.jvillalba.apod.classic.activities.ViewActivity

	 in Method: void onRequestPermissionsResult(int,java.lang.String[],int[])
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r4 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r4 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r4.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r4.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r4 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r4 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r4.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: java.lang.String getConcat(android.widget.TextView,java.lang.String)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r4 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r4.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r4 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r4.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r4 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


	 in Method: boolean onCreateOptionsMenu(android.view.Menu)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: void checkPermission(java.lang.String)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r1 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

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

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r1 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


	 in Method: void setDataNasaAPOD(com.jvillalba.apod.classic.model.NASA)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: void downloadPicasso(java.lang.String)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: void OlderVersions(java.lang.String)
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
			at statement: $r1 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: void onCreate(android.os.Bundle)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


	 in Method: boolean CheckPermission(java.lang.String)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


	 in Method: boolean onOptionsItemSelected(android.view.MenuItem)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: java.lang.String getImageNameApod()
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


Findings in Java Class: com.jvillalba.apod.classic.model.PicassoDownloader

	 in Method: void saveImage(android.graphics.Bitmap)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r6 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r6 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r6 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r7 = virtualinvoke $r6.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r7 = virtualinvoke $r6.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r6 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r6 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r7 = virtualinvoke $r6.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: void onPrepareLoad(android.graphics.drawable.Drawable)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


	 in Method: void <init>(java.lang.String,android.content.Context)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: void onBitmapLoaded(android.graphics.Bitmap,com.squareup.picasso.Picasso$LoadedFrom)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


Findings in Java Class: com.jvillalba.apod.classic.API.Tls12SocketFactory

	 in Method: okhttp3.OkHttpClient$Builder enableTls12OnPreLollipop(okhttp3.OkHttpClient$Builder)
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

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r4 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


	 in Method: void <init>(javax.net.ssl.SSLSocketFactory)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


Findings in Java Class: com.jvillalba.apod.classic.activities.MainActivity

	 in Method: void enforceIconBar()
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


	 in Method: void picassoClearCache(java.io.File)
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
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: boolean onOptionsItemSelected(android.view.MenuItem)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


	 in Method: boolean onCreateOptionsMenu(android.view.Menu)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


	 in Method: void getNasaAPODS()
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


	 in Method: void onCreate(android.os.Bundle)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r5 = virtualinvoke $r4.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r4 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


Findings in Java Class: com.jvillalba.apod.classic.API.API

	 in Method: retrofit2.Retrofit getApi()
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r1 = virtualinvoke $r0.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r0 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r1 = virtualinvoke $r0.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r1 = virtualinvoke $r0.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r1 = virtualinvoke $r0.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r0 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r0 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r1 = virtualinvoke $r0.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r0 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r1 = virtualinvoke $r0.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r1 = virtualinvoke $r0.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r1 = virtualinvoke $r0.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r1 = virtualinvoke $r0.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


Findings in Java Class: com.jvillalba.apod.classic.adapter.MyAdapter

	 in Method: void <init>(int,com.jvillalba.apod.classic.adapter.MyAdapter$OnItemClickListener)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


Findings in Java Class: com.jvillalba.apod.classic.activities.ImageActivity$1

	 in Method: void run()
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


Findings in Java Class: com.jvillalba.apod.classic.activities.ImageActivity$4

	 in Method: void onClick(android.view.View)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r2 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r3 = virtualinvoke $r2.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


Findings in Java Class: com.jvillalba.apod.classic.activities.ImageActivity$2

	 in Method: void run()
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


Findings in Java Class: com.jvillalba.apod.classic.activities.ImageActivity$3

	 in Method: void run()
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r1 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r2 = virtualinvoke $r1.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


Findings in Java Class: com.jvillalba.apod.classic.activities.ViewActivity$1

	 in Method: void onClick(android.view.View)
		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")

		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()


Findings in Java Class: com.jvillalba.apod.classic.controller.NasaController

	 in Method: void getNASAAPODS(com.jvillalba.apod.classic.adapter.MyAdapter,android.content.Context)
		IncompleteOperationError violating CrySL rule for javax.crypto.Cipher
			Operation on object of type javax.crypto.Cipher object not completed. Expected call to init
			at statement: $r4 = virtualinvoke $r3.<javax.crypto.Cipher: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for javax.crypto.Cipher
			First parameter (with value "DES") should be any of {AES, PBEWithHmacSHA224AndAES_128, PBEWithHmacSHA256AndAES_128, PBEWithHmacSHA384AndAES_128, PBEWithHmacSHA512AndAES_128, PBEWithHmacSHA224AndAES_256, PBEWithHmacSHA256AndAES_256, PBEWithHmacSHA384AndAES_256, PBEWithHmacSHA512AndAES_256, RSA}
			at statement: $r3 = staticinvoke <javax.crypto.Cipher: javax.crypto.Cipher getInstance(java.lang.String)>("DES")


Findings in Java Class: okhttp3.internal.platform.ConscryptPlatform

	 in Method: javax.net.ssl.SSLContext getSSLContext()
		ConstraintError violating CrySL rule for javax.net.ssl.SSLContext
			First parameter (with value "TLS") should be any of {TLSv1, TLSv1.1, TLSv1.2}
			at statement: $r3 = staticinvoke <javax.net.ssl.SSLContext: javax.net.ssl.SSLContext getInstance(java.lang.String,java.security.Provider)>("TLS", $r2)


Findings in Java Class: okhttp3.internal.platform.Platform

	 in Method: javax.net.ssl.SSLContext getSSLContext()
		ConstraintError violating CrySL rule for javax.net.ssl.SSLContext
			First parameter (with value "TLS") should be any of {TLSv1, TLSv1.1, TLSv1.2}
			at statement: $r2 = staticinvoke <javax.net.ssl.SSLContext: javax.net.ssl.SSLContext getInstance(java.lang.String)>("TLS")


======================= CogniCrypt Summary ==========================
	Number of CrySL rules: 44
	Number of Objects Analyzed: 71

	CogniCrypt found the following violations. For details see description above.
	ImpreciseValueExtractionError: 1
	IncompleteOperationError: 71
	ConstraintError: 68
	RequiredPredicateError: 5
=====================================================================```
