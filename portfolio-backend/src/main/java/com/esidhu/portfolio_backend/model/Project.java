package com.esidhu.portfolio_backend.model;

import java.time.Instant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "projects")
public class Project {
  @Id
  @Column(name = "id", unique = true, updatable = false)
  // No generated value, as this uses github repo id
  private Long id;
  private String name;
  private String owner;
  private String description;
  @Lob
  @Column(name = "content")
  private String readMeContent;
  private Instant latestCommitDate;
  private String title;
  private String photoUrl;
}
