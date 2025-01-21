package telegram_manga_bot;


import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
@AllArgsConstructor
public class TelegramMangaBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(TelegramMangaBotApplication.class, args);
    }
}

