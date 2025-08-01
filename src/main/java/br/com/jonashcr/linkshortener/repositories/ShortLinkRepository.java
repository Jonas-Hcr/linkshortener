package br.com.jonashcr.linkshortener.repositories;

import br.com.jonashcr.linkshortener.domain.shortlink.ShortLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShortLinkRepository extends JpaRepository<ShortLink, String> {
    Optional<ShortLink> findByShortCode(String shortCode);
}
