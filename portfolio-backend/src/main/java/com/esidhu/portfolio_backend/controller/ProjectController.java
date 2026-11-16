package com.esidhu.portfolio_backend.controller;

import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.esidhu.portfolio_backend.model.Project;
import com.esidhu.portfolio_backend.service.GitService;
import com.esidhu.portfolio_backend.service.ProjectService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {
  private final ProjectService projectService;

  @GetMapping
  public Page<Project> getProjects(@RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size) {
    return projectService.getAllProjects(page, size);
  }

  @GetMapping("/{id}")
  public Project getProject(@PathVariable Long id) {
    return projectService.getProject(id);
  }
}
