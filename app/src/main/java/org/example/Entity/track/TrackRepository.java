package org.example.Entity.track;

import javax.sound.midi.Track;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<Track, String>{
    
}
