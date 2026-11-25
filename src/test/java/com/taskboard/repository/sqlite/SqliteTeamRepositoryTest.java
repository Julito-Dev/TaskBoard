package com.taskboard.repository.sqlite;

import com.taskboard.model.Task;
import com.taskboard.model.TaskStatus;
import com.taskboard.model.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SqliteTeamRepositoryTest extends SqliteRepositoryTestBase {

    private SqliteTeamRepository teams;
    private SqliteTaskRepository tasks;

    @BeforeEach
    void setUpRepositories() {
        teams = new SqliteTeamRepository(database);
        tasks = new SqliteTaskRepository(database);
    }

    private Team saveTeam(String name) {
        return teams.save(new Team(0, name, name.substring(0, 1)));
    }

    private Task saveTask(Team team, String title, TaskStatus status, int position) {
        return tasks.save(new Task(0, team.getId(), title, null, status, position));
    }

    @Test
    void saveAssignsGeneratedIdAndLeavesOriginalUntouched() {
        Team draft = new Team(0, "Universidad", "U");

        Team saved = teams.save(draft);

        assertTrue(saved.getId() > 0);
        assertEquals(0, draft.getId());
        assertEquals("Universidad", saved.getName());
        assertEquals("U", saved.getIcon());
    }

    @Test
    void saveAssignsDistinctIds() {
        Team first = saveTeam("Uno");
        Team second = saveTeam("Dos");

        assertNotEquals(first.getId(), second.getId());
    }

    @Test
    void findAllReturnsEmptyListWhenDatabaseIsEmpty() {
        List<Team> result = teams.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllReturnsEverySavedTeam() {
        saveTeam("Uno");
        saveTeam("Dos");

        assertEquals(2, teams.findAll().size());
    }

    @Test
    void findByIdReturnsSavedTeam() {
        Team saved = saveTeam("Universidad");

        Optional<Team> found = teams.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(saved, found.get());
    }

    @Test
    void findByIdReturnsEmptyForUnknownId() {
        assertTrue(teams.findById(999).isEmpty());
    }

    @Test
    void updatePersistsChangesForExistingTeam() {
        Team saved = saveTeam("Viejo");

        teams.save(new Team(saved.getId(), "Nuevo", "N"));

        Team reloaded = teams.findById(saved.getId()).orElseThrow();
        assertEquals("Nuevo", reloaded.getName());
        assertEquals("N", reloaded.getIcon());
    }

    @Test
    void updateWithUnknownIdDoesNotCreateARow() {
        teams.save(new Team(999, "Fantasma", "F"));

        assertTrue(teams.findById(999).isEmpty());
        assertTrue(teams.findAll().isEmpty());
    }

    @Test
    void deleteRemovesTeam() {
        Team saved = saveTeam("Borrar");

        teams.delete(saved.getId());

        assertTrue(teams.findById(saved.getId()).isEmpty());
    }

    @Test
    void deleteWithUnknownIdDoesNotThrow() {
        assertDoesNotThrow(() -> teams.delete(999));
    }

    @Test
    void deleteCascadesToTheTeamsTasks() {
        Team saved = saveTeam("Con tareas");
        Task task = saveTask(saved, "Tarea", TaskStatus.TODO, 1);

        teams.delete(saved.getId());

        assertEquals(0, tasks.countByTeam(saved.getId()));
        assertTrue(tasks.findById(task.getId()).isEmpty());
    }

    @Test
    void namesWithQuotesAreStoredAndReadBack() {
        Team saved = saveTeam("O'Brien");

        assertEquals("O'Brien", teams.findById(saved.getId()).orElseThrow().getName());
    }

    @Test
    void unicodeNamesAreStoredAndReadBack() {
        Team saved = saveTeam("Programación");

        assertEquals("Programación", teams.findById(saved.getId()).orElseThrow().getName());
    }

    @Test
    void nullNameIsRejectedByTheNotNullConstraint() {
        assertThrows(RuntimeException.class, () -> teams.save(new Team(0, null, "X")));
    }

    @Test
    void nullIconIsAllowed() {
        Team saved = teams.save(new Team(0, "Sin icono", null));

        assertNull(teams.findById(saved.getId()).orElseThrow().getIcon());
    }

    @Test
    void duplicateNamesAreAllowed() {
        Team first = saveTeam("Repetido");
        Team second = saveTeam("Repetido");

        assertNotEquals(first.getId(), second.getId());
        assertEquals(2, teams.findAll().size());
    }

    @Test
    void dataSurvivesAReopen() {
        Team saved = saveTeam("Persistente");

        SqliteTeamRepository reopened = new SqliteTeamRepository(reopenDatabase());

        assertEquals(Optional.of(saved), reopened.findById(saved.getId()));
    }
}
