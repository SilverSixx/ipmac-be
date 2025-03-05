package dev.datpl.adminservice.repository;

import dev.datpl.adminservice.pojo.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnerRepository extends JpaRepository<Partner, Long> {
}
