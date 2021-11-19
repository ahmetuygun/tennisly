package com.tennisly.club.service.impl;

import com.tennisly.club.domain.Challenge;
import com.tennisly.club.repository.ChallengeRepository;
import com.tennisly.club.service.ChallengeService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Challenge}.
 */
@Service
@Transactional
public class ChallengeServiceImpl implements ChallengeService {

    private final Logger log = LoggerFactory.getLogger(ChallengeServiceImpl.class);

    private final ChallengeRepository challengeRepository;

    public ChallengeServiceImpl(ChallengeRepository challengeRepository) {
        this.challengeRepository = challengeRepository;
    }

    @Override
    public Challenge save(Challenge challenge) {
        log.debug("Request to save Challenge : {}", challenge);
        return challengeRepository.save(challenge);
    }

    @Override
    public Optional<Challenge> partialUpdate(Challenge challenge) {
        log.debug("Request to partially update Challenge : {}", challenge);

        return challengeRepository
            .findById(challenge.getId())
            .map(existingChallenge -> {
                if (challenge.getMatchTime() != null) {
                    existingChallenge.setMatchTime(challenge.getMatchTime());
                }
                if (challenge.getChallengeStatus() != null) {
                    existingChallenge.setChallengeStatus(challenge.getChallengeStatus());
                }
                if (challenge.getStatus() != null) {
                    existingChallenge.setStatus(challenge.getStatus());
                }

                return existingChallenge;
            })
            .map(challengeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Challenge> findAll(Pageable pageable) {
        log.debug("Request to get all Challenges");
        return challengeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Challenge> findOne(Long id) {
        log.debug("Request to get Challenge : {}", id);
        return challengeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Challenge : {}", id);
        challengeRepository.deleteById(id);
    }
}
