package edu.wm.cs.masc.mutation.reflection;

import edu.wm.cs.masc.utils.config.PropertiesReader;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.io.FilenameUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class ClassReflection {
    private ArrayList<String> interfaces;
    private ArrayList<String> classes;
    private ArrayList<String> abstractClasses;

    public ArrayList<String> getInterfaces() {
        return interfaces;
    }

    public ArrayList<String> getClasses() {
        return classes;
    }

    public ArrayList<String> getAbstractClasses() {
        return abstractClasses;
    }

    private final PropertiesReader configReader;
    private final String packageName;

    private final boolean properConfigFile = false;

    public static boolean isFinal(String classNameWithPackage) {
        String modifiers = getModifiers(classNameWithPackage);
        if (modifiers == null) throw new IllegalArgumentException("Invalid: " + classNameWithPackage);
        return modifiers.contains("final");
    }

    public static String getClassNameWithoutPackage(String classNameWithPackage) throws ClassNotFoundException {
        Class<?> c;
        c = Class.forName(classNameWithPackage);
        return c.getSimpleName();
    }


    public static String getPackageName(String classNameWithPackage) throws ClassNotFoundException {
        Class<?> c;
        c = Class.forName(classNameWithPackage);
        return c.getPackage().getName();
    }

    private boolean isProperPropertiesFile() {
        Iterator<String> it = configReader.getKeys();
        boolean has_interfaces = false;
        boolean has_classes = false;

        while (it.hasNext()) {
            String current_value = it.next();
            if (current_value.compareToIgnoreCase("classes") == 0) has_classes = true;
            if (current_value.compareToIgnoreCase("interfaces") == 0) has_interfaces = true;
        }

        return (has_interfaces && has_classes);
    }

    private void setInterfaces() {
        interfaces = new ArrayList<String>(Arrays.asList(configReader.getArrayValuesForAKey("interfaces")));
        Iterator<String> it = interfaces.iterator();
        ArrayList<String> temp_interfaces = new ArrayList<String>();
        while (it.hasNext()) {
            temp_interfaces.add(packageName + "." + it.next());
        }
        interfaces = temp_interfaces;
    }

    private void setClasses() {
        Iterator<String> current_classes = Arrays.stream(configReader.getArrayValuesForAKey("classes")).iterator();

        while (current_classes.hasNext()) {
            String currentClass = current_classes.next();
            String classNameWithPackage = packageName + "." + currentClass;
            EnumClassType current_class_type = getType(classNameWithPackage);
            if (current_class_type == EnumClassType.Abstract) {
                abstractClasses.add(classNameWithPackage);
            } else if (current_class_type == EnumClassType.Class) {
                classes.add(classNameWithPackage);
            }
        }
    }

    public static String getModifiers(String classNameWithPackage) {
        Class<?> c;
        try {
            c = Class.forName(classNameWithPackage);
            return Modifier.toString(c.getModifiers());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static EnumClassType getType(String classNameWithPackage) {
        String modifiers = getModifiers(classNameWithPackage);
        if (modifiers == null) throw new IllegalArgumentException("Invalid " + classNameWithPackage);
        if (modifiers.contains(EnumClassType.Interface.toString().toLowerCase())) {
            return EnumClassType.Interface;
        } else if (modifiers.contains(EnumClassType.Abstract.toString().toLowerCase())) {
            return EnumClassType.Abstract;
        } else {
            return EnumClassType.Class;
        }
    }

    public static int getSmallestConstructorParameterSize(
            String classNameWithPackage
    ) throws ClassNotFoundException {
        Constructor[] constructors = Class
                .forName(classNameWithPackage).getDeclaredConstructors();

        //does not have constructor
        if (constructors.length == 0)
            return 0;

        //assumption: we start with assumption that a constructor will have at most 100 parameters
        int size = 100;

        for (Constructor c : constructors) {
            if (c.getParameterCount() < size)
                size = c.getParameterCount();
        }
        return size;

    }

    public static boolean doesClassRequireConstructor(
            String abstractClassNameWithPackage)
            throws ClassNotFoundException {
        return getSmallestConstructorParameterSize(
                abstractClassNameWithPackage) > 0;
    }

    public static Constructor<?> getSmallestConstructorWithParameter(
            String classNameWithPackage
    ) throws ClassNotFoundException {
        Constructor<?>[] constructors = Class
                .forName(classNameWithPackage).getDeclaredConstructors();

        //does not have constructor
        if (constructors.length == 0)
            return null;

        Constructor<?> returnConstructor = null;
        //assumption: we start with assumption that a constructor will have at most 100 parameters
        int size = 100;

        for (Constructor c : constructors) {
            if (c.getParameterCount() < size) {
                size = c.getParameterCount();
                returnConstructor = c;
            }
        }
        return returnConstructor;

    }

    private void setValues() {
        interfaces = new ArrayList<String>();
        classes = new ArrayList<String>();
        abstractClasses = new ArrayList<String>();

        setInterfaces();
        setClasses();

//        System.out.println(interfaces);
//        System.out.println(abstractClasses);
//        System.out.println(classes);

    }

    public ClassReflection(String propertiesFilePath) throws ConfigurationException {
        configReader = new PropertiesReader(propertiesFilePath);
        if (!isProperPropertiesFile())
            throw new IllegalArgumentException("Invalid format in file: " + propertiesFilePath);
        packageName = FilenameUtils.getBaseName(propertiesFilePath);
        System.out.println(packageName);
        setValues();

    }


}
