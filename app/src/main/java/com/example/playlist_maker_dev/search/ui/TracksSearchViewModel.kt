package com.example.playlist_maker_dev.search.ui

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlist_maker_dev.R
import com.example.playlist_maker_dev.creator.Creator
import com.example.playlist_maker_dev.domain.api.TracksInteractor
import com.example.playlist_maker_dev.domain.models.Track
import com.example.playlist_maker_dev.search.data.TracksState

class TracksSearchViewModel(application: Application) : AndroidViewModel(application) {


    private val tracksInteractor = Creator.provideTracksInteractor(getApplication())
    private val handler = Handler(Looper.getMainLooper())

    private val stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData

    private var latestSearchText: String? = null

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { searchRequest(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    private fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(TracksState.Loading)

            tracksInteractor.searchTracks(newSearchText, object : TracksInteractor.TracksConsumer {
                 override fun consume(foundMovies: List<Track>?) {
                    val tracks = mutableListOf<Track>()
                    if (foundMovies != null) {
                        tracks.addAll(foundMovies)
                    }

                    when {
                        errorMessage != null -> {
                            renderState(
                                TracksState.Error(getApplication<Application>().getString(R.string.something_went_wrong),
                                )
                            )
                            //showToast.postValue(errorMessage)
                        }

                        tracks.isEmpty() -> {
                            renderState(
                                TracksState.Empty(getApplication<Application>().getString(R.string.nothing_found),
                                )
                            )
                        }

                        else -> {
                            renderState(
                                TracksState.Content(
                                    movies = tracks,
                                )
                            )
                        }
                    }

                }
            })
        }
    }

    private fun renderState(state: TracksState) {
        stateLiveData.postValue(state)
    }


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                TracksSearchViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }
}

/*companion object {
        private const val SEARCH_USER_INPUT = "search_user_input"
        private val SEARCH_DEF: CharSequence = ""
        const val AUDIO_PLAYER = "track_for_player"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                TracksSearchViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }
}*/