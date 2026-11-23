package com.taskboard.ui;

import com.taskboard.model.Team;
import com.taskboard.model.Task;
import com.taskboard.repository.TeamRepository;
import com.taskboard.repository.TaskRepository;
import com.taskboard.model.TaskStatus;

import javax.swing.*;



import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

public class MainPanel extends JPanel {
    // Equipos de la App. El sidebar tiene su propia copia, entonces los cambios se le pasan por su API
    private final List<Team> teams = new ArrayList<>();    // ESTA LISTA ES LA CORRECTA
    private final TeamRepository teamRepository;
    private final TaskRepository taskRepository;


    private final ContentPanel content;
    private final SidebarPanel sidebar;

    public MainPanel(TeamRepository teamRepository, TaskRepository taskRepository){
        this.taskRepository = taskRepository;
        this.teamRepository = teamRepository;
        content = new ContentPanel(this::askNewTask, this::moveTask, this::editTask, this::deleteTask);

        setLayout(new BorderLayout());

        // Carga los equipos desde la base de datos
        teams.addAll(teamRepository.findAll());

        // El sidebar avisa cual equipo se selecciono y cuando el usuario quiere agregar uno
        sidebar = new SidebarPanel(teams, this::selectTeam, this::askNewTeam, this::taskCountFor);

        add(sidebar, BorderLayout.WEST);
        add(content, BorderLayout.CENTER);
    }

    private void selectTeam(Team team){
        content.showTeam(team, taskRepository.findByTeam(team.getId()));
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


    private void askNewTask(TaskStatus status){
        Team team = sidebar.getSelectedTeam();
        if (team == null){
            JOptionPane.showMessageDialog(this, "Selecciona un equipo primero");
            return;
        }

        JTextField titleField = new JTextField();
        JTextField descriptionField = new JTextField();

        JPanel form = new JPanel(new GridLayout(0,1,0,6));
        form.add(new JLabel("Título:"));
        form.add(titleField);
        form.add(new JLabel("Descripción (opcional):"));
        form.add(descriptionField);

        int option = JOptionPane.showConfirmDialog(this, form, "Nueva tarea", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option != JOptionPane.OK_OPTION){
            return;
        }

        String title = titleField.getText().trim();
        if(title.isBlank()){
            return;
        }

        String description = descriptionField.getText().trim();
        if (description.isBlank()){
            description = null;
        }

        int position = nextPosition(team.getId(), status);

        try{
            taskRepository.save(new Task(0, team.getId(), title, description, status, position));
            selectTeam(team);       // recarga el board desde la BD
            sidebar.setTeams(teams);    // actualiza el contador de tareas
        }

        catch (RuntimeException e){
            JOptionPane.showMessageDialog(this, "No se pudo guardar la tarea: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void moveTask(Task task, TaskStatus nweStatus){
        if (task == null || nweStatus == null || task.getStatus() == nweStatus){
            return;
        }

        Team team = sidebar.getSelectedTeam();
        if(team == null){
            return;
        }

        int position = nextPosition(team.getId(), nweStatus);

        try{
            taskRepository.save(task.withStatusAndPosition(nweStatus, position));
            selectTeam(team); 
        }
        catch (RuntimeException e){
            JOptionPane.showMessageDialog(this, "No se pudo mover la tarea: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editTask(Task task){
        if (task == null){
            return;
        }

        Team team = sidebar.getSelectedTeam();
        if (team == null){
            return;
            } 
        
        JTextField titleField = new JTextField(task.getTitle());
        JTextField descriptionField = new JTextField(task.getDescription() == null ? "" : task.getDescription());

        JPanel form = new JPanel(new GridLayout(0, 1, 0, 6));
        form.add(new JLabel("Título:"));
        form.add(titleField);
        form.add(new JLabel("Descripción:"));
        form.add(descriptionField);
        

        int option = JOptionPane.showConfirmDialog(this, form, "Editar Tarea", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option != JOptionPane.OK_OPTION){
            return;
        }

        String title = titleField.getText().trim();
        if (title.isBlank()){
            return;
        }

        String description = descriptionField.getText().trim();
        if (description.isBlank()){
            description = null;
        }

        try{
            taskRepository.save(task.withDetails(title, description));
            selectTeam(team);
        }

        catch (RuntimeException e){
            JOptionPane.showMessageDialog(this, "No se pudo editar la tarea: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void deleteTask(Task task){
        if (task == null){
            return;
        }

        Team team = sidebar.getSelectedTeam();
        if (team == null){
            return;
        }

        int option = JOptionPane.showConfirmDialog(this, "¿Eliminar la tarea \"" + task.getTitle() + "\"?", "Eliminar tarea", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (option != JOptionPane.YES_OPTION){
            return;
        }

        try {
            taskRepository.delete(task.getId());
            selectTeam(team);
            sidebar.setTeams(teams);
        }

        catch (RuntimeException e){
            JOptionPane.showMessageDialog(this, "No se pudo eliminar la tarea: "+ e.getMessage(), "Eliminar Tarea", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int nextPosition(int teamId, TaskStatus status){
        return taskRepository.findByTeam(teamId).stream().filter(t -> t.getStatus() == status).mapToInt(Task::getPosition).max().orElse(0) + 1;
    }

    private int taskCountFor(Team team){
        return taskRepository.countByTeam(team.getId());
    }
}
