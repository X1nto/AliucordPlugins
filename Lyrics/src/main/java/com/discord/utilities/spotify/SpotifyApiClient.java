package com.discord.utilities.spotify;

import com.discord.models.domain.spotify.ModelSpotifyTrack;

import rx.Observable;

public class SpotifyApiClient {

    public final Observable<ModelSpotifyTrack> getSpotifyTrack() {
        return new Observable<>();
    }

}
