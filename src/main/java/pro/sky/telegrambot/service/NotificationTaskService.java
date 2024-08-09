package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.Message;
import pro.sky.telegrambot.entity.NotificationTask;

public interface NotificationTaskService {

    NotificationTask add(Message message);
    NotificationTask getByDateTime();
}
