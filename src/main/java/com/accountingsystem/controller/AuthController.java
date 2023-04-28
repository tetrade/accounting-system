package com.accountingsystem.controller;


import com.accountingsystem.configs.jwt.JwtUtils;
import com.accountingsystem.controller.dtos.UserDto;
import com.accountingsystem.controller.dtos.mappers.UserMapper;
import com.accountingsystem.service.UserDetailsImpl;
import com.accountingsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth-api/")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	UserMapper userMapper;

	@Autowired
	UserService userService;

	@PostMapping("/sign-in")
	public ResponseEntity<UserDto> authUser(@RequestBody LoginRequest loginRequest, HttpServletResponse httpServletResponse) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(
						loginRequest.getLogin(), loginRequest.getPassword()
				));


		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		// В случае если работа будет происходить с куки
		Cookie cookie = new Cookie("jwt", jwt);
		cookie.setPath("/");
		cookie.setMaxAge(36000);
		httpServletResponse.addCookie(cookie);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		return ResponseEntity.ok(userMapper.mapToUserDto(userDetails));
	}

	@PostMapping("/sign-up")
	public ResponseEntity<Void> registerUser(@RequestBody SignUpRequest signupRequest) {
		userService.createNewUser(signupRequest);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
