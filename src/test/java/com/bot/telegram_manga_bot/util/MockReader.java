package com.bot.telegram_manga_bot.util;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Paths;

public class MockReader {

    @SneakyThrows
    public static String readMock(String fileName) {
        return Files.readString(Paths.get("src/test/resources/mocks/" + fileName));
    }
}
