package com.taskboard.ui;

import com.taskboard.model.Team;

import javax.swing.*;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

public class MainPanel extends JPanel {
    // Equipos de la App. El sidebar tiene su propia copia, entonces los cambios se le pasan por su API
    private final List<Team> teams = new ArrayList<>();

    private final ContentPanel content = new ContentPanel();
    private final SidebarPanel sidebar;

    private int nextId = 1;  // La id no debe ser decidida por esta clase, lo deberia hacer SQL.

    public MainPanel(){
        setLayout(new BorderLayout());

        // Equipos hardcodeados. Esto no deberia de ir aca. (Solo demo)
        teams.add(new Team(nextId++, "Universidad", "U", 6));
        teams.add(new Team(nextId++, "Proyecto", "PR", 1));

        // El sidebar avisa cual equipo se selecciono y cuando el usuario quiere agregar uno
        sidebar = new SidebarPanel(teams, content::showTeam, this::askNewTeam);

        add(sidebar, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);
    }


    private void askNewTeam(){
        String name = JOptionPane.showInputDialog(this, "Nombre del equipo:", "Nuevo Equipo", JOptionPane.QUESTION_MESSAGE);
        if(name == null || name.isBlank()){
            return;
        }

        String formated = name.trim();
        Team team = new Team(nextId, formated, formated.substring(0,1), 0);

        sidebar.addTeam(team);
    }
}
