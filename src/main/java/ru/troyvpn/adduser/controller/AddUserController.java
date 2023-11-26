package ru.troyvpn.adduser.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.troyvpn.adduser.service.UserService;

@RestController
@RequestMapping("/api/v1/")
public class AddUserController {
    private final UserService userService;

    public AddUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("addUser/{username}")
    private ResponseEntity<String> addUser(@PathVariable String username) {
        String res = userService.addUser(username);
        return ResponseEntity.ok(res);
    }

    @GetMapping("removeUser/{username}")
    private ResponseEntity<String> removeUser(@PathVariable String username) {
        String res = userService.removeUser(username);
        return ResponseEntity.ok(res);
    }

    @GetMapping("getPass/{username}")
    private ResponseEntity<String> getPasswordByUsername(@PathVariable String username) {
        String pw = userService.getPasswordByUsername(username);
        return ResponseEntity.ok(pw);
    }


}
