package org.example.spotify;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingDeque;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import com.google.common.net.MediaType;

@Service
public class SpotifyAuthService {

    private final SpotifyProperties properties;

    public SpotifyAuthService(SpotifyProperties properties){
        this.properties = properties;
    }

    public String buildAuthorizationUrl(String state){

        String scope = "user-library-read";

        return properties.getAuthorizationUrl()
                +"?response_type=code"
                +"&client_id=" +encode(properties.getClientId())
                +"&scope=" +encode(scope)
                +"&state=" +encode(state);
    }

    public String generateState(){
        return UUID.randomUUID().toString();
    }

    private String encode(String value){
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
        
    }

    public SpotifyTokenResponse exchangeCodeForToken(String code){

        RestClient client = RestClient.create();

        String credentials = properties.getClientId() + ":" +properties.getClientSecret();

        String basicAuth = Base64.getEncoder()
                .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        LinkedMultiValueMap<String, String> form = new LinkedMultiValueMap<>()  ;    
        
        form.add("grant_type", "authorization_code");
        form.add("code", code);
        form.add("redirect_url", properties.getRedirectUri());

        return client.post()
            .uri(properties.getTokenUrl())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header("Authorization", "Basic " + basicAuth)
            .body(form)
            .retrieve()
            .body(SpotifyTokenResponse.class);
    }

}
