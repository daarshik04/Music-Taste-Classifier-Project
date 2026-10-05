package org.example.spotify;

import java.util.List;

public class SpotifyTracksResponse {

    private int total;
    private int limit;
    private int offset;
    private String next;
    private List<SpotifyTrackItem> items;

    public int getTotal(){
        return total;
    }

    public int getLimit(){
        return limit;
    }

    public int getOffset(){
        return offset;
    }

    public String getNext(){
        return next;
    }

    public List<SpotifyTrackItem> getItems(){
        return items;
    }

}