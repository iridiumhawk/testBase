package com.cherkasov;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Lu on 14.06.2017.
 */
public class TestDataBase {

    private DataBaseCreateAndPopulate dataSource;

    @Before
    public void setUp() {
        dataSource = new DataBaseCreateAndPopulate();
        dataSource.openConnection();
//        dataSource.eraseAll();
        dataSource.populate();
    }

    @After
    public void tearDown() {
        dataSource.closeConnection();
    }

    @Test
    public void check() {
        assertNotNull(dataSource.getConn());
    }
}
