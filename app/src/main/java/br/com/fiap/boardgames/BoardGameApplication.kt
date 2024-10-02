package br.com.fiap.boardgames

import android.app.Application

class BoardGameApplication : Application() {
    val database by lazy { BoardGameDatabase.getDatabase(this) }
    val repository by lazy { BoardGameRepository(database.boardGameDao()) }
}
