package com.example.playlist_maker_dev.media.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlist_maker_dev.R
import com.example.playlist_maker_dev.databinding.ActivityMediaBinding
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaBinding

    private lateinit var tabMediator: TabLayoutMediator

    private val viewModel by viewModel<MediaViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setOnClickListener { finish() }

        binding.viewPager.adapter = MediaViewPagerAdapter(
            fragmentManager = supportFragmentManager,
            lifecycle = lifecycle,
        )

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}