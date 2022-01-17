package br.com.rd.ModoSelvagem.security;

import br.com.rd.ModoSelvagem.model.entity.Customer;
import br.com.rd.ModoSelvagem.repository.CustomerRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationViaTokenFilter extends OncePerRequestFilter {

    private TokenService tokenService;
    private CustomerRepository customerRepository;

    public AuthenticationViaTokenFilter(TokenService tokenService, CustomerRepository customerRepository) {
        this.tokenService = tokenService;
        this.customerRepository = customerRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = retrieveToken(request);
        boolean valid = tokenService.isTokenValid(token);

        if (valid) {
            authenticateCustomer(token);
        }

        filterChain.doFilter(request, response);

    }

    private void authenticateCustomer(String token) {
        Long idCustomer = tokenService.getIdCustomer(token);
        Customer customer = customerRepository.findById(idCustomer).get();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(customer, null, customer.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String retrieveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
            return null;
        }
        return token.substring(7, token.length());
    }

}
