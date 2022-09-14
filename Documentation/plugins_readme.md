# Plugin architecture

## Running own plugins
MASC supports 6 types of operators - 5 predefined operator types plus one more for any custom operator type that does not fall within these five. You can write your own operators for each of the 6 types.

To write your own operators as plugins, these are the general steps you will need to follow - 

1. Write the code in a .java file 
2. Compile the code to get the .class file 
3. Place the .class file in /plugins/
3. Run the jar normally
4. Find the generated mutated apps in /app/outputs

Let us see each of these steps in detail.

# 0. The operator types

Running the jar requires normally requires a properties file to be supplied as command line argument. The properties file specifies the type of operator to be run like this - 
> `type = StringOperator`

StringOperator is just one of the total 6 operator types. The other five are - IntOperator, ByteOperator, Interproc, Flexible, and Custom.

# 1. Writing the code for a plugin
The general structure for the code of a custom plugin looks like this - 

1. package plugins
2. Two mandatory import statements
3. Class definition extending/inheriting a specific Abstraction
4. One and only one specific constructor
5. Overriding  `mutation()` method - 


**About the imports -** Apart from the two mandatory imports, the class can have any other import statement as required by the user. 
**About the inheritance -** The plugin can be a part of a more complex inheritance tree if the user so wishes. It is fine as long as the plugin is a sub class of the specified Abstraction, even if it is deep down the inheritance tree from the abstraction.  

The import statements, the Abstraction and the constructor is different for each operator type. Let us see what they are - 

## **1.1 String operator**

**Import statements -**
``` 
import edu.wm.cs.masc.mutation.operators.restrictive.stringoperator.AStringOperator;
import edu.wm.cs.masc.mutation.properties.StringOperatorProperties;
```
**Abstract class to extend -** `AStringOperator`

**Constructor -**
```
public ClassName(StringOperatorProperties properties) {
        super(properties);
}
```



## **1.2 Int operator**

**Import statements -**
``` 
import edu.wm.cs.masc.mutation.operators.restrictive.intoperator.AIntOperator;
import edu.wm.cs.masc.mutation.properties.IntOperatorProperties;
```
**Abstract class to extend -** `AIntOperator`

**Constructor -**
```
public ClassName(IntOperatorProperties p) {
        super(p);
}
```



## **1.3 Byte operator**

**Import statements -**
``` 
import edu.wm.cs.masc.mutation.operators.restrictive.byteoperator.AByteOperator;
import edu.wm.cs.masc.mutation.properties.ByteOperatorProperties;
```
**Abstract class to extend -** `AByteOperator`

**Constructor -**
```
public ClassName(ByteOperatorProperties p) {
        super(p);
}
```



## **1.4 Interprocedural operator**

**Import statements -**
``` 
import edu.wm.cs.masc.mutation.operators.restrictive.interprocoperator.AInterProcOperator;
import edu.wm.cs.masc.mutation.properties.InterprocProperties;
```
**Abstract class to extend -** `AInterProcOperator`

**Constructor -**
```
public ClassName(InterprocProperties p) {
        super(p);
}
```



## **1.5 Flexible operator**

**Import statements -**
``` 
import edu.wm.cs.masc.mutation.operators.flexible.AFlexibleOperator;
import edu.wm.cs.masc.mutation.properties.FlexibleOperatorProperties;
```
**Abstract class to extend -** ` AFlexibleOperator `

**Constructor -**
```
public ClassName(FlexibleOperatorProperties p) {
        super(p);
}
```



## **1.6 Custom operator**

**Import statements -**
``` 
import edu.wm.cs.masc.mutation.operators.custom.ACustomGenericOperator;
import edu.wm.cs.masc.mutation.properties.CustomOperatorProperties;
```
**Abstract class to extend -** ` ACustomGenericOperator `

**Constructor -**
```
public ClassName(CustomOperatorProperties p) {
        super(p);
}
```

## **Sample code**
```
package plugins;

import edu.wm.cs.masc.mutation.operators.restrictive.stringoperator.AStringOperator;
import edu.wm.cs.masc.mutation.properties.StringOperatorProperties;

public class MyStrOperatorPlugin extends AStringOperator {

    public MyStrOperatorPlugin(StringOperatorProperties properties) {
        super(properties);
    }

    @Override
    public String mutation() {
        return "Cipher.getInstance("AES")";
    }
}
```
# **1.7 About the `mutation()` method**
Whatever this method returns in the mutation that will be injected in the output mutated apps. In most cases, the return value might not be as simple as the one shown in the example code above. It is very common, recommended even, to be fetching values from properties file. That is the purpose of the properties file, after all. The code of an operator should not be tied to the details of a specific API. Instead, the operator should make use of the values as provided by the properties file so that the operator can be used for virtually any API just by changing the values of the properties file. The structure for properties file for the 5 operator types (except custom type) is fixed, and the values are already available and ready to use. So the `mutation()` function in the above sample should look like this:
```
@Override
    public String mutation() {
        StringBuilder s = new StringBuilder();
        s.append(api_name)
                .append(".")
                .append(invocation)
                .append("(\"")
                .append(insecureParam.toLowerCase())
                .append("\");");
        return s.toString();
    }
```

# **1.8 About `CustomGenericOperator`** 
Custom operator type are the operators that extend `CustomGenericOperator` abstract class. These operators have no fixed structure for the properties file. Hence no values are preloaded and all values can be manually loaded like this - 
`getAttribute("key")`

If a matching key is found in the properties file, then its corresponding value will be retrieved. Else if no matching keys are found, the user will be asked to input its value once only. The input will be saved and the values will be used for every subsequent query until the program ends. 


Values can be fetched from properties file using the operatorProperties as shown in the example below - 

# 2. Compiling the code
You code for plugins refer to classes already within MASC's source code. Hence it won't compile without reference to those classes. This can be easily solved by adding the MASC.jar to classpath as such. Open a command prompt in folder where your code is, and run this command: 
> ` javac -cp directory/MASC.jar *.java   `

# 3. Placing the class file in /plugins/ folder
Place the  `.class` files in /plugins/ folder. Alternatively, you can place your .java files here and compile them here so that the class files are generated here. MASC will ignore all files with extension other than `.class`. 

You can place as many plugins of different types in /plugins/. But only the plugins of the same type as specified in properties file will be run.


# 4. Running the jar
To run the jar, execute the command: 
> `java -jar MASC.jar propertiesFileName.properties`

# 5. The output
Generated mutated apps will be produces in `app/output/`
Output from plugins will have `plugins.` prefixed in their names.



