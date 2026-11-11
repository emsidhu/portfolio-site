package com.esidhu.portfolio_backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esidhu.portfolio_backend.model.Project;

public interface ProjectRepo extends JpaRepository<Project, Long> {
}
