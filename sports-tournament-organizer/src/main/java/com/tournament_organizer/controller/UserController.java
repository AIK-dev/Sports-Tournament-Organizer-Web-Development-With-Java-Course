package com.tournament_organizer.controller;

import com.tournament_organizer.dto.in.UserInDTO;
import com.tournament_organizer.entity.User;
import com.tournament_organizer.service.contracts.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping
    public ResponseEntity<User> create(@RequestBody @Valid UserInDTO dto) {
        User created = userService.create(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> find(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.find(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Integer id,
                                       @RequestBody @Valid UserInDTO dto) {
        return ResponseEntity.ok(userService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        userService.delete(id);
    }
}