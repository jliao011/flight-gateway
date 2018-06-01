package com.gateway.controller;

import java.util.Arrays;

import javax.naming.AuthenticationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gateway.domain.User;
import com.gateway.repository.RoleRepository;
import com.gateway.repository.UserRepository;
import com.gateway.service.SecurityService;

@Controller
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private SecurityService securityService;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
			SecurityService securityService, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.securityService = securityService;
		this.roleRepository = roleRepository;
	}
//	@Autowired
//	private UserController(UserRepository userRepository, SecurityService securityService) {
//		this.userRepository = userRepository;
//		this.securityService = securityService;
//	}

	@RequestMapping("/register")
	public String showRegistrationPage() {
		return "views/registerView";
	}

	@RequestMapping("/login")
	public String ShowLoginPage() {
		return "views/loginView";
	}

	@RequestMapping(value = "/registerAction", method = RequestMethod.POST)
	public String register(@ModelAttribute("user") User user, ModelMap modelMap) {
		User duplicate = this.userRepository.findByEmail(user.getEmail());
		if (duplicate != null) {
			modelMap.addAttribute("error", "User with email " + user.getEmail() + " already exists.");
			return "views/registerView";
		}
		String inputPassword = user.getPassword();
		user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRoles(Arrays.asList(this.roleRepository.findByName("USER")));
		user = this.userRepository.save(user);
		logger.info("User created: {}", user.toString());
		try {
			this.securityService.login(user.getEmail(), inputPassword);
			logger.info("Login successfull");
			return "views/index";
		} catch (DisabledException | LockedException | AuthenticationException | UsernameNotFoundException
				| BadCredentialsException e) {
			modelMap.addAttribute("error", e.getMessage());
			return "views/registerView";
		}
	}

	@RequestMapping(value = "/loginAction", method = RequestMethod.POST)
	public String login(@RequestParam("email") String email, @RequestParam("password") String password,
			ModelMap modelMap) {
		logger.info("User login name {}, password {}", email, password);
//		User user = this.userRepository.findByEmail(email);
//		if (user == null || !user.getPassword().equals(password)) {
//			modelMap.addAttribute("error", "Invalid email or password.");
//			logger.warn("User with Email: {}, Password: {} does not exist", email, password);
//			return "views/loginView";
//		}
//		logger.info("User login successfull: {}", user.toString());
//		return "/index";
		try {
			this.securityService.login(email, password);
			logger.info("Login successfull");
			return "views/index";
		} catch (DisabledException | LockedException | AuthenticationException | UsernameNotFoundException
				| BadCredentialsException e) {
			modelMap.addAttribute("error", e.getMessage());
			logger.warn("Error when login: {}", e.getMessage());
			return "views/loginView";
		}
	}

}
