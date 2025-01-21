CREATE TABLE types
(
    id   BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL
);
COMMENT ON TABLE types IS 'Таблица типов манги';
COMMENT ON column types.id IS 'Номер типа манги';
COMMENT ON column types.name IS 'Тип манги';
CREATE TABLE manga
(
    id            BIGSERIAL PRIMARY KEY,
    rus_name      TEXT   NOT NULL,
    eng_name      TEXT   NOT NULL,
    cover_url     TEXT,
    description   TEXT,
    released_year INTEGER,
    created_at    TIMESTAMP,
    type_id       BIGINT NOT NULL REFERENCES types (id)
);
COMMENT ON TABLE manga IS 'Таблица манг';
COMMENT ON column manga.id IS 'Номер манги';
COMMENT ON column manga.rus_name IS 'Русское название манги';
COMMENT ON column manga.eng_name IS 'Английское название манги';
COMMENT ON column manga.cover_url IS 'Ссылка на обложку манги';
COMMENT ON column manga.description IS 'Описание манги';
COMMENT ON column manga.released_year IS 'Год выхода манги';
COMMENT ON column manga.created_at IS 'Время когда манга была добавлена в БД';
COMMENT ON column manga.type_id IS 'Номер типа манги';

CREATE TABLE genres
(
    id   BIGSERIAL PRIMARY KEY,
    name TEXT NOT NULL
);
COMMENT ON TABLE genres IS 'Таблица жанров манги';
COMMENT ON column genres.id IS 'Номер жанра';
COMMENT ON column genres.name IS 'Название жанра';

CREATE TABLE manga_genres
(

    genre_id BIGINT REFERENCES genres (id) NOT NULL,
    manga_id BIGINT REFERENCES manga (id)  NOT NULL
);
COMMENT ON TABLE manga_genres IS 'Таблица для связи жанров и манг';
COMMENT ON column manga_genres.genre_id IS 'Номер манги';
COMMENT ON column manga_genres.manga_id IS 'Номер жанра';