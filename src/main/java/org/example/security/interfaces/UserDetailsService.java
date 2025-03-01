package org.example.security.interfaces;

import org.example.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.swing.*;

public interface UserDetailsService {
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
}
