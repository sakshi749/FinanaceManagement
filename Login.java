package com.mycompany.financemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Login {
    private JFrame frame;

    public Login() {
        showInitialScreen();
    }

    private void showInitialScreen() {
        frame = new JFrame("Finance Management System");
        frame.setSize(300, 150);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(2, 1));

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        frame.add(loginBtn);
        frame.add(registerBtn);

        loginBtn.addActionListener(e -> showLoginForm());
        registerBtn.addActionListener(e -> showRegisterForm());

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void showLoginForm() {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(3, 2));
        frame.setSize(300, 180);

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();
        JButton submitBtn = new JButton("Submit");
        JButton backBtn = new JButton("Back");

        frame.add(userLabel);
        frame.add(userField);
        frame.add(passLabel);
        frame.add(passField);
        frame.add(submitBtn);
        frame.add(backBtn);

        frame.revalidate();
        frame.repaint();

        submitBtn.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter username and password");
                return;
            }

            try {
                Connection conn = DBConnection.getConnection();
                String query = "SELECT * FROM users WHERE username = ? AND password = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, username);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(frame, "Login successful!");
                    frame.dispose();
                    new FmsGUI(username);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid credentials");
                }

                conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage());
            }
        });

        backBtn.addActionListener(e -> showInitialScreen());
    }

    private void showRegisterForm() {
    frame.getContentPane().removeAll();
    frame.setLayout(new GridLayout(4, 2));
    frame.setSize(350, 200);

    JLabel userLabel = new JLabel("Username:");
    JTextField userField = new JTextField();
    JLabel passLabel = new JLabel("Password:");
    JPasswordField passField = new JPasswordField();
    JButton registerBtn = new JButton("Register");
    JButton backBtn = new JButton("Back");

    frame.add(userLabel);
    frame.add(userField);
    frame.add(passLabel);
    frame.add(passField);
    frame.add(registerBtn);
    frame.add(backBtn);

    frame.revalidate();
    frame.repaint();

    registerBtn.addActionListener(e -> {
        String username = userField.getText().trim();
        String password = new String(passField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill all fields");
            return;
        }

        try {
            Connection conn = DBConnection.getConnection();

            // Check if username exists
            String checkQuery = "SELECT * FROM users WHERE username = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(frame, "Username already exists!");
            } else {
                // Insert into users table (without 'name' column)
                String insertQuery = "INSERT INTO users (username, password) VALUES (?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                insertStmt.setString(1, username);
                insertStmt.setString(2, password);
                insertStmt.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Registered successfully! You can now login.");
                showLoginForm();
            }

            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage());
        }
    });

    backBtn.addActionListener(e -> showInitialScreen());
}
}