package com.esidhu.portfolio_backend.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import com.esidhu.portfolio_backend.model.Project;
import com.esidhu.portfolio_backend.repo.ProjectRepo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class ProjectService {
  private final ProjectRepo projectRepo;
  private final GitService gitService;

  public Page<Project> getAllProjects(int page, int size) {
    Pair<List<Project>, HttpStatusCode> response = gitService.getAllRepos();
    HttpStatusCode status = response.getSecond();
    List<Project> allProjects = response.getFirst();

    // Define page element sorters
    Sort dateSort = Sort.by(Project::getLatestCommitDate).descending();
    Sort idSort = Sort.by(Project::getId).ascending();
    // Update all projects in list in case they have been modified
    if (status != HttpStatus.NOT_MODIFIED)
      allProjects.forEach(this::updateProject);

    return projectRepo.findAll(PageRequest.of(page, size, dateSort.and(idSort)));
  }

  public Project getProject(Long id) {
    Pair<Project, HttpStatusCode> response = gitService.getRepo(id);
    // Update project with new data if it was modified since last GET
    if (response.getSecond() != HttpStatus.NOT_MODIFIED)
      updateProject(response.getFirst());

    return projectRepo.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
  }

  private void updateProject(Project project) {
    Long id = project.getId();

    project.setLatestCommitDate(gitService.getRepoLatestCommitDate(id));
    // Project update ignores null fields in new project object,
    // so no need to check modified status for commit date or README content
    // However repo might not have README, so check for 404 status
    Pair<String, HttpStatusCode> readMeResponse = gitService.getRepoReadMe(id);
    boolean readMeExists = readMeResponse.getSecond() != HttpStatus.NOT_FOUND;
    if (readMeExists) {
      project.setReadMeContent(readMeResponse.getFirst());
    }

    // Update project in DB
    Optional<Project> existingProject = projectRepo.findById(id);
    Project updatedProject;
    // Overwrite null fields if project already exists in DB
    if (existingProject.isPresent()) {
      updatedProject = existingProject.get();
      BeanUtils.copyProperties(project, updatedProject, getNullPropertyNames(project));
    } else {
      updatedProject = project;
    }
    projectRepo.save(updatedProject);
    // Clear readMeContent if the file was not found
    if (!readMeExists) {
      projectRepo.clearReadMeContent(id);
    }
  }

  private String[] getNullPropertyNames(Object source) {
    BeanWrapper sourceWrapper = new BeanWrapperImpl(source);
    return Arrays.stream(sourceWrapper.getPropertyDescriptors())
        .map(descriptor -> descriptor.getName())
        .filter(propertyName -> sourceWrapper.getPropertyValue(propertyName) == null)
        .toArray(String[]::new);
  }
}
