package com.example.myapplication.view.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.controller.LoginActivityViewModel
import com.example.myapplication.controller.LoginActivityViewModelFactory
import com.example.myapplication.makeToast
import com.example.myapplication.model.database.LocadoraDatabase
import com.example.myapplication.view.home.HomeActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var editTextDocument: AppCompatEditText
    private lateinit var editTextPassword: AppCompatEditText

    private lateinit var btnLogin: AppCompatButton
    private lateinit var btnRegister: AppCompatButton

    private lateinit var viewModel: LoginActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        editTextDocument = findViewById(R.id.document_label)
        editTextPassword = findViewById(R.id.password_label)

        btnLogin = findViewById(R.id.btn_login)
        btnRegister = findViewById(R.id.btn_register)

        viewModel = ViewModelProvider(
            this,
            LoginActivityViewModelFactory(LocadoraDatabase.instance(applicationContext))
        )[LoginActivityViewModel::class.java]

        setupListener()
        setupObservers()
    }

    private fun setupListener() {
        btnLogin.setOnClickListener {
            viewModel.login(editTextDocument.text.toString(), editTextPassword.text.toString())
        }
        btnRegister.setOnClickListener {
            Intent(this, RegisterActivity::class.java).let {
                startActivity(it)
            }
        }
    }

    private fun setupObservers() {
        viewModel.cliente.observe(this) {
            Intent(this, HomeActivity::class.java).let {
                startActivity(it)
                finish()
            }
        }

        viewModel.error.observe(this) {
            makeToast(it)
        }
    }

}