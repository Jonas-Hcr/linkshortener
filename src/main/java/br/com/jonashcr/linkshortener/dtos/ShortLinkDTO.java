package br.com.jonashcr.linkshortener.dtos;

public record ShortLinkDTO(String shortCode, String originalUrl, int clicks) {
}
