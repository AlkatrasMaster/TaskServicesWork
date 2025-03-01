package org.example.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.UserDTO;
import org.example.exception.UserNotFoundException;
import org.example.model.User;
import org.example.service.implementations.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;



/**
 * TODO: Класс UserController является REST-контроллером в Spring Boot приложении,
 *      который обрабатывает HTTP-запросы, связанные с управлением пользователями.
 * -------------------------------------------------
 *     Основные функции класса:
 *
 *     Управление пользователями:
 *         Создание новых пользователей (POST /api/users)
 *         Обновление существующих пользователей (PUT /api/users/{id})
 *         Удаление пользователей (DELETE /api/users/{id})
 *         Получение информации о пользователе (GET /api/users/{id})
 *         Получение информации о текущем пользователе (GET /api/users/me)
 *         Получение списка всех пользователей (GET /api/users)
 *     Безопасность:
 *         Требует аутентификации для всех методов (@PreAuthorize("authenticated"))
 *         Различные уровни доступа:
 *             ROLE_USER или ROLE_ADMIN для создания и обновления пользователей
 *             ROLE_ADMIN для удаления пользователей
 *             ROLE_USER для просмотра информации о пользователях
 *     Обработка данных:
 *         Преобразование сущностей User в DTO для ответа клиенту
 *         Валидация входных данных (@Valid)
 *         Логирование операций через @Slf4
 *     Ответы API:
 *         Использование ResponseEntity для возврата HTTP-ответов
 *         Возврат данных в формате DTO
 *         Корректные HTTP-статусы (201 для создания, 200 для остальных операций)
 * --------------------------------------------------------
 */


@RestController
@RequestMapping("/api/users")
@PreAuthorize("authenticated")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserDTO userDTO) { // создание нового пользователя
        log.info("Создание нового пользователя: {}", userDTO.getUsername());

        User user= userService.registerUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDto(user));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody @Valid UserDTO userDTO) { // обновление существующего пользователя
        log.info("Обновление пользователя с ID: {}", id);

        User updateUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(mapToDto(updateUser));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {  // удаление пользователя
        log.info("Удаление пользователя с ID: {}", id);

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("authenticated")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) { // получение информации о пользователе
        log.info("Получение информации о пользователе с ID: {}", id);

        User user = userService.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        return ResponseEntity.ok(mapToDto(user));
    }

    @GetMapping("/me")
    @PreAuthorize("authenticated")
    public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal User user) { // получение информации о текущем пользователе
        log.info("Получение информации о текущем пользователе");

        UserDTO userDTO = mapToDto(user);
        return ResponseEntity.ok(userDTO);
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() { // получение списка всех пользователей
        log.info("Получение списка всех пользователей");

        List<User> users = userService.findAll();
        return ResponseEntity.ok(users.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList()));


    }

    public UserDTO mapToDto(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRoles());
        return dto;
    }

}
