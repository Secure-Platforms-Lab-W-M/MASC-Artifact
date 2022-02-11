# MASC

## Concepts

The author of MASC (yours truly) created it as a research prototype, and invented terms on the fly.
Some of these terms did not make sense later, and were dropped. Some of these still made somewhat sense, and survived.
Therefore, in this readme - I will be describing the high level concepts each of these terms represent, and then will describe how they interact with each other.

### Layers

Layers are the abstraction at where MASC introduces mutation / makes mutation instances. There are three layers,

    - MainScope Layer, where a minimal code / example is created that contains mutation, or misused Crypto API. Was previously BareBone Layer.
    - Exhaustive Layer, where a simple misuse instance is introduced through mutation at all possible reachable locations. This is done to verify that all reachable code locations are indeed scanned by analysis tools.
    - Similarity Layer, where we selectively mutate by finding existing crypto APIs usage. For example, if given input code has `Cipher.getInstance(...)` anywhere, it will selectively introduce misuse instances through mutation at location adjacent )i.e. below) that `Cipher.getInstance()` function call. Previously known as Selective Layer.

### Operators

Operators are responsible for the code snippet used as part of the mutation.
For example, it can mean something simple, such as creating the following code:

```java
Cipher.getInstance(<param>);
```

As this code will be inserted while mutating source code, or *creating mutation instance*.

It can also represent something partial, which will be responsible for mutation instance.
An example is introducing a call to a function (A) through mutation, where the function belongs to a class (B) that does not exist in the code in the first place. Or, it can be creating an anonymous inner class object (A) from a class (B) that is not part of the code!

However, To make the call real, we need to introduce the class (B) in the code base. That is achieved through `Makers` and `Builders`. An example for this type of operator is FlexibleOperator. It is only concerned about the call (A) and not the reference (B) the call is making. Therefore, it will create something like this

```java
new CryptoTestExt(){
    public boolean verify(java.lang.String arg0, javax.net.ssl.SSLSession arg1) {
    	return true;
    }
```

However, the `CryptoTestExt` is from a class that is not present, so trying to compile a code that contains the above snippet will inevitably fail.

A Builder, will build the necessary structures for those necessary references.

### Builders

As discussed above, builders take care of creating necessary structures that are required by operators.

An example is:

```java
public interface CryptoTestExt implements javax.net.ssl.HostnameVerifier{ }
```

The builder here is creating the interface CryptoTestExt. Of course, it was given the name of the interface, the name of the interface it is implementing, as well as the nature of inheritance.

### MutationMakers

MutationMakers are more part of the MainScope Layer. Maker makes workable, minimal applications that contains a particular Crypto API misuse instance. Therefore, it will be creating something like this while depending on both `Operator` and `Builder`:

```java
public class CryptoTest { --- (1)
  public static void main(java.lang.String[] args) throws java.lang.Exception {
    java.lang.System.out.println("Hello");

    new CryptoTestExt(){ -- (2)
    public boolean verify(java.lang.String arg0, javax.net.ssl.SSLSession arg1) {
    	return true;
    }
    };
  }
}
```
Where the body starting from (1) is created through `Maker`, but the anonymous inner class object (2) is actually coming from the `Operator`!

> To summarize, Operator defines the misuse instance, Builder creates the structure around Operator to make it usable/callable by other code snippets. Maker creates code segments that makes those calls in a MainScope setting.

The next segment is about how MASC can be extended for supporting more types of Operators/Builders/Makers through MainScope layer for fun and profit.

## Extending MainScope

### Create a type under OperatorType

First, you need to define a name that represents the operator type. For example, `StringDifferentCase` represents an operator that deals with a String parameter being given to a crytpo API in a different case from what people usually use. An example of misuse instance in this case would be passing `aes` instead of `AES`. However, Operators are generic in terms of mutation, so it will receive the input String and will create misuse instance based on that input String.

The following are the currently available operators in MASC. Each of these operators belong to one type of `RootOperatorType` conceptually.

Current operators are:

```java
public enum OperatorType {
    StringDifferentCase,
    StringNoiseReplace,
    StringSafeReplaceWithUnsafe,
    StringUnsafeReplaceWithUnsafe,
    StringStringCaseTransform,
    StringValueInVariable,
    ByteLoop,
    ByteCurrentTime,
    Interproc,
    AIOEmptyFromAbstractType,
    AIOEmptyAbstractClassExtendsAbstractClass,
    AIOEmptyAbstractClassImplementsInterface,
    AIOEmptyInterfaceExtendsInterface,
    AIOGenericAbstractClassExtendsAbstractClass,
    AIOGenericAbstractClassImplementsInterface,
    AIOGenericInterfaceExtendsInterface,
    AIOSpecificAbstractClassExtendsAbstractClass,
    AIOSpecificAbstractClassImplementsInterface,
    AIOSpecificInterfaceExtendsInterface,
}
```

All of these operators belong to one of the following RootOperatorTypes conceptually. The word conceptual is important: they are grouped together based on the concept they represent in the MASC setting.

```java
public enum RootOperatorType {
   StringOperator,
   ByteOperator,
   Interproc,
   Flexible
}
```

### Implementing each of the operators

After naming your Operator, you can start defining them. The following rules apply:

- The implemented operator name must follow the type name from `OperatorType`. It is a requirement for consistency.
- Each of the operators must implement the `IOperator` interface. An example is `InterProcOperator`.
    - They might also be under an abstract class implementing the interface, Examples are `AStringOperator` for all String based Operators, and `AByteOperator` for all byte based operators. Note that both `AStringOperator` and `AByteOperator` implements the `IOperator` interface.
    - The norm is to create fields used in the abstract class which can be used by the concrete operators, and then receive it from the related properties file. A concrete operator may not have to use all of those fields, but these are made available regardless.



### Creating Properties file
Since Operators rely on properties files for creating misuse instances (for example: the name of the crypto API to be used), create a `.properties` file first.

Mutations across different scopes require different fields to be specified.  The Main scope created novel files that have the mutation in a class' main method.   The Exhaustive scope places mutations at every distinct applicable location across all input files.  The Selective scope places mutations only where they are likely to occur in practice.  Here are explanations for the minimum required fields for each scope.

Currently the Exhaustive and Selective scopes only work for IntOperator, StringOperator, and ByteOperator mutation types.  Other operators can be made to work if needed, but that is outside the main goals of this project.

#### Main
```
outputDir = <Directory mutated files should be saved in>
scope = Main
type = <Type that is from RootOperatorType Enum>
api_name =  <API used in mutations>
className = <Name for generated classes>
appName = <Name of the app>
```

#### Exhaustive
```
lib4ast = <Path to MDroid source files>
appSrc = <Directory of files to apply mutations to>
outputDir = <Directory mutated files should be saved in>
scope = Exhaustive
type = <Type that is from RootOperatorType Enum>
api_name =  <API used in mutations>
className = <Name for generated classes>
appName = <Name of the app>
```

#### SELECTIVE
```
appSrc = <Directory of files to apply mutations to>
outputDir = <Directory mutated files should be saved in>
scope = Selective
type = <Type that is from RootOperatorType Enum>
api_name =  <API used in mutations>
className = <Name for generated classes>
appName = <Name of the app>
```

Some `.properties` files require additional fields.  Examples and explanations of the various different `.properties` files follow.

Note, if you extend MASC with a new mutation operator, you should also create a properties reader extending the `edu.wm.cs.masc.properties.AOperatorProperties`.
The `AOperatorProperties` already contains appropriate methods for the keys mentioned above. The extended class needs to do similarly using `reader.get` for the keys.

Also, any field values that represent variable names should end with `%d`.  MASC will replace the `%d` flag appropriately based on the chosen scope.  Leaving off the flag will result in mutated code that may not compile correctly.

## Sample Configurations

Cipher.properties

```properties
lib4ast = C:/Users/Trevor Stalnaker/Documents/GitHub/CSci435-Fall21-MASC/masc-core/app/src/main/java/masc/edu/wm/cs/masc/muse/mdroid
appSrc = C:/Users/Trevor Stalnaker/Desktop/input/
outputDir = C:/Users/Trevor Stalnaker/Desktop/output/
scope = EXHAUSTIVE
//EXHAUSTIVE, MAIN, SELECTIVE

appName = MuseTest

type = StringOperator
apiName = javax.crypto.Cipher
invocation = getInstance
secureParam = AES/GCM/NoPadding
insecureParam = AES
noise = ~
variableName = cryptoVariable%d
className = CryptoTest
propertyName = propertyName
```

ByteOperator.properties

```properties
lib4ast = C:/Users/Trevor Stalnaker/Documents/GitHub/CSci435-Fall21-MASC/masc-core/app/src/main/java/masc/edu/wm/cs/masc/muse/mdroid
appSrc = C:/Users/Trevor Stalnaker/Desktop/input/
outputDir = C:/Users/Trevor Stalnaker/Desktop/output/
scope = EXHAUSTIVE
//EXHAUSTIVE, MAIN, SELECTIVE

appName = MuseTest

type = ByteOperator
apiName = javax.crypto.spec.IvParameterSpec
className = CryptoTest
tempVariableName = cryptoTemp%d
apiVariable = ivSpec%d
```

IntOperator.Properties
```
lib4ast = C:/Users/Trevor Stalnaker/Documents/GitHub/CSci435-Fall21-MASC/masc-core/app/src/main/java/masc/edu/wm/cs/masc/muse/mdroid
appSrc = C:/Users/Trevor Stalnaker/Desktop/input/
outputDir = C:/Users/Trevor Stalnaker/Desktop/output/
scope = EXHAUSTIVE
//EXHAUSTIVE, MAIN, SELECTIVE

appName = MuseTest

type = IntOperator
misuse = PBE
variableName = iterCount%d
iterationCount = 50
className = CryptoTest

// For PBE misuse
apiName = javax.crypto.spec
invocation = PBEKeySpec
password = very_secure
salt = {80\,45\,56}

// For ShortKey misuse
algorithm = RSA
keyGenVarName = keyGen%d
```

HostnameVerifier.properties

```properties
   type = Flexible
   outputDir = /Users/amitseal/workspaces/mutationbackyard/reproduce
   apiName =  javax.net.ssl.HostnameVerifier
   className = CryptoTest
   booleanReturn = true
   otherClassName = CryptoTestExt
   booleanInCondition = true
```

InterprocCipher.properties

```properties
type = Interproc
outputDir = /Users/amitseal/workspaces/mutationbackyard/reproduce
apiName = javax.crypto.Cipher
invocation = getInstance
secureParam = AES/GCM/NoPadding
insecureParam = AES
noise = ~
variableName = cryptoVariable
className = CryptoTest
propertyName = propertyName
otherClassName = CipherPack
try_catch = False

```

InterprocCipherTryCatch.properties

```properties
type = Interproc
outputDir = /Users/amitseal/workspaces/mutationbackyard/reproduce
apiName = javax.crypto.Cipher
invocation = getInstance
secureParam = AES/GCM/NoPadding
insecureParam = AES
noise = ~
variableName = cryptoVariable
className = CryptoTest
propertyName = propertyName
otherClassName = CipherPack
try_catch = True
```


IVParameterSpec.properties
```properties
type = ByteOperator
outputDir = /Users/amitseal/workspaces/mutationbackyard/reproduce
apiName = javax.crypto.spec.IvParameterSpec
className = CryptoTest
tempVariableName = cryptoTemp
apiVariable = ivSpec

```

MessageDigest.properties

```properties
type = StringOperator
outputDir = /Users/amitseal/workspaces/mutationbackyard/reproduce
apiName =  java.security.MessageDigest
invocation = getInstance
secureParam = SHA-256
insecureParam = MD5
noise = ~
variableName = cryptoVariable
className = CryptoTest
propertyName = propVariable
```

X509TrustManager.properties

```properties
type = Flexible
outputDir = /Users/amitseal/workspaces/mutationbackyard/reproduce
apiName =  javax.net.ssl.X509TrustManager
className = CryptoTest
booleanReturn = true
booleanInCondition = true
otherClassName = CryptoTestExt
specificCondition = if (!(null != arg1 || arg1.equalsIgnoreCase("RSA") || arg0.length >= 314)) {
throw new java.security.cert.CertificateException("checkServerTrusted: AuthType is not RSA");
}
```


X509TrustManagerMulti.properties

```properties
type = Flexible
outputDir = /Users/amitseal/workspaces/mutationbackyard/reproduce
apiName =  javax.net.ssl.X509TrustManager
className = CryptoTest
booleanReturn = true
hasDependencies = true
```
