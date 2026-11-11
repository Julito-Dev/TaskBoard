package com.taskboard.ui;

import com.taskboard.model.Team;
import com.taskboard.repository.TeamRepository;
import com.taskboard.repository.TaskRepository;

import javax.swing.*;



import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

public class MainPanel extends JPanel {
    // Equipos de la App. El sidebar tiene su propia copia, entonces los cambios se le pasan por su API
    private final List<Team> teams = new ArrayList<>();    // ESTA LISTA ES LA CORRECTA
    private final TeamRepository teamRepository;
    private final TaskRepository taskRepository;


    private final ContentPanel content = new ContentPanel();
    private final SidebarPanel sidebar;

    public MainPanel(TeamRepository teamRepository, TaskRepository taskRepository){
        this.taskRepository = taskRepository;
        this.teamRepository = teamRepository;

        setLayout(new BorderLayout());

        // Carga los equipos desde la base de datos
        teams.addAll(teamRepository.findAll());

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
        
        try{
            Team saved = teamRepository.save(new Team(0, formated, formated.substring(0,1)));
            teams.add(saved); // Guarda solo si tuvo exito el save
            sidebar.setTeams(teams);
            sidebar.selectTeam(saved);
        }
        catch (RuntimeException e){
            JOptionPane.showMessageDialog(this, "No se pudo guardar el equipo: " + e.getMessage());
        }

    }

    private int taskCountFor(Team team){
        return taskRepository.countByTeam(team.getId());
    }
}
