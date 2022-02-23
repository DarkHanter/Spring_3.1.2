package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
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
    public String deleteUser(@PathVariable("id") long id, @AuthenticationPrincipal User user, HttpSession httpSession) {
        userService.removeUser(id);
        if(id == user.getId()) {
            httpSession.invalidate();
        }
        return "redirect:/admin/users";
    }

    @PostMapping(value = "/admin/users")
    public String addUser(@ModelAttribute("user") @Valid User user,
                          BindingResult bindingResult,
                          ModelMap modelMap) {
        if (bindingResult.hasErrors()) {
            modelMap.addAttribute("users", userService.getAllUsers());
            modelMap.addAttribute("roles", roleService.getAllRoles());
            modelMap.addAttribute("user", user);
            return "users";
        }
        checkUser(user);
        return "redirect:/admin/users";
    }

    @PostConstruct
    void create() {
        Role roleAdmin = new Role("ADMIN");
        Role roleUser = new Role("USER");
        User user1 = new User("Thomas", "Angelo", "thomAng", "1234", Set.of(roleAdmin));
        User user2 = new User("Don", "Salieri", "don", "don", Set.of(roleUser));
        User user3 = new User("Paulie", "Lombardo", "paulie", "4321", Set.of(roleUser, roleAdmin));
        checkRole(roleAdmin);
        checkRole(roleUser);
        checkUser(user1);
        checkUser(user2);
        checkUser(user3);
    }

    public void checkUser(User user) {
        if(userService.isUsernameAvailable(user.getUsername())) {
            userService.addUser(user);
        } else {
            System.out.println("User with username '" + user.getUsername() + "' existed");
        }
    }

    public void checkRole(Role role) {
        if(userService.isRoleAvailable(role.getRole())) {
            roleService.save(role);
        } else {
            System.out.println("Role '" + role.getRole() +"' existed");
        }
    }
}
