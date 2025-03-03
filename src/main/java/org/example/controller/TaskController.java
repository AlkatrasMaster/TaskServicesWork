package org.example.controller;

import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dto.TaskDTO;
import org.example.dto.UserDTO;
import org.example.exception.TaskNotFoundException;
import org.example.model.Task;
import org.example.model.User;
import org.example.service.implementations.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;


/**
 * TODO: Контроллер является частью презентационного слоя приложения и обеспечивает REST API для работы с задачами,
 *  включая создание, чтение, обновление и удаление, с учетом прав доступа и валидации данных.
 * -------------------------------------------------
 *     Основные функции класса:
 *
 *     Обработка HTTP-запросов:
 *         Создание новых задач (POST /api/tasks)
 *         Обновление существующих задач (PUT /api/tasks/{id})
 *         Удаление задач (DELETE /api/tasks/{id})
 *         Получение задачи по ID (GET /api/tasks/{id})
 *         Получение всех задач (GET /api/tasks)
 *     Безопасность и авторизация:
 *         Проверка аутентификации (@PreAuthorize("authenticated"))
 *         Проверка прав доступа для разных операций
 *         Логирование действий пользователей
 *     Валидация данных:
 *         Проверка корректности входных данных (@Valid)
 *         Обработка Optional результатов
 *         Возврат соответствующих HTTP-статусов
 *     Преобразование данных:
 *         Конвертация между Task и TaskDto
 *         Преобразование User в UserDto
 *         Формирование ответов в нужном формате,
 * --------------------------------------------------------
 */



@RestController
@RequestMapping("/api/tasks")
@PreAuthorize("authenticated")
public class TaskController {
    private static final Logger log = LogManager.getLogger(TaskController.class);
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping // создание новой задачи
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<TaskDTO> createTask(@RequestBody @Valid TaskDTO taskDTO, Principal principal) {
        log.info("Создание новой задачи для пользователя {}", principal.getName());

        Task task = taskService.createTask(taskDTO, principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDto(task));
    }

    @PutMapping("/{id}")  // обновление существующей задачи
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody @Valid TaskDTO taskDTO, Principal principal) {
        log.info("Обновление задачи {} для пользователя {}", id, principal.getName());
        Task updatedTask = taskService.updateTask(id, taskDTO, principal.getName());
        return ResponseEntity.ok(mapToDto(updatedTask));
    }


    @DeleteMapping("/{id}")   // удаление задачи
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTask(@PathVariable long id) {
        log.info("Удаление задачи {}", id);

        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping  // получение задачи по ID или всех задач
    @PreAuthorize("authenticated")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long id) {
        log.info("Получение задания {}", id);

        Task task = taskService.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Задача не найдена"));

        return ResponseEntity.ok(mapToDto(task));
    }

    @GetMapping  // получение задачи по всем задачам
    @PreAuthorize("authenticated")
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        log.info("Получение всех заданий");

        List<Task> tasks = taskService.findAll();
        return ResponseEntity.ok(tasks.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList()));
    }


    private TaskDTO mapToDto(Task task) { // преобразование сущности Task в DTO
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());
        if (task.getAssignee() != null) {
            dto.setAssignee(mapUserToDto(task.getAssignee()));
        }
        return dto;
    }

    private UserDTO mapUserToDto(User user) {  // преобразование сущности User в DTO
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        return dto;
    }
}
