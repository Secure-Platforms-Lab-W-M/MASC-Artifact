# Source code of MASC Prototype

MASC Prototype's source code consists of three modules.

- MASC's core `masc-core` contains builders and generators that work with crypto APIs and mutation operators to generate API use instances (mutants).
It relies on JavaPoet library and Reflection API to extract properties from a given crypto API, uses the values specified by user, and creates crypto API use instances. Depending on the values specified, the instances will be misuse. It can also be used to create barebone mutants.

- MDroidPlus was extended to create the selective scope of MASC. select crypto API specific locations. It then "injects" mutants. These mutants are from masc-core.

- Muse was extended to create the exhaustive scope of MASC, while considering additional considerations in terms of crypto APIs and locations.


## Running MASC modules

Running MASC and its modules is straightforward.

We did our best effort to document the source code and follow proper coding conventions, which should make this easy to navigate through the source code.

## Running MASC modules

We share sample runtime arguments we used for running MASC modules.

### Running MASC Core

```sh
MASCBarebone <propertiesfile.properties path>
## contents of a properties file
type = StringOperator
outputDir = /Users/XXX/workspaces/mutationbackyard/reproduce
apiName = javax.crypto.Cipher
invocation = getInstance
secureParam = AES/GCM/NoPadding
insecureParam = AES
noise = ~
variableName = cryptoVariable
className = CryptoTest
propertyName = propertyName
```

### for MDroidPlus Extension

```sh
/Users/XXX/git/XXX/MDroidPlus/libs4ast/ /Users/XXX/workspaces/mutationbackyard/sources/car-report car-report /Users/XXX/workspaces/mutationbackyard/mutations/ /Users/XXX/workspaces/Android/operator/ false
### contents of operator.properties inside operator dir
601 =	edu.wm.cs.mplus.operators.crypto.CipherInstance
602 = 	edu.wm.cs.mplus.operators.crypto.RandomInt
603 =   edu.wm.cs.mplus.operators.crypto.IvParameterSpec
604 =   edu.wm.cs.mplus.operators.crypto.SSLContextInstance
605 =   edu.wm.cs.mplus.operators.crypto.MessageDigest
606 =   edu.wm.cs.mplus.operators.crypto.HostnameVerifierInstance
607 =   edu.wm.cs.mplus.operators.crypto.HttpsURLHostnameVerifier
608 =   edu.wm.cs.mplus.operators.crypto.TrustManagerInstance
```

### for mSE Extension

```sh
# receives runtime argument

/Users/XXX/git/XXX/output/templates/ActivityLauncherreach.properties
## contents of properties file
lib4ast: /Users/XXX/git/MDroidPlus/libs4ast
appSrc: /Users/XXX/git/XXX/activitylauncher-reset
operatorType: REACHABILITY

//REQUIRED FOR MUTATE
appName: ActivityLauncher
output: /Users/XXX/git/XXX/output/ActivityLauncher/reach/
```
