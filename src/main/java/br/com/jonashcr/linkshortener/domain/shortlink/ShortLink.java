package br.com.jonashcr.linkshortener.domain.shortlink;

import br.com.jonashcr.linkshortener.dtos.ShortLinkDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity(name = "short_link")
@Table(name = "short_link")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ShortLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String shortCode;

    @Column(unique = true)
    private String originalUrl;

    private int clicks;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public ShortLink(ShortLinkDTO data) {
        this.shortCode = data.shortCode();
        this.originalUrl = data.originalUrl();
        this.clicks = data.clicks();
    }
}

