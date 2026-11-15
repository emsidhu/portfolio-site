package com.esidhu.portfolio_backend.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esidhu.portfolio_backend.model.Etag;

public interface EtagRepo extends JpaRepository<Etag, Long> {
    Optional<Etag> findByUrl(String url);
}
