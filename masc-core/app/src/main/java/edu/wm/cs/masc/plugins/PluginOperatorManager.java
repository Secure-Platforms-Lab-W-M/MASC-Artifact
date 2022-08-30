package edu.wm.cs.masc.plugins;
/**
 * This class has the responsibility of loading all external operator classes
 * provided by the user and instantiate them.
 * @author: Yusuf Ahmed
 */

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

    private PluginOperatorManager() {
        String packageName = "plugins";
        String folderDir = "app\\build\\libs";
        // folderDir = ".";
        load_custom_classes(packageName, folderDir);
    }

    /**
     * Constructor calls this function to load all custom .class files provided by the user in plugins/
     */
    private void load_custom_classes(String packageName, String folderDir) {
        File[] files = new File(folderDir + "\\" + packageName).listFiles();
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

    /**
     * Loads a single class from external .class file.
     * @param folder parent directory of the .class file
     * @param packageName package name of the class
     * @param className name of  the class to load
     * @return The loaded class as java Class
     * @throws ClassNotFoundException if the file does not have the class
     */
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

    /**
     * Checks if the loaded class is an operator or some other class
     * related or unrelated to an operator
     * @param customOPClass The loaded class in question
     * @return true if the class is an operator, false otherwise.
     */
    private boolean isOperatorClass(Class customOPClass) {
        return IOperator.class.isAssignableFrom(customOPClass) &&
                !Modifier.isAbstract(customOPClass.getModifiers()) &&
                !Modifier.isInterface(customOPClass.getModifiers());
    }

    /**
     * Checks if a loaded class is an operator.
     * If yes, it is checked to see if the operator is of the type of operator specified in the properties file.
     * If yes, the class is instantiated and stored in a list.
     * @param path name of the properties file along with its path
     * @param desiredOperatorProperties The related object of {@link AOperatorProperties} class that is compatible with the desired operator type
     * @return list of operator objects of the type specified in the properties file.
     */
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

    /**
     * Get instance of single object
     * @return a instance of this class
     */
    public static PluginOperatorManager getInstance() {
        return SINGLE_INSTANCE;
    }
}