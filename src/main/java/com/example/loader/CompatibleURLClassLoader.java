package com.example.loader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompatibleURLClassLoader {
    private static CompatibleURLClassLoader loader = null;
    private List<URL> jarList = new ArrayList<>();
    private Map<String, URLClassLoader> jarMap = new HashMap<>();

    public static CompatibleURLClassLoader getInstance() {
        if(loader == null) {
            loader = new CompatibleURLClassLoader();
        }

        return loader;
    }

    public void addURL(URL url) {
        jarList.add(url);
        jarMap.put(url.getPath(), new URLClassLoader(new URL[] { url }));
    }

    public void addFile(String path) {
        File f = new File(path);

        try {
            addURL(f.toURI().toURL());
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public URLClassLoader getLoader(URL url) {
        return jarMap.get(url.getPath());
    }

    public URLClassLoader getLoader(String path) {
        try {
            return getLoader(new File(path).toURI().toURL());
        } catch (MalformedURLException e) {
            System.out.println(e);
        }

        return null;
    }

    public void loadJarFiles() {
        Thread thread = Thread.currentThread();

        URLClassLoader loader = new URLClassLoader(jarList.toArray(new URL[jarList.size()]), thread.getContextClassLoader());
        thread.setContextClassLoader(loader);
    }

    public Constructor<?> getConstructor(String className) {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(className).getConstructor();
        } catch(ClassNotFoundException e) {
            System.out.println(e);
        } catch (NoSuchMethodException e) {
            System.out.println(e);
        }

        return null;
    }
}