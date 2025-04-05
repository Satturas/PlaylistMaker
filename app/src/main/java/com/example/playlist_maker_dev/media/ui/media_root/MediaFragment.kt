package com.example.playlist_maker_dev.media.ui.media_root

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment

class MediaFragment : Fragment() {

    /*private var _binding: FragmentMediaBinding? = null
    private val binding get() = _binding!!

    private lateinit var tabMediator: TabLayoutMediator*/

    //private val viewModel by viewModel<MediaViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //_binding = FragmentMediaBinding.inflate(inflater, container, false)
        // return binding.root
        return ComposeView(requireContext()).apply {
            setContent {
                MediaScreen()
            }
        }
    }

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = MediaViewPagerAdapter(
            fragmentManager = childFragmentManager,
            lifecycle = lifecycle,
        )

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        tabMediator.detach()
    }*/
}