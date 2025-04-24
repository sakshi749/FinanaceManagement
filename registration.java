package com.mycompany.financemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class registration {
    private JFrame frame;

    public registration() {
        showRegistrationForm();
    }

    private void showRegistrationForm() {
        frame = new JFrame("User Registration");
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(4, 2));

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

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        registerBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill all fields");
                return;
            }

            try {
                Connection conn = DBConnection.getConnection();

                // Check if username already exists
                Statement checkStmt = conn.createStatement();
                ResultSet rs = checkStmt.executeQuery(
                        "SELECT * FROM users WHERE username = '" + username + "'");

                if (rs.next()) {
                    JOptionPane.showMessageDialog(frame, "Username already exists!");
                } else {
                    Statement insertStmt = conn.createStatement();
                    insertStmt.executeUpdate("INSERT INTO users (username, password) VALUES ('" + username + "', '" + password + "')");

                    JOptionPane.showMessageDialog(frame, "Registered successfully! You can now login.");
                    frame.dispose();
                    new Login(); // Open login window
                }

                conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage());
            }
        });

        backBtn.addActionListener(e -> {
            frame.dispose();
            new Login(); // Go back to login window
        });
    }
}
