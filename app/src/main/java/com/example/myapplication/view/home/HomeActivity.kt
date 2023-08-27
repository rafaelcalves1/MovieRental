package com.example.myapplication.view.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.controller.HomeActivityViewModel
import com.example.myapplication.controller.MainActivityViewModelFactory
import com.example.myapplication.makeToast
import com.example.myapplication.model.database.ClientSession
import com.example.myapplication.model.database.LocadoraDatabase
import com.example.myapplication.model.database.entity.ClientEntity
import com.example.myapplication.model.database.entity.MovieEntity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeActivity : AppCompatActivity() {

    private lateinit var viewModel: HomeActivityViewModel
    private val adapter: MainAdapter = MainAdapter()

    private lateinit var recyclerView: RecyclerView
    private lateinit var flotActBtn: FloatingActionButton

    private lateinit var movieNameEditText: AppCompatEditText
    private lateinit var movieYearEditText: AppCompatEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(
            this,
            MainActivityViewModelFactory(LocadoraDatabase.instance(applicationContext))
        )[HomeActivityViewModel::class.java]

        recyclerView = findViewById(R.id.recyclerView)
        flotActBtn = findViewById(R.id.btn_add_filme)

        recyclerView.apply {
            adapter = this@HomeActivity.adapter
            layoutManager =
                GridLayoutManager(this@HomeActivity, 2, GridLayoutManager.VERTICAL, false)
        }

        viewModel.fetchMovies()

        setupObservers()
        setupListener()
    }

    private fun setupObservers() {
        viewModel.error.observe(this) {
            makeToast(it)
        }

        viewModel.moviesList.observe(this) {
            adapter.submitList(it)
        }

        viewModel.rented.observe(this) {
            makeToast(it)
        }
    }

    private fun setupListener() {
        adapter.setListener {
            ClientSession.client?.let { cliente ->
                if (cliente.isAdmin) {
                    showDialogRemoveFilme(it)
                    return@setListener
                }
                showDialogAlugaFilme(it, cliente)
            }
        }

        adapter.setLongListener {
            ClientSession.client?.let { cliente ->
                if (cliente.isAdmin) return@setLongListener
                if (it.isRented.not()) return@setLongListener
                showDialogDevolverFilme(it, cliente)
            }
        }

        flotActBtn.setOnClickListener {
            if(ClientSession.client?.isAdmin?.not() == true ){
                makeToast("Apenas um usuário Admin consegue adicionar novos filmes!")
                return@setOnClickListener
            }
            showDialogAddFilme()
        }
    }

    private fun showDialogAddFilme() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.layout_dialog_add_filme, null)

        movieNameEditText = dialogView.findViewById(R.id.editTextMovieName)
        movieYearEditText = dialogView.findViewById(R.id.editTextYearMovie)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Confirmar") { _, _ ->
                val nomeFilme = movieNameEditText.text.toString()
                val anoFilme = movieYearEditText.text.toString()

                viewModel.addMovie(nomeFilme, anoFilme)
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }

    private fun showDialogRemoveFilme(movieEntity: MovieEntity) {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Deseja deletar o filme?")
            .setPositiveButton("Sim") { _, _ ->
                viewModel.deleteMovie(movieEntity.id)
            }
            .setNegativeButton("Não", null)
            .create()

        dialog.show()
    }

    private fun showDialogAlugaFilme(movieEntity: MovieEntity, clientEntity: ClientEntity) {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Deseja alugar o ${movieEntity.name} ?")
            .setPositiveButton("Sim") { _, _ ->
                viewModel.rentMovie(movieEntity, clientEntity)
            }
            .setNegativeButton("Não", null)
            .create()

        dialog.show()
    }

    private fun showDialogDevolverFilme(movieEntity: MovieEntity, clientEntity: ClientEntity) {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Deseja devolver o ${movieEntity.name} ?")
            .setPositiveButton("Sim") { _, _ ->
                viewModel.returnRentedMovie(movieEntity, clientEntity)
            }
            .setNegativeButton("Não", null)
            .create()

        dialog.show()
    }
}