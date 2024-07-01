package cloud.macca.microservices.frontend.filter;

import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Component
public class RefreshTokenFilter extends OncePerRequestFilter {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkEndpoint;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return !path.startsWith("/profile");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // we want to refresh the access token, if any, if expired!

        Cookie[] cookies = request.getCookies();
        String refreshToken = Arrays.stream(cookies).filter(c -> c.getName().equals("refresh_token")).toString();
        String accessToken = Arrays.stream(cookies).filter(c -> c.getName().equals("access_token")).toString();

        if(refreshToken == null || accessToken == null){
            // keep the request going, it will eventually fail if the accessToken is null!
            filterChain.doFilter(request, response);
        }
        JwkProvider jwkProvider = new JwkProviderBuilder(jwkEndpoint)
                .build();
        RSAKeyProvider keyProvider = new RSAKeyProvider() {
            // we just need to implement this because we know that we will only
            // use this provider to verify jwts!
            @Override
            public RSAPublicKey getPublicKeyById(String s) {
                try {
                    return (RSAPublicKey) jwkProvider.get(s).getPublicKey();
                } catch (JwkException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public RSAPrivateKey getPrivateKey() {
                return null;
            }

            @Override
            public String getPrivateKeyId() {
                return null;
            }
        };

        Algorithm algo = Algorithm.RSA256(keyProvider);
        DecodedJWT jwtAccessToken = JWT.require(algo).build().verify(accessToken);
        if(jwtAccessToken.getExpiresAt().getTime() < Date.from(Instant.now()).getTime()){
            // if the token is expired, then refresh it using the oauth2 refresh token mechanism
        }
    }

}