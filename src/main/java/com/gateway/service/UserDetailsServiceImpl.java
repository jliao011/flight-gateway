package com.gateway.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gateway.domain.User;
import com.gateway.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final static Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
	private UserRepository userRepository;

	@Autowired
	private UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = this.userRepository.findByEmail(username);
		logger.info("Find user in UserDetailsService: {}", username);
		if (user == null) {
			logger.info("Find user {} result is null,throw exception here.", username);
			throw new UsernameNotFoundException("User not found for email " + username);
		}
		logger.info("Find user result returned: {}", user.toString());
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				user.getRoles());
	}

}
