package com.cherkasov;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Properties;


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

  public List<String> getColumnFromTable(String tableName, String columnName) {

    Objects.requireNonNull(tableName);
    Objects.requireNonNull(columnName);


    List<String> list = new ArrayList<>();
    ResultSet rs = null;
    try (Statement st = conn.createStatement()) {

      //get results

      rs = st.executeQuery(String.format("SELECT %s FROM %s", columnName, tableName));

      while (rs.next()) {
        list.add(rs.getString(columnName));
      }
      rs.close();

    } catch (SQLException e) {
      System.out.println(e.getMessage());
      return Collections.emptyList();
    } finally {
      if (rs != null){

        try {
          rs.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }


    return list;
  }

  private static ArrayList<String> listOfQueries = null;

  //todo use this method in main SQL parser?
  /**
   * @param   path    Path to the SQL file
   * @return          List of query strings
   */
  public ArrayList<String> createQueries(String path) {
    String queryLine = new String();
    StringBuffer sBuffer = new StringBuffer();
    listOfQueries = new ArrayList<String>();

    try {
      FileReader fr = new FileReader(new File(path));
      BufferedReader br = new BufferedReader(fr);

      //read the SQL file line by line
      while ((queryLine = br.readLine()) != null) {
        // ignore comments beginning with #
        int indexOfCommentSign = queryLine.indexOf('#');
        if (indexOfCommentSign != -1) {
          if (queryLine.startsWith("#")) {
            queryLine = new String("");
          } else {
            queryLine = new String(queryLine.substring(0, indexOfCommentSign - 1));
          }
        }
        // ignore comments beginning with --
        indexOfCommentSign = queryLine.indexOf("--");
        if (indexOfCommentSign != -1) {
          if (queryLine.startsWith("--")) {
            queryLine = new String("");
          } else {
            queryLine = new String(queryLine.substring(0, indexOfCommentSign - 1));
          }
        }
        // ignore comments surrounded by /* */
        indexOfCommentSign = queryLine.indexOf("/*");
        if (indexOfCommentSign != -1) {
          if (queryLine.startsWith("#")) {
            queryLine = new String("");
          } else {
            queryLine = new String(queryLine.substring(0, indexOfCommentSign - 1));
          }

          sBuffer.append(queryLine + " ");
          // ignore all characters within the comment
          do {
            queryLine = br.readLine();
          }
          while (queryLine != null && !queryLine.contains("*/"));
          indexOfCommentSign = queryLine.indexOf("*/");
          if (indexOfCommentSign != -1) {
            if (queryLine.endsWith("*/")) {
              queryLine = new String("");
            } else {
              queryLine = new String(
                  queryLine.substring(indexOfCommentSign + 2, queryLine.length() - 1));
            }
          }
        }

        //  the + " " is necessary, because otherwise the content before and after a line break are concatenated
        // like e.g. a.xyz FROM becomes a.xyzFROM otherwise and can not be executed
        if (queryLine != null) {
          sBuffer.append(queryLine + " ");
        }
      }
      br.close();

      // here is our splitter ! We use ";" as a delimiter for each request
      String[] splittedQueries = sBuffer.toString().split(";");

      // filter out empty statements
      for (int i = 0; i < splittedQueries.length; i++) {
        if (!splittedQueries[i].trim().equals("") && !splittedQueries[i].trim().equals("\t")) {
          listOfQueries.add(new String(splittedQueries[i]));
        }
      }
    } catch (Exception e) {
      System.out.println("*** Error : " + e.toString());
      System.out.println("*** ");
      System.out.println("*** Error : ");
      e.printStackTrace();
      System.out.println("################################################");
      System.out.println(sBuffer.toString());
    }
    return listOfQueries;
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
/*        try {
            sqlQuery = Files.
                    readAllLines(Paths.get(this.getClass().getClassLoader().getResource(scriptFile).toURI())).
                    stream().
                    collect(Collectors.joining(" "));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }*/

  //todo check script with one line or foreach
    createQueries(this.getClass().getClassLoader().getResource(scriptFile).getFile());

    try (Statement st = conn.createStatement()) {

      for (String query : listOfQueries) {
        st.executeUpdate(query);
      }

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

