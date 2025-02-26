package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.domains.enums.TaskStatus;
import org.example.dto.TaskDTO;
import org.example.model.Task;
import org.example.model.User;
import org.example.service.implementations.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService {
    @Override
    public Task createTask(TaskDTO taskDTO, String authorUsername) {
        return null;
    }

    @Override
    public Task updateTask(Long taskId, TaskDTO taskDTO, String updaterUsername) {
        return null;
    }

    @Override
    public void deleteTask(Long taskId) {

    }

    @Override
    public Task findById(Long id) {
        return null;
    }

    @Override
    public List<Task> findAll() {
        return null;
    }

    @Override
    public List<Task> findByAuthor(User author) {
        return null;
    }

    @Override
    public List<Task> findByAssignee(User assignee) {
        return null;
    }

    @Override
    public List<Task> findByStatus(TaskStatus status) {
        return null;
    }
}
