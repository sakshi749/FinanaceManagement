package com.mycompany.financemanagementsystem;
import java.io.*;

public class FileHandling {

    private final String fileName = "data.txt";

    public void saveData(Repository repository) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(repository);
            System.out.println("Our data is saved to a file.");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public Repository loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            Repository repository = (Repository) ois.readObject();
            System.out.println("Our data has been loaded from a file.");
            return repository;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("error loading file, no previous data found .");
            return null;
        }
    }
}

