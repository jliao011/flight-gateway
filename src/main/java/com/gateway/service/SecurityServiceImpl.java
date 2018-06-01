package com.gateway.service;

import javax.naming.AuthenticationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

	private final static Logger logger = LoggerFactory.getLogger(SecurityServiceImpl.class);
	private UserDetailsService userDetailsService;
	private AuthenticationManager authenticationManager;

	@Autowired
	private SecurityServiceImpl(UserDetailsService userDetailsService, AuthenticationManager authenticationManager) {
		this.userDetailsService = userDetailsService;
		this.authenticationManager = authenticationManager;
	}

	@Override
	public boolean login(String username, String password) throws AuthenticationException, UsernameNotFoundException,
			DisabledException, LockedException, BadCredentialsException {
		// only return true or throw exception
		logger.info("in security service login for user {}, password {}", username, password);
		try {
			// this method throws UsernameNotFoundException
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			logger.info("Show user details for {}: {}, {}", username, userDetails.getPassword(),
					userDetails.getAuthorities());
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password,
					userDetails.getAuthorities());
			try {
				// this method throws AuthenticationException (runtime):
				// Disabled,Locked,BadCredentialException
				this.authenticationManager.authenticate(token);
			} catch (DisabledException e) {
				logger.info("Error: {}", e.getMessage());
				throw new DisabledException("User " + username + " is disabled.");
			} catch (LockedException e) {
				logger.info("Error: {}", e.getMessage());
				throw new LockedException("User " + username + " is locked.");
			} catch (BadCredentialsException e) {
				logger.info("Error: {}", e.getMessage());
				throw new BadCredentialsException("Your password for user " + username + " is not correct.");
			}
			boolean result = token.isAuthenticated();
			if (result) {
				SecurityContextHolder.getContext().setAuthentication(token);
				logger.info("Authenticated login for user: {}", username);
				return true;
			} else
				throw new AuthenticationException("Token cannot be authenticated correctly.");
		} catch (UsernameNotFoundException e) {
			throw new UsernameNotFoundException("User " + username + " not found.");
		}

	}

}
