package com.example.playlist_maker_dev.main.domain

class MainScreenInterectorImpl(private val repository: MainScreenRepository) : MainScreenInterector {

    override fun onSearchClick() = repository.onSearchClick()

    override fun onMediaClick() = repository.onMediaClick()

    override fun onSettingsClick() = repository.onSettingsClick()
}