package com.taskboard.ui;

import com.taskboard.model.Team;

import javax.swing.*;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

public class MainPanel extends JPanel {
    // Equipos de la App. El sidebar tiene su propia copia, entonces los cambios se le pasan por su API
    private final List<Team> teams = new ArrayList<>();    // ESTA LISTA ES LA CORRECTA

    private final ContentPanel content = new ContentPanel();
    private final SidebarPanel sidebar;

    private int nextId = 1;  // La id no debe ser decidida por esta clase, lo deberia hacer SQL.

    public MainPanel(){
        setLayout(new BorderLayout());

        // Equipos hardcodeados. Esto no deberia de ir aca. (Solo demo)
        teams.add(new Team(nextId++, "Universidad", "U"));
        teams.add(new Team(nextId++, "Proyecto", "PR"));

        // El sidebar avisa cual equipo se selecciono y cuando el usuario quiere agregar uno
        sidebar = new SidebarPanel(teams, content::showTeam, this::askNewTeam, this::taskCountFor);

        add(sidebar, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);
    }


    private void askNewTeam(){
        // Dispara el mensaje para agregar un equipo nuevo
        String name = JOptionPane.showInputDialog(this, "Nombre del equipo:", "Nuevo Equipo", JOptionPane.QUESTION_MESSAGE);
        if(name == null || name.isBlank()){
            return;
        }

        String formated = name.trim();
        Team team = new Team(nextId++, formated, formated.substring(0,1));

        teams.add(team);   //Agrega a la unica lista verdadera 
        sidebar.setTeams(teams);  // Crea la snapshot de la lista y los dibuja
        sidebar.selectTeam(team); // Selecciona el equipo creado automaticamente
    }

    private int taskCountFor(Team team){
        // M1: No existe taksRepository en esta Milestone. TaskRepository.getCountByID(team.getID()) se encargara de este metodo.
        return 0;
    }
}
