package de.thbingen.epro;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	@override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
				.withUser("User1")
				.password("password1")
				.roles("ADMIN")
				.and()
				.withUser("User2")
				.password("password2")
				.roles("USER")
				.and()
				.withUser("User3")
				.password("password2")
				.roles("BU_ADMIN");
	}
}