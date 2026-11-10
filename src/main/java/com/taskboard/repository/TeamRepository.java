package com.taskboard.repository;

import com.taskboard.model.Team;

import java.util.List;
import java.util.Optional;

/**
 * Contract for storing and retrieving {@link Team} instances.
 */
public interface TeamRepository {

    /**
     * Returns every stored team.
     *
     * @return all teams, or an empty list if there are none
     */
    List<Team> findAll();

    /**
     * Finds a team by its id.
     *
     * @param id the team id
     * @return the team, or an empty {@link Optional} if it does not exist
     */
    Optional<Team> findById(int id);

    /**
     * Saves a team.
     * <p>
     * Convention: if {@code team.getId() == 0} the team is inserted and the
     * storage assigns its id; otherwise the existing team with that id is updated.
     *
     * @param team the team to save
     * @return the saved team, with its id assigned when it was an insert
     */
    Team save(Team team);

    /**
     * Deletes the team with the given id.
     *
     * @param id the team id
     */
    void delete(int id);
}