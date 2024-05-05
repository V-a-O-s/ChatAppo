package ch.jchat.chatapp.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ch.jchat.chatapp.enums.EPlatformRoles;
import ch.jchat.chatapp.exceptions.CustomAccessDeniedHandler;
import ch.jchat.chatapp.security.jwt.JwtAuthenticationFilter;
import ch.jchat.chatapp.services.UserDetailService;
import lombok.AllArgsConstructor;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {

    private UserDetailService userDetailService;
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private CustomAccessDeniedHandler accessDeniedHandler;
    private CustomLogoutHandler customLogoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth ->
        auth.requestMatchers("/api/auth/**","/swagger-ui/**","/v3/api-docs/**").permitAll()
            .requestMatchers("/api/v*/team/admin/**").hasRole(EPlatformRoles.ADMIN.name())
            .requestMatchers("/api/v*/team/support/**","/api/auth/s/**").hasAnyRole(EPlatformRoles.ADMIN.name(), EPlatformRoles.SUPPORTER.name())
            .requestMatchers("/api/v1/user/**").hasAnyRole(EPlatformRoles.ADMIN.name(), EPlatformRoles.SUPPORTER.name(), EPlatformRoles.MVP.name(), EPlatformRoles.VIP.name(), EPlatformRoles.USER.name())
            .anyRequest()
            .authenticated()
        ).userDetailsService(userDetailService)
        .exceptionHandling(e->e.accessDeniedHandler(accessDeniedHandler)
        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
        .sessionManagement(session -> session
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .logout(l->l.logoutUrl("/api/auth/logout")
            .addLogoutHandler(customLogoutHandler)
            .logoutSuccessHandler(
                (request, response, authentication) -> SecurityContextHolder.clearContext()
            ))
        .build();
        
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
