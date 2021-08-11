# Cognicrypt

```log
Analyzing JAR: /Users/amitseal/git/muse2repo/minimal/RandomNumber/StaticSeedSecureRandom/basecase/example.jar
=======================================
***Violated Rule 11: Found predictable seeds in code ***Constants: ["Seed"]
[UnitContainer{unit=$r5 = "Seed", method='<SecureRand: void main(java.lang.String[])>'}]
=======================================
=======================================
***Violated Rule 11a: Found predictable seeds in code ***Constants: [0]
[UnitContainer{unit=$r4 = staticinvoke <java.lang.StringCoding: byte[] encode(java.nio.charset.Charset,char[],int,int)>(r1, $r3, 0, $i0), method='<java.lang.String: byte[] getBytes(java.nio.charset.Charset)>'}]
=======================================
Total Heuristics: 4
Total Orthogonal: 0
Total Constants: 4
Total Slices: 4
Average Length: 3.0
Depth: 1, Count 4
```
