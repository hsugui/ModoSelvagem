package br.com.rd.ModoSelvagem.security;

import br.com.rd.ModoSelvagem.model.entity.Customer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    @Value("${selvagem.jwt.expiration}")
    private String expiration;

    @Value("${selvagem.jwt.secret}")
    private String secret;

    public String generateToken(Authentication authentication) {
        Customer logged = (Customer) authentication.getPrincipal();
        Date today = new Date();
        Date expDat = new Date(today.getTime() + Long.parseLong(expiration));

        return Jwts.builder()
                .setIssuer("Modo Selvagem API")
                .setSubject(logged.getId().toString())
                .setIssuedAt(today)
                .setExpiration(expDat)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public Long getIdCustomer(String token) {
        Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }
}
