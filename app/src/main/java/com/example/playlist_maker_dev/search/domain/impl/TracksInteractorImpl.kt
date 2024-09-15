package com.example.playlist_maker_dev.search.domain.impl

import com.example.playlist_maker_dev.search.domain.api.TracksInteractor
import com.example.playlist_maker_dev.search.domain.models.Track
import com.example.playlist_maker_dev.search.domain.repository.TracksRepository
import com.example.playlist_maker_dev.util.Resource
import com.example.playlist_maker_dev.util.ResourceData
import com.example.playlist_maker_dev.util.SearchConsumer
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    /*override fun searchTracks(term: String, consumer: SearchConsumer<List<Track>?>) {
        executor.execute {
            when(val resource = repository.searchTracks(term)) {
                is Resource.Success -> { consumer.consume(ResourceData.Data(resource.data)) }
                is Resource.Error -> { consumer.consume() }
            }
        }
    }*/

    override fun searchTracks(term: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            when(val resource = repository.searchTracks(term)) {
                is Resource.Success -> { consumer.consume(Resource.Success(resource.data), null) }
                is Resource.Error -> { consumer.consume(Resource.Error(resource.message.toString()), resource.message) }
            }
        }
    }


    /*override fun loadTrackData(trackId: String, consumer: Consumer<Track>) {
        executor.execute {
            val track = trackId?.let { trackDAO.getTrackById(trackId) }
        }
    }*/
}