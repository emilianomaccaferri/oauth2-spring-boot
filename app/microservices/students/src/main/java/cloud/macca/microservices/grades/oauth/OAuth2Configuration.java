package cloud.macca.microservices.grades.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.logging.Logger;

@Configuration
@EnableWebSecurity
public class OAuth2Configuration {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                .anyRequest().authenticated()
        )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.decoder(rolesDecoder()))
                );
        return http.build();
    }

    @Bean
    JwtDecoder rolesDecoder(){
        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromIssuerLocation(issuerUri);
        OAuth2TokenValidator<Jwt> withEmailCheck = new DelegatingOAuth2TokenValidator<>(emailValidator());
        jwtDecoder.setJwtValidator(withEmailCheck);
        return jwtDecoder;
    }

    static class EmailVerifiedValidator implements OAuth2TokenValidator<Jwt> {
        OAuth2Error error = new OAuth2Error("email_not_verified", "You must verify your email to continue!", null);

        @Override
        public OAuth2TokenValidatorResult validate(Jwt token) {
            String clientId = token.getClaimAsString("client_id");
            if(clientId.equals("aggregator")){
                return OAuth2TokenValidatorResult.success();
            }
            boolean isEmailVerified = token.getClaimAsBoolean("email_verified");
            Logger.getGlobal().info(String.valueOf(isEmailVerified));
            if(!isEmailVerified){
                return OAuth2TokenValidatorResult.failure(error);
            }
            return OAuth2TokenValidatorResult.success();
        }
    }

    OAuth2TokenValidator<Jwt> emailValidator(){
        return new EmailVerifiedValidator();
    }

}
