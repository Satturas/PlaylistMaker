package com.example.playlist_maker_dev.domain.impl

import com.example.playlist_maker_dev.data.network.ITunesApiService
import com.example.playlist_maker_dev.domain.api.TracksInteractor
import com.example.playlist_maker_dev.domain.dao.TrackDAO
import com.example.playlist_maker_dev.domain.models.Track
import com.example.playlist_maker_dev.domain.repository.TracksRepository
import com.example.playlist_maker_dev.player.domain.consumer.Consumer
import com.example.playlist_maker_dev.player.domain.consumer.ConsumerData
import com.example.playlist_maker_dev.util.Resource
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository, private val trackDAO: TrackDAO) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(term: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            when(val resource = repository.searchTracks(term)) {
                is Resource.Success -> { consumer.consume(resource.data, null) }
                is Resource.Error -> { consumer.consume(null, resource.message) }
            }
        }
    }


    override fun loadTrackData(trackId: String, consumer: Consumer<Track>) {
        executor.execute {
            val track = trackId?.let { trackDAO.getTrackById(trackId) }
        }
    }
}