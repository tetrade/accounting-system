package com.accountingsystem.controller;


import com.accountingsystem.configs.jwt.JwtProperties;
import com.accountingsystem.configs.jwt.JwtUtils;
import com.accountingsystem.controller.dtos.LoginRequest;
import com.accountingsystem.controller.dtos.SignUpRequest;
import com.accountingsystem.controller.dtos.UserDto;
import com.accountingsystem.controller.dtos.mappers.UserMapper;
import com.accountingsystem.service.UserDetailsImpl;
import com.accountingsystem.service.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("api/auth/")
@CrossOrigin(allowCredentials = "true", originPatterns = "*")
@Setter
@Getter
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;
    private final UserService userService;
    private final JwtProperties jwtProperties;

    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserMapper userMapper,
                          UserService userService, JwtProperties jwtProperties) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userMapper = userMapper;
        this.userService = userService;
        this.jwtProperties = jwtProperties;

    }

    @PostMapping("/sign-in")
    public ResponseEntity<UserDto> authUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse httpServletResponse,
                                            HttpServletRequest httpServletRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getLogin(), loginRequest.getPassword()
                ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setPath("/");
        cookie.setMaxAge(jwtProperties.getExpirationMs());
        httpServletResponse.addCookie(cookie);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        userService.logUserLogin(userDetails.getLogin(), httpServletRequest.getRemoteAddr());

        return ResponseEntity.ok(userMapper.mapToUserDto(userDetails));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> registerUser(@Valid @RequestBody SignUpRequest signupRequest) {
        userService.createNewUser(signupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
