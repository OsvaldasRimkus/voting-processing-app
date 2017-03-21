package voting.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import voting.model.CountyRep;
import voting.service.security.UserDetailsServiceImpl;

/**
 * Created by andrius on 3/6/17.
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/api/auth/**", "api/results/**").permitAll()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .successForwardUrl("/api/auth/authorities")
                    .permitAll()
                    .and()
                .httpBasic()
                    .and()
                .logout()
                    .logoutSuccessUrl("/api/auth/logout")
                    .permitAll();

        http.headers().frameOptions().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("admin.admin").password("admin").roles("ADMIN");
        auth.userDetailsService(userDetailsService).passwordEncoder(CountyRep.PASSWORD_ENCODER);
    }
}
