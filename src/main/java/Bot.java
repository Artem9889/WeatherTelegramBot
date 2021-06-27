import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Bot extends TelegramLongPollingBot {


    public static int incrementStart = 0;
    public static int incrementHelp = 0;
    public static int incrementInformation = 0;
    public static int incrementOfficialSite = 0;
    public static int total = 0;

    public static void main(String[] args) {

        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);

        try {
            setButtons(sendMessage);
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }


    }


    public void onUpdateReceived(Update update) {
        Model model = new Model();
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "/start":
                    if (message.getText().equals("/start")) {
                        incrementStart++;
                    }
                    sendMsg(message, "Привіт, " + message.getChat().getUserName() + "\uD83D\uDE43 \nНатискай на \uD83D\uDC49 '/help' та дізнавайся, як спілкуватися зі мною \uD83D\uDE01");
                    break;
                case "/help⚙":
                    if (message.getText().equals("/help⚙")) {
                        incrementHelp++;
                    }
                    sendMsg(message, "\n" +
                            "➖ 1️⃣ Напиши своє місто(або інше місто)\uD83D\uDE42\n" +
                            "➖ 2️⃣ Відправ мені та очікуй... я тобі повідомлю про погоду в твоєму місті\uD83E\uDD17\n\n\nНатискай '/information' та дізнавайся всю інформацію про мене\uD83C\uDF1E");
                    break;
                case "/officialsite\uD83C\uDF21":
                    if (message.getText().equals("/officialsite\uD83C\uDF21")) {
                        incrementOfficialSite++;
                    }
                    try {
                        URL url = new URL("https://sinoptik.ua/");
                        sendMsg(message, "Посилання на офіційний сайт\uD83C\uDF24 - " + url);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    break;
                case "/information\uD83D\uDCDC":
                    if (message.getText().equals("/information\uD83D\uDCDC")) {
                        incrementInformation++;
                    }
                    sendMsg(message, "Привіт\uD83D\uDD90, я надаю поточні дані про погоду в будь-якому місці на Землі\uD83D\uDE43, включаючи понад 200 000 міст!\uD83D\uDC4C" +
                            " Я збираю та обробляю дані про погоду з різних джерел, таких як глобальні та місцеві моделі погоди, супутники, радари та велика мережа метеостанцій.\uD83D\uDE0E\n" +
                            "➖ Тех.підтримка⚙: @molyavin ✉️");
                    break;
                case "/botstatistics":
                    total = incrementStart+incrementHelp+incrementInformation+incrementOfficialSite;
                    sendMsg(message, "\uD83D\uDCCA\uD83D\uDCC8\uD83D\uDCC9" +
                            "\n\uD83D\uDCB0 ➖  Кількість користувачів, які написали 'start'- (" + incrementStart + ")\n\uD83D\uDCB0 ➖  Кількість користувачів, які написали 'help' - (" + incrementHelp + ")\n" +
                            "\uD83D\uDCB0 ➖  Кількість користувачів, які написали 'information' - (" + incrementInformation + ")\n" +
                            "\uD83D\uDCB0 ➖  Кількість користувачів, які написали 'officialsite'- (" + incrementOfficialSite + ")\n\n\uD83D\uDCB0 ➖  Загальна сума введених команд - ("+total+")");
                    break;

                case "/deletestatistics":
                    if (message.getText().equals("/deletestatistics")) {
                        incrementStart = 0;
                        incrementHelp = 0;
                        incrementInformation = 0;
                        incrementOfficialSite = 0;
                    }
                    sendMsg(message, " \uD83E\uDD11- Статистика очищена! ");
                    break;

                default:
                    try {
                        sendMsg(message, Weather.getWeather(message.getText(), model));
                    } catch (IOException e) {
                        sendMsg(message, "Таке місто не існує!\uD83D\uDE12\n");
                    }
            }
        }
    }

    private void deleteMessage(Long chatId, Integer messageId) {
        Message message = new Message();


    }

    public void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowsList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("/officialsite\uD83C\uDF21"));
        keyboardFirstRow.add(new KeyboardButton("/help⚙"));
        keyboardFirstRow.add(new KeyboardButton("/information\uD83D\uDCDC"));


        keyboardRowsList.add(keyboardFirstRow);

        replyKeyboardMarkup.setKeyboard(keyboardRowsList);
    }


    public String getBotUsername() {
        return "new_sinoptik_bot";
    }

    public String getBotToken() {
        return "1745450920:AAFrDkM9DF93t4Zo4PvXpGoyihW5c1t9AwU";
    }
}

