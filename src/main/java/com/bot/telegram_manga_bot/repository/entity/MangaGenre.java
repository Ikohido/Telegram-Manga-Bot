package com.bot.telegram_manga_bot.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "genres")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MangaGenre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    @EqualsAndHashCode.Exclude
    private String genre;
    @ManyToMany(mappedBy = "genres", fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    private List<MangaEntity> manga;

    public MangaGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "MangaGenres{" +
                "genre='" + genre + '\'' +
                '}';
    }
}
