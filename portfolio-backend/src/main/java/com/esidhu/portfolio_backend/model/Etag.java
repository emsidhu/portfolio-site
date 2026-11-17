package com.esidhu.portfolio_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "etags")
public class Etag {
  @Id
  @Column(name = "url", nullable = false, updatable = false, length = 512)
  private String url;
  private String value;

  public Etag(String url) {
    this.url = url;
  }
}
