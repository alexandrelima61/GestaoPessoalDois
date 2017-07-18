/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gerenciapessoal.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 *
 * @author jalima
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Bean
    @Override
    public AppUserDetailsService userDetailsService() {
        return new AppUserDetailsService();
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JsfLoginUrlAuthenticationEntryPoint jsfLoginEntry = new JsfLoginUrlAuthenticationEntryPoint();
        jsfLoginEntry.setLoginFormUrl("/Login.xhtml");
        jsfLoginEntry.setRedirectStrategy(new JsfRedirectStrategy());
        
        JsfAccessDeniedHandler jsfDeniedHandler = new JsfAccessDeniedHandler();
        jsfDeniedHandler.setLoginPath("/AcessoNegado.xhtml");
        jsfDeniedHandler.setContextRelative(true);
        
        http
            .csrf().disable()
            .headers().frameOptions().sameOrigin()
            .and()
                
            .authorizeRequests()                
                .antMatchers("/Login.xhtml","/Erro.xhtml","/javax.faces.resource/**")
                    .permitAll()
                .antMatchers("/Home.xhtml","/AcessoNegado.xhtml", "/usuarios/CadastroUsuario.xhtml")
                    .authenticated()
                .antMatchers("/usuarios/PesquisaUsuario.xhtml","/banco/CadastroBanco.xhtml")
                    .hasRole("ADMINISTRADORES")
                .antMatchers("/lancamentos/**","/conta/**","banco/PesquisaBanco.xhtml")
                    .hasAnyRole("COMUN","ADMINISTRADORES")
                .anyRequest().denyAll()
                .and()

            .formLogin()
                .loginPage("/Login.xhtml")
                .failureUrl("/Login.xhtml?invalid=true")
                .and()

            .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .and()
            
            .exceptionHandling()
                .accessDeniedPage("/AcessoNegado.xhtml")
                .authenticationEntryPoint(jsfLoginEntry)
                .accessDeniedHandler(jsfDeniedHandler);
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(new Md5PasswordEncoder());
    }
    
}
