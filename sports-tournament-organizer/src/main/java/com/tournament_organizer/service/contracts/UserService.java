package com.tournament_organizer.service.contracts;


import com.tournament_organizer.dto.in.UserInDTO;
import com.tournament_organizer.entity.User;

import java.util.List;

public interface UserService {
    User create(UserInDTO dto);

    List<User> findAll();

    User find(Integer id);

    User update(Integer id, UserInDTO dto);

    void delete(Integer id);
    User findByUsername(String username);
    User findByEmail(String email);
}
