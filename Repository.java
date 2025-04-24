package com.mycompany.financemanagementsystem;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class Repository implements Serializable {
    private static final long serialVersionUID = 1L;

    final public List<Expenses> expList;
    final public List<Category> catList;

    private static Repository repository;

    public float totalBudget = 0;
    public final String expenseFilePath = "expenses.txt";

    // Private constructor
    private Repository() {
        expList = new ArrayList<>();
        catList = new ArrayList<>();
    }

    // Singleton instance loader with file support
    public static Repository getRepository() {
        if (repository == null) {
            FileHandling fh = new FileHandling();
            Repository loadedRepo = fh.loadData();

            if (loadedRepo != null) {
                repository = loadedRepo;
            } else {
                repository = new Repository();
            }
        }
        return repository;
    }

    // Getters
    public List<Expenses> getExpenses() {
        return expList;
    }

    public List<Category> getCatList() {
        return catList;
    }
}

