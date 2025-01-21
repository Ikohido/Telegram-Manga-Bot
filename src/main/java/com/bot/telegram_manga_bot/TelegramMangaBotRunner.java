package com.bot.telegram_manga_bot;

import com.bot.telegram_manga_bot.service.AnimegoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class TelegramMangaBotRunner implements CommandLineRunner {

    private final AnimegoService animegoService;

    public TelegramMangaBotRunner(AnimegoService animegoService) {
        this.animegoService = animegoService;
    }

    @Override
    public void run(String[] args) {
        animegoService.getAllManga();
    }

}
