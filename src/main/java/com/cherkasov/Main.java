package com.cherkasov;

import java.sql.SQLException;

/**
 * Created by Lu on 17.06.2017.
 */
public class Main {

    private static final String DRIVER = "oracle.jdbc.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:system/qwerty1234@//localhost:1521/xe";
/*
    jdbc.url=jdbc:oracle:thin:[<user>/<password>]@//<host>[:<port>]/<service>
#jdbc.url=jdbc:oracle:thin:[<user>/<password>]@<host>[:<port>]:<SID>
#jdbc.url=jdbc:oracle:thin:[<user>/<password>]@//<host>[:<port>]/<service>
    */
    private static Configuration config;

    public static void main(String[] args) throws SQLException {

        config = new Configuration("app.properties", args);

//        loadFromSqlFile(config.getValue("sql.file"));

        SqlDataGetter sql = new SqlDataGetter(URL, DRIVER);

        sql.openConnection();

        System.out.println(sql.getConn().isClosed());

        System.out.println(sql.getResultsFromTable("SELECT * FROM TABLETEST", ";"));

//        config.getAllProperties(System.out);
    }

    private static void loadFromSqlFile(String value) {
        System.out.println(value);
    }
}
