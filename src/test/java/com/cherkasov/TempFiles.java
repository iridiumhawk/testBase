package com.cherkasov;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lu on 17.06.2017.
 */
public class TempFiles {
    private List<String> files = new ArrayList<>();

    public void createFile(String fileName, List<String> content) {


        files.add(fileName);
    }

    public void deleteFile(String fileName) {
        try {
            Files.delete(Paths.get(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteAllFiles() {
        files.forEach(this::deleteFile);
    }

}
/*
    @Test
    public void whenCommentInQueryIsDashShouldRemove() throws IOException {
//    Files.readAllLines(Paths.get("sql.txt"));
        StringBuilder resultLine = new StringBuilder();

//    FileUtils.removeDishCommentaryFromQuery(Files.readAllLines(Paths.get("sql.txt"), Charset.forName("UTF-8")));
        Files.readAllLines(Paths.get("sql.txt"), Charset.forName("UTF-8")).forEach(v -> resultLine.append(v).append(" ").append("\n\r"));
        System.out.println(resultLine.toString());

        System.out.println(removeDishCommentaryFromQuery(resultLine.toString()));
/*
    List<String> query = new ArrayList<>();
    query.add("Test1\n\r");
    query.add("-- test\n\rTest1.5 ");
    query.add("Test2 --test\n\r");
    query.add("--\n\r");
    query.add(" --\n\r");
    query.add("Test3\n\r");

//    "Test1 --test\n\r Test2 --\n\r Test3"

    List<String> result = FileUtils.removeDishCommentaryFromQuery(query);

//    System.out.println(result);
//    assertEquals("Test1", result.get(0));
//    assertEquals("Test2", result.get(1));
//    assertEquals("", result.get(2));
//    assertEquals("Test3", result.get(3));
    }
        */        /*
    @Test
    public void whenCommentInQueryIsMultiLineShouldRemove() {
        List<String> query = new ArrayList<>();
        query.add("Test1\n\r");
        query.add("/* Test */\n\r");
//        query.add("Test2 /*Test*/\n\r");
//        query.add("/*\n\r");
//        query.add("Test");
//        query.add("*/");
//        query.add("Test3 /*Test");
//        query.add("Test */");
//
//        String result = FileUtils.removeMultiLineCommentaryFromQuery(query);

//    System.out.println(result);
//    assertTrue(result.get(0).contains("Test1"));
//    assertEquals("Test2", result.get(1));
//    assertEquals("Test3", result.get(2));
//    }

/*
----------------


public static String removeDishCommentaryFromQuery(String query) {
    // ignore comments beginning with --

    StringBuilder resultLine = new StringBuilder();
    query.forEach(v -> resultLine.append(v).append(" "));

    String result = query.toString().replaceAll("--(.)*\\n\\r", "");
    System.out.println(result);

    return result;
        */
  /*  List<String> result = new ArrayList<>();

    for (String queryLine : query) {
      int indexOfCommentSign = queryLine.indexOf("--");
      if (indexOfCommentSign == -1) {
        result.add(queryLine);
      } else if (!queryLine.startsWith("--")) {
        result.add(queryLine.substring(0, indexOfCommentSign - 1));

      }

    }
    return result;
    }*/
/*
public static String removeMultiLineCommentaryFromQuery(List<String> query) {
    StringBuilder resultLine = new StringBuilder();
    query.forEach(v -> resultLine.append(v).append(" "));

    String result = resultLine.toString().replaceAll("/\\*(?:.|[\\n\\r])*?\\*/", "");
    /*
    System.out.println(result);
    return result;

    List<String> result = new ArrayList<>();

    // ignore comments surrounded by *//* *//*

    boolean skipLine = false;

    for (String queryLine : query) {

      int indexOfCommentSign = queryLine.indexOf("*//*");

      if (indexOfCommentSign != -1 && queryLine.contains("*//*")) {
        //cut part line with comment in one line
        Pattern commentPattern = Pattern.compile("/\\*.*?\\*//*", Pattern.DOTALL);
        result.add(commentPattern.matcher(queryLine).replaceAll(""));
      } else if (indexOfCommentSign != -1) {
        result.add(queryLine.substring(0, indexOfCommentSign - 1));
        skipLine = true;
        continue;
      }

      // ignore all characters within the comment
      if (skipLine) {
        if (!queryLine.contains("*//*")) {
          continue;
        } else {
          skipLine = false;
          indexOfCommentSign = queryLine.indexOf("*//*");
          if (!queryLine.endsWith("*//*")) {
            result.add(queryLine.substring(indexOfCommentSign + 2, queryLine.length() - 1));
          }
        }
      } else {
        result.add(queryLine);
      }
    }
    return result;

    }
    */