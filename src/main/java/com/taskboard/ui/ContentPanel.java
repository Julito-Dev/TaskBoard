package com.taskboard.ui;

import src.main.java.com.taskboard.ui.BoardPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

public class ContentPanel extends JPanel{
    private final JLabel title;

    public ContentPanel(){
        setLayout(new BorderLayout());
        setBackground(new Color(35, 41, 58));

        title = new JLabel("Selecciona un equipo");
        title.setForeground(new Color(231,235,244));
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setBorder(new EmptyBorder(24, 24, 16, 24));

        add(title, BorderLayout.NORTH);
        add(new BoardPanel(), BorderLayout.CENTER);
    }

    public void showTeam(String name){
        // Muestra el equipo que el usuario selecciono en el Sidebar
        title.setText(name);
    }
}
