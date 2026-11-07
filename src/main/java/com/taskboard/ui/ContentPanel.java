package com.taskboard.ui;


import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.taskboard.model.Team;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

public class ContentPanel extends JPanel{

    // Colores utiles
     private static final Color BACKGROUND = new Color(23,28,40);
    private static final Color HOVER = new Color(42,52,74);
    private static final Color SELECTED = new Color(48,59,83);
    private static final Color TEXT = new Color(231, 235, 244);
    private static final Color MUTED = new Color(151, 163, 185);
    private static final Color ACCENT = new Color(126, 151, 255);


    private final JLabel title;

    // This fields are used to show the team info
    private final JLabel nameLabel = new JLabel();
    private final JLabel idLabel = new JLabel();
    private final JLabel taskcountLabel = new JLabel();

    public ContentPanel(){
        setLayout(new BorderLayout());
        setBackground(new Color(35, 41, 58));

        title = new JLabel("Selecciona un equipo");
        title.setForeground(new Color(231,235,244));
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setBorder(new EmptyBorder(24, 24, 16, 24));

        add(title, BorderLayout.NORTH);
        
        // Team Info Panel

        JPanel teamInfoPanel = new JPanel();
        teamInfoPanel.setLayout(new BoxLayout(teamInfoPanel, BoxLayout.Y_AXIS));
        teamInfoPanel.setOpaque(false);
        teamInfoPanel.setBorder(new EmptyBorder(0,24,24,24));

        teamInfoPanel.add(nameLabel);
        teamInfoPanel.add(Box.createVerticalStrut(8));
        teamInfoPanel.add(idLabel);
        teamInfoPanel.add(Box.createVerticalStrut(8));
        teamInfoPanel.add(taskcountLabel);

        
        add(teamInfoPanel, BorderLayout.CENTER);
    }


    public void showTeam(Team team){
        // Muestra el equipo y su informacion en el Sidebar
        title.setText(team.getName());
        nameLabel.setText("Nombre: " + team.getName());
        nameLabel.setForeground(TEXT);
        idLabel.setText("ID: " + team.getId());
        idLabel.setForeground(TEXT);
        taskcountLabel.setText("Tareas: 0"); //ES el unico valor hardcodeado, porque aun no se implementa un repositorio para las tareas.
        taskcountLabel.setForeground(TEXT);
    }
}
