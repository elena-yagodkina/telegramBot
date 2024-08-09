package pro.sky.telegrambot.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity
@Table(name = "notification_task")
public class NotificationTask {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    private Integer chatId;
    @Getter
    private String message;
    @Column(name="date_time")
    private LocalDateTime dateTime;

    @Override
    public String toString() {
        return "Напоминание о задаче: " + '\n' +
                "\"Необходимо " + message + "\"\n" +
                "в " + dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    }

    public NotificationTask() {
    }

    public NotificationTask(Long id, Integer chatId, String message, LocalDateTime datetime) {
        this.id = id;
        this.chatId = chatId;
        this.message = message;
        this.dateTime = dateTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setChatId(Integer chat_id) {
        this.chatId = chat_id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDatetime() {
        return dateTime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.dateTime = datetime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return Objects.equals(id, that.id) && Objects.equals(chatId, that.chatId) && Objects.equals(message, that.message) && Objects.equals(dateTime, that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, message, dateTime);
    }
}
