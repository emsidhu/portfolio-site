package com.esidhu.portfolio_backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import tools.jackson.databind.JsonNode;
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

  public Project updateProject(Long id, JsonNode updates) {
    if (updates == null || !updates.isObject()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body must be a JSON object");
    }

    Set<String> editableFields = Set.of(
        "featuredDescription",
        "descriptionOverride",
        "title",
        "technologies",
        "runUrl",
        "isDownload",
        "isActive",
        "photoUrl");
    updates.properties().forEach(property -> {
      String field = property.getKey();
      if (!editableFields.contains(field)) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported project field: " + field);
      }
    });

    Project project = projectRepo.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

    if (updates.has("featuredDescription")) {
      project.setFeaturedDescription(readNullableText(updates.get("featuredDescription"), "featuredDescription"));
    }
    if (updates.has("descriptionOverride")) {
      project.setDescriptionOverride(readNullableText(updates.get("descriptionOverride"), "descriptionOverride"));
    }
    if (updates.has("title")) {
      project.setTitle(readNullableText(updates.get("title"), "title"));
    }
    if (updates.has("runUrl")) {
      project.setRunUrl(readNullableText(updates.get("runUrl"), "runUrl"));
    }
    if (updates.has("photoUrl")) {
      project.setPhotoUrl(readNullableText(updates.get("photoUrl"), "photoUrl"));
    }
    if (updates.has("technologies")) {
      project.setTechnologies(readNullableTextList(updates.get("technologies"), "technologies"));
    }
    if (updates.has("isDownload")) {
      project.setIsDownload(readBoolean(updates.get("isDownload"), "isDownload"));
    }
    if (updates.has("isActive")) {
      project.setIsActive(readBoolean(updates.get("isActive"), "isActive"));
    }

    return projectRepo.save(project);
  }

  private String readNullableText(JsonNode value, String field) {
    if (value.isNull()) {
      return null;
    }
    if (!value.isString()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, field + " must be a string or null");
    }
    return value.stringValue();
  }

  private Boolean readBoolean(JsonNode value, String field) {
    if (!value.isBoolean()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, field + " must be a boolean");
    }
    return value.booleanValue();
  }

  private List<String> readNullableTextList(JsonNode value, String field) {
    if (value.isNull()) {
      return null;
    }
    if (!value.isArray()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, field + " must be an array of strings or null");
    }
    List<String> strings = new ArrayList<>();
    for (JsonNode item : value) {
      if (!item.isString()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, field + " must contain only strings");
      }
      strings.add(item.stringValue());
    }
    return strings;
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
