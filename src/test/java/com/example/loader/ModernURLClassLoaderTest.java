package com.example.loader;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ModernURLClassLoaderTest {
    private static final String DEFAULT_PATH = System.getProperty("user.dir") + "/src/test/resources/";
    private static final String[] JAR_PATHS = new String[] { "calculator.jar", "helloworld.jar" };
    private static ModernURLClassLoader loader;

    @BeforeClass
    public static void setup() {
        loader = ModernURLClassLoader.getInstance();

        for(String jarPath : JAR_PATHS) {
            loader.addFile(DEFAULT_PATH + jarPath);
        }

        loader.loadJarFiles();
    }

    @Test
    public void testGetResourceInJar() throws IOException {
        URLClassLoader ucl = loader.getLoader(DEFAULT_PATH + "helloworld.jar");
        InputStream is = ucl.getResourceAsStream("README.md");

        assertEquals("Hello, World", IOUtils.toString(is, StandardCharsets.UTF_8));
    }

    @Test
    public void testInvokeMethod() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        URLClassLoader ucl = loader.getLoader(DEFAULT_PATH + "helloworld.jar");
        Class<?> cls = Class.forName("HelloWorld", true, ucl);
        Method m = cls.getMethod("getMessage");

        assertEquals("Hello, World", m.invoke(cls.newInstance()));
    }

    @Test
    public void testInvokeStaticMethod() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        URLClassLoader ucl = loader.getLoader(DEFAULT_PATH + "calculator.jar");
        Class<?> cls = Class.forName("com.example.Calculator", true, ucl);
        Method m2 = cls.getMethod("add", int.class, int.class);

        assertEquals(3, m2.invoke(null, 1, 2));
    }
}
