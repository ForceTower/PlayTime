package dev.forcetower.playtime.view.details

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.forcetower.playtime.core.model.storage.Movie
import dev.forcetower.playtime.core.source.repository.MovieRepository

class DetailsViewModel @ViewModelInject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    fun movie(id: Int): LiveData<Movie> {
        return repository.movie(id)
    }
}