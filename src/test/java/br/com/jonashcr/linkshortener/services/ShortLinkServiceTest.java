package br.com.jonashcr.linkshortener.services;

import br.com.jonashcr.linkshortener.domain.shortlink.ShortLink;
import br.com.jonashcr.linkshortener.repositories.ShortLinkRepository;
import br.com.jonashcr.linkshortener.infra.exceptions.InvalidUrlException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class ShortLinkServiceTest {

    @Mock
    private ShortLinkRepository shortLinkRepository;

    @InjectMocks
    private ShortLinkService shortLinkService;

    private ShortLink shortLink;
    private final String validUrl = "https://www.google.com";
    private final String invalidUrl = "invalid-url";

    @BeforeEach
    void setUp() {
        shortLink = new ShortLink();
        shortLink.setOriginalUrl(validUrl);
        shortLink.setShortCode("abc123");
        shortLink.setClicks(0);
    }

    @Test
    void shouldCreateShortLink() {
        when(shortLinkRepository.save(any(ShortLink.class))).thenReturn(shortLink);

        ShortLink result = shortLinkService.createShortLink(validUrl);

        assertNotNull(result);
        assertEquals(validUrl, result.getOriginalUrl());
        verify(shortLinkRepository).save(any(ShortLink.class));
    }

    @Test
    void shouldThrowExceptionWhenUrlIsInvalid() {
        assertThrows(InvalidUrlException.class, () -> {
            shortLinkService.createShortLink(invalidUrl);
        });

        verify(shortLinkRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenUrlIsDuplicated() {
        when(shortLinkRepository.save(any(ShortLink.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicate entry"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            shortLinkService.createShortLink(validUrl);
        });

        assertEquals("URL já cadastrada.", exception.getMessage());
    }

    @Test
    void shouldGetLinkByCode() {
        when(shortLinkRepository.findByShortCode("abc123")).thenReturn(Optional.of(shortLink));

        ShortLink result = shortLinkService.getByCode("abc123");

        assertNotNull(result);
        assertEquals(shortLink.getOriginalUrl(), result.getOriginalUrl());
    }

    @Test
    void shouldThrowExceptionWhenLinkNotFound() {
        when(shortLinkRepository.findByShortCode("notfound")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            shortLinkService.getByCode("notfound");
        });

        assertEquals("Short link não encontrado.", exception.getMessage());
    }

    @Test
    void shouldIncrementClicks() {
        shortLink.setClicks(5);

        shortLinkService.incrementClicks(shortLink);

        assertEquals(6, shortLink.getClicks());
        verify(shortLinkRepository).save(shortLink);
    }

    @Test
    void shouldReturnAllLinks() {
        List<ShortLink> links = new ArrayList<>();
        links.add(shortLink);
        when(shortLinkRepository.findAll()).thenReturn(links);

        Iterable<ShortLink> result = shortLinkService.getAllLinks();

        assertNotNull(result);
        assertTrue(result.iterator().hasNext());
        verify(shortLinkRepository).findAll();
    }
}
