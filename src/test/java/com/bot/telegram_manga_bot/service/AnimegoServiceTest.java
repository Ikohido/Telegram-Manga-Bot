package com.bot.telegram_manga_bot.service;

import com.bot.telegram_manga_bot.client.AnimegoClient;
import com.bot.telegram_manga_bot.repository.MangaEntityRepository;
import com.bot.telegram_manga_bot.repository.TypeRepository;
import com.bot.telegram_manga_bot.repository.entity.MangaEntity;
import com.bot.telegram_manga_bot.repository.entity.MangaGenre;
import com.bot.telegram_manga_bot.service.dto.MangaDto;
import com.bot.telegram_manga_bot.util.MockReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

import static org.mockito.Mockito.when;

@SpringBootTest
public class AnimegoServiceTest {
    @Autowired
    AnimegoService animegoService;
    @SpyBean
    AnimegoClient animegoClient;
    @SpyBean
    ParserService parserService;
    @SpyBean
    MangaEntityRepository mangaEntityRepository;
    @Autowired
    TypeRepository typeRepository;


    @BeforeEach
    @AfterEach
    public void clearAll() {
        mangaEntityRepository.deleteAll();
        typeRepository.deleteAll();
    }

    /**
     * Тест проверяет, что метод getAllManga() работает правильно в цикле
     * Given: Мокирование методов
     * When: Парсим манги
     * Then: Убеждаемся, что получаем одну мангу и цикл закончил работу после того, как не нашёл больше ссылок на манги
     */
    @Test
    public void getAllMangaTest() {
        // --- Given ---

        // Парсим страницу для получения ссылок на манги, в файле только 1 ссылка на мангу
        when(animegoClient.makeRequestByPage(1))
                .thenReturn(Optional.of(MockReader.readMock("TestGetMangaLinksFile.html")));

        when(animegoClient.makeRequestByPage(2)).thenReturn(Optional.empty());

        // Парсим страницу с самой мангой
        when(animegoClient.makeRequestForMangaPage(
                "https://animego.org/manga/magicheskaya-remeslennica-daliya-ne-unyvaet-1881")
        ).thenReturn(Optional.of(MockReader.readMock("TestParseMangaFile.html")));


        // --- When ---
        animegoService.getAllManga();

        List<MangaEntity> actualMangaList = mangaEntityRepository.getAll();

        // --- Then ---
        Assertions.assertEquals(1, actualMangaList.size());
    }

    /**
     * Данный метод тестирует превращение MangaDTO в MangaEntity.
     * Given: Создание Тестовых сущностей MangaDTO и MangaEntity
     * When: Превращение MangaDTO в MangaEntity.
     * Then: Сравнение тестовой манги и манги, созданной с помощью проверяемого метода.
     */
    @Test
    public void getMangaEntityTest() {
        // --- Given ---
        Set<String> testSetGenres = new HashSet<>();
        testSetGenres.add("романтика");
        testSetGenres.add("драма");
        List<MangaGenre> testListGenres = new ArrayList<>();
        MangaDto testMangaDto = new MangaDto("Атри: Мои дорогие моменты", "ATRI -My Dear Moments-"
                , "https://animego.org/media/cache/thumbs_250x350/upload/manga/images/6679478508673562628222.jpg",
                "", 2022, testSetGenres, "Манга");
        MangaEntity testMangaEntity = new MangaEntity("Атри: Мои дорогие моменты", "ATRI -My Dear Moments-"
                , "https://animego.org/media/cache/thumbs_250x350/upload/manga/images/6679478508673562628222.jpg",
                "", 2022, testListGenres, 1L, Timestamp.from(Instant.now()));
        // --- When ---
        MangaEntity actualMangaEntity = animegoService.toMangaEntity(testMangaDto);
        // --- Then ---
        Assertions.assertEquals(testMangaEntity, actualMangaEntity);
    }

}

