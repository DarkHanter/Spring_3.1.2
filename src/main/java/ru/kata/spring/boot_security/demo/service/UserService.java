package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    void addUser(User user);
    void editUser(User user);
    void removeUser(long id);
    User getUser(long id);
    List<User> getAllUsers();
    boolean isUsernameAvailable(String name);
    boolean isRoleAvailable(String name);
}