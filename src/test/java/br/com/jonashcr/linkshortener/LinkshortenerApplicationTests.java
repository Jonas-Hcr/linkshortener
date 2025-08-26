package br.com.jonashcr.linkshortener;

import br.com.jonashcr.linkshortener.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.context.annotation.Import;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig.class)
class LinkshortenerApplicationTests {

    @Test
    void contextLoads() {
    }

}
