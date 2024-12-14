package pro.sky.telegrambot.sender;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.scheduling.annotation.Scheduled;
import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.service.NotificationTaskService;

import java.util.List;

public class TelegramBotSender {
    private final TelegramBot telegramBot;
    private final NotificationTaskService notificationTaskService;

    public TelegramBotSender(TelegramBot telegramBot, NotificationTaskService notificationTaskService) {
        this.telegramBot = telegramBot;
        this.notificationTaskService = notificationTaskService;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    private void sendMessage() {
        List<NotificationTask> notificationTasks = (List<NotificationTask>) notificationTaskService.getByDateTime();
        notificationTasks.forEach(notificationTask -> {
            if (notificationTask != null) {
                SendMessage sendMessage = new SendMessage(notificationTask.getChatId(), notificationTask.toString());
                SendResponse response = telegramBot.execute(sendMessage);
            }
        });
    }
}
