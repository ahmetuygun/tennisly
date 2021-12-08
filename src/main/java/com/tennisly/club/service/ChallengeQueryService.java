package com.tennisly.club.service;

import com.tennisly.club.domain.*; // for static metamodels
import com.tennisly.club.domain.Challenge;
import com.tennisly.club.repository.ChallengeRepository;
import com.tennisly.club.repository.PlayerRepository;
import com.tennisly.club.repository.UserRepository;
import com.tennisly.club.security.SecurityUtils;
import com.tennisly.club.service.criteria.ChallengeCriteria;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Challenge} entities in the database.
 * The main input is a {@link ChallengeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Challenge} or a {@link Page} of {@link Challenge} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ChallengeQueryService extends QueryService<Challenge> {

    private final Logger log = LoggerFactory.getLogger(ChallengeQueryService.class);

    private final ChallengeRepository challengeRepository;

    public ChallengeQueryService(ChallengeRepository challengeRepository) {
        this.challengeRepository = challengeRepository;
    }

    /**
     * Return a {@link List} of {@link Challenge} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Challenge> findByCriteria(ChallengeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Challenge> specification = createSpecification(criteria);
        return challengeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Challenge} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */

    @Autowired
    UserService userService;

    @Autowired
    PlayerRepository playerRepository;

    @Transactional(readOnly = true)
    public Page<Challenge> findByCriteria(ChallengeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Challenge> specification = createSpecification(criteria);

        final Optional<User> user = userService.getUserWithAuthorities();
        if (user.isPresent()) {
            Optional<Player> player = playerRepository.findOneByInternalUser_Id(user.get().getId());
            if (player.isPresent()) {
                return challengeRepository.findAllByAcceptor_IdOrProposer_Id(player.get().getId(), player.get().getId(), page);
            }
        }
        return Page.empty();
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ChallengeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Challenge> specification = createSpecification(criteria);
        return challengeRepository.count(specification);
    }

    /**
     * Function to convert {@link ChallengeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Challenge> createSpecification(ChallengeCriteria criteria) {
        Specification<Challenge> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Challenge_.id));
            }
            if (criteria.getMatchTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMatchTime(), Challenge_.matchTime));
            }
            if (criteria.getChallengeStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getChallengeStatus(), Challenge_.challengeStatus));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Challenge_.status));
            }

            if (criteria.getCordId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCordId(), root -> root.join(Challenge_.cord, JoinType.LEFT).get(Cord_.id))
                    );
            }
            if (criteria.getProposerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProposerId(), root -> root.join(Challenge_.proposer, JoinType.LEFT).get(Player_.id))
                    );
            }
            if (criteria.getAcceptorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAcceptorId(), root -> root.join(Challenge_.acceptor, JoinType.LEFT).get(Player_.id))
                    );
            }
        }
        return specification;
    }
}
