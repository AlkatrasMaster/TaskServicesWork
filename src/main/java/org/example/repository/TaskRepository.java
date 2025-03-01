package org.example.repository;

import org.example.domains.enums.Role;
import org.example.domains.enums.TaskStatus;
import org.example.model.Task;
import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {

    List<Task> findByRolesContains(Role role);
    Optional<Task> findById(Long id);
    List<Task> findByCreatedTasksStatus(TaskStatus status);
    List<Task> findByAssignedTaskStatus(TaskStatus status);
    List<Task> findByAuthors(User authors);
    List<Task> findByAssignee(User assignee);
    List<Task> findByStatus(TaskStatus status);
}
