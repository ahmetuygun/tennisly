package com.tennisly.club.service;

import com.tennisly.club.domain.Player;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Player}.
 */
public interface PlayerService {
    /**
     * Save a player.
     *
     * @param player the entity to save.
     * @return the persisted entity.
     */
    Player save(Player player);

    /**
     * Partially updates a player.
     *
     * @param player the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Player> partialUpdate(Player player);

    /**
     * Get all the players.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Player> findAll(Pageable pageable);

    /**
     * Get the "id" player.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Player> findOne(Long id);

    Optional<Player> findByLogin(String login);

    /**
     * Delete the "id" player.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
