package com.cherkasov;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.logging.Level;

/**
 * Class for getting data from database by sql script
 */
public class SqlDataGetter {

  private Connection conn;
  private String connectionUrl;
  private String driver;

  /**
   * Creates object with URL and driver
   * @param connectionUrl   url for connect to the database
   * @param driver          driver for connect to the database
   */
  public SqlDataGetter(String connectionUrl, String driver) {

    Objects.requireNonNull(connectionUrl);
    Objects.requireNonNull(driver);

    this.connectionUrl = connectionUrl;
    this.driver = driver;

  }

  /**
   * Creates object with the given connection
   * @param connection      connection to the database
   */
  public SqlDataGetter(Connection connection) {

    Objects.requireNonNull(connection);

    this.conn = connection;
  }

  public Connection getConn() {
    return conn;
  }

  /**
   * Uses connection to the database and gets result by formatted query from file
   *
   * @param sqlQuery  for execute
   * @return          list strings of results
   */
  public List<String> getResultsFromTable(String sqlQuery, String delimeter) {

    Objects.requireNonNull(sqlQuery, "Query string is null");
    Objects.requireNonNull(conn, "Connection not established, it is null");

    List<String> result = new ArrayList<>();
    ResultSet rs = null;

    long time = System.currentTimeMillis();

    try (Statement st = conn.createStatement()) {

      st.setQueryTimeout(1);
      //get results
      rs = st.executeQuery(sqlQuery);

//      System.out.println(rs.toString());
      //from result set give metadata
      ResultSetMetaData metaData = rs.getMetaData();

      //columns count from metadata object
      int numOfCols = metaData.getColumnCount();

      //todo add column name in the begin of file? String name = metaData.getColumnName(i);
/*
      while (rs.next()) {
        StringJoiner sj = new StringJoiner(delimeter);

        for (int i = 1; i <= numOfCols; i++) {
          sj.add(rs.getString(i));
        }

        String desiredString = sj.toString();

        result.add(desiredString);
      }*/

    } catch (SQLException e) {
//      LOG.log(Level.SEVERE, "SQLException by query. Error: " + e.getMessage());
//      FileUtils.saveErrorLog(sqlQuery);
      System.out.println("Error" + e.getMessage());

      return Collections.emptyList();

    } finally {

      if (rs != null) {

        try {
          rs.close();
        } catch (SQLException e) {
//          LOG.log(Level.SEVERE, "SQLException close resultset" + e.getMessage());
        }
      }
    }

     time = System.currentTimeMillis() - time;

    System.out.println(time);

    return result;
  }

  /**
   * Closes current connection to the database
   */
  public void closeConnection() {
    try {
      if (conn != null) {
        conn.commit();
        conn.close();
      }
    } catch (SQLException e) {
//      LOG.log(Level.SEVERE, "SQLException by close connection" + e.getMessage());
    }
  }

  /**
   * Opens connection to the database by connection URL with given driver
   */
  public void openConnection() {
    Objects.requireNonNull(driver, "Driver is null");
    Objects.requireNonNull(connectionUrl, "Connection string is null");

    try {
      Class.forName(driver);
      conn = DriverManager.getConnection(connectionUrl);
      System.out.println("Established");
    } catch (ClassNotFoundException | SQLException e) {
      System.out.println(
          "Exception by open connection: " + e.getMessage() + " " + driver + " " + connectionUrl);
    }
  }
}
