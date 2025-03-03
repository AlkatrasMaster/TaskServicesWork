package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.domains.enums.TaskStatus;
import org.example.dto.TaskDTO;
import org.example.exception.TaskNotFoundException;
import org.example.exception.UserNotFoundException;
import org.example.model.Task;
import org.example.model.User;
import org.example.repository.TaskRepository;
import org.example.service.implementations.TaskService;
import org.example.service.implementations.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    public TaskServiceImpl(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    @Override
    public Task createTask(TaskDTO taskDTO, String authorUsername) {
        User author = userService.findByUsername(authorUsername)
                .orElseThrow(() -> new UserNotFoundException("Автор не найден"));

        Task task = mapToEntity(taskDTO);
        task.setAuthor(author);

        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Long taskId, TaskDTO taskDTO, String updaterUsername) {
        Task existingTask = findByIdOrElseThrow(taskId);
        User updater = userService.findByUsername(updaterUsername)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        existingTask.setTitle(taskDTO.getTitle());
        existingTask.setDescription(taskDTO.getDescription());
        existingTask.setStatus(taskDTO.getStatus());
        existingTask.setPriority(taskDTO.getPriority());
        existingTask.setAssignee(taskDTO.getAssignee() != null
                ? userService.findById(taskDTO.getAssignee().getId()).orElse(null)
                : null);

        return taskRepository.save(existingTask);
    }

    @Override
    public void deleteTask(Long taskId) {
        Task task = findByIdOrElseThrow(taskId);
        taskRepository.delete(task);

    }

    @Override
    public Optional<Task> findById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> findByAuthor(User author) {
        return taskRepository.findByAuthors(author);
    }

    @Override
    public List<Task> findByAssignee(User assignee) {
        return taskRepository.findByAssignee(assignee);
    }

    @Override
    public List<Task> findByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }


    private Task findByIdOrElseThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Задача не найдена"));
    }

    private Task mapToEntity(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());
        task.setPriority(taskDTO.getPriority());
        return task;
    }
}
