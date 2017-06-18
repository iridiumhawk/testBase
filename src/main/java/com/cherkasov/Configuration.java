package com.cherkasov;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
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
//        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
        try (FileReader reader = new FileReader(fileName)) {
            // load a properties file
            prop.load(reader);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return prop;
    }

    private void parseArguments(String[] args) {
        //todo parse input args here
        //for each args.key. check parameter and add to property
        prop.setProperty("jdbc.connectionString",
                getValueFromArgs(prop.getProperty("args.key.connectionString"), args));
        prop.setProperty("datesFile",
                getValueFromArgs(prop.getProperty("args.key.datesFileName"), args));

    }

    private String getValueFromArgs(String property, String[] args) {
        //gets args and look up for key
//        "-"+property
        for (String arg : args) {
            if (("-"+property).equals(arg)) {
//                return args[i+1]; //if not null
            }
        }
        return "";
    }

    public String getValue(String key) {
        return prop.getProperty(key);
    }

    public void setValue(String key, String value) {
        prop.setProperty(key, value);
    }

    public void getAllProperties(PrintStream out){
        prop.list(out);
    }
}
