package com.example.basicauth.controller;

import com.example.basicauth.dto.CreateResponseDto;
import com.example.basicauth.dto.LoginRequestDto;
import com.example.basicauth.dto.LoginResponseDto;
import com.example.basicauth.entity.Users;
import com.example.basicauth.exception.handleResponseStatusException;
import com.example.basicauth.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    private static final Logger log = LoggerFactory.getLogger(UserController.class);


    @PostMapping("/create")
    public ResponseEntity<CreateResponseDto> saveUsers(@RequestBody LoginRequestDto loginRequestDto) {
        return new ResponseEntity<>(userService.saveUsers(loginRequestDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return new ResponseEntity<>(userService.login(loginRequestDto),HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Users>> listall(HttpServletRequest request){
        return new ResponseEntity<>(userService.list(request),HttpStatus.OK);
    }
}

