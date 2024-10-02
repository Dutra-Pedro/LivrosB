package br.com.fiap.boardgames

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.OnFocusChangeListener
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.fiap.boardgames.databinding.ActivityMainBinding
import br.com.fiap.boardgames.databinding.DialogEditBoardGameBinding
import com.bumptech.glide.Glide


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as BoardGameApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpLogo()
        setUpListeners()
        setUpRecyclerView()
    }

    private fun setUpLogo() {
        Glide
            .with(this)
            .load("https://www.designi.com.br/images/preview/12476685.jpg")
            .into(binding.imageLogo)
    }


    private fun setUpListeners() {
        binding.buttonAddLivro.setOnClickListener {
            val livroTitulo = binding.editTextLivroTitulo.text.toString()
            val livroAutor = binding.editTextLivroAutor.text.toString()
            val livroImageURL = binding.editTextLivroImageUrl.text.toString()
            if (livroTitulo.isNotBlank() && livroAutor.isNotBlank()) {
                mainViewModel.insert(
                    BoardGame(
                        Titulo = livroTitulo,
                        Autor = livroAutor,
                        UrlImagem = livroImageURL
                    )
                )
                binding.editTextLivroTitulo.text.clear()
                binding.editTextLivroAutor.text.clear()
                binding.editTextLivroImageUrl.text.clear()
                binding.editTextLivroTitulo.requestFocus()
            }
        }
    }


    private fun setUpRecyclerView() {
        val adapter = MainListAdapter(
            onEditClick = { game ->
                showEditDialog(game)
            },
            onDeleteClick = { game -> mainViewModel.delete(game) }
        )
        binding.recyclerViewLivros.adapter = adapter
        //binding.recyclerViewGames.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewLivros.layoutManager = GridLayoutManager(this, 2)

        // Adicionar Divider
        val dividerItemDecoration = DividerItemDecoration(
            binding.recyclerViewLivros.context,
            (binding.recyclerViewLivros.layoutManager as LinearLayoutManager).orientation
        )
        binding.recyclerViewLivros.addItemDecoration(dividerItemDecoration)

        mainViewModel.allBoardGames.observe(this) { games ->
            games?.let { adapter.setBoardGames(it) }
        }
    }

    private fun showEditDialog(boardGame: BoardGame) {
        val dialogBinding = DialogEditBoardGameBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(this)
        builder.setView(dialogBinding.root)

        // Preenche os campos de texto com os dados do jogo atual
        dialogBinding.editTextLivroTitulo.setText(boardGame.Titulo)
        dialogBinding.editTextLivroAutor.setText(boardGame.Autor)
        dialogBinding.editTextLivroImageUrl.setText(boardGame.UrlImagem)

        /*dialogBinding.editTextGameImageUrl.onFocusChangeListener =
            OnFocusChangeListener { view, hasFocus ->
                if (!hasFocus) {
                    Glide.with(view)
                        .load(dialogBinding.editTextGameImageUrl.text.toString())
                        .into(dialogBinding.ivGame)
                }
            }*/

        dialogBinding.editTextLivroImageUrl.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                Glide.with(dialogBinding.root.context)
                    .load(dialogBinding.editTextLivroImageUrl.text.toString())
                    .into(dialogBinding.ivGame)
            }
        })

        builder.setTitle("Editar Livro")
        builder.setPositiveButton("Salvar") { _, _ ->
            val updatedGame = boardGame.copy(
                Titulo = dialogBinding.editTextLivroTitulo.text.toString(),
                Autor = dialogBinding.editTextLivroAutor.text.toString(),
                UrlImagem = dialogBinding.editTextLivroImageUrl.text.toString()
            )
            mainViewModel.update(updatedGame)
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }

}