package org.example.spotify;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController 
@RequestMapping("/api/spotify")
public class SpotifyLibraryController {
    
    private final SpotifyClient spotifyClient;

    public SpotifyLibraryController (SpotifyClient spotifyClient){
        this.spotifyClient = spotifyClient;
    }

    @GetMapping("/tracks")
    public SpotifyTracksResponse getTracks(HttpSession session){
        
        String accessToken = (String) session.getAttribute("spotify_access_token");

        if(accessToken == null){
            throw new IllegalStateException(
                "Spotify is not conneted. Visit /api/spotify/login first"
            );
        }

        return spotifyClient.getSavedTracks(accessToken, 0, 50);
    }
}
