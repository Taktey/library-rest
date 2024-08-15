package ru.libraryRest.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTUtil {

    @Value("${jwt_secret}")
    public String secret;

    @Value("${jwt_issuer}")
    public String issuer;

    public String generateToken(String username, String role) {
        return JWT.create()
                .withSubject("Details about user")
                .withClaim("username", username)
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withIssuer(issuer)
                .withExpiresAt(Date.from(ZonedDateTime.now().plusMinutes(60).toInstant()))
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndReturnUsername(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("Details about user")
                .withIssuer(issuer)
                .build();
        DecodedJWT verify = verifier.verify(token);
        return verify.getClaim("username").asString();
    }


    public String getUsername(String tokenFromRequest) {
        String onlyToken = tokenFromRequest.substring(7);
        return validateTokenAndReturnUsername(onlyToken);
    }
}
