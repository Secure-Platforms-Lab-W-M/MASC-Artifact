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
		Variable: $r7
		Type: java.security.SecureRandom
		Statement: specialinvoke $r7.<java.security.SecureRandom: void <init>(byte[])>(r1)
		Method: <SecureRand: void main(java.lang.String[])>
		SHA-256: 133170bcc4784be22bb095eca601bda85a4212f70fc16b2db3961cc72a619f88
		Secure: true
	Object:
		Variable: $r6
		Type: java.security.SecureRandom
		Statement: specialinvoke $r6.<java.security.SecureRandom: void <init>(byte[])>(r1)
		Method: <SecureRand: void main(java.lang.String[])>
		SHA-256: f88ca687d66fdb85fba80806925186c3e54ad83ab2811401d7bf354b44ec33ac
		Secure: true

======================= CogniCrypt Summary ==========================
	Number of CrySL rules: 44
	Number of Objects Analyzed: 2
No violation of any of the rules found.=====================================================================
Analysis finished after 2 seconds
```
