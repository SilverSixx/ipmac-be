package dev.datpl.trainingservice.repository;

import dev.datpl.trainingservice.pojo.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnerRepository extends JpaRepository<Partner, Long> {
}
