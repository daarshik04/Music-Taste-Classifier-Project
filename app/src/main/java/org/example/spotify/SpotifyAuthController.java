package org.example.spotify;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${app.frontend-url}")
    private String frontendUrl;

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
    public void callback(
        @RequestParam(required =false) String code,
        @RequestParam(required =false) String state,
        @RequestParam(required =false) String error,
        HttpSession session,HttpServletResponse response
    ) throws IOException{

        String expectedState = (String) session.getAttribute("spotify_oauth_state");

        if (expectedState ==null || !expectedState.equals(state)){
            session.removeAttribute("spotify_oauth_state");
            response.sendRedirect(
                frontendUrl + "/?spotify=error&reason=invalid_state"
            );
            return;
        }

        session.removeAttribute("spotify_oauth_state");

        if(error !=null){
            response.sendRedirect(
                frontendUrl + "/?spotify=error&reason=denied"
            );
            return ;
        }

        if (code == null || code.isBlank()){
            response.sendRedirect(
                frontendUrl + "/?spotify=error&reason=missing_code"
            );
            return;
        }
        try{
            SpotifyTokenResponse token = authService.exchangeCodeForToken(code);

            session.setAttribute("spotify_access_token", token.getAccessToken());

            session.setAttribute("spotify_refresh_token", token.getRefreshToken());

            System.out.println("Token stored: " +(session.getAttribute("spotify_access_token") != null));
    

            response.sendRedirect(frontendUrl + "/?spotify=connected");
        }catch(Exception ex){
            response.sendRedirect(
                frontendUrl + "/?spotify=error&reason=token_exchange"
            );
        }
    }

}
