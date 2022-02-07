package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.Set;

@Controller
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = "/user")
    public String showUserInfo(@AuthenticationPrincipal User user, ModelMap modelMap) {
        modelMap.addAttribute("user", user);
        modelMap.addAttribute("role", user.getRoles());
        return "user";
    }

    @GetMapping(value = "/admin")
    public String showAdminInfo(@AuthenticationPrincipal User user, ModelMap modelMap) {
        modelMap.addAttribute("admin", user);
        modelMap.addAttribute("role", user.getRoles());
        return "admin";
    }

    @GetMapping(value = "/admin/users")
    public String showAllUsers(ModelMap modelMap, User user) {
        modelMap.addAttribute("users", userService.getAllUsers());
        modelMap.addAttribute("roles", roleService.getAllRoles());
        modelMap.addAttribute("user", user);
        return "users";
    }

    @PostMapping(value = "/admin/users")
    public String addUser(@ModelAttribute("user") User user) {
        userService.addUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping(value = "/admin/users/edit/{id}")
    public String editUserForm(ModelMap model, @PathVariable("id") long id) {
        model.addAttribute("user", userService.getUser(id));
        model.addAttribute("roles", roleService.getAllRoles());
        return "edit";
    }

    @PatchMapping(value = "/admin/users/edit")
    public String editUser(User user) {
        userService.editUser(user);
        return "redirect:/admin/users";
    }

    @DeleteMapping(value = "/admin/users/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.removeUser(id);
        return "redirect:/admin/users";
    }

    @PostConstruct
    void create() {
        Role roleAdmin = new Role("ADMIN");
        Role roleUser = new Role("USER");
        roleService.save(roleAdmin);
        roleService.save(roleUser);
        userService.addUser(new User("Thomas", "Angelo", "thomAng", "1234", Set.of(roleAdmin)));
        userService.addUser(new User("Don", "Salieri", "don", "don", Set.of(roleUser)));
        userService.addUser(new User("Paulie", "Lombardo", "paulie", "4321", Set.of(roleUser, roleAdmin)));
    }
}
