package de.thbingen.epro.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;



@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsService userDetailsService;

	//setting up Authentication
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		/*
		auth.inMemoryAuthentication()
				.withUser("User1")
				.password(encoder().encode("password1"))
				.roles("ADMIN")
				.and()
				.withUser("User2")
				.password(encoder().encode("password2"))
				.roles("USER")
				.and()
				.withUser("User3")
				.password(encoder().encode("password3"))
				.roles("BU_ADMIN");
		 */

		auth.userDetailsService(userDetailsService);
	}

	//setting up Authorization
	/*
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/users/**", "/roles/**", "/privileges/**").hasRole("ADMIN")
				.antMatchers("/businessUnits").hasAnyRole("ADMIN", "BU_ADMIN", "USER")
				.antMatchers("/companyobjectives").hasAnyRole("ADMIN", "BU_ADMIN", "USER")
				.antMatchers("/companyKeyResultHistory").hasAnyRole("ADMIN", "BU_ADMIN", "USER")
				.antMatchers("/businessUnitKeyResultHistory").hasAnyRole("ADMIN", "BU_ADMIN", "USER")
				.antMatchers("/").permitAll()
				.and().formLogin();
	}
	 */


	@Bean
	public PasswordEncoder encoder() {
		return NoOpPasswordEncoder.getInstance();
	}

}