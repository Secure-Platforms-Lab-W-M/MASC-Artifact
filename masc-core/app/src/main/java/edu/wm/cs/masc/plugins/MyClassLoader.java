package edu.wm.cs.masc.plugins;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class MyClassLoader extends ClassLoader {

    String pluginsFolderDir = "plugins";

    public MyClassLoader(ClassLoader parent) {
        super(parent);
    }

    public Class loadClass(String dir, String name) throws ClassNotFoundException {
        if(!name.endsWith(".class"))
            return super.loadClass(name);

        name = name.substring(0, name.length()-6);

        try {
            String url = "file:" + dir + "\\" + name + ".class";
            URL myUrl = new URL(url);
            URLConnection connection = myUrl.openConnection();
            InputStream input = null;

            input = connection.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int data = input.read();

            while(data != -1){
                buffer.write(data);
                data = input.read();
            }

            input.close();

            byte[] classData = buffer.toByteArray();

            return defineClass("plugins." + name,
                    classData, 0, classData.length);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("File not found");
        }

        return null;
    }

}
