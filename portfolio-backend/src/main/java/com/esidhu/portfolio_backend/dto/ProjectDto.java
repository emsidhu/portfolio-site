package com.esidhu.portfolio_backend.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDto {
  private Long id;
  private String name;
  private String owner;
  private String description;
  private String readMeContent;
  private Instant latestCommitDate;
}
