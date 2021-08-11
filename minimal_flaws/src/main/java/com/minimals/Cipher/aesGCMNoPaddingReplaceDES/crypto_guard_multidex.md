```
Analyzing JAR: /media/data/muse2/muse2repo_master/minimal/Cipher/aesGCMNoPaddingReplaceDES//example.jar
=======================================
***Violated Rule 1: Found broken crypto schemes ***Constants: ["DES"]
[UnitContainer{unit=$r3 = virtualinvoke $r2.<java.lang.String: java.lang.String replace(java.lang.CharSequence,java.lang.CharSequence)>("AES/GCM/NoPadding", "DES"), method='<CipherExample: void main(java.lang.String[])>'}]
=======================================
=======================================
***Violated Rule 1a: Found broken crypto schemes ***Constants: ["AES/GCM/NoPadding", "AES/GCM/NoPadding", 16]
=======================================
Total Heuristics: 1
Total Orthogonal: 0
Total Constants: 0
Total Slices: 1
Average Length: 3.0
Depth: 1, Count 1
```
