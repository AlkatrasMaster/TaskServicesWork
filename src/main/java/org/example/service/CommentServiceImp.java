package org.example.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.controller.TaskController;
import org.example.dto.CommentDTO;
import org.example.exception.CommentNotFoundException;
import org.example.exception.TaskNotFoundException;
import org.example.exception.UserNotFoundException;
import org.example.model.Comment;
import org.example.model.Task;
import org.example.model.User;
import org.example.repository.CommentRepository;
import org.example.service.implementations.CommentService;
import org.example.service.implementations.TaskService;
import org.example.service.implementations.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

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

@Service
public class CommentServiceImp implements CommentService {

    private static final Logger log = LogManager.getLogger(CommentServiceImp.class);
    private final CommentRepository commentRepository;
    private final TaskService taskService;
    private final UserService userService;


    @Autowired
    public CommentServiceImp(CommentRepository commentRepository, TaskService taskService, UserService userService) {
        this.commentRepository = commentRepository;
        this.taskService = taskService;
        this.userService = userService;
    }


    @Override
    public Comment createComment(CommentDTO commentDTO, String username) {  //  создание нового комментария
        log.info("Создание комментария для пользователя: {}", username);

        User author = userService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Автор не найден"));

        Task task = taskService.findById(commentDTO.getTaskId())
                .orElseThrow(() -> new TaskNotFoundException("Задача не найдена"));

        Comment comment = mapToEntry(commentDTO);
        comment.setTask(task);
        comment.setAuthor(author);

        return commentRepository.save(comment);
    }

    @Override
    public Comment updateComment(Long id, CommentDTO commentDTO) { // обновление существующего комментария
        Comment existingComment = findByIdElseThrow(id);
        Comment updateComment = mapToEntry(commentDTO);
        updateComment.setId(id);

        return commentRepository.save(updateComment);
    }

    @Override
    public void deleteComment(Long id) {  //  удаление комментария
        Comment comment = findByIdElseThrow(id);
        commentRepository.delete(comment);

    }

    @Override
    public Comment findById(Long id) { // поиск комментария по ID
        return findByIdElseThrow(id);
    }

    @Override
    public List<Comment> findAll() { // получение всех комментариев
        return commentRepository.findAll();
    }

    @Override
    public List<Comment> findByTask(Task task) { // поиск комментариев по задаче
        return commentRepository.findByTask(task);
    }

    @Override
    public List<Comment> findByAuthor(User author) { //  поиск комментариев по автору
        return commentRepository.findByAuthor(author);
    }

    private Comment findByIdElseThrow(Long id) { // безопасный поиск с выбросом исключения
        return commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Комментарий не найден"));
    }

    private Comment mapToEntry(CommentDTO commentDTO) { //преобразование DTO в сущность
        Comment comment = new Comment();
        comment.setText(commentDTO.getText());
        return comment;
    }
}
