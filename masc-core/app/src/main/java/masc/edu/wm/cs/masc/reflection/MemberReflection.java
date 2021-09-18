package masc.edu.wm.cs.masc.reflection;

import com.squareup.javapoet.TypeName;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * MemberReflection class is a utility class based on Reflection related to class members
 */
public class MemberReflection {

    //    private String className;
    private Method[] allMethods;
    private Method[] eligibleMethods;

//
//    public ArrayList<MemberContainer> getMethodSignatures() {
//        return methodSignaturesWhichCanBeExtended;
//    }

//    public ArrayList<String> getConstructors(String classNameWithPackage) {
//        return constructors;
//    }

    /**
     * creates a MemberReflection Object that contains methods and constructors
     *
     * @param classNameWithPackage
     * @throws ClassNotFoundException
     */
    public MemberReflection(String classNameWithPackage) throws ClassNotFoundException {
        //    ArrayList<MemberContainer> methodSignaturesWhichCanBeExtended;
        //    ArrayList<String> constructors;
        Class<?> c;
        c = Class.forName(classNameWithPackage);
        this.allMethods = c.getMethods();
        List<Method> methods = Arrays.asList(this.allMethods);
        // this is done to make sure that the contents of allMethods are always in same order.
        // hopefully.
        methods.sort(Comparator.comparing(Method::toString));

        this.allMethods = methods.toArray(new Method[0]);
        this.eligibleMethods = new Method[]{};
    }


    public static String getGeneratedMethod(Method method, boolean returnValueBoolean) {
        String returnType = method.getReturnType().getName();
        String formattedReturnType = "";
        boolean returnTypeIsVoid = returnType.compareToIgnoreCase("void") == 0;

        //define return type based on method's return type
        if (method.getReturnType().equals(void.class)) {
            formattedReturnType = "void ";
        } else {
            formattedReturnType = getNonVoidReturnType(returnType);
        }

        String methodSignature = "public " + formattedReturnType + method.getName() + "(";
        methodSignature += getGeneratedArguments(method);

        methodSignature += ")";
        // throw exceptions if any
        methodSignature += getThrowsGeneratedCode(method);


        methodSignature += " {\n";

        if (!returnTypeIsVoid) {
            if (returnType.compareTo("boolean") == 0) {
                methodSignature += "\treturn " + returnValueBoolean + ";\n";
            } else {
                methodSignature += "\treturn null;\n";
            }
        }

        methodSignature += "}\n";

        return methodSignature;
    }


    public static String getDummyParametersForSuper(Constructor<?> constructor, boolean booleanParameter) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        String parameters = "";
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> currentParameterType = parameterTypes[i];

            //if the parameter type is boolean
            if (currentParameterType == boolean.class) {
                parameters += booleanParameter;
            } else {
                parameters += "null";
            }
            if (i < parameterTypes.length - 1)
                parameters += ", ";
        }
        return parameters;
    }

    public static String getStringForDummyParameters(Class<?>[] parameterTypes, boolean booleanParameter) {
        String dummyParameters = "";
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> currentParameterType = parameterTypes[i];
            //if the parameter type is boolean
            if (currentParameterType == boolean.class) {
                dummyParameters += booleanParameter;
            } else {
                dummyParameters += "null";
            }
//            callToSuperStatement += currentParameterType.getName() + " arg"+i;
            if (i < parameterTypes.length - 1)
                dummyParameters += ", ";
        }
        return dummyParameters;
    }

    private static String getGeneratedArguments(Method method) {
        String generatedArguments = "";
        for (int i = 0; i < method.getParameterTypes().length; i++) {
            generatedArguments += method.getParameters()[i];
            //add , after adding each argument
            if (i != (method.getParameterCount() - 1))
                generatedArguments += ", ";
        }
        return generatedArguments;
    }


    public static boolean hasThrows(Method method) {
        return method.getExceptionTypes().length != 0;
    }

    public static String bogusConditionForType(TypeName type){
        if(type.isPrimitive()){
            return " == 0";
        }else{
            return " == null";
        }
    }

    //is used to add a throws declaration after method signature
    private static String getThrowsGeneratedCode(Method method) {
        String throwsExceptions = "";
        if (method.getExceptionTypes().length != 0) {
            throwsExceptions = " throws ";
            int countExceptions = method.getExceptionTypes().length;
            int currentIndex = 0;
            for (Class<?> c : method.getExceptionTypes()) {

                throwsExceptions += c.getName();
                currentIndex += 1;
                if (currentIndex < countExceptions) throwsExceptions += ", ";
            }
        }
        return throwsExceptions;
    }

    /**
     * is used to convert Literal array return type to code generation friendly return type.
     * e.g. [LX509TrustManager becomes X509TrustManager[]
     *
     * @param returnType
     * @return
     */
    private static String getNonVoidReturnType(String returnType) {
        String formattedReturnType = "";
        int returnArrayDimension = 0;
        //replace [L if any, increment array dimension counter
        if (returnType.contains("[L")) {
            returnArrayDimension += 1;
            returnType = returnType.replace("[L", "");
        }

        //count any remaining [
        returnArrayDimension += StringUtils
                .countMatches(
                        returnType, "[");

        //repeat array dimension accordingly
        String dimensionArray = StringUtils.repeat("[]", returnArrayDimension);

        formattedReturnType += returnType + dimensionArray + " ";
        //remove semicolon
        formattedReturnType = formattedReturnType.replace(";", "");
        return formattedReturnType;
    }

    public Method[] getAllMethods() {
        return allMethods;
    }

    public Method[] getEligibleMethods() {
        ArrayList<Method> eligible = new ArrayList<Method>();
        for (Method method : allMethods) {
            //we ignore final methods
//            if (method.toString().contains("final")) continue;
            if (Modifier.isFinal(method.getModifiers())) continue;
//            and concrete methods as well
//            if (!method.toString().contains("abstract")) continue;
            if (!Modifier.isAbstract(method.getModifiers())) continue;
            eligible.add(method);
        }
        this.eligibleMethods = eligible.toArray(this.eligibleMethods);
        return this.eligibleMethods;
    }

    public static String getMethodSimpleName(Method method) {
        return method.getName();
    }

}
//        methodSignaturesWhichCanBeExtended = new ArrayList<MemberContainer>();
//        constructors = new ArrayList<String>();
//
//        /* Need both list of members and inherited members */
//        Method[] methods = c.getMethods();
//        String stringMethod = "";
//        Constructor[] temp_constructors = c.getConstructors();
//
//        for (Method method : methods) {
//
//            if (method != null)
//                stringMethod = method.toString();
//            // if the method is final, no need to override it or contain it
//            if (stringMethod.contains("final")) continue;
//            // if the method is not abstract, no need to override it or contain it
//            if (!stringMethod.contains("abstract")) continue;
//            System.out.println("declaring class: "+method.getDeclaringClass().getSimpleName());
//            System.out.println(method.getName() +" "+ method.getDeclaringClass());
//            MemberContainer mc = new MemberContainer(method.getName(), stringMethod, method.getDeclaringClass().getName());
//            methodSignaturesWhichCanBeExtended.add(mc);
//        }
//
//        String stringConstructor = "";
//        for (Constructor constructor : temp_constructors) {
//            if (constructor != null)
//                stringConstructor = constructor.toString();
//            constructors.add(stringConstructor);
//        }
//
//    }
//
//    /**
//     * formats parameters to add variable placeholders after each parameter type
//     * @param methodFullSignature
//     * @return
//     */
//    public static String formatParameters(String methodFullSignature) {
//
//        int paranthesisEndIndex = methodFullSignature.indexOf(')');
//        String prior = methodFullSignature.substring(0, paranthesisEndIndex);
//        String posterior = methodFullSignature.substring(paranthesisEndIndex, methodFullSignature.length());
//
//        int indexOfParenthesisStart = prior.indexOf('(');
//        String subStrAfterParenthesisStart = prior.substring(indexOfParenthesisStart);
//        if (subStrAfterParenthesisStart.length() == 1) return methodFullSignature;
//
//        String[] splits = prior.split(",");
//
//        StringBuilder temp = new StringBuilder();
//
//        for (int i = 0; i < splits.length - 1; i++) {
//            temp.append(splits[i]).append(" a").append(i).append(", ");
//        }
//
//        temp.append(splits[splits.length - 1]).append(" a").append(splits.length - 1);
//        return temp + posterior;
//    }
//
//    /**
//     * formats method signature by replacing abstract,
//     * and changes packageName.className.methodName to just methodName
//     *
//     * @param methodMemberContainer
//     * @param classNameWithPackage
//     * @return
//     */
//    public static MemberContainer getCodeGenerationFriendlyMethodSignature(
//            MemberContainer methodMemberContainer,
//            String classNameWithPackage) {
//
//        String method = methodMemberContainer.getMemberFullName();
//        String methodName = methodMemberContainer.getMemberName();
//
//        System.out.println(method+","+methodName);
//        if (method.contains("abstract")) {
//            method = method.replace("abstract ", "");
//        }
//        String methodNameWithPackage = classNameWithPackage + "." + methodName;
//        methodName = method.replace(methodNameWithPackage, methodName);
//        return new MemberContainer(method, methodName, "");
//    }
//}
