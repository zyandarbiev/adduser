package ru.troyvpn.adduser.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.troyvpn.adduser.service.AddUserService;

@RestController
@RequestMapping("/api/v1/addUser")
public class AddUserController {
    private final AddUserService addUserService;

    public AddUserController(AddUserService addUserService) {
        this.addUserService = addUserService;
    }

    @GetMapping("/{username}")
    private ResponseEntity<String> addUser(@PathVariable String username) {
        String res = addUserService.addUser(username);
        return ResponseEntity.ok(res);
    }
}
