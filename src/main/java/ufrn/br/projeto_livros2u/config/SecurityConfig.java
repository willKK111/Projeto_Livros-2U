package ufrn.br.projeto_livros2u.config;

import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import static org.springframework.security.config.web.server.ServerHttpSecurity.http;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain configure(HttpSecurity http)  throws Exception{
        return http
                .authorizeHttpRequests(
                        auth ->{
                            auth.requestMatchers("/index").permitAll();
                            auth.requestMatchers("/cadastroUsuario", "/cadusuario").permitAll();
                            auth.requestMatchers("/login").permitAll();
                            auth.requestMatchers("/admin", "/cadastro", "/deletar/{id}", "/editar/{id}","/salvar","/restaurar").hasRole("ADMIN");
                            auth.requestMatchers("/adicionarCarrinho/{id}","/finalizarCompra", "/verCarrinho").hasRole("USER");
                            auth.anyRequest().authenticated();
                        }
                ).formLogin(login -> login
                        .loginPage("/login")
                        .successHandler(authenticationSuccessHandler())
                        .permitAll()
                )
                .logout(l->{
                    l.logoutUrl("/logout");
                    l.clearAuthentication(true);
                    l.deleteCookies().invalidateHttpSession(true);
                })
                .build();
    }
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() { //Redireciona para determinad página a depender da Role do usuário
        return (request, response, auth) -> {
            String role = auth.getAuthorities().stream()
                    .findFirst()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .orElse("ROLE_USER");

            if(role.equals("ROLE_ADMIN")) {
                response.sendRedirect("/admin");
            } else if(role.equals("ROLE_USER")) {
                response.sendRedirect("/index");
            }
        };
    }
    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }
}