package com.esidhu.portfolio_backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.esidhu.portfolio_backend.model.Etag;

public interface EtagRepo extends JpaRepository<Etag, String> {
  @Modifying
  @Transactional
  @Query("UPDATE Etag e SET e.value = :value WHERE e.url = :url")
  int updateValueByUrl(@Param("url") String url, @Param("value") String value);
}
