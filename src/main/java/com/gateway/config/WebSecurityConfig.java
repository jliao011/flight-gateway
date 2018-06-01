package com.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {

//		http.formLogin().loginPage("/login").permitAll().and().logout().logoutSuccessUrl("/login");
//		http.httpBasic();
//		http.csrf().disable();
//		http.authorizeRequests().antMatchers("/reservations/**", "/startCheckIn", "/confirmCheckIn")
//				.hasAnyAuthority("USER");
//		http.authorizeRequests()
//				.antMatchers("/", "/stylesheets/**", "/login", "/loginAction", "/register", "/registerAction",
//						"/flightReservation", "/findFlightsAction", "/reservations/**")
//				.permitAll().anyRequest().authenticated();

		http.authorizeRequests()
				.antMatchers("/", "/stylesheets/**", "/login", "/loginAction", "/register", "/registerAction",
						"/flightReservation", "/findFlightsAction", "/startCheckIn", "/reservations/**")
				.permitAll()
				.antMatchers("/showReservation", "/completeReservation", "/checkin", "/startCheckIn", "/confirmCheckIn")
				.hasAnyAuthority("ADMIN", "USER")
				.antMatchers("/addFlights", "/updateFlight", "/updateFlightAction", "deleteFlight")
				.hasAnyAuthority("ADMIN").and().formLogin().loginPage("/login").and().logout()
				.logoutSuccessUrl("/login").and().httpBasic().and().csrf().disable();

//		http.authorizeRequests()
//				.antMatchers("/", "/stylesheets/**", "/login", "/loginAction", "/register", "/registerAction",
//						"/flightReservation", "/findFlightsAction", "/reservations/**")
//				.permitAll().antMatchers("/reservations/**", "/startCheckIn", "/confirmCheckIn").hasAnyAuthority("USER")
//				.anyRequest().authenticated().and().formLogin().loginPage("/login").permitAll().and().logout()
//				.logoutSuccessUrl("/login").and().httpBasic().and().csrf().disable();
	}

	// create bean for encoder
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

}
