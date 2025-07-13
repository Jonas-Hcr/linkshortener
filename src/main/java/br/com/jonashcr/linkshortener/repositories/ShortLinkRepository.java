package br.com.jonashcr.linkshortener.repositories;

import br.com.jonashcr.linkshortener.domain.shortlink.ShortLink;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortLinkRepository extends JpaRepository<ShortLink, String> {
}
