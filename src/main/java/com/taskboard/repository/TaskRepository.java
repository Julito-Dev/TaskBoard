package com.taskboard.repository;

import com.taskboard.model.Task;
import com.taskboard.model.TaskStatus;

import java.util.List;
import java.util.Optional;


public interface TaskRepository {

    Task save(Task task);

    Optional<Task> findById(int id);

    List<Task> findByTeamId(int teamId);

    List<Task> findByTeamIdAndStatus(int teamId, TaskStatus status);

    void deleteById(int id);
}