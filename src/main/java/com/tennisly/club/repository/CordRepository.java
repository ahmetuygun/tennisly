package com.tennisly.club.repository;

import com.tennisly.club.domain.Cord;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Cord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CordRepository extends JpaRepository<Cord, Long>, JpaSpecificationExecutor<Cord> {}
