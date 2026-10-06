package org.example.Entity.album;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table(name ="album")
public class Album {

    @Id 
    private String spotifyId;

    @Column 
    private String name;

    protected Album(){}

    public Album(String spotifyId, String name){
        this.spotifyId = spotifyId;
        this.name = name;
    }

    public String getName(){
        return name;
    }
    public String getSpotifyId(){
        return spotifyId;
    }
    
}
