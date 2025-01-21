package com.bot.telegram_manga_bot.service.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Set;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class MangaDto {

    private String rusName;

    private String engName;

    private String imgUrl;

    private String description;

    private int releasedYear;

    private Set<String> genres;

    private String mangaType;

}