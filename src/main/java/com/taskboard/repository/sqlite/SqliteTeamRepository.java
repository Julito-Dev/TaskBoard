package com.taskboard.repository.sqlite;

import java.sql.*;
import com.taskboard.model.Team;
import com.taskboard.repository.TeamRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqliteTeamRepository implements TeamRepository {
    // Esta clase es la implementacion de la interfaz para el almacenamiento de equipos.

    private final Database database;

    public SqliteTeamRepository(Database database) {
        this.database = database;
    }

    // Metodo guardar - actualizar
    @Override 
    public Team save(Team team){

        if (team.getId() == 0 ){   // id = 0 representa un objeto recien creado. No hay problema ya que AUTOINCREMENT incia en 1, asi que solo existe un objeto con id=0 antes de insertarse a la tabla.
            String insert = """
            INSERT INTO teams (name, icon) VALUES (?,?)""";  //solo 2 campos porque Sqlite asigna automaticamente el ID
            
            try (Connection conn = database.getConnection(); PreparedStatement psmt = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)){
                
                psmt.setString(1, team.getName());
                psmt.setString(2, team.getIcon());

                psmt.executeUpdate();

                // Obtener el ID generado para retornar el nuevo equipo.
                try(ResultSet keys = psmt.getGeneratedKeys()){
                    keys.next();
                    int generated = keys.getInt(1);  // devuelve la primera columna

                    // Devolvemos el "nuevo" objeto
                    return new Team(generated, team.getName(), team.getIcon());  // Este objeto lo usara el MainPanel en algun punto
                }
            }

            catch (SQLException e){
                throw new RuntimeException("Insert Failed", e);
            }

        }

        // Logica de UPDATE 
        
        String update = """
        UPDATE teams SET name= ?, icon= ? WHERE id = ?""";

        try (Connection conn = database.getConnection(); PreparedStatement psmt = conn.prepareStatement(update)) {

            psmt.setString(1, team.getName()); // Actualiza el nombre
            psmt.setString(2, team.getIcon()); // Actualiza el icono
            psmt.setInt(3, team.getId()); // Como el equipo ya existia, entonces este campo solo es un filtro para el query.
            psmt.executeUpdate();
            return team;
        }

        catch (SQLException e){
            throw new RuntimeException("Update Team failed", e);
        }
    }


    // Obtener todos los equipos. Retorna una lista 

    @Override 
    public List<Team> findAll(){

        List<Team> teams = new ArrayList<>();

        String selectAll = """
        SELECT * FROM teams""";

        try(Connection conn = database.getConnection(); PreparedStatement psmt = conn.prepareStatement(selectAll); ResultSet data = psmt.executeQuery()){

            // Armar el objeto a partir de sus datos guardados
            while(data.next()){

                Team team = new Team(data.getInt("id"), data.getString("name"), data.getString("icon"));

                teams.add(team);
            }
        }

        catch (SQLException e){
            throw new RuntimeException("Error retrieving the Teams", e);
        }

        return teams;
    }


    // Metodo para buscar por ID

    @Override 
    public Optional<Team> findById(int id){
        String findId = """
        SELECT * FROM teams WHERE id = ?""";

        try(Connection conn = database.getConnection(); PreparedStatement psmt = conn.prepareStatement(findId)){

            psmt.setInt(1, id);

            try(ResultSet data = psmt.executeQuery()) {
                if (data.next()){
                    Team team = new Team(data.getInt("id"), data.getString("name"), data.getString("icon"));
                    return Optional.of(team);
                }
            }

        }
        catch (SQLException e){
            throw new RuntimeException("Error", e);
        }

        // Si el objeto no existe entonces Optional queda vacio. No necesita validacion
        return Optional.empty();
    }

    // Logica para el DELETE

    @Override 
    public void delete(int id){
        String delete = """
        DELETE FROM teams WHERE id = ?""";

        try(Connection conn = database.getConnection(); PreparedStatement psmt = conn.prepareStatement(delete)){

            psmt.setInt(1, id);
            psmt.executeUpdate();
        }

        catch (SQLException e){
            throw new RuntimeException("Error deleting the team", e);
        }
    }

}
