package com.example.demo.mapper;

import com.example.demo.dto.UserRegistrationRequestDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto toUserResponse(User user);

    @Mapping(target = "id", ignore = true)
    User toModel(UserRegistrationRequestDto requestDto);
}
