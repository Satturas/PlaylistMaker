package com.example.playlist_maker_dev.data

import com.example.playlist_maker_dev.data.dto.TracksSearchRequest
import com.example.playlist_maker_dev.data.dto.TracksSearchResponse
import com.example.playlist_maker_dev.domain.api.TracksRepository
import com.example.playlist_maker_dev.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(term: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(term))
        if (response.resultCode == 200) {
            return (response as TracksSearchResponse).results.map {
                    trackDto ->
                Track(
                    trackId = trackDto.trackId,
                    trackName = trackDto.trackName,
                    artistName = trackDto.artistName,
                    trackTimeMillis = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackDto.trackTimeMillis.toLong()),
                    artworkUrl100 = trackDto.artworkUrl100,
                    collectionName = trackDto.collectionName,
                    releaseDate = trackDto.releaseDate,
                    primaryGenreName = trackDto.primaryGenreName,
                    country = trackDto.country,
                    previewUrl = trackDto.previewUrl
                ) }
        } else {
            return emptyList()
        }
    }
}