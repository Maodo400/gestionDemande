package sn.esp.gestion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.esp.gestion.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
