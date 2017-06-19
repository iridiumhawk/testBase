package com.cherkasov;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * Created by Lu on 19.06.2017.
 */
public class ConfigurationTest {
    Configuration configuration;

    @Before
    public void setUp() throws Exception {
        configuration = new Configuration("app.properties");
    }

    @After
    public void tearDown() throws Exception {
        configuration = null;
    }

    @Test
    public void getValue() throws Exception {
        assertEquals("test.sql", configuration.getValue("sql.file"));

    }

    @Test
    public void setValue() throws Exception {
        configuration.setValue("sql.file.test", "test.sql");
        assertEquals("test.sql", configuration.getValue("sql.file.test"));

    }

    @Test
    public void getAllProperties() throws Exception {
        assertTrue(!configuration.getAllProperties().isEmpty());
    }

    @Test
    public void testPrivateMethod() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
//        Class<?> clazz = Class.forName("com.cherkasov.Configuration");
        Class<?> clazz = configuration.getClass();
        Method method = clazz.getDeclaredMethod("getValueFromArgs", String.class, String[].class);
        method.setAccessible(true);
        method.invoke(configuration, "-t", new String[]{"test"});

    }

}