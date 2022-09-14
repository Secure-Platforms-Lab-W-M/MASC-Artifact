package edu.wm.cs.masc.plugins;

import edu.wm.cs.masc.mutation.operators.IOperator;
import edu.wm.cs.masc.mutation.properties.AOperatorProperties;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

public class PluginOperatorManager {
    private static final PluginOperatorManager SINGLE_INSTANCE = new PluginOperatorManager();
    private final ArrayList<Class> customOperators = new ArrayList<>();
    private final ArrayList<IOperator> operators = new ArrayList<>();

    public boolean isInProd() {
        String className = this.getClass().getName().replace('.', '/');
        String classJar =
                this.getClass().getResource("/" + className + ".class").toString();
        return classJar.startsWith("jar:");
    }

    private PluginOperatorManager() {
        String packageName = "plugins";
        String folderDir = "app/build/libs/";
        if(isInProd()) folderDir = "";
        File[] files = new File(folderDir + packageName).listFiles();
        File folder = new File(folderDir);


        if(files == null)
        {
            System.out.println("No custom plugins. Continuing...");
            return;
        }

        for(File file: files)
        {
            if(! file.getName().endsWith(".class"))
                continue;

            try {
                Class CustomOPClass = loadClass(folder, packageName, file.getName().split("[.]")[0]);

                if(isOperatorClass(CustomOPClass))
                {
                    customOperators.add(CustomOPClass);
                }
            } catch (ClassNotFoundException e) {
                System.out.println("Class not found: " + file.getName());
            }
        }
    }

    private Class loadClass(File folder, String packageName, String className) throws ClassNotFoundException {

        try {
            URL url = folder.toURI().toURL();
            URL[] urls = new URL[]{url};

            //load this folder into Class loader
            ClassLoader cl = new URLClassLoader(urls);

            //load the Address class in 'c:\\other_classes\\'
            Class  cls = cl.loadClass(packageName + "." + className);

            return cls;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;

    }

    private boolean isOperatorClass(Class customOPClass) {
        return IOperator.class.isAssignableFrom(customOPClass) &&
                !Modifier.isAbstract(customOPClass.getModifiers()) &&
                !Modifier.isInterface(customOPClass.getModifiers());
    }

    public ArrayList<IOperator> initializePlugins(String path, AOperatorProperties desiredOperatorProperties){

        for(Class customOperator: customOperators)
        {
            try {
                Constructor c = customOperator.getDeclaredConstructors()[0];
                String loadedOperatorProperties = c.getParameterTypes()[0].getName();
                if(desiredOperatorProperties.getClass().getName().equals(loadedOperatorProperties)){
                    AOperatorProperties operatorProperties = (AOperatorProperties) c.getParameterTypes()[0].getDeclaredConstructor(String.class).newInstance(path);
                    IOperator operator = (IOperator) customOperator.getDeclaredConstructor(operatorProperties.getClass()).newInstance(operatorProperties);
                    operators.add(operator);
                }
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        return operators;
    }

    public static PluginOperatorManager getInstance() {
        return SINGLE_INSTANCE;
    }
}
