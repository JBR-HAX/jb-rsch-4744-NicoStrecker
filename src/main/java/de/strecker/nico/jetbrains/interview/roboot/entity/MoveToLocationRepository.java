package de.strecker.nico.jetbrains.interview.roboot.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoveToLocationRepository extends JpaRepository<MoveToLocationEntity, Integer> {
}
