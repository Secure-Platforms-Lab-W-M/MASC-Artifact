
# TrustManager

- [TrustManager](#trustmanager)
- [Icon indicators](#icon-indicators)
- [Issues Summary](#issues-summary)
  - [1. BadSSL_Naive](#1-badsslnaive)
    - [Checking by Tools](#checking-by-tools)
  - [2. BareBone_X509TrustManager](#2-barebonex509trustmanager)
    - [Checking by Tools](#checking-by-tools-1)
  - [3. BareBone_X509TrustManagerCanNotBypassExt](#3-barebonex509trustmanagercannotbypassext)
    - [Checking by Tools](#checking-by-tools-2)
  - [4. X509ExtendedTrustManager](#4-x509extendedtrustmanager)
    - [Checking by Tools](#checking-by-tools-3)
  - [5. BareBone_X509TrustManagerCanBypassExt](#5-barebonex509trustmanagercanbypassext)
    - [Checking by Tools](#checking-by-tools-4)
  - [6. ITrustManagerUser](#6-itrustmanageruser)
    - [Checking by Tools](#checking-by-tools-5)
  - [7. BareBone_x509TrustManagerCanNotBypass](#7-barebonex509trustmanagercannotbypass)
    - [Checking by Tools](#checking-by-tools-6)
  - [8. BareboneTrustManagerConditional](#8-barebonetrustmanagerconditional)
    - [Checking by Tools](#checking-by-tools-7)
- [Existential Proofs](#existential-proofs)

# Icon indicators

&checkmark; means a tool successfully detected an issue.

&cross; means a tool was unable to detect the said issue, even though it may have detected some other issues.

# Issues Summary

TrustManager should never do something like this

```java
public X509Certificate[] getAcceptedIssuers() {
    // TODO Auto-generated method stub
    return null;
}
```

or this

```java
@Override
public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {  // Noncompliant, nothing means trust any client
}

@Override
public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException { // Noncompliant, this method never throws exception, it means trust any client
    LOG.log(Level.SEVERE, ERROR_MESSAGE);
}
```

Because neither of the methods are throwing an Exception when they are supposed to check whether the client/server are to be trusted and then throw an exception accordingly.


## 1. BadSSL_Naive 

A working example that bypasses bad certificate by doing this is shown in [BadSSL_Naive.java](1BadSSL_Naive/BadSSL_Naive.java) file.

### Checking by Tools

-  [CryptoGuard](1BadSSL_Naive/cg_BadSSL_Naive.log) &checkmark;
-  [CrySL](1BadSSL_Naive/crysl.md) &cross;


## 2. BareBone_X509TrustManager

A barebone version that does just this is shown in [BareBone_X509TrustManager](2BareBone_X509TrustManager/BareBone_X509TrustManager.java).

### Checking by Tools

- [CryptoGuard](2BareBone_X509TrustManager/cg_BareBone_X509TrustManager.log) &checkmark;
- [CrySL](2BareBone_X509TrustManager/crysl.md) &cross;

## 3. BareBone_X509TrustManagerCanNotBypassExt

A developer can implement upon X509TrustManager as shown in [BareBone_X509TrustManagerCanNotBypassExt](3BareBone_X509TrustManagerCanNotBypass/BareBone_X509TrustManagerCanNotBypassExt.java) which is used in [BareBone_X509TrustManagerCanNotBypass](3BareBone_X509TrustManagerCanNotBypass/BareBone_X509TrustManagerCanNotBypass.java) .

### Checking by Tools

- [CryptoGuard](3BareBone_X509TrustManagerCanNotBypass/cg_BareBone_X509TrustManagerCanNotBypass.log) &checkmark;
- [CrySL](3BareBone_X509TrustManagerCanNotBypass/crysl.md) &cross;

## 4. X509ExtendedTrustManager

However, `X509ExtendedTrustManager` is an abstract class which implements `X509TrustManager` interface. It can be used in exactly same way as [BareBone_X509TrustManager](2BareBone_X509TrustManager/BareBone_X509TrustManager.java), as shown in [BareBone_X509ExtendedTrustManagerCanBypass](4BareBone_X509ExtendedTrustManagerCanBypass/BareBone_X509ExtendedTrustManagerCanBypass.java) but this may not get detected.

### Checking by Tools

- [CryptoGuard](4BareBone_X509ExtendedTrustManagerCanBypass/cg_BareBone_X509ExtendedTrustManager.log) &cross;
- [CrySL](4BareBone_X509ExtendedTrustManagerCanBypass/crysl.md) &cross;

## 5. BareBone_X509TrustManagerCanBypassExt

Moreover, if the X509TrustManager is implemented in an abstract class [BareBone_X509TrustManagerCanBypassExt](5BareBone_X509TrustManagerCanBypass/BareBone_X509TrustManagerCanBypassExt.java), which is then used to instantiate an object in [BareBone_X509TrustManagerCanBypass](5BareBone_X509TrustManagerCanBypass/BareBone_X509TrustManagerCanBypass.java), this may not get caught either.

To *highlight* the difference between `BareBone_X509TrustManagerCanBypassExt` and `BareBone_X509TrustManagerCanNotBypassExt`, I'd like to draw your attention to the following diff:

```diff
diff -u BareBone_X509TrustManagerCanNotBypassExt.java BareBone_X509TrustManagerCanBypassExt.java 
--- BareBone_X509TrustManagerCanNotBypassExt.java       2020-02-13 23:30:51.000000000 -0500
+++ BareBone_X509TrustManagerCanBypassExt.java  2020-02-13 23:53:52.000000000 -0500
@@ -1,27 +1,4 @@
-import java.security.cert.CertificateException;
-import java.security.cert.X509Certificate;
-
 import javax.net.ssl.X509TrustManager;
 
-public class BareBone_X509TrustManagerCanNotBypassExt implements X509TrustManager {
-
-    @Override
-    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
-        // TODO Auto-generated method stub
-
-    }
-
-    @Override
-    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
-        // TODO Auto-generated method stub
-
-    }
-
-    @Override
-    public X509Certificate[] getAcceptedIssuers() {
-        // TODO Auto-generated method stub
-        return null;
-    }
-
-
+public abstract class BareBone_X509TrustManagerCanBypassExt implements X509TrustManager {
 }
```

### Checking by Tools

-  [CryptoGuard](5BareBone_X509TrustManagerCanBypass/cg_BareBone_X509TrustManagerCanBypass.log) &cross;
-  [CrySL](5BareBone_X509TrustManagerCanBypass/crysl.md) &cross;

You can generate the minimal jars by running the `makejar.sh` file.

## 6. ITrustManagerUser

[ITrustManager](6ITrustManagerUser/ITrustManager.java) is an interface that extends X509TrustManager, and then is used in [ITrustmanagerUser](6ITrustManagerUser/ITrustManagerUser.java).

### Checking by Tools

- [CryptoGuard](6ITrustManagerUser/cg_ITrustManagerUser.log) &checkmark;
- [CrySL](6ITrustManagerUser/crysl.md) &cross;

## 7. BareBone_x509TrustManagerCanNotBypass

[BareBone_X509TrustManagerCanNotBypassExt](7BareBone_x509TrustManagerCanNotBypass/BareBone_X509TrustManagerCanNotBypassExt.java) is an abstract that extends X509TrustManager with implementations of abstract methods, and then is used in [BareBone_X509TrustManagerCanNotBypass](7BareBone_x509TrustManagerCanNotBypass/BareBone_X509TrustManagerCanNotBypass.java).

### Checking by Tools

- [CryptoGuard](7BareBone_x509TrustManagerCanNotBypass/7cg_BareBone_X509TrustManagerCanBypass.log) &checkmark;
- [CrySL](7BareBone_x509TrustManagerCanNotBypass/crysl.md) &cross;

## 8. BareboneTrustManagerConditional

[8BareboneTrustManagerConditional/BareboneTrustManagerConditional.java](8BareboneTrustManagerConditional/BareboneTrustManagerConditional.java) is a barebone class that creates an anonymous inner class object of X509TrustManager. It then creates conditions which are insufficient for checking server/client trust.

### Checking by Tools

- [CryptoGuard](8BareboneTrustManagerConditional/cg_BareboneTrustManagerConditional.md) &cross;
- [CrySL](8BareboneTrustManagerConditional/crysl.md) &cross;

# Existential Proofs

- [Abstract class that extends on X509Extended509TrustManager](https://github.com/jecstarinnovations/etm/blob/ed73bdbcaf3c5e878fb8e8a928ad411cfcd66908/etm-server-core/src/main/java/com/jecstar/etm/server/core/tls/AbstractConfigurableTrustManager.java)
- abstract class that extends on X509TrustManager [[1]](https://github.com/mlundblad/bc-java/blob/master/tls/src/main/java/org/bouncycastle/jsse/BCX509ExtendedTrustManager.java), [[2]](https://github.com/liveqmock/ui5-idea/blob/6d4909b0ca7655e8ccdc2c0a9794d46eb3139baa/platform/platform-api/src/com/intellij/util/net/ssl/ClientOnlyTrustManager.java)
- X509TrustManager implementations which do not check server certificate properly and throws exceptions only in very rare cases [[1]](https://android.googlesource.com/platform/dalvik.git/+/cc05ad238516f1303687aba4a978e24e57c0c07a/libcore/support/src/test/java/org/apache/harmony/xnet/tests/support/X509TrustManagerImpl.java), [[2]](https://stackoverflow.com/questions/35545126/an-unsafe-implementation-of-the-interface-x509trustmanager-from-google)
