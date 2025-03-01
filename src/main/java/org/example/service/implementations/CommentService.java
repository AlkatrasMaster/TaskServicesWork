package org.example.service.implementations;

import org.example.dto.CommentDTO;
import org.example.model.Comment;
import org.example.model.Task;
import org.example.model.User;

import java.util.List;


/**
 * TODO: Класс взаимодействует с другими компонентами:
 * -------------------------------------------------
 *     Использует CommentDto для передачи данных
 *     Работает с сущностью Comment для хранения в базе данных
 *     Интегрируется с TaskService через связи между комментариями и задачами
 *     Использует UserService для работы с пользователями   ,
 * --------------------------------------------------
 */

public interface CommentService {
    Comment createComment(CommentDTO commentDTO, String username); //  создание нового комментария
    Comment updateComment(Long id, CommentDTO commentDTO); // обновление существующего комментария
    void deleteComment(Long id); // удаление комментария
    Comment findById(Long id); // поиск комментария по ID
    List<Comment> findAll(); // получение всех комментариев
    List<Comment> findByTask(Task task); // поиск комментариев по задаче
    List<Comment> findByAuthor(User author); // поиск комментариев по автору
}
