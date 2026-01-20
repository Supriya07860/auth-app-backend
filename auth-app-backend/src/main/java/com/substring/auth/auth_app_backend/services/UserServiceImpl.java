package com.substring.auth.auth_app_backend.services;

import com.substring.auth.auth_app_backend.dtos.UserDto;
import com.substring.auth.auth_app_backend.entities.Provider;
import com.substring.auth.auth_app_backend.entities.User;
import com.substring.auth.auth_app_backend.exceptions.ResourceNotFoundException;
import com.substring.auth.auth_app_backend.helpers.UserHelper;
import com.substring.auth.auth_app_backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional                // Ensures that the method runs within a transaction
    public UserDto createUser(UserDto userDto) {

        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("User with given email already exists");
        }

        User user = modelMapper.map(userDto, User.class);

        user.setProvider(
                userDto.getProvider() != null
                        ? userDto.getProvider()
                        : Provider.LOCAL
        );

        User savedUser = userRepository.save(user);

        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String email) {

       User user=  userRepository
                .findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with email"));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        //Convert UUID to User id
        UUID uId = UserHelper.parseUUID(userId);
        User existingUser = userRepository
                .findById(uId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with given id"));
        // not going to change email for this project
        if(userDto.getName() != null){
            existingUser.setName(userDto.getName());
        }
        if(userDto.getImage()!= null){
            existingUser.setImage(userDto.getImage());
        }
        if(userDto.getProvider()!= null){
            existingUser.setProvider(userDto.getProvider());
        }
        // for security purpose, change password update logic
        if(userDto.getPassword() != null){
            existingUser.setPassword(userDto.getPassword());
        }
        existingUser.setEnable(userDto.isEnable());
        existingUser.setUpdatedAt(Instant.now());
        User updatedUser = userRepository.save(existingUser);

        return modelMapper.map(updatedUser, UserDto.class);

    }

    @Override
    public void deleteUser(String userId) {
        UUID uId = UserHelper.parseUUID(userId);
        User user = userRepository.findById(uId).orElseThrow(() -> new ResourceNotFoundException("User not found with given id"));
        userRepository.delete(user);
    }

    @Override
    public UserDto getUserById(UUID userId) {
        return null;
    }

//    @Override
//    public UserDto getUserById(UUID userId) {
//        return null;
//    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(UserHelper.parseUUID(userId)).orElseThrow(() -> new ResourceNotFoundException("User not found with given id"));

        return modelMapper.map(user, UserDto.class);
    }

    @Override
    @Transactional
    public Iterable<UserDto> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }
}
