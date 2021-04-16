package sn.esp.gestion.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.esp.gestion.domain.ChefService;

/**
 * Spring Data SQL repository for the ChefService entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChefServiceRepository extends JpaRepository<ChefService, Long> {}
