package com.cherkasov;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

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

}
