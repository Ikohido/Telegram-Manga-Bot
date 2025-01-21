package com.bot.telegram_manga_bot.repository;

import com.bot.telegram_manga_bot.repository.entity.MangaGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<MangaGenre, Long> {
    List<MangaGenre> findByGenreIn(List<String> genres);

    MangaGenre findByGenre(String name);

    @Override
    List<MangaGenre> findAll();
}