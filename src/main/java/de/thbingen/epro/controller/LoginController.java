package de.thbingen.epro.controller;

import de.thbingen.epro.security.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    UserAuthenticationService authentication;

    @PostMapping("/login")
    String login(@RequestBody Map<String, String> body) {
        return authentication.login(body.get("username"), body.get("password"))
                .orElseThrow(() -> new RuntimeException("invalid login and/or password"));
    }
}
