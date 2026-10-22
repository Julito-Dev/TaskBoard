package com.taskboard;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.taskboard.ui.MainPanel;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame window = new JFrame("TaskBoard");
            window.setSize(900,600);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setLocationRelativeTo(null);
            window.setContentPane(new MainPanel());
            window.setVisible(true);
        });
    }
}