package br.com.jonashcr.linkshortener.services;

import br.com.jonashcr.linkshortener.domain.shortlink.ShortLink;
import br.com.jonashcr.linkshortener.repositories.ShortLinkRepository;
import br.com.jonashcr.linkshortener.infra.exceptions.InvalidUrlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;

@Service
public class ShortLinkService {

    @Autowired
    private ShortLinkRepository shortLinkRepository;

    public ShortLink createShortLink(String originalUrl) {
        validateUrl(originalUrl);

        try {
            ShortLink shortLink = new ShortLink();
            shortLink.setOriginalUrl(originalUrl);
            shortLink.setShortCode(generateShortCode(originalUrl));
            return shortLinkRepository.save(shortLink);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("URL já cadastrada.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void validateUrl(String url) {
        try {
            new URL(url);
        } catch (MalformedURLException e) {
            throw new InvalidUrlException("URL inválida. Por favor, insira uma URL válida.");
        }
    }

    protected String generateShortCode(String originalUrl) {
        return Integer.toHexString(originalUrl.hashCode());
    }

    public Iterable<ShortLink> getAllLinks() {
        return shortLinkRepository.findAll();
    }

    public ShortLink getByCode(String shortCode) {
        return shortLinkRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("Short link não encontrado."));
    }

    public void incrementClicks(ShortLink link) {
        link.setClicks(link.getClicks() + 1);
        shortLinkRepository.save(link);
    }
}
