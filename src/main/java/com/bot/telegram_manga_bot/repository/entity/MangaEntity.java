package com.bot.telegram_manga_bot.repository.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.sql.Timestamp;
import java.util.List;


@Data
@Entity
@Table(name = "manga")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MangaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "rus_name")
    @EqualsAndHashCode.Exclude
    private String rusName;
    @Column(name = "eng_name")
    @EqualsAndHashCode.Exclude
    private String engName;
    @URL
    @Column(name = "cover_url")
    @EqualsAndHashCode.Exclude
    private String imgUrl;
    @Column(name = "description")
    @EqualsAndHashCode.Exclude
    private String description;
    @Column(name = "released_year")
    @EqualsAndHashCode.Exclude
    private int releasedYear;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "manga_genres",
            joinColumns = @JoinColumn(name = "manga_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @EqualsAndHashCode.Exclude
    private List<MangaGenre> genres;
    @Column(name = "type_id")
    @EqualsAndHashCode.Exclude
    private Long mangaType;
    @Column(name = "created_at")
    @EqualsAndHashCode.Exclude
    private Timestamp createdAt;

    public MangaEntity(String rusName, String engName, String imgUrl, String description, int releasedYear, List<MangaGenre> genres, Long mangaType, Timestamp createdAt) {
        this.rusName = rusName;
        this.engName = engName;
        this.imgUrl = imgUrl;
        this.description = description;
        this.releasedYear = releasedYear;
        this.genres = genres;
        this.mangaType = mangaType;
        this.createdAt = createdAt;
    }
}