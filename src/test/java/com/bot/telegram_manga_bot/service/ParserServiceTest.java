package com.bot.telegram_manga_bot.service;

import com.bot.telegram_manga_bot.client.AnimegoClient;
import com.bot.telegram_manga_bot.service.dto.MangaDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootTest
public class ParserServiceTest {
    @Autowired
    ParserService parserService;
    @Autowired
    AnimegoClient animegoClient;

    /**
     * Данный метод тестирует получение 20 манг с 1 страницы сайта
     * Given: Чтение html файла и создание из него String
     * When: Создание коллекции ссылок на манги и заполнение этой коллекции 20-ю ссылками.
     * Then: Проверка того, что ссылок действительно 20.
     */
    @Test
    @SneakyThrows
    public void testGetMangaLinks() {
        // --- Given ---
        String content = Files.readString(Paths.get("src/test/resources/testfiles/TestGetMangaLinksFile.html"));
        // --- When ---
        List<String> parserList = parserService
                .getMangaLinks(content);
        // --- Then ---
        Assertions.assertEquals(20, parserList.size());
    }

    /**
     * Данный метод тестирует корректное сохранение жанров и сущности манги с жанрами
     * Given: Создание String из html страницы манги.
     * When: Создание MangaDTO сущности для сравнения и создание MangaDTO сущности с помощью проверяемого метода.
     * Then: Сравнение тестовой сущности и сущности, созданной с помощью проверяемого метода
     */
    @Test
    @SneakyThrows
    public void testParseManga() {
        // --- Given ---
        String content = Files.readString(Paths.get("src/test/resources/testfiles/TestParseMangaFile.html"));
        // --- When ---
        Set<String> testSet = new HashSet<>();
        testSet.add("романтика");
        testSet.add("драма");
        MangaDto testMangaDto = new MangaDto("Атри: Мои дорогие моменты", "ATRI -My Dear Moments-"
                , "https://animego.org/media/cache/thumbs_250x350/upload/manga/images/6679478508673562628222.jpg",
                "", 2022, testSet, "Манга");
        MangaDto mangaDto = parserService.parseManga(content).get();
        // --- Then ---
        Assertions.assertEquals(testMangaDto, mangaDto);
    }

}
