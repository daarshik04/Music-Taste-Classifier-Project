package org.example.Entity.track;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table(name="tracks")
public class Track {

    @Id 
    private String spotifyId;

    @Column 
    private String name;

    private String uri;

    protected Track(){}

    public Track(String spotifyId, String name, String uri){
        this.spotifyId = spotifyId;
        this.name = name;
        this.uri = uri;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    
}
