package dev.forcetower.playtime.view.featured

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.forcetower.playtime.core.model.ui.MovieFeatured
import dev.forcetower.playtime.databinding.FragmentMoviesFeaturedBinding
import dev.forcetower.toolkit.components.BaseFragment

class MoviesFeaturedFragment : BaseFragment() {
    private lateinit var binding: FragmentMoviesFeaturedBinding
    private lateinit var adapter: FeaturedAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = FeaturedAdapter()
        return FragmentMoviesFeaturedBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerMovies.adapter = adapter


//        adapter.submitList(listOf(
//            MovieFeatured(1, "Spider Man: Homecoming", "2017/04/20", "https://image.tmdb.org/t/p/original/tPpYGm2mVecue7Bk3gNVoSPA5qn.jpg"),
//            MovieFeatured(2, "Spider Man: Homecoming", "2017/04/20", "https://image.tmdb.org/t/p/original/tPpYGm2mVecue7Bk3gNVoSPA5qn.jpg"),
//            MovieFeatured(3, "Spider Man: Homecoming", "2017/04/20", "https://image.tmdb.org/t/p/original/tPpYGm2mVecue7Bk3gNVoSPA5qn.jpg"),
//            MovieFeatured(4, "Spider Man: Homecoming", "2017/04/20", "https://image.tmdb.org/t/p/original/tPpYGm2mVecue7Bk3gNVoSPA5qn.jpg"),
//            MovieFeatured(5, "Spider Man: Homecoming", "2017/04/20", "https://image.tmdb.org/t/p/original/tPpYGm2mVecue7Bk3gNVoSPA5qn.jpg"),
//            MovieFeatured(6, "Spider Man: Homecoming", "2017/04/20", "https://image.tmdb.org/t/p/original/tPpYGm2mVecue7Bk3gNVoSPA5qn.jpg"),
//            MovieFeatured(7, "Spider Man: Homecoming", "2017/04/20", "https://image.tmdb.org/t/p/original/tPpYGm2mVecue7Bk3gNVoSPA5qn.jpg"),
//        ))
    }
}