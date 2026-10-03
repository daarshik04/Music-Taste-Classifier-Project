package org.example.Controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestParam;


@RestController 
public class HealthController {

    @GetMapping("/api/health")
    public String health() {
        return "{\"status\" : \"UP\"}";
    }
    
    
}
