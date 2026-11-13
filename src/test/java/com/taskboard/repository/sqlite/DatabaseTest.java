package com.taskboard.repository.sqlite;

import com.taskboard.model.Team;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseTest extends SqliteRepositoryTestBase {

    @Test
    void initializeCreatesTeamsAndTasksTables() throws SQLException {
        try (Connection conn = database.getConnection(); Statement stmt = conn.createStatement()) {
            try (ResultSet teams = stmt.executeQuery("SELECT * FROM teams")) {
                assertTrue(teams.getMetaData().getColumnCount() > 0);
            }
            try (ResultSet tasks = stmt.executeQuery("SELECT * FROM tasks")) {
                assertTrue(tasks.getMetaData().getColumnCount() > 0);
            }
        }
    }

    @Test
    void initializeIsIdempotentAndKeepsExistingData() {
        SqliteTeamRepository teams = new SqliteTeamRepository(database);
        Team saved = teams.save(new Team(0, "Universidad", "U"));

        database.initialize();

        assertEquals(Optional.of(saved), teams.findById(saved.getId()));
        assertEquals(1, teams.findAll().size());
    }

    @Test
    void foreignKeysPragmaIsEnabledOnEveryConnection() throws SQLException {
        try (Connection conn = database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("PRAGMA foreign_keys")) {
            assertTrue(rs.next());
            assertEquals(1, rs.getInt(1));
        }
    }
}
