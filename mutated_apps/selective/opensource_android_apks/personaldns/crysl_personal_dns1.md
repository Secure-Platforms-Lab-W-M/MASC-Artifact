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

Findings in Java Class: util.Encryption

	 in Method: void init_AES(java.lang.String)
		RequiredPredicateError violating CrySL rule for javax.crypto.spec.IvParameterSpec
			First parameter was not properly randomized
			at statement: specialinvoke $r5.<javax.crypto.spec.IvParameterSpec: void <init>(byte[])>($r3)

		RequiredPredicateError violating CrySL rule for javax.crypto.spec.IvParameterSpec
			First parameter was not properly randomized
			at statement: specialinvoke $r5.<javax.crypto.spec.IvParameterSpec: void <init>(byte[])>($r3)

		RequiredPredicateError violating CrySL rule for javax.crypto.spec.IvParameterSpec
			First parameter was not properly randomized
			at statement: specialinvoke $r5.<javax.crypto.spec.IvParameterSpec: void <init>(byte[])>($r3)

		RequiredPredicateError violating CrySL rule for javax.crypto.spec.IvParameterSpec
			First parameter was not properly randomized
			at statement: specialinvoke $r5.<javax.crypto.spec.IvParameterSpec: void <init>(byte[])>($r3)

		RequiredPredicateError violating CrySL rule for javax.crypto.spec.IvParameterSpec
			First parameter was not properly randomized
			at statement: specialinvoke $r5.<javax.crypto.spec.IvParameterSpec: void <init>(byte[])>($r3)


======================= CogniCrypt Summary ==========================
	Number of CrySL rules: 44
	Number of Objects Analyzed: 5

	CogniCrypt found the following violations. For details see description above.
	RequiredPredicateError: 5
=====================================================================```
