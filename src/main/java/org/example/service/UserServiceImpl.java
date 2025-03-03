package org.example.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.controller.TaskController;
import org.example.domains.enums.Role;
import org.example.dto.UserDTO;
import org.example.exception.UserAlreadyExistsException;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.service.implementations.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LogManager.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public User registerUser(UserDTO userDTO) {
        log.info("Регистрация нового пользователя: {}", userDTO.getUsername());

        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Пользователь с таким именем уже существует");
        }

        User user = mapToEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        return userRepository.save(user);

    }

    @Override
    public User updateUser(Long id, UserDTO userDTO) {
        User existingUser = findByIdOrElseThrow(id);

        existingUser.setUsername(userDTO.getUsername());
        existingUser.setEmail(userDTO.getEmail());

        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = findByIdOrElseThrow(id);
        userRepository.delete(user);

    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.empty();
    }


    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findByRole(Role role) {
        return userRepository.findByRolesContains(role);
    }

    private User findByIdOrElseThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    private User mapToEntity(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getEmail());
        user.setEmail(userDTO.getEmail());
        user.setRoles(userDTO.getRoles());
        return user;
    }

}
