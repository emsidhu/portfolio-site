package com.esidhu.portfolio_backend.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.esidhu.portfolio_backend.model.Project;
import com.esidhu.portfolio_backend.service.ProjectService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {
  private final ProjectService projectService;

  @GetMapping
  public Page<Project> getProjects(@RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "99") int size) {
    return projectService.getAllProjects(page, size);
  }

  @GetMapping("/{id}")
  public Project getProject(@PathVariable Long id) {
    return projectService.getProject(id);
  }

  @GetMapping("/active")
  public Page<Project> getActiveProjects(@RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "99") int size) {
    return projectService.getActiveProjects(page, size);
  }

  @GetMapping("/featured")
  public List<Project> getFeaturedProjects() {
    return projectService.getFeaturedProjects();
  }

  @GetMapping("/current")
  public Project getCurrentProject() {
    return projectService.getCurrentProject();
  }

  @PatchMapping("/{id}/active")
  public ResponseEntity<Project> setProjectActive(@PathVariable Long id,
      @RequestParam Boolean value) {
    return ResponseEntity.ok().body(projectService.setProjectActive(id, value));
  }

  @PatchMapping("/{id}/featured")
  public ResponseEntity<Project> setProjectFeatured(@PathVariable Long id,
      @RequestParam Boolean value) {
    return ResponseEntity.ok().body(projectService.setProjectFeatured(id, value));
  }

  // Sets project with given id to current and removes current status from
  // previously current project
  @PatchMapping("/{id}/current")
  public ResponseEntity<Project> setProjectCurrent(@PathVariable Long id) {
    return ResponseEntity.ok().body(projectService.setProjectCurrent(id));
  }
}
