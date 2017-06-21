package com.cherkasov;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.sql.Statement;
import org.junit.After;
import org.junit.Before;
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
