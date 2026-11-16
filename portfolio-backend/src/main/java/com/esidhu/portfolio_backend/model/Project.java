package com.esidhu.portfolio_backend.model;

import java.time.Instant;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
  @JsonProperty("content")
  private String readMeContent;
  private Instant latestCommitDate;
  private String title;
  private String photoUrl;

  @JsonProperty("owner")
  private void unpackOwnerLogin(Map<String, Object> owner) {
    this.owner = (String) owner.get("login");
  }

  @JsonProperty("commit")
  private void unpackCommitDate(Map<String, Object> commit) {
    Object author = commit.get("author");
    if (author instanceof Map<?, ?> authorMap) {
      Object date = authorMap.get("date");
      if (date instanceof String dateString) {
        this.latestCommitDate = Instant.parse(dateString);
      }
    }
  }
}
