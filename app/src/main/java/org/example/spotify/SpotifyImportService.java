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

    public int importAllTracks(String accessToken){

        int offset = 0;
        int limit = 50;
        int fetched = 0;

        while(true){
            SpotifyTracksResponse response = spotifyClient.getSavedTracks(
                accessToken, offset, limit
            );

            if (response == null || response.getItems() ==null || response.getItems().isEmpty()){
                break;
            }

            for(SpotifyTrackItem item : response.getItems()){

                SpotifyTrackItem.Track spotifyTrack =item.getTrack();

                if(spotifyTrack == null || spotifyTrack.getId() == null){
                    continue;
                }

                trackRepository.save(
                    new Track(
                        spotifyTrack.getId(),
                        spotifyTrack.getName(),
                        spotifyTrack.getUri()
                    )
                );

                if(spotifyTrack.getAlbum() != null && spotifyTrack.getAlbum().getId() != null){

                    var album = spotifyTrack.getAlbum();

                    albumRepository.save(
                        new Album(album.getId(), album.getName())
                    );
                }

                if(spotifyTrack.getArtists() != null){
                    for(var artist: spotifyTrack.getArtists()){
                        if(artist.getId() != null){
                            artistRepository.save(
                                new Artist(artist.getId(), artist.getName())
                            );
                        }
                    }
                }
            }

            int pageSize = response.getItems().size();
            fetched += pageSize;
            offset += pageSize;

            if(offset >= response.getTotal() || pageSize < limit){
                break;
            }
        }

        return fetched;
    }    
    
}
