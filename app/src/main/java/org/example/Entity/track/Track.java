package org.example.Entity.track;

import java.util.HashSet;
import java.util.Set;

import org.example.Entity.album.Album;
import org.example.Entity.artist.Artist;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity 
@Table(name="tracks")
public class Track {

    @Id 
    private String spotifyId;

    @Column 
    private String name;

    private String uri;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name= "album_id")
    private Album album;

    @ManyToMany 
    @JoinTable(
        name = "track_artists",
        joinColumns = @JoinColumn(name = "track_id"),
        inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    private Set<Artist> artists = new HashSet<>();

    protected Track(){}

    public Track(String spotifyId, String name, String uri){
        this.spotifyId = spotifyId;
        this.name = name;
        this.uri = uri;
    }

    public void setAlbum(Album album){
        this.album = album;
    }

    public void setArtists(Set<Artist> artists){
        this.artists = artists;
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

    public Album getAlbum(){
        return album;
    }

    public Set<Artist> getArtist(){
        return artists;
    }

    

    
}
