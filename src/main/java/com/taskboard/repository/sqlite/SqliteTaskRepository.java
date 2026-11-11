package com.taskboard.repository.sqlite;


import com.taskboard.repository.TaskRepository;
import com.taskboard.model.Task;
import com.taskboard.model.TaskStatus;
import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;


public class SqliteTaskRepository implements TaskRepository {
    

    // Metodo para guardar - actualizar
    @Override 
    public Task save(Task task){
        
        if(task.getId() == 0){
            String save = """
            INSERT INTO tasks (team_id, title, description, status, position) VALUES (?,?,?,?,?)""";

            
            try (Connection conn = Database.getConnection(); PreparedStatement psmt = conn.prepareStatement(save, Statement.RETURN_GENERATED_KEYS)){

                psmt.setInt(1, task.getTeamId());
                psmt.setString(2, task.getTitle());
                psmt.setString(3, task.getDescription());
                psmt.setString(4, task.getStatus().name());
                psmt.setInt(5, task.getPosition());
                psmt.executeUpdate();
                
                // No hace falta agregar la foreign key porque Sqlite ya lo hace automaticamente

                // Obtenemos el id para retornar la nueva tarea
                try(ResultSet key = psmt.getGeneratedKeys()){
                    key.next();
                    int generated = key.getInt(1);

                    return new Task(generated, task.getTeamId(), task.getTitle(), task.getDescription(), task.getStatus(), task.getPosition());
                }
            }

            catch (SQLException e){
                    throw new RuntimeException("Insert tasks Failed", e);
                }
        }


        // Logica de UPDATE
        String update = """
        UPDATE tasks SET title= ?, description= ?, status= ?, position= ? WHERE id = ?""";

        try (Connection conn = Database.getConnection(); PreparedStatement psmt = conn.prepareStatement(update)){
            psmt.setString(1, task.getTitle());
            psmt.setString(2, task.getDescription());
            psmt.setString(3, task.getStatus().name());
            psmt.setInt(4, task.getPosition());
            psmt.setInt(5, task.getId());  // FILTRO POR ID
            psmt.executeUpdate();
            return task;
        }

        catch (SQLException e){
            throw new RuntimeException("Update Task Failed", e);
        }
    }


    //Metodo para obtener todas las tareas por equipo
    @Override
    public List<Task> findByTeam(int teamId){
        List<Task> tasks = new ArrayList<>();

        String findByTeam = """
        SELECT * FROM tasks WHERE team_id = ? ORDER BY position""";

        try (Connection conn = Database.getConnection(); PreparedStatement psmt = conn.prepareStatement(findByTeam)){

            psmt.setInt(1, teamId);

            try(ResultSet data = psmt.executeQuery()){
                while (data.next()){
                    Task task = new Task(data.getInt("id"), data.getInt("team_id"), data.getString("title"), data.getString("description"), TaskStatus.valueOf(data.getString("status")), data.getInt("position"));

                    tasks.add(task);
                }
            }
        }

        catch (SQLException e){
            throw new RuntimeException("Error retrieving the tasks", e);
        }

        return tasks;
    }


    // Metodo para encontrar por ID

    @Override
    public Optional<Task> findById(int id){
        String findById = """
        SELECT * FROM tasks WHERE id = ?""";

        try (Connection conn = Database.getConnection(); PreparedStatement psmt = conn.prepareStatement(findById)){

            psmt.setInt(1, id);

            try (ResultSet data = psmt.executeQuery()){
                if (data.next()) {
                    Task task = new Task(data.getInt("id"), data.getInt("team_id"), data.getString("title"), data.getString("description"), TaskStatus.valueOf(data.getString("status")), data.getInt("position"));
                    return Optional.of(task);
                }
            }
        }
        catch (SQLException e){
            throw new RuntimeException("Error searching the task", e);
        }

        return Optional.empty();
    }

    // Metodo para contar tareas por equipo

    @Override 
    public int countByTeam(int teamId){

        // Filtrar por equipo
        String findByTeam = """
        SELECT COUNT(*) FROM tasks WHERE team_id = ?
        """;

        try (Connection conn = Database.getConnection(); PreparedStatement psmt = conn.prepareStatement(findByTeam)) {

        psmt.setInt(1, teamId);

        try (ResultSet data = psmt.executeQuery()) {
            if (data.next()) {
                return data.getInt(1);
            }
            return 0;
        }

        } 
        catch (SQLException e) {
            throw new RuntimeException("Error counting the tasks", e);
        }
    }

    // Metodo para borrar tareas por id

    @Override 
    public void delete(int id){
        String delete = """
        DELETE FROM tasks WHERE id = ?""";

        try (Connection conn = Database.getConnection(); PreparedStatement psmt = conn.prepareStatement(delete)){

            psmt.setInt(1, id);
            psmt.executeUpdate();
        }
        catch(SQLException e){
            throw new RuntimeException("Error deleting the task", e);
        }
    }   
} 

