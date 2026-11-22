package com.esidhu.portfolio_backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.esidhu.portfolio_backend.model.Etag;

public interface EtagRepo extends JpaRepository<Etag, String> {
  @Modifying(clearAutomatically = true)
  @Transactional
  @Query(value = """
      INSERT INTO etags (url, value)
      VALUES (:url, :value)
      ON CONFLICT (url)
      DO UPDATE SET value = EXCLUDED.value
      """, nativeQuery = true)
  void upsertEtag(@Param("url") String url, @Param("value") String value);
}
