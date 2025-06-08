package org.example.sporteventsorganizer.service.contracts;

import org.example.sporteventsorganizer.dto.in.UserInDTO;
import org.example.sporteventsorganizer.models.User;

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
