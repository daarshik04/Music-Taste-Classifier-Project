package org.example.spotify;

import org.example.Entity.album.AlbumRepository;
import org.example.Entity.artist.ArtistRepository;
import org.example.Entity.library.SavedTrack;
import org.example.Entity.library.SavedTrackRepository;
import org.example.Entity.track.Track;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import org.example.Entity.album.Album;
import org.example.Entity.artist.Artist;
import org.example.Entity.track.TrackRepository;
import org.example.Entity.user.User;
import org.example.Entity.user.UserRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service 
public class SpotifyImportService {

    private final SpotifyClient spotifyClient;
    private final TrackRepository trackRepository;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final SavedTrackRepository savedTrackRepository;
    private final UserRepository userRepository;
    

    public SpotifyImportService(
        SpotifyClient spotifyClient,
        TrackRepository trackRepository,
        ArtistRepository artistRepository,
        AlbumRepository albumRepository,
        SavedTrackRepository savedTrackRepository,
        UserRepository userRepository
    ){
        this.spotifyClient = spotifyClient;
        this.trackRepository = trackRepository;
        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
        this.savedTrackRepository = savedTrackRepository;
        this.userRepository = userRepository;
    }

    @Transactional 
    public int importAllTracks(String accessToken){

        User user = userRepository.findFirstByOrderByIdAsc().orElseGet(()->
                              userRepository.save(new User("Spotify Library Owner"))
                            );

        int offset = 0;
        int limit = 50;
        int processed = 0;

        while(true){
            SpotifyTracksResponse response = spotifyClient.getSavedTracks(
                accessToken, offset, limit
            );

            if (response == null || response.getItems() ==null || response.getItems().isEmpty()){
                break;
            }

            for(SpotifyTrackItem item : response.getItems()){

                SpotifyTrackItem.Track source =item.getTrack();

                if(source == null || source.getId() == null){
                    continue;
                }

                Album album =null;

                if(source.getAlbum() != null && source.getAlbum().getId() != null){

                    var sourceAlbum = source.getAlbum();

                    album = albumRepository.save(
                        new Album(
                            sourceAlbum.getId(),
                            sourceAlbum.getName()
                        )
                    );
                }

                Set<Artist> artists = new HashSet<>();

                if(source.getArtists() != null){
                    for(var sourceArtist : source.getArtists()){
                        if(sourceArtist.getId() ==null){
                            continue;
                        }

                        artists.add(artistRepository.save(
                            new Artist(
                                sourceArtist.getId(),
                                sourceArtist.getName()
                            )
                        ));
                    }
                }

                Track track = new Track(
                    source.getId(),
                    source.getName(),
                    source.getUri()
                );

                track.setAlbum(album);
                track.setArtists(artists);

                Track savedTrackEntity = trackRepository.save(track);

                Instant addedAt =item.getAdded_at() == null
                        ? null
                        : Instant.parse(item.getAdded_at());

                SavedTrack savedTrack = savedTrackRepository.findByUser_IdAndTrack_SpotifyId(
                    user.getId(),
                    savedTrackEntity.getSpotifyId()
                )        .orElseGet(()-> new SavedTrack(user, savedTrackEntity, addedAt));

                savedTrack.setAddedAt(addedAt);
                savedTrackRepository.save(savedTrack);

                processed++;
            }
            
            int pageSize = response.getItems().size();
            offset += pageSize;

            if(offset >= response.getTotal() || pageSize < limit){
                break;
            }
        }

        return processed;
    }    
    
}
