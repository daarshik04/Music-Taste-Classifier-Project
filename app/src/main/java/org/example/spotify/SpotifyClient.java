package org.example.spotify;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service 
public class SpotifyClient {
    
    private final SpotifyProperties properties;
    private final RestClient client;

    public SpotifyClient(SpotifyProperties properties){
        this.properties = properties;

        this.client = RestClient.builder().baseUrl(properties.getApiBaseUrl()).build();
        
    }
    public SpotifyTracksResponse getSavedTracks(String accessToken,int offset, int limit){
        return client.get()
                 .uri(uriBuilder -> uriBuilder
                                   .path("/me/tracks")
                                   .queryParam("limit",limit)
                                   .queryParam("offset", offset)
                                   .build())
                  .headers(headers ->
                        headers.setBearerAuth(accessToken))
                .retrieve()
                .body(SpotifyTracksResponse.class);                                    

                 
    }
}
