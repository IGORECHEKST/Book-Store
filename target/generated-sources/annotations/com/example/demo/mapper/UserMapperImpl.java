package com.example.demo.mapper;

import com.example.demo.dto.UserRegistrationRequestDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-22T16:15:00+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserResponseDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponseDto userResponseDto = new UserResponseDto();

        userResponseDto.setId( user.getId() );
        userResponseDto.setEmail( user.getEmail() );
        userResponseDto.setFirstName( user.getFirstName() );
        userResponseDto.setLastName( user.getLastName() );
        userResponseDto.setShippingAddress( user.getShippingAddress() );

        return userResponseDto;
    }

    @Override
    public User toModel(UserRegistrationRequestDto requestDto) {
        if ( requestDto == null ) {
            return null;
        }

        User user = new User();

        user.setEmail( requestDto.getEmail() );
        user.setPassword( requestDto.getPassword() );
        user.setFirstName( requestDto.getFirstName() );
        user.setLastName( requestDto.getLastName() );
        user.setShippingAddress( requestDto.getShippingAddress() );

        return user;
    }
}
