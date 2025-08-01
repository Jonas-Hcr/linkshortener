package br.com.jonashcr.linkshortener.services;

import br.com.jonashcr.linkshortener.domain.shortlink.ShortLink;
import br.com.jonashcr.linkshortener.repositories.ShortLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class ShortLinkService {

    @Autowired
    private ShortLinkRepository shortLinkRepository;

    public ShortLink createShortLink(String originalUrl) {
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

    protected String generateShortCode(String originalUrl) {
        return Integer.toHexString(originalUrl.hashCode());
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
