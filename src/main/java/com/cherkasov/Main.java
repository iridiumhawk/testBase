package com.cherkasov;

/**
 * Created by Lu on 17.06.2017.
 */
public class Main {
    private static Configuration config;

    public static void main(String[] args) {

        config = new Configuration("app.properties", args);

        loadFromSqlFile(config.getValue("sql.file"));

        config.getAllProperties(System.out);
    }

    private static void loadFromSqlFile(String value) {
        System.out.println(value);
    }
}
