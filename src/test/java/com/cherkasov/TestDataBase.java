package com.cherkasov;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


public class TestDataBase {

  private DataBaseCreateAndPopulate hsqlDb;

  @Before
  public void setUp() {
    hsqlDb = new DataBaseCreateAndPopulate();
    hsqlDb.openConnection();
    hsqlDb.populate();
  }

  @After
  public void tearDown() {
    hsqlDb.closeConnection();
  }

  @Test
  public void testConnection() throws Exception{
    assertNotNull(hsqlDb.getConn());
    assertFalse(hsqlDb.getConn().isClosed());
  }

  @Test
  public void testSelectFromDb() throws Exception{
    assertEquals("Felix", hsqlDb.getColumnFromTable("star", "firstname").get(0));
  }

  @Test
  @Ignore
  public void shouldReturnEmptyResultIfQueryTimeout() {
//    SqlDataGetter sql = new SqlDataGetter(hsqlDb.getConn());
    String query =
            "CREATE PROCEDURE sleep(t BIGINT) "
                    + "NO SQL "
                    + "LANGUAGE JAVA PARAMETER STYLE JAVA "
                    + "EXTERNAL NAME 'CLASSPATH:java.lang.Thread.sleep';   ";

//    ExecutionTimeMeter.startTimer("sleep_query");

//    exception.expect(SQLException.class);
    try (Statement st = hsqlDb.getConn().createStatement()) {

      int timeOut = 2000;
      Runnable runner = () -> {
        try {
          Thread.sleep(timeOut);
          System.out.println("test");

          st.cancel();
//          hsqlDb.getConn().close();

        } catch (InterruptedException e) {
          e.printStackTrace();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      };

      new Thread(runner).start();


      st.execute(query);
//      st.setQueryTimeout(2);
//      System.out.println(st.getQueryTimeout());
      st.executeQuery("CALL sleep(10000)");


    } catch (SQLException e) {
      e.printStackTrace();
    }

/*    String sql = "select slow_query(10) from dual";
    PreparedStatement stmt = conn.prepareStatement(sql);
    stmt.setQueryTimeout(2);*/

//    List<String> result = sql.getResultsFromTable(query, conf.getValue("csv.delimiter"));
//    ExecutionTimeMeter.stopTimer("sleep_query");
  }


    /* @Test
   @Ignore
   public void insertIntoTableHSQL() throws SQLException {
     SqlDataGetter sql = new SqlDataGetter(jdbcUrl, jdbcDriver);
     sql.openConnection();

     Statement st = sql.getConn().createStatement();
 //    CREATE TABLE movies (starid INTEGER, movieid INTEGER PRIMARY KEY, title VARCHAR(40));
     for (int i = 1000009; i < 2000000; i++) {
       st.execute("INSERT INTO movies(starid, movieid, title) VALUES('"
           + Integer.toString(i) + "','" + Integer.toString(i) + "','test')");
     }

     sql.closeConnection();
   }
 */

  private final String jdbcDriver = "org.hsqldb.jdbcDriver";
  private final String jdbcUrl = "jdbc:hsqldb:file:c:/java/base/testdb";
  @Test
  @Ignore
  public void shouldReturnEmptyResultIfQueryTimeoutWithFileDatabase() throws SQLException {

    int timeOut = 5;
//    SqlDataGetter sql = new SqlDataGetter(hsqlDb.getConn());
    String query =
        "CREATE PROCEDURE sleep(t BIGINT) "
            + "NO SQL "
            + "LANGUAGE JAVA PARAMETER STYLE JAVA "
            + "EXTERNAL NAME 'CLASSPATH:java.lang.Thread.sleep';   ";

    /*
      String query =
        "CREATE PROCEDURE sleep(msec BIGINT) "
            + "NO SQL "
            + "LANGUAGE JAVA PARAMETER STYLE JAVA "
            + "EXTERNAL NAME 'CLASSPATH:java.lang.Thread.sleep';   "

            + "CREATE FUNCTION sleep_times(t INT) RETURNS TABLE(r_pid VARCHAR(20), r_id VARCHAR(20)) "
//            + "SPECIFIC child_func "
            + "CONTAINS  SQL  "
//            + "SIGNAL SQLSTATE '45000' "

//            + "ALTER SPECIFIC ROUTINE child_func "
            + "BEGIN ATOMIC "
            + "DECLARE my_var INT; "
            + "SET my_var = 0; "
            + "loop_label: WHILE my_var < t DO "
            + "SET my_var = my_var + 1; "
            + "CALL sleep(1000); "
            + "END WHILE loop_label; "
            + "RETURN TABLE(SELECT firstname, lastname FROM star); "
            + "END ";
    */

    String querySelect = "Select * from movies";


//    Connection conn = hsqlDb.getConn();
    ExecutionTimeMeter.startTimer("connect");

    SqlDataGetter sql = new SqlDataGetter(jdbcUrl, jdbcDriver);
    sql.openConnection();
    Connection conn = sql.getConn();
/*
    Connection conn = null;
    try {
      Class.forName(jdbcDriver);
       conn = DriverManager.getConnection(jdbcUrl);

    } catch (ClassNotFoundException | SQLException e) {

    }*/

    ExecutionTimeMeter.stopTimer("connect");

    List<String> result = new ArrayList<>();

    int count = 0;

    ExecutionTimeMeter.startTimer("sleep_query");
    long startTime = System.currentTimeMillis();

//    exception.expect(SQLException.class);
    try (Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {

//      threadTimeout(st);

//      st.execute(query);
      st.setQueryTimeout(timeOut);
//      System.out.println(st.getQueryTimeout());
//      st.executeQuery("CALL sleep(10000)");

      ExecutionTimeMeter.startTimer("execute");

//      st.setFetchSize(100000);

      ResultSet rs = st.executeQuery(querySelect);

      ExecutionTimeMeter.stopTimer("execute");


      ResultSetMetaData metaData = rs.getMetaData();

      int numOfCols = metaData.getColumnCount();

      ExecutionTimeMeter.startTimer("rs");
      while (rs.next()) {

/*        rs.last();
        int rowNum = rs.getRow();
        System.out.println(rowNum);*/

        if ((System.currentTimeMillis() - startTime) / 1000 >= timeOut) {
          System.out.println("timeout " + (System.currentTimeMillis() - startTime));
          break;
        }

        count++;
        StringJoiner sj = new StringJoiner(";");

        for (int i = 1; i <= numOfCols; i++) {
          sj.add(rs.getString(i));
        }

        result.add(sj.toString());


      }

      ExecutionTimeMeter.stopTimer("rs");
      ExecutionTimeMeter.stopTimer("sleep_query");

      conn.close();
    } catch (SQLException e) {
//      e.printStackTrace();
      System.out.println(e.getMessage());
    } finally {
      if (conn != null){
        conn.close();
      }
    }

    System.out.println(count);
    System.out.println(result.size());

/*    String sql = "select slow_query(10) from dual";
    PreparedStatement stmt = conn.prepareStatement(sql);
    stmt.setQueryTimeout(2);*/

//    List<String> result = sql.getResultsFromTable(query, conf.getValue("csv.delimiter"));
//    SQLTransactionRollbackException
//HsqlException

  }

}
