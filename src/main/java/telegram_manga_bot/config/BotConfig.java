package telegram_manga_bot.config;

import com.bot.telegram_manga_bot.service.bot_services.BotService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Getter
@Setter
@Profile("!test")
public class BotConfig {
    @Value("${telegram.bot-name}")
    String botName;

    @Value("${telegram.bot-token}")
    String botToken;
    @Bean
    public BotService botService() {
        BotService botService = new BotService();
        botService.setBotToken(botToken);
        botService.setBotUsername(botName);
        return botService;
    }
}

