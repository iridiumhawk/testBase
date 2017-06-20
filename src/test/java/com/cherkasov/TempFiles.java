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
