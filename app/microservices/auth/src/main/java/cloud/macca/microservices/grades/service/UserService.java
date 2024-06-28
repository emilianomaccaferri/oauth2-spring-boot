package cloud.macca.microservices.grades.service;

import cloud.macca.microservices.grades.dto.User;
import cloud.macca.microservices.grades.error.ExpiredAccessTokenError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Service
public class UserService {
    private final RestClient http;

    @Value("${auth.endpoint}")
    private String authEndpoint;

    public UserService(
            RestClient.Builder builder
    ){
        this.http = builder.build();
    }

    public User getUserInfo(String accessToken){
        return this.http.get()
                .uri(authEndpoint + "/protocol/openid-connect/userinfo")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    throw new ExpiredAccessTokenError("access token expired");
                })
                .body(User.class);
    }

}
