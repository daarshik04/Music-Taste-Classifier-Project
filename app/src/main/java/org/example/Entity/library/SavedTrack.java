package org.example.Entity.library;

import java.time.Instant;

import org.example.Entity.track.Track;
import org.example.Entity.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity 
@Table(
    name = "user_saved_tracks",
    uniqueConstraints = @UniqueConstraint (
        name = "uk_user_saved_track",
        columnNames = {"user_id", "track_id"}
    )
)
public class SavedTrack {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name ="user_id", nullable=false )
    private User user;

    @ManyToOne(fetch =FetchType.LAZY, optional =false)
    @JoinColumn(name ="track_id", nullable =false)
    private Track track;

    @Column(name= "added_at")
    private Instant addedAt;

    protected SavedTrack(){}

    public SavedTrack(User user, Track track, Instant addedAt){
        this.user = user;
        this.track = track;
        this.addedAt = addedAt;
    }

    public Long getId(){
        return id;
    }
    
    public User getUser(){
        return user;
    }

    public Track getTrack(){
        return track;
    }

    public Instant getAddedAt(){
        return addedAt;
    }

    public void setAddedAt(Instant addedAt) {
        this.addedAt = addedAt;
    }
    
}
