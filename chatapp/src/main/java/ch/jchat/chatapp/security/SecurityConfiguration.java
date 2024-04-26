package ch.jchat.chatapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import ch.jchat.chatapp.enums.EPlatformRoles;
import ch.jchat.chatapp.services.UserDetailService;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private UserDetailService userDetailService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth ->
        auth.requestMatchers("/","/api/auth/**").permitAll()
            .requestMatchers("/api/team/admin/**").hasRole(EPlatformRoles.ADMIN.name())
            .requestMatchers("/api/team/support/**").hasAnyRole(EPlatformRoles.ADMIN.name(), EPlatformRoles.SUPPORTER.name())
            .requestMatchers("/api/user/mvp/**").hasAnyRole(EPlatformRoles.ADMIN.name(), EPlatformRoles.SUPPORTER.name(), EPlatformRoles.MVP.name())
            .requestMatchers("/api/user/vip/**").hasAnyRole(EPlatformRoles.ADMIN.name(), EPlatformRoles.SUPPORTER.name(), EPlatformRoles.MVP.name(), EPlatformRoles.VIP.name())
            .requestMatchers("/api/user/**").hasAnyRole(EPlatformRoles.ADMIN.name(), EPlatformRoles.SUPPORTER.name(), EPlatformRoles.MVP.name(), EPlatformRoles.VIP.name(), EPlatformRoles.USER.name())
            .anyRequest().authenticated()
        );//.formLogin(Customizer.withDefaults())
        //.httpBasic(Customizer.withDefaults());
        return http.build();
    }

    /*
    @Bean
    public InMemoryUserDetailsManager userDetailsService(@Autowired PasswordEncoder encoder) {

        UserDetails admin = User.builder()
            .username("admin")
            .password("$2a$12$6Ph3dJJKllgHz.CLs49Iy.l4MnAEswp3HzTgXaxWIyXmNIycGxFfe")
            .roles(EPlatformRoles.ADMIN.name())
            .build();
        UserDetails user = User.builder()
            .username("user")
            .password("$2a$12$3TZ/WHECKEJ8ONOwi9yLNOK3UaCa5973j4sR5GEtjS/46BvhsfJsK")
            .roles(EPlatformRoles.USER.name())
            .build();
        return new InMemoryUserDetailsManager(admin,user);
    }
    //*/

    @Bean
    public UserDetailsService userDetailsService(){
        return userDetailService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
