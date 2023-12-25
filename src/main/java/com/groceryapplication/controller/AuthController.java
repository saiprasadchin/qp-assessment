package com.groceryapplication.controller;

import com.groceryapplication.dto.LoginRequest;
import com.groceryapplication.dto.LoginResponse;
import com.groceryapplication.dto.RegisterRequest;
import com.groceryapplication.models.user.Role;
import com.groceryapplication.models.user.User;
import com.groceryapplication.repository.RoleRepository;
import com.groceryapplication.repository.UserRepository;
import com.groceryapplication.security.JWTGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTGenerator jwtGenerator;

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginDto) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        Optional<User> user = userRepository.findByUsername(loginDto.getUsername());

        return  new ResponseEntity<>(new LoginResponse(token,user.get()), HttpStatus.OK);
    }


    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerDto) {
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            return new ResponseEntity<>("Username is taken!", HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        Role roles = roleRepository.findByName("USER").get();
        user.setRoles(Collections.singletonList(roles));
        userRepository.save(user);
        return  new ResponseEntity<>("User register success!", HttpStatus.OK);
    }

    @GetMapping("heart-beat")
    public ResponseEntity<String> heartBeatApi() {
        return new ResponseEntity<>("Application is up and running...", HttpStatus.OK);
    }
}
