package ru.poldjoke.demo.jokebot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.poldjoke.demo.jokebot.model.Joke;

@Service
public class TelegramBotService {
    private final TelegramBot telegramBot;
    private final JokeService jokeService;

    @Autowired
    public TelegramBotService(TelegramBot telegramBot, JokeService jokeService) {
        this.telegramBot = telegramBot;
        this.jokeService = jokeService;
        this.telegramBot.setUpdatesListener(updates -> {
            updates.forEach(this::buttonClickReact);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, Throwable::printStackTrace);
    }
    private void sendSelectJokeByIdMessage(long chatId) {
        SendMessage request = new SendMessage(chatId, "ID анекдота:")
                .parseMode(ParseMode.HTML)  // Определение способа форматирования текста
                .disableWebPagePreview(true)  // Отключение предпросмотра веб-ссылок
                .disableNotification(true);  // Отключение уведомлений для сообщения
        telegramBot.execute(request);  // Выполнение отправки сообщения
    }

    private void buttonClickReact(Update update) {
        if (update.message() != null && update.message().text() != null) {
            String messageText = update.message().text();
            long chatId = update.message().chat().id();

            switch (messageText) {
                case "/start":
                    sendStartMessage(chatId);
                    break;
                case "Все анекдоты":
                    sendAllJokes(chatId, 0, 10); // Первая страница, 10 шуток
                    break;
                case "Случайный анедкот":
                    sendRandomJoke(chatId);
                    break;
                case "Анекдот по ID":
                    sendSelectJokeByIdMessage(chatId);
                    break;
                default:
                    if (isNumeric(messageText)) {
                        long jokeId = Long.parseLong(messageText);
                        sendJokeById(chatId, jokeId);
                    } else {
                        sendUnknownCommand(chatId);
                    }
                    break;
            }
        }
    }

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    private void sendStartMessage(long chatId) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup(
                new String[]{"Все анекдоты", "Случайный анекдот "},
                new String[]{"Анекдот по ID"}
        )
                .oneTimeKeyboard(true)
                .resizeKeyboard(true);

        SendMessage request = new SendMessage(chatId, "Выберите действие:")
                .replyMarkup(keyboardMarkup);

        telegramBot.execute(request);
    }

    private void sendAllJokes(long chatId, int page, int size) {
        Page<Joke> jokesPage = jokeService.getJokes(PageRequest.of(page, size)); // Используем пагинацию
        StringBuilder stringBuilder = new StringBuilder();

        jokesPage.forEach(joke -> {
            stringBuilder.append(joke.getId()).append(". ").append(joke.getText()).append("\n");
        });

        SendMessage request = new SendMessage(chatId, stringBuilder.toString())
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true);

        telegramBot.execute(request);
    }

    private void sendJokeById(long chatId, long jokeId) {
        Joke joke = jokeService.getJokeById(jokeId).orElse(null);
        SendMessage request;
        if (joke != null) {
            request = new SendMessage(chatId, joke.getId() + ". " + joke.getText())
                    .parseMode(ParseMode.HTML)
                    .disableWebPagePreview(true)
                    .disableNotification(true);
        } else {
            request = new SendMessage(chatId, "Шутка с указанным id не найдена.")
                    .parseMode(ParseMode.HTML)
                    .disableWebPagePreview(true)
                    .disableNotification(true);
        }
        telegramBot.execute(request);
    }

    private void sendRandomJoke(long chatId) {
        Joke randomJoke = jokeService.getRandomJoke();
        if (randomJoke != null) {
            SendMessage request = new SendMessage(chatId, randomJoke.getText())
                    .parseMode(ParseMode.HTML)
                    .disableWebPagePreview(true)
                    .disableNotification(true);
            telegramBot.execute(request);
        }
    }

    private void sendUnknownCommand(long chatId) {
        SendMessage request = new SendMessage(chatId, "Неизвестная команда.")
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(true);
        telegramBot.execute(request);
    }
}
