package org.example.Entity.artist;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table(name = "artist")
public class Artist {
    
    @Id 
    private String spotifyId;

    @Column(nullable =false)
    private String name;

    protected Artist(){}

    public Artist(String spotifyId, String name){
        this.spotifyId = spotifyId;
        this.name = name;
    }

    public String getSpotifyId(){
        return spotifyId;
    }
    public String getName(){
        return name;
    }

    
}
