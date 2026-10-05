package org.example.spotify;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    
    @GetMapping("/callback")
    public String callback(
        @RequestParam(required =false) String code,
        @RequestParam(required =false) String state,
        @RequestParam(required =false) String error,
        HttpSession session
    ){
        if (error !=null){
            return "Spotify authorization failed:" +error;
        }

        String expectedState = (String) session.getAttribute("spotify_oauth_state");

        if (expectedState ==null || !expectedState.equals(state)){
            return "Invalid OAuth state";
        }

        SpotifyTokenResponse token = authService.exchangeCodeForToken(code);

        session.setAttribute("spotify_access_token", token.getAccessToken());
        session.setAttribute("spotify_refresh_token", token.getRefreshToken());

        return "Spotify connected Successfully !";
    }

}
