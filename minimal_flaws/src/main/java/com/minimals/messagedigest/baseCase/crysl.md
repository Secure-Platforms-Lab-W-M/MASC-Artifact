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
		Type: java.security.MessageDigest
		Statement: r1 = staticinvoke <java.security.MessageDigest: java.security.MessageDigest getInstance(java.lang.String)>(varReplacer0)
		Method: <MessageDigestBase: void main(java.lang.String[])>
		SHA-256: 7e8c4b35f6e8bf48242aa72648c6fd8ba99b339f231b3769b1c229ab2c312e3a
		Secure: false

Findings in Java Class: MessageDigestBase

	 in Method: void main(java.lang.String[])
		IncompleteOperationError violating CrySL rule for MessageDigest (on Object #7e8c4b35f6e8bf48242aa72648c6fd8ba99b339f231b3769b1c229ab2c312e3a)
			Operation on object of type java.security.MessageDigest object not completed. Expected call to digest, update
			at statement: $r4 = virtualinvoke r1.<java.security.MessageDigest: java.lang.String getAlgorithm()>()

		ConstraintError violating CrySL rule for MessageDigest (on Object #7e8c4b35f6e8bf48242aa72648c6fd8ba99b339f231b3769b1c229ab2c312e3a)
			First parameter (with value "MD5") should be any of {SHA-256, SHA-384, SHA-512}
			at statement: r1 = staticinvoke <java.security.MessageDigest: java.security.MessageDigest getInstance(java.lang.String)>(varReplacer0)


======================= CogniCrypt Summary ==========================
	Number of CrySL rules: 44
	Number of Objects Analyzed: 1

	CogniCrypt found the following violations. For details see description above.
	ConstraintError: 1
	IncompleteOperationError: 1
=====================================================================
Analysis finished after 2 seconds
```
