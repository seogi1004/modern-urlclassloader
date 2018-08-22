package com.example.loader;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;

public class CompatibleURLClassLoaderTest {
    public static final String JAR_PATH = System.getProperty("user.dir") + "/src/test/resources/helloworld.jar";

    public static void main(String[] args) throws IOException {
        CompatibleURLClassLoader loader = CompatibleURLClassLoader.getInstance();
        loader.addFile(JAR_PATH);
        loader.loadJarFiles();

        URLClassLoader outputLoader = loader.getLoader(JAR_PATH);
        InputStream is = outputLoader.getResourceAsStream("README.md");

        System.out.println(IOUtils.toString(is, StandardCharsets.UTF_8));
    }
}
