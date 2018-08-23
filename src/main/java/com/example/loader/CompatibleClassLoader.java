package com.example.loader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompatibleClassLoader {
    private List<URL> jarList = new ArrayList<>();
    private Map<String, URLClassLoader> jarMap = new HashMap<>();
    private static CompatibleClassLoader loader = null;

    public static CompatibleClassLoader getInstance() {
        if(loader == null) {
            loader = new CompatibleClassLoader();
        }

        return loader;
    }

    public void addURL(URL url) {
        jarList.add(url);
        jarMap.put(url.getPath(), new URLClassLoader(new URL[] { url }));
    }

    public void addFile(String path) {
        addFile(new File(path));
    }

    public void addFile(File file) {
        try {
            addURL(file.toURI().toURL());
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public URLClassLoader getLoader(URL url) {
        return jarMap.get(url.getPath());
    }

    public URLClassLoader getLoader(String path) {
        return getLoader(new File(path));
    }

    public URLClassLoader getLoader(File file) {
        try {
            return getLoader(file.toURI().toURL());
        } catch (MalformedURLException e) {
            System.out.println(e);
        }

        return null;
    }

    public void loadJarFiles() {
        Thread thread = Thread.currentThread();

        URLClassLoader loader = new URLClassLoader(jarList.toArray(new URL[jarList.size()]), thread.getContextClassLoader());
        thread.setContextClassLoader(loader);

        jarList.clear();
    }
}
