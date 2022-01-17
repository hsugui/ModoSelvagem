package br.com.rd.ModoSelvagem.security;

import br.com.rd.ModoSelvagem.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static java.util.Arrays.asList;

@EnableWebSecurity
@Configuration
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private CustomerRepository customerRepository;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(asList("Authorization", "Cache-Control", "Content-Type"));
        corsConfiguration.setAllowedOrigins(asList("http://localhost:3000"));
        corsConfiguration.setAllowedMethods(asList("GET", "POST", "PUT", "DELETE", "PUT", "OPTIONS", "PATCH", "DELETE"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setExposedHeaders(asList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    //Authentication config
    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authenticationService).passwordEncoder(new BCryptPasswordEncoder());
    }

    //Authorization config
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/products").permitAll()
                .antMatchers(HttpMethod.GET, "/products/*").permitAll()
                //.antMatchers(HttpMethod.GET, "/categories").permitAll()
                .antMatchers(HttpMethod.POST, "/products").permitAll()
                .antMatchers(HttpMethod.GET, "/products/category/*").permitAll()
                .antMatchers(HttpMethod.GET, "/products/subcategory/*").permitAll()
                .antMatchers(HttpMethod.GET, "/categories").permitAll()
                .antMatchers(HttpMethod.GET, "/categories/*").permitAll()
                .antMatchers(HttpMethod.POST, "/categories").permitAll()
                .antMatchers(HttpMethod.GET, "/subcategories").permitAll()
                .antMatchers(HttpMethod.GET, "/home/*").permitAll()
                .antMatchers(HttpMethod.POST, "/subcategories").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.GET, "/customers").permitAll()
                .antMatchers(HttpMethod.POST, "/customers").permitAll()
                .antMatchers(HttpMethod.GET, "/customers/*").permitAll()
                .antMatchers(HttpMethod.PUT, "/customers/*").permitAll()
                .antMatchers(HttpMethod.GET, "/success").permitAll()
                .antMatchers(HttpMethod.GET, "/success/*").permitAll()
                .antMatchers(HttpMethod.POST, "/success").permitAll()
                .antMatchers(HttpMethod.GET, "/checkout").permitAll()
                .antMatchers(HttpMethod.POST, "/checkout").permitAll()
                .antMatchers(HttpMethod.POST, "/sending-email").permitAll()
                .antMatchers(HttpMethod.GET, "/invoices/*").permitAll()
                .antMatchers(HttpMethod.GET, "/invoices/order/*").permitAll()
                .antMatchers(HttpMethod.GET, "/dashboard/account/*").permitAll()
                .antMatchers(HttpMethod.PUT, "/dashboard/account/*").permitAll()
                .antMatchers(HttpMethod.GET, "/dashboard/orders").permitAll()
                .antMatchers(HttpMethod.GET, "/dashboard/orders/*").permitAll()
                .antMatchers(HttpMethod.GET, "/dashboard/account/*/orders").permitAll()
                .antMatchers(HttpMethod.GET, "/dashboard/account/*/orders/*").permitAll()
                .antMatchers(HttpMethod.POST, "/dashboard/account/*/addresses").permitAll()
                .antMatchers(HttpMethod.GET, "/dashboard/account/*/addresses").permitAll()
                .antMatchers(HttpMethod.GET, "/dashboard/account/*/addresses/*").permitAll()
                .antMatchers(HttpMethod.PUT, "/dashboard/account/*/addresses/*").permitAll()
                .antMatchers(HttpMethod.DELETE, "/dashboard/account/*/addresses/*").permitAll()
                .antMatchers(HttpMethod.GET, "/contact/*").permitAll()
                .antMatchers(HttpMethod.POST, "/contact").permitAll()
                .antMatchers(HttpMethod.POST, "/forgotPassword").permitAll()
                .antMatchers(HttpMethod.GET, "/forgotPassword/reset").permitAll()
                .antMatchers(HttpMethod.POST, "/forgotPassword/reset/*").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(new AuthenticationViaTokenFilter(tokenService, customerRepository), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**");
    }
}
