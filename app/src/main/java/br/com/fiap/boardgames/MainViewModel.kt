package br.com.fiap.boardgames

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel(private val repository: BoardGameRepository) : ViewModel() {
    val allBoardGames: LiveData<List<BoardGame>> = repository.allBoardGames

    fun insert(game: BoardGame) {
        viewModelScope.launch {
            repository.insert(game)
        }
    }

    fun update(game: BoardGame) {
        viewModelScope.launch {
            repository.update(game)
        }
    }

    fun delete(game: BoardGame) {
        viewModelScope.launch {
            repository.delete(game)
        }
    }
}

