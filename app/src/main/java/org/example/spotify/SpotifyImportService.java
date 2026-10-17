package org.example.spotify;

import org.example.Entity.album.AlbumRepository;
import org.example.Entity.artist.ArtistRepository;
import org.example.Entity.track.Track;
import org.example.Entity.album.Album;
import org.example.Entity.artist.Artist;
import org.example.Entity.track.TrackRepository;
import org.springframework.stereotype.Service;

@Service 
public class SpotifyImportService {

    private final SpotifyClient spotifyClient;
    private final TrackRepository trackRepository;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;

    public SpotifyImportService(
        SpotifyClient spotifyClient,
        TrackRepository trackRepository,
        ArtistRepository artistRepository,
        AlbumRepository albumRepository
    ){
        this.spotifyClient = spotifyClient;
        this.trackRepository = trackRepository;
        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
    }

    public int importFirstPage(String accessToken){
        SpotifyTracksResponse response = spotifyClient.getSavedTracks(
            accessToken,
             0, 
             50);

        for (SpotifyTrackItem item: response.getItems()){

            SpotifyTrackItem.Track spotifyTrack = item.getTrack();

            if(spotifyTrack == null){
                continue;
            }

            Track track = new Track(
                spotifyTrack.getId(),
                spotifyTrack.getName(),
                spotifyTrack.getUri()
            );

            trackRepository.save(track);

            if(spotifyTrack.getAlbum() != null){

                SpotifyTrackItem.Album album = spotifyTrack.getAlbum();

                albumRepository.save(
                    new Album(
                        album.getId(),
                        album.getName()
                    )
                );
            }

            if(spotifyTrack.getArtists() != null){

                for(SpotifyTrackItem.Artist artist : spotifyTrack.getArtists()){

                    artistRepository.save(
                        new Artist(
                            artist.getId(),
                            artist.getName()
                        )
                    );
                }
            }
        }    
        
        return response.getItems().size();
    }
    
    
}
