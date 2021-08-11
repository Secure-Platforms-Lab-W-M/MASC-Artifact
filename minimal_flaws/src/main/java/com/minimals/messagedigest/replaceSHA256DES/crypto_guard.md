```
Analyzing JAR: /media/data/muse2/muse2repo_master/minimal/messagedigest/replaceSHA256DES//example.jar
=======================================
***Violated Rule 2: Found broken hash functions ***Constants: ["MD5"]
[UnitContainer{unit=$r4 = virtualinvoke $r3.<java.lang.String: java.lang.String replace(java.lang.CharSequence,java.lang.CharSequence)>("SHA-256", "MD5"), method='<MessageDigestComplex: void main(java.lang.String[])>'}]
=======================================
=======================================
***Violated Rule 2a: Found broken hash functions ***Constants: ["SHA-256", 16, "SHA-256"]
=======================================
Total Heuristics: 1
Total Orthogonal: 0
Total Constants: 0
Total Slices: 1
Average Length: 3.0
Depth: 1, Count 1
```
