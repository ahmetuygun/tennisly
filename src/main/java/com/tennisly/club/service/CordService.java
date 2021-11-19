package com.tennisly.club.service;

import com.tennisly.club.domain.Cord;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Cord}.
 */
public interface CordService {
    /**
     * Save a cord.
     *
     * @param cord the entity to save.
     * @return the persisted entity.
     */
    Cord save(Cord cord);

    /**
     * Partially updates a cord.
     *
     * @param cord the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Cord> partialUpdate(Cord cord);

    /**
     * Get all the cords.
     *
     * @return the list of entities.
     */
    List<Cord> findAll();
    /**
     * Get all the Cord where Challenge is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Cord> findAllWhereChallengeIsNull();

    /**
     * Get the "id" cord.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Cord> findOne(Long id);

    /**
     * Delete the "id" cord.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
