package com.example.basicauth.mapper;

import com.example.basicauth.dto.CreateResponseDto;
import com.example.basicauth.dto.LoginRequestDto;
import com.example.basicauth.dto.LoginResponseDto;
import com.example.basicauth.entity.Users;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

//    public LoginResponseDto mapUserToResponse(Users users){
//        LoginResponseDto loginResponseDto = new LoginResponseDto();
//        loginResponseDto.setUsername(users.getUsername());
//        loginResponseDto.setPassword(users.getPassword());
//        return loginResponseDto;
//    }

    public CreateResponseDto mapUserToResponseCreateUser(Users users){
        CreateResponseDto createResponseDto = new CreateResponseDto();
        createResponseDto.setUsername(users.getUsername());
        createResponseDto.setPassword(users.getPassword());
        return createResponseDto;
    }


    public Users mapRequestToUser(LoginRequestDto loginRequestDto) {
        Users users = new Users();
        users.setUsername(loginRequestDto.getUsername());
        users.setPassword(loginRequestDto.getPassword());
        return users;
    }
}
