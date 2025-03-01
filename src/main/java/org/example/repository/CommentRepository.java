package org.example.repository;

import org.example.model.Comment;
import org.example.model.Task;
import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * TODO: Класс используется для:
 * ------------------------------------------------------------
 *     является частью слоя данных (Data Access Layer)
 *    и обеспечивает доступ к данным комментариев в базе данных,
 * -------------------------------------------------------------
 *     Базовые операции с комментариями:
 * -------------------------------------------------------------
 *     Создание новых комментариев
 *     Чтение существующих комментариев
 *     Обновление комментариев
 *     Удаление комментариев
 * --------------------------------------------------------------
 */

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTask(Task task); // поиск комментариев по задаче
    List<Comment> findByAuthor(User author); // поиск комментариев по автору
}
