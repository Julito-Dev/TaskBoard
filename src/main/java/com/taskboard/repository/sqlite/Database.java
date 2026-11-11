package com.taskboard.repository.sqlite;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    // Esta clase crea una base de datos taskboard.db 

    private static final String URL = "jdbc:sqlite:taskboard.db";

    public static Connection getConnection() throws SQLException{
        Connection conn = DriverManager.getConnection(URL);

        // Habilita la foreign_key
        try(Statement stmt = conn.createStatement()){
            stmt.execute("PRAGMA foreign_keys = ON");
        }

        return conn;

    }

    public static void initialize(){

        String createTeams = """
        CREATE TABLE IF NOT EXISTS teams (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            icon TEXT
        )""";

        String createTasks = """
        CREATE TABLE IF NOT EXISTS tasks (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            team_id INTEGER NOT NULL,
            title TEXT NOT NULL,
            description TEXT,
            status TEXT NOT NULL,
            position INTEGER NOT NULL,
            FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE
        )""";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()){
            stmt.execute(createTeams);
            stmt.execute(createTasks);

        }
        catch (SQLException e){
            throw new RuntimeException("Database Initialization failed", e);
        }

    }

}
