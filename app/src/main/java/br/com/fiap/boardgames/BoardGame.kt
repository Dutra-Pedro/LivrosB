package br.com.fiap.boardgames

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "boardgames")
data class BoardGame(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val Titulo: String,
    val UrlImagem: String? = null,
    val Autor: String
)
