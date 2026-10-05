package org.example.spotify;

public class SpotifyTrackItem {

    private String added_at;
    private Track track;

    public String getAdded_at(){
        return added_at;
    }

    public Track getTrack(){
        return track;
    }

    public static class Track{

        private String id;
        private String name;
        private String uri;
        private Album album;
        private Artist[] artists;

        public String getId(){
            return id;
        }
        public String getName(){
            return name;
        }
        public String getUri(){
            return uri;
        }
        public Album getAlbum(){
            return album;
        }
        public Artist[] getArtists(){
            return artists;
        }
    }

    public static class Album {
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    public static class Artist {
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }


    
}
