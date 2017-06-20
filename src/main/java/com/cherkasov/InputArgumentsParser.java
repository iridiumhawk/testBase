package com.cherkasov;

import java.util.Objects;

/**
 * Parses input line parameters
 */
public class InputArgumentsParser {

  private static String connectionString;
  private static String sqlFileName;
  private static boolean enableDebug;

  private InputArgumentsParser() {
  }

  /**
   * Wrapper for System.out.println()
   *
   * @param text for output to console
   */
  private static void toConsole(String text) {

    System.out.println(text);
  }

  /**
   * Parses input line arguments
   *
   * @param args input arguments
   */
  public static void getParameters(String[] args) {

    Objects.requireNonNull(args);

    if (args.length == 0) {

      //print command help
      toConsole("Help:");
      toConsole("-c [url] string to database connect");
      toConsole("-f [file] file with sql script");
      toConsole("-d enable debug mode");

      System.exit(0);

    }

    enableDebug = false;

    for (int i = 0; i < args.length; i++) {
      try {
        switch (args[i]) {

          case "-c":
            connectionString = args[i + 1];
            break;

          case "-d":
            enableDebug = true;
            break;

          case "-f":
            sqlFileName = args[i + 1];
            break;

        }
      } catch (ArrayIndexOutOfBoundsException e) {
        toConsole("InputArgumentsParser: index not correct");
      }
    }
  }

  public static String getConnectionString() {
    return connectionString;
  }

  public static String getSqlFileName() {
    return sqlFileName;
  }
}
