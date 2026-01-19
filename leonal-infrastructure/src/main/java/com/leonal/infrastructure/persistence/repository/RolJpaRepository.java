package com.leonal.infrastructure.persistence.repository;

import com.leonal.infrastructure.persistence.entity.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RolJpaRepository extends JpaRepository<RolEntity, UUID> {
  Optional<RolEntity> findByNombre(String nombre);
}
