package com.example.playlist_maker_dev.search.domain.impl

import com.example.playlist_maker_dev.search.domain.api.TracksInteractor
import com.example.playlist_maker_dev.search.domain.repository.TracksRepository
import com.example.playlist_maker_dev.util.Resource
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(term: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            when (val resource = repository.searchTracks(term)) {
                is Resource.Success -> {
                    consumer.consume(Resource.Success(resource.data), null)
                }

                is Resource.Error -> {
                    consumer.consume(Resource.Error(resource.message.toString()), resource.message)
                }
            }
        }
    }
}