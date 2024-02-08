package com.example.basicauth.service;

import com.example.basicauth.dto.CreateResponseDto;
import com.example.basicauth.dto.LoginRequestDto;
import com.example.basicauth.dto.LoginResponseDto;
import com.example.basicauth.entity.Users;
import com.example.basicauth.exception.handleResponseStatusException;
import com.example.basicauth.mapper.UserMapper;
import com.example.basicauth.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserRepository userRepository;
    private UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public CreateResponseDto saveUsers(LoginRequestDto loginRequestDto) {
        String encodedPassword = encodeBase64(loginRequestDto.getPassword());
        loginRequestDto.setPassword(encodedPassword);

        Users users = userMapper.mapRequestToUser(loginRequestDto);
        Users savedusers = userRepository.save(users);
        CreateResponseDto createResponseDto = userMapper.mapUserToResponseCreateUser(savedusers);
        return createResponseDto;
    }

    private String encodeBase64(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes());
    }

    private String decodeBase64(String encodedValue) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedValue);
        return new String(decodedBytes);
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {

        Optional<Users> storedUser = userRepository.findByUsername(loginRequestDto.getUsername());

        String storedPassword = decodeBase64(storedUser.get().getPassword());
        if (storedPassword.equals(loginRequestDto.getPassword())) {
        LoginResponseDto loginResponseDto = new LoginResponseDto();
         loginResponseDto.setMessage("User Logged in Successfully.");
            return loginResponseDto;
        } else {
          LoginResponseDto loginResponseDto = new LoginResponseDto();
          loginResponseDto.setMessage("Invalid Credentials");
           return loginResponseDto;
        }
    }


   public List<Users> list(HttpServletRequest request) {

          String authorizationHeader = request.getHeader("Authorization");
          if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {
              authenticateUser(authorizationHeader);
              return userRepository.findAll();
          } else {
              throw new handleResponseStatusException("You are not authorized to view the list");
          }


    }

    private void authenticateUser(String authorizationHeader) {
        String credentials = decodeBase64(authorizationHeader.substring(6));
        String[] splitCredentials = credentials.split(":");
        String username = splitCredentials[0];
        String password = splitCredentials[1];

        Optional<Users> storedUser = userRepository.findByUsername(username);

        if (storedUser.isPresent() && password.equals(decodeBase64(storedUser.get().getPassword()))) {
        } else {
            throw new handleResponseStatusException("password not matched");
        }
    }

    public List<String> fetchName(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {
            String credentials = decodeBase64(authorizationHeader.substring(6));
            String[] splitCredentials = credentials.split(":");
            String username = splitCredentials[0];
            String password = splitCredentials[1];


            Optional<Users> storedUser = userRepository.findByUsername(username);

                if (password.equals(decodeBase64(storedUser.get().getPassword()))) {
                    System.out.println("Authentication successful");
                    List<Users> users = userRepository.findAll();

                    return users.stream()
                            .map(user->user.getUsername())
                            .collect(Collectors.toList());                }
                else {
                    throw new handleResponseStatusException("Password not matched");
                }
        }
        throw new handleResponseStatusException("unauthorized");
    }


}

