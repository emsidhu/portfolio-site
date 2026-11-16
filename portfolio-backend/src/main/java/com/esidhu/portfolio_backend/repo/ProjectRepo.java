package com.esidhu.portfolio_backend.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.esidhu.portfolio_backend.model.Project;

public interface ProjectRepo extends JpaRepository<Project, Long> {
  public Optional<Project> findByName(String name);

  @Modifying
  @Transactional
  @Query("UPDATE Project p SET p.readMeContent = null WHERE p.id = :id")
  void clearReadMeContent(@Param("id") Long id);
}
