package com.bot.telegram_manga_bot.service;


import com.bot.telegram_manga_bot.service.dto.MangaDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ParserService {

    public List<String> getMangaLinks(String htmlMangaUrlByPage) {
        Document htmlDocument = Jsoup.parse(htmlMangaUrlByPage);
        Elements mangaElementsByPage = htmlDocument.getElementsByClass("col-12 col-md-6");
        return mangaElementsByPage.stream().map(e -> e.select("a.d-block").attr("href")).toList();
    }

    public Optional<MangaDto> parseManga(String htmlManga) {
        Document mangaPage = Jsoup.parse(htmlManga);
        return Optional.ofNullable(mangaPage.selectFirst("main.content.p-3#content")).map(this::parseElement);
    }

    private MangaDto parseElement(Element mangaElement) {
        String rusName = Optional.ofNullable(mangaElement.selectFirst("h1[itemprop=name]"))
                .map(Element::text).orElse("");

        String engName = Optional.of(mangaElement.select("ul.list-unstyled.small.mb-0"))
                .flatMap(ul -> ul.select("li").stream()
                        .map(Element::text)
                        .filter(text -> text.matches("^[A-Za-z0-9\\s\\-]+$")) // Проверка на английские символы
                        .findAny()).orElse("");

        String imgUrl = Optional.of(mangaElement.select("img").attr("src")).orElse("");

        String description = Optional.ofNullable(mangaElement.selectFirst("div[data-readmore=content]"))
                .map(Element::text).orElse("");

        int releasedYear = Optional.of(mangaElement.select("span[data-label]"))
                .map(element -> element.attr("data-label"))
                .map(dataLabel -> dataLabel.substring(dataLabel.length() - 4))
                .map(Integer::parseInt).orElse(0);

        Set<String> genreNames = mangaElement.select("a[itemprop=genre]").stream()
                .map(Element::text)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        String typeName = Optional.ofNullable(mangaElement.selectFirst("dd.col-7.col-sm-8.mb-1"))
                .map(Element::text).orElse("");

        return new MangaDto(rusName, engName, imgUrl, description, releasedYear, genreNames, typeName);
    }
}
