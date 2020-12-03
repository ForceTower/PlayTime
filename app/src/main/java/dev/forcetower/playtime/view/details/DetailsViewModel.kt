package dev.forcetower.playtime.view.details

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dev.forcetower.playtime.core.model.storage.Release
import dev.forcetower.playtime.core.model.ui.MovieWithRelations
import dev.forcetower.playtime.core.source.repository.MovieRepository

class DetailsViewModel @ViewModelInject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    fun movie(id: Int): LiveData<MovieWithRelations> {
        return repository.movie(id)
    }

    fun releaseDate(id: Int): LiveData<Release?> {
        return repository.releaseDate(id).asLiveData()
    }
}