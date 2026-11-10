package com.taskboard.repository;

import com.taskboard.model.Task;

import java.util.List;
import java.util.Optional;

/**
 * Contract for storing and retrieving {@link Task} instances.
 */
public interface TaskRepository {

    /**
     * Returns the tasks that belong to a team.
     *
     * @param teamId the team id
     * @return the team's tasks, or an empty list if there are none
     */
    List<Task> findByTeam(int teamId);

    /**
     * Finds a task by its id.
     *
     * @param id the task id
     * @return the task, or an empty {@link Optional} if it does not exist
     */
    Optional<Task> findById(int id);

    /**
     * Saves a task.
     * <p>
     * Convention: if {@code task.getId() == 0} the task is inserted and the
     * storage assigns its id; otherwise the existing task with that id is updated.
     *
     * @param task the task to save
     * @return the saved task, with its id assigned when it was an insert
     */
    Task save(Task task);

    /**
     * Deletes the task with the given id.
     *
     * @param id the task id
     */
    void delete(int id);

    /**
     * Counts the tasks that belong to a team.
     *
     * @param teamId the team id
     * @return the number of tasks of that team
     */
    int countByTeam(int teamId);
}