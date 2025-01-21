package telegram_manga_bot.service.bot_services;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;



@Component
public class MainMenuService {
    public SendMessage getMainMenuMessage(final long chatId, final String textMessage) {
        final ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard();

        return createMessageWithKeyboard(chatId, textMessage, replyKeyboardMarkup);
    }

    protected ReplyKeyboardMarkup getMainMenuKeyboard() {
        // Создаем объект ReplyKeyboardMarkup и настраиваем параметры клавиатуры
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);

        // Создаем и настраиваем строки клавиатуры

        KeyboardRow row1 = new KeyboardRow();
        row1.add("Случайная манга");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("Манга с начала списка");

        KeyboardRow row3 = new KeyboardRow();
        row3.add("Манга с конца списка");
        // Добавляем строки в список клавиатуры
        List<KeyboardRow> keyboard = List.of(row1, row2, row3);


        // Привязываем список строк клавиатуры к разметке клавиатуры
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }
    public SendMessage getSubMenuMessage(final long chatId, final String textMessage) {
        final ReplyKeyboardMarkup replyKeyboardMarkup = getSubMenuKeyboard();
        return createMessageWithKeyboard(chatId, textMessage, replyKeyboardMarkup);
    }
    protected ReplyKeyboardMarkup getSubMenuKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);

        KeyboardRow row1 = new KeyboardRow();
        row1.add("Опция 1");

        KeyboardRow row2 = new KeyboardRow();
        row2.add("Опция 2");

        KeyboardRow row3 = new KeyboardRow();
        row3.add("Вернуться в главное меню");

        List<KeyboardRow> keyboard = List.of(row1, row2, row3);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

// Это короче метод для создания сообщений когда тыкаешь на кнопки клавиатуры
    private SendMessage createMessageWithKeyboard(final long chatId, String textMessage, final ReplyKeyboardMarkup replyKeyboardMarkup) {
        final SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textMessage);
        if (replyKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }

        return sendMessage;
    }
}
