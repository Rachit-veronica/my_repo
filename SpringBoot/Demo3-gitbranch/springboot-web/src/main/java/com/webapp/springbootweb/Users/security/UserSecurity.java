package com.webapp.springbootweb.Users.security;
import com.webapp.springbootweb.Users.Service.CustomUserDetailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) 
public class UserSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable() // if client is not browser , CDRF is a attack and spring security help us to
                                  // protect it
                .authorizeRequests()
                .antMatchers("/","/signin","/signin/**","/*.css").permitAll()
                // .antMatchers("/","/product/new","/product/edit/**","/product/save","/product/delete/**").hasRole("MODERATOR")
                .antMatchers("/product/**").hasRole("MODERATOR")
                .antMatchers("/product","/").hasAnyRole("USER","MODERATOR")
                .antMatchers("/user/**","/product/new","/product/edit/**","/product/save","/product/delete/**").hasRole("ADMIN")
                .anyRequest().authenticated().and().oauth2Login()
                .and()
                .exceptionHandling().accessDeniedPage("/403")
                .and()
                .formLogin()
                .loginPage("/signin")
                .loginProcessingUrl("/signin-processing")
                .defaultSuccessUrl("/", true)
                // .defaultSuccessUrl("/login")
                .failureUrl("/signin?error=true"); 
    }


    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // auth.inMemoryAuthentication().withUser("admin").password(this.passwordEncoder().encode("password")).roles("ADMIN");
        auth.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder());
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10); // its encode the pass having strenght =10
    }

    private ClientRegistration clientRegistration(){
        return CommonOAuth2Provider.GITHUB.getBuilder("github").clientId("568dbf350d94ae25cf0b")
                .clientSecret("38ee068096058add4eeb27f6379b86813c977299").build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(){
        ClientRegistration  clientRegistration = clientRegistration();
        return new InMemoryClientRegistrationRepository(clientRegistration);
    }
}