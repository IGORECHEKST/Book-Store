package com.example.demo.service.impl;

import com.example.demo.dto.UserRegistrationRequestDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.exception.RegistrationException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Role;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;
import com.example.demo.repository.ShoppingCartRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("Can't register user with email: "
                    + requestDto.getEmail());
        }

        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        Role userRole = roleService.getRoleByRoleName(Role.RoleName.ROLE_USER);
        user.setRoles(Set.of(userRole));

        User savedUser = userRepository.save(user);

        createShoppingCartForUser(savedUser);

        return userMapper.toDto(savedUser);
    }

    private void createShoppingCartForUser(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }
}
