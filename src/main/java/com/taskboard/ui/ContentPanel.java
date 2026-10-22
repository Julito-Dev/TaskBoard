package com.taskboard.ui;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

public class ContentPanel extends JPanel{
    private final JLabel title;

    public ContentPanel(){
        setLayout(new BorderLayout());
        setBackground(new Color(35, 41, 58));

        title = new JLabel("Selecciona un equipo", SwingConstants.CENTER);
        title.setForeground(new Color(231,235,244));
        title.setFont(new Font("SansSerif", Font.BOLD, 22));

        add(title, BorderLayout.CENTER);
    }

    public void showTeam(String name){
        // Muestra el equipo que el usuario selecciono en el Sidebar
        title.setText(name);
    }
}
