package com.esidhu.portfolio_backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.esidhu.portfolio_backend.dto.ProjectDto;
import com.esidhu.portfolio_backend.model.Project;
import com.esidhu.portfolio_backend.repo.ProjectRepo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class ProjectService {
  private final ProjectRepo projectRepo;
  private final GitService gitService;
  private final Sort dateSort = Sort.by(Project::getLatestCommitDate).descending();
  private final Sort idSort = Sort.by(Project::getId);

  public Page<Project> getAllProjects(int page, int size) {
    // Update the info for all the modified projects in this page
    // Add 1 to page since git API pages start at 1
    List<ProjectDto> modifiedProjects = gitService.getAllRepos(page + 1, size);
    modifiedProjects.forEach(this::updateProject);

    return projectRepo.findAll(PageRequest.of(page, size, dateSort.and(idSort)));
  }

  public Project getProject(Long id) {
    Pair<Optional<ProjectDto>, HttpStatusCode> response = gitService.getRepo(id);
    // Update project with new data if it was modified since last GET
    if (response.getSecond() != HttpStatus.NOT_MODIFIED)
      updateProject(response.getFirst().orElseThrow(() -> new ResponseStatusException(
          HttpStatus.NOT_FOUND, "Project not found")));

    return projectRepo.findById(id).orElseThrow(() -> new ResponseStatusException(
        HttpStatus.NOT_FOUND, "Project not found"));
  }

  public Page<Project> getActiveProjects(int page, int size) {
    List<Long> activeIdList = projectRepo.findActiveIds();
    List<ProjectDto> modifiedProjects = gitService.getListOfRepos(activeIdList);
    modifiedProjects.forEach(this::updateProject);

    Sort dateSort = Sort.by(Project::getLatestCommitDate).descending();
    Sort idSort = Sort.by(Project::getId);
    return projectRepo.findByIsActive(true, PageRequest.of(page, size, dateSort.and(idSort)));
  }

  public List<Project> getFeaturedProjects() {
    List<Long> featuredIdList = projectRepo.findFeaturedIds();
    List<ProjectDto> modifiedProjects = gitService.getListOfRepos(featuredIdList);
    modifiedProjects.forEach(this::updateProject);

    return projectRepo.findByIsFeatured(true, dateSort.and(idSort));
  }

  public Project getCurrentProject() {
    Long currentId = projectRepo.findCurrentProjectId()
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Current project does not exist"));

    Pair<Optional<ProjectDto>, HttpStatusCode> response = gitService.getRepo(currentId);
    if (response.getSecond() != HttpStatus.NOT_MODIFIED) {
      updateProject(response.getFirst()
          .orElseThrow(() -> new ResponseStatusException(
              HttpStatus.NOT_FOUND, "Current project does not exist")));
    }

    return projectRepo.findByIsCurrent(true)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Current project does not exist"));
  }

  public Project setProjectActive(Long id, Boolean value) {
    log.info("Setting project {} isActive to {}", id, value);
    Project project = projectRepo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Project not found"));

    project.setIsActive(value);
    return projectRepo.save(project);
  }

  public Project setProjectFeatured(Long id, Boolean value) {
    log.info("Setting project {} isFeatured to {}", id, value);
    Project project = projectRepo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Project not found"));

    // Only check limit if setting 'isFeatured' to true and it isn't already true
    if (Boolean.TRUE.equals(value) && !Boolean.TRUE.equals(project.getIsFeatured())) {
      List<Project> currentFeatured = projectRepo.findAllFeatured();
      if (currentFeatured.size() >= 3) {
        throw new ResponseStatusException(
            HttpStatus.CONFLICT,
            "Cannot feature more than 3 projects at once.");
      }
    }

    project.setIsFeatured(value);
    return projectRepo.save(project);
  }

  public Project setProjectCurrent(Long id) {
    log.info("Setting project {} to current", id);

    // Get rid of existing current project before setting new project to current
    projectRepo.clearAllCurrent();
    projectRepo.flush();

    Project project = projectRepo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND, "Project not found"));
    project.setIsCurrent(true);
    return projectRepo.save(project);
  }

  private void updateProject(ProjectDto projectDto) {
    Long id = projectDto.getId();

    projectDto.setLatestCommitDate(gitService.getRepoLatestCommitDate(id));

    // Update project in DB
    Optional<Project> existingProject = projectRepo.findById(id);
    Project updatedProject;
    if (existingProject.isPresent()) {
      updatedProject = existingProject.get();
      updatedProject.setName(projectDto.getName());
      updatedProject.setOwner(projectDto.getOwner());
      updatedProject.setDescription(projectDto.getDescription());
      if (projectDto.getLatestCommitDate() != null) {
        updatedProject.setLatestCommitDate(projectDto.getLatestCommitDate());
      }
    } else {
      updatedProject = Project.builder()
          .id(projectDto.getId())
          .name(projectDto.getName())
          .owner(projectDto.getOwner())
          .description(projectDto.getDescription())
          .latestCommitDate(projectDto.getLatestCommitDate())
          .build();
    }
    projectRepo.save(updatedProject);
  }
}
