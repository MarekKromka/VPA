package com.example.app.ws.service;

import com.example.app.ws.shared.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface IUserService extends UserDetailsService {
    UserDto createUser(UserDto user);
    UserDto getUser(String email);
    UserDto getUserByUserId(String userId);
    UserDto updateUser(String userId, UserDto user);
    void deleteUser(String userId);

    List<UserDto> getUsers(int page, int limit);

    List<UserDto> findUsers(UserDto userDetails);
}
