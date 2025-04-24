package com.mycompany.financemanagementsystem;

import java.util.Scanner;
import javax.swing.SwingUtilities;

public class StartApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean keepRunning = true;

        while (keepRunning) {
            System.out.println("\n=== Welcome to Finance Management System ===");
            System.out.println("1. Text-based interface");
            System.out.println("2. GUI-based interface (Login)");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine(); // Consume leftover newline

            switch (choice) {
                case 1 -> {
                    Services service = new Services();
                    service.showMenu(); // CLI mode
                }
                case 2 -> {
                    // Always run Swing GUI in the Event Dispatch Thread
                    SwingUtilities.invokeLater(() -> {
                        new Login(); // Show login GUI
                    });
                }
                case 3 -> {
                    System.out.println("Exiting application. Goodbye!");
                    keepRunning = false;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }

        sc.close();
    }
}




