package telegram_manga_bot.client;

import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.util.TimeValue;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Component
public class AnimegoClient {

    private final RestClient client = RestClient.builder()
            .requestFactory(clientHttpRequestFactory()).baseUrl("https://animego.org/manga").build();

    public Optional<String> makeRequestByPage(int pageNumber) {
        return client.get().uri(
                uriBuilder -> uriBuilder.queryParam("sort", "a.createdAt")
                        .queryParam("direction", "desc")
                        .queryParam("type", "mangas")
                        .queryParam("page", pageNumber)
                        .build()
        ).exchange((request, response) -> {
                    if (response.getStatusCode() == NOT_FOUND) {
                        System.out.println("Список манг закончен.");
                        return Optional.empty();
                    }
                    return Optional.of(new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8));
                }
        );
    }

    public Optional<String> makeRequestForMangaPage(String mangaLink) {
        return client.get().uri(mangaLink).exchange((request, response) -> {
                    if (response.getStatusCode() == NOT_FOUND) {
                        System.out.println("Манга на странице " + mangaLink + " не найдена!");
                        return Optional.empty();
                    }
                    String responseText = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
                    if (responseText.isEmpty()) return Optional.empty();

                    return Optional.of(responseText);
                }
        );
    }

    @Bean
    private ClientHttpRequestFactory clientHttpRequestFactory() {
        int maxRetryAttempts = 3;
        long delayInMillis = 100L;

        CloseableHttpClient client = HttpClients.custom()
                .setRetryStrategy(
                        new DefaultHttpRequestRetryStrategy(maxRetryAttempts, TimeValue.ofMilliseconds(delayInMillis))
                ).build();

        return new HttpComponentsClientHttpRequestFactory(client);
    }
}


