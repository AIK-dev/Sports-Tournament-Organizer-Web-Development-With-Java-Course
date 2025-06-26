package com.tournament_organizer.service;

import com.tournament_organizer.dto.in.UserInDTO;
import com.tournament_organizer.mapper.UserMapper;
import com.tournament_organizer.entity.User;
import com.tournament_organizer.repository.UserRepository;
import com.tournament_organizer.service.contracts.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
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
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User find(Integer id) {
        return userRepository.findById(id).orElseThrow(()->new EntityNotFoundException("User"));
    }

    @Override
    public User update(Integer id, UserInDTO dto) {
        User userToUpdate = find(id);
        userMapper.updateDto(userToUpdate, dto);
        return userRepository.save(userToUpdate);
    }
    @Override
    public void delete(Integer id) {
        userRepository.deleteById(id);
    }
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(()->new EntityNotFoundException("User"));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("User"));
    }
}
