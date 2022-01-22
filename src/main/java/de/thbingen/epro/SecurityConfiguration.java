package de.thbingen.epro;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;



@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	//setting up Authentication
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
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
	}

	//setting up Authorization
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/administration").hasRole("ADMIN")
				.antMatchers("/restricted").hasAnyRole("ADMIN", "BU_ADMIN")
				.antMatchers("/something").hasAnyRole("ADMIN", "BU_ADMIN", "USER")
				.antMatchers("/").permitAll()
				.and().formLogin();
	}
	
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}