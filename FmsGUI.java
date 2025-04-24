package com.mycompany.financemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class FmsGUI {
    private String username;

    public FmsGUI(String username) {
        this.username = username;

        // Main frame
        JFrame frame = new JFrame("Finance Management System");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        frame.add(welcomeLabel, BorderLayout.NORTH);

        // Center panel with buttons
        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        JButton userButton = new JButton("Balance Overview");
        JButton budgetButton = new JButton("Budget");
        JButton expensesButton = new JButton("Expenses");
        JButton reportButton = new JButton("View Reports");

        centerPanel.add(userButton);
        centerPanel.add(budgetButton);
        centerPanel.add(expensesButton);
        centerPanel.add(reportButton);
        frame.add(centerPanel, BorderLayout.CENTER);

        // Exit button
        JButton exitButton = new JButton("Exit");
        frame.add(exitButton, BorderLayout.SOUTH);
        exitButton.addActionListener(e -> frame.dispose());

        // Button Actions
        userButton.addActionListener(e -> openBalanceOverview());
        budgetButton.addActionListener(e -> openBudgetInput());
        expensesButton.addActionListener(e -> openExpensesInput());
        reportButton.addActionListener(e -> openReportView());

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void openBalanceOverview() {
        JFrame balanceFrame = new JFrame("Balance Overview");
        balanceFrame.setSize(350, 200);
        balanceFrame.setLayout(new GridLayout(3, 1));

        try (Connection conn = DBConnection.getConnection()) {
            String userQuery = "SELECT id FROM users WHERE username = ?";
            PreparedStatement userStmt = conn.prepareStatement(userQuery);
            userStmt.setString(1, username);
            ResultSet userRs = userStmt.executeQuery();

            if (userRs.next()) {
                int userId = userRs.getInt("id");

                String budgetQuery = "SELECT total_budget FROM budgets WHERE user_id = ?";
                PreparedStatement budgetStmt = conn.prepareStatement(budgetQuery);
                budgetStmt.setInt(1, userId);
                ResultSet budgetRs = budgetStmt.executeQuery();

                String expenseQuery = "SELECT SUM(amount) as total_expense FROM expenses WHERE user_id = ?";
                PreparedStatement expenseStmt = conn.prepareStatement(expenseQuery);
                expenseStmt.setInt(1, userId);
                ResultSet expenseRs = expenseStmt.executeQuery();

                if (budgetRs.next()) {
                    double budget = budgetRs.getDouble("total_budget");
                    double expenses = 0;
                    if (expenseRs.next()) {
                        expenses = expenseRs.getDouble("total_expense");
                    }
                    double balance = budget - expenses;
                    JLabel balanceLabel = new JLabel("Remaining Balance: Rs. " + balance);
                    JLabel remarkLabel = new JLabel();

                    if (balance > (0.5 * budget)) {
                        remarkLabel.setText("Great! You are managing your finances well.");
                    } else if (balance > 0) {
                        remarkLabel.setText("Caution: You are nearing your budget limit.");
                    } else {
                        remarkLabel.setText("Alert! You have exceeded your budget.");
                    }

                    balanceFrame.add(new JLabel("Total Budget: Rs. " + budget));
                    balanceFrame.add(new JLabel("Total Expenses: Rs. " + expenses));
                    balanceFrame.add(balanceLabel);
                    balanceFrame.add(remarkLabel);
                } else {
                    JOptionPane.showMessageDialog(balanceFrame, "No budget data found for user.");
                }
            } else {
                JOptionPane.showMessageDialog(balanceFrame, "User not found.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(balanceFrame, "Database error: " + ex.getMessage());
        }

        balanceFrame.setLocationRelativeTo(null);
        balanceFrame.setVisible(true);
    }

    private void openBudgetInput() {
        JFrame budgetFrame = new JFrame("Set Budget");
        budgetFrame.setSize(300, 150);
        budgetFrame.setLayout(new GridLayout(2, 2));

        JLabel amountLabel = new JLabel("Total Budget:");
        JTextField amountField = new JTextField();
        JButton saveBtn = new JButton("Save");

        budgetFrame.add(amountLabel);
        budgetFrame.add(amountField);
        budgetFrame.add(new JLabel());
        budgetFrame.add(saveBtn);

        saveBtn.addActionListener(e -> {
            String budgetStr = amountField.getText();
            if (budgetStr.isEmpty()) {
                JOptionPane.showMessageDialog(budgetFrame, "Please enter a total budget.");
                return;
            }

            try (Connection conn = DBConnection.getConnection()) {
                String userQuery = "SELECT id FROM users WHERE username = ?";
                PreparedStatement userStmt = conn.prepareStatement(userQuery);
                userStmt.setString(1, username);
                ResultSet rs = userStmt.executeQuery();

                if (rs.next()) {
                    int userId = rs.getInt("id");
                    String insertQuery = "INSERT INTO budgets (user_id, total_budget) VALUES (?, ?)";
                    PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                    insertStmt.setInt(1, userId);
                    insertStmt.setDouble(2, Double.parseDouble(budgetStr));
                    insertStmt.executeUpdate();

                    JOptionPane.showMessageDialog(budgetFrame, "Budget saved successfully!");
                } else {
                    JOptionPane.showMessageDialog(budgetFrame, "User not found.");
                }
            } catch (SQLIntegrityConstraintViolationException ex) {
                JOptionPane.showMessageDialog(budgetFrame, "Budget for this user already exists.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(budgetFrame, "Database error: " + ex.getMessage());
            }
        });

        budgetFrame.setLocationRelativeTo(null);
        budgetFrame.setVisible(true);
    }

    private void openExpensesInput() {
        JFrame expensesFrame = new JFrame("Add Expenses");
        expensesFrame.setSize(300, 200);
        expensesFrame.setLayout(new GridLayout(3, 2));

        JLabel descLabel = new JLabel("Description:");
        JTextField descField = new JTextField();

        JLabel amountLabel = new JLabel("Amount:");
        JTextField amountField = new JTextField();

        JButton saveBtn = new JButton("Save");

        expensesFrame.add(descLabel);
        expensesFrame.add(descField);
        expensesFrame.add(amountLabel);
        expensesFrame.add(amountField);
        expensesFrame.add(new JLabel());
        expensesFrame.add(saveBtn);

        saveBtn.addActionListener(e -> {
            String description = descField.getText();
            String amountStr = amountField.getText();

            if (description.isEmpty() || amountStr.isEmpty()) {
                JOptionPane.showMessageDialog(expensesFrame, "Please fill all fields.");
                return;
            }

            try (Connection conn = DBConnection.getConnection()) {
                String userQuery = "SELECT id FROM users WHERE username = ?";
                PreparedStatement userStmt = conn.prepareStatement(userQuery);
                userStmt.setString(1, username);
                ResultSet rs = userStmt.executeQuery();

                if (rs.next()) {
                    int userId = rs.getInt("id");
                    String insertQuery = "INSERT INTO expenses (user_id, description, amount) VALUES (?, ?, ?)";
                    PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                    insertStmt.setInt(1, userId);
                    insertStmt.setString(2, description);
                    insertStmt.setDouble(3, Double.parseDouble(amountStr));
                    insertStmt.executeUpdate();

                    JOptionPane.showMessageDialog(expensesFrame, "Expense saved successfully!");
                } else {
                    JOptionPane.showMessageDialog(expensesFrame, "User not found.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(expensesFrame, "Database error: " + ex.getMessage());
            }
        });

        expensesFrame.setLocationRelativeTo(null);
        expensesFrame.setVisible(true);
    }

    private void openReportView() {
        JFrame reportFrame = new JFrame("Reports");
        reportFrame.setSize(400, 300);
        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);

        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT e.description, e.amount FROM expenses e JOIN users u ON e.user_id = u.id WHERE u.username = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            StringBuilder sb = new StringBuilder();
            sb.append("Description\tAmount\n");
            sb.append("----------------------\n");
            while (rs.next()) {
                sb.append(rs.getString("description")).append("\t").append(rs.getDouble("amount")).append("\n");
            }
            reportArea.setText(sb.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(reportFrame, "Database error: " + ex.getMessage());
        }

        reportFrame.add(new JScrollPane(reportArea));
        reportFrame.setLocationRelativeTo(null);
        reportFrame.setVisible(true);
    }
}