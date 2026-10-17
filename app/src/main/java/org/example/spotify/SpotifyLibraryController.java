package org.example.spotify;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController 
@RequestMapping("/api/spotify")
public class SpotifyLibraryController {
    
    private final SpotifyClient spotifyClient;
    private final SpotifyImportService spotifyImportService;

    public SpotifyLibraryController (SpotifyClient spotifyClient,
        SpotifyImportService spotifyImportService ){

        this.spotifyClient = spotifyClient;
        this.spotifyImportService = spotifyImportService;
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

    @PostMapping("/import")
    public String importTracks(HttpSession session){

        String accessToken = (String) session.getAttribute("spotify_access_token");

        if(accessToken == null){
            throw new IllegalStateException(
                "Spotify is not connected !"
            );
        }

        int imported = spotifyImportService.importFirstPage(accessToken);

        return "Imported " + imported +" tracks";
    }

    @GetMapping("/debug/session")
    public Map<String, Object> debugSession(HttpSession session) {
    return Map.of(
        "sessionId", session.getId(),
        "hasAccessToken",
        session.getAttribute("spotify_access_token") != null
    );
}

}
