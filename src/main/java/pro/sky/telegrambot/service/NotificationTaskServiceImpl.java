package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.exception.NotificationTaskException;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NotificationTaskServiceImpl implements NotificationTaskService {
    NotificationTaskRepository notificationTaskRepository;
    private final Logger logger = LoggerFactory.getLogger(NotificationTaskServiceImpl.class);

    public NotificationTaskServiceImpl(NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;
    }

    @Override public NotificationTask add(Message message) {
        NotificationTask notificationTask = new NotificationTask();
        notificationTask.setChatId(Math.toIntExact(message.chat().id()));
        notificationTask.setMessage(getMessage(message.text()));
        notificationTask.setDatetime(getDateTime(message));
        notificationTask.setId(null);
        try {
            NotificationTask saved = notificationTaskRepository.save(notificationTask);
            logger.debug("Saved notification task {}", saved);
            return saved;
        } catch (DataIntegrityViolationException e) {
            StringBuilder exceptionMessage = new StringBuilder();
            exceptionMessage.append("Задача не была добавлена в расписание! \nПодробнее: ");
            exceptionMessage.append(e.getMessage()
                    .substring(e.getMessage()
                                    .indexOf("[ОШИБКА: ") + 8,
                            e.getMessage()
                                    .indexOf("Подробности")));
            throw new NotificationTaskException(exceptionMessage.toString());
        }
    }

    private static String getMessage(String desc) {
        String match = "";
        Matcher m = Pattern.compile("(\\D+)").matcher(desc);
        while (m.find()) {
            match = m.group();
        }
        return match.trim();
    }

    private LocalDateTime getDateTime(Message message) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            return LocalDateTime.parse(getDateTimeString(message.text()), formatter);
        } catch (Exception e) {
            StringBuilder exceptionMessage = new StringBuilder();
            exceptionMessage.append("Задача не была добавлена в расписание! \nПодробнее: ");
            exceptionMessage.append(e.getMessage());
            throw new NotificationTaskException(exceptionMessage.toString());
        }
    }

    private static String getDateTimeString(String desc) {
        String match = "";
        Matcher m = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4}\\s\\d{2}:\\d{2})").matcher(desc);
        while (m.find()) {
            match = m.group();
        }
        return match;
    }

    @Override public NotificationTask getByDateTime() {
        LocalDateTime dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        return notificationTaskRepository.findByDateTime(dateTime);
    }
}
