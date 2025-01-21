package com.bot.telegram_manga_bot.repository;

import com.bot.telegram_manga_bot.repository.entity.MangaEntity;
import com.bot.telegram_manga_bot.repository.entity.MangaGenre;
import com.bot.telegram_manga_bot.repository.entity.MangaType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class GenreRepositoryTest {
    @Autowired
    MangaEntityRepository mangaEntityRepository;
    @Autowired
    GenreRepository genreRepository;
    @Autowired
    TypeRepository typeRepository;

    @BeforeEach
    @AfterEach
    public void clearAll() {
        mangaEntityRepository.deleteAll();
        genreRepository.deleteAll();
        typeRepository.deleteAll();
    }


    /**
     * Данный метод тестирует корректное сохранение жанров и сущности манги с жанрами
     * Given: Создание типов и жанров.
     * When: Создание сущности манги, присваивание жанров, времени, типа и сохранение сущности в БД.
     * Then: Проверка того, что жанры манги корректно сохранились.
     */
    @Test
    public void saveMangaGenres() {
        // --- Given ---
        Long typeId = typeRepository.save(new MangaType("Манхва")).getId();
        List<MangaGenre> genres = Arrays.asList(
                new MangaGenre("Безумие"),
                new MangaGenre("Тактика"),
                new MangaGenre("Экшн")
        );
        genreRepository.saveAll(genres);
        // --- When ---
        MangaEntity mangaEntity = new MangaEntity("Название на русском",
                "English Name",
                "http://image.url",
                "Описание манги",
                2023,
                genres, typeId,
                Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS)));
        mangaEntityRepository.save(mangaEntity);
        // --- Then ---
        List<MangaGenre> expectedGenres = mangaEntityRepository.getById(mangaEntity.getId()).get().getGenres();
        Assertions.assertIterableEquals(expectedGenres, genres);
    }

    /**
     * Данный метод тестирует метод, который должен достать манги с указанным жанром. Даже если у манги несколько жанров.
     * Given: Создание коллекций жанров.
     * When: Создание манг с одним общим жанром и одним случайным и их сохранение в БД.
     * Then: Проверка того, что метод getByGenreIn() ищет все манги с указанным жанром и вывод всех этих манг.
     */
    @Test
    public void getByGenreIn() {
        // --- Given ---
        Long typeId = typeRepository.save(new MangaType("Манхва")).getId();
        List<String> firstTestGenres = List.of("Сёнен", "Экшн");
        List<String> secondTestGenres = List.of("Сёнен");
        List<String> thirdTestGenres = List.of("Тактика", "Сёнен");
        List<MangaGenre> genres = Arrays.asList(
                new MangaGenre("Сёнен"),
                new MangaGenre("Тактика"),
                new MangaGenre("Экшн")
        );
        genreRepository.saveAll(genres);
        List<MangaGenre> firstMangaTestGenres = genreRepository.findByGenreIn(firstTestGenres);
        List<MangaGenre> secondMangaTestGenres = genreRepository.findByGenreIn(secondTestGenres);
        List<MangaGenre> thirdMangaTestGenres = genreRepository.findByGenreIn(thirdTestGenres);
        // --- When ---

        MangaEntity firstManga = new MangaEntity("Название на русском",
                "English Name",
                "http://image.url",
                "Описание манги1",
                2023, firstMangaTestGenres,
                typeId,
                Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS)));
        MangaEntity secondManga = new MangaEntity("Название на русском2",
                "English Name2",
                "http://image.url",
                "Описание манги2",
                2023, secondMangaTestGenres,
                typeId,
                Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS)));
        MangaEntity thirdManga = new MangaEntity("Название на русском3",
                "English Name3",
                "http://image.url",
                "Описание манги3",
                2023, thirdMangaTestGenres,
                typeId,
                Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS)));
        List<MangaEntity> mangaEntities = Arrays.asList(firstManga, secondManga, thirdManga);
        mangaEntityRepository.saveAll(mangaEntities);
        // --- Then ---

        List<MangaEntity> allMangaEntities = mangaEntityRepository.getByGenresIn(secondMangaTestGenres);
        Assertions.assertEquals(firstManga, allMangaEntities.get(0));
    }

    /**
     * Данный метод тестирует метод, который достает рандомную мангу из таблицы в БД.
     * Given: Создание коллекций жанров.
     * When: Создание манг и их сохранение в БД.
     * Then: Проверка того, что метод getRandomManga() действительно достает случайную мангу.
     */
    @Test
    public void getRandomManga() {
        // --- Given ---
        Long typeId = typeRepository.save(new MangaType("Манхва")).getId();
        List<String> firstTestGenres = List.of("Сёнен", "Экшн");
        List<String> secondTestGenres = List.of("Сёнен");
        List<String> thirdTestGenres = List.of("Тактика", "Сёнен");
        List<MangaGenre> genres = Arrays.asList(
                new MangaGenre("Сёнен"),
                new MangaGenre("Тактика"),
                new MangaGenre("Экшн")
        );
        genreRepository.saveAll(genres);
        List<MangaGenre> firstMangaTestGenres = genreRepository.findByGenreIn(firstTestGenres);
        List<MangaGenre> secondMangaTestGenres = genreRepository.findByGenreIn(secondTestGenres);
        List<MangaGenre> thirdMangaTestGenres = genreRepository.findByGenreIn(thirdTestGenres);
        // --- When ---
        MangaEntity firstManga = new MangaEntity("Название на русском",
                "English Name",
                "http://image.url",
                "Описание манги1",
                2023, firstMangaTestGenres,
                typeId,
                Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS)));
        MangaEntity secondManga = new MangaEntity("Название на русском2",
                "English Name2",
                "http://image.url",
                "Описание манги2",
                2023, secondMangaTestGenres,
                typeId,
                Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS)));
        MangaEntity thirdManga = new MangaEntity("Название на русском3",
                "English Name3",
                "http://image.url",
                "Описание манги3",
                2023, thirdMangaTestGenres,
                typeId,
                Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS)));
        List<MangaEntity> mangaEntities = Arrays.asList(firstManga, secondManga, thirdManga);
        mangaEntityRepository.saveAll(mangaEntities);
        // --- Then ---
        MangaEntity expectedManga = mangaEntityRepository.getRandomManga().get();
        Assertions.assertEquals(expectedManga, mangaEntityRepository.getById(expectedManga.getId()).get());
    }

    /**
     * Данный метод тестирует метод, который должен достать манги, добавленные в БД в определенный промежуток времени.
     * Given: Создание коллекций жанров и их сохранение.
     * When: Создание манг с разным временем их добавления в БД.
     * Then: Проверка того, что метод getMangaEntitiesByCreatedAt() действительно достает манги с определенным временем добавления.
     */
    @Test
    public void getMangaEntitiesByCreatedAt() {
        // --- Given ---
        Long typeId = typeRepository.save(new MangaType("Манхва")).getId();
        List<MangaGenre> genres = Arrays.asList(
                new MangaGenre("Сёнен"),
                new MangaGenre("Тактика"),
                new MangaGenre("Экшн")
        );
        genreRepository.saveAll(genres);
        // --- When ---
        MangaEntity firstManga = new MangaEntity("Название на русском",
                "English Name",
                "http://image.url",
                "Описание манги1",
                2023, genres,
                typeId,
                Timestamp.from(Instant.now().minus(1, ChronoUnit.HOURS)));
        MangaEntity secondManga = new MangaEntity("Название на русском2",
                "English Name2",
                "http://image.url",
                "Описание манги2",
                2023, genres,
                typeId,
                Timestamp.from(Instant.now().minus(1, ChronoUnit.DAYS)));
        MangaEntity thirdManga = new MangaEntity("Название на русском3",
                "English Name3",
                "http://image.url",
                "Описание манги3",
                2023, genres,
                typeId,
                Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS)));
        List<MangaEntity> mangaEntities = Arrays.asList(firstManga, secondManga, thirdManga);
        mangaEntityRepository.saveAll(mangaEntities);
        // --- Then ---
        List<MangaEntity> allMangaEntities = mangaEntityRepository.getMangaEntitiesByCreatedAt(
                Timestamp.from(Instant.now().minus(2, ChronoUnit.HOURS))
        );
        List<MangaEntity> expectedManga = Arrays.asList(
                firstManga, thirdManga
        );
        Assertions.assertEquals(expectedManga, allMangaEntities);
    }

    /**
     * Ниже указан тест, проверяющий выдачу указанного кол-ва манг из БД с начала списка таблицы по столбцу id.
     * Given: Создание коллекций жанров и типа манги и их сохранение.
     * When: Создание манг.
     * Then: Три проверки - 1 проверяет способность метода доставать указанное кол-во манг.
     * 2 проверяет действительно ли метод достает манги с начала списка.
     * 3 окончательно проверяет корректность метода в плане выдачи манг с начала списка.
     */
    @Test
    public void getMangaByBeginning() {
        // --- Given ---
        Long typeId = typeRepository.save(new MangaType("Манхва")).getId();
        List<MangaGenre> genres = Arrays.asList(
                new MangaGenre("Сёнен"),
                new MangaGenre("Тактика"),
                new MangaGenre("Экшн")
        );
        genreRepository.saveAll(genres);
        // --- When ---
        MangaEntity firstManga = new MangaEntity("Название на русском",
                "English Name",
                "http://image.url",
                "Описание манги1",
                2023, genres,
                typeId,
                Timestamp.from(Instant.now().minus(1, ChronoUnit.HOURS)));
        MangaEntity secondManga = new MangaEntity("Название на русском2",
                "English Name2",
                "http://image.url",
                "Описание манги2",
                2023, genres,
                typeId,
                Timestamp.from(Instant.now().minus(1, ChronoUnit.DAYS)));
        MangaEntity thirdManga = new MangaEntity("Название на русском3",
                "English Name3",
                "http://image.url",
                "Описание манги3",
                2023, genres,
                typeId,
                Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS)));
        List<MangaEntity> mangaEntities = Arrays.asList(firstManga, secondManga, thirdManga);
        mangaEntityRepository.saveAll(mangaEntities);
        // --- Then ---
        int expectedCount = 3;
        List<MangaEntity> allMangaEntities = mangaEntityRepository.getMangaByBeginning(expectedCount);
        Assertions.assertEquals(expectedCount, allMangaEntities.size());
        Assertions.assertEquals(firstManga.getId(), allMangaEntities.get(0).getId());
        Assertions.assertNotEquals(thirdManga.getId(), allMangaEntities.get(0).getId());
    }

    /**
     * Ниже указан тест, проверяющий выдачу указанного кол-ва манг из БД с конца списка таблицы по столбцу id.
     * Given: Создание коллекций жанров и типа манги и их сохранение.
     * When: Создание манг.
     * Then: Три проверки - 1 проверяет способность метода доставать указанное кол-во манг.
     * 2 проверяет действительно ли метод достает манги с конца списка.
     * 3 окончательно проверяет корректность метода в плане выдачи манг с конца списка.
     */
    @Test
    public void getMangaByEnd() {
        // --- Given ---
        Long typeId = typeRepository.save(new MangaType("Манхва")).getId();
        List<MangaGenre> genres = Arrays.asList(
                new MangaGenre("Сёнен"),
                new MangaGenre("Тактика"),
                new MangaGenre("Экшн")
        );
        genreRepository.saveAll(genres);
        // --- When ---
        MangaEntity firstManga = new MangaEntity("Название на русском",
                "English Name",
                "http://image.url",
                "Описание манги1",
                2023, genres,
                typeId,
                Timestamp.from(Instant.now().minus(1, ChronoUnit.HOURS)));
        MangaEntity secondManga = new MangaEntity("Название на русском2",
                "English Name2",
                "http://image.url",
                "Описание манги2",
                2023, genres,
                typeId,
                Timestamp.from(Instant.now().minus(1, ChronoUnit.DAYS)));
        MangaEntity thirdManga = new MangaEntity("Название на русском3",
                "English Name3",
                "http://image.url",
                "Описание манги3",
                2023, genres,
                typeId,
                Timestamp.from(Instant.now().truncatedTo(ChronoUnit.SECONDS)));
        List<MangaEntity> mangaEntities = Arrays.asList(firstManga, secondManga, thirdManga);
        mangaEntityRepository.saveAll(mangaEntities);
        // --- Then ---
        int expectedCount = 3;
        List<MangaEntity> allMangaEntities = mangaEntityRepository.getMangaByEnd(expectedCount);
        Assertions.assertEquals(expectedCount, allMangaEntities.size());
        Assertions.assertNotEquals(firstManga.getId(), allMangaEntities.get(0).getId());
        Assertions.assertEquals(thirdManga.getId(), allMangaEntities.get(0).getId());
    }
}
