package com.tennisly.club.service;

import com.tennisly.club.domain.Challenge;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Challenge}.
 */
public interface ChallengeService {
    /**
     * Save a challenge.
     *
     * @param challenge the entity to save.
     * @return the persisted entity.
     */
    Challenge save(Challenge challenge);

    /**
     * Partially updates a challenge.
     *
     * @param challenge the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Challenge> partialUpdate(Challenge challenge);

    /**
     * Get all the challenges.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Challenge> findAll(Pageable pageable);

    /**
     * Get the "id" challenge.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Challenge> findOne(Long id);

    /**
     * Delete the "id" challenge.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
