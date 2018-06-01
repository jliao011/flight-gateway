package com.gateway.service;

import javax.naming.AuthenticationException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface SecurityService {
	boolean login(String username, String password) throws AuthenticationException, UsernameNotFoundException,
			DisabledException, LockedException, BadCredentialsException;
}
