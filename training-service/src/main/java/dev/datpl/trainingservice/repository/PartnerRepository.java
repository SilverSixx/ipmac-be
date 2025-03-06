package dev.datpl.trainingservice.repository;

import dev.datpl.trainingservice.pojo.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PartnerRepository extends JpaRepository<Partner, String>, UserRepository {
    Optional<Partner> findByUserId(String userId);
}
