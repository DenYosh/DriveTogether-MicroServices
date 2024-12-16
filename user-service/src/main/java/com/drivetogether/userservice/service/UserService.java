package com.drivetogether.userservice.service;

import com.drivetogether.userservice.dto.UserDTO;
import com.drivetogether.userservice.model.User;
import com.drivetogether.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserDTO createUser(UserDTO userDTO) {
        User user = new User(null, userDTO.getName(), userDTO.getEmail(), userDTO.getPhoneNumber(), userDTO.getAddress());
        user = userRepository.save(user);
        return new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getPhoneNumber(), user.getAddress());
    }
}
