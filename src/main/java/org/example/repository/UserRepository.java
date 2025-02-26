package org.example.repository;

import org.example.domains.enums.Role;
import org.example.domains.enums.TaskStatus;
import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByRolesContains(Role role);
    Optional<User> findByUsername(String username);
    List<User> findByCreatedTasksStatus(TaskStatus status);
    List<User> findByAssignedTasksStatus(TaskStatus status);
}
