package com.mycompany.financemanagementsystem;

import java.io.IOException;
import java.util.List;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Date;

public class Services {
    Repository repository;

    public Services() {
        repository = Repository.getRepository();
    }

    public void showMenu() {
        Scanner in = new Scanner(System.in);
        int choice;

        while (true) {
            printMenu();
            System.out.print("ENTER YOUR CHOICE: ");

            try {
                choice = in.nextInt();
                in.nextLine(); // clear newline

                switch (choice) {
                    case 1 -> setBudget(in);
                    case 2 -> addCategory(in);
                    case 3 -> showCategoryList();
                    case 4 -> expenseEntry(in);
                    case 5 -> expenseList();
                    case 6 -> showTotalExpense();
                    case 7 -> viewBudgetSummary();
                    case 0 -> {
                        FileHandling fh = new FileHandling();
                        fh.saveData(repository);
                        System.out.println("Exiting the system. Thank you!");
                        System.exit(0);
                    }
                    default -> System.out.println("Invalid choice! Please select a valid option.");
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number.");
                in.nextLine(); // Clear buffer
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
                in.nextLine(); // Clear buffer
            }
        }
    }

    public void printMenu() {
        System.out.println("--------- MENU ---------");
        System.out.println("1. SET TOTAL BUDGET");
        System.out.println("2. ADD CATEGORY");
        System.out.println("3. CATEGORY LIST");
        System.out.println("4. EXPENSE ENTRY");
        System.out.println("5. EXPENSE LIST");
        System.out.println("6. SHOW TOTAL EXPENSE");
        System.out.println("7. VIEW BUDGET SUMMARY");
        System.out.println("0. EXIT");
    }

    public void pressAnyKeyToStart() {
        try {
            System.out.println("Press Enter to continue...");
            System.in.read();
        } catch (IOException ex) {
            System.out.println("An error occurred while waiting for key press: " + ex.getMessage());
        }
    }

    public void addCategory(Scanner in) {
        System.out.print("Enter category name: ");
        String catName = in.nextLine();
        Category cat = new Category(catName);
        repository.catList.add(cat);
        System.out.println("Category \"" + catName + "\" added successfully.");
        pressAnyKeyToStart();
    }

    public void showCategoryList() {
        System.out.println("\n--------- CATEGORY LIST ---------");
        List<Category> cList = repository.catList;

        if (cList.isEmpty()) {
            System.out.println("No categories found.");
        } else {
            for (int i = 0; i < cList.size(); i++) {
                Category c = cList.get(i);
                System.out.println((i + 1) + ". " + c.getName() + ", " + c.getCategoryId());
            }
        }

        pressAnyKeyToStart();
    }

    public void expenseEntry(Scanner in) {
        System.out.println("\nEnter details for expense entry");
        showCategoryList();

        System.out.print("Choose category: ");
        int catChoice = in.nextInt();
        Category selectedCat = repository.catList.get(catChoice - 1);
        in.nextLine();

        System.out.println("Your category choice: " + selectedCat.getName());
        System.out.println("Enter Amount:");
        float amount = in.nextFloat();
        in.nextLine();
        System.out.println("Enter Remark : ");
        String remark = in.nextLine();
        Date date = new Date();

        Expenses expense = new Expenses();
        expense.setCategoryId(selectedCat.getCategoryId());
        expense.setAmount(amount);
        expense.setRemark(remark);
        expense.setDate(date);

        repository.expList.add(expense);
        System.out.println("Expense entry added successfully.");
        pressAnyKeyToStart();
    }

    private void expenseList() {
        System.out.println("---------EXPENSE LIST---------");
        List<Expenses> expList = repository.expList;

        if (expList.isEmpty()) {
            System.out.println("No expense entries found.");
        } else {
            for (int i = 0; i < expList.size(); i++) {
                Expenses exp = expList.get(i);
                String catName = getCategoryNameById(exp.getCategoryId());
                System.out.println((i + 1) + ". " +
                    catName + " | " +
                    exp.getAmount() + " | " +
                    exp.getRemark() + " | " +
                    exp.getDate());
            }
        }

        pressAnyKeyToStart();
    }

    String getCategoryNameById(Long categoryId) {
        for (Category c : repository.catList) {
            if (c.getCategoryId().equals(categoryId)) {
                return c.getName();
            }
        }
        return null;
    }

    private void showTotalExpense() {
        float total = 0;
        for (Expenses exp : repository.expList) {
            total += exp.getAmount();
        }

        System.out.println("\n Total Expenses: " + total);
        pressAnyKeyToStart();
    }

    private void setBudget(Scanner in) {
        System.out.print("Enter your total available budget: ");
        repository.totalBudget = in.nextFloat();
        in.nextLine();
        System.out.println("Budget set successfully: " + repository.totalBudget);
        pressAnyKeyToStart();
    }

    private void viewBudgetSummary() {
        float totalSpent = 0;
        for (Expenses exp : repository.expList) {
            totalSpent += exp.getAmount();
        }

        float remaining = repository.totalBudget - totalSpent;

        System.out.println("--------- Budget Summary ---------");
        System.out.println("Total Budget: " + repository.totalBudget);
        System.out.println("Total Spent: " + totalSpent);
        System.out.println("Remaining Balance: " + remaining);

        if (remaining < (repository.totalBudget * 0.2)) {
            System.out.println("Warning: You're nearing your budget limit!");
        }

        pressAnyKeyToStart();
    }
}






