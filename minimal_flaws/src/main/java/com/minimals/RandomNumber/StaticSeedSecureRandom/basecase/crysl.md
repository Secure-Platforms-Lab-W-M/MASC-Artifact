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
		Variable: $r6
		Type: java.security.SecureRandom
		Statement: specialinvoke $r6.<java.security.SecureRandom: void <init>()>()
		Method: <SecureRand: void main(java.lang.String[])>
		SHA-256: 1ae6eb5df249405cf266be62ebea7335a673230bf61aa595088ad17a87ddfbaf
		Secure: true
	Object:
		Variable: $r7
		Type: java.security.SecureRandom
		Statement: specialinvoke $r7.<java.security.SecureRandom: void <init>()>()
		Method: <SecureRand: void main(java.lang.String[])>
		SHA-256: 7d58b3c98b032553bc66941b4e2adcd485e4d8b112bf82c94e593e4dcec6a748
		Secure: true

======================= CogniCrypt Summary ==========================
	Number of CrySL rules: 44
	Number of Objects Analyzed: 2
No violation of any of the rules found.=====================================================================
Analysis finished after 3 seconds
```
