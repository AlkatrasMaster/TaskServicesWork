package org.example.repository;

import org.example.domains.enums.TaskStatus;
import org.example.model.Task;
import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {

    List<Task> findByAuthors(User authors);
    List<Task> findByAssignee(User assignee);
    List<Task> findByStatus(TaskStatus status);
}
