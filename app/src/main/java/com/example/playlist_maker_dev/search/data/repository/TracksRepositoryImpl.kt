package com.example.playlist_maker_dev.search.data.repository

import com.example.playlist_maker_dev.search.data.dto.TracksSearchRequest
import com.example.playlist_maker_dev.search.data.dto.TracksSearchResponse
import com.example.playlist_maker_dev.search.data.network.NetworkClient
import com.example.playlist_maker_dev.search.domain.models.Track
import com.example.playlist_maker_dev.search.domain.repository.TracksRepository
import com.example.playlist_maker_dev.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Locale

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {

    override fun searchTracks(term: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(term))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }

            in 200..299 -> {
                with(response as TracksSearchResponse) {
                    val data = results.map { trackDto ->
                        Track(
                            trackId = trackDto.trackId,
                            trackName = trackDto.trackName,
                            artistName = trackDto.artistName,
                            trackTimeMillis = SimpleDateFormat("mm:ss", Locale.getDefault()).format(
                                trackDto.trackTimeMillis
                            ),
                            artworkUrl100 = trackDto.artworkUrl100,
                            collectionName = trackDto.collectionName,
                            releaseDate = trackDto.releaseDate,
                            primaryGenreName = trackDto.primaryGenreName,
                            country = trackDto.country,
                            previewUrl = trackDto.previewUrl
                        )
                    }
                    emit(Resource.Success(data))
                }
            }

            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }
}