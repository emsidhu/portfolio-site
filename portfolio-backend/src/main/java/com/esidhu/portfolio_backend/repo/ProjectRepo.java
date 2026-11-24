package com.esidhu.portfolio_backend.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.esidhu.portfolio_backend.model.Project;

import jakarta.persistence.LockModeType;

public interface ProjectRepo extends JpaRepository<Project, Long> {
  public Optional<Project> findByName(String name);

  @Modifying(clearAutomatically = true)
  @Transactional
  @Query("UPDATE Project p SET p.isCurrent = false WHERE p.isCurrent = true")
  void clearAllCurrent();

  // Locks featured projects to prevent race conditions
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT p FROM Project p WHERE p.isFeatured = true")
  List<Project> findAllFeatured();

  Optional<Project> findByIsCurrent(Boolean isCurrent);

  List<Project> findByIsFeatured(Boolean isFeatured, Sort sort);

  Page<Project> findByIsActive(Boolean isActive, Pageable pageable);

  @Query("SELECT p.id FROM Project p WHERE p.isCurrent = true")
  Optional<Long> findCurrentProjectId();

  @Query("SELECT p.id FROM Project p WHERE p.isFeatured = true ORDER BY p.latestCommitDate DESC, p.id ASC")
  List<Long> findFeaturedIds();

  @Query("SELECT p.id FROM Project p WHERE p.isActive = true ORDER BY p.latestCommitDate DESC, p.id ASC")
  List<Long> findActiveIds();

}
