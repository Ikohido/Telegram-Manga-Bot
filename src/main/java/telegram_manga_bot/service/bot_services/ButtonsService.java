package telegram_manga_bot.service.bot_services;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
public class ButtonsService {
public InlineKeyboardMarkup getInlineMessageButtons(){
    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    InlineKeyboardButton buttonStart = new InlineKeyboardButton();
    InlineKeyboardButton buttonEnd = new InlineKeyboardButton();
    InlineKeyboardButton buttonRandom = new InlineKeyboardButton();

    buttonStart.setText("Манга с начала списка");
    buttonEnd.setText("Манга с конца списка");
    buttonRandom.setText("Случайная манга.");

    buttonStart.setCallbackData("StartManga");
    buttonEnd.setCallbackData("EndManga");
    buttonRandom.setCallbackData("RandomManga");

    List<InlineKeyboardButton> firstRowOfButtons = List.of(buttonStart, buttonEnd);
    List<InlineKeyboardButton> secondRowOfButtons = List.of(buttonRandom);
    List<List<InlineKeyboardButton>> rowList = List.of(firstRowOfButtons, secondRowOfButtons);

    inlineKeyboardMarkup.setKeyboard(rowList);

    return inlineKeyboardMarkup;
}
}
