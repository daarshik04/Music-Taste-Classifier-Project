package org.example.Entity.library;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SavedTrackRepository extends JpaRepository<SavedTrack, Long> {
    
    Optional<SavedTrack> findByUser_IdAndTrack_SpotifyId(
        Long userId,
        String spotifyTrackId
    );
}
