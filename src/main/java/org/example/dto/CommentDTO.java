package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * TODO: Класс используется для:
 * -------------------------------------------------
 *     Передачи данных между контроллером и сервисом
 *     Создания и обновления комментариев через REST API
 *     Изоляции логики преобразования данных
 *     Упрощения передачи только необходимых полей  ,
 * --------------------------------------------------
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private String text; // текст комментария
    private Long taskId; // ID задачи, к которой относится комментарий

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
