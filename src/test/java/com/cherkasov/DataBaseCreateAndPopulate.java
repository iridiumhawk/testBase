package com.cherkasov;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Created by Lu on 14.06.2017.
 */
public class DataBaseCreateAndPopulate {
    private final String jdbcUrl;
    private final String jdbcDriver;
    private final String jdbcUser;
    private final String jdbcPassword;
    private final String scriptFile;
    private final String propertiesFile = "hsql.properties";
    private Properties properties;
    private Connection conn;

    public Connection getConn() {
        return conn;
    }

    public DataBaseCreateAndPopulate() {
        this.properties = loadProperties();
        this.jdbcUrl = properties.getProperty("jdbc.url");
        this.jdbcDriver = properties.getProperty("jdbc.driverClassName");
        this.scriptFile = properties.getProperty("populate.script");
        this.jdbcUser = properties.getProperty("jdbc.username");
        this.jdbcPassword = properties.getProperty("jdbc.password");

//        System.out.println(toString());
    }

    @Override
    public String toString() {
        return "{" +
                "jdbcUrl='" + jdbcUrl + '\'' +
                ", jdbcDriver='" + jdbcDriver + '\'' +
                ", jdbcUser='" + jdbcUser + '\'' +
                ", jdbcPassword='" + jdbcPassword + '\'' +
                ", scriptFile='" + scriptFile + '\'' +
                ", propertiesFile='" + propertiesFile + '\'' +
                '}';
    }

    private Properties loadProperties() {
        Properties prop = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(propertiesFile)) {
//        try (InputStream input = new FileInputStream(propertiesFile)){
//getClass().getClassLoader().getResourceAsStream("config.properties")
            //DataBaseCreateAndPopulate.class

            // load a properties file
            prop.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return prop;
    }

    public void eraseAll() {
        Objects.requireNonNull(conn);

        //truncate
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("DROP TABLE test");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void populate() {
        Objects.requireNonNull(scriptFile);
        Objects.requireNonNull(conn);

        String sqlQuery = "";
/*        File file = null;
        try {
            file = new File(this.getClass().getClassLoader().getResource(scriptFile).toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }*/

        try {
            //todo check script with one line or foreach
            sqlQuery = Files.
                    readAllLines(Paths.get(this.getClass().getClassLoader().getResource(scriptFile).toURI())).
                    stream().
                    collect(Collectors.joining(" "));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        try (Statement st = conn.createStatement()) {
            st.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {

        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void openConnection() {
        Objects.requireNonNull(jdbcDriver);
        Objects.requireNonNull(jdbcUrl);

        try {
            Class.forName(jdbcDriver);
            conn = DriverManager.getConnection(jdbcUrl);
//            conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

}

