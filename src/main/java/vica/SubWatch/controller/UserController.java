package vica.SubWatch.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vica.SubWatch.domain.*;
import vica.SubWatch.service.JwtService;
import vica.SubWatch.service.UserService;


@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final JwtService jwtService;
    private final UserService userService;

    public UserController(JwtService jwtService,UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody UserRegisterDTO userRegisterDTO) {
        User newUser = userService.registerUser(userRegisterDTO);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@Validated @RequestBody UserLoginDTO loginUserDto) {
        User authenticatedUser = userService.loginUser(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());
        loginResponse.setUserId(authenticatedUser.getId());

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {
        boolean isChanged = userService.changePassword(request.getEmail(), request.getNewPassword());
        if (isChanged) {
            return ResponseEntity.ok("Password changed successfully");
        } else {
            return ResponseEntity.status(400).body("Password change failed");
        }
    }


}
