package dev.forcetower.playtime.view.listing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.forcetower.playtime.databinding.FragmentTitleListBinding
import dev.forcetower.toolkit.components.BaseFragment
import dev.forcetower.toolkit.lifecycle.EventObserver
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieListFragment : BaseFragment() {
    private lateinit var binding: FragmentTitleListBinding
    private lateinit var adapter: MovieAdapter
    private val viewModel: ListingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = FragmentTitleListBinding.inflate(inflater, container, false).also {
            binding = it
        }.root

        adapter = MovieAdapter(viewModel)

        binding.recyclerMovies.apply {
            adapter = this@MovieListFragment.adapter
            itemAnimator?.apply {
                changeDuration = 0L
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val type = requireArguments().getInt(LIST_TYPE)

        lifecycleScope.launch {
            viewModel.getListOfType(type).collectLatest {
                adapter.submitData(it)
            }
        }
        viewModel.movieClick.observe(viewLifecycleOwner, EventObserver {
            val directions = ListingFragmentDirections.actionListingToMovieDetails(it.id, null)
            findNavController().navigate(directions)
        })
    }

    companion object {
        private const val LIST_TYPE = "list_type"

        fun ofOption(value: Int): MovieListFragment {
            return MovieListFragment().apply {
                arguments = bundleOf(LIST_TYPE to value)
            }
        }
    }
}