package com.example.travelcamp.config;

import com.example.travelcamp.enumm.Role;
import com.example.travelcamp.services.UserAuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    public final UserAuthService userAuthService;

    public SecurityConfig(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    //определяем метод хэширования паролей хэш-функцией BCrypt
    @Bean
    public PasswordEncoder getPasswordEncode(){
        return new BCryptPasswordEncoder();
    }

    //HttpSecurity отвечает за объект аутентификации
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        // --- начало настройки доступа --- //
        //Конфигурируем работу SS
        http
//                .cors()
//                .and()
                // указываем, что все страницы должны быть защищены аутентификацией
                .authorizeHttpRequests()
                // Указываем доступы к страницам для разных ролей
                .requestMatchers("/admin/**").hasAuthority(Role.ADMIN.name())
                .requestMatchers("/user/**").hasAuthority(Role.USER.name())
                //Указываем, что указанные страницы доступны всем пользователям.
                // Добавляем в этот список также таблицы стилей, JS, папки с картинками и тд (т.к. в момент, когда у пользователя не будет роли - ему все это будет недоступно)
               .requestMatchers("/authentication", "/registration", "/tours", "/tour/info/{id}", "/tour/search",
                       "/index","/error", "/person_account", "/logout", "/resources/**", "/static/**",
                       "/css/**", "/js/**", "/img/**").permitAll()
                // указываем, что все остальные страницы доступны user и admin
//                .anyRequest().hasAnyRole(Role.ADMIN.name(), Role.MODERATOR.name(),Role.USER.name(), Role.GUIDE.name())
        // --- конец настройки доступа --- //
                .and() //дальше настраивается аутентификация, соединяем ее с настройкой доступа
                //указываем какой url запрос будет отправляться при заходе на защищенные страницы
                .formLogin().loginPage("/authentication")
    //указываем на какой адрес будут отправляться данные с формы. Задаем url, который используется по умолчанию для обработки
    // формы аутентификации по средствам SS. SS будет ждать объект с формы аутентиф и затем сверять логин и пароль с данными в БД
                .loginProcessingUrl("/process_login")
    //Указываем на какой url необходимо направить пользователя после успешной аутентификации.
    // Вторым аргументом указывается true чтобы перенаправление шло в любом случае после успешной аутентификации
                .defaultSuccessUrl("/person_account", true)
    // Указываем, куда перенаправлять пользователя при неудачной аутентификации. В запрос будет передан объект error,
    // который будет проверяться на форме и при наличии данного объекта в запросе выводится сообщение, указанное в представлении ("Неправильный логин или пароль").
                .failureUrl("/authentication?error")
    // --- добавляем логаут --- //
    //указываем по какому url будет осуществлён логаут и куда направит после успешного логаута
    //всё это реализуется стандартными средствами и методами SS.
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/authentication");
        return http.build();
    }

    //Указываем, что аутентификация приложения будет осуществляться с помощью базовых настроек SS: authenticationManagerBuilder
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        //добавляем хэширование пароля при аутентификации
        authenticationManagerBuilder.userDetailsService(userAuthService).passwordEncoder(getPasswordEncode());
    }
}
