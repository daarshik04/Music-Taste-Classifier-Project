package org.example.spotify;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController 
@RequestMapping("/api/spotify")
public class SpotifyAuthController {

    private final SpotifyAuthService authService;

    public SpotifyAuthController(SpotifyAuthService authService){
        this.authService = authService;
    }

    @GetMapping("/login")
    public void login(HttpSession session, HttpServletResponse response) throws IOException{

        String state = authService.generateState();
        session.setAttribute("spotify_oauth_state", state);

        String authorizationUrl = authService.buildAuthorizationUrl(state);

        response.sendRedirect(authorizationUrl);
        
    }

}
