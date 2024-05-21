package com.nighthawk.spring_portfolio;

import org.springframework.beans.factory.annotation.Autowired;
import com.nighthawk.spring_portfolio.mvc.jwt.JwtAuthenticationEntryPoint;
import com.nighthawk.spring_portfolio.mvc.jwt.JwtRequestFilter;
import com.nighthawk.spring_portfolio.mvc.person.PersonDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

import java.util.Arrays;


/*
* To enable HTTP Security in Spring
*/
@Configuration
@EnableWebSecurity  // Beans to enable basic Web security
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	private PersonDetailsService personDetailsService;

    // @Bean  // Sets up password encoding style
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// configure AuthenticationManager so that it knows from where to load
		// user for matching credentials
		// Use BCryptPasswordEncoder
		auth.userDetailsService(personDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	
    // Provide security configuration
		@Bean
		public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
			http
				.csrf(csrf -> csrf
					.disable()
				)
				// list the requests/endpoints need to be authenticated
				.authorizeHttpRequests(auth -> auth
					.requestMatchers(HttpMethod.POST,"/authenticate").permitAll()
				    .requestMatchers(HttpMethod.POST, "/api/person/**").permitAll()
					.requestMatchers(HttpMethod.GET, "/api/person/**").authenticated()
					.requestMatchers(HttpMethod.PUT, "/api/person/**").authenticated()
					.requestMatchers("/mvc/person/update/**", "/mvc/person/delete/**").hasAnyAuthority("ROLE_ADMIN")
					.requestMatchers("/api/person/delete/**", "/api/class_period/delete/**").hasAnyAuthority("ROLE_ADMIN")
					.requestMatchers("/api/person/delete/self").hasAnyAuthority("ROLE_USER")
					//.requestMatchers("/api/class_period/post/**", "/api/class_period/set_seating_chart").authenticated()
					.requestMatchers("/api/person/post/**").permitAll()
					.requestMatchers("/**").permitAll()
				)
				// support cors
				.cors(Customizer.withDefaults())
				.headers(headers -> headers
					.addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Credentials", "true"))
					.addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-ExposedHeaders", "*", "Authorization"))
					.addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Headers", "Content-Type", "Authorization", "x-csrf-token"))
					.addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-MaxAge", "600"))
					.addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Methods", "POST", "GET", "OPTIONS", "HEAD", "PATCH", "DELETE"))
					//.addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Origin", "https://john-scc.github.io"))
					//.addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Origin", "http://localhost:4100"))
				)
				.formLogin(form -> form 
					.loginPage("/login")
				)
				.logout(logout -> logout
					.deleteCookies("jwt")
					.logoutSuccessUrl("/")
				)
				// make sure we use stateless session; 
				// session won't be used to store user's state.
				.exceptionHandling(exceptions -> exceptions
					.authenticationEntryPoint(jwtAuthenticationEntryPoint)
				)
				.sessionManagement(session -> session
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
				// Add a filter to validate the tokens with every request
				.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
			return http.build();
	}
	// used for login on blog
	/*
	 * private CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-csrf-token"));
		configuration.setExposedHeaders(Arrays.asList("authorization"));
		configuration.setAllowCredentials(true);
		configuration.setAllowedOrigins(Arrays.asList("https://john-scc.github.io"));
		//configuration.setAllowedOrigins(Arrays.asList("http://localhost:4100"));


		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	 */
}
