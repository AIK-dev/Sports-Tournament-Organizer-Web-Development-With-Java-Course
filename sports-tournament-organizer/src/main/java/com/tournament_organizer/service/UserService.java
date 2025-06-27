package com.tournament_organizer.service;

import com.tournament_organizer.dto.user.UserInDTO;
import com.tournament_organizer.entity.User;
import com.tournament_organizer.mappers.UserMapper;
import com.tournament_organizer.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public User create(UserInDTO dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new EntityExistsException("Username already exists");
        }
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new EntityExistsException("Email already exists");
        }
        User createdUser = userMapper.fromDto(dto);
        return userRepository.save(createdUser);
    }
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User find(Integer id) {
        return userRepository.findById(id).orElseThrow(()->new EntityNotFoundException("User"));
    }


    public User update(Integer id, UserInDTO dto) {
        User userToUpdate = find(id);
        userMapper.updateDto(userToUpdate, dto);
        return userRepository.save(userToUpdate);
    }
    public void delete(Integer id) {
        userRepository.deleteById(id);
    }
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(()->new EntityNotFoundException("User"));
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("User"));
    }
}