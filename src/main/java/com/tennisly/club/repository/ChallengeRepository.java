package com.tennisly.club.repository;

import com.tennisly.club.domain.Challenge;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Challenge entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long>, JpaSpecificationExecutor<Challenge> {
    Page<Challenge> findAllByAcceptor_IdOrProposer_Id(Long accepterId, Long proposerId, Pageable pageable);
}
