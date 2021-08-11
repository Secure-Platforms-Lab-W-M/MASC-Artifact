```
Analyzing JAR: /media/data/muse2/muse2repo_master/minimal/IV//baseCase//example.jar
=======================================
***Violated Rule 10: Found constant IV in code ***Constants: ["Hello"]
[UnitContainer{unit=$r3 = "Hello", method='<StaticIV: void main(java.lang.String[])>'}]
=======================================
=======================================
***Violated Rule 10a: Found constant IV in code ***Constants: [0]
[UnitContainer{unit=$r3 = staticinvoke <java.lang.StringCoding: byte[] encode(char[],int,int)>($r2, 0, $i0), method='<java.lang.String: byte[] getBytes()>'}]
=======================================
Total Heuristics: 1
Total Orthogonal: 0
Total Constants: 1
Total Slices: 1
Average Length: 3.0
Depth: 1, Count 1
```
