package br.com.jonashcr.linkshortener.controllers;

import br.com.jonashcr.linkshortener.domain.shortlink.ShortLink;
import br.com.jonashcr.linkshortener.dtos.ShortLinkDTO;
import br.com.jonashcr.linkshortener.dtos.UrlDTO;
import br.com.jonashcr.linkshortener.services.ShortLinkService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class ShortLinkController {

    @Autowired
    private ShortLinkService service;

    @Value("${app.base-url}")
    private String baseUrl;

    @PostMapping("/api/shorten")
    public ResponseEntity<ShortLinkDTO> shorten(@RequestBody UrlDTO body) {
        ShortLink shortLink = service.createShortLink(body.url());

        String fullShortLink = baseUrl + "/" + shortLink.getShortCode();

        ShortLinkDTO shortLinkDTO = new ShortLinkDTO(
            fullShortLink,
            shortLink.getOriginalUrl(),
            shortLink.getClicks()
        );

        return ResponseEntity.ok(shortLinkDTO);
    }

    @GetMapping("/{shortCode}")
    public void redirect(@PathVariable String shortCode, HttpServletRequest req, HttpServletResponse res) throws IOException {
        if (req.getMethod().equalsIgnoreCase("HEAD")) {
            res.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        ShortLink link = service.getByCode(shortCode);
        service.incrementClicks(link);
        res.sendRedirect(link.getOriginalUrl());
    }
}
