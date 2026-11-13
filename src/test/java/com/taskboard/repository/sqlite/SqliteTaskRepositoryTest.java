package com.taskboard.repository.sqlite;

import com.taskboard.model.Task;
import com.taskboard.model.TaskStatus;
import com.taskboard.model.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SqliteTaskRepositoryTest extends SqliteRepositoryTestBase {

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
        Team team = saveTeam("Equipo");
        Task draft = new Task(0, team.getId(), "Tarea", "Desc", TaskStatus.TODO, 1);

        Task saved = tasks.save(draft);

        assertTrue(saved.getId() > 0);
        assertEquals(0, draft.getId());
        assertEquals(team.getId(), saved.getTeamId());
    }

    @Test
    void saveRejectsTasksForUnknownTeams() {
        Task orphan = new Task(0, 999, "Huérfana", null, TaskStatus.TODO, 1);

        assertThrows(RuntimeException.class, () -> tasks.save(orphan));
    }

    @Test
    void findByTeamReturnsOnlyThatTeamsTasks() {
        Team first = saveTeam("Primero");
        Team second = saveTeam("Segundo");
        saveTask(first, "A", TaskStatus.TODO, 1);
        saveTask(first, "B", TaskStatus.TODO, 2);
        saveTask(second, "C", TaskStatus.TODO, 1);

        List<Task> result = tasks.findByTeam(first.getId());

        assertEquals(2, result.size());
        assertEquals(List.of("A", "B"), result.stream().map(Task::getTitle).toList());
    }

    @Test
    void findByTeamOrdersByPositionNotInsertionOrder() {
        Team team = saveTeam("Equipo");
        saveTask(team, "Tercera", TaskStatus.TODO, 3);
        saveTask(team, "Primera", TaskStatus.TODO, 1);
        saveTask(team, "Segunda", TaskStatus.TODO, 2);

        List<String> titles = tasks.findByTeam(team.getId()).stream().map(Task::getTitle).toList();

        assertEquals(List.of("Primera", "Segunda", "Tercera"), titles);
    }

    @Test
    void findByTeamReturnsEmptyListForUnknownTeam() {
        List<Task> result = tasks.findByTeam(999);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findByIdReturnsSavedTask() {
        Team team = saveTeam("Equipo");
        Task saved = saveTask(team, "Tarea", TaskStatus.IN_PROGRESS, 5);

        Optional<Task> found = tasks.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(saved, found.get());
        assertEquals(TaskStatus.IN_PROGRESS, found.get().getStatus());
        assertEquals(5, found.get().getPosition());
    }

    @Test
    void findByIdReturnsEmptyForUnknownId() {
        assertTrue(tasks.findById(999).isEmpty());
    }

    @Test
    void countByTeamReturnsZeroForEmptyTeam() {
        Team team = saveTeam("Vacío");

        assertEquals(0, tasks.countByTeam(team.getId()));
    }

    @Test
    void countByTeamCountsOnlyThatTeamsTasks() {
        Team first = saveTeam("Primero");
        Team second = saveTeam("Segundo");
        saveTask(first, "A", TaskStatus.TODO, 1);
        saveTask(first, "B", TaskStatus.DONE, 2);
        saveTask(second, "C", TaskStatus.TODO, 1);

        assertEquals(2, tasks.countByTeam(first.getId()));
        assertEquals(1, tasks.countByTeam(second.getId()));
    }

    @Test
    void updatePersistsStatusAndPosition() {
        Team team = saveTeam("Equipo");
        Task saved = saveTask(team, "Tarea", TaskStatus.TODO, 1);

        tasks.save(new Task(saved.getId(), team.getId(), saved.getTitle(), saved.getDescription(), TaskStatus.DONE, 7));

        Task reloaded = tasks.findById(saved.getId()).orElseThrow();
        assertEquals(TaskStatus.DONE, reloaded.getStatus());
        assertEquals(7, reloaded.getPosition());
    }

    @Test
    void updateWithUnknownIdDoesNotCreateARow() {
        Team team = saveTeam("Equipo");
        tasks.save(new Task(999, team.getId(), "Fantasma", null, TaskStatus.TODO, 1));

        assertTrue(tasks.findById(999).isEmpty());
        assertTrue(tasks.findByTeam(team.getId()).isEmpty());
    }

    @Test
    void deleteRemovesTask() {
        Team team = saveTeam("Equipo");
        Task saved = saveTask(team, "Borrar", TaskStatus.TODO, 1);

        tasks.delete(saved.getId());

        assertTrue(tasks.findById(saved.getId()).isEmpty());
        assertEquals(0, tasks.countByTeam(team.getId()));
    }

    @Test
    void deleteWithUnknownIdDoesNotThrow() {
        assertDoesNotThrow(() -> tasks.delete(999));
    }

    @Test
    void nullDescriptionIsAllowed() {
        Team team = saveTeam("Equipo");
        Task saved = saveTask(team, "Sin descripción", TaskStatus.TODO, 1);

        assertNull(tasks.findById(saved.getId()).orElseThrow().getDescription());
    }

    @ParameterizedTest
    @EnumSource(TaskStatus.class)
    void everyStatusRoundTrips(TaskStatus status) {
        Team team = saveTeam("Equipo");
        Task saved = saveTask(team, "Tarea " + status, status, 1);

        assertEquals(status, tasks.findById(saved.getId()).orElseThrow().getStatus());
    }

    @Test
    void dataSurvivesAReopen() {
        Team team = saveTeam("Equipo");
        Task saved = saveTask(team, "Persistente", TaskStatus.DONE, 1);

        SqliteTaskRepository reopened = new SqliteTaskRepository(reopenDatabase());

        assertEquals(Optional.of(saved), reopened.findById(saved.getId()));
    }
}
