package com.cherkasov;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Utility class for measuring time spent by some operation
 */
public class ExecutionTimeMeter {

  private static Map<String, Long> timer = new HashMap<>();

  /**
   * Disables of creating ob]ect for the class
   */
  private ExecutionTimeMeter() {
  }

  /**
   * Saves start time of beginning operation
   *
   * @param origin name of operation
   */
  public static void startTimer(String origin) {

    Objects.requireNonNull(origin);

    timer.put(origin, System.currentTimeMillis());
  }

  /**
   * Gets stop time of operation and prints to console summary time
   *
   * @param origin name of operation
   * @param message for description operation
   */
  public static void stopTimer(String origin, String message) {

    Objects.requireNonNull(origin);

    Long timeStart = timer.get(origin);
    if (timeStart == null) {
      return;
    }

    float timeInSeconds = (float) (System.currentTimeMillis() - timeStart) / 1000;

    if (message != null) {
      System.out.printf("Time spent by %s: %.3f seconds; %s%n", origin, timeInSeconds, message);
    } else {
      System.out.printf("Time spent by %s: %.3f seconds%n", origin, timeInSeconds);
    }

  }

  /**
   * Deletes time of operation
   *
   * @param origin name of operation
   */
  public static void stopTimer(String origin) {
    Objects.requireNonNull(origin);

    stopTimer(origin, null);
  }
}
