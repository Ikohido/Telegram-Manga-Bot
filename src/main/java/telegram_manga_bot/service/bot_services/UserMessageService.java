package telegram_manga_bot.service.bot_services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class UserMessageService {
    @Autowired
    private MainMenuService mainMenuService;

    public void handleUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();

            log.info("Новое нажатие кнопки встроенной клавиатуры от пользователя: {}, chatId: {}, со следующей" +
                            " информацией: {} ",
                    callbackQuery.getFrom().getUserName(),
                    callbackQuery.getFrom().getId(),
                    callbackQuery.getData());
            processCallbackQuery(callbackQuery);
        }
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("Новое сообщение от пользователя: {}, chatId: {}, со следующим текстом: {}",
                    message.getFrom().getUserName(),
                    message.getChatId(),
                    message.getText());
        }
    }
//TODO Доделать норм логирование тыканий по кнопкам
    private void processCallbackQuery(CallbackQuery callbackQuery) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        if (callbackQuery.getData().equals("StartManga")) {
            answerCallbackQuery = sendAnswerCallbackQuery("StartManga", true, callbackQuery);
            System.out.println(answerCallbackQuery);
        }
    }

    private AnswerCallbackQuery sendAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackQuery) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
        answerCallbackQuery.setShowAlert(alert);
        answerCallbackQuery.setText(text);
        return answerCallbackQuery;
    }
}
