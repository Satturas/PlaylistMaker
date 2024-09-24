package com.example.playlist_maker_dev.main.domain

class MainScreenInteractorImpl(private val repository: MainScreenRepository) :
    MainScreenInteractor {

    override fun onSearchClick() = repository.onSearchClick()

    override fun onMediaClick() = repository.onMediaClick()

    override fun onSettingsClick() = repository.onSettingsClick()
}