package com.cherkasov;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

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
/*        Statement st = sql.getConn().createStatement();

        for (int i = 0; i < 100000; i++) {
           st.execute("INSERT INTO TABLETEST(COLUMN1, COLUMN2) VALUES('"
                   + Integer.toString(i)+  "','"+ Integer.toString(i)+"')");
        }*/

//        List<String> res = sql.getResultsFromTable("SELECT COLUMN1,COLUMN2 FROM (SELECT COLUMN1,COLUMN2 FROM TABLETEST WHERE COLUMN1=COLUMN2)", ";");
        List<String> res = sql.getResultsFromTable
                ("SELECT func_slow( 500 )\n" +
                        " FROM dual " +
                        "CONNECT BY level <= 2000", ";");

        System.out.println(res.size());

//        System.out.println(sql.getResultsFromTable("SELECT * FROM TABLETEST", ";"));

//        config.getAllProperties(System.out);
    }

    private static void loadFromSqlFile(String value) {
        System.out.println(value);
    }
}
