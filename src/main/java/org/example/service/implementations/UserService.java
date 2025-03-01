package org.example.service.implementations;

import org.example.domains.enums.Role;
import org.example.dto.UserDTO;
import org.example.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User registerUser(UserDTO userDTO);
    User updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
    Optional<User> findById(Long id);
    List<User> findAll();
    Optional<User> findByUsername(String username);
    List<User> findByRole(Role role);
}
