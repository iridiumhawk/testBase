package com.cherkasov;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Lu on 18.06.2017.
 */
public class Configuration {
    private Properties prop = new Properties();

    private Configuration() {
    }

    public Configuration(String fileName) {
        prop = loadProperties(fileName);
    }

    public Configuration(String fileName, String[] args) {

        this(fileName);
//        prop = loadProperties(fileName);

        if (args != null && args.length != 0) {
            parseArguments(args);
        }
    }


    private Properties loadProperties(String fileName) {
        Properties prop = new Properties();

        //todo check file getting
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {

            // load a properties file
            prop.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return prop;
    }

    private void parseArguments(String[] args) {
        //parse input args here
        prop.setProperty("connectionString", getValueFromArgs(prop.getProperty("args.key.connectionString")));
        prop.setProperty("datesFile", getValueFromArgs(prop.getProperty("args.key.datesFileName")));

    }

    private String getValueFromArgs(String property) {
        //gets args and look up for key
//        "-"+property
        return "";
    }

    public String getValue(String key) {
        return prop.getProperty(key);
    }

    public void setValue(String key, String value) {
        prop.setProperty(key, value);
    }
}
