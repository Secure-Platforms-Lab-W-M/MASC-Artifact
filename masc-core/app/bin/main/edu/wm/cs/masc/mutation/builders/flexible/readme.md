# Rule

Will contain the misuse code generation approaches

## AnonymousObjectBuilderAllInOne

Inputs are

```String abstractClassNameWithPackage=javax.net.ssl.HostnameVerifier,
String classNameForMain=BareBone,
boolean returnValueBoolean=true;
```

Output will be:

```java
public class BareBone {
  public static javax.net.ssl.HostnameVerifier getHostnameVerifier() {
    return new javax.net.ssl.HostnameVerifier(){
    @Override
    public boolean verify(java.lang.String arg0, javax.net.ssl.SSLSession arg1) {
    	return true;
    }
    };
  }

  public static void main(java.lang.String[] args) {
    java.lang.System.out.println("Hello");
    getHostnameVerifier();
  }
}

```

## EmptyAbstractClassImplementingAbstractType

Two possible builders:

- InterfaceImplementationBuilder
- SubClassBuilder

Both accepts same input parameters. Example values are:

```java
String emptyAbstractClassName = "BareBone_X509TrustManagerCanBypassExt",
// for InterfaceImplementationBuilder
String interfaceWithPackageName = "javax.net.ssl.X509TrustManager",
// or for SubClassBuilder
String superClassWithPackageName = "javax.net.ssl.X509ExtendedTrustManager",
String mainClassName = "BareBone_X509TrustManagerCanBypass"
```

This will return a two cell TypeSpec. The first typeSpec will contain an abstract class that implements/extends the super type. The output in this case will be

```java
public abstract class BareBone_X509TrustManagerCanBypassExt implements javax.net.ssl.X509TrustManager {
}
```

or,

```java
public abstract class BareBone_X509TrustManagerCanBypassExt extends javax.net.ssl.X509ExtendedTrustManager {
}
```

The second TypeSpec will contain a Class that creates a method which returns an anonymous inner object based on the new abstract class. In this case, the sample outputs will be: 

```java
public class BareBone_X509TrustManagerCanBypass {
  public static javax.net.ssl.X509TrustManager getBareBone_X509TrustManagerCanBypassExt() {
    return new BareBone_X509TrustManagerCanBypassExt(){
    @Override
    public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, java.lang.String arg1) throws java.security.cert.CertificateException {
    }
    @Override
    public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, java.lang.String arg1) throws java.security.cert.CertificateException {
    }
    @Override
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
      return null;
    }
    };
  }

  public static void main(java.lang.String[] args) {
    java.lang.System.out.println("Hello");
    getBareBone_X509TrustManagerCanBypassExt();
  }
}
```

or

```java

public class BareBone_X509TrustManagerCanBypass {
  public static javax.net.ssl.X509ExtendedTrustManager getBareBone_X509TrustManagerCanBypassExt() {
    return new BareBone_X509TrustManagerCanBypassExt(){
    @Override
    public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, java.lang.String arg1, javax.net.ssl.SSLEngine arg2) throws java.security.cert.CertificateException {
    }
    @Override
    public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, java.lang.String arg1, java.net.Socket arg2) throws java.security.cert.CertificateException {
    }
    @Override
    public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, java.lang.String arg1, javax.net.ssl.SSLEngine arg2) throws java.security.cert.CertificateException {
    }
    @Override
    public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, java.lang.String arg1, java.net.Socket arg2) throws java.security.cert.CertificateException {
    }
    @Override
    public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, java.lang.String arg1) throws java.security.cert.CertificateException {
    }
    @Override
    public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, java.lang.String arg1) throws java.security.cert.CertificateException {
    }
    @Override
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
    	return null;
    }
    };
  }

  public static void main(java.lang.String[] args) {
    java.lang.System.out.println("Hello");
    getBareBone_X509TrustManagerCanBypassExt();
  }
}
```

## AbstractClassImplementingAbstractType

This is almost like `EmptyAbstractClassImplementingAbstractType`, but instead of creating an empty abstract class, this creates an abstract class containing the overriding methods of the supertype.
There are two possible builders,

- InterfaceImplementationBuilder, and
- SubClassBuilder

Both accepts the following parameters:

```java
/**
* @param abstractClassName name of the abstract class to be created
* @param superClassWithPackageName abstract class with package name which will be extended
* @param mainClassName name of the main class where anonymous inner class object will be created from the abstract className
* @param valueForBooleanReturnInOverridenMethods boolean return value to be used for overriding methods which return a boolean value
* @return The TypeSpec[] object containing both TypeSpecs.
* @throws ClassNotFoundException in case the interface is not found
*/
```

Therefore, for something like this

```java
typeSpecs = AbstractClassImplementingAbstractType.SubClassBuilder(
        "BareBone_X509TrustManagerCanBypassExt",
        "javax.net.ssl.HostnameVerifier",
        "BareBone_X509TrustManagerCanBypass",
        false
);

for(TypeSpec spec: typeSpecs){
    System.out.println(spec.toString());
}
```

The outputs will be

```java
public abstract class BareBone_X509TrustManagerCanBypassExt extends javax.net.ssl.HostnameVerifier {
  @java.lang.Override
  public boolean verify(java.lang.String arg0, javax.net.ssl.SSLSession arg1) {
    return false;
  }
}

```

and

```java
public class BareBone_X509TrustManagerCanBypass {
  public static javax.net.ssl.HostnameVerifier getBareBone_X509TrustManagerCanBypassExt() {
    return new BareBone_X509TrustManagerCanBypassExt(){
    };
  }

  public static void main(java.lang.String[] args) {
    java.lang.System.out.println("Hello");
    getBareBone_X509TrustManagerCanBypassExt();
  }
}

```