package org.example.service.implementations;

import org.example.domains.enums.TaskStatus;
import org.example.dto.TaskDTO;
import org.example.model.Task;
import org.example.model.User;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    Task createTask(TaskDTO taskDTO, String authorUsername);
    Task updateTask(Long taskId, TaskDTO taskDTO, String updaterUsername);
    void deleteTask(Long taskId);
    Optional<Task> findById(Long id);
    List<Task> findAll();
    List<Task> findByAuthor(User author);
    List<Task> findByAssignee(User assignee);
    List<Task> findByStatus(TaskStatus status);
}
