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
		Variable: r3
		Type: java.security.SecureRandom
		Statement: r3 = staticinvoke <java.security.SecureRandom: java.security.SecureRandom getInstance(java.lang.String)>(varReplacer1)
		Method: <SecureRand: void main(java.lang.String[])>
		SHA-256: 6db923d7fafb5a2b64f136cdb45d54545b6f44c680bbf9db73ac30c43db5d04b
		Secure: true
	Object:
		Variable: r2
		Type: java.security.SecureRandom
		Statement: r2 = staticinvoke <java.security.SecureRandom: java.security.SecureRandom getInstance(java.lang.String)>(varReplacer0)
		Method: <SecureRand: void main(java.lang.String[])>
		SHA-256: 602b6fd6778f7b506ca6b5c2fb896bcb85cd011efbdbc64652746c3a51ba3f74
		Secure: true

======================= CogniCrypt Summary ==========================
	Number of CrySL rules: 44
	Number of Objects Analyzed: 2
No violation of any of the rules found.=====================================================================
Analysis finished after 2 seconds
```
