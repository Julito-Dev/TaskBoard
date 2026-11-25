package com.taskboard;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.taskboard.ui.MainPanel;
import com.taskboard.repository.sqlite.SqliteTaskRepository;
import com.taskboard.repository.sqlite.SqliteTeamRepository;
import com.taskboard.repository.TeamRepository;
import com.taskboard.repository.TaskRepository;
import com.taskboard.repository.sqlite.Database;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // inicializar la BD
            Database database = new Database("jdbc:sqlite:taskboard.db");
            try{
                database.initialize();
            }
            catch(RuntimeException e){
                JOptionPane.showMessageDialog(null, "No se pudo abrir la base de datos " + e.getMessage());
                return;
            }


            TeamRepository teamRepository = new SqliteTeamRepository(database);
            TaskRepository taskRepository = new SqliteTaskRepository(database);
            
            // Crear la ventana
            JFrame window = new JFrame("TaskBoard");
            window.setSize(900,600);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setLocationRelativeTo(null);
            window.setContentPane(new MainPanel(teamRepository, taskRepository));
            window.setVisible(true);
        });
    }
}