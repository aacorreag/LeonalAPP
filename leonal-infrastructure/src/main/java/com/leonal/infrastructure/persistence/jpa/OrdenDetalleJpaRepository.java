package com.leonal.infrastructure.persistence.jpa;

import com.leonal.infrastructure.persistence.entity.OrdenDetalleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrdenDetalleJpaRepository extends JpaRepository<OrdenDetalleEntity, UUID> {
}
