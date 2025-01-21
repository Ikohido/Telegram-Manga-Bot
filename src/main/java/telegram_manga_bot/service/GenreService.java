package telegram_manga_bot.service;

import com.bot.telegram_manga_bot.repository.GenreRepository;
import com.bot.telegram_manga_bot.repository.entity.MangaGenre;
import jakarta.annotation.PostConstruct;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
@Service
public class GenreService {
    private final GenreRepository genreRepository;
    private final Map<String, MangaGenre> genreCache = new ConcurrentHashMap<>();


    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }
    @PostConstruct
    public void init() {
        loadAllGenresToCache();
    }

    @Cacheable("genres")
    public void loadAllGenresToCache() {
        List<MangaGenre> genres = genreRepository.findAll();
        genres.forEach(genre -> genreCache.put(genre.getGenre().toLowerCase(), genre));
    }
    public List<MangaGenre> determineGenres(List<String> genreNames) {
        List<MangaGenre> existingGenres = genreNames.stream()
                .map(genre -> genreCache.get(genre.toLowerCase()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<MangaGenre> newGenreNames = genreNames.stream()
                .filter(genre -> !genreCache.containsKey(genre.toLowerCase()))
                .map(MangaGenre::new)
                .toList();

        if (!newGenreNames.isEmpty()) {
            List<MangaGenre> savedGenres = genreRepository.saveAll(newGenreNames);
            savedGenres.forEach(genre -> genreCache.put(genre.getGenre().toLowerCase(), genre));
            existingGenres.addAll(savedGenres);
        }

        return existingGenres;
    }

}
