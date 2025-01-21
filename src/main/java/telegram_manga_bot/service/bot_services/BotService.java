package telegram_manga_bot.service.bot_services;

import com.bot.telegram_manga_bot.config.BotState;
import com.bot.telegram_manga_bot.repository.MangaEntityRepository;
import com.bot.telegram_manga_bot.repository.entity.MangaEntity;
import com.vdurmont.emoji.EmojiParser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Service
public class BotService extends TelegramLongPollingBot {
    @Autowired
    private UserMessageService userMessageService;
    @Autowired
    private ButtonsService buttonsService;
    @Autowired
    private MainMenuService mainMenuService;
    @Autowired
    private MangaEntityRepository mangaEntityRepository;
    private BotState botState = BotState.MAIN_MENU;
    String webHook;
    String botUsername;
    String botToken;


    @Override
    public void onUpdateReceived(Update update) {
        userMessageService.handleUpdate(update);
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            try {
                switch (botState) {
                    case MAIN_MENU -> {
                        switch (messageText) {
                            case "/start" -> {
                                startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                            }
                            case "Манга с начала списка" -> {
                                String text = "Укажите количество манг, которое нужно вывести (максимум 20)";
                                sendMessage(chatId, text);
                                botState = BotState.ASK_MANGA_BY_BEGINNING;

                            }
                            case "Манга с конца списка" -> {
                                String text = "Укажите количество манг, которое нужно вывести (максимум 20)";
                                sendMessage(chatId, text);
                                botState = BotState.ASK_MANGA_BY_END;
                            }
                            case "Случайная манга" -> {
                                Optional<MangaEntity> randomManga = mangaEntityRepository.getRandomManga();
                                if (randomManga.isPresent()) {
                                    // Формируем сообщение с информацией о манге
                                    String mangaInfo = String.format("Название: %s\nОписание: %s\nОбложка: %s\nСсылка: %s",
                                            randomManga.get().getRusName(), randomManga.get().getDescription(),
                                            randomManga.get().getImgUrl(), randomManga.get().getMangaUrl());

                                    sendMessage(chatId, mangaInfo);
                                } else {
                                    sendMessage(chatId, "Не удалось найти случайную мангу.");
                                }
                            }
                            default -> {
                                sendMessage(chatId, "Я таких команд еще не знаю(");
                            }
                        }
                    }
                    case ASK_MANGA_BY_BEGINNING -> mangaByBeginning(chatId, messageText);
                    case ASK_MANGA_BY_END -> mangaByEnd(chatId, messageText);
                    default -> sendMessage(chatId, "Я таких команд еще не знаю(");
                }


            } catch (TelegramApiException | MalformedURLException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private void mangaByBeginning(long chatId, String messageText) throws TelegramApiException {
        int count;
        try {
            count = Integer.parseInt(messageText);
            // Проверяем, что число в пределах допустимого диапазона
            if (count < 1 || count > 20) {
                sendMessage(chatId, "Пожалуйста, введите число от 1 до 20.");
                return;
            }
        } catch (NumberFormatException e) {
            // Если введено не число, отправляем сообщение с просьбой ввести корректное число
            sendMessage(chatId, "Пожалуйста, введите корректное число от 1 до 20.");
            return;
        }
        List<MangaEntity> mangas = mangaEntityRepository.getMangaByBeginning(count);

        // Формируем сообщение для отправки пользователю
        StringBuilder responseText = new StringBuilder("Список манги:\n");
        int number = 0;
        for (MangaEntity manga : mangas) {
            responseText.append(number).append("\n").append("Название: ")
                    .append(manga.getRusName()).append("\n")
                    .append("Ссылка на мангу: ").append("\n")
                    .append(manga.getMangaUrl()).append("\n");
            number++;
        }


        sendMessage(chatId, responseText.toString());

        // Возвращаем пользователя в главное меню
        botState = BotState.MAIN_MENU;
    }

    private void mangaByEnd(long chatId, String messageText) throws TelegramApiException {
        int count;
        try {
            count = Integer.parseInt(messageText);
            // Проверяем, что число в пределах допустимого диапазона
            if (count < 1 || count > 20) {
                sendMessage(chatId, "Пожалуйста, введите число от 1 до 20.");
                return;
            }
        } catch (NumberFormatException e) {
            // Если введено не число, отправляем сообщение с просьбой ввести корректное число
            sendMessage(chatId, "Пожалуйста, введите корректное число от 1 до 20.");
            return;
        }
        List<MangaEntity> mangas = mangaEntityRepository.getMangaByEnd(count);
        StringBuilder responseText = new StringBuilder("Список манги:\n");
        int number = 0;
        for (MangaEntity manga : mangas) {
            responseText.append(number).append("\n").append("Название: ")
                    .append(manga.getRusName()).append("\n")
                    .append("Ссылка на мангу: ").append("\n")
                    .append(manga.getMangaUrl()).append("\n");
            number++;
        }

        sendMessage(chatId, responseText.toString());

        // Возвращаем пользователя в главное меню
        botState = BotState.MAIN_MENU;
    }

    private void startCommandReceived(long chatId, String name) throws MalformedURLException, TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        String answer = EmojiParser.parseToUnicode("Привет, сладкий пирожок " + name + " :heart:");
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(answer);
        execute(sendMessage);
        // Это клавиатура, класс для клавиатуры MainMenuService.
        sendMessage.setReplyMarkup(mainMenuService.getMainMenuKeyboard());
        sendGif(chatId, new InputFile().setMedia("https://media1.tenor.com/m/7Oyfyw2oz8EAAAAC/gachi-gachimuchi.gif"));
        // Это кнопки под сообщениями бота. Класс ButtonService
        sendMessage.setText("Выбирай, что тебе нужно.");
        execute(sendMessage);
    }


    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMessageWithKeyboard(long chatId, String text, ReplyKeyboardMarkup keyboardMarkup) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setReplyMarkup(keyboardMarkup);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendGif(long chatId, InputFile inputFile) {
        SendAnimation sendAnimation = new SendAnimation();
        sendAnimation.setChatId(String.valueOf(chatId));
        sendAnimation.setAnimation(inputFile);
        try {
            execute(sendAnimation);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

}
