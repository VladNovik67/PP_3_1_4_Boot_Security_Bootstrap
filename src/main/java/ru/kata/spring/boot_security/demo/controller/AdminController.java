package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImp;
import ru.kata.spring.boot_security.demo.service.UsersServiceImp;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UsersServiceImp usersServiceImp;
    private final RoleServiceImp roleServiceImp;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public AdminController(UsersServiceImp usersServiceImp, RoleServiceImp roleServiceImp, PasswordEncoder passwordEncoder) {
        this.usersServiceImp = usersServiceImp;
        this.roleServiceImp = roleServiceImp;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping()
    public String showAll(ModelMap model, Principal principal) {
        model.addAttribute("userss", usersServiceImp.getAllUsers());
        model.addAttribute("loginUser", usersServiceImp.findByUsername(principal.getName()));
        model.addAttribute("roles", roleServiceImp.findAllRoles());
        model.addAttribute("userNew", new User());
        return "admin/showAll";
    }


    @PostMapping
    public String create(@ModelAttribute("user") User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usersServiceImp.saveUser(user);
        return "redirect:/admin";
    }


    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") User user, @PathVariable("id") long id) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usersServiceImp.updateUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        usersServiceImp.deleteUser(usersServiceImp.findUserById(id));
        return "redirect:/admin";
    }
}
