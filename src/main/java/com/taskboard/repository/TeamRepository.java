package com.taskboard.repository;

import com.taskboard.model.Team;

import java.util.List;
import java.util.Optional;

public interface TeamRepository {

    Team save(Team team);

    Optional<Team> findById(int id);

    List<Team> findAll();

    void deleteById(int id);
}