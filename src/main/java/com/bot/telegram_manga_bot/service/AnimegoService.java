package com.bot.telegram_manga_bot.service;

import com.bot.telegram_manga_bot.client.AnimegoClient;
import com.bot.telegram_manga_bot.repository.MangaEntityRepository;
import com.bot.telegram_manga_bot.repository.entity.MangaEntity;
import com.bot.telegram_manga_bot.repository.entity.MangaGenre;
import com.bot.telegram_manga_bot.service.dto.MangaDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AnimegoService {

    private final AnimegoClient animegoClient;
    private final ParserService parserService;
    private final MangaEntityRepository mangaEntityRepository;
    private final TypeService typeService;
    private final GenreService genreService;

    public void getAllManga() {


        int pageNumber = 1;

        while (true) {
            Optional<String> htmlMangaByPageOptional = animegoClient.makeRequestByPage(pageNumber);

            if (htmlMangaByPageOptional.isEmpty()) {
                break;
            }

            List<String> mangaURLsByPage = parserService.getMangaLinks(htmlMangaByPageOptional.get());

            if (mangaURLsByPage.isEmpty()) break;
            List<MangaEntity> allMangaEntities = new ArrayList<>();
            mangaURLsByPage.stream()
                    .map(animegoClient::makeRequestForMangaPage)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(parserService::parseManga)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(currentManga -> {
                        MangaEntity mangaEntity = toMangaEntity(currentManga);
                        System.out.println(mangaEntity);
                        if (mangaEntityRepository.getMangaEntityByRusNameAndImgUrl(
                                currentManga.getRusName(), currentManga.getImgUrl()).isPresent()
                        ) {
                            return;
                        }
                        allMangaEntities.add(mangaEntity);
                    });
            mangaEntityRepository.saveAll(allMangaEntities);
            pageNumber++;
        }

    }

    public MangaEntity toMangaEntity(MangaDto mangaDto) {
        Long mangaType = typeService.determineType(mangaDto.getMangaType().toLowerCase());
        List<String> genreNames = mangaDto.getGenres().stream().toList();
        List<MangaGenre> genres = genreService.determineGenres(genreNames);
        return new MangaEntity(mangaDto.getRusName(), mangaDto.getEngName(), mangaDto.getImgUrl(),
                mangaDto.getDescription(), mangaDto.getReleasedYear(), genres, mangaType,
                Timestamp.from(Instant.now()));
    }
}
