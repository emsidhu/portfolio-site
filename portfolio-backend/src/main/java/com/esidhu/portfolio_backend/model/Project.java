package com.esidhu.portfolio_backend.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "projects")
public class Project {
  @Id
  @Column(name = "id", unique = true, updatable = false)
  // No generated value, as this uses github repo id
  private Long id;
  private String name;
  private String owner;
  @Column(columnDefinition = "TEXT")
  private String description;
  @Column(columnDefinition = "TEXT")
  private String descriptionOverride;
  private String featuredDescription;
  private LocalDate latestCommitDate;
  private String title;
  private List<String> technologies;
  private String photoUrl;
  private String runUrl;
  @Builder.Default
  private Boolean isDownload = false;
  @Builder.Default
  private Boolean isActive = false;
  @Builder.Default
  private Boolean isFeatured = false;
  @Builder.Default
  private Boolean isCurrent = false;
}
