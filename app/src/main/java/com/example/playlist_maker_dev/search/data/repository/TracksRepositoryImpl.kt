package com.example.playlist_maker_dev.search.data.repository

import com.example.playlist_maker_dev.search.data.dto.TracksSearchRequest
import com.example.playlist_maker_dev.search.data.dto.TracksSearchResponse
import com.example.playlist_maker_dev.search.data.network.NetworkClient
import com.example.playlist_maker_dev.search.domain.models.Track
import com.example.playlist_maker_dev.search.domain.repository.TracksRepository
import com.example.playlist_maker_dev.util.Resource
import java.text.SimpleDateFormat
import java.util.Locale

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTracks(term: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(term))

        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }

            in 200..299 -> {
                Resource.Success((response as TracksSearchResponse).results.map { trackDto ->
                    Track(
                        trackId = trackDto.trackId,
                        trackName = trackDto.trackName,
                        artistName = trackDto.artistName,
                        trackTimeMillis = SimpleDateFormat("mm:ss", Locale.getDefault()).format(
                            trackDto.trackTimeMillis.toLong()
                        ),
                        artworkUrl100 = trackDto.artworkUrl100,
                        collectionName = trackDto.collectionName,
                        releaseDate = trackDto.releaseDate,
                        primaryGenreName = trackDto.primaryGenreName,
                        country = trackDto.country,
                        previewUrl = trackDto.previewUrl
                    )
                })
            }

            else -> {
                Resource.Error("Ошибка сервера")
            }
        }

    }
}