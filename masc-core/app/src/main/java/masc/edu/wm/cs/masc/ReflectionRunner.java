package masc.edu.wm.cs.masc;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ReflectionRunner {

    public static void main(String[] args) throws ClassNotFoundException {
//        ClassDecompiler de = new ClassDecompiler("")
        String reflection_properties_path = System.getProperty("user.dir")+"/src/main/resources/reflection/";
        String properties_file_name = "javax.net.ssl.properties";

//
//        try {
//            ClassReflection de = new ClassReflection(reflection_properties_path+properties_file_name);
//
//        } catch (ConfigurationException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            MemberReflection m = new MemberReflection("javax.net.ssl.X509TrustManager");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        Class<?> c = Class.forName("javax.net.ssl.HostnameVerifier");
        Method[] methods = c.getMethods();
        Method m = methods[0];
        System.out.println(m.getParameterCount());
        for (Parameter p: m.getParameters()){
            System.out.println(p.toString());

        }
//        try {
//        Class<?> c = Class.forName("javax.net.ssl.X509TrustManager");
//            Class<?> c = Class.forName("java.lang.String");
//            System.out.println(Modifier.toString(c.getModifiers()));
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

    }
}
