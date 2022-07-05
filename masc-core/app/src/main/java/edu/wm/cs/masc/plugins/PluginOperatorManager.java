package edu.wm.cs.masc.plugins;

import edu.wm.cs.masc.mutation.operators.IOperator;
import edu.wm.cs.masc.mutation.operators.custom.ACustomGenericOperator;
import edu.wm.cs.masc.mutation.operators.flexible.AFlexibleOperator;
import edu.wm.cs.masc.mutation.operators.restrictive.byteoperator.AByteOperator;
import edu.wm.cs.masc.mutation.operators.restrictive.intoperator.AIntOperator;
import edu.wm.cs.masc.mutation.operators.restrictive.stringoperator.AStringOperator;
import edu.wm.cs.masc.mutation.properties.*;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.checkerframework.checker.units.qual.A;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

public class PluginOperatorManager {
    private static final PluginOperatorManager SINGLE_INSTANCE = new PluginOperatorManager();
    private ArrayList<Class> customOperators = new ArrayList<>();
    private ArrayList<IOperator> operators = new ArrayList<>();

    private PluginOperatorManager() {
        String folderDir = "plugins";
        folderDir = "app\\build\\libs\\" + folderDir;
        File[] files = new File(folderDir).listFiles();

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
                Class CustomOPClass = loadClass(folderDir, file);

                if(isOperatorClass(CustomOPClass))
                {
                    customOperators.add(CustomOPClass);
                    System.out.println(CustomOPClass);
                }
            } catch (ClassNotFoundException e) {
                System.out.println("Class not found: " + file.getName());
            }
        }
    }

    private Class loadClass(String folderDir, File file) throws ClassNotFoundException {
        ClassLoader parentClassLoader = MyClassLoader.class.getClassLoader();
        MyClassLoader classLoader = new MyClassLoader(parentClassLoader);
        Class CustomOPClass = classLoader.loadClass(folderDir, file.getName());
        return CustomOPClass;
    }

    private boolean isOperatorClass(Class customOPClass) {
        return IOperator.class.isAssignableFrom(customOPClass) &&
                !Modifier.isAbstract(customOPClass.getModifiers()) &&
                !Modifier.isInterface(customOPClass.getModifiers());
    }

    public ArrayList<IOperator> initializeCustomPlugins(String path){

        for(Class customOperator: customOperators)
        {
            try {
                Constructor c = customOperator.getDeclaredConstructors()[0];
                AOperatorProperties shit = (AOperatorProperties) c.getParameterTypes()[0].getDeclaredConstructor(String.class).newInstance(path);
                IOperator operator = (IOperator) customOperator.getDeclaredConstructor(shit.getClass()).newInstance(shit);
                operators.add(operator);
                // System.out.println(operator.mutation());
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
