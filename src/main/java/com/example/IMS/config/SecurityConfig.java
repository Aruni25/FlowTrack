package com.example.IMS.config;

import com.example.IMS.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                // Public resources
                .antMatchers("/css/**", "/js/**", "/images/**", "/api/chatbot/**").permitAll()
                .antMatchers("/", "/home", "/landing", "/about", "/pricing", "/get-started").permitAll()
                
                // Registration & Authentication
                .antMatchers("/register/**", "/login").permitAll()
                
                // Platform Admin Routes
                .antMatchers("/admin/**", "/platform/**").hasAuthority("ROLE_PLATFORM_ADMIN")
                
                // Retailer Routes
                .antMatchers("/retailer/**", "/inventory/**", "/transactions/**").hasAuthority("ROLE_RETAILER")
                
                // Vendor Routes
                .antMatchers("/vendor/**", "/orders/**", "/products/**").hasAuthority("ROLE_VENDOR")
                
                // Investor Routes
                .antMatchers("/investor/**", "/investments/**", "/portfolio/**").hasAuthority("ROLE_INVESTOR")
                
                // Legacy routes - will be refactored
                .antMatchers("/ItemCreate", "/ItemEdit/**", "/ItemDelete/**").hasAnyAuthority("ROLE_PLATFORM_ADMIN", "ROLE_RETAILER")
                .antMatchers("/vendors/**").hasAnyAuthority("ROLE_PLATFORM_ADMIN", "ROLE_RETAILER")
                
                .anyRequest().authenticated()
            .and()
            .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler((request, response, authentication) -> {
                    // Role-based redirect after login
                    String role = authentication.getAuthorities().iterator().next().getAuthority();
                    if (role.equals("ROLE_PLATFORM_ADMIN")) {
                        response.sendRedirect("/admin/dashboard");
                    } else if (role.equals("ROLE_RETAILER")) {
                        response.sendRedirect("/retailer/dashboard");
                    } else if (role.equals("ROLE_VENDOR")) {
                        response.sendRedirect("/vendor/dashboard");
                    } else if (role.equals("ROLE_INVESTOR")) {
                        response.sendRedirect("/investor/dashboard");
                    } else {
                        response.sendRedirect("/");
                    }
                })
                .failureUrl("/login?error=true")
                .permitAll()
            .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/landing?logout=true")
                .permitAll();
    }
}
