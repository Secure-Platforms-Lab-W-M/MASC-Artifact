```
Analyzing JAR: /media/data/muse2/muse2repo_master/minimal/IV//complexCase//example.jar
=======================================
***Violated Rule 10a: Found constant IV in code ***Constants: ["", 65, 1, 0]
[UnitContainer{unit=r12 = "", method='<ComplexStaticIV: void main(java.lang.String[])>'}, UnitContainer{unit=i1 = 65, method='<ComplexStaticIV: void main(java.lang.String[])>'}, UnitContainer{unit=i1 = i1 + 1, method='<ComplexStaticIV: void main(java.lang.String[])>'}, UnitContainer{unit=$r3 = staticinvoke <java.lang.StringCoding: byte[] encode(char[],int,int)>($r2, 0, $i0), method='<java.lang.String: byte[] getBytes()>'}]
=======================================
Total Heuristics: 7
Total Orthogonal: 0
Total Constants: 0
Total Slices: 1
Average Length: 11.0
Depth: 1, Count 7
```
